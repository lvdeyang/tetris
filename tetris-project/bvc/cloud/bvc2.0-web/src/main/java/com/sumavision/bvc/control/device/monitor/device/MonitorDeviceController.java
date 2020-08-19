package com.sumavision.bvc.control.device.monitor.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.base.bo.PlayerBundleBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.EncoderDecoderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.live.MonitorLiveCommons;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/device")
public class MonitorDeviceController {

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private CommonQueryUtil commonQueryUtil;

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private EncoderDecoderUserMapDAO encoderDecoderUserMapDao;
	
	@Autowired
	private MonitorLiveCommons monitorLiveCommons;
	
	/**
	 * 查询设备的接入层id<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月28日 下午4:28:30
	 * @param String bundleId 设备id
	 * @return String layerId 接入层id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/layer/id")
	public Object queryLayerId(
			String bundleId,
			HttpServletRequest request) throws Exception{
		
		BundlePO bundle = bundleDao.findByBundleId(bundleId);
		
		if(SOURCE_TYPE.EXTERNAL.equals(bundle.getSourceType())){
			String networkId = monitorLiveCommons.queryNetworkLayerId();
			return networkId;
		}else{
			return bundle.getAccessNodeUid();
		}
	}
	
	/**
	 * 查询可呼叫的用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午2:12:51
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<ExternalUserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/callable/users")
	public Object loadCallableUsers(HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询有权限的用户
		List<UserBO> users = resourceService.queryUserresByUserId(userId, TerminalType.PC_PLATFORM);
		Collections.sort(users, new UserBO.UserComparatorFromFolderIndex());
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user : users){
			userIds.add(user.getId());
		}
		List<EncoderDecoderUserMap> userMaps = encoderDecoderUserMapDao.findByUserIdIn(userIds);
		if(users!=null && users.size()>0){
			for(UserBO user:users){
				if(user.getId().equals(userId)) continue;
				String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
//				EncoderDecoderUserMap userMap = commonQueryUtil.queryUserMapById(userMaps, user.getId());
				if("ldap".equals(user.getCreater()) ||
				   (!"ldap".equals(user.getCreater()) && encoderId!=null)){
					filteredUsers.add(user);
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
	 * 轮询设备以及用户状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月10日 下午6:44:05
	 * @return List<TreeNodeVO> 节点列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/loop/device/and/user")
	public Object loopDeviceAndUser(HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<TreeNodeVO> nodes = new ArrayList<TreeNodeVO>();
		
		//查询有权限的设备
		List<BundlePO> bundleEntities = resourceQueryUtil.queryUseableBundles(userId);
		if(bundleEntities!=null && bundleEntities.size()>0){
			for(BundlePO bundleBody:bundleEntities){
				BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
												.setName(bundleBody.getBundleName())
												.setFolderId(bundleBody.getFolderId())
												.setBundleId(bundleBody.getBundleId())
												.setModel(bundleBody.getDeviceModel())
												.setNodeUid(bundleBody.getAccessNodeUid())
												.setOnlineStatus(bundleBody.getOnlineStatus().toString())
												.setLockStatus(bundleBody.getLockStatus())
												.setType(bundleBody.getBundleType());
				nodes.add(new TreeNodeVO().set(bundle));
			}
		}
		
		//查询有权限的用户
		List<UserBO> userEntities = resourceService.queryUserresByUserId(userId, TerminalType.PC_PLATFORM);
		Collections.sort(userEntities, new UserBO.UserComparatorFromFolderIndex());
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user : userEntities){
			userIds.add(user.getId());
		}
		List<EncoderDecoderUserMap> userMaps = encoderDecoderUserMapDao.findByUserIdIn(userIds);
		if(userEntities!=null && userEntities.size()>0){
			for(UserBO userEntity:userEntities){
				EncoderDecoderUserMap userMap = commonQueryUtil.queryUserMapById(userMaps, userEntity.getId());
				nodes.add(new TreeNodeVO().set(userEntity, userMap));
			}
		}
		
		return nodes;
	}
	
	/**
	 * 查询用户绑定的第17个播放器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月4日 下午4:21:40
	 * @return WebSipPlayerVO 播放器
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/user/binding/player")
	public Object findUserBindingPlayer(HttpServletRequest request) throws Exception{
		
		/*AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		//查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
		if(avTpls==null || avTpls.size()<=0){
			throw new AvtplNotFoundException("系统缺少点播系统参数模板！");
		}
		targetAvtpl = avTpls.get(0);
		//查询codec模板档位
		Set<AvtplGearsPO> gears = targetAvtpl.getGears();
		for(AvtplGearsPO gear:gears){
			targetGear = gear;
			break;
		}
		
		CodecParamVO codec = new CodecParamVO().set(targetAvtpl, targetGear);*/
		
