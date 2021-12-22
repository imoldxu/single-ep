package com.yyg.eprescription.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SalesRecordVo {

	private Long id;
	
	private Long orderid;//订单编号
	
	private String orderno;//订单编号
	
	private Long prescriptionid;//处方
	
	private String prescriptionno;//处方号
	
	private String regNo;//登记号
	
	private String doctorname;//医生姓名
	
	private String department;//科室
	
	private Integer drugid;//药品编号
	
	private String drugno;//药品编号
	
	private String drugname;//药品编号
	
	private Integer num;//销售数量
	
	private String standard;//规格
	
	private String drugunit;//单位
	
	private Integer price;//销售价格
	
	private String drugcompany;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date createtime;//创建时间
	
	private Integer refundnum;

	private Integer payway;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public Long getPrescriptionid() {
		return prescriptionid;
	}

	public void setPrescriptionid(Long prescriptionid) {
		this.prescriptionid = prescriptionid;
	}

	public String getPrescriptionno() {
		return prescriptionno;
	}

	public void setPrescriptionno(String prescriptionno) {
		this.prescriptionno = prescriptionno;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public Integer getDrugid() {
		return drugid;
	}

	public void setDrugid(Integer drugid) {
		this.drugid = drugid;
	}

	public String getDrugname() {
		return drugname;
	}

	public void setDrugname(String drugname) {
		this.drugname = drugname;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getDrugunit() {
		return drugunit;
	}

	public void setDrugunit(String drugunit) {
		this.drugunit = drugunit;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getRefundnum() {
		return refundnum;
	}

	public void setRefundnum(Integer refundnum) {
		this.refundnum = refundnum;
	}

	public String getDoctorname() {
		return doctorname;
	}

	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDrugcompany() {
		return drugcompany;
	}

	public void setDrugcompany(String drugcompany) {
		this.drugcompany = drugcompany;
	}

	public String getDrugno() {
		return drugno;
	}

	public void setDrugno(String drugno) {
		this.drugno = drugno;
	}

	public Integer getPayway() {
		return payway;
	}

	public void setPayway(Integer payway) {
		this.payway = payway;
	}
	
	
}
