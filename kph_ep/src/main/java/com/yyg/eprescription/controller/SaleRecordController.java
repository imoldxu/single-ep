package com.yyg.eprescription.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.SaleRecordQuery;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.User;
import com.yyg.eprescription.service.BillService;
import com.yyg.eprescription.service.SalesRecordService;
import com.yyg.eprescription.util.ExportUtil;
import com.yyg.eprescription.util.MoneyUtil;
import com.yyg.eprescription.vo.SalesRecordDepartmentStatisticVo;
import com.yyg.eprescription.vo.SalesRecordStatisticVo;
import com.yyg.eprescription.vo.SalesRecordVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/saleRecord")
@Api("售药记录")
public class SaleRecordController {

	@Autowired
	SalesRecordService salesRecordService;
	@Autowired
	BillService billService;
	
	@RequiresRoles({"manager"})
	//@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/querySaleRecord", method = RequestMethod.GET)
	@ApiOperation(value = "查询销售记录", notes = "查询销售记录")
	public PageResult<SalesRecordVo> queryBill(
			@ApiParam(name = "query", value = "查询信息") @Valid SaleRecordQuery query,
			HttpServletRequest request, HttpServletResponse response) {
	
		PageResult<SalesRecordVo> result = salesRecordService.query(query);
		
		return result;
	}

