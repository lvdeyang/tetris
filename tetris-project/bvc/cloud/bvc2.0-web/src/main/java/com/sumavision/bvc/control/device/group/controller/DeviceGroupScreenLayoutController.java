package com.sumavision.bvc.control.device.group.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.control.device.group.vo.DeviceGroupScreenLayoutVO;
import com.sumavision.bvc.device.group.dao.DeviceGroupScreenLayoutDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupScreenLayoutPO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/device/group/screen/layout")
public class DeviceGroupScreenLayoutController {

	@Autowired
	private DeviceGroupScreenLayoutDAO deviceGroupScreenLayoutDao;
	
	/**
	 * @Title: 查询设备组下所有的布局方案 
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return List<DeviceGroupScreenLayoutVO> 布局方案
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/all/{groupId}", method = RequestMethod.GET)
	public Object queryAll(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		List<DeviceGroupScreenLayoutPO> layouts = deviceGroupScreenLayoutDao.findByGroupId(groupId);
		List<DeviceGroupScreenLayoutVO> _layouts = DeviceGroupScreenLayoutVO.getConverter(DeviceGroupScreenLayoutVO.class).convert(layouts, DeviceGroupScreenLayoutVO.class);
		
		return _layouts;
	}
	
}
