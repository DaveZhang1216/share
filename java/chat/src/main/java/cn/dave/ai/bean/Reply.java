package cn.dave.ai.bean;

import java.util.ArrayList;
import java.util.List;

public class Reply {
	private boolean useLess;//死了
	private String text; //文本
	private String filePath;//文件路径
	private String base64ImageString;
	private List<Words> list = new ArrayList<Words>();
	
	private List<Good> goods = new ArrayList<>();//物品列表
	
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isUseLess() {
		return useLess;
	}

	public void setUseLess(boolean useLess) {
		this.useLess = useLess;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void addGood(Good good) {
		this.goods.add(good);
	}
	public List<Good> getGoods(){
		return goods;
	}
	
	public void addWord(Words words) {
		this.list.add(words);
	}
	public List<Words> getWordsList(){
		return this.list;
	}

	public String getBase64ImageString() {
		return base64ImageString;
	}

	public void setBase64ImageString(String base64ImageString) {
		this.base64ImageString = base64ImageString;
	}

	public List<Words> getList() {
		return list;
	}

	public void setList(List<Words> list) {
		this.list = list;
	}
	
	
}
