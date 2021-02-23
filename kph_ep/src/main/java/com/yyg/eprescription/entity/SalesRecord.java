package com.yyg.eprescription.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="t_sales_record")
public class SalesRecord {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long id;
	
	@Column(name = "orderid")
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long orderid;//订单编号
	
	@Column(name = "prescriptionid")
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private Long prescriptionid;//处方
	
	@Column(name = "drugid")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer drugid;//药品编号
	
	@Column(name = "drugname")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String drugname;//药品编号
	
	@Column(name = "num")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer num;//销售数量
	
	@Column(name = "standard")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String standard;//规格
	
	@Column(name = "drugunit")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String drugunit;//单位
	
	@Column(name = "drugcompany")
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String drugcompany;//单位
	
	@Column(name = "price")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer price;//销售价格
	
	@Column(name = "createtime")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	private Date createtime;//创建时间
	
	@Column(name = "refundnum")
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer refundnum;//退货数量，将退货挂在销售记录上是为了保障销售价格和退的价格一致，以及多次退货的处理。若将退货单独记录成一条salesRecord为负数num的话，多次退货若出现价格不一致的，就不太好判断是退的哪一笔了
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDrugid() {
		return drugid;
	}

	public void setDrugid(Integer drugid) {
		this.drugid = drugid;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}


	public String getDrugname() {
		return drugname;
	}

	public void setDrugname(String drugname) {
		this.drugname = drugname;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Long getPrescriptionid() {
		return prescriptionid;
	}

	public void setPrescriptionid(Long prescriptionid) {
		this.prescriptionid = prescriptionid;
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

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Integer getRefundnum() {
		return refundnum;
	}

	public void setRefundnum(Integer refundnum) {
		this.refundnum = refundnum;
	}

	public String getDrugcompany() {
		return drugcompany;
	}

	public void setDrugcompany(String drugcompany) {
		this.drugcompany = drugcompany;
	}

}
