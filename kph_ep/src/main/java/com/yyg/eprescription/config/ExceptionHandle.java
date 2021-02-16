package com.yyg.eprescription.config;

import javax.validation.ConstraintViolationException;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.context.Response;


/**
 * 全局的异常处理
 * @author 老徐
 *
 */
@ControllerAdvice
public class ExceptionHandle {

	//private static final Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);
	
	@ExceptionHandler(value = Exception.class)
	//@ResponseBody
	public ResponseEntity<Response> handle(Exception e) {
		if (e instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException methodE = (MethodArgumentNotValidException) e;
			FieldError error = (FieldError) methodE.getBindingResult().getAllErrors().get(0);
			Response errorbody = Response.Error(ErrorCode.ARG_ERROR, error.getCode());
			return new ResponseEntity<>(errorbody, HttpStatus.BAD_REQUEST);
		} else if (e instanceof ConstraintViolationException) {
			ConstraintViolationException methodE = (ConstraintViolationException)e;
			//TODO:msg只要:后面的内容
			Response errorbody = Response.Error(ErrorCode.ARG_ERROR, methodE.getMessage());
			return new ResponseEntity<>(errorbody, HttpStatus.BAD_REQUEST);
		} else if (e instanceof HandleException) {
			HandleException methodE = (HandleException) e;
			Response errorbody = Response.Error(methodE.getErrorCode(), methodE.getMessage());
			if (methodE.getErrorCode() == ErrorCode.ARG_ERROR) {
				return new ResponseEntity<Response>(errorbody, HttpStatus.BAD_REQUEST);
			}else {
				return new ResponseEntity<>(errorbody, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		e.printStackTrace();
		Response errorbody = Response.SystemError();
		return new ResponseEntity<>(errorbody, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@ExceptionHandler(value = Exception.class)
//	public ResponseEntity<Response> handleSystemException(Exception e) {
//		e.printStackTrace();
//		Response errors = Response.SystemError();
//		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
}