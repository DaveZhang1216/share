package cn.dave.acol.api.bean;


public class MeterDto {
	private String meterNum;//表具号码
	private String meterType;//表具类型
	private String standard;
	private boolean using;//是否正在使用
	private long produceTime;
	private boolean bind;//是否绑定
	private String deviceType;//设备类型
	private String deviceNum;//采集器编号
	private String iccid;//手机卡号
	private long lastCon;//最后上传时间
	private float meterVolt;//表具电压
	private float deviceVolt;//设备电压
	private double scSum;//标总
	private float scFlow;//标流
	private int signal;//信号强度
	private float press;
	private float temp;
	private String userName;//用户名
	private String userAddress;//用户地址
	private boolean isException;//是否异常
	public String getMeterNum() {
		return meterNum;
	}
	public void setMeterNum(String meterNum) {
		this.meterNum = meterNum;
	}
	public String getMeterType() {
		return meterType;
	}
	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public boolean isUsing() {
		return using;
	}
	public void setUsing(boolean using) {
		this.using = using;
	}
	public long getProduceTime() {
		return produceTime;
	}
	public void setProduceTime(long produceTime) {
		this.produceTime = produceTime;
	}
	public boolean isBind() {
		return bind;
	}
	public void setBind(boolean bind) {
		this.bind = bind;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public long getLastCon() {
		return lastCon;
	}
	public void setLastCon(long lastCon) {
		this.lastCon = lastCon;
	}
	public float getMeterVolt() {
		return meterVolt;
	}
	public void setMeterVolt(float meterVolt) {
		this.meterVolt = meterVolt;
	}
	public float getDeviceVolt() {
		return deviceVolt;
	}
	public void setDeviceVolt(float deviceVolt) {
		this.deviceVolt = deviceVolt;
	}
	public double getScSum() {
		return scSum;
	}
	public void setScSum(double scSum) {
		this.scSum = scSum;
	}
	public float getScFlow() {
		return scFlow;
	}
	public void setScFlow(float scFlow) {
		this.scFlow = scFlow;
	}
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public float getPress() {
		return press;
	}
	public void setPress(float press) {
		this.press = press;
	}
	public float getTemp() {
		return temp;
	}
	public void setTemp(float temp) {
		this.temp = temp;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public boolean isException() {
		return isException;
	}
	public void setException(boolean isException) {
		this.isException = isException;
	}
}
