package com.sumavision.tetris.bvc.model.agenda;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/agenda/layout/template")
public class AgendaLayoutTemplateController {

	@Autowired
	private AgendaLayoutTemplateQuery agendaLayoutTemplateQuery;
	
	@Autowired
	private AgendaLayoutTemplateService agendaLayoutTemplateService;
	
	/**
	 * 查询议程布局信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午2:36:51
	 * @param Long agendaId 议程id
	 * @param Long roleId 角色id
	 * @return List<AgendaLayoutTemplateVO> 布局信息列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long agendaId,
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		return agendaLayoutTemplateQuery.load(agendaId, roleId);
	}
	
	/**
	 * 添加议程布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午1:55:37
	 * @param Long agendaId 议程id
	 * @param Long roleId 角色id
	 * @param Long terminalId 终端id
	 * @param Long layoutId 布局id
	 * @return AgendaLayoutTemplateVO 布局信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long agendaId,
			Long roleId,
			Long terminalId,
			Long layoutId,
			HttpServletRequest request) throws Exception{
		
		return agendaLayoutTemplateService.add(agendaId, roleId, terminalId, layoutId);
	}
	
	/**
	 * 修改议程布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午1:55:37
	 * @param Long id 布局信息id
	 * @param Long agendaId 议程id
	 * @param Long roleId 角色id
	 * @param Long terminalId 终端id
	 * @param Long layoutId 布局id
	 * @return AgendaLayoutTemplateVO 布局信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			Long agendaId,
			Long roleId,
			Long terminalId,
			Long layoutId,
			HttpServletRequest request) throws Exception{
		
		return agendaLayoutTemplateService.edit(id, agendaId, roleId, terminalId, layoutId);
	}
	
	/**
	 * 删除议程布局br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午2:04:59
	 * @param Long id 布局信息id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		agendaLayoutTemplateService.delete(id);
		return null;
	}
	
}
