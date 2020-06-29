package com.sumavision.tetris.bvc.model.role;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.bvc.model.role.exception.RoleChannelNotFoundException;
import com.sumavision.tetris.bvc.model.role.exception.RoleNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;

@Service
public class RoleChannelTerminalBundleChannelPermissionService {

	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private RoleChannelTerminalBundleChannelPermissionDAO roleChannelTerminalBundleChannelPermissionDao;
	
	/**
	 * 添加角色通道和终端通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午4:56:50
	 * @param Long roleId 角色id
	 * @param Long roleChannelId 角色通道id
	 * @param Long terminalId 终端id
	 * @param JSONString terminalChannelIds 终端通道id列表
	 * @return List<RoleChannelTerminalBundleChannelPermissionVO> 关联列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<RoleChannelTerminalBundleChannelPermissionVO> add(
			Long roleId,
			Long roleChannelId,
			Long terminalId,
			String terminalChannelIds) throws Exception{
		RolePO role = roleDao.findOne(roleId);
		if(role == null){
			throw new RoleNotFoundException(roleId);
		}
		RoleChannelPO roleChannel = roleChannelDao.findOne(roleChannelId);
		if(roleChannel == null){
			throw new RoleChannelNotFoundException(roleChannelId);
		}
		TerminalPO terminal = terminalDao.findOne(terminalId);
		if(terminal == null){
			throw new TerminalNotFoundException(terminalId);
		}
		List<Long> parsedTerminalChannelIds = JSON.parseArray(terminalChannelIds, Long.class);
		List<TerminalChannelPO> terminalChannels = terminalChannelDao.findAll(parsedTerminalChannelIds);
		List<RoleChannelTerminalBundleChannelPermissionPO> entities = new ArrayList<RoleChannelTerminalBundleChannelPermissionPO>();
		List<RoleChannelTerminalBundleChannelPermissionVO> permissions = new ArrayList<RoleChannelTerminalBundleChannelPermissionVO>();
		if(terminalChannels!=null && terminalChannels.size()>0){
			for(TerminalChannelPO terminalChannel:terminalChannels){
				RoleChannelTerminalBundleChannelPermissionPO entity = new RoleChannelTerminalBundleChannelPermissionPO();
				entity.setRoleId(role.getId());
				entity.setRoleChannelId(roleChannel.getId());
				entity.setTerminalId(terminal.getId());
				entity.setTerminalChannelId(terminalChannel.getId());
				entity.setUpdateTime(new Date());
				entities.add(entity);
			}
			roleChannelTerminalBundleChannelPermissionDao.save(entities);
			for(RoleChannelTerminalBundleChannelPermissionPO entity:entities){
				RoleChannelTerminalBundleChannelPermissionVO permission = new RoleChannelTerminalBundleChannelPermissionVO();
				permission.set(entity)
						  .setRoleName(role.getName())
						  .setRoleChannelName(roleChannel.getName())
						  .setTerminalName(terminal.getName());
				for(TerminalChannelPO terminalChannel:terminalChannels){
					if(terminalChannel.getId().equals(permission.getTerminalChannelId())){
						permission.setTerminalChannelName(terminalChannel.getName());
						break;
					}
				}
				permissions.add(permission);
			}
		}
		return permissions;
	}
	
	/**
	 * 删除关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午5:06:44
	 * @param Long id 关联id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		RoleChannelTerminalBundleChannelPermissionPO permission = roleChannelTerminalBundleChannelPermissionDao.findOne(id);
		if(permission != null){
			roleChannelTerminalBundleChannelPermissionDao.delete(permission);
		}
	}
	
}
