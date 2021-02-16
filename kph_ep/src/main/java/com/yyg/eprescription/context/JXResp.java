package com.yyg.eprescription.context;

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
	
	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getErrmsg() {
		return Errmsg;
	}

	public void setErrmsg(String errmsg) {
		Errmsg = errmsg;
	}

	public Object getData() {
		return Data;
	}

	public void setData(Object data) {
		Data = data;
	}
	
}
