package com.sumavision.tetris.cs.channel.broad.terminal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.schedule.ScheduleService;

@Service
@Transactional(rollbackFor = Exception.class)
public class BroadTerminalBroadInfoService {
	@Autowired
	private BroadTerminalBroadInfoQuery broadTerminalBroadInfoQuery;
	
	@Autowired
	private BroadTerminalBroadInfoDAO broadTerminalBroadInfoDAO;
	
	@Autowired
	private ScheduleService scheduleService;
	
	public BroadTerminalBroadInfoVO saveInfo(Long channelId, String level, Boolean hasFile) throws Exception {
		if (channelId == null) return null;
		if (level == null) level = BroadTerminalLevelType.NORMAL.toString();
		BroadTerminalBroadInfoPO broadTerminalBroadInfoPO = broadTerminalBroadInfoQuery.findByChannelId(channelId);
		if (broadTerminalBroadInfoPO == null) broadTerminalBroadInfoPO = new BroadTerminalBroadInfoPO();
		Boolean removeSchedule = false;
		if (broadTerminalBroadInfoPO.getHasFile() != null && broadTerminalBroadInfoPO.getHasFile() && hasFile == false) removeSchedule = true;
		
		broadTerminalBroadInfoPO.setChannelId(channelId);
		broadTerminalBroadInfoPO.setLevel(BroadTerminalLevelType.fromName(level).getName());
		broadTerminalBroadInfoPO.setHasFile(hasFile);
		
		broadTerminalBroadInfoDAO.save(broadTerminalBroadInfoPO);
		
		if (removeSchedule) scheduleService.removeByChannelId(channelId);
		
		return new BroadTerminalBroadInfoVO().set(broadTerminalBroadInfoPO);
	}
}
