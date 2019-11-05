package cn.dave.chat.mode.Impl;

import java.util.List;
import com.alibaba.fastjson.JSONObject;

import cn.dave.chat.bean.MessageAutoReply;
import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.manager.Config;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;
import cn.zhouyafeng.itchat4j.api.WechatTools;

public class AutoReplyMode implements CmdMode{

	private int step = 0; //0 代表还没设置
	private String nickName; //昵称
	private MessageAutoReply messageAutoReply; //消息自动回复
	private Config config = Config.getSingleton();
	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "自动回复配置";
	}
	
	
	@Override
	public String operatorInfor() {
		// TODO Auto-generated method stub
		if(step == 0) {// 第 0  步
			messageAutoReply = new MessageAutoReply();//新建对象
			String msg = "发送自动回复对象的昵称 \r\n ";
			msg+= "回复 * 将对所有人有效 \r\n";
			msg+="回复 清空 可清除所有配置过的自动回复 \r\n";
			msg+= "回复  查看 可查看配置信息";
			return msg;
		}else {
			String message =  "存在正在配置的对象昵称为： "+nickName+"\r\n";
			message+= "发送自动回复的文文字或图片";
			return message;
		}
	}
	
	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException {
		// TODO Auto-generated method stub
		//判断是否为清理命令
		if(modeBean.getResultType() == ResultType.TEXT) {
			if(modeBean.getText().equals("清空")) {
				step = 0;
				messageAutoReply = null;
				config.clearMessageAutoReplies();//收到清空则直接清空
				modeBean.setText("自动回复已清理");
				return ;
			}else if(modeBean.getText().equals("查看")) {
				List<MessageAutoReply> messageAutoReplies = config.getMessageAutoReplies();
				String ret = "信息如下： \r\n";
				for(MessageAutoReply mid : messageAutoReplies) {//所有
					ret +="昵称： "+ mid.getUserId()+"\r\n";
					if(mid.getResultType() == ResultType.TEXT) {
						ret+="自动回复内容:  "+mid.getText()+"\r\n \r\n";
					}else if(mid.getResultType() == ResultType.PIC){
						ret+="自动回复内容为：图片 \r\n \r\n";
					}
				}
				
				MessageAutoReply all = config.getAll();
				if(all != null) {
					ret+="自动回复对象： 所有人 \r\n";
					if(all.getResultType() == ResultType.TEXT) {
						ret+="自动回复内容:  "+all.getText()+"\r\n \r\n";
					}else if(all.getResultType() == ResultType.PIC){
						ret+="自动回复内容为：图片 \r\n \r\n";
					}
				}
				modeBean.setText(ret);
				return;
			}
			
		}
		//什么都还没做
		if(step == 0) {// 0步
			//指定对象
			if(modeBean.getResultType() != ResultType.TEXT) { 
				throw new ModeHandlerException("发送的消息类型错误");
			}
			if(modeBean.getText().equals("*")) {//设置所有人
				messageAutoReply.setObjectAll();
				String msg = "自动回复对象为： 所有人 \r\n";
				msg+= "发送自动回复的文文字或图片";
				modeBean.setText(msg);
				step = 1;
				return;
			}else {
				List<JSONObject> contactList = WechatTools.getContactList();
				for(JSONObject jsonObject : contactList) {
					if(jsonObject.getString("NickName").equals(modeBean.getText())) {
						//找到了
						nickName = modeBean.getText();
						messageAutoReply.setUserId(jsonObject.getString("UserName"));
						String msg = "自动回复对象昵称： "+modeBean.getText()+"\r\n";
						msg+= "设置自动回复的图片或文字";
						modeBean.setText(msg);
						step = 1;
						return;
					}
				}
				modeBean.setText("配置对象失败  没有找到改昵称");
				return;
			}
		}else { //直接配置信息
			messageAutoReply.setResultType(modeBean.getResultType());
			messageAutoReply.setFilePath(modeBean.getFilePath());
			messageAutoReply.setText(modeBean.getText());
			config.addMessageAutoReplies(messageAutoReply);
			step = 0;
			messageAutoReply = null;
			modeBean.setResultType(ResultType.TEXT);
			modeBean.setText("配置成功");
		}
		
	}
	
}
