package com.yyg.eprescription.bo;

import javax.validation.constraints.NotNull;

public class UpDownDrugBo {

	@NotNull
	private Integer drugid;

	public Integer getDrugid() {
		return drugid;
	}

	public void setDrugid(Integer drugid) {
		this.drugid = drugid;
	}
	
}
