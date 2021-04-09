package com.sumavision.tetris.bvc.model.terminal.audio;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalAudioOutputPO.class, idClass = Long.class)
public interface TerminalAudioOutputDAO extends BaseDAO<TerminalAudioOutputPO>{

	/**
	 * 查询终端内没有配置到屏幕上的音频输出列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 上午11:53:46
	 * @param Long terminalId 终端id
	 * @return List<TerminalAudioOutputPO> 音频输出列表
	 */
	@Query(value = "SELECT * FROM TETRIS_BVC_MODEL_TERMINAL_AUDIO_OUTPUT WHERE TERMINAL_ID=?1 AND ID NOT IN("
			+ "SELECT IFNULL(TERMINAL_AUDIO_OUTPUT_ID,-1) FROM TETRIS_BVC_MODEL_TERMINAL_PHYSICAL_SCREEN WHERE TERMINAL_ID=?1)", nativeQuery = true)
	public List<TerminalAudioOutputPO> findFreeAudioOutputByTerminalId(Long terminalId);
	
	/**
	 * 查询终端下的音频输出列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 上午10:53:33
	 * @param Long terminalId 终端id
	 * @return List<TerminalAudioOutputPO> 音频输出列表
	 */
	public List<TerminalAudioOutputPO> findByTerminalId(Long terminalId);
	
}
