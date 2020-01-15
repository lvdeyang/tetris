package com.sumavision.bvc.control.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.enumeration.AutoBuildAgenda;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * @ClassName: 模板中的默认议程
 * @author zsy
 * @date 2018年3月29日 14:05:14 
 */
@Controller
@RequestMapping(value="/system/agenda/scheme")
public class AgendaSchemeController {
	
	/**
	 * @Title: 查询全部自动建立的议程，适用于表格列检索 
	 * @throws Exception
	 * @return List<AutoBuildAgendaVO> 自动建立的议程
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/all", method = RequestMethod.GET)
	public Object queryAll(HttpServletRequest request) throws Exception{
		
		JSONArray agendas = new JSONArray();
		AutoBuildAgenda[] types = AutoBuildAgenda.values();
		for(AutoBuildAgenda type:types){
			JSONObject agenda = new JSONObject();
			agenda.put("id", type.getId());
			agenda.put("name", type.getName());
			agendas.add(agenda);
		}
		return agendas;
	}

}
