package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.base.bo.UnbindResouceBO;
import com.suma.venus.resource.base.bo.UnbindRolePrivilegeBO;
import com.suma.venus.resource.base.bo.UnbindUserPrivilegeBO;
import com.suma.venus.resource.base.bo.UserAndResourceIdBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.bo.DeviceInfoBO;
import com.suma.venus.resource.bo.PrivilegeStatusBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.dao.SerInfoDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.lianwang.auth.AuthNotifyXml;
import com.suma.venus.resource.lianwang.auth.DevAuthXml;
import com.suma.venus.resource.lianwang.auth.UserAuthXml;
import com.suma.venus.resource.lianwang.status.DeviceStatusXML;
import com.suma.venus.resource.lianwang.status.NotifyRouteLinkXml;
import com.suma.venus.resource.lianwang.status.NotifyUserDeviceXML;
import com.suma.venus.resource.lianwang.status.StatusXMLUtil;
import com.suma.venus.resource.lianwang.status.UserStatusXML;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.suma.venus.resource.service.router.RouterService;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.SerInfoPO;
import com.suma.venus.resource.pojo.SerInfoPO.SerInfoType;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.util.XMLBeanUtils;
import com.suma.venus.resource.vo.NodeInfoVO;
import com.suma.venus.resource.vo.NodeVO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.system.role.SystemRoleVO;

/**
 * 联网新功能相关处理Service<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月31日 下午3:37:57
 */
@Service
public class ResourceRemoteService {
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private StatusXMLUtil statusXMLUtil;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private SerInfoAndNodeService serInfoAndNodeService;
	
	@Autowired
	private SerInfoDao serInfoDao;
	
	@Autowired
	private SerNodeDao serNodeDao;
	
	@Autowired
	private WorkNodeDao workNodeDao;
	
	@Autowired
	private RouterService routerService;
	
	@Autowired
	private UserQueryService userService;

	/**
	 * 获取联网接入id<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午1:23:28
	 * @return String 联网接入id
	 */
	public String queryLocalLayerId() throws Exception{
		
		WorkNodePO workNode = workNodeDao.findTopByType(NodeType.ACCESS_LIANWANG);
		if(workNode == null || workNode.getNodeUid() == null){
			throw new BaseException(StatusCode.ERROR, "联网接入未注册！");
		}
		
		return workNode.getNodeUid();
	}
	
	/**
	 * 查询设备和用户所在应用单元的号码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午2:01:04
	 * @param List<DeviceInfoBO> infos 设备和用户号码信息
	 * @return List<String> 应用单元号码列表
	 */
	public List<String> querySerNodeList(List<DeviceInfoBO> infos) throws Exception{
		
		List<String> userCodes = new ArrayList<String>();
		List<String> deviceCodes = new ArrayList<String>();
		for(DeviceInfoBO info: infos){
			if(info.getType().equals("device")){
				deviceCodes.add(info.getCode());
			}else if(info.getType().equals("user")){
				userCodes.add(info.getCode());
			}
		}
		
		
		List<String> nodeUuids = new ArrayList<String>();
		if(deviceCodes.size() > 0){
			List<BundlePO> bundles = bundleDao.findByUsernameIn(deviceCodes);
			for(BundlePO bundle: bundles){
				if(bundle.getEquipNode() != null){
					if(!nodeUuids.contains(bundle.getEquipNode())){
						nodeUuids.add(bundle.getEquipNode());
					}
				}
			}
		}
		
		if(userCodes.size() > 0){
			List<FolderUserMap> userMaps = folderUserMapDao.findByUserNoIn(userCodes);
			for(FolderUserMap map: userMaps){
				if(map.getUserNode() != null){
					if(!nodeUuids.contains(map.getUserNode())){
						nodeUuids.add(map.getUserNode());
					}
				}
			}
		}
		
		List<SerInfoPO> serInfos = serInfoDao.findBySerNodeInAndSerTypeAndSourceType(nodeUuids, SerInfoType.APPLICATION.getNum(), SOURCE_TYPE.EXTERNAL);
		List<String> apps = new ArrayList<String>();
		for(SerInfoPO serInfo: serInfos){
			apps.add(serInfo.getSerNo());
		}
		
		return apps;
	}	
	
