package cn.dave.ai.robot.impl;

import java.util.ArrayList;

import org.json.JSONObject;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;

public class BaiduFaceRobot {
	private AipFace client;
	public BaiduFaceRobot(String appId,String apiKey,String secretKey) {
		client = new AipFace(appId, apiKey, secretKey);
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
	}
	/**
	 * 对比2张图片中人脸的相似度
	 * @param filePath1
	 * @param filePath2
	 * @return
	 */
	public float compareFace(String filePath1, String filePath2) {
		String image1 = filePath1;
		String image2 = filePath2;
		MatchRequest req1 = new MatchRequest(image1, "BASE64");
		MatchRequest req2 = new MatchRequest(image2, "BASE64");
		ArrayList<MatchRequest> requests = new ArrayList<>();
		requests.add(req1);
		requests.add(req2);
		JSONObject match = client.match(requests);
		System.out.println(match.toString(2));
		
		return 0;
		
		
	}
		
}
