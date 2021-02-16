package com.yyg.eprescription.controller;



import java.io.IOException;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yyg.eprescription.bo.OpenPrescriptionBo;
import com.yyg.eprescription.bo.SearchOption;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.Response;
import com.yyg.eprescription.entity.Prescription;
import com.yyg.eprescription.entity.PrescriptionDrugs;
import com.yyg.eprescription.mapper.DoctorDrugsMapper;
import com.yyg.eprescription.service.DiagnosisMsgService;
import com.yyg.eprescription.service.PrescriptionService;
import com.yyg.eprescription.util.ExportUtil;
import com.yyg.eprescription.vo.CountPrescriptionInfo;
import com.yyg.eprescription.vo.PrescriptionInitVo;
import com.yyg.eprescription.vo.PrescriptionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/prescition")
@Api(description = "处方签接口")
public class PrescriptionController {
	
	@Autowired
	PrescriptionService prescriptionService;
	
	/**
	 * 从武胜医院的webservice获取信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
//	private Patient getDiagnosisInfoByWS(String type, String number) throws Exception{
//		return new WSAPIProxy().getDiagnosis(type, number);
//	}
	
	
//	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
//	@RequestMapping(value = "/getDiagnosis", method = RequestMethod.GET)
//	@ApiOperation(value = "获取诊断信息", notes = "获取诊断信息")
//	public Response getDiagnosis(
//			@ApiParam(name = "diagnosisnum", value = "诊断号") @RequestParam(name = "diagnosisnum") String diagnosisnum,
//			HttpServletRequest request, HttpServletResponse respons) {
//		respons.setHeader("Access-Control-Allow-Origin", "*");
//		respons.setHeader("Access-Control-Allow-Methods", "GET");
//		if(diagnosisnum.equalsIgnoreCase("-1")){
//			DiagnosisVo diagnosis = new DiagnosisVo();
//			diagnosis.setNum(getSysNumber());
//			Response resp = new Response(ErrorCode.OK, diagnosis, ErrorCode.OK_MSG);
//			return resp;
//		}else{
//			//TODO 从医院HIS系统中获得相关信息,武胜医院使用武胜的代码
//			try{
//				DiagnosisVo diagnosis = getDiagnosisInfo(diagnosisnum);
//				if(diagnosis != null){
//					Response resp = new Response(ErrorCode.OK, diagnosis, ErrorCode.OK_MSG);
//					return resp;
//				}else{
//					Response resp = new Response(ErrorCode.ARG_ERROR, null, "诊断号错误，请检查就诊号信息");
//					return resp;
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//				Response resp = new Response(ErrorCode.ARG_ERROR, null, e.getMessage());
//				return resp;
//			}
//		}
//	}
	
//	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
//	@RequestMapping(value = "/getDiagnosisByType", method = RequestMethod.GET)
//	@ApiOperation(value = "获取诊断信息--武胜专用", notes = "获取诊断信息--武胜专用")
//	public Response getDiagnosisByType(
//			@ApiParam(name = "type", value = "门诊1，住院2") @RequestParam(name = "type") String type,
//			@ApiParam(name = "diagnosisnum", value = "诊断号") @RequestParam(name = "diagnosisnum") String diagnosisnum,
//			HttpServletRequest request, HttpServletResponse respons) {
//		respons.setHeader("Access-Control-Allow-Origin", "*");
//		respons.setHeader("Access-Control-Allow-Methods", "GET");
//		if(diagnosisnum.equalsIgnoreCase("-1")){
//			DiagnosisVo diagnosis = new DiagnosisVo();
//			diagnosis.setNum(getSysNumber());
//			Response resp = new Response(ErrorCode.OK, diagnosis, ErrorCode.OK_MSG);
//			return resp;
//		}else{
//			//TODO 从医院HIS系统中获得相关信息,武胜医院使用武胜的代码
//			try{
//				//Diagnosis diagnosis = getDiagnosisInfo(diagnosisnum);
//				DiagnosisVo diagnosis = getDiagnosisInfoByWS(type, diagnosisnum);
//				if(diagnosis != null){
//					Response resp = new Response(ErrorCode.OK, diagnosis, ErrorCode.OK_MSG);
//					return resp;
//				}else{
//					Response resp = new Response(ErrorCode.ARG_ERROR, null, "诊断号错误，请检查就诊号信息");
//					return resp;
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//				Response resp = new Response(ErrorCode.ARG_ERROR, null, e.getMessage());
//				return resp;
//			}
//		}
//	}

	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	@ApiOperation(value = "初始化处方基本信息", notes = "初始化处方基本信息")
	public Response init(
			@ApiParam(name = "cardNo", value = "患者卡号") @RequestParam(name = "cardNo") String cardNo,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
		
		try{
			PrescriptionInitVo diagnosis = prescriptionService.init(cardNo);
			if(diagnosis != null){
				Response resp = new Response(ErrorCode.OK, diagnosis, "OK");
				return resp;
			}else{
				Response resp = new Response(ErrorCode.ARG_ERROR, null, "诊断号错误，请检查就诊号信息");
				return resp;
			}
		}catch (Exception e) {
			e.printStackTrace();
			Response resp = new Response(ErrorCode.ARG_ERROR, null, e.getMessage());
			return resp;
		}	
	}	
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getPrescriptionList", method = RequestMethod.GET)
	@ApiOperation(value = "获取处方列表", notes = "获取处方列表")
	public Response getPrescriptionList(
			@ApiParam(name = "option", value = "查询条件") @Valid SearchOption searchOption,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
		Response resp = null;
		try{
			List<Prescription> plist = prescriptionService.queryPrescription(searchOption);
			
			resp = new Response(ErrorCode.OK, plist, "成功");		
			return resp;
		}catch (Exception e) {
			e.printStackTrace();
			return new Response(ErrorCode.ARG_ERROR, null, "系统异常");		
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
		
		PrescriptionInfo info = prescriptionService.getPrescriptionById(id);
		
		resp = new Response(ErrorCode.OK, info, "成功");		
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/open", method = RequestMethod.POST)
	@ApiOperation(value = "开处方", notes = "开处方")
	public Response open(
			//@ApiParam(name = "prescriptionInfo", value = "处方信息") @RequestParam(name = "prescriptionInfo") String prescriptionInfo,
			//@ApiParam(name = "drugList", value = "处方药列表") @RequestParam(name = "drugList") String drugList,
			@ApiParam(name="openPrescriptionBo",value="开处方")  @RequestBody @Valid OpenPrescriptionBo openPrescriptionBo,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;
		try{
//			Prescription p = JSON.parseObject(prescriptionInfo, Prescription.class);
//			if(p.getNum() == null || p.getNum().isEmpty()){
//				System.out.println("num is miss ====>"+prescriptionInfo);
//			}
//			
//			List<PrescriptionDrugs> drugs = JSON.parseArray(drugList, PrescriptionDrugs.class);
//			if (p==null || drugs==null) {
//				resp = new Response(ErrorCode.ARG_ERROR, null, "请求参数错误");
//				return resp;
//			}else if(drugs.isEmpty()){
//				resp = new Response(ErrorCode.ARG_ERROR, null, "请选择至少一种药品");
//				return resp;
//			}
			
			//prescriptionService.open(p, drugs);
			prescriptionService.open(openPrescriptionBo);
			
			resp = new Response(ErrorCode.OK, null, "OK");
			return resp;
		}catch (Exception e) {
			//System.out.println("prescription is ====>"+prescriptionInfo);
			//System.out.println("drugLis is ====>"+ drugList);
			e.printStackTrace();
			resp = new Response(ErrorCode.ARG_ERROR, null, "系统异常");
			return resp;
		}
	}
	
//	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
//	@RequestMapping(value = "/over", method = RequestMethod.POST)
//	@ApiOperation(value = "已领药", notes = "已领药")
//	public Response over(
//			@ApiParam(name = "id", value = "处方id") @RequestParam(name = "id") Integer id,
//			HttpServletRequest request, HttpServletResponse respons) {
//		respons.setHeader("Access-Control-Allow-Origin", "*");
//		respons.setHeader("Access-Control-Allow-Methods", "POST");
//		
//		Response resp = null;
//		
//		Prescription p = prescriptionMapper.selectByPrimaryKey(id);
//		if (p==null) {
//			resp = new Response(ErrorCode.ARG_ERROR, null, "请求参数错误");
//			return resp;
//		}
//		p.setState(Prescription.STATE_OVER);
//		prescriptionMapper.updateByPrimaryKey(p);
//		
//		resp = new Response(ErrorCode.OK, p, ErrorCode.OK_MSG);
//		return resp;
//	}
	
//	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
//	@RequestMapping(value = "/rollback", method = RequestMethod.POST)
//	@ApiOperation(value = "回退成未领药", notes = "回退成未领药")
//	public Response rollback(
//			@ApiParam(name = "id", value = "处方id") @RequestParam(name = "id") Integer id,
//			HttpServletRequest request, HttpServletResponse respons) {
//		respons.setHeader("Access-Control-Allow-Origin", "*");
//		respons.setHeader("Access-Control-Allow-Methods", "POST");
//		
//		Response resp = null;
//		
//		Prescription p = prescriptionMapper.selectByPrimaryKey(id);
//		if (p==null) {
//			resp = new Response(ErrorCode.ARG_ERROR, null, "请求参数错误");
//			return resp;
//		}
//		p.setState(Prescription.STATE_NEW);
//		prescriptionMapper.updateByPrimaryKey(p);
//		
//		resp = new Response(ErrorCode.OK, p, ErrorCode.OK_MSG);
//		return resp;
//	}
	
	
	//中医处方统计要和西医分开
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
	@ApiOperation(value = "统计导出处方签", notes = "统计导出处方签")
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
		
	    List<CountPrescriptionInfo> infoList = prescriptionService.count(lastMonthStr);
	    
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
