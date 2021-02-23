package com.yyg.eprescription.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.SaleRecordQuery;
import com.yyg.eprescription.service.SalesRecordService;
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
	
	@RequiresRoles({"manager"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/querySaleRecord", method = RequestMethod.GET)
	@ApiOperation(value = "查询销售记录", notes = "查询销售记录")
	public PageResult<SalesRecordVo> queryBill(
			@ApiParam(name = "query", value = "查询信息") @Valid SaleRecordQuery query,
			HttpServletRequest request, HttpServletResponse response) {
	
		PageResult<SalesRecordVo> result = salesRecordService.query(query);
		
		return result;
	}
	
	@RequiresRoles({"admin"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/statistic", method = RequestMethod.GET)
	@ApiOperation(value = "查询销售记录", notes = "查询销售记录")
	public PageResult<SalesRecordStatisticVo> statistic(
			@ApiParam(name = "query", value = "查询信息") @Valid SaleRecordQuery query,
			HttpServletRequest request, HttpServletResponse response) {
	
		PageResult<SalesRecordStatisticVo> result = salesRecordService.statistic(query);
		
		return result;
	}
}
