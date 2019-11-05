package cn.dave.ai.robot.impl;

public class Result {
	private int groupType;
	private String resultType;
	private Value values;
	public int getGroupType() {
		return groupType;
	}
	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public Value getValues() {
		return values;
	}
	public void setValues(Value values) {
		this.values = values;
	}
	@Override
	public String toString() {
		if(resultType.equals("text")) {
			return values.getText()+"  ";
		}else if(resultType.equals("url")) {
			return "链接地址"+values.getUrl()+"  ";
		}else {
			return "";
		}
		
	}
	
	
}
