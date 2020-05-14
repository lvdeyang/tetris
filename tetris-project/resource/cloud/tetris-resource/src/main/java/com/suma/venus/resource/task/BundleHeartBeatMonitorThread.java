package com.suma.venus.resource.task;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;

public class BundleHeartBeatMonitorThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleHeartBeatMonitorThread.class);

	// 设备的超时时间
	private long timeout;

	private BundleHeartBeatService bundleHeartBeatService;

	private AlarmFeignClientService alarmFeignClientService;

	public BundleHeartBeatMonitorThread(BundleHeartBeatService bundleHeartBeatService,
			AlarmFeignClientService alarmFeignClientService, long timeout) {

		this.bundleHeartBeatService = bundleHeartBeatService;
		this.alarmFeignClientService = alarmFeignClientService;
		this.timeout = timeout;
	}

	@Override
	public void run() {
		checkHeartBeat();
	}

	// 删除检测失效节点
	public void checkHeartBeat() {

		ConcurrentHashMap<String, Long> bunldeStatusMap = bundleHeartBeatService.getBunldeStatusMap();

		LOGGER.info("check heartBeat in, map=" + JSONObject.toJSONString(bunldeStatusMap));

		Iterator<Map.Entry<String, Long>> it = bunldeStatusMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Long> e = it.next();
			if ((System.currentTimeMillis() - bunldeStatusMap.get(e.getKey())) > timeout) {

				try {
					alarmFeignClientService.triggerAlarm("11011000", e.getKey(), e.getKey(), null, false,
							Calendar.getInstance().getTime());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				bundleHeartBeatService.removeBundleStatus(e.getKey());
				// TODO 发出告警

			}
		}
	}
}
