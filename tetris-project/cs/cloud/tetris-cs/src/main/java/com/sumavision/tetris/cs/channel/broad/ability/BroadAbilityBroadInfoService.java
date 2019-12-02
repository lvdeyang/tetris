package com.sumavision.tetris.cs.channel.broad.ability;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.channel.Adapter;
import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.exception.ChannelUdpIpAndPortAlreadyExistException;
import com.sumavision.tetris.cs.channel.exception.ChannelUdpUserIdAlreadyExistException;
import com.sumavision.tetris.cs.config.ServerProps;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.user.UserVO;

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
	
	@Autowired
	private ServerProps serverProps;
	
	/**
	 * 预播发用户VO转换<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午1:45:37
	 * @param List<UserVO> users 预播发用户列表
	 * @param String port 播发端口
	 * @return
	 */
	public List<BroadAbilityBroadInfoVO> changeVO(List<UserVO> users, String port) throws Exception {
		List<BroadAbilityBroadInfoVO> abilityBroadInfoVOs = new ArrayList<BroadAbilityBroadInfoVO>();
		if (users != null){
			for (UserVO user : users) {
				abilityBroadInfoVOs.add(new BroadAbilityBroadInfoVO()
						.setUserId(user.getId())
						.setPreviewUrlPort(port != null && !port.isEmpty() ? port : "9999"));
			}
		}
		return abilityBroadInfoVOs;
	}
	
	/**
	 * 保存频道的下发信息设置<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 下午3:49:25
	 * @param channelId 频道id
	 * @param infoVOs 下发设置列表
	 */
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
			List<Long> mediaIds = removePos.stream().map(BroadAbilityBroadInfoPO::getMediaId).collect(Collectors.toList());
			if (mediaIds != null && !mediaIds.isEmpty()) while (mediaIds.remove(null));
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
			infoPO.setUserId(broadAbilityBroadInfoVO.getUserId());
			String previewIp = broadAbilityBroadInfoVO.getPreviewUrlIp();
			String previewPort = broadAbilityBroadInfoVO.getPreviewUrlPort();
			if (previewIp != null && previewPort != null) {
				if (previewIp.isEmpty() || previewPort.isEmpty()) continue;
				infoPO.setPreviewUrlIp(previewIp);
				infoPO.setPreviewUrlPort(previewPort);
				MediaVideoStreamVO mediaVideoStream = mediaVideoStreamService.addVideoStreamTask(adapter.getUdpUrlFromIpAndPort(previewIp, previewPort), channelPO.getName());
				infoPO.setMediaId(mediaVideoStream.getId());
			} else {
				infoPO.setPreviewUrlPort(previewPort != null && !previewPort.isEmpty() ? previewPort : "9999");
			}
			saveInfoPOs.add(infoPO);
		}
		
		broadAbilityBroadInfoDAO.save(saveInfoPOs);
		
		return saveInfoPOs;
	}
	
	/**
	 * 删除所有频道下发信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 下午3:47:22
	 * @param channelId
	 * @throws Exception
	 */
	public void remove(Long channelId) throws Exception {
		List<BroadAbilityBroadInfoVO> infoVOs = queryFromChannelId(channelId);
		if (infoVOs == null || infoVOs.isEmpty()) return;
		List<Long> mediaIds = new ArrayList<Long>();
		for (BroadAbilityBroadInfoVO infoVO : infoVOs) {
			if (infoVO.getMediaId() != null) mediaIds.add(infoVO.getMediaId());
		}
		if (mediaIds != null && !mediaIds.isEmpty()) mediaVideoStreamService.remove(mediaIds);
		broadAbilityBroadInfoDAO.deleteByChannelId(channelId);
	}
	
	/**
	 * 根据频道id获取下发信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 下午3:46:29
	 * @param channelId 频道id
	 */
	public List<BroadAbilityBroadInfoVO> queryFromChannelId(Long channelId) throws Exception {
		List<BroadAbilityBroadInfoPO> infoPOs = broadAbilityBroadInfoDAO.findByChannelId(channelId);
		return BroadAbilityBroadInfoVO.getConverter(BroadAbilityBroadInfoVO.class).convert(infoPOs, BroadAbilityBroadInfoVO.class);
	}
	
	/**
	 * 查询ip和端口对是否被占用<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 下午3:44:39
	 * @param channelId 频道id(可为空)
	 * @param abilityBroadInfoVOs 待检测新设置
	 */
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
	
	/**
	 * 查询预播发终端用户是否被占用<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 下午3:45:38
	 * @param channelId 频道id(可为空)
	 * @param userVOs 待检测用户列表
	 */
	public void checkUserUse(Long channelId, List<UserVO> userVOs) throws Exception {
		if (userVOs == null || userVOs.isEmpty()) return;
		List<Long> userIds = userVOs.stream().map(UserVO::getId).collect(Collectors.toList());
		List<BroadAbilityBroadInfoPO> abilityBroadInfoPOs = 
				channelId == null ? broadAbilityBroadInfoDAO.findByUserIdIn(userIds) : broadAbilityBroadInfoDAO.findByUserIdInExceptChannelId(userIds, channelId);
		
		if (!abilityBroadInfoPOs.isEmpty()) {
			Long userId = abilityBroadInfoPOs.get(0).getUserId();
			for (UserVO user : userVOs) {
				if (userId.equals(user.getId())) {
					Long sameChannelId = abilityBroadInfoPOs.get(0).getChannelId();
					ChannelPO channelPO = channelDAO.findOne(sameChannelId);
					throw new ChannelUdpUserIdAlreadyExistException(user.getNickname(), channelPO != null ? channelPO.getName() : "不详");
				}
			}
		}
	}
	
	/**
	 * 根据ip查询可用端口(给文件转流提供)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月28日 上午10:27:05
	 * @param String searchIp 查询的ip
	 * @param Long startPort 查询的端口
	 * @param String 可用的首个端口
	 */
	public Long queryLocalPort(String searchIp, Long startPort) throws Exception{
		if (searchIp == null || searchIp.isEmpty()) searchIp = serverProps.getIp();
		List<String> ports = broadAbilityBroadInfoDAO.findByPreviewUrlIp(searchIp);
		if (ports == null || ports.isEmpty()) return startPort;
		
		Long returnPort = 0l;
		for (Long i = startPort; i < startPort + 2000; i++) {
			String port = i.toString();
			if (!ports.contains(port)) {
				return i;
			}
		}
		
		return returnPort;
	}
}
