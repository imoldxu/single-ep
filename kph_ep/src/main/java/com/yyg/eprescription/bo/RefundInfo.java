package com.yyg.eprescription.bo;

import javax.validation.constraints.NotNull;

public class RefundInfo{
	
	@NotNull
	private Long recordid;

	@NotNull
	private Integer toRefund;

	public Long getRecordid() {
		return recordid;
	}

	public void setRecordid(Long recordid) {
		this.recordid = recordid;
	}

	public Integer getToRefund() {
		return toRefund;
	}

	public void setToRefund(Integer toRefund) {
		this.toRefund = toRefund;
	}
	
}