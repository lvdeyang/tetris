package com.sumavision.tetris.cs.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.cs.channel.autoBroad.ChannelAutoBroadInfoDAO;
import com.sumavision.tetris.cs.channel.autoBroad.ChannelAutoBroadInfoPO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoDAO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoPO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.cs.channel.broad.file.BroadFileBroadInfoService;
import com.sumavision.tetris.cs.channel.broad.file.BroadFileBroadInfoVO;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalBroadInfoPO;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalBroadInfoQuery;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalQuery;
import com.sumavision.tetris.cs.channel.exception.ChannelNotExistsException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class ChannelQuery {
	@Autowired
	private ChannelDAO channelDao;
	
	@Autowired
	private BroadTerminalQuery broadTerminalQuery;
	
	@Autowired
	private ChannelAutoBroadInfoDAO channelAutoBroadInfoDAO;
	
	@Autowired
	private BroadFileBroadInfoService broadFileBroadInfoService;
	
	@Autowired
	private BroadAbilityBroadInfoDAO broadAbilityBroadInfoDAO;
	
	@Autowired
	private BroadTerminalBroadInfoQuery broadTerminalBroadInfoQuery;
	
	@Autowired
	private Adapter adapter;
	
	@Autowired
	private UserQuery userQuery;

	/**
	 * 分页获取频道列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Integer currentPage 当前页
	 * @param Integer pageSize 分页大小
	 * @return Page<ChannelPO> channels 频道列表
	 */
	public Map<String, Object> findAll(Integer currentPage, Integer pageSize, ChannelType type) throws Exception {
		UserVO user = userQuery.current();
		
		List<ChannelPO> channels;
		Page<ChannelPO> newPageChannels = null;
		if (currentPage == null || pageSize == null) {
			freshBroadStatus(channelDao.findAllByGroupIAndType(user.getGroupId(), type.toString()));
			channels = channelDao.findAllByGroupIAndType(user.getGroupId(), type.toString());
		} else {
			Pageable page = new PageRequest(currentPage - 1, pageSize);
			Page<ChannelPO> pageChannels = channelDao.PagefindAllByGroupIdAndType(user.getGroupId(), type.toString(), page);
			freshBroadStatus(pageChannels.getContent());
			newPageChannels = channelDao.PagefindAllByGroupIdAndType(user.getGroupId(), type.toString(), page);
			channels = newPageChannels.getContent();
		}
		List<ChannelVO> channelVOs = ChannelVO.getConverter(ChannelVO.class).convert(channels, ChannelVO.class);
		for (ChannelVO channelVO : channelVOs) {
			if (channelVO.getAutoBroad() != null && channelVO.getAutoBroad()) {
				ChannelAutoBroadInfoPO channelAutoBroadInfoPO = channelAutoBroadInfoDAO.findByChannelId(channelVO.getId());
				if (channelAutoBroadInfoPO != null) {
					channelVO.setAutoBroadDuration(channelAutoBroadInfoPO.getDuration());
					channelVO.setAutoBroadStart(channelAutoBroadInfoPO.getStartTime());
					channelVO.setAutoBroadShuffle(channelAutoBroadInfoPO.getShuffle());
				}
			}
			
			BroadWay channelBroadWay = BroadWay.fromName(channelVO.getBroadWay());
			if (channelBroadWay == BroadWay.ABILITY_BROAD) {
				List<BroadAbilityBroadInfoPO> broadAbilityBroadInfoPOs = broadAbilityBroadInfoDAO.findByChannelId(channelVO.getId());
				List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs = new ArrayList<BroadAbilityBroadInfoVO>();
				List<UserVO> outputUsers = new ArrayList<UserVO>();
				for (BroadAbilityBroadInfoPO broadAbilityBroadInfoPO : broadAbilityBroadInfoPOs) {
					String previewIp = broadAbilityBroadInfoPO.getPreviewUrlIp();
					String previewPort = broadAbilityBroadInfoPO.getPreviewUrlPort();
					Long userId = broadAbilityBroadInfoPO.getUserId();
					if (userId != null) {
						outputUsers.add(userQuery.findByIdIn(new ArrayListWrapper<Long>().add(userId).getList()).get(0).setEquipType(TerminalType.QT_MEDIA_EDITOR.toString()));
						if (channelVO.getOutputUserPort() == null || channelVO.getOutputUserPort().isEmpty()) channelVO.setOutputUserPort(broadAbilityBroadInfoPO.getPreviewUrlPort());
						if (channelVO.getOutputUserEndPort() == null || channelVO.getOutputUserEndPort().isEmpty()) channelVO.setOutputUserEndPort(broadAbilityBroadInfoPO.getPreviewUrlEndPort());
					} else {
						if (previewIp != null && previewPort != null) {
							broadAbilityBroadInfoVOs.add(new BroadAbilityBroadInfoVO().set(broadAbilityBroadInfoPO));
						}
					}
				}
				if (broadAbilityBroadInfoVOs.isEmpty()) broadAbilityBroadInfoVOs.add(new BroadAbilityBroadInfoVO().setPreviewUrlIp("").setPreviewUrlPort(""));
				channelVO.setOutput(broadAbilityBroadInfoVOs);
				channelVO.setOutputUsers(outputUsers);
			} else if (channelBroadWay == BroadWay.FILE_DOWNLOAD_BROAD){
				List<UserVO> outputUsers = new ArrayList<UserVO>();
				List<BroadFileBroadInfoVO> broadFileBroadInfoPOs = broadFileBroadInfoService.queryFromChannelId(channelVO.getId());
				for (BroadFileBroadInfoVO broadFileBroadInfoVO : broadFileBroadInfoPOs) {
					outputUsers.add(userQuery.findByIdIn(new ArrayListWrapper<Long>().add(broadFileBroadInfoVO.getUserId()).getList()).get(0).setEquipType(broadFileBroadInfoVO.getUserEquipType()));
				}
				channelVO.setOutputUsers(outputUsers);
			} else {
				BroadTerminalBroadInfoPO broadTerminalBroadInfoPO = broadTerminalBroadInfoQuery.findByChannelId(channelVO.getId());
				if (broadTerminalBroadInfoPO != null) {
					channelVO.setLevel(broadTerminalBroadInfoPO.getLevel());
					channelVO.setHasFile(broadTerminalBroadInfoPO.getHasFile());
					channelVO.setEndDate(broadTerminalBroadInfoPO.getEndDate());
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("rows", channelVOs)
				.put("total", newPageChannels == null ? channelVOs.size() : newPageChannels.getTotalElements())
				.getMap();
	}
	
	/**
	 * 根据播发方式和状态查询频道列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午9:43:20
	 * @param BroadWay broadWay 播发方式
	 * @param ChannelBroadStatus broadStatus 播发状态
	 */
	public List<ChannelVO> queryByBroadWayAndStatus(BroadWay broadWay, ChannelBroadStatus broadStatus) throws Exception {
		List<ChannelPO> channelPOs = channelDao.findByBroadWayAndBroadcastStatus(broadWay.getName(), broadStatus.getName());
		return ChannelVO.getConverter(ChannelVO.class).convert(channelPOs, ChannelVO.class);
	}

	/**
	 * 根据id查询频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return ChannelPO 频道
	 */
	public ChannelPO findByChannelId(Long channelId) throws Exception {
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) throw new ChannelNotExistsException(channelId);
		return channel;
	}
	
	/**
	 * 根据id获取频道状态<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午5:43:36
	 * @param Long channelId 频道id
	 * @return String 频道状态
	 */
	public String getBroadstatus(Long channelId) throws Exception {
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) throw new ChannelNotExistsException(channelId);
		
		if (BroadWay.fromName(channel.getBroadWay()) == BroadWay.TERMINAL_BROAD) {
			return broadTerminalQuery.getChannelBroadstatus(channelId);
		} else {
			return channel.getBroadcastStatus();
		}
	}
	
	/**
	 * 读配置文件获取分屏模板<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月11日 下午5:33:21
	 * @param Long channelId 频道Id
	 * @return JSONArray 分屏模板数组
	 */
	public JSONArray getTemplate(Long channelId, Long scheduleId) throws Exception {
		ChannelPO channel = findByChannelId(channelId);
		if (BroadWay.fromName(channel.getBroadWay()) == BroadWay.TERMINAL_BROAD) {
			BroadTerminalBroadInfoPO broadInfoPO = broadTerminalBroadInfoQuery.findByChannelId(channelId);
			if (broadInfoPO != null && broadInfoPO.getHasFile() != null && !broadInfoPO.getHasFile()){
				return adapter.getOneTemplate();
			}
		}
		return adapter.getAllTemplate();
	}

	/**
	 * 更新播发状态(只更新终端播发频道的状态)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param List<ChannelPO> channels 预更新的频道列表
	 */
	private void freshBroadStatus(List<ChannelPO> channels) throws Exception {
		if (channels == null || channels.isEmpty()) return;
		List<ChannelPO> terminalChannels = new ArrayList<ChannelPO>();
		for (ChannelPO item : channels) {
			if (item.getBroadWay().equals(BroadWay.TERMINAL_BROAD.getName())) {
				terminalChannels.add(item);
			}
		}
		if (terminalChannels == null || terminalChannels.isEmpty()) return;
		broadTerminalQuery.refreshChannelBroadstatus(terminalChannels);
	}
}