		Long userId = userUtils.getUserIdFromSession(request);
		
//		UserBO user = userUtils.queryUserById(userId);
		
		PlayerBundleBO entity = resourceQueryUtil.querySpecifiedPlayerBundle(userId);
		
		//播放器不存在
		if(entity == null) return null;
		
		//只要能查到，就一定是播放器
		//if(!entity.getBundleId().equals(resourceQueryUtil.queryDecodeBundleIdByUserId(userId))) return null;
		
		String clientIp = request.getHeader("X-Real-IP");
		clientIp = (clientIp==null||"".equals(clientIp))?request.getRemoteAddr():clientIp;
		
		String port = getPlayerPort();
		
		WebSipPlayerVO player = new WebSipPlayerVO().set(entity)
													.setIp(clientIp)
													.setPort(port);
		
		/*return new HashMapWrapper<String, Object>().put("player", player)
												   .put("codec", codec);*/
		
		/*return new WebSipPlayerVO().setCode("ldy_1")
						           .setUsername("ldy_1")
						           .setPassword("123456")
						           .setBundleId("f646efe7b5e142ffbaeb24747197d770")
						           .setBundleName("ldy_1")
						           .setBundleType("VenusTerminal")
						           .setLayerId("suma-venus-access-JV210-68")
						           .setRegisterLayerIp("192.165.56.71")
						           .setRegisterLayerPort("5060")
						           .setIp("192.165.56.71")
						           .setVideoChannelId("VenusVideoOut_1")
						           .setVideoBaseType("VenusVideoOut")
						           .setAudioChannelId("VenusAudioOut_1")
						           .setAudioBaseType("VenusAudioOut");*/
		
