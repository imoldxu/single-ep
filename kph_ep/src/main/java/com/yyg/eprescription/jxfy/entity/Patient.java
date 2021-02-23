package com.yyg.eprescription.jxfy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="user_creat_card_info")
public class Patient implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5896329768547867452L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer patientId;
	
	@Column(name = "reg_no")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@ApiModelProperty(value = "reg_no")
	private String regNo;
	
	@Column(name = "card_no")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@ApiModelProperty(value = "card_no")
	private String cardNo;
	
	@Column(name = "user_name")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@ApiModelProperty(value = "user_name")
	private String patientName;
	
	@Column(name = "user_age")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@ApiModelProperty(value = "user_age")
	private Integer patientAge;
	
	@Column(name = "user_sex")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@ApiModelProperty(value = "user_sex")
	private String patientSex;
	
	@Column(name = "user_dob")
	@ColumnType(jdbcType = JdbcType.DATE)
	@ApiModelProperty(value = "user_dob")
	private Date patientBirthday;
	
	@Column(name = "creat_card_date")
	@ColumnType(jdbcType = JdbcType.DATE)
	@ApiModelProperty(value = "creat_card_date")
	private Date createTime;

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public Date getPatientBirthday() {
		return patientBirthday;
	}

	public void setPatientBirthday(Date patientBirthday) {
		this.patientBirthday = patientBirthday;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
