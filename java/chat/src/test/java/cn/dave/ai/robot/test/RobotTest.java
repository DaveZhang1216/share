package cn.dave.ai.robot.test;

import java.util.List;

import org.junit.Test;

import cn.dave.ai.bean.Good;
import cn.dave.ai.bean.Reply;
import cn.dave.ai.bean.Words;
import cn.dave.ai.robot.Robot;
import cn.dave.ai.robot.RobotFactory;
import cn.dave.ai.robot.impl.BaiduImageRobot;
import cn.dave.ai.robot.impl.BaiduSpeechRobot;
import cn.dave.ai.robot.impl.ClassifyRobot;
import cn.dave.ai.robot.impl.ImageToolRobot;
import cn.dave.ai.robot.impl.TulinRobot;
import cn.dave.utils.Base64Utils;

public class RobotTest {
	@Test
	public void textDiscernmentTest() {
		String apiKey = "596da08e55ea468f92ffbfef31d48095";
		TulinRobot robot = new TulinRobot(apiKey);
		Reply textDiscernment = robot.textDiscernment("很高兴能认识你", "123");
		if(textDiscernment!=null) {
			System.out.println(textDiscernment.getText());
		}else {
			System.out.println("失败了");
		}
	}
	@Test
	public void robotTest() {
		Robot robot = RobotFactory.getRobot();
		//先说句话   合成语音
		Reply sayContent = robot.speechText("哈哈,又见面了", "E://wechat/voicePath");
		if(sayContent.isUseLess()) {
			System.out.println("语音合成出错");
		}
		//语音翻译
		Reply speechReply = robot.speechReply(sayContent.getFilePath());
		if(speechReply.isUseLess()) {
			System.out.println("语音翻译出错");
		}
		//语意解析
		Reply textDiscernment = robot.textDiscernment(speechReply.getText(), "123");
		if(textDiscernment.isUseLess()) {
			System.out.println("图灵累了要休息了");
		}
		System.out.println("应答是   "+textDiscernment.getText());
	}
	
	@Test
	public void speechTest() {
		String appId = "17012873";
		String apiKey = "uaVZGXGtSbnNjc1iCrAHTiRs";
		String secretKey = "Szylm5wObcy2NPbFapusdUS8jSjIQgAL";
		
		BaiduSpeechRobot baiduRobot = new BaiduSpeechRobot(appId, apiKey, secretKey);
		Reply synthesis = baiduRobot.synthesis("你好","E://wechat/voicePath");
		if(synthesis.isUseLess()) {
			System.out.println("语音合成错误");
			return;
		}else {
			Reply asr = baiduRobot.asr(synthesis.getFilePath(), "123");
			if(asr.isUseLess()) {
				System.out.println("语音识别错误");
				return;
			}
			System.out.println("语音识别成功："+asr.getText());
		}
	}
	@Test
	public void orcTest() {
		BaiduImageRobot robot = new BaiduImageRobot("17017993", "O4gWT6fCgcQc73r4YSHxqVRg", "MfgGbNTlzRH1aeiihUGj8yj9FGRt3zoF");
		Reply orc = robot.orc("E:\\wechat\\picPath\\1.jpg");
		if(orc.isUseLess()) {
			System.out.println("失败");
			return;
		}
		if(!orc.getWordsList().isEmpty()) {
			for(Words words : orc.getWordsList()) {
				System.out.println("识别出来单词： "+ words.getContent()+" 平均可信度："+ words.getAverage()+" 最小可信度：" + words.getMin());
			}
		}
		
	}
	
