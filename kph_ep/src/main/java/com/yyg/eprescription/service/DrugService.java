package com.yyg.eprescription.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.DrugQuery;
import com.yyg.eprescription.bo.ModifyStockBo;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.Drug;
import com.yyg.eprescription.mapper.DrugMapper;
import com.yyg.eprescription.util.ChineseCharacterUtil;
import com.yyg.eprescription.vo.ShortDrugInfo;

@Service
public class DrugService {

	@Autowired
	DrugMapper drugMapper;
	@Autowired
	DoctorDrugService doctorDrugService;
	
	@Cacheable(cacheNames="simpleDrugListByKey")
	public List<ShortDrugInfo> queryDrugsByKeys(int type, String keys) {
		List<ShortDrugInfo> ret = new ArrayList<ShortDrugInfo>();
		
		if(type == 1){
			keys = keys.toUpperCase();
			List<ShortDrugInfo> matchList = drugMapper.getDrugsByKeys(keys);
			if(matchList.isEmpty()){
				ret = drugMapper.getDrugsByKeys("%"+keys+"%");	
			}else{
				ret.addAll(matchList);
				Integer myid = matchList.get(0).getId();
				List<ShortDrugInfo> druglist = drugMapper.getDrugsByKeysWithoutID(myid, "%"+keys+"%");
				ret.addAll(druglist);
			}
		}else{
			keys = keys.toUpperCase();
			List<ShortDrugInfo> matchList = drugMapper.getZyDrugsByKeys(keys);
			if(matchList.isEmpty()){
				ret = drugMapper.getZyDrugsByKeys("%"+keys+"%");	
			}else{
				ret.addAll(matchList);
				Integer myid = matchList.get(0).getId();
				List<ShortDrugInfo> druglist = drugMapper.getZyDrugsByKeysWithoutID(myid, "%"+keys+"%");
				ret.addAll(druglist);
			}
		}
		return ret;
	}
	
	public PageResult<Drug> queryDrugInfoByKeys(DrugQuery query) {
		
		if(!StringUtils.isEmpty(query.getKeys())) {
			query.setKeys("%"+query.getKeys()+"%");
		}
		
		List<List<?>> sqlResult = drugMapper.queryDrugWithTotal(query);
		
		PageResult<Drug> result = PageResult.buildPageResult(sqlResult, Drug.class);
		
		return result;
	}
	
	@Cacheable(cacheNames="simpleDrugListByCategory")
	public List<ShortDrugInfo> queryDrugByCategory(int type, String category) {
		List<ShortDrugInfo> ret = null;
		if(type==1){
			ret = drugMapper.getDrugBySubCategory(category);
		}else{
			ret = drugMapper.getZyDrugBySubCategory(category);	
		}
		return ret;
	}
	
	@Cacheable(cacheNames="drug")
	public Drug getDrugById(Integer drugid) {
		Drug drug = drugMapper.selectByPrimaryKey(drugid);
		return drug;
	}

	@CacheEvict(cacheNames={"simpleDrugListByKey","simpleDrugListByCategory"}, allEntries=true)
	public void insertList(List<Drug> drugList) {
		drugMapper.insertList(drugList);	
	}
	
	@Caching(
        put = {
        	@CachePut(cacheNames="drug", key="#result.id")
        },
        evict = {
        	@CacheEvict(cacheNames={"simpleDrugListByKey","simpleDrugListByCategory"}, allEntries=true)	
        }
    )
	public Drug addDrug(Drug drug) {
		drug.setFullkeys(ChineseCharacterUtil.convertHanzi2Pinyin(drug.getDrugname(), false));
		drug.setShortnamekeys(ChineseCharacterUtil.convertHanzi2Pinyin(drug.getShortname(), false));;
		drug.setState(Drug.STATE_OK);//有库存药品可见
		drugMapper.insertUseGeneratedKeys(drug);
		return drug;
	}
	
	
	@Caching(
        put = {
        	@CachePut(cacheNames="drug", key="#drug.id")
        },
        evict = {
        	@CacheEvict(cacheNames={"simpleDrugListByKey","simpleDrugListByCategory","simpleDrugListByDoctor"}, allEntries=true)	
        }
    )
	@Transactional
	public Drug updateDrug(Drug drug) {
		Drug dbDrug = drugMapper.selectDrugForUpdate(drug.getId());
		drug.setStock(dbDrug.getStock());
		drug.setState(dbDrug.getState());//沿用原来数据库存储的药品状态
		drugMapper.updateByPrimaryKey(drug);
		return drug; 
	}
	
//	@Transactional
//	public int deleteDrug(int drugid) {
//		int opRet = drugMapper.deleteByPrimaryKey(drugid);
//		if(opRet!=0) {
//			doctorDrugService.delete(drugid);
//		}
//		return opRet;
//	}
	
	@Caching(
        put = {
        	@CachePut(cacheNames="drug")
        },
        evict = {
        	@CacheEvict(cacheNames={"simpleDrugListByKey","simpleDrugListByCategory","simpleDrugListByDoctor"}, allEntries=true)	
        }
    )
	@Transactional
	public Drug downDrug(Integer drugid) {
		Drug drug = drugMapper.selectDrugForUpdate(drugid);
		drug.setState(Drug.STATE_EMPTY);
		drugMapper.updateByPrimaryKey(drug);
		return drug;
	}
	
	@Caching(
        put = {
        	@CachePut(cacheNames="drug")
        },
        evict = {
        	@CacheEvict(cacheNames={"simpleDrugListByKey","simpleDrugListByCategory","simpleDrugListByDoctor"}, allEntries=true)	
        }
    )
	@Transactional
	public Drug upDrug(Integer drugid) {
		Drug drug = drugMapper.selectDrugForUpdate(drugid);
		drug.setState(Drug.STATE_OK);
		drugMapper.updateByPrimaryKey(drug);
		return drug;
	}
	
	/**
	   *  在领药或退药时修改库存，库存可以减少为负数，负数之后无法给患者药品，则需要走退货流程，退货会将库存又补回来
	 * @param drugid
	 * @param modifyNumber
	 */
	@CachePut(cacheNames="drug", key="#drugid")
	@Transactional
	public Drug modifyStock(Integer drugid, Integer modifyNumber) {
		Drug drug = drugMapper.selectDrugForUpdate(drugid);
		Integer stock = drug.getStock();
		drug.setStock(stock-modifyNumber);//--相当于增加，正数为减少
		drugMapper.updateByPrimaryKey(drug);
		return drug;
	}

    @CachePut(cacheNames="drug", key="#modifyStockBo.id")
    @Transactional
	public Drug saveStock(ModifyStockBo modifyStockBo) {
		Drug drug = drugMapper.selectDrugForUpdate(modifyStockBo.getId());
		drug.setStock(modifyStockBo.getStock());
		drugMapper.updateByPrimaryKey(drug);
		return drug;
	}
}
