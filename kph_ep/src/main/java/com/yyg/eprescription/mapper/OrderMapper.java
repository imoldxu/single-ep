package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.entity.Order;

public interface OrderMapper extends BaseMapper<Order>{
	
	Order selectOrderForUpdate(@Param("orderno") String orderno);

	List<Order> queryUnpayOrders(@Param("regNo") String regNo );

}
