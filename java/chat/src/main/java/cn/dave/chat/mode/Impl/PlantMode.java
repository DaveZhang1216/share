package cn.dave.chat.mode.Impl;

import cn.dave.ai.bean.Reply;
import cn.dave.ai.robot.RecognizeType;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;

public class PlantMode extends AbstractMode{

	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "植物识别";
	}

	@Override
	protected ResultType needFileType() {
		// TODO Auto-generated method stub
		return ResultType.PIC;
	}

	@Override
	protected void replyToModeBean(Reply reply, ModeBean modeBean) {
		// TODO Auto-generated method stub
		modeBean.setResultType(ResultType.TEXT);
		modeBean.setText(reply.getText());
	}

	@Override
	protected Reply handler(String input) {
		// TODO Auto-generated method stub
		return robot.detect(input, RecognizeType.PLANT);
	}

}
