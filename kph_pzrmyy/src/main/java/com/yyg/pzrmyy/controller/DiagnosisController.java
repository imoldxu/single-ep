package com.yyg.pzrmyy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yyg.pzrmyy.context.Response;
import com.yyg.pzrmyy.context.TestBo;
import com.yyg.pzrmyy.entity.User;
import com.yyg.pzrmyy.service.PatientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("医院诊断信息接口")
public class DiagnosisController {
	
	@Autowired
	PatientService patientService;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/patient", method = RequestMethod.GET)
	@ApiOperation(value = "根据就诊卡获取患者信息", notes = "根据就诊卡获取患者信息")
	public Response getPatientByNumber(
			@ApiParam(name = "cardNo", value = "就诊卡号") @RequestParam(name = "cardNo") String cardNo,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
		Response resp = null;
		User diagnosis = null;
		try{
		    diagnosis = patientService.getDiagnosisByCardNo(cardNo);
		    resp = new Response(Response.SUCCESS, diagnosis, "成功");
		}catch (Exception e) {
			e.printStackTrace();
			resp = new Response(Response.ERROR, null, e.getMessage());
		}
		
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/msg/pharmacy_f", method = RequestMethod.POST)
	@ApiOperation(value = "根据就诊卡获取患者信息", notes = "根据就诊卡获取患者信息")
	public Response test(
			@ApiParam(name = "testBo", value = "就诊卡号") @RequestBody TestBo testBo,
			HttpServletRequest request, HttpServletResponse respons) {
		respons.setHeader("Access-Control-Allow-Origin", "*");
		respons.setHeader("Access-Control-Allow-Methods", "GET");
		Response resp = new Response(1, "", "ok");
		
		System.out.println(JSON.toJSONString(testBo));
		
		return resp;
	}
		
}
