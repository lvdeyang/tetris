package com.sumavision.tetris.bvc.model.agenda;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.agenda.exception.AgendaLayoutTemplateNotFoundException;
import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.role.exception.RoleNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.terminal.layout.exception.LayoutNotFoundException;

@Service
public class AgendaLayoutTemplateService {

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
	 * 添加议程布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午1:55:37
	 * @param Long agendaId 议程id
	 * @param Long roleId 角色id
	 * @param Long terminalId 终端id
	 * @param Long layoutId 布局id
	 * @return AgendaLayoutTemplateVO 布局信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaLayoutTemplateVO add(
			Long agendaId,
			Long roleId,
			Long terminalId,
			Long layoutId) throws Exception{
		AgendaPO agenda = agendaDao.findOne(agendaId);
		if(agenda == null){
			throw new AgendaNotFoundException(agendaId);
		}
		RolePO role = roleDao.findOne(roleId);
		if(role == null){
			throw new RoleNotFoundException(roleId);
		}
		TerminalPO terminal = terminalDao.findOne(terminalId);
		if(terminal == null){
			throw new TerminalNotFoundException(terminalId);
		}
		LayoutPO layout = layoutDao.findOne(layoutId);
		if(layout == null){
			throw new LayoutNotFoundException(layoutId);
		}
		AgendaLayoutTemplatePO entity = new AgendaLayoutTemplatePO();
		entity.setAgendaId(agendaId);
		entity.setRoleId(roleId);
		entity.setTerminalId(terminalId);
		entity.setLayoutId(layoutId);
		entity.setUpdateTime(new Date());
		agendaLayoutTemplateDao.save(entity);
		return new AgendaLayoutTemplateVO().set(entity)
										   .setAgendaName(agenda.getName())
										   .setRoleName(role.getName())
										   .setTerminalName(terminal.getName())
										   .setLayoutName(layout.getName());
	}
	
	/**
	 * 修改议程布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午1:55:37
	 * @param Long id 布局信息id
	 * @param Long agendaId 议程id
	 * @param Long roleId 角色id
	 * @param Long terminalId 终端id
	 * @param Long layoutId 布局id
	 * @return AgendaLayoutTemplateVO 布局信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaLayoutTemplateVO edit(
			Long id,
			Long agendaId,
			Long roleId,
			Long terminalId,
			Long layoutId) throws Exception{
		AgendaLayoutTemplatePO entity = agendaLayoutTemplateDao.findOne(id);
		if(entity == null){
			throw new AgendaLayoutTemplateNotFoundException(id);
		}
		AgendaPO agenda = agendaDao.findOne(agendaId);
		if(agenda == null){
			throw new AgendaNotFoundException(agendaId);
		}
		RolePO role = roleDao.findOne(roleId);
		if(role == null){
			throw new RoleNotFoundException(roleId);
		}
		TerminalPO terminal = terminalDao.findOne(terminalId);
		if(terminal == null){
			throw new TerminalNotFoundException(terminalId);
		}
		LayoutPO layout = layoutDao.findOne(layoutId);
		if(layout == null){
			throw new LayoutNotFoundException(layoutId);
		}
		entity.setAgendaId(agendaId);
		entity.setRoleId(roleId);
		entity.setTerminalId(terminalId);
		entity.setLayoutId(layoutId);
		entity.setUpdateTime(new Date());
		agendaLayoutTemplateDao.save(entity);
		return new AgendaLayoutTemplateVO().set(entity)
										   .setAgendaName(agenda.getName())
										   .setRoleName(role.getName())
										   .setTerminalName(terminal.getName())
										   .setLayoutName(layout.getName());
	}
	
	/**
	 * 删除议程布局br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午2:04:59
	 * @param Long id 布局信息id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		AgendaLayoutTemplatePO entity = agendaLayoutTemplateDao.findOne(id);
		if(entity != null){
			agendaLayoutTemplateDao.delete(entity);
		}
	}
	
}
