package com.yyg.eprescription.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.yyg.eprescription.bo.OrderQuery;
import com.yyg.eprescription.bo.RefundDrugBo;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.Bill;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.entity.PrescriptionDrugs;
import com.yyg.eprescription.entity.SalesRecord;
import com.yyg.eprescription.mapper.OrderMapper;
import com.yyg.eprescription.vo.DrugItem;
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
		order.setCreatetime(new Date());
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
	public Order createRefundOrder(RefundDrugBo refundDrugBo) {

		Long oid = refundDrugBo.getOrderid();

		Order oldOrder = getOrderById(oid);
		oldOrder.getPrescriptionid();

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
		order.setCreatetime(new Date());
		if (oldOrder.getPayway() == Bill.CASH) {
			// 现金退款需要收费处点击退款才认为是已退款
			order.setState(Order.STATE_REFUNDIND);
		} else {
			order.setState(Order.STATE_REFUNDED);
		}
		order.setPayway(oldOrder.getPayway());
		orderMapper.insertUseGeneratedKeys(order);

		String payMode = billService.payWay2PayMode(oldOrder.getPayway());

		if (order.getState() == Order.STATE_REFUNDED) {
			// 若是非现金的退款，默认为已退款
			billService.create(order, Bill.TYPE_REFUND, payMode, "");
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

	public PageResult<IOrderVo> queryPatientOrder(OrderQuery query) {
		List<IOrderVo> totalList = orderMapper.queryOrder(query.getRegNo(), null, null, null, 1, 10000);

		List<IOrderVo> pageList = orderMapper.queryOrder(query.getRegNo(), null, null, null, query.getCurrent(),
				query.getPageSize());
		// List<IOrderVo> list= ( List<IOrderVo>) pageList.get(0);
		// Integer total = (Integer) pageList.get(1).get(0);

		PageResult<IOrderVo> result = new PageResult<IOrderVo>();
		result.setData(pageList);
		result.setSuccess(true);
		result.setTotal(totalList.size());
		return result;
	}

	public List<JXOrderVo> queryUnpayOrderForWX(@Valid JXOrderQuery query) {

		List<IOrderVo> ret = orderMapper.queryPatientOrder(query.getPatientNo(), Order.STATE_NEW);

		List<JXOrderVo> result = toJxOrder(ret);

		return result;
	}

	public List<JXOrderVo> queryRefundOrderForWX(@Valid JXOrderQuery query) {

		// List<IOrderVo> ret = orderMapper.queryPatientOrder(query.getPatientNo(),
		// Order.STATE_NEW);
		// List<OrderVo> result = toJxOrder(ret);
		// return result;
		return null;
	}

	private List<JXOrderVo> toJxOrder(List<IOrderVo> ret) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

		List<JXOrderVo> result = ret.stream().map(o -> {
			JXOrderVo jxo = new JXOrderVo();
			jxo.setAdm(o.getRegNo());
			String tradeInfo = o.getTradeInfo();
			List<PrescriptionDrugs> druglist = JSON.parseArray(tradeInfo, PrescriptionDrugs.class);

			List<DrugItem> items = druglist.stream().map(drug -> {
				DrugItem item = new DrugItem();
				item.setArcmiDesc(drug.getMyusage());
				item.setArcmiRemark(drug.getFrequency());
				item.setDiscAmount("0");
				item.setOEOrdId(drug.getId().toString());
				item.setOEOrdRecDeptAddr("便民药房");
				item.setPrice(drug.getPrice().toString());
				item.setQty(drug.getNumber().toString());
				item.setTotalAmount(String.valueOf(drug.getPrice() * drug.getNumber()));
				item.setUOM(drug.getUnit());
				return item;
			}).collect(Collectors.toList());
			jxo.setItems(items);
			jxo.setOEOrdDate(dateFormat.format(o.getCreateTime()));
			jxo.setOEOrdTime(timeFormat.format(o.getCreateTime()));
			jxo.setOEOrdDeptDesc(o.getDepartment());
			jxo.setOEOrdDocDesc(o.getDoctorname());
			jxo.setPhTradeNo(o.getOrderno());
			return jxo;
		}).collect(Collectors.toList());
		return result;
	}

	public PageResult<IOrderVo> queryOrder(OrderQuery orderQuery) {

		List<IOrderVo> pageList = orderMapper.queryOrder(orderQuery.getRegNo(), orderQuery.getState(),
				orderQuery.getStartTime(), orderQuery.getEndTime(), orderQuery.getCurrent(), orderQuery.getPageSize());
		// List<IOrderVo> list= ( List<IOrderVo>) pageList.get(0);
		List<IOrderVo> totalList = orderMapper.queryOrder(orderQuery.getRegNo(), orderQuery.getState(),
				orderQuery.getStartTime(), orderQuery.getEndTime(), 1, 100000); // (Integer) pageList.get(1).get(0);

		PageResult<IOrderVo> result = new PageResult<IOrderVo>();
		result.setData(pageList);
		result.setSuccess(true);
		result.setTotal(totalList.size());
		return result;
	}

	// 现金支付完成
	@Transactional
	public void cashOver(String orderno) {

		Order order = orderMapper.selectOrderForUpdate(orderno);

		if (order.getState() != Order.STATE_NEW) {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "订单状态异常");
		}

		order.setState(Order.STATE_PAYED);
		order.setPayway(Bill.CASH);
		orderMapper.updateByPrimaryKey(order);

		billService.create(order, Bill.TYPE_PAY, "cash", "");

		return;
	}

	// 现金退款完成
	@Transactional
	public void cashRefund(String orderno) {

		Order order = orderMapper.selectOrderForUpdate(orderno);
		if (order.getState() == Order.STATE_REFUNDED) {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "订单已退款，无需重复退款");
		}

		if (order.getState() != Order.STATE_PAYED && order.getState() != Order.STATE_REFUNDIND) {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "订单状态异常");
		}

		order.setState(Order.STATE_REFUNDED);

		orderMapper.updateByPrimaryKey(order);

		billService.create(order, Bill.TYPE_REFUND, "cash", "");
		return;
	}

	// 已支付
	@Transactional
	public Order payOver(String orderno, int amount, String payMode, String payid) {
		// 获取order以便更新，避免重复创建
		Order order = orderMapper.selectOrderForUpdate(orderno);
		if (order.getState() == Order.STATE_PAYED) {
			// 若是已经支付过的订单，则不处理
			Bill bill = billService.getBillByOrder(order.getId());
			String billPayMode = billService.payWay2PayMode(bill.getPayway());
			if(bill.getPayid().equalsIgnoreCase(payid) && billPayMode.equalsIgnoreCase(payMode)) {
				//正常支付
				return order;	
			}else {
				throw new HandleException(ErrorCode.NORMAL_ERROR, "其他渠道已经支付");
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
	 * 退款完成
	 * 
	 * @param huid
	 * @param amount
	 * @param transactionList
	 * @return
	 */
	@Transactional
	public void RefundOver(String orderno, String payMode, String payid) {
		// 获取order以便更新，避免重复创建
		Order order = orderMapper.selectOrderForUpdate(orderno);
		if (order.getState() == Order.STATE_REFUNDED) {
			// 若是已经支付过的订单，则不处理
			return;
		}
		if (order.getState() != Order.STATE_PAYED && order.getState() != Order.STATE_REFUNDIND) {
			throw new RuntimeException("订单状态异常");
		}
		order.setState(Order.STATE_REFUNDED);
		orderMapper.updateByPrimaryKey(order);

		billService.create(order, Bill.TYPE_REFUND, payMode, payid);
	}

}
