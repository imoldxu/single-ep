package com.yyg.eprescription.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_user")
public class User {

	@JsonIgnore
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer id;
	
	@Column(name = "name")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String name;
	
	@Column(name = "phone")
	@ColumnType(jdbcType = JdbcType.CHAR)
	private String phone;
	
	@JsonIgnore
	@Column(name = "password")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String password;
	
	@Transient
	private Set<Role> roles;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;
	
	public final static int STATE_VALID = 1;
	public final static int STATE_INVALID = 0;
	
	@Column(name = "state")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
}
