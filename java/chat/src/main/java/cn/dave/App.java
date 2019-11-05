package cn.dave;

import cn.dave.ai.robot.Robot;
import cn.dave.ai.robot.RobotFactory;
import cn.dave.chat.Chat;
import cn.dave.chat.ChatHandler;
import cn.dave.chat.impl.ChatHandlerImpl;
import cn.dave.chat.impl.WeChatImpl;
import cn.dave.utils.CMD;
import cn.dave.utils.http.HttpServer;

public class App {
	private static String getSelfIp() {
		return CMD.runPython("E:\\wechat\\python\\query.py", "selfip");
	}
	public static void main(String[] args) {
		HttpServer createSingleton = HttpServer.createSingleton(getSelfIp(), 8889);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				createSingleton.startSimpleHttpServer();
			}
		}).start();
		/**
		 *	 获取图灵智能机器人 		
		 */
		Robot robot = RobotFactory.getRobot();
		/**
		 * 	对文本、声音、图片消息的处理 接口
		 * 	robot 选择了各类网络机器人   以及tensorflow训练的模型
		 */
		ChatHandler chatHandler = new ChatHandlerImpl(robot);
		/**
		 *     聊天工具选用微信   可以选用 webAPI 实现版的  itchat4j
		 *     chatHandler 对消息的处理交给 它
		 *     qrPath 二维码保存路径启动后打开图片扫描登录
		 *     voicePath 语音保存路径
		 *     vedioPath 视频保存地址
		 *     picPath 图片保存路径
		 *     mediaPath文件保存路径
		 **/
		Chat chat = new WeChatImpl(chatHandler, "E://wechat/qrPath", "E://wechat/voicePath",
					"E://wechat/vedioPath", "E://wechat/picPath", "E://wechat/mediaPath");
		chat.start();  //**启动后如果未弹出扫码图片  到指定qrPath下扫描二维码登录微信
	}
}
