package com.yyg.eprescription.service;

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yyg.eprescription.context.ErrorCode;
import com.yyg.eprescription.context.HandleException;
import com.yyg.eprescription.vo.PrescriptionInitVo;

@Service
public class HospitalPatientService {

	@Autowired
	CloseableHttpClient client;

	public PrescriptionInitVo getDiagnosisInfo(String cardNo) throws Exception {
		PrescriptionInitVo ret = null;
		URI uri = new URIBuilder().setScheme("http").setHost("127.0.0.1").setPort(8867).setPath("/patient")
				.setParameter("cardNo", cardNo).build();

		HttpGet httpMethod = new HttpGet(uri);

		CloseableHttpResponse response = client.execute(httpMethod);
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				String resp = EntityUtils.toString(response.getEntity());
				JSONObject respObj = JSON.parseObject(resp);
				Integer code = respObj.getInteger("code");
				if (code == ErrorCode.OK) {
					ret = respObj.getObject("data", PrescriptionInitVo.class);
					return ret;
				} else {
					String msg = respObj.getString("msg");
					throw new HandleException(ErrorCode.NORMAL_ERROR, msg);
				}
			} else {
				throw new HandleException(ErrorCode.NORMAL_ERROR, "获取不到患者信息");
			}
		} finally {
			response.close();
		}
	}

}