	@RequiresRoles({"manager"})
	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	@ApiOperation(value = "导出销售记录", notes = "导出销售记录")
	public void downloadExcel(@ApiParam(name = "query", value = "查询信息") @Valid SaleRecordQuery query,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");  
        
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		if(!user.getPhone().equals("15378184216")) {
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你没有权限进行此操作");
		}
		
		List<SalesRecordVo> result = salesRecordService.queryAll(query);
		
		try {
			String fileName = "download.xlsx";  
            respons.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
			ServletOutputStream outputStream = respons.getOutputStream();
			XSSFWorkbook workBook = exportDetailExcel(result);
			try {  
		        workBook.write(outputStream);
		        outputStream.flush();
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            try {  
	                outputStream.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }
	        }  
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private XSSFWorkbook exportDetailExcel(List<SalesRecordVo> list) {
		// 创建一个workbook 对应一个excel应用文件
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 在workbook中添加一个sheet,对应Excel文件中的sheet
		XSSFSheet sheet = workBook.createSheet("统方信息");
		ExportUtil exportUtil = new ExportUtil(workBook, sheet);
		XSSFCellStyle headStyle = exportUtil.getHeadStyle();
		XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
		// 构建表头
		XSSFRow headRow = sheet.createRow(0);
		String[] titles = { "药品编号", "药品名", "规格", "厂家", "数量", "单价", "医生名", "科室", "付款方式" };
		XSSFCell cell = null;
		for (int i = 0; i < titles.length; i++) {
			cell = headRow.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(titles[i]);
		}
		// 构建表体数据
		if (list != null && list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				XSSFRow bodyRow = sheet.createRow(j + 1);
				SalesRecordVo info = list.get(j);

				int index = 0;

				// 药品编号
				cell = bodyRow.createCell(index++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(info.getDrugno());

				// 药品名称
				cell = bodyRow.createCell(index++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(info.getDrugname());

				// 规格
				cell = bodyRow.createCell(index++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(info.getStandard());

				// 厂商
				cell = bodyRow.createCell(index++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(info.getDrugcompany());

				// 销售数量
				cell = bodyRow.createCell(index++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(info.getNum() - info.getRefundnum());

				// 销售价格
				cell = bodyRow.createCell(index++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(MoneyUtil.changeF2Y(info.getPrice()));

				// 科室
				cell = bodyRow.createCell(index++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(info.getDepartment());

				// 医生姓名
				cell = bodyRow.createCell(index++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(info.getDoctorname());

				// 付款方式
				cell = bodyRow.createCell(index++);
				cell.setCellStyle(bodyStyle);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(billService.payWay2Name(info.getPayway()));
			}
		}

		return workBook;
	}

	
//	@RequiresRoles({"admin"})
//	//@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
//	@RequestMapping(value = "/statistic", method = RequestMethod.GET)
//	@ApiOperation(value = "查询销售记录", notes = "查询销售记录")
//	public PageResult<SalesRecordStatisticVo> statistic(
//			@ApiParam(name = "query", value = "查询信息") @Valid SaleRecordQuery query,
//			HttpServletRequest request, HttpServletResponse response) {
//	
//		PageResult<SalesRecordStatisticVo> result = salesRecordService.statistic(query);
//		
//		return result;
//	}
	
//	@RequiresRoles({"admin"})
//	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
//	@ApiOperation(value = "统计导出处方签", notes = "统计导出处方签")
//	public void downloadExcel(@ApiParam(name = "query", value = "查询信息") @Valid SaleRecordQuery query,
//			HttpServletRequest request, HttpServletResponse respons) {
//		respons.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");  
//        
//		List<SalesRecordStatisticVo> result = salesRecordService.statisticAll(query);
//		
//		try {
//			String fileName = "download.xlsx";  
//            respons.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
//			ServletOutputStream outputStream = respons.getOutputStream();
//			XSSFWorkbook workBook = exportExcel(result);
//			try {  
//		        workBook.write(outputStream);
//		        outputStream.flush();
//	        } catch (IOException e) {  
//	            e.printStackTrace();  
//	        } finally {  
//	            try {  
//	                outputStream.close();  
//	            } catch (IOException e) {  
//	                e.printStackTrace();  
//	            }  
//	        }  
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
	
//	private XSSFWorkbook exportExcel(List<SalesRecordStatisticVo> list) {  
//        // 创建一个workbook 对应一个excel应用文件  
//        XSSFWorkbook workBook = new XSSFWorkbook();  
//        // 在workbook中添加一个sheet,对应Excel文件中的sheet  
//        XSSFSheet sheet = workBook.createSheet("统方信息");  
//        ExportUtil exportUtil = new ExportUtil(workBook, sheet);  
//        XSSFCellStyle headStyle = exportUtil.getHeadStyle();  
//        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();  
//        // 构建表头  
//        XSSFRow headRow = sheet.createRow(0);
//        String[] titles = { "药品编号","药品名","规格","厂商", "医生名", "科室", "销售数量", "销售金额"}; 
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
//                SalesRecordStatisticVo info = list.get(j);  
//  
//                //药品编号
//                cell = bodyRow.createCell(0);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getDrugno());  
//  
//                //药品名称
//                cell = bodyRow.createCell(1);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getDrugname());  
//  
//                //规格
//                cell = bodyRow.createCell(2);  
//                cell.setCellStyle(bodyStyle);  
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getStandard());
//
//                //厂商
//                cell = bodyRow.createCell(3);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getDrugcompany());
//
//                //医生姓名
//                cell = bodyRow.createCell(4);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getDoctorname());  
//  
//                //科室
//                cell = bodyRow.createCell(5);  
//                cell.setCellStyle(bodyStyle);  
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getDepartment());
//
//                //销售价格
//                cell = bodyRow.createCell(6);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(MoneyUtil.changeF2Y(info.getTotalPrice()));
//                
//                //销售数量
//                cell = bodyRow.createCell(7);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.NUMERIC);
//                cell.setCellValue(info.getTotalSale());
//            }  
//		}
//
//    return workBook;
//    }
//	
//	@RequiresRoles({"manager"})
//	@RequestMapping(value = "/department/statistic", method = RequestMethod.GET)
//	@ApiOperation(value = "查询部门销售记录", notes = "查询部门销售记录")
//	public PageResult<SalesRecordDepartmentStatisticVo> statistic(
//			@ApiParam(name = "query", value = "查询信息") @Valid SaleRecordQuery query,
//			HttpServletRequest request, HttpServletResponse response) {
//	
//		PageResult<SalesRecordDepartmentStatisticVo> result = salesRecordService.departStatistic(query);
//		
//		return result;
//	}
	
//	@RequiresRoles({"manager"})
//	@RequestMapping(value = "/department/statistic/excel", method = RequestMethod.GET)
//	@ApiOperation(value = "统计导出处方签", notes = "统计导出处方签")
//	public void downloadExcel(@ApiParam(name = "query", value = "查询信息") @Valid SaleRecordQuery query,
//			HttpServletRequest request, HttpServletResponse respons) {
//		respons.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");  
//        
//		List<SalesRecordDepartmentStatisticVo> result = salesRecordService.departStatisticAll(query);
//		
//		try {
//			String fileName = "download.xlsx";  
//            respons.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
//			ServletOutputStream outputStream = respons.getOutputStream();
//			XSSFWorkbook workBook = exportDepartmentExcel(result);
//			try {  
//		        workBook.write(outputStream);
//		        outputStream.flush();
//	        } catch (IOException e) {  
//	            e.printStackTrace();  
//	        } finally {  
//	            try {  
//	                outputStream.close();  
//	            } catch (IOException e) {  
//	                e.printStackTrace();  
//	            }  
//	        }  
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	private XSSFWorkbook exportDepartmentExcel(List<SalesRecordDepartmentStatisticVo> list) {  
//        // 创建一个workbook 对应一个excel应用文件  
//        XSSFWorkbook workBook = new XSSFWorkbook();  
//        // 在workbook中添加一个sheet,对应Excel文件中的sheet  
//        XSSFSheet sheet = workBook.createSheet("销售信息");  
//        ExportUtil exportUtil = new ExportUtil(workBook, sheet);  
//        XSSFCellStyle headStyle = exportUtil.getHeadStyle();  
//        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();  
//        // 构建表头  
//        XSSFRow headRow = sheet.createRow(0);
//        String[] titles = { "药品编号","药品名","规格","厂商", "科室", "销售数量", "销售金额"}; 
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
//                SalesRecordDepartmentStatisticVo info = list.get(j);  
//  
//                //药品编号
//                cell = bodyRow.createCell(0);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getDrugno());  
//  
//                //药品名称
//                cell = bodyRow.createCell(1);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getDrugname());  
//  
//                //规格
//                cell = bodyRow.createCell(2);  
//                cell.setCellStyle(bodyStyle);  
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getStandard());
//
//                //厂商
//                cell = bodyRow.createCell(3);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getDrugcompany());
//
//                //科室
//                cell = bodyRow.createCell(4);  
//                cell.setCellStyle(bodyStyle);  
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(info.getDepartment());
//
//                //销售价格
//                cell = bodyRow.createCell(5);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(MoneyUtil.changeF2Y(info.getTotalPrice()));
//                
//                //销售数量
//                cell = bodyRow.createCell(6);  
//                cell.setCellStyle(bodyStyle);
//                cell.setCellType(CellType.NUMERIC);
//                cell.setCellValue(info.getTotalSale());
//            }  
//		}
//
//    return workBook;
//    }
}
