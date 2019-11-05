package cn.dave.chat;

import cn.dave.chat.exception.HandlerException;

public interface ChatHandler {
	/**
	 * 	 文字信息处理器
	 * @param text 收到的文字信息
	 * @param userId 微信用户标识号
	 * @return 对文字进行响应
	 * @throws 处理系统出现异常时抛出
	 */
	public String textHandler(String text,String userId) throws HandlerException;
	
	/**
	 * @param filePath   MP3格式语音所在地址
	 * @param userId  用户信息
	 * @return 解析后输出
	 */
	public String voiceHandler(String filePath,String userId) throws HandlerException;
	
	/**
	 * @param filePath 图片路径
	 * @param userId
	 * @return
	 * @throws HandlerException
	 */
	public String picHandler(String filePath,String userId) throws HandlerException;
}
