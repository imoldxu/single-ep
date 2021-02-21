package com.yyg.eprescription.mapper;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.eprescription.entity.User;

public interface UserMapper extends BaseMapper<User>{

	public User selectUserByPhone(@Param(value = "phone") String phone);
	
	public User selectUserById(@Param(value = "uid") Integer uid);
}
