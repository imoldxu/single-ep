package com.yyg.eprescription.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.BillQuery;
import com.yyg.eprescription.bo.JXBillPageQuery;
import com.yyg.eprescription.bo.JXBillQuery;
import com.yyg.eprescription.context.JXResp;
import com.yyg.eprescription.entity.Bill;
import com.yyg.eprescription.entity.User;
import com.yyg.eprescription.service.BillService;
import com.yyg.eprescription.util.ExportUtil;
import com.yyg.eprescription.util.IPUtils;
import com.yyg.eprescription.util.MoneyUtil;
import com.yyg.eprescription.vo.BillStatisticVo;
import com.yyg.eprescription.vo.JXBillVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/bill")
@Api("账单")
public class BillController {

	@Autowired
	BillService billService;
	@Autowired
	private Environment env;
	
	@RequiresRoles({"manager"})
	@RequestMapping(value = "/queryBill", method = RequestMethod.GET)
	@ApiOperation(value = "查询账单", notes = "对账")
	public PageResult<Bill> queryBill(
			@ApiParam(name = "billQuery", value = "查询信息") @Valid BillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		PageResult<Bill> result = billService.query(billQuery);
		
		return result;
	}
	
	@RequiresRoles({"manager"})
	@RequestMapping(value = "/statistic", method = RequestMethod.GET)
	@ApiOperation(value = "统计账单", notes = "统计账单")
	public BillStatisticVo statistic(
			@ApiParam(name = "billQuery", value = "查询信息") @Valid BillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		BillStatisticVo result = billService.statistic(billQuery);
	
		return result;
	}
	
	@RequiresRoles({"tollman"})
	@RequestMapping(value = "/queryMyBill", method = RequestMethod.GET)
	@ApiOperation(value = "查询收费员账单", notes = "查询收费员账单")
	public PageResult<Bill> queryMyBill(
			@ApiParam(name = "billQuery", value = "查询信息") @Valid BillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		billQuery.setPayid(user.getPhone());
		
		PageResult<Bill> result = billService.query(billQuery);
		
		return result;
	}
	
	@RequiresRoles({"tollman"})
	@RequestMapping(value = "/downloadMyBill", method = RequestMethod.GET)
	@ApiOperation(value = "下载收费员账单明细", notes = "下载收费员账单明细")
	public void downloadMyBill(
			@ApiParam(name = "billQuery", value = "查询信息") @Valid BillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		billQuery.setPayid(user.getPhone());
		
		List<Bill> result = billService.queryAll(billQuery);
	
		try {
			String fileName = "download.xlsx";  
            response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
			ServletOutputStream outputStream = response.getOutputStream();
			XSSFWorkbook workBook = exportExcel(result);
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
	
	@RequiresRoles({"manager"})
	@RequestMapping(value = "/downloadBill", method = RequestMethod.GET)
	@ApiOperation(value = "下载账单明细", notes = "下载账单明细")
	public void downloadBill(
			@ApiParam(name = "billQuery", value = "查询信息") @Valid BillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		List<Bill> result = billService.queryAll(billQuery);
	
		try {
			String fileName = "download.xlsx";  
            response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
			ServletOutputStream outputStream = response.getOutputStream();
			XSSFWorkbook workBook = exportExcel(result);
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
	
	@RequiresRoles({"tollman"})
	@RequestMapping(value = "/mystatistic", method = RequestMethod.GET)
	@ApiOperation(value = "统计我的账单", notes = "统计我的账单")
	public BillStatisticVo myStatistic(
			@ApiParam(name = "billQuery", value = "查询信息") @Valid BillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		billQuery.setPayid(user.getPhone());
		
		BillStatisticVo result = billService.statistic(billQuery);
	
		return result;
	}
	
	//医院需要的接口
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/queryRefundBill", method = RequestMethod.POST)
	@ApiOperation(value = "查询退费", notes = "查询退费")
	public JXResp queryRefundBill(
			@ApiParam(name = "billQuery", value = "查询信息") @RequestBody @Valid JXBillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		String host = env.getProperty("hospital.host");
		String ip = IPUtils.getRealIp(request);
		if(!host.equals(ip)) {
			return new JXResp("-1", "非授权的IP访问");
		}
		
		try {
			List<JXBillVo> list = billService.queryRefundBill(billQuery);
	
			JXResp resp = new JXResp(list);

			return resp;
		}catch (Exception e) {
			return new JXResp("-1", e.getMessage());
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/reconcile", method = RequestMethod.POST)
	@ApiOperation(value = "对账", notes = "对账")
	public JXResp reconcile(
			@ApiParam(name = "billQuery", value = "查询信息") @RequestBody @Valid JXBillPageQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		String host = env.getProperty("hospital.host");
		String ip = IPUtils.getRealIp(request);
		if(!host.equals(ip)) {
			return new JXResp("-1", "非授权的IP访问");
		}
		
		try {
			List<JXBillVo> list = billService.reconcile(billQuery);
	
			JXResp resp = new JXResp(list);

			return resp;
		}catch (Exception e) {
			return new JXResp("-1", e.getMessage());
		}
	}
	
	private XSSFWorkbook exportExcel(List<Bill> list) {  
        // 创建一个workbook 对应一个excel应用文件  
        XSSFWorkbook workBook = new XSSFWorkbook();  
        // 在workbook中添加一个sheet,对应Excel文件中的sheet  
        XSSFSheet sheet = workBook.createSheet("账单明细");  
        ExportUtil exportUtil = new ExportUtil(workBook, sheet);  
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();  
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();  
        // 构建表头  
        XSSFRow headRow = sheet.createRow(0);
        String[] titles = { "账单编号","收款工号/第三方渠道订单号","支付方式","支付金额","支付时间","关联订单号"}; 
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
        	SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (int j = 0; j < list.size(); j++)  
            {  
                XSSFRow bodyRow = sheet.createRow(j + 1);  
                Bill info = list.get(j);  
  
                //账单编号
                cell = bodyRow.createCell(0);  
                cell.setCellStyle(bodyStyle);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(info.getId());  
  
                //收款工号/流水号
                cell = bodyRow.createCell(1);  
                cell.setCellStyle(bodyStyle);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(info.getPayid());  
  
                //支付方式
                cell = bodyRow.createCell(2);  
                cell.setCellStyle(bodyStyle);  
                cell.setCellType(CellType.STRING);
                cell.setCellValue(billService.payWay2Name(info.getPayway()));

                //支付金额
                cell = bodyRow.createCell(3);  
                cell.setCellStyle(bodyStyle);
                cell.setCellType(CellType.STRING);
                if(info.getType() == Bill.TYPE_PAY) {
                	cell.setCellValue(MoneyUtil.changeF2Y(info.getAmount()));	
                }else {
                	cell.setCellValue(MoneyUtil.changeF2Y((0-info.getAmount())));
                }
                
                //支付时间
                cell = bodyRow.createCell(4);  
                cell.setCellStyle(bodyStyle);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(formater.format(info.getCreatetime()));  
  
                //关联订单号
                cell = bodyRow.createCell(5);  
                cell.setCellStyle(bodyStyle);  
                cell.setCellType(CellType.STRING);
                cell.setCellValue(info.getOrderno());
            }  
		}

        return workBook;
    }
}
