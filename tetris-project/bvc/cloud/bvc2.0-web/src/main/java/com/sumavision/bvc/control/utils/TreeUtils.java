package com.sumavision.bvc.control.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.service.util.QueryUtil;

/**
 * 树相关操作，提取公用方法<br/>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月26日 下午4:52:14
 */
@Service
public class TreeUtils {

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private ResourceService resourceService;

	/**
	 * @Title: 设备组成员树<br/>
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return 设备组树信息
	 */
	public Object queryGroupTree(Long groupId) throws Exception{
		
//		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		List<CommandGroupMemberPO> members = group.getMembers();
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//过滤掉无用的文件夹
		List<FolderBO> usefulFolders = filterUsefulFolders(folders, null, members);
		
		//得到所有的相关用户
		List<UserBO> userBOs = commandCommonUtil.queryUsersByMembers(members);
		
		//找所有的根
		List<FolderBO> roots = findRoots(usefulFolders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, usefulFolders, null, null, members, userBOs);
		}
		
		return _roots;
	}
	
	/**
	 * 过滤出有用的文件夹<br/>
	 * <p>即子孙含有bundles或users的文件夹</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午11:39:48
	 * @param allFolders 过滤前的文件夹
	 * @param bundles
	 * @param users
	 * @return 过滤后的文件夹
	 */
	private List<FolderBO> filterUsefulFolders(
			List<FolderBO> allFolders,
			List<BundleBO> bundles,
			List<CommandGroupMemberPO> users){
		
		Set<FolderBO> usefulFolders = new HashSet<FolderBO>();
		Set<Long> ids = new HashSet<Long>();
		if(bundles!=null && bundles.size()>0){
			for(BundleBO bundle : bundles){
				ids.add(bundle.getFolderId());
			}
		}
		if(users!=null && users.size()>0){
			for(CommandGroupMemberPO user : users){
				ids.add(user.getFolderId());
			}
		}
		
		Set<Long> newIds = new HashSet<Long>();
		for(Long id : ids){
			for(FolderBO folder : allFolders){
				if(folder.getId().equals(id)){
					usefulFolders.add(folder);
					
					String parentPath = folder.getParentPath();
					if(parentPath != null){
						String[] parentPathStrings = parentPath.split("/");
						for(String path : parentPathStrings){
							if(!path.equals("")){
								newIds.add(Long.parseLong(path));
							}
						}
					}
				}
			}
		}
		
		if(newIds.size() > 0){
			for(Long id : newIds){
				for(FolderBO folder : allFolders){
					if(folder.getId().equals(id)){
						usefulFolders.add(folder);
					}
				}
			}
		}
		
		return new ArrayList<FolderBO>(usefulFolders);
	}
	
	/**递归组文件夹层级。
	 * userBOs列表可以通过commandCommonUtil.queryUsersByMembers(members)获得，用来给members提供用户的在线状态*/
	private void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles, 
			List<ChannelBO> channels,
			List<CommandGroupMemberPO> members,
			List<UserBO> userBOs){
		
		if(userBOs == null) userBOs = new ArrayList<UserBO>();
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles, channels, members, userBOs);
			}
		}
		
		//往里装设备
		if(bundles!=null && bundles.size()>0){
			for(BundleBO bundle:bundles){
				if(bundle.getFolderId()!=null && root.getId().equals(bundle.getFolderId().toString())){
					TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
															.setChildren(new ArrayList<TreeNodeVO>());
					root.getChildren().add(bundleNode);
					if(!BundleBO.BundleRealType.XT.toString().equals(bundle.getRealType()) && channels!=null && channels.size()>0){
						for(ChannelBO channel:channels){
							if(channel.getBundleId().equals(bundleNode.getId())){
								TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
								bundleNode.getChildren().add(channelNode);
							}
						}
					}
				}
			}
		}
		
		//往里装用户
		if(members!=null && members.size()>0){
			for(CommandGroupMemberPO member : members){
				if(member.getFolderId()!=null && root.getId().equals(member.getFolderId().toString())){
					UserBO userBO = queryUtil.queryUserById(userBOs, member.getUserId());
					TreeNodeVO userNode = new TreeNodeVO().set(member, userBO);
					root.getChildren().add(userNode);
				}
			}
		}
		
	}
	
	/**
	 * 查找根节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:32:53
	 * @param List<FolderBO> folders 查找范围
	 * @return List<FolderBO> 根节点列表
	 */
	private List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
}
