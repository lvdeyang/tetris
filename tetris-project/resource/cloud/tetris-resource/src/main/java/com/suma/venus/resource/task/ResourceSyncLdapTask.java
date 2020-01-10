package com.suma.venus.resource.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.util.EquipSyncLdapUtils;

@Component
public class ResourceSyncLdapTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceSyncLdapTask.class);

	private final long delay = 10 * 60 * 1000;
	
	@Autowired
	EquipSyncLdapUtils equipSyncLdapUtils;
	
	@Autowired
	UserQueryFeign userFeign;
	
	@Autowired
	SerNodeDao serNodeDao;
	
	@Autowired
	FolderDao folderDao;
	
	@Scheduled(fixedDelay = delay)
	public void sync(){
		
		LOGGER.info("ResourceSyncLdapTask in");
		
		try {
			SerNodePO bvcSerNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
			SerNodePO externalSerNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.EXTERNAL);
			if(null == bvcSerNodePO || null == externalSerNodePO || !externalSerNodePO.getNodeUuid().equals(bvcSerNodePO.getNodeFather())){
				//如果bvc和外部系统的双方node还没有建立起父子关系，退出，不能往下进行同步
				LOGGER.info("ResourceSyncLdapTask， serNode don't meet all the conditions. return");
				return ;
			}
			
			List<FolderPO> bvcRootFolders = folderDao.findBvcRootFolders();
			FolderPO bvcParentfolderPO = folderDao.findOne(bvcRootFolders.get(0).getParentId());
			List<FolderPO> externalFolders = folderDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
			if(null == bvcParentfolderPO || !externalFolders.contains(bvcParentfolderPO)){
				//如果bvc和外部系统的组织机构还没有建立起父子关系，退出，不能往下进行同步
				LOGGER.info("ResourceSyncLdapTask， folder don't meet all the conditions. return");
				return ;
			}
			
			LOGGER.info("+++++++++++++Resource sync ldap timer starts++++++++++++++++++++");
			
			//从ldap下载设备数据
			equipSyncLdapUtils.handleSyncFromLdap();
			
			//向ldap上传设备信息
			equipSyncLdapUtils.handleSyncToLdap("false");
			
			//通知用户服务进行ldap同步
			userFeign.notifySyncLdap();
		} catch (Exception e) {
			LOGGER.error("" , e);
		}
	}
}
