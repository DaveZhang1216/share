package cn.dave.ai.bean;

public class Words {
	private String content;//识别内容
	private double average; //识别平均值
	private double min;//识别最小值
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	
	
}
