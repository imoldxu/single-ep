package com.x.commons.mybatis;

import io.swagger.annotations.ApiModelProperty;

public class Pagination {

	@ApiModelProperty("页码，从1开始")
    private Integer current;

	@ApiModelProperty("页面大小")
    private Integer pageSize;

    @ApiModelProperty("总数")
    private Long total;

    @ApiModelProperty("总页数")
    private Integer pages;

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}
	
}
