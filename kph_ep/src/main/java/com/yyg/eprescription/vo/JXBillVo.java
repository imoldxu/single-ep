package com.yyg.eprescription.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JXBillVo {

	
	private String InvoiceNO;	//本次交易唯一标识
	
	
	private String TPTradeNo;   //第三方系统订单号	是	[string]
	
	
	private String TradeFlag;   //交易方式 1缴费 2退费	是	[string]
	
	
	private String PayMode;     //支付方式 wxjs alipayjs	是	[string]	
	
	
	private String PayDate;     //交易日期	是	[string]	
	
	
	private String PayTime;     //交易时间	是	[string]	
	
	
	private String PayAmt;      //交易金额 (单位分)	是	[string]	
	
	
	private String PharmacyCode="jxfyhpk"; //	药店唯一标识	是	[string]	
	
	
	private String PatientName; //	病人姓名	是	[string]	
	
	
	private String PatientNo;    //病人登记号
	
	@JsonProperty("InvoiceNO")
	public String getInvoiceNO() {
		return InvoiceNO;
	}
	public void setInvoiceNO(String invoiceNO) {
		InvoiceNO = invoiceNO;
	}
	
	@JsonProperty("TPTradeNo")
	public String getTPTradeNo() {
		return TPTradeNo;
	}
	public void setTPTradeNo(String tPTradeNo) {
		TPTradeNo = tPTradeNo;
	}
	
	@JsonProperty("TradeFlag")
	public String getTradeFlag() {
		return TradeFlag;
	}
	public void setTradeFlag(String tradeFlag) {
		TradeFlag = tradeFlag;
	}
	
	@JsonProperty("PayMode")
	public String getPayMode() {
		return PayMode;
	}
	public void setPayMode(String payMode) {
		PayMode = payMode;
	}
	
	@JsonProperty("PayDate")
	public String getPayDate() {
		return PayDate;
	}
	public void setPayDate(String payDate) {
		PayDate = payDate;
	}
	
	@JsonProperty("PayTime")
	public String getPayTime() {
		return PayTime;
	}
	public void setPayTime(String payTime) {
		PayTime = payTime;
	}
	
	@JsonProperty("PayAmt")
	public String getPayAmt() {
		return PayAmt;
	}
	public void setPayAmt(String payAmt) {
		PayAmt = payAmt;
	}
	
	@JsonProperty("PharmacyCode")
	public String getPharmacyCode() {
		return PharmacyCode;
	}
	public void setPharmacyCode(String pharmacyCode) {
		PharmacyCode = pharmacyCode;
	}
	
	@JsonProperty("PatientName")
	public String getPatientName() {
		return PatientName;
	}
	public void setPatientName(String patientName) {
		PatientName = patientName;
	}
	
	@JsonProperty("PatientNo")
	public String getPatientNo() {
		return PatientNo;
	}
	public void setPatientNo(String patientNo) {
		PatientNo = patientNo;
	}
	
}
