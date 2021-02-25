package com.yyg.pzrmyy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="user_creat_card_info")
public class Patient implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5896329768547867452L;


	@JsonProperty("patientId")
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@ApiModelProperty(value = "id")
	private int id;
	
	@Column(name = "reg_no")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@ApiModelProperty(value = "reg_no")
	private String regNo;
	
	@Column(name = "card_no")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@ApiModelProperty(value = "card_no")
	private String cardNo;
	
	@JsonProperty("patientName")
	@Column(name = "user_name")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@ApiModelProperty(value = "user_name")
	private String userName;
	
	@JsonProperty("patientAge")
	@Column(name = "user_age")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@ApiModelProperty(value = "user_age")
	private Integer userAge;
	
	@JsonProperty("patientSex")
	@Column(name = "user_sex")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@ApiModelProperty(value = "user_sex")
	private String userSex;
	
	@JsonProperty("patientBirthday")
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "user_dob")
	@ColumnType(jdbcType = JdbcType.DATE)
	@ApiModelProperty(value = "user_dob")
	private Date userBirthday;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "creat_card_date")
	@ColumnType(jdbcType = JdbcType.DATE)
	@ApiModelProperty(value = "creat_card_date")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserAge() {
		return userAge;
	}

	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public Date getUserBirthday() {
		return userBirthday;
	}

	public void setUserBirthday(Date userBirthday) {
		this.userBirthday = userBirthday;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
