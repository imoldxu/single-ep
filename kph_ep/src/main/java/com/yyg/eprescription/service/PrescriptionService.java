package com.yyg.eprescription.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yyg.eprescription.bo.OpenPrescriptionBo;
import com.yyg.eprescription.bo.SearchOption;
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
		diagnosisVo.setNum(getSysNumber());
		return diagnosisVo;
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
	
	public List<Prescription> queryPrescription(SearchOption searchOption){
		Example ex = new Example(Prescription.class);
		Criteria criteria = ex.createCriteria();
		if(searchOption.getNumber() != null && !searchOption.getNumber().isEmpty()){
			criteria = criteria.andEqualTo("num", searchOption.getNumber());
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
		if(searchOption.getPatientphone() != null && !searchOption.getPatientphone().isEmpty()){
			criteria = criteria.andEqualTo("patientphone", searchOption.getPatientphone());
		}
		if(searchOption.getStartdate() != null && !searchOption.getStartdate().isEmpty()){
			String startDate =UTCStringtODefaultString(searchOption.getStartdate());
			criteria = criteria.andGreaterThanOrEqualTo("createdate", startDate);
		}
		if(searchOption.getEnddate() != null && !searchOption.getEnddate().isEmpty()){
			String endDate =UTCStringtODefaultString(searchOption.getEnddate());
			criteria = criteria.andLessThanOrEqualTo("createdate", endDate);
		}
		if(searchOption.getState() != null && !searchOption.getState().isEmpty()){
			criteria = criteria.andEqualTo("state", searchOption.getState());
		}
		ex.setOrderByClause("id Desc");
		
		int pageIndex = searchOption.getPageindex().intValue();
		int maxSize = 50;
		RowBounds rowBounds = new RowBounds(pageIndex*maxSize, maxSize);
		List<Prescription> plist = prescriptionMapper.selectByExampleAndRowBounds(ex, rowBounds);
		return plist;
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
