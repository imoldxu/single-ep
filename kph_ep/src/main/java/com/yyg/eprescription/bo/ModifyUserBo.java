package com.yyg.eprescription.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ModifyUserBo {

	@NotBlank
	@Pattern(regexp="^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$")
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
