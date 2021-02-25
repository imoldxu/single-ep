package com.yyg.pzrmyy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyg.pzrmyy.entity.Patient;
import com.yyg.pzrmyy.mapper.PatientMapper;

@Service
public class PatientService {

	@Autowired
	PatientMapper patientMapper;
	
	public Patient getDiagnosisByCardNo(String cardNo) throws Exception {
		Patient ret = patientMapper.getPatientByCardNo(cardNo);
		if(ret==null) {
			throw new Exception("卡号没有对应的患者");
		}else {
			return ret;
		}
	}
	
}
