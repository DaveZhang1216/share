package cn.dave.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import com.baidu.aip.util.Base64Util;

public class Base64Utils {
	public static boolean Base64ToImage(String imgStr, String imgFilePath) {
		if(StringUtils.isEmpty(imgStr)) {
			return false;
		}
		try {
			byte[] b = Base64Util.decode(imgStr);
			for(int i=0;i<b.length;i++) {
				if(b[i]<0) {
					b[i]+=256;
				}
			}
			OutputStream outputStream = new FileOutputStream(imgFilePath);
			outputStream.write(b);
			outputStream.flush();
			outputStream.close();
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
}
