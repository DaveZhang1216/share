package cn.dave.chat.manager;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.context.support.StaticApplicationContext;

import cn.dave.chat.bean.MessageAutoReply;
import cn.dave.chat.bean.TimeAdvise;


/**
 * 	对微信的配置
 * @author DaveZhang
 */
public class Config {
	private static Config config = new Config();
	private static String intercptorMessage;//拦截器消息
	private String selfUserName;
	List<TimeAdvise> timeAdvises = new ArrayList<>();//定时通知
	
	//配置自动回复
	List<MessageAutoReply> messageAutoReplies = new ArrayList<MessageAutoReply>();
	private MessageAutoReply all = null;
	
	//查询定时发送
	
	//配置定时发送  指定时间 对象 消息（暂时仅支持文本）  
	public static Config getSingleton() {
		return config;
	}
	private Config() {
		
	}
	
	public String getSelfUserName() {
		return this.selfUserName;
	}
	public void setSelfUserName(String selfUserName) {
		this.selfUserName = selfUserName;
	}
	/**
	 * 	添加一个自动回复 
	 * @param messageAutoReply
	 */
	public void addMessageAutoReplies(MessageAutoReply messageAutoReply) {
		if(messageAutoReply.isAll()) {//需要设置的对象是所有人
			this.all = messageAutoReply;
		}else {//个人的时候
			for(MessageAutoReply mid : messageAutoReplies) { //找到了就返回
				if(mid.getUserId().equals(messageAutoReply.getUserId())) {
					mid.setResultType(messageAutoReply.getResultType());
					mid.setFilePath(messageAutoReply.getFilePath());
					mid.setText(messageAutoReply.getText());
					return;
				}
			}
			//新用户
			messageAutoReplies.add(messageAutoReply);
		}
	}
	public void clearMessageAutoReplies() {
		messageAutoReplies.clear();
		all = null;
	}
	public List<MessageAutoReply> getMessageAutoReplies() {
		return messageAutoReplies;
	}
	public MessageAutoReply getAll() {
		return all;
	}
	/**
	 * 添加一个 定时通知
	 * @param timeAdvise
	 */
	public void addTimeAdvise(TimeAdvise timeAdvise) {
		this.timeAdvises.add(timeAdvise);
	}
	public List<TimeAdvise> getTimeAdvises() {
		return timeAdvises;
	}
	/**
	 * 如果 拦截器存在则返回 否则 返回null
	 * @return
	 */
	public String getIntercptorMessage() {
		return intercptorMessage;
	}
	/**
	 * 打开拦截器并  设置拦截器响应信息 
	 * @param intercptorMsg
	 */
	public void setIntercptorMessage(String intercptorMsg) {
		intercptorMessage = intercptorMsg;
	}
	/**
	 * 关闭拦截器
	 */
	public void closeIntercptor() {
		intercptorMessage = null;
	}
}
