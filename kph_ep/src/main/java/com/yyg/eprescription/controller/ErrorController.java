package com.yyg.eprescription.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.context.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/error")
@Api("错误接口")
public class ErrorController {

	//@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/unlogin")
	@ApiOperation(value = "未登陆", notes = "未登陆")
	public Response unlogin() {
		throw new HandleException(ErrorCode.UNLOGIN, "未登陆");
	}
	
	//@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/unAuth")
	@ApiOperation(value = "权限不足", notes = "权限不足")
	public Response unAuth() {
		throw new HandleException(ErrorCode.DOMAIN_ERROR, "你没有权限进行此操作");
	}
}
