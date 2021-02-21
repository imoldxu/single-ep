package com.yyg.eprescription.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.BillQuery;
import com.yyg.eprescription.bo.JXBillQuery;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.dto.BillStatisticDTO;
import com.yyg.eprescription.entity.Bill;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.entity.Prescription;
import com.yyg.eprescription.mapper.BillMapper;
import com.yyg.eprescription.vo.BillStatisticVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class BillService {

	@Autowired
	BillMapper billMapper;
	
	//创建账单, type1是 缴费，2是退款
	public Bill create(Order order, int type, String payMode, String payid) {
		Bill bill = new Bill();
		bill.setCreatetime(new Date());
		bill.setAmount(order.getAmount());
		bill.setOrderno(order.getOrderno());
		bill.setPayid(payid);
		bill.setType(type);
		bill.setPayway(payMode2Payway(payMode));
		billMapper.insertUseGeneratedKeys(bill);
		
		return bill;
	}
	
	public String payWay2PayMode(int payway){
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
		case Bill.CASH:
			paymode = "cash";
			break;
		default:
			break;
		}
		return paymode;
	}
	
	public int payMode2Payway(String payMode){
		if(payMode.equalsIgnoreCase("wxjs")) {
			return Bill.WXPAY;
		} else if(payMode.equalsIgnoreCase("alipayjs")) {
			return Bill.ZFB;
		} else if(payMode.equalsIgnoreCase("shiyibao")) {
			return Bill.SHIYIBAO;
		} else if(payMode.equalsIgnoreCase("shengyibao")) {
			return Bill.SHENGYIBAO;
		} else if(payMode.equalsIgnoreCase("cash")){
			return Bill.CASH;
		} else {
			throw new HandleException(403, "不支持的支付方式");
		}
	}
	
	//获取账单明细
	public PageResult<Bill> query(BillQuery billQuery) {
		String endTime = billQuery.getEndTime();
		String startTime = billQuery.getStartTime();
		Integer payway = billQuery.getPayway();
		String orderno = billQuery.getOrderno();
		String payid = billQuery.getPayid();
		Integer pageIndex = billQuery.getCurrent();
		Integer maxSize = billQuery.getPageSize();
		
		Example ex = new Example(Bill.class);
		Criteria criteria = ex.createCriteria();
		if(orderno!=null) {
			criteria = criteria.andEqualTo("orderno", orderno);
		}
		if(payid!=null) {
			criteria = criteria.andEqualTo("payid", payid);
		}
		if(payway!=null) {
			criteria = criteria.andEqualTo("payway", payway);
		}
		if(startTime!=null && endTime!=null) {
			criteria = criteria.andBetween("createtime", startTime, endTime);
		}
		
		RowBounds rowBounds = new RowBounds((pageIndex-1)*maxSize, maxSize);
		
		int total = billMapper.selectCountByExample(ex);
		List<Bill> plist = billMapper.selectByExampleAndRowBounds(ex, rowBounds);
		
		PageResult<Bill> result = new PageResult<Bill>();
		result.setData(plist);
		result.setTotal(total);
		result.setSuccess(true);
		return result;
	}
	
	//获取对账明细
	public List<Bill> reconcile(JXBillQuery billQuery) throws Exception {
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

	public BillStatisticVo statistic(BillQuery billQuery) {
		
		List<BillStatisticDTO> dtos = billMapper.statistic(billQuery);
		BillStatisticVo vo = new BillStatisticVo();
		vo.setIncome(0);
		vo.setPay(0);
		dtos.forEach(dto->{
			if(dto.getType() == Bill.TYPE_PAY) {
				vo.setIncome(dto.getTotalAmount());
			}else if(dto.getType() == Bill.TYPE_REFUND) {
				vo.setPay(dto.getTotalAmount());
			}
		});
		
		return vo;
	}

	public Bill getBillByOrder(Long oid) {
		Example ex = new Example(Bill.class);
		ex.createCriteria().andEqualTo("orderid", oid).andEqualTo("type", Bill.TYPE_PAY);
		Bill bill = billMapper.selectOneByExample(ex);
		return bill;
	}

	
	
	
}
