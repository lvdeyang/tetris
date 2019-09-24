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
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.cs.bak.VersionSendQuery;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class ChannelQuery {
	@Autowired
	private ChannelDAO channelDao;

	@Autowired
	private VersionSendQuery versionSendQuery;
	
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
		return new HashMapWrapper<String, Object>().put("rows", ChannelVO.getConverter(ChannelVO.class).convert(newChannels.getContent(), ChannelVO.class))
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
	public ChannelPO findByChannelId(Long channelId) {
		return channelDao.findOne(channelId);
	}
	
	/**
	 * 根据频道id查询能力播发下发类型<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return 下发类型
	 */
	public BroadAbilityQueryType broadCmd(Long channelId){
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel.getBroadUrlIp() == null && channel.getBroadUrlPort() == null) {
			return BroadAbilityQueryType.NEW;
		}else if (channel.getBroadUrlIp().equals(channel.getPreviewUrlIp())
				&& channel.getBroadUrlPort().equals(channel.getPreviewUrlPort())) {
			return BroadAbilityQueryType.COVER;
		}else {
			return BroadAbilityQueryType.CHANGE;
		}
	}
	
	/**
	 * 播发成功后保留播发信息(能力播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public void saveBroad(Long channelId){
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel != null) {
			channel.setBroadUrlIp(channel.getPreviewUrlIp());
			channel.setBroadUrlPort(channel.getPreviewUrlPort());
		}
		channelDao.save(channel);
	}

	/**
	 * 更新播发状态(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return 下发类型
	 */
	private void freshBroadStatus(List<ChannelPO> channels) throws Exception {
		if (channels == null || channels.isEmpty()) return;
		
		List<String> broadId = new ArrayList<String>();

		for (ChannelPO item : channels) {
			String versionCode = versionSendQuery.getBroadId(item.getId());
			if (item.getBroadWay().equals(BroadWay.TERMINAL_BROAD.getName()) && !versionCode.isEmpty()) {
				broadId.add(versionCode);
			}
		}

		if (broadId != null && broadId.size() > 0) {
			JSONObject statusRequestJsonObject = new JSONObject();
			statusRequestJsonObject.put("ids", broadId);
			
			String url = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.TERMINAL_BROAD);
			if (!url.isEmpty()) {
				JSONObject response = HttpRequestUtil.httpPost(
						"http://" + url + "/ed/speaker/querySendFile",
						statusRequestJsonObject);
				if (response != null && response.get("result").toString().equals("1") && response.get("data") != null) {
					JSONArray statusArray = (JSONArray) response.get("data");
					if (statusArray != null && statusArray.size() > 0) {
						for (int i = 0; i < statusArray.size(); i++) {
							JSONObject item = (JSONObject) statusArray.get(i);
							String id = item.getString("id");
							if (id != null && !id.isEmpty()) {
								Long channelId = versionSendQuery.getChannelId(id);
								if (channelId != null) {
									ChannelPO channelPO = channelDao.findOne(channelId);
									if (channelPO != null && item.containsKey("status") && item.get("status") != null) {
										String status = getStatusFromNum(item.getString("status"));
										if (!status.isEmpty()) {
											channelPO.setBroadcastStatus(status);
										}
										channelDao.save(channelPO);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public boolean sendAbilityRequest(BroadAbilityQueryType type, ChannelPO channel) throws Exception{
		return sendAbilityRequest(type, channel, null, null, null);
	}
	
	public boolean sendAbilityRequest(BroadAbilityQueryType type, ChannelPO channel, Long duration) throws Exception{
		return sendAbilityRequest(type, channel, null, null, duration);
	}
	
	public boolean sendAbilityRequest(BroadAbilityQueryType type, ChannelPO channel, List<String> input, JSONObject output) throws Exception{
		return sendAbilityRequest(type, channel, input, output, null);
	}
	
	/**
	 * 能力播发相关http请求<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param BroadAbilityQueryType type 请求类型
	 * @param ChannelPO channel 频道
	 * @param List<String> input 播发媒资列表(有顺序，仅COVER和NEW播发使用)
	 * @param JSONObject output 能力端的输出(仅NEW播发使用)
	 * @param Long duration 跳转值(仅SEEK使用)
	 * @throws Exception 
	 */
	public boolean sendAbilityRequest(BroadAbilityQueryType type, ChannelPO channel, List<String> input, JSONObject output, Long duration) throws Exception{
//		JSONObject request = new JSONObject();
//		request.put("id", channel.getBroadId());
//		if (BroadAbilityQueryType.COVER == type) {
//			request.put("cmd", type.getCmd());
//			request.put("input", input);
//		} else if (BroadAbilityQueryType.NEW == type) {
//			request.put("cmd", type.getCmd());
//			request.put("output", output);
//			request.put("local_ip", "");
//			request.put("loop_count", "1");
//			request.put("input", input);
//		} else if (BroadAbilityQueryType.STOP == type || BroadAbilityQueryType.DELETE == type) {
//			request.put("cmd", type.getCmd());
//		} else if (BroadAbilityQueryType.CHANGE == type) {
//			if (sendAbilityRequest(BroadAbilityQueryType.DELETE, channel)) {
//				return sendAbilityRequest(BroadAbilityQueryType.NEW, channel, input, output);
//			} else {
//				return false;
//			}
//		} else if (BroadAbilityQueryType.SEEK == type) {
//			request.put("cmd", type.getCmd());
//			request.put("duration", duration);
//		}
//		JSONObject response = HttpRequestUtil.httpPost("http://" + ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.ABILITY_BROAD), request);
//		if (response != null && response.containsKey("stat") && response.getString("stat").equals("success")) {
			return true;
//		}else {
//			return false;
//		}
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
