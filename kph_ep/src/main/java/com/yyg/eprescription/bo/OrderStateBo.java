package com.yyg.eprescription.bo;

import javax.validation.constraints.NotBlank;

public class OrderStateBo {

	@NotBlank(message="orderno不能为空")
	private String orderno;

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	
}
