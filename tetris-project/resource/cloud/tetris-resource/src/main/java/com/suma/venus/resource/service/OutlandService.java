package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
import com.suma.venus.resource.base.bo.BundlePrivilegeBO;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.controller.ControllerBase;
import com.suma.venus.resource.controller.FolderManageController;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.dao.RolePrivilegeMapDAO;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.dao.SerNodeRolePermissionDAO;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.suma.venus.resource.pojo.RolePrivilegeMap;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.SerNodeRolePermissionPO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.SerNodePO.ConnectionStatus;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.suma.venus.resource.vo.FolderTreeVO;
import com.suma.venus.resource.vo.SerNodeVO;
import com.sumavision.tetris.bvc.business.dispatch.TetrisDispatchService;
import com.sumavision.tetris.bvc.business.dispatch.bo.PassByBO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.system.role.SystemRoleQuery;
import com.sumavision.tetris.system.role.SystemRoleVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class OutlandService extends ControllerBase{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FolderManageController.class);

	private final Long BUNDLE_NODE_ID_BASE = 100000l;

	@Autowired
	private SerNodeDao serNodeDao;
	
	@Autowired
	private SerNodeRolePermissionDAO serNodeRolePermissionDAO;
	
	@Autowired
	private SystemRoleQuery systemRoleQuery;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private WorkNodeDao workNodeDao;
	
	@Autowired
	private TetrisDispatchService tetrisDispatchService;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private BundleService bundleService;
	
	@Autowired
	private PrivilegeDAO privilegeDAO;
	
	@Autowired
	private RolePrivilegeMapDAO rolePrivilegeMapDAO;
	
	/**
	 * 查询本域信息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 下午4:38:02
	 * SerNodeVO 本域信息
	 */
	public SerNodeVO queryInland()throws Exception{
		SerNodePO serNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		SerNodeVO serNodeVO = new SerNodeVO();
		if (serNodePO != null) {
			serNodeVO = SerNodeVO.transFromPO(serNodePO);
		}
		return serNodeVO;
	}
	
	/**
	 * 修改本域名称及口令<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:15:58
	 * @param name 名称
	 * @param password 口令
	 * @return SerNodeVO 本域节点信息
	 */
	public SerNodeVO inland(String name,String password)throws Exception{
		SerNodePO serNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		
		if(serNodePO == null){
			SerNodePO newserNodePO = new SerNodePO();
			newserNodePO.setNodeName(name);
			newserNodePO.setPassword(password);
			newserNodePO.setSourceType(SOURCE_TYPE.SYSTEM);
			serNodePO = newserNodePO;
		}
		String oldName = serNodePO.getNodeName();
		serNodePO.setNodeName(name);
		serNodePO.setPassword(password);
		serNodeDao.save(serNodePO);
		SerNodeVO serNodeVO = SerNodeVO.transFromPO(serNodePO);
		
		List<SerNodePO> serNodePOs = serNodeDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
		if (serNodePOs != null && !serNodePOs.isEmpty()) {
			for (SerNodePO forserNodePO : serNodePOs) {
				forserNodePO.setStatus(ConnectionStatus.OFF);
				forserNodePO.setOperate(ConnectionStatus.OFF);
			}
		}
		
		//发送消息
		PassByBO passByBO = new PassByBO();
		List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
		Map<String, Object> pass_by_content = makeAjaxData();
		pass_by_content.put("cmd", "localServerNodeNameChange");
		pass_by_content.put("oldName", oldName);
		pass_by_content.put("newName", name);
		passByBO.setPass_by_content(pass_by_content);
		if (workNodePOs != null && !workNodePOs.isEmpty()) {
			passByBO.setLayer_id(workNodePOs.get(0).getNodeUid());
		}
		tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
		return serNodeVO;
	}

	
	/**
	 * 查询外域信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:21:51
	 * @return List<SerNodeVO> 外域信息列表
	 */
	public List<SerNodeVO> queryOutland()throws Exception{
		List<SerNodePO> serNodePOs = serNodeDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
		List<SerNodeVO> serNodeVOs = SerNodeVO.transFromPOs(serNodePOs);
		if (serNodePOs != null && !serNodePOs.isEmpty()) {
			Set<Long> serNodeIds = new HashSet<Long>();
			for (SerNodePO serNodePO : serNodePOs) {
				serNodeIds.add(serNodePO.getId());
			}
			List<SerNodeRolePermissionPO> serNodeRolePermissionPOs = serNodeRolePermissionDAO.findBySerNodeIdIn(serNodeIds);
			Set<Long> roleIds = new HashSet<Long>();
			if(serNodeRolePermissionPOs != null && !serNodeRolePermissionPOs.isEmpty()){
				for (SerNodeRolePermissionPO serNodeRolePermissionPO : serNodeRolePermissionPOs) {
					roleIds.add(serNodeRolePermissionPO.getRoleId());
				}
				List<SystemRoleVO> systemRoleVOs= systemRoleQuery.queryAllRoles();
				for (SerNodeVO serNodeVO : serNodeVOs) {
					List<SystemRoleVO> folderSystemRoleVOs = new ArrayList<SystemRoleVO>();
					for (SerNodeRolePermissionPO serNodeRolePermissionPO : serNodeRolePermissionPOs) {
						if(serNodeVO.getId().equals(serNodeRolePermissionPO.getSerNodeId())){
							for (SystemRoleVO systemRoleVO : systemRoleVOs) {
								if(serNodeRolePermissionPO.getRoleId().toString().equals(systemRoleVO.getId())){
									folderSystemRoleVOs.add(systemRoleVO);
									break;
								}
							}
						}
					}
					serNodeVO.setBusinessRoles(JSON.toJSONString(folderSystemRoleVOs));
				}
			}
 		}
		return serNodeVOs;
	}
	
	/**
	 * 创建外域<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午3:10:25
	 * @param name 外域名称
	 * @param password 外域口令
	 * @param roleIds 外域绑定的角色id
	 * @return data(成功时返回外域信息，失败时返回错误信息)
	 */
	public Map<String, Object> addOutland(String name,String password,String roleIds)throws Exception{
		Map<String, Object> data= new HashMap<String, Object>();
		try {
			SerNodePO serNodePO = new SerNodePO();
			serNodePO.setNodeName(name);
			serNodePO.setPassword(password);
			serNodePO.setSourceType(SOURCE_TYPE.EXTERNAL);
			serNodePO.setOperate(ConnectionStatus.OFF);
			serNodePO.setStatus(ConnectionStatus.OFF);
			serNodeDao.save(serNodePO);
			List<Long> roleIDs = new ArrayList<Long>();
			String[] roleString = roleIds.split(",");
			for (int i = 0; i < roleString.length; i++) {
				Long roleId = Long.valueOf(roleString[i]);
				roleIDs.add(roleId);
			}
			List<SerNodeRolePermissionPO> serNodeRolePermissionPOs = new ArrayList<SerNodeRolePermissionPO>();
			if (roleIds != null && !roleIds.isEmpty()) {
				for (Long roleId : roleIDs) {
					SerNodeRolePermissionPO serNodeRolePermissionPO = new SerNodeRolePermissionPO();
					serNodeRolePermissionPO.setRoleId(roleId);
					serNodeRolePermissionPO.setSerNodeId(serNodePO.getId());
					serNodeRolePermissionPOs.add(serNodeRolePermissionPO);
				}
			}
			serNodeRolePermissionDAO.save(serNodeRolePermissionPOs);
			
			SerNodeVO serNodeVO = SerNodeVO.transFromPO(serNodePO);
			
			List<SerNodePO> localSerNodePO = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);
			SerNodeVO localSerNodeVO = new SerNodeVO();
			localSerNodeVO.setNodeName(localSerNodePO.get(0).getNodeName());
			
			//发送消息
			PassByBO passByBO = new PassByBO();
			List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
			Map<String, Object> pass_by_content = new HashMap<String, Object>();
			List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
			foreign.add(new HashMap<String, Object>());
			foreign.get(0).put("name", name);
			foreign.get(0).put("password", password);
			foreign.get(0).put("operate", ConnectionStatus.ON);
			pass_by_content.put("cmd", "foreignAdd");
			pass_by_content.put("local", localSerNodeVO);
			pass_by_content.put("foreign", foreign);
			passByBO.setPass_by_content(pass_by_content);
			if (workNodePOs != null && !workNodePOs.isEmpty()) {
				passByBO.setLayer_id(workNodePOs.get(0).getNodeUid());
			}
			tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
			
			data.put("serNodeVO", serNodeVO);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
			data.put(ERRMSG, "添加失败");
		}
		return data;
	}
	
	/**
	 * 查询外域设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午4:14:28
	 * @return 
	 * @throws Exception
	 */
	public Object queryOutlandBundle(Long serNodeId)throws Exception{
		SerNodePO serNodePO = serNodeDao.findOne(serNodeId);
//		List<BundlePO> bundlePOs = bundleDao.findByEquipFactInfo(serNodePO.getNodeName());
		List<BundlePO> bundlePOs = bundleDao.findAll();
		Set<Long> folderIds = new HashSet<Long>();
		if (bundlePOs != null && !bundlePOs.isEmpty()) {
			for (BundlePO bundlePO : bundlePOs) {
				folderIds.add(bundlePO.getFolderId()==null ? 0:bundlePO.getFolderId());
			}
		}
		List<FolderPO> folderPOs = folderDao.findByIdIn(folderIds);
		if (folderPOs != null && !folderPOs.isEmpty()) {
			for (FolderPO folderPO : folderPOs) {
				if (folderPO.getParentPath() != null && !folderPO.getParentPath().equals("")) {
					String[] parentPath = folderPO.getParentPath().split("/");
					for (int i = 1; i < parentPath.length; i++) {
						folderIds.add(Long.valueOf(parentPath[i]));
					}
				}
			}
		}
		List<FolderPO> relateFolderPOs = folderDao.findByIdIn(folderIds);
		List<FolderTreeVO> treefolder = initTree(relateFolderPOs);
		List<FolderTreeVO> folder = new ArrayList<FolderTreeVO>();
		if (folderPOs != null && !folderPOs.isEmpty()) {
			for (FolderPO folderPO : relateFolderPOs) {
				if (treefolder != null && !treefolder.isEmpty()) {
					for (FolderTreeVO folderTreeVO : treefolder) {
						if (folderPO.getId().equals(folderTreeVO.getId())) {
							folder.add(folderTreeVO);
						}
					}
				}
			}
		}
		return treefolder;
	}
	
	/**
	 * 断开外域连接<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 下午5:13:47
	 * @param id 外域id
	 * @return SerNodeVO 外域信息
	 */
	public SerNodeVO outlandOff (Long id)throws Exception{
		SerNodePO serNodePO = serNodeDao.findOne(id);
		List<SerNodePO> localSerNodePO = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);
		SerNodeVO localSerNodeVO = new SerNodeVO();
		Map<String, Object> local = new HashMap<String, Object>();
		localSerNodeVO.setNodeName(localSerNodePO.get(0).getNodeName());
		serNodePO.setOperate(ConnectionStatus.OFF);
		serNodeDao.save(serNodePO);
		SerNodeVO serNodeVO = new SerNodeVO();
		serNodeVO.setNodeName(serNodePO.getNodeName());
		serNodeVO.setOperate(serNodePO.getOperate());
		List<SerNodeVO> serNodeVOs = new ArrayList<SerNodeVO>();
		serNodeVOs.add(serNodeVO);
		
		//发送消息
		PassByBO passByBO = new PassByBO();
		List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
		Map<String, Object> pass_by_content = makeAjaxData();
		List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
		foreign.add(new HashMap<String, Object>());
		foreign.get(0).put("name",serNodeVO.getNodeName());
		foreign.get(0).put("operate", ConnectionStatus.OFF);
		local.put("name", localSerNodeVO.getNodeName());
		pass_by_content.put("cmd", "foreignOff");
		pass_by_content.put("local", local);
		pass_by_content.put("foreign", foreign);
		passByBO.setPass_by_content(pass_by_content);
		if (workNodePOs != null && !workNodePOs.isEmpty()) {
			passByBO.setLayer_id(workNodePOs.get(0).getNodeUid());
		}
		tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
		return null;
	}
	
	/**
	 * 修改外域信息（口令、外域名称）<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午10:08:52
	 * @param id 外域id
	 * @param name 外域新名称
	 * @param password 外域口令
	 * @return serNodeVO 外域信息
	 */
	public Object outlandChange(Long id,String name,String password,String roleIds)throws Exception{
		SerNodePO serNodePO = serNodeDao.findOne(id);
		String oldname  = serNodePO.getNodeName();
		List<SerNodePO> localSerNodePOs = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);
		serNodePO.setPassword(password);
		serNodePO.setNodeName(name);
		serNodePO.setOperate(ConnectionStatus.OFF);
		SerNodeVO serNodeVO = SerNodeVO.transFromPO(serNodePO);
		serNodeDao.save(serNodePO);
		
		List<SerNodeRolePermissionPO> oldserNodeRolePermissionPOs = serNodeRolePermissionDAO.findBySerNodeId(id);
		serNodeRolePermissionDAO.delete(oldserNodeRolePermissionPOs);
		
		List<Long> roleIDs = new ArrayList<Long>();
		String[] roleString = roleIds.split(",");
		for (int i = 0; i < roleString.length; i++) {
			Long roleId = Long.valueOf(roleString[i]);
			roleIDs.add(roleId);
		}
		List<SerNodeRolePermissionPO> serNodeRolePermissionPOs = new ArrayList<SerNodeRolePermissionPO>();
		if (roleIds != null && !roleIds.isEmpty()) {
			for (Long roleId : roleIDs) {
				SerNodeRolePermissionPO serNodeRolePermissionPO = new SerNodeRolePermissionPO();
				serNodeRolePermissionPO.setRoleId(roleId);
				serNodeRolePermissionPO.setSerNodeId(serNodePO.getId());
				serNodeRolePermissionPOs.add(serNodeRolePermissionPO);
			}
		}
		serNodeRolePermissionDAO.save(serNodeRolePermissionPOs);
		
		//发送消息
		PassByBO passByBO = new PassByBO();
		List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
		Map<String, Object> pass_by_content = makeAjaxData();
		Map<String, Object> local = new HashMap<String, Object>();
		local.put("name", localSerNodePOs.get(0).getNodeName());
		List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
		foreign.add(new HashMap<String, Object>());
		foreign.get(0).put("oldName", oldname);
		foreign.get(0).put("newName", name);
		foreign.get(0).put("password", password);
		foreign.get(0).put("operate", ConnectionStatus.OFF);
		
		pass_by_content.put("cmd", "foreignEdit");
		pass_by_content.put("local", local);
		pass_by_content.put("foreign", foreign);
		passByBO.setPass_by_content(pass_by_content);
		if (workNodePOs != null && !workNodePOs.isEmpty()) {
			passByBO.setLayer_id(workNodePOs.get(0).getNodeUid());
		}
		tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
		return serNodeVO;
	}
	
	
	/**
	 * 连接外域<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午10:21:39
	 * @param id 外域id
	 * @return serNodeVO 外域信息
	 */
	public Object outlandOn(Long id)throws Exception{
		SerNodePO serNodePO = serNodeDao.findOne(id);
		serNodePO.setOperate(ConnectionStatus.ON);
		List<SerNodePO> localSerNodePOs = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);
		serNodeDao.save(serNodePO);
		SerNodeVO serNodeVO = SerNodeVO.transFromPO(serNodePO);
		
		//发送消息
		PassByBO passByBO = new PassByBO();
		List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
		Map<String, Object> pass_by_content = new HashMap<String, Object>();
		Map<String, Object> local = new HashMap<String, Object>();
		local.put("name", localSerNodePOs.get(0).getNodeName());
		List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
		foreign.add(new HashMap<String, Object>());
		foreign.get(0).put("name", serNodePO.getNodeName());
		foreign.get(0).put("operate", ConnectionStatus.ON);
		pass_by_content.put("cmd", "foreignOn");
		pass_by_content.put("local", local);
		pass_by_content.put("foreign", foreign);
		passByBO.setPass_by_content(pass_by_content);
		if (workNodePOs != null && !workNodePOs.isEmpty()) {
			passByBO.setLayer_id(workNodePOs.get(0).getNodeUid());
		}
		tetrisDispatchService.dispatch(new ArrayListWrapper<PassByBO>().add(passByBO).getList());
		return serNodeVO;
	}
	
	/**
	 * 删除外域<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 上午10:59:22
	 * @param id 外域id
	 */
	public Object outlandDelete(Long id)throws Exception{
		SerNodePO serNodePO = serNodeDao.findOne(id);
		if (serNodePO != null) {
			serNodeDao.delete(serNodePO);
		}
		return null;
	}
	
	/**
	 * 外域设备授权本域用户查询<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 下午3:15:36
	 * @param roleId 角色id
	 * @param serNodeId 外域id
	 * @param deviceModel 设备类型
	 * @param keyword 关键字
	 * @param folderId 目录id
	 * @param pageNum 页码
	 * @param countPerPage 每页数量
	 * @return 
	 */
	public Map<String,Object> queryOutlandBundlePrivilege(
			Long roleId,
			Long serNodeId, 
			String deviceModel, 
			String keyword, 
			Long folderId, 
			int pageNum, 
			int countPerPage) throws Exception{
		Map<String, Object> data = makeAjaxData();
		try {
			List<BundlePrivilegeBO> bundlePrivileges = new ArrayList<BundlePrivilegeBO>();
			SerNodePO serNodePO = serNodeDao.findOne(serNodeId);
			Set<String> bundleIdsall = bundleService.queryBundleSetByMultiParams(deviceModel, SOURCE_TYPE.EXTERNAL.toString(), keyword, folderId);
			Set<String> bundleIds = new HashSet<String>();
			List<BundlePO> bundlePOs = bundleDao.findByEquipFactInfo(serNodePO.getNodeName());
			Set<String> reeourceStrings = new HashSet<String>();
			if (bundlePOs !=  null&&!bundlePOs.isEmpty()) {
				for (BundlePO bundlePO : bundlePOs) {
					bundleIds.add(bundlePO.getBundleId());
				}
				bundleIdsall.retainAll(bundleIds);
				List<BundlePO> selectBundlePOs = bundleDao.findByBundleIdIn(bundleIdsall);
				if (selectBundlePOs != null && !selectBundlePOs.isEmpty() ) {
					for (BundlePO bundlePO : selectBundlePOs) {
						reeourceStrings.add(bundlePO.getBundleId() + "-r");
						reeourceStrings.add(bundlePO.getBundleId() + "-w");
						reeourceStrings.add(bundlePO.getBundleId() + "-c");
						reeourceStrings.add(bundlePO.getBundleId() + "-lr");
						reeourceStrings.add(bundlePO.getBundleId() + "-d");
					}
					List<PrivilegePO> privilegePOs = privilegeDAO.findByResourceIndentityIn(reeourceStrings);
					Set<String> resourcePrivilege = new HashSet<String>();
					if (privilegePOs != null && !privilegePOs.isEmpty()) {
						for (PrivilegePO privilegePO : privilegePOs) {
							resourcePrivilege.add(privilegePO.getResourceIndentity());
						}
					}
					ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
					if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
						Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
						for (BundlePO bundlePO : selectBundlePOs) {
							boolean hasReadPrivilege = privilegeCodes.contains(bundlePO.getBundleId() + "-r");
							boolean hasWritePrivilege = privilegeCodes.contains(bundlePO.getBundleId() + "-w");
							boolean hasCloudPrivilege = privilegeCodes.contains(bundlePO.getBundleId() + "-c");
							boolean hasLocalReadPrivilege = privilegeCodes.contains(bundlePO.getBundleId() + "-lr");
							boolean hasDownloadPrivilege = privilegeCodes.contains(bundlePO.getBundleId() + "-d");
							
							boolean canReadPrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-r");
							boolean canWritePrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-w");
							boolean canCloudPrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-c");
							boolean canLocalReadPrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-lr");
							boolean canDownloadPrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-d");
							BundlePrivilegeBO bundlePrivilege = getBundlePrivilegefromPO(bundlePO);
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
							//-------------*-*---------------------
							if (canReadPrivilege) {
								bundlePrivilege.setCanReadPrivilege(true);;
							}
							if (canWritePrivilege) {
								bundlePrivilege.setCanWritePrivilege(true);
							}
							if (canCloudPrivilege) {
								bundlePrivilege.setCanCloudPrivilege(true);
							}
							if (canLocalReadPrivilege) {
								bundlePrivilege.setCanLocalReadPrivilege(true);
							}
							if (canDownloadPrivilege) {
								bundlePrivilege.setCanDownloadPrivilege(true);
							}
							bundlePrivileges.add(bundlePrivilege);
						}
					}else {
						for (BundlePO bundlePO : selectBundlePOs) {
							boolean canReadPrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-r");
							boolean canWritePrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-w");
							boolean canCloudPrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-c");
							boolean canLocalReadPrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-lr");
							boolean canDownloadPrivilege = privilegePOs.contains(bundlePO.getBundleId() + "-d");
							
							BundlePrivilegeBO bundlePrivilege = getBundlePrivilegefromPO(bundlePO);
							if (canReadPrivilege) {
								bundlePrivilege.setCanReadPrivilege(true);;
							}
							if (canWritePrivilege) {
								bundlePrivilege.setCanWritePrivilege(true);
							}
							if (canCloudPrivilege) {
								bundlePrivilege.setCanCloudPrivilege(true);
							}
							if (canLocalReadPrivilege) {
								bundlePrivilege.setCanLocalReadPrivilege(true);
							}
							if (canDownloadPrivilege) {
								bundlePrivilege.setCanDownloadPrivilege(true);
							}
							bundlePrivileges.add(bundlePrivilege);
						}
					}
					return bundlePageResponse(pageNum, countPerPage, bundlePrivileges);
				}
				
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "查询错误");
			return data;
		}
		
		return data;
	}
 	
	/**
	 * 外域设备授权本域用户修改<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 下午5:11:29
	 * @param roleId 角色id
	 * @param preBundlePrivilege 修改前的权限状态 
	 * @param bundleprivilege 修改后的权限状态
	 * @return data 错误信息
	 */
	public Map<String, Object> submitBundlePrivilege(Long roleId, List<String> preBundlePrivilege, List<String> bundleprivilege)throws Exception{
		Map<String, Object> data = makeAjaxData();
		try {
			if (preBundlePrivilege != null && !preBundlePrivilege.isEmpty()) {
				List<String> unbindprivilege = preBundlePrivilege;
				unbindprivilege.removeAll(bundleprivilege);
				List<RolePrivilegeMap> unbindPermission = rolePrivilegeMapDAO.findByRoleIdAndResourceIdIn(roleId, unbindprivilege);
				rolePrivilegeMapDAO.delete(unbindPermission);
			}
			if (bundleprivilege != null && !bundleprivilege.isEmpty()) {
				List<String> bindPrivilege = bundleprivilege;
				bindPrivilege.removeAll(preBundlePrivilege);
				
				List<RolePrivilegeMap> newbindPermission = new ArrayList<RolePrivilegeMap>();
				List<PrivilegePO> privilegePOs = privilegeDAO.findByResourceIndentityIn(bindPrivilege);
				if (privilegePOs != null && !privilegePOs.isEmpty()) {
					for (PrivilegePO privilegePO : privilegePOs) {
						RolePrivilegeMap rolePrivilegeMap = new RolePrivilegeMap();
						rolePrivilegeMap.setPrivilegeId(privilegePO.getId());
						rolePrivilegeMap.setRoleId(roleId);
						newbindPermission.add(rolePrivilegeMap);
					}
				}
				rolePrivilegeMapDAO.save(newbindPermission);
			}
		} catch (Exception e) {
			LOGGER.error("", e);
			data.put(ERRMSG, "权限修改失败");
			return data;
		}
		return data;
	}
	
	private List<FolderTreeVO> initTree(List<FolderPO> relateFolderPOs){
		Map<String, Object> data = makeAjaxData();
		List<FolderPO> rootFolders = new ArrayList<FolderPO>();;
		if (relateFolderPOs != null && !relateFolderPOs.isEmpty()) {
			for (FolderPO folderPO : relateFolderPOs) {
				if (folderPO.getParentPath() == null || folderPO.getParentPath().equals("")) {
					rootFolders.add(folderPO);
				}
			}
		}
		List<FolderTreeVO> tree = new LinkedList<FolderTreeVO>();
		if (rootFolders != null && !rootFolders.isEmpty()) {
			for (FolderPO rootFolderPO : rootFolders) {
				FolderTreeVO rootTreeVO = createFolderNodeFromFolderPO(rootFolderPO);
				rootTreeVO.setChildren(createChildrenTreeNodes(rootFolderPO.getId()));
				tree.add(rootTreeVO);
			}
		}
		
		data.put("tree", tree);
		return tree;
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
	
	private ResourceIdListBO queryResourceByRoleId(Long roleId) throws Exception{
		
		List<PrivilegePO> privileges = privilegeDAO.findByRoleId(roleId);
		
		ResourceIdListBO bo = new ResourceIdListBO();
		bo.setResourceCodes(new ArrayList<String>());
		for(PrivilegePO privilege: privileges){
			bo.getResourceCodes().add(privilege.getResourceIndentity());
		}
		
		return bo;
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

	
	public static void  main(String[] args){
		
 	}
}