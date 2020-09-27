package com.sumavision.tetris.bvc.model.agenda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/layout/virtual/source/template")
public class LayoutVirtualSourceTemplateController {
	
	@Autowired
	private LayoutVirtualSourceTemplateService layoutVirtualSourceTemplateService;

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bind/virtual")
	public Object bindVirtual(
			Long agendaLayoutTemplateId,
			Long layoutPositionId,
			Long virtualSourceId) throws Exception{
		return layoutVirtualSourceTemplateService.bindVirtual(agendaLayoutTemplateId, layoutPositionId, virtualSourceId);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/unbind/virtual")
	public Object unbindVirtual(
			Long agendaLayoutTemplateId,
			Long layoutPositionId)throws Exception{
		return layoutVirtualSourceTemplateService.unbindVirtual(agendaLayoutTemplateId, layoutPositionId);
	}
}
