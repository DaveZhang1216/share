package cn.dave.ai.robot.impl;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.baidu.aip.imageprocess.AipImageProcess;

import cn.dave.ai.bean.Reply;


public class ImageToolRobot {
	private AipImageProcess client;
	public ImageToolRobot(String appId,String apiKey,String secretKey) {
		client = new AipImageProcess(appId, apiKey, secretKey);
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
	}
	
	public Reply doubleImage(String filePath) {
		JSONObject res = client.imageQualityEnhance(filePath, new HashMap<>());
		Reply reply = new Reply();
		reply.setUseLess(false);
		String string = res.getString("image");
		if(StringUtils.isEmpty(string)) {
			reply.setUseLess(true);
			return reply;
		}
		reply.setBase64ImageString(string);
		return reply;
	}
	public Reply colourizeImage(String filePath) {
		//JSONObject res = 
		JSONObject colourize = client.colourize(filePath, new HashMap<>());
		Reply reply = new Reply();
		reply.setUseLess(false);
		String string = colourize.getString("image");
		if(StringUtils.isEmpty(string)) {
			reply.setUseLess(true);
			return reply;
		}
		reply.setBase64ImageString(string);
		return reply;
	}
}
