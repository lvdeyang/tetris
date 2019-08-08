package com.sumavision.tetris.cs.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProgramQuery {
	@Autowired
	private ProgramDAO programDao;
	
	@Autowired
	private ScreenQuery screenQuery;

	/**
	 * 根据排期id获取排期分屏信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param scheduleId 排期id
	 * @return ProgramVO 分屏信息
	 */
	public ProgramVO getProgram(Long scheduleId) throws Exception {

		ProgramVO program = new ProgramVO();
		ProgramPO programPO = programDao.findByScheduleId(scheduleId);

		if(programPO == null)
			return null;
		
		program.set(programPO);
		program.setScreenInfo(screenQuery.getScreenInfo(programPO.getId()));
		return program;
	}
}
