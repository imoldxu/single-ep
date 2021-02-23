package com.yyg.eprescription.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.OpenPrescriptionBo;
import com.yyg.eprescription.bo.PrescriptionQuery;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.Drug;
import com.yyg.eprescription.entity.Prescription;
import com.yyg.eprescription.entity.PrescriptionDrugs;
import com.yyg.eprescription.entity.PrescriptionNumber;
import com.yyg.eprescription.jxfy.entity.Patient;
import com.yyg.eprescription.mapper.PrescriptionDrugsMapper;
import com.yyg.eprescription.mapper.PrescriptionMapper;
import com.yyg.eprescription.mapper.PrescriptionNumberMapper;
import com.yyg.eprescription.vo.PrescriptionInitVo;
import com.yyg.eprescription.vo.PrescriptionInfo;

import tk.mybatis.mapper.entity.Example;

@Service
public class PrescriptionService {

	private static final Logger logger = LoggerFactory.getLogger(PrescriptionService.class);
	
	@Autowired
	PrescriptionMapper prescriptionMapper;
	@Autowired
	PrescriptionDrugsMapper pDrugMapper;
	@Autowired
	PrescriptionNumberMapper pNumberMapper;
	@Autowired
	PatientService patientService;
	@Autowired
	DrugService drugService;
	//@Autowired
	//HospitalPatientService hospitalPatientService;
	@Autowired
	OrderService orderService;
	@Autowired
	DoctorDrugService doctorDrugService;
	@Autowired
	DiagnosisMsgService diagnosisMsgService;
	@Autowired
	PayService payService;
	
	/**
	 * 初始化处方的基本信息
	 * @param cardNo
	 * @return
	 * @throws Exception
	 */
	public PrescriptionInitVo init(String cardNo) throws Exception {
		//PrescriptionInitVo initInfo = hospitalPatientService.getDiagnosisInfo(cardNo);
		Patient patient = patientService.getPatientByCardNo(cardNo);
		PrescriptionInitVo initInfo = new PrescriptionInitVo();
		initInfo.setCardNo(patient.getCardNo());
		if(patient.getPatientAge()==0) {
			Date birthday = patient.getPatientBirthday();
			String age = getAge(birthday);
			initInfo.setPatientage(age);
		}else {
			initInfo.setPatientage(patient.getPatientAge()+"岁");
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		initInfo.setPatientBirthday(format.format(patient.getPatientBirthday()));
		initInfo.setPatientId(patient.getPatientId());
		initInfo.setPatientname(patient.getPatientName());
		initInfo.setPatientsex(patient.getPatientSex());
		initInfo.setRegNo(patient.getRegNo());
		initInfo.setPrescriptionno(getSysNumber());
		return initInfo;
	}
	
	/**
	 * 根据出生日期计算年龄、不足1岁精确到几月或几天
	 * @param birthDayStr
	 * @return
	 * @throws ParseException
	 */
	private String getAge(Date birthDay) throws ParseException {
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//Date birthDay = format.parse(birthDayStr);
		
		Calendar now = Calendar.getInstance();
        long nowmillSeconds = now.getTimeInMillis();
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDay);
        long birmillSeconds = birth.getTimeInMillis();
        Calendar difference = Calendar.getInstance();
        long millis = nowmillSeconds - birmillSeconds;
        difference.setTimeInMillis(millis);
        int year = difference.get(Calendar.YEAR);
        int month = difference.get(Calendar.MONTH);
        int day = difference.get(Calendar.DAY_OF_MONTH);
        //int hour = difference.get(Calendar.HOUR_OF_DAY);
        if (year > 1970) {
            return year - 1970 + "岁";
        } else if (month > Calendar.JANUARY) {
            return month - Calendar.JANUARY + "个月";
        } else if (day > 1) {
            return day - 1 + "天";
        }else{
            return "1天";
        }
    }
	
