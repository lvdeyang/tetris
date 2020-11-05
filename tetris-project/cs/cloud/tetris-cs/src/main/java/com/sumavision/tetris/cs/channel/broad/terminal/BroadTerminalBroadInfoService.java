package com.sumavision.tetris.cs.channel.broad.terminal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.channel.SetTerminalBroadBO;
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
	
	/**
	 * 设置频道的终端播发相关信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 上午9:44:08
	 * @param Long channelId 频道id
	 * @param SetTerminalBroadBO terminalBroadBO 相关信息数据类型
	 * @return BroadTerminalBroadInfoVO
	 */
	public BroadTerminalBroadInfoVO saveInfo(Long channelId, SetTerminalBroadBO terminalBroadBO) throws Exception {
		if (channelId == null || terminalBroadBO == null) return null;
		String level = terminalBroadBO.getLevel();
		Boolean hasFile = terminalBroadBO.getHasFile();
		if (level == null) level = BroadTerminalLevelType.NORMAL.toString();
		BroadTerminalBroadInfoPO broadTerminalBroadInfoPO = broadTerminalBroadInfoQuery.findByChannelId(channelId);
		if (broadTerminalBroadInfoPO == null) broadTerminalBroadInfoPO = new BroadTerminalBroadInfoPO();
		Boolean removeSchedule = false;
		if (broadTerminalBroadInfoPO.getHasFile() != null && broadTerminalBroadInfoPO.getHasFile() != hasFile) removeSchedule = true;
		
		broadTerminalBroadInfoPO.setChannelId(channelId);
		broadTerminalBroadInfoPO.setLevel(BroadTerminalLevelType.fromName(level).getName());
		broadTerminalBroadInfoPO.setHasFile(hasFile);
		
		broadTerminalBroadInfoDAO.save(broadTerminalBroadInfoPO);
		
		if (removeSchedule) scheduleService.removeByChannelId(channelId);
		
		return new BroadTerminalBroadInfoVO().set(broadTerminalBroadInfoPO);
	}
	
	/**
	 * 设置排期单整体结束时间<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 上午9:43:47
	 * @param Long channelId 频道id
	 * @param String endDate 停止时间
	 * @return BroadTerminalBroadInfoVO
	 */
	public BroadTerminalBroadInfoVO setEndDate(Long channelId, String endDate) throws Exception {
		if (channelId == null) return null;
		BroadTerminalBroadInfoPO broadTerminalBroadInfoPO = broadTerminalBroadInfoQuery.findByChannelId(channelId);
		if (broadTerminalBroadInfoPO == null) broadTerminalBroadInfoPO = new BroadTerminalBroadInfoPO();
		broadTerminalBroadInfoPO.setEndDate(endDate);
		broadTerminalBroadInfoDAO.save(broadTerminalBroadInfoPO);
		return new BroadTerminalBroadInfoVO().set(broadTerminalBroadInfoPO);
	}
}