	@Test
	public void doubleImageTest() {
		ImageToolRobot imageToolRobot = new ImageToolRobot("17048470", "keztcPccDYYG0fRVBA5LKubq", "I3vPvPb0Gpl5OLseuTw3yi0aK5jxB1ti");
		Reply doubleImage = imageToolRobot.doubleImage("E:\\wechat\\picPath\\1.jpg");
		if(doubleImage.isUseLess()) {
			System.out.println("此api 无用");
		}else {
			//System.out.println(doubleImage.getText());
			Base64Utils.Base64ToImage(doubleImage.getBase64ImageString(), "E:\\wechat\\picPath\\xh.jpg");
		}
	}
	//黑白照片 彩色化
	@Test
	public void colorImageTest() {
		ImageToolRobot imageToolRobot = new ImageToolRobot("17048470", "keztcPccDYYG0fRVBA5LKubq", "I3vPvPb0Gpl5OLseuTw3yi0aK5jxB1ti");
		String image = "E:\\wechat\\picPath\\x.jpg";
		Reply reply = imageToolRobot.colourizeImage(image);//doubleImage("E:\\wechat\\picPath\\1.jpg");
		if(reply.isUseLess()) {
			System.out.println("此api 无用");
		}else {
			//System.out.println(doubleImage.getText());
			Base64Utils.Base64ToImage(reply.getBase64ImageString(), "E:\\wechat\\picPath\\xh.jpg");
		}
	}
	private static ClassifyRobot classifyRobot = new ClassifyRobot("17053261", "mza9XycwcBMpy35hMIW3GS2j", "Paz6EFTp7HYGW0CSWUZ4LzApPV4ISVie");
	//识别菜品
	@Test 
	public void dishTest() {
		Reply dishDetect = classifyRobot.dishDetect("E:\\wechat\\picPath\\egg.jpg");
		if(dishDetect.isUseLess()) {
			System.out.println("无效");
		}else {
			List<Good> goods = dishDetect.getGoods();
			for(Good good : goods) {
				System.out.println("品名： "+ good.getName()+ "  卡路里： "+good.getCalorie() + "分数： "+good.getScore());
			}
		}
	}
	@Test
	public void carDetect() {
		Reply carDetect = classifyRobot.carDetect("E:\\wechat\\picPath\\car.jpg");
		if(carDetect.isUseLess()) {
			System.out.println("无效");
		}else {
			List<Good> goods = carDetect.getGoods();
			for(Good good : goods) {
				System.out.println("品名： "+ good.getName() + "分数： "+good.getScore());
			}
		}
	}
	@Test
	public void logoDetect() {
		Reply logoDetect = classifyRobot.logoDetect("E:\\wechat\\picPath\\logo.jpg");
		if(logoDetect.isUseLess()) {
			System.out.println("无效");
		}else {
			List<Good> goods = logoDetect.getGoods();
			for(Good good : goods) {
				System.out.println("品名： "+ good.getName() + "   可能性： "+good.getScore());
			}
		}
	}
	//动物识别
	@Test
	public void animalDetect() {
		Reply animalDetect = classifyRobot.animalDetect("E:\\wechat\\picPath\\animal.jpg");
		if(animalDetect.isUseLess()) {
			System.out.println("无效");
		}else {
			List<Good> goods = animalDetect.getGoods();
			for(Good good : goods) {
				System.out.println("品名： "+ good.getName() + "   可能性： "+good.getScore());
			}
		}
	}
	//植物识别
	@Test 
	public void plantDetect() {
		Reply plantDetect = classifyRobot.plantDetect("E:\\wechat\\picPath\\plant.jpg");
		if(plantDetect.isUseLess()) {
			System.out.println("无效");
		}else {
			List<Good> goods = plantDetect.getGoods();
			for(Good good : goods) {
				System.out.println("品名： "+ good.getName() + "   可能性： "+good.getScore());
			}
		}
	}
	//地标识别
	@Test
	public void landMark() {
		//Reply plantDetect = classifyRobot.plantDetect("E:\\wechat\\picPath\\plant.jpg");
		Reply landMark = classifyRobot.landMark("E:\\wechat\\picPath\\land.jpg");
		if(landMark.isUseLess()) {
			System.out.println("无法识别");
		}else {
			List<Good> goods = landMark.getGoods();
			for(Good good : goods) {
				System.out.println("品名： "+ good.getName());
			}
		}
	}
	@Test
	public void redWine() {
		Reply redwine = classifyRobot.redwine("E:\\wechat\\picPath\\wine.png");
		if(redwine.isUseLess()) {
			System.out.println("无法识别");
		}else {
			List<Good> goods = redwine.getGoods();
			for(Good good : goods) {
				System.out.println("品名： "+ good.getName()+ "国家： "+good.getCountryCn()+"庄园： "+good.getWineryCn());
			}
		}
	}
	/**
	 * failed
	 */
	@Test
	public void flower() {
		//Reply landMark = classifyRobot.landMark("E:\\wechat\\picPath\\flower.jpg");
		Reply flower = classifyRobot.flower("E:\\wechat\\picPath\\flower.jpg");
		if(flower.isUseLess()) {
			System.out.println("无法识别");
		}else {
			List<Good> goods = flower.getGoods();
			for(Good good : goods) {
				System.out.println("品名： "+ good.getName());
			}
		}
	}
}
