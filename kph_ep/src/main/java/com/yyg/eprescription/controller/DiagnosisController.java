package com.yyg.eprescription.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.Response;
import com.yyg.eprescription.service.DiagnosisMsgService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/diagnosis")
@Api("诊断信息")
public class DiagnosisController {

	@Autowired
	DiagnosisMsgService diagnosisMsgService;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDiagnosisByKeys", method = RequestMethod.GET)
	@ApiOperation(value = "获取诊断信息", notes = "获取诊断信息")
	public Response getDiagnosisByKeys(
			@ApiParam(name = "keys", value = "keys") @RequestParam(name = "keys") String keys,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
					
		List<String> ret = diagnosisMsgService.getDiagnosis(keys);	
				
		Response resp = new Response(ErrorCode.OK, ret, Response.SUCCESS_MSG);
		return resp;
	}
}
