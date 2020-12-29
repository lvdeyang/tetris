package com.suma.venus.resource.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.suma.application.ldap.department.dao.LdapDepartmentDao;
import com.suma.application.ldap.department.po.LdapDepartmentPo;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.dao.RolePrivilegeMapDAO;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.dao.SerNodeRolePermissionDAO;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.suma.venus.resource.pojo.RolePrivilegeMap;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.SerNodePO.ConnectionStatus;
import com.suma.venus.resource.pojo.SerNodeRolePermissionPO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.FolderUserMap.FolderUserComparator;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.FolderService;
import com.suma.venus.resource.service.UserQueryService;
import com.suma.venus.resource.util.DepartSyncLdapUtils;
import com.suma.venus.resource.vo.BundleVO;
import com.suma.venus.resource.vo.FolderTreeVO;
import com.suma.venus.resource.vo.FolderVO;
import com.sumavision.tetris.bvc.business.dispatch.TetrisDispatchService;
import com.sumavision.tetris.bvc.business.dispatch.bo.PassByBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;

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
	
	@Autowired
	private Path path;
	
	@Autowired
	private PrivilegeDAO privilegeDAO;
	
	@Autowired
	private WorkNodeDao workNodeDao;
	
	@Autowired
	private SerNodeDao serNodeDao;
	
	@Autowired
	private RolePrivilegeMapDAO rolePrivilegeMapDAO;
	
	@Autowired
	private SerNodeRolePermissionDAO serNodeRolePermissionDAO;
	
	@Autowired
	private TetrisDispatchService tetrisDispatchService;

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
			Long removeableRoot = 0l;
			if (!removeableRoot.equals(folder.getParentId()) && null == folder.getParentPath()) {
				// 根节点不能删除
				data.put(ERRMSG, "根节点不能删除");
				return data;
			}
			
			// 调整同级数据的index
			if(folder.getFolderIndex() != null){
				int oldIndex = folder.getFolderIndex();
				FolderPO parentFolder = folderDao.findOne(folder.getParentId());
				if(parentFolder != null)handleOldParentFolderIndexChange(parentFolder, oldIndex);
			}

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

			List<String> bundleIdStrings = new ArrayList<String>();
			int maxIndex = caculateMaxIndex(folderPO);
			for (String bundleId : bundleIdArr) {
				BundlePO bundle = bundleService.findByBundleId(bundleId);
				bundle.setFolderId(folderId);
				bundle.setSyncStatus(SYNC_STATUS.ASYNC);

				maxIndex++;
				bundle.setFolderIndex(maxIndex);

				bundleService.save(bundle);
				bundleNodes.add(createBundleNode(folderId, bundle));
				
				bundleIdStrings.add(bundleId);
			}

			data.put("bundleNodes", bundleNodes);
			
			//设备修改组织机构外域
			try {
				PassByBO passByBOnew = new PassByBO();
				List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
				SerNodePO serNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
				Map<String, Object> local = new HashMap<String, Object>();
				Map<String, Object> pass_by_content = new HashMap<String, Object>();
				local.put("name", serNodePO.getNodeName());
				//处理组织机构
				
				//------------------
				Set<Long> allFolderIds = new HashSet<Long>();
				if(folderPO.getParentPath() != null && !folderPO.getParentPath().equals("")){
					String[] allfolderIds = folderPO.getParentPath().split("/");
					for (int i = 1; i < allfolderIds.length; i++) {
						allFolderIds.add(Long.parseLong(allfolderIds[i]));
					}
				}
				List<FolderPO> allFolderPOs = folderDao.findByIdIn(allFolderIds);
				allFolderPOs.add(folderPO);
				Map<Long, String> idUuidMap = new HashMap<Long, String>();
				List<FolderVO> folderVOs = new ArrayList<FolderVO>();
				if(allFolderPOs!=null && allFolderPOs.size()>0){
					for(FolderPO folderPO1:allFolderPOs){
						FolderVO folderVO = FolderVO.fromFolderPO(folderPO1);
						folderVOs.add(folderVO);
						idUuidMap.put(folderVO.getId(), folderVO.getUuid());
					}
					
					for(FolderVO folderVO:folderVOs){
//						folderVO.setParentId(idUuidMap.get(folderVO.getId()));
						String parentPath = folderVO.getParentPath();
						if(parentPath==null || "".equals(parentPath)) continue;
						StringBufferWrapper newParentPath = new StringBufferWrapper();
						String[] parentIds = parentPath.split("/");
						for(int i=1; i<parentIds.length; i++){
							newParentPath.append("/").append(idUuidMap.get(Long.valueOf(parentIds[i])));
						}
						folderVO.setParentPath(newParentPath.toString());
					}
				}
				//----------------------
				
				List<PrivilegePO> privilegePOs = privilegeDAO.findByIndentify(bundleIdStrings);
				Set<Long> privilegelLongs = new HashSet<Long>();
				if (privilegePOs != null && !privilegePOs.isEmpty()) {
					for (PrivilegePO privilegePO : privilegePOs) {
						privilegelLongs.add(privilegePO.getId());
					}
				}
				List<RolePrivilegeMap> rolePrivilegeMaps = rolePrivilegeMapDAO.findByPrivilegeIdIn(privilegelLongs);
				Set<Long> serNodeIds = new HashSet<Long>();
				if (rolePrivilegeMaps != null && !rolePrivilegeMaps.isEmpty()) {
					Long roleId = rolePrivilegeMaps.get(0).getRoleId();
					List<SerNodeRolePermissionPO> serNodeRolePermissionPOs = serNodeRolePermissionDAO.findByRoleId(roleId);
					if (serNodeRolePermissionPOs != null && !serNodeRolePermissionPOs.isEmpty()) {
						for (SerNodeRolePermissionPO serNodeRolePermissionPO : serNodeRolePermissionPOs) {
							serNodeIds.add(serNodeRolePermissionPO.getSerNodeId());
						}
					}
				}
				List<SerNodePO> serNodePOs = serNodeDao.findByIdIn(serNodeIds);
				List<Map<String, Object>> devices = new ArrayList<Map<String,Object>>();
				
				for (int i = 0; i < bundleIdStrings.size(); i++) {
					devices.add(new HashMap<String, Object>());
					devices.get(i).put("bundleId", bundleIdStrings.get(i));
					devices.get(i).put("institution", folderPO.getUuid());
				}
				
				if (serNodePOs != null && !serNodePOs.isEmpty()) {
					List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < serNodePOs.size(); i++) {
						foreign.add(new HashMap<String, Object>());
						foreign.get(i).put("name", serNodePOs.get(i).getNodeName());
						foreign.get(i).put("institutions", folderVOs);
						foreign.get(i).put("devices", devices);
					}
					pass_by_content.put("cmd", "deviceInstitutionChange");
					pass_by_content.put("local", local);
					pass_by_content.put("foreign", foreign);
					passByBOnew.setPass_by_content(pass_by_content);
					if (workNodePOs != null && !workNodePOs.isEmpty()) {
						passByBOnew.setLayer_id(workNodePOs.get(0).getNodeUid());
					}
					tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBOnew).getList());
					System.out.println("--------修改设备组织机构***——————————" + passByBOnew);
				}
				
			} catch (Exception e) {
				LOGGER.error(e.toString());
			}
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
			
			List<FolderTreeVO> userNodes = new LinkedList<FolderTreeVO>();
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
			for(UserBO user: users){
				userNodes.add(createUserNode(folderId, user));
			}

			data.put("userNodes", userNodes);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			data.put(ERRMSG, e.getMessage());
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
			
			//设备修改组织机构外域
			try {
				PassByBO passByBOnew = new PassByBO();
				List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
				SerNodePO serNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
				Map<String, Object> local = new HashMap<String, Object>();
				Map<String, Object> pass_by_content = new HashMap<String, Object>();
				local.put("name", serNodePO.getNodeName());
				//处理组织机构
				
				//------------------
				Set<Long> allFolderIds = new HashSet<Long>();
				if(folder.getParentPath() != null && !folder.getParentPath().equals("")){
					String[] allfolderIds = folder.getParentPath().split("/");
					for (int i = 1; i < allfolderIds.length; i++) {
						allFolderIds.add(Long.parseLong(allfolderIds[i]));
					}
				}
				List<FolderPO> allFolderPOs = folderDao.findByIdIn(allFolderIds);
				allFolderPOs.add(folder);
				Map<Long, String> idUuidMap = new HashMap<Long, String>();
				List<FolderVO> folderVOs = new ArrayList<FolderVO>();
				if(allFolderPOs!=null && allFolderPOs.size()>0){
					for(FolderPO folderPO1:allFolderPOs){
						FolderVO folderVO = FolderVO.fromFolderPO(folderPO1);
						folderVOs.add(folderVO);
						idUuidMap.put(folderVO.getId(), folderVO.getUuid());
					}
					
					for(FolderVO folderVO:folderVOs){
//						folderVO.setParentId(idUuidMap.get(folderVO.getId()));
						String parentPath = folderVO.getParentPath();
						if(parentPath==null || "".equals(parentPath)) continue;
						StringBufferWrapper newParentPath = new StringBufferWrapper();
						String[] parentIds = parentPath.split("/");
						for(int i=1; i<parentIds.length; i++){
							newParentPath.append("/").append(idUuidMap.get(Long.valueOf(parentIds[i])));
						}
						folderVO.setParentPath(newParentPath.toString());
					}
				}
				//----------------------
				
				List<SerNodePO> ExserNodePOs = serNodeDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
				List<SerNodePO> serNodePOs = new ArrayList<SerNodePO>();
				if(ExserNodePOs != null&& ExserNodePOs.size()>0){
					for (SerNodePO serNodePO2 : ExserNodePOs) {
						if (serNodePO2.getStatus().equals(ConnectionStatus.ON)) {
							serNodePOs.add(serNodePO2);
						}
					}
				}
				List<Map<String, Object>> devices = new ArrayList<Map<String,Object>>();
				
				if (serNodePOs != null && !serNodePOs.isEmpty()) {
					List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < serNodePOs.size(); i++) {
						foreign.add(new HashMap<String, Object>());
						foreign.get(i).put("name", serNodePOs.get(i).getNodeName());
						foreign.get(i).put("institutions", folderVOs);
						foreign.get(i).put("devices", devices);
					}
					pass_by_content.put("cmd", "deviceInstitutionChange");
					pass_by_content.put("local", local);
					pass_by_content.put("foreign", foreign);
					passByBOnew.setPass_by_content(pass_by_content);
					if (workNodePOs != null && !workNodePOs.isEmpty()) {
						passByBOnew.setLayer_id(workNodePOs.get(0).getNodeUid());
					}
					tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBOnew).getList());
					System.out.println("--------修改设备组织机构***gaiming——————————" + passByBOnew);
				}
				
			} catch (Exception e) {
				LOGGER.error(e.toString());
			}
		} catch (Exception e) {
			LOGGER.error("Fail to modify folder : ", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}
	
	@RequestMapping(value = "/queryTreeChildrenByParentId", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryTreeChildrenByParentId(@RequestParam(value = "parentId", required = false) Long parentId) {
		Map<String, Object> data = makeAjaxData();
		try {

			data.put("tree", createChildrenTreeNodes(parentId, true, 1));
		} catch (

		Exception e) {
			LOGGER.error("Fail to init folder tree", e);
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
	
	@RequestMapping(value = "/changeFolderIndex", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeFolderIndex(
			Long draggingId,
			Long dropId,
			Long folderId,
			String type){
		Map<String, Object> data = makeAjaxData();
		try {
			FolderUserComparator comparator = new FolderUserMap.FolderUserComparator();
			List<FolderUserMap> users = folderUserMapDao.findByFolderId(folderId);
			List<FolderUserMap> sortUsers = new ArrayList<FolderUserMap>();
			Collections.sort(users, comparator);
			FolderUserMap draggingNode = null;
			FolderUserMap dropNode = null;
			for(FolderUserMap user:users){
				if(user.getUserId().equals(draggingId)) draggingNode = user;
				if(user.getUserId().equals(dropId)) dropNode = user;
			}
			for(int i=0; i<users.size(); i++){
				FolderUserMap user = users.get(i);
				if(!user.getUserId().equals(draggingId) && !user.getUserId().equals(dropId)){
					sortUsers.add(user);
				}else{
					if(user.getUserId().equals(dropId)){
						if("before".equals(type)){
							sortUsers.add(draggingNode);
							sortUsers.add(dropNode);
						}else if("after".equals(type)){
							sortUsers.add(dropNode);
							sortUsers.add(draggingNode);
						}
					}
				}
			}
			for(int i=0; i<sortUsers.size(); i++){
				sortUsers.get(i).setFolderIndex(Long.parseLong(String.valueOf(i+1)));
			}
			folderUserMapDao.save(sortUsers);
		} catch (Exception e) {
			LOGGER.error("Fail to init folder tree", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}
	
	@RequestMapping(value = "/initTreeWithOutMember", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> initAllTreeWithOutMember() {
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
				rootTreeVO.setChildren(createChildrenTreeNodes(rootFolderPO.getId(), false, null));
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
	
	// 解除根目录
	@RequestMapping(value = "/releaseRootNode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> releaseRootNode(Long folderId){
		Map<String, Object> data = makeAjaxData();
		try {
			
			FolderPO folder = folderDao.findOne(folderId);
			if(folder == null){
				data.put(ERRMSG, "未获取到文件夹，id："+folderId);
				return data;
			}
			folder.setBeBvcRoot(false);
			folder.setParentId(0l);
			folder.setParentPath(null);
			folderDao.save(folder);
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
			
			departSyncLdapUtils.handleFolderUserSyncToLdap();

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
			
			departSyncLdapUtils.handleFolderUserSyncFromLdap();

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

				}

				// 再从本地删除从ldap下载下来的目录数据
				folderDao.delete(externalFolderPOs);
			}
			
			departSyncLdapUtils.handleFolderUserCleanUpLdap();

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
				List<FolderUserMap> userBOs = folderUserMapDao.findByFolderUuidOrderByFolderIndex(folderPO.getUuid());
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
	
	private List<FolderTreeVO> createChildrenTreeNodes(Long parentId, boolean withMembers, Integer level) {
		List<FolderTreeVO> children = new LinkedList<FolderTreeVO>();
		try {

			if ((level != null && level <= 0) || parentId == null) {
				return children;
			}

			// 添加子分组节点(递归)
			// TODO
			List<FolderPO> childrenPO = folderService.findByParentId(parentId);
			for (FolderPO childFolder : childrenPO) {
				FolderTreeVO folderNodeVO = createFolderNodeFromFolderPO(childFolder);
				if (level == null || level > 0) {
					if (level != null) {
						level--;
					}
					folderNodeVO.setChildren(createChildrenTreeNodes(childFolder.getId(), withMembers, level));
				}
				children.add(folderNodeVO);
			}

			Collections.sort(children, Comparator.comparing(FolderTreeVO::getFolderIndex));

			// 添加子bundle节点
			if (withMembers) {
				List<BundlePO> bundles = bundleService.findByFolderId(parentId);
				for (BundlePO bundle : bundles) {
					children.add(createBundleNode(parentId, bundle));
				}
			}

			// 添加子用户节点
			if (withMembers) {
				try {
					FolderPO folderPO = folderDao.findOne(parentId);
					Map<String, List<UserBO>> usersMap = userFeign.queryUsersByFolderUuid(folderPO.getUuid());
					for (UserBO userBO : usersMap.get("users")) {
						children.add(createUserNode(parentId, userBO));
					}
				} catch (Exception e) {
					LOGGER.error("", e);
				}
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
		userNodeVO.setFolderIndex(user.getFolderIndex() == null ? null: user.getFolderIndex().intValue());
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
	
	/**
	 * 导入分组<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 上午10:42:01
	 * @param uploadFile
	 */
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> importFolders(@RequestParam("filePoster") MultipartFile uploadFile) {
		Map<String, Object> data = makeAjaxData();
		try {
			//int successCnt = importExcel(uploadFile.getInputStream());
			int successCnt = importcsv(uploadFile.getInputStream());
			data.put("successCnt", successCnt);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}
	
	/**
	 * 导入用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 下午4:06:07
	 */
	@RequestMapping(value = "/user/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> importUserFolders(@RequestParam("userFile") MultipartFile uploadFile) {
		Map<String, Object> data = makeAjaxData();
		try {
			//int successCnt = importExcel(uploadFile.getInputStream());
			int successCnt = importUserCSV(uploadFile.getInputStream());
			data.put("successCnt", successCnt);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}
	
	private int importExcel(InputStream is) throws Exception {
		int successCnt = 0;

		Workbook workbook = WorkbookFactory.create(is);
		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();

		// 按行读取并处理数据,第一行标题栏忽略
		for (int i = 1; i <= lastRowNum; i++) {
			// TODO

			try {
				Row row = sheet.getRow(i);
				
				if (row.getCell(0) == null || row.getCell(1) == null
						|| StringUtils.isEmpty(row.getCell(0).getStringCellValue())
						|| StringUtils.isEmpty(row.getCell(1).getStringCellValue())) {
					continue;
				}
				
				String folderUuid = row.getCell(0).getStringCellValue();
				String folderName = row.getCell(1).getStringCellValue();

				String parentUuid = null;
				String folderType = null;

				if (row.getCell(2) != null) {
					parentUuid = row.getCell(2).getStringCellValue();
				}

				if (row.getCell(3) != null) {
					folderType = row.getCell(3).getStringCellValue();
				}

				if (StringUtils.isEmpty(folderUuid) || StringUtils.isEmpty(folderName)) {
					continue;
				}

				FolderPO folderPO = folderDao.findTopByUuid(folderUuid);

				if (folderPO == null) {
					// TODO 怎么处理？
					folderPO = new FolderPO();
					folderPO.setUuid(folderUuid);

				}

				folderPO.setName(folderName);
				
				FolderPO parentFolder;
				if (!StringUtils.isEmpty(parentUuid)) {
					parentFolder = folderDao.findTopByUuid(parentUuid);
				} else {
					// 导入的根目录处理
					switch (folderType) {
					case "终端":
						folderPO.setFolderType(FolderType.TERMINAL);
						break;
					case "监控":
						folderPO.setFolderType(FolderType.MONITOR);
						break;
					case "直播":
						folderPO.setFolderType(FolderType.LIVE);
						break;
					case "点播":
						folderPO.setFolderType(FolderType.ON_DEMAND);
						break;
					case "用户":
						folderPO.setFolderType(FolderType.USER);
						break;
					default:
						folderPO.setFolderType(FolderType.TERMINAL);
						break;
					}

					List<FolderPO> templistFolderPOs = folderDao.findBvcRootFolders();
					
					parentFolder = folderDao.findFolderByTypeAndParent(folderPO.getFolderType(), templistFolderPOs.get(0).getId());
				}
				
				if(parentFolder == null) {
					continue;
				}
				
				folderPO.setParentId(parentFolder.getId());

				if (null != parentFolder.getParentPath()) {
					folderPO.setParentPath(parentFolder.getParentPath() + "/" + parentFolder.getId());
				} else {
					folderPO.setParentPath("/" + parentFolder.getId());
				}
				
				folderPO.setFolderType(parentFolder.getFolderType());

				int maxIndex = caculateMaxfolderIndex(parentFolder, folderPO);
				handleNewParentFolderIndexChange(parentFolder, maxIndex + 1);
				folderPO.setFolderIndex(maxIndex + 1);

				folderService.save(folderPO);
				successCnt++;

			} catch (Exception e) {
				LOGGER.error(e.toString());
				continue;
			}
		}

		return successCnt;
	}
	
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public ResponseEntity<byte[]> export(HttpServletResponse response) {
		try {
			String fileName = "分组数据表";
			File file = createCSV();
			// createCSV().write(os);
			System.out.println(file.getAbsolutePath());

			FileInputStream in = new FileInputStream(file);
			byte[] body = new byte[in.available()];
			in.read(body);
			HttpHeaders headers = new HttpHeaders();
			// headers.setContentType(MediaType.);
			headers.setContentDispositionFormData("attachment", new String((fileName + ".csv").getBytes("UTF-8"), "iso-8859-1"));
			
			try {
				in.close();
				file.delete();

			} catch (Exception e) {
				LOGGER.info("FileInputStream close, delte temp file error");
				e.printStackTrace();
			}

			return new ResponseEntity<byte[]>(body, headers, HttpStatus.CREATED);

		} catch (Exception e) {
			LOGGER.info("Fail to export csv of users:", e);
		}

		return null;
	}
	
	/**
	 * 导出用户关联文件夹csv<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 下午3:07:24
	 */
	@RequestMapping(value = "/user/export", method = RequestMethod.POST)
	public ResponseEntity<byte[]> exportUser(HttpServletResponse response) {
		try {
			String fileName = "用户分组数据表";
			File file = createUserCSV();
			// createCSV().write(os);
			System.out.println(file.getAbsolutePath());

			FileInputStream in = new FileInputStream(file);
			byte[] body = new byte[in.available()];
			in.read(body);
			HttpHeaders headers = new HttpHeaders();
			// headers.setContentType(MediaType.);
			headers.setContentDispositionFormData("attachment", new String((fileName + ".csv").getBytes("UTF-8"), "iso-8859-1"));
			
			try {
				in.close();
				file.delete();

			} catch (Exception e) {
				LOGGER.info("FileInputStream close, delte temp file error");
				e.printStackTrace();
			}

			return new ResponseEntity<byte[]>(body, headers, HttpStatus.CREATED);

		} catch (Exception e) {
			LOGGER.info("Fail to export csv of users:", e);
		}

		return null;
	}

	private File createCSV() throws Exception {
		
		String webappPath = path.webappPath();
		
		String filePath = new StringBufferWrapper().append(webappPath)
												   .append("exportFoldersCSVTemp_")
												   .append(Calendar.getInstance().getTimeInMillis())
												   .append(".csv")
												   .toString();

		File file = new File(filePath);
		
		LOGGER.info("file.getAbsolutePath()=" + file.getAbsolutePath());
		// FileWriter fileWriter = new FileWriter(file,true);
		// System.out.println(file.getAbsolutePath());

		Appendable printWriter = new PrintWriter(file, "utf-8");
		CSVPrinter csvPrinter = CSVFormat.EXCEL.withHeader("分组编码", "分组名称", "父分组编码", "分组类型").print(printWriter);

		List<FolderPO> rootFolders = folderService.findByParentPath(null);
		if (rootFolders.isEmpty()) {
			LOGGER.error("数据库错误：不存在根节点");
		} else {
			for (FolderPO rootFolderPO : rootFolders) {
				createChildData(rootFolderPO, csvPrinter);
			}

		}

		csvPrinter.flush();
		// printWriter.flush();
		// printWriter.close();
		csvPrinter.close();

		return file;

	}
	
	private File createUserCSV() throws Exception {
		
		String webappPath = path.webappPath();
		
		String filePath = new StringBufferWrapper().append(webappPath)
												   .append("exportFolderMapUserCSVTemp_")
												   .append(Calendar.getInstance().getTimeInMillis())
												   .append(".csv")
												   .toString();

		File file = new File(filePath);

		Appendable printWriter = new PrintWriter(file, "utf-8");
		CSVPrinter csvPrinter = CSVFormat.EXCEL.withHeader("用户账号", "用户昵称", "密码", "所属分组编码").print(printWriter);

		List<FolderUserMap> maps = folderUserMapDao.findAll();
		for(FolderUserMap map: maps){
			csvPrinter.printRecord("", map.getUserName(), "", map.getFolderUuid());
		}

		csvPrinter.flush();
		csvPrinter.close();

		return file;

	}

	// 递归方法
	private void createChildData(FolderPO parentFolderPO, CSVPrinter csvPrinter) {
		String parentUuid = "";

		String typeStr = "";

		if (parentFolderPO.getParentId() != null && parentFolderPO.getParentId() != -1l) {

			FolderPO tempPo = folderDao.findOne(parentFolderPO.getParentId());
			if (tempPo != null) {
				parentUuid = tempPo.getUuid();
			}
		}

		if (parentFolderPO.getFolderType() != null) {
			switch (parentFolderPO.getFolderType().toString()) {
	
			case "TERMINAL":
				typeStr = "终端";
				break;
			case "MONITOR":
				typeStr = "监控";
				break;
			case "LIVE":
				typeStr = "直播";
				break;
			case "ON_DEMAND":
				typeStr = "点播";
				break;
			default:
				break;
			}
		}

		try {
			csvPrinter.printRecord(parentFolderPO.getUuid(), parentFolderPO.getName(), parentUuid, typeStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<FolderPO> childFolderPOList = folderDao.findByParentId(parentFolderPO.getId());

		if (!CollectionUtils.isEmpty(childFolderPOList)) {

			for (FolderPO folderPO : childFolderPOList) {
				// 递归
				createChildData(folderPO, csvPrinter);
			}

		}

	}
	
	private int importcsv(InputStream is) throws Exception {
		int successCnt = 0;

		CSVParser parser = CSVParser.parse(is, Charset.forName("utf-8"), CSVFormat.DEFAULT
				.withHeader("folderUuid", "folderName", "parentUuid", "folderType").withSkipHeaderRecord());
		for (CSVRecord csvRecord : parser) {

			if (StringUtils.isEmpty(csvRecord.get("folderUuid")) || StringUtils.isEmpty(csvRecord.get("folderName"))) {
				continue;
			}
			try {

				String folderUuid = csvRecord.get("folderUuid");
				String folderName = csvRecord.get("folderName");
				String parentUuid = csvRecord.get("parentUuid");
				String folderType = csvRecord.get("folderType");

				FolderPO folderPO = folderDao.findTopByUuid(folderUuid);

				if (folderPO == null) {
					folderPO = new FolderPO();
					folderPO.setUuid(folderUuid);
				}

				folderPO.setName(folderName);
				folderPO.setToLdap(true);

				FolderPO parentFolder;
				
				if (!StringUtils.isEmpty(parentUuid)) {
					
					switch (folderType) {
					case "终端":
						folderPO.setFolderType(FolderType.TERMINAL);
						break;
					case "监控":
						folderPO.setFolderType(FolderType.MONITOR);
						break;
					case "直播":
						folderPO.setFolderType(FolderType.LIVE);
						break;
					case "点播":
						folderPO.setFolderType(FolderType.ON_DEMAND);
						break;
					default:
						folderPO.setFolderType(FolderType.TERMINAL);
						break;
					}
					
					parentFolder = folderDao.findTopByUuid(parentUuid);
					
					folderPO.setParentId(parentFolder.getId());

					if (null != parentFolder.getParentPath()) {
						folderPO.setParentPath(parentFolder.getParentPath() + "/" + parentFolder.getId());
					} else {
						folderPO.setParentPath("/" + parentFolder.getId());
					}
					
					if (parentFolder.getFolderType() != null) {
						folderPO.setFolderType(parentFolder.getFolderType());
					}
					
				} else {
					
					// 导入的根目录处理
					switch (folderType) {
					case "终端":
						folderPO.setFolderType(FolderType.TERMINAL);
						break;
					case "监控":
						folderPO.setFolderType(FolderType.MONITOR);
						break;
					case "直播":
						folderPO.setFolderType(FolderType.LIVE);
						break;
					case "点播":
						folderPO.setFolderType(FolderType.ON_DEMAND);
						break;
					default:
						folderPO.setFolderType(null);
						break;
					}
					
					folderPO.setParentId(-1l);
					folderPO.setBeBvcRoot(true);
					folderPO.setParentPath(null);
					
					parentFolder = new FolderPO();
					parentFolder.setId(-1l);

//					List<FolderPO> templistFolderPOs = folderDao.findBvcRootFolders();
//					
//					if (CollectionUtils.isEmpty(templistFolderPOs)) {
//						continue;
//					}
//
//					parentFolder = folderDao.findFolderByTypeAndParent(folderPO.getFolderType(),
//							templistFolderPOs.get(0).getId());
//					
//					if (parentFolder == null) {
//						parentFolder = templistFolderPOs.get(0);
//					}

				}

				int maxIndex = caculateMaxfolderIndex(parentFolder, folderPO);
				handleNewParentFolderIndexChange(parentFolder, maxIndex + 1);
				folderPO.setFolderIndex(maxIndex + 1);

				folderService.save(folderPO);
				successCnt++;

			} catch (Exception e) {
				LOGGER.error(e.toString());
				continue;
			}
		}

		return successCnt;

	}
	
	private int importUserCSV(InputStream is) throws Exception {
		int successCnt = 0;

		CSVParser parser = CSVParser.parse(is, Charset.forName("utf-8"), CSVFormat.DEFAULT
				.withHeader("userName", "nickName", "password", "folderUuid").withSkipHeaderRecord());
		
		List<JSONObject> allData = new ArrayList<JSONObject>();
		List<String> allNames = new ArrayList<String>();
		Set<String> allFolders = new HashSet<String>();
		
		for (CSVRecord csvRecord : parser) {

			if (StringUtils.isEmpty(csvRecord.get("nickName")) || StringUtils.isEmpty(csvRecord.get("folderUuid"))) {
				continue;
			}

			String nickName = csvRecord.get("nickName");
			String folderUuid = csvRecord.get("folderUuid");
			
			JSONObject data = new JSONObject();
			data.put("nickName", nickName);
			data.put("folderUuid", folderUuid);
			allData.add(data);
			allNames.add(nickName);
			allFolders.add(folderUuid);

			successCnt++;
		}
		
		List<FolderUserMap> maps = folderUserMapDao.findAll();
		List<FolderPO> folders = folderDao.findByUuidIn(allFolders);
		List<UserBO> users = userQueryService.queryUsersByNicknameIn(allNames);
		
		List<FolderUserMap> needSaveMaps = new ArrayList<FolderUserMap>();
		for(JSONObject data: allData){
			String nickName = data.getString("nickName");
			String folderUuid = data.getString("folderUuid");
			
			FolderPO folderPO = null;
			for(FolderPO folder: folders){
				if(folder.getUuid().equals(folderUuid)){
					folderPO = folder;
					break;
				}
			}
			
			UserBO userBO = null;
			for(UserBO user: users){
				if(user.getName().equals(nickName)){
					userBO = user;
					break;
				}
			}
			
			if(folderPO != null && userBO != null){
				
				FolderUserMap folderUserMap = null;
				for(FolderUserMap map: maps){
					if(map.getUserName().equals(nickName)){
						folderUserMap = map;
						break;
					}
				}
				
				if(folderUserMap == null){
					folderUserMap = new FolderUserMap();
				}
				
				folderUserMap.setFolderId(folderPO.getId());
				folderUserMap.setFolderIndex(folderPO.getFolderIndex().longValue());
				folderUserMap.setFolderUuid(folderUuid);
				folderUserMap.setUserId(userBO.getId());
				folderUserMap.setUserUuid(userBO.getUser().getUuid());
				folderUserMap.setUserNo(userBO.getUserNo());
				folderUserMap.setUserName(nickName);
				folderUserMap.setSyncStatus(0);
				folderUserMap.setCreator(userBO.getCreater());
				
				needSaveMaps.add(folderUserMap);
			}
			
		}
		
		folderUserMapDao.save(needSaveMaps);

		return successCnt;

	}
	
}
