package cn.dave.utils;

import java.io.File;
import java.util.List;

import cn.dave.ai.bean.Words;

public class Utils {
	/**
	 * 是否为纯数字
	 * @param text
	 * @return
	 */
	public static boolean isNum(String text) {
		return text.matches("[0-9]+");
	}
	
	/**
	 * 字符串 前面的0 去除
	 * @param str
	 * @return
	 */
	public static String deletePreZero(String str) {
		String replaceAll = str.replaceAll("^(0+)", "");
		return replaceAll;
	}
	
	/**
	 * 再文本内容中找到表号 没有找到返回null
	 * @param lists
	 * @return
	 */
	public static String anaysisMeterNum(List<Words> lists) {
		for(Words words : lists) {
			if(isNum(words.getContent())) {//内容一定要为文本
				String newContext = deletePreZero(words.getContent());
				if(newContext.length() >= 7) {
					if(words.getMin()>0.7)
						return newContext; //表号
				}
			}
		}
		return null;
	}
	/**
	 * 	获取文件所在文件夹
	 * @param filePath
	 * @return
	 */
	public static String getParentPathByFilePath(String filePath) {
		File file = new File(filePath);
		return file.getParent();
	}
	
	public static String bytesToHexString(byte[] src,int len) {
		return bytesToHexString(src, 0, len);
	}
	
	public static String bytesToHexStringForWatch(byte[] src,int len) {
		return bytesToHexString(src, 0, len, "-");
	}
	/**
	 * 
	 * @param src 数据源
	 * @param offset 数据偏移
	 * @param len 数据长度
	 * @param separator  数据间分割符号
	 * @return
	 */
	public static String bytesToHexString(byte[] src, int offset, int len,String separator) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || len <= 0) {
			return null;
		}
		for (int i = offset; i < offset+len; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			if(i<len-1) {//不是最后一个
				stringBuilder.append(separator);
			}
		}
		return stringBuilder.toString();
	}
	public static String bytesToHexString(byte[] src, int offset, int len) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || len <= 0) {
			return null;
		}
		for (int i = offset; i < offset+len; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
