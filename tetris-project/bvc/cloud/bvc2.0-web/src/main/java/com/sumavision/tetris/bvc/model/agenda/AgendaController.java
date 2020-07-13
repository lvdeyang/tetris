package com.sumavision.tetris.bvc.model.agenda;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller(value = "com.sumavision.tetris.bvc.model.agenda.AgendaController")
@RequestMapping(value = "/tetris/bvc/model/agenda")
public class AgendaController {

	@Autowired
	private AgendaQuery agendaQuery;
	
	@Autowired
	private AgendaService agendaService;
	
	/**
	 * 查询类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:26:16
	 * @return Map<String, String> 音频类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		
		return agendaQuery.queryTypes();
	}
	
	/**
	 * 分页查询议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:32:18
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<AgendaVO> 议程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage, 
			int pageSize, 
			HttpServletRequest request) throws Exception{
		
		return agendaQuery.load(currentPage, pageSize);
	}
	
	/**
	 * 添加议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:38:25
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Integer volume 音量
	 * @param String audioOperationType 音频操作类型
	 * @return AgendaVO 议程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String remark,
			Integer volume,
			String audioOperationType,
			HttpServletRequest request) throws Exception{
		
		return agendaService.add(name, remark, volume, audioOperationType);
	}
	
	/**
	 * 修改议程信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:42:26
	 * @param Long id 议程id
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Integer volume 音量
	 * @param String audioOperationType 音频操作类型
	 * @return AgendaVO 议程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String remark,
			Integer volume,
			String audioOperationType,
			HttpServletRequest request) throws Exception{
		
		return agendaService.edit(id, name, remark, volume, audioOperationType);
	}
	
	/**
	 * 删除议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:43:30
	 * @param Long id 议程id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		agendaService.delete(id);
		return null;
	}
	
}
