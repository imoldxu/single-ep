package com.yyg.eprescription.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.OpenPrescriptionBo;
import com.yyg.eprescription.bo.PrescriptionQuery;
import com.yyg.eprescription.entity.Prescription;
import com.yyg.eprescription.entity.PrescriptionDrugs;
import com.yyg.eprescription.entity.PrescriptionNumber;
import com.yyg.eprescription.mapper.PrescriptionDrugsMapper;
import com.yyg.eprescription.mapper.PrescriptionMapper;
import com.yyg.eprescription.mapper.PrescriptionNumberMapper;
import com.yyg.eprescription.vo.CountPrescriptionInfo;
import com.yyg.eprescription.vo.PrescriptionInitVo;
import com.yyg.eprescription.vo.PrescriptionInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class PrescriptionService {

	@Autowired
	PrescriptionMapper prescriptionMapper;
	@Autowired
	PrescriptionDrugsMapper pDrugMapper;
	@Autowired
	PrescriptionNumberMapper pNumberMapper;
	
	@Autowired
	HospitalPatientService hospitalPatientService;
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
		PrescriptionInitVo diagnosisVo = hospitalPatientService.getDiagnosisInfo(cardNo);
		if(diagnosisVo.getPatientage().equals("0")) {
			String birthday = diagnosisVo.getPatientBirthday();
			String age = getAge(birthday);
			diagnosisVo.setPatientage(age);
		}else {
			diagnosisVo.setPatientage(diagnosisVo.getPatientage()+"岁");
		}
		diagnosisVo.setNum(getSysNumber());
		return diagnosisVo;
	}
	
	/**
	 * 根据出生日期计算年龄、不足1岁精确到几月或几天
	 * @param birthDayStr
	 * @return
	 * @throws ParseException
	 */
	private String getAge(String birthDayStr) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date birthDay = format.parse(birthDayStr);
		
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
        int hour = difference.get(Calendar.HOUR_OF_DAY);
        if (year > 1970) {
            return year - 1970 + "岁";
        } else if (month > Calendar.JANUARY) {
            return month - Calendar.JANUARY + "月";
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
		p.setNum(openP.getNum());
		p.setPatientage(openP.getPatientage());
		p.setPatientname(openP.getPatientname());
		p.setPatientphone(openP.getPatientphone());
		p.setPatientsex(openP.getPatientsex());
		p.setPatientBirthday(openP.getPatientBirthday());
		p.setRegNo(openP.getRegNo());
		p.setType(openP.getType());
	    
		//检查是否有相同编号的处方签	
		Example ex = new Example(Prescription.class);
		ex.createCriteria().andEqualTo("num", p.getNum());
		List<Prescription> pList = prescriptionMapper.selectByExample(ex);
		if(!pList.isEmpty()){
			//相同的处方，先删除，再插入？不允许重复提交
			Example drugEx = new Example(PrescriptionDrugs.class);
			drugEx.createCriteria().andEqualTo("prescriptionid", pList.get(0).getId());
			pDrugMapper.deleteByExample(drugEx);
			
			prescriptionMapper.delete(pList.get(0));
		}	
		prescriptionMapper.insertUseGeneratedKeys(p);
		Long pid = p.getId();
		
		for(PrescriptionDrugs pdrug : openP.getDrugs()){
			pdrug.setPrescriptionid(pid);
			
			if(pdrug.getDrugid()==null){
				System.out.println("药品id丢失");
			}
			
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
		payService.noticePay(p);
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
		Example ex = new Example(Prescription.class);
		Criteria criteria = ex.createCriteria();
		if(searchOption.getNum() != null && !searchOption.getNum().isEmpty()){
			criteria = criteria.andEqualTo("num", searchOption.getNum());
		}
		if(searchOption.getDoctorname() != null && !searchOption.getDoctorname().isEmpty()){
			criteria = criteria.andEqualTo("doctorname", searchOption.getDoctorname());
		}
		if(searchOption.getDepartment() != null && !searchOption.getDepartment().isEmpty()){
			criteria = criteria.andEqualTo("department", searchOption.getDepartment());	
		}
		if(searchOption.getPatientname() != null && !searchOption.getPatientname().isEmpty()){
			criteria = criteria.andEqualTo("patientname", searchOption.getPatientname());		
		}
		if(searchOption.getRegNo() != null && !searchOption.getRegNo().isEmpty()){
			criteria = criteria.andEqualTo("regNo", searchOption.getRegNo());
		}
		if(searchOption.getStartdate() != null && !searchOption.getStartdate().isEmpty()){
			String startDate = searchOption.getStartdate();//UTCStringtODefaultString(searchOption.getStartdate());
			criteria = criteria.andGreaterThanOrEqualTo("createdate", startDate);
		}
		if(searchOption.getEnddate() != null && !searchOption.getEnddate().isEmpty()){
			String endDate = searchOption.getEnddate();//UTCStringtODefaultString(searchOption.getEnddate());
			criteria = criteria.andLessThanOrEqualTo("createdate", endDate);
		}
		if(searchOption.getState() != null && !searchOption.getState().isEmpty()){
			criteria = criteria.andEqualTo("state", searchOption.getState());
		}
		ex.setOrderByClause("id Desc");
		int pageIndex = 1;
		if(searchOption.getCurrent() != null) {
			pageIndex = searchOption.getCurrent().intValue();
		}		
		int maxSize = 50;
		if(searchOption.getPageSize() != null) {
			maxSize = searchOption.getPageSize().intValue();
		}	
		RowBounds rowBounds = new RowBounds((pageIndex-1)*maxSize, maxSize);
		
		int total = prescriptionMapper.selectCountByExample(ex);
		List<Prescription> plist = prescriptionMapper.selectByExampleAndRowBounds(ex, rowBounds);
		
		PageResult<Prescription> result = new PageResult<Prescription>();
		result.setData(plist);
		result.setTotal(total);
		result.setSuccess(true);
		return result;
	}
	
	private String UTCStringtODefaultString(String UTCString) {
        UTCString = UTCString.replace("Z", " UTC");
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
		try {
			date = utcFormat.parse(UTCString);
           return defaultFormat.format(date);

		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
 	    
	}
	
	public List<CountPrescriptionInfo> count(String lastMonthStr) {
		List<CountPrescriptionInfo> infoList = prescriptionMapper.countPrescription(lastMonthStr+"-1", lastMonthStr+"-31");
		return infoList;
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
			return "K"+todaynum+formatNumber(number.getNumber());
		}else{
			PrescriptionNumber number = list.get(0);
			number.setNumber(number.getNumber()+1);
			pNumberMapper.updateByPrimaryKey(number);
			return "K"+todaynum+formatNumber(number.getNumber());
		}
	}
	
	private static final String STR_FORMAT = "0000"; 

	private String formatNumber(Integer intHao){
	    DecimalFormat df = new DecimalFormat(STR_FORMAT);
	    return df.format(intHao);
	}

	
}
