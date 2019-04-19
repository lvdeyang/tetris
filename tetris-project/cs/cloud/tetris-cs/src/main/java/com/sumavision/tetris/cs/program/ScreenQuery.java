package com.sumavision.tetris.cs.program;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScreenQuery {
	@Autowired
	private ScreenDAO screenDao;

	public List<ScreenVO> getScreenInfo(Long programId) throws Exception {
		List<ScreenPO> screenPOList = screenDao.findByProgramId(programId);
		List<ScreenVO> screenVOList = new ArrayList<ScreenVO>();
		if (screenPOList != null && screenPOList.size() > 0) {
			for (int i = 0; i < screenPOList.size(); i++) {
				screenVOList.add(new ScreenVO().set(screenPOList.get(i)));
			}
		}
		return screenVOList;
	}
}
