package com.yyg.eprescription.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.AddUserBo;
import com.yyg.eprescription.bo.LoginBo;
import com.yyg.eprescription.bo.ModifyPasswordBo;
import com.yyg.eprescription.bo.ModifyUserBo;
import com.yyg.eprescription.bo.UserQuery;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.entity.Role;
import com.yyg.eprescription.entity.User;
import com.yyg.eprescription.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
@Api("用户接口")
public class UserController {

	@Autowired
	UserService userService;
	
	@RequiresRoles({"admin"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(value = "用户注册账户", notes = "用户注册接口")
	public void register(@ApiParam(name="addUserBo",value="添加用户信息") @Valid @RequestBody AddUserBo addUserBo) {
		userService.register(addUserBo);	
		return;
	}
	
	@RequiresRoles({"admin"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(path="/queryUser", method = RequestMethod.GET)
	@ApiOperation(value = "查询用户", notes = "查询用户")
	public PageResult<User> query(@ApiParam(name="query",value="查询") @Valid UserQuery query) {
		
		PageResult<User> result = userService.query(query);
		
		return result;
	}
	
	@RequiresRoles(value={"admin","manager","tollman"}, logical = Logical.OR)
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(path="/password", method = RequestMethod.PUT)
	@ApiOperation(value = "修改密码", notes = "修改密码")
	public void modify(@ApiParam(name="addUserBo",value="添加用户信息") @Valid @RequestBody ModifyPasswordBo modifyPasswordBo) {
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		userService.modifyPassword(user, modifyPasswordBo);	
		return;
	}
	
	@RequiresRoles({"admin"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(method = RequestMethod.PUT)
	@ApiOperation(value = "更新用户信息", notes = "更新用户信息")
	public User updateUser(@ApiParam(name="user",value="用户") @RequestBody User user) {	
		user = userService.updateUser(user);
		return user;	
	}
	
	@RequiresRoles({"admin"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(path="/validUser", method = RequestMethod.PUT)
	@ApiOperation(value = "启用用户", notes = "启用用户")
	public void validUser(@ApiParam(name="modifyUserbo",value="用户") @RequestBody ModifyUserBo modifyUserbo) {	
		userService.validUser(modifyUserbo.getPhone());
		return;	
	}
	
	@RequiresRoles({"admin"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(path="/invalidUser", method = RequestMethod.PUT)
	@ApiOperation(value = "停用用户", notes = "停用用户")
	public void invalidUser(@ApiParam(name="modifyUserbo",value="用户") @RequestBody ModifyUserBo modifyUserbo) {	
		userService.invalidUser(modifyUserbo.getPhone());
		return;	
	}
	
	@RequiresRoles({"admin"})
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(path="/resetPassword", method = RequestMethod.PUT)
	@ApiOperation(value = "重置用户密码", notes = "重置用户密码")
	public void resetPassword(@ApiParam(name="modifyUserbo",value="重置用户密码") @RequestBody ModifyUserBo modifyUserbo) {	
		userService.resetPassword(modifyUserbo.getPhone());
		return;	
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "用户信息", notes = "用户信息")
	public User get() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		if(user == null) {
			throw new HandleException(ErrorCode.UNLOGIN, "还未登陆");
		}
		return user;
	}
	
//	@RequiresRoles({"manager"})
//	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
//	@RequestMapping(value = "/role", method = RequestMethod.POST)
//	@ApiOperation(value = "添加角色", notes = "添加角色")
//	public void addRole(@ApiParam(name="name",value="角色名称") @RequestParam(name="name") String name) {
//		userService.addRole(name);
//		return;
//	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	@ApiOperation(value = "查询角色", notes = "查询角色")
	public List<Role> listRole() {
		List<Role> roleList = userService.listRole();
		return roleList;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/session", method = RequestMethod.POST)
	@ApiOperation(value = "用户登陆", notes = "用户登陆接口")
	public User login(@ApiParam(name="loginBo",value="登陆信息") @Valid @RequestBody LoginBo loginBo) {
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setTimeout(8*3600*1000);//8个小时过期,避免收费处重复登录
		AuthenticationToken token = new UsernamePasswordToken(loginBo.getPhone(), loginBo.getPassword());
		subject.login(token);
		User user = (User) subject.getPrincipal();
		return user;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/session", method = RequestMethod.DELETE)
	@ApiOperation(value = "用户登出", notes = "用户登出接口")
	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
	}
}
