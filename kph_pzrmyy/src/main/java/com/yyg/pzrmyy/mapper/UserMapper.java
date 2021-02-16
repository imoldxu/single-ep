package com.yyg.pzrmyy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.x.commons.mybatis.BaseMapper;
import com.yyg.pzrmyy.entity.User;

public interface UserMapper extends BaseMapper<User> {

	public List<User> getUserByCardNo(@Param(value="cardNo")String cardNo);

}
