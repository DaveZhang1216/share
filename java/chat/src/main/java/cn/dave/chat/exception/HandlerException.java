package cn.dave.chat.exception;

/**
 * 	无法继续处理消息时抛出
 * @author DaveZhang
 *
 */
public class HandlerException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6536877962601085629L;

	public HandlerException(String msg) {
		super(msg);
	}
}
