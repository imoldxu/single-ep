package com.yyg.eprescription.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class AddUserBo {

	@NotBlank
	private String name;
	
	@NotBlank
	@Pattern(regexp="^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$", message="请输入正确的手机号")
	private String phone;
	
	@NotBlank
	private String role;
	
//	@NotEmpty
//	private List<Integer> roleIds;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
//	public List<Integer> getRoleIds() {
//		return roleIds;
//	}
//	public void setRoleIds(List<Integer> roleIds) {
//		this.roleIds = roleIds;
//	}
	
}