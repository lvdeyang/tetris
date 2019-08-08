package com.sumavision.tetris.cs.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.program.ProgramService;
import com.sumavision.tetris.cs.schedule.exception.ScheduleNotExistsException;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleService {
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ScheduleDAO scheduleDAO;
	
	@Autowired
	private ProgramService programService;
	
	/**
	 * 添加排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param channelId 频道id
	 * @param broadDate 播发日期
	 * @param remark 备注
	 * @return ScheduleVO 排期信息
	 */
	public ScheduleVO add(Long channelId, String broadDate, String remark) throws Exception{
		SchedulePO schedulePO = new SchedulePO();
		schedulePO.setChannelId(channelId);
		schedulePO.setUpdateTime(new Date());
		schedulePO.setBroadDate(broadDate);
		schedulePO.setRemark(remark);
		scheduleDAO.save(schedulePO);
		
		return new ScheduleVO().set(schedulePO);
	}
	
	/**
	 * 删除排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param scheduleId 排期id
	 */
	public ScheduleVO remove(Long scheduleId) throws Exception{
		SchedulePO schedule = scheduleDAO.findOne(scheduleId);
		
		if (schedule == null) throw new ScheduleNotExistsException(scheduleId);
		
		scheduleDAO.delete(schedule);
		
		return new ScheduleVO().set(schedule);
	}
	
	/**
	 * 编辑排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param scheduleId 排期id
	 * @param broadDate 播发日期
	 * @param remark 备注
	 * @return ScheduleVO 排期信息
	 */
	public ScheduleVO edit(Long scheduleId, String broadDate, String remark) throws Exception{
		SchedulePO schedule = scheduleDAO.findOne(scheduleId);
		
		if (schedule == null) throw new ScheduleNotExistsException(scheduleId);
		
		schedule.setBroadDate(broadDate);
		schedule.setRemark(remark);
		
		return new ScheduleVO().set(schedule);
	}
	
	/**
	 * 根据频道id删除排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param channelId 频道id
	 */
	public void removeByChannelId(Long channelId) throws Exception {
		List<SchedulePO> schedulePOs = scheduleDAO.findByChannelId(channelId);
		
		if (schedulePOs == null || schedulePOs.isEmpty()) return;
		
		for (SchedulePO schedulePO : schedulePOs) {
			programService.removeProgramByScheduleId(schedulePO.getId());
		}
		
		scheduleDAO.deleteInBatch(schedulePOs);
	}
}
