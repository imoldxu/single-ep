package com.yyg.eprescription.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yyg.eprescription.entity.DoctorDrugs;
import com.yyg.eprescription.mapper.DoctorDrugsMapper;
import com.yyg.eprescription.vo.ShortDrugInfo;

import tk.mybatis.mapper.entity.Example;

@Service
public class DoctorDrugService {

	@Autowired
	DoctorDrugsMapper doctorDrugsMapper;
	
	@CacheEvict(cacheNames="simpleDrugListByDoctor", key="#doctorName + #department")
	public void add(String doctorName,String department,int drugid) {
		Example dex = new Example(DoctorDrugs.class);
		dex.createCriteria().andEqualTo("drugid", drugid).andEqualTo("department", department).andEqualTo("doctorname", doctorName);
		List<DoctorDrugs> doctorDrugs = doctorDrugsMapper.selectByExample(dex);
		if(doctorDrugs.isEmpty()){
			DoctorDrugs doctorDrug = new DoctorDrugs();
			doctorDrug.setDepartment(department);
			doctorDrug.setDoctorname(doctorName);
			doctorDrug.setDrugid(drugid);
			doctorDrugsMapper.insert(doctorDrug);
		}
	}
	
	@Cacheable(cacheNames="simpleDrugListByDoctor", key="#doctorName + #department")
	public List<ShortDrugInfo> queryDrugByDoctor(int type, String doctorName, String department){
		List<ShortDrugInfo> ret = null;
		if(1 == type){
			ret = doctorDrugsMapper.getDrugsByDoctor(doctorName, department);
		}else{
			ret = doctorDrugsMapper.getZyDrugsByDoctor(doctorName, department);
		}
		return ret;
	}
	
//	public void delete(int drugid) {
//		Example ex = new Example(DoctorDrugs.class);
//		ex.createCriteria().andEqualTo("drugid", drugid);
//		doctorDrugsMapper.deleteByExample(ex);
//	}
	
}
