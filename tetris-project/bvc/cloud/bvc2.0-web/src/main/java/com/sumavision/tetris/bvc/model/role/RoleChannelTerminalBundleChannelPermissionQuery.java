package com.sumavision.tetris.bvc.model.role;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;

@Component
public class RoleChannelTerminalBundleChannelPermissionQuery {

	@Autowired
	private RoleChannelTerminalBundleChannelPermissionDAO roleChannelTerminalBundleChannelPermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	/**
	 * 根据角色通道查询终端通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月29日 下午2:36:20
	 * @param Long roleChannelId 角色通道id
	 * @return List<RoleChannelTerminalBundleChannelPermissionVO> 通道关联列表
	 */
	public List<RoleChannelTerminalBundleChannelPermissionVO> load(Long roleChannelId) throws Exception{
		List<RoleChannelTerminalBundleChannelPermissionVO> permissions = new ArrayList<RoleChannelTerminalBundleChannelPermissionVO>();
		List<RoleChannelTerminalBundleChannelPermissionPO> permissionEntities = roleChannelTerminalBundleChannelPermissionDao.findByRoleChannelId(roleChannelId);
		if(permissionEntities==null || permissionEntities.size()<=0) return permissions;
		List<Long> roleIds = new ArrayList<Long>();
		List<Long> roleChannelIds = new ArrayList<Long>();
		List<Long> terminalIds = new ArrayList<Long>();
		List<Long> terminalChannelIds = new ArrayList<Long>();
		for(RoleChannelTerminalBundleChannelPermissionPO permissionEntity:permissionEntities){
			roleIds.add(permissionEntity.getRoleId());
			roleChannelIds.add(permissionEntity.getRoleChannelId());
			terminalIds.add(permissionEntity.getTerminalId());
			terminalChannelIds.add(permissionEntity.getTerminalChannelId());
		}
		List<RolePO> roles = roleDao.findAll(roleIds);
		List<RoleChannelPO> roleChannels = roleChannelDao.findAll(roleChannelIds);
		List<TerminalPO> terminals = terminalDao.findAll(terminalIds);
		List<TerminalChannelPO> terminalChannels = terminalChannelDao.findAll(terminalChannelIds);
		for(RoleChannelTerminalBundleChannelPermissionPO permissionEntity:permissionEntities){
			RoleChannelTerminalBundleChannelPermissionVO permission = new RoleChannelTerminalBundleChannelPermissionVO().set(permissionEntity);
			for(RolePO role:roles){
				if(role.getId().equals(permission.getRoleId())){
					permission.setRoleName(role.getName());
					break;
				}
			}
			for(RoleChannelPO roleChannel:roleChannels){
				if(roleChannel.getId().equals(permission.getRoleChannelId())){
					permission.setRoleChannelName(roleChannel.getName());
					break;
				}
			}
			for(TerminalPO terminal:terminals){
				if(terminal.getId().equals(permission.getTerminalId())){
					permission.setTerminalName(terminal.getName());
					break;
				}
			}
			for(TerminalChannelPO terminalChannel:terminalChannels){
				if(terminalChannel.getId().equals(permission.getTerminalChannelId())){
					permission.setTerminalChannelName(terminalChannel.getName());
					break;
				}
			}
			permissions.add(permission);
		}
		return permissions;
	}
	
}
