package com.yyg.eprescription.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yyg.eprescription.bo.OrderQuery;
import com.yyg.eprescription.bo.PayOverBo;
import com.yyg.eprescription.bo.RefundOrderQuery;
import com.yyg.eprescription.context.JXResp;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.service.OrderService;
import com.yyg.eprescription.vo.OrderVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("订单接口")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/queryOrder", method = RequestMethod.POST)
	@ApiOperation(value = "查询待支付的订单", notes = "查询待支付的订单")
	public JXResp queryOrder(
			@ApiParam(name = "orderQuery", value = "查询信息") @RequestBody @Valid OrderQuery orderQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		List<OrderVo> list = orderService.queryOrder(orderQuery);
		
		JXResp ret = new JXResp(list);
		return ret;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/queryRefundOrder", method = RequestMethod.POST)
	@ApiOperation(value = "查询退款的订单", notes = "查询退款的订单")
	public JXResp queryRefundOrder(
			@ApiParam(name = "refundQuery", value = "查询信息") @RequestBody @Valid RefundOrderQuery refundQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		
	
		return null;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/payOver", method = RequestMethod.POST)
	@ApiOperation(value = "支付通知", notes = "支付通知")
	public JXResp payOver(
			@ApiParam(name = "payOverBo", value = "查询信息") @RequestBody @Valid PayOverBo payOverBo,
			HttpServletRequest request, HttpServletResponse response) {
	
		int amount = Integer.valueOf(payOverBo.getPayAmt()).intValue();
		Order order = orderService.payOver(payOverBo.getPhTradeNo(), amount, payOverBo.getPayMode(), payOverBo.getTPTradeNo());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("InvoiceNO", order.getOrderno());
		JXResp resp = new JXResp(map);
		return resp;
	}
}
