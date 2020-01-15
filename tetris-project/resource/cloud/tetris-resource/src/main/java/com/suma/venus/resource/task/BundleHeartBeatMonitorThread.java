package com.suma.venus.resource.task;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class BundleHeartBeatMonitorThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleHeartBeatMonitorThread.class);

	// 设备的超时时间
	private final long timeout = 15 * 1000;

	private BundleHeartBeatService bundleHeartBeatService;

	public BundleHeartBeatMonitorThread(BundleHeartBeatService bundleHeartBeatService) {

		this.bundleHeartBeatService = bundleHeartBeatService;
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
				bundleHeartBeatService.removeBundleStatus(e.getKey());
				//TODO 发出告警
			}
		}
	}
}
