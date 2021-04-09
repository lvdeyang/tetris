package com.sumavision.tetris.bvc.model.terminal.physical.screen;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalPhysicalScreenChannelPermissionPO.class, idClass = Long.class)
public interface TerminalPhysicalScreenChannelPermissionDAO extends BaseDAO<TerminalPhysicalScreenChannelPermissionPO>{

	public List<TerminalPhysicalScreenChannelPermissionPO> findByTerminalChannelIdIn(Collection<Long> TerminalChannelId);
	
	/**
	 * 根据终端物理屏幕id查询通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午4:34:16
	 * @param Collection<Long> terminalPhysicalScreenId 物理屏幕id列表
	 * @return List<TerminalPhysicalScreenChannelPermissionPO> 通道关联数据列表
	 */
	public List<TerminalPhysicalScreenChannelPermissionPO> findByTerminalPhysicalScreenIdIn(Collection<Long> terminalPhysicalScreenIds);
	
	/**
	 * 统计终端物理屏幕关联的解码通道数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 上午11:21:09
	 * @param Long terminalPhysicalScreenId 终端物理屏幕id
	 * @return long 通道数量
	 */
	public long countByTerminalPhysicalScreenId(Long terminalPhysicalScreenId);
	
	/**
	 * 根据终端物理屏幕id查询通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午4:34:16
	 * @param Long terminalPhysicalScreenId 物理屏幕id
	 * @return List<TerminalPhysicalScreenChannelPermissionPO> 通道关联数据列表
	 */
	public List<TerminalPhysicalScreenChannelPermissionPO> findByTerminalPhysicalScreenId(Long terminalPhysicalScreenId);
	 
	/**
	 * 根据视频通道查询物理屏幕视频通道关联信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午4:10:19
	 * @param Long terminalChannelId 终端通道id
	 * @return List<TerminalPhysicalScreenChannelPermissionPO> 关联信息
	 */
	public List<TerminalPhysicalScreenChannelPermissionPO> findByTerminalChannelId(Long terminalChannelId);
	
}
