package cn.dave.chat.mode.Impl;

import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.manager.Config;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;

public class InterceptorMode implements CmdMode{

	private Config config = Config.getSingleton();
	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "拦截器配置";
	}

	@Override
	public String operatorInfor() {
		// TODO Auto-generated method stub
		if(config.getIntercptorMessage() != null) {
			return "回复 * 关闭拦截器";
		}else {
			return "回复 * 开启拦截器";
		}
	}
	
	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException {
		// TODO Auto-generated method stub
		if(modeBean.getResultType() == ResultType.TEXT) {
			if(modeBean.getText().contains("*")) {
				if(config.getIntercptorMessage() == null) {
					config.setIntercptorMessage("");
					modeBean.setText("拦截器成功开启");
				}else {
					config.closeIntercptor();
					modeBean.setText("拦截器关闭成功 \r\n 回复 # 返回上级菜单");
				}
				return;
			}
		}
		throw new ModeHandlerException("不可识别的类型");
	}

}
