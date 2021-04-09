package com.sumavision.bvc.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.device.command.emergent.broadcast.CommandEmergentBroadcastServiceImpl;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * @ClassName: 对应急广播系统提供的接口
 * @author zsy
 * @date 2020年3月10日 下午2:27:00
 */
@Controller
@RequestMapping(value = "/api/server/emergent")
public class ApiEmergentBroadcastController {
	
	@Autowired
	private CommandEmergentBroadcastServiceImpl commandEmergentBroadcastServiceImpl;
	
	/**
	 * 根据经纬度、半径查询设备列表，业务也会将设备列表推送给终端<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月11日 上午8:24:08
	 * @param longitude
	 * @param latitude
	 * @param raidus
	 * @param unifiedId 消息预警唯一标识
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/visible/bundle")
	public Object queryVisibleBundle(
			String longitude,
			String latitude,
			String raidus,
			String unifiedId,
			HttpServletRequest request) throws Exception{
		
		//查询设备列表，同时业务也会将设备列表推送给终端
		List<EmergentBundleVO> bundleVOs = commandEmergentBroadcastServiceImpl.queryAndNotifyDevices(longitude, latitude, Long.parseLong(raidus), unifiedId);
		
		return bundleVOs;
	}
}