	/**
	 * 联网发送信息同步命令信息处理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午10:56:13
	 * @param requestJson
	 */
	public void notifyXml(String cmd, String xml) throws Exception{

		// 获取联网发来的notify消息
		if ("syncinfo".equals(cmd)) {
			NotifyUserDeviceXML xmlBean = XMLBeanUtils.xmlToBean(xml, NotifyUserDeviceXML.class);
			List<DeviceStatusXML> devlist = xmlBean.getDevlist();
			List<BundlePO> toUpdateBundles = new ArrayList<BundlePO>();
			// 更新设备在线状态
			if (null != devlist) {
				for (DeviceStatusXML dev : devlist) {
					BundlePO bundle = bundleDao.findByUsername(dev.getDevid());
					if (null != bundle) {
						ONLINE_STATUS status = dev.getStatus() == 1 ? ONLINE_STATUS.ONLINE
								: ONLINE_STATUS.OFFLINE;
						if (status != bundle.getOnlineStatus()) {
							bundle.setOnlineStatus(status);
							toUpdateBundles.add(bundle);
						}
					}
				}
			}
			if (!toUpdateBundles.isEmpty()) {
				bundleDao.save(toUpdateBundles);
			}

			// 更新对应用户的在线状态
			List<UserStatusXML> userlist = xmlBean.getUserlist();
			List<UserBO> userBOs = new ArrayList<UserBO>();
			if (null != userlist) {
				for (UserStatusXML userXml : userlist) {
					userBOs.add(statusXMLUtil.toUserBO(userXml));
				}
			}
			if (!userBOs.isEmpty()) {
				//TODO:更新用户状态--现在先放在folderUserMap里面
				List<String> userNos = new ArrayList<String>();
				for(UserBO userBO: userBOs){
					userNos.add(userBO.getUserNo());
				}
				List<FolderUserMap> maps = folderUserMapDao.findByUserNoIn(userNos);
				for(UserBO userBO: userBOs){
					for(FolderUserMap map: maps){
						if(map.getUserNo().equals(userBO.getUserNo())){
							map.setUserStatus(userBO.isLogined());
							break;
						}
					}
				}
				folderUserMapDao.save(maps);
			}
		}

		if ("syncroutelink".equals(cmd)) {
			NotifyRouteLinkXml xmlBean = XMLBeanUtils.xmlToBean(xml, NotifyRouteLinkXml.class);
			//更新路由
			serInfoAndNodeService.updateSerNode(xmlBean);
		}
		
		if ("authnotify".equals(cmd)) {
			
			AuthNotifyXml authNotifyXml = XMLBeanUtils.xmlToBean(xml, AuthNotifyXml.class);
			String userNo = authNotifyXml.getUserid();
			UserBO userBO = userService.queryUserByUserNo(userNo);
			if (PrivilegeStatusBO.OPR_ADD.equals(authNotifyXml.getOperation())) {
				bindUserPrivilegeFromXml(authNotifyXml, userBO.getId());
			} else if (PrivilegeStatusBO.OPR_REMOVE.equals(authNotifyXml.getOperation())) {
				unbindUserPrivilegeFromXml(authNotifyXml, userBO.getId());
			} else if (PrivilegeStatusBO.OPR_EDIT.equals(authNotifyXml.getOperation())) {
				// 修改第一步清除旧权限，第二部绑定新权限
				unbindUserPrivilegeFromXml(authNotifyXml, userBO.getId());
				bindUserPrivilegeFromXml(authNotifyXml, userBO.getId());
			}
			
		}
	}
	
