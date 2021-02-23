package com.yyg.eprescription.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JXDrugItem {

	
	private String OEOrdId; // 医嘱号=药品编号 是 [string]
	
	
	private String ArcmiDesc; // 医嘱名称 是 [string]
	

	private String Price; // 医嘱单价 是 [string]
	

	private String Qty;// 医嘱数量 是 [string]
	

	private String UOM; // 医嘱数量单位 是 [string]
	
	
	private String TotalAmount; // 总金额 是 [string]
	

	private String DiscAmount; // 折扣金额 是 [string]
	

	private String ArcmiRemark; // 医嘱备注 是 [string]
	
	
	private String OEOrdRecDeptAddr; // 取药位置 是 [string]
	
	@JsonProperty("OEOrdId")
	public String getOEOrdId() {
		return OEOrdId;
	}
	public void setOEOrdId(String oEOrdId) {
		OEOrdId = oEOrdId;
	}
	
	@JsonProperty("ArcmiDesc")
	public String getArcmiDesc() {
		return ArcmiDesc;
	}
	public void setArcmiDesc(String arcmiDesc) {
		ArcmiDesc = arcmiDesc;
	}
	
	@JsonProperty("Price")
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	
	@JsonProperty("Qty")
	public String getQty() {
		return Qty;
	}
	public void setQty(String qty) {
		Qty = qty;
	}
	
	@JsonProperty("UOM")
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	
	@JsonProperty("TotalAmount")
	public String getTotalAmount() {
		return TotalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		TotalAmount = totalAmount;
	}
	
	@JsonProperty("DiscAmount")
	public String getDiscAmount() {
		return DiscAmount;
	}
	public void setDiscAmount(String discAmount) {
		DiscAmount = discAmount;
	}
	
	@JsonProperty("ArcmiRemark")
	public String getArcmiRemark() {
		return ArcmiRemark;
	}
	public void setArcmiRemark(String arcmiRemark) {
		ArcmiRemark = arcmiRemark;
	}
	
	@JsonProperty("OEOrdRecDeptAddr")
	public String getOEOrdRecDeptAddr() {
		return OEOrdRecDeptAddr;
	}
	public void setOEOrdRecDeptAddr(String oEOrdRecDeptAddr) {
		OEOrdRecDeptAddr = oEOrdRecDeptAddr;
	}
	
}
