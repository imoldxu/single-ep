package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.entity.Prescription;
import com.yyg.eprescription.vo.CountPrescriptionInfo;

@Repository("prescriptionMapper")
public interface PrescriptionMapper extends BaseMapper<Prescription> {
 
	public List<CountPrescriptionInfo> countPrescription(@Param(value="startdate")String startdate, @Param(value="enddate")String enddate);
}
