package cn.dave.ai.robot.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;

import cn.dave.ai.bean.Reply;
import cn.dave.utils.AudioUtils;

public class BaiduSpeechRobot {
	private static Logger logger = LoggerFactory.getLogger(BaiduSpeechRobot.class);

	private AipSpeech client;
	public BaiduSpeechRobot(String appId, String apiKey, String secretKey) {
		client = new AipSpeech(appId, apiKey, secretKey);
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
	}
	/**
	 * 语音识别
	 * mp3格式的文件
	 * @param path
	 * @param cuid
	 * @return
	 */
	public Reply asr(String path, String cuid) {
		HashMap<String, Object> options = new HashMap<>();
		options.put("cuid", cuid);
		File file = new File(path);
		String parent = file.getParent();
		long time = new Date().getTime();
		AudioUtils.convertMP3ToPcm(path, parent+"\\"+time+".pcm");
		JSONObject asr = client.asr(parent+"\\"+time+".pcm", "pcm", 16000, options);
		int errorCode = asr.getInt("err_no");
		Reply reply = new Reply();
		if (errorCode!=0) { // 无法识别
			reply.setUseLess(true);
			logger.error("语音识别 错误码为： "+errorCode);
			return reply;
		}
		logger.info(asr.toString());
		reply.setUseLess(false);
		JSONArray jsonArray = asr.getJSONArray("result");
		if (jsonArray != null) {
			reply.setText(jsonArray.getString(0));
		}
		return reply;
	}

	public Reply synthesis(String text, String path) {
		HashMap<String, Object> options = new HashMap<>();
		options.put("spd", "5");
		options.put("pit", "5");
		options.put("per", "1");
		TtsResponse res = client.synthesis(text, "zh", 1, options);
		Reply reply = new Reply();
		if (res == null) {
			logger.error("无响应");
			reply.setUseLess(true);
			return reply;
		}
		JSONObject result = res.getResult();
		if (result != null) {
			reply.setUseLess(true);
			logger.error(result.toString(2));
		}
		byte[] data = res.getData();
		if (data != null) {
			reply.setUseLess(false);
			long time = new Date().getTime();
			try {
				Util.writeBytesToFileSystem(data, path + "/" + time + ".mp3");
				logger.info("合成声音成功");
				reply.setFilePath(path + "/" + time + ".mp3");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				reply.setUseLess(true);
				e.printStackTrace();
			}
		} else {
			reply.setUseLess(true);
			logger.info("合成失败");
			if (res != null) {
				logger.info(res.getResult().getString("err_msg"));
			}
		}
		return reply;
	}
}
