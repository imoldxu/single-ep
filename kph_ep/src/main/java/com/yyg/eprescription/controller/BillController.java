package com.yyg.eprescription.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.BillQuery;
import com.yyg.eprescription.bo.JXBillQuery;
import com.yyg.eprescription.context.JXResp;
import com.yyg.eprescription.entity.Bill;
import com.yyg.eprescription.service.BillService;
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
	
	@RequiresRoles({"manager"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/queryBill", method = RequestMethod.GET)
	@ApiOperation(value = "查询账单", notes = "对账")
	public PageResult<Bill> queryBill(
			@ApiParam(name = "billQuery", value = "查询信息") @Valid BillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		PageResult<Bill> result = billService.query(billQuery);
		
		return result;
	}
	
	@RequiresRoles({"manager"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/statistic", method = RequestMethod.GET)
	@ApiOperation(value = "统计账单", notes = "统计账单")
	public BillStatisticVo statistic(
			@ApiParam(name = "billQuery", value = "查询信息") @Valid BillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
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
			@ApiParam(name = "billQuery", value = "查询信息") @RequestBody @Valid JXBillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		try {
			List<JXBillVo> list = billService.reconcile(billQuery);
	
			JXResp resp = new JXResp(list);

			return resp;
		}catch (Exception e) {
			return new JXResp("-1", e.getMessage());
		}
	}
	
}
