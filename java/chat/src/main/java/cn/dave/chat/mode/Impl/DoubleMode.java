package cn.dave.chat.mode.Impl;

import cn.dave.ai.bean.Reply;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;

public class DoubleMode extends AbstractMode{

	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "图片无损放大";
	}

	@Override
	protected ResultType needFileType() {
		// TODO Auto-generated method stub
		return ResultType.PIC;
	}

	@Override
	protected void replyToModeBean(Reply reply, ModeBean modeBean) {
		// TODO Auto-generated method stub
		modeBean.setFilePath(reply.getFilePath());
	}

	@Override
	protected Reply handler(String input) {
		// TODO Auto-generated method stub
		return robot.doubleImage(input);
	}

}
