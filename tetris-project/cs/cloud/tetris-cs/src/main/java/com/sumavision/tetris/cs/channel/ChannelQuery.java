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
import com.sumavision.tetris.cs.HttpRequestUtil;
import com.sumavision.tetris.cs.bak.VersionSendQuery;

@Component
public class ChannelQuery {
	@Autowired
	private ChannelDAO channelDao;

	@Autowired
	private VersionSendQuery versionSendQuery;

	public List<ChannelPO> findAll(int currentPage, int pageSize) throws Exception {
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<ChannelPO> channels = channelDao.findAll(page);
		freshBroadStatus(channels.getContent());

		Page<ChannelPO> newChannels = channelDao.findAll(page);
		return newChannels.getContent();
	}

	public ChannelPO findByChannelId(Long channelId) {
		return channelDao.findOne(channelId);
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
				JSONObject response = HttpRequestUtil.httpPost(
						"http://" + ChannelBroadStatus.getBroadcastIPAndPort() + "/ed/speaker/querySendFile",
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
