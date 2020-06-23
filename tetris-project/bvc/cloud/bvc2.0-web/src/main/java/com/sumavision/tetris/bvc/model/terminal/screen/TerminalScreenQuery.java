package com.sumavision.tetris.bvc.model.terminal.screen;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;

@Component
public class TerminalScreenQuery {

	@Autowired
	private TerminalScreenDAO terminalScreenDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	/**
	 * 查询终端下的屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月22日 下午4:46:06
	 * @param Long terminalId 终端id
	 * @return List<TerminalScreenVO> 屏幕列表
	 */
	public List<TerminalScreenVO> load(Long terminalId) throws Exception{
		List<TerminalScreenPO> entities = terminalScreenDao.findByTerminalId(terminalId);
		if(entities!=null && entities.size()>0){
			List<Long> terminalChannelIds = new ArrayList<Long>();
			for(TerminalScreenPO entity:entities){
				terminalChannelIds.add(entity.getTerminalChannelId());
			}
			List<TerminalChannelPO> channels = terminalChannelDao.findAll(terminalChannelIds);
			List<TerminalScreenVO> screens = new ArrayList<TerminalScreenVO>();
			for(TerminalScreenPO entity:entities){
				TerminalChannelPO targetChannel = null;
				for(TerminalChannelPO channel:channels){
					if(channel.getId().equals(entity.getTerminalChannelId())){
						targetChannel = channel;
						break;
					}
				}
				screens.add(new TerminalScreenVO().set(entity).setTerminalChannelName(targetChannel.getName()));
			}
			return screens;
		}
		return null;
	}
	
}
