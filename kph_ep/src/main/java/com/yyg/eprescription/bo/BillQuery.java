package com.yyg.eprescription.bo;

import javax.validation.constraints.NotNull;

public class BillQuery {

	private String startTime;
	
	private String endTime;
	
	private Integer payway;

	private String payid;
	
	private String orderno;
	
	private Integer current;
	
	private Integer pageSize;
	
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

	public Integer getPayway() {
		return payway;
	}

	public void setPayway(Integer payway) {
		this.payway = payway;
	}

	public String getPayid() {
		return payid;
	}

	public void setPayid(String payid) {
		this.payid = payid;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
}
