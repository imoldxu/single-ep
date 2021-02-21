package com.yyg.eprescription.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.JXOrderQuery;
import com.yyg.eprescription.bo.OrderQuery;
import com.yyg.eprescription.bo.OrderStateBo;
import com.yyg.eprescription.bo.JXPayOverBo;
import com.yyg.eprescription.bo.RefundDrugBo;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.context.JXResp;
import com.yyg.eprescription.entity.Order;
import com.yyg.eprescription.service.OrderService;
import com.yyg.eprescription.vo.IOrderVo;
import com.yyg.eprescription.vo.JXOrderVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api("订单接口")
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@RequiresRoles({"manager"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/queryOrder", method = RequestMethod.GET)
	@ApiOperation(value = "管理平台查询的订单", notes = "管理平台查询的订单")
	public PageResult<IOrderVo> queryOrder(
			@ApiParam(name = "orderQuery", value = "查询信息") @Valid OrderQuery orderQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		PageResult<IOrderVo> result = orderService.queryOrder(orderQuery);
		
		return result;
	}
	
	@RequiresRoles({"manager"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/deliver", method = RequestMethod.PUT)
	@ApiOperation(value = "确认现金支付", notes = "确认现金支付")
	public void deliver(
			@ApiParam(name = "orderStateBo", value = "订单状态修改") @RequestBody @Valid OrderStateBo orderStateBo,
			HttpServletRequest request, HttpServletResponse response) {
		
		orderService.deliver(orderStateBo.getOrderno());
		
		return;
	}
	
	@RequiresRoles({"manager"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/refundDrug", method = RequestMethod.POST)
	@ApiOperation(value = "退药", notes = "退药")
	public void refundDrug(
			@ApiParam(name = "refundDrugBo", value = "退药") @RequestBody @Valid RefundDrugBo refundDrugBo,
			HttpServletRequest request, HttpServletResponse response) {
		
		orderService.createRefundOrder(refundDrugBo);
		
		return;
	}
	
	@RequiresRoles({"tollman"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/queryPatientOrder", method = RequestMethod.GET)
	@ApiOperation(value = "医院收费处查询订单", notes = "医院收费处查询订单")
	public PageResult<IOrderVo> queryPatientOrder(
			@ApiParam(name = "orderQuery", value = "查询信息") @Valid OrderQuery query,
			HttpServletRequest request, HttpServletResponse response) {
	
		if(query.getRegNo()==null || query.getRegNo().isEmpty()) {
			throw new HandleException(ErrorCode.ARG_ERROR, "登记号不能为空");
		}
		PageResult<IOrderVo> result = orderService.queryPatientOrder(query);
		
		return result;
	}
	
	@RequiresRoles({"tollman"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/cashOver", method = RequestMethod.PUT)
	@ApiOperation(value = "确认现金支付", notes = "确认现金支付")
	public void cashOrder(
			@ApiParam(name = "orderStateBo", value = "订单状态修改") @RequestBody @Valid OrderStateBo orderStateBo,
			HttpServletRequest request, HttpServletResponse response) {
		
		orderService.cashOver(orderStateBo.getOrderno());
		
		return;
	}
	
	@RequiresRoles({"tollman"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/cashRefund", method = RequestMethod.PUT)
	@ApiOperation(value = "确认医院现金退款", notes = "确认医院现金退款")
	public void cashRefund(
			@ApiParam(name = "orderStateBo", value = "订单状态修改")  @RequestBody @Valid OrderStateBo orderStateBo,
			HttpServletRequest request, HttpServletResponse response) {
		
		orderService.cashRefund(orderStateBo.getOrderno());
		
		return;
	}
	
	//以下是医院需要的接口
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/queryUnpayOrder", method = RequestMethod.POST)
	@ApiOperation(value = "微信查询待缴费订单", notes = "微信查询待缴费订单")
	public JXResp queryUnpayOrder(
			@ApiParam(name = "orderQuery", value = "查询信息") @RequestBody @Valid JXOrderQuery query,
			HttpServletRequest request, HttpServletResponse response) {
	
		List<JXOrderVo> list = orderService.queryUnpayOrderForWX(query);
		
		JXResp ret = new JXResp(list);
		return ret;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/queryRefundOrder", method = RequestMethod.GET)
	@ApiOperation(value = "医院收费处查询的订单", notes = "医院收费处查询的订单")
	public JXResp queryRefundOrder(
			@ApiParam(name = "orderQuery", value = "查询信息") @RequestBody @Valid JXOrderQuery orderQuery,
			HttpServletRequest request, HttpServletResponse response) {
	
		try {
			List<JXOrderVo> list = orderService.queryRefundOrderForWX(orderQuery);
			
			JXResp ret = new JXResp(list);
			return ret;
		}catch (Exception e) {
			JXResp resp = new JXResp("-8","系统异常");
			return resp;
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/payOver", method = RequestMethod.POST)
	@ApiOperation(value = "支付通知", notes = "支付通知")
	public JXResp payOver(
			@ApiParam(name = "payOverBo", value = "查询信息") @RequestBody @Valid JXPayOverBo payOverBo,
			HttpServletRequest request, HttpServletResponse response) {
	
		int amount = Integer.valueOf(payOverBo.getPayAmt()).intValue();
		try {
			Order order = orderService.payOver(payOverBo.getPhTradeNo(), amount, payOverBo.getPayMode(), payOverBo.getTPTradeNo());
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("InvoiceNO", order.getOrderno());
			JXResp resp = new JXResp(map);
			return resp;
		} catch (HandleException e) {
			JXResp resp = new JXResp("-9","该订单已被支付");
			return resp;
		} catch (Exception e) {
			JXResp resp = new JXResp("-8","系统异常");
			return resp;
		}
	}
}
