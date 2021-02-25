package com.yyg.eprescription.bo;

public class OrderQuery {

	private String regNo; //	病人登记号	
	
	private String prescriptionno;//处方号
	
	private String startTime;
	
	private String endTime;
	
	private String doctorname;
	
	private String patientname;
	
	private Integer payway;
	
	private Integer state;
	
	private Integer current;
	
	private Integer pageSize;

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public String getPrescriptionno() {
		return prescriptionno;
	}

	public void setPrescriptionno(String prescriptionno) {
		this.prescriptionno = prescriptionno;
	}

	public Integer getPayway() {
		return payway;
	}

	public void setPayway(Integer payway) {
		this.payway = payway;
	}

	public String getDoctorname() {
		return doctorname;
	}

	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}

	public String getPatientname() {
		return patientname;
	}

	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	
}
