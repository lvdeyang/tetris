package com.sumavision.tetris.cs.upgrade;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.cs.area.AreaVO;
import com.sumavision.tetris.cs.channel.Adapter;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalLevelType;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalQueryType;
import com.sumavision.tetris.cs.channel.exception.ChannelTerminalRequestErrorException;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class CsUpgradeService {
	
	@Autowired
	private Adapter adapter;
	
	public void start(String version, CsUpgradeBroadWay broadWay, MediaCompressVO compress, List<AreaVO> areaList) throws Exception {
		if (version == null || version.isEmpty()
				|| broadWay == null
				|| compress == null || compress.getPreviewUrl() == null || compress.getPreviewUrl().isEmpty()
				|| areaList == null || areaList.isEmpty()) return;
		
		JSONObject requestJson = new JSONObject();
		requestJson.put("fileType", "1");
		requestJson.put("updateVersion", version);
		requestJson.put("regionList", areaList.stream().map(AreaVO::getAreaId).collect(Collectors.toList()));
		requestJson.put("fileSize", compress.getSize());
		JSONObject response = null;
		switch (broadWay) {
			case BROAD_4G:
				requestJson.put("filepath", adapter.getEncodeUrl(compress.getPreviewUrl()));
				System.out.println("upgrade(4G): " + requestJson.toJSONString());
				response = HttpRequestUtil.httpPost(adapter.getTerminalUrl(BroadTerminalQueryType.IP_PUSH_SEND), requestJson);
				if (response != null && response.containsKey("result") && response.getString("result").equals("1")){
					
				} else {
					throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.IP_PUSH_SEND.getAction(), response != null ? response.getString("message") : "");
				}
				break;
			case BROAD_DTMB:
				requestJson.put("hasfile", 1);
				requestJson.put("level", BroadTerminalLevelType.NORMAL.getLevel());
				requestJson.put("id", DateUtil.format(new Date(), DateUtil.currentDateTimePattern));
				requestJson.put("filePath", compress.getPreviewUrl());
				System.out.println("upgrade(DTMB): " + requestJson.toJSONString());
				response = HttpRequestUtil.httpPost(adapter.getTerminalUrl(BroadTerminalQueryType.START_SEND_FILE), requestJson);
				if (response != null && response.containsKey("result") && response.getString("result").equals("1")) {
					
				} else {
					throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.START_SEND_FILE.getAction(), response != null ? response.getString("message") : "");
				}
				break;
			default:
				break;
		}
	}
}
