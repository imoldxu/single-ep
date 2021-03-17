package com.yyg.eprescription.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.jxfy.entity.Patient;
import com.yyg.eprescription.jxfy.mapper.PatientMapper;


@Service
public class PatientService {

	@Autowired
	PatientMapper patientMapper;
	
	public Patient getPatientByCardNo(String cardNo, String regNo) {
		Patient ret = patientMapper.getPatientByCardNo(cardNo, regNo);
		if(ret == null) {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "没有找到匹配的患者信息，请联系信息科");
		}
		return ret;
	}
	
}
