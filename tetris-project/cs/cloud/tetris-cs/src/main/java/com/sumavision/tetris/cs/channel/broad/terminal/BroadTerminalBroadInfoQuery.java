package com.sumavision.tetris.cs.channel.broad.terminal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BroadTerminalBroadInfoQuery {
	@Autowired
	private BroadTerminalBroadInfoDAO broadTerminalBroadInfoDAO;
	
	public BroadTerminalBroadInfoPO findByChannelId(Long channelId) throws Exception {
		BroadTerminalBroadInfoPO broadInfoPO = broadTerminalBroadInfoDAO.findByChannelId(channelId);
		return broadInfoPO;
	}
}
