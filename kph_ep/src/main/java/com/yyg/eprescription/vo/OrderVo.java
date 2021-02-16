package com.yyg.eprescription.vo;

import java.util.List;

public class OrderVo {

	private String Adm;    //处方系统的就诊号	是	[string]	
	private String PhTradeNo; //	处方订单号	是	[string]	
	private String OEOrdDate; //	开单日期	是	[string]	
	private String OEOrdTime; //开单时间	是	[string]	
	private String OEOrdDeptDesc; //	开单科室	是	[string]	
	private String OEOrdDocDesc; //	开单医生	是	[string]	
	private List<DrugItem> items;
	public String getAdm() {
		return Adm;
	}
	public void setAdm(String adm) {
		Adm = adm;
	}
	public String getPhTradeNo() {
		return PhTradeNo;
	}
	public void setPhTradeNo(String phTradeNo) {
		PhTradeNo = phTradeNo;
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
	public String getOEOrdDeptDesc() {
		return OEOrdDeptDesc;
	}
	public void setOEOrdDeptDesc(String oEOrdDeptDesc) {
		OEOrdDeptDesc = oEOrdDeptDesc;
	}
	public String getOEOrdDocDesc() {
		return OEOrdDocDesc;
	}
	public void setOEOrdDocDesc(String oEOrdDocDesc) {
		OEOrdDocDesc = oEOrdDocDesc;
	}
	public List<DrugItem> getItems() {
		return items;
	}
	public void setItems(List<DrugItem> items) {
		this.items = items;
	}	
	
}
