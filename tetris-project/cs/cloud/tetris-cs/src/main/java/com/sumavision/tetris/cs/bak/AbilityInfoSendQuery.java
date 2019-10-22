package com.sumavision.tetris.cs.bak;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.cs.channel.Adapter;

@Component
public class AbilityInfoSendQuery {
	@Autowired
	private AbilityInfoSendDAO abilityInfoSendDAO;
	
	@Autowired
	private Adapter adapter;
	
	public AbilityInfoSendPO getByChannelId(Long channelId) throws Exception{
		AbilityInfoSendPO info = abilityInfoSendDAO.findByChannelId(channelId);
		return info;
	}
	
	public Integer getBroadId(Long channelId) throws Exception{
		AbilityInfoSendPO abilityInfoSendPO = abilityInfoSendDAO.findByChannelId(channelId);
		if (abilityInfoSendPO != null) {
			return abilityInfoSendPO.getBroadId();
		} else {
			abilityInfoSendPO = new AbilityInfoSendPO();
			abilityInfoSendPO.setChannelId(channelId);
			abilityInfoSendPO.setUpdateTime(new Date());
			abilityInfoSendPO.setBroadId(adapter.getNewId(abilityInfoSendDAO.getAbilityBroadIds()));
			abilityInfoSendDAO.save(abilityInfoSendPO);
			
			return abilityInfoSendPO.getBroadId();
		}
	}
	
	public AbilityInfoSendPO save(Long channelId, String broadUrlIp, String broadUrlPort, Boolean encryption) throws Exception{
		AbilityInfoSendPO abilityInfoSendPO = abilityInfoSendDAO.findByChannelId(channelId);
		if (abilityInfoSendPO == null) {
			abilityInfoSendPO = new AbilityInfoSendPO();
			abilityInfoSendPO.setChannelId(channelId);
			abilityInfoSendPO.setUpdateTime(new Date());
			abilityInfoSendPO.setBroadId(adapter.getNewId(abilityInfoSendDAO.getAbilityBroadIds()));
		}
		
		abilityInfoSendPO.setBroadUrlIp(broadUrlIp);
		abilityInfoSendPO.setBroadUrlPort(broadUrlPort);
		abilityInfoSendPO.setBroadEncryption(encryption);
		
		abilityInfoSendDAO.save(abilityInfoSendPO);
		
		return abilityInfoSendPO;
	}
}
