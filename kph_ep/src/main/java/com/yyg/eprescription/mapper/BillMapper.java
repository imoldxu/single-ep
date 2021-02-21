package com.yyg.eprescription.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.bo.BillQuery;
import com.yyg.eprescription.dto.BillStatisticDTO;
import com.yyg.eprescription.entity.Bill;

public interface BillMapper extends BaseMapper<Bill>{

	public List<Bill> queryBill(String payMode, String type, Date start, Date end);

	public List<BillStatisticDTO> statistic(@Param("query") BillQuery query);

}
