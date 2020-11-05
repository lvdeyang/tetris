package com.sumavision.bvc.control.device.monitor.message;

import com.sumavision.bvc.device.monitor.message.MonitorCalledMessagePO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorCalledMessageVO extends AbstractBaseVO<MonitorCalledMessageVO, MonitorCalledMessagePO>{

	private String message;
	
	public String getMessage() {
		return message;
	}

	public MonitorCalledMessageVO setMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public MonitorCalledMessageVO set(MonitorCalledMessagePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?null:DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setMessage(entity.getMessage());
		return this;
	}

}
