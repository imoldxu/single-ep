package com.yyg.eprescription.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yyg.eprescription.context.Response;
import com.yyg.eprescription.entity.DoctorDrugs;
import com.yyg.eprescription.entity.Drug;
import com.yyg.eprescription.entity.ShortDrugInfo;
import com.yyg.eprescription.mapper.DoctorDrugsMapper;
import com.yyg.eprescription.mapper.DrugMapper;
import com.yyg.eprescription.util.ExcelUtils;
import com.yyg.eprescription.util.JSONUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.entity.Example;

@RestController
@RequestMapping(value = "/drug")
@Api(description = "药品接口")
public class DrugController {
	
	@Autowired
	DrugMapper drugMapper;
	@Autowired
	DoctorDrugsMapper doctorDrugsMapper;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDrugsByKeys", method = RequestMethod.GET)
	@ApiOperation(value = "根据药品的拼音首字母缩写或药品名称搜索药品", notes = "根据药品的拼音首字母缩写或药品名称搜索药品")
	public Response getDrugsByKeys(
			@ApiParam(name = "keys", value = "拼音首字母索引或药品名称") @RequestParam(name = "keys") String keys,
			@ApiParam(name = "type", value = "西药为1，中药为2") @RequestParam(name = "type") int type,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		
		List<ShortDrugInfo> ret = new ArrayList<ShortDrugInfo>();
		
		
		if(type == 1){
			keys = keys.toUpperCase();
			List<ShortDrugInfo> matchList = drugMapper.getDrugsByKeys(keys);
			if(matchList.isEmpty()){
				ret = drugMapper.getDrugsByKeys("%"+keys+"%");	
			}else{
				ret.addAll(matchList);
				Integer myid = matchList.get(0).getId();
				List<ShortDrugInfo> druglist = drugMapper.getDrugsByKeysWithoutID(myid, "%"+keys+"%");
				ret.addAll(druglist);
			}
		}else{
			keys = keys.toUpperCase();
			List<ShortDrugInfo> matchList = drugMapper.getZyDrugsByKeys(keys);
			if(matchList.isEmpty()){
				ret = drugMapper.getZyDrugsByKeys("%"+keys+"%");	
			}else{
				ret.addAll(matchList);
				Integer myid = matchList.get(0).getId();
				List<ShortDrugInfo> druglist = drugMapper.getZyDrugsByKeysWithoutID(myid, "%"+keys+"%");
				ret.addAll(druglist);
			}
		}
		Response resp = new Response(Response.SUCCESS, ret, Response.SUCCESS_MSG);
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDrugInfoListByKeys", method = RequestMethod.GET)
	@ApiOperation(value = "根据药品的拼音缩写搜索药品", notes = "根据药品的拼音缩写搜索药品")
	public Response getDrugInfoListByKeys(
			@ApiParam(name = "keys", value = "拼音首字母索引") @RequestParam(name = "keys") String keys,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		
		List<Drug> ret = new ArrayList<Drug>();

		keys = keys.toUpperCase();
		
		Example ex = new Example(Drug.class);
		ex.or().andLike("drugname", "%"+keys+"%");
		ex.or().andLike("fullkeys", "%"+keys+"%");
		ex.or().andLike("shortnamekeys", "%"+keys+"%");
		ret = drugMapper.selectByExample(ex);
		
		Response resp = new Response(Response.SUCCESS, ret, Response.SUCCESS_MSG);
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDrugInfoListByCategory", method = RequestMethod.GET)
	@ApiOperation(value = "根据分类搜索药品", notes = "根据分类搜索药品")
	public Response getDrugBySubCategory(
			@ApiParam(name = "category", value = "分类") @RequestParam(name = "category") String category,
			@ApiParam(name = "type", value = "西药为1，中药为2") @RequestParam(name = "type") int type,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		
		List<ShortDrugInfo> ret = null;
		if(type==1){
			ret = drugMapper.getDrugBySubCategory(category);
		}else{
			ret = drugMapper.getZyDrugBySubCategory(category);	
		}
		Response resp = new Response(Response.SUCCESS, ret, Response.SUCCESS_MSG);
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getMyDrugInfoList", method = RequestMethod.GET)
	@ApiOperation(value = "获得我的常用药品", notes = "获得我的常用药品")
	public Response getMyDrugInfoList(
			@ApiParam(name = "doctorname", value = "医生姓名") @RequestParam(name = "doctorname") String doctorname,
			@ApiParam(name = "department", value = "医生科室") @RequestParam(name = "department") String department,
			@ApiParam(name = "type", value = "西药为1，中药为2") @RequestParam(name = "type") int type,
			HttpServletRequest request, HttpServletResponse response) {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		
		if(doctorname == null || doctorname.isEmpty()){
			Response resp = new Response(Response.ERROR, null, "请输入医生姓名");
			return resp; 
		}
		if(department == null || department.isEmpty()){
			Response resp = new Response(Response.ERROR, null, "请输入医生科室");
			return resp; 
		}
		
		List<ShortDrugInfo> ret = null;
		if(1 == type){
			ret = doctorDrugsMapper.getDrugsByDoctor(doctorname, department);
			//ret = drugMapper.getCommonDrugByDoctor(doctorname, department);
		}else{
			ret = doctorDrugsMapper.getZyDrugsByDoctor(doctorname, department);
			//ret = drugMapper.getCommonZyDrugByDoctor(doctorname, department);
		}
		
		Response resp = new Response(Response.SUCCESS, ret, Response.SUCCESS_MSG);
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDrugByID", method = RequestMethod.GET)
	@ApiOperation(value = "获取药品信息", notes = "获取药品信息")
	public Response getDrugByID(
			@ApiParam(name = "drugid", value = "药品id") @RequestParam(name = "drugid") Integer drugid,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		
		Drug drug = drugMapper.selectByPrimaryKey(drugid);
		Response resp = null;
		if(drug==null){
			resp = new Response(Response.ERROR, null, "请求的药品不存在或已下架");
		}else{
			resp = new Response(Response.SUCCESS, drug, Response.SUCCESS_MSG);
		}
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/uploadByExcel", method = RequestMethod.POST)
	@ApiOperation(value = "新增上传药品信息", notes = "新增上传药品信息")
	public Response uploadByExcel(@RequestPart(value="file") MultipartFile file,HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		if(file==null){
			return new Response(Response.ERROR, null, "请求参数异常");
		}
		
		//获取文件名
	    String name=file.getOriginalFilename();
	    //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
	    long size=file.getSize();
	    if(name == null || ("").equals(name) && size==0){
	    	return new Response(Response.ERROR, null, "文件不存在或没有内容");
	    }
	    //批量导入。参数：文件名，文件。
	    ExcelUtils excelUtils = new ExcelUtils();
	    
	    Response resp = null;
	    
	    try{
	    	List<Drug> drugList = excelUtils.getExcelInfo(name, file);
	    	//drugMapper.insertDrugs(drugList);
			drugMapper.insertList(drugList);
	    	resp = new Response(Response.SUCCESS, drugList, Response.SUCCESS_MSG);    
	    }catch(IOException ioe){
	    	 resp = new Response(Response.ERROR,null, ioe.getMessage());
	    }catch (Exception e) {
	    	e.printStackTrace();
	        resp = new Response(Response.ERROR,null, "导入失败");
	    }

		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/modifyDrug", method = RequestMethod.POST)
	@ApiOperation(value = "修改药品信息", notes = "{     \"id\": 23,     \"drugname\": \"硫酸亚铁片\",     \"standard\": \"0.3g*60片\",     \"category\": \"OTC\",     \"price\": 38,     \"unit\": \"盒\",     \"form\": \"片剂\",     \"singledose\": \"1\",     \"doseunit\": \"片\",     \"defaultusage\": \"饭前\",     \"frequency\": \"一天三次\",     \"fullkeys\": \"LSYTP\",     \"shortnamekeys\": \"LSYTP\"   }")
	public Response modifyDrug(@RequestParam(value="drugInfo") String drugInfo,HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;
		
		Drug drug = JSONUtils.getObjectByJson(drugInfo, Drug.class);
		if (drug == null) {
			resp = new Response(Response.ERROR, null, "请求参数错误");
			return resp;
		}
		
		if(drug.getId() != null){
			int opRet = drugMapper.updateByPrimaryKey(drug);
			if(opRet == 0){
				resp = new Response(Response.ERROR, null, "修改失败");    
			}else{
				resp = new Response(Response.SUCCESS, drug, Response.SUCCESS_MSG);    
			}
		}else{
			resp = new Response(Response.ERROR, null, "药品不存在");
		}
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/delDrug", method = RequestMethod.POST)
	@ApiOperation(value = "删除药品信息", notes = "{     \"id\": 23,     \"drugname\": \"硫酸亚铁片\",     \"standard\": \"0.3g*60片\",     \"category\": \"OTC\",     \"price\": 38,     \"unit\": \"盒\",     \"form\": \"片剂\",     \"singledose\": \"1\",     \"doseunit\": \"片\",     \"defaultusage\": \"饭前\",     \"frequency\": \"一天三次\",     \"fullkeys\": \"LSYTP\",     \"shortnamekeys\": \"LSYTP\"   }")
	public Response delDrug(@RequestParam(value="drugid") int drugid, HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;			
		int optRet = drugMapper.deleteByPrimaryKey(drugid);
	
		if(optRet!=0){
			Example ex = new Example(DoctorDrugs.class);
			ex.createCriteria().andEqualTo("drugid", drugid);
			doctorDrugsMapper.deleteByExample(ex);
			
			resp = new Response(Response.SUCCESS, null, "删除成功");
		}else{
			resp = new Response(Response.ERROR, null, "药品不存在");
		}
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/downDrug", method = RequestMethod.POST)
	@ApiOperation(value = "下架药品", notes = "{     \"id\": 23,     \"drugname\": \"硫酸亚铁片\",     \"standard\": \"0.3g*60片\",     \"category\": \"OTC\",     \"price\": 38,     \"unit\": \"盒\",     \"form\": \"片剂\",     \"singledose\": \"1\",     \"doseunit\": \"片\",     \"defaultusage\": \"饭前\",     \"frequency\": \"一天三次\",     \"fullkeys\": \"LSYTP\",     \"shortnamekeys\": \"LSYTP\"   }")
	public Response downDrug(@RequestParam(value="drugid") int drugid, HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;	
		//int optRet = drugMapper.deleteByPrimaryKey(drugid);
		Drug drug = drugMapper.selectByPrimaryKey(drugid);
		drug.setState(Drug.STATE_EMPTY);
		int optRet = drugMapper.updateByPrimaryKey(drug);
		if(optRet!=0){
			resp = new Response(Response.SUCCESS, null, "删除成功");
		}else{
			resp = new Response(Response.ERROR, null, "药品不存在");
		}
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/upDrug", method = RequestMethod.POST)
	@ApiOperation(value = "上架药品信息", notes = "上架药品信息")
	public Response upDrug(@RequestParam(value="drugid") int drugid, HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;			
		//int optRet = drugMapper.deleteByPrimaryKey(drugid);
		Drug drug = drugMapper.selectByPrimaryKey(drugid);
		drug.setState(Drug.STATE_OK);
		int optRet = drugMapper.updateByPrimaryKey(drug);
		if(optRet!=0){
			resp = new Response(Response.SUCCESS, null, "删除成功");
		}else{
			resp = new Response(Response.ERROR, null, "药品不存在");
		}
		return resp;
	}
}
