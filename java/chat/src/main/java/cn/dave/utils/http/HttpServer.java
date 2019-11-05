package cn.dave.utils.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.dave.chat.manager.Config;
import cn.dave.utils.CMD;
import cn.zhouyafeng.itchat4j.api.WechatTools;

public class HttpServer {
	private static Logger logger = LoggerFactory.getLogger(HttpServer.class);
	private static String ip;//自身ip 
	private static int port;//需要配置映射关系
	private static HttpServer httpServer = null;
	public static HttpServer createSingleton(String ip, int port) {
		if(httpServer == null) {
			HttpServer.ip = ip;
			HttpServer.port = port;
			httpServer = new HttpServer();
		}
		return httpServer;
	}
	
	public static HttpServer getSingleton() {
		return httpServer;
	}
	
	private HttpServer() {
		
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
	private String getInfoByIp(String ip) {
		return CMD.runPython("E:\\wechat\\python\\query.py", "ip "+ip);
	}
	public void startSimpleHttpServer() {
		try {
            /*监听端口号，只要是8888就能接收到*/
            ServerSocket ss = new ServerSocket(80);
            while (true) {
                /*实例化客户端，固定套路，通过服务端接受的对象，生成相应的客户端实例*/
                Socket socket = ss.accept();
                /*获取客户端输入流，就是请求过来的基本信息：请求头，换行符，请求体*/
                BufferedReader bd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                /**
                 * 接受HTTP请求，并解析数据
                 */
                String requestHeader;
                while ((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty()) {
                    /**
                    * 获得GET参数
                     */
                    if (requestHeader.startsWith("GET")) {
                        int begin = requestHeader.indexOf("/toupiao/") + 1;
                        if(begin >0) {
                        	int end = requestHeader.indexOf("HTTP/");
                            String url = requestHeader.substring(begin, end);
                            String[] split = url.split("/");
                            if(split.length==2) {
                            	String userName = split[1];
                            	int indexOf = userName.indexOf("?");
                            	if(indexOf >0) {
                            		userName = userName.substring(0,indexOf);
                            	}
                            	List<JSONObject> contactList = WechatTools.getContactList();
                            	String infor = "";
                    			for(JSONObject jsonObject : contactList) {
                    				if(jsonObject.getString("UserName").equals(userName)) {
                    					//找到了NickName
                    					infor = "nickName = "+ jsonObject.getString("NickName");
                    					WechatTools.sendMsgByUserName("谢谢.....   ",  userName);
                    				}
                    			}
                            	if(infor.isEmpty()) {
                            		infor = "userName = "+userName;
                            	}
                            	String ipInfo = getInfoByIp(socket.getInetAddress().getHostAddress());
                            	if(ipInfo.equals("None")) {
                            		String info = infor + "\r\n"+"ip: "+socket.getInetAddress().getHostAddress();
                            		WechatTools.sendMsgByUserName(info,
                            				Config.getSingleton().getSelfUserName());
                            	}else {
                            		String info = infor + "\r\n"+"info = "+ ipInfo;
                            		WechatTools.sendMsgByUserName(info, Config.getSingleton().getSelfUserName());
                            	}
                            }
                        }
                    }
                }
                /*发送回执*/
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                pw.println("HTTP/1.1 200 OK");
                pw.println("Content-type:text/html;charset=UTF-8");
                pw.println();
                pw.println("<h1> 感谢您 ,  珍贵的一票  </h1>");
                pw.flush();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
