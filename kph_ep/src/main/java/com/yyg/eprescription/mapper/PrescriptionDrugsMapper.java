package com.yyg.eprescription.mapper;

//import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.entity.PrescriptionDrugs;

@Repository("prescriptionDrugsMapper")
//@Mapper
public interface PrescriptionDrugsMapper extends BaseMapper<PrescriptionDrugs> {
   
}
