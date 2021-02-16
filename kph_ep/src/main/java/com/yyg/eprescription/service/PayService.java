package com.yyg.eprescription.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yyg.eprescription.entity.Prescription;

@Service
public class PayService {

	@Autowired
	CloseableHttpClient client;
	@Autowired
	private Environment env;

	public boolean noticePay(Prescription prescription) throws Exception {
		String host = env.getProperty("pay.host", "http://127.0.0.1");

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		
		Date orderDate = prescription.getCreatedate();
		
		HttpPost httpMethod = new HttpPost(host + "/msg/pharmacy_f");

		httpMethod.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());

		//List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		//formparams.add(new BasicNameValuePair("PatientName", prescription.getPatientname()));
		//formparams.add(new BasicNameValuePair("PatientNo", prescription.getRegNo()));
		//formparams.add(new BasicNameValuePair("PatientCardNo", prescription.getCardNo()));
		//formparams.add(new BasicNameValuePair("OEOrdDate", dateFormat.format(orderDate)));
		//formparams.add(new BasicNameValuePair("OEOrdTime", timeFormat.format(orderDate)));
		//formparams.add(new BasicNameValuePair("PharmacyCode", "jxfyhpk"));
		//UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		//httpMethod.setEntity(entity);
		HashMap<String, Object> map = new HashMap<>();

		map.put("PatientName", prescription.getPatientname());
		map.put("PatientNo", prescription.getRegNo());
		map.put("PatientCardNo", prescription.getCardNo());
		map.put("OEOrdDate", dateFormat.format(orderDate));
		map.put("OEOrdTime", timeFormat.format(orderDate));
		map.put("PharmacyCode", "jxfyhpk");
		String postData = JSON.toJSONString(map);

		httpMethod.setEntity(new StringEntity(postData, ContentType.APPLICATION_JSON));
		
		CloseableHttpResponse response = client.execute(httpMethod);
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				String resp = EntityUtils.toString(response.getEntity());
				JSONObject respObj = JSON.parseObject(resp);
				String code = respObj.getString("code");
				if ("1".equals(code)) {

					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} finally {
			response.close();
		}
	}

}
