package cn.dave.ai.robot.impl;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.imageclassify.AipImageClassify;

import cn.dave.ai.bean.Good;
import cn.dave.ai.bean.Reply;

/**
 * 	分类机器人
 * @author DaveZhang
 *
 */
public class ClassifyRobot {
	
	private AipImageClassify client;
	public ClassifyRobot(String appId, String apiKey, String secretKey) {
		client = new AipImageClassify(appId, apiKey, secretKey);
		client.setConnectionTimeoutInMillis(2000);
	    client.setSocketTimeoutInMillis(60000);
	}
	/**
	 * 菜品识别 
	 * 内涵卡路里信息
	 * @param imagePath
	 * @return
	 */
	public Reply dishDetect(String imagePath) {
		Reply reply = new Reply();
		reply.setUseLess(false);
		HashMap<String, String> options = new HashMap<String,String>();
		options.put("top_num", "3");
		options.put("filter_threshold", "0.7");
		JSONObject dishDetect = client.dishDetect(imagePath, options);
		try {
			JSONArray jsonArray = dishDetect.getJSONArray("result");
			int len = jsonArray.length();
			for(int i=0;i<len;i++) {
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				 double score = jsonObject.getDouble("probability");
				 if(score>0.3 || i==0) {//可信度大于0.3 或者第一个
					 Good good = new Good();
					 good.setScore(score);
					 good.setName(jsonObject.getString("name"));
					 boolean boolean1 = jsonObject.getBoolean("has_calorie");
					 if(boolean1) {
						 good.setCalorie((float)jsonObject.getDouble("calorie"));
					 }
					 reply.addGood(good);
				 }
			}
		}catch (Exception e) {
			// TODO: handle exception
			reply.setUseLess(true);
		}
		return reply;
	}
	/**
	 * 	汽车识别
	 * @param imagePath
	 * @return
	 */
	public Reply carDetect(String imagePath) {
		Reply reply = new Reply();
		reply.setUseLess(false);
		HashMap<String, String> options = new HashMap<String,String>();
		options.put("top_num", "3");
		JSONObject dishDetect = client.carDetect(imagePath, options);
		try {
			JSONArray jsonArray = dishDetect.getJSONArray("result");
			int len = jsonArray.length();
			for(int i=0;i<len;i++) {
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				double score = jsonObject.getDouble("score");
				 if(score >0.3 || i==0) {//可信度大于0.3 或者第一个
					 Good good = new Good();
					 good.setScore(score);
					 good.setName(jsonObject.getString("name"));
					 reply.addGood(good);
				 }
			}
		}catch (Exception e) {
			// TODO: handle exception
			reply.setUseLess(true);
		}
		return reply;
	}
	/**
	 *	 获取商标名称
	 * @param imagePath
	 * @return
	 */
	public Reply logoDetect(String imagePath) {
		Reply reply = new Reply();
		reply.setUseLess(false);
		HashMap<String, String> options = new HashMap<String,String>();
		options.put("custom_lib", "false");
		JSONObject dishDetect = client.logoSearch(imagePath, options);
		try {
			JSONArray jsonArray = dishDetect.getJSONArray("result");
			int len = jsonArray.length();
			for(int i=0;i<len;i++) {
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				 double probability = jsonObject.getDouble("probability");
				 if(probability>0.3 || i==0) {//可信度大于0.3 或者第一个
					 Good good = new Good();
					 good.setScore(probability);
					 good.setName(jsonObject.getString("name"));
					 reply.addGood(good);
				 }
			}
		}catch (Exception e) {
			// TODO: handle exception
			reply.setUseLess(true);
		}
		return reply;
	}
	/**
	 * 	动物识别
	 * @param imagePath
	 * @return
	 */
	public Reply animalDetect(String imagePath) {
		Reply reply = new Reply();
		reply.setUseLess(false);
		HashMap<String, String> options = new HashMap<String,String>();
		options.put("top_num", "3");
		JSONObject dishDetect = client.animalDetect(imagePath, options);
		try {
			JSONArray jsonArray = dishDetect.getJSONArray("result");
			int len = jsonArray.length();
			for(int i=0;i<len;i++) {
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				 double score = jsonObject.getDouble("score");
				 if(score > 0.3 || i==0) {//可信度大于0.3 或者第一个
					 Good good = new Good();
					 good.setScore(score);
					 good.setName(jsonObject.getString("name"));
					 reply.addGood(good);
				 }
			}
		}catch (Exception e) {
			// TODO: handle exception
			reply.setUseLess(true);
		}
		return reply;
	}
	/**
	 * 	 植物识别
	 * @param imagePath
	 * @return
	 */
	public Reply plantDetect(String imagePath) {
		Reply reply = new Reply();
		reply.setUseLess(false);
		HashMap<String, String> options = new HashMap<String,String>();
		JSONObject dishDetect = client.plantDetect(imagePath, options);
		try {
			JSONArray jsonArray = dishDetect.getJSONArray("result");
			int len = jsonArray.length();
			for(int i=0;i<len;i++) {
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				 double score = jsonObject.getDouble("score");
				 if(score>0.3 || i==0) {//可信度大于0.3 或者第一个
					 Good good = new Good();
					 good.setScore(score);
					 good.setName(jsonObject.getString("name"));
					 reply.addGood(good);
				 }
			}
		}catch (Exception e) {
			// TODO: handle exception
			reply.setUseLess(true);
		}
		return reply;
	}
	/**
	 * 	地标识别
	 * @param image
	 * @return
	 */
	public Reply landMark(String imagePath) {
		Reply reply = new Reply();
		reply.setUseLess(false);
		HashMap<String, String> options = new HashMap<String,String>();
		JSONObject dishDetect = client.landmark(imagePath, options);
		JSONObject jsonObject = dishDetect.getJSONObject("result");
		if(!StringUtils.isEmpty(jsonObject.getString("landmark"))) {
			Good good = new Good();
			good.setName(jsonObject.getString("landmark"));
			reply.addGood(good);
		}
		return reply;
	}
	/**
	 * 	红酒识别
	 * @param imagePath
	 * @return
	 */
	public Reply redwine(String imagePath) {
		Reply reply = new Reply();
		reply.setUseLess(false);
		HashMap<String, String> options = new HashMap<String,String>();
		JSONObject redwine = client.redwine(imagePath, options);
		JSONObject jsonObject = redwine.getJSONObject("result");
		String string = jsonObject.getString("wineNameCn");
		if(!StringUtils.isEmpty(string)) {
			 Good good = new Good();
			 good.setName(string);
			 int hasdetail = jsonObject.getInt("hasdetail");
			 if(hasdetail == 1) {
				 good.setCountryCn(jsonObject.getString("countryCn"));
				 good.setWineryCn(jsonObject.getString("wineryCn"));
			 }
			 reply.addGood(good);
		}else {
			reply.setUseLess(true);
		}
		return reply;
	}
	/**
	 * 	暂时不支持
	 * @param imagePath
	 * @return
	 */
	public Reply flower(String imagePath) {
		Reply reply = new Reply();
		reply.setUseLess(false);
		HashMap<String, String> options = new HashMap<String,String>();
		options.put("top_num", "3");
		JSONObject dishDetect = client.flower(imagePath, options);
		try {
			System.out.println(dishDetect.toString(2));
			JSONArray jsonArray = dishDetect.getJSONArray("result");
			int len = jsonArray.length();
			for(int i=0;i<len;i++) {
				 JSONObject jsonObject = jsonArray.getJSONObject(i);
				 double score = jsonObject.getDouble("score");
				 if(score>0.3 || i==0) {//可信度大于0.3 或者第一个
					 Good good = new Good();
					 good.setScore(score);
					 good.setName(jsonObject.getString("name"));
					 reply.addGood(good);
				 }
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			reply.setUseLess(true);
		}
		return reply;
	}
}
