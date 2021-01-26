package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.service.exception.SystemNodeNotFoundException;

@Service
public class FolderService extends CommonService<FolderPO> implements InitializingBean {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(FolderService.class);

	@Autowired
	private FolderDao folderDao;

	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private SerNodeDao serNodeDao;

	/**
	 * 初始化根节点数据
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<FolderPO> allFoders = folderDao.findAll();
		if (allFoders.isEmpty()) {
			// 初始化根目录

			FolderPO rootFolder = new FolderPO();
			rootFolder.setName("根目录");
			rootFolder.setParentId(-1l);
			// rootFolder.setFolderType();
			rootFolder.setBeBvcRoot(true);
			rootFolder.setToLdap(true);
			rootFolder.setFolderIndex(0);
			folderDao.save(rootFolder);

			List<FolderPO> rootFolders = new ArrayList<>();
			// 根终端目录设置默认UUID
			FolderPO rootTerminalFolder = new FolderPO();
			rootTerminalFolder.setName("终端根目录");
			rootTerminalFolder.setParentId(rootFolder.getId());
			rootTerminalFolder.setParentPath("/" + rootFolder.getId());
			rootTerminalFolder.setFolderType(FolderType.TERMINAL);
			// rootTerminalFolder.setBeBvcRoot(true);
			rootTerminalFolder.setToLdap(true);
			rootTerminalFolder.setFolderIndex(1);
			rootFolders.add(rootTerminalFolder);

			FolderPO rootMonitorFolder = new FolderPO();
			rootMonitorFolder.setName("监控根目录");
			rootMonitorFolder.setParentId(rootFolder.getId());
			rootMonitorFolder.setParentPath("/" + rootFolder.getId());
			rootMonitorFolder.setFolderType(FolderType.MONITOR);
			// rootMonitorFolder.setBeBvcRoot(true);
			rootMonitorFolder.setToLdap(false);
			rootMonitorFolder.setFolderIndex(2);
			rootFolders.add(rootMonitorFolder);

			FolderPO rootLiveFolder = new FolderPO();
			rootLiveFolder.setName("直播根目录");
			rootLiveFolder.setParentId(rootFolder.getId());
			rootLiveFolder.setParentPath("/" + rootFolder.getId());
			rootLiveFolder.setFolderType(FolderType.LIVE);
			// rootLiveFolder.setBeBvcRoot(true);
			rootLiveFolder.setToLdap(false);
			rootLiveFolder.setFolderIndex(3);
			rootFolders.add(rootLiveFolder);

			FolderPO rootOnDemandFolder = new FolderPO();
			rootOnDemandFolder.setName("点播根目录");
			rootOnDemandFolder.setParentId(-1l);
			rootOnDemandFolder.setParentPath(null);
			rootOnDemandFolder.setFolderType(FolderType.ON_DEMAND);
			// rootOnDemandFolder.setBeBvcRoot(true);
			rootOnDemandFolder.setToLdap(false);
			rootOnDemandFolder.setFolderIndex(4);
			rootFolders.add(rootOnDemandFolder);

			folderDao.saveAll(rootFolders);
		}
	}

	public List<FolderPO> findByParentId(Long parentId) {
		return folderDao.findByParentId(parentId);
	}

	public List<FolderPO> findByParentPath(String parentPath) {
		return folderDao.findByParentPath(parentPath);
	}

	public FolderPO findByName(String name) {
		return folderDao.findByName(name);
	}

	public Long findIdByName(String name) {
		return folderDao.findIdByName(name);
	}
	
	/**
	 * 给用户绑定文件夹<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月27日 上午10:11:48
	 * @param FolderPO folder
	 * @param List<UserBO> users
	 * @param Long maxIndex
	 */
	public void setFolderToUsers(FolderPO folder, List<UserBO> users, Long maxIndex) throws Exception{
		SerNodePO sysnode = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		if(sysnode == null){
			throw new SystemNodeNotFoundException("未创建本地服务节点，无法将用户加入到分组");
		}
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user: users){
			userIds.add(user.getId());
		}
		List<FolderUserMap> maps = folderUserMapDao.findByUserIdIn(userIds);
		List<FolderUserMap> needSaveMaps = new ArrayList<FolderUserMap>();
		for(UserBO user: users){
			boolean flag = false;
			if(maps != null && maps.size() > 0){
				for(FolderUserMap map: maps){
					if(map.getUserId().equals(user.getId())){
						map.setFolderId(folder.getId());
						map.setFolderUuid(folder.getUuid());
						map.setFolderIndex(maxIndex);
						map.setSyncStatus(0);
						needSaveMaps.add(map);
						maxIndex++;
						flag = true;
						break;
					}
				}
			}
			if(!flag){
				FolderUserMap map = new FolderUserMap();
				map.setFolderId(folder.getId());
				map.setFolderUuid(folder.getUuid());
				map.setFolderIndex(maxIndex);
				map.setUserId(user.getId());
				map.setUserUuid(user.getUser().getUuid());
				map.setUserName(user.getName());
				map.setUserNo(user.getUserNo());
				map.setUserNode(sysnode.getNodeUuid());
				map.setCreator(user.getCreater());
				map.setSyncStatus(0);
				needSaveMaps.add(map);
				maxIndex++;
			}
		}
		if(needSaveMaps.size() > 0){
			folderUserMapDao.saveAll(needSaveMaps);
		}
	}

	/**
	 * d删除foler，如果folder非叶子节点，需要先递归删除其后代节点
	 * 
	 * @param id
	 */
	@Override
	public void delete(FolderPO po) {
		recursiveDelete(po);
	}

	/** 递归删除folderpo，并解除bundle的对应分组关联 */
	private void recursiveDelete(FolderPO po) {
		List<FolderPO> childFolders = folderDao.findByParentId(po.getId());
		if (!childFolders.isEmpty()) {
			for (FolderPO childFolder : childFolders) {
				recursiveDelete(childFolder);
			}
		} else {
			List<BundlePO> bundles = bundleDao.findByFolderId(po.getId());
			if (!bundles.isEmpty()) {
				for (BundlePO bundle : bundles) {
					bundle.setFolderId(null);
					bundle.setFolderIndex(null);
				}
				bundleDao.saveAll(bundles);
			}
			folderDao.delete(po);
		}
	}

}
