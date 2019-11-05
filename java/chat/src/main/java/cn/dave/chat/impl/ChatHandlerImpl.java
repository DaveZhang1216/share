package cn.dave.chat.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.dave.ai.bean.Reply;
import cn.dave.ai.robot.Robot;
import cn.dave.chat.ChatHandler;
import cn.dave.chat.exception.HandlerException;
import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;
import cn.zhouyafeng.itchat4j.api.MessageTools;

public class ChatHandlerImpl implements ChatHandler{
	private static Logger log = LoggerFactory.getLogger(ChatHandlerImpl.class);
	private Robot robot;
	private CmdHandler cmdHandler;
	
	
	public ChatHandlerImpl(Robot robot) {
		// TODO Auto-generated constructor stub
		this.robot = robot;
		this.cmdHandler = CmdHandler.getSingleton();
	}
	@Override
	public String textHandler(String text, String userId) throws HandlerException {
		// TODO Auto-generated method stub
		
		Reply textDiscernment = robot.textDiscernment(text, userId);
		if(textDiscernment.isUseLess()) {
			throw new HandlerException("智能机器人已经无法在处理信息了");
		}
		return textDiscernment.getText();
	}
	@Override
	public String voiceHandler(String filePath, String userId) throws HandlerException {
		// TODO Auto-generated method stub
		//收到语音先转成文字
		Reply speechReply = robot.speechReply(filePath);
		if(speechReply.isUseLess()) {
			return "5555 我没有听明白";
		}
		String text = speechReply.getText();
		//转成文字后交给语音机器人解析
		return this.textHandler(text, userId);
	}
	
	@Override
	public String picHandler(String filePath, String userId) throws HandlerException {
		// TODO Auto-generated method stub
		CmdMode state = cmdHandler.getState(userId);
		log.info("mode :   "+Boolean.toString(state == null));
		if(state != null) {
			log.info("state ----- "+ state.getModeName());
			ModeBean modeBean = new ModeBean();
			modeBean.setResultType(ResultType.PIC);
			modeBean.setFilePath(filePath);
			modeBean.setUserId(userId);
			try {
				state.modeHandler(modeBean);
				if(modeBean.getResultType()==ResultType.TEXT) {
					log.info("回复消息-------------------------------------------------------");
					MessageTools.sendMsgById(modeBean.getText(), userId);
				}else if(modeBean.getResultType() == ResultType.PIC){
					log.info("回复图片-------------------------------------------------------");
					MessageTools.sendPicMsgByUserId(userId, modeBean.getFilePath());
				}
			} catch (ModeHandlerException e) {
				// TODO Auto-generated catch block
				MessageTools.sendMsgById(e.getMessage(), userId);
			}
		}
		return null;
	}
}
