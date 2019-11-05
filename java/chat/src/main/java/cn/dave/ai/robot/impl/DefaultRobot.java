package cn.dave.ai.robot.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import cn.dave.ai.bean.Good;
import cn.dave.ai.bean.Reply;
import cn.dave.ai.robot.RecognizeType;
import cn.dave.ai.robot.Robot;
import cn.dave.utils.Base64Utils;
import cn.dave.utils.HashUtil;
import cn.dave.utils.Utils;

public class DefaultRobot implements Robot{

	private TulinRobot tulinRobot = new TulinRobot("596da08e55ea468f92ffbfef31d48095");
	private BaiduSpeechRobot baiduRobot = new BaiduSpeechRobot("17012873", "uaVZGXGtSbnNjc1iCrAHTiRs", "Szylm5wObcy2NPbFapusdUS8jSjIQgAL");
	private BaiduImageRobot baiduImageRobot = new BaiduImageRobot("17017993", "O4gWT6fCgcQc73r4YSHxqVRg", "MfgGbNTlzRH1aeiihUGj8yj9FGRt3zoF");
	private ImageToolRobot imageToolRobot = new ImageToolRobot("17048470", "keztcPccDYYG0fRVBA5LKubq", "I3vPvPb0Gpl5OLseuTw3yi0aK5jxB1ti");
	private ClassifyRobot classifyRobot = new ClassifyRobot("17053261", "mza9XycwcBMpy35hMIW3GS2j", "Paz6EFTp7HYGW0CSWUZ4LzApPV4ISVie");
	@Override
	public Reply textDiscernment(String language, String userId) {
		// TODO Auto-generated method stub
		return tulinRobot.textDiscernment(language, userId);
	}
	@Override
	public Reply speechReply(String filePath) {
		// TODO Auto-generated method stub
		return baiduRobot.asr(filePath, HashUtil.getHash_32("rwrwrwtt44135gdasg"));
	}

	@Override
	public Reply speechText(String text, String path) {
		// TODO Auto-generated method stub
		return baiduRobot.synthesis(text, path);
	}
	@Override
	public Reply orc(String path) {
		// TODO Auto-generated method stub
		return baiduImageRobot.orc(path);
	}
	@Override
	public Reply doubleImage(String image) {
		// TODO Auto-generated method stub
		Reply doubleImage = imageToolRobot.doubleImage(image);
		if(!doubleImage.isUseLess()) {
			//System.out.println(doubleImage.getText());
			String filePath = Utils.getParentPathByFilePath(image)+File.separator+new Date().getTime()+".jpg";
			Base64Utils.Base64ToImage(doubleImage.getBase64ImageString(),
									filePath);
			doubleImage.setFilePath(filePath);
		}
		return doubleImage;
	}
	@Override
	public Reply colorImage(String image) {
		// TODO Auto-generated method stub
		Reply colourizeImage = imageToolRobot.colourizeImage(image);
		if(!colourizeImage.isUseLess()) {
			String filePath = Utils.getParentPathByFilePath(image)+File.separator+new Date().getTime()+".jpg";
			Base64Utils.Base64ToImage(colourizeImage.getBase64ImageString(),
									filePath);
			colourizeImage.setFilePath(filePath);
		}
		return colourizeImage;
	}
	
	@Override
	public Reply detect(String image, RecognizeType recognizeType) {
		// TODO Auto-generated method stub
		Reply detect = null;
		if(recognizeType == RecognizeType.ANIMAL) {
			detect = classifyRobot.animalDetect(image);
		}else if(recognizeType == RecognizeType.CAR) {
			detect = classifyRobot.carDetect(image);
		}else if(recognizeType == RecognizeType.DISH) {
			detect = classifyRobot.dishDetect(image);
		}else if(recognizeType == RecognizeType.LANDMARK) {
			detect = classifyRobot.landMark(image);
		}else if(recognizeType == RecognizeType.LOGO) {
			detect = classifyRobot.logoDetect(image);
		}else if(recognizeType == RecognizeType.PLANT) {
			detect = classifyRobot.plantDetect(image);
		}else if(recognizeType == RecognizeType.REDWINE) {
			detect = classifyRobot.redwine(image);
		}
		if(!detect.isUseLess()) {
			List<Good> goods = detect.getGoods();
			if(goods!= null && !goods.isEmpty()) {
				detect.setText(goods.get(0).getName());
			}
		}
		return detect;
	}
	
	
	

}