	/**
	 * 根据xml信息绑定授权<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午6:17:18
	 * @param AuthNotifyXml authNotifyXml
	 * @param Long userId
	 * @throws Exception 
	 */
	public void bindUserPrivilegeFromXml(AuthNotifyXml authNotifyXml, Long userId) throws Exception {
		RoleAndResourceIdBO roleAndResourceIdBO = new RoleAndResourceIdBO();
		roleAndResourceIdBO.setResourceCodes(new ArrayList<String>());
		//查询私有角色
		SystemRoleVO role = userService.queryPrivateRoleId(userId); 
		roleAndResourceIdBO.setRoleId(Long.valueOf(role.getId()));
		for (DevAuthXml devAuthXml : authNotifyXml.getDevlist()) {
			String devId = devAuthXml.getDevid();
			BundlePO bundle = bundleDao.findByUsername(devId);
			if (null == bundle) {
				continue;
			}
			if ("1".equals(devAuthXml.getAuth().substring(0, 1))) {
				roleAndResourceIdBO.getResourceCodes().add(bundle.getBundleId() + "-w");
			}
			if ("1".equals(devAuthXml.getAuth().substring(1, 2))) {
				roleAndResourceIdBO.getResourceCodes().add(bundle.getBundleId() + "-r");
			}
		}
		for (UserAuthXml userAuthXml : authNotifyXml.getUserlist()) {
			String authUserNo = userAuthXml.getUserid();
			// UserBO authUserBO =
			// userFeign.queryUserInfoByUserNo(authUserNo).get("user");
			if ("1".equals(userAuthXml.getAuth().substring(0, 1))) {
				roleAndResourceIdBO.getResourceCodes().add(authUserNo + "-w");
			}
			if ("1".equals(userAuthXml.getAuth().subSequence(1, 2))) {
				roleAndResourceIdBO.getResourceCodes().add(authUserNo + "-hj");
			}
			if ("1".equals(userAuthXml.getAuth().subSequence(2, 3))) {
				roleAndResourceIdBO.getResourceCodes().add(authUserNo + "-zk");
			}
			if ("1".equals(userAuthXml.getAuth().subSequence(4, 5))) {
				roleAndResourceIdBO.getResourceCodes().add(authUserNo + "-r");
			}
		}
		
		// 绑定权限
		userService.bindRolePrivilege(roleAndResourceIdBO);
	}

