package cn.dave.chat.mode.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;
import cn.zhouyafeng.itchat4j.api.MessageTools;

public class DataBaseSaveMode implements CmdMode{
	/**
	 * 获取response要下载的文件的默认路径
	 * @param response
	 * @return
	 */
	public static String getFilePath(HttpResponse response) {
		String filepath = "E://wechat/mediaPath//";
		String filename = getFileName(response);
 
		if (filename != null) {
			filepath += filename;
		} else {
			filepath += "unknow";
		}
		return filepath;
	}
	/**
	 * 获取response header中Content-Disposition中的filename值
	 * @param response
	 * @return
	 */
	public static String getFileName(HttpResponse response) {
		Header contentHeader = response.getFirstHeader("Content-Disposition");
		String filename = null;
		if (contentHeader != null) {
			HeaderElement[] values = contentHeader.getElements();
			if (values.length == 1) {
				NameValuePair param = values[0].getParameterByName("filename");
				if (param != null) {
					try {
						//filename = new String(param.getValue().toString().getBytes(), "utf-8");
						//filename=URLDecoder.decode(param.getValue(),"utf-8");
						filename = param.getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return filename;
	}

	private String download(String url) {
		try {
			HttpClient client = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = client.execute(httpget);
 
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			
			String filepath = getFilePath(response);
			File file = new File(filepath);
			file.getParentFile().mkdirs();
			FileOutputStream fileout = new FileOutputStream(file);
			/**
			 * 根据实际运行效果 设置缓冲区大小
			 */
			byte[] buffer=new byte[1000000];
			int ch = 0;
			while ((ch = is.read(buffer)) != -1) {
				fileout.write(buffer,0,ch);
			}
			is.close();
			fileout.flush();
			fileout.close();
			return filepath;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "数据库备份";
	}

	@Override
	public String operatorInfor() {
		// TODO Auto-generated method stub
		return "回复*确认数据库备份";
	}

	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException {
		// TODO Auto-generated method stub
		if(modeBean.getResultType()==ResultType.TEXT) {
			if(modeBean.getText().equals("*")) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String filePath = download("http://47.105.93.170:9999/data/download/database");
						if(filePath!=null) {
							MessageTools.sendFileMsgByUserId(modeBean.getUserId(), filePath);
							MessageTools.sendMsgById("已备份至电脑如文件太大将无法发送至微信", modeBean.getUserId());
						}else {
							MessageTools.sendMsgById("备份失败", modeBean.getUserId());
						}
					}
				}).start();
				modeBean.setText("备份完毕后自动通知");
				return;
			}
		}
		modeBean.setResultType(ResultType.TEXT);
		modeBean.setText("无法识别的指令");
	}
}
