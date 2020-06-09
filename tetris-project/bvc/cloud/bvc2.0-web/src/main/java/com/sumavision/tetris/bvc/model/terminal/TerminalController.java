package com.sumavision.tetris.bvc.model.terminal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal")
public class TerminalController {

	@Autowired
	private TerminalQuery terminalQuery;
	
	@Autowired
	private TerminalService terminalService;
	
	/**
	 * 分页查询终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午2:59:49
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return Long total 总数据量
	 * @return List<TerminalVO> rows 终端列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return terminalQuery.load(currentPage, pageSize);
	}
	
	/**
	 * 添加终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午3:14:38
	 * @param String name 终端名称
	 * @return TerminalVO 终端
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/create")
	public Object create(
			String name,
			HttpServletRequest request) throws Exception{
		
		return terminalService.create(name);
	}
	
	/**
	 * 修改终端名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月8日 上午9:37:28
	 * @param Long id 终端id
	 * @param String name 名称
	 * @return TerminalVO 终端
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		return terminalService.edit(id, name);
	}
	
	/**
	 * 删除终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月8日 上午9:43:50
	 * @param Long id 终端id
 	 * @return TerminalVO 终端
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		return terminalService.delete(id);
	}
	
}
