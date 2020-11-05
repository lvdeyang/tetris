package com.sumavision.tetris.bvc.model.terminal;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalBundleChannelPO.class, idClass = Long.class)
public interface TerminalBundleChannelDAO extends BaseDAO<TerminalBundleChannelPO>{

	/**
	 * 查询终端设备下的通道列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午4:20:29
	 * @param Long terminalBundleId 终端设备id
	 * @return List<TerminalBundleChannelPO> 通道列表
	 */
	public List<TerminalBundleChannelPO> findByTerminalBundleIdOrderByChannelIdAsc(Long terminalBundleId);
	
	/**
	 * 根据终端设备id和通道id查询通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午2:28:14
	 * @param Long terminalBundleId 终端设备id
	 * @param String channelId 通道id
	 * @return List<TerminalBundleChannelPO> 通道列表
	 */
	public List<TerminalBundleChannelPO> findByTerminalBundleIdAndChannelId(Long terminalBundleId, String channelId);
	
	/**
	 * 根据类型查询终端设备通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 上午9:01:29
	 * @param Long terminalBundleId 终端设备id
	 * @param TerminalBundleChannelType type 通道类型
	 * @return List<TerminalBundleChannelPO> 通道列表
	 */
	public List<TerminalBundleChannelPO> findByTerminalBundleIdAndType(Long terminalBundleId, TerminalBundleChannelType type);
	
}
