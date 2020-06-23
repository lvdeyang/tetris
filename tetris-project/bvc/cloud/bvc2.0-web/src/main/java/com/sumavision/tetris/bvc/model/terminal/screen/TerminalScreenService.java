package com.sumavision.tetris.bvc.model.terminal.screen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalScreenService {

	@Autowired
	private TerminalScreenDAO terminalScreenDao;
	
	/**
	 * 删除屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午5:13:16
	 * @param Long id 屏幕id
	 */
	public void delete(Long id) throws Exception{
		TerminalScreenPO screen = terminalScreenDao.findOne(id);
		if(screen != null){
			terminalScreenDao.delete(screen);
		}
	}
	
}
