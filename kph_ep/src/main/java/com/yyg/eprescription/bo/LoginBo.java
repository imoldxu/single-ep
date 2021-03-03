package com.yyg.eprescription.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class LoginBo {

	@NotBlank
	@Pattern(regexp="^1[3456789]d{9}", message="请输入正确的手机号")
	private String phone;
	
	@NotBlank
	private String password;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
