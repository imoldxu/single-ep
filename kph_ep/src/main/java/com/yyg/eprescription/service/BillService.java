package com.yyg.eprescription.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.BillQuery;
import com.yyg.eprescription.bo.HospitalBillQuery;
import com.yyg.eprescription.bo.JXBillPageQuery;
import com.yyg.eprescription.bo.JXBillQuery;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.dto.BillDTO;
import com.yyg.eprescription.dto.BillStatisticDTO;
import com.yyg.eprescription.entity.Bill;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.mapper.BillMapper;
import com.yyg.eprescription.vo.BillStatisticVo;
import com.yyg.eprescription.vo.JXBillVo;

import tk.mybatis.mapper.entity.Example;

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
		case Bill.YIDIYIBAO:
			paymode = "yidiyibao";
			break;
		case Bill.CASH:
			paymode = "cash";
			break;
		default:
			throw new HandleException(403, "不支持的支付方式");
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
		} else if(payMode.equalsIgnoreCase("yidiyibao")) {
			return Bill.YIDIYIBAO;
		} else if(payMode.equalsIgnoreCase("cash")){
			return Bill.CASH;
		} else {
			throw new HandleException(403, "不支持的支付方式");
		}
	}
	
	//获取账单明细
	public PageResult<Bill> query(BillQuery billQuery) {
		
		List<List<?>> sqlResult = billMapper.queryBillWithTotal(billQuery);
		
		PageResult<Bill> result = PageResult.buildPageResult(sqlResult, Bill.class);
		return result;
	}
	
	public List<JXBillVo> queryRefundBill(JXBillQuery billQuery) {
		HospitalBillQuery query = new HospitalBillQuery();
		
		try {
			Date end = convert2Date(billQuery.getEndDate(), billQuery.getEndTime());
			Date start = convert2Date(billQuery.getStartDate(), billQuery.getStartTime());
	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String[] payModes = billQuery.getPayMode().split("\\|");
			String payType = billQuery.getTradeFlag();
			Integer type = Integer.valueOf(payType);
			if(type != 2) {
				throw new HandleException(ErrorCode.ARG_ERROR, "TradeFlag参数错误，只能是2");
			}
			List<Integer> payways = new ArrayList<Integer>();
			for(int i=0;i<payModes.length;i++) {
				Integer payway = payMode2Payway(payModes[i]);
				payways.add(payway);
			}
			query.setPayway(payways);
			query.setStartTime(sdf.format(start));
			query.setEndTime(sdf.format(end));
			query.setType(type);
		
		}catch (ParseException e) {
			throw new HandleException(ErrorCode.ARG_ERROR, "时间参数错误");
		}
		List<BillDTO> list = billMapper.jxfyQueryBill(query);
		
		List<JXBillVo> ret = toJXBillVo(list);
		
		return ret;
	}
	
	//获取对账明细
	public List<JXBillVo> reconcile(JXBillPageQuery billQuery) {
		HospitalBillQuery query = new HospitalBillQuery();
		
		try {
			Date end = convert2Date(billQuery.getEndDate(), billQuery.getEndTime());
			Date start = convert2Date(billQuery.getStartDate(), billQuery.getStartTime());
	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String[] payModes = billQuery.getPayMode().split("\\|");
			String payType = billQuery.getTradeFlag();
			Integer type = Integer.valueOf(payType);
			if(type != 1 && type != 2) {
				throw new HandleException(ErrorCode.ARG_ERROR, "TradeFlag参数错误，只能是1或2");
			}
			List<Integer> payways = new ArrayList<Integer>();
			for(int i=0;i<payModes.length;i++) {
				Integer payway = payMode2Payway(payModes[i]);
				payways.add(payway);
			}
			query.setPayway(payways);
			query.setStartTime(sdf.format(start));
			query.setEndTime(sdf.format(end));
			query.setType(type);
			query.setCurrent(billQuery.getPage());
			query.setPageSize(billQuery.getCount());
		}catch (ParseException e) {
			throw new HandleException(ErrorCode.ARG_ERROR, "时间参数错误");
		}
		List<BillDTO> list = billMapper.jxfyQueryBill(query);
		
		List<JXBillVo> ret = toJXBillVo(list);
		
		return ret;
	}

	private List<JXBillVo> toJXBillVo(List<BillDTO> list) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		List<JXBillVo> ret = list.stream().map(dto->{
			JXBillVo vo = new JXBillVo();
			vo.setInvoiceNO(dto.getOrderno());
			vo.setPatientName(dto.getPatientname());
			vo.setPatientNo(dto.getRegNo());
			vo.setPayAmt(dto.getAmount().toString());
			vo.setTPTradeNo(dto.getPayid());
			vo.setTradeFlag(dto.getType().toString());
			vo.setPayDate(dateFormat.format(dto.getCreatetime()));
			vo.setPayTime(timeFormat.format(dto.getCreatetime()));
			vo.setPayMode(payWay2PayMode(dto.getPayway()));
			return vo;
		}).collect(Collectors.toList());
		return ret;
	}
	
	private Date convert2Date(String date, String time) throws ParseException {
		String dateTime = date+" "+time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

	//获取
	public Bill getPayBillByOrderno(String orderno) {
		Example ex = new Example(Bill.class);
		ex.createCriteria().andEqualTo("orderno", orderno).andEqualTo("type", Bill.TYPE_PAY);
		Bill bill = billMapper.selectOneByExample(ex);
		return bill;
	}

}
