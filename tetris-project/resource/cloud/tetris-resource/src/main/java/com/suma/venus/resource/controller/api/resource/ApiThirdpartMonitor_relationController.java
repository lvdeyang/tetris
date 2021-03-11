package com.suma.venus.resource.controller.api.resource;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.controller.ControllerBase;
import com.suma.venus.resource.service.ApiThirdpartMonitor_relationService;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

@Controller
@RequestMapping(value = "/api/thirdpart/monitor_relation")
public class ApiThirdpartMonitor_relationController extends ControllerBase{
	
	@Autowired
	private ApiThirdpartMonitor_relationService apiThirdpartMonitor_relationService;

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
	public Object queryServerNodeInfo(HttpServletRequest request)throws Exception{
		
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		
		System.out.println("-------------------------查本域以及外域信息-------------------------------");
		return apiThirdpartMonitor_relationService.queryServerNodeInfo(wrapper);
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
		
		System.out.println("-------------------------外域连接断开通知-------------------------------");
		return apiThirdpartMonitor_relationService.foreignServerNodeOff(wrapper);
	}
	
	/**
	 * 通知对方域的组织机构以及设备信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午8:27:24
	 * @param request
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/foreign/server/node/message")
	public Object foreignServerNodeMessage(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		
		System.out.println("-------------------------通知对方域的组织机构以及设备信息-------------------------------");
		return apiThirdpartMonitor_relationService.foreignServerNodeMessage(wrapper);
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

		System.out.println("-------------------------添加设备授权通知-------------------------------");
		return apiThirdpartMonitor_relationService.devicePermissionAdd(wrapper);
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
		
		System.out.println("-------------------------删除设备授权通知-------------------------------");
		return apiThirdpartMonitor_relationService.devicePermissionRemove(wrapper);
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
	@RequestMapping(value = "/device/institution/change")
	public Object devicePermissionChange(HttpServletRequest request)throws Exception{
		
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		
		System.out.println("-------------------------设备修改组织机构通知-------------------------------");
		return apiThirdpartMonitor_relationService.devicePermissionChange(wrapper);
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
		
		System.out.println("-------------------------设备状态变动通知-------------------------------");
		return apiThirdpartMonitor_relationService.deviceStatusChange(wrapper);
	}
	
	/**
	 * 外域连接通知（批量、不会同步对方域组织机构和设备信息）<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午2:35:00
	 * @param wrapper
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/foreign/server/node/on")
	public Object foreignServerNodeOn(HttpServletRequest request)throws Exception{
		
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		
		System.out.println("-------------------------外域连接通知（批量、不会同步对方域组织机构和设备信息）-------------------------------");
		return apiThirdpartMonitor_relationService.foreignServerNodeOn(wrapper);
	}
	
	/**
	 * 查询外域下有权限的设备<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午4:53:38
	 * @param wrapper
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/foreign/server/information")
	public Object foreignServerInformation(HttpServletRequest request)throws Exception{
		
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		
 		String foreignName = wrapper.getString("foreign");
 		System.out.println("-------------------------查询外域下有权限的设备-------------------------------");
		return apiThirdpartMonitor_relationService.foreignServerInformation(foreignName);
	}
	
	/**
	 * 查询passby消息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 上午10:58:48
	 * @param layerId 接入id
	 * @return List<LianwangPassbyVO> passby消息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/passby/message")
	public Object queryPassbyMessage(HttpServletRequest request)throws Exception{
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		String layerId = wrapper.getString("layerId");
		return apiThirdpartMonitor_relationService.queryPassbyMessage(layerId);
	}
	
	/**
	 * 修改设备信息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月11日 下午3:16:20
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value ="/device/information/change")
	public Object deviceInformationChange(HttpServletRequest request)throws Exception{
		
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		
		System.out.println("-------------------------修改设备信息-------------------------------");
		return apiThirdpartMonitor_relationService.deviceInformationChange(wrapper);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value ="/on/foreign/resource/receive" )
	public Object onForeignResourceReceive(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		String userId = wrapper.getString("userId");
		JSONObject message = wrapper.getJSONObject("message");
		System.out.println("收到连网推送外域的外域查询数据............");
		System.out.println(message.toJSONString());
		System.out.println(new StringBufferWrapper().append("目标用户：").append(userId).toString());
		
		return apiThirdpartMonitor_relationService.onForeignResourceReceive(userId,message);
		
	}
}
