package com.yyg.eprescription.controller;



import java.io.IOException;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.yyg.eprescription.context.Response;
import com.yyg.eprescription.entity.CountPrescriptionInfo;
import com.yyg.eprescription.entity.Diagnosis;
import com.yyg.eprescription.entity.DiagnosisMsg;
import com.yyg.eprescription.entity.DoctorDrugs;
import com.yyg.eprescription.entity.Prescription;
import com.yyg.eprescription.entity.PrescriptionDrugs;
import com.yyg.eprescription.entity.PrescriptionInfo;
import com.yyg.eprescription.entity.PrescriptionNumber;
import com.yyg.eprescription.entity.SearchOption;
import com.yyg.eprescription.mapper.DiagnosisMsgMapper;
import com.yyg.eprescription.mapper.DoctorDrugsMapper;
import com.yyg.eprescription.mapper.PrescriptionDrugsMapper;
import com.yyg.eprescription.mapper.PrescriptionMapper;
import com.yyg.eprescription.mapper.PrescriptionNumberMapper;
import com.yyg.eprescription.util.ChineseCharacterUtil;
import com.yyg.eprescription.util.ExportUtil;
import com.yyg.eprescription.util.HttpClientUtil;
import com.yyg.eprescription.util.JSONUtils;
import com.yyg.eprescription.util.WSAPIProxy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@RestController
@RequestMapping(value = "/prescition")
@Api(description = "处方签接口")
public class PrescriptionController {
	
	@Autowired
	PrescriptionMapper prescriptionMapper;
	@Autowired
	PrescriptionDrugsMapper pDrugMapper;
	@Autowired
	PrescriptionNumberMapper pNumberMapper;
	@Autowired
	DiagnosisMsgMapper msgMapper;
	@Autowired
	DoctorDrugsMapper doctorDrugsMapper;
	
