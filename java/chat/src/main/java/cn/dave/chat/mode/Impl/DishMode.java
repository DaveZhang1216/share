package cn.dave.chat.mode.Impl;

import java.util.List;

import cn.dave.ai.bean.Good;
import cn.dave.ai.bean.Reply;
import cn.dave.ai.robot.RecognizeType;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;

public class DishMode extends AbstractMode{

	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "菜品识别";
	}

	@Override
	protected ResultType needFileType() {
		// TODO Auto-generated method stub
		return ResultType.PIC;
	}

	@Override
	protected void replyToModeBean(Reply reply, ModeBean modeBean) {
		// TODO Auto-generated method stub
		List<Good> goods = reply.getGoods();
		if(!goods.isEmpty()) {
			modeBean.setResultType(ResultType.TEXT);
			modeBean.setText( "品名为： "+goods.get(0).getName()+"  卡路里： "+goods.get(0).getCalorie());
		}
	}

	@Override
	protected Reply handler(String input) {
		// TODO Auto-generated method stub
		return robot.detect(input, RecognizeType.DISH);
	}

}
