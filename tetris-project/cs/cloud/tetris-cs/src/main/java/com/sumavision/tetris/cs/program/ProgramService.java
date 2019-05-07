package com.sumavision.tetris.cs.program;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProgramService {
	@Autowired
	private ProgramQuery programQuery;

	@Autowired
	private ProgramDAO programDao;
	
	@Autowired
	private ScreenService screenService;
	
	@Autowired
	private ScreenQuery screenQuery;
	
	public ProgramVO setProgram(ProgramVO programVO) throws Exception {

		ProgramPO programPO = this.setProgramScreenNum(programVO.getChannelId(), programVO.getScreenNum());
		
		ProgramVO returnProgramVO = new ProgramVO().set(programPO);
		
		returnProgramVO.setScreenInfo(this.setScreenInfo(programPO.getId(), programVO.getScreenInfo()));

		return returnProgramVO;
	}

	private ProgramPO setProgramScreenNum(Long channelId, Long screenNum) {
		ProgramPO program = programDao.findByChannelId(channelId);
		if (program != null) {
			program.setScreenNum(screenNum);
		}else {
			program = new ProgramPO();
			program.setChannelId(channelId);
			program.setScreenNum(screenNum);
			program.setUpdateTime(new Date());
		}
		programDao.save(program);
		
		return program;
	}

	private List<ScreenVO> setScreenInfo(Long programId, List<ScreenVO> screenList) throws Exception {
		return screenService.setScreenInfo(programId, screenList);
	}
	
	public void removeProgramByChannelId(Long channelId) {
		ProgramPO program = programDao.findByChannelId(channelId);
		if (program != null) {
			programDao.delete(program);
			screenService.removeScreen(program.getId());
		}
	}
}
