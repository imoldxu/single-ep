package com.yyg.eprescription.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 处方初始化信息
 * @author 老徐
 *
 */
public class PrescriptionInitVo implements Serializable{

	private static final long serialVersionUID = 5896329768547867452L;

	@JsonIgnoreProperties
	private Integer patientId;
	
	private String prescriptionno;//处方签号
	
	private String regNo;//锦欣妇幼的患者登记号
	
	private String cardNo;//锦欣妇幼的患者卡号
	
	private String doctorname;//医生姓名
	
	private String department;//部门
	
	private String patientname;
	
	private String patientage;
	
	private String patientsex;
	
	private String patientBirthday;//患者的生日，不足1岁的，要用生日算几个月，几天
	
	private String patientphone;
	
	private String diagnosis;//诊断信息

	public String getDoctorname() {
		return doctorname;
	}

	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPatientname() {
		return patientname;
	}

	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}

	public String getPatientage() {
		return patientage;
	}

	public void setPatientage(String patientage) {
		this.patientage = patientage;
	}

	public String getPatientsex() {
		return patientsex;
	}

	public void setPatientsex(String patientsex) {
		this.patientsex = patientsex;
	}

	public String getPatientphone() {
		return patientphone;
	}

	public void setPatientphone(String patientphone) {
		this.patientphone = patientphone;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPrescriptionno() {
		return prescriptionno;
	}

	public void setPrescriptionno(String prescriptionno) {
		this.prescriptionno = prescriptionno;
	}

	public String getPatientBirthday() {
		return patientBirthday;
	}

	public void setPatientBirthday(String patientBirthday) {
		this.patientBirthday = patientBirthday;
	}

}