	/**
	 * 根据xml解绑授权<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午6:18:22
	 * @param authNotifyXml
	 * @param userId
	 * @throws Exception 
	 */
	public void unbindUserPrivilegeFromXml(AuthNotifyXml authNotifyXml, Long userId) throws Exception {
		UnbindRolePrivilegeBO unbindRolePrivilegeBO = new UnbindRolePrivilegeBO();
		unbindRolePrivilegeBO.setUnbindPrivilege(new ArrayList<UnbindResouceBO>());
		//查询私有角色
		SystemRoleVO role = userService.queryPrivateRoleId(userId); 
		unbindRolePrivilegeBO.setRoleId(Long.valueOf(role.getId()));
		for (DevAuthXml devAuthXml : authNotifyXml.getDevlist()) {
			String devId = devAuthXml.getDevid();
			BundlePO bundle = bundleDao.findByUsername(devId);
			if (null == bundle) {
				continue;
			}
			unbindRolePrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(bundle.getBundleId() + "-w", false));
			unbindRolePrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(bundle.getBundleId() + "-r", false));
		}
		for (UserAuthXml userAuthXml : authNotifyXml.getUserlist()) {
			String authUserNo = userAuthXml.getUserid();
			unbindRolePrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(authUserNo + "-w", false));
			unbindRolePrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(authUserNo + "-r", false));
			unbindRolePrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(authUserNo + "-hj", false));
			unbindRolePrivilegeBO.getUnbindPrivilege().add(new UnbindResouceBO(authUserNo + "-zk", false));
		}
		userService.unbindRolePrivilege(unbindRolePrivilegeBO);
	}
	
	/**
	 * 更新路由信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 上午9:46:54
	 * @param String app_num 应用号码
	 * @param String status 上下线状态
	 */
	public void updateRouter(String app_num, String status) throws Exception{
		
		SerNodePO other_node = serNodeDao.findByApplicationId(app_num);
		List<SerNodePO> nodes = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);
		
		if(nodes != null && nodes.size() > 0 && other_node != null){
			
			SerNodePO local_node = nodes.get(0);
			List<String> routers = JSONArray.parseArray(local_node.getNodeRouter(), String.class);
			if(routers == null){
				routers = new ArrayList<String>();
			}
			
			if(status.equals("online")){
				if(!routers.contains(other_node.getNodeUuid())){
					routers.add(other_node.getNodeUuid());
				}
			}else if(status.equals("offline")){
				if(routers.contains(other_node.getNodeUuid())){
					routers.remove(other_node.getNodeUuid());
				}
			}
			local_node.setNodeRouter(JSON.toJSONString(routers));
			serNodeDao.save(local_node);
			
			//同步路由 -- 联网主动来获取
			/*List<SerNodePO> serNodePOs = serNodeDao.findAll();
			statusXMLUtil.createRouteLinkXml(serNodePOs);*/
			
		}
	}
	
	/**
	 * 查询服务节点<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:51:43
	 * @param String layer_id
	 * @param String seq
	 * @param String cmd
	 * @return NodeInfoVO
	 */
	public NodeInfoVO queryNodeInfo(String layer_id, String seq, String cmd) throws Exception{
	
		List<WorkNodePO> layers = workNodeDao.findByType(NodeType.ACCESS_LIANWANG);
		WorkNodePO lianWang = null;
		if(layers != null && layers.size() > 0){
			lianWang = layers.get(0);
			lianWang.setNodeUid(layer_id);
		}else{
			lianWang = new WorkNodePO();
			lianWang.setNodeUid(layer_id);
			lianWang.setName("联网接入");
			lianWang.setType(NodeType.ACCESS_LIANWANG);
		}
		
		workNodeDao.save(lianWang);
		
		NodeInfoVO nodeInfo = new NodeInfoVO();
		nodeInfo.setCmd(cmd)
				.setLayer_id(layer_id)
				.setSeq(seq);
		
		SerNodePO serNode = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		if(serNode != null){
			
			List<SerNodePO> allNodes = serNodeDao.findAll();
			List<String> allList = new ArrayList<String>();
			List<String> fatherList = new ArrayList<String>();
			List<String> relationList = new ArrayList<String>();
			List<String> sonList = new ArrayList<String>();
			if(serNode.getNodeFather() != null && !serNode.getNodeFather().equals("NULL")){
				String[] fatherNodes = serNode.getNodeFather().split(",");
				fatherList = Arrays.asList(fatherNodes);
				
			}
			if(serNode.getNodeRelations() != null && !serNode.getNodeRelations().equals("NULL")){
				String[] relationNodes = serNode.getNodeRelations().split(",");
				relationList = Arrays.asList(relationNodes);
			}
			
			for(SerNodePO node: allNodes){
				if(node.getNodeFather() != null && !node.getNodeFather().equals("NULL")){
					String[] fatherNodes = node.getNodeFather().split(",");
					List<String> _fathers = Arrays.asList(fatherNodes);
					if(_fathers.contains(serNode.getNodeUuid())){
						sonList.add(node.getNodeUuid());
					}
				}
			}
			
			allList.add(serNode.getNodeUuid());
			allList.addAll(fatherList);
			allList.addAll(relationList);
			allList.addAll(sonList);
			
			List<SerInfoPO> infos = serInfoDao.findBySerNodeIn(allList);
			
			NodeVO self = generateNodeVO(serNode.getNodeUuid(), infos);
			nodeInfo.setSelf(self);
			if(fatherList.size() > 0){
				nodeInfo.setSupers(new ArrayList<NodeVO>());
				for(String father: fatherList){
					NodeVO fatherNode = generateNodeVO(father, infos);
					if(fatherNode != null){
						nodeInfo.getSupers().add(fatherNode);
					}
				}
				if(nodeInfo.getSupers().size() > 0){
					nodeInfo.getSupers().get(0).setIs_publish(true);
				}
			}
			if(relationList.size() > 0){
				nodeInfo.setRelations(new ArrayList<NodeVO>());
				for(String relation: relationList){
					NodeVO relationNode = generateNodeVO(relation, infos);
					if(relationNode != null){
						nodeInfo.getRelations().add(relationNode);
					}
				}
			}
			if(sonList.size() > 0){
				nodeInfo.setSubors(new ArrayList<NodeVO>());
				for(String son: sonList){
					NodeVO sonNode = generateNodeVO(son, infos);
					if(sonNode != null){
						nodeInfo.getSubors().add(sonNode);
					}
				}
			}
			
		}else{
			throw new BaseException(StatusCode.ERROR, "本服务节点不存在");
		}
		
		return nodeInfo;
	}
	
	/**
	 * 查询路由下一跳<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 上午9:46:37
	 * @param String seq 命令标识号
	 * @param String cmd query_next_node
	 * @param String no 待查询的号码
	 * @param String type all, device, user, app
	 * @return JSONObject response
	 */
	public JSONObject queryNextRouterNode(String seq, String cmd, String no, String type) throws Exception{
		
		JSONObject response = new JSONObject();
		response.put("seq", seq);
		response.put("cmd", cmd);
		response.put("no", no);
		
		List<SerNodePO> allNodes = serNodeDao.findAll();
		SerNodePO self = querySelf(allNodes);
		SerNodePO end = null;
		String nodeUuid = null;
		if(type.equals("device")){
			
			BundlePO bundle = bundleDao.findByUsername(no);
			if(bundle != null){
				nodeUuid = bundle.getEquipNode();
			}
			
		}else if(type.equals("user")){
			
			FolderUserMap userMap = folderUserMapDao.findByUserNo(no);
			if(userMap != null){
				nodeUuid = userMap.getUserNode(); 
			}
			
		}else if(type.equals("app")){
			
			SerInfoPO serInfo = serInfoDao.findBySerNo(no);
			if(serInfo != null){
				nodeUuid = serInfo.getSerNode();
			}
			
		}else if(type.equals("all")){
			
			BundlePO bundle = bundleDao.findByUsername(no);
			if(bundle != null){
				//设备
				nodeUuid = bundle.getEquipNode();
			}else{
				FolderUserMap userMap = folderUserMapDao.findByUserNo(no);
				if(userMap != null){
					//用户
					nodeUuid = userMap.getUserNode(); 
				}else {
					SerInfoPO serInfo = serInfoDao.findBySerNo(no);
					if(serInfo != null){
						//应用
						nodeUuid = serInfo.getSerNode();
					}
				}
			}
		}
		
		if(nodeUuid == null){
			throw new BaseException(StatusCode.ERROR, "设备所属节点不存在！");
		}
		
		end = queryByUuid(allNodes, nodeUuid);
		
		String router = routerService.queryReasonableRouter(allNodes, self, end);
		if(router == null){
			response.put("next_sig_no", "");
		}else {
			String next = router.split(",")[1];
			SerInfoPO signal = serInfoDao.findBySerNodeAndSerType(next, SerInfoType.SIGNAL.getNum());
			response.put("next_sig_no", signal.getSerNo());
		}
		
		return response;
		
	}
	
	/**
	 * 获取状态信息同步信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午1:50:51
	 * @param String seq 命令标识号
	 * @param String cmd 常量字符串, request_sync_info
	 * @param String app_num 需要同步信息节点的应用号码
	 * @return JSONObject response
	 */
	public JSONObject querySyncInfo(String seq, String cmd, String app_num) throws Exception{
		
		JSONObject response = new JSONObject();
		response.put("seq", seq);
		response.put("cmd", cmd);
		response.put("app_num", app_num);
		
		//状态同步信息
		List<SerInfoPO> serInfoPOs = serInfoDao.findBySourceType(SOURCE_TYPE.SYSTEM);
		List<BundlePO> localDevs = bundleDao.findSyncedLocalDevs();
		List<FolderUserMap> maps = folderUserMapDao.findLocalLdapMap();
		List<Long> userIds = new ArrayList<Long>();
		for(FolderUserMap map: maps){
			userIds.add(map.getUserId());
		}
		
		List<UserBO> users = userService.queryUsersByUserIds(userIds, TerminalType.PC_PORTAL);
		
		NotifyUserDeviceXML syncInfoXML = statusXMLUtil.createResourcesXml(serInfoPOs, localDevs, users);
		String syncInfo = XMLBeanUtils.beanToXml(NotifyUserDeviceXML.class, syncInfoXML);
		
		response.put("sync_info", syncInfo);
		
		//路由同步信息
		List<SerNodePO> serNodePOs = serNodeDao.findAll();
		NotifyRouteLinkXml routerInfoXml = statusXMLUtil.createRouteLinkXml(serNodePOs);
		String routerInfo = XMLBeanUtils.beanToXml(NotifyRouteLinkXml.class, routerInfoXml);
		
		response.put("sync_route_info", routerInfo);
		
		return response;
	}
	
	/**
	 * 生成节点信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:38:08
	 * @param String nodeUuid 节点标识
	 * @param List<SerInfoPO> infos 所有节点设备信息
	 * @return NodeVO 联网需要的节点信息
	 */
	public NodeVO generateNodeVO(String nodeUuid, List<SerInfoPO> infos) throws Exception{
		
		NodeVO nodeVO = new NodeVO();
		for(SerInfoPO info: infos){
			if(info.getSerNode().equals(nodeUuid) && info.getSerType().equals(SerInfoType.APPLICATION.getNum())){
				nodeVO.setApp_code(info.getSerNo());
			}
			if(info.getSerNode().equals(nodeUuid) && info.getSerType().equals(SerInfoType.SIGNAL.getNum())){
				nodeVO.setSig_code(info.getSerNo());
				nodeVO.setSig_pwd(info.getSerPwd());
				nodeVO.setSig_user(info.getSerNo());
				nodeVO.setSip_addr(new StringBufferWrapper().append(info.getSerAddr())
															.append(":")
															.append(info.getSerPort())
															.toString());
			}
		}
		
		if(nodeVO.getApp_code() == null || nodeVO.getSig_code() == null){
			nodeVO = null;
		}
		
		return nodeVO;
	}
	
	/**
	 * 查询本节点node<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 上午9:50:53
	 * @param List<SerNodePO> all 所有节点信息
	 * @return SerNodePO 本节点信息
	 */
	public SerNodePO querySelf(List<SerNodePO> all) throws Exception{
		SerNodePO self = null;
		for(SerNodePO node: all){
			if(node.getSourceType().equals(SOURCE_TYPE.SYSTEM)){
				self = node;
				break;
			}
		}
		if(self == null) throw new BaseException(StatusCode.ERROR, "当前系统未创建服务节点！");
		
		return self;
	}
	
	/**
	 * 根据节点标识查询节点<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 上午11:01:03
	 * @param List<SerNodePO> nodes 所有节点信息
	 * @param String uuid 节点标识
	 * @return SerNodePO 节点信息
	 */
	public SerNodePO queryByUuid(List<SerNodePO> nodes, String uuid) throws Exception{
		for(SerNodePO node: nodes){
			if(node.getNodeUuid().equals(uuid)){
				return node;
			}
		}
		return null;
	}
	
}
