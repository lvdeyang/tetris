package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jcajce.provider.symmetric.AES.Wrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.controller.ControllerBase;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.dao.RolePrivilegeMapDAO;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.suma.venus.resource.pojo.RolePrivilegeMap;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.pojo.SerNodePO.ConnectionStatus;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.suma.venus.resource.vo.BundleVO;
import com.suma.venus.resource.vo.ChannelSchemeVO;
import com.suma.venus.resource.vo.SerNodeVO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceFeign;
import com.sumavision.bvc.device.monitor.live.device.UserBundleBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.PassByBO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApiThirdpartMonitor_relationService extends ControllerBase{
	
	@Autowired
	private SerNodeDao serNodeDao;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ChannelTemplateDao channelTemplateDao;
	
	@Autowired
	private ChannelSchemeDao channelSchemeDao;
	
	@Autowired
	private UserQueryService userQueryService;
	
	@Autowired
	private MonitorLiveDeviceFeign monitorLiveDeviceFeign;
	
	@Autowired
	private PrivilegeDAO privilegeDAO;
	
	@Autowired
	private RolePrivilegeMapDAO rolePrivilegeMapDAO;
	
	/**
	 * 查本域以及外域信息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午6:45:12
	 * @return data{"local", serNodeVO 本域信息
	 *               "foreign", serNodeVOs 外域信息}
	 */
	public Map<String, Object> queryServerNodeInfo()throws Exception{
		Map<String, Object> data = makeAjaxData();
		SerNodePO serNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		SerNodeVO serNodeVO = SerNodeVO.transFromPO(serNodePO);
		List<SerNodePO> serNodePOs = serNodeDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
		List<SerNodeVO> serNodeVOs = SerNodeVO.transFromPOs(serNodePOs);
		data.put("local", serNodeVO);
		data.put("foreign", serNodeVOs);
		return data;
	}
	
	/**
	 * 外域连接断开通知（批量）<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午7:16:06
	 * @param request foreign:[{name:'外域名称'}]
	 * @return null
	 */
	public Object foreignServerNodeOff(JSONHttpServletRequestWrapper wrapper)throws Exception{
		JSONArray foreignNames = wrapper.getJSONArray("foreign");
		Set<String> serverNodeName = new HashSet<String>();
		for (int i = 0; i < foreignNames.size(); i++) {
			JSONObject jsonObject = foreignNames.getJSONObject(i);
			String name = jsonObject.getString("name");
			serverNodeName.add(name);
		}
		List<SerNodePO> serNodePOs = serNodeDao.findByNodeNameIn(serverNodeName);
		if (serNodePOs != null && !serNodePOs.isEmpty()) {
			for (SerNodePO serNodePO : serNodePOs) {
				serNodePO.setOperate(ConnectionStatus.OFF);
				serNodePO.setStatus(ConnectionStatus.OFF);
			}
		}
		serNodeDao.save(serNodePOs);
		return null;
	}
	
	/**
	 * 外域连接通知（批量、不会同步对方域组织机构和设备信息）<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午2:35:00
	 * @param wrapper
	 * @return
	 * @throws Exception
	 */
	public Object foreignServerNodeOn(JSONHttpServletRequestWrapper wrapper)throws Exception{
		JSONArray foreignNames =  wrapper.getJSONArray("foreign");
		Set<String> serverNodeName = new HashSet<String>();
		for (int i = 0; i < foreignNames.size(); i++) {
			JSONObject jsonObject = foreignNames.getJSONObject(i);
			String name = jsonObject.getString("name");
			serverNodeName.add(name);
		}
		//外域连接开启
		List<SerNodePO> serNodePOs = serNodeDao.findByNodeNameIn(serverNodeName);
		if (serNodePOs != null&& !serNodePOs.isEmpty()) {
			for (SerNodePO serNodePO : serNodePOs) {
				serNodePO.setOperate(ConnectionStatus.DONE);
				serNodePO.setStatus(ConnectionStatus.ON);
			}
		}
		serNodeDao.save(serNodePOs);
		return null;
	}
	
	/**
	 * 通知对方域的组织机构以及设备信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午8:27:24
	 * @param request
	 */
	public Object foreignServerNodeMessage(JSONHttpServletRequestWrapper wrapper) throws Exception{
		JSONArray foreign = wrapper.getJSONArray("foreign");
		JSONArray institutionsArray = new JSONArray();
		JSONArray devicesaArray = new JSONArray();
		Set<String> serverNodeName = new HashSet<String>();
		for (int i = 0; i < foreign.size(); i++) {
			JSONObject jsonObject = foreign.getJSONObject(i);
			institutionsArray.add(foreign.getJSONObject(i).getJSONArray("institutions"));
			devicesaArray.add(foreign.getJSONObject(i).getJSONArray("devices"));
			String name = jsonObject.getString("name");
			serverNodeName.add(name);
		}
		//外域连接开启
//		List<SerNodePO> serNodePOs = serNodeDao.findByNodeNameIn(serverNodeName);
//		if (serNodePOs != null&& !serNodePOs.isEmpty()) {
//			for (SerNodePO serNodePO : serNodePOs) {
//				serNodePO.setOperate(ConnectionStatus.DONE);
//				serNodePO.setStatus(ConnectionStatus.ON);
//			}
//		}
//		serNodeDao.save(serNodePOs);
		
		//外域下组织机构信息
		List<FolderPO> folderPOs = new ArrayList<FolderPO>();
		List<FolderPO> removeFolderPOs = new ArrayList<FolderPO>();
		for (int i = 0; i < institutionsArray.size(); i++) {
			JSONObject jsonObject = institutionsArray.getJSONObject(i);
			FolderPO folderPO = new FolderPO();
			folderPO.setName(jsonObject.getString("name"));
			folderPO.setUuid(jsonObject.getString("uuid"));
			folderPO.setSourceType(SOURCE_TYPE.EXTERNAL);
			folderPO.setParentPath(jsonObject.getString("parentPath"));
			folderPOs.add(folderPO);
		}
		List<FolderPO> existedFolderPOs = folderDao.findAll();
		if (existedFolderPOs != null && !existedFolderPOs.isEmpty()) {
			for (FolderPO folderPO : existedFolderPOs) {
				if (folderPOs != null && !folderPOs.isEmpty()) {
					for (FolderPO newFolderPO : folderPOs) {
						if (folderPO.getUuid().equals(newFolderPO.getUuid())) {
							folderPO.setName(newFolderPO.getName());
							removeFolderPOs.add(newFolderPO);
						}
					}
				}
			}
		}
		removeFolderPOs.removeAll(removeFolderPOs);
		folderDao.save(existedFolderPOs);
		folderDao.save(folderPOs);
		List<FolderPO> newFolders = folderDao.findAll();
		if(folderPOs != null && !folderPOs.isEmpty()){
			for (FolderPO folderPO : folderPOs) {
				String[] parentPathStrings = folderPO.getParentPath().split("/");
				StringBufferWrapper parentPath = new StringBufferWrapper();
				if (newFolders != null && !newFolders.isEmpty()) {
					for (FolderPO newfolderPO : newFolders) {
						if (parentPathStrings != null && parentPathStrings.length > 0) {
							for (int i = 1; i < parentPathStrings.length; i++) {
								if (newfolderPO.getUuid().equals(parentPathStrings[i])) {
									parentPath.append("/").append(newfolderPO.getId());
								}
								if (newfolderPO.getUuid().equals(parentPathStrings[parentPathStrings.length-1])) {
									folderPO.setParentId(newfolderPO.getId());
								}
							}
						}else {
							folderPO.setParentId(-1l);
						}
						folderPO.setSourceType(SOURCE_TYPE.EXTERNAL);
					}
				}
			}
		}
		folderDao.save(folderPOs);
		
		//外域下设备信息
		List<BundlePO> bundlePOs = new ArrayList<BundlePO>();
		List<BundlePO> removeBundlePOs = new ArrayList<BundlePO>();
		List<BundlePO> existedBundlePOs =  bundleDao.findAll(); 
		JSONArray channels = new JSONArray();
		for (int i = 0; i < devicesaArray.size(); i++) {
			JSONObject jsonObject = devicesaArray.getJSONObject(i); 
			channels.add(jsonObject.getJSONArray("channels"));
			BundleVO bundleVO =  JSONObject.toJavaObject(jsonObject,BundleVO.class);			
			String institution = jsonObject.getString("institution");
			if (newFolders != null && !newFolders.isEmpty()) {
				for (FolderPO newfolderPO : newFolders) {
					if (newfolderPO.getUuid().equals(institution)) {
						bundleVO.setFolderId(newfolderPO.getId());
					}
				}
			}
			BundlePO bundlePO = bundleVO.toPO();
			bundlePO.setSourceType(SOURCE_TYPE.EXTERNAL);
			bundlePOs.add(bundlePO);
			if (existedBundlePOs != null && !existedBundlePOs.isEmpty()) {
				for (BundlePO bundlePO1 : existedBundlePOs) {
					for (BundlePO bundlePO2 : bundlePOs) {
						if (bundlePO1.getBundleId().equals(bundlePO2.getBundleId())) {
							removeBundlePOs.add(bundlePO2);
						}
					}
				}
			}
		}
		bundlePOs.removeAll(removeBundlePOs);
		bundleDao.save(bundlePOs);
		//外域下的通道信息
		List<ChannelTemplatePO> templatePOs  = channelTemplateDao.findAll();
		List<ChannelSchemePO> channelSchemePOs = channelSchemeDao.findAll();
		List<ChannelSchemePO> newchannelSchemePOs = new ArrayList<ChannelSchemePO>();
		for (int i = 0; i < channels.size(); i++) {
			JSONObject jsonObject = channels.getJSONObject(i);
			ChannelSchemeVO channelSchemeVO = JSONObject.toJavaObject(jsonObject, ChannelSchemeVO.class);
			if (templatePOs != null && !templatePOs.isEmpty()) {
				for (ChannelTemplatePO channelTemplatePO : templatePOs) {
					if (channelSchemeVO.getDeviceModel().equals(channelTemplatePO.getDeviceModel()) 
							&& channelSchemeVO.getChannelName().equals(channelTemplatePO.getChannelName())) {
						channelSchemeVO.setChannelTemplateID(channelTemplatePO.getId());
					}
				}
			}
			ChannelSchemePO channelSchemePO = new ChannelSchemePO();
			channelSchemePO.setBundleId(channelSchemeVO.getBundleId());
			channelSchemePO.setChannelId(channelSchemeVO.getChannelId());
			channelSchemePO.setChannelName(channelSchemeVO.getChannelName());
			channelSchemePO.setChannelTemplateID(channelSchemeVO.getChannelTemplateID());
			channelSchemePO.setChannelStatus(LockStatus.IDLE);
			newchannelSchemePOs.add(channelSchemePO);
		}
		List<ChannelSchemePO> removeChannelSchemePOs = new ArrayList<ChannelSchemePO>();
		if(channelSchemePOs != null && !channelSchemePOs.isEmpty()){
			for (ChannelSchemePO channelSchemePO : channelSchemePOs) {
				if (newchannelSchemePOs != null && !newchannelSchemePOs.isEmpty()) {
					for (ChannelSchemePO newchannelSchemePO : newchannelSchemePOs) {
						if (newchannelSchemePO.getBundleId().equals(channelSchemePO.getBundleId()) 
								&& newchannelSchemePO.getChannelName().equals(channelSchemePO.getChannelName())) {
							channelSchemePO.setChannelTemplateID(newchannelSchemePO.getChannelTemplateID());
							removeChannelSchemePOs.add(newchannelSchemePO);
						}
					}
				}
			}
		}
		newchannelSchemePOs.remove(removeChannelSchemePOs);
		channelSchemeDao.save(newchannelSchemePOs);
		channelSchemeDao.save(channelSchemePOs);
		return null;
	}

	/**
	 * 添加设备授权通知<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月5日 下午7:41:11
	 * @param request
	 * @return
	 */
	public Object devicePermissionAdd(JSONHttpServletRequestWrapper wrapper)throws Exception{
		JSONArray foreign = wrapper.getJSONArray("foreign");
		JSONArray institutionsArray = new JSONArray();
		JSONArray devicesaArray = new JSONArray();
		Set<String> serverNodeName = new HashSet<String>();
		List<String> toBindChecks = new ArrayList<String>();
		for (int i = 0; i < foreign.size(); i++) {
			JSONObject jsonObject = foreign.getJSONObject(i);
			institutionsArray.add(foreign.getJSONObject(i).getJSONArray("institutions"));
			devicesaArray.add(foreign.getJSONObject(i).getJSONArray("devices"));
			String name = jsonObject.getString("name");
			toBindChecks = JSONObject.parseArray(jsonObject.getJSONArray("bindChecks").toJSONString(), String.class);
			serverNodeName.add(name);
		}
		
		//授权添加
		List<PrivilegePO> privilegePOs  = privilegeDAO.findByResourceIndentityIn(toBindChecks);
		List<String> exiStrings = new ArrayList<String>();
		if (toBindChecks != null && !toBindChecks.isEmpty()) {
			for (String toBindCheck : toBindChecks) {
				if (privilegePOs != null && !privilegePOs.isEmpty()) {
					for (PrivilegePO privilegePO : privilegePOs) {
						if (privilegePO.getResourceIndentity().equals(toBindCheck)) {
							exiStrings.add(toBindCheck);
						}
					}
				}
			}
		}
		toBindChecks.removeAll(exiStrings);
		List<PrivilegePO> newprivilegePos = new ArrayList<PrivilegePO>();
		if (toBindChecks != null && !toBindChecks.isEmpty()) {
			for (String toBindCheck : toBindChecks) {
				PrivilegePO privilegePO = new PrivilegePO();
				privilegePO.setResourceIndentity(toBindCheck);
				newprivilegePos.add(privilegePO);
			}
		}
		privilegeDAO.save(newprivilegePos);
		
		//外域连接开启
		List<SerNodePO> serNodePOs = serNodeDao.findByNodeNameIn(serverNodeName);
		if (serNodePOs != null&& !serNodePOs.isEmpty()) {
			for (SerNodePO serNodePO : serNodePOs) {
				serNodePO.setOperate(ConnectionStatus.OFF);
			}
		}
		serNodeDao.save(serNodePOs);
		
		//外域下组织机构信息
		List<FolderPO> folderPOs = new ArrayList<FolderPO>();
		List<FolderPO> removeFolderPOs = new ArrayList<FolderPO>();
		for (int i = 0; i < institutionsArray.size(); i++) {
			JSONObject jsonObject = institutionsArray.getJSONObject(i);
			FolderPO folderPO = new FolderPO();
			folderPO.setName(jsonObject.getString("name"));
			folderPO.setUuid(jsonObject.getString("uuid"));
			folderPO.setSourceType(SOURCE_TYPE.EXTERNAL);
			folderPO.setParentPath(jsonObject.getString("parentPath"));
			folderPOs.add(folderPO);
		}
		List<FolderPO> existedFolderPOs = folderDao.findAll();
		if (existedFolderPOs != null && !existedFolderPOs.isEmpty()) {
			for (FolderPO folderPO : existedFolderPOs) {
				if (folderPOs != null && !folderPOs.isEmpty()) {
					for (FolderPO newFolderPO : folderPOs) {
						if (folderPO.getUuid().equals(newFolderPO.getUuid())) {
							folderPO.setName(newFolderPO.getName());
							removeFolderPOs.add(newFolderPO);
						}
					}
				}
			}
		}
		removeFolderPOs.removeAll(removeFolderPOs);
		folderDao.save(existedFolderPOs);
		folderDao.save(folderPOs);
		List<FolderPO> newFolders = folderDao.findAll();
		if(folderPOs != null && !folderPOs.isEmpty()){
			for (FolderPO folderPO : folderPOs) {
				String[] parentPathStrings = folderPO.getParentPath().split("/");
				StringBufferWrapper parentPath = new StringBufferWrapper();
				if (newFolders != null && !newFolders.isEmpty()) {
					for (FolderPO newfolderPO : newFolders) {
						if (parentPathStrings != null && parentPathStrings.length > 0) {
							for (int i = 1; i < parentPathStrings.length; i++) {
								if (newfolderPO.getUuid().equals(parentPathStrings[i])) {
									parentPath.append("/").append(newfolderPO.getId());
								}
								if (newfolderPO.getUuid().equals(parentPathStrings[parentPathStrings.length-1])) {
									folderPO.setParentId(newfolderPO.getId());
								}
							}
						}else {
							folderPO.setParentId(-1l);
						}
					}
				}
			}
		}
		folderDao.save(folderPOs);
		
		//外域下设备信息
		Set<String> bundleIds = new HashSet<String>();
		
		List<BundlePO> bundlePOs = new ArrayList<BundlePO>();
		List<BundlePO> removeBundlePOs = new ArrayList<BundlePO>();
		List<BundlePO> existedBundlePOs =  bundleDao.findAll(); 
		JSONArray channels = new JSONArray();
		for (int i = 0; i < devicesaArray.size(); i++) {
			JSONObject jsonObject = devicesaArray.getJSONObject(i); 
			channels.add(jsonObject.getJSONArray("channels"));
			BundleVO bundleVO =  JSONObject.toJavaObject(jsonObject,BundleVO.class);			
			String institution = jsonObject.getString("institution");
			if (newFolders != null && !newFolders.isEmpty()) {
				for (FolderPO newfolderPO : newFolders) {
					if (newfolderPO.getUuid().equals(institution)) {
						bundleVO.setFolderId(newfolderPO.getId());
					}
				}
			}
			BundlePO bundlePO = bundleVO.toPO();
			
			bundleIds.add(bundlePO.getBundleId());
			
			bundlePOs.add(bundlePO);
			if (existedBundlePOs != null && !existedBundlePOs.isEmpty()) {
				for (BundlePO bundlePO1 : existedBundlePOs) {
					for (BundlePO bundlePO2 : bundlePOs) {
						if (bundlePO1.getBundleId().equals(bundlePO2.getBundleId())) {
							removeBundlePOs.add(bundlePO2);
						}
					}
				}
			}
		}
		bundlePOs.removeAll(removeBundlePOs);
		bundleDao.save(bundlePOs);
		//外域下的通道信息
		List<ChannelTemplatePO> templatePOs  = channelTemplateDao.findAll();
		List<ChannelSchemePO> channelSchemePOs = channelSchemeDao.findAll();
		List<ChannelSchemePO> newchannelSchemePOs = new ArrayList<ChannelSchemePO>();
		for (int i = 0; i < channels.size(); i++) {
			JSONObject jsonObject = channels.getJSONObject(i);
			ChannelSchemeVO channelSchemeVO = JSONObject.toJavaObject(jsonObject, ChannelSchemeVO.class);
			if (templatePOs != null && !templatePOs.isEmpty()) {
				for (ChannelTemplatePO channelTemplatePO : templatePOs) {
					if (channelSchemeVO.getDeviceModel().equals(channelTemplatePO.getDeviceModel()) 
							&& channelSchemeVO.getChannelName().equals(channelTemplatePO.getChannelName())) {
						channelSchemeVO.setChannelTemplateID(channelTemplatePO.getId());
					}
				}
			}
			ChannelSchemePO channelSchemePO = new ChannelSchemePO();
			channelSchemePO.setBundleId(channelSchemeVO.getBundleId());
			channelSchemePO.setChannelId(channelSchemeVO.getChannelId());
			channelSchemePO.setChannelName(channelSchemeVO.getChannelName());
			channelSchemePO.setChannelTemplateID(channelSchemeVO.getChannelTemplateID());
			channelSchemePO.setChannelStatus(LockStatus.IDLE);
			newchannelSchemePOs.add(channelSchemePO);
		}
		List<ChannelSchemePO> removeChannelSchemePOs = new ArrayList<ChannelSchemePO>();
		if(channelSchemePOs != null && !channelSchemePOs.isEmpty()){
			for (ChannelSchemePO channelSchemePO : channelSchemePOs) {
				if (newchannelSchemePOs != null && !newchannelSchemePOs.isEmpty()) {
					for (ChannelSchemePO newchannelSchemePO : newchannelSchemePOs) {
						if (newchannelSchemePO.getBundleId().equals(channelSchemePO.getBundleId()) 
								&& newchannelSchemePO.getChannelName().equals(channelSchemePO.getChannelName())) {
							channelSchemePO.setChannelTemplateID(newchannelSchemePO.getChannelTemplateID());
							removeChannelSchemePOs.add(newchannelSchemePO);
						}
					}
				}
			}
		}
		newchannelSchemePOs.remove(removeChannelSchemePOs);
		channelSchemeDao.save(newchannelSchemePOs);
		channelSchemeDao.save(channelSchemePOs);
		
		return null;
	}
	
	/**
	 * 删除设备授权通知<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午8:45:53
	 * @param request
	 * @return
	 */
	public Object devicePermissionRemove(JSONHttpServletRequestWrapper wrapper)throws Exception{
		JSONArray foreign = new JSONArray(wrapper.getJSONArray("foreign"));
		Set<String> deleteBundleIds = new HashSet<String>();
		JSONArray devicesArray = new JSONArray();
		List<String> toUnbindChecks = new ArrayList<String>();
		List<String> toUnbindWriteCheList = new ArrayList<String>();
		
		for (int i = 0; i < foreign.size(); i++) {
			JSONObject jsonObject = foreign.getJSONObject(i);
			
			devicesArray.add(foreign.getJSONObject(i).getJSONArray("devices"));
			toUnbindChecks = JSONObject.parseArray(jsonObject.getJSONArray("unBindChecks").toJSONString(), String.class);
			toUnbindWriteCheList = JSONObject.parseArray(jsonObject.getString("toUnbindWriteCheList").toString(),String.class);
		}
		
		//删除权限和授权
		List<PrivilegePO> unbindprivilegePOs = privilegeDAO.findByResourceIndentityIn(toUnbindChecks);
		Set<Long> privilegeIds = new HashSet<Long>();
		if (unbindprivilegePOs != null && !unbindprivilegePOs.isEmpty()) {
			for (PrivilegePO privilegePO : unbindprivilegePOs) {
				privilegeIds.add(privilegePO.getId());
			}
			List<RolePrivilegeMap> rolePrivilegeMaps = rolePrivilegeMapDAO.findByPrivilegeIdIn(privilegeIds);
			rolePrivilegeMapDAO.delete(rolePrivilegeMaps);
			privilegeDAO.delete(unbindprivilegePOs);
		}
		
		
		//失去权限后停止转发
		List<UserBO> userBOs = userQueryService.findAll();
		if (toUnbindWriteCheList != null && !toUnbindWriteCheList.isEmpty()&& userBOs != null) {
			List<UserBundleBO> userBundleBOs = new ArrayList<UserBundleBO>();
			for (UserBO userBO : userBOs) {
				UserBundleBO userBundleBO = new UserBundleBO();
				userBundleBO.setUserId(userBO.getId());
				userBundleBO.setBundleIds(toUnbindWriteCheList);
				userBundleBOs.add(userBundleBO);
			}
			monitorLiveDeviceFeign.stopLiveByLosePrivilege(JSONArray.toJSONString(userBundleBOs));
		}
		
		//删除没有权限的设备
		for (int i = 0; i < devicesArray.size(); i++) {
			String bundleId = devicesArray.getJSONObject(i).getString("bundleId");
			deleteBundleIds.add(bundleId);
		}
		List<PrivilegePO> privilegePOs = privilegeDAO.findByIndentify(deleteBundleIds);
		Set<String> bindBundleIds = new HashSet<String>();
		if (deleteBundleIds != null && !deleteBundleIds.isEmpty()) {
			for (String deleteBundleId : deleteBundleIds) {
				if (privilegePOs != null && !privilegePOs.isEmpty()) {
					for (PrivilegePO privilegePO : privilegePOs) {
						if (deleteBundleId.equals(privilegePO.getResourceIndentity().split("-")[0])) {
							bindBundleIds.add(deleteBundleId);
						}
					}
				}
			}
		}
		deleteBundleIds.removeAll(bindBundleIds);
		List<BundlePO> bundlePOs = bundleDao.findByBundleIdIn(deleteBundleIds);
		bundleDao.delete(bundlePOs);
		return null;
	}
	
	/**
	 * 设备修改组织机构通知<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午10:31:39
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object devicePermissionChange(JSONHttpServletRequestWrapper wrapper)throws Exception{
		JSONArray foreign = new JSONArray(wrapper.getJSONArray("foreign"));
		JSONArray institutions = new JSONArray();
		List<BundleVO> bundleVOs = new ArrayList<BundleVO>();
		for (int i = 0; i < foreign.size(); i++) {
			JSONObject jsonObject = foreign.getJSONObject(i);
			String sername = jsonObject.getString("name");
			institutions.add(jsonObject.getJSONArray("institutions"));
			JSONArray devicesJsonArray = jsonObject.getJSONArray("devices");
			for (int j = 0; j < devicesJsonArray.size(); j++) {
				BundleVO bundleVO = JSONObject.toJavaObject(jsonObject,BundleVO.class);
				bundleVO.setEquipFactInfo(sername);
				bundleVOs.add(bundleVO);
			}
		}
		folderUpdate(institutions);
		List<FolderPO> folderPOs = folderDao.findAll();
		Set<String> bundleIds = new HashSet<String>();
		List<BundlePO> changeBundlePOs = new ArrayList<BundlePO>();
		if(bundleVOs!=null && !bundleVOs.isEmpty()){
			for (BundleVO bundleVO : bundleVOs) {
				if(folderPOs!=null&&folderPOs.isEmpty()){
					for (FolderPO folderPO : folderPOs) {
						if(bundleVO.getInstitution().equals(folderPO.getUuid())){
							bundleVO.setFolderId(folderPO.getId());
							break;
						}
					}
				}
				changeBundlePOs.add(bundleVO.toPO());
			}
		}
		List<BundlePO> bundlePOs = bundleDao.findByBundleIdIn(bundleIds);
		if (changeBundlePOs != null&&!changeBundlePOs.isEmpty()) {
			for (BundlePO bundlePO : changeBundlePOs) {
				if (bundlePOs != null && !bundlePOs.isEmpty()) {
					for (BundlePO oldbundlePO : bundlePOs) {
						if(bundlePO.getBundleId().equals(oldbundlePO.getBundleId())){
							oldbundlePO.setFolderId(bundlePO.getFolderId());
						}
					}
				}
			}
		}
		bundleDao.save(bundlePOs);
		return null;
	}
	
	/**
	 * 外域下组织机构更新<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午10:28:19
	 * @param institutionsArray
	 * @throws Exception
	 */
	public Object folderUpdate(JSONArray institutionsArray)throws Exception{
		List<FolderPO> folderPOs = new ArrayList<FolderPO>();
		List<FolderPO> removeFolderPOs = new ArrayList<FolderPO>();
		for (int i = 0; i < institutionsArray.size(); i++) {
			JSONObject jsonObject = institutionsArray.getJSONObject(i);
			FolderPO folderPO = new FolderPO();
			folderPO.setName(jsonObject.getString("name"));
			folderPO.setUuid(jsonObject.getString("uuid"));
			folderPO.setSourceType(SOURCE_TYPE.EXTERNAL);
			folderPO.setParentPath(jsonObject.getString("parentPath"));
			folderPOs.add(folderPO);
		}
		List<FolderPO> existedFolderPOs = folderDao.findAll();
		if (existedFolderPOs != null && !existedFolderPOs.isEmpty()) {
			for (FolderPO folderPO : existedFolderPOs) {
				if (folderPOs != null && !folderPOs.isEmpty()) {
					for (FolderPO newFolderPO : folderPOs) {
						if (folderPO.getUuid().equals(newFolderPO.getUuid())) {
							folderPO.setName(newFolderPO.getName());
							removeFolderPOs.add(newFolderPO);
						}
					}
				}
			}
		}
		removeFolderPOs.removeAll(removeFolderPOs);
		folderDao.save(existedFolderPOs);
		folderDao.save(folderPOs);
		List<FolderPO> newFolders = folderDao.findAll();
		if(folderPOs != null && !folderPOs.isEmpty()){
			for (FolderPO folderPO : folderPOs) {
				String[] parentPathStrings = folderPO.getParentPath().split("/");
				StringBufferWrapper parentPath = new StringBufferWrapper();
				if (newFolders != null && !newFolders.isEmpty()) {
					for (FolderPO newfolderPO : newFolders) {
						if (parentPathStrings != null && parentPathStrings.length > 0) {
							for (int i = 1; i < parentPathStrings.length; i++) {
								if (newfolderPO.getUuid().equals(parentPathStrings[i])) {
									parentPath.append("/").append(newfolderPO.getId());
								}
								if (newfolderPO.getUuid().equals(parentPathStrings[parentPathStrings.length-1])) {
									folderPO.setParentId(newfolderPO.getId());
								}
							}
						}else {
							folderPO.setParentId(-1l);
						}
					}
				}
			}
		}
		folderDao.save(folderPOs);
		return null;
	}
	
	//device/status/change
	/**
	 * 设备状态变动通知<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午11:51:52
	 * @param request
	 * @return
	 */
	public Object deviceStatusChange(JSONHttpServletRequestWrapper wrapper)throws Exception{
		JSONArray foreign = new JSONArray(wrapper.getJSONArray("foreign"));
		Set<String> bundleIds = new HashSet<String>(); 
		List<BundlePO> changeBundlePOs = new ArrayList<BundlePO>();
		for (int i = 0; i < foreign.size(); i++) {
			JSONObject jsonObject = foreign.getJSONObject(i);
			String name = jsonObject.getString("name");
			JSONArray devicesArray = new JSONArray(jsonObject.getJSONArray("devices"));
			for (int j = 0; j < devicesArray.size(); j++) {
				BundleVO bundleVO = JSONObject.toJavaObject(jsonObject,BundleVO.class);
				BundlePO bundlePO = bundleVO.toPO();
				bundlePO.setEquipFactInfo(name);
				changeBundlePOs.add(bundlePO);
				bundleIds.add(bundlePO.getBundleId());
			}
		}
		List<BundlePO> bundlePOs = bundleDao.findByBundleIdIn(bundleIds);
		if (changeBundlePOs != null && !changeBundlePOs.isEmpty()) {
			for (BundlePO bundlePO : changeBundlePOs) {
				if (bundlePOs != null && !bundlePOs.isEmpty()) {
					for (BundlePO oldbundlePO : bundlePOs) {
						if(bundlePO.getBundleId().equals(oldbundlePO.getBundleId())){
							oldbundlePO.setOnlineStatus(bundlePO.getOnlineStatus());
							oldbundlePO.setEquipFactInfo(bundlePO.getEquipFactInfo());
						}
					}
				}
			}
		}
		bundleDao.save(bundlePOs);
		return null;
	}
	
	/**
	 * 设备绑定权限<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午11:25:47
	 */
	private boolean bindResourceCodes(Long roleId, List<String> toBindChecks) throws Exception {
		if (!toBindChecks.isEmpty()) {
			RoleAndResourceIdBO bo = new RoleAndResourceIdBO();
			bo.setRoleId(roleId);
			bo.setResourceCodes(toBindChecks);
			
			return userQueryService.bindRolePrivilege(bo);
		}
		return true;
	}
	
	/**
	 * 查询外域下有权限的设备<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午4:53:38
	 * @param wrapper 
	 */
	public List<Map<String, Object>> foreignServerInformation(JSONHttpServletRequestWrapper wrapper)throws Exception{
		
		String foreignName = wrapper.getString("foreign");
		SerNodePO serNodePO = serNodeDao.findByNodeName(foreignName);
		List<BundlePO> bundlePOs = bundleDao.findByEquipFactInfo(foreignName);
		List<BundleVO> bundleVOs = new ArrayList<BundleVO>();
		Set<String> bundleIds = new HashSet<String>();
		Set<Long> folderIds = new HashSet<Long>();
		if (bundlePOs != null && !bundlePOs.isEmpty()) {
			for (BundlePO bundlePO : bundlePOs) {
				folderIds.add(bundlePO.getFolderId()==null? 0l:bundlePO.getFolderId());
				bundleIds.add(bundlePO.getBundleId());
				BundleVO bundleVO = BundleVO.fromPO(bundlePO);
				bundleVOs.add(bundleVO);
			}
		}
		List<FolderPO> institutions = new ArrayList<FolderPO>();
		List<FolderPO> allFolderPOs = folderDao.findAll();
		if(folderIds != null && !folderIds.isEmpty()){
			List<FolderPO> folderPOs = folderDao.findByIdIn(folderIds);
			if(folderPOs !=null && !folderPOs.isEmpty()){
				for (FolderPO folderPO : folderPOs) {
					StringBufferWrapper parentpath = new StringBufferWrapper();
					if(folderPO.getParentPath() != null && folderPO.getParentPath().equals("")){
						String[] parentPathStrings = folderPO.getParentPath().split("/"); 
						for (int i = 1; i < parentPathStrings.length; i++) {
							if (allFolderPOs != null && !allFolderPOs.isEmpty()) {
								for (FolderPO allFolderPO : allFolderPOs) {
									if(parentPathStrings[i].equals(allFolderPO.getId().toString())){
										parentpath.append("/").append(allFolderPO.getUuid());
									}
								}
							}
						}
					}
					folderPO.setParentPath(parentpath.toString());
				}
			}
			institutions.addAll(folderPOs);
		}
		List<ChannelSchemePO> channelSchemePOs = channelSchemeDao.findByBundleIdIn(bundleIds);
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
		List<Map<String, Object>> foreign = new ArrayList<Map<String, Object>>();
			foreign.add(new HashMap<String, Object>());
			foreign.get(0).put("name", serNodePO.getNodeName());
			foreign.get(0).put("institutions", institutions);
			foreign.get(0).put("devices", bundleVOs);
		return foreign;
	}
}
