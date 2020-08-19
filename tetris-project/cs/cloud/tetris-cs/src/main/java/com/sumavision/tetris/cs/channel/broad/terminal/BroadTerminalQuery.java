package com.sumavision.tetris.cs.channel.broad.terminal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.cs.bak.VersionSendQuery;
import com.sumavision.tetris.cs.channel.Adapter;
import com.sumavision.tetris.cs.channel.ChannelBroadStatus;
import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;

@Component
public class BroadTerminalQuery {
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private ChannelDAO channelDao;
	
	@Autowired
	private VersionSendQuery versionSendQuery;
	
	@Autowired
	private Adapter adapter;

	/**
	 * 查询播发状态(终端播发状态)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public String getChannelBroadstatus(Long channelId) throws Exception {
		String status = "";
		
		String versionSendNum = versionSendQuery.getLastBroadId(channelId);
		if (!versionSendNum.isEmpty()) {
			List<String> ids = new ArrayList<String>();
			ids.add(versionSendNum);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("ids", ids);
			JSONObject response = HttpRequestUtil.httpPost(adapter.getTerminalUrl(BroadTerminalQueryType.QUERY_SEND_FILE), jsonObject);
			if (response != null && response.get("result").toString().equals("1") && response.get("data") != null) {
				JSONArray statusArray = (JSONArray) response.get("data");
				if (statusArray != null && statusArray.size() > 0) {
					JSONObject item = (JSONObject) statusArray.get(0);
					status = (item.containsKey("status") && item.get("status") != null)
							? ChannelBroadStatus.fromStatusNum(item.getString("status")) : "";
				}
//			} else {
//				throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.QUERY_SEND_FILE.getAction(), response != null ? response.getString("message") : "");
			}
		}

		return status;
	}
	
	/**
	 * 更新播发状态<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param List<ChannelPO> channels 预更新的频道列表
	 */
	public void refreshChannelBroadstatus(List<ChannelPO> channels) throws Exception {
		List<String> broadId = new ArrayList<String>();

		for (ChannelPO item : channels) {
			String versionCode = versionSendQuery.getLastBroadId(item.getId());
			if (!versionCode.isEmpty()) {
				broadId.add(versionCode);
			}
		}

		if (broadId != null && broadId.size() > 0) {
			JSONObject statusRequestJsonObject = new JSONObject();
			statusRequestJsonObject.put("ids", broadId);
			
			JSONObject response = HttpRequestUtil.httpPost(adapter.getTerminalUrl(BroadTerminalQueryType.QUERY_SEND_FILE),statusRequestJsonObject);
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
									String status = ChannelBroadStatus.fromStatusNum(item.getString("status"));
									if (!status.isEmpty()) {
										channelPO.setBroadcastStatus(status);
									}
									channelDao.save(channelPO);
								}
								//TODO:response里面只有push平台有的id，平台没有的话业务不更新状态
								
							}
						}
					}
				}
//			} else {
//				throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.QUERY_SEND_FILE.getAction(), response != null ? response.getString("message") : "");
			}
		}
	}
}