	/**
	 * 开处方，开处方的同时，需要创建待支付的Order
	 * @param p
	 * @param drugs
	 * @throws Exception 
	 */
	@Transactional
	public void open(OpenPrescriptionBo openP) throws Exception{	
		Prescription p = new Prescription();
		Date now = new Date();
		p.setCreatedate(now);
		p.setCardNo(openP.getCardNo());
		p.setDepartment(openP.getDepartment());
		p.setDiagnosis(openP.getDiagnosis());
		p.setDoctorname(openP.getDoctorname());
		p.setPrescriptionno(openP.getPrescriptionno());
		p.setPatientage(openP.getPatientage());
		p.setPatientname(openP.getPatientname());
		p.setPatientphone(openP.getPatientphone());
		p.setPatientsex(openP.getPatientsex());
		p.setPatientBirthday(openP.getPatientBirthday());
		p.setRegNo(openP.getRegNo());
		p.setType(openP.getType());
	    
		//检查是否有相同编号的处方签	
		Example ex = new Example(Prescription.class);
		ex.createCriteria().andEqualTo("prescriptionno", p.getPrescriptionno());
		List<Prescription> pList = prescriptionMapper.selectByExample(ex);
		if(!pList.isEmpty()){
			//不允许重复提交
			throw new HandleException(ErrorCode.NORMAL_ERROR, "处方已提交，不可重复提交");
			//相同的处方，先删除，再插入？
//			Example drugEx = new Example(PrescriptionDrugs.class);
//			drugEx.createCriteria().andEqualTo("prescriptionid", pList.get(0).getId());
//			pDrugMapper.deleteByExample(drugEx);
//			
//			prescriptionMapper.delete(pList.get(0));
		}
		prescriptionMapper.insertUseGeneratedKeys(p);
		Long pid = p.getId();
		
		for(PrescriptionDrugs pdrug : openP.getDrugs()){
			pdrug.setPrescriptionid(pid);
			if(pdrug.getDrugid()==null){
				logger.error("药品id丢失");
				throw new HandleException(ErrorCode.ARG_ERROR, "请求数据异常，联系管理员");
			}
			Drug drug = drugService.getDrugById(pdrug.getDrugid());
			//价格后台来给，避免前端给错价格
			if(!pdrug.getDrugname().equals(drug.getDrugname())){
				logger.error("开方药品不匹配");
				throw new HandleException(ErrorCode.ARG_ERROR, "请求数据异常，联系管理员");
			}
			pdrug.setPrice(drug.getPrice());
			pdrug.setCategory(drug.getCategory());
			
			//加入医生开药信息
			doctorDrugService.add(p.getDoctorname(), p.getDepartment(), pdrug.getDrugid());
		}
		//添加开药信息
		pDrugMapper.insertList(openP.getDrugs());
		
		//加入诊断信息
		String dmsg = p.getDiagnosis();
		diagnosisMsgService.add(dmsg);

		//创建订单
		orderService.create(pid, openP.getRegNo(), openP.getDrugs());

		//通知医院
		try {
			payService.noticePay(p);
		}catch (Exception e) {
			logger.error("通知医院在线支付系统网络异常");
			//throw new HandleException(ErrorCode.NORMAL_ERROR, "通知医院在线支付系统网络异常");
		}
	}
	
	public Prescription getPrescription(Long id) {
		Prescription p = prescriptionMapper.selectByPrimaryKey(id);
		return p;
	}
	
	public PrescriptionInfo getPrescriptionById(Integer id) {
		Prescription p = prescriptionMapper.selectByPrimaryKey(id);
		
		if(p == null){
			return null;
		}
		
		Example ex = new Example(PrescriptionDrugs.class);
		ex.createCriteria().andEqualTo("prescriptionid", id);
		List<PrescriptionDrugs> drugList = pDrugMapper.selectByExample(ex);
		
		PrescriptionInfo info = new PrescriptionInfo();
		info.setPrescription(p);
		info.setDrugList(drugList);
		
		return info;
	}
	
	public PageResult<Prescription> queryPrescription(PrescriptionQuery searchOption){
	
		List<List<?>> sqlResult = prescriptionMapper.queryPrescriptionWithTotal(searchOption);
		
		PageResult<Prescription> result = PageResult.buildPageResult(sqlResult, Prescription.class);
		return result;
	}
		
	private synchronized String getSysNumber(){
		Date now = new Date();
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    localSimpleDateFormat.setLenient(false);
	    String today = localSimpleDateFormat.format(now);
	    SimpleDateFormat strFormat = new SimpleDateFormat("yyMMdd");
	    strFormat.setLenient(false);
	    String todaynum = strFormat.format(now);    
	    Example ex = new Example(PrescriptionNumber.class);
		ex.createCriteria().andEqualTo("opendate", today);
		List<PrescriptionNumber> list = pNumberMapper.selectByExample(ex);
		if(list.isEmpty()){
			PrescriptionNumber number = new PrescriptionNumber();
			number.setNumber(1);
			number.setOpendate(now);
			pNumberMapper.insert(number);
			return "B"+todaynum+formatNumber(number.getNumber());
		}else{
			PrescriptionNumber number = list.get(0);
			number.setNumber(number.getNumber()+1);
			pNumberMapper.updateByPrimaryKey(number);
			return "B"+todaynum+formatNumber(number.getNumber());
		}
	}
	
	private static final String STR_FORMAT = "0000"; 

	private String formatNumber(Integer intHao){
	    DecimalFormat df = new DecimalFormat(STR_FORMAT);
	    return df.format(intHao);
	}

	
}
