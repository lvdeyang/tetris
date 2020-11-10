package com.suma.venus.resource.controller.api.resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
import com.suma.venus.resource.controller.ControllerBase;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.SerNodePO.ConnectionStatus;
import com.suma.venus.resource.vo.BundleVO;
import com.suma.venus.resource.vo.ChannelSchemeVO;
import com.suma.venus.resource.vo.SerNodeVO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

@Controller
@RequestMapping(value = "/api/thirdpart/bqlw")
public class ApiThirdpartBqlw extends ControllerBase{
	
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

	/**
	 * 查本域以及外域信息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午6:45:12
	 * @return data{"local", serNodeVO 本域信息
	 *               "foreign", serNodeVOs 外域信息}
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/server/node/info")
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/foreign/server/node/off")
	public Object foreignServerNodeOff(HttpServletRequest request)throws Exception{
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		JSONArray foreignNames = wrapper.getJSONArray("foreign");
		Set<String> serverNodeName = new HashSet<String>();
		for (int i = 0; i < foreignNames.size(); i++) {
			JSONObject jsonObject = foreignNames.getJSONObject(i);
			String name = jsonObject.getString("name");
			serverNodeName.add(name);
		}
		List<SerNodePO> serNodePOs = serNodeDao.findByNodeNameIn(serverNodeName);
		if (serNodePOs != null&& !serNodePOs.isEmpty()) {
			for (SerNodePO serNodePO : serNodePOs) {
				serNodePO.setOperate(ConnectionStatus.OFF);
			}
		}
		serNodeDao.save(serNodePOs);
		return null;
	}
	
	/**
	 * 外域连接通知（批量，会通知对方域的组织机构以及设备信息）<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午8:27:24
	 * @param request
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/foreign/server/node/on")
	public Object foreignServerNodeOn(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
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
		List<SerNodePO> serNodePOs = serNodeDao.findByNodeNameIn(serverNodeName);
		if (serNodePOs != null&& !serNodePOs.isEmpty()) {
			for (SerNodePO serNodePO : serNodePOs) {
				serNodePO.setOperate(ConnectionStatus.ON);
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/permission/add")
	public Object devicePermissionAdd(HttpServletRequest request)throws Exception{
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/permission/remove")
	public Object devicePermissionRemove(HttpServletRequest request)throws Exception{
		JSONHttpServletRequestWrapper wrapper =new JSONHttpServletRequestWrapper(request);
		JSONArray foreign = new JSONArray(wrapper.getJSONArray("foreign"));
		Set<String> deleteBundleIds = new HashSet<String>();
		JSONArray devicesArray = new JSONArray();
		for (int i = 0; i < foreign.size(); i++) {
			devicesArray.add(foreign.getJSONObject(i).getJSONArray("devices"));
		}
		for (int i = 0; i < devicesArray.size(); i++) {
			String bundleId = devicesArray.getJSONObject(i).getString("bundleId");
			deleteBundleIds.add(bundleId);
		}
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/permission/change")
	public Object devicePermissionChange(HttpServletRequest request)throws Exception{
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
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
		folderUpdata(institutions);
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
	public void folderUpdata(JSONArray institutionsArray)throws Exception{
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/status/change")
	public Object deviceStatusChange(HttpServletRequest request)throws Exception{
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
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
}
