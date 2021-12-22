package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.bo.SaleRecordQuery;
import com.yyg.eprescription.entity.SalesRecord;
import com.yyg.eprescription.vo.SalesRecordDepartmentStatisticVo;
import com.yyg.eprescription.vo.SalesRecordStatisticVo;
import com.yyg.eprescription.vo.SalesRecordVo;

public interface SalesRecordMapper extends BaseMapper<SalesRecord>{
	
	public List<List<?>> querySalesRecordWithTotal(@Param("query") SaleRecordQuery query);

	public List<SalesRecordVo> querySalesRecord(@Param("query") SaleRecordQuery query);
	
	public List<List<?>> statistic(@Param("query") SaleRecordQuery query);

	public List<SalesRecordStatisticVo> statisticAll(@Param("query") SaleRecordQuery query);

	public List<List<?>> departStatistic(@Param("query") SaleRecordQuery query);

	public List<SalesRecordDepartmentStatisticVo> departStatisticAll(@Param("query") SaleRecordQuery query);


}
