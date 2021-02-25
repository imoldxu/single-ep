package com.yyg.eprescription.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.AddUserBo;
import com.yyg.eprescription.bo.ModifyPasswordBo;
import com.yyg.eprescription.bo.UserQuery;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.Role;
import com.yyg.eprescription.entity.User;
import com.yyg.eprescription.entity.UserRole;
import com.yyg.eprescription.mapper.RoleMapper;
import com.yyg.eprescription.mapper.UserMapper;
import com.yyg.eprescription.mapper.UserRoleMapper;

import tk.mybatis.mapper.entity.Example;

/**
 * 用户、角色管理
 * @author 老徐
 *
 */
@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	RoleMapper roleMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	UserRoleMapper userRoleMapper;

	public User login(String phone, String password) {
		User user = userMapper.selectUserByPhone(phone);
		if(user == null){
			throw new HandleException(ErrorCode.LOGIN_ERROR, "用户不存在");
		}else{
			if(user.getState() == User.STATE_VALID) {
				if(user.getPassword().equals(password)){
					return user;
				}else{
					throw new HandleException(ErrorCode.LOGIN_ERROR, "密码错误");
				}
			}else {
				throw new HandleException(ErrorCode.LOGIN_ERROR, "用户已被停用");
			}
		}
	}

	@Transactional
	public void register(AddUserBo addUserBo) {
		String name = addUserBo.getName();
		String phone =addUserBo.getPhone();
		String password = "7dd75c55c0f3a84969cacc5fcdbbd980";//md5("123456"+"x")->hex 
		//List<Integer> roleIds = addUserBo.getRoleIds();
		String roleName = addUserBo.getRole();
		
		Example ex = new Example(User.class);
		ex.createCriteria().andEqualTo("phone", phone);
		User user = userMapper.selectOneByExample(ex);
		if(user != null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "手机号重复、用户已存在");
		}else{
			user = new User();
			user.setName(name);
			user.setPhone(phone);
			user.setPassword(password);
			user.setState(User.STATE_VALID);
			userMapper.insertUseGeneratedKeys(user);
			
			Role role = roleMapper.getRoleByName(roleName);
			if(role==null) {
				throw new HandleException(ErrorCode.ARG_ERROR, "角色名称错误，没有对应的角色");
			}
			UserRole userRole = new UserRole();
			userRole.setRid(role.getId());
			userRole.setUid(user.getId());
			userRoleMapper.insert(userRole);
			
//			List<UserRole> userRoles = roleIds.stream().map(roleId->{
//				UserRole userRole = new UserRole();
//				userRole.setRid(roleId);
//				userRole.setUid(uid);
//				return userRole;
//			}).collect(Collectors.toList());			
//			userRoleMapper.insertList(userRoles);
		}
	}

	@Transactional
	public void updateRole(Integer uid, Set<Role> roles) {
	
		userRoleMapper.deleteAllRoleByUser(uid);
		
		List<UserRole> userRoleList = new ArrayList<UserRole>();
		roles.forEach(role->{
			UserRole userRole = new UserRole();
			userRole.setRid(role.getId());
			userRole.setUid(uid);
			userRoleList.add(userRole);
		});
		
		userRoleMapper.insertList(userRoleList);
	}
	
	public void addRole(String name) {
		Role role = roleMapper.getRoleByName(name);
		if(role != null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "角色已存在");
		}else{
			role = new Role();
			role.setName(name);
			roleMapper.insertUseGeneratedKeys(role);
		}
	}

	public List<Role> listRole() {
		return roleMapper.selectAll();
	}

	@Transactional
	public User updateUser(User user) {
		
		userMapper.updateByPrimaryKey(user);
		
		Set<Role> roles = user.getRoles();
		
		updateRole(user.getId(), roles);
		
		return user;
	}

	public User getUserById(Integer uid) {
		User user = userMapper.selectUserById(uid);
		return user;
	}

	public void modifyPassword(User user, @Valid ModifyPasswordBo modifyPasswordBo) {
		User dbUser = getUserById(user.getId());
		if(dbUser.getPassword().equals(modifyPasswordBo.getOldPassword())) {
			dbUser.setPassword(modifyPasswordBo.getNewPassword());
			userMapper.updateByPrimaryKey(dbUser);
		}else {
			throw new HandleException(ErrorCode.NORMAL_ERROR, "旧密码错误");
		}
	}
	
	public void resetPassword(@NotBlank String phone) {
		User user = userMapper.selectUserByPhone(phone);
		user.setPassword("7dd75c55c0f3a84969cacc5fcdbbd980");
		userMapper.updateByPrimaryKey(user);
	}
	
	public void validUser(@NotBlank String phone) {
		User user = userMapper.selectUserByPhone(phone);
		user.setState(User.STATE_VALID);
		userMapper.updateByPrimaryKey(user);
	}
	
	public void invalidUser(@NotBlank String phone) {
		User user = userMapper.selectUserByPhone(phone);
		user.setState(User.STATE_INVALID);
		userMapper.updateByPrimaryKey(user);
	}

	public PageResult<User> query(UserQuery query) {
		
		List<List<?>> sqlResult = userMapper.queryUser(query);
		
		PageResult<User> result = PageResult.buildPageResult(sqlResult, User.class);
		
		return result;
	}

	
}
