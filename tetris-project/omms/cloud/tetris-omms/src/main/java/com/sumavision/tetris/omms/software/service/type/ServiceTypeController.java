package com.sumavision.tetris.omms.software.service.type;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/service/type")
public class ServiceTypeController {
	
	@Autowired
	private ServiceTypeService serviceTypeService;
	
	
	@Autowired
	private ServiceTypeQuery serviceTypeQuery;
	
	/**
	 * 查询所有服务类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月20日 下午3:55:56
	 * @return List<OmmsSoftwareServiceTypeTreeNodeVO> 服务类型树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/all")
	public Object findAll(HttpServletRequest request) throws Exception{
		
		return serviceTypeQuery.findAll();
	}
	
	/**
	 * 一键创建服务类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月20日 下午4:22:00
	 * @return List<ServiceTypeVO> 创建的服务类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/one/button/create")
	public Object oneButtonCreate(HttpServletRequest request) throws Exception{
		return serviceTypeService.oneButtonCreate();
	}
	
	/**
	 * 保存服务类型字段<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月10日 上午11:14:13
	 * @param Long id 服务类型id
	 * @param String columnKey 字段索引
	 * @param String columnValue 字段值
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/column")
	public Object saveColumn(
			HttpServletRequest request,
			Long id,
			String columnKey,
			String columnValue) throws Exception{
		serviceTypeService.saveColumn(id, columnKey, columnValue);
		return null;
	}
	
}
