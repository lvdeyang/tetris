package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.base.bo.ResultBO;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.ExtraInfoDao;
import com.suma.venus.resource.dao.ScreenRectTemplateDao;
import com.suma.venus.resource.dao.ScreenSchemeDao;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Base64;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Md5Encoder;

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
			List<String> bundleIds = queryBundleIdListByMultiParams(null, null, null, folderId);
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

		return queryBundlesByMultiParams(deviceModel, null, keyword, null, true);
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

		List<BundlePO> bundleList = bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, null, false));

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

		return queryBundleIdSetByMultiParams(deviceModel, sourceType, keyword, null);

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
		return queryBundleIdSetByMultiParams(deviceModel, null, keyword, null);

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
		return queryBundleIdSetByMultiParams(deviceModel, null, null, null);
	}

	public Set<String> queryBundleIdByAccessNodeUid(String accessNodeUid) {
		return bundleDao.queryBundleIdByAccessNodeUid(accessNodeUid);
	}

	public List<BundlePO> queryBundlesByMultiParams(String deviceModel, String sourceType, String keyword, Long folderId, boolean withoutFolder) {
		return bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, folderId, withoutFolder));
	}

	public Set<String> queryBundleIdSetByMultiParams(String deviceModel, String sourceType, String keyword, Long folderId) {
		List<BundlePO> bundlePOList = bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, folderId, false));
		return bundlePOList.stream().map(b -> b.getBundleId()).collect(Collectors.toSet());
	}

	public List<String> queryBundleIdListByMultiParams(String deviceModel, String sourceType, String keyword, Long folderId) {
		List<BundlePO> bundlePOList = bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, folderId, false));
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
	public void createUserBundle(String userId, String username, String userNo) throws Exception{
		try {
			List<BundlePO> bundlePOs = new ArrayList<BundlePO>();
			List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
			List<String> bundleIds = new ArrayList<String>();
			// 创建17个播放器资源
			for (int i = 1; i <= 17; i++) {
				BundlePO bundlePO = new BundlePO();
				bundlePO.setBundleName(username + "_" + i);
				bundlePO.setUsername(userNo + "_" + i);
				//bundlePO.setOnlinePassword(password);
				bundlePO.setBundleId(BundlePO.createBundleId());
				bundlePO.setDeviceModel("player");
				bundlePO.setBundleType("VenusTerminal");
				bundlePO.setBundleAlias("播放器");
				bundlePO.setBundleNum(userNo + "_" + i);
				bundlePO.setUserId(Long.valueOf(userId));
				// 默认上线
				bundlePO.setOnlineStatus(ONLINE_STATUS.ONLINE);

				bundlePOs.add(bundlePO);
				bundleIds.add(bundlePO.getBundleId());

				configDefaultAbility(bundlePO);
				// 配置两路解码通道(音频解码和视频解码各一路)
				//channelSchemePOs.addAll(channelSchemeService.createAudioAndVideoDecodeChannel(bundlePO.getBundleId()));
			}
			
			//创建机顶盒设备 -- 先注掉
			BundlePO bundlePO = new BundlePO();
			bundlePO.setBundleName(username + "_机顶盒");
			bundlePO.setUsername("机顶盒_" + userNo);
			//bundlePO.setOnlinePassword(password);
			bundlePO.setBundleId(BundlePO.createBundleId());
			bundlePO.setDeviceModel("tvos");
			bundlePO.setBundleType("VenusTerminal");
			bundlePO.setBundleNum("机顶盒_" + userNo);
			bundlePO.setUserId(Long.valueOf(userId));
			// 默认上线
			bundlePO.setOnlineStatus(ONLINE_STATUS.ONLINE);

			bundlePOs.add(bundlePO);
			bundleIds.add(bundlePO.getBundleId());
			configDefaultAbility(bundlePO);

			// 保存数据库
			bundleDao.save(bundlePOs);
			channelSchemeDao.save(channelSchemePOs);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
