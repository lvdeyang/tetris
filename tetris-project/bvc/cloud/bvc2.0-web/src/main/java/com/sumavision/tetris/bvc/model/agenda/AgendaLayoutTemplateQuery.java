package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.role.exception.RoleNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPO;

@Component
public class AgendaLayoutTemplateQuery {

	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private AgendaLayoutTemplateDAO agendaLayoutTemplateDao;
	
	/**
	 * 查询议程布局信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午2:36:51
	 * @param Long agendaId 议程id
	 * @param Long roleId 角色id
	 * @return List<AgendaLayoutTemplateVO> 布局信息列表
	 */
	public List<AgendaLayoutTemplateVO> load(
			Long agendaId,
			Long roleId) throws Exception{
		AgendaPO agenda = agendaDao.findOne(agendaId);
		if(agenda == null){
			throw new AgendaNotFoundException(agendaId);
		}
		RolePO role = roleDao.findOne(roleId);
		if(role == null){
			throw new RoleNotFoundException(roleId);
		}
		List<AgendaLayoutTemplateVO> templates = new ArrayList<AgendaLayoutTemplateVO>();
		List<AgendaLayoutTemplatePO> entities = agendaLayoutTemplateDao.findByAgendaIdAndRoleId(agendaId, roleId);
		if(entities!=null && entities.size()>0){
			Set<Long> terminalIds = new HashSet<Long>();
			Set<Long> layoutIds = new HashSet<Long>();
			for(AgendaLayoutTemplatePO entity:entities){
				terminalIds.add(entity.getTerminalId());
				layoutIds.add(entity.getLayoutId());
			}
			List<TerminalPO> terminals = terminalDao.findAll(terminalIds);
			List<LayoutPO> layouts = layoutDao.findAll(layoutIds);
			for(AgendaLayoutTemplatePO entity:entities){
				AgendaLayoutTemplateVO template = new AgendaLayoutTemplateVO().set(entity)
																			  .setAgendaName(agenda.getName())
																			  .setRoleName(role.getName());
				if(terminals!=null && terminals.size()>0){
					for(TerminalPO terminal:terminals){
						if(terminal.getId().equals(template.getTerminalId())){
							template.setTerminalName(terminal.getName());
							break;
						}
					}
				}
				if(layouts!=null && layouts.size()>0){
					for(LayoutPO layout:layouts){
						if(layout.getId().equals(template.getLayoutId())){
							template.setLayoutName(layout.getName());
							break;
						}
					}
				}
				templates.add(template);
			}
		}
		return templates;
	}
	
}
