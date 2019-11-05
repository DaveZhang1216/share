package cn.dave.chat.mode.Impl;

import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;

public class QuestionMode implements CmdMode{

	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "信息";
	}

	@Override
	public String operatorInfor() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException {
		// TODO Auto-generated method stub
		
	}

}
