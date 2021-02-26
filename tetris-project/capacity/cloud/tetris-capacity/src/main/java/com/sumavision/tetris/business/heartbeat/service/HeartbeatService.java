package com.sumavision.tetris.business.heartbeat.service;

import com.sumavision.tetris.business.common.TransformModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.capacity.bo.request.ResultCodeResponse;
import com.sumavision.tetris.capacity.config.CapacityProps;
import com.sumavision.tetris.capacity.service.CapacityService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class HeartbeatService {
	
	@Autowired
	private CapacityService capacityService;
	
	@Autowired
	private CapacityProps capacityProps;

	/**
	 * 设置心跳地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 下午1:16:39
	 * @param String ip 能力ip
	 * @param String heartbeatUrl 心跳地址
	 */
	public void setHeartbeatUrl(String ip, String heartbeatUrl) throws Exception{
		TransformModule transformModule = new TransformModule(ip);
		ResultCodeResponse response = capacityService.putHeartbeatUrl(transformModule, heartbeatUrl);
		if(response.getResult_code().equals("1")){
			throw new BaseException(StatusCode.ERROR, "url格式错误");
		}
	}
	
}
