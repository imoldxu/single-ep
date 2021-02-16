package com.yyg.eprescription.bo;

import javax.validation.constraints.NotBlank;

public class BillQuery {

	@NotBlank(message="不能为空")
	private String StartDate;
	@NotBlank(message="不能为空")
	private String EndDate;
	@NotBlank(message="不能为空")
	private String StartTime;
	@NotBlank(message="不能为空")
	private String EndTime;
	@NotBlank(message="不能为空")
	private String PayMode;
	@NotBlank(message="不能为空")
	private String TradeFlag;
	@NotBlank(message="不能为空")
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
