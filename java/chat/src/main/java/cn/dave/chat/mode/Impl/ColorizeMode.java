package cn.dave.chat.mode.Impl;

import cn.dave.ai.bean.Reply;
import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;

/**
 * 	上色模式，给图片上色
 * @author DaveZhang
 *
 */
public class ColorizeMode extends AbstractMode{

	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "黑白图片上色";
	}

	@Override
	protected ResultType needFileType() {
		// TODO Auto-generated method stub
		return ResultType.PIC;//需要文件
	}
	//将机器人转换为模式Bean返回
	@Override
	protected void replyToModeBean(Reply reply, ModeBean modeBean) {
		// TODO Auto-generated method stub
		
		modeBean.setFilePath(reply.getFilePath());
	}
	//使用机器人进行颜色识别
	@Override
	protected Reply handler(String input) {
		// TODO Auto-generated method stub
		return robot.colorImage(input);
	}

	
	




}
