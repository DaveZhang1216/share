package cn.dave.chat.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import cn.dave.chat.MessageFilter;
import cn.dave.chat.bean.MessageAutoReply;
import cn.dave.chat.manager.Config;
import cn.dave.chat.mode.ResultType;
import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

public class MessageFilterImpl implements MessageFilter {
	private static Logger log = LoggerFactory.getLogger(MessageFilterImpl.class);
	private CmdHandler cmdHandler = CmdHandler.getSingleton();
	private Config config = Config.getSingleton();
	
	/**
	 * 
	 * @param msg
	 * @return false: 消息已经内部消耗     true:消息还要继续
	 */
	private boolean beforeCheckHandler(BaseMsg msg) {
		//是否为开始或推出命令
		if(msg.getType().equals(MsgTypeEnum.TEXT.getType())) {
			if(cmdHandler.startOrExit(msg.getFromUserName(), msg.getText())) {
				//设置用户名
				Config.getSingleton().setSelfUserName(msg.getFromUserName());
				return false;
			}
			if(cmdHandler.msgHandler(msg.getFromUserName(), msg.getText())) {//处理消息
				return false;
			}
			if(cmdHandler.chooseMode(msg.getFromUserName(), msg.getText())) {//模式选择
				return false;
			}
		}
		
		String userId = msg.getFromUserName();
		// 获取个人响应相关信息
		List<MessageAutoReply> messageAutoReplies = config.getMessageAutoReplies();
		for (MessageAutoReply messageAutoReply : messageAutoReplies) {
			if (messageAutoReply.getUserId().equals(userId)) {// 相等 发送响应消息
				// MessageTools.sendMsgById(me, id);
				if (messageAutoReply.getResultType() == ResultType.TEXT) {
					MessageTools.sendMsgById(messageAutoReply.getText(), userId);
				} else if (messageAutoReply.getResultType() == ResultType.PIC) {
					MessageTools.sendPicMsgByUserId(userId, messageAutoReply.getFilePath());
				}
				return false;
			}
		}

		// 获取所有的响应 
		MessageAutoReply all = config.getAll();
		if (all != null) {
			if (all.getResultType() == ResultType.TEXT) {
				MessageTools.sendMsgById(all.getText(), userId);
			} else if (all.getResultType() == ResultType.PIC) {
				MessageTools.sendPicMsgByUserId(userId, all.getFilePath());
			}
			return false;
		}
		// 拦截器
		if (config.getIntercptorMessage() != null) {
			if(config.getIntercptorMessage().isEmpty()) {
				log.info("拦截器已打开 后续处理被拦截");
				return false;
			}
			MessageTools.sendMsgById(config.getIntercptorMessage(), userId);
			return false;
		}
		return true;
	}
	@Override
	public boolean msgCheck(BaseMsg msg) {
		// TODO Auto-generated method stub
		if (msg.isGroupMsg())// 群消息不处理
			return false;
		if(!this.beforeCheckHandler(msg)) {// 内部消耗
			return false;
		}
		if (msg.getType().equals(MsgTypeEnum.TEXT.getType())) { // 消息是文本类型消息时 感谢zhouyafeng这个msg很完整
			if (msg.getText().equals("获取好友信息列表")) {
				List<JSONObject> contactList = WechatTools.getContactList();
				for (JSONObject jsonObject : contactList) {
					// 获取好友信息列表
					log.info(jsonObject.toJSONString());
				}
				return false;
			} else if (msg.getText().equals("获取我的信息")) { // 等自己如果向开源时将会 将此业务做成一个配置文件
				List<JSONObject> contactList = WechatTools.getContactList();
				for (JSONObject jsonObject : contactList) {
					// 获取我的信息列表
					if (jsonObject.getString("UserName").equals(msg.getFromUserName())) {
						String message = "";
						message += "昵称： " + jsonObject.getString("NickName") + "\r\n";
						message += "UserName: " + jsonObject.getString("UserName") + "\r\n";
						message += "头像地址: " + jsonObject.getString("HeadImgUrl") + "\r\n";
						WechatTools.sendMsgByUserName(message, msg.getFromUserName());
						return false;
					}
				}
				return false;
			}
		}
		return true;
	}
}
