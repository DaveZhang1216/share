package cn.dave.chat.impl;

import cn.dave.chat.Chat;
import cn.dave.chat.ChatHandler;
import cn.dave.chat.DefaultMessageHandler;
import cn.zhouyafeng.itchat4j.Wechat;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;

public class WeChatImpl implements Chat{
	private ChatHandler chatHandler;
	private String qrCodePath;
	private String voicePath;
	private String vedioPath;
	private String picPath;
	private String mediaPath;
	/**
	 * 
	 * @param chatHandler  信息处理工具
	 * @param qrCodePath
	 * @param voicePath
	 * @param vedioPath
	 * @param picPath
	 * @param mediaPath
	 */
	public WeChatImpl(ChatHandler chatHandler, String qrCodePath,String voicePath, String vedioPath,String picPath,String mediaPath) {
		// TODO Auto-generated constructor stub
		this.chatHandler = chatHandler;
		this.qrCodePath = qrCodePath;
		this.voicePath = voicePath;
		this.vedioPath = vedioPath;
		this.picPath = picPath;
		this.mediaPath = mediaPath;
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		IMsgHandlerFace msgHandlerFace = new DefaultMessageHandler(chatHandler, voicePath, vedioPath, picPath, mediaPath,new MessageFilterImpl());
		Wechat wechat = new Wechat(msgHandlerFace, qrCodePath);
		wechat.start();
	}
	
}
