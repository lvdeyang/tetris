package com.sumavision.tetris.cs.program;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScreenQuery {
	@Autowired
	private ScreenDAO screenDao;

	/**
	 * 根据分屏id获取媒资排单信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param programId 分屏id
	 * @return List<ScreenVO> 排单表
	 */
	public List<ScreenVO> getScreenInfo(Long programId) throws Exception {
		List<ScreenPO> screenPOList = screenDao.findByProgramId(programId);
		return ScreenVO.getConverter(ScreenVO.class).convert(screenPOList, ScreenVO.class);
	}
}
