package com.sumavision.tetris.bvc.model.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;

@Component
public class RoleChannelTerminalChannelPermissionQuery {

	@Autowired
	private RoleChannelTerminalChannelPermissionDAO roleChannelTerminalBundleChannelPermissionDao;
	
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
	public List<RoleChannelTerminalChannelPermissionVO> load(Long roleChannelId) throws Exception{
		List<RoleChannelTerminalChannelPermissionVO> permissions = new ArrayList<RoleChannelTerminalChannelPermissionVO>();
		List<RoleChannelTerminalChannelPermissionPO> permissionEntities = roleChannelTerminalBundleChannelPermissionDao.findByRoleChannelId(roleChannelId);
		if(permissionEntities==null || permissionEntities.size()<=0) return permissions;
		List<Long> roleIds = new ArrayList<Long>();
		List<Long> roleChannelIds = new ArrayList<Long>();
		List<Long> terminalIds = new ArrayList<Long>();
		List<Long> terminalChannelIds = new ArrayList<Long>();
		for(RoleChannelTerminalChannelPermissionPO permissionEntity:permissionEntities){
			roleIds.add(permissionEntity.getRoleId());
			roleChannelIds.add(permissionEntity.getRoleChannelId());
			terminalIds.add(permissionEntity.getTerminalId());
			terminalChannelIds.add(permissionEntity.getTerminalChannelId());
		}
		List<RolePO> roles = roleDao.findAll(roleIds);
		List<RoleChannelPO> roleChannels = roleChannelDao.findAll(roleChannelIds);
		List<TerminalPO> terminals = terminalDao.findAll(terminalIds);
		List<TerminalChannelPO> terminalChannels = terminalChannelDao.findAll(terminalChannelIds);
		for(RoleChannelTerminalChannelPermissionPO permissionEntity:permissionEntities){
			RoleChannelTerminalChannelPermissionVO permission = new RoleChannelTerminalChannelPermissionVO().set(permissionEntity);
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
	
	/**
	 * 查询角色终端通道映射<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 下午3:27:46
	 * @param Long roleId 角色id
	 * @return List<RoleChannelTerminalChannelPermissionVO> 通道关联
	 */
	public List<RoleChannelTerminalChannelPermissionVO> loadByRoleIdAndTerminalId(
			Long roleId,
			Long terminalId) throws Exception{
		RolePO roleEntity = roleDao.findOne(roleId);
		List<RoleChannelPO> roleChannelEntities = roleChannelDao.findByRoleId(roleId);
		List<Long> roleChannelIds = new ArrayListWrapper<Long>().add(-1l).getList();
		if(roleChannelEntities!=null && roleChannelEntities.size()>0){
			for(RoleChannelPO roleChannelEntity:roleChannelEntities){
				roleChannelIds.add(roleChannelEntity.getId());
			}
		}
		List<RoleChannelTerminalChannelPermissionPO> permissionEntities = roleChannelTerminalBundleChannelPermissionDao.findByRoleChannelIdInAndTerminalId(roleChannelIds, terminalId);
		
		Set<Long> terminalIds = new HashSetWrapper<Long>().add(-1l).getSet();
		Set<Long> terminalChannelIds = new HashSetWrapper<Long>().add(-1l).getSet();
		if(permissionEntities!=null && permissionEntities.size()>0){
			for(RoleChannelTerminalChannelPermissionPO permissionEntity:permissionEntities){
				terminalIds.add(permissionEntity.getTerminalId());
				terminalChannelIds.add(permissionEntity.getTerminalChannelId());
			}
		}
		
		List<TerminalPO> terminalEntities = terminalDao.findAll(terminalIds);
		List<TerminalChannelPO> terminalChannelEntities = terminalChannelDao.findAll(terminalChannelIds);
		
		List<RoleChannelTerminalChannelPermissionVO> permissions = new ArrayList<RoleChannelTerminalChannelPermissionVO>();
		if(permissionEntities!=null && permissionEntities.size()>0){
			for(RoleChannelTerminalChannelPermissionPO permissionEntity:permissionEntities){
				RoleChannelTerminalChannelPermissionVO permission = new RoleChannelTerminalChannelPermissionVO().set(permissionEntity);
				permission.setRoleName(roleEntity.getName());
				if(roleChannelEntities!=null && roleChannelEntities.size()>0){
					for(RoleChannelPO roleChannelEntity:roleChannelEntities){
						if(roleChannelEntity.getId().equals(permissionEntity.getRoleChannelId())){
							permission.setRoleChannelName(roleChannelEntity.getName());
							break;
						}
					}
				}
				if(terminalEntities!=null && terminalEntities.size()>0){
					for(TerminalPO terminalEntity:terminalEntities){
						if(terminalEntity.getId().equals(permissionEntity.getTerminalId())){
							permission.setTerminalName(terminalEntity.getName());
							break;
						}
					}
				}
				if(terminalChannelEntities!=null && terminalChannelEntities.size()>0){
					for(TerminalChannelPO terminalChannelEntity:terminalChannelEntities){
						if(terminalChannelEntity.getId().equals(permissionEntity.getTerminalChannelId())){
							permission.setTerminalChannelName(terminalChannelEntity.getName());
							break;
						}
					}
				}
				permissions.add(permission);
			}
		}
		return permissions;
	}
	
}
