package com.sumavision.tetris.cs.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;
import com.sumavision.tetris.commons.context.AsynchronizedSystemInitialization;

@Service
@Transactional(rollbackFor = Exception.class)
public class CsInitialization implements AsynchronizedSystemInitialization{
	
	private static final Logger LOG = LoggerFactory.getLogger(CsInitialization.class);
	
	@Autowired
	private AlarmFeignClientService alarmFeignClientService;

	@Override
	public int index() {
		return 0;
	}

	@Override
	public void init() {
		try {
			alarmFeignClientService.subscribeAlarm("11070001", "/api/server/cs/channel/alarm/reboot", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
