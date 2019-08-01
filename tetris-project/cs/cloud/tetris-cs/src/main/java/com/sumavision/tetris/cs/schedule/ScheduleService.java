package com.sumavision.tetris.cs.schedule;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.schedule.exception.ScheduleNotExistsException;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleService {
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ScheduleDAO scheduleDAO;
	
	public ScheduleVO add(Long channelId, String broadDate, String remark) throws Exception{
		SchedulePO schedulePO = new SchedulePO();
		schedulePO.setChannelId(channelId);
		schedulePO.setUpdateTime(new Date());
		schedulePO.setBroadDate(broadDate);
		schedulePO.setRemark(remark);
		scheduleDAO.save(schedulePO);
		
		return new ScheduleVO().set(schedulePO);
	}
	
	public ScheduleVO remove(Long scheduleId) throws Exception{
		SchedulePO schedule = scheduleDAO.findOne(scheduleId);
		
		if (schedule == null) throw new ScheduleNotExistsException(scheduleId);
		
		scheduleDAO.delete(schedule);
		
		return new ScheduleVO().set(schedule);
	}
	
	public ScheduleVO edit(Long scheduleId, String broadDate, String remark) throws Exception{
		SchedulePO schedule = scheduleDAO.findOne(scheduleId);
		
		if (schedule == null) throw new ScheduleNotExistsException(scheduleId);
		
		schedule.setBroadDate(broadDate);
		schedule.setRemark(remark);
		
		return new ScheduleVO().set(schedule);
	}
}
