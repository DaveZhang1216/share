package cn.dave.chat.mode.Impl;


import java.util.List;

import cn.dave.chat.bean.TimeAdvise;
import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.manager.Config;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;
import cn.zhouyafeng.itchat4j.api.MessageTools;

public class TimeAdviseMode implements CmdMode{

	Thread thread = null;
	
	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "定时通知模式";
	}

	@Override
	public String operatorInfor() {
		// TODO Auto-generated method stub
		String retString =  "输入时间间隔(#开头)：单位分钟 \r\n ";
		retString+= "如： #1 1分钟通知一次";
		return retString;
		
	}

	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException {
		// TODO Auto-generated method stub
		if(modeBean.getResultType() == ResultType.TEXT) {
			if(thread ==null) {
				thread= new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while(true) {
							try {
								Thread.sleep(60*1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							List<TimeAdvise> timeAdvises = Config.getSingleton().getTimeAdvises();
							for(TimeAdvise timeAdvise : timeAdvises) {
								if(timeAdvise.needSend()) {
									MessageTools.sendMsgById("在线中！！！", timeAdvise.getUserId());
								}
							}
						}
					}
				});
				thread.start();
			}
			try {
				String text = modeBean.getText();
				text = text.replace("#", "");
				int min = Integer.parseInt(text);
				List<TimeAdvise> timeAdvises = Config.getSingleton().getTimeAdvises();
				for(TimeAdvise midAdvise :timeAdvises) {
					if(midAdvise.getUserId().equals(modeBean.getUserId())) {//已经存在
						midAdvise.setTime(min);//重新设置
						modeBean.setText("配置成功");
						return;
					}
				}
				timeAdvises.add(new TimeAdvise(min, modeBean.getUserId()));
				modeBean.setText("配置成功");
				return;
			}catch (Exception e) {
				// TODO: handle exception
				modeBean.setText("必须为纯数字");
				return;
			}
		}else {
			modeBean.setResultType(ResultType.TEXT);
			modeBean.setText("必须为纯数字");
			return;
		}
	}

}