		return player;
	}
	
	
	/**
	 * 查询用户的16个播放器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月27日 下午4:42:34
	 * @return List<WebSipPlayerVO> 播放器列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/web/sip/players")
	public Object findWebSipPlayers(HttpServletRequest request) throws Exception{
		
		/*AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		//查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
		if(avTpls==null || avTpls.size()<=0){
			throw new AvtplNotFoundException("系统缺少点播系统参数模板！");
		}
		targetAvtpl = avTpls.get(0);
		//查询codec模板档位
		Set<AvtplGearsPO> gears = targetAvtpl.getGears();
		for(AvtplGearsPO gear:gears){
			targetGear = gear;
			break;
		}
		
		CodecParamVO codec = new CodecParamVO().set(targetAvtpl, targetGear);*/
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<PlayerBundleBO> entities = resourceQueryUtil.queryPlayerBundlesByUserId(userId);
		
		String clientIp = request.getHeader("X-Real-IP");
		clientIp = (clientIp==null||"".equals(clientIp))?request.getRemoteAddr():clientIp;
		
		String port = getPlayerPort();
		
		List<WebSipPlayerVO> players = new ArrayList<WebSipPlayerVO>();
		for(PlayerBundleBO entity:entities){
			players.add(new WebSipPlayerVO().set(entity));
		}
		
		for(WebSipPlayerVO player:players){
			player.setIp(clientIp)
				  .setPort(port);
		}
		
		return players;
	}
	
	/**
	 * 查询设备下的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 下午4:38:18
	 * @param @PathVariable bundleId 设备id
	 * @param @PathVariable int type 通道类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
	 * @return encodeVideo List<ChannelVO> 视频编码通道
	 * @return encodeAudio List<ChannelVO> 音频编码通道
	 * @return decodeVideo List<ChannelVO> 视频解码通道
	 * @return decodeAudio List<ChannelVO> 音频解码通道
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/channels/{bundleId}/{type}")
	public Object findChannels(
			@PathVariable String bundleId,
			@PathVariable int type,
			HttpServletRequest request) throws Exception{
		
		List<ChannelVO> encodeVideo = new ArrayList<ChannelVO>();
		List<ChannelVO> encodeAudio = new ArrayList<ChannelVO>();
		List<ChannelVO> decodeVideo = new ArrayList<ChannelVO>();
		List<ChannelVO> decodeAudio = new ArrayList<ChannelVO>();
		
		//根据bundleIds从资源层查询channels
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(bundleId).getList(), type);
		
		if(queryChannels!=null && queryChannels.size()>0){
			for(ChannelSchemeDTO channel:queryChannels){
				if("VenusVideoIn".equals(channel.getBaseType())){
					encodeVideo.add(new ChannelVO().set(channel));
				}else if("VenusAudioIn".equals(channel.getBaseType())){
					encodeAudio.add(new ChannelVO().set(channel));
				}else if("VenusVideoOut".equals(channel.getBaseType())){
					decodeVideo.add(new ChannelVO().set(channel));
				}else if("VenusAudioOut".equals(channel.getBaseType())){
					decodeAudio.add(new ChannelVO().set(channel));
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("encodeVideo", encodeVideo)
												   .put("encodeAudio", encodeAudio)
												   .put("decodeVideo", decodeVideo)
												   .put("decodeAudio", decodeAudio)
												   .getMap();
	}
	
	/**
	 * 查询组织机构下所有的设备和点播用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:42:33
	 * @param @PathVariable int type 通道类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
	 * @param @PathVariable boolean withChannel 是否要查询通道
	 * @return List<TreeNodeVO> 设备通道树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/{type}/{withChannel}")
	public Object findInstitutionTree(
			@PathVariable int type,
			@PathVariable boolean withChannel,
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
		
		//只在编码中查询有权限的用户
		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		if(type==0 || type==2){
			List<UserBO> users = resourceService.queryUserresByUserId(userId, TerminalType.PC_PLATFORM);
			Collections.sort(users, new UserBO.UserComparatorFromFolderIndex());
			List<Long> userIds = new ArrayList<Long>();
			for(UserBO user : users){
				userIds.add(user.getId());
			}
//			List<EncoderDecoderUserMap> userMaps = encoderDecoderUserMapDao.findByUserIdIn(userIds);
			if(users!=null && users.size()>0){
				for(UserBO user:users){
//					EncoderDecoderUserMap userMap = commonQueryUtil.queryUserMapById(userMaps, user.getId());
					String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
					if("ldap".equals(user.getCreater()) || (!"ldap".equals(user.getCreater()) && encoderId!=null)){
						filteredUsers.add(user);
					}
				}
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			if(withChannel){
				recursionFolder(_root, folders, filteredBundles, channels, filteredUsers);
			}else{
				recursionFolder(_root, folders, filteredBundles, null, filteredUsers);
			}
		}
		
		return _roots;
		
	}
	
	/**
	 * 根据通道类型以及文件夹类型查询设备树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:42:33
	 * @param @PathVariable int type 通道类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
	 * @param @PathVariable boolean withChannel 是否要查询通道
	 * @param @pathVariable String folderType 文件夹类型
	 * @return List<TreeNodeVO> 设备通道树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/tree/by/folder/type/{type}/{withChannel}/{folderType}")
	public Object findTreeByFolderType(
			@PathVariable int type,
			@PathVariable boolean withChannel,
			@PathVariable String folderType,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//从资源层查bundles（需要userId）
		List<BundlePO> queryBundles = resourceService.queryBundlesByFolderTypeAndUserId(FolderType.valueOf(folderType.toUpperCase()), userId);
		
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		Set<Long> folderIds = new HashSet<Long>();
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
						.setType(bundleBody.getBundleType());

				bundles.add(bundle);
				
				bundleIds.add(bundleBody.getBundleId());
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
				folderIds.add(bundle.getFolderId());
			}
		}
		
		//根据folderIds查询父级信息
		List<FolderPO> foldersTree = resourceQueryUtil.queryFoldersTree(folderIds);
		if(foldersTree != null && foldersTree.size()>0){
			for(FolderPO allFolderPO: foldersTree){
				FolderBO folderBO = new FolderBO().set(allFolderPO);					  						  
				folders.add(folderBO);			
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
	 * 根据通道类型查询设备树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:42:33
	 * @param @PathVariable int type 通道类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
	 * @param @PathVariable boolean withChannel 是否要查询通道
	 * @return List<TreeNodeVO> 设备通道树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/tree/{type}/{withChannel}")
	public Object findTree(
			@PathVariable int type,
			@PathVariable boolean withChannel,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//从资源层查bundles（需要userId）
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		Set<Long> folderIds = new HashSet<Long>();
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
						.setType(bundleBody.getBundleType());

				bundles.add(bundle);
				
				bundleIds.add(bundleBody.getBundleId());
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
				folderIds.add(bundle.getFolderId());
			}
		}
		
		//根据folderIds查询父级信息
		List<FolderPO> foldersTree = resourceQueryUtil.queryFoldersTree(folderIds);
		if(foldersTree != null && foldersTree.size()>0){
			for(FolderPO allFolderPO: foldersTree){
				FolderBO folderBO = new FolderBO().set(allFolderPO);					  						  
				folders.add(folderBO);			
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
	
	/**
	 * 从配置文件中获取播放器端口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月6日 下午2:09:14
	 * @return String 端口
	 */
	private String getPlayerPort() throws Exception{
		Properties prop = new Properties();
		prop.load(MonitorDeviceController.class.getClassLoader().getResourceAsStream("properties/player.properties"));
		return prop.getProperty("port");
	}
	
}
