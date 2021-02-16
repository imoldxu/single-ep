
package com.yyg.eprescription.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyg.eprescription.entity.PrescriptionDrugs;
import com.yyg.eprescription.entity.SalesRecord;
import com.yyg.eprescription.mapper.SalesRecordMapper;
import com.yyg.eprescription.util.DateUtils;
import com.yyg.eprescription.util.MoneyUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class SalesRecordService {

	@Autowired
	SalesRecordMapper recordMapper;
	@Autowired
	OrderService orderService;
	
	public List<SalesRecord> getRecord(int pageIndex, int maxSize){
		Example ex = new Example(SalesRecord.class);
		ex.setOrderByClause("id DESC");
		RowBounds rowBounds = new RowBounds((pageIndex-1)*maxSize, maxSize);
		List<SalesRecord> list = recordMapper.selectByExampleAndRowBounds(ex, rowBounds);
		return list;
	}
	
//	@Transactional
//	public void createByStore(Integer storeid, Order order){
//		
//		String transactionInfo =  order.getInfo();
//		List<TransactionDrug> transactionList = null;
//		try{
//			transactionList = JSONUtils.getObjectListByJson(transactionInfo, TransactionDrug.class);
//		}catch (Exception e) {
//			throw new HandleException(ErrorCode.DATA_ERROR, "内部数据异常");
//		}
//		List<SalesRecord> recordList = new ArrayList<SalesRecord>();
//		for(TransactionDrug transdrug : transactionList){
//			SalesRecord record = new SalesRecord();
//			
//			record.setCreatetime(new Date());
//			
//			record.setDoctorname(transdrug.getDoctorname());
//			record.setDoctorid(transdrug.getDoctorid());
//			
//			record.setDrugid(transdrug.getDrugid());
//			record.setDrugname(transdrug.getDrugname());
//			
//			record.setExid(transdrug.getExid());
//			
//			record.setHospitalid(transdrug.getHospitalid());
//			record.setHospitalname(transdrug.getHospitalname());
//			
//			record.setOrderid(order.getId());
//			record.setPrescriptionid(transdrug.getPrescriptionid());
//			
//			Store store = userClient.getStore(storeid).fetchOKData(Store.class);
//			record.setStorename(store.getName());
//			record.setNum(transdrug.getNum());
//			double rate = store.getRate();
//			
//			//FIXME 采用费率计算出结算价格，而没有采用单个药品的结算费用，此方案在调整费率时比较容易实现，采用单个药品的结算费用更灵活多变
//			StoreDrug storeDrug = drugClient.getDrugByStore(storeid, transdrug.getDrugid()).fetchOKData(StoreDrug.class);
//			record.setPrice(storeDrug.getPrice());
//			record.setSettlementprice(getInt(storeDrug.getPrice()*rate));
//			record.setTotalsettlementprice(record.getSettlementprice()*transdrug.getNum());
//			record.setStoreid(storeid);
//			record.setRefundnum(0);
//			recordList.add(record);
//		}
//
//		recordMapper.insertList(recordList);
//			
//		accountService.settleSalesRecords(recordList);//结算交易
//	}

	private int getInt(double number){
	    BigDecimal bd=new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
	    return Integer.parseInt(bd.toString()); 
	}
	
	/**
	 * 创建领药记录
	 * @param transactionInfo
	 */
	@Transactional
	public void create(Long orderid, String transactionInfo) {
		
		List<PrescriptionDrugs> transactionList = null;
		try {
			transactionList = JSON.parseArray(transactionInfo, PrescriptionDrugs.class);
		} catch (Exception e) {
			// FIXME: 此处不应该出现问题
			e.printStackTrace();
		} 
		
		List<SalesRecord> recordList = new ArrayList<SalesRecord>();
		for(PrescriptionDrugs transdrug : transactionList){
			SalesRecord record = new SalesRecord();
			
			record.setCreatetime(new Date());
					
			record.setDrugname(transdrug.getDrugname());
			
			record.setOrderid(orderid);
			record.setPrescriptionid(transdrug.getPrescriptionid());
			
			record.setNum(transdrug.getNumber());
			
			recordList.add(record);
		}

		recordMapper.insertList(recordList);
	}
	
//	public List<SalesRecord> getRecordBySeller(Integer sellerid, String doctorName, String startDate, String endDate, int pageIndex, int pageSize) {
//		if(startDate==null || startDate.isEmpty()){
//			startDate = "1970-1-1";
//		}else{
//			startDate = DateUtils.UTCStringtODefaultString(startDate);
//		}
//		if(endDate == null || endDate.isEmpty()){
//			endDate = "2099-12-31";
//		}else{
//			endDate = DateUtils.UTCStringtODefaultString(endDate);
//		}
//		Example ex = new Example(SalesRecord.class);
//		if(doctorName==null || doctorName.isEmpty()){
//			ex.createCriteria().andEqualTo("sellerid", sellerid).andBetween("createtime", startDate, endDate);	
//		}else{
//			ex.createCriteria().andEqualTo("sellerid", sellerid).andBetween("createtime", startDate, endDate).andEqualTo("doctorname", doctorName);	
//		}
//		ex.setOrderByClause("id DESC");
//		RowBounds rowBounds = new RowBounds((pageIndex-1)*pageSize, pageSize);
//		List<SalesRecord> list = recordMapper.selectByExampleAndRowBounds(ex, rowBounds);
//		return list;
//	}

//	public List<SalesRecord> getRecords(String startDate, String endDate, int pageIndex, int pageSize) {
//		Example ex = new Example(SalesRecord.class);
//		if(startDate==null || startDate.isEmpty()){
//			startDate = "1970-01-01 00:00:00";
//		}else{
//			startDate = DateUtils.UTCStringtODefaultString(startDate)+" 00:00:00";
//		}
//		if(endDate==null || endDate.isEmpty()){
//			endDate = "2099-12-31 24:00:00";
//		}else{
//			endDate = DateUtils.UTCStringtODefaultString(endDate)+" 24:00:00";
//		}
//		ex.createCriteria().andGreaterThanOrEqualTo("createtime", startDate+" 00:00:00").andLessThanOrEqualTo("createtime", endDate+" 24:00:00");
//		ex.setOrderByClause("id DESC");
//		RowBounds rowBounds = new RowBounds((pageIndex-1)*pageSize, pageSize);
//		List<SalesRecord> list = recordMapper.selectByExampleAndRowBounds(ex, rowBounds);
//		return list;
//	}

	/**
	 * 退货
	 * @param storeid
	 * @param pid
	 * @param refundDrugs
	 */
	@Transactional
	public void refund(Long pid, List<PrescriptionDrugs> refundDrugs) {
		List<SalesRecord> refundRecordList = new ArrayList<SalesRecord>();
		
		for(PrescriptionDrugs refundDrug : refundDrugs) {
			int refundNum = refundDrug.getNumber();
			//List<SalesRecord> toRefundRecordList = recordMapper.getSalesRecordForRefund(refundDrug.getDrugid(), pid);
//			for(SalesRecord toRefundRecord:toRefundRecordList) {
//				int canRefundNum = toRefundRecord.getNum()-toRefundRecord.getRefundnum();
//				if(canRefundNum>=refundNum) {					
//					toRefundRecord.setRefundnum(toRefundRecord.getRefundnum()+refundNum);
//					recordMapper.updateByPrimaryKey(toRefundRecord);
//					
//					SalesRecord refundRecord = new SalesRecord();
//					refundRecord.setCreatetime(new Date());
//					refundRecord.setDrugid(toRefundRecord.getDrugid());
//					refundRecord.setDrugname(toRefundRecord.getDrugname());
//					refundRecord.setNum(0-refundNum);
//					refundRecord.setOrderid(null);
//					refundRecord.setPrescriptionid(toRefundRecord.getPrescriptionid());
//					refundRecord.setPrice(toRefundRecord.getPrice());
//					refundRecord.setRefundnum(0);
//					recordMapper.insert(refundRecord);
//					
//					refundRecordList.add(refundRecord);
//					break;
//				}else {
//					refundNum = refundNum - canRefundNum;
//					
//					toRefundRecord.setRefundnum(toRefundRecord.getRefundnum()+canRefundNum);
//					recordMapper.updateByPrimaryKey(toRefundRecord);
//					
//					SalesRecord refundRecord = new SalesRecord();
//					refundRecord.setCreatetime(new Date());
//					refundRecord.setDrugid(toRefundRecord.getDrugid());
//					refundRecord.setDrugname(toRefundRecord.getDrugname());
//					refundRecord.setNum(0-canRefundNum);
//					refundRecord.setOrderid(null);
//					refundRecord.setPrescriptionid(toRefundRecord.getPrescriptionid());
//					refundRecord.setPrice(toRefundRecord.getPrice());
//					refundRecord.setRefundnum(0);
//					recordMapper.insert(refundRecord);
//					
//					refundRecordList.add(refundRecord);
//				}
//			}
		}
		
//		orderService.createRefundOrder(regno, amount, transactionList);
	}

//	public JSONArray getDaysIncome(Date now, int size) {
//		JSONArray ret = new JSONArray();
//		for(int i=size, j=0; i>=1; i--,j++) {
//			String day = null;
//			if(j==0) {
//				day = DateUtils.formatDate(now);
//			}else {
//				day = DateUtils.formatDate(DateUtils.reduceDays(now, j));
//			}
//			Integer income = recordMapper.getIncomeByDay(day);
//			if(income == null) {
//				income = 0;
//			}
//			
//			JSONArray temp = new JSONArray();
//			temp.add(i);
//			temp.add(MoneyUtil.amountF2Y(income));
//			ret.add(temp);
//		}
//		return ret;
//	}

//	public JSONArray getMonthsIncome(Date date, int size) {
//		JSONArray ret = new JSONArray();
//		
//		for(int i=size, j=0; i>=1; i--,j++) {
//			int month;
//			int year;
//			if(j==0) {
//				month = DateUtils.getMonth(date);
//				year = DateUtils.getYear(date);
//			}else {
//				Date tempDate = DateUtils.addMonths(date, 0-j);
//				month = DateUtils.getMonth(tempDate);
//				year = DateUtils.getYear(tempDate);
//			}
//			Integer income = recordMapper.getIncomeByMonth(month, year);
//			if(income == null) {
//				income = 0;
//			}
//			JSONArray temp = new JSONArray();
//			temp.add(i);
//			temp.add(MoneyUtil.amountF2Y(income));
//			ret.add(temp);
//		}
//		return ret;
//	}
	
//	public List<PieData> getSalesByDrug(Date startDate,Date endDate, int size){
//		
//		String startTime = DateUtils.formatDate(startDate)+" 00:00:00";
//		String endTime = DateUtils.formatDate(endDate)+" 24:00:00";
//		
//		List<PieData> ret = recordMapper.getSalesByDrug(startTime, endTime, size);
//		return ret;
//	}
	
//	public List<PieData> getSalesByStore(Date startDate,Date endDate, int size){
//		
//		String startTime = DateUtils.formatDate(startDate)+" 00:00:00";
//		String endTime = DateUtils.formatDate(endDate)+" 24:00:00";
//		
//		List<PieData> ret = recordMapper.getSalesByStore(startTime, endTime, size);
//		return ret;
//	}
}
