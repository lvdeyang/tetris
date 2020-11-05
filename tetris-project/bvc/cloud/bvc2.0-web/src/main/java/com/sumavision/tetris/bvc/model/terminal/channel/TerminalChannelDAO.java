package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalChannelPO.class, idClass = Long.class)
public interface TerminalChannelDAO extends BaseDAO<TerminalChannelPO>{

	/**
	 * 查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午3:05:08
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelPO> 通道列表
	 */
	public List<TerminalChannelPO> findByTerminalIdOrderByTypeAscNameAsc(Long terminalId);
	
	/**
	 * 根据类型查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午3:05:08
	 * @param Long terminalId 终端id
	 * @param TerminalChannelType type 通道类型
	 * @return List<TerminalChannelPO> 通道列表
	 */
	public List<TerminalChannelPO> findByTerminalIdAndTypeOrderByTypeAscNameAsc(Long terminalId, TerminalChannelType type);
	
	/**
	 * 根据角色通道id查询终端通道<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月23日 上午10:41:49
	 * @param roleChannelIds
	 * @return
	 */
	@Query(value = "select * from TETRIS_BVC_MODEL_TERMINAL_CHANNEL channel left join TETRIS_BVC_MODEL_ROLE_CHANNEL_TERMINAL_BUNDLE_CHANNEL m on channel.id = m.terminal_channel_id where m.role_channel_id in ?1", nativeQuery = true)
	public List<TerminalChannelPO> findByRoleChannelIdIn(List<Long> roleChannelIds);
	
	/**
	 * 根据terminalId和screenPrimaryKey查询终端通道<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月8日 下午1:04:24
	 * @param terminalId
	 * @param screenPrimaryKey
	 * @return
	 */
	@Query(value = "select * from TETRIS_BVC_MODEL_TERMINAL_CHANNEL channel left join TETRIS_BVC_MODEL_TERMINAL_SCREEN s on channel.id = s.terminal_channel_id where s.terminal_id = ?1 and s.screen_primary_key = ?2", nativeQuery = true)	
	public List<TerminalChannelPO> findByTerminalIdAndScreenPrimaryKey(Long terminalId, String screenPrimaryKey);
	
}
