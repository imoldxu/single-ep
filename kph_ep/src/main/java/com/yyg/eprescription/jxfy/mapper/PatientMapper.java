package com.yyg.eprescription.jxfy.mapper;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.jxfy.entity.Patient;

public interface PatientMapper extends BaseMapper<Patient> {

	public Patient getPatientByCardNo(@Param(value="cardNo")String cardNo);

}
