package com.yyg.eprescription.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class IOrderVo {

	private Long id;
	
	private String regNo;
	
	private String orderno;
	
	private Long prescriptionid;
	
	private String prescriptionno;
	
	private String patientname;
	
	private String doctorname;

	private String department;
	
	private Integer amount;
	
	private Integer payway;
	
	private Integer state;
	
	private String tradeInfo;
	
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss", timezone="GMT+8")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}	

	public String getPatientname() {
		return patientname;
	}

	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}

	public String getDoctorname() {
		return doctorname;
	}

	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}

	public Integer getPayway() {
		return payway;
	}

	public void setPayway(Integer payway) {
		this.payway = payway;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTradeInfo() {
		return tradeInfo;
	}

	public void setTradeInfo(String tradeInfo) {
		this.tradeInfo = tradeInfo;
	}

	public String getPrescriptionno() {
		return prescriptionno;
	}

	public void setPrescriptionno(String prescriptionno) {
		this.prescriptionno = prescriptionno;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Long getPrescriptionid() {
		return prescriptionid;
	}

	public void setPrescriptionid(Long prescriptionid) {
		this.prescriptionid = prescriptionid;
	}
	
}
