package com.yyg.eprescription.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yyg.eprescription.entity.Drug;
import com.yyg.eprescription.mapper.DrugMapper;
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
	
	public List<Drug> queryDrugInfoByKeys(String keys) {
		List<Drug> ret = new ArrayList<Drug>();

		keys = keys.toUpperCase();
		
		Example ex = new Example(Drug.class);
		ex.or().andLike("drugname", "%"+keys+"%");
		ex.or().andLike("fullkeys", "%"+keys+"%");
		ex.or().andLike("shortnamekeys", "%"+keys+"%");
		ret = drugMapper.selectByExample(ex);
		
		return ret;
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
		// TODO Auto-generated method stub
		drugMapper.insertList(drugList);	
	}
	
	public int updateDrug(Drug drug) {
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
	
	public int downDrug(int drugid) {
		Drug drug = drugMapper.selectByPrimaryKey(drugid);
		drug.setState(Drug.STATE_EMPTY);
		int opRet = drugMapper.updateByPrimaryKey(drug);
		return opRet;
	}
	
	public int upDrug(int drugid) {
		Drug drug = drugMapper.selectByPrimaryKey(drugid);
		drug.setState(Drug.STATE_OK);
		int opRet = drugMapper.updateByPrimaryKey(drug);
		return opRet;
	}
}
