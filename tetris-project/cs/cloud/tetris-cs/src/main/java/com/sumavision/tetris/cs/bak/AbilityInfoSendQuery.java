package com.sumavision.tetris.cs.bak;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.cs.channel.BroadAbilityBroadInfoService;
import com.sumavision.tetris.cs.channel.BroadAbilityBroadInfoVO;

@Component
public class AbilityInfoSendQuery {
	@Autowired
	private AbilityInfoSendDAO abilityInfoSendDAO;
	
	@Autowired
	private BroadAbilityBroadInfoService broadAbilityBroadInfoService;
	
	public List<AbilityInfoSendPO> getByChannelId(Long channelId) throws Exception{
		List<AbilityInfoSendPO> info = abilityInfoSendDAO.findByChannelId(channelId);
		return info;
	}
	
	public Boolean checkBroadInfoChanged(Long channelId) throws Exception {
		List<AbilityInfoSendPO> aliveInfoSendPOs = getByChannelId(channelId);
		List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs = broadAbilityBroadInfoService.queryFromChannelId(channelId);
		
		if (aliveInfoSendPOs == null
				|| aliveInfoSendPOs.isEmpty()
				|| broadAbilityBroadInfoVOs == null
				|| broadAbilityBroadInfoVOs.isEmpty()) return true;
		
		if (broadAbilityBroadInfoVOs.size() != aliveInfoSendPOs.size()) return true;
		
		for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : broadAbilityBroadInfoVOs) {
			Boolean found = false;
			for (AbilityInfoSendPO aliveInfoSendPO : aliveInfoSendPOs) {
				if (broadAbilityBroadInfoVO.getId() == aliveInfoSendPO.getPreviewId()) {
					if (!broadAbilityBroadInfoVO.getPreviewUrlIp().equals(aliveInfoSendPO.getBroadUrlIp())
							|| !broadAbilityBroadInfoVO.getPreviewUrlPort().equals(broadAbilityBroadInfoVO.getPreviewUrlPort())) {
						return true;
					}
					found = true;
					break;
				}
			}
			if (!found) return true;
		}
		
		return false;
	}
	
	public void save(Long channelId, Boolean encryption) throws Exception{
		abilityInfoSendDAO.deleteByChannelId(channelId);
		
		List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs = broadAbilityBroadInfoService.queryFromChannelId(channelId);
		
		if (broadAbilityBroadInfoVOs == null || broadAbilityBroadInfoVOs.isEmpty()) return;
		
		List<AbilityInfoSendPO> savePOs = new ArrayList<AbilityInfoSendPO>();
		for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : broadAbilityBroadInfoVOs) {
			AbilityInfoSendPO abilityInfoSendPO = new AbilityInfoSendPO();
			abilityInfoSendPO.setChannelId(channelId);
			abilityInfoSendPO.setUpdateTime(new Date());
			abilityInfoSendPO.setBroadUrlIp(broadAbilityBroadInfoVO.getPreviewUrlIp());
			abilityInfoSendPO.setBroadUrlPort(broadAbilityBroadInfoVO.getPreviewUrlPort());
			abilityInfoSendPO.setBroadEncryption(encryption);
			abilityInfoSendPO.setPreviewId(broadAbilityBroadInfoVO.getId());
			abilityInfoSendPO.setMediaId(broadAbilityBroadInfoVO.getMediaId());
			savePOs.add(abilityInfoSendPO);
		}
		
		abilityInfoSendDAO.save(savePOs);
	}
}
