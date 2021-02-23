package com.yyg.eprescription.bo;

import java.util.List;

public class HospitalBillQuery {

	private List<Integer> payway;
	
	private Integer type;
	
	private String startTime;
	
	private String endTime;

	public List<Integer> getPayway() {
		return payway;
	}

	public void setPayway(List<Integer> payway) {
		this.payway = payway;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
