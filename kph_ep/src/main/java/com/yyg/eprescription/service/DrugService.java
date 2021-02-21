package com.yyg.eprescription.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.DrugQuery;
import com.yyg.eprescription.entity.Drug;
import com.yyg.eprescription.entity.Prescription;
import com.yyg.eprescription.mapper.DrugMapper;
import com.yyg.eprescription.util.ChineseCharacterUtil;
import com.yyg.eprescription.vo.ShortDrugInfo;

import tk.mybatis.mapper.entity.Example;

@Service
public class DrugService {

	@Autowired
	DrugMapper drugMapper;
	@Autowired
	DoctorDrugService doctorDrugService;
	//DoctorDrugsMapper doctorDrugsMapper;
	
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
		
		Example ex = new Example(Drug.class);
		
		if(query.getKeys()!= null && !query.getKeys().isEmpty()) {
			String keys = query.getKeys();
			keys = keys.toUpperCase();
		
			ex.or().andLike("drugname", "%"+keys+"%");
			ex.or().andLike("fullkeys", "%"+keys+"%");
			ex.or().andLike("shortnamekeys", "%"+keys+"%");
		}
		ex.setOrderByClause("id Desc");
		int pageIndex = 1;
		if(query.getCurrent() != null) {
			pageIndex = query.getCurrent().intValue();
		}		
		int maxSize = 50;
		if(query.getPageSize() != null) {
			maxSize = query.getPageSize().intValue();
		}	
		RowBounds rowBounds = new RowBounds((pageIndex-1)*maxSize, maxSize);
		
		int total = drugMapper.selectCountByExample(ex);
		List<Drug> list = drugMapper.selectByExampleAndRowBounds(ex, rowBounds);
		
		PageResult<Drug> result = new PageResult<Drug>();
		result.setData(list);
		result.setTotal(total);
		result.setSuccess(true);
		
		return result;
	}
	
	public List<ShortDrugInfo> queryDrugByCategory(int type, String category) {
		List<ShortDrugInfo> ret = null;
		if(type==1){
			ret = drugMapper.getDrugBySubCategory(category);
		}else{
			ret = drugMapper.getZyDrugBySubCategory(category);	
		}
		return ret;
	}
	
	public Drug getDrugById(int drugid) {
		Drug drug = drugMapper.selectByPrimaryKey(drugid);
		return drug;
	}

	public void insertList(List<Drug> drugList) {
		drugMapper.insertList(drugList);	
	}
	
	public int addDrug(Drug drug) {
		drug.setFullkeys(ChineseCharacterUtil.convertHanzi2Pinyin(drug.getDrugname(), false));
		drug.setShortnamekeys(ChineseCharacterUtil.convertHanzi2Pinyin(drug.getShortname(), false));;
		drug.setState(Drug.STATE_OK);//缺省上传之后药品可见
		return drugMapper.insert(drug);
	}
	
	public int updateDrug(Drug drug) {
		Drug dbDrug = getDrugById(drug.getId());
		drug.setState(dbDrug.getState());
		return drugMapper.updateByPrimaryKey(drug);
	}
	
	@Transactional
	public int deleteDrug(int drugid) {
		int opRet = drugMapper.deleteByPrimaryKey(drugid);
		if(opRet!=0) {
			doctorDrugService.delete(drugid);
		}
		return opRet;
	}
	
	public int downDrug(Integer drugid) {
		Drug drug = drugMapper.selectByPrimaryKey(drugid);
		drug.setState(Drug.STATE_EMPTY);
		int opRet = drugMapper.updateByPrimaryKey(drug);
		return opRet;
	}
	
	public int upDrug(Integer drugid) {
		Drug drug = drugMapper.selectByPrimaryKey(drugid);
		drug.setState(Drug.STATE_OK);
		int opRet = drugMapper.updateByPrimaryKey(drug);
		return opRet;
	}
}
