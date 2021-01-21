package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.bo.mq.ResourceBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.EncoderDecoderUserMapDAO;
import com.suma.venus.resource.dao.ExtraInfoDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.dao.RolePrivilegeMapDAO;
import com.suma.venus.resource.dao.ScreenRectTemplateDao;
import com.suma.venus.resource.dao.ScreenSchemeDao;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.suma.venus.resource.pojo.RolePrivilegeMap;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Base64;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Md5Encoder;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserVO;

@Service
public class BundleService extends CommonService<BundlePO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleService.class);

	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private ExtraInfoDao extraInfoDao;

	@Autowired
	private UserQueryFeign userFeign;

	@Autowired
	private ChannelTemplateService channelTemplateService;

	@Autowired
	private ChannelSchemeService channelSchemeService;

	@Autowired
	private ScreenSchemeDao screenSchemeDao;

	@Autowired
	private ScreenRectTemplateDao screenRectTemplateDao;
	
	@Autowired
	private UserQueryService userQueryService;
	
	@Autowired
	private ChannelSchemeDao channelSchemeDao;
	
	@Autowired
	private Base64 base64;
	
	@Autowired
	private Md5Encoder md5Encoder;
	
	@Autowired
	private WorkNodeService workNodeService;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private PrivilegeDAO privilegeDao;
	
	@Autowired
	private RolePrivilegeMapDAO rolePrivilegeMapDao;
	
	@Autowired
	private EncoderDecoderUserMapDAO encoderDecoderUserMapDao;
	
	/**
	 * 检验设备密码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月26日 上午11:12:02
	 * @param BundlePO bundle
	 * @param String password
	 * @return boolean 校验正确/失败
	 */
	public boolean checkBundleAndPassword(BundlePO bundle, String password) throws Exception{
		
		if(bundle.getOnlinePassword().equals(password)){
			return true;
		}
		
		if(bundle.getOnlinePassword().equals(base64.decode(password))){
			return true;
		}
		
		if(md5(bundle, password)){
			return true;
		}
		
		return false;
	}
	
	public boolean md5(BundlePO bundle, String password) throws Exception{
		
		JSONObject passwordJson = JSONObject.parseObject(password);
		
		String realm =  passwordJson.getString("realm");
		String uri = passwordJson.getString("uri");
		String nonce = passwordJson.getString("nonce");
		String response = passwordJson.getString("response").replace("\"", "");
		
		String hash1 = md5Encoder.encode(new StringBuffer().append(bundle.getUsername())
														   .append(":")
														   .append(realm)
														   .append(":")
														   .append(bundle.getOnlinePassword())
														   .toString());
		
		String hash2 = md5Encoder.encode(new StringBuffer().append("REGISTER")
														   .append(":")
														   .append(uri)
														   .toString());
		
		String responseCheck = md5Encoder.encode(new StringBuffer().append(hash1)
															  .append(":")
															  .append(nonce)
															  .append(":")
															  .append(hash2)
															  .toString());
		
		return response.equals(responseCheck);
	}

	/** 根据userId查询有权限的bundleId **/
	public List<String> queryBundleIdsByUserId(Long userId) {
		try {
			Set<String> bundleIds = bundleDao.queryAllBundleId();
			//ResourceIdListBO bo = userFeign.queryResourceByUserId(userId);
			ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
			Set<String> userBundleIds = new HashSet<String>();
			if (null != bo && null != bo.getResourceCodes()) {
				for (String resourceCode : bo.getResourceCodes()) {
					// 用户绑定的bundle资源的resouceCode有一种特别格式:bundleId-r(可写)/bundleId-w(可读)
					if (resourceCode.endsWith("-r") || resourceCode.endsWith("-w")) {
						userBundleIds.add(resourceCode.substring(0, resourceCode.length() - 2));
					} else {
						userBundleIds.add(resourceCode);
					}
				}
				bundleIds.retainAll(userBundleIds);
				return new ArrayList<String>(bundleIds);
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query bundleId by userId : " + userId, e);
		}

		return new ArrayList<String>();
	}

	/** 根据userId查询有权限的bundle **/
	public List<BundlePO> queryByUserId(Long userId) {
		List<String> bundleIds = queryBundleIdsByUserId(userId);
		if (!bundleIds.isEmpty()) {
			return bundleDao.findInBundleIds(bundleIds);
		}

		return new ArrayList<BundlePO>();
	}

	/** 根据userId查询播放器资源的bundleId **/
	@Deprecated
	public List<String> queryPlayerBundleIdsByUserId(Long userId) {
		try {
			List<String> playerBundleIds = bundleDao.findAllPlayerBundleIds();
			//ResourceIdListBO bo = userFeign.queryResourceByUserId(userId);
			ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
			if (null != bo && null != bo.getResourceCodes()) {
				playerBundleIds.retainAll(bo.getResourceCodes());
				return playerBundleIds;
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query player bundleId by userId : " + userId, e);
		}

		return new ArrayList<String>();
	}

	/** 根据userId查询播放器资源bundle **/
	@Deprecated
	public List<BundlePO> queryPlayerBundlesByUserId(Long userId) {
		List<String> bundleIds = queryPlayerBundleIdsByUserId(userId);
		if (!bundleIds.isEmpty()) {
			return bundleDao.findInBundleIds(bundleIds);
		}

		return new ArrayList<BundlePO>();
	}

	/** 根据userId和folderId查询文件夹下具有权限的bundleId **/
	public List<String> queryBundleIdsByUserIdAndFolderId(Long userId, Long folderId) {
		try {
			List<String> bundleIds = queryBundleIdListByMultiParams(null, null, null, folderId,null);
			//ResourceIdListBO bo = userFeign.queryResourceByUserId(userId);
			ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
			Set<String> userBundleIds = new HashSet<String>();
			if (null != bo && null != bo.getResourceCodes()) {
				for (String resourceCode : bo.getResourceCodes()) {
					// 用户绑定的bundle资源的resouceCode有一种特别格式:bundleId-r(可写)/bundleId-w(可读)
					if (resourceCode.endsWith("-r") || resourceCode.endsWith("-w")) {
						userBundleIds.add(resourceCode.substring(0, resourceCode.length() - 2));
					} else {
						userBundleIds.add(resourceCode);
					}
				}
				bundleIds.retainAll(userBundleIds);
				return bundleIds;
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query bundleId by userId : " + userId + " and folderId : " + folderId, e);
		}
		return new ArrayList<String>();
	}

	/** 根据userId和folderId查询文件夹下具有权限的bundlepo **/
	public List<BundlePO> queryByUserIdAndFolderId(Long userId, Long folderId) {
		List<String> bundleIds = queryBundleIdsByUserIdAndFolderId(userId, folderId);
		if (!bundleIds.isEmpty()) {
			return bundleDao.findInBundleIds(bundleIds);
		}

		return new ArrayList<BundlePO>();
	}

	@Deprecated
	public List<BundlePO> findByDeviceModelAndKeywordAndNoFolder(String deviceModel, String keyword) {

		return queryBundlesByMultiParams(deviceModel, null, keyword, null, null, true);
	}

	@Deprecated
	public List<BundlePO> queryByUserIdAndDevcieModelAndKeyword(Long userId, String deviceModel, String keyword) {
		return queryByUserIdAndDevcieModelAndKeyword(userId, deviceModel, null, keyword);
	}

	public List<BundlePO> queryByUserIdAndDevcieModelAndKeyword(Long userId, String deviceModel, String sourceType, String keyword) {
		// Set<String> bundleIds =
		// queryBundleIdByDeviceModelAndSourceTypeAndKeyword(deviceModel, sourceType,
		// keyword);
		// List<BundlePO> bundleList = bundleDao.findByBundleIdIn(bundleIds);

		List<BundlePO> bundleList = bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, null, null, false));

		if (null != userId && userId > 0 && !bundleList.isEmpty()) {
			List<BundlePO> userBundles = queryByUserId(userId);
			bundleList.retainAll(userBundles);
		}

		return bundleList;
	}

	@Deprecated
	public Set<String> queryBundleIdByDeviceModelAndSourceTypeAndKeyword(String deviceModel, String sourceType, String keyword) {
//		SOURCE_TYPE sourceTypeEnum = SOURCE_TYPE.fromName(sourceType);
//		if (null == sourceTypeEnum) {// 来源类型为空
//			if (StringUtils.isEmpty(deviceModel) && StringUtils.isEmpty(keyword)) {
//				return bundleDao.queryAllBundleId();
//			} else if (!StringUtils.isEmpty(deviceModel) && StringUtils.isEmpty(keyword)) {
//				return bundleDao.queryBundleIdByDeviceModel(deviceModel);
//			} else if (StringUtils.isEmpty(deviceModel) && !StringUtils.isEmpty(keyword)) {
//				return bundleDao.queryBundleIdByBundleNameOrUsernameLike(keyword);
//			} else {
//				return bundleDao.queryBundleIdByDeviceModelAndNameLike(deviceModel, keyword);
//			}
//		} else {
//			if (StringUtils.isEmpty(deviceModel) && StringUtils.isEmpty(keyword)) {
//				return bundleDao.queryBundleIdsBySourceType(sourceTypeEnum);
//			} else if (!StringUtils.isEmpty(deviceModel) && StringUtils.isEmpty(keyword)) {
//				return bundleDao.queryBundleIdByDeviceModelAndSourceType(deviceModel, sourceTypeEnum);
//			} else if (StringUtils.isEmpty(deviceModel) && !StringUtils.isEmpty(keyword)) {
//				return bundleDao.queryBundleIdByNameLikeAndSourceType(keyword, sourceTypeEnum);
//			} else {
//				return bundleDao.queryBundleIdByDeviceModelAndNameLikeAndSourceType(deviceModel, keyword, sourceTypeEnum);
//			}
//		}

		return queryBundleIdSetByMultiParams(deviceModel, sourceType, keyword, null , null);

	}

	@Deprecated
	public Set<String> queryBundleIdByDeviceModelAndKeyword(String deviceModel, String keyword) {
//		if (StringUtils.isEmpty(deviceModel) && StringUtils.isEmpty(keyword)) {
//			return bundleDao.queryAllBundleId();
//		} else if (!StringUtils.isEmpty(deviceModel) && StringUtils.isEmpty(keyword)) {
//			return bundleDao.queryBundleIdByDeviceModel(deviceModel);
//		} else if (StringUtils.isEmpty(deviceModel) && !StringUtils.isEmpty(keyword)) {
//			return bundleDao.queryBundleIdByBundleNameOrUsernameLike(keyword);
//		} else {
//			return bundleDao.queryBundleIdByDeviceModelAndNameLike(deviceModel, keyword);
//		}
//		
		return queryBundleIdSetByMultiParams(deviceModel, null, keyword, null, null);

	}

	/** 根据模板对bundle生成一套默认配置 */
	@Transactional(rollbackFor = Exception.class)
	public void configDefaultAbility(BundlePO bundle) throws Exception {
		List<ChannelTemplatePO> channelTemplates = channelTemplateService.findByDeviceModel(bundle.getDeviceModel());
		if ("VenusTerminal".equals(bundle.getBundleType()) || "VenusProxy".equals(bundle.getBundleType())) {
			// channels
			for (ChannelTemplatePO channelTemplate : channelTemplates) {
				String[] defaultIds = channelTemplate.getDefaultChannelIds().split(",");
				for (String defaultId : defaultIds) {
					ChannelSchemePO channelScheme = new ChannelSchemePO();
					channelScheme.setBundleId(bundle.getBundleId());
					channelScheme.setChannelId(defaultId);
					channelScheme.setChannelName(channelTemplate.getChannelName());
					channelScheme.setChannelTemplateID(channelTemplate.getId());
					channelSchemeService.save(channelScheme);
				}
			}

			// screens
			Set<String> screenIds = screenRectTemplateDao.findScreenIdsByDeviceModel(bundle.getDeviceModel());
			for (String screenId : screenIds) {
				ScreenSchemePO screenSchemePO = new ScreenSchemePO();
				screenSchemePO.setBundleId(bundle.getBundleId());
				screenSchemePO.setScreenId(screenId);
				screenSchemePO.setDeviceModel(bundle.getDeviceModel());
				screenSchemeDao.save(screenSchemePO);
			}
		} else {
			for (ChannelTemplatePO channelTemplate : channelTemplates) {
				createTerminalChannelSchemes(bundle.getBundleId(), channelTemplate);
			}
		}
	}

	private void createTerminalChannelSchemes(String bundleId, ChannelTemplatePO channelTemplate) {
		for (int i = 1; i <= channelTemplate.getMaxChannelCnt(); i++) {
			ChannelSchemePO channelScheme = new ChannelSchemePO();
			channelScheme.setBundleId(bundleId);
			channelScheme.setChannelId(channelTemplate.getBaseType() + "_" + i);
			channelScheme.setChannelName(channelTemplate.getChannelName());
			channelScheme.setChannelTemplateID(channelTemplate.getId());
			channelSchemeService.save(channelScheme);
		}
	}

	public BundlePO findByBundleId(String bundleId) {
		return bundleDao.findByBundleId(bundleId);
	}

	public List<BundlePO> findByDeviceModel(String deviceModel) {
		return bundleDao.findByDeviceModel(deviceModel);
	}

	public Set<String> queryAllBundleId() {
		return bundleDao.queryAllBundleId();
	}

	public List<BundlePO> findByFolderId(Long folderId) {
		return bundleDao.findByFolderId(folderId);
	}

	public List<BundlePO> findByAccessNodeUid(String accessNodeUid) {
		return bundleDao.findByAccessNodeUid(accessNodeUid);
	}

	public BundlePO findByUsername(String username) {
		return bundleDao.findByUsername(username);
	}

	public Set<String> queryBundleIdByDeviceModel(String deviceModel) {
		return queryBundleIdSetByMultiParams(deviceModel, null, null, null, null);
	}

	public Set<String> queryBundleIdByAccessNodeUid(String accessNodeUid) {
		return bundleDao.queryBundleIdByAccessNodeUid(accessNodeUid);
	}

	public List<BundlePO> queryBundlesByMultiParams(String deviceModel, String sourceType, String keyword, Long folderId, String coderType, boolean withoutFolder) {
		return bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, folderId, coderType, withoutFolder));
	}

	public Set<String> queryBundleIdSetByMultiParams(String deviceModel, String sourceType, String keyword, Long folderId, String coderType) {
		List<BundlePO> bundlePOList = bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, folderId, coderType, false));
		return bundlePOList.stream().map(b -> b.getBundleId()).collect(Collectors.toSet());
	}

	public List<String> queryBundleIdListByMultiParams(String deviceModel, String sourceType, String keyword, Long folderId, String coderType) {
		List<BundlePO> bundlePOList = bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, folderId, coderType, false));
		return bundlePOList.stream().map(b -> b.getBundleId()).collect(Collectors.toList());
	}
	
	/**
	 * 创建用户--创建17个播放器和一个机顶盒设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月7日 下午2:32:13
	 * @param String userId 用户id
	 * @param String username 用户名
	 * @param String userNo 用户号码
	 */
    @Transactional(rollbackFor = Exception.class)
	public void createUserBundle(String userId, String username, String userNo, String worknodeUid) throws Exception{
		
		try {
			List<BundlePO> bundlePOs = new ArrayList<BundlePO>();
			List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
			List<String> bundleIds = new ArrayList<String>();
			
			
			WorkNodePO choseWorkNode = new WorkNodePO();
			if(worknodeUid != null && !worknodeUid.equals("")){
				choseWorkNode = workNodeService.findByNodeUid(worknodeUid);
			}else{
				//自动选择player接入层
				List<WorkNodePO> tvosLayers = workNodeService.findByType(NodeType.ACCESS_JV210);
				choseWorkNode = workNodeService.choseWorkNode(tvosLayers);
			}
						
			// 创建17个播放器资源
			for (int i = 1; i <= 17; i++) {
				BundlePO bundlePO = new BundlePO();
				bundlePO.setBundleName(username + "_" + i);
				bundlePO.setUsername(userNo + "_" + i);
				bundlePO.setOnlinePassword(userNo);
				bundlePO.setBundleId(BundlePO.createBundleId());
				bundlePO.setDeviceModel("player");
				bundlePO.setBundleType("VenusTerminal");
				bundlePO.setBundleAlias("播放器");
				bundlePO.setBundleNum(userNo + "_" + i);
				bundlePO.setUserId(Long.valueOf(userId));
				
				if(choseWorkNode != null){
					bundlePO.setAccessNodeUid(choseWorkNode.getNodeUid());
				}
				
				// 默认上线
				bundlePO.setOnlineStatus(ONLINE_STATUS.OFFLINE);

				bundlePOs.add(bundlePO);
				bundleIds.add(bundlePO.getBundleId());

				configDefaultAbility(bundlePO);
				// 配置两路解码通道(音频解码和视频解码各一路)
				//channelSchemePOs.addAll(channelSchemeService.createAudioAndVideoDecodeChannel(bundlePO.getBundleId()));
			}
			
			//创建本地编码器encoder
			BundlePO encoder = new BundlePO();
			encoder.setBundleName(username + "_encoder");
			encoder.setUsername(userNo + "_encoder");
			encoder.setOnlinePassword(userNo + "_encoder");
			encoder.setBundleId(BundlePO.createBundleId());
			encoder.setDeviceModel("encoder");
			encoder.setBundleType("VenusTerminal");
			encoder.setBundleNum(userNo + "_encoder");
			encoder.setUserId(Long.valueOf(userId));
			
			if(choseWorkNode != null){
				encoder.setAccessNodeUid(choseWorkNode.getNodeUid());
			}
			
			// 默认上线
			encoder.setOnlineStatus(ONLINE_STATUS.OFFLINE);
			
			bundlePOs.add(encoder);
			bundleIds.add(encoder.getBundleId());
			configDefaultAbility(encoder);
			
			//创建机顶盒设备 
			BundlePO tvos = new BundlePO();
			tvos.setBundleName(username + "_机顶盒");
			tvos.setUsername(userNo + "_tvos");
			//tvos.setOnlinePassword(password);
			tvos.setBundleId(BundlePO.createBundleId());
			tvos.setDeviceModel("tvos");
			tvos.setBundleType("VenusTerminal");
			tvos.setBundleNum(userNo + "_tvos");
			tvos.setUserId(Long.valueOf(userId));
			tvos.setOnlineStatus(ONLINE_STATUS.OFFLINE);

			bundlePOs.add(tvos);
			bundleIds.add(tvos.getBundleId());
			configDefaultAbility(tvos);
			
			//创建pc终端
			BundlePO pc = new BundlePO();
			pc.setBundleName(username + "_pc");
			pc.setUsername(userNo + "_pc");
			//pc.setOnlinePassword(password);
			pc.setBundleId(BundlePO.createBundleId());
			pc.setDeviceModel("pc");
			pc.setBundleType("VenusTerminal");
			pc.setBundleNum(userNo + "_pc");
			pc.setUserId(Long.valueOf(userId));
			// 默认上线
			pc.setOnlineStatus(ONLINE_STATUS.OFFLINE);

			bundlePOs.add(pc);
			bundleIds.add(pc.getBundleId());
			configDefaultAbility(pc);
			
			//创建zk终端
			BundlePO zk = new BundlePO();
			zk.setBundleName(username + "_zk");
			zk.setUsername(userNo + "_zk");
			//pc.setOnlinePassword(password);
			zk.setBundleId(BundlePO.createBundleId());
			zk.setDeviceModel("zk");
			zk.setBundleType("VenusTerminal");
			zk.setBundleNum(userNo + "_zk");
			zk.setUserId(Long.valueOf(userId));
			// 默认上线
			zk.setOnlineStatus(ONLINE_STATUS.OFFLINE);

			bundlePOs.add(zk);
			bundleIds.add(zk.getBundleId());
			configDefaultAbility(zk);
			
			//创建jv220终端
			BundlePO jv220 = new BundlePO();
			jv220.setBundleName(username + "_jv220");
			jv220.setUsername(userNo + "_jv220");
			//jv220.setOnlinePassword(password);
			jv220.setBundleId(BundlePO.createBundleId());
			jv220.setDeviceModel("jv220");
			jv220.setBundleType("VenusTerminal");
			jv220.setBundleNum(userNo + "_jv220");
			jv220.setUserId(Long.valueOf(userId));
			// 默认上线
			jv220.setOnlineStatus(ONLINE_STATUS.OFFLINE);

			bundlePOs.add(jv220);
			bundleIds.add(jv220.getBundleId());
			configDefaultAbility(jv220);

			// 保存数据库
			bundleDao.save(bundlePOs);
			channelSchemeDao.save(channelSchemePOs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * 创建游客设备--播放器和pc<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 上午9:11:37
	 * @param String userId 用户id
	 * @param String username 用户名
	 * @param String userNo 用户号码
	 */
	public void createTouristBundle(String userId, String username, String userNo) throws Exception{
		
		try {
			List<BundlePO> bundlePOs = new ArrayList<BundlePO>();
			List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
			List<String> bundleIds = new ArrayList<String>();
			
			//自动选择player接入层
			List<WorkNodePO> tvosLayers = workNodeService.findByType(NodeType.ACCESS_JV210);
			WorkNodePO choseWorkNode = workNodeService.choseWorkNode(tvosLayers);
				
			// 创建17个播放器资源
			for (int i = 1; i <= 17; i++) {
				BundlePO bundlePO = new BundlePO();
				bundlePO.setBundleName(username + "_" + i);
				bundlePO.setUsername(userNo + "_" + i);
				bundlePO.setOnlinePassword(userNo);
				bundlePO.setBundleId(BundlePO.createBundleId());
				bundlePO.setDeviceModel("player");
				bundlePO.setBundleType("VenusTerminal");
				bundlePO.setBundleAlias("播放器");
				bundlePO.setBundleNum(userNo + "_" + i);
				bundlePO.setUserId(Long.valueOf(userId));
				
				if(choseWorkNode != null){
					bundlePO.setAccessNodeUid(choseWorkNode.getNodeUid());
				}
				
				// 默认上线
				bundlePO.setOnlineStatus(ONLINE_STATUS.OFFLINE);

				bundlePOs.add(bundlePO);
				bundleIds.add(bundlePO.getBundleId());

				configDefaultAbility(bundlePO);
				// 配置两路解码通道(音频解码和视频解码各一路)
				//channelSchemePOs.addAll(channelSchemeService.createAudioAndVideoDecodeChannel(bundlePO.getBundleId()));
			}
			
			//创建pc终端
			BundlePO pc = new BundlePO();
			pc.setBundleName(username + "_pc");
			pc.setUsername(userNo + "_pc");
			//pc.setOnlinePassword(password);
			pc.setBundleId(BundlePO.createBundleId());
			pc.setDeviceModel("pc");
			pc.setBundleType("VenusTerminal");
			pc.setBundleNum(userNo + "_pc");
			pc.setUserId(Long.valueOf(userId));
			// 默认上线
			pc.setOnlineStatus(ONLINE_STATUS.OFFLINE);

			bundlePOs.add(pc);
			bundleIds.add(pc.getBundleId());
			configDefaultAbility(pc);
			
			//创建jv220终端
			BundlePO jv220 = new BundlePO();
			jv220.setBundleName(username + "_jv220");
			jv220.setUsername(userNo + "_jv220");
			//jv220.setOnlinePassword(password);
			jv220.setBundleId(BundlePO.createBundleId());
			jv220.setDeviceModel("jv220");
			jv220.setBundleType("VenusTerminal");
			jv220.setBundleNum(userNo + "_jv220");
			jv220.setUserId(Long.valueOf(userId));
			// 默认上线
			jv220.setOnlineStatus(ONLINE_STATUS.OFFLINE);

			bundlePOs.add(jv220);
			bundleIds.add(jv220.getBundleId());
			configDefaultAbility(jv220);

			// 保存数据库
			bundleDao.save(bundlePOs);
			channelSchemeDao.save(channelSchemePOs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 同步用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 上午10:14:33
	 */
	public void syncUser() throws Exception{
		
		List<UserBO> users = userQueryService.queryAllUserBaseInfo(null);
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user: users){
			userIds.add(user.getId());
		}
		
		//删除设备
		List<BundlePO> needRemoveBundles = bundleDao.findByUserIdNotIn(userIds);
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundle: needRemoveBundles){
			bundleIds.add(bundle.getBundleId());
		}
		
		List<ChannelSchemePO> needRemoveChannels = channelSchemeDao.findByBundleIdIn(bundleIds);
		List<ScreenSchemePO> needRemoveScreens = screenSchemeDao.findByBundleIdIn(bundleIds);
		
		//删除用户绑定分组
		List<FolderUserMap> maps = folderUserMapDao.findByUserIdNotIn(userIds);
		
		//删除用户权限
		List<String> indentities = new ArrayList<String>();
		indentities.add("-1");
		for(UserBO user: users){
			if(user.getUserNo() != null){
				indentities.add(user.getUserNo());
			}
		}
		
		//删除设备权限
		List<String> bundleIndentities = new ArrayList<String>();
		bundleIndentities.add("-1");
		for(BundlePO bundle: needRemoveBundles){
			if(bundle.getBundleId() != null){
				bundleIndentities.add(bundle.getBundleId());
			}
		}
		
		List<PrivilegePO> privileges = privilegeDao.findByNotIndentify(indentities);
		privileges.addAll(privilegeDao.findByIndentify(bundleIndentities));
		
		//删除角色权限绑定关系
		List<Long> privilegeIds = new ArrayList<Long>();
		for(PrivilegePO privilege: privileges){
			privilegeIds.add(privilege.getId());
		}
		
		List<RolePrivilegeMap> roleMaps = rolePrivilegeMapDao.findByPrivilegeIdIn(privilegeIds);
		
		//删除用户绑定编解码器关系
		List<EncoderDecoderUserMap> bundleMaps = encoderDecoderUserMapDao.findByUserIdNotIn(userIds);
		
		bundleDao.delete(needRemoveBundles);
		channelSchemeDao.delete(needRemoveChannels);
		screenSchemeDao.delete(needRemoveScreens);
		
		folderUserMapDao.delete(maps);
		privilegeDao.delete(privileges);
		rolePrivilegeMapDao.delete(roleMaps);
		encoderDecoderUserMapDao.delete(bundleMaps);
		
	}
	
	/**
	 * 删除用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月10日 下午1:12:08
	 * @param List<UserVO> users 用户信息
	 */
	public void removeUser(List<UserVO> users) throws Exception{
		
		List<Long> needRemoveUserIds = new ArrayList<Long>();
		List<String> needRemoveUserNos = new ArrayList<String>();
		for(UserVO user: users){
			needRemoveUserIds.add(user.getId());
			if(user.getUserno() != null){
				needRemoveUserNos.add(user.getUserno());
			}
		}
		
		//删除设备
		List<BundlePO> needRemoveBundles = bundleDao.findByUserIdIn(needRemoveUserIds);
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundle: needRemoveBundles){
			bundleIds.add(bundle.getBundleId());
		}
		
		List<ChannelSchemePO> needRemoveChannels = channelSchemeDao.findByBundleIdIn(bundleIds);
		List<ScreenSchemePO> needRemoveScreens = screenSchemeDao.findByBundleIdIn(bundleIds);
		
		//删除用户绑定分组
		List<FolderUserMap> maps = folderUserMapDao.findByUserIdIn(needRemoveUserIds);
		
		//删除用户权限
		List<String> indentities = new ArrayList<String>();
		indentities.add("-1");
		indentities.addAll(needRemoveUserNos);
		
		//删除设备权限
		for(BundlePO bundle: needRemoveBundles){
			if(bundle.getBundleId() != null){
				indentities.add(bundle.getBundleId());
			}
		}
		
		List<PrivilegePO> privileges = privilegeDao.findByIndentify(indentities);
		
		//删除角色权限绑定关系
		List<Long> privilegeIds = new ArrayList<Long>();
		for(PrivilegePO privilege: privileges){
			privilegeIds.add(privilege.getId());
		}
		
		List<RolePrivilegeMap> roleMaps = rolePrivilegeMapDao.findByPrivilegeIdIn(privilegeIds);
		
		//删除用户绑定编解码器
		List<EncoderDecoderUserMap> bundleMaps = encoderDecoderUserMapDao.findByUserIdIn(needRemoveUserIds);
		
		bundleDao.delete(needRemoveBundles);
		channelSchemeDao.delete(needRemoveChannels);
		screenSchemeDao.delete(needRemoveScreens);
		
		folderUserMapDao.delete(maps);
		privilegeDao.delete(privileges);
		rolePrivilegeMapDao.delete(roleMaps);
		encoderDecoderUserMapDao.delete(bundleMaps);
		
	}
	
	public Set<String> queryBundleSetByMultiParams(String deviceModel, String sourceType, String keyword, Long folderId, String coderType) {
		
		List<BundlePO> bundlePOList = new ArrayList<BundlePO>();
		
		bundlePOList.addAll(bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, folderId, coderType, false)));
		
		List<FolderPO> parentFolders = folderDao.findByParentId(folderId);
		List<FolderPO> folders = folderDao.findByParentPathLike(new StringBufferWrapper().append("%")
																					 .append("/")
																					 .append(folderId)
																					 .append("/")
																					 .append("%")
																					 .toString());
		for(FolderPO folderPO: parentFolders){
			bundlePOList.addAll(bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, folderPO.getId(), coderType, false)));
		}
		for(FolderPO folderPO: folders){
			bundlePOList.addAll(bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, folderPO.getId(), coderType, false)));
		}
		
		return bundlePOList.stream().map(b -> b.getBundleId()).collect(Collectors.toSet());
	}
	
	/**
	 * 查询用户下对应资源类型的资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午5:00:27
	 * @param List<Long> userIds userIds 用户列表
	 * @param String type 资源类型
	 * @return List<ResourceBO> 资源信息
	 */
	public List<ResourceBO> queryResource(List<Long> userIds, String type) throws Exception{
		
		List<BundlePO> bundles = bundleDao.findByDeviceModelAndUserIdIn(type, userIds);
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundle: bundles){
			bundleIds.add(bundle.getBundleId());
		}
		List<ChannelSchemePO> channels = channelSchemeDao.findByBundleIdIn(bundleIds);
		
		List<ResourceBO> resources = new ArrayList<ResourceBO>();
		for(BundlePO bundle: bundles){
			ResourceBO resourceBO = new ResourceBO();
			resourceBO.setUserId(bundle.getUserId().toString());
			resourceBO.setBundleId(bundle.getBundleId());
			resourceBO.setLayerId(bundle.getAccessNodeUid());
			resourceBO.setType(bundle.getDeviceModel());
			for(ChannelSchemePO channel: channels){
				if(channel.getBundleId().equals(bundle.getBundleId()) && channel.getChannelId().equals("VenusVideoIn_1")){
					resourceBO.setVideoChannelId(channel.getChannelId());
				}
				if(channel.getBundleId().equals(bundle.getBundleId()) && channel.getChannelId().equals("VenusAudioIn_1")){
					resourceBO.setAudioChannelId(channel.getChannelId());
				}
				if(channel.getBundleId().equals(bundle.getBundleId()) && channel.getChannelId().equals("VenusVideoIn_2")){
					resourceBO.setScreenVideoChannelId(channel.getChannelId());
				}
				if(channel.getBundleId().equals(bundle.getBundleId()) && channel.getChannelId().equals("VenusAudioIn_2")){
					resourceBO.setScreenAudioChannelId(channel.getChannelId());
				}
			}
			resources.add(resourceBO);
		}
		
		return resources;
	}
	
	/**
	 * 删除用户创建的设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午3:20:33
	 * @param Long userId 用户id
	 */
	public void deleteByUserId(Long userId) throws Exception{
		bundleDao.deleteByUserId(userId);
	}
	
	/**
	 * 批量删除用户创建的设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午3:20:33
	 * @param List<Long> userIds 用户id数组
	 */
	public void deleteByUserIdIn(Collection<Long> userIds) throws Exception{
		bundleDao.deleteByUserIdIn(userIds);
	}
	
	//留着测试sip认证
	public static void main(String[] args) throws Exception{
		
		String password = "{\n    \"uri\": \"sip:192.165.56.18:5060\",\n    \"realm\": \"192.165.56.18\",\n    \"nonce\": \"NTQ0MDU3ODoxOTIuMTY1LjU2LjE4OnN1bWF2aXNpb25yZA==\",\n    \"response\": \"\\\"64d0419cf3ee6806b79112fae26406d6\\\"\"\n}";
		
		JSONObject passwordJson = JSONObject.parseObject(password);
		
		String realm = "127.0.0.1";
		String uri = "sip:10.1.1.244:5060";
		String nonce = "MTUzNDM1OjEyNy4wLjAuMTpzdW1hdmlzaW9ucmQ=";
		String userName = "pangzhiyuan_sip_encoder";
		String online = "pangzhiyuan_sip_encoder";
		
		String hash1 = encode(new StringBuffer().append(userName)
											   .append(":")
											   .append(realm)
											   .append(":")
											   .append(online)
											   .toString());
		
		String hash2 = encode(new StringBuffer().append("REGISTER")
											   .append(":")
											   .append(uri)
											   .toString());
		
		String responseCheck = encode(new StringBuffer().append(hash1)
														  .append(":")
														  .append(nonce)
														  .append(":")
														  .append(hash2)
														  .toString());
		
		System.out.println(responseCheck);
		
	}
	
	public static String encode(String message) throws Exception{
		return DigestUtils.md5DigestAsHex(message.getBytes());
	}
}
