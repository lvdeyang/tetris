package com.suma.venus.resource.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.suma.application.ldap.department.dao.LdapDepartmentDao;
import com.suma.application.ldap.department.po.LdapDepartmentPo;
import com.suma.application.ldap.user.dao.LdapUserDao;
import com.suma.application.ldap.user.po.LdapUserPo;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.ldap.LdapDepartInfoUtil;
import com.suma.venus.resource.ldap.LdapUserInfoUtil;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserService;
import com.sumavision.tetris.user.UserVO;

@Component
public class DepartSyncLdapUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartSyncLdapUtils.class);

	@Autowired
	LdapDepartmentDao ldapDepartmentDao;

	@Autowired
	LdapDepartInfoUtil ldapDepartInfoUtil;

	@Autowired
	FolderDao folderDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private UserQueryService userService;
	
	@Autowired
	private LdapUserDao ldapUserDao;
	
	@Autowired
	private LdapUserInfoUtil ldapUserInfoUtil;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private UserService userFeignService;
	
	@Autowired
	private SerNodeDao serNodeDao;

	public int handleSyncFromLdap() {
		List<LdapDepartmentPo> ldapDeparts = ldapDepartmentDao.queryAllDepartment();
		int successCnt = 0;
		if (!CollectionUtils.isEmpty(ldapDeparts)) {
			for (LdapDepartmentPo ldapDepart : ldapDeparts) {
				try {
					FolderPO folder = folderDao.findTopByUuid(ldapDepart.getOrgUuid());
					if (null == folder) {
						// 系统内不存在这条数据，直接存
						FolderPO folderPO = ldapDepartInfoUtil.ldapToPojo(ldapDepart);
						folderDao.save(folderPO);
						successCnt++;
					} else {
						// 系统内已存在这条数据,判断是否是LDAP数据，是的话更新局部属性
						if (SOURCE_TYPE.EXTERNAL.equals(folder.getSourceType())) {
							ldapDepartInfoUtil.ldapMofidyPojo(ldapDepart, folder);
							folderDao.save(folder);
							successCnt++;
						}
					}
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}

		return successCnt;
	}
	
	/**
	 * 下载用户分组ldap<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午4:41:57
	 */
	public void handleFolderUserSyncFromLdap() throws Exception{
		
		List<LdapUserPo> ldapUsers = ldapUserDao.queryAllUsers();
		List<LdapUserPo> needAddLdapUser = new ArrayList<LdapUserPo>();
		List<FolderUserMap> existMaps = folderUserMapDao.findFromLdapMap();
		List<FolderUserMap> needRemoveMaps = new ArrayList<FolderUserMap>();
		
		SerNodePO self = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		
		if (!CollectionUtils.isEmpty(ldapUsers)) {
			for(FolderUserMap map: existMaps){
				boolean needDelete = true;
				for(LdapUserPo ldapUser: ldapUsers){
					if(ldapUser.getUserUuid().equals(map.getUserUuid())){
						needDelete = false;
						break;
					}
				}
				if(needDelete){
					needRemoveMaps.add(map);
				}
			}
			
			for(LdapUserPo ldapUser: ldapUsers){
				if(!ldapUser.getUserFactInfo().equals(self.getNodeFactInfo())){
					boolean needAdd = true;
					for(FolderUserMap map: existMaps){
						if(map.getUserUuid().equals(ldapUser.getUserUuid())){
							needAdd = false;
							break;
						}
					}
					if(needAdd){
						needAddLdapUser.add(ldapUser);
					}
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(needRemoveMaps)){
			List<Long> removeUserIds = new ArrayList<Long>();
			for(FolderUserMap map: needRemoveMaps){
				removeUserIds.add(map.getUserId());
			}
			
			userQuery.deleteLdapUser(removeUserIds);
		}
		
		if(!CollectionUtils.isEmpty(needAddLdapUser)){
			List<UserVO> userVOs = new ArrayList<UserVO>();
			Set<String> folderUuids = new HashSet<String>();
			for(LdapUserPo ldapUser: needAddLdapUser){
				userVOs.add(ldapUserInfoUtil.ldapToPojo(ldapUser));
				folderUuids.add(ldapUser.getUserOrg());
			}
			
			List<UserVO> _users = userFeignService.addLdapUser(userVOs);
			List<FolderPO> folders = folderDao.findByUuidIn(folderUuids);
			
			List<FolderUserMap> needAddMaps = new ArrayList<FolderUserMap>();
			for(LdapUserPo ldapUser: needAddLdapUser){
				UserVO addUser = null;
				LdapUserPo addLdapUser = null;
				for(UserVO _user: _users){
					if(_user.getUuid().equals(ldapUser.getUserUuid())){
						addUser = _user;
						addLdapUser = ldapUser;
						break;
					}
				}
				
				FolderPO addFolder = null;
				for(FolderPO folder: folders){
					if(folder.getUuid().equals(ldapUser.getUserOrg())){
						addFolder = folder;
						break;
					}
				}
				
				if(addUser != null && addFolder != null && addLdapUser != null && addLdapUser != null){
					FolderUserMap map = new FolderUserMap();
					map.setCreator("ldap");
					map.setFolderId(addFolder.getId());
					map.setFolderIndex(0l);
					map.setFolderUuid(addFolder.getUuid());
					map.setUserId(addUser.getId());
					map.setUserUuid(addUser.getUuid());
					map.setUserName(addUser.getUsername());
					map.setUserNode(addLdapUser.getUserNode());
					map.setUserNo(addUser.getUserno());
					
					needAddMaps.add(map);
				}
			}
			
			folderUserMapDao.saveAll(needAddMaps);
		}
		
	}

	public int handleSyncToLdap() {
		List<FolderPO> folderPOs = folderDao.findFoldersSyncToLdap();
		List<FolderPO> successFolders = new ArrayList<FolderPO>();
		for (FolderPO folderPO : folderPOs) {
			try {
				List<LdapDepartmentPo> ldapDepartmentPos = ldapDepartmentDao.getDepartByUuid(folderPO.getUuid());
				SerNodePO self = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
				if (!CollectionUtils.isEmpty(ldapDepartmentPos)) {
					LdapDepartmentPo ldapDepart = ldapDepartInfoUtil.pojoToLdap(folderPO, self);
					ldapDepartmentDao.update(ldapDepart);
				} else {
					// 新建
					LdapDepartmentPo ldapDepart = ldapDepartInfoUtil.pojoToLdap(folderPO, self);
					ldapDepartmentDao.save(ldapDepart);
				}

				folderPO.setSyncStatus(SYNC_STATUS.SYNC);
				successFolders.add(folderPO);
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}

		if (!successFolders.isEmpty()) {
			folderDao.saveAll(successFolders);
		}
		return successFolders.size();
	}
	
	/**
	 * 用户分组上传到ldap<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午1:30:29
	 */
	public void handleFolderUserSyncToLdap() throws Exception {
		
		//本系统下
		List<FolderUserMap> maps = folderUserMapDao.findLocalLdapMap();
		List<Long> userIds = new ArrayList<Long>();
		for(FolderUserMap map: maps){
			userIds.add(map.getUserId());
			map.setSyncStatus(1);
		}
		
		List<UserBO> users = userService.queryUsersByUserIds(userIds, TerminalType.PC_PORTAL);
		SerNodePO self = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		
		//ldap上本系统的数据
		List<LdapUserPo> allLdapUsers = ldapUserDao.getUserByFactInfo(self.getNodeFactInfo());
		
		for(UserBO user: users){
			List<LdapUserPo> ldapUserPoList = ldapUserDao.getUserByUuid(user.getUser().getUuid());
			
			if (!CollectionUtils.isEmpty(ldapUserPoList)) {
				// TODO
				LdapUserPo ldapUser = ldapUserInfoUtil.pojoToLdap(user, self);
				ldapUserDao.update(ldapUser);
				
				allLdapUsers.removeAll(ldapUserPoList);
				
			} else {
				LdapUserPo ldapUser = ldapUserInfoUtil.pojoToLdap(user, self);
				ldapUserDao.save(ldapUser);
			}
		}
		
		//删除多余的用户
		if(allLdapUsers.size() > 0){
			ldapUserDao.removeAll(allLdapUsers);
		}
		
		folderUserMapDao.saveAll(maps);
	}

	public String handleCleanUpLdap() {

		// 先从Ldap删除本地上传上去的数据
		List<FolderPO> folderPOs = folderDao.findBySourceType(SOURCE_TYPE.SYSTEM);
		if (!CollectionUtils.isEmpty(folderPOs)) {
			for (FolderPO folderPO : folderPOs) {
				try {

					List<LdapDepartmentPo> ldapDeparts = ldapDepartmentDao.getDepartByUuid(folderPO.getUuid());
					if (!CollectionUtils.isEmpty(ldapDeparts)) {
						ldapDepartmentDao.remove(ldapDeparts.get(0));
					}

					folderPO.setSyncStatus(SYNC_STATUS.ASYNC);
					folderDao.save(folderPO);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

		// 再从本地删除从ldap下载下来的数据
		List<FolderPO> externalFolderPOs = folderDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
		if (!CollectionUtils.isEmpty(externalFolderPOs)) {
			folderDao.deleteInBatch(externalFolderPOs);
		}

		return "";
	}
	
	/**
	 * 重置ldap数据<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午4:43:54
	 */
	public void handleFolderUserCleanUpLdap() throws Exception{
		
		SerNodePO self = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		
		//删除本地上传的ldap数据
		List<LdapUserPo> ldapUsers = ldapUserDao.getUserByFactInfo(self.getNodeFactInfo());
		ldapUserDao.removeAll(ldapUsers);
		
		//删除本地下载的ldap数据
		List<FolderUserMap> maps = folderUserMapDao.findFromLdapMap();
		folderUserMapDao.deleteInBatch(maps);
		
		//删除ldap用户
		userFeignService.deleteLdapUser();
		
		List<FolderUserMap> localMaps = folderUserMapDao.findLocalLdapMap();
		for(FolderUserMap localMap: localMaps){
			localMap.setSyncStatus(0);
		}
		folderUserMapDao.saveAll(localMaps);
	}

}
