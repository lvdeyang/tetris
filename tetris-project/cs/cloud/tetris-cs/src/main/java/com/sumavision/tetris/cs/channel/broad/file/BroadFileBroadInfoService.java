package com.sumavision.tetris.cs.channel.broad.file;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.exception.ChannelUdpUserIdAlreadyExistException;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class BroadFileBroadInfoService {
	@Autowired
	private BroadFileBroadInfoDAO broadFileBroadInfoDAO;
	
	@Autowired
	private ChannelDAO channelDAO;
	
	/**
	 * 预播发用户VO转换<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午1:55:25
	 * @param List<UserVO> users 预播发用户列表
	 * @return List<BroadFileBroadInfoVO> 转换后的VO
	 */
	public List<BroadFileBroadInfoVO> changeVO(List<UserVO> users) throws Exception {
		List<BroadFileBroadInfoVO> saveinInfoVOs = new ArrayList<BroadFileBroadInfoVO>();
		if (users != null) {
			for (UserVO userVO : users) {
				saveinInfoVOs.add(new BroadFileBroadInfoVO()
						.setUserId(userVO.getId())
						.setUserIp(userVO.getIp())
						.setUserEquipType(userVO.getEquipType()));
			}
		}
		return saveinInfoVOs;
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
		List<BroadFileBroadInfoPO> fileBroadInfoPOs = 
				channelId == null ? broadFileBroadInfoDAO.findByUserIdIn(userIds) : broadFileBroadInfoDAO.findByUserIdInExceptChannelId(userIds, channelId);
		
		if (!fileBroadInfoPOs.isEmpty()) {
			Long userId = fileBroadInfoPOs.get(0).getUserId();
			for (UserVO user : userVOs) {
				if (userId.equals(user.getId())) {
					Long sameChannelId = fileBroadInfoPOs.get(0).getChannelId();
					ChannelPO channelPO = channelDAO.findOne(sameChannelId);
					throw new ChannelUdpUserIdAlreadyExistException(user.getNickname(), channelPO != null ? channelPO.getName() : "不详");
				}
			}
		}
	}
	
	/**
	 * 保存频道的下发信息设置<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 上午11:03:53
	 * @param channelId 频道id
	 * @param infoVOs 下发设置列表
	 */
	public List<BroadFileBroadInfoPO> saveInfoList(Long channelId, List<BroadFileBroadInfoVO> infoVOs) throws Exception {
		if (infoVOs == null || infoVOs.isEmpty()) {
			broadFileBroadInfoDAO.deleteByChannelId(channelId);
			return new ArrayList<BroadFileBroadInfoPO>();
		};
		ChannelPO channelPO = channelDAO.findOne(channelId);
		if (channelPO == null) return null; 

		List<BroadFileBroadInfoPO> aliveInfoPOs = broadFileBroadInfoDAO.findByChannelId(channelId);
		List<BroadFileBroadInfoVO> saveInfoVOs = new ArrayList<BroadFileBroadInfoVO>();
		
		if (!aliveInfoPOs.isEmpty()) {
			/** 数据库移除被替换的播发用户 */
			List<BroadFileBroadInfoPO> removePos = new ArrayList<BroadFileBroadInfoPO>();
			for (BroadFileBroadInfoPO aliveInfoPO : aliveInfoPOs) {
				if (!infoVOs.contains(new BroadFileBroadInfoVO().set(aliveInfoPO))) {
					removePos.add(aliveInfoPO);
				}
			}
			broadFileBroadInfoDAO.deleteInBatch(removePos);
			
			/** 获取需保存的播发用户 */
			List<BroadFileBroadInfoVO> aliveInfoVOs = BroadFileBroadInfoVO.getConverter(BroadFileBroadInfoVO.class)
					.convert(aliveInfoPOs, BroadFileBroadInfoVO.class);
			for (BroadFileBroadInfoVO broadFileBroadInfoVO : infoVOs) {
				if (!aliveInfoVOs.contains(broadFileBroadInfoVO)) {
					saveInfoVOs.add(broadFileBroadInfoVO);
				}
			}
		} else {
			saveInfoVOs = infoVOs;
		}
		
		List<BroadFileBroadInfoPO> saveInfoPOs = new ArrayList<BroadFileBroadInfoPO>();
		for (BroadFileBroadInfoVO broadFileBroadInfoVO : saveInfoVOs) {
			BroadFileBroadInfoPO infoPO = new BroadFileBroadInfoPO();
			infoPO.setChannelId(channelId);
			infoPO.setUserId(broadFileBroadInfoVO.getUserId());
			infoPO.setUserIp(broadFileBroadInfoVO.getUserIp());
			infoPO.setUserEquipType(broadFileBroadInfoVO.getUserEquipType());
			saveInfoPOs.add(infoPO);
		}
		
		broadFileBroadInfoDAO.save(saveInfoPOs);
		
		return saveInfoPOs;
	}
	
	/**
	 * 删除所有频道下发信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 下午3:47:22
	 * @param Long channelId 频道id
	 */
	public void remove(Long channelId) throws Exception {
		List<BroadFileBroadInfoVO> infoVOs = queryFromChannelId(channelId);
		if (infoVOs == null || infoVOs.isEmpty()) return;
		broadFileBroadInfoDAO.deleteByChannelId(channelId);
	}
	
	/**
	 * 根据频道id获取下发信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 下午3:46:29
	 * @param channelId 频道id
	 */
	public List<BroadFileBroadInfoVO> queryFromChannelId(Long channelId) throws Exception {
		List<BroadFileBroadInfoPO> infoPOs = broadFileBroadInfoDAO.findByChannelId(channelId);
		return BroadFileBroadInfoVO.getConverter(BroadFileBroadInfoVO.class).convert(infoPOs, BroadFileBroadInfoVO.class);
	}
	
	/**
	 * 根据用户Id查询下发信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月6日 下午4:45:07
	 * @param List<Long> userIds 用户Id数组
	 * @return List<BroadFileBroadInfoVO> 下发信息
	 */
	public List<BroadFileBroadInfoVO> queryFromUserIds(List<Long> userIds) throws Exception {
		List<BroadFileBroadInfoPO> infoPOs = broadFileBroadInfoDAO.findByUserIdIn(userIds);
		return BroadFileBroadInfoVO.getConverter(BroadFileBroadInfoVO.class).convert(infoPOs, BroadFileBroadInfoVO.class);
	}
}
