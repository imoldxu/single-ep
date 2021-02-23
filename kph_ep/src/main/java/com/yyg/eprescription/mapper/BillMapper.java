package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.bo.BillQuery;
import com.yyg.eprescription.bo.HospitalBillQuery;
import com.yyg.eprescription.dto.BillDTO;
import com.yyg.eprescription.dto.BillStatisticDTO;
import com.yyg.eprescription.entity.Bill;

public interface BillMapper extends BaseMapper<Bill>{

	public List<List<?>> queryBillWithTotal(@Param("query") BillQuery query);

	public List<BillDTO> jxfyQueryBill(@Param("query") HospitalBillQuery query);
	
	public List<BillStatisticDTO> statistic(@Param("query") BillQuery query);

}
