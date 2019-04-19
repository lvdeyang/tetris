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
		List<ProgramPO> programList = programDao.findAll();
		ProgramPO programPO = new ProgramPO();
		Boolean set = false;
		if (programList != null && programList.size() > 0) {
			for (int i = 0; i < programList.size(); i++) {
				if (programList.get(i).getChannelId() == channelId) {
					programPO = programList.get(i);
					programPO.setScreenNum(screenNum);
					set = true;
					break;
				}
			}
		}
		if (!set) {
			programPO = new ProgramPO();
			programPO.setChannelId(channelId);
			programPO.setScreenNum(screenNum);
			programPO.setUpdateTime(new Date());
		}
		programDao.save(programPO);
		return programPO;
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
