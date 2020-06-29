package com.sumavision.tetris.bvc.model.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleChannelQuery {

	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	/**
	 * 查询通道类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午3:50:02
	 * @return Map<String, String> 类型列表
	 */
	public Map<String, String> queryTypes() throws Exception{
		RoleChannelType[] values = RoleChannelType.values();
		Map<String, String> roleChannelTypes = new HashMap<String, String>();
		for(RoleChannelType value:values){
			roleChannelTypes.put(value.toString(), value.getName());
		}
		return roleChannelTypes;
	}
	
	/**
	 * 查询角色下的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 下午3:57:06
	 * @param Long roleId 角色id
	 * @return List<RoleChannelVO> 通道列表
	 */
	public List<RoleChannelVO> load(Long roleId) throws Exception{
		List<RoleChannelPO> channels = roleChannelDao.findByRoleId(roleId);
		return RoleChannelVO.getConverter(RoleChannelVO.class).convert(channels, RoleChannelVO.class);
	}
	
}
