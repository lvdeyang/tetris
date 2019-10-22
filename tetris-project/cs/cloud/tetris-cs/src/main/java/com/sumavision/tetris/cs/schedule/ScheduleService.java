package com.sumavision.tetris.cs.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.program.ProgramService;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.api.ApiServerScheduleVO;
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
		
		programService.removeProgramByScheduleId(schedule.getId());
		
		scheduleDAO.delete(schedule);
		
		return new ScheduleVO().set(schedule);
	}
	
	public List<ScheduleVO> removeInBatch(List<Long> scheduleIds) throws Exception{
		if (scheduleIds == null || scheduleIds.isEmpty()) return null;
		
		List<SchedulePO> schedulePOs = scheduleDAO.findAll(scheduleIds);
		
		if (schedulePOs == null || schedulePOs.isEmpty()) return null;
		
		for (SchedulePO schedulePO : schedulePOs) {
			programService.removeProgramByScheduleId(schedulePO.getId());
		}
		
		scheduleDAO.deleteInBatch(schedulePOs);
		
		return ScheduleVO.getConverter(ScheduleVO.class).convert(schedulePOs, ScheduleVO.class);
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
	
	/**
	 * 根据频道id添加排期(自动生成排期节目)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午3:45:26
	 * @param channelId 频道id
	 * @param broadDate 排期时间
	 * @param screens 排期节目
	 * @return
	 */
	public ScheduleVO addSchedule(Long channelId, String broadDate, List<ScreenVO> screens) throws Exception {
		if (screens == null || screens.isEmpty()) return null;
		
		ScheduleVO schedule = add(channelId, broadDate, "");
			
		Date date = new Date();
		List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
		for (int i = 0; i < screens.size(); i++) {
			ScreenVO screen = new ScreenVO();
			ScreenVO item = screens.get(i);
			screen.setUpdateTime(date);
			screen.setSerialNum(1l);
			screen.setIndex((long)(i+1));
			screen.setName(item.getName());
			screen.setPreviewUrl(item.getPreviewUrl());
			screen.setHotWeight(item.getHotWeight());
			screenVOs.add(screen);
		}
		ProgramVO program = new ProgramVO();
		program.setScheduleId(schedule.getId());
		program.setScreenNum(1l);
		program.setUpdateTime(date);
		program.setScreenInfo(screenVOs);
			
		schedule.setProgram(programService.setProgram(program));
			
		return schedule;
	}
	
	/**
	 * 根据频道id批量添加排期<br/>
	 * <b>作者:</b>sms<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月18日 上午11:01:05
	 * @param Long channelId 频道id
	 * @param List<ApiServerScheduleVO> scheduleList 排期单
	 */
	public List<ScheduleVO> addSchedules(Long channelId, List<ApiServerScheduleVO> scheduleList) throws Exception {
		if (scheduleList == null || scheduleList.isEmpty()) return null;
		
		List<ScheduleVO> scheduleVOs = new ArrayList<ScheduleVO>();
		
		for (ApiServerScheduleVO apiServerScheduleVO : scheduleList) {
			ScheduleVO schedule = add(channelId, apiServerScheduleVO.getBroadDate(), "");
			
			Date date = new Date();
			List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
			List<String> assetPaths = apiServerScheduleVO.getAssetPaths();
			for (int i = 0; i < assetPaths.size(); i++) {
				ScreenVO screen = new ScreenVO();
				screen.setUpdateTime(date);
				screen.setSerialNum(1l);
				screen.setIndex((long)(i+1));
				screen.setPreviewUrl(assetPaths.get(i));
				screenVOs.add(screen);
			}
			ProgramVO program = new ProgramVO();
			program.setScheduleId(schedule.getId());
			program.setScreenNum(1l);
			program.setUpdateTime(date);
			program.setScreenInfo(screenVOs);
			
			schedule.setProgram(programService.setProgram(program));
			
			scheduleVOs.add(schedule);
		}
		
		return scheduleVOs;
	}
}
