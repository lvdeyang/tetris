package com.sumavision.tetris.cs.program;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/cs/program")
public class ProgramController {
	@Autowired
	private ProgramQuery programQuery;
	
	@Autowired
	private ProgramService programService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get")
	public Object programGet(Long id,HttpServletRequest request) throws Exception {
		
		ProgramVO program = programQuery.getProgram(id);

		return program;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set")
	public Object programSet(String programInfo,HttpServletRequest request) throws Exception {
		
		ProgramVO program = JSONObject.parseObject(programInfo,ProgramVO.class);
		
		ProgramVO retrunProgram = programService.setProgram(program);
		
		return retrunProgram;
	}
}
