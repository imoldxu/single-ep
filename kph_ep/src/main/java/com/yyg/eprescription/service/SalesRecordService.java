
package com.yyg.eprescription.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.RefundInfo;
import com.yyg.eprescription.bo.SaleRecordQuery;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.Drug;
import com.yyg.eprescription.entity.PrescriptionDrugs;
import com.yyg.eprescription.entity.SalesRecord;
import com.yyg.eprescription.mapper.SalesRecordMapper;
import com.yyg.eprescription.vo.SalesRecordStatisticVo;
import com.yyg.eprescription.vo.SalesRecordVo;


@Service
public class SalesRecordService {

	@Autowired
	SalesRecordMapper recordMapper;
	@Autowired
	DrugService drugService;
	/**
	 * 分页查询
	 * @param query
	 * @return
	 */
	public PageResult<SalesRecordVo> query(SaleRecordQuery query){
		
		List<List<?>> sqlResult = recordMapper.querySalesRecordWithTotal(query);		
		PageResult<SalesRecordVo> result = PageResult.buildPageResult(sqlResult, SalesRecordVo.class);	
		return result;
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
			e.printStackTrace();
		} 
		
		List<SalesRecord> recordList = new ArrayList<SalesRecord>();
		for(PrescriptionDrugs transdrug : transactionList){
			SalesRecord record = new SalesRecord();
			
			Drug drug = drugService.getDrugById(transdrug.getDrugid());
			
			record.setCreatetime(new Date());
			record.setDrugid(transdrug.getDrugid());		
			record.setDrugname(transdrug.getDrugname());
			record.setNum(transdrug.getNumber());
			record.setOrderid(orderid);
			record.setStandard(transdrug.getStandard());
			record.setDrugunit(transdrug.getUnit());
			record.setDrugcompany(drug.getCompany());
			record.setPrescriptionid(transdrug.getPrescriptionid());
			record.setPrice(transdrug.getPrice());
			record.setRefundnum(0);
			recordList.add(record);
		}

		recordMapper.insertList(recordList);
	}

	/**
	 * 退货
	 * @param storeid
	 * @param pid
	 * @param refundDrugs
	 */
	@Transactional
	public List<SalesRecord> refund(List<RefundInfo> refundDrugs) {
		List<SalesRecord> refundList = new ArrayList<SalesRecord>();
		
		refundDrugs.forEach(refundDrug->{
			int toRefund = refundDrug.getToRefund();
			Long recordid = refundDrug.getRecordid();
			SalesRecord record = recordMapper.selectByPrimaryKey(recordid);
			
			Integer num = record.getNum();
			Integer refundnum = record.getRefundnum();
			if(num-refundnum<toRefund) {
				throw new HandleException(ErrorCode.ARG_ERROR, "退货数量不能超过销售数量");
			}
			
			record.setRefundnum(refundnum+toRefund);
			recordMapper.updateByPrimaryKey(record);
			
			SalesRecord refundRecord = new SalesRecord();
			refundRecord.setDrugid(record.getDrugid());
			refundRecord.setDrugname(record.getDrugname());
			refundRecord.setDrugunit(record.getDrugunit());
			refundRecord.setDrugcompany(record.getDrugcompany());
			refundRecord.setNum(toRefund);
			refundRecord.setPrescriptionid(record.getPrescriptionid());
			refundRecord.setPrice(record.getPrice());
			refundRecord.setStandard(record.getStandard());
			refundList.add(record);
		});
		
		return refundList;
		
	}

	public PageResult<SalesRecordStatisticVo> statistic(SaleRecordQuery query){
		
		List<List<?>> sqlResult = recordMapper.statistic(query);
		
		PageResult<SalesRecordStatisticVo> result = PageResult.buildPageResult(sqlResult, SalesRecordStatisticVo.class);
		
		return  result;
	}
	
}
