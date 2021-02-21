package com.yyg.eprescription.bo;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yyg.eprescription.entity.PrescriptionDrugs;

@Valid
public class OpenPrescriptionBo {

	@NotBlank
	private String num;//门诊号
	
	private Integer type = 1;//默认是西药
	
	@NotBlank
	private String doctorname;//医生姓名
	
	@NotBlank
	private String department;//科室
	
	@NotBlank
	private String diagnosis;//诊断

	@NotBlank
	private String cardNo;//患者就诊卡卡号
	
	@NotBlank
	private String regNo;//患者登记号
	
	@NotBlank
	private String patientname;//患者姓名
	
	private String patientage;//患者年龄
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date patientBirthday;//患者年龄
	
	@NotBlank
	private String patientsex;//患者性别
	
	private String patientphone;//患者电话
	
	List<PrescriptionDrugs> drugs;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

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

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
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

	public Date getPatientBirthday() {
		return patientBirthday;
	}

	public void setPatientBirthday(Date patientBirthday) {
		this.patientBirthday = patientBirthday;
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

	public List<PrescriptionDrugs> getDrugs() {
		return drugs;
	}

	public void setDrugs(List<PrescriptionDrugs> drugs) {
		this.drugs = drugs;
	}
	
}
