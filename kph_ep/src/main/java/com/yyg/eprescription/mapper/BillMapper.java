package com.yyg.eprescription.mapper;

import java.util.Date;
import java.util.List;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.entity.Bill;

public interface BillMapper extends BaseMapper<Bill>{

	List<Bill> queryBill(String payMode, String type, Date start, Date end);

}
