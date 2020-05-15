package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderScreenPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupDecoderScreenPO.class, idClass = long.class)
public interface CommandGroupDecoderScreenDAO extends MetBaseDAO<CommandGroupDecoderScreenPO>{

//	public CommandGroupUserPlayerPO findByBundleId(String bundleId);
//	
//	/**
//	 * 根据用户以及位置查询布局信息<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年4月26日 下午2:09:36
//	 * @param int locationIndex 位置
//	 * @param Long userInfoId 用户信息
//	 * @return CommandGroupUserPlayerPO 布局信息
//	 */
//	@Query("from com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO player where player.locationIndex=?1 and player.userInfo.id=?2")
//	public CommandGroupUserPlayerPO findByLocationIndexAndUserInfoId(int locationIndex, Long userInfoId);
	
}
