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
import com.suma.venus.resource.controller.ControllerBase;
import com.suma.venus.resource.controller.FolderManageController;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.dao.SerNodeRolePermissionDAO;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
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
	
	/**
	 * 创建本域<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 下午4:38:02
	 * @param name 本域名称
	 * @param password 本域口令
	 * @throws Exception
	 */
	public SerNodeVO createrInland(String name,String password)throws Exception{
		SerNodePO serNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		SerNodePO newserNodePO = new SerNodePO();
		if (serNodePO == null) {
			newserNodePO.setNodeName(name);
			newserNodePO.setPassword(password);
			newserNodePO.setSourceType(SOURCE_TYPE.SYSTEM);
			serNodeDao.save(newserNodePO);
		}else throw new Exception("仅能存在一个本域信息");
		SerNodeVO serNodeVO = SerNodeVO.transFromPO(newserNodePO);
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
		String oldName = serNodePO.getNodeName();
		serNodePO.setNodeName(name);
		serNodePO.setPassword(password);
		serNodeDao.save(serNodePO);
		SerNodeVO serNodeVO = SerNodeVO.transFromPO(serNodePO);
		
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
					List<SystemRoleVO> bindRoles = new ArrayList<SystemRoleVO>();
					for (SerNodeRolePermissionPO serNodeRolePermissionPO : serNodeRolePermissionPOs) {
						if(serNodeVO.getId().equals(serNodeRolePermissionPO.getSerNodeId())){
							for (SystemRoleVO systemRoleVO : systemRoleVOs) {
								if(serNodeRolePermissionPO.getRoleId().equals(systemRoleVO.getId())){
									bindRoles.add(systemRoleVO);
									break;
								}
							}
						}
					}
					serNodeVO.setBusinessRoles(JSON.toJSONString(bindRoles));
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
	 * @return data(成功时返回外域名称，失败时返回错误信息)
	 */
	public Map<String, Object> addOutland(String name,String password,List<Long> roleIds)throws Exception{
		Map<String, Object> data= new HashMap<String, Object>();
		try {
			SerNodePO serNodePO = new SerNodePO();
			serNodePO.setNodeName(name);
			serNodePO.setPassword(password);
			serNodePO.setOperate(ConnectionStatus.OFF);;
			serNodeDao.save(serNodePO);
			List<SerNodeRolePermissionPO> serNodeRolePermissionPOs = new ArrayList<SerNodeRolePermissionPO>();
			if (roleIds != null && !roleIds.isEmpty()) {
				for (Long roleId : roleIds) {
					SerNodeRolePermissionPO serNodeRolePermissionPO = new SerNodeRolePermissionPO();
					serNodeRolePermissionPO.setRoleId(roleId);
					serNodeRolePermissionPO.setSerNodeId(serNodePO.getId());
					serNodeRolePermissionPOs.add(serNodeRolePermissionPO);
				}
			}
			serNodeRolePermissionDAO.save(serNodeRolePermissionPOs);
			
			List<SerNodePO> localSerNodePO = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);
			SerNodeVO localSerNodeVO = new SerNodeVO();
			localSerNodeVO.setNodeName(localSerNodePO.get(0).getNodeName());
			
			//发送消息
			PassByBO passByBO = new PassByBO();
			List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
			Map<String, Object> pass_by_content = new HashMap<String, Object>();
			List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
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
			
			data.put("OutlandName", serNodePO.getNodeName());
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
		List<BundlePO> bundlePOs = bundleDao.findByEquipFactInfo(serNodePO.getNodeName());
		Set<Long> folderIds = new HashSet<Long>();
		if (bundlePOs != null && !bundlePOs.isEmpty()) {
			for (BundlePO bundlePO : bundlePOs) {
				folderIds.add(bundlePO.getFolderId());
			}
		}
		List<FolderPO> folderPOs = folderDao.findByIdIn(folderIds);
		List<FolderTreeVO> treefolder = initTree();
		List<FolderTreeVO> folder = new ArrayList<FolderTreeVO>();
		if (folderPOs != null && !folderPOs.isEmpty()) {
			for (FolderPO folderPO : folderPOs) {
				if (treefolder != null && !treefolder.isEmpty()) {
					for (FolderTreeVO folderTreeVO : treefolder) {
						if (folderPO.getId().equals(folderTreeVO.getId())) {
							folder.add(folderTreeVO);
						}
					}
				}
			}
		}
		return folder;
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
	public Object outlandChange(Long id,String name,String password)throws Exception{
		SerNodePO serNodePO = serNodeDao.findOne(id);
		String oldname  = serNodePO.getNodeName();
		List<SerNodePO> localSerNodePOs = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);
		serNodePO.setPassword(password);
		serNodePO.setOperate(ConnectionStatus.ON);
		SerNodeVO serNodeVO = SerNodeVO.transFromPO(serNodePO);
		serNodeDao.save(serNodePO);
		
		//发送消息
		PassByBO passByBO = new PassByBO();
		List<WorkNodePO> workNodePOs = workNodeDao.findByType(NodeType.ACCESS_QTLIANGWANG);
		Map<String, Object> pass_by_content = makeAjaxData();
		Map<String, Object> local = new HashMap<String, Object>();
		local.put("name", localSerNodePOs.get(0).getNodeName());
		List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
		foreign.get(0).put("oldName", oldname);
		foreign.get(0).put("newName", name);
		foreign.get(0).put("password", password);
		foreign.get(0).put("operate", ConnectionStatus.ON);
		
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
		foreign.get(0).put("name", serNodePO.getNodeName());
		foreign.get(0).put("operate", ConnectionStatus.ON);
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
	
	
	private List<FolderTreeVO> initTree(){
		Map<String, Object> data = makeAjaxData();
		List<FolderPO> rootFolders = folderService.findByParentPath(null);
		if (rootFolders.isEmpty()) {
			data.put(ERRMSG, "数据库错误：不存在根节点");
		}
//		FolderPO rootFolderPO = rootFolders.get(0);
		List<FolderTreeVO> tree = new LinkedList<FolderTreeVO>();
		for (FolderPO rootFolderPO : rootFolders) {
			FolderTreeVO rootTreeVO = createFolderNodeFromFolderPO(rootFolderPO);
			rootTreeVO.setChildren(createChildrenTreeNodes(rootFolderPO.getId()));
			tree.add(rootTreeVO);
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

			// 添加子用户节点
//			try {
//				FolderPO folderPO = folderDao.findOne(parentId);
//				/**Map<String, List<UserBO>> usersMap = userFeign.queryUsersByFolderUuid(folderPO.getUuid());
//				for (UserBO userBO : usersMap.get("users")) {
//					children.add(createUserNode(parentId, userBO));
//				}*/
//				List<FolderUserMap> userBOs = folderUserMapDao.findByFolderUuidOrderByFolderIndex(folderPO.getUuid());
//				for (FolderUserMap userBO : userBOs) {
//					children.add(createUserNode(parentId, userBO));
//				}
//			} catch (Exception e) {
//				LOGGER.error("", e);
//			}

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
}
