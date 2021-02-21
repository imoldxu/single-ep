package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.vo.IOrderVo;

public interface OrderMapper extends BaseMapper<Order>{
	
	Order selectOrderForUpdate(@Param("orderno") String orderno);

	List<IOrderVo> queryOrder(@Param("regNo") String regNo, @Param("state") Integer state, @Param("startTime") String StartTime, @Param("endTime") String endTime, @Param("current") Integer current, @Param("pageSize") Integer pageSize );
	
	List<IOrderVo> queryPatientOrder(@Param("regNo")String regNo, @Param("state") Integer state);

}
