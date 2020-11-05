package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.bvc.model.agenda.exception.LayoutVirtualSourceAlreadyBind;
import com.sumavision.tetris.bvc.model.agenda.exception.LayoutVirtualSourceNotAlreadyBind;

@Service
public class LayoutVirtualSourceTemplateService {
	
	@Autowired
	private LayoutVirtualSourceTemplateDAO layoutVirtualSourceTemplateDAO;

	public Object bindVirtual(
			Long agendaLayoutTemplateId,
			Long layoutPositionId,
			Long virtualSourceId) throws Exception{
		List<LayoutVirtualSourceTemplatePO> layoutVirtualSourceTemplatePOs = layoutVirtualSourceTemplateDAO.findAll();
		for (LayoutVirtualSourceTemplatePO layoutVirtualSourceTemplatePO : layoutVirtualSourceTemplatePOs) {
			if(agendaLayoutTemplateId.equals(layoutVirtualSourceTemplatePO.getAgendaLayoutTemplateId())
					&& layoutPositionId.equals(layoutVirtualSourceTemplatePO.getLayoutPositionId())){
				throw new LayoutVirtualSourceAlreadyBind();
			}
		}
		LayoutVirtualSourceTemplatePO layoutVirtualSourceTemplatePO = new LayoutVirtualSourceTemplatePO();
		layoutVirtualSourceTemplatePO.setAgendaLayoutTemplateId(agendaLayoutTemplateId);
		layoutVirtualSourceTemplatePO.setLayoutPositionId(layoutPositionId);
		layoutVirtualSourceTemplatePO.setVirtualSourceId(virtualSourceId);
		
		layoutVirtualSourceTemplateDAO.save(layoutVirtualSourceTemplatePO);
		return null;
	}
	
	public Object unbindVirtual(
			Long agendaLayoutTemplateId,
			Long layoutPositionId)throws Exception{
		List<LayoutVirtualSourceTemplatePO> layoutVirtualSourceTemplatePOs = layoutVirtualSourceTemplateDAO.findAll();
		Integer a = null;
		for(LayoutVirtualSourceTemplatePO layoutVirtualSourceTemplatePO:layoutVirtualSourceTemplatePOs){
			if(agendaLayoutTemplateId.equals(layoutVirtualSourceTemplatePO.getAgendaLayoutTemplateId())
					&& layoutPositionId.equals(layoutVirtualSourceTemplatePO.getLayoutPositionId())){
				layoutVirtualSourceTemplateDAO.delete(layoutVirtualSourceTemplatePO);
				a = 0;
			}
		}
		if(a == null){
			throw new LayoutVirtualSourceNotAlreadyBind();
		}			
		return null;
	}
	
}
