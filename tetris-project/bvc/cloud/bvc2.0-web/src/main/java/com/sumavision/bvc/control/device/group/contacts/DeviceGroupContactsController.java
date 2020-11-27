package com.sumavision.bvc.control.device.group.contacts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value="/device/group/contacts")
public class DeviceGroupContactsController{
	
	@Autowired
	private DeviceGroupContactsService deviceGroupContactsService;
	
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * 添加联系人对应的设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午9:15:41
	 * @param bundleId 设备id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/add")
	public Object add(
			String bundleIds,
			HttpServletRequest request) throws Exception{
		
		if(bundleIds == null || bundleIds.equals("")){
			return null;
		}
		
		List<String> bundleIdList = JSONArray.parseArray(bundleIds, String.class);
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		deviceGroupContactsService.add(userId, bundleIdList);
		
		return null;
	}
	
	/**
	 * 查询联系人对应的设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午9:15:41
	 * @param hasSelected true查询已经添加的设备,false或null查询未添加的设备
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/query")
	public Object query(
			Boolean hasSelected,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		return deviceGroupContactsService.query(userId, hasSelected);
	}
	
	/**
	 * 删除联系人对应的设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午9:15:41
	 * @param bundleId 设备id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/delete")
	public Object delete(
			String bundleIds,
			HttpServletRequest request) throws Exception{
		
		if(bundleIds == null || bundleIds.equals("")){
			return null;
		}
		
		List<String> bundleIdList = JSONArray.parseArray(bundleIds, String.class);
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		deviceGroupContactsService.delete(userId, bundleIdList);
		
		return null;
		
	}
}
