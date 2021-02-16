package com.yyg.eprescription.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yyg.eprescription.bo.BillQuery;
import com.yyg.eprescription.context.JXResp;
import com.yyg.eprescription.context.Response;
import com.yyg.eprescription.entity.Bill;
import com.yyg.eprescription.service.BillService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class BillController {

	@Autowired
	BillService billService;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/queryBill", method = RequestMethod.POST)
	@ApiOperation(value = "对账", notes = "对账")
	public JXResp queryBill(
			@ApiParam(name = "billQuery", value = "查询信息") @RequestBody @Valid BillQuery billQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		try {
			List<Bill> list = billService.query(billQuery);
	
			JXResp resp = new JXResp(list);

			return resp;
		}catch (Exception e) {
			return new JXResp("-1", e.getMessage());
		}
	}
	
}
