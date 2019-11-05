package cn.dave.chat.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.Impl.AnimalMode;
import cn.dave.chat.mode.Impl.AutoReplyMode;
import cn.dave.chat.mode.Impl.CarMode;
import cn.dave.chat.mode.Impl.ColorizeMode;
import cn.dave.chat.mode.Impl.DataBaseSaveMode;
import cn.dave.chat.mode.Impl.DishMode;
import cn.dave.chat.mode.Impl.DoubleMode;
import cn.dave.chat.mode.Impl.InterceptorMode;
import cn.dave.chat.mode.Impl.LandMarkMode;
import cn.dave.chat.mode.Impl.LogoMode;
import cn.dave.chat.mode.Impl.PlantMode;
import cn.dave.chat.mode.Impl.QueryIpMode;
import cn.dave.chat.mode.Impl.QueryMeterMode;
import cn.dave.chat.mode.Impl.RedWineMode;
import cn.dave.chat.mode.Impl.TimeAdviseMode;

//命令管理器
public class CmdManager {
	private static Logger log = LoggerFactory.getLogger(CmdManager.class);
	private static CmdManager cmdManager = new CmdManager();
	/**
	 * 	管理模式
	 */
	private List<CmdMode> cmdModes = new ArrayList<CmdMode>();
	
	public static CmdManager getSingleton() {
		return cmdManager;
	}
	private CmdManager() {
		cmdModes.add(new AutoReplyMode());
		cmdModes.add(new InterceptorMode());//拦截器
		cmdModes.add(new TimeAdviseMode());
		cmdModes.add(new ColorizeMode());
		cmdModes.add(new DoubleMode());
		cmdModes.add(new DishMode());
		cmdModes.add(new CarMode());
		cmdModes.add(new LogoMode());
		cmdModes.add(new AnimalMode());
		cmdModes.add(new PlantMode());
		cmdModes.add(new LandMarkMode());
		cmdModes.add(new RedWineMode());
		cmdModes.add(new QueryMeterMode());
		cmdModes.add(new DataBaseSaveMode());
		cmdModes.add(new QueryIpMode());
	}
	/**
	 * 	获取 操作方式
	 * @return
	 */
	public String getInfors() {
		String msg =  "控制指令如下： \r\n";
		for(int i=0;i<cmdModes.size();i++) {
			msg += "回复  "+ (i+1)+": "+cmdModes.get(i).getModeName()+" \r\n";
		}
		msg += "回复 #： 退出工具模式 \r\n";
		return msg;
	}
	
	public CmdMode getCmdModeById(int cmd) {
		if(cmd > 0) {
			log.info("试图获取模式-"+ cmd);
			if(cmdModes.size() >=cmd) {
				log.info("试图获取成功了吗  "+ Boolean.toString(cmdModes.get(cmd-1)==null));
				return cmdModes.get(cmd-1);
			}
		}
		return null;
	}
	/**
	 * 	进入摸个模式时使用
	 * @param cmd
	 * @return
	 */
	public String entryMode(int cmd) {
		if(cmd > 0) {
			if(cmdModes.size() >=cmd) {
				String infor = "已进入  "+cmdModes.get(cmd-1).getModeName()+"模式 \r\n";
				infor +="回复数字 切换响应模式 \r\n";
				infor +="回复#  返回上层菜单 \r\n";
				infor+= cmdModes.get(cmd-1).operatorInfor();
				return infor;
			}
		}
		return null;
	}
}
