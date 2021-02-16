package com.yyg.eprescription.bo;

public class OrderQuery {

	private String PharmacyCode;	//药店唯一标识	是	[string]	目前暂默认为：jxfyhpk	查看
	private String PatientNo; //	病人登记号	是	[string]		
	private String ClientType; //客户端	是	[string]	目前默认为：CSYT
	
	public String getPharmacyCode() {
		return PharmacyCode;
	}
	public void setPharmacyCode(String pharmacyCode) {
		PharmacyCode = pharmacyCode;
	}
	public String getPatientNo() {
		return PatientNo;
	}
	public void setPatientNo(String patientNo) {
		PatientNo = patientNo;
	}
	public String getClientType() {
		return ClientType;
	}
	public void setClientType(String clientType) {
		ClientType = clientType;
	}
	
}
