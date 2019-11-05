package cn.dave.chat.bean;

public class TimeAdvise {
	private int time; //周期
	private int lastTime;//剩余时间
	private String userId;//通知人
	
	public boolean needSend() {
		if(lastTime -1 ==0) {
			lastTime = time;
			return true;
		}else {
			lastTime--;
		}
		return false;
	}
	public TimeAdvise(int time, String userId) {
		if(time<=0) {
			this.time = 1;
			lastTime = 1;
		}else {
			lastTime = time;
			this.time = time;
		}
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public void setTime(int time) {
		if(time<=0) {
			this.time = 1;
			this.lastTime = 1;
		}else {
			this.time = time;
			this.lastTime = time;
		}
	}
}
