package cn.dave.chat.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.events.StartDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.manager.Config;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;
import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;

/**
 * 命令处理器
 * 
 * @author DaveZhang
 *
 */
public class CmdHandler {
	private static Logger log = LoggerFactory.getLogger(CmdHandler.class);
	private static CmdHandler cmdHandler = new CmdHandler();
	private CmdManager cmdManager = CmdManager.getSingleton();

	public static CmdHandler getSingleton() {
		return cmdHandler;
	}

	// 用户 状态
	/**
	 */
	private Map<String, Integer> states = new HashMap<String, Integer>();

	private CmdHandler() {

	}

	/**
	 * @param userName
	 * @param cmd
	 * @return 如果是启动或者推出指令返回true 否则返回false
	 */
	public boolean startOrExit(String userName, String cmd) {
		if (cmd.contains("图像") || cmd.contains("图片") || cmd.contains("菜单")) { // 图像处理
			WechatTools.sendMsgByUserName(cmdManager.getInfors(), userName);
			states.put(userName, 0);
			return true;
		}
		if (cmd.equals("#")) { // 是否推出
			Integer state = states.get(userName);
			if (state != null) {
				if (state == 0) {
					states.remove(userName);// 推出模式
					WechatTools.sendMsgByUserName("退出成功 ", userName);
					return true;
				} else {
					states.put(userName, 0); // 返回上级菜单
					WechatTools.sendMsgByUserName(cmdManager.getInfors(), userName);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 模式选择
	 * 
	 * @param userName
	 * @param cmd
	 * @return 选择模式则返回true 否则返回false
	 */
	public boolean chooseMode(String userName, String cmd) {
		// 启动模式的方式
		Integer state = states.get(userName);
		if (state != null) {// state == 0
			try {
				Integer parseInt = Integer.parseInt(cmd);
				String entryMode = cmdManager.entryMode(parseInt);
				if (entryMode != null) {
					log.info("放入了    " + parseInt);
					states.put(userName, parseInt);
					WechatTools.sendMsgByUserName(entryMode, userName);
					return true;
				} else {
					// MessageTools.sendMsgById("无法识别的命令", userName);
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				// 发生错误
				// MessageTools.sendMsgById("无法识别的命令", userName);
				return false;
			}
		} else { // 否则
			return false;
		}
	}

	/**
	 * @param userName
	 * @param msg 
	 * @return 命令成功处理返回true 否则 返回false
	 */
	public boolean msgHandler(String userName, String msg) {
		CmdMode state = this.getState(userName); // 检查是否进入模式 命令只有文本信息
		if (state != null) {
			ModeBean modeBean = new ModeBean();
			modeBean.setResultType(ResultType.TEXT);
			modeBean.setText(msg);
			modeBean.setUserId(userName);
			try {
				state.modeHandler(modeBean);
				if (modeBean.getResultType() == ResultType.TEXT) {
					WechatTools.sendMsgByUserName(modeBean.getText(), userName);
					return true;
				}else if(modeBean.getResultType() == ResultType.PIC){
					//WechatTools.sendMsgByUserName("不可能的结果", msg.getFromUserName());
					MessageTools.sendPicMsgByUserId(userName, modeBean.getFilePath());
					return true;
				}else if(modeBean.getResultType() == ResultType.File) {
					MessageTools.sendFileMsgByUserId(userName, modeBean.getFilePath());
					return true;
				}
			} catch (ModeHandlerException e) {
				// TODO Auto-generated catch block
				MessageTools.sendMsgById(e.getMessage(), userName);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return 如果无模式返回 null 否则返回模式
	 */
	public CmdMode getState(String userName) {
		Integer integer = states.get(userName);
		log.info("是否有缓存： " + Boolean.toString(integer != null));
		if (integer == null) {
			return null;
		} else {
			log.info("有缓存  不知道是否能获取到此模式");
			return cmdManager.getCmdModeById(integer);
		}
	}

}
