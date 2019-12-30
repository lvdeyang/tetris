package com.sumavision.tetris.cs.program;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

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
	
	/**
	 * 获取模板时获取已设定分屏信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月12日 上午11:39:45
	 * @param Long scheduleId 
	 * @param templates
	 */
	public void getScreen2Template(Long scheduleId, JSONArray templates) throws Exception {
		ProgramVO programVO = getProgram(scheduleId);
		if (programVO == null) return;
		List<TemplateVO> templateVOs = JSON.parseArray(templates.toJSONString(), TemplateVO.class);
		for (TemplateVO template : templateVOs) {
			if (template.getId() == programVO.getScreenId() && template.getScreenNum() == programVO.getScreenNum()) {
				for (TemplateScreenVO templateScreenVO : template.getScreen()) {
					for (ScreenVO screenVO : programVO.getScreenInfo()) {
						if (screenVO.getSerialNum() == templateScreenVO.getNo()) {
							if (templateScreenVO.getData() == null) template.setScreen(new ArrayList<TemplateScreenVO>());
							templateScreenVO.getData().add(screenVO);
						}
					}
				}
				break;
			}
		}
	}
}
