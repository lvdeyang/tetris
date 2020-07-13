package com.sumavision.tetris.bvc.model.agenda;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/agenda/forward")
public class AgendaForwardController {

	@Autowired
	private AgendaForwardQuery agendaForwardQuery;
	
	@Autowired
	private AgendaForwardService agendaForwardService;
	
	/**
	 * 查询议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 上午10:04:39
	 * @param Long agendaId 议程id
	 * @return List<AgendaForwardVO> 转发列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long agendaId,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardQuery.load(agendaId);
	}
	
	/**
	 * 添加议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 上午10:11:51
	 * @param String type 议程转发类型
	 * @param String sourceType 源类型
	 * @param String sourceId 源id
	 * @param String destinationType 目的类型
	 * @param String destinationId 目的id
	 * @param Long agendaId 议程id
	 * @return AgendaForwardVO 议程转发
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String type,
			String sourceType,
			String sourceId,
			String destinationType,
			String destinationId,
			Long agendaId,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardService.add(type, sourceType, sourceId, destinationType, destinationId, agendaId);
	}
	
	/**
	 * 删除议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 上午10:14:28
	 * @param Long id 转发id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		agendaForwardService.delete(id);
		return null;
	}
}
