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
@RequestMapping(value = "/api/thirdpart/web/resource")
public class ApiThirdpartWebResourceController {
	
	@Autowired
	private ApiResourceService apiResourceService;
	
	/**
	 *查询emr设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月25日 上午8:39:47
	 * @return Map<String, Object> data emr设备
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bundle/query")
	public Object bundleQuery(HttpServletRequest request) throws Exception{
		return apiResourceService.queryBundle();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bundle/list")
	public Object bundleList(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		String worknodeId = requestWrapper.getString("worknodeId");
		return apiResourceService.bundleList(worknodeId);
	}
}
