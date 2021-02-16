package com.yyg.eprescription.bo;

public class RefundOrderQuery {

	private String StartDate;
	
	private String EndDate;
	
	private String StartTime;
	
	private String EndTime;
	
	private String PayMode;
	
	private String TradeFlag;
	
	private String PharmacyCode;

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getPayMode() {
		return PayMode;
	}

	public void setPayMode(String payMode) {
		PayMode = payMode;
	}

	public String getTradeFlag() {
		return TradeFlag;
	}

	public void setTradeFlag(String tradeFlag) {
		TradeFlag = tradeFlag;
	}

	public String getPharmacyCode() {
		return PharmacyCode;
	}

	public void setPharmacyCode(String pharmacyCode) {
		PharmacyCode = pharmacyCode;
	}
	
}
