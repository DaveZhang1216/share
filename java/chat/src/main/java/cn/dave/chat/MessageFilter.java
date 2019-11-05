package cn.dave.chat;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;

/**
 * 
 * @author DaveZhang
 *
 */
public interface MessageFilter {
	/**
	 * @param msg
	 * @return 返回true 则处理 否则不需要处理
	 */
	public boolean msgCheck(BaseMsg msg);
}
