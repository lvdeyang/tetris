package com.sumavision.bvc.control.device.group.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.control.device.group.vo.ChannelForwardVO;
import com.sumavision.bvc.control.device.group.vo.ChannelNameVO;
import com.sumavision.bvc.control.device.group.vo.CombineAudioSrcVO;
import com.sumavision.bvc.control.device.group.vo.CombineVideoSrcVo;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupVO;
import com.sumavision.bvc.control.device.group.vo.GroupMemberChannelVO;
import com.sumavision.bvc.control.device.group.vo.GroupMemberScreenVO;
import com.sumavision.bvc.control.device.group.vo.ScreenNameVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeIcon;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeType;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.dao.ChannelForwardDAO;
import com.sumavision.bvc.device.group.dao.CombineAudioDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupRecordSchemeDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.exception.PublishStreamException;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.group.service.AudioServiceImpl;
import com.sumavision.bvc.device.group.service.DeviceGroupServiceImpl;
import com.sumavision.bvc.device.group.service.PublishStreamServiceImpl;
import com.sumavision.bvc.device.group.service.VideoServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.meeting.logic.record.mims.MimsService;
import com.sumavision.bvc.system.dao.ChannelNameDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/device/group/control")
public class DeviceGroupControlController {

	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupConfigDAO deviceGroupConfigDao;
	
	@Autowired
	private DeviceGroupBusinessRoleDAO deviceGroupBusinessRoleDao;
	
	@Autowired
	private CombineVideoSrcDAO combineVideoSrcDao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private ChannelForwardDAO channelForwardDao;
	
	@Autowired
	private ChannelNameDAO channelNameDao;
	
	@Autowired
	private DeviceGroupRecordSchemeDAO deviceGroupRecordSchemeDao;
	
	@Autowired
	private DeviceGroupServiceImpl deviceGroupServiceImpl;
	
	@Autowired
	private PublishStreamServiceImpl publishStreamServiceImpl;
	
	@Autowired
	private VideoServiceImpl videoServiceImpl;
	