	/**
	 * 从武胜医院的webservice获取信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	private Diagnosis getDiagnosisInfoByWS(String type, String number) throws Exception{
		return new WSAPIProxy().getDiagnosis(type, number);
	}
	
	private Diagnosis getDiagnosisInfo(String number) throws Exception {
		HttpClientUtil h = new HttpClientUtil();
		Diagnosis ret = null;
		try {
			h.open("http://127.0.0.1:8867/diagnosis/getDiagnosisByNumber", "get");
			h.addParameter("number", number);

			h.setRequestHeader("Cookie", "Language=zh_CN;UserAgent=PC");
			int status = h.send();
			if(status==200){
				String resp = h.getResponseBodyAsString("utf-8");
				JsonNode respNode = JSONUtils.getJsonObject(resp);
				JsonNode codeNode = respNode.get("code");
				int code = codeNode.asInt();
				if(code == Response.SUCCESS){
					JsonNode dataNode = respNode.get("data");
					String dataStr = dataNode.toString();
					ret = JSONUtils.getObjectByJson(dataStr, Diagnosis.class);
				}else{
					JsonNode msgNode = respNode.get("msg");
					String msg = msgNode.asText();
					throw new Exception(msg);
				}
			}else{
				throw new Exception("系统异常，请联系管理员,status="+status);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("系统异常，请联系管理员");
		} finally {
			h.close();
		}
		return ret;
    }
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDiagnosis", method = RequestMethod.GET)
	@ApiOperation(value = "获取诊断信息", notes = "获取诊断信息")
	public Response getDiagnosis(
			@ApiParam(name = "diagnosisnum", value = "诊断号") @RequestParam(name = "diagnosisnum") String diagnosisnum,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
		if(diagnosisnum.equalsIgnoreCase("-1")){
			Diagnosis diagnosis = new Diagnosis();
			diagnosis.setNum(getSysNumber());
			Response resp = new Response(Response.SUCCESS, diagnosis, Response.SUCCESS_MSG);
			return resp;
		}else{
			//TODO 从医院HIS系统中获得相关信息,武胜医院使用武胜的代码
			try{
				Diagnosis diagnosis = getDiagnosisInfo(diagnosisnum);
				if(diagnosis != null){
					Response resp = new Response(Response.SUCCESS, diagnosis, Response.SUCCESS_MSG);
					return resp;
				}else{
					Response resp = new Response(Response.ERROR, null, "诊断号错误，请检查就诊号信息");
					return resp;
				}
			}catch (Exception e) {
				e.printStackTrace();
				Response resp = new Response(Response.ERROR, null, e.getMessage());
				return resp;
			}
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDiagnosisByType", method = RequestMethod.GET)
	@ApiOperation(value = "获取诊断信息--武胜专用", notes = "获取诊断信息--武胜专用")
	public Response getDiagnosisByType(
			@ApiParam(name = "type", value = "门诊1，住院2") @RequestParam(name = "type") String type,
			@ApiParam(name = "diagnosisnum", value = "诊断号") @RequestParam(name = "diagnosisnum") String diagnosisnum,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
		if(diagnosisnum.equalsIgnoreCase("-1")){
			Diagnosis diagnosis = new Diagnosis();
			diagnosis.setNum(getSysNumber());
			Response resp = new Response(Response.SUCCESS, diagnosis, Response.SUCCESS_MSG);
			return resp;
		}else{
			//TODO 从医院HIS系统中获得相关信息,武胜医院使用武胜的代码
			try{
				//Diagnosis diagnosis = getDiagnosisInfo(diagnosisnum);
				Diagnosis diagnosis = getDiagnosisInfoByWS(type, diagnosisnum);
				if(diagnosis != null){
					Response resp = new Response(Response.SUCCESS, diagnosis, Response.SUCCESS_MSG);
					return resp;
				}else{
					Response resp = new Response(Response.ERROR, null, "诊断号错误，请检查就诊号信息");
					return resp;
				}
			}catch (Exception e) {
				e.printStackTrace();
				Response resp = new Response(Response.ERROR, null, e.getMessage());
				return resp;
			}
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDiagnosisByKeys", method = RequestMethod.GET)
	@ApiOperation(value = "获取诊断信息", notes = "获取诊断信息")
	public Response getDiagnosisByKeys(
			@ApiParam(name = "keys", value = "keys") @RequestParam(name = "keys") String keys,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
					
		keys = keys.toUpperCase();
		List<String> ret = msgMapper.getMsgByKeys(keys+"%");
		//ret = msgMapper.getMsgByKeys(keys+"%");	
				
		Response resp = new Response(Response.SUCCESS, ret, Response.SUCCESS_MSG);
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getPrescriptionList", method = RequestMethod.GET)
	@ApiOperation(value = "获取处方列表", notes = "获取处方列表")
	public Response getPrescriptionList(
			@ApiParam(name = "option", value = "查询条件") @RequestParam(name = "option") String option,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
		Response resp = null;
		try{
			Example ex = new Example(Prescription.class);
			Criteria criteria = ex.createCriteria();
			SearchOption searchOption = JSONUtils.getObjectByJson(option, SearchOption.class);
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
			
			resp = new Response(Response.SUCCESS, plist, "成功");		
			return resp;
		}catch (Exception e) {
			System.out.println("arg option is====>"+option);
			e.printStackTrace();
			return new Response(Response.ERROR, null, "系统异常");		
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getPrescriptionByID", method = RequestMethod.GET)
	@ApiOperation(value = "获取处方详情", notes = "获取处方详情")
	public Response getPrescriptionByID(
			@ApiParam(name = "id", value = "处方id") @RequestParam(name = "id") Integer id,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
		Response resp = null;
		
		Prescription p = prescriptionMapper.selectByPrimaryKey(id);
		
		if(p == null){
			return new Response(Response.ERROR, null, "请求的处方不存在");
		}
		
		Example ex = new Example(PrescriptionDrugs.class);
		ex.createCriteria().andEqualTo("prescriptionid", id);
		List<PrescriptionDrugs> drugList = pDrugMapper.selectByExample(ex);
		
		PrescriptionInfo info = new PrescriptionInfo();
		info.setPrescription(p);
		info.setDrugList(drugList);
		
		resp = new Response(Response.SUCCESS, info, "成功");		
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/open", method = RequestMethod.POST)
	@ApiOperation(value = "开处方", notes = "开处方")
	public Response open(
			@ApiParam(name = "prescriptionInfo", value = "处方信息") @RequestParam(name = "prescriptionInfo") String prescriptionInfo,
			@ApiParam(name = "drugList", value = "处方药列表") @RequestParam(name = "drugList") String drugList,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;
		try{
		
			Prescription p = JSONUtils.getObjectByJson(prescriptionInfo, Prescription.class);
			if(p.getNum() == null || p.getNum().isEmpty()){
				System.out.println("num is miss ====>"+prescriptionInfo);
			}
			
			List<PrescriptionDrugs> drugs = JSONUtils.getObjectListByJson(drugList, PrescriptionDrugs.class);
			if (p==null || drugs==null) {
				resp = new Response(Response.ERROR, null, "请求参数错误");
				return resp;
			}else if(drugs.isEmpty()){
				resp = new Response(Response.ERROR, null, "请选择至少一种药品");
				return resp;
			}
			
			Date now = new Date();
			p.setCreatedate(now);
			p.setState(Prescription.STATE_NEW);
		    
			//检查是否有相同编号的处方签	
			Example ex = new Example(Prescription.class);
			ex.createCriteria().andEqualTo("num", p.getNum()).andEqualTo("state", "生成处方");
			List<Prescription> pList = prescriptionMapper.selectByExample(ex);
			if(!pList.isEmpty()){
				//相同的处方，先删除，再插入
				Example drugEx = new Example(PrescriptionDrugs.class);
				drugEx.createCriteria().andEqualTo("prescriptionid", pList.get(0).getId());
				pDrugMapper.deleteByExample(drugEx);
				
				prescriptionMapper.delete(pList.get(0));
			}	
			prescriptionMapper.insert(p);
			Long pid = p.getId();
			
			for(PrescriptionDrugs pdrug : drugs){
				pdrug.setPrescriptionid(pid);
				
				if(pdrug.getDrugid()==null){
					System.out.println("药品id丢失,druglist=="+drugList);
				}
				
				//加入医生开药信息
				Example dex = new Example(DoctorDrugs.class);
				dex.createCriteria().andEqualTo("drugid", pdrug.getDrugid()).andEqualTo("department", p.getDepartment()).andEqualTo("doctorname", p.getDoctorname());
				List<DoctorDrugs> doctorDrugs = doctorDrugsMapper.selectByExample(dex);
				if(doctorDrugs.isEmpty()){
					DoctorDrugs doctorDrug = new DoctorDrugs();
					doctorDrug.setDepartment(p.getDepartment());
					doctorDrug.setDoctorname(p.getDoctorname());
					doctorDrug.setDrugid(pdrug.getDrugid());
					doctorDrugsMapper.insert(doctorDrug);
				}
			}
			
			pDrugMapper.insertList(drugs);
			
			
			//加入诊断信息
			String dmsg = p.getDiagnosis();
			List<String> msgList = msgMapper.getMsgByKeys(dmsg);	
			if(msgList.isEmpty()){
				DiagnosisMsg msg = new DiagnosisMsg();
				msg.setDiagnosis(dmsg);
				String fullKeys = ChineseCharacterUtil.convertHanzi2Pinyin(dmsg, true);
				msg.setFullkeys(fullKeys);
				String shortKeys = ChineseCharacterUtil.convertHanzi2Pinyin(dmsg, false);
				msg.setShortkeys(shortKeys);
				msgMapper.insert(msg);
			}
			resp = new Response(Response.SUCCESS, null, Response.SUCCESS_MSG);
			return resp;
		}catch (Exception e) {
			System.out.println("prescription is ====>"+prescriptionInfo);
			System.out.println("drugLis is ====>"+ drugList);
			e.printStackTrace();
			resp = new Response(Response.ERROR, null, "系统异常");
			return resp;
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/over", method = RequestMethod.POST)
	@ApiOperation(value = "已领药", notes = "已领药")
	public Response over(
			@ApiParam(name = "id", value = "处方id") @RequestParam(name = "id") Integer id,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;
		
		Prescription p = prescriptionMapper.selectByPrimaryKey(id);
		if (p==null) {
			resp = new Response(Response.ERROR, null, "请求参数错误");
			return resp;
		}
		p.setState(Prescription.STATE_OVER);
		prescriptionMapper.updateByPrimaryKey(p);
		
		resp = new Response(Response.SUCCESS, p, Response.SUCCESS_MSG);
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/rollback", method = RequestMethod.POST)
	@ApiOperation(value = "回退成未领药", notes = "回退成未领药")
	public Response rollback(
			@ApiParam(name = "id", value = "处方id") @RequestParam(name = "id") Integer id,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;
		
		Prescription p = prescriptionMapper.selectByPrimaryKey(id);
		if (p==null) {
			resp = new Response(Response.ERROR, null, "请求参数错误");
			return resp;
		}
		p.setState(Prescription.STATE_NEW);
		prescriptionMapper.updateByPrimaryKey(p);
		
		resp = new Response(Response.SUCCESS, p, Response.SUCCESS_MSG);
		return resp;
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
	
	public static String UTCStringtODefaultString(String UTCString) {
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
	
	private static final String STR_FORMAT = "0000"; 

	private String formatNumber(Integer intHao){
	    DecimalFormat df = new DecimalFormat(STR_FORMAT);
	    return df.format(intHao);
	}
	
	//中医处方统计要和西医分开
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
	@ApiOperation(value = "导出处方签", notes = "导出处方签")
	public void downloadExcel(
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
		respons.setContentType("application/x-msdownload");  
        
		Date now = new Date();
		
		Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(now);
        int i = localCalendar.get(Calendar.MONTH);
        if(i==0){
        	int y = localCalendar.get(Calendar.YEAR);
        	localCalendar.set(Calendar.YEAR, y-1);
        	localCalendar.set(Calendar.MONTH, 11);
        }else{
        	localCalendar.set(Calendar.MONTH, i - 1);
        }
		Date lastMonth = localCalendar.getTime();
        
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM");
	    localSimpleDateFormat.setLenient(false);
	    String lastMonthStr = localSimpleDateFormat.format(lastMonth);
		
		List<CountPrescriptionInfo> infoList = prescriptionMapper.countPrescription(lastMonthStr+"-1", lastMonthStr+"-31");
	
		try {
			String fileName = "处方统计表"+lastMonthStr+".xlsx";  
            respons.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
			ServletOutputStream outputStream = respons.getOutputStream();
			exportExcel(infoList, outputStream);		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void exportExcel(List<CountPrescriptionInfo> list, ServletOutputStream outputStream) {  
        // 创建一个workbook 对应一个excel应用文件  
        XSSFWorkbook workBook = new XSSFWorkbook();  
        // 在workbook中添加一个sheet,对应Excel文件中的sheet  
        XSSFSheet sheet = workBook.createSheet("统方信息");  
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);  
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();  
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();  
        // 构建表头  
        XSSFRow headRow = sheet.createRow(0);
        String[] titles = { "药品名", "医生名", "科室", "数量", "计价单位"}; 
        XSSFCell cell = null;  
        for (int i = 0; i < titles.length; i++)  
        {  
            cell = headRow.createCell(i);  
            cell.setCellStyle(headStyle);  
            cell.setCellValue(titles[i]);  
        }  
        // 构建表体数据  
        if (list != null && list.size() > 0)  
        {  
            for (int j = 0; j < list.size(); j++)  
            {  
                XSSFRow bodyRow = sheet.createRow(j + 1);  
                CountPrescriptionInfo info = list.get(j);  
  
                cell = bodyRow.createCell(0);  
                cell.setCellStyle(bodyStyle);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(info.getDrugname());  
  
                cell = bodyRow.createCell(1);  
                cell.setCellStyle(bodyStyle);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(info.getDoctorname());  
  
                cell = bodyRow.createCell(2);  
                cell.setCellStyle(bodyStyle);  
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(info.getDepartment());

                cell = bodyRow.createCell(3);  
                cell.setCellStyle(bodyStyle);
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(info.getCountnumber());
                
                cell = bodyRow.createCell(4);  
                cell.setCellStyle(bodyStyle);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(info.getDrugunit());
            }  
        }  
        try  
        {  
            workBook.write(outputStream);  
            outputStream.flush();  
            outputStream.close();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        finally  
        {  
            try  
            {  
                outputStream.close();  
            }  
            catch (IOException e)  
            {  
                e.printStackTrace();  
            }  
        }  
  
    }
}
