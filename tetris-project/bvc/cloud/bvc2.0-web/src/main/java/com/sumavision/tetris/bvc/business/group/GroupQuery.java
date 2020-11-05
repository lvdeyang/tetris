package com.sumavision.tetris.bvc.business.group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeType;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallPO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.system.role.SystemRoleQuery;
import com.sumavision.tetris.system.role.SystemRoleVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class GroupQuery{
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private GroupDAO groupDao;

	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private ConferenceHallDAO conferenceHallDao;
	
	@Autowired
	private SystemRoleQuery systemRoleQuery;
	
	@Autowired
	private PrivilegeDAO privilegeDao;
	
	/**
	 * 获取会议列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>sum<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月14日 上午7:55:59
	 * @return List<GroupVO>会议列表
	 * @throws Exception
	 */
	public List<GroupVO> queryTvosGroupList() throws Exception{
		UserVO user = userQuery.current();
		List<BundlePO> bundleList = bundleDao.findBundleByUserIdAndDeviceModel(user.getId(),"tvos");
		TerminalPO terminal = terminalDao.findByType(TerminalType.ANDROID_TVOS);
		List<TerminalBundlePO> terminalbundle = terminalBundleDao.findByTerminalId(terminal.getId());
		List<TerminalBundleConferenceHallPermissionPO> terminalBundleConferenceHallPermissions = terminalBundleConferenceHallPermissionDao.findByTerminalBundleIdAndBundleId(terminalbundle.get(0).getId(),bundleList.get(0).getBundleId());
		List<GroupMemberPO> groupMembers = groupMemberDao.findByGroupMemberTypeAndOriginId(GroupMemberType.MEMBER_HALL,terminalBundleConferenceHallPermissions.get(0).getConferenceHallId().toString());
		Set<Long> groupIds = new HashSet<Long>();
		for (GroupMemberPO groupMember:groupMembers) {
			groupIds.add(groupMember.getGroupId());
		}
		List<GroupPO> groups = groupDao.findAll(groupIds);
		return GroupVO.getConverter(GroupVO.class).convert(groups, GroupVO.class);
	}
	
	/**
	 * 根据文件夹id查询目录下的子目录以及虚拟设备和用户（没有文件夹id返回根目录）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月18日 下午3:11:19
	 * @param Long folderId  文件夹id
	 * @return List<TreeNodeVO> 文件夹、用户、虚拟设备
	 */
	public List<TreeNodeVO> queryOrganization(Long folderId) throws Exception{
		List<TreeNodeVO> nodes = new ArrayList<TreeNodeVO>();
		if( folderId == null){
			List<FolderPO> entities = folderDao.findByParentIdAndFolderTypeNotIn(-1l);
			if(entities!=null && entities.size()>0){
				for(FolderPO entity:entities){
					TreeNodeVO node = new TreeNodeVO();
					node.setId(entity.getId().toString());
					node.setName(entity.getName());
					node.setType(TreeNodeType.FOLDER);
					nodes.add(node);
				}
			}
		}else {
			UserVO user = userQuery.current();
			List<SystemRoleVO> roles = systemRoleQuery.queryRolesByUser(user.getId());
			Set<Long> roleIds = new HashSet<Long>();
			roleIds.add(-1l);
			if(roles!=null && roles.size()>0){
				for(SystemRoleVO role:roles){
					roleIds.add(Long.valueOf(role.getId()));
				}
			}
			
			List<FolderPO> folderEntities = folderDao.findByParentIdAndFolderTypeNotIn(folderId);
			if(folderEntities!=null && folderEntities.size()>0){
				for(FolderPO entity:folderEntities){
					TreeNodeVO node = new TreeNodeVO();
					node.setId(entity.getId().toString());
					node.setName(entity.getName());
					node.setType(TreeNodeType.FOLDER);
					nodes.add(node);
				}
			}
			
			List<ConferenceHallPO> hallEntities = conferenceHallDao.findPermissionConferenceHallsByFolderId(roleIds, folderId);
			if(hallEntities!=null && hallEntities.size()>0){
				for(ConferenceHallPO entity:hallEntities){
					TreeNodeVO node = new TreeNodeVO();
					node.setId(entity.getId().toString());
					node.setName(entity.getName());
					node.setType(TreeNodeType.CONFERENCE_HALL);
					node.setBundleStatus("ONLINE");
					nodes.add(node);
				}
			}
			
			List<PrivilegePO> privileges = privilegeDao.findByRoleIdIn(roleIds);
			if(privileges!=null && privileges.size()>0){
				Set<String> userNos = new HashSet<String>();
				for(PrivilegePO po:privileges){
					userNos.add(po.getResourceIndentity().split("-")[0]);
				}
				List<UserVO> users = userQuery.findByUsernoIn(userNos);
				List<FolderUserMap> folderUserMaps = folderUserMapDao.findByFolderId(folderId);
				if(users!=null && users.size()>0 &&
					folderUserMaps!=null && folderUserMaps.size()>0){
					Set<Long> permissionUserIds = new HashSet<Long>();
					for(FolderUserMap map:folderUserMaps){
						for(UserVO u:users){
							if(map.getUserId().equals(u.getId())){
								permissionUserIds.add(map.getUserId());
								break;
							}
						}
					}
					if(permissionUserIds.size() > 0){
						List<UserVO> permissionUsers = userQuery.findByIdIn(permissionUserIds);
						for(UserVO permissionUser:permissionUsers){
							TreeNodeVO node = new TreeNodeVO();
							node.setId(permissionUser.getId().toString());
							node.setName(permissionUser.getNickname());
							node.setType(TreeNodeType.USER);
							node.setBundleStatus(permissionUser.getStatus());
							nodes.add(node);
						}
					}
				}
			}
		}
		return nodes;
	}
	
}
