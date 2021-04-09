package com.sumavision.bvc.control.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundlePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelType;
import com.sumavision.tetris.mvc.util.BaseUtils;

/**
 * 树相关操作，提取公用方法//第二轮重构<br/>
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
	private GroupDAO groupDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;

	@Autowired
	private RoleDAO roleDAO;

	private String separator = "&&";
	
//	/**
//	 * @Title: 设备组成员树<br/>
//	 * @param groupId 设备组id
//	 * @throws Exception 
//	 * @return 设备组树信息
//	 */
//	public Object queryGroupTree(Long groupId) throws Exception{
//		
//		List<FolderBO> folders = new ArrayList<FolderBO>();
//		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
//		
////		GroupPO group = groupDao.findOne(groupId);
//		List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
//		
//		//查询所有非点播的文件夹
//		List<FolderPO> totalFolders = resourceService.queryAllFolders();
//		for(FolderPO folder:totalFolders){
//			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
//				folders.add(new FolderBO().set(folder));
//			}
//		}
//		
//		//过滤掉无用的文件夹
//		List<FolderBO> usefulFolders = filterUsefulFolders(folders, null, members);
//		
//		//得到所有的相关用户【这里没有处理设备】
//		List<UserBO> userBOs = businessCommonService.queryUsersByGroupMembers(members);
//		
//		//找所有的根
//		List<FolderBO> roots = findRoots(usefulFolders);
//		for(FolderBO root:roots){
//			TreeNodeVO _root = new TreeNodeVO().set(root)
//											   .setChildren(new ArrayList<TreeNodeVO>());
//			_roots.add(_root);
//			recursionFolder(_root, usefulFolders, null, null, members, userBOs);
//		}
//		
//		return _roots;
//	}
	
	/**
	 * 设备组成员树：这里和第二轮重构前的完全不一样<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月25日 下午1:54:10
	 * @param groupId 业务组id
	 * @param withChannel 是否带通道
	 * @param channelTypeList 要查询通道类型
	 * @return List<TreeNodeVO> _roots 业务组树
	 * @throws Exception
	 */
	public Object queryGroupTree(Long groupId, 
			Boolean withChannel,
			String channelTypeList) throws Exception{
		
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByGroupId(groupId);
		
		//通过成员的folderId查找到所有文件夹以及父文件夹。
		//为所有成员以及文件夹建立对应的TreeNodeVO
		//通过parentId构建TreeNodeVO的关系
		Map<Long, TreeNodeVO> folderIdAndTreeNodeMap= generateTreeNodeVo(members,withChannel, channelTypeList);
		
		//找所有的根
		_roots = folderIdAndTreeNodeMap.values().stream().filter(treeNode->{
			return (treeNode.getParentId() != null && treeNode.getParentId().equals(-1L)) ? true : false ;
		}).collect(Collectors.toList());
		
		return _roots;
	}


	public Object queryCoorateGroupTree(Long groupId,
								 Boolean withChannel,
								 String channelTypeList) throws Exception{

		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		RolePO rolePO=roleDAO.findByInternalRoleType(InternalRoleType.COMMAND_COOPERATION);
		List<BusinessGroupMemberPO> members = businessGroupMemberDao.findByNoJoinGroupIdAndRoleId(groupId,rolePO.getId());

		//通过成员的folderId查找到所有文件夹以及父文件夹。
		//为所有成员以及文件夹建立对应的TreeNodeVO
		//通过parentId构建TreeNodeVO的关系
		Map<Long, TreeNodeVO> folderIdAndTreeNodeMap= generateTreeNodeVo(members,withChannel, channelTypeList);

		//找所有的根
		_roots = folderIdAndTreeNodeMap.values().stream().filter(treeNode->{
			return (treeNode.getParentId() != null && treeNode.getParentId().equals(-1L)) ? true : false ;
		}).collect(Collectors.toList());

		return _roots;
	}
	
	/**
	 * 通过成员的folderId查找到所有文件夹以及父文件夹,并为所有成员以及文件夹建立对应的TreeNodeVO.
	 * 通过parentId构建TreeNodeVO的关系
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月25日 下午2:03:17
	 * @param members
	 * @param withChannel
	 * @param channelTypeList
	 * @return Map<String, TreeNodeVO>,文件夹以FolderPO的id作为key
	 */
	public Map<Long, TreeNodeVO> generateTreeNodeVo(List<BusinessGroupMemberPO> members, Boolean withChannel, String channelTypeList){
		
		//所有文件夹的map集合[folderId, FolderPO],
		Map<Long, FolderPO> allFoldeIdAndFolder = new HashMap<Long, FolderPO>();
		
		//从成员中拿到成员父文件夹集合
		Set<Long> folederIdList= members.stream().filter(member->{
			return member.getFolderId()!=null;
		}).map(BusinessGroupMemberPO::getFolderId).collect(Collectors.toSet());
		Map<Long, FolderPO> memberFolderMap= resourceService.queryFoleders(folederIdList);
		allFoldeIdAndFolder.putAll(memberFolderMap);
		
		//成员直属folder的所有父级文件夹集合
		Set<Long> parentFolderIdList= memberFolderMap.values().stream().filter(folder->{
			return BaseUtils.stringIsNotBlank(folder.getParentPath());
		}).flatMap(folder->{
			return Stream.of(folder.getParentPath().replaceFirst("/", "").split("/")).map(Long::valueOf);
		}).collect(Collectors.toSet());
		allFoldeIdAndFolder.putAll(resourceService.queryFoleders(parentFolderIdList));
		
		//所有文件夹的map集合[]
		Map<Long, TreeNodeVO> allFolderTreeNodeMap = new HashMap<Long, TreeNodeVO>();
		
		//创建TreeNodeVO
		//生成members对应的TreeNodeVO集合
		List<TreeNodeVO> memberTreeNodeList= members.stream().map(member->{
			FolderPO folder = allFoldeIdAndFolder.get(member.getFolderId());
			if(folder == null){
				return null;
			}
			if(withChannel && BaseUtils.collectionIsNotBlank(member.getChannels())){
				List<String> channelTypeStrList = JSONArray.parseArray(channelTypeList, String.class);
				List<BusinessGroupMemberTerminalChannelPO> channelList = new ArrayList<BusinessGroupMemberTerminalChannelPO>();
				for(String typeStr :channelTypeStrList){
					for(BusinessGroupMemberTerminalChannelPO channel : member.getChannels()){
						if(channel.getTerminalChannelType().toString().equals(typeStr)){
							channelList.add(channel);
						}
					}
				}
				
				return new TreeNodeVO().set(member, folder.getId(), channelList);
			}else{
				return new TreeNodeVO().set(member, folder.getId());
			}
		}).filter(node->{return node == null? false : true;}).collect(Collectors.toList());
		
		//生成FolderPO对应的TreeNodeVO
		for(FolderPO folder : allFoldeIdAndFolder.values()){
			allFolderTreeNodeMap.put(folder.getId(), new TreeNodeVO().set(folder));
		}
		
		//将成员TreeNode放在文件夹TreeNode下
		memberTreeNodeList.stream().forEach(memberTreeNode->{
			TreeNodeVO folderTreeNode = allFolderTreeNodeMap.get(memberTreeNode.getParentId());
			if(folderTreeNode.getChildren() == null){
				folderTreeNode.setChildren(new ArrayList<TreeNodeVO>());
			}
			folderTreeNode.getChildren().add(memberTreeNode);
		});
		
		allFolderTreeNodeMap.values().stream().forEach(treeNode->{
			if(treeNode.getParentId() != null && !treeNode.getParentId().equals(-1L)){
				TreeNodeVO parentTreeNode = allFolderTreeNodeMap.get(treeNode.getParentId());
				if(parentTreeNode != null){
					List<TreeNodeVO> children = parentTreeNode.getChildren();
					if(children == null){
						children = new ArrayList<TreeNodeVO>();
						parentTreeNode.setChildren(children);
					}
					children.add(treeNode);
				}
			}
		});
		
		return allFolderTreeNodeMap;
	}

	/**
	 * @Title: 设备组成员树<br/>
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return 设备组树信息
	 */
	/*public Object queryGroupTree1(Long groupId) throws Exception{
		
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
	}*/
	
