package com.suma.venus.resource.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.BundlePrivilegeBO;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.base.bo.UnbindResouceBO;
import com.suma.venus.resource.base.bo.UnbindRolePrivilegeBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.base.bo.UserresPrivilegeBO;
import com.suma.venus.resource.bo.PrivilegeStatusBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.dao.RolePrivilegeMapDAO;
import com.suma.venus.resource.dao.SerInfoDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.dao.SerNodeRolePermissionDAO;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.lianwang.auth.AuthNotifyXml;
import com.suma.venus.resource.lianwang.auth.AuthXmlUtil;
import com.suma.venus.resource.lianwang.auth.DevAuthXml;
import com.suma.venus.resource.lianwang.auth.UserAuthXml;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.suma.venus.resource.pojo.RolePrivilegeMap;
import com.suma.venus.resource.pojo.SerInfoPO;
import com.suma.venus.resource.pojo.SerInfoPO.SerInfoType;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.SerNodeRolePermissionPO;
import com.suma.venus.resource.pojo.VirtualResourcePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.OperationLogService;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.UserQueryService;
import com.suma.venus.resource.service.VirtualResourceService;
import com.suma.venus.resource.util.XMLBeanUtils;
import com.suma.venus.resource.vo.BundleVO;
import com.suma.venus.resource.vo.ChannelSchemeVO;
import com.suma.venus.resource.vo.FolderVO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceFeign;
import com.sumavision.bvc.device.monitor.live.device.UserBundleBO;
import com.sumavision.tetris.alarm.bo.OprlogParamBO.EOprlogType;
import com.sumavision.tetris.bvc.business.dispatch.TetrisDispatchService;
import com.sumavision.tetris.bvc.business.dispatch.bo.PassByBO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

