package cn.dave.ai.robot.impl;


import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import cn.dave.ai.bean.Reply;
import cn.dave.utils.HashUtil;

public class TulinRobot{
	private Logger logger = Logger.getLogger(TulinRobot.class);
	private RestTemplate restTemplate = new RestTemplate();
	private String apiKey;
	public TulinRobot(String apiKey) {
		this.apiKey = apiKey;
	}
	public Reply textDiscernment(String language,String userId) {
		// TODO Auto-generated method stub
		//转换为32位hash
		userId = HashUtil.getHash_32(userId);
		TuLinRequest tuLinRequest = new TuLinRequest();
		UserInfo userInfo = new UserInfo();
		userInfo.setApiKey(apiKey);
		userInfo.setUserId(userId);
		tuLinRequest.setUserInfo(userInfo);
		Perception perception = new Perception();
		InputText inputText = new InputText();
		inputText.setText(language);
		perception.setInputText(inputText);
		tuLinRequest.setPerception(perception);
		tuLinRequest.setUserInfo(userInfo);
		try {
			String postForObject = restTemplate.postForObject("http://openapi.tuling123.com/openapi/api/v2", tuLinRequest, String.class);
			TuLinReply tuLinReply = JSON.parseObject(postForObject, TuLinReply.class);
			String code = tuLinReply.getIntent().getCode();
			Reply reply = new Reply();
			if(code.equals("4003")) {
				reply.setUseLess(true);
			}else {
				reply.setUseLess(false);
			}
			if(code.equals("5000") || code.equals("6000")||code.equals("4000")
					||code.equals("4001")||code.equals("4002")||code.equals("4005")
					||code.equals("4007")|| code.equals("4100")
					||code.equals("4200")||code.equals("4300")
					||code.equals("4400")||code.equals("4500")||code.equals("4600")
					||code.equals("4602")||code.equals("7002")||code.equals("8008")) {
				logger.info("错误码： "+code);
				return null;
			}
			String text = "";
			List<Result> results = tuLinReply.getResults();
			if(results != null) {
				for(Result result : results) {
					text = text + result.toString();
				}
				reply.setText(text);
				return reply;
			}
		}catch(Exception e) {
			logger.info(e.getMessage());
			return null;
		}
		return null;
	}
}
