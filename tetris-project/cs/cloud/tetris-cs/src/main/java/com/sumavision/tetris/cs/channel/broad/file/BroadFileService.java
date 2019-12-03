package com.sumavision.tetris.cs.channel.broad.file;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cs.channel.ChannelBroadStatus;
import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalService;
import com.sumavision.tetris.cs.channel.exception.ChannelBroadNoneOutputException;
import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.cs.schedule.ScheduleVO;
import com.sumavision.tetris.cs.schedule.exception.ScheduleNoneToBroadException;
import com.sumavision.tetris.user.UserEquipType;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;

@Service
@Transactional(rollbackFor = Exception.class)
public class BroadFileService {
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private ChannelDAO channelDAO;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private BroadTerminalService broadTerminalService;
	
	@Autowired
	private BroadFileBroadInfoService broadFileBroadInfoService;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	/**
	 * 文件下载开始播发<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午3:07:26
	 * @param channelId 频道id
	 */
	public void startFileBroadcast(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		List<ScheduleVO> scheduleVOs = scheduleQuery.getByChannelId(channelId);
		if (scheduleVOs == null || scheduleVOs.isEmpty()) throw new ScheduleNoneToBroadException(channel.getName());
		
		List<BroadFileBroadInfoVO> broadInfoVOs = broadFileBroadInfoService.queryFromChannelId(channelId);
		if (broadInfoVOs == null || broadInfoVOs.isEmpty()) throw new ChannelBroadNoneOutputException();
		
		sendWebsocket(broadInfoVOs, channel);
		
		if (channel.getAutoBroad() == null || !channel.getAutoBroad()){
			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
		} else {
			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING);
		}
		channelDAO.save(channel);
	}
	
	public void stopFileBroadcast(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
		channelDAO.save(channel);
	}
	
	/**
	 * 给终端用户webSocket下节目单<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月3日 下午3:24:34
	 * @param broadInfoVOs 预播发信息列表
	 * @param channel 频道信息
	 */
	public void sendWebsocket(List<BroadFileBroadInfoVO> broadInfoVOs, ChannelPO channel) throws Exception {
		List<BroadFileBroadInfoVO> qtUsers = new ArrayList<BroadFileBroadInfoVO>();
		List<BroadFileBroadInfoVO> terminalUsers = new ArrayList<BroadFileBroadInfoVO>();
		for (BroadFileBroadInfoVO infoVO : broadInfoVOs) {
			if (infoVO.getUserEquipType().equals(UserEquipType.QT.toString())) {
				qtUsers.add(infoVO);
			} else if (infoVO.getUserEquipType().equals(UserEquipType.PUSH.toString())) {
				terminalUsers.add(infoVO);
			}
		}
		if (!qtUsers.isEmpty()) {
			List<Long> qtUserIds = qtUsers.stream().map(BroadFileBroadInfoVO::getUserId).collect(Collectors.toList());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("channelId", channel.getId());
			for (Long id : qtUserIds) {
				websocketMessageService.send(id, jsonObject.toJSONString(), WebsocketMessageType.COMMAND);
			}
		}
//		if (!terminalUsers.equals(UserEquipType.PUSH.toString())) {
//			List<Long> teminalUserIds = terminalUsers.stream().map(BroadFileBroadInfoVO::getUserId).collect(Collectors.toList());
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("channelId", channel.getId());
//			for (Long id : teminalUserIds) {
//				websocketMessageService.send(id, jsonObject.toJSONString(), WebsocketMessageType.COMMAND);
//			}
//		}
	}
	
	public void addScheduleDeal(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		
		if (channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)) {
			startFileBroadcast(channelId);
		}
	}
}
