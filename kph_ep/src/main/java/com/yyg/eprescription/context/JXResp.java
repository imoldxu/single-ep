package com.yyg.eprescription.context;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JXResp {

	private String Code;

	
	private String Errmsg;
	

	private Object Data;

	public JXResp(Object Data) {
		this.Code = "1";
		this.Data = Data;
		this.Errmsg = "OK";
	}
	
	public JXResp(String code, String msg) {
		this.Code = code;
		this.Data = null;
		this.Errmsg = msg;
	}
	
	@JsonProperty("Code")
	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	@JsonProperty("Errmsg")
	public String getErrmsg() {
		return Errmsg;
	}

	public void setErrmsg(String errmsg) {
		Errmsg = errmsg;
	}

	@JsonProperty("Data")
	public Object getData() {
		return Data;
	}

	public void setData(Object data) {
		Data = data;
	}
	
}
