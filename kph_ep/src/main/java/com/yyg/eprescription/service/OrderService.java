package com.yyg.eprescription.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yyg.eprescription.bo.OrderQuery;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.Bill;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.entity.PrescriptionDrugs;
import com.yyg.eprescription.mapper.OrderMapper;
import com.yyg.eprescription.vo.OrderVo;

import tk.mybatis.mapper.entity.Example;

@Service
public class OrderService {
  
	@Autowired
	OrderMapper orderMapper;
	@Autowired
	BillService billService;
	@Autowired
	SalesRecordService salesRecordService;

	//创建订单
	public Order create(Long prescriptionid, String regNo, List<PrescriptionDrugs> transactionList){
		Order order = new Order();
		String sn = generateOrderSN(regNo);
		order.setPrescriptionid(prescriptionid);
		order.setOrderno(sn);
		order.setTradeInfo(JSON.toJSONString(transactionList));
		order.setRegNo(regNo);
		int amount = 0;
		for(PrescriptionDrugs transaction: transactionList) {
			int subPrice = transaction.getPrice()*transaction.getNumber();
			amount += subPrice;
		}
		order.setAmount(amount);
		order.setCreatetime(new Date());
		order.setState(Order.STATE_NEW);
		orderMapper.insertUseGeneratedKeys(order);
		return order;
	}
	
	//已支付
	@Transactional
	public Order payOver(String orderno, int amount, String payMode, String payid){
		//获取order以便更新，避免重复创建
		Order order = orderMapper.selectOrderForUpdate(orderno);
		if(order.getState() == Order.STATE_PAYED) {
			//若是已经支付过的订单，则不处理
			return order;
		}
		if(order.getAmount() != amount) {
			throw new HandleException(ErrorCode.ARG_ERROR, "支付金额与订单金额不一致");
		}
		if(order.getState() != Order.STATE_NEW) {
			throw new HandleException(ErrorCode.ARG_ERROR, "订单状态异常");
		}
		order.setState(Order.STATE_PAYED);
		orderMapper.updateByPrimaryKey(order);
		
		billService.create(order, Bill.TYPE_PAY, payMode, payid);
		return order;
	}
	
	/**
	 * 出货
	 * @param orderid
	 */
	@Transactional
	public void Deliver(String orderno){
		Order order = orderMapper.selectOrderForUpdate(orderno);
		
		order.setState(Order.STATE_COMPLETE);
		orderMapper.updateByPrimaryKey(order);
		
		//创建出货记录
		salesRecordService.create(order.getId(), order.getTradeInfo());
	}
	
	/**
	 * 创建退款订单
	 * @param regno
	 * @param amount
	 * @param transactionList
	 * @return
	 */
	public Order createRefundOrder(String regno, List<PrescriptionDrugs> transactionList){
		Order order = new Order();
		String sn = generateOrderSN(regno);
		order.setOrderno(sn);
		order.setTradeInfo(JSON.toJSONString(transactionList));
		order.setRegNo(regno);
		int amount = 0;
		for(PrescriptionDrugs transaction: transactionList) {
			int subPrice = transaction.getPrice()*transaction.getNumber();
			amount += subPrice;
		}
		order.setAmount(amount);
		order.setCreatetime(new Date());
		order.setState(Order.STATE_REFUNDIND);
		orderMapper.insertUseGeneratedKeys(order);
		
		//salesRecordService.refund(pid, refundDrugs);
		return order;
	}
	
	/**
	 * 创建退款订单
	 * @param huid
	 * @param amount
	 * @param transactionList
	 * @return
	 */
	@Transactional
	public void RefundOver(String orderno, String payMode, String payid){
		//获取order以便更新，避免重复创建
		Order order = orderMapper.selectOrderForUpdate(orderno);
		if(order.getState() == Order.STATE_REFUNDED) {
			//若是已经支付过的订单，则不处理
			return;
		}
		if(order.getState() != Order.STATE_REFUNDIND) {
			throw new RuntimeException("订单状态异常");
		}
		order.setState(Order.STATE_REFUNDED);
		orderMapper.updateByPrimaryKey(order);
		
		billService.create(order, Bill.TYPE_REFUND, payMode, payid);
	}
	
	/**
	 * 生成按日期的订单号
	 * @param uid
	 * @return
	 */
	private String generateOrderSN(String regno){
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
        String sn = sdf.format(date);
        
        String uidStr = regno;
        
        Random r = new Random(date.getTime());
        int number = r.nextInt(999999);
		String randomStr = String.format("%06d", number);
		
		sn = uidStr+sn+randomStr;
        
        return sn;
	}

	public List<OrderVo> queryOrder(OrderQuery orderQuery) {
		
		String regNo = orderQuery.getPatientNo();
		List<Order> orders = orderMapper.queryUnpayOrders(regNo);
		
		return null;
	}
}
