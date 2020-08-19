package com.sumavision.tetris.bvc.business.group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.kahadb.util.LFUCache;
import org.bouncycastle.asn1.x509.UserNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.BundlePO.ABLE_STATUS;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeType;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.meeting.logic.po.CommonPO;
import com.sumavision.bvc.vo.FolderVO;
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
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class GroupQuery{
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private GroupDAO groupDao;

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private TerminalDAO terminalDAO;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDAO;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDAO;
	
	@Autowired
	private GroupMemberDAO groupMemberDAO;
	
	@Autowired
	private GroupDAO groupDAO;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDAO;
	
	@Autowired
	private ConferenceHallDAO conferenceHallDAO;
	/**
	 * 
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
		
		
		List<GroupVO> groupList = new ArrayList<GroupVO>();
		//根据用户ID和设备类型获取机顶盒列表
		List<BundlePO> bundleList = bundleDao.findBundleByUserIdAndDeviceModel(user.getId(),"tvos");
		//查类型为机顶盒的终端类型
		TerminalPO terminal = terminalDAO.findByType(TerminalType.ANDROID_TVOS);
		//根据终端类型和机顶盒获取会场
		List<TerminalBundlePO> terminalbundle = terminalBundleDAO.findByTerminalId(terminal.getId());
		//根据机顶盒id和终端设备类型获取会场和设备关联表
		List<TerminalBundleConferenceHallPermissionPO> terminalBundleConferenceHallPermissionPOs = terminalBundleConferenceHallPermissionDAO.findByTerminalBundleIdAndBundleId(terminalbundle.get(0).getId(),bundleList.get(0).getBundleId());
		//根据会场获取group
		List<GroupMemberPO> groupMemberPOs = groupMemberDAO.findByGroupMemberTypeAndOriginId(GroupMemberType.MEMBER_HALL,terminalBundleConferenceHallPermissionPOs.get(0).getConferenceHallId().toString());
		//获取group
		for (GroupMemberPO groupMemberPO : groupMemberPOs) {
			GroupPO groupPO=groupDao.findOne(groupMemberPO.getGroupId());
			groupList.add(new com.sumavision.tetris.bvc.business.group.GroupVO().set(groupPO));
		}
		
		return groupList;		 
	}
	
	/**
	 * 
	 * 根据文件夹id查询目录下的子目录以及设备和用户（没有文件夹id返回根目录）<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月18日 下午3:11:19
	 * @param Long id  文件夹id
	 * @return FolderVO
	 */
	public List<TreeNodeVO>  queryOrganization(Long id) throws Exception{
		/*FolderPO folder =  ;
		//
		List<FolderVO> folderList = new ArrayList<FolderVO>();
		List<FolderPO> folderid = folderDao.findOne();
		return folderList;*/
		//Map<String, String> map=new LinkedHashMap<String, String>(); 
		/*Map<String, String> dataMap = new LinkedHashMap<String, String>();
		List<FolderVO> folderList = new ArrayList<FolderVO>();*/
		//List<TreeNodeVO> treeNodeVOs = new ArrayList<TreeNodeVO>();
		//根据id查询它的子目录
		List<TreeNodeVO> treeNodeVOs = new ArrayList<TreeNodeVO>();
		if( id == null){
			List<FolderPO> rootFolderPOs = folderDao.findRootOptions();
//			List<FolderPO> rootFolderPOs = folderDao.findBvcRootFolders();
			for (FolderPO rootFolderPO : rootFolderPOs) {
				TreeNodeVO treeNodeVO = new TreeNodeVO();
				treeNodeVO.setId(rootFolderPO.getId().toString());
				treeNodeVO.setName(rootFolderPO.getName());
				//treeNodeVO.setType(TreeNodeType.USER);
				treeNodeVOs.add(treeNodeVO);
			}
		}else {
			FolderPO folderid = folderDao.findOne(id);
			List<FolderPO> sonFolders = folderDao.findByParentId(folderid.getId()); 
			//根据folder查出虚拟设备halls
			List<ConferenceHallPO> conferenceHallPOs = conferenceHallDAO.findByFolderId(folderid.getId());
			//根据folder查出用户
			List<FolderUserMap> folderUserMaps = folderUserMapDAO.findByFolderId(folderid.getId());
			
			
			for (FolderUserMap folderUserMap : folderUserMaps) {
				TreeNodeVO treeNodeVO = new TreeNodeVO();
				treeNodeVO.setId(folderUserMap.getUserId().toString());
				treeNodeVO.setName(folderUserMap.getUserName());
				treeNodeVO.setType(TreeNodeType.USER);
				treeNodeVOs.add(treeNodeVO);
			}
			for (FolderPO sonFolder: sonFolders) {
				TreeNodeVO treeNodeVO = new TreeNodeVO();
				treeNodeVO.setId(sonFolder.getId().toString());
				treeNodeVO.setName(sonFolder.getName());
				treeNodeVO.setType(TreeNodeType.FOLDER);
				treeNodeVOs.add(treeNodeVO);
			}
			/*for (FolderPO sonFolder: sonFolders) {
				TreeNodeVO treeNodeVO = new TreeNodeVO().setId(sonFolder.getId().toString())
				.setName(sonFolder.getName())
				.setType(TreeNodeType.FOLDER);
				treeNodeVOs.add(treeNodeVO);
			}*/
			for (ConferenceHallPO conferenceHallPO : conferenceHallPOs) {
				TreeNodeVO treeNodeVO = new TreeNodeVO();
				treeNodeVO.setId(conferenceHallPO.getId().toString());
				treeNodeVO.setName(conferenceHallPO.getName());
				treeNodeVO.setType(TreeNodeType.CONFERENCE_HALL);
				treeNodeVOs.add(treeNodeVO);
			}
			
		}
		
		
		/*
        }*/
		//return 子目录的表 设备表 用户表 
		//TreeNodeVO treeNodeVOs = new TreeNodeVO().set(sonFolder)
		
		/*for (TreeNodeVO treeNodeVO : treeNodeVOs) {
			treeNodeVOs.add();
		}*/
		
		//if（id == null）返回根目录
		
		return treeNodeVOs;
	}
	/*List<FolderBO> roots = findRoots(folders);
	for(FolderBO root:roots){
		TreeNodeVO _root = new TreeNodeVO().set(root)
										   .setChildren(new ArrayList<TreeNodeVO>());
		_roots.add(_root);
		if(withChannel){
			recursionFolder(_root, folders, filteredBundles, channels, null);
		}else{
			recursionFolder(_root, folders, filteredBundles, null, null);
		}
	}
	
	return _roots;*/
}
