package com.sumavision.tetris.cs.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.channel.exception.ChannelUdpIpAndPortAlreadyExistException;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class BroadAbilityBroadInfoService {
	@Autowired
	private BroadAbilityBroadInfoDAO broadAbilityBroadInfoDAO;
	
	@Autowired
	private MediaVideoStreamService mediaVideoStreamService;
	
	@Autowired
	private ChannelDAO channelDAO;
	
	@Autowired
	private Adapter adapter;
	
	public List<BroadAbilityBroadInfoPO> saveInfoList(Long channelId, List<BroadAbilityBroadInfoVO> infoVOs) throws Exception {
		if (infoVOs == null || infoVOs.isEmpty()) return null;
		ChannelPO channelPO = channelDAO.findOne(channelId);
		if (channelPO == null) return null; 
		
		List<BroadAbilityBroadInfoPO> aliveInfoPOs = broadAbilityBroadInfoDAO.findByChannelId(channelId);
		List<BroadAbilityBroadInfoVO> saveInfoVOs = new ArrayList<BroadAbilityBroadInfoVO>();
		
		if (!aliveInfoPOs.isEmpty()) {
			/** 数据库移除被替换的Ip和端口对 */
			List<BroadAbilityBroadInfoPO> removePos = new ArrayList<BroadAbilityBroadInfoPO>();
			for (BroadAbilityBroadInfoPO aliveInfoPO : aliveInfoPOs) {
				if (!infoVOs.contains(new BroadAbilityBroadInfoVO().set(aliveInfoPO))) {
					removePos.add(aliveInfoPO);
				}
			}
			broadAbilityBroadInfoDAO.deleteInBatch(removePos);
			/** 从视频流媒资中删除 */
			List<Long> mediaIds = removePos.stream().map(BroadAbilityBroadInfoPO::getId).collect(Collectors.toList());
			if (mediaIds != null && !mediaIds.isEmpty()) mediaVideoStreamService.remove(mediaIds);
			
			/** 获取需保存的ip和端口对 */
			List<BroadAbilityBroadInfoVO> aliveInfoVOs = BroadAbilityBroadInfoVO.getConverter(BroadAbilityBroadInfoVO.class)
					.convert(aliveInfoPOs, BroadAbilityBroadInfoVO.class);
			for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : infoVOs) {
				if (!aliveInfoVOs.contains(broadAbilityBroadInfoVO)) {
					saveInfoVOs.add(broadAbilityBroadInfoVO);
				}
			}
		} else {
			saveInfoVOs = infoVOs;
		}
		
		List<BroadAbilityBroadInfoPO> saveInfoPOs = new ArrayList<BroadAbilityBroadInfoPO>();
		for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : saveInfoVOs) {
			BroadAbilityBroadInfoPO infoPO = new BroadAbilityBroadInfoPO();
			infoPO.setChannelId(channelId);
			infoPO.setPreviewUrlIp(broadAbilityBroadInfoVO.getPreviewUrlIp());
			infoPO.setPreviewUrlPort(broadAbilityBroadInfoVO.getPreviewUrlPort());
			MediaVideoStreamVO mediaVideoStream = mediaVideoStreamService.addVideoStreamTask(
					adapter.getUdpUrlFromIpAndPort(broadAbilityBroadInfoVO.getPreviewUrlIp(), broadAbilityBroadInfoVO.getPreviewUrlPort()), channelPO.getName());
			infoPO.setMediaId(mediaVideoStream.getId());
			saveInfoPOs.add(infoPO);
		}
		
		broadAbilityBroadInfoDAO.save(saveInfoPOs);
		
		return saveInfoPOs;
	}
	
	public void saveInfoPOs(List<BroadAbilityBroadInfoPO> infoPOs) throws Exception {
		if (infoPOs == null || infoPOs.isEmpty()) return;
		
		broadAbilityBroadInfoDAO.save(infoPOs);
	}
	
	public void remove(Long channelId) throws Exception {
		List<BroadAbilityBroadInfoVO> infoVOs = queryFromChannelId(channelId);
		if (infoVOs == null || infoVOs.isEmpty()) return;
		List<Long> mediaIds = infoVOs.stream().map(BroadAbilityBroadInfoVO::getId).collect(Collectors.toList());
		if (mediaIds != null && !mediaIds.isEmpty()) mediaVideoStreamService.remove(mediaIds);
	}
	
	public List<BroadAbilityBroadInfoVO> queryFromChannelId(Long channelId) throws Exception {
		List<BroadAbilityBroadInfoPO> infoPOs = broadAbilityBroadInfoDAO.findByChannelId(channelId);
		return BroadAbilityBroadInfoVO.getConverter(BroadAbilityBroadInfoVO.class).convert(infoPOs, BroadAbilityBroadInfoVO.class);
	}
	
	public void checkIpAndPortExists(Long channelId, List<BroadAbilityBroadInfoVO> abilityBroadInfoVOs) throws Exception {
		if (abilityBroadInfoVOs == null || abilityBroadInfoVOs.isEmpty()) return;
		List<BroadAbilityBroadInfoPO> abilityBroadInfoPOs = 
				channelId == null ? broadAbilityBroadInfoDAO.findAll() : broadAbilityBroadInfoDAO.findAllExceptChannelId(channelId);
		if (abilityBroadInfoPOs == null || abilityBroadInfoPOs.isEmpty()) return;
		
		List<BroadAbilityBroadInfoVO> checkAbilityBroadInfoVOs = 
				BroadAbilityBroadInfoVO.getConverter(BroadAbilityBroadInfoVO.class).convert(abilityBroadInfoPOs, BroadAbilityBroadInfoVO.class);
		
		for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : abilityBroadInfoVOs) {
			if (checkAbilityBroadInfoVOs.contains(broadAbilityBroadInfoVO)) {
				throw new ChannelUdpIpAndPortAlreadyExistException(broadAbilityBroadInfoVO.getPreviewUrlIp(),broadAbilityBroadInfoVO.getPreviewUrlPort());
			}
		}
	}
}
