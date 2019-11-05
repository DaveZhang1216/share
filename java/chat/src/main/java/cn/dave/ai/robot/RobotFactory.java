package cn.dave.ai.robot;

import cn.dave.ai.robot.impl.DefaultRobot;

public class RobotFactory{
	private static Robot robot = new DefaultRobot();
	/**
	 * 	获取一个图灵机器人
	 * @return
	 */
	public static Robot getRobot() {
		return robot;
	}
	
}
