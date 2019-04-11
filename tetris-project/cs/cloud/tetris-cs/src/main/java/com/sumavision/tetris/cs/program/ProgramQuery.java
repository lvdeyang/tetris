package com.sumavision.tetris.cs.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProgramQuery {
	@Autowired
	private ProgramDAO programDao;
	
	@Autowired
	private ScreenQuery screenQuery;

	public ProgramVO getProgram(Long channelId) throws Exception {

		ProgramVO program = new ProgramVO();
		ProgramPO programPO = programDao.findByChannelId(channelId);

		if(programPO == null)
			return null;
		
		program.set(programPO);
		program.setScreenInfo(screenQuery.getScreenInfo(programPO.getId()));
		return program;
	}
}
