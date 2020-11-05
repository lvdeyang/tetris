package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;

@Service
public class RoleAgendaPermissionService {

	@Autowired
	private RoleAgendaPermissionDAO roleAgendaPermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	/**
	 * 向议程中添加角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午10:21:03
	 * @param Long agendaId 议程id
	 * @param JSONString roleIds 角色id列表
	 * @return List<RoleAgendaPermissionVO> 关联列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<RoleAgendaPermissionVO> add(
			Long agendaId,
			String roleIds) throws Exception{
		AgendaPO agenda = agendaDao.findOne(agendaId);
		if(agenda == null){
			throw new AgendaNotFoundException(agendaId);
		}
		List<Long> parsedRoleIds = JSON.parseArray(roleIds, Long.class);
		List<RolePO> roles = null;
		if(parsedRoleIds.size() > 0){
			roles = roleDao.findAll(parsedRoleIds);
		}
		List<RoleAgendaPermissionPO> entities = new ArrayList<RoleAgendaPermissionPO>();
		if(roles!=null && roles.size()>0){
			for(RolePO role:roles){
				RoleAgendaPermissionPO entity = new RoleAgendaPermissionPO();
				entity.setAgendaId(agendaId);
				entity.setRoleId(role.getId());
				entity.setUpdateTime(new Date());
				entities.add(entity);
			}
			roleAgendaPermissionDao.save(entities);
		}
		List<RoleAgendaPermissionVO> permissions = RoleAgendaPermissionVO.getConverter(RoleAgendaPermissionVO.class).convert(entities, RoleAgendaPermissionVO.class);
		if(roles!=null && roles.size()>0){
			for(RoleAgendaPermissionVO permission:permissions){
				for(RolePO role:roles){
					if(permission.getRoleId().equals(role.getId())){
						permission.setRoleName(role.getName());
						break;
					}
				}
			}
		}
		return permissions;
	}
	
	/**
	 * 议程中删除角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月1日 上午10:27:26
	 * @param Long id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		RoleAgendaPermissionPO entity = roleAgendaPermissionDao.findOne(id);
		if(entity != null){
			roleAgendaPermissionDao.delete(entity);
		}
	}
}
