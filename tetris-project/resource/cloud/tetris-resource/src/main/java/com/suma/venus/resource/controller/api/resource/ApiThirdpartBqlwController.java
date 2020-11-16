package com.suma.venus.resource.controller.api.resource;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.controller.ControllerBase;
import com.suma.venus.resource.service.ApiThirdpartBqlwService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/thirdpart/bqlw")
public class ApiThirdpartBqlwController extends ControllerBase{
	
	@Autowired
	private ApiThirdpartBqlwService apiThirdpartBqlwService;

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
	public Object queryServerNodeInfo()throws Exception{
		
		return apiThirdpartBqlwService.queryServerNodeInfo();
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
		
		return apiThirdpartBqlwService.foreignServerNodeOff(request);
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
		
		return apiThirdpartBqlwService.foreignServerNodeOn(request);
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

		return apiThirdpartBqlwService.devicePermissionAdd(request);
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
		
		return apiThirdpartBqlwService.devicePermissionRemove(request);
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
		
		return apiThirdpartBqlwService.devicePermissionChange(request);
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
		
		return apiThirdpartBqlwService.folderUpdate(institutionsArray);
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
		
		return apiThirdpartBqlwService.deviceStatusChange(request);
	}
}
