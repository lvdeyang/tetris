package com.sumavision.tetris.cs.program;

import java.util.ArrayList;
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
	
	/**
	 * 编辑分屏<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param programVO 分屏信息
	 * @return ProgramVO 编辑后的分屏信息
	 */
	public ProgramVO setProgram(ProgramVO programVO) throws Exception {

		ProgramPO programPO = this.setProgramScreenNum(programVO.getScheduleId(), programVO.getScreenNum(), programVO.getScreenId());
		
		ProgramVO returnProgramVO = new ProgramVO().set(programPO);
		
		returnProgramVO.setScreenInfo(this.setScreenInfo(programPO.getId(), programVO.getScreenInfo()));

		return returnProgramVO;
	}
	
	public ProgramVO setProgram(Long scheduleId, TemplateVO templateVO) throws Exception {
		ProgramPO programPO = setProgramScreenNum(scheduleId, templateVO.getScreenNum(), templateVO.getId());
		ProgramVO returnProgramVO = new ProgramVO().set(programPO);
		
		List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
		List<TemplateScreenVO> templateScreenVOs = templateVO.getScreen();
		for (TemplateScreenVO templateScreenVO : templateScreenVOs) {
			if (templateScreenVO.getData() != null) screenVOs.addAll(templateScreenVO.getData());
		}
		
		returnProgramVO.setScreenInfo(this.setScreenInfo(programPO.getId(), screenVOs));
		
		return returnProgramVO;
	}

	/**
	 * 修改屏幕数据库<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param scheduleId 排期id
	 * @param screenNum 分屏数
	 * @return ProgramPO 分屏信息
	 */
	private ProgramPO setProgramScreenNum(Long scheduleId, Long screenNum, Long screenId) {
		ProgramPO program = programDao.findByScheduleId(scheduleId);
		if (program != null) {
			program.setScreenNum(screenNum);
			program.setScreenId(screenId);
		}else {
			program = new ProgramPO();
			program.setScheduleId(scheduleId);
			program.setScreenNum(screenNum);
			program.setScreenId(screenId);
			program.setUpdateTime(new Date());
		}
		programDao.save(program);
		
		return program;
	}

	/**
	 * 修改屏幕媒资排单数据库<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param programId 屏幕id
	 * @param screenList 排单列表
	 * @return List<ScreenVO> 媒资排单列表
	 */
	private List<ScreenVO> setScreenInfo(Long programId, List<ScreenVO> screenList) throws Exception {
		return screenService.setScreenInfo(programId, screenList);
	}
	
	/**
	 * 删除分屏信息(附带删除排单列表)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param scheduleId 排期id
	 */
	public void removeProgramByScheduleId(Long scheduleId) {
		ProgramPO program = programDao.findByScheduleId(scheduleId);
		if (program != null) {
			programDao.delete(program);
			screenService.removeScreen(program.getId());
		}
	}
}
