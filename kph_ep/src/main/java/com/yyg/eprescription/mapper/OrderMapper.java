package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.bo.OrderQuery;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.vo.IOrderVo;

public interface OrderMapper extends BaseMapper<Order>{
	
	Order selectOrderForUpdate(@Param("orderno") String orderno);

	IOrderVo getOrderByNo(@Param("orderno") String orderno);
	
	List<IOrderVo> queryPatientUnpayOrder(@Param("regNo")String regNo, @Param("now") String now);

	List<List<?>> queryOrderWithTotal(@Param("query") OrderQuery query);
	
}
