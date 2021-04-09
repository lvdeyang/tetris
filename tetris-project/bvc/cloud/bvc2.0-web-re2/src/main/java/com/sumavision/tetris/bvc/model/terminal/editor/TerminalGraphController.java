package com.sumavision.tetris.bvc.model.terminal.editor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/terminal/graph")
public class TerminalGraphController {

	@Autowired
	private TerminalGraphQuery terminalGraphQuery;
	
	/**
	 * 查询终端拓补图<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午10:55:39
	 * @param Long terminalId 终端id
	 * @return TerminalGraphNodeVO 终端拓补图
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long terminalId,
			HttpServletRequest request) throws Exception{
		
		return terminalGraphQuery.load(terminalId);
	}
	
}
