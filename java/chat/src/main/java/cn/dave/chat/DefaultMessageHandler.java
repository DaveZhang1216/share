package cn.dave.chat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.beans.RecommendInfo;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.DownloadTools;

public class DefaultMessageHandler implements IMsgHandlerFace{
	private Logger log = Logger.getLogger(DefaultMessageHandler.class);
	private ChatHandler chatHandler;
	private MessageFilter messageFilter;
	private String voicePath;
	private String vedioPath;
	private String picPath;
	private String mediaPath;
	/**
	 * @param chatHandler
	 * @param voicePath  
	 * @param vedioPath
	 * @param picPath
	 * @param mediaPath
	 */
	public DefaultMessageHandler(ChatHandler chatHandler,String voicePath, String vedioPath,String picPath,String mediaPath,MessageFilter messageFilter) {
		this.chatHandler = chatHandler;
		this.voicePath = voicePath;
		this.vedioPath = vedioPath;
		this.picPath = picPath;
		this.mediaPath = mediaPath;
		this.messageFilter = messageFilter;
	}
	private void logoutHandler(Exception exception,String fromUserName) {
		MessageTools.sendMsgById("我下线了  O_o_O", fromUserName);
		//WechatTools.logout();//下线
		log.error(exception.getMessage());
	}
	private void logUserInfo(BaseMsg baseMsg) {
		RecommendInfo recommendInfo = baseMsg.getRecommendInfo();
		String nickName = recommendInfo.getNickName();
		String province = recommendInfo.getProvince();
		String city = recommendInfo.getCity();
		log.info("用户昵称： "+ nickName);
		log.info("用户省份： "+province);
		log.info("用户城市： "+city);
		log.info("用户id: "+ baseMsg.getFromUserName());
	}
	
	@Override
	public String mediaMsgHandle(BaseMsg baseMsg) {
		// TODO Auto-generated method stub
		if(!messageFilter.msgCheck(baseMsg)) {//消息过滤器 没有经过验证
			return null;
		}
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());// 这里使用收到图片的时间作为文件名
		String mediaPath = this.mediaPath + File.separator + fileName + ".txt"; // 调用此方法来保存图片
		DownloadTools.getDownloadFn(baseMsg, MsgTypeEnum.MEDIA.getType(), mediaPath); // 保存图片的路径
		return "文件已经保存完毕";
	}

	@Override
	public String nameCardMsgHandle(BaseMsg baseMsg) {
		// TODO Auto-generated method stub
		if(!messageFilter.msgCheck(baseMsg)) {//消息过滤器 没有经过验证
			return null;
		}
		return "名片已收到";
	}

	@Override
	public String picMsgHandle(BaseMsg baseMsg) {
		// TODO Auto-generated method stub
		if(!messageFilter.msgCheck(baseMsg)) {//消息过滤器 没有经过验证
			return null;
		}
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());// 这里使用收到图片的时间作为文件名
		String picPath = this.picPath + File.separator + fileName + ".jpg"; // 调用此方法来保存图片
		DownloadTools.getDownloadFn(baseMsg, MsgTypeEnum.PIC.getType(), picPath); // 保存图片的路径
		this.logUserInfo(baseMsg);
		return chatHandler.picHandler(picPath, baseMsg.getFromUserName());
	}
	@Override
	public void sysMsgHandle(BaseMsg baseMsg) {
		// TODO Auto-generated method stub
		if(!messageFilter.msgCheck(baseMsg)) {//消息过滤器 没有经过验证
			return;
		}
		String text = baseMsg.getContent();
		log.info(text);
	}

	@Override
	public String textMsgHandle(BaseMsg baseMsg) {
		if(!messageFilter.msgCheck(baseMsg)) {//消息过滤器 没有经过验证
			return null;
		}
		// TODO Auto-generated method stub
		String fromUserName = baseMsg.getFromUserName();//userId
		this.logUserInfo(baseMsg);
		try {
			return chatHandler.textHandler(baseMsg.getText(), fromUserName);
		}catch (Exception e) {
			// TODO: handle exception
			this.logoutHandler(e, fromUserName);
		}
		return null;
	}

	@Override
	public String verifyAddFriendMsgHandle(BaseMsg baseMsg) {
		if(!messageFilter.msgCheck(baseMsg)) {//消息过滤器 没有经过验证
			return null;
		}
		// TODO Auto-generated method stub
		MessageTools.addFriend(baseMsg, true); // 同意好友请求，false为不接受好友请求
		this.logUserInfo(baseMsg);
		RecommendInfo recommendInfo = baseMsg.getRecommendInfo();
		String nickName = recommendInfo.getNickName();
		String text =nickName +" 你好欢迎添加我为好友！";
		return text;
	}

	@Override
	public String viedoMsgHandle(BaseMsg baseMsg) {
		if(!messageFilter.msgCheck(baseMsg)) {//消息过滤器 没有经过验证
			return null;
		}
		// TODO Auto-generated method stub
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String viedoPath = this.vedioPath + File.separator + fileName + ".mp4";
		DownloadTools.getDownloadFn(baseMsg, MsgTypeEnum.VIEDO.getType(), viedoPath);
		return "视频保存成功";
	}

	@Override
	public String voiceMsgHandle(BaseMsg baseMsg) {
		if(!messageFilter.msgCheck(baseMsg)) {//消息过滤器 没有经过验证
			return null;
		}
		// TODO Auto-generated method stub
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String voicePath = this.voicePath + File.separator + fileName + ".mp3";
		DownloadTools.getDownloadFn(baseMsg, MsgTypeEnum.VOICE.getType(), voicePath);
		String fromUserName = baseMsg.getFromUserName();
		log.info("用户id : "+ fromUserName);
		try {
			return	chatHandler.voiceHandler(voicePath, baseMsg.getFromUserName());
		}catch (Exception e) {
			// TODO: handle exception
			this.logoutHandler(e, fromUserName);
		}
		return null;
	}
}
