package com.sumavision.bvc.control.device.group.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAvtplGearsVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAvtplVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleParamVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupRecordSchemeVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupScreenLayoutVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAvtplDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAvtplGearsDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupRecordSchemeDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupScreenLayoutDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO;
import com.sumavision.bvc.device.group.po.DeviceGroupScreenLayoutPO;
import com.sumavision.bvc.device.group.po.DeviceGroupScreenPositionPO;
import com.sumavision.bvc.device.group.service.MeetingServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

@Controller
@RequestMapping(value = "/device/group/param")
public class DeviceGroupParamController {

	@Autowired
	private DeviceGroupAvtplDAO deviceGroupAvtplDao;
	
	@Autowired
	private DeviceGroupAvtplGearsDAO deviceGroupAvtplGearsDao;
	
	@Autowired
	private DeviceGroupBusinessRoleDAO deviceGroupBusinessRoleDao;
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupRecordSchemeDAO deviceGroupRecordSchemeDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private DeviceGroupConfigVideoSrcDAO deviceGroupConfigVideoSrcDao;
	
	@Autowired
	private DeviceGroupScreenLayoutDAO deviceGroupScreenLayoutDao;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private MeetingServiceImpl meetingServiceImpl;
	
	/**
	 * @Title: 查询设备组参数方案
	 * @param groupId
	 * @param pageSize
	 * @param currentPage
	 * @throws Exception
	 * @return Object 返回类型 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/avtpl/{groupId}", method = RequestMethod.GET)
	public Object queryAvtpl(
			@PathVariable Long groupId,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		List<DeviceGroupAvtplVO> avtpls = new ArrayList<DeviceGroupAvtplVO>();
		
		DeviceGroupAvtplPO avtplPO = deviceGroupAvtplDao.findByGroupId(groupId);
		DeviceGroupAvtplVO avtpl = new DeviceGroupAvtplVO().set(avtplPO);
		
		avtpls.add(avtpl);
		
		JSONObject data = new JSONObject();
		data.put("rows", avtpls);
		data.put("total", avtpls.size());
		
		return data;
	}
	
	/**
	 * @Title: 修改数设备组参数方案
	 * @param id
	 * @param name
	 * @param videoFormat
	 * @param videoFormatSpare
	 * @param audioFormat
	 * @param usageType
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/avtpl/{id}")
	public Object updateAvtpl(
			@PathVariable Long id,
			String name,
			String videoFormat,
			String videoFormatSpare,
			String audioFormat,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupAvtplPO avtpl = deviceGroupAvtplDao.findOne(id);
		avtpl.setName(name);
		avtpl.setVideoFormat(VideoFormat.fromName(videoFormat));
		avtpl.setVideoFormatSpare(VideoFormat.fromName(videoFormatSpare));
		avtpl.setAudioFormat(AudioFormat.fromName(audioFormat));
		avtpl.setUpdateTime(new Date());
		deviceGroupAvtplDao.save(avtpl);
		
		DeviceGroupAvtplVO _avtpl = new DeviceGroupAvtplVO().set(avtpl);
		
		logService.logsHandle(user.getName(), "修改设备组参数方案", "设备组名称："+avtpl.getGroup().getName());
		
		return _avtpl;
	}
	
	/**
	 * @Title: 查询设备组参数方案的档位信息
	 * @param avtplId
	 * @param pageSize
	 * @param currentPage
	 * @throws Exception
	 * @return Object 返回类型 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/avtpl/gears/{avtplId}", method = RequestMethod.GET)
	public Object queryAvtplGears(
			@PathVariable Long avtplId,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		List<DeviceGroupAvtplGearsPO> gearsPOs = deviceGroupAvtplGearsDao.findByAvtplId(avtplId);
		
		List<DeviceGroupAvtplGearsVO> gears = DeviceGroupAvtplGearsVO.getConverter(DeviceGroupAvtplGearsVO.class).convert(gearsPOs, DeviceGroupAvtplGearsVO.class);
		
		JSONObject data = new JSONObject();
		data.put("rows", gears);
		data.put("total", gears.size());
		
		return data;
	}
	
	/**
	 * @Title: 新增档位数据 
	 * @param id  参数模板id
	 * @param name 档位名称
	 * @param videoBitRate
	 * @param videoBitRateSpare
	 * @param videoResolution
	 * @param videoResolutionSpare
	 * @param audioBitRate
	 * @param level 
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/avtpl/gears/{id}")
	public Object save(
			@PathVariable Long id,
			String name,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String audioBitRate,
			String level,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupAvtplPO tpl = deviceGroupAvtplDao.findOne(id);
		tpl.setUpdateTime(new Date());
		if(tpl.getGears() == null) tpl.setGears(new HashSet<DeviceGroupAvtplGearsPO>());
		
		DeviceGroupAvtplGearsPO gear = new DeviceGroupAvtplGearsPO();
		gear.setName(name);
		gear.setVideoBitRate(videoBitRate);
		gear.setVideoBitRateSpare(videoBitRateSpare);
		gear.setVideoResolution(Resolution.fromName(videoResolution));
		gear.setVideoResolutionSpare(Resolution.fromName(videoResolutionSpare));
		gear.setAudioBitRate(audioBitRate);
		gear.setLevel(GearsLevel.fromName(level));
		gear.setUpdateTime(new Date());
		gear.setAvtpl(tpl);
		tpl.getGears().add(gear);
		deviceGroupAvtplGearsDao.save(gear);
		
		DeviceGroupAvtplGearsVO _gear = new DeviceGroupAvtplGearsVO().set(gear);
		
		logService.logsHandle(user.getName(), "新增档位数据", "设备组名称："+tpl.getGroup().getName()+"参数方案名称："+tpl.getName()+"档位名称："+_gear.getName());
		
		return _gear;
	}
	
	/**
	 * @Title: 修改档位数据 
	 * @param id  档位的id
	 * @param name 档位名称
	 * @param avtplId
	 * @param videoBitRate
	 * @param videoBitRateSpare
	 * @param videoResolution
	 * @param videoResolutionSpare
	 * @param audioBitRate
	 * @param level 
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/avtpl/gears/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String audioBitRate,
			String level,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupAvtplGearsPO gear = deviceGroupAvtplGearsDao.findOne(id);
		gear.setName(name);
		gear.setVideoBitRate(videoBitRate);
		gear.setVideoBitRateSpare(videoBitRateSpare);
		gear.setVideoResolution(Resolution.fromName(videoResolution));
		gear.setVideoResolutionSpare(Resolution.fromName(videoResolutionSpare));
		gear.setAudioBitRate(audioBitRate);
		gear.setLevel(GearsLevel.fromName(level));
		gear.setUpdateTime(new Date());
		deviceGroupAvtplGearsDao.save(gear);
		
		DeviceGroupAvtplGearsVO _avtplGtpl = new DeviceGroupAvtplGearsVO().set(gear);
		
		logService.logsHandle(user.getName(), "修改档位数据", "设备组名称："+gear.getAvtpl().getGroup().getName()+"参数方案名称："+gear.getAvtpl().getName()+"档位名称："+gear.getName());
		
		return _avtplGtpl;
	}
	
	/**
	 * @Title: 根据档位id删除数据 
	 * @param id
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/avtpl/gears/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupAvtplGearsPO gear = deviceGroupAvtplGearsDao.findOne(id);
		
		//解关联
		DeviceGroupAvtplPO tpl = gear.getAvtpl();
		tpl.getGears().remove(gear);
		gear.setAvtpl(null);
		deviceGroupAvtplDao.save(tpl);
		
		deviceGroupAvtplGearsDao.delete(gear);
		return null;
	}
	
	/**
	 * @Title: 根据id批量删除档位
	 * @param ids
	 * @param request
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/avtpl/gears/remove/all")
	public Object removeAll(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);
		
		//解关联
		List<DeviceGroupAvtplGearsPO> gears = deviceGroupAvtplGearsDao.findAll(ids);
		DeviceGroupAvtplPO tpl = gears.get(0).getAvtpl();
		for(DeviceGroupAvtplGearsPO gear:gears){
			tpl.getGears().remove(gear);
			gear.setAvtpl(null);
		}
		deviceGroupAvtplDao.save(tpl);
		deviceGroupAvtplGearsDao.deleteByIdIn(ids);
		return null;
	}
	
	/**
	 * @Title: 根据groupId查询设备组业务角色
	 * @param groupId
	 * @param request
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/role/{groupId}", method=RequestMethod.GET)
	public Object queryRole(@PathVariable Long groupId,
							int pageSize,
							int currentPage,
							HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		List<DeviceGroupBusinessRolePO> rolePOs = deviceGroupBusinessRoleDao.findByGroupId(groupId);
		
		List<DeviceGroupBusinessRoleParamVO> roles = DeviceGroupBusinessRoleParamVO.getConverter(DeviceGroupBusinessRoleParamVO.class).convert(rolePOs, DeviceGroupBusinessRoleParamVO.class);
		
		JSONObject data = new JSONObject();
		data.put("rows", roles);
		data.put("total", roles.size());
		
		return data;
	}
	
	/**
	 * @Title: 新建设备组角色 
	 * @param: groupId 设备组id
	 *		   name 角色名称
	 *         special 角色属性
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/role/{groupId}")
	public Object saveRole(@PathVariable Long groupId,
						   String name,
						   String special,
						   String type,
						   HttpServletRequest request) throws Exception{
		
		DeviceGroupBusinessRolePO role = meetingServiceImpl.saveNewRole(groupId, name, special, type);
	
		DeviceGroupBusinessRoleParamVO _role = new DeviceGroupBusinessRoleParamVO().set(role);
		
		return _role;
	}
	
	/**
	 * @Title: 编辑设备组角色 
	 * @param: id 角色id
	 *		   name 角色名称
	 *         special 角色属性
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/role/{groupId}/{id}")
	public Object updateRole(
			@PathVariable Long groupId,
			@PathVariable Long id,
     	    String name,
		    String special,
	        String type,
		    HttpServletRequest request) throws Exception{
		
		DeviceGroupBusinessRolePO role = meetingServiceImpl.updateRole(groupId, id, name, special, type);
		
		DeviceGroupBusinessRoleParamVO _role = new DeviceGroupBusinessRoleParamVO().set(role);
		
		return _role;
	}
	
	/**
	 * @Title: 删除设备组角色 
	 * @param: id 角色id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/role/{groupId}/{id}")
	public Object removeRole(
			@PathVariable Long groupId,
			@PathVariable Long id,
	        HttpServletRequest request) throws Exception{
		
		meetingServiceImpl.removeRole(groupId, id);
		
		return null;
	}
	
	/**
	 * @Title: 批量删除设备组角色 
	 * @param: ids 批量删除的角色id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all/role/{groupId}")
	public Object removeAllRole(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"),Long.class);
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);

		//成员解绑角色
		Set<DeviceGroupMemberPO> members = group.getMembers();
		for(DeviceGroupMemberPO member: members){
			if(ids.contains(member.getRoleId())){
				member.setRoleId(null);
				member.setRoleName(null);
			}
		}
		
		deviceGroupDao.save(group);
		
		deviceGroupRecordSchemeDao.deleteByRoleIdIn(ids);
		deviceGroupConfigVideoDstDao.deleteByRoleIdIn(ids);
		deviceGroupConfigVideoSrcDao.deleteByRoleIdIn(ids);
		deviceGroupBusinessRoleDao.deleteByIdIn(ids);
		
		return null;
	}
	
	/**
	 * @Title: 查询设备组中的可录制角色
	 * @param: groupId 设备组id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/record/role/{groupId}", method = RequestMethod.GET)
	public Object queryRecordRole(@PathVariable Long groupId,
							      HttpServletRequest request) throws Exception{
		
		List<DeviceGroupBusinessRolePO> recordRoles = deviceGroupBusinessRoleDao.findByTypeAndGroupId(BusinessRoleType.RECORDABLE, groupId);
		List<DeviceGroupBusinessRoleVO> _recordRoles = DeviceGroupBusinessRoleVO.getConverter(DeviceGroupBusinessRoleVO.class).convert(recordRoles, DeviceGroupBusinessRoleVO.class);
		
		JSONObject data = new JSONObject();
		data.put("role", _recordRoles);
		
		return data;
	}
	
	/**
	 * @Title: 查询设备组的录制方案
	 * @param: groupId 设备组id
	 * 		   pageSize 分页-总页数
	 * 		   currentPageSize 分页-当前页数
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/record/scheme/{groupId}", method = RequestMethod.GET)
	public Object queryScheme(@PathVariable Long groupId,
							  int pageSize,
							  int currentPage,
							  HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		List<DeviceGroupRecordSchemePO> recordSchemePOs = deviceGroupRecordSchemeDao.findByGroupId(groupId);

		List<DeviceGroupRecordSchemeVO> recordSchemeVOs = DeviceGroupRecordSchemeVO.getConverter(DeviceGroupRecordSchemeVO.class).convert(recordSchemePOs, DeviceGroupRecordSchemeVO.class);
		
		JSONObject data = new JSONObject();
		data.put("rows", recordSchemeVOs);
		data.put("total", recordSchemeVOs.size());
		
		return data;
	}
	
	/**
	 * @Title: 新建设备组录制方案
	 * @param: groupId 设备组id
	 * 		   name 录制名称
	 * 		   roleId 录制角色id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/record/scheme/{groupId}")
	public Object saveScheme(@PathVariable Long groupId,
							 String name,
							 Long roleId,
							 HttpServletRequest request) throws Exception{
		
		DeviceGroupRecordSchemePO recordScheme = new DeviceGroupRecordSchemePO();
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		DeviceGroupBusinessRolePO businessRole = deviceGroupBusinessRoleDao.findOne(roleId);
		
		recordScheme.setGroup(group);
		recordScheme.setName(name);
		recordScheme.setRoleId(roleId);
		recordScheme.setRoleName(businessRole.getName());
		recordScheme.setUpdateTime(new Date());
		
		deviceGroupRecordSchemeDao.save(recordScheme);
		
		DeviceGroupRecordSchemeVO recordSchemeVO = new DeviceGroupRecordSchemeVO().set(recordScheme);
		
		return recordSchemeVO;
	}
	
	/**
	 * @Title: 编辑设备组录制方案
	 * @param: id 录制方案id
	 * 		   name 录制名称
	 * 		   roleId 录制角色id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/record/scheme/{id}")
	public Object updateScheme(@PathVariable Long id,
							 String name,
							 Long roleId,
							 HttpServletRequest request) throws Exception{
		
		DeviceGroupRecordSchemePO recordScheme = deviceGroupRecordSchemeDao.findOne(id);
		DeviceGroupBusinessRolePO businessRole = deviceGroupBusinessRoleDao.findOne(roleId);
		
		recordScheme.setName(name);
		recordScheme.setRoleId(roleId);
		recordScheme.setRoleName(businessRole.getName());
		recordScheme.setUpdateTime(new Date());
		
		deviceGroupRecordSchemeDao.save(recordScheme);
		
		DeviceGroupRecordSchemeVO recordSchemeVO = new DeviceGroupRecordSchemeVO().set(recordScheme);
		
		return recordSchemeVO;
		
	}
	
	/**
	 * @Title: 删除设备组录制方案
	 * @param: id 要删除的录制方案id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/record/scheme/{id}")
	public Object removeScheme(@PathVariable Long id,
							 HttpServletRequest request) throws Exception{
		
		deviceGroupRecordSchemeDao.delete(id);
		
		return null;
	}
	
	/**
	 * @Title: 批量删除设备组录制方案
	 * @param: ids 要批量删除的录制方案id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all/record/scheme")
	public Object removeAllScheme(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);

		deviceGroupRecordSchemeDao.deleteByIdIn(ids);
		
		return null;
	}
	
	/**
	 * @Title: 查询设备组布局方案
	 * @param: groupId 设备组id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/layout/{groupId}", method=RequestMethod.GET)
	public Object queryLayout(@PathVariable Long groupId,
							  int pageSize,
							  int currentPage,
							  HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		List<DeviceGroupScreenLayoutPO> layouts = deviceGroupScreenLayoutDao.findByGroupId(groupId);
		List<DeviceGroupScreenLayoutVO> _layouts = DeviceGroupScreenLayoutVO.getConverter(DeviceGroupScreenLayoutVO.class).convert(layouts, DeviceGroupScreenLayoutVO.class);
		
		JSONObject data = new JSONObject();
		data.put("rows", _layouts);
		data.put("total", _layouts.size());
		
		return data;
		
	}
	
	/**
	 * @Title: 新建设备组布局方案
	 * @param: groupId 设备组id
	 * 		   name 布局名称
	 * 		   websiteDraw 布局配置
	 * 		   position 布局坐标
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/layout/{groupId}")
	public Object saveLayout(@PathVariable Long groupId,
							 String name,
							 String websiteDraw,
							 String position,
							 HttpServletRequest request) throws Exception{
		
		List<DeviceGroupScreenPositionPO> positions = JSON.parseArray(position,DeviceGroupScreenPositionPO.class);
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		DeviceGroupScreenLayoutPO layout = new DeviceGroupScreenLayoutPO();
		layout.setGroup(group);
		layout.setName(name);
		layout.setWebsiteDraw(websiteDraw);
		layout.setUpdateTime(new Date());
		
		//和DeviceGroupScreenPositionPO加关联
		if(layout.getPositions() == null){
			layout.setPositions(new HashSet<DeviceGroupScreenPositionPO>());
			layout.getPositions().addAll(positions);
			for(DeviceGroupScreenPositionPO p : positions){
				p.setLayout(layout);
			}
		}
		
		deviceGroupScreenLayoutDao.save(layout);
		
		DeviceGroupScreenLayoutVO _layout = new DeviceGroupScreenLayoutVO().set(layout);
		
		return _layout;
	}
	
	/**
	 * @Title: 编辑设备组布局方案
	 * @param: id 布局方案id
	 * 		   name 布局名称
	 * 		   websiteDraw 布局配置
	 * 		   position 布局坐标
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/layout/{id}")
	public Object updateLayout(@PathVariable Long id,
							   String name,
							   String websiteDraw,
							   String position,
							   HttpServletRequest request) throws Exception{
		
		List<DeviceGroupScreenPositionPO> positions = JSON.parseArray(position, DeviceGroupScreenPositionPO.class);
		
		DeviceGroupScreenLayoutPO layout = deviceGroupScreenLayoutDao.findOne(id);
		
		layout.setName(name);
		layout.setWebsiteDraw(websiteDraw);
		layout.setUpdateTime(new Date());
		
		if(positions != null){
			//解关联
			Set<DeviceGroupScreenPositionPO> oldPositions = layout.getPositions();
			layout.getPositions().removeAll(oldPositions);
			for(DeviceGroupScreenPositionPO op : oldPositions){
				op.setLayout(null);
			}
			
			//加关联
			layout.getPositions().addAll(positions);
			for(DeviceGroupScreenPositionPO p : positions){
				p.setLayout(layout);
			}
		}
		deviceGroupScreenLayoutDao.save(layout);
		
		DeviceGroupScreenLayoutVO _layout = new DeviceGroupScreenLayoutVO().set(layout);
		
		return _layout;
	}
	
	/**
	 * @Title: 删除设备组布局方案
	 * @param: id 布局方案id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/layout/{id}")
	public Object removeLayout(@PathVariable Long id,
							   HttpServletRequest request) throws Exception{
		
		deviceGroupScreenLayoutDao.delete(id);
		
		return null;
	}
	
	/**
	 * @Title: 批量删除设备组布局方案
	 * @param: ids 批量删除的布局方案id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all/layout")
	public Object removeAllLayout(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);
		
		deviceGroupScreenLayoutDao.deleteByIdIn(ids);
		
		return null;
	}
}
