package cn.dave.ai.bean;

public class Good {
	private String name; //商品名称
	private float calorie;//卡路里
	private String countryCn;//哪个国家产的
	private String wineryCn;// 庄园
	private String description;//描述
	private double score;//置信度
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getCalorie() {
		return calorie;
	}
	public void setCalorie(float calorie) {
		this.calorie = calorie;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCountryCn() {
		return countryCn;
	}
	public void setCountryCn(String contryCn) {
		this.countryCn = contryCn;
	}
	public String getWineryCn() {
		return wineryCn;
	}
	public void setWineryCn(String wineryCn) {
		this.wineryCn = wineryCn;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	
}
