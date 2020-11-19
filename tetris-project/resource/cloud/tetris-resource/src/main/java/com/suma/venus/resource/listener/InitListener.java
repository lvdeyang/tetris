package com.suma.venus.resource.listener;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.suma.venus.message.service.MessageService;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.mq.MQCallBackService;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.task.BundleHeartBeatService;

/**
 * 启动listener
 * 
 * @author lxw
 *
 */
@Component
@Order(value = 1)
public class InitListener implements ApplicationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitListener.class);

	@Value("${spring.cloud.client.ipAddress}")
	private String localIp;

	@Autowired
	private MessageService messageService;

	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private BundleHeartBeatService bundleHeartBeatService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 默认就把node的名称写入到消息服务的配置里然后只要重启就重新注册
		// messageService.recoveryMessageService(new MQCallBackService());

		// 初始化告警客户端
		// AlarmOprlogClientService.initClient("suma-venus-resource",
		// RegisterStatus.getNodeId(), localIp, messageService);

		List<BundlePO> bundlePOs = bundleDao.findByDeviceModel("transcode");

		LOGGER.info("==============InitListener" + "================= " + bundlePOs.size());

		if (CollectionUtils.isEmpty(bundlePOs)) {

			for (BundlePO po : bundlePOs) {
				// po.setOnlineStatus(ONLINE_STATUS.OFFLINE);
				bundleHeartBeatService.startBundleHeartBeatMonitor();
				bundleHeartBeatService.initBundleStatus(po);
			}

			// bundleDao.save(bundlePOs);
		}

		LOGGER.info("init transcode device done");
	}
}
