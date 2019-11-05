package cn.dave.chat.mode;

import cn.dave.chat.exception.ModeHandlerException;

public interface CmdMode {
	
	/**
	 * 	需要定义一个 模式名称
	 * @return
	 */
	public String getModeName();
	
	/**
	 * 选中此模式时的操作提示
	 * @return
	 */
	public String operatorInfor();
	
	/**
	 * 
	 * @param modeBean 输入  输出
	 * @return
	 * @throws ModeHandlerException 传入非预期则抛出错误
	 */
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException;
	
	//public void entryHandler(ModeBean modeBean);
	
}
