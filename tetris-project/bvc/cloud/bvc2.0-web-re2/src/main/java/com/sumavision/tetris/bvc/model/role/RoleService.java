package com.sumavision.tetris.bvc.model.role;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.role.exception.RoleNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelType;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Service
public class RoleService {

	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private RoleChannelTerminalChannelPermissionDAO roleChannelTerminalChannelPermissionDao;
	
	/**
	 * 添加内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:38:17
	 * @param String name 角色名称
	 * @param String internalRoleType 内置角色类型
	 * @param String roleUserMappingType 授权类型
	 * @return RoleVO 角色
	 */
	@Transactional(rollbackFor = Exception.class)
	public RoleVO addInternal(
			String name,
			String internalRoleType,
			String roleUserMappingType) throws Exception{
		RolePO role = new RolePO();
		role.setName(name);
		role.setInternalRoleType(internalRoleType==null?null:InternalRoleType.valueOf(internalRoleType));
		role.setRoleUserMappingType(roleUserMappingType==null?null:RoleUserMappingType.valueOf(roleUserMappingType));
		role.setUpdateTime(new Date());
		roleDao.save(role);
		return new RoleVO().set(role);
	}
	
	/**
	 * 添加自定义角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 下午3:49:53
	 * @param String name 角色名称
	 * @param Long businessId 业务id
	 * @param String roleUserMappingType 授权类型
	 * @return RoleVO 角色
	 */
	@Transactional(rollbackFor = Exception.class)
	public RoleVO addCustom(
			String name,
			Long businessId,
			String roleUserMappingType) throws Exception{
		RolePO roleEntity = new RolePO();
		roleEntity.setName(name);
		roleEntity.setBusinessId(businessId);
		roleEntity.setRoleUserMappingType(roleUserMappingType==null?null:RoleUserMappingType.valueOf(roleUserMappingType));
		roleEntity.setUpdateTime(new Date());
		roleDao.save(roleEntity);
		RoleVO role = new RoleVO().set(roleEntity)
								  .setChannels(new ArrayList<RoleChannelVO>());
		if(roleEntity.getRoleUserMappingType().equals(RoleUserMappingType.ONE_TO_ONE)){
			RoleChannelPO videoEncodeChannelEntity = new RoleChannelPO();
			videoEncodeChannelEntity.setName("视频编码1");
			videoEncodeChannelEntity.setType(RoleChannelType.VIDEO_ENCODE);
			videoEncodeChannelEntity.setRoleId(roleEntity.getId());
			roleChannelDao.save(videoEncodeChannelEntity);
			role.getChannels().add(new RoleChannelVO().set(videoEncodeChannelEntity));
			
			RoleChannelPO audioEncodeChannelEntity = new RoleChannelPO();
			audioEncodeChannelEntity.setName("音频编码1");
			audioEncodeChannelEntity.setType(RoleChannelType.AUDIO_ENCODE);
			audioEncodeChannelEntity.setRoleId(roleEntity.getId());
			roleChannelDao.save(audioEncodeChannelEntity);
			role.getChannels().add(new RoleChannelVO().set(audioEncodeChannelEntity));
			
			//为角色绑定终端通道
			List<TerminalPO> terminalEntities = terminalDao.findAll();
			List<TerminalChannelPO> terminalChannelEntities = terminalChannelDao.findByTypeIn(new ArrayListWrapper<TerminalChannelType>().add(TerminalChannelType.VIDEO_ENCODE).add(TerminalChannelType.AUDIO_ENCODE).getList());
			
			if(terminalEntities!=null && terminalEntities.size()>0 && 
					terminalChannelEntities!=null && terminalChannelEntities.size()>0){
				List<RoleChannelTerminalChannelPermissionPO> permissions = new ArrayList<RoleChannelTerminalChannelPermissionPO>(); 
				for(TerminalPO terminalEntity:terminalEntities){
					TerminalChannelPO terminalVideoEncodeChannel = null;
					TerminalChannelPO terminalAudioEncodeChannel = null;
					for(TerminalChannelPO terminalChannelEntity:terminalChannelEntities){
						if(terminalVideoEncodeChannel==null &&
								TerminalChannelType.VIDEO_ENCODE.equals(terminalChannelEntity.getType()) && 
								terminalEntity.getId().equals(terminalChannelEntity.getTerminalId())){
							terminalVideoEncodeChannel = terminalChannelEntity;
						}
						if(terminalAudioEncodeChannel==null &&
								TerminalChannelType.AUDIO_ENCODE.equals(terminalChannelEntity.getType()) &&
								terminalEntity.getId().equals(terminalChannelEntity.getTerminalId())){
							terminalAudioEncodeChannel = terminalChannelEntity;
						}
						if(terminalVideoEncodeChannel!=null && terminalAudioEncodeChannel!=null){
							break;
						}
					}
					if(terminalVideoEncodeChannel != null){
						RoleChannelTerminalChannelPermissionPO permission = new RoleChannelTerminalChannelPermissionPO();
						permission.setRoleId(roleEntity.getId());
						permission.setRoleChannelId(videoEncodeChannelEntity.getId());
						permission.setTerminalId(terminalEntity.getId());
						permission.setTerminalChannelId(terminalVideoEncodeChannel.getId());
						permission.setUpdateTime(new Date());
						permissions.add(permission);
					}
					if(terminalAudioEncodeChannel != null){
						RoleChannelTerminalChannelPermissionPO permission = new RoleChannelTerminalChannelPermissionPO();
						permission.setRoleId(roleEntity.getId());
						permission.setRoleChannelId(audioEncodeChannelEntity.getId());
						permission.setTerminalId(terminalEntity.getId());
						permission.setTerminalChannelId(terminalAudioEncodeChannel.getId());
						permission.setUpdateTime(new Date());
						permissions.add(permission);
					}
				}
				if(permissions.size() > 0){
					roleChannelTerminalChannelPermissionDao.save(permissions);
				}
			}
		}
		return role;
	}
	
	/**
	 * 修改内置角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:38:17
	 * @param Long id 角色id
	 * @param String name 角色名称
	 * @param String internalRoleType 内置角色类型
	 * @param String roleUserMappingType 授权类型
	 * @return RoleVO 角色
	 */
	@Transactional(rollbackFor = Exception.class)
	public RoleVO editInternal(
			Long id,
			String name,
			String internalRoleType,
			String roleUserMappingType) throws Exception{
		RolePO role = roleDao.findOne(id);
		if(role == null){
			throw new RoleNotFoundException(id);
		}
		role.setName(name);
		role.setInternalRoleType(internalRoleType==null?null:InternalRoleType.valueOf(internalRoleType));
		role.setRoleUserMappingType(roleUserMappingType==null?null:RoleUserMappingType.valueOf(roleUserMappingType));
		roleDao.save(role);
		return new RoleVO().set(role);
	}
	
	/**
	 * 删除角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午1:44:13
	 * @param Long id 角色id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		RolePO role = roleDao.findOne(id);
		if(role != null){
			roleDao.delete(role);
		}
		List<RoleChannelPO> channels = roleChannelDao.findByRoleId(id);
		if(channels!=null && channels.size()>0){
			roleChannelDao.deleteInBatch(channels);
		}
	}
	
}
