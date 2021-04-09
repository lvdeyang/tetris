package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;
@RepositoryDefinition(domainClass = TerminalChannelBundleChannelPermissionPO.class, idClass = Long.class)
public interface TerminalChannelBundleChannelPermissionDAO extends BaseDAO<TerminalChannelBundleChannelPermissionPO>{

	/**
	 * 根据终端通道查询通道关联（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午1:09:36
	 * @param Collection<Long> terminalChannelId 终端通道id列表
	 * @return List<TerminalChannelBundleChannelPermissionPO> 通道关联列表
	 */
	public List<TerminalChannelBundleChannelPermissionPO> findByTerminalChannelIdIn(Collection<Long> terminalChannelId);
	
	/**
	 * 根据终端通道查询通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午1:09:36
	 * @param Long terminalChannelId 终端通道id
	 * @return List<TerminalChannelBundleChannelPermissionPO> 通道关联列表
	 */
	public List<TerminalChannelBundleChannelPermissionPO> findByTerminalChannelId(Long terminalChannelId);
	
	/*@Query(value = "select new com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionDTO("+
	            "permission.channelParamsType, "+
	            "bundle.name, "+
	            "bundle.type, "+
	            "channel.channelId, "+
	            "channel.type, "+
	            "bundle.bundleType, "+
	            "permission.terminalChannelId) "+
			"from com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionPO permission, "+
	            "com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO channel, "+
			"com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO bundle "+
	            "where permission.terminalChannelId in ?1 and permission.terminalBundleId = bundle.id and permission.terminalBundleChannelId = channel.id")
	public List<TerminalChannelBundleChannelPermissionDTO> findByPermission(Long terminalChannelId);*/
	
	/*@Query(value = "select new com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionDTO("+
            "permission.channelParamsType, "+
            "bundle.name, "+
            "bundle.type, "+
            "channel.channelId, "+
            "channel.type, "+
            "bundle.bundleType, "+
            "permission.terminalChannelId) "+
		"from com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionPO permission, "+
            "com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO channel, "+
		"com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO bundle "+
            "where permission.terminalChannelId in ?1 and permission.terminalBundleId = bundle.id and permission.terminalBundleChannelId = channel.id")
	public List<TerminalChannelBundleChannelPermissionDTO> findByPermission(Collection<Long> terminalChannelIds);*/

	/**
	 * 通过终端id、终端设备id、终端设备通道id查找<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 下午4:10:50
	 * @return TerminalChannelBundleChannelPermissionPO
	 */
	public TerminalChannelBundleChannelPermissionPO findByTerminalIdAndTerminalBundleIdAndTerminalBundleChannelId(Long terminalId, Long terminalBundleId, Long terminalBundleChannelId);
	
	/**
	 * 通过终端id查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 下午7:38:20
	 * @param terminalId 终端id 
	 * @return List<TerminalChannelBundleChannelPermissionPO>集合
	 */
	public List<TerminalChannelBundleChannelPermissionPO> findByTerminalId(Long terminalId);

}
