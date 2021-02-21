package com.yyg.eprescription.bo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JXPayOverBo {
	
	@JsonProperty("PatientNo")
	private String PatientNo; //	病人登记号	是	[string]		
	
	@JsonProperty("TPTradeNo")
	private String TPTradeNo; //	第三方系统交易号	是	[string]		
	
	@JsonProperty("ClientType")
	private String ClientType; //	客户端	是	[string]	CSYT	查看
	
	@JsonProperty("PharmacyCode")
	private String PharmacyCode; //	药店唯一标识	是	[string]	jxfyhpk	查看
	
	@JsonProperty("PayMode")
	private	String PayMode; //	支付方式	是	[string]	wxjs	查看
	
	@JsonProperty("PayAmt")
	private String PayAmt;	//支付金额	是	[string]	单位分	查看
	
	@JsonProperty("PhTradeNo")
	private String PhTradeNo; //	处方订单号	是	[string]
	
	public String getPatientNo() {
		return PatientNo;
	}
	public void setPatientNo(String patientNo) {
		PatientNo = patientNo;
	}
	public String getTPTradeNo() {
		return TPTradeNo;
	}
	public void setTPTradeNo(String tPTradeNo) {
		TPTradeNo = tPTradeNo;
	}
	public String getClientType() {
		return ClientType;
	}
	public void setClientType(String clientType) {
		ClientType = clientType;
	}
	public String getPharmacyCode() {
		return PharmacyCode;
	}
	public void setPharmacyCode(String pharmacyCode) {
		PharmacyCode = pharmacyCode;
	}
	public String getPayMode() {
		return PayMode;
	}
	public void setPayMode(String payMode) {
		PayMode = payMode;
	}
	public String getPayAmt() {
		return PayAmt;
	}
	public void setPayAmt(String payAmt) {
		PayAmt = payAmt;
	}
	public String getPhTradeNo() {
		return PhTradeNo;
	}
	public void setPhTradeNo(String phTradeNo) {
		PhTradeNo = phTradeNo;
	}
	
}