//	/**
//	 * 过滤出有用的文件夹<br/>
//	 * <p>即子孙含有bundles或users的文件夹</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年12月3日 上午11:39:48
//	 * @param allFolders 过滤前的文件夹
//	 * @param bundles
//	 * @param users
//	 * @return 过滤后的文件夹
//	 */
//	private List<FolderBO> filterUsefulFolders(
//			List<FolderBO> allFolders,
//			List<BundleBO> bundles,
//			List<GroupMemberPO> users){
//		
//		Set<FolderBO> usefulFolders = new HashSet<FolderBO>();
//		Set<Long> ids = new HashSet<Long>();
//		if(bundles!=null && bundles.size()>0){
//			for(BundleBO bundle : bundles){
//				ids.add(bundle.getFolderId());
//			}
//		}
//		if(users!=null && users.size()>0){
//			for(GroupMemberPO user : users){
//				ids.add(user.getFolderId());
//			}
//		}
//		
//		Set<Long> newIds = new HashSet<Long>();
//		for(Long id : ids){
//			for(FolderBO folder : allFolders){
//				if(folder.getId().equals(id)){
//					usefulFolders.add(folder);
//					
//					String parentPath = folder.getParentPath();
//					if(parentPath != null){
//						String[] parentPathStrings = parentPath.split("/");
//						for(String path : parentPathStrings){
//							if(!path.equals("")){
//								newIds.add(Long.parseLong(path));
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		if(newIds.size() > 0){
//			for(Long id : newIds){
//				for(FolderBO folder : allFolders){
//					if(folder.getId().equals(id)){
//						usefulFolders.add(folder);
//					}
//				}
//			}
//		}
//		
//		return new ArrayList<FolderBO>(usefulFolders);
//	}
//	
//	/**递归组文件夹层级。
//	 * userBOs列表可以通过commandCommonUtil.queryUsersByMembers(members)获得，用来给members提供用户的在线状态*/
//	private void recursionFolder(
//			TreeNodeVO root, 
//			List<FolderBO> folders, 
//			List<BundleBO> bundles, 
//			List<ChannelBO> channels,
//			List<GroupMemberPO> members,
//			List<UserBO> userBOs){
//		
//		if(userBOs == null) userBOs = new ArrayList<UserBO>();
//		
//		//往里装文件夹
//		for(FolderBO folder:folders){
//			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
//				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
//														.setChildren(new ArrayList<TreeNodeVO>());
//				root.getChildren().add(folderNode);
//				recursionFolder(folderNode, folders, bundles, channels, members, userBOs);
//			}
//		}
//		
//		//往里装设备
//		if(bundles!=null && bundles.size()>0){
//			for(BundleBO bundle:bundles){
//				if(bundle.getFolderId()!=null && root.getId().equals(bundle.getFolderId().toString())){
//					TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
//															.setChildren(new ArrayList<TreeNodeVO>());
//					root.getChildren().add(bundleNode);
//					if(!BundleBO.BundleRealType.XT.toString().equals(bundle.getRealType()) && channels!=null && channels.size()>0){
//						for(ChannelBO channel:channels){
//							if(channel.getBundleId().equals(bundleNode.getId())){
//								TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
//								bundleNode.getChildren().add(channelNode);
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		//往里装用户【后续处理设备】
//		if(members!=null && members.size()>0){
//			for(GroupMemberPO member : members){
//				if(member.getFolderId()!=null && root.getId().equals(member.getFolderId().toString())){
//					if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
//						UserBO userBO = queryUtil.queryUserById(userBOs, Long.parseLong(member.getOriginId()));
//						TreeNodeVO userNode = new TreeNodeVO().set(member, userBO);
//						root.getChildren().add(userNode);
//					}
//				}
//			}
//		}
//		
//	}
	
//	/**
//	 * 查找根节点<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年4月17日 下午1:32:53
//	 * @param List<FolderBO> folders 查找范围
//	 * @return List<FolderBO> 根节点列表
//	 */
//	private List<FolderBO> findRoots(List<FolderBO> folders){
//		List<FolderBO> roots = new ArrayList<FolderBO>();
//		for(FolderBO folder:folders){
//			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
//				roots.add(folder);
//			}
//		}
//		return roots;
//	}
	
	//-------------------------------new Agenda----------------------------------
//	/**
//	 * 取出包含成员的文件夹
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年11月25日 下午1:18:24
//	 * @param allFolders 所有文件夹
//	 * @param members 对应BusinessGroupMemberPO集合
//	 * @return
//	 */
//	private List<FolderBO> filterUsefulFoldersNew(
//			List<FolderBO> allFolders,
//			List<BusinessGroupMemberPO> members){
//		
//		Map<Long, FolderBO> folderIdAndFolderBoMap= allFolders.stream().collect(Collectors.toMap(FolderBO::getId, Function.identity()));
//		
//		Set<FolderBO> memberFolders = new HashSet<FolderBO>();
//		Set<Long> folderIds = new HashSet<Long>();
//
//		if(BaseUtils.collectionIsNotBlank(members)){
//			for(BusinessGroupMemberPO member : members){
//				folderIds.add(member.getFolderId());
//			}
//		}
//		
//		if(BaseUtils.collectionIsNotBlank(folderIds)){
//			folderIds.stream().map
//		}
//		
////		Set<Long> newIds = new HashSet<Long>();
////		for(Long id : ids){
////			for(FolderBO folder : allFolders){
////				if(folder.getId().equals(id)){
////					memberFolders.add(folder);
////					
////					String parentPath = folder.getParentPath();
////					if(BaseUtils.stringIsNotBlank(parentPath)){
////						Stream.of(parentPath.split("/")).map(path->{
////							
////						})
////						String[] parentPathStrings = parentPath.split("/");
////						for(String path : parentPathStrings){
////							if(!path.equals("")){
////								newIds.add(Long.parseLong(path));
////							}
////						}
////					}
////				}
////			}
////		}
////		
////		if(newIds.size() > 0){
////			for(Long id : newIds){
////				for(FolderBO folder : allFolders){
////					if(folder.getId().equals(id)){
////						memberFolders.add(folder);
////					}
////				}
////			}
////		}
//		
//		return new ArrayList<FolderBO>(memberFolders);
//	}
	
//	/**
//	 * 递归组文件夹层级。<br/>
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年11月25日 下午1:16:56
//	 * @param root 层节点
//	 * @param folders 所有文件夹
//	 * @param channels 所有通道
//	 * @param members 所有成员
//	 */
//	private void recursionFolderNew(
//			TreeNodeVO root, 
//			List<FolderBO> folders, 
//			List<BusinessGroupMemberTerminalBundleChannelPO> channels,
//			List<BusinessGroupMemberPO> members){
//		
//		//往里装文件夹
//		for(FolderBO folder:folders){
//			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
//				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
//														.setChildren(new ArrayList<TreeNodeVO>());
//				root.getChildren().add(folderNode);
//				recursionFolderNew(folderNode, folders, channels, members);
//			}
//		}
//		
//		//往里装BusinessGroupMember成员
//		if(members!=null && members.size()>0){
//			for(BusinessGroupMemberPO member : members){
//				if(member.getFolderId()!=null && root.getId().equals(member.getFolderId().toString())){
//					if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
//						TreeNodeVO userNode = new TreeNodeVO().set(member);
//						root.getChildren().add(userNode);
//					}
//				}
//			}
//		}
//		
//	}
	
	//-------------------------------new Agenda----------------------------------
}
