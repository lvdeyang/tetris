package com.suma.venus.resource.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.suma.application.ldap.department.dao.LdapDepartmentDao;
import com.suma.application.ldap.department.po.LdapDepartmentPo;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.FolderService;
import com.suma.venus.resource.service.UserQueryService;
import com.suma.venus.resource.util.DepartSyncLdapUtils;
import com.suma.venus.resource.vo.BundleVO;
import com.suma.venus.resource.vo.FolderTreeVO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 分组管理controller
 * 
 * @author lxw
 *
 */
@Controller
@RequestMapping("/folder")
public class FolderManageController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(FolderManageController.class);

	private final Long BUNDLE_NODE_ID_BASE = 100000l;

	private final Long USER_NODE_ID_BASE = 800000l;

	@Autowired
	private FolderService folderService;

	@Autowired
	private BundleService bundleService;

	@Autowired
	private FolderDao folderDao;

	@Autowired
	private DepartSyncLdapUtils departSyncLdapUtils;

	@Autowired
	private UserQueryFeign userFeign;

	@Autowired
	private LdapDepartmentDao ldapDepartmentDao;

	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private UserQueryService userQueryService;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addFolder(@RequestParam("name") String name, @RequestParam("parentId") Long parentId, @RequestParam("toLdap") Boolean toLdap) {
		Map<String, Object> data = makeAjaxData();
		try {
			FolderPO folder = new FolderPO();
			folder.setName(name);
			folder.setParentId(parentId);
			FolderPO parentFolder = folderService.get(parentId);
			if ((null != toLdap && toLdap) && (null == parentFolder.getToLdap() || !parentFolder.getToLdap())) {
				data.put(ERRMSG, "添加分组失败：分组数据的上传ldap属性与父节点矛盾");
				return data;
			}

			folder.setToLdap(toLdap);
			if (null != parentFolder.getParentPath()) {
				folder.setParentPath(parentFolder.getParentPath() + "/" + parentFolder.getId());
			} else {
				folder.setParentPath("/" + parentFolder.getId());
			}
			folder.setFolderType(parentFolder.getFolderType());

			// 查询同级已有最大index
			// int maxIndex = caculateMaxIndex(parentFolder);

			// 查询同级中，子目录中最大的index值
			int maxIndex = caculateMaxfolderIndex(parentFolder, folder);

			handleNewParentFolderIndexChange(parentFolder, maxIndex + 1);

			folder.setFolderIndex(maxIndex + 1);
			folderService.save(folder);

			data.put("folder", createFolderNodeFromFolderPO(folder));
		} catch (Exception e) {
			LOGGER.error("Fail to add folder : " + name, e);
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteFolder(@RequestParam("id") Long id) {
		Map<String, Object> data = makeAjaxData();
		try {
			FolderPO folder = folderService.get(id);
			if (null == folder.getParentPath()) {
				// 根节点不能删除
				data.put(ERRMSG, "根节点不能删除");
				return data;
			}

			// 调整同级数据的index
			int oldIndex = folder.getFolderIndex();
			FolderPO parentFolder = folderDao.findOne(folder.getParentId());
			handleOldParentFolderIndexChange(parentFolder, oldIndex);

			folderService.delete(folder);

			// 从ldap删除
			if (!SOURCE_TYPE.EXTERNAL.equals(folder.getSourceType())) {
				try {
					List<LdapDepartmentPo> ldapDeparts = ldapDepartmentDao.getDepartByUuid(folder.getUuid());
					if (CollectionUtils.isEmpty(ldapDeparts)) {
						ldapDepartmentDao.remove(ldapDeparts.get(0));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (Exception e) {
			LOGGER.error("Fail to delete folder by id : " + id, e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	/**
	 * 设置设备上的分组信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "setFolderOfBundles", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setFolderOfBundles(@RequestParam("folderId") Long folderId, @RequestParam("bundleIds") String bundleIds) {
		Map<String, Object> data = makeAjaxData();
		try {
			List<FolderTreeVO> bundleNodes = new LinkedList<FolderTreeVO>();
			String[] bundleIdArr = bundleIds.split(",");
			FolderPO folderPO = folderDao.findOne(folderId);

			int maxIndex = caculateMaxIndex(folderPO);
			for (String bundleId : bundleIdArr) {
				BundlePO bundle = bundleService.findByBundleId(bundleId);
				bundle.setFolderId(folderId);
				bundle.setSyncStatus(SYNC_STATUS.ASYNC);

				maxIndex++;
				bundle.setFolderIndex(maxIndex);

				bundleService.save(bundle);
				bundleNodes.add(createBundleNode(folderId, bundle));
			}

			data.put("bundleNodes", bundleNodes);
		} catch (Exception e) {
			LOGGER.error("Fail to set folder : ", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	/**
	 * 重置设备上的分组信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "resetFolderOfBundles", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> resetFolderOfBundles(@RequestParam("bundleIds") String bundleIds) {
		Map<String, Object> data = makeAjaxData();
		try {
			String[] bundleIdArr = bundleIds.split(",");
			for (String bundleId : bundleIdArr) {
				BundlePO bundle = bundleService.findByBundleId(bundleId);

				FolderPO parentPO = folderDao.findOne(bundle.getFolderId());

				if (bundle.getFolderIndex() != null) {
					handleOldParentFolderIndexChange(parentPO, bundle.getFolderIndex());
				}

				bundle.setFolderId(null);
				bundle.setFolderIndex(null);

				bundleService.save(bundle);
			}
		} catch (Exception e) {
			LOGGER.error("Fail to reset folder : ", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	// 为用户设置分组信息
	@RequestMapping(value = "setFolderToUsers", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setFolderToUsers(@RequestParam("folderId") Long folderId, @RequestParam("users") String userIds) {
		Map<String, Object> data = makeAjaxData();
		try {
			FolderPO folderPO = folderDao.findOne(folderId);
			
			List<UserBO> users = JSONObject.parseArray(userIds, UserBO.class);

			// 如果是非上传ldap的分组，禁止绑定用户
			if (null == folderPO.getToLdap() || !folderPO.getToLdap()) {
				data.put(ERRMSG, "无法为非上传ldap的分组绑定用户数据");
				return data;
			}

			int maxIndex = caculateMaxIndex(folderPO);
			maxIndex++;

//			userFeign.setFolderToUsers(folderPO.getUuid(), usernames, maxIndex);
			folderService.setFolderToUsers(folderPO, users, Long.valueOf(maxIndex));

		} catch (Exception e) {
			LOGGER.error("Fail to set folder : ", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	// 为用户清除分组信息
	@RequestMapping(value = "resetFolderOfUsers", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> resetFolderOfUsers(@RequestParam("userIds") String userIds) {
		Map<String, Object> data = makeAjaxData();
		try {

			String[] userIdArr = userIds.split(",");
			List<Long> resetUserIds = new ArrayList<Long>();
			for(String userId: userIdArr){
				resetUserIds.add(Long.valueOf(userId));
			}
			List<FolderUserMap> maps = folderUserMapDao.findByUserIdIn(resetUserIds);
			folderUserMapDao.delete(maps);

//			for (String username : usernameArr) {
//
//				Map<String, UserBO> userBOMap = userFeign.queryUserInfo(username);
//
//				if (!CollectionUtils.isEmpty(userBOMap)) {
//					UserBO userBO = userBOMap.get("user");
//
//					if (userBO.getFolderUuid() != null && userBO.getFolderIndex() != null) {
//
//						FolderPO folderPO = folderDao.findTopByUuid(userBO.getFolderUuid());
//						if (folderPO != null) {
//							handleOldParentFolderIndexChange(folderPO, userBO.getFolderIndex());
//						}
//					}
//				}
//			}
//
//			userFeign.resetFolderOfUsers(usernames);
		} catch (Exception e) {
			LOGGER.error("Fail to set folder : ", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "modify", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modifyFolder(@RequestParam("id") Long id, @RequestParam("name") String name, @RequestParam("toLdap") Boolean toLdap) {
		Map<String, Object> data = makeAjaxData();
		try {
			FolderPO folder = folderService.get(id);

			if (null != folder.getParentPath()) {
				FolderPO parentFolder = folderService.get(folder.getParentId());
				if ((null != toLdap && toLdap) && (null == parentFolder.getToLdap() || !parentFolder.getToLdap())) {
					data.put(ERRMSG, "修改分组失败：分组数据的上传ldap属性与父节点矛盾");
					return data;
				}
			}

			folder.setName(name);
			folder.setToLdap(toLdap);
			folder.setSyncStatus(SYNC_STATUS.ASYNC);
			folderService.save(folder);
		} catch (Exception e) {
			LOGGER.error("Fail to modify folder : ", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/initTree", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> initTree() {
		Map<String, Object> data = makeAjaxData();
		try {
			List<FolderPO> rootFolders = folderService.findByParentPath(null);
			if (rootFolders.isEmpty()) {
				data.put(ERRMSG, "数据库错误：不存在根节点");
				return data;
			}
//			FolderPO rootFolderPO = rootFolders.get(0);
			List<FolderTreeVO> tree = new LinkedList<FolderTreeVO>();
			for (FolderPO rootFolderPO : rootFolders) {
				FolderTreeVO rootTreeVO = createFolderNodeFromFolderPO(rootFolderPO);
				rootTreeVO.setChildren(createChildrenTreeNodes(rootFolderPO.getId()));
				tree.add(rootTreeVO);
			}
			data.put("tree", tree);
		} catch (Exception e) {
			LOGGER.error("Fail to init folder tree", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}
	
	/**
	 * 查询文件夹树（不包含设备用户）<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月10日 下午1:47:14
	 * @return List<FolderTreeVO>
	 */
	@JsonBody
	@RequestMapping(value = "/query/tree", method = RequestMethod.POST)
	@ResponseBody
	public Object queryTree(HttpServletRequest request) throws Exception{
		
		List<FolderPO> rootFolders = folderDao.findByParentPathAndFolderType(null, null);
		if (rootFolders.isEmpty()) {
			throw new BaseException(StatusCode.ERROR, "数据库错误，没有根节点！");
		}
		List<FolderTreeVO> tree = new LinkedList<FolderTreeVO>();
		for (FolderPO rootFolderPO : rootFolders) {
			FolderTreeVO rootTreeVO = createFolderNodeFromFolderPO(rootFolderPO);
			rootTreeVO.setChildren(createFolderChildrenTreeNodes(rootFolderPO.getId()));
			tree.add(rootTreeVO);
		}
		
		return tree;
		
	}

	@RequestMapping(value = "/queryBundlesWithoutFolder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryBundlesWithoutFolder(@RequestParam("deviceModel") String deviceModel, @RequestParam("keyword") String keyword) {
		Map<String, Object> data = makeAjaxData();
		try {
			List<BundlePO> bundlePOs = bundleService.findByDeviceModelAndKeywordAndNoFolder(deviceModel, keyword);
			List<BundleVO> bundles = new ArrayList<BundleVO>();
			for (BundlePO bundlePO : bundlePOs) {
				BundleVO vo = BundleVO.fromPO(bundlePO);
				bundles.add(vo);
			}
			data.put("resources", bundles);
		} catch (Exception e) {
			LOGGER.error("Fail to query bundles without folder", e);
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}

	@RequestMapping(value = "/queryUsersWithoutFolder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryUsersWithoutFolder(@RequestParam("keyword") String keyword) {
		Map<String, Object> data = makeAjaxData();
		try {
//			List<UserBO> userBOs = userFeign.queryUsersByNameLikeAndNoFolder(keyword).get("users");
//			List<UserBO> userBOs = userFeign.queryUsersByNameLike(keyword).get("users");
			List<UserBO> userBOs = userQueryService.queryUserLikeUserName(keyword);
			List<FolderUserMap> maps = folderUserMapDao.findAll();
			List<UserBO> filterUsers = new ArrayList<UserBO>();
			for(UserBO user: userBOs){
				boolean flag = true;
				for(FolderUserMap map: maps){
					if(map.getUserId().equals(user.getId())){
						flag = false;
						break;
					}
				}
				if(flag){
					filterUsers.add(user);
				}
			}
			
			data.put("users", filterUsers);
		} catch (Exception e) {
			LOGGER.error("Fail to query bundles without folder", e);
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}

	// 查询可作为根目录选择的数据
	@RequestMapping(value = "/queryRootOptions", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryRootOptions() {
		Map<String, Object> data = makeAjaxData();
		try {
			List<FolderPO> rootOptions = folderDao.findRootOptions();
			data.put("rootOptions", rootOptions);
		} catch (Exception e) {
			LOGGER.error("Fail to query RootOptions", e);
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}

	// 设置根目录
	@RequestMapping(value = "/setRoot", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setRoot(@RequestParam("rootId") Long rootId) {
		Map<String, Object> data = makeAjaxData();
		try {
			// 将bvc内部的目录树挂到该指定的目录下
			FolderPO newFolderPO = folderDao.findOne(rootId);
			List<FolderPO> bvcRootFolders = folderDao.findBvcRootFolders();
			for (FolderPO bvcRootFolder : bvcRootFolders) {
				bvcRootFolder.setParentId(newFolderPO.getId());
				if (null != newFolderPO.getParentPath()) {
					bvcRootFolder.setParentPath(newFolderPO.getParentPath() + "/" + newFolderPO.getId());
				} else {
					bvcRootFolder.setParentPath("/" + newFolderPO.getId());
				}
				bvcRootFolder.setSyncStatus(SYNC_STATUS.ASYNC);
				folderDao.save(bvcRootFolder);
				// 递归更新bvc目录非根目录的父级路径
				recurseUpdateParentPath(bvcRootFolder);
			}

		} catch (Exception e) {
			LOGGER.error("Fail to query RootOptions", e);
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}

	// 设置根目录
	@RequestMapping(value = "/resetRootNode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> resetRoot() {
		Map<String, Object> data = makeAjaxData();
		try {
			// 重新设置bvc内部根节点为目录树的根
			List<FolderPO> bvcRootFolders = folderDao.findBvcRootFolders();

			for (FolderPO bvcRootFolder : bvcRootFolders) {

				if (bvcRootFolder.getParentId().longValue() == -1) {
					LOGGER.info("bvcroot no need to be reset");
					continue;
				}

				FolderPO oldParentFolderPO = folderDao.findOne(bvcRootFolder.getParentId());

				int oldIndex = bvcRootFolder.getFolderIndex();

				handleOldParentFolderIndexChange(oldParentFolderPO, oldIndex);

				bvcRootFolder.setParentId(-1l);
				bvcRootFolder.setFolderIndex(1);
				bvcRootFolder.setParentPath(null);
				folderDao.save(bvcRootFolder);
				recurseUpdateParentPath(bvcRootFolder);
			}

		} catch (Exception e) {
			LOGGER.error("reset root node error", e);
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}

	@RequestMapping(value = "/changeNodePosition", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeNodePosition(@RequestParam("nodeId") String nodeId, @RequestParam("nodeType") String nodeType, @RequestParam("changeType") String changeType,
			@RequestParam("newTargetId") String newTargetId, @RequestParam("newTargetType") String newTargetType, @RequestParam("newTargetIndex") Integer newTargetIndex,
			@RequestParam(value = "newTargetParentId", required = false) Long newTargetParentId) {

		LOGGER.info("changeNodePosition nodeId=" + nodeId + ", nodeType=" + nodeType + ", changeType=" + changeType + ", newTargetId=" + newTargetId + ", newTargetIndex="
				+ newTargetIndex + ", newTargetParentId=" + newTargetParentId + ", newTargetType=" + newTargetType);

		Map<String, Object> data = makeAjaxData();

		if (StringUtils.isEmpty(nodeId) || StringUtils.isEmpty(nodeType) || StringUtils.isEmpty(changeType) || changeType.equals("none") || newTargetId == null) {

			data.put(ERRMSG, "参数");
			return data;
		}

		try {
			// 拖拽的节点，可能是目录，也可能是终端或者用户
			FolderPO changeFolderPO = null;
			// BundlePO changeBundlePO = null;
			// UserBO changeUserBO = null;

			FolderPO oldParentPO = null;
			FolderPO newParentPO = null;

			int oldIndex = 0;
			int newIndex = 0;
			boolean rootFlag = false;

			if (nodeType.equals("FOLDER")) {

				changeFolderPO = folderDao.findOne(Long.valueOf(nodeId));

				if (changeFolderPO.getParentId().longValue() == -1) {
					rootFlag = true;
				}

				if (!rootFlag) {
					oldParentPO = folderDao.findOne(changeFolderPO.getParentId());
				}

				oldIndex = changeFolderPO.getFolderIndex();

				if (changeType.equals("inner")) {

					newParentPO = folderDao.findOne(Long.valueOf(newTargetId));
					changeFolderPO.setParentId(Long.valueOf(newTargetId));

					// int maxIndex = caculateMaxIndex(newParentPO);

					int maxIndex = caculateMaxfolderIndex(newParentPO, changeFolderPO);

					newIndex = maxIndex + 1;

				} else if (changeType.equals("before")) {

					newParentPO = folderDao.findOne(newTargetParentId);
					changeFolderPO.setParentId(newTargetParentId);

					if (newTargetType.equals("USER") || newTargetType.equals("BUNDLE")) {
						int maxIndex = caculateMaxfolderIndex(newParentPO, changeFolderPO);
						newIndex = maxIndex + 1;
						System.out.println("before maxIndex=" + maxIndex);
					} else {
						newIndex = newTargetIndex;

					}

				} else if (changeType.equals("after")) {

					newParentPO = folderDao.findOne(newTargetParentId);
					changeFolderPO.setParentId(newTargetParentId);
					if (newTargetType.equals("USER") || newTargetType.equals("BUNDLE")) {
						int maxIndex = caculateMaxfolderIndex(newParentPO, changeFolderPO);
						newIndex = maxIndex + 1;
						System.out.println("after maxIndex=" + maxIndex);
					} else {
						newIndex = newTargetIndex;
					}

				}

				// 原有文件夹下的所有index处理
				if (!rootFlag) {
					handleOldParentFolderIndexChange(oldParentPO, oldIndex);
				}

				// 新目录下需要处理的index的处理 区分before和after
				handleNewParentFolderIndexChange(newParentPO, newIndex);

				if (null != newParentPO.getParentPath()) {
					changeFolderPO.setParentPath(newParentPO.getParentPath() + "/" + newParentPO.getId());
				} else {
					changeFolderPO.setParentPath("/" + newParentPO.getId());
				}

				changeFolderPO.setFolderIndex(newIndex);
				changeFolderPO.setSyncStatus(SYNC_STATUS.ASYNC);
				folderDao.save(changeFolderPO);

				recurseUpdateParentPath(changeFolderPO);

			}

			/*
			 * else if (nodeType.equals("BUNDLE")) {
			 * 
			 * 
			 * changeBundlePO = bundleService.findByBundleId(nodeId);
			 * 
			 * oldParentPO = folderDao.findOne(changeBundlePO.getFolderId()); oldIndex =
			 * changeBundlePO.getFolderIndex();
			 * 
			 * if (changeType.equals("inner")) {
			 * 
			 * newParentPO = folderDao.findOne(Long.valueOf(newTargetId)); int maxIndex =
			 * caculateMaxIndex(newParentPO); newIndex = maxIndex + 1;
			 * changeBundlePO.setFolderId(Long.valueOf(newTargetId));
			 * 
			 * // 原有文件夹下的所有index处理 handleOldParentFolderIndexChange(oldParentPO, oldIndex);
			 * 
			 * } else if (changeType.equals("before")) {
			 * 
			 * newParentPO = folderDao.findOne(newTargetParentId);
			 * changeBundlePO.setFolderId(newTargetParentId);
			 * 
			 * newIndex = newTargetIndex;
			 * 
			 * // 原有文件夹下的所有index处理 handleOldParentFolderIndexChange(oldParentPO, oldIndex);
			 * 
			 * // 新目录下需要处理的index的处理 区分before和after
			 * handleNewParentFolderIndexChange(newParentPO, newIndex);
			 * 
			 * } else if (changeType.equals("after")) {
			 * 
			 * newParentPO = folderDao.findOne(newTargetParentId);
			 * changeBundlePO.setFolderId(newTargetParentId); newIndex = newTargetIndex + 1;
			 * 
			 * // 原有文件夹下的所有index处理 handleOldParentFolderIndexChange(oldParentPO, oldIndex);
			 * 
			 * // 新目录下需要处理的index的处理 区分before和after
			 * handleNewParentFolderIndexChange(newParentPO, newIndex);
			 * 
			 * }
			 * 
			 * changeBundlePO.setFolderIndex(newIndex);
			 * changeBundlePO.setSyncStatus(SYNC_STATUS.ASYNC);
			 * bundleService.save(changeBundlePO);
			 * 
			 * } else if (nodeType.equals("USER")) { //此分支暂时不用
			 * 
			 * changeUserBO = userFeign.queryUserInfo(nodeId).get("user");
			 * 
			 * oldParentPO = folderDao.findTopByUuid(changeUserBO.getFolderUuid()); oldIndex
			 * = changeUserBO.getFolderIndex();
			 * 
			 * if (changeType.equals("inner")) {
			 * 
			 * newParentPO = folderDao.findOne(Long.valueOf(newTargetId)); int maxIndex =
			 * caculateMaxIndex(newParentPO); newIndex = maxIndex + 1;
			 * 
			 * // 原有文件夹下的所有index处理 handleOldParentFolderIndexChange(oldParentPO, oldIndex);
			 * 
			 * } else if (changeType.equals("before")) {
			 * 
			 * newParentPO = folderDao.findOne(newTargetParentId); newIndex =
			 * newTargetIndex;
			 * 
			 * // 原有文件夹下的所有index处理 handleOldParentFolderIndexChange(oldParentPO, oldIndex);
			 * 
			 * // 新目录下需要处理的index的处理 区分before和after
			 * handleNewParentFolderIndexChange(newParentPO, newIndex);
			 * 
			 * } else if (changeType.equals("after")) { newParentPO =
			 * folderDao.findOne(newTargetParentId); newIndex = newTargetIndex + 1;
			 * 
			 * // 原有文件夹下的所有index处理 handleOldParentFolderIndexChange(oldParentPO, oldIndex);
			 * 
			 * // 新目录下需要处理的index的处理 区分before和after
			 * handleNewParentFolderIndexChange(newParentPO, newIndex); }
			 * 
			 * // 接口 userFeign.setFolderToUsers(newParentPO.getUuid(),
			 * changeUserBO.getName(), newIndex); }
			 */

		} catch (Exception e) {
			LOGGER.error("Fail to changeNodePosition", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;

	}

	private void recurseUpdateParentPath(FolderPO bvcParentFolder) {
		List<FolderPO> bvcNoneRootFolders = folderDao.findByParentId(bvcParentFolder.getId());
		for (FolderPO bvcNoneFolder : bvcNoneRootFolders) {
			if (null != bvcParentFolder.getParentPath()) {
				bvcNoneFolder.setParentPath(bvcParentFolder.getParentPath() + "/" + bvcParentFolder.getId());
			} else {
				bvcNoneFolder.setParentPath("/" + bvcParentFolder.getId());
			}
			bvcNoneFolder.setSyncStatus(SYNC_STATUS.ASYNC);
			folderDao.save(bvcNoneFolder);
			recurseUpdateParentPath(bvcNoneFolder);
		}
	}

	@RequestMapping(value = "/ldapSync", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ldapSync() {
		Map<String, Object> data = makeAjaxData();
		try {
			// 从ldap服务器向资源层同步设备信息
			int fromLdapToResource = departSyncLdapUtils.handleSyncToLdap();

			// 从资源层向ldap服务器同步设备信息
			int fromResourceToLdap = departSyncLdapUtils.handleSyncFromLdap();

			data.put("fromLdapToResource", fromLdapToResource);
			data.put("fromResourceToLdap", fromResourceToLdap);
		} catch (Exception e) {
			LOGGER.error("LDAP sync error", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/syncToLdap", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> syncToLdap() {
		Map<String, Object> data = makeAjaxData();
		try {
			int successCnt = departSyncLdapUtils.handleSyncToLdap();

			data.put("successCnt", successCnt);
		} catch (Exception e) {
			LOGGER.error("LDAP sync error", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/syncFromLdap", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> syncFromLdap() {
		Map<String, Object> data = makeAjaxData();
		try {
			int successCnt = departSyncLdapUtils.handleSyncFromLdap();

			data.put("successCnt", successCnt);
		} catch (Exception e) {
			LOGGER.error("LDAP sync error", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/cleanUpLdap", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cleanUpLdap() {
		Map<String, Object> data = makeAjaxData();
		try {

			// 先从Ldap删除本地上传上去的数据
			List<FolderPO> folderPOs = folderDao.findBySourceType(SOURCE_TYPE.SYSTEM);
			if (!CollectionUtils.isEmpty(folderPOs)) {
				for (FolderPO folderPO : folderPOs) {
					try {

						List<LdapDepartmentPo> ldapDeparts = ldapDepartmentDao.getDepartByUuid(folderPO.getUuid());
						if (!CollectionUtils.isEmpty(ldapDeparts)) {
							ldapDepartmentDao.remove(ldapDeparts.get(0));
						}

						folderPO.setSyncStatus(SYNC_STATUS.ASYNC);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				folderDao.save(folderPOs);
			}

			// 重新设置bvc内部根节点为目录树的根
			List<FolderPO> bvcRootFolders = folderDao.findBvcRootFolders();
			for (FolderPO bvcRootFolder : bvcRootFolders) {

				if (bvcRootFolder.getParentPath() == null) {
					continue;
				}

				bvcRootFolder.setParentId(-1l);
				bvcRootFolder.setFolderIndex(1);
				bvcRootFolder.setParentPath(null);
				folderDao.save(bvcRootFolder);
				recurseUpdateParentPath(bvcRootFolder);
			}

			// 处理LDAP下载目录下的子目录
			List<FolderPO> externalFolderPOs = folderDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
			if (!CollectionUtils.isEmpty(externalFolderPOs)) {

				// 处理 还挂载到ldap目录下的自建目录
				for (FolderPO externalFolder : externalFolderPOs) {

					List<FolderPO> brotherFolders = folderDao.findByParentId(externalFolder.getId());

					// bvc根目录

					if (!CollectionUtils.isEmpty(brotherFolders)) {

						FolderPO bvcRootFolder = folderDao.findBvcRootFolders().get(0);

						for (FolderPO systemFolder : brotherFolders) {

							if (systemFolder.getBeBvcRoot() || systemFolder.getSourceType().equals(SOURCE_TYPE.EXTERNAL)) {
								continue;
							}

							systemFolder.setParentId(bvcRootFolder.getId());

							if (null != bvcRootFolder.getParentPath()) {
								systemFolder.setParentPath(bvcRootFolder.getParentPath() + "/" + bvcRootFolder.getId());
							} else {
								systemFolder.setParentPath("/" + bvcRootFolder.getId());
							}

							int maxIndex = caculateMaxfolderIndex(bvcRootFolder, systemFolder);
							int newIndex = maxIndex + 1;

							// 新目录下需要处理的index的处理 区分before和after
							handleNewParentFolderIndexChange(systemFolder, newIndex);

							systemFolder.setFolderIndex(newIndex);
							systemFolder.setSyncStatus(SYNC_STATUS.ASYNC);
							folderDao.save(systemFolder);

							recurseUpdateParentPath(systemFolder);
						}
					}

					// 删除目录下挂载的bundle 和 user
					List<BundlePO> bundlePOs = bundleDao.findByFolderId(externalFolder.getId());

					if (!CollectionUtils.isEmpty(bundlePOs)) {

						for (BundlePO bundlePO : bundlePOs) {
							bundlePO.setFolderId(null);
							bundlePO.setSyncStatus(SYNC_STATUS.ASYNC);
						}

						bundleDao.save(bundlePOs);

					}

					//TODO:Ldap没做
					Map<String, List<UserBO>> usersMap = userFeign.queryUsersByFolderUuid(externalFolder.getUuid());

					if (!CollectionUtils.isEmpty(usersMap.get("users"))) {

						StringBuilder sb = new StringBuilder();

						for (UserBO userBO : usersMap.get("users")) {
							sb.append(userBO.getName()).append(",");
						}

						userFeign.resetFolderOfUsers(sb.toString().substring(0, sb.toString().length() - 1));
					}

				}

				// 再从本地删除从ldap下载下来的目录数据
				folderDao.delete(externalFolderPOs);
			}

		} catch (Exception e) {
			LOGGER.error("LDAP sync error", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	private Integer caculateMaxIndex(FolderPO folderPO) {

		// 查询所有同级folders
		List<Integer> brotherFolderIndexs = new ArrayList<Integer>();
		List<FolderPO> brotherFolders = folderDao.findByParentId(folderPO.getId());

		for (FolderPO tempFolder : brotherFolders) {
			if (tempFolder.getFolderIndex() != null) {
				brotherFolderIndexs.add(tempFolder.getFolderIndex());
			}
		}

		/*
		 * if (!CollectionUtils.isEmpty(brotherFolders)) {
		 * brotherFolderIndexs.addAll(brotherFolders.stream().map(brotherFolder ->
		 * brotherFolder.getFolderIndex()).collect(Collectors.toList())); }
		 */

		// 查询所有同级bundles
		List<BundlePO> brothersBundles = bundleService.findByFolderId(folderPO.getId());

		for (BundlePO tempBundle : brothersBundles) {
			if (tempBundle.getFolderIndex() != null) {
				brotherFolderIndexs.add(tempBundle.getFolderIndex());
			}
		}

		/*
		 * if (!CollectionUtils.isEmpty(brothersBundles)) {
		 * brotherFolderIndexs.addAll(brothersBundles.stream().map(brothersBundle ->
		 * brothersBundle.getFolderIndex()).collect(Collectors.toList())); }
		 */

		// 查询素有同级users
		//Map<String, List<UserBO>> usersMap = userFeign.queryUsersByFolderUuid(folderPO.getUuid());
		List<FolderUserMap> brotherUsers = folderUserMapDao.findByFolderUuid(folderPO.getUuid());
		/*if (!CollectionUtils.isEmpty(usersMap)) {
			List<UserBO> brotherUsers = usersMap.get("users");

			for (UserBO tempUser : brotherUsers) {
				if (tempUser.getFolderIndex() != null) {
					brotherFolderIndexs.add(tempUser.getFolderIndex());
				}
			}
		}*/
		if (brotherUsers != null && brotherUsers.size() > 0) {
	
			for (FolderUserMap tempUser : brotherUsers) {
				if (tempUser.getFolderIndex() != null) {
					brotherFolderIndexs.add(tempUser.getFolderIndex().intValue());
				}
			}
		}

		/*
		 * if (!CollectionUtils.isEmpty(brotherUsers)) {
		 * brotherFolderIndexs.addAll(brotherUsers.stream().map(brotherUser ->
		 * brotherUser.getFolderIndex()).collect(Collectors.toList())); }
		 */

		LOGGER.info("caculateMaxIndex, brotherFolderIndexs=" + JSONObject.toJSONString(brotherFolderIndexs));

		if (!CollectionUtils.isEmpty(brotherFolderIndexs)) {
			return brotherFolderIndexs.stream().max(Integer::compare).get();

		}

		return 0;

	}

	private Integer caculateMaxfolderIndex(FolderPO folderPO, FolderPO changechildFolderPO) {

		// 查询所有同级folders
		List<Integer> brotherFolderIndexs = new ArrayList<Integer>();
		List<FolderPO> brotherFolders = folderDao.findByParentId(folderPO.getId());

		for (FolderPO folderTemp : brotherFolders) {
			if (!folderTemp.getUuid().equals(changechildFolderPO.getUuid())) {
				brotherFolderIndexs.add(folderTemp.getFolderIndex());
			}
		}

		if (!CollectionUtils.isEmpty(brotherFolderIndexs)) {
			return brotherFolderIndexs.stream().max(Integer::compare).get();
		}

		return 0;

	}

	private void handleOldParentFolderIndexChange(FolderPO oldParentFolderPO, int oldIndex) {
		// 查询所有同级folders
		List<FolderPO> brotherFolders = folderDao.findByParentId(oldParentFolderPO.getId());

		List<FolderPO> modifyFolders = new ArrayList<>();

		for (FolderPO folderPO : brotherFolders) {
			if (folderPO.getFolderIndex() > oldIndex) {
				folderPO.setFolderIndex(folderPO.getFolderIndex() - 1);
				modifyFolders.add(folderPO);
			}
		}

		folderDao.save(modifyFolders);

		//TODO：暂时不支持 
		/** // 查询所有同级bundles
		List<BundlePO> brothersBundles = bundleService.findByFolderId(oldParentFolderPO.getId());
		List<BundlePO> modifyBundles = new ArrayList<>();

		for (BundlePO bundlePO : brothersBundles) {
			if (bundlePO.getFolderIndex() != null && bundlePO.getFolderIndex() > oldIndex) {
				bundlePO.setFolderIndex(bundlePO.getFolderIndex() - 1);
				modifyBundles.add(bundlePO);
			}
		}

		// 存储bundle
		bundleDao.save(modifyBundles);

		// 查询素有同级users
		Map<String, List<UserBO>> usersMap = userFeign.queryUsersByFolderUuid(oldParentFolderPO.getUuid());
		List<UserBO> brotherUsers = usersMap.get("users");
		for (UserBO userBO : brotherUsers) {

			if (userBO.getFolderIndex() != null && userBO.getFolderIndex() > oldIndex) {
				userFeign.setFolderToUsers(oldParentFolderPO.getUuid(), userBO.getName(), userBO.getFolderIndex() - 1);
			}
		}*/
	}

	private void handleNewParentFolderIndexChange(FolderPO newParentFolderPO, int newIndex) {
		// 查询所有同级folders
		List<FolderPO> brotherFolders = folderDao.findByParentId(newParentFolderPO.getId());

		List<FolderPO> modifyFolders = new ArrayList<>();

		for (FolderPO folderPO : brotherFolders) {
			if (folderPO.getFolderIndex() >= newIndex) {
				folderPO.setFolderIndex(folderPO.getFolderIndex() + 1);
				modifyFolders.add(folderPO);
			}
		}

		folderDao.save(modifyFolders);

		//TODO： 暂时不支持
		/** // 查询所有同级bundles
		List<BundlePO> brothersBundles = bundleService.findByFolderId(newParentFolderPO.getId());
		List<BundlePO> modifyBundles = new ArrayList<>();

		for (BundlePO bundlePO : brothersBundles) {
			if (bundlePO.getFolderIndex() != null && bundlePO.getFolderIndex() >= newIndex) {
				bundlePO.setFolderIndex(bundlePO.getFolderIndex() + 1);
				modifyBundles.add(bundlePO);
			}
		}

		bundleDao.save(modifyBundles);

		// 查询素有同级users
		Map<String, List<UserBO>> usersMap = userFeign.queryUsersByFolderUuid(newParentFolderPO.getUuid());
		List<UserBO> brotherUsers = usersMap.get("users");

		for (UserBO userBO : brotherUsers) {

			if (userBO.getFolderIndex() != null && userBO.getFolderIndex() >= newIndex) {
				userFeign.setFolderToUsers(newParentFolderPO.getUuid(), userBO.getName(), userBO.getFolderIndex() + 1);
			}

		}*/

	}
	
	/**
	 * 获取文件夹<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月10日 下午2:19:09
	 * @param parentId
	 * @return
	 */
	private List<FolderTreeVO> createFolderChildrenTreeNodes(Long parentId) {
		List<FolderTreeVO> children = new LinkedList<FolderTreeVO>();
		try {

			// 添加子分组节点(递归)
			List<FolderPO> childrenPO = folderService.findByParentId(parentId);
			for (FolderPO childFolder : childrenPO) {
				FolderTreeVO folderNodeVO = createFolderNodeFromFolderPO(childFolder);
				folderNodeVO.setChildren(createFolderChildrenTreeNodes(childFolder.getId()));
				children.add(folderNodeVO);
			}

			Collections.sort(children, Comparator.comparing(FolderTreeVO::getFolderIndex));

		} catch (Exception e) {
			LOGGER.error("Fail to creat folder tree children", e);
		}
		
		return children;
	}

	private List<FolderTreeVO> createChildrenTreeNodes(Long parentId) {
		List<FolderTreeVO> children = new LinkedList<FolderTreeVO>();
		try {

			// 添加子分组节点(递归)
			// TODO
			List<FolderPO> childrenPO = folderService.findByParentId(parentId);
			for (FolderPO childFolder : childrenPO) {
				FolderTreeVO folderNodeVO = createFolderNodeFromFolderPO(childFolder);
				folderNodeVO.setChildren(createChildrenTreeNodes(childFolder.getId()));
				children.add(folderNodeVO);
			}

			Collections.sort(children, Comparator.comparing(FolderTreeVO::getFolderIndex));

			// 添加子bundle节点
			List<BundlePO> bundles = bundleService.findByFolderId(parentId);
			for (BundlePO bundle : bundles) {
				children.add(createBundleNode(parentId, bundle));
			}

			// 添加子用户节点
			try {
				FolderPO folderPO = folderDao.findOne(parentId);
				/**Map<String, List<UserBO>> usersMap = userFeign.queryUsersByFolderUuid(folderPO.getUuid());
				for (UserBO userBO : usersMap.get("users")) {
					children.add(createUserNode(parentId, userBO));
				}*/
				List<FolderUserMap> userBOs = folderUserMapDao.findByFolderUuid(folderPO.getUuid());
				for (FolderUserMap userBO : userBOs) {
					children.add(createUserNode(parentId, userBO));
				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}

		} catch (Exception e) {
			LOGGER.error("Fail to creat folder tree children", e);
		}
		return children;
	}

	private FolderTreeVO createBundleNode(Long parentId, BundlePO bundle) {
		FolderTreeVO bundleNodeVO = new FolderTreeVO();
		bundleNodeVO.setId(BUNDLE_NODE_ID_BASE + bundle.getId());
		bundleNodeVO.setParentId(parentId);
		bundleNodeVO.setName(bundle.getBundleName());
		bundleNodeVO.setBeFolder(false);
		bundleNodeVO.setBundleId(bundle.getBundleId());
		bundleNodeVO.setFolderIndex(bundle.getFolderIndex());
		bundleNodeVO.setNodeType("BUNDLE");
		bundleNodeVO.setSystemSourceType(bundle.getSourceType().equals(SOURCE_TYPE.SYSTEM) ? true : false);
		return bundleNodeVO;
	}

	private FolderTreeVO createUserNode(Long parentId, UserBO user) {
		FolderTreeVO userNodeVO = new FolderTreeVO();
		userNodeVO.setId(USER_NODE_ID_BASE + user.getId());
		userNodeVO.setParentId(parentId);
		userNodeVO.setName(user.getName());
		userNodeVO.setBeFolder(false);
		userNodeVO.setUsername(user.getName());
		userNodeVO.setFolderIndex(user.getFolderIndex());
		userNodeVO.setNodeType("USER");
		userNodeVO.setSystemSourceType(user.getCreater().equals("ldap") ? false : true);
		return userNodeVO;
	}
	
	/**
	 * 用户树节点<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月27日 上午11:06:10
	 * @param Long parentId
	 * @param FolderUserMap user 
	 * @return FolderTreeVO
	 */
	private FolderTreeVO createUserNode(Long parentId, FolderUserMap user) {
		FolderTreeVO userNodeVO = new FolderTreeVO();
		userNodeVO.setId(user.getUserId());
		userNodeVO.setParentId(parentId);
		userNodeVO.setName(user.getUserName());
		userNodeVO.setBeFolder(false);
		userNodeVO.setUsername(user.getUserName());
		userNodeVO.setFolderIndex(user.getFolderIndex().intValue());
		userNodeVO.setNodeType("USER");
		userNodeVO.setSystemSourceType(user.getCreator().equals("ldap") ? false : true);
		return userNodeVO;
	}

	private FolderTreeVO createFolderNodeFromFolderPO(FolderPO folder) {
		FolderTreeVO folderNodeVO = new FolderTreeVO();
		folderNodeVO.setId(folder.getId());
		folderNodeVO.setParentId(folder.getParentId());
		folderNodeVO.setName(folder.getName());
		folderNodeVO.setBeFolder(true);
		folderNodeVO.setToLdap(folder.getToLdap());
		folderNodeVO.setChildren(new LinkedList<FolderTreeVO>());
		folderNodeVO.setSystemSourceType(SOURCE_TYPE.SYSTEM.equals(folder.getSourceType()) ? true : false);
		if (folder.getFolderType() != null) {
			folderNodeVO.setFolderType(folder.getFolderType().toString());
		}
		folderNodeVO.setFolderIndex(folder.getFolderIndex());
		folderNodeVO.setNodeType("FOLDER");
		return folderNodeVO;
	}
}
