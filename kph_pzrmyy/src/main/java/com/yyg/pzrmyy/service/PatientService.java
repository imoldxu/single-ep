package com.yyg.pzrmyy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyg.pzrmyy.entity.User;
import com.yyg.pzrmyy.mapper.UserMapper;

@Service
public class PatientService {

	@Autowired
	UserMapper userMapper;
	
	public User getDiagnosisByCardNo(String cardNo) throws Exception {
		List<User> ret = userMapper.getUserByCardNo(cardNo);
		if(ret.isEmpty()) {
			throw new Exception("卡号没有对应的患者");
		}else {
			return ret.get(0);
		}
	}
	
}
