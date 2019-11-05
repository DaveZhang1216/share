package cn.dave.acol.api;

import org.springframework.web.client.RestTemplate;

import cn.dave.acol.api.bean.MeterDto;

public class DeviceService {
	private RestTemplate restTemplate = new RestTemplate();
	private String serverUrl;
	public DeviceService(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	/**
	 * @param meterNum
	 * @return  没有找到返回null
	 */
	public MeterDto getMeterByMeterNum(String meterNum) {
		MeterDto forObject = restTemplate.getForObject(serverUrl+"/device/meter/"+meterNum, MeterDto.class);
		return forObject;
	}
}
