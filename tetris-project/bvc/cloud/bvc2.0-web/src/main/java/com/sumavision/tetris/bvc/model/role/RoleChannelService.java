package com.sumavision.tetris.bvc.model.role;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.role.exception.RoleChannelNotFoundException;
import com.sumavision.tetris.bvc.model.role.exception.RoleNotFoundException;

@Service
public class RoleChannelService {

	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	/**
	 * 添加角色通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午4:04:46
	 * @param String name 名称
	 * @param String type 通道类型
	 * @param Long roleId 角色id
	 * @return RoleChannelVO 角色通道
	 */
	@Transactional(rollbackFor = Exception.class)
	public RoleChannelVO add(
			String name,
			String type,
			Long roleId) throws Exception{
		RolePO role = roleDao.findOne(roleId);
		if(role == null){
			throw new RoleNotFoundException(roleId);
		}
		RoleChannelPO roleChannel = new RoleChannelPO();
		roleChannel.setName(name);
		roleChannel.setType(RoleChannelType.valueOf(type));
		roleChannel.setRoleId(roleId);
		roleChannel.setUpdateTime(new Date());
		roleChannelDao.save(roleChannel);
		return new RoleChannelVO().set(roleChannel);
	}
	
	/**
	 * 修改角色通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午4:04:46
	 * @param Long id 通道id
	 * @param String name 名称
	 * @param String type 通道类型
	 * @param Long roleId 角色id
	 * @return RoleChannelVO 角色通道
	 */
	@Transactional(rollbackFor = Exception.class)
	public RoleChannelVO edit(
			Long id,
			String name,
			String type,
			Long roleId) throws Exception{
		RoleChannelPO roleChannel = roleChannelDao.findOne(id);
		if(roleChannel == null){
			throw new RoleChannelNotFoundException(id);
		}
		RolePO role = roleDao.findOne(roleId);
		if(role == null){
			throw new RoleNotFoundException(roleId);
		}
		roleChannel.setName(name);
		roleChannel.setType(RoleChannelType.valueOf(type));
		roleChannel.setRoleId(roleId);
		roleChannelDao.save(roleChannel);
		return new RoleChannelVO().set(roleChannel);
	}
	
	/**
	 * 删除角色通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午4:12:35
	 * @param Long id 通道id
	 */
	public void delete(Long id) throws Exception{
		RoleChannelPO roleChannel = roleChannelDao.findOne(id);
		if(roleChannel != null){
			roleChannelDao.delete(roleChannel);
		}
	}
	
}
