package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.bo.SaleRecordQuery;
import com.yyg.eprescription.entity.SalesRecord;
import com.yyg.eprescription.vo.SalesRecordStatisticVo;
import com.yyg.eprescription.vo.SalesRecordVo;


public interface SalesRecordMapper extends BaseMapper<SalesRecord>{
	
	public Integer countSalesRecord(@Param("query") SaleRecordQuery query);
	
	public List<SalesRecordVo> querySalesRecord(@Param("query") SaleRecordQuery query);
	
	public List<SalesRecordStatisticVo> statistic(@Param("query") SaleRecordQuery query);
}
