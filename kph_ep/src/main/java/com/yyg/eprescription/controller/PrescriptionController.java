package com.yyg.eprescription.controller;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.OpenPrescriptionBo;
import com.yyg.eprescription.bo.PrescriptionQuery;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.context.Response;
import com.yyg.eprescription.entity.Prescription;
import com.yyg.eprescription.service.PrescriptionService;
import com.yyg.eprescription.vo.PrescriptionInitVo;
import com.yyg.eprescription.vo.PrescriptionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/prescription")
@Api(description = "处方签接口")
public class PrescriptionController {
	
	@Autowired
	PrescriptionService prescriptionService;
	
	//@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	@ApiOperation(value = "初始化处方基本信息", notes = "初始化处方基本信息")
	public Response init(
			@ApiParam(name = "cardNo", value = "患者卡号") @RequestParam(name = "cardNo", required=false) String cardNo,
			@ApiParam(name = "regNo", value = "患者登记号") @RequestParam(name = "regNo", required=false) String regNo,
			HttpServletRequest request, HttpServletResponse respons) throws Exception {
		//respons.setHeader("Access-Control-Allow-Origin", "*");
		//respons.setHeader("Access-Control-Allow-Methods", "GET");
		if(StringUtils.isEmpty(cardNo) && StringUtils.isEmpty(regNo)) {
			throw new HandleException(ErrorCode.ARG_ERROR, "至少提供卡号或登记号");
		}
		
		PrescriptionInitVo diagnosis = prescriptionService.init(cardNo, regNo);
		if(diagnosis != null){
			Response resp = new Response(ErrorCode.OK, diagnosis, "OK");
			return resp;
		}else{
			throw new HandleException(ErrorCode.ARG_ERROR, "请检查患者就诊卡号信息");
		}
	}	
	
	//@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getPrescriptionList", method = RequestMethod.GET)
	@ApiOperation(value = "获取处方列表", notes = "获取处方列表")
	public PageResult<Prescription> getPrescriptionList(
			@ApiParam(name = "option", value = "查询条件") @Valid PrescriptionQuery searchOption,
			HttpServletRequest request, HttpServletResponse respons) {
		//respons.setHeader("Access-Control-Allow-Origin", "*");
		//respons.setHeader("Access-Control-Allow-Methods", "GET");
		
		PageResult<Prescription> ret = prescriptionService.queryPrescription(searchOption);
			
		return ret;
	}
	
	//@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getPrescriptionByID", method = RequestMethod.GET)
	@ApiOperation(value = "获取处方详情", notes = "获取处方详情")
	public Response getPrescriptionByID(
			@ApiParam(name = "id", value = "处方id") @RequestParam(name = "id") Integer id,
			HttpServletRequest request, HttpServletResponse respons) {
		//respons.setHeader("Access-Control-Allow-Origin", "*");
		//respons.setHeader("Access-Control-Allow-Methods", "GET");
		Response resp = null;
		
		PrescriptionInfo info = prescriptionService.getPrescriptionById(id);
		
		resp = new Response(ErrorCode.OK, info, "成功");		
		return resp;
	}
	
	//@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/open", method = RequestMethod.POST)
	@ApiOperation(value = "开处方", notes = "开处方")
	public Response open(
			//@ApiParam(name = "prescriptionInfo", value = "处方信息") @RequestParam(name = "prescriptionInfo") String prescriptionInfo,
			//@ApiParam(name = "drugList", value = "处方药列表") @RequestParam(name = "drugList") String drugList,
			@ApiParam(name="openPrescriptionBo",value="开处方")  @RequestBody @Valid OpenPrescriptionBo openPrescriptionBo,
			HttpServletRequest request, HttpServletResponse respons) {
		
		try {
			Date now = new Date();
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date limit = formater.parse("2021-03-31");
			if(now.after(limit)) {
				return new Response(ErrorCode.NORMAL_ERROR, null, "授权已过期，请联系管理员");
			}
		}catch (Exception e) {
			return new Response(ErrorCode.NORMAL_ERROR, null, "授权已过期，请联系管理员");
		}
			
		Response resp = null;
		try{	
			prescriptionService.open(openPrescriptionBo);
			
			resp = new Response(ErrorCode.OK, null, "OK");
			return resp;
		}catch (Exception e) {
			e.printStackTrace();
			resp = new Response(ErrorCode.ARG_ERROR, null, "系统异常");
			return resp;
		}
	}
	
