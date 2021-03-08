package com.yyg.eprescription.bo;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class ModifyStockBo {

	@Positive
	private Integer id;
	
	@PositiveOrZero
	private Integer stock;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
}
