package cn.dave.chat.mode.Impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.dave.acol.api.DeviceService;
import cn.dave.acol.api.bean.MeterDto;
import cn.dave.ai.bean.Reply;
import cn.dave.ai.robot.Robot;
import cn.dave.ai.robot.RobotFactory;
import cn.dave.chat.exception.ModeHandlerException;
import cn.dave.chat.mode.CmdMode;
import cn.dave.chat.mode.ModeBean;
import cn.dave.chat.mode.ResultType;
import cn.dave.utils.Utils;

public class QueryMeterMode implements CmdMode{

	private DeviceService deviceService = new DeviceService("http://www.acolscada.cn:7777");
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Robot robot = RobotFactory.getRobot();
	private String meterQuery(String meterNum) {
		//分析到表号后 获取表号
		if(meterNum != null) {
			//获取表号
			MeterDto meter = deviceService.getMeterByMeterNum(meterNum);//找寻表
			if(meter == null) {
				return "解析出来的表具号为： "+meterNum+"但未找到该表具";
			}
			String ret = "-------表具信息区域--------\r\n";
			ret = ret+"表具编号为： "+meter.getMeterNum()+"\r\n" + 
					"表具类型： "+meter.getMeterType()+"\r\n" +
					"表具规格： "+meter.getStandard()+"\r\n" +
					"表具生产日期： "+df.format(new Date(meter.getProduceTime()))+"\r\n";
			if(meter.isBind()) {
				//采集设备
				if(meter.getDeviceType().equals("采集器")) {
					ret += "采集器编号： "+ meter.getDeviceNum()+"\r\n";
				}else {
					ret+= "设备类型： 预付费类型\r\n";
				}
			}
			if(meter.isUsing()){
				ret+="-----------用户信息区域-----------\r\n";
				ret+=  "用户名： "+meter.getUserName()+"\r\n";
				ret+=  "用户地址： "+meter.getUserAddress()+"\r\n";
			}
			if(meter.getLastCon() > 10000000L) {
				ret+="----------最新记录区域-----------\r\n";
				ret+="上传时间： "+df.format(new Date(meter.getLastCon()))+"\r\n";
				ret+="标况总量： "+meter.getScSum()+"\r\n";
				ret+="标况流量： "+meter.getScFlow()+"\r\n";
				ret+="温度： "+ meter.getTemp()+"\r\n";
				ret+= "压力: "+ meter.getPress()+"\r\n";
				ret+="设备电压: "+ meter.getDeviceVolt()+"\r\n";
				if(meter.getDeviceType().equals("采集器")) {
					ret+= "表具电压： "+ meter.getMeterVolt()+"\r\n";
				}
			}
			return ret;
		}else {
			return "图片中没办法识别到表号  可尝试在白纸上手写表号后拍图";
		}
	}
	@Override
	public String getModeName() {
		// TODO Auto-generated method stub
		return "表具信息查询";
	}

	@Override
	public String operatorInfor() {
		// TODO Auto-generated method stub
		return "发送需要查询的表号(支持携带表号的图片)";
	}

	@Override
	public void modeHandler(ModeBean modeBean) throws ModeHandlerException {
		// TODO Auto-generated method stub
		if(modeBean.getResultType() == ResultType.TEXT) {
			if(Utils.isNum(modeBean.getText())) {
				String meterNum = Utils.deletePreZero(modeBean.getText());
				if(!meterNum.isEmpty()) {
					 String meterQuery = meterQuery(meterNum);
					 modeBean.setResultType(ResultType.TEXT);
					 modeBean.setText(meterQuery);
				}else {
					modeBean.setResultType(ResultType.TEXT);
					 modeBean.setText("输入的表号无效");
				}
			}else {
				 modeBean.setResultType(ResultType.TEXT);
				 modeBean.setText("输入的表号无效");
			}
		}else if(modeBean.getResultType() == ResultType.PIC) {
			//检测图片中的内容
			Reply orc = robot.orc(modeBean.getFilePath());
			//对内容进行处理
			String anaysisMeterNum = Utils.anaysisMeterNum(orc.getWordsList());
			String meterQuery = meterQuery(anaysisMeterNum);
			modeBean.setResultType(ResultType.TEXT);
			modeBean.setText(meterQuery);
		}
	}
	
}
