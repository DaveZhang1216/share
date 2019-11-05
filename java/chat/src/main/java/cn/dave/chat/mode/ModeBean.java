package cn.dave.chat.mode;

/**
 *  	模式处理结果 
 * @author DaveZhang
 *
 */
public class ModeBean {
	private String userId;
	private ResultType resultType; //类型
	private String text; //文本
	private String filePath; //文件
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
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
