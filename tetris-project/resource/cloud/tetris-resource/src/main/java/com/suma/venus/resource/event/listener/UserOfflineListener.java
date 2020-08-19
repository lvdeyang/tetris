package com.suma.venus.resource.event.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.lianwang.status.StatusXMLUtil;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionClosedEvent;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserOfflineListener implements ApplicationListener<WebsocketSessionClosedEvent>{

	@Autowired
	private UserQueryService userQueryService;
	
	@Autowired
	private StatusXMLUtil statusXMLUtil;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Override
	public void onApplicationEvent(WebsocketSessionClosedEvent event) {
		try {
			FolderUserMap map = folderUserMapDao.findByUserId(event.getUserId());
			if(map != null && map.getFolderId() != null && map.getSyncStatus().equals(1)){
				FolderPO folder = folderDao.findOne(map.getFolderId());
				
				//本系统下的用户不在map里面存状态
				if(folder != null && folder.getToLdap()){
					String connectCenterLayerID = resourceRemoteService.queryLocalLayerId();
					UserBO userBO = userQueryService.queryUserByUserId(event.getUserId(), TerminalType.QT_ZK);
					userBO.setLogined(false);
					List<UserBO> localUserBOs = new ArrayList<UserBO>();
					localUserBOs.add(userBO);
					System.out.println("用户" + userBO.getName() + "上线了。。。。。。。。。");
					statusXMLUtil.sendResourcesXmlMessage(null, null, localUserBOs, connectCenterLayerID, 1500);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
