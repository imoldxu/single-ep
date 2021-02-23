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

	@SuppressWarnings("unchecked")
	public static <T> PageResult<T> buildPageResult(List<List<?>> sqlResult, Class<T> T) {
		List<T> list = ( List<T>) sqlResult.get(0);
		Integer total = (Integer)sqlResult.get(1).get(0);
		
		PageResult<T> result = new PageResult<T>();
		result.setData(list);
		result.setTotal(total);
		result.setSuccess(true);
		return result;
	}
}
