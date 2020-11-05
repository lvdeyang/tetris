package com.sumavision.tetris.bvc.model.agenda;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/role/agenda/permission")
public class RoleAgendaPermissionController {

	@Autowired
	private RoleAgendaPermissionQuery roleAgendaPermissionQuery;
	
	@Autowired
	private RoleAgendaPermissionService roleAgendaPermissionService;
	
	/**
	 * 查询议程关联的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午8:59:38
	 * @param Long agendaId 议程id
	 * @return List<RoleAgendaPermissionVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long agendaId,
			HttpServletRequest request) throws Exception{
		
		return roleAgendaPermissionQuery.load(agendaId);
	}
	
	/**
	 * 查询议程关联的角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午8:59:38
	 * @param Long agendaId 议程id
	 * @param String channelType 通道类型VIDEO_ENCODE[|AUDIO_ENCODE][|VIDEO_DECODE][|AUDIO_DECODE][|ENCODE][|DECODE][|ALL]
	 * @return List<RoleAgendaPermissionVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/with/channels")
	public Object loadWithChannels(
			Long agendaId,
			String channelType,
			HttpServletRequest request) throws Exception{
		
		return roleAgendaPermissionQuery.loadWithChannel(agendaId, channelType);
	}

	/**
	 * 向议程中添加角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午10:21:03
	 * @param Long agendaId 议程id
	 * @param JSONString roleIds 角色id列表
	 * @return List<RoleAgendaPermissionVO> 关联列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long agendaId,
			String roleIds,
			HttpServletRequest request) throws Exception{
		
		return roleAgendaPermissionService.add(agendaId, roleIds);
	}
	
	/**
	 * 议程中删除角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午10:27:26
	 * @param Long id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		roleAgendaPermissionService.delete(id);
		return null;
	}
	
	
}
