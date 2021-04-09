package com.sumavision.tetris.bvc.model.terminal.audio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TerminalAudioOutputQuery {

	@Autowired
	private TerminalAudioOutputDAO terminalAudioOutputDao;
	
	/**
	 * 根据终端查询音频输出<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月15日 上午10:52:50
	 * @param Long terminalId 终端id
	 * @return List<TerminalAudioOutputVO> 音频输出列表
	 */
	public List<TerminalAudioOutputVO> load(Long terminalId) throws Exception{
		List<TerminalAudioOutputPO> entities = terminalAudioOutputDao.findByTerminalId(terminalId);
		return TerminalAudioOutputVO.getConverter(TerminalAudioOutputVO.class).convert(entities, TerminalAudioOutputVO.class);
	}
	
}