	//中医处方统计要和西医分开
//	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
//	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
//	@ApiOperation(value = "统计导出处方签", notes = "统计导出处方签")
//	public void downloadExcel(
//			HttpServletRequest request, HttpServletResponse respons) {
//		respons.setHeader("Access-Control-Allow-Origin", "*");
//		respons.setHeader("Access-Control-Allow-Methods", "GET");
//		respons.setContentType("application/x-msdownload");  
//        
//		Date now = new Date();
//		
//		Calendar localCalendar = Calendar.getInstance();
//        localCalendar.setTime(now);
//        int i = localCalendar.get(Calendar.MONTH);
//        if(i==0){
//        	int y = localCalendar.get(Calendar.YEAR);
//        	localCalendar.set(Calendar.YEAR, y-1);
//        	localCalendar.set(Calendar.MONTH, 11);
//        }else{
//        	localCalendar.set(Calendar.MONTH, i - 1);
//        }
//		Date lastMonth = localCalendar.getTime();
//        
//		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM");
//	    localSimpleDateFormat.setLenient(false);
//	    String lastMonthStr = localSimpleDateFormat.format(lastMonth);
//		
//	    List<CountPrescriptionInfo> infoList = prescriptionService.count(lastMonthStr);
//	    
//		try {
//			String fileName = "处方统计表"+lastMonthStr+".xlsx";  
//            respons.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
//			ServletOutputStream outputStream = respons.getOutputStream();
//			exportExcel(infoList, outputStream);		
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
//	private void exportExcel(List<CountPrescriptionInfo> list, ServletOutputStream outputStream) {  
//        // 创建一个workbook 对应一个excel应用文件  
//        XSSFWorkbook workBook = new XSSFWorkbook();  
//        // 在workbook中添加一个sheet,对应Excel文件中的sheet  
//        XSSFSheet sheet = workBook.createSheet("统方信息");  
//        ExportUtil exportUtil = new ExportUtil(workBook, sheet);  
//        XSSFCellStyle headStyle = exportUtil.getHeadStyle();  
//        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();  
//        // 构建表头  
//        XSSFRow headRow = sheet.createRow(0);
//        String[] titles = { "药品名", "医生名", "科室", "数量", "计价单位"}; 
//        XSSFCell cell = null;  
//        for (int i = 0; i < titles.length; i++)  
//        {  
//            cell = headRow.createCell(i);  
//            cell.setCellStyle(headStyle);  
//            cell.setCellValue(titles[i]);  
//        }  
//        // 构建表体数据  
//        if (list != null && list.size() > 0)  
//        {  
//            for (int j = 0; j < list.size(); j++)  
//            {  
//                XSSFRow bodyRow = sheet.createRow(j + 1);  
//                CountPrescriptionInfo info = list.get(j);  
//  
//                cell = bodyRow.createCell(0);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(info.getDrugname());  
//  
//                cell = bodyRow.createCell(1);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(info.getDoctorname());  
//  
//                cell = bodyRow.createCell(2);  
//                cell.setCellStyle(bodyStyle);  
//                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(info.getDepartment());
//
//                cell = bodyRow.createCell(3);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
//                cell.setCellValue(info.getCountnumber());
//                
//                cell = bodyRow.createCell(4);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(info.getDrugunit());
//            }  
//        }  
//        try  
//        {  
//            workBook.write(outputStream);  
//            outputStream.flush();  
//            outputStream.close();  
//        }  
//        catch (IOException e)  
//        {  
//            e.printStackTrace();  
//        }  
//        finally  
//        {  
//            try  
//            {  
//                outputStream.close();  
//            }  
//            catch (IOException e)  
//            {  
//                e.printStackTrace();  
//            }  
//        }  
//  
//    }
}
