package com.yyg.eprescription.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.annotation.JsonFormat;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_bill")
public class Bill {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;
	
	@Column(name = "amount")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer amount;//金额，都是正数
	
	public static final int TYPE_PAY = 1;
	public static final int TYPE_REFUND = 2;
	
	@Column(name = "type")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer type;//账单类型，1是缴费，2是退费
	
	public static final int WXPAY = 1;
	public static final int ZFB = 2;
	public static final int SHIYIBAO = 3;
	public static final int SHENGYIBAO = 4;
	public static final int CASH = 5; 
	
	@Column(name = "payway")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer payway;//支付方式
	
	@Column(name = "payid")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String payid;//第三方支付编号
	
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;//交易的时间
	
	@Column(name = "orderno")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String orderno;//关联订单

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPayway() {
		return payway;
	}

	public void setPayway(Integer payway) {
		this.payway = payway;
	}

	public String getPayid() {
		return payid;
	}

	public void setPayid(String payid) {
		this.payid = payid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	
}
