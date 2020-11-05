package com.suma.venus.resource.controller.api.resource;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.service.ApiResourceService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

/**
 * G01资源接口<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月19日 下午1:45:02
 */
@Controller
@RequestMapping(value = "/api/g01/web/resource")
public class ApiG01WebResourceController {
	
	@Autowired
	private ApiResourceService apiResourceService;

	/**
	 * 添加g01设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:22:35
	 * @param String bundleIp 设备ip
	 * @param String daId DA系统id
	 * @return G01BundleVO g01设备信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bundle/add")
	public Object bundleAdd(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String bundleIp = requestWrapper.getString("bundleIp");
		String daId = requestWrapper.getString("daId");
		String location = requestWrapper.getString("location");
		String group = requestWrapper.getString("group");
		String type = requestWrapper.getString("type");
		
		
		return apiResourceService.addG01Bundle(bundleIp, daId,location,group,type);
	}
	
	/**
	 * 认证上线g01设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:24:46
	 * @param String bundleIp 设备ip
	 * @param String daId DA系统id
	 * @return G01BundleVO g01设备信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bundle/certify")
	public Object bundleCertify(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String bundleIp = requestWrapper.getString("bundleIp");
		String daId = requestWrapper.getString("daId");
		
		return apiResourceService.certifyG01Bundle(bundleIp, daId);
	}
	
	/**
	 * 删除g01设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:26:20
	 * @param String bundleId 设备bundleId
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bundle/delete")
	public Object bundleDelete(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String bundleId = requestWrapper.getString("bundleId");
		
		apiResourceService.deleteG01Bundle(bundleId);
		
		return null;
	}
	
	/**
	 * 更新g01设备<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月24日 上午9:53:19
	 * @param String bundleId  设备id
	 * @param String location 设备地址，经纬度
	 * @param String group 设备分组
	 * @param String type 设备类型
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bundle/update")
	public Object bundleUpdate(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String bundleId = requestWrapper.getString("bundleId");
		String location = requestWrapper.getString("location");
		String group = requestWrapper.getString("group");
		String type = requestWrapper.getString("type");
		
		return apiResourceService.updateG01Bundle(bundleId,location,group,type);
		
	}
	
}
