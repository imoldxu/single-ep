package com.yyg.eprescription.bo;

public class SaleRecordQuery {

	private String regNo;
	
	private String prescriptionno;
	
	private String doctorname;
	
	private String department;
	
	private String drugname;
	
	private String drugcompany;
	
	private Integer statisticOrderType;
	
	private String startTime;
	
	private String endTime;
	
	private Integer current;
	
	private Integer pageSize;

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getPrescriptionno() {
		return prescriptionno;
	}

	public void setPrescriptionno(String prescriptionno) {
		this.prescriptionno = prescriptionno;
	}

	public String getDoctorname() {
		return doctorname;
	}

	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDrugname() {
		return drugname;
	}

	public void setDrugname(String drugname) {
		this.drugname = drugname;
	}

	public String getDrugcompany() {
		return drugcompany;
	}

	public void setDrugcompany(String drugcompany) {
		this.drugcompany = drugcompany;
	}

	public Integer getStatisticOrderType() {
		return statisticOrderType;
	}

	public void setStatisticOrderType(Integer statisticOrderType) {
		this.statisticOrderType = statisticOrderType;
	}
	
}
