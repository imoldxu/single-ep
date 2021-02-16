package com.yyg.pzrmyy.context;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestBo {

	@JsonProperty("PatientName")
	private String PatientName;
	@JsonProperty("PatientNo")
	private String PatientNo;
	@JsonProperty("PatientCardNo")
	private String PatientCardNo;
	@JsonProperty("OEOrdDate")
	private String OEOrdDate;
	@JsonProperty("OEOrdTime")
	private String OEOrdTime;
	@JsonProperty("PharmacyCode")
	private String PharmacyCode;
	public String getPatientName() {
		return PatientName;
	}
	public void setPatientName(String patientName) {
		PatientName = patientName;
	}
	public String getPatientNo() {
		return PatientNo;
	}
	public void setPatientNo(String patientNo) {
		PatientNo = patientNo;
	}
	public String getPatientCardNo() {
		return PatientCardNo;
	}
	public void setPatientCardNo(String patientCardNo) {
		PatientCardNo = patientCardNo;
	}
	public String getOEOrdDate() {
		return OEOrdDate;
	}
	public void setOEOrdDate(String oEOrdDate) {
		OEOrdDate = oEOrdDate;
	}
	public String getOEOrdTime() {
		return OEOrdTime;
	}
	public void setOEOrdTime(String oEOrdTime) {
		OEOrdTime = oEOrdTime;
	}
	public String getPharmacyCode() {
		return PharmacyCode;
	}
	public void setPharmacyCode(String pharmacyCode) {
		PharmacyCode = pharmacyCode;
	}
	
	
}
