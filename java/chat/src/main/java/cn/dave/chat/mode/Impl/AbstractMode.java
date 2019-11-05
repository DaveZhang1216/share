package cn.dave.chat.mode.Impl;

import cn.dave.ai.bean.Reply;
import cn.dave.ai.robot.Robot;
import cn.dave.ai.robot.RobotFactory;
import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;

public abstract class AbstractMode implements CmdMode{

	protected static Robot robot = RobotFactory.getRobot();
	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException{
		// TODO Auto-generated method stub
		if(modeBean.getResultType() == this.needFileType()) {
			Reply reply = null;
			if(modeBean.getResultType() == ResultType.PIC) {
				reply = this.handler(modeBean.getFilePath());
			}else if(modeBean.getResultType()== ResultType.TEXT) {
				reply = this.handler(modeBean.getText());
			}
			if(reply.isUseLess()) {
				throw new ModeHandlerException("无法处理");
			}
			this.replyToModeBean(reply, modeBean);
		}else {
			throw new ModeHandlerException("给定的类型错误");
		}
	}
	@Override
	public String operatorInfor() {
		if(this.needFileType() == ResultType.PIC) {
			return "发送需要处理的图片";
		}else if(this.needFileType() == ResultType.TEXT) {
			return "发送需要处理的文字";
		}
		return "模式可能在调试阶段,发生了不可能的情况！";
	}
	/**
	 * 	指定能够处理的文件类型
	 * @return
	 */
	protected abstract ResultType needFileType();
	
	/**
	 * 	将机器人的响应 转换成模式响应
	 * @param reply
	 * @param modeBean
	 */
	protected abstract void replyToModeBean(Reply reply, ModeBean modeBean);
	/**
	 * @param input 输入
	 * @return  处理结果
	 */
	protected abstract Reply handler(String input);
}
