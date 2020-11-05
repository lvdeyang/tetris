package com.sumavision.tetris.business.director.bo;

import com.sumavision.tetris.capacity.bo.request.AllRequest;

/**
 * 导播任务返回参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月26日 上午9:59:53
 */
public class DirectorRequestBO {

	private String capacityIp;
	
	private AllRequest request;

	public String getCapacityIp() {
		return capacityIp;
	}

	public void setCapacityIp(String capacityIp) {
		this.capacityIp = capacityIp;
	}

	public AllRequest getRequest() {
		return request;
	}

	public void setRequest(AllRequest request) {
		this.request = request;
	}
	
}
