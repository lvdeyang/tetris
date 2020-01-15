package com.sumavision.bvc.control.device.group.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.ChannelNameVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupVO;
import com.sumavision.bvc.control.device.group.vo.GroupMemberChannelVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.device.auth.bo.SetUserAuthByUsernamesAndCidsBO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.BundlePreviewBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupRecordSchemeDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.feign.LiveAndAssetAuthServiceClient;
import com.sumavision.bvc.meeting.logic.dao.OmcRecordDao;
import com.sumavision.bvc.meeting.logic.po.OmcRecordPO;
import com.sumavision.bvc.meeting.logic.record.omc.CdnPO;
import com.sumavision.bvc.system.dao.ChannelNameDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/device/group/preview")
public class DeviceGroupPreviewController {

	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupBusinessRoleDAO deviceGroupBusinessRoleDao;
	
	@Autowired
	private ChannelNameDAO channelNameDao;
	
	@Autowired
	private DeviceGroupRecordSchemeDAO deviceGroupRecordSchemeDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private OmcRecordDao omcRecordDao;
	
	@Autowired
	private LiveAndAssetAuthServiceClient liveAndAssetAuthServiceClient;
	
	/**
	 * @Title: 设备组控制页面初始化数据 <br/>
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return group:DeviceGroupVO 设备组基本信息
	 * @return members:
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/code/{groupId}", method = RequestMethod.GET)
	public Object queryCode(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
						
		//返回会议基本信息
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		DeviceGroupVO _group = new DeviceGroupVO().setBasicInfo(group);
		
		//返回会议成员
		Set<DeviceGroupMemberPO> members = group.getMembers();
		List<GroupMemberChannelVO> channelsVO = new ArrayList<GroupMemberChannelVO>();
		
//		//保留有录制且可预览的成员
//		Set<DeviceGroupMemberPO> members = new HashSet<DeviceGroupMemberPO>();
//		
		Set<RecordPO> recordPOs = group.getRecords();
		CdnPO cdnPo = CdnPO.getFromOmcConfigFile();
				
		List<FolderBO> folders = new ArrayList<FolderBO>();
//		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<BundlePreviewBO> previewBundles = new ArrayList<BundlePreviewBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();
		
		//找到所有角色
		List<DeviceGroupBusinessRolePO> rolePOs = deviceGroupBusinessRoleDao.findByGroupId(groupId);
		
		//通道别名
		List<ChannelNamePO> channelNamePOs = channelNameDao.findAll();
		HashMap<String, String> channelAlias = new HashMap<String, String>();
		for(ChannelNamePO channelNamePO : channelNamePOs){
			channelAlias.put(channelNamePO.getChannelType(), channelNamePO.getName());
		}
		
		//角色过滤录制
		List<Long> roleIds = deviceGroupRecordSchemeDao.findRoleIdsByGroupId(groupId);
		
		//角色信息
		List<DeviceGroupBusinessRoleVO> roleVOs = DeviceGroupBusinessRoleVO.getConverter(DeviceGroupBusinessRoleVO.class).convert(rolePOs, DeviceGroupBusinessRoleVO.class);
		for(DeviceGroupBusinessRoleVO roleVO: roleVOs){
			if(roleIds.contains(roleVO.getId())){
				roleVO.setType(BusinessRoleType.RECORDABLE);
			}else{
				roleVO.setType(BusinessRoleType.DEFAULT);
			}
			Set<ChannelType> channelTypes = queryUtil.queryUnionChannelTypesByRole(group, roleVO.getId());
			for(ChannelType channelType: channelTypes){	
				ChannelNameVO channelName = new ChannelNameVO().setChannelType(channelType)
																 .setName(channelAlias.get(channelType.toString()));
				roleVO.getChannels().add(channelName);
			}						
		}
		
		//设备过滤录制
		Set<Long> folderIds = new HashSet<Long>();
		for(DeviceGroupMemberPO member: members){
			//判断成员是否有录制及播放地址
			boolean hasRecord = false;
			String previewPlayUrl = null;
			for(RecordPO recordPO : recordPOs){
				if(recordPO.getVideoBundleId().equals(member.getBundleId())){
					String uuid = recordPO.getUuid();
					OmcRecordPO omcRecordPO = omcRecordDao.findByUuid(uuid);
					if(null!=omcRecordPO){
						String cdnChannelId = omcRecordPO.getCdnChannelId();
						if(null!=cdnChannelId && !cdnChannelId.equals("")){
							previewPlayUrl = new StringBuilder().append("http://")
							   .append(cdnPo.getPlayIp())
							   .append(":")
							   .append(cdnPo.getPlayPort())
							   .append("/ipvs/")
							   .append(cdnChannelId)
							   .append(".m3u8?playtype=tstv&offset=1&sid=xxx&param=xxx")
							   .toString();	
							hasRecord = true;
							break;
						}
					}
				}
			}
			//没有录制则不添加该成员到树
			if(!hasRecord){
				break;
			}
			
			Set<DeviceGroupMemberChannelPO> channelsPO = member.getChannels();
			Set<DeviceGroupMemberChannelPO> filterChannelsPO = new HashSet<DeviceGroupMemberChannelPO>();
			filterChannelsPO.addAll(channelsPO);
			
			//过滤可录制角色设备(过滤树channel)
			if(roleIds != null && roleIds.size()>0){
				for(DeviceGroupMemberChannelPO channelPO: channelsPO){				
					for(Long roleId: roleIds){					
						if(channelPO.getMember().getRoleId() != null){
							if(channelPO.getMember().getRoleId().equals(roleId) && channelPO.getType().isVideoDecode()){
								filterChannelsPO.remove(channelPO);
								break;
							}
						}						
					}
				}
			}
			
			List<GroupMemberChannelVO> channelVOsList = GroupMemberChannelVO.getConverter(GroupMemberChannelVO.class).convert(filterChannelsPO, GroupMemberChannelVO.class);
			channelsVO.addAll(channelVOsList);
			
			//排序
			Collections.sort(channelsVO, new GroupMemberChannelVO.ChannelComparetor());
			
			for(DeviceGroupMemberChannelPO channelPO:channelsPO){
				ChannelBO channelBO = new ChannelBO().set(channelPO)
													.setMemberId(member.getId());
				channels.add(channelBO);
			}
			folderIds.add(member.getFolderId());
			previewBundles.add(new BundlePreviewBO().set(member, previewPlayUrl));
		}
		
		//排序
		Collections.sort(previewBundles, new BundleBO.BundleIdComparator());
		
		//根据folderIds查询父级信息
		Map<Long, FolderPO> queryFoleders = resourceService.queryFoleders(folderIds);
		Collection<FolderPO> folderValues = queryFoleders.values();
		Iterator<FolderPO> iter = folderValues.iterator();
		while(iter.hasNext()){
			FolderPO folderPO = iter.next();
			if(folderPO!=null &&!folderPO.getParentId().equals(Long.valueOf(TreeNodeVO.FOLDERID_ROOT))){
				String parentPath = folderPO.getParentPath();
				String[] paths = parentPath.split("/");
				for(int i=1; i<paths.length; i++){
					folderIds.add(Long.valueOf(paths[i]));
				}
			}		
		}
		
		//根据新folderIds查询所有层级（文件夹）
		Map<Long, FolderPO> allFoleders = resourceService.queryFoleders(folderIds);
		Collection<FolderPO> allValues = allFoleders.values();
		Iterator<FolderPO> allIter = allValues.iterator();
		while(allIter.hasNext()){
			FolderPO allFolderPO = allIter.next();
			if(allFolderPO == null) continue;
			FolderBO folderBO = new FolderBO().setId(allFolderPO.getId())
					  						  .setName(allFolderPO.getName())
					  						  .setParentId(allFolderPO.getParentId())
					  						  .setParentPath(allFolderPO.getParentPath());
			folders.add(folderBO);			
		}
		
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolderWithPreviewPlayUrl(_root, folders, previewBundles, new ArrayList<ChannelBO>());//new ArrayList<ChannelBO>();
		}
		
		//返回大小屏的模式
		List<String> screenLayouts = new ArrayList<String>();
		for(ScreenLayout screenLayout : ScreenLayout.values()){
			screenLayouts.add(screenLayout.getName());
		}
		
		return new HashMapWrapper<String, Object>().put("group", _group)
												   .put("membersTree", _roots)
												   .put("members", channelsVO)
												   .put("roles", roleVOs)
												   .put("screenLayouts", screenLayouts)
												   .getMap();
	}
	
	/**
	 * @Title: 设备组成员树<br/>
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return 设备组树信息
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/tree/{groupId}", method = RequestMethod.GET)
	public Object queryTree(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//返回会议基本信息
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		//返回会议成员
		Set<DeviceGroupMemberPO> members = group.getMembers();
		Set<GroupMemberChannelVO> channelsVO = new HashSet<GroupMemberChannelVO>();
		
		Set<Long> folderIds = new HashSet<Long>();
		for(DeviceGroupMemberPO member: members){
			Set<DeviceGroupMemberChannelPO> channelsPO = member.getChannels();
			
			List<GroupMemberChannelVO> channelVOsList = GroupMemberChannelVO.getConverter(GroupMemberChannelVO.class).convert(channelsPO, GroupMemberChannelVO.class);
			channelsVO.addAll(channelVOsList);
			
			for(DeviceGroupMemberChannelPO channelPO:channelsPO){
				ChannelBO channelBO = new ChannelBO().set(channelPO)
													.setMemberId(member.getId());
				channels.add(channelBO);
			}
			folderIds.add(member.getFolderId());
			bundles.add(new BundleBO().set(member));
		}
		
		//根据folderIds查询父级信息
		Map<Long, FolderPO> queryFoleders = resourceService.queryFoleders(folderIds);
		Collection<FolderPO> folderValues = queryFoleders.values();
		Iterator<FolderPO> iter = folderValues.iterator();
		while(iter.hasNext()){
			FolderPO folderPO = iter.next();
			if(folderPO!=null && folderPO.getParentId()!=TreeNodeVO.FOLDERID_ROOT && 
					folderPO.getParentId()!=TreeNodeVO.FOLDERID_COMBINEJV230 &&
					folderPO.getParentId()!=TreeNodeVO.FOLDERID_SPOKESMAN){
				String parentPath = folderPO.getParentPath();
				String[] paths = parentPath.split("/");
				for(int i=1; i<paths.length; i++){
					folderIds.add(Long.valueOf(paths[i]));
				}
			}		
		}
		
		//根据新folderIds查询所有层级（文件夹）
		Map<Long, FolderPO> allFoleders = resourceService.queryFoleders(folderIds);
		Collection<FolderPO> allValues = allFoleders.values();
		Iterator<FolderPO> allIter = allValues.iterator();
		while(allIter.hasNext()){
			FolderPO allFolderPO = allIter.next();
			if(allFolderPO == null) continue;
			FolderBO folderBO = new FolderBO().setId(allFolderPO.getId())
					  						  .setName(allFolderPO.getName())
					  						  .setParentId(allFolderPO.getParentId())
					  						  .setParentPath(allFolderPO.getParentPath());
			folders.add(folderBO);			
		}
		
		//加入大屏文件夹
		for(BundleBO bundle:bundles){
			if("combineJv230".equals(bundle.getModel())){
				folders.add(new FolderBO().setId(TreeNodeVO.FOLDERID_COMBINEJV230).setName("拼接屏设备"));
				break;
			}
		}
		
		//排序
		Collections.sort(bundles, new BundleBO.BundleIdComparator());
		Collections.sort(bundles, new BundleBO.BundleStatusComparator());
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, bundles, channels);
		}
		
		return new HashMapWrapper<String, Object>().put("membersTree", _roots)			   
				   .getMap();
	}
	
	/**找到根节点*/
	public List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
	/**递归组文件夹层级*/
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles, 
			List<ChannelBO> channels){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles, channels);
			}
		}
		
		//往里装设备
		for(BundleBO bundle:bundles){
			if(bundle.getFolderId().toString().equals(root.getId())){
				TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(bundleNode);
				for(ChannelBO channel:channels){
					if(channel.getBundleId().equals(bundleNode.getId())){
						TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
						bundleNode.getChildren().add(channelNode);
					}
				}
			}
		}
	}
	
	/**递归组文件夹层级，带预览地址*/
	public void recursionFolderWithPreviewPlayUrl(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundlePreviewBO> bundles, 
			List<ChannelBO> channels){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolderWithPreviewPlayUrl(folderNode, folders, bundles, channels);
			}
		}
		
		//往里装设备
		for(BundlePreviewBO bundle:bundles){
			if(bundle.getFolderId().toString().equals(root.getId())){
				TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(bundleNode);
				for(ChannelBO channel:channels){
					if(channel.getBundleId().equals(bundleNode.getId())){
						TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
						bundleNode.getChildren().add(channelNode);
					}
				}
			}
		}
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/add/record/{groupId}", method = RequestMethod.POST)
	public void addSelectRecord(
			@PathVariable Long groupId, String recordList, String userName, HttpServletRequest request) throws Exception{
			
		JSONArray records = JSON.parseArray(recordList);
		userName = userName.trim();
		
		//添加看会权限
		SetUserAuthByUsernamesAndCidsBO bo = new SetUserAuthByUsernamesAndCidsBO();
		
		//添加用户名
		if(!userName.contains("omcuser")){
			userName = userName + ",omcuser";
		}
		ArrayList<String> userNames = new ArrayList<String>();
		for(String userNameTemp : userName.split(",")){
			userNames.add(userNameTemp);
		}
		bo.setUsernames(userNames);
		
		//添加videoLiveID(cid)
		Set<String> cids = new HashSet<String>();
		for(int i=0; i<records.size(); i++){
			String recordUuid = records.getJSONObject(i).getString("recordUuid");
			OmcRecordPO omcRecordPO = omcRecordDao.findByUuid(recordUuid);
			cids.add(omcRecordPO.getBoVideoLiveID());
		}
		bo.setCids(cids);
		
		try {
			liveAndAssetAuthServiceClient.setUserAuthByUsernames(bo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.warn("添加看会权限的setUserAuthByUsernames报错");
		}
		
		
	}
}