	@Autowired
	private AudioServiceImpl audioServiceImpl;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private MimsService mimsService;
	
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
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
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
			bundles.add(new BundleBO().set(member));
		}
		bundles = resourceQueryUtil.appendBundleOnlineStatusIntoBundleBos(bundles);
		_group.setCount(bundles);
		
		//排序
		Collections.sort(bundles, new BundleBO.BundleIdComparator());
		Collections.sort(bundles, new BundleBO.BundleStatusComparator());
		
		//根据新folderIds查询所有层级（文件夹）
		List<FolderPO> allFolders = resourceQueryUtil.queryFoldersTree(folderIds);

		if(allFolders != null && allFolders.size()>0){
			for(FolderPO allFolderPO: allFolders){
				if(allFolderPO == null) continue;
				FolderBO folderBO = new FolderBO().set(allFolderPO);
				folders.add(folderBO);			
			}
		}
		
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, bundles, channels);
		}
		
		//加入发言人节点
		TreeNodeVO spokesManRoot = new TreeNodeVO().setId(String.valueOf(TreeNodeVO.FOLDERID_SPOKESMAN))
												   .setName("发言人列表")
												   .setType(TreeNodeType.FOLDER)
												   .setKey()
												   .setIcon(TreeNodeIcon.FOLDER.getName())
												   .setChildren(new ArrayList<TreeNodeVO>());
		_roots.add(spokesManRoot);
		
		//查询系统默认角色
		DeviceGroupBusinessRolePO chairmanRole = null;
		List<DeviceGroupBusinessRolePO> spokesmanRoles = new ArrayList<DeviceGroupBusinessRolePO>();
		for(DeviceGroupBusinessRolePO role:rolePOs){
			if(BusinessRoleSpecial.CHAIRMAN.equals(role.getSpecial())){
				chairmanRole = role;
			}else if(BusinessRoleSpecial.SPOKESMAN.equals(role.getSpecial())){
				spokesmanRoles.add(role);
			}
		}
		
		//查询编码通道类型并集
		Map<ChannelType, String> typeMap = queryUtil.queryUnionEncodeChannelType(group);
		
		if(typeMap.size() > 0){
			//加入主席
			if(chairmanRole != null){
				String bundleName = queryUtil.queryBundleNameByUniqueRole(group, chairmanRole.getId());
				TreeNodeVO chairmanNode = new TreeNodeVO().set(chairmanRole, bundleName)
														  .setChildren(new ArrayList<TreeNodeVO>());
				Set<ChannelType> types = typeMap.keySet();
				for(ChannelType type:types){
					TreeNodeVO channelNode = new TreeNodeVO().set(chairmanRole, type, typeMap.get(type));
					chairmanNode.getChildren().add(channelNode);
				}
				spokesManRoot.getChildren().add(chairmanNode);
			}
			
			//加入发言人
			if(spokesmanRoles.size() > 0){
				for(DeviceGroupBusinessRolePO spokesmanRole:spokesmanRoles){
					String bundleName = queryUtil.queryBundleNameByUniqueRole(group, spokesmanRole.getId());
					TreeNodeVO spokesmanNode = new TreeNodeVO().set(spokesmanRole, bundleName)
							  								   .setChildren(new ArrayList<TreeNodeVO>());
					Set<ChannelType> types = typeMap.keySet();
					for(ChannelType type:types){
						TreeNodeVO channelNode = new TreeNodeVO().set(spokesmanRole, type, typeMap.get(type));
						spokesmanNode.getChildren().add(channelNode);
					}
					spokesManRoot.getChildren().add(spokesmanNode);
				}
			}
		}
		
		//加入虚拟源节点
		TreeNodeVO virtualSourceRoot = new TreeNodeVO().setId(String.valueOf(TreeNodeVO.FOLDERID_VIRTUALSOURCE))
													   .setName("虚拟源列表")
													   .setType(TreeNodeType.FOLDER)
													   .setKey()
													   .setIcon(TreeNodeIcon.FOLDER.getName())
													   .setChildren(new ArrayList<TreeNodeVO>());
		_roots.add(virtualSourceRoot);
		
		//查询虚拟源
		List<DeviceGroupConfigVideoPO> virtualVideos = new ArrayList<DeviceGroupConfigVideoPO>();
		List<DeviceGroupConfigPO> virtualConfigs = deviceGroupConfigDao.findByGroupIdAndType(groupId, ConfigType.VIRTUAL);
		for(DeviceGroupConfigPO virConfig: virtualConfigs){
			//判断有没有合屏源（新建的时候没有position，修改的时候必须有源，所以根据有误position判断即可）
			for(DeviceGroupConfigVideoPO configVideoPO : virConfig.getVideos()){
				if(configVideoPO.getPositions()!=null && configVideoPO.getPositions().size()!=0){
					virtualVideos.add(configVideoPO);
				}
			}
		}
		for(DeviceGroupConfigVideoPO video: virtualVideos){
			TreeNodeVO virtualNode = new TreeNodeVO().set(video);
			virtualSourceRoot.getChildren().add(virtualNode);
		}
		
		//返回大小屏的模式--对象
		List<JSONObject> screenLayouts = new ArrayList<JSONObject>();
		for(ScreenLayout screenLayout : ScreenLayout.values()){
			if(screenLayout.isVisible()){
				JSONObject layout = new JSONObject();
				layout.put("value", screenLayout.getName());
				layout.put("label", screenLayout.getName());
				screenLayouts.add(layout);
			}		
		}		
		
		return new HashMapWrapper<String, Object>().put("group", _group)
												   .put("membersTree", _roots)
												   .put("members", channelsVO)
												   .put("roles", roleVOs)
												   .put("screenLayouts", screenLayouts)
												   .getMap();
	}
	
	/**
	 * @Title: 查询转发需要的角色 
	 * @param @param groupId
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/forward/roles/{groupId}", method = RequestMethod.GET)
	public Object queryForwardRoles(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
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
		
		return roleVOs;		
	}
	
	/**
	 * @Title: 查询转发需要的设备和角色--通道 
	 * @param @param groupId
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/members/and/roles/channel/{groupId}", method = RequestMethod.GET)
	public Object queryMembersAndRolesChannel(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
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
		
		//成员信息
		List<GroupMemberChannelVO> channelsVO = new ArrayList<GroupMemberChannelVO>();
		for(DeviceGroupMemberPO member: members){
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
		}
		
		//排序
		Collections.sort(channelsVO, new GroupMemberChannelVO.ChannelComparetor());
		
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
		
		return new HashMapWrapper<String, Object>().put("members", channelsVO)
												   .put("roles", roleVOs)
												   .getMap();	
	}
	
	/**
	 * @Title: 查询转发需要的设备和角色--屏幕 
	 * @param @param groupId
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/members/and/roles/screen/{groupId}", method = RequestMethod.GET)
	public Object queryMembersAndRolesScreen(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		//找到所有角色
		List<DeviceGroupBusinessRolePO> rolePOs = deviceGroupBusinessRoleDao.findByGroupId(groupId);
		
		//角色过滤录制
		List<Long> roleIds = deviceGroupRecordSchemeDao.findRoleIdsByGroupId(groupId);
		
		//成员信息
		List<GroupMemberScreenVO> screensVO = new ArrayList<GroupMemberScreenVO>();
		for(DeviceGroupMemberPO member: members){
			Set<DeviceGroupMemberScreenPO> screenPOs = member.getScreens();
			Set<DeviceGroupMemberScreenPO> filterScreenPOs = new HashSet<DeviceGroupMemberScreenPO>();
			filterScreenPOs.addAll(screenPOs);
			
			//过滤可录制角色设备(过滤树channel)
			if(roleIds != null && roleIds.size()>0){
				for(DeviceGroupMemberScreenPO screenPO: screenPOs){				
					for(Long roleId: roleIds){					
						if(screenPO.getMember().getRoleId() != null){
							if(screenPO.getMember().getRoleId().equals(roleId)){
								filterScreenPOs.remove(screenPO);
								break;
							}
						}						
					}
				}
			}
			
			List<GroupMemberScreenVO> screenVOsList = GroupMemberScreenVO.getConverter(GroupMemberScreenVO.class).convert(filterScreenPOs, GroupMemberScreenVO.class);
			screensVO.addAll(screenVOsList);
		}
		
		//排序
		Collections.sort(screensVO, new GroupMemberScreenVO.ScreenComparetor());
		
		//角色信息
		List<DeviceGroupBusinessRoleVO> roleVOs = DeviceGroupBusinessRoleVO.getConverter(DeviceGroupBusinessRoleVO.class).convert(rolePOs, DeviceGroupBusinessRoleVO.class);
		for(DeviceGroupBusinessRoleVO roleVO: roleVOs){
			if(roleIds.contains(roleVO.getId())){
				roleVO.setType(BusinessRoleType.RECORDABLE);
			}else{
				roleVO.setType(BusinessRoleType.DEFAULT);
			}
			Set<String> screenIds = queryUtil.queryUnionScreenIdsByRole(group, roleVO.getId());
			screenIds.add("screen_1");
			for(String screenId: screenIds){	
				ScreenNameVO screenName = new ScreenNameVO().setScreenId(screenId)
						 									.setName("屏幕"+screenId.split("_")[1]);
				roleVO.getScreens().add(screenName);
			}						
		}
		
		return new HashMapWrapper<String, Object>().put("members", screensVO)
												   .put("roles", roleVOs)
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
		DeviceGroupVO _group = new DeviceGroupVO();
		
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
		bundles = resourceQueryUtil.appendBundleOnlineStatusIntoBundleBos(bundles);
		_group.setCount(bundles);
		
		//根据新folderIds查询所有层级（文件夹）
		List<FolderPO> allFolders = resourceQueryUtil.queryFoldersTree(folderIds);

		if(allFolders != null && allFolders.size()>0){
			for(FolderPO allFolderPO: allFolders){
				if(allFolderPO == null) continue;
				FolderBO folderBO = new FolderBO().set(allFolderPO);
				folders.add(folderBO);			
			}
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
				   .put("group", _group)
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
	@RequestMapping(value = "/query/tree/and/spokesman/{groupId}", method = RequestMethod.GET)
	public Object queryTreeAndSpokesman(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//返回会议基本信息
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		//找到所有角色
		List<DeviceGroupBusinessRolePO> rolePOs = deviceGroupBusinessRoleDao.findByGroupId(groupId);
		
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
		
		//根据新folderIds查询所有层级（文件夹）
		List<FolderPO> allFolders = resourceQueryUtil.queryFoldersTree(folderIds);

		if(allFolders != null && allFolders.size()>0){
			for(FolderPO allFolderPO: allFolders){
				if(allFolderPO == null) continue;
				FolderBO folderBO = new FolderBO().set(allFolderPO);
				folders.add(folderBO);			
			}
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
		
		//加入发言人节点
		TreeNodeVO spokesManRoot = new TreeNodeVO().setId(String.valueOf(TreeNodeVO.FOLDERID_SPOKESMAN))
												   .setName("发言人列表")
												   .setType(TreeNodeType.FOLDER)
												   .setKey()
												   .setIcon(TreeNodeIcon.FOLDER.getName())
												   .setChildren(new ArrayList<TreeNodeVO>());
		_roots.add(spokesManRoot);
		
		//查询系统默认角色
		DeviceGroupBusinessRolePO chairmanRole = null;
		List<DeviceGroupBusinessRolePO> spokesmanRoles = new ArrayList<DeviceGroupBusinessRolePO>();
		for(DeviceGroupBusinessRolePO role:rolePOs){
			if(BusinessRoleSpecial.CHAIRMAN.equals(role.getSpecial())){
				chairmanRole = role;
			}else if(BusinessRoleSpecial.SPOKESMAN.equals(role.getSpecial())){
				spokesmanRoles.add(role);
			}
		}
		
		//查询编码通道类型并集
		Map<ChannelType, String> typeMap = queryUtil.queryUnionEncodeChannelType(group);
		
		if(typeMap.size() > 0){
			//加入主席
			if(chairmanRole != null){
				String bundleName = queryUtil.queryBundleNameByUniqueRole(group, chairmanRole.getId());
				TreeNodeVO chairmanNode = new TreeNodeVO().set(chairmanRole, bundleName)
														  .setChildren(new ArrayList<TreeNodeVO>());
				Set<ChannelType> types = typeMap.keySet();
				for(ChannelType type:types){
					TreeNodeVO channelNode = new TreeNodeVO().set(chairmanRole, type, typeMap.get(type));
					chairmanNode.getChildren().add(channelNode);
				}
				spokesManRoot.getChildren().add(chairmanNode);
			}
			
			//加入发言人
			if(spokesmanRoles.size() > 0){
				for(DeviceGroupBusinessRolePO spokesmanRole:spokesmanRoles){
					String bundleName = queryUtil.queryBundleNameByUniqueRole(group, spokesmanRole.getId());
					TreeNodeVO spokesmanNode = new TreeNodeVO().set(spokesmanRole, bundleName)
							  								   .setChildren(new ArrayList<TreeNodeVO>());
					Set<ChannelType> types = typeMap.keySet();
					for(ChannelType type:types){
						TreeNodeVO channelNode = new TreeNodeVO().set(spokesmanRole, type, typeMap.get(type));
						spokesmanNode.getChildren().add(channelNode);
					}
					spokesManRoot.getChildren().add(spokesmanNode);
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("membersTree", _roots)			   
				   .getMap();
	}
	
	/**
	 * @Title: 设备组全局加音量<br/>
	 * @param groupId 设备组id
	 * @param volume 音量值
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/add/volume/{groupId}")
	public Object addVolume(
			@PathVariable Long groupId,
			int volume,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		group.setVolume(volume);
		
		//TODO 增益协议
		Set<CombineAudioPO> combineAudios = group.getCombineAudios();
		if(combineAudios != null && combineAudios.size()>0){
			audioServiceImpl.setAudioVolume(group, combineAudios);
		}
		
		deviceGroupDao.save(group);
		
		logService.logsHandle(user.getName(), "调节音量", "设备组名称："+group.getName()+"音量值："+volume);
		
		return null;
	}
	
	/**
	 * @Title: 添加已选音频<br/>
	 * @param groupId 设备组id
	 * @param audioList 打开音频的成员id数组
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/add/audio/{groupId}")
	public Object addSelectedAudio(
			@PathVariable Long groupId,
			String audioList,
			HttpServletRequest request) throws Exception{
			
		UserVO user = userUtils.getUserFromSession(request);
		
		JSONArray audioArr = JSON.parseArray(audioList);		
		List<Long> audioMemberIds = new ArrayList<Long>();
		for(int i=0;i<audioArr.size();i++){
			JSONObject audioObject = audioArr.getJSONObject(i);
			audioMemberIds.add(JSONObject.parseObject(audioObject.getString("param")).getLong("memberId"));
		}
		
		DeviceGroupPO group = deviceGroupServiceImpl.setGroupAudio(groupId, audioMemberIds);
		
		logService.logsHandle(user.getName(), "设置混音", "设备组名称："+group.getName());
		
		//写了获取之后再写vo
		return null;
	}
	
	/**
	 * @Title: 根据合屏uuid查询合屏源<br/>
	 * @param groupId 设备组id
	 * @param uuid 合屏uuid
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/forward/combineVideo/{uuid}", method = RequestMethod.GET)
	public Object queryForwardCombineVideo(
			@PathVariable String uuid,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		List<CombineVideoSrcPO> combineVideoSrcPOs = combineVideoSrcDao.findByCombineUuid(uuid);
		List<CombineVideoSrcVo> videoSrcs = new ArrayList<CombineVideoSrcVo>();
		if(combineVideoSrcPOs != null && combineVideoSrcPOs.size() > 0 ){
			videoSrcs = CombineVideoSrcVo.getConverter(CombineVideoSrcVo.class).convert(combineVideoSrcPOs, CombineVideoSrcVo.class);
		}
		
		JSONObject data = new JSONObject();
		data.put("rows", videoSrcs);
		data.put("total", 1);
		
		return data;
	}
	
	/**
	 * @Title: 查询合屏<br/>
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/combineVideo/{groupId}", method = RequestMethod.GET)
	public Object queryCombineVideo(
			@PathVariable Long groupId,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{

		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<Long> findByGroupId = combineVideoDao.findByGroupId(groupId, page);
		List<Long> content = findByGroupId.getContent();
		long total = 0l;
		
		List<CombineVideoSrcVo> videoSrcs = new ArrayList<CombineVideoSrcVo>();
		
		if(content != null && content.size()>0){	
			total = findByGroupId.getTotalElements();
			List<CombineVideoSrcPO> srcList = combineVideoSrcDao.findByGroupId(content);	
			videoSrcs = CombineVideoSrcVo.getConverter(CombineVideoSrcVo.class).convert(srcList, CombineVideoSrcVo.class);
		}
		
		JSONObject data = new JSONObject();
		data.put("rows", videoSrcs);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 根据混音uuid查混音源
	 * @param uuid 混音uuid
	 * @param pageSize
	 * @param currentPage
	 * @return
	 * @throws Exception    设定文件 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/forward/combineAudio/{uuid}", method = RequestMethod.GET)
	public Object queryForwardCombineAudio(
			@PathVariable String uuid,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		CombineAudioPO combineAudioPO = combineAudioDao.findByCombineUuid(uuid);
		List<CombineAudioSrcVO> audioSrcs = new ArrayList<CombineAudioSrcVO>();
		if(combineAudioPO != null){
			audioSrcs = CombineAudioSrcVO.getConverter(CombineAudioSrcVO.class).convert(combineAudioPO.getSrcs(), CombineAudioSrcVO.class);
		}
		
		JSONObject data = new JSONObject();
		data.put("rows", audioSrcs);
		data.put("total", 1);
		
		return data;		
	}
	
	/**
	 * @Title: 查询混音<br/>
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/combineAudio/{groupId}", method = RequestMethod.GET)
	public Object queryCombineAudio(
			@PathVariable Long groupId,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<CombineAudioPO> pageCombineAudioPOs = combineAudioDao.findByGroupId(groupId, page);
		List<CombineAudioPO> combineAudioPOs = pageCombineAudioPOs.getContent();
		long total = 0l;
		
		List<CombineAudioSrcVO> audioSrcs = new ArrayList<CombineAudioSrcVO>();
		
		if(combineAudioPOs != null && combineAudioPOs.size()>0){
			total = pageCombineAudioPOs.getTotalElements();
			List<CombineAudioSrcPO> srcPOs = new ArrayList<CombineAudioSrcPO>();	
			for(CombineAudioPO combineAudioPO: combineAudioPOs){
				srcPOs.addAll(combineAudioPO.getSrcs());
			}
			
			audioSrcs = CombineAudioSrcVO.getConverter(CombineAudioSrcVO.class).convert(srcPOs, CombineAudioSrcVO.class);
		}
		
		JSONObject data = new JSONObject();
		data.put("rows", audioSrcs);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 查询转发<br/>
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/forward/{groupId}", method = RequestMethod.GET)
	public Object queryForward(
			@PathVariable Long groupId,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		Page<ChannelForwardPO> pageForwards = channelForwardDao.findByGroupId(groupId, page);
		List<ChannelForwardPO> forwardPOs = pageForwards.getContent();
		long total = 0l;
		
		List<ChannelForwardVO> forwardList = new ArrayList<ChannelForwardVO>();
		
		if(forwardPOs != null && forwardPOs.size() > 0){
			total = pageForwards.getTotalElements();
			forwardList = ChannelForwardVO.getConverter(ChannelForwardVO.class).convert(forwardPOs, ChannelForwardVO.class);
		}
		
		JSONObject data = new JSONObject();
		data.put("rows", forwardList);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 查询转发<br/>
	 * @param uuid 
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/combine/forward/{uuid}", method = RequestMethod.GET)
	public Object queryCombineForward(
			@PathVariable String uuid,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		Page<ChannelForwardPO> pageForwards = channelForwardDao.findByCombineUuid(uuid, page);
		List<ChannelForwardPO> forwardPOs = pageForwards.getContent();
		long total = 0l;
		
		List<ChannelForwardVO> forwardList = new ArrayList<ChannelForwardVO>();
		
		if(forwardPOs != null && forwardPOs.size() > 0){
			total = pageForwards.getTotalElements();
			forwardList = ChannelForwardVO.getConverter(ChannelForwardVO.class).convert(forwardPOs, ChannelForwardVO.class);
		}
		
		JSONObject data = new JSONObject();
		data.put("rows", forwardList);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 根据id删除转发
	 * @param groupId
	 * @param forwardId
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/forward/{groupId}/{forwardId}")
	public Object removeForward(
			@PathVariable Long groupId,
			@PathVariable Long forwardId,
			HttpServletRequest request) throws Exception{
						
		UserVO user = userUtils.getUserFromSession(request);
		
		deviceGroupServiceImpl.removeForward(groupId, forwardId);
		
		logService.logsHandle(user.getName(), "根据id删除转发", "转发id："+forwardId);
		
		return null;
	}
	
	/**
	 * @Title: 根据id转发重发
	 * @param groupId
	 * @param forwardId
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/refresh/forward/{groupId}/{forwardId}")
	public Object refreshForward(
			@PathVariable Long groupId,
			@PathVariable Long forwardId,
			HttpServletRequest request) throws Exception{
						
		UserVO user = userUtils.getUserFromSession(request);
		
		deviceGroupServiceImpl.refreshForward(groupId, forwardId);
		
		logService.logsHandle(user.getName(), "根据id转发重发", "转发id："+forwardId);
		
		return null;
	}
	
	/**
	 * @Title:根据合屏uuid合屏重发
	 * @param groupId
	 * @param combineVideoUuid
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/refresh/combineVideo/{groupId}")
	public Object refreshCombineVideo(
			@PathVariable Long groupId,
			String combineVideoUuid,
			HttpServletRequest request) throws Exception{
						
		UserVO user = userUtils.getUserFromSession(request);
		
		videoServiceImpl.refreshCombineVideo(groupId, combineVideoUuid);
		
		logService.logsHandle(user.getName(), "根据合屏uuid合屏重发", "合屏uuid："+combineVideoUuid);
		
		return null;
	}
	
	/**
	 * @Title:根据合屏uuid合屏删除
	 * @param groupId
	 * @param combineVideoUuid
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/combineVideo/{groupId}/{combineVideoUuid}")
	public Object removeCombineVideo(
			@PathVariable Long groupId,
			@PathVariable String combineVideoUuid,
			HttpServletRequest request) throws Exception{
						
		UserVO user = userUtils.getUserFromSession(request);
		
		deviceGroupServiceImpl.removeCombineVideo(groupId, combineVideoUuid);
		
		logService.logsHandle(user.getName(), "根据合屏uuid合屏删除", "合屏uuid："+combineVideoUuid);
		
		return null;
	}
	
	/**
	 * @Title:根据混音uuid重发
	 * @param groupId
	 * @param combineAudioUuid
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/refresh/combineAudio/{groupId}")
	public Object refreshCombineAudio(
			@PathVariable Long groupId,
			String combineAudioUuid,
			HttpServletRequest request) throws Exception{
						
		UserVO user = userUtils.getUserFromSession(request);
		
		audioServiceImpl.refreshCombineAduio(groupId, combineAudioUuid);
		
		logService.logsHandle(user.getName(), "根据混音uuid混音重发", "混音uuid："+combineAudioUuid);
		
		return null;
	}
	
	/**
	 * @Title: 设备组设备刷新<br/>
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/refresh/{groupId}")
	public Object devicesRefresh(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupServiceImpl.refreshDevice(groupId);
		
		logService.logsHandle(user.getName(), "设备组设备刷新", "设备组名称："+group.getName());
		
		return null;
	}
	
	/**
	 * @Title: 设备组同步角色<br/>
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/role/update/{groupId}")
	public Object roleUpdate(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupServiceImpl.roleUpdate(groupId);
		
		logService.logsHandle(user.getName(), "设备组同步角色", "设备组名称："+group.getName());
		
		return null;
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
	
	/**
	 * @Title: 把录制发布为RTMP<br/>
	 * @param groupId 设备组id
	 * @param rtmpUrl 发布地址，空字符串时会抛错
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/record/publishStream/{groupId}", method = RequestMethod.POST)
//	@RequestMapping(value = "/add/record/{groupId}", method = RequestMethod.POST)
	public Object publishStream(
			@PathVariable Long groupId, 
			String rtmpUrl, 
			HttpServletRequest request) throws Exception{
			
		String url = rtmpUrl.trim();
		
		if(url==null || url.equals("")){
			throw new PublishStreamException("发布地址不能为空");
		}
				
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		PublishStreamPO publishStream = publishStreamServiceImpl.autoPublishRtmp(group, group+"-会议直播", url);
		if(null == publishStream){			
			throw new PublishStreamException("没有可以发布的内容，请对“观众”转发视频");
		}
		mimsService.generateMimsResource(publishStream);
				
		return null;		
	}
}