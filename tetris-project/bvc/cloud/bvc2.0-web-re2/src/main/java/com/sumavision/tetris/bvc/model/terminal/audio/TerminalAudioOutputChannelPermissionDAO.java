package com.sumavision.tetris.bvc.model.terminal.audio;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass=TerminalAudioOutputChannelPermissionPO.class, idClass=Long.class)
public interface TerminalAudioOutputChannelPermissionDAO extends MetBaseDAO<TerminalAudioOutputChannelPermissionPO> {
	
	public List<TerminalAudioOutputChannelPermissionPO> findByTerminalAudioChannelIdInAndTerminalAudioOutputIdIn(Collection<Long>terminalAudioChannelId, Collection<Long> terminalAudioOutputId);

	/**
	 * 根据音频输出查询通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午4:46:17
	 * @param Long terminalAudioOutputId 音频输出id
	 * @return List<TerminalAudioOutputChannelPermissionPO> 通道关联列表
	 */
	public List<TerminalAudioOutputChannelPermissionPO> findByTerminalAudioOutputId(Long terminalAudioOutputId);
	
	/**
	 * 根据音频输出查询通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午4:46:17
	 * @param Collection<Long> terminalAudioOutputIds 音频输出id列表
	 * @return List<TerminalAudioOutputChannelPermissionPO> 通道关联列表
	 */
	public List<TerminalAudioOutputChannelPermissionPO> findByTerminalAudioOutputIdIn(Collection<Long> terminalAudioOutputIds);
	
	/**
	 * 根据音频解码通道查询音频输出通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 上午11:24:28
	 * @param Long terminalAudioChannelId 终端音频解码通道id
	 * @return List<TerminalAudioOutputChannelPermissionPO> 音频输出通道关联
	 */
	public List<TerminalAudioOutputChannelPermissionPO> findByTerminalAudioChannelId(Long terminalAudioChannelId);
	
}
