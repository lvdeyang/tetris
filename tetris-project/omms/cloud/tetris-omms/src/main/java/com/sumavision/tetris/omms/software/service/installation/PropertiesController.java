package com.sumavision.tetris.omms.software.service.installation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/properties")
public class PropertiesController {

	@Autowired
	private PropertiesQuery propertiesQuery;
	
	@Autowired
	private PropertiesService propertiesService;
	
	/**
	 * 查询服务属性值类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 上午11:06:44
	 * @return Set<String> 值类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/value/types")
	public Object findValueTypes(){
		return propertiesQuery.findValueTypes();
	}
	
	/**
	 * 添加服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 下午4:50:32
	 * @param Long installationPackageId 安装包id
	 * @param String propertyKey 属性key
	 * @param String propertyName 属性名称
	 * @param String valueType 值类型
	 * @param String propertyDefaultValue 默认值
	 * @return ServicePropertiesVO 服务属性
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long installationPackageId,
			String propertyKey,
			String propertyName,
			String valueType,
			String propertyDefaultValue,
			HttpServletRequest request) throws Exception{
		
		return propertiesService.add(installationPackageId, propertyKey, propertyName, valueType, propertyDefaultValue);
	}
	
	/**
	 * 修改服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 下午4:53:42
	 * @param Long id 属性id
	  * @param String propertyKey 属性key
	 * @param String propertyName 属性名称
	 * @param String valueType 值类型
	 * @param String propertyDefaultValue 默认值
	 * @return ServicePropertiesVO 服务属性
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String propertyKey,
			String propertyName,
			String valueType,
			String propertyDefaultValue,
			HttpServletRequest request) throws Exception{
		
		return propertiesService.edit(id, propertyKey, propertyName, valueType, propertyDefaultValue);
	}
	
	/**
	 * 删除服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 下午4:55:01
	 * @param Long id 属性id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id,
			HttpServletRequest request) throws Exception{
		
		propertiesService.remove(id);
		return null;
	}
	
}
