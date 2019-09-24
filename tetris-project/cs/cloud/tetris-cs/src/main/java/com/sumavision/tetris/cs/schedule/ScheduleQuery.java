package com.sumavision.tetris.cs.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.cs.program.ProgramQuery;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.api.ApiServerScheduleVO;

@Component
public class ScheduleQuery {
	@Autowired
	private ScheduleDAO scheduleDAO;
	
	@Autowired
	private ProgramQuery programQuery;
	
	/**
	 * 获取排期列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param Long channelId 频道id
	 * @param int currentPage 分页当前页
	 * @param int pageSize 分页大小
	 * @return total 列表总数
	 * @return List<ScheduleVO> 排期信息
	 */
	public Map<String, Object> getByChannelId(Long channelId, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<SchedulePO> schedulePages = scheduleDAO.findByChannelId(channelId, page);
		List<SchedulePO> schedules = schedulePages.getContent();
		List<ScheduleVO> scheduleVOs = ScheduleVO.getConverter(ScheduleVO.class).convert(schedules, ScheduleVO.class);
		for (ScheduleVO scheduleVO : scheduleVOs) {
			scheduleVO.setProgram(programQuery.getProgram(scheduleVO.getId()));
		}
		return new HashMapWrapper<String,Object>().put("data", scheduleVOs)
				.put("total", schedulePages.getTotalElements())
				.getMap();
	}
	
	/**
	 * 获取排期列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return List<ScheduleVO> 排期信息
	 */
	public List<ScheduleVO> getByChannelId(Long channelId) throws Exception {
		List<SchedulePO> schedulePOs = scheduleDAO.findByChannelId(channelId);
		
		return ScheduleVO.getConverter(ScheduleVO.class).convert(schedulePOs, ScheduleVO.class);
	}
}
