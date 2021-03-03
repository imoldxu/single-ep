package com.yyg.eprescription.bo;

import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JXBillPageQuery extends JXBillQuery{

	@JsonProperty(value="Count", access = JsonProperty.Access.WRITE_ONLY)
	@Positive(message="Count需要大于1")
	private Integer Count;
	
	@JsonProperty(value="Page", access = JsonProperty.Access.WRITE_ONLY)
	@Positive(message="Page需要大于1")
	private Integer Page;

	public Integer getCount() {
		return Count;
	}

	public void setCount(Integer count) {
		Count = count;
	}

	public Integer getPage() {
		return Page;
	}

	public void setPage(Integer page) {
		Page = page;
	}

}
