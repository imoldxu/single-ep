package com.yyg.eprescription.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JXOrderQuery {

	@NotBlank(message="订单号不能为空")
	@JsonProperty(value="PhTradeNo", access=JsonProperty.Access.WRITE_ONLY)
	private String orderno;	//药店唯一标识	是	[string]	目前暂默认为：jxfyhpk	查看
	
	@JsonProperty(value="PharmacyCode", access=JsonProperty.Access.WRITE_ONLY)
	private String PharmacyCode; //	jxfyhpk
	
	@Pattern(regexp="^CSYT")
	@JsonProperty(value="ClientType", access=JsonProperty.Access.WRITE_ONLY)
	private String ClientType; //目前默认为：CSYT
	
	public String getPharmacyCode() {
		return PharmacyCode;
	}
	public void setPharmacyCode(String pharmacyCode) {
		PharmacyCode = pharmacyCode;
	}
	
	public String getClientType() {
		return ClientType;
	}
	public void setClientType(String clientType) {
		ClientType = clientType;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

}
