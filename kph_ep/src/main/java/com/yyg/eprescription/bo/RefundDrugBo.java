package com.yyg.eprescription.bo;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Valid
public class RefundDrugBo {

	@NotNull
	private Long orderid;
	
	@NotEmpty
	List<RefundInfo> refundRecords;

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public List<RefundInfo> getRefundRecords() {
		return refundRecords;
	}

	public void setRefundRecords(List<RefundInfo> refundRecords) {
		this.refundRecords = refundRecords;
	}
	
}
