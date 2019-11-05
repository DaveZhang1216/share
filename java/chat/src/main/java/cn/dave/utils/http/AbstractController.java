package cn.dave.utils.http;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController {
	public static enum Method{
		GET,
		POST
	}
	public static class Params{
		private Map<String, String> params = new HashMap<String, String>();
		public void putParam(String key,String value) {
			params.put(key, value);
		}
		public String getParam(String key) {
			return params.get(key);
		}
	}
	
	public abstract boolean matchMethodAndUrl(String url, Method methods);
	
	public abstract String handler(Params params, String url);
	
}
