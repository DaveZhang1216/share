package cn.dave.chat.mode.Impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.manager.Config;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;
import cn.dave.utils.http.HttpServer;
import cn.zhouyafeng.itchat4j.api.WechatTools;

public class QueryIpMode implements CmdMode{

	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "对象信息收集";
	}

	@Override
	public String operatorInfor() {
		// TODO Auto-generated method stub
		return "请输入查询对象的昵称";
	}

	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException {
		// TODO Auto-generated method stub
		if(modeBean.getResultType() == ResultType.TEXT) {
			List<JSONObject> contactList = WechatTools.getContactList();
			for(JSONObject jsonObject : contactList) {
				if(jsonObject.getString("NickName").equals(modeBean.getText())) {
					//找到了
					WechatTools.sendMsgByUserName("点击连接, 帮忙投个票 （^-^）  \r\n"
					+"http://"+HttpServer.getSingleton().getIp()+":"+HttpServer.getSingleton().getPort()
					+"/"+"toupiao/"+jsonObject.getString("UserName"), jsonObject.getString("UserName"));
					modeBean.setText("链接已经发送等待用户确认后将告知结果");
					return;
				}
			}
		}else {
			modeBean.setResultType(ResultType.TEXT);
			modeBean.setText("当前模式不支持文字以外的回复！！！！");
		}
	}

}
