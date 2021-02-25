package com.yyg.pzrmyy.mapper;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.pzrmyy.entity.Patient;

public interface PatientMapper extends BaseMapper<Patient> {

	public Patient getPatientByCardNo(@Param(value="cardNo")String cardNo);

}
