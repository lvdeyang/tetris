package com.sumavision.bvc.control.device.command.group.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.EncoderDecoderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupForwardDemandDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeIcon;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeType;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.basic.forward.ForwardReturnBO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/query")
public class CommandQueryController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupMemberDAO commandGroupMemberDao;
	
	@Autowired
	private CommandGroupForwardDemandDAO commandGroupForwardDemandDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private EncoderDecoderUserMapDAO encoderDecoderUserMapDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	/**
	 * 查询组织机构及用户<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月25日
	 * @param @PathVariable boolean filterMode 过滤器模式 0全部，1在线，2离线
	 * @return total int 总数据量
	 * @return rows List<ExternalUserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/user/filter/{filterMode}")
	public Object findInstitutionTreeUser(@PathVariable int filterMode, HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询有权限的用户
		List<UserBO> users = resourceService.queryUserresByUserId(userId);
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user : users){
			userIds.add(user.getId());
		}
		List<EncoderDecoderUserMap> userMaps = encoderDecoderUserMapDao.findByUserIdIn(userIds);
		if(users!=null && users.size()>0){
			for(UserBO user:users){
				if(user.getId().equals(userId)) continue;
				EncoderDecoderUserMap userMap = commonQueryUtil.queryUserMapById(userMaps, user.getId());
				if(("ldap".equals(user.getCreater()) && userMap!=null && userMap.getDecodeBundleId()!=null) ||
//				   (!"ldap".equals(user.getCreater()) && user.getEncoderId()!=null)){// && user.getDecoderId()!=null)){
					(!"ldap".equals(user.getCreater()) && userMap.getEncodeBundleId()!=null) && !userMap.getEncodeBundleId().equals("")){//过滤掉编码器uuid为null和空字符串，其它情况无法过滤
					if(filterMode == 0
							|| filterMode == 1 && user.isLogined()
							|| filterMode == 2 && !user.isLogined()){
						filteredUsers.add(user);
					}
				}
			}
		}
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, null, null, filteredUsers);
		}
		
		return _roots;
	}
	
	/**
	 * 查询当前指挥外的所有用户<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月14日
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<ExternalUserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/user/except/command")
	public Object findInstitutionTreeUserExceptCommand(String id, HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
				
		List<Long> memberUserIds = commandGroupMemberDao.findUserIdsByGroupId(Long.parseLong(id));
		
		//查询有权限的用户
		List<UserBO> users = resourceService.queryUserresByUserId(userId);
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user : users){
			userIds.add(user.getId());
		}
		List<EncoderDecoderUserMap> userMaps = encoderDecoderUserMapDao.findByUserIdIn(userIds);
		if(users!=null && users.size()>0){
			for(UserBO user:users){
				if(user.getId().equals(userId)) continue;
				EncoderDecoderUserMap userMap = commonQueryUtil.queryUserMapById(userMaps, user.getId());
				if(("ldap".equals(user.getCreater()) && userMap!=null && userMap.getDecodeBundleId()!=null) ||
				   (!"ldap".equals(user.getCreater()) && userMap.getEncodeBundleId()!=null) && !userMap.getEncodeBundleId().equals("")){// && user.getDecoderId()!=null)){
					if(!memberUserIds.contains(user.getId())){
						filteredUsers.add(user);
					}
				}
			}
		}
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, null, null, filteredUsers);
		}
		
		return _roots;
	}

	/**
	 * 查询组织机构下所有的设备<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月25日
	 * @param @PathVariable int type 通道类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
	 * @param @PathVariable boolean withChannel 是否要查询通道
	 * @param @PathVariable boolean filterMode 过滤器模式 0全部，1在线，2离线
//	 * @param @PathVariable int nodeType 节点类型类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
	 * @return List<TreeNodeVO> 设备通道树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/bundle/{type}/{withChannel}/{filterMode}")
	public Object findInstitutionTreeBundle(
			@PathVariable int type,
			@PathVariable boolean withChannel,
			@PathVariable int filterMode,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//查询有权限的设备
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundleBody:queryBundles){
			//过滤在线离线
			if(filterMode == 0
					|| filterMode == 1 && bundleBody.getOnlineStatus().equals(ONLINE_STATUS.ONLINE)
					|| filterMode == 2 && bundleBody.getOnlineStatus().equals(ONLINE_STATUS.OFFLINE)){
				
				if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
					BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
							.setName(bundleBody.getBundleName())
							.setFolderId(bundleBody.getFolderId())
							.setBundleId(bundleBody.getBundleId())
							.setModel(bundleBody.getDeviceModel())
							.setNodeUid(bundleBody.getAccessNodeUid())
							.setOnlineStatus(bundleBody.getOnlineStatus().toString())
							.setLockStatus(bundleBody.getLockStatus())
							.setType(bundleBody.getBundleType())
							.setRealType(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())?BundleBO.BundleRealType.XT.toString():BundleBO.BundleRealType.BVC.toString());
	
					bundles.add(bundle);
					
					bundleIds.add(bundleBody.getBundleId());
				}
			}
		}
		
		//根据bundleIds从资源层查询channels
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(bundleIds, type);
		if(queryChannels != null){
			for(ChannelSchemeDTO channel:queryChannels){
				ChannelBO channelBO = new ChannelBO().setChannelId(channel.getChannelId())
													 //起别名
												     .setName(ChannelType.transChannelName(channel.getChannelId()))
													 .setBundleId(channel.getBundleId())
													 .setChannelName(channel.getChannelName())
													 .setChannelType(channel.getBaseType());
	
				channels.add(channelBO);
			}
		}
		
		//过滤无通道设备
		Set<String> filteredBundleIds = new HashSet<String>();
		for(ChannelBO channel:channels){
			filteredBundleIds.add(channel.getBundleId());
		}
		List<BundleBO> filteredBundles = new ArrayList<BundleBO>();
		for(BundleBO bundle:bundles){
			if(filteredBundleIds.contains(bundle.getBundleId())){
				filteredBundles.add(bundle);
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
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
		
		return _roots;
		
	}
	
	/**
	 * 查找可用于关联某一播放器的解码器<br/>
	 * <p>查询结果排除了该用户已经关联到其它播放器的解码器</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午1:12:11
	 * @param serial
	 * @param withChannel
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/bundle/for/player")
	public Object findInstitutionTreeBundleForPlayer(
			int serial,
			boolean withChannel,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//查询有权限的设备
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundleBody:queryBundles){
			if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
				BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
						.setName(bundleBody.getBundleName())
						.setFolderId(bundleBody.getFolderId())
						.setBundleId(bundleBody.getBundleId())
						.setModel(bundleBody.getDeviceModel())
						.setNodeUid(bundleBody.getAccessNodeUid())
						.setOnlineStatus(bundleBody.getOnlineStatus().toString())
						.setLockStatus(bundleBody.getLockStatus())
						.setType(bundleBody.getBundleType())
						.setRealType(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())?BundleBO.BundleRealType.XT.toString():BundleBO.BundleRealType.BVC.toString());

				bundles.add(bundle);
				
				bundleIds.add(bundleBody.getBundleId());
			}		
		}
		
		//根据bundleIds从资源层查询channels
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(bundleIds, 1);
		if(queryChannels != null){
			for(ChannelSchemeDTO channel:queryChannels){
				ChannelBO channelBO = new ChannelBO().setChannelId(channel.getChannelId())
													 //起别名
												     .setName(ChannelType.transChannelName(channel.getChannelId()))
													 .setBundleId(channel.getBundleId())
													 .setChannelName(channel.getChannelName())
													 .setChannelType(channel.getBaseType());
	
				channels.add(channelBO);
			}
		}
		
		//过滤已经被绑定的设备
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		List<CommandGroupUserPlayerPO> players = userInfo.getPlayers();
		CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(players, serial);
		Set<String> castedBundleIds = new HashSet<String>();
		for(CommandGroupUserPlayerCastDevicePO device : player.getCastDevices()){
			castedBundleIds.add(device.getDstBundleId());
		}
		
		//过滤无通道设备
		Set<String> filteredBundleIds = new HashSet<String>();
		for(ChannelBO channel:channels){
			filteredBundleIds.add(channel.getBundleId());
		}
		List<BundleBO> filteredBundles = new ArrayList<BundleBO>();
		for(BundleBO bundle:bundles){
			if(filteredBundleIds.contains(bundle.getBundleId())
					&& !castedBundleIds.contains(bundle.getBundleId())){
				filteredBundles.add(bundle);
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
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
		
		return _roots;
		
	}
	
	/**
	 * 查询用户所在的指挥列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月15日 下午2:37:19
	 * @return TreeNodeVO 指挥树节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<CommandGroupPO> commands = commandGroupDao.findByMemberUserId(userId);
		
		//加入指挥组节点
		TreeNodeVO commandRoot = new TreeNodeVO().setId(String.valueOf(TreeNodeVO.FOLDERID_COMMAND))
											     .setName("指挥组列表")
											     .setType(TreeNodeType.FOLDER)
											     .setKey()
											     .setIcon(TreeNodeIcon.FOLDER.getName())
											     .setChildren(new ArrayList<TreeNodeVO>());
		
		for(CommandGroupPO command: commands){
			GroupType type = command.getType();
			if(!type.equals(GroupType.BASIC)){
				continue;
			}
			TreeNodeVO commandTree = new TreeNodeVO().set(command);
			commandRoot.getChildren().add(commandTree);
		}
		
		return (new ArrayListWrapper<TreeNodeVO>().add(commandRoot).getList());
		
	}
	
	/**
	 * 查询用户已经进入的指挥<br/>
	 * <p>用户CONNECT的指挥，以及用户作为主席的所有指挥</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月12日 上午10:21:58
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/entered")
	public Object findEnterdCommandGroup(HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		JSONArray groups = new JSONArray();
		
		List <CommandGroupPO> commands = commandGroupDao.findEnteredGroupByMemberUserId(userId);
		
		for(CommandGroupPO command : commands){
			GroupType type = command.getType();
			if(!type.equals(GroupType.BASIC)){
				continue;
			}
			JSONObject group = new JSONObject();
			group.put("id", command.getId().toString());
			group.put("name", command.getName());
			group.put("creator", command.getUserId().toString());
			if(type.equals(GroupType.BASIC)){// || type.equals(GroupType.COOPERATE) || type.equals(GroupType.SECRET)){
				group.put("type", "command");
			}else{
				group.put("type", "meeting");
			}
			group.put("status", command.getStatus().getCode());
			groups.add(group);
		}
		
		return groups;
	}
	
	/**
	 * 查询当前指挥内的所有转发点播<br/>
	 * <p>包括设备、文件</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午1:57:43
	 * @param id 指挥id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/command/forward")
	public Object findCommandForwardDemand(
			String id,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
//		CommandGroupPO group = commandGroupDao.findOne(Long.parseLong(id));
		Page<CommandGroupForwardDemandPO> demands = commandGroupForwardDemandDao.findByGroupId(Long.parseLong(id), page);
		if(demands == null){
			return null;
		}
		
		List<ForwardReturnBO> rows = new ArrayList<ForwardReturnBO>();
		for(CommandGroupForwardDemandPO demand : demands){
			if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_DEVICE)){
				ForwardReturnBO row = new ForwardReturnBO().setByDevice(demand);
				rows.add(row);
			}else if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_FILE)){
				ForwardReturnBO row = new ForwardReturnBO().setByFile(demand);
				rows.add(row);
			}
		}
		
		return JSON.toJSONString(new HashMapWrapper<String, Object>()
				.put("total", demands.getTotalElements())
				.put("rows", rows)
				.getMap());
	}
	
	/**递归组文件夹层级*/
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles, 
			List<ChannelBO> channels,
			List<UserBO> users){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles, channels, users);
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
		if(users!=null && users.size()>0){
			List<Long> userIds = new ArrayList<Long>();
			for(UserBO user : users){
				userIds.add(user.getId());
			}
			List<EncoderDecoderUserMap> userMaps = encoderDecoderUserMapDao.findByUserIdIn(userIds);
			for(UserBO user:users){
				if(user.getFolderId()!=null && root.getId().equals(user.getFolderId().toString())){
					EncoderDecoderUserMap userMap = commonQueryUtil.queryUserMapById(userMaps, user.getId());
					TreeNodeVO userNode = new TreeNodeVO().set(user, userMap);
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
