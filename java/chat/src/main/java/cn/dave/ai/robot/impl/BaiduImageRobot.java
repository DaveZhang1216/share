package cn.dave.ai.robot.impl;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.aip.ocr.AipOcr;

import cn.dave.ai.bean.Reply;
import cn.dave.ai.bean.Words;

public class BaiduImageRobot {
	private static Logger logger = LoggerFactory.getLogger(BaiduSpeechRobot.class);
	private AipOcr client;
	public BaiduImageRobot(String appId,String apiKey,String secretKey) {
		client = new AipOcr(appId, apiKey, secretKey);
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
	}
	/**
	 * 对指定路径的图像文字识别
	 * @param filePath
	 * @return
	 */
	public Reply orc(String filePath) {
		Reply reply = new Reply();
		reply.setUseLess(false);
		HashMap<String, String> options = new HashMap<>();
		options.put("detect_direction", "true");
		options.put("probability", "true");
		JSONObject basicGeneral = client.basicAccurateGeneral(filePath, options);
		logger.info(basicGeneral.toString(2));
		try {
			JSONArray jsonArray = basicGeneral.getJSONArray("words_result");
			int len = jsonArray.length();
			for(int i=0;i<len;i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Words words = new Words();
				words.setAverage(jsonObject.getJSONObject("probability").getDouble("average"));
				words.setMin(jsonObject.getJSONObject("probability").getDouble("min"));
				words.setContent(jsonObject.getString("words"));
				reply.addWord(words);
			}
		}catch (Exception e) {
			// TODO: handle exception
			reply.setUseLess(true);
		}
		return reply;
	}
}
