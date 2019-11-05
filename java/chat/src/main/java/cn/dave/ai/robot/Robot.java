package cn.dave.ai.robot;

import cn.dave.ai.bean.Reply;

public interface Robot {
	/**
	 * 对文字进行识别
	 * @return
	 */
	public Reply textDiscernment(String language,String userId);
	/**
	 * 翻译语音记录
	 * @return
	 */
	public Reply speechReply(String filePath);
	
	/**
	 * 将文字 使用声音合成技术将出来 合成出来的声音放在filePath中
	 * @param text
	 * @param path 传入目录
	 * @return
	 */
	public Reply speechText(String text,String path);
	
	/**
	 * 识别文字
	 * @param path
	 * @return
	 */
	public Reply orc(String path);
	
	/**
	 * 	无损放大图像
	 *       放大的图片在reply.getFilePath()获取
	 * @return
	 */
	public Reply doubleImage(String image);
	/**
	 * 	图像彩色化
	 * 	图片在reply.getFilePath()获取
	 * @param image
	 * @return
	 */
	public Reply colorImage(String image);
	
	/**
	 * 	图片识别
	 * @param image
	 * @param recognizeType
	 * @return
	 */
	public Reply detect(String image, RecognizeType recognizeType);
}
