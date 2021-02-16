package com.yyg.eprescription.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyg.eprescription.bo.BillQuery;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.Bill;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.mapper.BillMapper;

@Service
public class BillService {

	@Autowired
	BillMapper billMapper;
	
	//创建账单, type1是 缴费，2是退款
	public Bill create(Order order, int type, String payMode, String payid) {
		Bill bill = new Bill();
		bill.setCreatetime(new Date());
		bill.setAmount(order.getAmount());
		bill.setOrderid(order.getId());
		bill.setPayid(payid);
		bill.setType(type);
		bill.setPayway(payMode2Payway(payMode));
		billMapper.insertUseGeneratedKeys(bill);
		
		return bill;
	}
	
	private String payWay2PayMode(int payway){
		String paymode = "";
		switch (payway) {
		case Bill.WXPAY:
			paymode = "wxjs";
			break;
		case Bill.ZFB:
			paymode = "alipayjs";
			break;
		case Bill.SHIYIBAO:
			paymode = "shiyibao";
			break;
		case Bill.SHENGYIBAO:
			paymode = "shengyibao";
			break;
		default:
			break;
		}
		return paymode;
	}
	
	private int payMode2Payway(String payMode){
		if(payMode.equalsIgnoreCase("wxjs")) {
			return Bill.WXPAY;
		} else if(payMode.equalsIgnoreCase("alipayjs")) {
			return Bill.ZFB;
		} else if(payMode.equalsIgnoreCase("shiyibao")) {
			return Bill.SHIYIBAO;
		} else if(payMode.equalsIgnoreCase("shengyibao")) {
			return Bill.SHENGYIBAO;
		} else {
			throw new HandleException(403, "不支持的支付方式");
		}
	}
	
	//获取账单明细
	public List<Bill> query(BillQuery billQuery) throws Exception {
		Date end = convert2Date(billQuery.getEndDate(), billQuery.getEndTime());
		Date start = convert2Date(billQuery.getStartDate(), billQuery.getStartTime());

		String payMode = billQuery.getPayMode();
		String type = billQuery.getTradeFlag();
		List<Bill> list = billMapper.queryBill(payMode, type, start, end);
		return list;
	}
	
	private Date convert2Date(String date, String time) throws ParseException {
		String dateTime = date+" "+time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sdf.parse(dateTime);
	}
	
	//统计总的收支
	public double statistics(Date start, Date end) {
		return 0.0d;
	}

	
	
	
}
