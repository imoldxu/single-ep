package com.x.commons.mybatis;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 分页结果.
 * @author Ryan
 */
public class PageResult<T> {

	@ApiModelProperty("分页信息")
    private Integer total;
    
    @ApiModelProperty("数据")
    private List<T> data;

    private boolean success;
    
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
