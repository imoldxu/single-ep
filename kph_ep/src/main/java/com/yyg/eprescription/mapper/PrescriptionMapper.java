package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.bo.PrescriptionQuery;
import com.yyg.eprescription.entity.Prescription;

@Repository("prescriptionMapper")
public interface PrescriptionMapper extends BaseMapper<Prescription> {
 
	public List<List<?>> queryPrescriptionWithTotal(@Param("query") PrescriptionQuery query); 

}
