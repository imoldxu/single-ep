package com.yyg.eprescription.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JXOrderVo {

	
	private String Adm;    //处方系统的就诊号	是	[string]
	
	private String RecipelNo; //处方号
	
	private String PhTradeNo; //	处方订单号	是	[string]	
	
	private String OEOrdDate; //	开单日期	是	[string]	
	
	private String OEOrdTime; //开单时间	是	[string]	
	
	private String OEOrdDeptDesc; //	开单科室	是	[string]	
	
	private String OEOrdDocDesc; //	开单医生	是	[string]	
	
	private String OEOrdState; //订单的状态
	
	private List<JXDrugItem> items;
	
	@JsonProperty("Adm")
	public String getAdm() {
		return Adm;
	}
	public void setAdm(String adm) {
		Adm = adm;
	}
	
	@JsonProperty("RecipelNo")
	public String getRecipelNo() {
		return RecipelNo;
	}
	public void setRecipelNo(String recipelNo) {
		RecipelNo = recipelNo;
	}
	
	@JsonProperty("PhTradeNo")
	public String getPhTradeNo() {
		return PhTradeNo;
	}
	public void setPhTradeNo(String phTradeNo) {
		PhTradeNo = phTradeNo;
	}
	
	@JsonProperty("OEOrdDate")
	public String getOEOrdDate() {
		return OEOrdDate;
	}
	public void setOEOrdDate(String oEOrdDate) {
		OEOrdDate = oEOrdDate;
	}
	
	@JsonProperty("OEOrdTime")
	public String getOEOrdTime() {
		return OEOrdTime;
	}
	public void setOEOrdTime(String oEOrdTime) {
		OEOrdTime = oEOrdTime;
	}
	
	@JsonProperty("OEOrdDeptDesc")
	public String getOEOrdDeptDesc() {
		return OEOrdDeptDesc;
	}
	public void setOEOrdDeptDesc(String oEOrdDeptDesc) {
		OEOrdDeptDesc = oEOrdDeptDesc;
	}
	
	@JsonProperty("OEOrdDocDesc")
	public String getOEOrdDocDesc() {
		return OEOrdDocDesc;
	}
	public void setOEOrdDocDesc(String oEOrdDocDesc) {
		OEOrdDocDesc = oEOrdDocDesc;
	}
	
	public List<JXDrugItem> getItems() {
		return items;
	}
	public void setItems(List<JXDrugItem> items) {
		this.items = items;
	}
	
	@JsonProperty("OEOrdState")
	public String getOEOrdState() {
		return OEOrdState;
	}
	
	public void setOEOrdState(String oEOrdState) {
		OEOrdState = oEOrdState;
	}	
	
}
