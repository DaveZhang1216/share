package cn.dave.ai.robot.test;


import java.io.File;

import org.junit.Test;

import cn.dave.utils.HashUtil;
import cn.dave.utils.Utils;

public class UtilTest {
	@Test
	public void hash32Test() {
		String hash = HashUtil.getHash_32("@@d052d34");
		System.out.println(hash);
		File file = new File("E://wechat/voicePath/1.jpg");
		System.out.println(file.getParent());
	}
	@Test
	public void numberTest() {
		double a = 113;
		float b = (float)a;
		System.out.println(b);
		System.out.println(Utils.isNum("43242410042904"));
		System.out.println(Utils.isNum("00002424324"));
		System.out.println(Utils.isNum("129847abc"));
	}
	@Test
	public void pathTest() {
		System.out.println(Utils.getParentPathByFilePath("E:\\wechat\\picPath\\xh.jpg"));
	}
	
}
