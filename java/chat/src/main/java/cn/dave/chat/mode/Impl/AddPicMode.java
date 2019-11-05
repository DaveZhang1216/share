package cn.dave.chat.mode.Impl;

import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
public class AddPicMode implements CmdMode{

	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "获取添加国旗后的图像";
	}

	@Override
	public String operatorInfor() {
		// TODO Auto-generated method stub
		return "回复* 获取添加国旗后的图像";
	}

	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException {
		// TODO Auto-generated method stub
		/*Core core = Core.getInstance();
		if(modeBean.getResultType() == ResultType.TEXT) {
			List<JSONObject> friends = WechatTools.getContactList();
			String baseUrl = "https://" + core.getIndexUrl(); // 基础URL
			String skey = (String) core.getLoginInfo().get(StorageLoginInfoEnum.skey.getKey());
			for (int i = 0; i < friends.size(); i++) {
				JSONObject friend = friends.get(i);
				String url = baseUrl + friend.getString("HeadImgUrl") + skey;
				// String fileName = friend.getString("NickName");
				String headPicPath = path + File.separator + i + ".jpg";

				HttpEntity entity = myHttpClient.doGet(url, null, true, null);
				try {
					OutputStream out = new FileOutputStream(headPicPath);
					byte[] bytes = EntityUtils.toByteArray(entity);
					out.write(bytes);
					out.flush();
					out.close();

				} catch (Exception e) {
					LOG.info(e.getMessage());
				}

			}
		}*/
	}
	
}
