package com.sumavision.tetris.bvc.model.terminal.screen;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TerminalScreenPrimaryKeyQuery {

	@Autowired
	private TerminalScreenPrimaryKeyDAO terminalScreenPrimaryKeyDao;
	
	/**
	 * 查询屏幕主键<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:06:40
	 * @return List<TerminalScreenPrimaryKeyVO> 屏幕主键
	 */
	public List<TerminalScreenPrimaryKeyVO> load() throws Exception{
		List<TerminalScreenPrimaryKeyPO> entities = terminalScreenPrimaryKeyDao.findAll();
		return TerminalScreenPrimaryKeyVO.getConverter(TerminalScreenPrimaryKeyVO.class).convert(entities, TerminalScreenPrimaryKeyVO.class);
	}
	
}