@Controller
@RequestMapping("/resource")
public class BindResourceController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(BindResourceController.class);

	private final String BIND_TYPE_BINDED = "binded";
	private final String BIND_TYPE_UNBINDED = "unbinded";

	@Autowired
	private BundleService bundleService;

	@Autowired
	private VirtualResourceService virtualResourceService;

	@Autowired
	private UserQueryFeign userFeign;

	@Autowired
	private AuthXmlUtil authXmlUtil;

	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private PrivilegeDAO privilegeDao;
	
	@Autowired
	private RolePrivilegeMapDAO rolePrivilegeMapDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private UserQueryService userQueryService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private SerNodeDao serNodeDao;
	
	@Autowired
	private SerInfoDao serInfoDao;
	
	@Autowired
	private TetrisDispatchService tetrisDispatchService;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private WorkNodeDao workNodeDao;
	
	@Autowired
	private SerNodeRolePermissionDAO serNodeRolePermissionDAO;
	
	@Autowired
	private ChannelSchemeDao channelSchemeDao;
	
	@Autowired
	private MonitorLiveDeviceFeign monitorLiveDeviceFeign;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private OperationLogService operationLogService;
	
	
	@RequestMapping(value = "/getAllUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllUser() {
		Map<String, Object> data = makeAjaxData();
		try {
			List<UserBO> users = new ArrayList<UserBO>();
			users = userQueryService.queryAllUserBaseInfo(null);
			data.put("users", users);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "查询用户错误");
		}

		return data;
	}

	@RequestMapping(value = "/queryBundlesOfRole", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryBundles(Long roleId, String bindType, String deviceModel, String keyword, String showAutoCreate, Long folderId, int pageNum, int countPerPage) {
		boolean showAutoCreateBoolean = false;

		if (!StringUtils.isEmpty(showAutoCreate) && showAutoCreate.equals("true")) {
			showAutoCreateBoolean = true;
		}
		
		try {
			if (BIND_TYPE_UNBINDED.equals(bindType)) {// 只查未绑定的资源
				List<BundlePrivilegeBO> bundlePrivileges = getUnbindedBundles(queryUnbindedBundleIds(roleId, deviceModel, keyword));
				return bundlePageResponse(pageNum, countPerPage, bundlePrivileges);
			} else if (BIND_TYPE_BINDED.equals(bindType)) {// 只查已绑定的资源
				List<BundlePrivilegeBO> bundlePrivileges = getBindedBundles(roleId, deviceModel, keyword);
				return bundlePageResponse(pageNum, countPerPage, bundlePrivileges);
			} else {// 绑定和未绑定都查
				List<BundlePrivilegeBO> bundlePrivileges = getBundles(roleId, deviceModel, keyword, folderId);
				return bundlePageResponse(pageNum, countPerPage, bundlePrivileges);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			Map<String, Object> data = makeAjaxData();
			data.put(ERRMSG, "查询错误");
			return data;
		}
	}

	@RequestMapping(value = "/queryUsersOfRole", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryUsersOfRole(Long roleId, String keyword, Long folderId, int pageNum, int countPerPage) {
		try {
			return userPageResponse(pageNum, countPerPage, getUsers(roleId, keyword, folderId));
		} catch (Exception e) {
			LOGGER.error(e.toString());
			Map<String, Object> data = makeAjaxData();
			data.put(ERRMSG, "查询错误");
			return data;
		}
	}

	private Map<String, Object> bundlePageResponse(int pageNum, int countPerPage, List<BundlePrivilegeBO> bundlePrivileges) {
		Map<String, Object> data = makeAjaxData();
		if (null != bundlePrivileges) {
			// 排序
			Collections.sort(bundlePrivileges, new Comparator<BundlePrivilegeBO>() {
				@Override
				public int compare(BundlePrivilegeBO o1, BundlePrivilegeBO o2) {
					return (int) (o2.getId() - o1.getId());
				}
			});
			int from = (pageNum - 1) * countPerPage;
			int to = (pageNum * countPerPage > bundlePrivileges.size()) ? bundlePrivileges.size() : pageNum * countPerPage;
			data.put("resources", bundlePrivileges.subList(from, to));
			data.put("total", bundlePrivileges.size());
			return data;
		} else {
			data.put("resources", new ArrayList<BundlePrivilegeBO>());
			data.put("total", 0);
			return data;
		}
	}

	private Map<String, Object> userPageResponse(int pageNum, int countPerPage, List<UserresPrivilegeBO> userresPrivileges) {
		Map<String, Object> data = makeAjaxData();
		if (null != userresPrivileges) {
			// 排序
			Collections.sort(userresPrivileges, new Comparator<UserresPrivilegeBO>() {
				@Override
				public int compare(UserresPrivilegeBO o1, UserresPrivilegeBO o2) {
					return (int) (o2.getId() - o1.getId());
				}
			});
			int from = (pageNum - 1) * countPerPage;
			int to = (pageNum * countPerPage > userresPrivileges.size()) ? userresPrivileges.size() : pageNum * countPerPage;
			data.put("users", userresPrivileges.subList(from, to));
			data.put("total", userresPrivileges.size());
			return data;
		} else {
			data.put("users", new ArrayList<BundlePrivilegeBO>());
			data.put("total", 0);
			return data;
		}
	}

	private List<BundlePrivilegeBO> getUnbindedBundles(Set<String> bundleIds) throws Exception {
		List<BundlePrivilegeBO> bundlePrivileges = new ArrayList<BundlePrivilegeBO>();
		if (null != bundleIds) {
			for (String bundleId : bundleIds) {
				BundlePO po = bundleService.findByBundleId(bundleId);
				bundlePrivileges.add(getBundlePrivilegefromPO(po));
			}
		}

		return bundlePrivileges;
	}

	private Set<String> queryUnbindedBundleIds(Long roleId, String deviceModel, String keyword) throws Exception {
		Set<String> bundleIds = bundleService.queryBundleIdSetByMultiParams(deviceModel, null, keyword, null);
		if (bundleIds.isEmpty()) {
			return null;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			List<String> bindResourceCodes = filterBindedBundleIds(bindResourceIdBO.getResourceCodes());
			bundleIds.removeAll(bindResourceCodes);
		}
		return bundleIds;
	}

	// 过滤出已绑定的bundleId
	private List<String> filterBindedBundleIds(List<String> bindResourceCodes) {
		List<String> resultList = new ArrayList<String>();
		for (String bindResourceCode : bindResourceCodes) {
			if (bindResourceCode.endsWith("-r") || bindResourceCode.endsWith("-w")) {
				resultList.add(bindResourceCode.substring(0, bindResourceCode.length() - 2));
			}
		}
		return resultList;
	}

	/** 查询角色具有权限的符合查询条件的bundle资源 */
	private List<BundlePrivilegeBO> getBindedBundles(Long roleId, String deviceModel, String keyword) throws Exception {
		List<BundlePrivilegeBO> bundlePrivileges = new ArrayList<BundlePrivilegeBO>();
		Set<String> bundleIds = bundleService.queryBundleIdSetByMultiParams(deviceModel, null, keyword, null);
		if (bundleIds.isEmpty()) {
			return bundlePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (String bundleId : bundleIds) {
				boolean hasReadPrivilege = privilegeCodes.contains(bundleId + "-r");
				boolean hasWritePrivilege = privilegeCodes.contains(bundleId + "-w");
				boolean hasCloudPrivilege = privilegeCodes.contains(bundleId + "-c");
				boolean hasLocalReadPrivilege = privilegeCodes.contains(bundleId + "-lr");
				boolean hasDownloadPrivilege = privilegeCodes.contains(bundleId + "-d");
				if (!hasReadPrivilege && !hasWritePrivilege) {
					continue;
				}
				BundlePO po = bundleService.findByBundleId(bundleId);
				BundlePrivilegeBO bundlePrivilege = getBundlePrivilegefromPO(po);
				if (hasReadPrivilege) {
					bundlePrivilege.setHasReadPrivilege(true);
				}
				if (hasWritePrivilege) {
					bundlePrivilege.setHasWritePrivilege(true);
				}
				if (hasCloudPrivilege) {
					bundlePrivilege.setHasCloudPrivilege(true);
				}
				if (hasLocalReadPrivilege) {
					bundlePrivilege.setHasLocalReadPrivilege(true);
				}
				if (hasDownloadPrivilege) {
					bundlePrivilege.setHasDownloadPrivilege(true);
				}
				bundlePrivileges.add(bundlePrivilege);
			}
		} else {
			return new ArrayList<BundlePrivilegeBO>();
		}

		return bundlePrivileges;
	}

	/** 查询所有资源，并标记其中有权限的资源 */
	private List<BundlePrivilegeBO> getBundles(Long roleId, String deviceModel, String keyword, Long folderId) throws Exception{
		List<BundlePrivilegeBO> bundlePrivileges = new ArrayList<BundlePrivilegeBO>();
		//Set<String> bundleIds = bundleService.queryBundleIdSetByMultiParams(deviceModel, null, keyword, folderId);
		//获取文件夹下的设备改为获取全部包含子文件夹
		Set<String> bundleIds = bundleService.queryBundleSetByMultiParams(deviceModel, null, keyword, folderId);
		if (bundleIds.isEmpty()) {
			return bundlePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (String bundleId : bundleIds) {
				boolean hasReadPrivilege = privilegeCodes.contains(bundleId + "-r");
				boolean hasWritePrivilege = privilegeCodes.contains(bundleId + "-w");
				boolean hasCloudPrivilege = privilegeCodes.contains(bundleId + "-c");
				boolean hasLocalReadPrivilege = privilegeCodes.contains(bundleId + "-lr");
				boolean hasDownloadPrivilege = privilegeCodes.contains(bundleId + "-d");
				BundlePO po = bundleService.findByBundleId(bundleId);
				BundlePrivilegeBO bundlePrivilege = getBundlePrivilegefromPO(po);
				if (hasReadPrivilege) {
					bundlePrivilege.setHasReadPrivilege(true);
				}
				if (hasWritePrivilege) {
					bundlePrivilege.setHasWritePrivilege(true);
				}
				if (hasCloudPrivilege) {
					bundlePrivilege.setHasCloudPrivilege(true);
				}
				if (hasLocalReadPrivilege) {
					bundlePrivilege.setHasLocalReadPrivilege(true);
				}
				if (hasDownloadPrivilege) {
					bundlePrivilege.setHasDownloadPrivilege(true);
				}
				bundlePrivileges.add(bundlePrivilege);
			}
		} else {
			for (String bundleId : bundleIds) {
				BundlePO po = bundleService.findByBundleId(bundleId);
				BundlePrivilegeBO bundlePrivilege = getBundlePrivilegefromPO(po);
				bundlePrivileges.add(bundlePrivilege);
			}
		}

		return bundlePrivileges;
	}
	
	private ResourceIdListBO queryResourceByRoleId(Long roleId) throws Exception{
		
		List<PrivilegePO> privileges = privilegeDao.findByRoleId(roleId);
		
		ResourceIdListBO bo = new ResourceIdListBO();
		bo.setResourceCodes(new ArrayList<String>());
		for(PrivilegePO privilege: privileges){
			bo.getResourceCodes().add(privilege.getResourceIndentity());
		}
		
		return bo;
		
	}
	
	/** 查询所有用户，并标记其中有权限的用户 */
	private List<UserresPrivilegeBO> getUsers(Long roleId, String keyword, Long folderId) throws Exception{
		List<UserresPrivilegeBO> userresPrivilegeBOs = new ArrayList<>();
		List<UserBO> userBOs = userQueryService.queryUserLikeUserName(keyword);
		
		List<UserBO> allUsers = new ArrayList<UserBO>();
		
		if(StringUtils.isEmpty(folderId)){
			allUsers = userBOs;
		}else{
			List<Long> userIds = new ArrayList<Long>();
			for(UserBO user: userBOs){
				userIds.add(user.getId());
			}
			
			List<FolderPO> parentFolders = folderDao.findByParentId(folderId);
			List<FolderPO> folders = folderDao.findByParentPathLike(new StringBufferWrapper().append("%")
																						 .append("/")
																						 .append(folderId)
																						 .append("/")
																						 .append("%")
																						 .toString());
			
			List<Long> folderIds = new ArrayList<Long>();
			folderIds.add(folderId);
			for(FolderPO folder: parentFolders){
				folderIds.add(folder.getId());
			}
			for(FolderPO folder: folders){
				folderIds.add(folder.getId());		
			}
			
			List<FolderUserMap> maps = folderUserMapDao.findByFolderIdInAndUserIdIn(folderIds, userIds);
			//List<FolderUserMap> maps = folderUserMapDao.findByFolderIdAndUserIdIn(folderId, userIds);
			for(UserBO user: userBOs){
				for(FolderUserMap map: maps){
					if(user.getId().equals(map.getUserId())){
						allUsers.add(user);
						break;
					}
				}
			}
		}
		
//		Map<String, List<UserBO>> resultMap = userFeign.queryUsersByNameLike(keyword)/* userFeign.queryUsers() */;
//		if (null != resultMap && null != resultMap.get("users")) {
//			allUsers = resultMap.get("users");
//		}
		if (null == allUsers || allUsers.isEmpty()) {
			return userresPrivilegeBOs;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (UserBO userBO : allUsers) {
				boolean hasReadPrivilege = privilegeCodes.contains(userBO.getUserNo() + "-r");
				boolean hasWritePrivilege = privilegeCodes.contains(userBO.getUserNo() + "-w");
				boolean hasCloudPrivilege = privilegeCodes.contains(userBO.getUserNo() + "-c");
				boolean hasLocalReadPrivilege = privilegeCodes.contains(userBO.getUserNo() + "-c");
				boolean hasDownloadPrivilege = privilegeCodes.contains(userBO.getUserNo() + "-c");
				boolean hasHJPrivilege = privilegeCodes.contains(userBO.getUserNo() + "-hj");
				boolean hasZKPrivilege = privilegeCodes.contains(userBO.getUserNo() + "-zk");
				boolean hasHYPrivilege = privilegeCodes.contains(userBO.getUserNo() + "-hy");
				UserresPrivilegeBO userresPrivilege = getUserresPrivilegeFromUserBO(userBO);
				if (hasReadPrivilege) {
					userresPrivilege.setHasReadPrivilege(true);
				}
				if (hasWritePrivilege) {
					userresPrivilege.setHasWritePrivilege(true);
				}
				if (hasCloudPrivilege) {
					userresPrivilege.setHasCloudPrivilege(true);
				}
				if (hasLocalReadPrivilege) {
					userresPrivilege.setHasLocalReadPrivilege(true);
				}
				if (hasDownloadPrivilege) {
					userresPrivilege.setHasDownloadPrivilege(true);
				}
				if (hasHJPrivilege) {
					userresPrivilege.setHasHJPrivilege(true);
				}
				if (hasZKPrivilege){
					userresPrivilege.setHasZKPrivilege(true);
				}
				if (hasHYPrivilege){
					userresPrivilege.setHasHYPrivilege(true);
				}
				userresPrivilegeBOs.add(userresPrivilege);
			}
		} else {
			for (UserBO userBO : allUsers) {
				userresPrivilegeBOs.add(getUserresPrivilegeFromUserBO(userBO));
			}
		}

		return userresPrivilegeBOs;
	}

	@RequestMapping(value = "/queryVirtualResourcesOfRole", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryVirtualResources(Long roleId, String bindType, String keyword, int pageNum, int countPerPage) {
		try {
			List<JSONObject> resourcePrivileges = null;
			if (BIND_TYPE_UNBINDED.equals(bindType)) {// 只查未绑定的资源
				resourcePrivileges = getUnbindedVirtualResources(roleId, keyword);
			} else if (BIND_TYPE_BINDED.equals(bindType)) {// 只查已绑定的资源
				resourcePrivileges = getBindedVirtualResources(roleId, keyword);
			} else {// 绑定和未绑定都查
				resourcePrivileges = getVirtualResources(roleId, keyword);
			}
			return virtualResourcePageResponse(pageNum, countPerPage, resourcePrivileges);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			Map<String, Object> data = makeAjaxData();
			data.put(ERRMSG, "查询错误");
			return data;
		}
	}

	private List<JSONObject> getVirtualResources(Long roleId, String keyword) throws Exception {
		List<JSONObject> virtualResourcePrivileges = new ArrayList<JSONObject>();
		Set<String> resourceIds = virtualResourceService.queryResourceIdByKeyword(keyword);
		if (resourceIds.isEmpty()) {
			return virtualResourcePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (String resourceId : resourceIds) {
				JSONObject resourceJson = new JSONObject();
				resourceJson.put("resourceId", resourceId);
				List<VirtualResourcePO> pos = virtualResourceService.findByResourceId(resourceId);
				for (VirtualResourcePO po : pos) {
					resourceJson.put(po.getAttrName(), po.getAttrValue());
				}
				if (privilegeCodes.contains(resourceId)) {
					resourceJson.put("hasPrivilege", true);
				} else {
					resourceJson.put("hasPrivilege", false);
				}
				virtualResourcePrivileges.add(resourceJson);
			}
		} else {
			for (String resourceId : resourceIds) {
				JSONObject resourceJson = new JSONObject();
				resourceJson.put("resourceId", resourceId);
				List<VirtualResourcePO> pos = virtualResourceService.findByResourceId(resourceId);
				for (VirtualResourcePO po : pos) {
					resourceJson.put(po.getAttrName(), po.getAttrValue());
				}
				resourceJson.put("hasPrivilege", false);
				virtualResourcePrivileges.add(resourceJson);
			}
		}

		return virtualResourcePrivileges;
	}

	/** 查询角色权限范围内的虚拟资源 */
	private List<JSONObject> getBindedVirtualResources(Long roleId, String keyword) throws Exception{
		List<JSONObject> virtualResourcePrivileges = new ArrayList<JSONObject>();
		Set<String> resourceIds = virtualResourceService.queryResourceIdByKeyword(keyword);
		if (resourceIds.isEmpty()) {
			return virtualResourcePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (String resourceId : resourceIds) {
				if (privilegeCodes.contains(resourceId)) {
					JSONObject resourceJson = new JSONObject();
					resourceJson.put("resourceId", resourceId);
					List<VirtualResourcePO> pos = virtualResourceService.findByResourceId(resourceId);
					for (VirtualResourcePO po : pos) {
						resourceJson.put(po.getAttrName(), po.getAttrValue());
					}
					resourceJson.put("hasPrivilege", true);
					virtualResourcePrivileges.add(resourceJson);
				}
			}
		}

		return virtualResourcePrivileges;
	}

	/** 查询角色权限未绑定的虚拟资源 */
	private List<JSONObject> getUnbindedVirtualResources(Long roleId, String keyword) throws Exception{
		List<JSONObject> virtualResourcePrivileges = new ArrayList<JSONObject>();
		Set<String> resourceIds = virtualResourceService.queryResourceIdByKeyword(keyword);
		if (resourceIds.isEmpty()) {
			return virtualResourcePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			resourceIds.removeAll(privilegeCodes);
		}

		for (String resourceId : resourceIds) {
			JSONObject resourceJson = new JSONObject();
			resourceJson.put("resourceId", resourceId);
			List<VirtualResourcePO> pos = virtualResourceService.findByResourceId(resourceId);
			for (VirtualResourcePO po : pos) {
				resourceJson.put(po.getAttrName(), po.getAttrValue());
			}
			resourceJson.put("hasPrivilege", false);
			virtualResourcePrivileges.add(resourceJson);
		}

		return virtualResourcePrivileges;
	}

	private Map<String, Object> virtualResourcePageResponse(int pageNum, int countPerPage, List<JSONObject> virtualResourcePrivileges) {
		Map<String, Object> data = makeAjaxData();
		if (null != virtualResourcePrivileges) {
			// 排序
			Collections.sort(virtualResourcePrivileges, new Comparator<JSONObject>() {
				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					return o2.getString("resourceId").compareTo(o1.getString("resourceId"));
				}
			});
			int from = (pageNum - 1) * countPerPage;
			int to = (pageNum * countPerPage > virtualResourcePrivileges.size()) ? virtualResourcePrivileges.size() : pageNum * countPerPage;
			data.put("resources", virtualResourcePrivileges.subList(from, to));
			data.put("total", virtualResourcePrivileges.size());
			return data;
		} else {
			data.put("resources", new ArrayList<BundlePrivilegeBO>());
			data.put("total", 0);
			return data;
		}
	}

	/** 查询虚拟资源信息 */
	@RequestMapping(value = "/queryVirtualResourceInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryVirtualResourceInfo(@RequestParam(value = "resourceId") String resourceId) {
		Map<String, Object> data = makeAjaxData();
		try {
			List<VirtualResourcePO> infos = virtualResourceService.findByResourceId(resourceId);
			data.put("infos", infos);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "查询错误");
		}

		return data;
	}

	/** 提交bundle权限绑定请求 */
	@RequestMapping(value = "/submitBundlePrivilege", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitBundlePrivilege(
			Long roleId, 
			String prevReadChecks,
			String prevWriteChecks,
			String prevCloudChecks,
			String prevLocalReadChecks, 
			String prevDownloadChecks, 
			String readChecks,
			String writeChecks, 
			String cloudChecks,
			String localReadChecks,
			String downloadChecks,
			Principal principal) {
		Map<String, Object> data = makeAjaxData();

		try {
			List<String> preReadCheckList = new ArrayList<String>();
			List<String> prevWriteCheckList = new ArrayList<String>();
			List<String> prevCloudCheckList = new ArrayList<String>();
			List<String> prevLocalReadCheckList = new ArrayList<String>();
			List<String> prevDownloadCheckList = new ArrayList<String>();
			List<String> readCheckList = new ArrayList<String>();
			List<String> writeCheckList = new ArrayList<String>();
			List<String> cloudCheckList = new ArrayList<String>();
			List<String> localReadCheckList = new ArrayList<String>();
			List<String> downloadCheckList = new ArrayList<String>();
			
			if (null != prevReadChecks && !prevReadChecks.isEmpty()) {
				preReadCheckList = Arrays.asList(prevReadChecks.split(","));
			}
			if (null != prevWriteChecks && !prevWriteChecks.isEmpty()) {
				prevWriteCheckList = Arrays.asList(prevWriteChecks.split(","));
			}
			if (null != prevCloudChecks && !prevCloudChecks.isEmpty()) {
				prevCloudCheckList = Arrays.asList(prevCloudChecks.split(","));
			}
			if (null != prevLocalReadChecks && !prevLocalReadChecks.isEmpty()) {
				prevLocalReadCheckList = Arrays.asList(prevLocalReadChecks.split(","));
			}
			if (null != prevDownloadChecks && !prevDownloadChecks.isEmpty()) {
				prevDownloadCheckList = Arrays.asList(prevDownloadChecks.split(","));
			}
			if (null != readChecks && !readChecks.isEmpty()) {
				readCheckList = Arrays.asList(readChecks.split(","));
			}
			if (null != writeChecks && !writeChecks.isEmpty()) {
				writeCheckList = Arrays.asList(writeChecks.split(","));
			}
			if (null != cloudChecks && !cloudChecks.isEmpty()) {
				cloudCheckList = Arrays.asList(cloudChecks.split(","));
			}
			if (null != localReadChecks && !localReadChecks.isEmpty()) {
				localReadCheckList = Arrays.asList(localReadChecks.split(","));
			}
			if (null != downloadChecks && !downloadChecks.isEmpty()) {
				downloadCheckList = Arrays.asList(downloadChecks.split(","));
			}

			List<String> toBindReadCheckList = getToBindPrivileges(preReadCheckList, readCheckList);
			List<String> toBindWriteCheckList = getToBindPrivileges(prevWriteCheckList, writeCheckList);
			List<String> toBindCloudCheckList = getToBindPrivileges(prevCloudCheckList, cloudCheckList);
			List<String> toBindLocalReadCheckList = getToBindPrivileges(prevLocalReadCheckList, localReadCheckList);
			List<String> toBindDownloadCheckList = getToBindPrivileges(prevDownloadCheckList, downloadCheckList);
			List<String> toBindChecks = new ArrayList<String>();
			for (String readCheck : toBindReadCheckList) {
				toBindChecks.add(readCheck + "-r");
			}
			for (String writeCheck : toBindWriteCheckList) {
				toBindChecks.add(writeCheck + "-w");
			}
			for (String cloudCheck : toBindCloudCheckList) {
				toBindChecks.add(cloudCheck + "-c");
			}
			for (String localReadCheck : toBindLocalReadCheckList) {
				toBindChecks.add(localReadCheck + "-lr");
			}
			for (String downloadCheck : toBindDownloadCheckList) {
				toBindChecks.add(downloadCheck + "-d");
			}
			if (!bindResourceCodes(roleId, toBindChecks)) {
				data.put(ERRMSG, "绑定失败");
				return data;
			}

			List<String> toUnbindReadCheckList = getToUnbindPrivileges(preReadCheckList, readCheckList);
			List<String> toUnbindWriteCheList = getToUnbindPrivileges(prevWriteCheckList, writeCheckList);
			List<String> toUnbindCloudCheList = getToUnbindPrivileges(prevCloudCheckList, cloudCheckList);
			List<String> toUnbindLocalReadListCheckList = getToUnbindPrivileges(prevLocalReadCheckList, localReadCheckList);
			List<String> toUnbindDownloadCheList = getToUnbindPrivileges(prevDownloadCheckList, downloadCheckList);
			List<String> toUnbindChecks = new ArrayList<String>();
			for (String readCheck : toUnbindReadCheckList) {
				toUnbindChecks.add(readCheck + "-r");
			}
			for (String writeCheck : toUnbindWriteCheList) {
				toUnbindChecks.add(writeCheck + "-w");
			}
			for (String cloudCheck : toUnbindCloudCheList) {
				toUnbindChecks.add(cloudCheck + "-c");
			}
			for (String localReadCheck : toUnbindLocalReadListCheckList) {
				toUnbindChecks.add(localReadCheck + "-lr");
			}
			for (String downloadCheck : toUnbindDownloadCheList) {
				toUnbindChecks.add(downloadCheck + "-d");
			}
			if (!unbindResourceCodes(roleId, toUnbindChecks)) {
				data.put(ERRMSG, "解绑失败");
				return data;
			}

			// 发送授权信息通知
			try {
//				List<UserBO> userBOs = userFeign.queryUsersByRole(roleId).get("users");
				List<UserBO> userBOs = userQueryService.queryUsersByRole(roleId);
				if (null != userBOs) {
					// 当前登录（操作）用户, TODO: 需要改
//					String oprusername = principal.getName();
//					UserBO oprUserBO = userFeign.queryUserInfo(oprusername).get("user");
					UserBO oprUserBO = userQueryService.current();
					Map<String, PrivilegeStatusBO> privilegeStatusMap = getPrivilegeStatusMap(preReadCheckList, prevWriteCheckList,prevCloudCheckList,prevLocalReadCheckList,prevDownloadCheckList, new ArrayList<String>(), new ArrayList<String>(),new ArrayList<String>(),
							readCheckList, writeCheckList,cloudCheckList,localReadCheckList,downloadCheckList, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
					String connectCenterLayerID = null;
					try{
						connectCenterLayerID = resourceRemoteService.queryLocalLayerId();
					}catch(Exception e){e.printStackTrace();}
					
					SerNodePO self = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
					SerInfoPO appInfo = serInfoDao.findBySerNodeAndSerType(self.getNodeUuid(), SerInfoType.APPLICATION.getNum());

					List<Long> consumeIds = new ArrayList<Long>();
					for (UserBO userBO : userBOs) {
						String appNo = serInfoDao.findByTypeAndUserNo(SerInfoType.APPLICATION.getNum(), userBO.getUserNo());
						if ("ldap".equals(userBO.getCreater())) {
							for (Entry<String, PrivilegeStatusBO> entry : privilegeStatusMap.entrySet()) {
								BundlePO bundle = bundleDao.findByBundleId(entry.getKey());
								if (null == bundle) {
									continue;
								}
								AuthNotifyXml authNotifyXml = new AuthNotifyXml();
								authNotifyXml.setAuthnodeid(self.getNodeUuid());
								authNotifyXml.setAuthuserid(oprUserBO.getUserNo());
								authNotifyXml.setUserid(userBO.getUserNo());
								authNotifyXml.setOperation(entry.getValue().getDevOprType());
								String authCode = null;
								if (!"remove".equalsIgnoreCase(authNotifyXml.getOperation())) {
									authCode = entry.getValue().getDevAuthCode();
								}
								authNotifyXml.getDevlist().add(new DevAuthXml(bundle.getUsername(), authCode));
								// 发送消息
								if (connectCenterLayerID != null) {
									JSONObject msgJson = authXmlUtil.createAuthNotifyMessage(appInfo.getSerNo(), appNo, XMLBeanUtils.beanToXml(AuthNotifyXml.class, authNotifyXml), connectCenterLayerID);
									PassByBO passByBO = JSONObject.parseObject(msgJson.toJSONString(), PassByBO.class);
									
									tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
								}
							}
						}else{
							JSONObject message = new JSONObject();
							message.put("businessType", "AuthUpdate");
							WebsocketMessageVO ws = websocketMessageService.send(userBO.getId(), message.toJSONString(), WebsocketMessageType.COMMAND);
							consumeIds.add(ws.getId());
						}
					}
					websocketMessageService.consumeAll(consumeIds);
					System.out.println("----------------------------****--------------");
				
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("", e);
			}
			
			//失去权限后停止转发
			try {
				List<UserBO> userBOs = userQueryService.queryUsersByRole(roleId);
				if (toUnbindWriteCheList != null && !toUnbindWriteCheList.isEmpty()&& userBOs != null) {
					List<String> writeUnbindChecks = new ArrayList<String>();
					for (String writeCheck : toUnbindWriteCheList) {
						writeUnbindChecks.add(writeCheck + "-w");
					}
					List<PrivilegePO> privilegePOs = privilegeDao.findByResourceIndentityIn(writeUnbindChecks);
					Set<Long> privilegeIdSet = new HashSet<Long>();
					if (privilegePOs != null && !privilegePOs.isEmpty()) {
						for (PrivilegePO privilegePO : privilegePOs) {
							privilegeIdSet.add(privilegePO.getId());
						}
					}
					Set<Long> roleIdin = new HashSet<Long>();
					List<RolePrivilegeMap> rolePrivilegeMaps = rolePrivilegeMapDao.findByPrivilegeIdIn(privilegeIdSet);
					if (rolePrivilegeMaps != null && !rolePrivilegeMaps.isEmpty()) {
						for (RolePrivilegeMap rolePrivilegeMap : rolePrivilegeMaps) {
							roleIdin.add(rolePrivilegeMap.getRoleId());
						}
					}
					if (roleIdin != null && !roleIdin.isEmpty()) {
						List<Long> roleIds = new ArrayList<Long>(roleIdin);
						List<UserBO> anotherUserBOs = userQueryService.findByRoleIdsIn(roleIds);
						userBOs.removeAll(anotherUserBOs);
					}
					if (userBOs != null && !userBOs.isEmpty()) {
						List<UserBundleBO> userBundleBOs = new ArrayList<UserBundleBO>();
						for (UserBO userBO : userBOs) {
							UserBundleBO userBundleBO = new UserBundleBO();
							userBundleBO.setUserId(userBO.getId());
							userBundleBO.setBundleIds(toUnbindWriteCheList);
							userBundleBOs.add(userBundleBO);
						}
						monitorLiveDeviceFeign.stopLiveByLosePrivilege(JSONArray.toJSONString(userBundleBOs));
					}
				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}
			
			try {
				List<SerNodeRolePermissionPO> serNodeRolePermissionPOs = serNodeRolePermissionDAO.findByRoleId(roleId);
				Set<Long> serNodeIds = new HashSet<Long>();
				if (serNodeRolePermissionPOs != null && !serNodeRolePermissionPOs.isEmpty()) {
					for (SerNodeRolePermissionPO serNodeRolePermissionPO : serNodeRolePermissionPOs) {
						serNodeIds.add(serNodeRolePermissionPO.getSerNodeId());
					}
					SerNodePO serNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
					List<SerNodePO> serNodePOs = serNodeDao.findByIdIn(serNodeIds);
					Map<String, Object> local = new HashMap<String, Object>();
					local.put("name", serNodePO.getNodeName());
					List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
					//添加设备授权
					Set<String> toBindBundleIds = new HashSet<String>();
					toBindBundleIds.addAll(toBindReadCheckList);
					toBindBundleIds.addAll(toBindWriteCheckList);
					toBindBundleIds.addAll(toBindCloudCheckList);
					toBindBundleIds.addAll(toBindLocalReadCheckList);
					toBindBundleIds.addAll(toBindDownloadCheckList);
					if (toBindBundleIds != null && !toBindBundleIds.isEmpty()) {
						Set<Long> folderIds = new HashSet<Long>();
	 					List<BundlePO> toBindBundlePOs = bundleDao.findByBundleIdIn(toBindBundleIds);
	 					List<BundleVO> bundleVOs = new ArrayList<BundleVO>();
	 					Set<Long> allFolderIds = new HashSet<Long>();
						if (toBindBundlePOs != null && !toBindBundlePOs.isEmpty()) {
							for (BundlePO bundlePO : toBindBundlePOs) {
								folderIds.add(bundlePO.getFolderId()==null? 0l:bundlePO.getFolderId());
								bundlePO.setEquipFactInfo(serNodePO.getNodeName());
								BundleVO bundleVO = BundleVO.fromPO(bundlePO);
								bundleVOs.add(bundleVO);
								allFolderIds.add(bundlePO.getFolderId()==null? 0l:bundlePO.getFolderId());
							}
						}
						
						List<FolderPO> bundleFolderPOs = folderDao.findByIdIn(folderIds);
						if(bundleFolderPOs != null && !bundleFolderPOs.isEmpty()){
							for (FolderPO folderPO : bundleFolderPOs) {
								if (null != folderPO.getParentPath() && !"".equals(folderPO.getParentPath())) {
									String[] allfolderIds = folderPO.getParentPath().split("/");
									for (int i = 1; i < allfolderIds.length; i++) {
										allFolderIds.add(Long.parseLong(allfolderIds[i]));
									}
								}
							}
						}
						List<FolderPO> allFolderPOs = folderDao.findByIdIn(allFolderIds);
						Map<Long, String> idUuidMap = new HashMap<Long, String>();
						List<FolderVO> folderVOs = new ArrayList<FolderVO>();
						if(allFolderPOs!=null && allFolderPOs.size()>0){
							for(FolderPO folderPO:allFolderPOs){
								FolderVO folderVO = FolderVO.fromFolderPO(folderPO);
								folderVOs.add(folderVO);
								idUuidMap.put(folderVO.getId(), folderVO.getUuid());
							}
							
							for(FolderVO folderVO:folderVOs){
//								folderVO.setParentId(idUuidMap.get(folderVO.getId()));
								String parentPath = folderVO.getParentPath();
								if(parentPath==null || "".equals(parentPath)) continue;
								StringBufferWrapper newParentPath = new StringBufferWrapper();
								String[] parentIds = parentPath.split("/");
								for(int i=1; i<parentIds.length; i++){
									newParentPath.append("/").append(idUuidMap.get(Long.valueOf(parentIds[i])));
								}
								folderVO.setParentPath(newParentPath.toString());
							}
							
							if (bundleVOs != null && !bundleVOs.isEmpty()) {
								for (BundleVO bundleVO : bundleVOs) {
									bundleVO.setInstitution(idUuidMap.get(bundleVO.getFolderId()));
								}
							}
						}
						List<ChannelSchemePO> channelSchemePOs = channelSchemeDao.findByBundleIdIn(toBindBundleIds);
						if (bundleVOs != null && !bundleVOs.isEmpty()) {
							for (BundleVO bundleVO : bundleVOs) {
								List<ChannelSchemeVO> inBundleVo = new ArrayList<ChannelSchemeVO>();
								if (channelSchemePOs != null && !channelSchemePOs.isEmpty()) {
									for (ChannelSchemePO channelSchemePO : channelSchemePOs) {
										ChannelSchemeVO channelSchemeVO = ChannelSchemeVO.fromPO(channelSchemePO);
										if (channelSchemeVO.getBundleId().equals(bundleVO.getBundleId())) {
											channelSchemeVO.setDeviceModel(bundleVO.getDeviceModel());
											inBundleVo.add(channelSchemeVO);
										}
									}
								}
								bundleVO.setChannels(inBundleVo);
							}
						}
						PassByBO passByBO = new PassByBO();
						Map<String, Object> pass_by_content = new HashMap<String, Object>();
						List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
						for (int i = 0; i < serNodePOs.size(); i++) {
							foreign.add(new HashMap<String, Object>());
							foreign.get(i).put("name", serNodePOs.get(i).getNodeName());
							foreign.get(i).put("institutions", folderVOs);
							foreign.get(i).put("devices", bundleVOs);
							foreign.get(i).put("bindChecks", toBindChecks);
						}
						pass_by_content.put("cmd", "devicePermissionAdd");
						pass_by_content.put("local", local);
						pass_by_content.put("foreign", foreign);
						passByBO.setPass_by_content(pass_by_content);
						if (workNodePOs != null && !workNodePOs.isEmpty()) {
							passByBO.setLayer_id(workNodePOs.get(0).getNodeUid());
						}
						tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
						System.out.println("------**发送Passby**------" + passByBO) ;
					}
					
					
					//删除设备授权
					Set<String> toUnBindBundleIds = new HashSet<String>();
					toUnBindBundleIds.addAll(toUnbindReadCheckList);
					toUnBindBundleIds.addAll(toUnbindWriteCheList);
					toUnBindBundleIds.addAll(toUnbindCloudCheList);
					toUnBindBundleIds.addAll(toUnbindLocalReadListCheckList);
					toUnBindBundleIds.addAll(toUnbindDownloadCheList);
					if (toUnBindBundleIds != null && !toUnBindBundleIds.isEmpty()) {
						List<Map<String, Object>> devices = new ArrayList<Map<String,Object>>();
						if (toUnBindBundleIds != null && !toUnBindBundleIds.isEmpty()) {
							Iterator<String> it = toUnBindBundleIds.iterator();
							 while(it.hasNext()){
								 Map<String, Object> map = new HashMap<String, Object>();
								 map.put("bundleId", it.next());
								 devices.add(map);
							}
						}
						List<SerNodeRolePermissionPO> serNodeRolePermissionPOsanother = serNodeRolePermissionDAO.findBySerNodeIdIn(serNodeIds);
						serNodeRolePermissionPOsanother.removeAll(serNodeRolePermissionPOs);
						Set<Long> uselesSerNodeIds = new HashSet<Long>(); 
						if (serNodeRolePermissionPOsanother != null && !serNodeRolePermissionPOsanother.isEmpty()) {
							for (SerNodeRolePermissionPO serNodeRolePermissionPO : serNodeRolePermissionPOsanother) {
								uselesSerNodeIds.add(serNodeRolePermissionPO.getSerNodeId());
							}
						}
						List<SerNodePO> uselessserNodePOs = serNodeDao.findByIdIn(uselesSerNodeIds);
						serNodePOs.removeAll(uselessserNodePOs);
						PassByBO passByBO = new PassByBO();
						Map<String, Object> pass_by_content = new HashMap<String, Object>();
						local.put("name", serNodePO.getNodeName());
						List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
						for (int i = 0; i < serNodePOs.size(); i++) {
							foreign.add(new HashMap<String, Object>());
							foreign.get(i).put("name", serNodePOs.get(i).getNodeName());
							foreign.get(i).put("devices", devices);
							foreign.get(i).put("unBindChecks", toUnbindChecks);
							//取消转发的设备
							foreign.get(i).put("toUnbindWriteCheList", toUnbindWriteCheList);
						}
						pass_by_content.put("cmd", "devicePermissionRemove");
						pass_by_content.put("local", local);
						pass_by_content.put("foreign", foreign);
						passByBO.setPass_by_content(pass_by_content);
						if (workNodePOs != null && !workNodePOs.isEmpty()) {
							passByBO.setLayer_id(workNodePOs.get(0).getNodeUid());
						}
						tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
						
						System.out.println("------**发送Passby**------" + passByBO) ;
					}
				}
				
				//修改授权日志
				UserVO userVO = userQuery.current();
				operationLogService.send(userVO.getUsername(), "修改权限", userVO.getUsername() + "修改了授权", EOprlogType.PRIVILEGE_CHANGE);
			} catch (Exception e) {
				LOGGER.error(e.toString());
				e.printStackTrace();
			}
			

		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "请求失败");
		}

		return data;
	}

	/** 提交用户权限绑定请求 */
	@RequestMapping(value = "/submitUserresPrivilege", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitUserresPrivilege(
			@RequestParam(value = "roleId") Long roleId,
			String prevReadChecks,
			String prevWriteChecks, 
			String prevCloudChecks,
			String prevLocalReadChecks,
			String prevDownloadChecks,
			String prevHJChecks,
			String prevZKChecks,
			String prevHYChecks, 
			String readChecks, 
			String writeChecks,
			String cloudChecks,
			String localReadChecks,
			String downloadChecks,
			String hjChecks, 
			String zkChecks, 
			String hyChecks, Principal principal) {
		Map<String, Object> data = makeAjaxData();

		try {
			List<String> preReadCheckList = new ArrayList<String>();
			List<String> prevWriteCheckList = new ArrayList<String>();
			List<String> prevCloudCheckList = new ArrayList<String>();
			List<String> prevLocalReadCheckList = new ArrayList<String>();
			List<String> prevDownloadCheckList = new ArrayList<String>();
			List<String> prevHJCheckeList = new ArrayList<String>();
			List<String> prevZKCheckeList = new ArrayList<String>();
			List<String> prevHYCheckeList = new ArrayList<String>();
			List<String> readCheckList = new ArrayList<String>();
			List<String> writeCheckList = new ArrayList<String>();
			List<String> cloudCheckList = new ArrayList<String>();
			List<String> localReadCheckList = new ArrayList<String>();
			List<String> downloadCheckList = new ArrayList<String>();
			List<String> hjCheckList = new ArrayList<String>();
			List<String> zkCheckList = new ArrayList<String>();
			List<String> hyCheckList = new ArrayList<String>();
			if (null != prevReadChecks && !prevReadChecks.isEmpty()) {
				preReadCheckList = Arrays.asList(prevReadChecks.split(","));
			}
			if (null != prevWriteChecks && !prevWriteChecks.isEmpty()) {
				prevWriteCheckList = Arrays.asList(prevWriteChecks.split(","));
			}
			if (null != prevCloudChecks && !prevCloudChecks.isEmpty()) {
				prevCloudCheckList = Arrays.asList(prevCloudChecks.split(","));
			}
			if (null != prevLocalReadChecks && !prevLocalReadChecks.isEmpty()) {
				prevLocalReadCheckList = Arrays.asList(prevLocalReadChecks.split(","));
			}
			if (null != prevDownloadChecks && !prevDownloadChecks.isEmpty()) {
				prevDownloadCheckList = Arrays.asList(prevDownloadChecks.split(","));
			}
			if (null != prevHJChecks && !prevHJChecks.isEmpty()) {
				prevHJCheckeList = Arrays.asList(prevHJChecks.split(","));
			}
			if (null != prevZKChecks && !prevZKChecks.isEmpty()) {
				prevZKCheckeList = Arrays.asList(prevZKChecks.split(","));
			}
			if (null != prevHYChecks && !prevHYChecks.isEmpty()) {
				prevHYCheckeList = Arrays.asList(prevHYChecks.split(","));
			}
			if (null != readChecks && !readChecks.isEmpty()) {
				readCheckList = Arrays.asList(readChecks.split(","));
			}
			if (null != writeChecks && !writeChecks.isEmpty()) {
				writeCheckList = Arrays.asList(writeChecks.split(","));
			}
			if (null != cloudChecks && !cloudChecks.isEmpty()) {
				cloudCheckList = Arrays.asList(cloudChecks.split(","));
			}
			if (null != localReadChecks && !localReadChecks.isEmpty()) {
				localReadCheckList = Arrays.asList(localReadChecks.split(","));
			}
			if (null != downloadChecks && !downloadChecks.isEmpty()) {
				downloadCheckList = Arrays.asList(downloadChecks.split(","));
			}
			if (null != hjChecks && !hjChecks.isEmpty()) {
				hjCheckList = Arrays.asList(hjChecks.split(","));
			}
			if (null != zkChecks && !zkChecks.isEmpty()) {
				zkCheckList = Arrays.asList(zkChecks.split(","));
			}
			if (null != hyChecks && !hyChecks.isEmpty()) {
				hyCheckList = Arrays.asList(hyChecks.split(","));
			}

			List<String> toBindReadCheckList = getToBindPrivileges(preReadCheckList, readCheckList);
			List<String> toBindWriteCheckList = getToBindPrivileges(prevWriteCheckList, writeCheckList);
			List<String> toBindCloudCheckList = getToBindPrivileges(prevCloudCheckList, cloudCheckList);
			List<String> toBindLocalReadCheckList = getToBindPrivileges(prevLocalReadCheckList, localReadCheckList);
			List<String> toBindDownloadCheckList = getToBindPrivileges(prevDownloadCheckList, downloadCheckList);
			List<String> toBindHJCheckList = getToBindPrivileges(prevHJCheckeList, hjCheckList);
			List<String> toBindZKCheckList = getToBindPrivileges(prevZKCheckeList, zkCheckList);
			List<String> toBindHYCheckList = getToBindPrivileges(prevHYCheckeList, hyCheckList);
			List<String> toBindChecks = new ArrayList<String>();
			for (String readCheck : toBindReadCheckList) {
				toBindChecks.add(readCheck + "-r");
			}
			for (String writeCheck : toBindWriteCheckList) {
				toBindChecks.add(writeCheck + "-w");
			}
			for (String cloudCheck : toBindCloudCheckList) {
				toBindChecks.add(cloudCheck + "-c");
			}
			for (String localReadCheck : toBindLocalReadCheckList) {
				toBindChecks.add(localReadCheck + "-lr");
			}
			for (String downloadCheck : toBindDownloadCheckList) {
				toBindChecks.add(downloadCheck + "-d");
			}
			for (String hjCheck : toBindHJCheckList) {
				toBindChecks.add(hjCheck + "-hj");
			}
			for (String zkCheck : toBindZKCheckList) {
				toBindChecks.add(zkCheck + "-zk");
			}
			for (String hyCheck : toBindHYCheckList) {
				toBindChecks.add(hyCheck + "-hy");
			}
			if (!bindResourceCodes(roleId, toBindChecks)) {
				data.put(ERRMSG, "绑定失败");
				return data;
			}

			List<String> toUnbindReadCheckList = getToUnbindPrivileges(preReadCheckList, readCheckList);
			List<String> toUnbindWriteCheList = getToUnbindPrivileges(prevWriteCheckList, writeCheckList);
			List<String> toUnbindCloudCheList = getToUnbindPrivileges(prevCloudCheckList, cloudCheckList);
			List<String> toUnbindLocalReadCheList = getToUnbindPrivileges(prevLocalReadCheckList, localReadCheckList);
			List<String> toUnbindDownloadCheList = getToUnbindPrivileges(prevDownloadCheckList, downloadCheckList);
			List<String> toUnbindHJCheckList = getToUnbindPrivileges(prevHJCheckeList, hjCheckList);
			List<String> toUnbindZKCheckList = getToUnbindPrivileges(prevZKCheckeList, zkCheckList);
			List<String> toUnbindHYCheckList = getToUnbindPrivileges(prevHYCheckeList, hyCheckList);
			List<String> toUnbindChecks = new ArrayList<String>();
			for (String readCheck : toUnbindReadCheckList) {
				toUnbindChecks.add(readCheck + "-r");
			}
			for (String writeCheck : toUnbindWriteCheList) {
				toUnbindChecks.add(writeCheck + "-w");
			}
			for (String cloudCheck : toUnbindCloudCheList) {
				toUnbindChecks.add(cloudCheck + "-c");
			}
			for (String localReadCheck : toUnbindLocalReadCheList) {
				toUnbindChecks.add(localReadCheck + "-lr");
			}
			for (String downloadCheck : toUnbindDownloadCheList) {
				toUnbindChecks.add(downloadCheck + "-d");
			}
			for (String hjCheck : toUnbindHJCheckList) {
				toUnbindChecks.add(hjCheck + "-hj");
			}
			for (String zkCheck : toUnbindZKCheckList) {
				toUnbindChecks.add(zkCheck + "-zk");
			}
			for (String hyCheck : toUnbindHYCheckList) {
				toUnbindChecks.add(hyCheck + "-hy");
			}
			if (!unbindResourceCodes(roleId, toUnbindChecks)) {
				data.put(ERRMSG, "解绑失败");
				return data;
			}

			// 发送授权信息通知
			try {
//				List<UserBO> userBOs = userFeign.queryUsersByRole(roleId).get("users");
				List<UserBO> userBOs = userQueryService.queryUsersByRole(roleId);
				if (null != userBOs) {
					// 当前登录（操作）用户
//					String oprusername = principal.getName();
//					UserBO oprUserBO = userFeign.queryUserInfo(oprusername).get("user");
					UserBO oprUserBO = userQueryService.current();
					Map<String, PrivilegeStatusBO> privilegeStatusMap = getPrivilegeStatusMap(preReadCheckList, prevWriteCheckList,prevCloudCheckList, prevLocalReadCheckList,prevDownloadCheckList,prevHJCheckeList, prevZKCheckeList, prevHYCheckeList,
							readCheckList, writeCheckList, cloudCheckList,localReadCheckList,downloadCheckList,hjCheckList, zkCheckList, hyCheckList);
					String connectCenterLayerID = resourceRemoteService.queryLocalLayerId();
					SerNodePO self = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
					SerInfoPO appInfo = serInfoDao.findBySerNodeAndSerType(self.getNodeUuid(), SerInfoType.APPLICATION.getNum());
					
					for (UserBO userBO : userBOs) {
						if ("ldap".equals(userBO.getCreater())) {
							String appNo = serInfoDao.findByTypeAndUserNo(SerInfoType.APPLICATION.getNum(), userBO.getUserNo());
							for (Entry<String, PrivilegeStatusBO> entry : privilegeStatusMap.entrySet()) {
								AuthNotifyXml authNotifyXml = new AuthNotifyXml();
								authNotifyXml.setAuthnodeid(self.getNodeUuid());
								authNotifyXml.setAuthuserid(oprUserBO.getUserNo());
								authNotifyXml.setUserid(userBO.getUserNo());
								authNotifyXml.setOperation(entry.getValue().getUserOprType());
								String authCode = null;
								if (!"remove".equalsIgnoreCase(authNotifyXml.getOperation())) {
									authCode = entry.getValue().getUserAuthCode();
								}
								authNotifyXml.getUserlist().add(new UserAuthXml(entry.getKey(), authCode));
								// 发送消息
								JSONObject msgJson = authXmlUtil.createAuthNotifyMessage(appInfo.getSerNo(), appNo, XMLBeanUtils.beanToXml(AuthNotifyXml.class, authNotifyXml), connectCenterLayerID);
								PassByBO passByBO = JSONObject.parseObject(msgJson.toJSONString(), PassByBO.class);
								
								tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
							}
						}
					}

				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}

		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "请求失败");
		}

		return data;
	}

	private Map<String, PrivilegeStatusBO> getPrivilegeStatusMap(
			List<String> preReadCheckList, List<String> prevWriteCheckList, List<String> prevCloudCheckList,List<String> prevLocalReadCheckList,List<String> prevDownloadCheckList, 
			List<String> prevHJCheckeList, List<String> prevZKCheckeList, List<String> prevHYCheckeList,
			List<String> readCheckList, List<String> writeCheckList,List<String> cloudCheckList,List<String> localReadCheckList,List<String> downloadCheckList,
			List<String> hjCheckList, List<String> zkCheckList, List<String> hyCheckList) {
		Map<String, PrivilegeStatusBO> privilegeStatusMap = createPrivilegeStatusMap(preReadCheckList, prevWriteCheckList, prevCloudCheckList,prevLocalReadCheckList,prevDownloadCheckList,prevHJCheckeList, prevZKCheckeList, prevHYCheckeList,
				readCheckList, writeCheckList, cloudCheckList,localReadCheckList,downloadCheckList,hjCheckList, zkCheckList, hyCheckList);
		for (Entry<String, PrivilegeStatusBO> entry : privilegeStatusMap.entrySet()) {
			if (preReadCheckList.contains(entry.getKey())) {
				entry.getValue().setPrevCanRead(true);
			}
			if (prevWriteCheckList.contains(entry.getKey())) {
				entry.getValue().setPrevCanWrite(true);
			}
			if (prevCloudCheckList.contains(entry.getKey())) {
				entry.getValue().setPrevCanCloud(true);
			}
			if (prevLocalReadCheckList.contains(entry.getKey())) {
				entry.getValue().setPrevCanLocalRead(true);
			}
			if (prevDownloadCheckList.contains(entry.getKey())) {
				entry.getValue().setPrevCanDownload(true);
			}
			if (prevHJCheckeList.contains(entry.getKey())) {
				entry.getValue().setPrevCanHJ(true);
			}
			if (prevZKCheckeList.contains(entry.getKey())) {
				entry.getValue().setPrevCanZK(true);
			}
			if (readCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanRead(true);
			}
			if (writeCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanWrite(true);
			}
			if (cloudCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanCloud(true);
			}
			if (localReadCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanLocalRead(true);
			}
			if (downloadCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanDownload(true);
			}
			if (hjCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanHJ(true);
			}
			if (zkCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanZK(true);
			}
		}
		return privilegeStatusMap;
	}

	private Map<String, PrivilegeStatusBO> createPrivilegeStatusMap(
			List<String> preReadCheckList, List<String> prevWriteCheckList, List<String> prevCloudCheckList,List<String> prevLocalReadCheckList,List<String> prevDownloadCheckList,
			List<String> prevHJCheckeList, List<String> prevZKCheckeList, List<String> prevHYCheckeList,
			List<String> readCheckList, List<String> writeCheckList,List<String> cloudCheckList,List<String> localReadCheckList,List<String> downloadCheckList, 
			List<String> hjCheckList, List<String> zkCheckList, List<String> hyCheckList) {
		Map<String, PrivilegeStatusBO> privilegeStatusMap = new HashMap<>();
		for (String code : preReadCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : prevWriteCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : prevCloudCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : prevLocalReadCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : prevDownloadCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : prevHJCheckeList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : prevZKCheckeList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : prevHYCheckeList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : readCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : writeCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : cloudCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : localReadCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : downloadCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : hjCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : zkCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : hyCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		return privilegeStatusMap;
	}

	/** 提交虚拟资源权限绑定请求 */
	@RequestMapping(value = "/submitVirtualResourcePrivilege", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitVirtualResourcePrivilege(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "prevChecks") String prevChecks,
			@RequestParam(value = "checks") String checks) {
		Map<String, Object> data = makeAjaxData();

		try {
			List<String> preCheckList = prevChecks.isEmpty() ? new ArrayList<String>() : Arrays.asList(prevChecks.split(","));
			List<String> checkList = checks.isEmpty() ? new ArrayList<String>() : Arrays.asList(checks.split(","));

			List<String> toBindCheckList = getToBindPrivileges(preCheckList, checkList);
			if (!toBindCheckList.isEmpty()) {
				if (!bindResourceCodes(roleId, toBindCheckList)) {
					data.put(ERRMSG, "绑定失败");
					return data;
				}
			}

			List<String> toUnbindCheckList = getToUnbindPrivileges(preCheckList, checkList);
			if (!toUnbindCheckList.isEmpty()) {
				if (!unbindResourceCodes(roleId, toUnbindCheckList)) {
					data.put(ERRMSG, "解绑失败");
					return data;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "请求失败");
		}

		return data;
	}

	private boolean bindResourceCodes(Long roleId, List<String> toBindChecks) throws Exception {
		if (!toBindChecks.isEmpty()) {
			RoleAndResourceIdBO bo = new RoleAndResourceIdBO();
			bo.setRoleId(roleId);
			bo.setResourceCodes(toBindChecks);
			
			return userQueryService.bindRolePrivilege(bo);
//			ResultBO result = userFeign.bindRolePrivilege(bo);
//			if (null == result || !result.isResult()) {
//				return false;
//			}
		}
		return true;
	}
	
	private boolean unbindResourceCodes(Long roleId, List<String> toUnbindCheckList) throws Exception{
		if (!toUnbindCheckList.isEmpty()) {
			UnbindRolePrivilegeBO bo = new UnbindRolePrivilegeBO();
			bo.setRoleId(roleId);
			bo.setUnbindPrivilege(new ArrayList<UnbindResouceBO>());
			for (String toUnbindCheck : toUnbindCheckList) {
				UnbindResouceBO unbind = new UnbindResouceBO();
				unbind.setResourceCode(toUnbindCheck);
				bo.getUnbindPrivilege().add(unbind);
			}
			
			return userQueryService.unbindRolePrivilege(bo);
//			ResultBO result = userFeign.unbindRolePrivilege(bo);
//			if (null == result || !result.isResult()) {
//				return false;
//			}
		}
		return true;
	}
	
	/** 获取要绑定的资源ID集合 */
	private List<String> getToBindPrivileges(List<String> prevCheckList, List<String> checkList) {
		if (prevCheckList.isEmpty()) {
			return checkList;
		}

		List<String> toBindList = new ArrayList<String>();
		for (String checkId : checkList) {
			if (!prevCheckList.contains(checkId)) {
				toBindList.add(checkId);
			}
		}
		return toBindList;
	}

	/** 获取要解绑的资源ID集合 */
	private List<String> getToUnbindPrivileges(List<String> prevCheckList, List<String> checkList) {
		if (checkList.isEmpty()) {
			return prevCheckList;
		}

		List<String> toUnbindList = new ArrayList<String>();
		for (String prevCheckId : prevCheckList) {
			if (!checkList.contains(prevCheckId)) {
				toUnbindList.add(prevCheckId);
			}
		}
		return toUnbindList;
	}

	private BundlePrivilegeBO getBundlePrivilegefromPO(BundlePO po) {
		BundlePrivilegeBO bundlePrivilege = new BundlePrivilegeBO();
		bundlePrivilege.setId(po.getId());
		bundlePrivilege.setBundleId(po.getBundleId());
		bundlePrivilege.setDeviceModel(po.getDeviceModel());
		bundlePrivilege.setName(po.getBundleName());
		bundlePrivilege.setUsername(po.getUsername());
		bundlePrivilege.setCodec(po.getCoderType()==null?null:po.getCoderType().toString());
		return bundlePrivilege;
	}

	private UserresPrivilegeBO getUserresPrivilegeFromUserBO(UserBO userBO) {
		UserresPrivilegeBO userresPrivilege = new UserresPrivilegeBO();
		userresPrivilege.setId(userBO.getId());
		userresPrivilege.setName(userBO.getName());
		userresPrivilege.setUserNo(userBO.getUserNo());
		return userresPrivilege;
	}
}
