package com.suma.venus.resource.task;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;

@Component
public class BundleHeartBeatService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleHeartBeatService.class);

	// 检测设备心跳动作的频率
	@Value("${checkBundleHeartBeatFreq}")
	private long freqTime;

	@Value("${checkBundleHeartBeatTimeout}")
	private long timeout;

	@Autowired
	BundleDao bundleDao;

	@Autowired
	AlarmFeignClientService alarmFeignClientService;

	// 记录系统中设备的最后心跳信息
	private final ConcurrentHashMap<String, Long> bunldeStatusMap = new ConcurrentHashMap<String, Long>();

	private ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

	private ScheduledFuture<?> t;

	public ConcurrentHashMap<String, Long> getBunldeStatusMap() {
		return bunldeStatusMap;
	}

	public void removeBundleStatus(String bundle_ip) {
		LOGGER.info("remove heartBeat， bundle_ip=" + bundle_ip);

		bunldeStatusMap.remove(bundle_ip);

		// TODO 设备离线，更新数据库status
		List<BundlePO> bundlePOs = bundleDao.findByDeviceIp(bundle_ip);

		if (!CollectionUtils.isEmpty(bundlePOs)) {
			BundlePO bundlePO = bundlePOs.get(0);
			bundlePO.setOnlineStatus(ONLINE_STATUS.OFFLINE);
			bundleDao.save(bundlePO);
		} else {
			LOGGER.warn("removeBundleStatus, cannot find budlePO, ip=" + bundle_ip);
		}

		// if (bunldeStatusMap.size() == 0 && t != null) {
		// TODO
		// t.cancel(true);
		// }

	}

	public void addBundleStatus(String bundle_ip, Long currentTime) {

		// boolean threadFlag = false;

		// startBundleHeartBeatMonitor();

		if (bunldeStatusMap.get(bundle_ip) == null) {
			// TODO 设备从离线变为上线，更新数据库status
			List<BundlePO> bundlePOs = bundleDao.findByDeviceIp(bundle_ip);

			if (CollectionUtils.isEmpty(bundlePOs)) {
				LOGGER.info("cannot find bundlePO, return");
				return;
			}

			BundlePO bundlePO = bundlePOs.get(0);

			bundlePO.setOnlineStatus(ONLINE_STATUS.ONLINE);
			bundleDao.save(bundlePO);

			try {
				alarmFeignClientService.recoverAlarm("11011000", bundle_ip, bundle_ip, null,
						Calendar.getInstance().getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			LOGGER.info("add=" + bundle_ip + ",time=" + currentTime);
		}

		bunldeStatusMap.put(bundle_ip, currentTime);

		// TODO 判断是否需要启动线程
		// if (threadFlag) {
		// startBundleHeartBeatMonitor();
		// }
	}

	public void initBundleStatus(BundlePO po) {

		// startBundleHeartBeatMonitor();

		if (bunldeStatusMap.get(po.getDeviceIp()) == null) {
			if (po.getOnlineStatus().equals(ONLINE_STATUS.ONLINE)) {
				bunldeStatusMap.put(po.getDeviceIp(), Calendar.getInstance().getTimeInMillis());
			}

		}

	}

	public synchronized void startBundleHeartBeatMonitor() {

		if (bunldeStatusMap.size() == 0) {
			LOGGER.info("new thread for bundle monitor");
			BundleHeartBeatMonitorThread thread = new BundleHeartBeatMonitorThread(this, alarmFeignClientService,
					timeout);
			t = pool.scheduleAtFixedRate(thread, freqTime, freqTime, TimeUnit.MILLISECONDS);
		}
	}

}
