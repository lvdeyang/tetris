package com.suma.venus.resource.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
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
	
	@Autowired
	private BundleDao bundleDao;
	
	@Scheduled(fixedDelay = delay)
	public void monitor(){
		try {
			List<WorkNodePO> layerNodes = workNodeService.findAll();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			List<String> needOfflineNodeIds = new ArrayList<String>();
			for (WorkNodePO layerNode : layerNodes) {
				if(null != layerNode.getLastHeartBeatTime() && !layerNode.getOnlineStatus().equals(ONLINE_STATUS.OFFLINE)){
					Date lastHeartbeatTime = sdf.parse(layerNode.getLastHeartBeatTime());
					if((new Date().getTime() - lastHeartbeatTime.getTime() > delay)){
						LOGGER.info("-----------------No heartbeat in 60s from layer : " + layerNode.getNodeUid() + "; set it offline-------------");
						layerNode.setOnlineStatus(ONLINE_STATUS.OFFLINE);
						needOfflineNodeIds.add(layerNode.getNodeUid());
						workNodeService.save(layerNode);
					}
				}
			}
			
			//当层节点离线时，层节点下的设备也离线
			if(needOfflineNodeIds != null && needOfflineNodeIds.size() > 0){
				List<BundlePO> needOfflineBundles = bundleDao.findByAccessNodeUidIn(needOfflineNodeIds);
				for(BundlePO bundle: needOfflineBundles){
					bundle.setOnlineStatus(ONLINE_STATUS.OFFLINE);
				}
				bundleDao.save(needOfflineBundles);
			}
		} catch (Exception e) {
			LOGGER.error("LayerHeartbeatMonitor Task Error : ", e);
		}
	}
}
