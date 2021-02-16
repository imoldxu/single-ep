package com.yyg.eprescription.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_order")
public class Order implements Serializable {

	private static final long serialVersionUID = 1026976489215761449L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;
	
	@Column(name = "orderno")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String orderno;
	
	@Column(name = "reg_no")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String regNo;  //医院患者登记号

	@Column(name = "trade_info")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String tradeInfo;  //交易内容
	
	@Column(name = "amount")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer amount;  //交易总金额
	
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;  //创建时间
	
	@Column(name = "invalidtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date invalidtime;  //失效时间

	@Column(name = "completetime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date completetime;  //完成时间

	public final static int STATE_NEW = 1;  //新建
	public final static int STATE_PAYED = 2; //已支付
	public final static int STATE_COMPLETE = 3; //已出货、已完成，出货之后不允许该订单直接退款
	public final static int STATE_REFUNDIND = 4; //待退款
	public final static int STATE_REFUNDED = 5; //已退款
	
	@Column(name = "state")
	@ColumnType(jdbcType = JdbcType.TINYINT)
	private Integer state;//状态

	@Column(name = "prescriptionid")
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long prescriptionid;//状态
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getTradeInfo() {
		return tradeInfo;
	}

	public void setTradeInfo(String tradeInfo) {
		this.tradeInfo = tradeInfo;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getInvalidtime() {
		return invalidtime;
	}

	public void setInvalidtime(Date invalidtime) {
		this.invalidtime = invalidtime;
	}

	public Date getCompletetime() {
		return completetime;
	}

	public void setCompletetime(Date completetime) {
		this.completetime = completetime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getPrescriptionid() {
		return prescriptionid;
	}

	public void setPrescriptionid(Long prescriptionid) {
		this.prescriptionid = prescriptionid;
	}

}
