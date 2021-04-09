package com.sumavision.tetris.bvc.model.terminal.audio;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = TerminalEncodeAudioVideoChannelPermissionPO.class, idClass = Long.class)
public interface TerminalEncodeAudioVideoChannelPermissionDAO extends MetBaseDAO<TerminalEncodeAudioVideoChannelPermissionPO> {
	
	public List<TerminalEncodeAudioVideoChannelPermissionPO> findByTerminalId(Long terminalId);
	
	/**
	 * 根据终端视频通道id查询通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午1:36:16
	 * @param Long terminalVideoChannelId 终端视频通道id
	 * @return List<TerminalEncodeAudioVideoChannelPermissionPO> 通道关联列表
	 */
	public List<TerminalEncodeAudioVideoChannelPermissionPO> findByTerminalVideoChannelId(Long terminalVideoChannelId);
	
	/**
	 * 根据终端音频通道id查询通道关联<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午7:27:15
	 * @param Long terminalAudioChannelId 终端音频通道id
	 * @return List<TerminalEncodeAudioVideoChannelPermissionPO> 通道关联
	 */
	public List<TerminalEncodeAudioVideoChannelPermissionPO> findByTerminalAudioChannelId(Long terminalAudioChannelId);

}
