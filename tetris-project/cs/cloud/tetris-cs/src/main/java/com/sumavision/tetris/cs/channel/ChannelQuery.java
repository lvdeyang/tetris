package com.sumavision.tetris.cs.channel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
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

	public List<ChannelPO> findAll(int currentPage, int pageSize) throws Exception {
		UserVO user = userQuery.current();
		
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<ChannelPO> channels = channelDao.findAllByGroupId(user.getGroupId(), page);
		freshBroadStatus(channels.getContent());

		Page<ChannelPO> newChannels = channelDao.findAllByGroupId(user.getGroupId(), page);
		return newChannels.getContent();
	}

	public ChannelPO findByChannelId(Long channelId) {
		return channelDao.findOne(channelId);
	}
	
	public String broadWay(Long channelId){
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel.getBroadUrlIp() == null && channel.getBroadUrlPort() == null) {
			return "new";
		}else if (channel.getBroadUrlIp().equals(channel.getPreviewUrlIp())
				&& channel.getBroadUrlPort().equals(channel.getPreviewUrlPort())) {
			return "cover";
		}else {
			return "change";
		}
	}
	
	public void saveBroad(Long channelId){
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel != null) {
			channel.setBroadUrlIp(channel.getPreviewUrlIp());
			channel.setBroadUrlPort(channel.getPreviewUrlPort());
		}
	}

	private void freshBroadStatus(List<ChannelPO> channelVOs) throws Exception {
		if (channelVOs != null && channelVOs.size() > 0) {
			List<String> broadId = new ArrayList<String>();

			for (ChannelPO item : channelVOs) {
				String versionCode = versionSendQuery.getBroadId(item.getId());
				if (!versionCode.isEmpty()) {
					broadId.add(versionCode);
				}
			}

			if (broadId != null && broadId.size() > 0) {
				JSONObject statusRequestJsonObject = new JSONObject();
				statusRequestJsonObject.put("ids", broadId);
				
				String url = ChannelBroadStatus.getBroadcastIPAndPort();
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
	}
	
	public boolean sendAbilityRequest(BroadAbilityQueryType type, ChannelPO channel, List<String> input,JSONObject output){
		JSONObject request = new JSONObject();
		request.put("id", channel.getBroadId());
		if (BroadAbilityQueryType.COVER == type) {
			request.put("cmd", type.getCmd());
			request.put("input", input);
		} else if (BroadAbilityQueryType.NEW == type) {
			request.put("cmd", type.getCmd());
			request.put("output", output);
			request.put("local_ip", "");
			request.put("loop_count", "1");
			request.put("input", input);
		} else if (BroadAbilityQueryType.STOP == type || BroadAbilityQueryType.DELETE == type) {
			request.put("cmd", type.getCmd());
		} else if (BroadAbilityQueryType.CHANGE == type) {
			if (sendAbilityRequest(BroadAbilityQueryType.DELETE, channel, null, null)) {
				return sendAbilityRequest(BroadAbilityQueryType.NEW, channel, input, output);
			} else {
				return false;
			}
		}
		JSONObject response = HttpRequestUtil.httpPost("http://" + "192.165.58.125" + ":" + "9000", request);
		if (response != null && response.containsKey("stat") && response.getString("stat").equals("success")) {
			return true;
		}else {
			return false;
		}
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
