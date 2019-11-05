package cn.dave.chat.bean;

import cn.dave.chat.mode.ResultType;

/**
 * 	自动回复消息
 * @author DaveZhang
 *
 */
public class MessageAutoReply {
	private ResultType resultType;//自动回复的类型
	private String text; //自动回复的消息
	private String filePath;//自动回复的文件或图片
	private String object; //对象
	
	public ResultType getResultType() {
		return resultType;
	}
	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getUserId() {
		return object; //昵称
	}
	public void setUserId(String object) {
		this.object = object;
	}
	public void setObjectAll() {
		this.object = "MESSAGEAUTOREPLYALLPERSON";
	}
	public boolean isAll() {
		return this.object.equals("MESSAGEAUTOREPLYALLPERSON");
	}
	
}
