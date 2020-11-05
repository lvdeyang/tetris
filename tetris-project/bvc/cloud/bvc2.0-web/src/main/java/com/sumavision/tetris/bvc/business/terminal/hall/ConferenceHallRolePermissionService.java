package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Service
public class ConferenceHallRolePermissionService {

	@Autowired
	private ConferenceHallRolePermissionDAO conferenceHallRolePermissionDao;
	
	/**
	 * 添加权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月5日 下午4:36:12
	 * @param Long roleId 角色id
	 * @param Long conferenceHallId 会场id
	 * @param String privilege 权限类型
	 * @return key String 权限类型key
	 * @return value String 权限类型value
	 */
	public Map<String, String> add(
			Long roleId,
			Long conferenceHallId,
			String privilege) throws Exception{
		PrivilegeType privilegeType = PrivilegeType.valueOf(privilege);
		ConferenceHallRolePermissionPO permission = new ConferenceHallRolePermissionPO();
		permission.setRoleId(roleId);
		permission.setConferenceHallId(conferenceHallId);
		permission.setPrivilegeType(PrivilegeType.valueOf(privilege));
		permission.setUpdateTime(new Date());
		conferenceHallRolePermissionDao.save(permission);
		return new HashMapWrapper<String, String>().put("key", privilegeType.toString())
												   .put("value", privilegeType.getName())
												   .getMap();
				
	}
	
	/**
	 * 删除权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月5日 下午4:36:12
	 * @param Long roleId 角色id
	 * @param Long conferenceHallId 会场id
	 * @param String privilege 权限类型
	 * @return key String 权限类型key
	 * @return value String 权限类型value
	 */
	public Map<String, String> remove(
			Long roleId,
			Long conferenceHallId,
			String privilege) throws Exception{
		PrivilegeType privilegeType = PrivilegeType.valueOf(privilege);
		ConferenceHallRolePermissionPO permission = conferenceHallRolePermissionDao.findByRoleIdAndConferenceHallIdAndPrivilegeType(roleId, conferenceHallId, privilegeType);
		if(permission != null){
			conferenceHallRolePermissionDao.delete(permission);
		}
		return new HashMapWrapper<String, String>().put("key", privilegeType.toString())
												   .put("value", privilegeType.getName())
												   .getMap();
	}
	
}
