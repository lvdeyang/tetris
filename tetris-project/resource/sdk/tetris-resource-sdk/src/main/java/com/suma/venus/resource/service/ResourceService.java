package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.suma.venus.resource.base.bo.BaseChannelParamTemplateBody;
import com.suma.venus.resource.base.bo.BundleBody;
import com.suma.venus.resource.base.bo.BundleConfig;
import com.suma.venus.resource.base.bo.BundleInfo;
import com.suma.venus.resource.base.bo.BundleParam;
import com.suma.venus.resource.base.bo.ChannelBody;
import com.suma.venus.resource.base.bo.ChannelConfig;
import com.suma.venus.resource.base.bo.ExternChannelParamTemplateBody;
import com.suma.venus.resource.base.bo.PlayerBundleBO;
import com.suma.venus.resource.base.bo.RectBO;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.base.bo.ScreenBO;
import com.suma.venus.resource.base.bo.UserAndResourceIdBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.suma.venus.resource.constant.VenusParamConstant;
import com.suma.venus.resource.constant.VenusParamConstant.ParamScope;
import com.suma.venus.resource.constant.VenusParamConstant.ParamType;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.dao.ExtraInfoDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.dao.LianwangPassbyDAO;
import com.suma.venus.resource.dao.LockBundleParamDao;
import com.suma.venus.resource.dao.LockChannelParamDao;
import com.suma.venus.resource.dao.LockScreenParamDao;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.dao.RolePrivilegeMapDAO;
import com.suma.venus.resource.dao.ScreenRectTemplateDao;
import com.suma.venus.resource.dao.ScreenSchemeDao;
import com.suma.venus.resource.dao.VirtualResourceDao;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ChannelParamPO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.pojo.LianwangPassbyPO;
import com.suma.venus.resource.pojo.LockBundleParamPO;
import com.suma.venus.resource.pojo.LockChannelParamPO;
import com.suma.venus.resource.pojo.LockScreenParamPO;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.suma.venus.resource.pojo.RolePrivilegeMap;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.pojo.VirtualResourcePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.system.role.SystemRoleVO;

/**
 * 资源层统一资源查询Service
 * 
 * @author lxw
 *
 */
