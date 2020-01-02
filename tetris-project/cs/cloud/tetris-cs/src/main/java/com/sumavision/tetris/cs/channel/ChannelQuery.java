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
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.cs.channel.autoBroad.ChannelAutoBroadInfoDAO;
import com.sumavision.tetris.cs.channel.autoBroad.ChannelAutoBroadInfoPO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoDAO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoPO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.cs.channel.broad.file.BroadFileBroadInfoService;
import com.sumavision.tetris.cs.channel.broad.file.BroadFileBroadInfoVO;
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
	public Map<String, Object> findAll(int currentPage, int pageSize, ChannelType type) throws Exception {
		UserVO user = userQuery.current();
		
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<ChannelPO> channels = channelDao.PagefindAllByGroupIdAndType(user.getGroupId(), type.toString(), page);
		freshBroadStatus(channels.getContent());
		
		Page<ChannelPO> newChannels = channelDao.PagefindAllByGroupIdAndType(user.getGroupId(), type.toString(), page);
		
		List<ChannelVO> channelVOs = ChannelVO.getConverter(ChannelVO.class).convert(newChannels.getContent(), ChannelVO.class);
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
						outputUsers.add(userQuery.findByIdIn(new ArrayListWrapper<Long>().add(userId).getList()).get(0));
						if (channelVO.getOutputUserPort() == null || channelVO.getOutputUserPort().isEmpty()) channelVO.setOutputUserPort(broadAbilityBroadInfoPO.getPreviewUrlPort());
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
					outputUsers.add(userQuery.findByIdIn(new ArrayListWrapper<Long>().add(broadFileBroadInfoVO.getUserId()).getList()).get(0));
				}
				channelVO.setOutputUsers(outputUsers);
			}
		}
		
		return new HashMapWrapper<String, Object>().put("rows", channelVOs)
				.put("total", newChannels.getTotalElements())
				.getMap();
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
		JSONArray template = new JSONArray();
		if (BroadWay.fromName(channel.getBroadWay()) == BroadWay.ABILITY_BROAD) {
			template.add(oneScreen());
		} else {
			String templateString = adapter.readTemplate();
			if (templateString == null || templateString.isEmpty()){
				template.add(oneScreen());
			}  else {
				template = JSONArray.parseArray(templateString);
			}
		}
		return template;
	}
	
	private JSONObject oneScreen() {
		JSONObject object = new JSONObject();
		object.put("id", 1);
		object.put("name", "一分屏");
		object.put("screenNum", 1);
		JSONArray screen = new JSONArray();
		JSONObject screenObject = new JSONObject();
		screenObject.put("no", 1);
		screenObject.put("width", "100%");
		screenObject.put("height", "100%");
		screenObject.put("top", "0%");
		screenObject.put("left", "0%");
		screen.add(screenObject);
		object.put("screen", screen);
		return object;
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
	
	public String getStatusFromNum(String statusNum) {
		String returnString = "";
		switch (statusNum) {
		case "0":
			returnString = ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING;
			break;
		case "1":
			returnString = ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED;
			break;
		case "2":
			returnString = ChannelBroadStatus.CHANNEL_BROAD_STATUS_STOPPED;
			break;
		default:
			break;
		}
		return returnString;
	}
}
