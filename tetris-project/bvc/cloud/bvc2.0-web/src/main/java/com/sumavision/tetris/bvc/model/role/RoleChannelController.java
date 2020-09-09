package com.sumavision.tetris.bvc.model.role;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/role/channel")
public class RoleChannelController {

	@Autowired
	private RoleChannelQuery roleChannelQuery;
	
	@Autowired
	private RoleChannelService roleChannelService;
	
	/**
	 * 查询通道类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午3:50:02
	 * @return Map<String, String> 类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		
		return roleChannelQuery.queryTypes();
	}
	
	/**
	 * 查询角色下的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午3:57:06
	 * @param Long roleId 角色id
	 * @return List<RoleChannelVO> 通道列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long roleId, 
			HttpServletRequest request) throws Exception{
		
		return roleChannelQuery.load(roleId);
	}
	
	/**
	 * 添加角色通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午4:04:46
	 * @param String name 名称
	 * @param String type 通道类型
	 * @param Long roleId 角色id
	 * @return RoleChannelVO 角色通道
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String type,
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		return roleChannelService.add(name, type, roleId);
	}
	
	/**
	 * 修改角色通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午4:04:46
	 * @param Long id 通道id
	 * @param String name 名称
	 * @param String type 通道类型
	 * @param Long roleId 角色id
	 * @return RoleChannelVO 角色通道
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String type,
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		return roleChannelService.edit(id, name, type, roleId);
	}
	
	/**
	 * 删除角色通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午4:12:35
	 * @param Long id 通道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		roleChannelService.delete(id);
		return null;
	}
	
}
