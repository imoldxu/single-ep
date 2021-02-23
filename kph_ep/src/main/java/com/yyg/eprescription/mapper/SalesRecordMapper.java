package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.bo.SaleRecordQuery;
import com.yyg.eprescription.entity.SalesRecord;

public interface SalesRecordMapper extends BaseMapper<SalesRecord>{
	
	public List<List<?>> querySalesRecordWithTotal(@Param("query") SaleRecordQuery query);
	
	public List<List<?>> statistic(@Param("query") SaleRecordQuery query);
}
