package com.suma.venus.resource.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.service.WorkNodeService;

/***
 * 周期性检测层节点心跳是否超时，并更新在线状态
 * @author Administrator
 *
 */
@Component
public class LayerHeartbeatMonitorTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(LayerHeartbeatMonitorTask.class);
	
	private final long delay = 60 * 1000;
	
	@Autowired
	private WorkNodeService workNodeService;
	
	@Scheduled(fixedDelay = delay)
	public void monitor(){
		try {
			List<WorkNodePO> layerNodes = workNodeService.findAll();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (WorkNodePO layerNode : layerNodes) {
				if(null != layerNode.getLastHeartBeatTime() && !layerNode.getOnlineStatus().equals(ONLINE_STATUS.OFFLINE)){
					Date lastHeartbeatTime = sdf.parse(layerNode.getLastHeartBeatTime());
					if((new Date().getTime() - lastHeartbeatTime.getTime() > delay)){
						LOGGER.info("-----------------No heartbeat in 60s from layer : " + layerNode.getNodeUid() + "; set it offline-------------");
						layerNode.setOnlineStatus(ONLINE_STATUS.OFFLINE);
						workNodeService.save(layerNode);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("LayerHeartbeatMonitor Task Error : ", e);
		}
	}
}