@Service
public class ResourceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);

	@Autowired
	private BundleService bundleService;

	@Autowired
	private VirtualResourceService virtualResourceService;

	@Autowired
	private ChannelSchemeService channelSchemeService;

	@Autowired
	private ChannelTemplateService channelTemplateService;

	@Autowired
	private ChannelTemplateDao channelTemplateDao;

	@Autowired
	private ChannelParamService channelParamService;

	@Autowired
	private ExtraInfoService extraInfoService;

	@Autowired
	private ExtraInfoDao extraInfoDao;

	@Autowired
	private UserQueryFeign userFeign;

	@Autowired
	private ParamJsonUtil paramJsonUtil;

	@Autowired
	private LockChannelParamService lockChannelParamService;

	@Autowired
	private FolderService folderService;

	@Autowired
	private ScreenRectTemplateDao screenRectTemplateDao;

	@Autowired
	private ScreenSchemeDao screenSchemeDao;

	@Autowired
	private LockScreenParamDao lockScreenParamDao;

	@Autowired
	private LockBundleParamDao lockBundleParamDao;

	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private ChannelSchemeDao channelSchemeDao;

	@Autowired
	private VirtualResourceDao virtualResourceDao;

	@Autowired
	private WorkNodeDao workNodeDao;

	@Autowired
	private FolderDao folderDao;

	@Autowired
	private LockChannelParamDao lockChannelParamDao;
	
	@Autowired
	private UserQueryService userQueryService;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private PrivilegeDAO privilegeDao;
	
	@Autowired
	private RolePrivilegeMapDAO rolePrivilegeMapDao;
	
	@Autowired
	private LianwangPassbyDAO lianwangPassbyDao;

	/** 通过userId查询具有权限的user */
	public List<UserBO> queryUserresByUserId(Long userId, TerminalType terminalType) {
		List<UserBO> userresList = new ArrayList<UserBO>();
		try {
//			List<UserBO> allUsers = userFeign.queryUsers().get("users");
			List<UserBO> allUsers = userQueryService.queryAllUserBaseInfo(terminalType);
			if (null == allUsers || allUsers.isEmpty()) {
				return userresList;
			}
			Map<String, UserBO> allUserMap = new HashMap<String, UserBO>();
			for(UserBO user: allUsers){
				allUserMap.put(user.getUserNo(), user);
			}

			Set<String> userNoSet = new HashSet<String>();
			//ResourceIdListBO bo = userFeign.queryResourceByUserId(userId);
			ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
			if (null != bo && null != bo.getResourceCodes()) {
				for (String code : bo.getResourceCodes()) {
					// 绑定的用户资源的resouceCode格式:userNo-r(录制)/userNo-w(点播)/userNo-hj(呼叫)
					if (code.endsWith("-r") || code.endsWith("-w")) {
						userNoSet.add(code.substring(0, code.length() - 2));
					} else if (code.endsWith("-hj") || code.endsWith("-zk") || code.endsWith("-hy")) {
						userNoSet.add(code.substring(0, code.length() - 3));
					} else {
						userNoSet.add(code);
					}
				}
				// 取交集，取出具有权限的userNo
				userNoSet.retainAll(allUserMap.keySet());
				for (String userNo : userNoSet) {
					userresList.add(allUserMap.get(userNo));
				}
			}

		} catch (Exception e) {
			LOGGER.error("", e);
		}

		return userresList;
	}

	/** 根据用户id，bundleId和业务类型判断用户对设备是否有权限 **/
	public boolean hasPrivilegeOfBundle(Long userId, String bundleId, BUSINESS_OPR_TYPE businessType) {
		try {
			String code = null;// 权限码
			switch (businessType) {
			case DIANBO:// 点播
				code = bundleId + "-w";
				break;
			case RECORD:// 录制
				code = bundleId + "-r";
				break;
			default:
				return false;
			}
			//ResourceIdListBO bo = userFeign.queryResourceByUserId(userId);
			ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
			if (bo.getResourceCodes().contains(code)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return false;
	}
	
	/**
	 * 批量判断用户对设备的权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月10日 下午4:26:56
	 * @param Long userId 被查询用户id
	 * @param List<String> bundleIds 被检测设备id列表
	 * @param BUSINESS_OPR_TYPE businessType 业务类型
	 * @return boolean
	 */
	public boolean hasPrivilegeOfBundle(Long userId, List<String> bundleIds, BUSINESS_OPR_TYPE businessType) throws Exception{
		String suffix = null;
		switch (businessType) {
		case DIANBO:// 点播
			suffix = "-w";
			break;
		case RECORD:// 录制
			suffix = "-r";
			break;
		default:
			return false;
		}
		List<String> codes = new ArrayList<String>();
		for(String bundleId:bundleIds){
			codes.add(new StringBufferWrapper().append(bundleId).append(suffix).toString());
		}
		ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
		for(String code:codes){
			boolean hasPrivilege = false;
			if (bo.getResourceCodes().contains(code)){
				hasPrivilege = true;
			}
			if(!hasPrivilege) return false;
		}
		return true;
	}

	/** 根据用户id，目标用户id和业务类型判断用户对目标用户是否有权限 **/
	public boolean hasPrivilegeOfUser(Long userId, Long dstUserId, BUSINESS_OPR_TYPE businessType) {
		try {
			if(userId.equals(dstUserId)) return true;
//			UserBO dstUserBO = userFeign.queryUserInfoById(dstUserId).get("user");
			UserBO dstUserBO = userQueryService.queryUserByUserId(dstUserId, TerminalType.QT_ZK);
			String code = null;// 权限码
			switch (businessType) {
			case DIANBO:// 点播
				code = dstUserBO.getUserNo() + "-w";
				break;
			case RECORD:// 录制
				code = dstUserBO.getUserNo() + "-r";
				break;
			case CALL:// 呼叫
				code = dstUserBO.getUserNo() + "-hj";
				break;
			case ZK:// 指挥
				code = dstUserBO.getUserNo() + "-zk";
				break;
			case HY:// 指挥
				code = dstUserBO.getUserNo() + "-hy";
				break;
			default:
				return false;
			}
//			ResourceIdListBO bo = userFeign.queryResourceByUserId(userId);
			ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
			if (bo.getResourceCodes().contains(code)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}

		return false;
	}
	
	/**
	 * 批量查询用户对用户权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月10日 下午4:20:40
	 * @param Long userId 被查询用户id
	 * @param List<Long> dstUserIds 被检测用户id列表
	 * @param BUSINESS_OPR_TYPE businessType 业务类型
	 * @return boolean
	 */
	public boolean hasPrivilegeOfUser(Long userId, List<Long> dstUserIds, BUSINESS_OPR_TYPE businessType) throws Exception{
		List<UserBO> dstUserBOs = userQueryService.queryUsersByUserIds(dstUserIds, null);
		if(dstUserBOs==null || dstUserBOs.size()<=0) return false;
		String suffix = null;
		switch (businessType) {
		case DIANBO:// 点播
			suffix = "-w";
			break;
		case RECORD:// 录制
			suffix = "-r";
			break;
		case CALL:// 呼叫
			suffix = "-hj";
			break;
		case ZK:// 指挥
			suffix = "-zk";
			break;
		case HY:// 会议
			suffix = "-hy";
			break;
		default:
			return false;
		}
		//所有需要校验的权限码
		List<String> codes = new ArrayList<String>();
		for(UserBO dstUser:dstUserBOs){
			if(!dstUser.getId().equals(userId)){//如果是对自己，就不用add
				codes.add(new StringBufferWrapper().append(dstUser.getUserNo()).append(suffix).toString());
			}
		}
		ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
		for(String code:codes){
			boolean hasPrivilege = false;
			if (bo.getResourceCodes().contains(code)){
				hasPrivilege = true;
			}
			if(!hasPrivilege) return false;
		}
		return true;
	}
	
	public UserBO hasNoPrivilegeOfUsers(Long userId, List<Long> dstUserIds, BUSINESS_OPR_TYPE businessType) throws Exception{
		List<UserBO> dstUserBOs = userQueryService.queryUsersByUserIds(dstUserIds, null);
		if(dstUserBOs==null || dstUserBOs.size()<=0) return null;
		String suffix = null;
		switch (businessType) {
		case DIANBO:// 点播
			suffix = "-w";
			break;
		case RECORD:// 录制
			suffix = "-r";
			break;
		case CALL:// 呼叫
			suffix = "-hj";
			break;
		case ZK:// 指挥
			suffix = "-zk";
			break;
		default:
			return null;
		}
		ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
		for(UserBO dstUser:dstUserBOs){
			if(dstUser.getId().equals(userId)){
				continue;//如果是对自己，则不用校验
			}
			String code = new StringBufferWrapper().append(dstUser.getUserNo()).append(suffix).toString();
			boolean hasPrivilege = false;
			if (bo.getResourceCodes().contains(code)){
				hasPrivilege = true;
			}
			if(!hasPrivilege){
				return dstUser;
			}
		}
		return null;
	}

	/** 通过userId查询bundles */
	public List<BundleBody> queryBundlesByUserId(Long userId) {
		List<BundleBody> bundles = new ArrayList<BundleBody>();
		try {
			List<BundlePO> bundlePOs = bundleService.queryByUserId(userId);
			for (BundlePO po : bundlePOs) {
				if (null != po.getAccessNodeUid()) {// 过滤掉接入层layer_id为空的bundle
					bundles.add(getBundleBodyFromPO(po));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query bundles by userId : " + userId, e);
		}

		return bundles;
	}

	/** 通过文件夹类型和userId查询bundle **/
	public List<BundlePO> queryBundlesByFolderTypeAndUserId(FolderType folderType, Long userId) {
		// List<BundleBody> bundles = new ArrayList<BundleBody>();
		List<BundlePO> bundlePOs = null;
		try {
			// 查询folderType对应的所有folder下的bundleId
			List<String> folderBundleIds = bundleDao.findBundleIdsByFolderType(folderType);
			// 查询userId具有权限的bundleId
			List<String> userBundleIds = bundleService.queryBundleIdsByUserId(userId);
			// 取交集
			folderBundleIds.retainAll(userBundleIds);
			if (!folderBundleIds.isEmpty()) {
				bundlePOs = bundleDao.findInBundleIds(folderBundleIds);
				/*
				 * for (BundlePO po : bundlePOs) { bundles.add(getBundleBodyFromPO(po)); }
				 */
			}

		} catch (Exception e) {
			LOGGER.error("Fail to query bundles by userId : " + userId + " and folderType : " + folderType.name(), e);
		}

		return bundlePOs;
	}

	/** 通过bundleIds查询channels */
	public Map<String, List<ChannelBody>> queryChannelsByBundleIds(List<String> bundleIds) {
		Map<String, List<ChannelBody>> map = new HashMap<String, List<ChannelBody>>();
		if (null == bundleIds) {
			return map;
		}

		for (String bundleId : bundleIds) {
			map.put(bundleId, queryChannelsOnBundle(bundleId));
		}
		return map;
	}

	/** 通过bundleIds查询channels */
	public Map<String, List<ScreenBO>> queryScreensByBundleIds(List<String> bundleIds) {
		Map<String, List<ScreenBO>> map = new HashMap<>();
		if (null == bundleIds) {
			return map;
		}

		for (String bundleId : bundleIds) {
			map.put(bundleId, queryScreensOnBundle(bundleId));
		}
		return map;
	}

	/** 根据folerIds查询folders(分组) */
	public Map<Long, FolderPO> queryFoleders(Set<Long> folderIds) {
		Map<Long, FolderPO> map = new HashMap<Long, FolderPO>();
		if (null == folderIds) {
			return map;
		}

		try {
			for (Long folderId : folderIds) {
				FolderPO po = folderService.get(folderId);
				map.put(folderId, po);
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query folders : ", e);
		}
		return map;
	}

	/** 查询所有分组 */
	public List<FolderPO> queryAllFolders() {
		List<FolderPO> folderPOs = folderService.findAll();

		Collections.sort(folderPOs, Comparator.comparing(FolderPO::getFolderIndex));

		return folderPOs;
	}

	/** 根据接入层id查询接入层 */
	public List<AccessNodeBO> queryAccessNodeByNodeUids(Collection<String> nodeUids) {
		List<AccessNodeBO> nodes = new ArrayList<AccessNodeBO>();
		if (nodeUids == null || nodeUids.size() <= 0)
			return nodes;
		List<WorkNodePO> entities = workNodeDao.findByNodeUidIn(nodeUids);
		for (WorkNodePO entity : entities) {
			AccessNodeBO node = new AccessNodeBO();
			node.setNodeUid(entity.getNodeUid());
			node.setIp(entity.getIp());
			node.setPort(entity.getPort() == null ? null : entity.getPort().toString());
			node.setDownloadPort(entity.getDownloadPort() == null ? null : entity.getDownloadPort().toString());
			nodes.add(node);
		}
		return nodes;
	}

	/** 根据userId查询16个播放器资源 **/
	@Deprecated
	public List<PlayerBundleBO> queryPlayerBundlesByUserId(Long userId) {
		List<PlayerBundleBO> playerBundles = new ArrayList<>();
		List<BundlePO> playerBundlePOs = bundleService.queryPlayerBundlesByUserId(userId);
		for (BundlePO bundlePO : playerBundlePOs) {
			// 过滤掉第17个播放器
			if (bundlePO.getUsername().endsWith("_17")) {
				continue;
			}
			PlayerBundleBO playerBundle = new PlayerBundleBO();
			playerBundle.setBundleId(bundlePO.getBundleId());
			playerBundle.setBundleNum(bundlePO.getBundleNum());
			playerBundle.setBundleName(bundlePO.getBundleName());
			playerBundle.setUsername(bundlePO.getUsername());
			playerBundle.setPassword(bundlePO.getOnlinePassword());
			playerBundle.setBundleType(bundlePO.getBundleType());
			playerBundle.setChannelIds(channelSchemeDao.findChannelIdsByBundleId(bundlePO.getBundleId()));
			playerBundle.setAccessLayerId(bundlePO.getAccessNodeUid());
			WorkNodePO accessLayer = workNodeDao.findByNodeUid(bundlePO.getAccessNodeUid());
			if (null != accessLayer) {
				playerBundle.setAccessLayerIp(accessLayer.getIp());
				playerBundle.setAccessLayerPort(accessLayer.getPort());
			}
			playerBundles.add(playerBundle);
		}
		return playerBundles;
	}

	/** 根据userId查询第17个播放器资源 **/
	@Deprecated
	public PlayerBundleBO querySpecifiedPlayerBundle(Long userId) {
		List<BundlePO> playerBundlePOs = bundleService.queryPlayerBundlesByUserId(userId);
		for (BundlePO bundlePO : playerBundlePOs) {
			// 过滤出第17个播放器
			if (bundlePO.getUsername().endsWith("_17")) {
				PlayerBundleBO playerBundle = new PlayerBundleBO();
				playerBundle.setBundleId(bundlePO.getBundleId());
				playerBundle.setBundleNum(bundlePO.getBundleNum());
				playerBundle.setBundleName(bundlePO.getBundleName());
				playerBundle.setUsername(bundlePO.getUsername());
				playerBundle.setPassword(bundlePO.getOnlinePassword());
				playerBundle.setBundleType(bundlePO.getBundleType());
				playerBundle.setChannelIds(channelSchemeDao.findChannelIdsByBundleId(bundlePO.getBundleId()));
				playerBundle.setAccessLayerId(bundlePO.getAccessNodeUid());
				WorkNodePO accessLayer = workNodeDao.findByNodeUid(bundlePO.getAccessNodeUid());
				if (null != accessLayer) {
					playerBundle.setAccessLayerIp(accessLayer.getIp());
					playerBundle.setAccessLayerPort(accessLayer.getPort());
				}
				return playerBundle;
			}
		}
		return null;
	}

	/** 根据用户号码查询关联的编码器bundle */
	public BundlePO queryLdapEncoderByUserNo(String userno) {
		try {
			Map<String, UserBO> userMap = userFeign.queryLdapUserByUserNo(userno);
			if (null == userMap || userMap.isEmpty() || null == userMap.get("user")) {
				return null;
			}

			String encoderId = userMap.get("user").getEncoderId();
			return bundleDao.findByBundleId(encoderId);
		} catch (Exception e) {
			LOGGER.error("Fail to query encoder by ldap userno : " + userno, e);
			return null;
		}
	}

	/**
	 * 根据用户id查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月3日 下午2:41:44
	 * 
	 * @param Long userId 用户id
	 * @return UserBO 用户
	 */
	public UserBO queryUserById(Long userId, TerminalType terminalType) {
		try {
//			Map<String, UserBO> resultMap = userFeign.queryUserInfoById(userId);
//			if (resultMap == null || resultMap.size() <= 0 || resultMap.get("user") == null)
//				return null;
//			return resultMap.get("user");
			
			return userQueryService.queryUserByUserId(userId, terminalType);
		} catch (Exception e) {
			return null;
		}
	}

	public List<UserBO> queryUserListByIds(String ids, TerminalType terminalType) {
		if (StringUtils.isEmpty(ids)) {
			return null;
		}

		try {
			
			String[] idArr = ids.split(",");
			List<Long> idList = new ArrayList<>();
			for (String id : idArr) {
				idList.add(Long.valueOf(id));
			}

//			Map<String, List<UserBO>> resultMap = userFeign.queryUserListByIds(ids);
			
			List<UserBO> userBOs = userQueryService.queryUsersByUserIds(idList, terminalType);

//			if (!CollectionUtils.isEmpty(resultMap) && !CollectionUtils.isEmpty(resultMap.get("users"))) {
//				return resultMap.get("users");
//			}

			return userBOs;
		} catch (Exception e) {
			LOGGER.error("Fail to query queryUserListByIds, ids : " + ids, e);
		}

		return null;

	}

	/** 根据用户号码查询关联的解码器bundle */
	public BundlePO queryLdapDecoderByUserNo(String userno) {
		try {
			Map<String, UserBO> userMap = userFeign.queryLdapUserByUserNo(userno);
			if (null == userMap || userMap.isEmpty() || null == userMap.get("user")) {
				return null;
			}

			String decoderId = userMap.get("user").getDecoderId();
			return bundleDao.findByBundleId(decoderId);
		} catch (Exception e) {
			LOGGER.error("Fail to query decoder by ldap userno : " + userno, e);
			return null;
		}
	}

	/** 根据username查询关联的编码器bundle **/
	public BundlePO queryEncoderByUsername(String username) {
		try {
//			Map<String, UserBO> userMap = userFeign.queryUserInfo(username);
//			if (null == userMap || userMap.isEmpty() || null == userMap.get("user")) {
//				return null;
//			}
//
//			String encoderId = userMap.get("user").getEncoderId();
			//return bundleDao.findByBundleId(encoderId);
			return userQueryService.queryEncoderByUserName(username);
		} catch (Exception e) {
			LOGGER.error("Fail to query encoder by username : " + username, e);
			return null;
		}
	}

	/** 根据username查询关联的解码器bundle **/
	public BundlePO queryDecoderByUsername(String username) {
		try {
			//TODO
			//Map<String, UserBO> userMap = userFeign.queryUserInfo(username);
			//if (null == userMap || userMap.isEmpty() || null == userMap.get("user")) {
			//	return null;
			//}

			//String decoderId = userMap.get("user").getDecoderId();
			//return bundleDao.findByBundleId(decoderId);
			return userQueryService.queryDecoderByUserName(username);
		} catch (Exception e) {
			LOGGER.error("Fail to query decoder by username : " + username, e);
			return null;
		}
	}

	/**
	 * 根据自定义查询条件查询bundle资源
	 * 
	 * @param condition
	 * @return
	 */
	public List<BundleBody> queryBundles(String condition) {
		try {
			Set<String> bundleIdSet = queryBundleIds(condition);
			List<BundleBody> result = new ArrayList<>();
			for (String bundleId : bundleIdSet) {
				BundlePO po = bundleService.findByBundleId(bundleId);
				if (null == po.getAccessNodeUid()) {// 过滤掉接入层layer_id为空的bundle
					continue;
				}

				BundleBody bundleBody = getBundleBodyFromPO(po);

				List<ExtraInfoPO> extraInfoPOs = extraInfoService.findByBundleId(bundleId);
				if (!extraInfoPOs.isEmpty()) {
					JSONObject extraInfos = new JSONObject();
					for (ExtraInfoPO extraInfoPO : extraInfoPOs) {
						extraInfos.put(extraInfoPO.getName(), extraInfoPO.getValue());
					}
					bundleBody.setExtra_info(extraInfos);
				}
				result.add(bundleBody);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("Fail to query bundle by condition : " + condition, e);
		}

		return null;
	}

	/**
	 * 根据自定义查询条件查询bundle资源及对应的channels
	 * 
	 * @param condition
	 * @return
	 */
	public List<BundleBody> queryBundleAndChannel(String condition) {
		List<BundleBody> result = new ArrayList<>();
		try {
			Set<String> bundleIdSet = queryBundleIds(condition);
			for (String bundleId : bundleIdSet) {
				BundlePO po = bundleService.findByBundleId(bundleId);
				if (null == po.getAccessNodeUid()) {// 过滤掉接入层layer_id为空的bundle
					continue;
				}
				BundleBody bundleBody = getBundleBodyFromPO(po);
				List<ChannelBody> channels = queryChannelsOnBundle(bundleId);
				if (!channels.isEmpty()) {
					bundleBody.setChannels(channels);
				}

				List<ExtraInfoPO> extraInfoPOs = extraInfoService.findByBundleId(bundleId);
				if (!extraInfoPOs.isEmpty()) {
					JSONObject extraInfos = new JSONObject();
					for (ExtraInfoPO extraInfoPO : extraInfoPOs) {
						extraInfos.put(extraInfoPO.getName(), extraInfoPO.getValue());
					}
					bundleBody.setExtra_info(extraInfos);
				}
				result.add(bundleBody);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("Fail to query by condition : " + condition, e);
		}
		return result;
	}

	/**
	 * 获取channel上当前的任务参数
	 */
	public String queryBusinessParamOnChannel(String bundleId, String channelId) {
		try {
			LockChannelParamPO lockChannelParam = lockChannelParamService.findByBundleIdAndChannelId(bundleId, channelId);
			if (null != lockChannelParam) {
				return lockChannelParam.getChannelParam();
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query business param on channel : ", e);
		}

		return null;
	}

	/**
	 * 根据给定条件查询资源层虚拟资源
	 * 
	 * @param condition json字符串，举例如下: { "userId" : 1, "type" : "record" }
	 * @return
	 */
	public List<JSONObject> queryVirtualResouces(String condition) {
		try {
			Set<String> resourceIdSet = virtualResourceService.queryAllResourceId();
			JSONObject conditionJson = JSONObject.parseObject(condition);
			for (Entry<String, Object> entry : conditionJson.entrySet()) {
				Set<String> resourceIds = virtualResourceService.queryResourceIdByAttrNameAndAttrValue(entry.getKey(), String.valueOf(entry.getValue()));
				if (resourceIds.isEmpty()) {
					return null;
				}
				resourceIdSet.retainAll(resourceIds);
			}

			if (resourceIdSet.isEmpty()) {
				return null;
			}

			List<JSONObject> result = new ArrayList<>();
			for (String resourceId : resourceIdSet) {
				JSONObject resourceJson = virtualResourceService.packageResource(resourceId);
				if (null != resourceJson) {
					resourceJson.put("resourceId", resourceId);
					result.add(resourceJson);
				}
			}

			return result;
		} catch (Exception e) {
			LOGGER.error("Fail to query virtual resource by condition : " + condition, e);
		}

		return null;
	}

	/**
	 * 查询用户对某一资源是否有权限
	 * 
	 * @param userId
	 * @param resourceId
	 * @return
	 * @throws Exception 
	 */
	public boolean hasPrivilegeOfResource(Long userId, String resourceId) throws Exception {
		UserAndResourceIdBO requetParam = new UserAndResourceIdBO();
		requetParam.setUserId(userId);
		List<String> resourceIds = new ArrayList<String>();
		resourceIds.add(resourceId);
		resourceIds.add(resourceId + "-r");
		resourceIds.add(resourceId + "-w");
		requetParam.setResourceCodes(resourceIds);
//		try {
//			UserPrivilegeBO userPrivilege = userFeign.hasPrivilege(requetParam);
//			if (null == userPrivilege || null == userPrivilege.getPrivilegePermission() || userPrivilege.getPrivilegePermission().isEmpty()) {
//				return false;
//			}
//			return userPrivilege.getPrivilegePermission().get(0).isHasPrivilege() || userPrivilege.getPrivilegePermission().get(1).isHasPrivilege()
//					|| userPrivilege.getPrivilegePermission().get(2).isHasPrivilege();
//		} catch (Exception e) {
//			LOGGER.error("Fail to get user's privilege of resource", e);
//		}
//
//		return false;
		
		return userQueryService.hasPrivilege(requetParam);
	}

	/** 查询用户对某一bundle资源是否有读权限 
	 * @throws Exception */
	public boolean hasReadPrivilegeOfBundle(Long userId, String bundleId) throws Exception {
		UserAndResourceIdBO requetParam = new UserAndResourceIdBO();
		requetParam.setUserId(userId);
		List<String> resourceIds = new ArrayList<String>();
		resourceIds.add(bundleId + "-r");
		resourceIds.add(bundleId);
		requetParam.setResourceCodes(resourceIds);
//		try {
//			UserPrivilegeBO userPrivilege = userFeign.hasPrivilege(requetParam);
//			if (null == userPrivilege || null == userPrivilege.getPrivilegePermission() || userPrivilege.getPrivilegePermission().isEmpty()) {
//				return false;
//			}
//			return userPrivilege.getPrivilegePermission().get(0).isHasPrivilege() || userPrivilege.getPrivilegePermission().get(1).isHasPrivilege();
//		} catch (Exception e) {
//			LOGGER.error("Fail to get user's privilege of resource", e);
//		}
//
//		return false;
		
		return userQueryService.hasPrivilege(requetParam);
	}

	/** 查询用户对某一bundle资源是否有写权限 
	 * @throws Exception */
	public boolean hasWritePrivilegeOfBundle(Long userId, String bundleId) throws Exception {
		UserAndResourceIdBO requetParam = new UserAndResourceIdBO();
		requetParam.setUserId(userId);
		List<String> resourceIds = new ArrayList<String>();
		resourceIds.add(bundleId + "-w");
		resourceIds.add(bundleId);
		requetParam.setResourceCodes(resourceIds);
//		try {
//			UserPrivilegeBO userPrivilege = userFeign.hasPrivilege(requetParam);
//			if (null == userPrivilege || null == userPrivilege.getPrivilegePermission() || userPrivilege.getPrivilegePermission().isEmpty()) {
//				return false;
//			}
//			return userPrivilege.getPrivilegePermission().get(0).isHasPrivilege() || userPrivilege.getPrivilegePermission().get(1).isHasPrivilege();
//		} catch (Exception e) {
//			LOGGER.error("Fail to get user's privilege of resource", e);
//		}
//
//		return false;
		
		return userQueryService.hasPrivilege(requetParam);
	}

	/**
	 * 查询某一bundle上所有配置的通道
	 * 
	 * @param bundleId
	 * @return
	 */
	public List<ChannelBody> queryChannelsOnBundle(String bundleId) {
		List<ChannelBody> channels = new ArrayList<>();
		try {
			List<ChannelSchemePO> pos = channelSchemeService.findByBundleId(bundleId);
			for (ChannelSchemePO po : pos) {
				channels.add(getChannelBodyFromPO(po));
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query channel by bundleId : " + bundleId, e);
		}

		return channels;
	}

	/**
	 * 查询某一bundle上配置的屏信息
	 * 
	 * @param bundleId
	 * @return
	 */
	public List<ScreenBO> queryScreensOnBundle(String bundleId) {
		List<ScreenBO> screens = new ArrayList<>();

		try {
			BundlePO bundle = bundleService.findByBundleId(bundleId);
			screens = getScreens(bundle.getDeviceModel());
		} catch (Exception e) {
			LOGGER.error("Fail to query screens by bundleId : " + bundleId, e);
		}

		return screens;
	}

	/**
	 * 根据channelName(如video_hd_encode)查询所有对应的通道
	 * 
	 * @param channelName
	 * @return
	 */
	public List<ChannelBody> queryChannelsByChannelName(String channelName) {
		List<ChannelBody> channels = new ArrayList<>();
		try {
			List<ChannelSchemePO> pos = channelSchemeService.findByChannelName(channelName);
			if (pos.isEmpty()) {
				return null;
			}
			for (ChannelSchemePO po : pos) {
				channels.add(getChannelBodyFromPO(po));
			}
			return channels;
		} catch (Exception e) {
			LOGGER.error("Fail to query channel by channelName : " + channelName, e);
		}

		return null;
	}

	/**
	 * 查询某一通道上配置的模板参数
	 * 
	 * @param bundleId
	 * @param channelId
	 * @return
	 */
	public String queryChannelSchemeParam(String bundleId, String channelId) {

		try {
			ChannelSchemePO channelScheme = channelSchemeService.findByBundleIdAndChannelId(bundleId, channelId);
			JSONObject channelConstraintJson = new JSONObject();
			JSONObject channelParamJson = getChannelParamJsonBody(channelScheme.getChannelTemplateID());
			channelConstraintJson.put(VenusParamConstant.PARAM_JSON_KEY_CHANNELPARAM, channelParamJson);

			return channelConstraintJson.toJSONString();
		} catch (Exception e) {
			LOGGER.error("Fail to query channel scheme : ", e);
		}
		return null;
	}

	/***
	 * 查询某一channel模板的channelParam内容
	 * 
	 * @param channelTemplateId
	 * @return
	 */
	public JSONObject getChannelParamJsonBody(Long channelTemplateId) {
		try {
			JSONObject channelParamJson = new JSONObject();
			channelParamJson.put(VenusParamConstant.PARAM_JSON_KEY_TYPE, ParamType.CONTAINER.getName());
			JSONObject channelParamConstaint = new JSONObject();
			channelParamJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, channelParamConstaint);

			List<ChannelParamPO> channelParams = channelParamService.findByParentChannelTemplateId(channelTemplateId);
			paramJsonUtil.createChannelParamJson(channelParamConstaint, channelParams);
			return channelParamJson;
		} catch (Exception e) {
			LOGGER.error("Fail to get channel param json by channel templateID:" + channelTemplateId, e);
			return null;
		}
	}

	/**
	 * 获取通道写锁定参数
	 * 
	 * @return
	 */
	public String getWriteLockParam(String bundleId, String channelId) {
		try {
			LockChannelParamPO lockParam = lockChannelParamService.findByBundleIdAndChannelId(bundleId, channelId);
			if (null == lockParam) {
				return null;
			}
			return lockParam.getChannelParam();
		} catch (Exception e) {
			LOGGER.error("Fail to get write lock param of bundleId:" + bundleId + " channelId:" + channelId, e);
		}

		return null;
	}

	/**
	 * 获取bundle能力配置
	 * 
	 * @return
	 */
	public String getBundleConfig(String bundleId) {
		BundleParam param = getBundleConfigParam(bundleId);
		if (null != param) {
			return JSONObject.toJSONString(param);
		}
		return null;
	}

	public BundleParam getBundleConfigParam(String bundleId) {
		try {
			BundleParam result = new BundleParam();
			BundlePO po = bundleService.findByBundleId(bundleId);
			if (null == po) {
				return null;
			}
			BundleBody bundleBody = new BundleBody();
			result.setBundle(bundleBody);
			bundleBody.setBundle_id(po.getBundleId());
			List<ChannelBody> channels = new ArrayList<ChannelBody>();
			bundleBody.setChannels(channels);
			List<ChannelSchemePO> configParams = channelSchemeService.findByBundleId(bundleId);
			for (ChannelSchemePO configParam : configParams) {
				channels.add(getChannelBodyFromPO(configParam));
			}

			return result;
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}
		return null;
	}

	/**
	 * 获取bundle整体信息
	 * 
	 * @return
	 */
	public BundleInfo getBundleInfo(String bundleId) {
		try {
			BundlePO bundlePO = bundleService.findByBundleId(bundleId);
			if (null == bundlePO) {
				return null;
			}
			BundleInfo bundleInfo = new BundleInfo();
			bundleInfo.setBundle_id(bundleId);
			bundleInfo.setBundle_type(bundlePO.getBundleType());
			bundleInfo.setOperate_index(bundlePO.getOperateIndex());
			// 填充bundleConfig部分
			bundleInfo.setBundle_config(createBundleConfig(bundleId));
			// 填充channels部分
			bundleInfo.setChannels(createChannelsOfBundleInfo(bundlePO.getBundleId()));

			// 填充screens部分
			bundleInfo.setScreens(createScreensOfBundleInfo(bundlePO.getBundleId()));

			// 设置pass_by_str
			JSONObject passby = new JSONObject();
			LockBundleParamPO lockBundleParamPO = lockBundleParamDao.findByBundleId(bundleId);
			if (null != lockBundleParamPO && null != lockBundleParamPO.getPassByStr()) {
				passby = JSONObject.parseObject(lockBundleParamPO.getPassByStr());
				//bundleInfo.setPass_by_str(lockBundleParamPO.getPassByStr());
			}
			
			List<LianwangPassbyPO> passbyPOs = lianwangPassbyDao.findUuid(bundleId);
			if(passbyPOs != null && passbyPOs.size() > 0){
				for(LianwangPassbyPO passbyPO: passbyPOs){
					passby.put(passbyPO.getType(), passbyPO.getProtocol());
				}
			}
			bundleInfo.setPass_by_str(passby.toJSONString());

			// bundle_extra_info
			List<ExtraInfoPO> extraInfos = extraInfoService.findByBundleId(bundleId);
			if (!extraInfos.isEmpty()) {
				JSONObject bundleExtraInfoJson = new JSONObject();
				for (ExtraInfoPO extraInfoPO : extraInfos) {
					bundleExtraInfoJson.put(extraInfoPO.getName(), extraInfoPO.getValue());
				}
				bundleInfo.setBundle_extra_info(bundleExtraInfoJson.toJSONString());
			}

			// user_extra_info
			/*Map<String, Object> userInfoMap = userFeign.queryExtraInfo(bundlePO.getUsername());
			if (null != userInfoMap.get("extraInfo")) {
				bundleInfo.setUser_extra_info(String.valueOf(userInfoMap.get("extraInfo")));
			}*/

			return bundleInfo;
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}

		return null;
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void batchClearBundles(List<String> bundleIdList) throws Exception {

		List<BundlePO> bundlePOList = bundleDao.findByBundleIdIn(bundleIdList);

		List<ChannelSchemePO> channelSchemePOList = channelSchemeDao.findByBundleIdIn(bundleIdList);

		List<ScreenSchemePO> screenSchemePOList = screenSchemeDao.findByBundleIdIn(bundleIdList);

		for (ChannelSchemePO channelSchemePO : channelSchemePOList) {
			channelSchemePO.setChannelStatus(LockStatus.IDLE);
			channelSchemePO.setOperateIndex(channelSchemePO.getOperateIndex() + 1);
		}

		for (ScreenSchemePO screenSchemePO : screenSchemePOList) {
			screenSchemePO.setStatus(LockStatus.IDLE);
		}

		for (BundlePO bundlePO : bundlePOList) {
			bundlePO.setLockStatus(LockStatus.IDLE);
			bundlePO.setOperateIndex(bundlePO.getOperateIndex() + 1);
			bundlePO.setOperateCount(0);
			bundlePO.setFreeAudioSrcCnt(bundlePO.getMaxAudioSrcCnt());
			bundlePO.setFreeVideoSrcCnt(bundlePO.getMaxVideoSrcCnt());
		}

		channelSchemeDao.save(channelSchemePOList);
		lockChannelParamDao.deleteBatchByBundleIds(bundleIdList);

		screenSchemeDao.save(screenSchemePOList);
		lockScreenParamDao.deleteBatchByBundleIds(bundleIdList);

		lockBundleParamDao.deleteBatchByBundleIds(bundleIdList);
		bundleDao.save(bundlePOList);

	}

	/**
	 * 批量获取bundle整体信息
	 * 
	 * 批量获取的过程 需要考虑到数据库搜索效率问题？
	 * 
	 * @return
	 */
	public List<BundleInfo> getBatchBundleInfos(List<String> bundleIdList) {

		List<BundleInfo> BundleInfoList = new LinkedList<BundleInfo>();

		try {
			List<BundlePO> bundlePOList = bundleDao.findInBundleIds(bundleIdList);
			List<LianwangPassbyPO> allPassby = lianwangPassbyDao.findByUuidIn(bundleIdList);

			if (CollectionUtils.isEmpty(bundlePOList)) {
				return null;
			}

			// 批量查询操作
			List<ChannelSchemePO> channelShchemePOList = channelSchemeDao.findByBundleIdIn(bundleIdList);
			Map<String, List<ChannelSchemePO>> channelSchemePOMap = channelShchemePOList.stream().collect(Collectors.groupingBy(ChannelSchemePO::getBundleId));

			List<Long> channelTemplateIdList = channelShchemePOList.stream().map(b -> b.getChannelTemplateID()).collect(Collectors.toList());
			List<ChannelTemplatePO> channelTemplatePOList = channelTemplateDao.findByIdIn(channelTemplateIdList);
			Map<Long, ChannelTemplatePO> channelTemplatePOMap = channelTemplatePOList.stream()
					.collect(Collectors.toMap(ChannelTemplatePO::getId, Function.identity(), (key1, key2) -> key2));

			List<ScreenSchemePO> screenSchemePOList = screenSchemeDao.findByBundleIdIn(bundleIdList);
			Map<String, List<ScreenSchemePO>> screenSchemePOMap = screenSchemePOList.stream().collect(Collectors.groupingBy(ScreenSchemePO::getBundleId));

			List<LockBundleParamPO> lockBundleParamPOList = lockBundleParamDao.findByBundleIdIn(bundleIdList);
			Map<String, LockBundleParamPO> lockBundleParamPOMap = lockBundleParamPOList.stream()
					.collect(Collectors.toMap(LockBundleParamPO::getBundleId, Function.identity(), (key1, key2) -> key2));

			List<ExtraInfoPO> extraInfoList = extraInfoDao.findByBundleIdIn(bundleIdList);
			Map<String, List<ExtraInfoPO>> extraInfoListMap = extraInfoList.stream().collect(Collectors.groupingBy(ExtraInfoPO::getBundleId));

			/*List<String> userNameReqList = bundlePOList.stream().map(b -> b.getUsername()).collect(Collectors.toList());
			Map<String, Object> userInfoFeignMap = userFeign.queryExtraInfoMap(userNameReqList);

			// @SuppressWarnings("unchecked")
			// Map<String, String> userInfoMap = (Map<String, String>)
			// userInfoFeignMap.get("extraInfo");
			String userInfoMapStr = (String) userInfoFeignMap.get("extraInfo");
			@SuppressWarnings("unchecked")
			Map<String, String> userInfoMap = (Map<String, String>) JSONObject.parse(userInfoMapStr);*/

			List<LockChannelParamPO> lockChannelParamPOList = lockChannelParamDao.findByBundleIdIn(bundleIdList);
			Map<String, LockChannelParamPO> lockChannelParamPOMap = new HashMap<String, LockChannelParamPO>();
			for (LockChannelParamPO lockChannelParamPO : lockChannelParamPOList) {
				lockChannelParamPOMap.put(lockChannelParamPO.getBundleId() + "-" + lockChannelParamPO.getChannelId(), lockChannelParamPO);
			}

			Map<String, BundleConfig> bundleConfigMap = createBundleConfigMap(channelSchemePOMap, channelTemplatePOMap);
			Map<String, List<JSONObject>> channelsOfBundleInfoMap = createChannelsOfBundleInfoMap(channelSchemePOMap, lockChannelParamPOMap);
			Map<String, List<JSONObject>> screensOfBundleInfoMap = createScreensOfBundleInfoMap(screenSchemePOMap);

			for (BundlePO bundlePO : bundlePOList) {

				BundleInfo bundleInfo = new BundleInfo();

				bundleInfo.setBundle_id(bundlePO.getBundleId());
				bundleInfo.setBundle_type(bundlePO.getBundleType());
				bundleInfo.setOperate_index(bundlePO.getOperateIndex());
				// 填充bundleConfig部分
				bundleInfo.setBundle_config(bundleConfigMap.get(bundlePO.getBundleId()));

				// 填充channels部分
				bundleInfo.setChannels(channelsOfBundleInfoMap.get(bundlePO.getBundleId()));

				// 填充screens部分
				bundleInfo.setScreens(screensOfBundleInfoMap.get(bundlePO.getBundleId()));

				// 设置pass_by_str
				JSONObject passby = new JSONObject();
				LockBundleParamPO lockBundleParamPO = lockBundleParamPOMap.get(bundlePO.getBundleId());
				if (null != lockBundleParamPO && null != lockBundleParamPO.getPassByStr()) {
					passby = JSONObject.parseObject(lockBundleParamPO.getPassByStr());
					//bundleInfo.setPass_by_str(lockBundleParamPO.getPassByStr());
				}
				
				List<LianwangPassbyPO> passbyPOs = queryByUuid(allPassby, bundlePO.getBundleId());
				if(passbyPOs != null && passbyPOs.size() > 0){
					for(LianwangPassbyPO passbyPO: passbyPOs){
						passby.put(passbyPO.getType(), passbyPO.getProtocol());
					}
				}
				bundleInfo.setPass_by_str(passby.toJSONString());

				// bundle_extra_info
				List<ExtraInfoPO> extraInfos = extraInfoListMap.get(bundlePO.getBundleId());
				if (!CollectionUtils.isEmpty(extraInfos)) {
					JSONObject bundleExtraInfoJson = new JSONObject();
					for (ExtraInfoPO extraInfoPO : extraInfos) {
						bundleExtraInfoJson.put(extraInfoPO.getName(), extraInfoPO.getValue());
					}
					bundleInfo.setBundle_extra_info(bundleExtraInfoJson.toJSONString());
				}

				// user_extra_info
				/*if (null != userInfoMap.get(bundlePO.getUsername())) {
					bundleInfo.setUser_extra_info(String.valueOf(userInfoMap.get(bundlePO.getUsername())));
				}*/

				BundleInfoList.add(bundleInfo);
			}

			return BundleInfoList;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.toString());
		}

		return null;
	}

	/** 批量查询、填充bundleInfo的bundle_config部分 */
	private Map<String, BundleConfig> createBundleConfigMap(Map<String, List<ChannelSchemePO>> channelSchemePOMap, Map<Long, ChannelTemplatePO> channelTemplatePOMap) {

		HashMap<String, BundleConfig> bundleConfigMap = new HashMap<String, BundleConfig>();

		for (String bundleId : channelSchemePOMap.keySet()) {

			List<ChannelSchemePO> channelSchemePOs = channelSchemePOMap.get(bundleId);

			BundleConfig bundleConfig = new BundleConfig();
			for (ChannelSchemePO channelConfigPO : channelSchemePOs) {

				ChannelConfig channelConfig = new ChannelConfig();
				channelConfig.setChannel_name(channelConfigPO.getChannelName());
				ChannelTemplatePO channelTemplatePO = channelTemplatePOMap.get(channelConfigPO.getChannelTemplateID());
				channelConfig.setBase_type(channelTemplatePO.getBaseType());
				channelConfig.setExtern_type(channelTemplatePO.getExternType());
				channelConfig.setChannel_id(channelConfigPO.getChannelId());
				bundleConfig.getChannels().add(channelConfig);
			}

			bundleConfigMap.put(bundleId, bundleConfig);
		}

		return bundleConfigMap;
	}

	/** 填充bundleInfo的bundle_config部分 */
	private BundleConfig createBundleConfig(String bundleId) {
		BundleConfig bundleConfig = new BundleConfig();
		List<ChannelSchemePO> channelConfigPOs = channelSchemeService.findByBundleId(bundleId);
		for (ChannelSchemePO channelConfigPO : channelConfigPOs) {
			ChannelConfig channelConfig = new ChannelConfig();
			channelConfig.setChannel_name(channelConfigPO.getChannelName());
			ChannelTemplatePO channelTemplatePO = channelTemplateService.get(channelConfigPO.getChannelTemplateID());
			channelConfig.setBase_type(channelTemplatePO.getBaseType());
			channelConfig.setExtern_type(channelTemplatePO.getExternType());
			channelConfig.setChannel_id(channelConfigPO.getChannelId());
			bundleConfig.getChannels().add(channelConfig);
		}
		return bundleConfig;
	}

	private Map<String, List<JSONObject>> createChannelsOfBundleInfoMap(Map<String, List<ChannelSchemePO>> channelSchemePOMap,
			Map<String, LockChannelParamPO> lockChannelParamPOMap) {

		Map<String, List<JSONObject>> channelsOfBundleInfoMap = new HashMap<String, List<JSONObject>>();

		for (String bundleId : channelSchemePOMap.keySet()) {
			List<JSONObject> channelJsons = new ArrayList<JSONObject>();

			List<ChannelSchemePO> channelConfigPOs = channelSchemePOMap.get(bundleId);
			for (ChannelSchemePO channelConfigPO : channelConfigPOs) {
				JSONObject channelJson = new JSONObject();
				channelJson.put("channel_id", channelConfigPO.getChannelId());
				channelJson.put("operate_index", channelConfigPO.getOperateIndex());
				if (LockStatus.BUSY == channelConfigPO.getChannelStatus()) {
					// 通道当前被占用
					LockChannelParamPO lockChannelParamPO = lockChannelParamPOMap.get(bundleId + "-" + channelConfigPO.getChannelId());
					if (null != lockChannelParamPO) {
						channelJson.put("channel_param", JSONObject.parseObject(lockChannelParamPO.getChannelParam()));
					}
					channelJson.put("status", "Open");
				} else {
					// 通道当前空闲
					channelJson.put("channel_param", null);
					channelJson.put("status", "Close");
				}
				channelJsons.add(channelJson);
			}

			channelsOfBundleInfoMap.put(bundleId, channelJsons);
		}

		return channelsOfBundleInfoMap;
	}

	/** 填充bundleInfo的channels部分 */
	private List<JSONObject> createChannelsOfBundleInfo(String bundleId) {
		List<JSONObject> channelJsons = new ArrayList<JSONObject>();
		List<ChannelSchemePO> channelConfigPOs = channelSchemeService.findByBundleId(bundleId);
		for (ChannelSchemePO channelConfigPO : channelConfigPOs) {
			JSONObject channelJson = new JSONObject();
			channelJson.put("channel_id", channelConfigPO.getChannelId());
			channelJson.put("operate_index", channelConfigPO.getOperateIndex());
			if (LockStatus.BUSY == channelConfigPO.getChannelStatus()) {
				// 通道当前被占用
				LockChannelParamPO lockChannelParamPO = lockChannelParamService.findByBundleIdAndChannelId(bundleId, channelConfigPO.getChannelId());
				if (null != lockChannelParamPO) {
					channelJson.put("channel_param", JSONObject.parseObject(lockChannelParamPO.getChannelParam()));
				}
				channelJson.put("status", "Open");
			} else {
				// 通道当前空闲
				channelJson.put("channel_param", null);
				channelJson.put("status", "Close");
			}
			channelJsons.add(channelJson);
		}

		return channelJsons;
	}

	private Map<String, List<JSONObject>> createScreensOfBundleInfoMap(Map<String, List<ScreenSchemePO>> screenSchemePOMap) {

		Map<String, List<JSONObject>> screensOfBundleInfoMap = new HashMap<String, List<JSONObject>>();

		for (String bundleId : screenSchemePOMap.keySet()) {

			List<JSONObject> screensJsons = new ArrayList<JSONObject>();
			List<ScreenSchemePO> screenSchemePOs = screenSchemePOMap.get(bundleId);
			for (ScreenSchemePO screenSchemePO : screenSchemePOs) {
				JSONObject screenJson = new JSONObject();
				screenJson.put("screen_id", screenSchemePO.getScreenId());
				if (LockStatus.BUSY == screenSchemePO.getStatus()) {
					screenJson.put("screen_status", "Open");
					// TODO
					LockScreenParamPO lockScreenParamPO = lockScreenParamDao.findByBundleIdAndScreenId(bundleId, screenSchemePO.getScreenId());
					if (null != lockScreenParamPO) {
						screenJson.put("rects", JSONArray.parseArray(lockScreenParamPO.getScreenParam()));
					}
				} else {
					screenJson.put("screen_status", "Close");
					// TODO
					List<ScreenRectTemplatePO> screenRectTemplates = screenRectTemplateDao.findByDeviceModelAndScreenId(screenSchemePO.getDeviceModel(),
							screenSchemePO.getScreenId());
					List<JSONObject> rects = new ArrayList<JSONObject>();
					for (ScreenRectTemplatePO screenRectTemplate : screenRectTemplates) {
						JSONObject rect = new JSONObject();
						rect.put("rect_id", screenRectTemplate.getRectId());
						rects.add(rect);
					}
					screenJson.put("rects", rects);
				}
				screensJsons.add(screenJson);
			}

			// return screensJsons;
			screensOfBundleInfoMap.put(bundleId, screensJsons);

		}

		return screensOfBundleInfoMap;
	}

	/*** 填充bundleInfo的screens部分 */
	private List<JSONObject> createScreensOfBundleInfo(String bundleId) {
		List<JSONObject> screensJsons = new ArrayList<JSONObject>();
		List<ScreenSchemePO> screenSchemePOs = screenSchemeDao.findByBundleId(bundleId);
		for (ScreenSchemePO screenSchemePO : screenSchemePOs) {
			JSONObject screenJson = new JSONObject();
			screenJson.put("screen_id", screenSchemePO.getScreenId());
			if (LockStatus.BUSY == screenSchemePO.getStatus()) {
				screenJson.put("screen_status", "Open");
				LockScreenParamPO lockScreenParamPO = lockScreenParamDao.findByBundleIdAndScreenId(bundleId, screenSchemePO.getScreenId());
				if (null != lockScreenParamPO) {
					screenJson.put("rects", JSONArray.parseArray(lockScreenParamPO.getScreenParam()));
				}
			} else {
				screenJson.put("screen_status", "Close");
				List<ScreenRectTemplatePO> screenRectTemplates = screenRectTemplateDao.findByDeviceModelAndScreenId(screenSchemePO.getDeviceModel(), screenSchemePO.getScreenId());
				List<JSONObject> rects = new ArrayList<JSONObject>();
				for (ScreenRectTemplatePO screenRectTemplate : screenRectTemplates) {
					JSONObject rect = new JSONObject();
					rect.put("rect_id", screenRectTemplate.getRectId());
					rects.add(rect);
				}
				screenJson.put("rects", rects);
			}
			screensJsons.add(screenJson);
		}

		return screensJsons;
	}

	/**
	 * 获取全局base channel param模板
	 * 
	 * @return
	 */
	public String getBaseChannelParamTemplate(String baseType) {
		try {
			JSONObject baseChannelParamTemplateBody = getBaseChannelParamTemplateJson(baseType);
			if (null != baseChannelParamTemplateBody) {
				return JSONObject.toJSONString(new BaseChannelParamTemplateBody(baseChannelParamTemplateBody));
			}
		} catch (Exception e) {
			LOGGER.error("Get base channel param template error : ", e);
		}

		return null;
	}

	public JSONObject getBaseChannelParamTemplateJson(String baseType) {
		try {
			ChannelParamPO baseTypeParam = channelParamService.findByParamNameAndConstValueAndParamScope(VenusParamConstant.PARAM_JSON_KEY_BASETYPE, baseType,
					ParamScope.GLOBAL_BASIC);
			if (null == baseTypeParam) {
				return null;
			}

			// 找其父节点
			ChannelParamPO baseParamPO = channelParamService.get(baseTypeParam.getParentChannelParamId());
			JSONObject baseParamJson = new JSONObject();
			baseParamJson.put(VenusParamConstant.PARAM_JSON_KEY_TYPE, ParamType.CONTAINER.getName());
			JSONObject baseParamConstraint = new JSONObject();
			baseParamJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, baseParamConstraint);
			paramJsonUtil.createChannelParamJson(baseParamConstraint, channelParamService.findByParentChannelParamId(baseParamPO.getId()));

			return baseParamJson;
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}

		return null;
	}

	/**
	 * 获取全局extern channel param模板
	 * 
	 * @return
	 */
	public String getExternChannelParamTemplate(String externType) {
		try {
			JSONObject externChannelParamTemplateJson = getExternChannelParamTemplateJson(externType);
			if (null != externChannelParamTemplateJson) {
				return JSONObject.toJSONString(new ExternChannelParamTemplateBody(externChannelParamTemplateJson));
			}
		} catch (Exception e) {
			LOGGER.error("Get base channel param template error : ", e);
		}
		return null;
	}

	public JSONObject getExternChannelParamTemplateJson(String externType) {
		try {
			ChannelParamPO externTypeParam = channelParamService.findByParamNameAndConstValueAndParamScope(VenusParamConstant.PARAM_JSON_KEY_EXTERNTYPE, externType,
					ParamScope.GLOBAL_EXTERN);
			if (null == externTypeParam) {
				return null;
			}

			ChannelParamPO externParam = channelParamService.get(externTypeParam.getParentChannelParamId());
			JSONObject externParamJson = new JSONObject();
			externParamJson.put(VenusParamConstant.PARAM_JSON_KEY_TYPE, ParamType.CONTAINER.getName());
			JSONObject externParamConstraint = new JSONObject();
			externParamJson.put(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT, externParamConstraint);
			paramJsonUtil.createChannelParamJson(externParamConstraint, channelParamService.findByParentChannelParamId(externParam.getId()));

			return externParamJson;
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}

		return null;
	}

	/**
	 * 查询能力模板中的channel模板信息
	 * 
	 * @param deviceModel
	 * @return map集合，key为channelName,value为channelParam
	 */
	public Map<String, String> queryChannelMapByDeviceModel(String deviceModel) {
		Map<String, String> channelMap = new HashMap<String, String>();
		try {
			List<ChannelTemplatePO> channelTemplates = channelTemplateService.findByDeviceModel(deviceModel);
			for (ChannelTemplatePO channelTemplate : channelTemplates) {
				JSONObject channelParam = getChannelParamJsonBody(channelTemplate.getId());
				if (null != channelParam) {
					JSONObject channelParamJson = new JSONObject();
					channelParamJson.put(VenusParamConstant.PARAM_JSON_KEY_CHANNELPARAM, channelParam);
					channelMap.put(channelTemplate.getChannelName(), channelParamJson.toJSONString());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query channel by device model", e);
		}
		return channelMap;
	}

	/**
	 * 通道参数匹配
	 * 
	 * @param businessParam 业务参数,json字符串格式 { "channel_param" : { 。。。 } }
	 * @param channelParam  通道能力参数，json字符串格式 { "channel_param" : { "type" :
	 *                      "container", "constraint" : { ... } } }
	 * @return
	 */
	public boolean matchChannelParam(String businessParam, String channelParam) {
		try {
			JSONObject businessParamJson = JSONObject.parseObject(businessParam).getJSONObject("channel_param");
			JSONObject channelParamJson = JSONObject.parseObject(channelParam).getJSONObject("channel_param").getJSONObject(VenusParamConstant.PARAM_JSON_KEY_CONSTRAINT);
			if (null == channelParamJson) {
				return false;
			}

			return paramJsonUtil.matchParam(businessParamJson, channelParamJson);
		} catch (Exception e) {
			LOGGER.error("Fail to match channel param", e);
			return false;
		}
	}

	/** 为bundle设备folder */
	public boolean setFolderToBundles(Long folderId, List<String> bundleIds) {
		try {
			for (String bundleId : bundleIds) {
				BundlePO bundlePO = bundleService.findByBundleId(bundleId);
				bundlePO.setFolderId(folderId);
				bundleService.save(bundlePO);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("Failt to set folderId to Bundles : ", e);
			return false;
		}
	}

	/** 清除bundle的文件夹ID */
	public boolean clearFolderIdOfBundles(List<String> bundleIds) {
		try {
			for (String bundleId : bundleIds) {
				BundlePO bundlePO = bundleService.findByBundleId(bundleId);
				bundlePO.setFolderId(null);
				bundleService.save(bundlePO);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("Failt to reset folderId of Bundles : ", e);
			return false;
		}
	}

	/** 向文件夹下添加点播资源 **/
	public List<JSONObject> setFolderIdToOnDemandResource(Long folderId, List<JSONObject> onDemandResources) {
		try {
			List<VirtualResourcePO> virtualResourcePOs = new ArrayList<VirtualResourcePO>();
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			Set<String> resourceIds = new HashSet<String>();
			
			//里面的用户只可能是同一个，所以先按照用户只是同一个来处理
			Long _userId = null;
			for (JSONObject onDemandResource : onDemandResources) {
				Long userId = onDemandResource.getLong("userId");
				if(userId != null){
					_userId = userId;
					break;
				}
			}
			
			SystemRoleVO roleVO = null;
			if(_userId != null){
				roleVO = userQueryService.queryPrivateRoleId(_userId);
			}
			
			// 绑定资源权限
			RoleAndResourceIdBO roleAndResourceIdBO = new RoleAndResourceIdBO();
			roleAndResourceIdBO.setResourceCodes(new ArrayList<String>());
			for (JSONObject onDemandResource : onDemandResources) {
				String resourceId = BundlePO.createBundleId();
				resourceIds.add(resourceId);

				roleAndResourceIdBO.getResourceCodes().add(resourceId);
				// 点播资源实体属性
				for (Entry<String, Object> entry : onDemandResource.entrySet()) {
					virtualResourcePOs.add(new VirtualResourcePO(entry.getKey(), String.valueOf(entry.getValue()), resourceId));
				}

				// 添加folderID属性
				if (!onDemandResource.containsKey("folderId")) {
					virtualResourcePOs.add(new VirtualResourcePO("folderId", String.valueOf(folderId), resourceId));
				}
			}

			virtualResourceDao.save(virtualResourcePOs);
			
			if(roleVO != null){
				roleAndResourceIdBO.setRoleId(Long.valueOf(roleVO.getId()));
				userQueryService.bindRolePrivilege(roleAndResourceIdBO);
			}
			
			for (String resourceId : resourceIds) {
				jsonList.add(virtualResourceService.packageResource(resourceId));
			}

			return jsonList;
		} catch (Exception e) {
			LOGGER.error("Failt to set folderId to on-demand resources: ", e);
			return null;
		}
	}

	/** 清除点播资源的文件夹ID **/
	public boolean clearFolderIdOfOnDemandResource(List<String> resourceIds) {
		try {
			for (String resourceId : resourceIds) {
				virtualResourceDao.deleteByAttrNameAndResourceId("folderId", resourceId);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("Failt to reset folderId of on-demand resources: ", e);
			return false;
		}
	}

	/** 查询点播资源的预览路径列表 */
	public Set<String> queryResourcePreviewUrl() {
		Set<String> previewUrls = virtualResourceDao.queryAttrValueByAttrName("previewUrl");
		return previewUrls;
	}

	/** 查询用户创建的点播资源 **/
	public List<JSONObject> queryOnDemandResourceCreatedByUser(Long userId) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			Set<String> resourceIds = virtualResourceDao.queryResourceIdByAttrNameAndAttrValue("userId", String.valueOf(userId));
			for (String resourceId : resourceIds) {
				jsonList.add(virtualResourceService.packageResource(resourceId));
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query on-demand resource created by userId : " + userId, e);
		}
		return jsonList;
	}

	/** 根据id查询点播资源 **/
	public JSONObject queryOnDemandResourceById(String resourceId) {
		try {
			return virtualResourceService.packageResource(resourceId);
		} catch (Exception e) {
			LOGGER.error("Fail to query on-demand resource created by id : " + resourceId, e);
		}
		return null;
	}

	/** 根据名称查询点播资源 **/
	public List<JSONObject> queryOnDemandResourceByName(String name) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			Set<String> resourceIds = virtualResourceDao.queryResourceIdByAttrNameAndAttrValue("name", name);
			for (String resourceId : resourceIds) {
				jsonList.add(virtualResourceService.packageResource(resourceId));
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query on-demand resource created by name : " + name, e);
		}
		return jsonList;
	}

	/** 根据名称模糊查询点播资源 **/
	public List<JSONObject> queryOnDemandResourceByNameLike(String name) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			Set<String> resourceIds = virtualResourceDao.queryResourceIdByNameLike(name);
			for (String resourceId : resourceIds) {
				jsonList.add(virtualResourceService.packageResource(resourceId));
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query on-demand resource created by name like : " + name, e);
		}
		return jsonList;
	}

	/** 查询文件夹下的点播资源 **/
	public List<JSONObject> queryOnDemandResourceByFolderId(Long folderId) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			Set<String> resourceIds = virtualResourceDao.queryResourceIdByAttrNameAndAttrValue("folderId", String.valueOf(folderId));
			for (String resourceId : resourceIds) {
				jsonList.add(virtualResourceService.packageResource(resourceId));
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query on-demand resource by folderId : " + folderId, e);
		}
		return jsonList;
	}

	/** 查询userID有权限的点播资源 **/
	public List<JSONObject> queryOnDemandResourceByUserId(Long userId) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			// 查询userId具有权限的所有资源ID
			//ResourceIdListBO bo = userFeign.queryResourceByUserId(userId);
			ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
			if (null != bo && null != bo.getResourceCodes() && !bo.getResourceCodes().isEmpty()) {
				// 查询所有虚拟资源ID
				Set<String> resourceIds = virtualResourceDao.queryAllResourceId();
				// 取交集
				resourceIds.retainAll(bo.getResourceCodes());
				for (String resourceId : resourceIds) {
					jsonList.add(virtualResourceService.packageResource(resourceId));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query on-demand resource by userId : " + userId, e);
		}
		return jsonList;
	}

	/** 查询文件夹下userID有权限的点播资源 **/
	public List<JSONObject> queryOnDemandResourceByFolderId(Long userId, Long folderId) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			// 查询userId具有权限的所有资源ID
			//ResourceIdListBO bo = userFeign.queryResourceByUserId(userId);
			ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
			if (null != bo && null != bo.getResourceCodes() && !bo.getResourceCodes().isEmpty()) {
				// 查询文件夹下的虚拟资源ID
				Set<String> resourceIds = virtualResourceDao.queryResourceIdByAttrNameAndAttrValue("folderId", String.valueOf(folderId));
				// 取交集
				resourceIds.retainAll(bo.getResourceCodes());
				for (String resourceId : resourceIds) {
					jsonList.add(virtualResourceService.packageResource(resourceId));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query on-demand resource by folderId : " + folderId, e);
		}
		return jsonList;
	}

	/** 删除点播资源 **/
	public boolean deleteOnDemandResource(List<String> resourceIds) {
		try {
			for (String resourceId : resourceIds) {
				virtualResourceDao.deleteByResourceId(resourceId);
			}
			if(resourceIds != null && resourceIds.size() > 0){
				
				List<PrivilegePO> privileges = privilegeDao.findByResourceIndentityIn(resourceIds);
				List<RolePrivilegeMap> maps = rolePrivilegeMapDao.findByResourceIdIn(resourceIds);
				
				privilegeDao.delete(privileges);
				rolePrivilegeMapDao.delete(maps);
			}

		} catch (Exception e) {
			LOGGER.error("Fail to delete on-demand resources", e);
			return false;
		}
		return true;
	}

	/** 修改点播资源 **/
	public boolean updateOnDemandResource(JSONObject resourceJson) {
		try {
			String resourceId = resourceJson.getString("resourceId");

			List<VirtualResourcePO> toAddList = new ArrayList<VirtualResourcePO>();
			for (Entry<String, Object> entry : resourceJson.entrySet()) {
				toAddList.add(new VirtualResourcePO(entry.getKey(), String.valueOf(entry.getValue()), resourceId));
			}
			// 保留userId
			if (!resourceJson.containsKey("userId")) {
				toAddList.add(virtualResourceDao.findByResourceIdAndAttrName(resourceId, "userId"));
			}
			// 保留folderId
			if (!resourceJson.containsKey("folderId")) {
				VirtualResourcePO folderIdResourcePO = virtualResourceDao.findByResourceIdAndAttrName(resourceId, "folderId");
				if (null != folderIdResourcePO) {
					toAddList.add(folderIdResourcePO);
				}
			}

			// 删除旧的
			virtualResourceDao.deleteByResourceId(resourceId);

			// 保存新的
			virtualResourceDao.save(toAddList);
		} catch (Exception e) {
			LOGGER.error("Fail to update on-demand resource" + resourceJson.toJSONString(), e);
			return false;
		}

		return true;
	}

	/** 查询某种类型的所有bundle信息 **/
	public List<BundleBody> querybundleInfos(String device_modle) {
		List<BundleBody> bundles = new ArrayList<BundleBody>();
		try {
			List<BundlePO> bundlePOs = bundleService.findByDeviceModel(device_modle);
			for (BundlePO bundlePO : bundlePOs) {
				BundleBody bundle = getBundleBodyFromPO(bundlePO);
				List<ExtraInfoPO> extraInfoPOs = extraInfoService.findByBundleId(bundlePO.getBundleId());
				if (!extraInfoPOs.isEmpty()) {
					JSONObject extraInfo = new JSONObject();
					for (ExtraInfoPO extraInfoPO : extraInfoPOs) {
						extraInfo.put(extraInfoPO.getName(), extraInfoPO.getValue());
					}
					bundle.setExtra_info(extraInfo);
				}
				bundles.add(bundle);
			}

		} catch (Exception e) {
			LOGGER.error(e.toString());
		}

		return bundles;
	}

	/** 根据用户名查询用户信息 **/
	public UserBO queryUserInfoByUsername(String username) {
		try {
//			Map<String, UserBO> userMap = userFeign.queryUserInfo(username);
//			if (null == userMap || userMap.isEmpty()) {
//				return null;
//			}
//
//			return userMap.get("user");
			
			return userQueryService.queryUserByUserName(username);
		} catch (Exception e) {
			LOGGER.error("", e);
			return null;
		}
	}

	/**
	 * 查询 所有ldap同步的用户未绑定的encoders、decoders 以及用户绑定的encoders、decoders
	 * 加上所有bvc系统内的用户未绑定的encoders、decoders以及 用户绑定的encoders、decoders
	 */
	public JSONObject queryEncodersAndEncoders() {
		JSONObject resultJson = new JSONObject();
		try {
			// 分别查出本地和ldap的编码器ID和解码器ID
			List<String> localEncoderIds = bundleDao.findEncoderIdsLocal();
			List<String> localDecoderIds = bundleDao.findDecoderIdsLocal();
			List<String> ldapEncoderIds = bundleDao.findEncoderIdsFromLdap();
			List<String> ldapDecoderIds = bundleDao.findDecoderIdsFromLdap();

			JSONObject localJson = new JSONObject();
			JSONObject ldapJson = new JSONObject();
			resultJson.put("ldap", ldapJson);
			resultJson.put("local", localJson);

			// 处理被用户绑定的编码器和解码器ID
			Map<String, List<UserBO>> userResultMap = userFeign.queryUserWithEncoderDecoder();
			if (null != userResultMap) {
				List<UserBO> users = userResultMap.get("users");
				if (!CollectionUtils.isEmpty(users)) {
					List<JSONObject> localUsers = new ArrayList<JSONObject>();
					List<JSONObject> ldapUsers = new ArrayList<JSONObject>();
					localJson.put("users", localUsers);
					ldapJson.put("users", ldapUsers);
					for (UserBO userBO : users) {
						JSONObject userJson = new JSONObject();
						BundlePO encoder = bundleDao.findByBundleId(userBO.getEncoderId());
						if (null == encoder) {
							continue;
						}
						userJson.put("encoder", createCoderJson(encoder.getUsername(), encoder.getOnlinePassword(), encoder.getBundleId()));
						BundlePO decoder = bundleDao.findByBundleId(userBO.getDecoderId());
						if (null == decoder) {
							continue;
						}
						userJson.put("decoder", createCoderJson(decoder.getUsername(), decoder.getOnlinePassword(), decoder.getBundleId()));
						if ("ldap".equalsIgnoreCase(userBO.getCreater())/* SOURCE_TYPE.EXTERNAL.equals(encoder.getSourceType()) */) {
							userJson.put("name", userBO.getUserNo());
							ldapUsers.add(userJson);
							// 从ldap编解码器集合中删除已被用户绑定的
							ldapEncoderIds.remove(userBO.getEncoderId());
							ldapDecoderIds.remove(userBO.getDecoderId());
						} else {
							userJson.put("name", userBO.getName());
							userJson.put("number", userBO.getUserNo());
							localUsers.add(userJson);
							// 从local编解码器集合中删除已被用户绑定的
							localEncoderIds.remove(userBO.getEncoderId());
							localDecoderIds.remove(userBO.getDecoderId());
						}
					}
				}
			}

			// 处理未被绑定的编码器解码器
			List<JSONObject> localEncoderJsons = new ArrayList<JSONObject>();
			localJson.put("encoders", localEncoderJsons);
			List<JSONObject> localDecoderJsons = new ArrayList<JSONObject>();
			localJson.put("decoders", localDecoderJsons);
			List<JSONObject> ldapEncoderJsons = new ArrayList<JSONObject>();
			ldapJson.put("encoders", ldapEncoderJsons);
			List<JSONObject> ldapDecoderJsons = new ArrayList<JSONObject>();
			ldapJson.put("decoders", ldapDecoderJsons);
			if (localEncoderIds.size() > 0) {
				List<BundlePO> localEncoders = bundleDao.findInBundleIds(localEncoderIds);
				for (BundlePO localEncoder : localEncoders) {
					localEncoderJsons.add(createCoderJson(localEncoder.getUsername(), localEncoder.getOnlinePassword(), localEncoder.getBundleId()));
				}
			}
			if (localDecoderIds.size() > 0) {
				List<BundlePO> localDecoders = bundleDao.findInBundleIds(localDecoderIds);
				for (BundlePO localDecoder : localDecoders) {
					localDecoderJsons.add(createCoderJson(localDecoder.getUsername(), localDecoder.getOnlinePassword(), localDecoder.getBundleId()));
				}
			}
			if (ldapEncoderIds.size() > 0) {
				List<BundlePO> ldapEncoders = bundleDao.findInBundleIds(ldapEncoderIds);
				for (BundlePO ldapEncoder : ldapEncoders) {
					ldapEncoderJsons.add(createCoderJson(ldapEncoder.getUsername(), ldapEncoder.getOnlinePassword(), null));
				}
			}
			if (ldapDecoderIds.size() > 0) {
				List<BundlePO> ldapDecoders = bundleDao.findInBundleIds(ldapDecoderIds);
				for (BundlePO ldapDecoder : ldapDecoders) {
					ldapDecoderJsons.add(createCoderJson(ldapDecoder.getUsername(), ldapDecoder.getOnlinePassword(), null));
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}

		return resultJson;
	}

	/** 根据用户号码查询ldap同步的用户信息 **/
	public UserBO queryLdapUserByUserNo(String userNo) {
		try {
			Map<String, UserBO> userMap = userFeign.queryLdapUserByUserNo(userNo);
			if (null == userMap || userMap.isEmpty()) {
				return null;
			}

			return userMap.get("user");
		} catch (Exception e) {
			LOGGER.error("", e);
			return null;
		}
	}

	/** 查询ldap同步的并且绑定编解码器的所有用户 **/
	public List<UserBO> queryLdapUserWithEncoderDecoder() {
		try {
			Map<String, List<UserBO>> userMap = userFeign.queryLdapUserWithEncoderDecoder();
			if (null == userMap) {
				return null;
			}

			return userMap.get("users");
		} catch (Exception e) {
			LOGGER.error("", e);
			return null;
		}
	}

	// wjw接口 绑定虚拟设备
	// 抛出异常
	public BundlePO bindVirtualDev(String bindId) throws Exception {

		if (StringUtils.isEmpty(bindId)) {
			Exception paramException = new Exception("param error");
			throw paramException;
		}

		List<BundlePO> bundlePOs = bundleDao.findByBundleTypeAndExtraBindId("VenusVirtual", bindId);
		LOGGER.info("findByBundleTypeAndExtraBindId result, bundlePOs.size=" + bundlePOs.size() + " , bundlePOs=" + JSONObject.toJSONString(bundlePOs));

		if (!CollectionUtils.isEmpty(bundlePOs)) {
			return bundlePOs.get(0);

		} else {
			// TODO 会不会影响效率？
			synchronized (this) {
				List<BundlePO> bundlePOTemp = bundleDao.findByBundleTypeAndExtraBindIdIsNull("VenusVirtual");
				// 随机选择一个
				BundlePO bundlePO = bundlePOTemp.get((int) (Math.random() * bundlePOTemp.size()));
				bundlePO.setExtraBindId(bindId);
				bundleDao.save(bundlePO);

				return bundlePO;
			}
		}
	}

	// wjw接口 解绑虚拟设备
	// 抛出异常
	public void unBindVirtualDev(String bundleId) throws Exception {
		if (StringUtils.isEmpty(bundleId)) {
			Exception paramException = new Exception("param error");
			throw paramException;
		}

		BundlePO bundlePO = bundleDao.findByBundleId(bundleId);
		if (bundlePO == null)
			return;
		// 字段置null，方便查询？
		bundlePO.setExtraBindId(null);
		bundleDao.save(bundlePO);
	}

	/** wjw接口：根据设备名称和接入ID创建虚拟设备 */
	public BundlePO createVirtualDev(String deviceName, String accessLayerId) {
		// check param
		if (StringUtils.isEmpty(deviceName)) {
			return null;
		}

		try {
			BundlePO bundlePO = new BundlePO();
			bundlePO.setBundleName(deviceName);
			bundlePO.setBundleId(BundlePO.createBundleId());
			bundlePO.setDeviceModel("virtual");
			bundlePO.setBundleType("VenusVirtual");
			bundlePO.setAccessNodeUid(accessLayerId);
			bundleDao.save(bundlePO);

			// 配置能力通道
			bundleService.configDefaultAbility(bundlePO);
			return bundlePO;
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return null;
	}

	/** wjw接口：根据bundleId删除虚拟设备 */
	public void deleteVirtualDev(String bundleId) {
		if (StringUtils.isEmpty(bundleId)) {
			return;
		}

		try {
			BundlePO bundlePO = bundleDao.findByBundleId(bundleId);
			if (null == bundlePO) {
				return;
			}

			bundleDao.delete(bundlePO);

			// 删除通道配置和可能存在的通道参数
			channelSchemeService.deleteByBundleId(bundleId);
			lockChannelParamDao.deleteByBundleId(bundleId);

			// 删除可能存在的bundle锁定参数
			lockBundleParamDao.deleteByBundleId(bundleId);

		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	private JSONObject createCoderJson(String username, String pwd, String bundleId) {
		JSONObject json = new JSONObject();
		json.put("user", username);
		json.put("pwd", pwd);
		json.put("bundleid", bundleId);
		return json;
	}

	private Set<String> queryBundleIds(String condition) {
		Set<String> bundleIdSet = bundleDao.queryAllBundleId();
		if (null != condition && condition.isEmpty()) {
			JSONObject conditionJson = JSONObject.parseObject(condition);
			for (Entry<String, Object> entry : conditionJson.entrySet()) {
				String conditionValue = String.valueOf(entry.getValue());
				if (entry.getKey().equals("deviceModel")) {
					bundleIdSet.retainAll(bundleService.queryBundleIdByDeviceModel(conditionValue));
				} else if (entry.getKey().equals("bundleId")) {
					BundlePO bundle = bundleService.findByBundleId(conditionValue);
					if (null == bundle || !bundleIdSet.contains(bundle.getBundleId())) {
						return null;
					}
					bundleIdSet.clear();
					bundleIdSet.add(bundle.getBundleId());
				} else if (entry.getKey().equals("accessNodeUid")) {
					bundleIdSet.retainAll(bundleService.queryBundleIdByAccessNodeUid(conditionValue));
				} else if (entry.getKey().equals("userId")) {
					// 根据用户权限关系查询资源
					Long userId = Long.valueOf(conditionValue);
					List<BundlePO> userBundles = bundleService.queryByUserId(userId);
					Set<String> userBundleIds = new HashSet<String>();
					for (BundlePO userBundle : userBundles) {
						userBundleIds.add(userBundle.getBundleId());
					}
					bundleIdSet.retainAll(userBundleIds);
				} else {// 动态属性
					bundleIdSet.retainAll(extraInfoService.queryBundleIdByNameAndValue(entry.getKey(), conditionValue));
				}

				if (bundleIdSet.isEmpty()) {
					return null;
				}
			}
		}

		return bundleIdSet;
	}

	private BundleBody getBundleBodyFromPO(BundlePO po) {
		BundleBody bundle = new BundleBody();
		bundle.setBundle_id(po.getBundleId());
		bundle.setDevice_model(po.getDeviceModel());
		bundle.setBundle_type(po.getBundleType());
		bundle.setBundle_name(po.getBundleName());
		bundle.setFolderId(po.getFolderId());
		bundle.setUsername(po.getUsername());
		bundle.setPassword(po.getOnlinePassword());
		bundle.setAccess_node_uid(po.getAccessNodeUid());
		return bundle;
	}

	private List<ScreenBO> getScreens(String deviceModel) {
		List<ScreenBO> screens = new ArrayList<ScreenBO>();

		Set<String> screenIds = screenRectTemplateDao.findScreenIdsByDeviceModel(deviceModel);
		for (String screenId : screenIds) {
			ScreenBO screenBO = new ScreenBO();
			screenBO.setScreen_id(screenId);
			screenBO.setRects(new ArrayList<RectBO>());
			List<ScreenRectTemplatePO> screenRectTemplatePOs = screenRectTemplateDao.findByScreenId(screenId);
			for (ScreenRectTemplatePO screenRectTemplatePO : screenRectTemplatePOs) {
				RectBO rectBO = new RectBO();
				rectBO.setRect_id(screenRectTemplatePO.getRectId());
				rectBO.setChannel(Arrays.asList(screenRectTemplatePO.getChannel().split(",")));
				rectBO.setParam(JSONObject.parseObject(screenRectTemplatePO.getParam()));
				screenBO.getRects().add(rectBO);
			}
			screens.add(screenBO);
		}

		return screens;
	}

	private ChannelBody getChannelBodyFromPO(ChannelSchemePO po) {
		ChannelBody channelBody = new ChannelBody();
		ChannelTemplatePO channelTemplate = channelTemplateService.get(po.getChannelTemplateID());
		channelBody.setBundle_id(po.getBundleId());
		channelBody.setChannel_id(po.getChannelId());
		channelBody.setChannel_name(po.getChannelName());
		channelBody.setChannel_state(po.getChannelStatus().toString());
		channelBody.setBase_type(channelTemplate.getBaseType());
		channelBody.setExtern_type(channelTemplate.getExternType());
		return channelBody;
	}
	
	/**
	 * 根据uuid标识查询passby<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月6日 下午1:17:34
	 * @param List<LianwangPassbyPO> all 所有passby
	 * @param String uuid 业务标识
	 * @return List<LianwangPassbyPO> 标识所属passby
	 */
	private List<LianwangPassbyPO> queryByUuid(List<LianwangPassbyPO> all, String uuid) throws Exception {
		
		List<LianwangPassbyPO> passbyPOs = new ArrayList<LianwangPassbyPO>();
		if(all != null && all.size() > 0){
			for(LianwangPassbyPO passby: all){
				if(passby.getUuid().equals(uuid)){
					passbyPOs.add(passby);
				}
			}
		}
		
		return passbyPOs;
	}
	
}
