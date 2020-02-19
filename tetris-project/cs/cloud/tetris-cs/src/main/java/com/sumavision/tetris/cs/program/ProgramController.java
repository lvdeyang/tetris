package com.sumavision.tetris.cs.program;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/cs/program")
public class ProgramController {
	@Autowired
	private ProgramQuery programQuery;
	
	@Autowired
	private ProgramService programService;
	
	/**
	 * 获取分屏信息(附带排单列表)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get")
	public Object programGet(Long id,HttpServletRequest request) throws Exception {
		
		ProgramVO program = programQuery.getProgram(id);

		return program;
	}
	
	/**
	 * 设置分屏信息(附带排单列表)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param programInfo 整体分屏信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set")
	public Object programSet(Long scheduleId, String programInfo,HttpServletRequest request) throws Exception {
		
		TemplateVO templateVO = JSON.parseObject(programInfo, TemplateVO.class);
		
		return programService.setProgram(scheduleId, templateVO);
	}
}
