package com.sumavision.tetris.cs.schedule;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class ScheduleQuery {
	@Autowired
	private ScheduleDAO scheduleDAO;
	
	public Map<String, Object> getByChannelId(Long channelId, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<SchedulePO> schedulePages = scheduleDAO.findByChannelId(channelId, page);
		List<SchedulePO> schedules = schedulePages.getContent();
		return new HashMapWrapper<String,Object>().put("data", ScheduleVO.getConverter(ScheduleVO.class).convert(schedules, ScheduleVO.class))
				.put("total", schedulePages.getTotalElements())
				.getMap();
	}
}
