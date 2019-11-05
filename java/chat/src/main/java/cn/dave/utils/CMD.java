package cn.dave.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class CMD {
	public static void main(String[] args) {
		String cmd = CMD.runPython("E:\\wechat\\python\\query.py", "ip 47.105.93.170");
		System.out.println(cmd);
	}
	public static String runPython(String filePath, String args) {
		return getCmd("cmd /c python "+filePath+" "+args);
	}
	public static String getCmd(String cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			String ret="";
			while((line = reader.readLine()) !=null) {
				ret = ret+line;
			}
			reader.close();
			process.getOutputStream().close();
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
