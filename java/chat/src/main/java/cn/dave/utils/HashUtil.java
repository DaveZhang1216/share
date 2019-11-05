package cn.dave.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
	/**
	 *  通过数据获取32位hash值
	 * @param data
	 * @return
	 */
	public static String getHash_32(String data) {
		 MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}//1.初始化MessageDigest信息摘要对象,并指定为MD5不分大小写都可以
         md.update(data.getBytes());//2.传入需要计算的字符串更新摘要信息，传入的为字节数组byte[],将字符串转换为字节数组使用getBytes()方法完成
         byte b[] = md.digest();//3.计算信息摘要digest()方法,返回值为字节数组
         int i;//定义整型
         //声明StringBuffer对象
           StringBuffer buf = new StringBuffer("");
           for (int offset = 0; offset < b.length; offset++) {
             i = b[offset];//将首个元素赋值给i
             if (i < 0)
               i += 256;
             if (i < 16)
               buf.append("0");//前面补0
             buf.append(Integer.toHexString(i));//转换成16进制编码
           }
           return buf.toString();//转换成字符串
	}
}
