package cn.dave.chat.mode.Impl;

import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;
import cn.dave.utils.AES;
import cn.dave.utils.Utils;

public class AESDEMode implements CmdMode{

	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "AES解密";
	}

	@Override
	public String operatorInfor() {
		// TODO Auto-generated method stub
		return "发送需要解密的16进制的字符串以及密钥\r\n 格式： src:解密数据  key:密钥";
	}

	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException {
		// TODO Auto-generated method stub
		if(modeBean.getResultType() != ResultType.TEXT) {
			throw new ModeHandlerException("仅支持文本模式");
		}
		String s = modeBean.getText();
		int indexOf = s.indexOf("src:");
		int indexOf2 = s.indexOf("key:");
		if(indexOf==-1 || indexOf2 == -1) {
			throw new ModeHandlerException("格式错误");
		}
		String src = null;
		if(indexOf > indexOf2) {//src 在后面
			 src = s.substring(indexOf+4,indexOf2);
		}else {//src在前面
			
		}
		
		src = src.replace(" ", "");
		src = src.replace("-", "");
		try {
			 byte[] decrypt = AES.Decrypt(src, "key");
			modeBean.setText(Utils.bytesToHexStringForWatch(decrypt,decrypt.length));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			modeBean.setText("机密出错:"+ e.getMessage());
			return ;
		}
		
	}

}
