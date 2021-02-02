package com.yyg.eprescription.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yyg.eprescription.BaseMapper;
import com.yyg.eprescription.entity.DoctorDrugs;
import com.yyg.eprescription.entity.ShortDrugInfo;

public interface DoctorDrugsMapper extends BaseMapper<DoctorDrugs>{

	List<ShortDrugInfo> getDrugsByDoctor(@Param(value="dname")String dname, @Param(value="depart")String depart);
	 
    List<ShortDrugInfo> getZyDrugsByDoctor(@Param(value="dname")String dname, @Param(value="depart")String depart);
    
}
