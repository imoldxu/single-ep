package com.yyg.eprescription.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JXBillQuery {

	@JsonProperty(value="StartDate", access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="不能为空")
	private String StartDate;
	
	@JsonProperty(value="EndDate", access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="不能为空")
	private String EndDate;
	
	@JsonProperty(value="StartTime", access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="不能为空")
	private String StartTime;
	
	@JsonProperty(value="EndTime", access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="不能为空")
	private String EndTime;
	
	@JsonProperty(value="PayMode", access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="不能为空")
	private String PayMode;
	
	@JsonProperty(value="TradeFlag", access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="不能为空")
	private String TradeFlag;
	
	@JsonProperty(value="PharmacyCode", access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="不能为空")
	@Pattern(regexp="^jxfyhpk")
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
