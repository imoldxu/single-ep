package com.yyg.eprescription.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.JXOrderQuery;
import com.yyg.eprescription.bo.JXUnpayOrderQuery;
import com.yyg.eprescription.bo.OrderQuery;
import com.yyg.eprescription.bo.RefundDrugBo;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.Bill;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.entity.PrescriptionDrugs;
import com.yyg.eprescription.entity.SalesRecord;
import com.yyg.eprescription.mapper.OrderMapper;
import com.yyg.eprescription.vo.JXDrugItem;
import com.yyg.eprescription.vo.IOrderVo;
import com.yyg.eprescription.vo.JXOrderVo;

@Service
public class OrderService {

	@Autowired
	OrderMapper orderMapper;
	@Autowired
	BillService billService;
	@Autowired
	SalesRecordService salesRecordService;

	// 创建订单
	public Order create(Long prescriptionid, String regNo, List<PrescriptionDrugs> transactionList) {
		Order order = new Order();
		String sn = generateOrderSN(regNo);
		order.setPrescriptionid(prescriptionid);
		order.setOrderno(sn);
		order.setTradeInfo(JSON.toJSONString(transactionList));
		order.setRegNo(regNo);
		int amount = 0;
		for (PrescriptionDrugs transaction : transactionList) {
			int subPrice = transaction.getPrice() * transaction.getNumber();
			amount += subPrice;
		}
		order.setAmount(amount);
		Date now = new Date();
		order.setCreatetime(now);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_YEAR, 3);
		order.setInvalidtime(calendar.getTime());
		order.setState(Order.STATE_NEW);
		orderMapper.insertUseGeneratedKeys(order);
		return order;
	}

	/**
	 * 出货
	 * 
	 * @param orderid
	 */
	@Transactional
	public void deliver(String orderno) {
		Order order = orderMapper.selectOrderForUpdate(orderno);

		if (order.getState() != Order.STATE_PAYED) {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "订单状态异常");
		}

		order.setCompletetime(new Date());
		order.setState(Order.STATE_COMPLETE);
		orderMapper.updateByPrimaryKey(order);

		// 创建出货记录
		salesRecordService.create(order.getId(), order.getTradeInfo());
	}

	private Order getOrderById(Long orderid) {
		Order order = orderMapper.selectByPrimaryKey(orderid);
		return order;
	}

	/**
	 * 创建退款订单
	 * 
	 * @param regno
	 * @param amount
	 * @param transactionList
	 * @return
	 */
	@Transactional
	public Order createRefundOrder(RefundDrugBo refundDrugBo) {

		Long oid = refundDrugBo.getOrderid();

		Order oldOrder = getOrderById(oid);
		oldOrder.getPrescriptionid();

		//退药记录
		List<SalesRecord> list = salesRecordService.refund(refundDrugBo.getRefundRecords());

		Order order = new Order();
		String sn = generateOrderSN(oldOrder.getRegNo());
		order.setOrderno(sn);
		order.setTradeInfo(JSON.toJSONString(list));
		order.setRegNo(oldOrder.getRegNo());
		int amount = 0;
		for (SalesRecord transaction : list) {
			int subPrice = transaction.getPrice() * transaction.getNum();
			amount += subPrice;
		}
		order.setPrescriptionid(oldOrder.getPrescriptionid());
		order.setAmount(amount);
		Date now = new Date();
		order.setCreatetime(now);
		if (oldOrder.getPayway() == Bill.CASH || oldOrder.getPayway() == Bill.BANKCARD || oldOrder.getPayway() == Bill.SHIYIBAO || oldOrder.getPayway() == Bill.YIDIYIBAO) {
			// 现金退款需要收费处点击退款才认为是已退款
			order.setState(Order.STATE_REFUNDIND);
		} else {
			order.setState(Order.STATE_REFUNDED);
		}
		order.setPayway(oldOrder.getPayway());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_YEAR, 3);
		order.setInvalidtime(calendar.getTime());
		orderMapper.insertUseGeneratedKeys(order);

		Bill payBill = billService.getPayBillByOrderno(oldOrder.getOrderno());
		String payMode = billService.payWay2PayMode(payBill.getPayway());

		if (order.getState() == Order.STATE_REFUNDED) {
			// 若是非现金的退款，默认为已退款
			billService.create(order, Bill.TYPE_REFUND, payMode, payBill.getPayid());
		}

		return order;
	}

	/**
	 * 生成按日期的订单号
	 * 
	 * @param uid
	 * @return
	 */
	private String generateOrderSN(String regno) {
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
		String sn = sdf.format(date);

		String uidStr = regno;

		Random r = new Random(date.getTime());
		int number = r.nextInt(999999);
		String randomStr = String.format("%06d", number);

		sn = uidStr + sn + randomStr;

		return sn;
	}

	public List<JXOrderVo> queryUnpayOrderForWX(@Valid JXUnpayOrderQuery query) {

		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String nowTime = formater.format(now);
		
		List<IOrderVo> ret = orderMapper.queryPatientUnpayOrder(query.getPatientNo(), nowTime);

		List<JXOrderVo> result = toJxOrderVoList(ret);

		return result;
	}

	public JXOrderVo getOrderByNo(JXOrderQuery orderQuery) {
	
		IOrderVo ret = orderMapper.getOrderByNo(orderQuery.getOrderno());
		
		JXOrderVo result = toJXOrderVo(ret);
		
		return result;
	}
	
	private List<JXOrderVo> toJxOrderVoList(List<IOrderVo> ret) {

		List<JXOrderVo> result = ret.stream().map(o -> {
			return toJXOrderVo(o);
		}).collect(Collectors.toList());
		return result;
	}

	private JXOrderVo toJXOrderVo(IOrderVo o) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		JXOrderVo jxo = new JXOrderVo();
		jxo.setAdm(o.getRegNo());
		jxo.setRecipelNo(o.getPrescriptionno());
		String tradeInfo = o.getTradeInfo();
		List<PrescriptionDrugs> druglist = JSON.parseArray(tradeInfo, PrescriptionDrugs.class);

		List<JXDrugItem> items = druglist.stream().map(drug -> {
			JXDrugItem item = new JXDrugItem();
			item.setArcmiDesc(drug.getDrugname());
			item.setArcmiRemark(drug.getMyusage()+" "+drug.getFrequency()+" "+drug.getSingledose());
			item.setDiscAmount("0");
			item.setOEOrdId(drug.getId().toString());
			item.setOEOrdRecDeptAddr("一楼母婴店旁药房取药");
			item.setPrice(drug.getPrice().toString());
			item.setQty(drug.getNumber().toString());
			item.setTotalAmount(String.valueOf(drug.getPrice() * drug.getNumber()));
			item.setUOM(drug.getUnit());
			return item;
		}).collect(Collectors.toList());
		jxo.setOEOrdState(o.getState().toString());
		jxo.setItems(items);
		jxo.setOEOrdDate(dateFormat.format(o.getCreateTime()));
		jxo.setOEOrdTime(timeFormat.format(o.getCreateTime()));
		jxo.setOEOrdDeptDesc(o.getDepartment());
		jxo.setOEOrdDocDesc(o.getDoctorname());
		jxo.setPhTradeNo(o.getOrderno());
		return jxo;
	}

	public PageResult<IOrderVo> queryOrder(OrderQuery orderQuery) {

		List<List<?>> sqlResult = orderMapper.queryOrderWithTotal(orderQuery);
				
		PageResult<IOrderVo> result = PageResult.buildPageResult(sqlResult, IOrderVo.class);
		return result;
	}

	// 现金支付完成
	@Transactional
	public void offlinePayOver(String orderno, String userName, String payMode) {

		Order order = orderMapper.selectOrderForUpdate(orderno);
		if (order.getState() >= Order.STATE_PAYED) {
			// 若是已经支付过的订单，则不处理
			Bill bill = billService.getPayBillByOrderno(orderno);
			String billPayMode = billService.payWay2PayMode(bill.getPayway());
			if(billPayMode.equalsIgnoreCase(payMode)) {
				//相同的渠道已经正常支付，重复的提交
				return;	
			}else {
				throw new HandleException(ErrorCode.OTHERPAY, "其他渠道已经支付");
			}
		}
		if (order.getState() != Order.STATE_NEW) {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "订单状态异常");
		}

		order.setState(Order.STATE_PAYED);
		Integer payway = billService.payMode2Payway(payMode);
		order.setPayway(payway);
		orderMapper.updateByPrimaryKey(order);

		billService.create(order, Bill.TYPE_PAY, payMode, userName);

		return;
	}

	//线下退款完成
	@Transactional
	public void offlineRefund(String orderno, String userName ) {

		Order order = orderMapper.selectOrderForUpdate(orderno);
		if (order.getState() == Order.STATE_REFUNDED) {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "订单已退款，无需重复退款");
		}
		if (order.getState() != Order.STATE_PAYED && order.getState() != Order.STATE_REFUNDIND) {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "订单状态异常");
		}
		if (order.getPayway() != Bill.CASH && order.getPayway() != Bill.BANKCARD && order.getPayway() != Bill.SHIYIBAO && order.getPayway() != Bill.YIDIYIBAO) {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "线上支付,请到便民药房发起退款");
		}

		order.setState(Order.STATE_REFUNDED);

		orderMapper.updateByPrimaryKey(order);

		String paymode = billService.payWay2PayMode(order.getPayway());
		
		billService.create(order, Bill.TYPE_REFUND, paymode, userName);
		return;
	}

	// 已支付
	@Transactional
	public Order payOver(String orderno, int amount, String payMode, String payid) {
		// 获取order以便更新，避免重复创建
		Order order = orderMapper.selectOrderForUpdate(orderno);
		if (order.getState() >= Order.STATE_PAYED) {
			// 若是已经支付过的订单，则不处理
			Bill bill = billService.getPayBillByOrderno(orderno);
			String billPayMode = billService.payWay2PayMode(bill.getPayway());
			if(bill.getPayid().equalsIgnoreCase(payid) && billPayMode.equalsIgnoreCase(payMode)) {
				//正常支付
				return order;	
			}else {
				throw new HandleException(ErrorCode.OTHERPAY, "其他渠道已经支付");
			}
		}
		if (order.getAmount() != amount) {
			throw new HandleException(ErrorCode.ARG_ERROR, "支付金额与订单金额不一致");
		}
		if (order.getState() != Order.STATE_NEW) {
			throw new HandleException(ErrorCode.ARG_ERROR, "订单状态异常");
		}
		
		Integer payway = billService.payMode2Payway(payMode);
		
		order.setPayway(payway);
		order.setState(Order.STATE_PAYED);
		orderMapper.updateByPrimaryKey(order);
		
		billService.create(order, Bill.TYPE_PAY, payMode, payid);
		return order;
	}

	/**
	 * 退款通知完成，不需要，默认线上就是退成功的
	 * 
	 * @param huid
	 * @param amount
	 * @param transactionList
	 * @return
	 */
//	@Transactional
//	public void RefundOver(String orderno, String payMode, String payid) {
//		// 获取order以便更新，避免重复创建
//		Order order = orderMapper.selectOrderForUpdate(orderno);
//		if (order.getState() == Order.STATE_REFUNDED) {
//			// 若是已经支付过的订单，则不处理
//			return;
//		}
//		if (order.getState() != Order.STATE_PAYED && order.getState() != Order.STATE_REFUNDIND) {
//			throw new RuntimeException("订单状态异常");
//		}
//		order.setState(Order.STATE_REFUNDED);
//		orderMapper.updateByPrimaryKey(order);
//
//		billService.create(order, Bill.TYPE_REFUND, payMode, payid);
//	}

	

}
