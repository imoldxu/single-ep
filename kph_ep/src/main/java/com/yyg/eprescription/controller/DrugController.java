package com.yyg.eprescription.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.x.commons.mybatis.PageResult;
import com.yyg.eprescription.bo.DrugQuery;
import com.yyg.eprescription.bo.UpDownDrugBo;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.context.Response;
import com.yyg.eprescription.entity.Drug;
import com.yyg.eprescription.service.DoctorDrugService;
import com.yyg.eprescription.service.DrugService;
import com.yyg.eprescription.util.ExcelUtils;
import com.yyg.eprescription.vo.ShortDrugInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/drug")
@Api("药品接口")
public class DrugController {

	@Autowired
	DrugService drugService;
	@Autowired
	DoctorDrugService doctorDrugService;
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDrugsByKeys", method = RequestMethod.GET)
	@ApiOperation(value = "根据药品的拼音首字母缩写或药品名称搜索药品", notes = "根据药品的拼音首字母缩写或药品名称搜索药品")
	public Response getDrugsByKeys(
			@ApiParam(name = "keys", value = "拼音首字母索引或药品名称") @RequestParam(name = "keys") String keys,
			@ApiParam(name = "type", value = "西药为1，中药为2") @RequestParam(name = "type", defaultValue="1") int type,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		
		List<ShortDrugInfo> ret = drugService.queryDrugsByKeys(type, keys);
		
		Response resp = new Response(ErrorCode.OK, ret, "OK");
		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/getDrugInfoListByKeys", method = RequestMethod.GET)
	@ApiOperation(value = "根据药品的拼音缩写搜索药品", notes = "根据药品的拼音缩写搜索药品")
	public PageResult<Drug> getDrugInfoListByKeys(
			@ApiParam(name = "query", value = "查询条件") @Valid DrugQuery query,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		
		PageResult<Drug> resp = drugService.queryDrugInfoByKeys(query);
		
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
		
		List<ShortDrugInfo> ret = drugService.queryDrugByCategory(type, category);
		
		Response resp = new Response(ErrorCode.OK, ret, "OK");
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
			Response resp = new Response(ErrorCode.ARG_ERROR, null, "请输入医生姓名");
			return resp; 
		}
		if(department == null || department.isEmpty()){
			Response resp = new Response(ErrorCode.ARG_ERROR, null, "请输入医生科室");
			return resp; 
		}
		
		List<ShortDrugInfo> ret = doctorDrugService.queryDrugByDoctor(type, doctorname, department);
		
		Response resp = new Response(ErrorCode.OK, ret, "OK");
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
		
		Drug drug = drugService.getDrugById(drugid);
		
		Response resp = null;
		if(drug==null){
			resp = new Response(ErrorCode.ARG_ERROR, null, "请求的药品不存在或已下架");
		}else{
			resp = new Response(ErrorCode.OK, drug, "OK");
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
			return new Response(ErrorCode.ARG_ERROR, null, "请求参数异常");
		}
		
		//获取文件名
	    String name=file.getOriginalFilename();
	    //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
	    long size=file.getSize();
	    if(name == null || ("").equals(name) && size==0){
	    	return new Response(ErrorCode.ARG_ERROR, null, "文件不存在或没有内容");
	    }
	    //批量导入。参数：文件名，文件。
	    ExcelUtils excelUtils = new ExcelUtils();
	    
	    Response resp = null;
	    
	    try{
	    	List<Drug> drugList = excelUtils.getExcelInfo(name, file);
	    	//drugMapper.insertDrugs(drugList);
			drugService.insertList(drugList);
	    	
	    	resp = new Response(ErrorCode.OK, drugList, "OK");    
	    }catch(IOException ioe){
	    	 resp = new Response(ErrorCode.ARG_ERROR,null, ioe.getMessage());
	    }catch (Exception e) {
	    	e.printStackTrace();
	        resp = new Response(ErrorCode.ARG_ERROR,null, "导入失败");
	    }

		return resp;
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/addDrug", method = RequestMethod.POST)
	@ApiOperation(value = "添加药品信息", notes = "")
	public Response addDrug(@ApiParam(name = "drug", value = "drug") @RequestBody @Valid Drug drug,
			HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;
		
		if(drug.getId() == null){
			int opRet = drugService.addDrug(drug);
			if(opRet == 0){
				throw new HandleException(ErrorCode.NORMAL_ERROR, "修改失败");    
			}else{
				resp = new Response(ErrorCode.OK, drug, "OK");
				return resp;
			}
		}else{
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/modifyDrug", method = RequestMethod.PUT)
	@ApiOperation(value = "修改药品信息", notes = "")
	public Response modifyDrug(@ApiParam(name = "drug", value = "drug") @RequestBody @Valid Drug drug,
			HttpServletRequest request,HttpServletResponse response) {
		//response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("Access-Control-Allow-Methods", "PUT");
		
		Response resp = null;
		
		if(drug.getId() != null){
			int opRet = drugService.updateDrug(drug);
			if(opRet == 0){
				throw new HandleException(ErrorCode.NORMAL_ERROR, "修改失败");    
			}else{
				resp = new Response(ErrorCode.OK, drug, "OK");
				return resp;
			}
		}else{
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
	}
	
//	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
//	@RequestMapping(value = "/delDrug", method = RequestMethod.POST)
//	@ApiOperation(value = "删除药品信息", notes = "删除药品")
//	public Response delDrug(@ApiParam(name="drugid", value="drugid") @RequestParam(value="drugid") int drugid, HttpServletRequest request,HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods", "POST");
//		
//		Response resp = null;			
//		int optRet = drugService.deleteDrug(drugid);
//	
//		if(optRet!=0){	
//			resp = new Response(ErrorCode.OK, null, "删除成功");
//		}else{
//			resp = new Response(ErrorCode.ARG_ERROR, null, "药品不存在");
//		}
//		return resp;
//	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/downDrug", method = RequestMethod.PUT)
	@ApiOperation(value = "下架药品", notes = "")
	public Response downDrug(@ApiParam(name="drugbo", value="drugbo") @RequestBody @Valid UpDownDrugBo drugbo, HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;	
		int opRet = drugService.downDrug(drugbo.getDrugid());
		if(opRet!=0){
			resp = new Response(ErrorCode.OK, null, "删除成功");
			return resp;
		}else{
			throw new HandleException(ErrorCode.ARG_ERROR, "药品不存在");
		}
	}
	
	@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
	@RequestMapping(value = "/upDrug", method = RequestMethod.PUT)
	@ApiOperation(value = "上架药品信息", notes = "上架药品信息")
	public Response upDrug(@ApiParam(name="drugbo", value="drugbo") @RequestBody @Valid UpDownDrugBo drugbo, HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		Response resp = null;			
		int opRet = drugService.upDrug(drugbo.getDrugid());
		if(opRet!=0){
			resp = new Response(ErrorCode.OK, null, "删除成功");
			return resp;
		}else{
			throw new HandleException(ErrorCode.ARG_ERROR, "药品不存在");
		}
	}
}
