package com.sumavision.tetris.cs.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeign;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;
import com.sumavision.tetris.commons.context.SystemInitialization;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.cs.channel.Adapter;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.broad.ChannelServerType;
import com.sumavision.tetris.cs.config.ServerProps;

@Service
@Transactional(rollbackFor = Exception.class)
public class CsInitialization implements SystemInitialization{
	
	private static final Logger LOG = LoggerFactory.getLogger(CsInitialization.class);
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private AlarmFeignClientService alarmFeignClientService;
	
	@Autowired
	private Adapter adapter;
	
	@Autowired
	private ServerProps serverProps;

	@Override
	public int index() {
		return 0;
	}

	@Override
	public void init() {
		JSONObject request = new JSONObject();
		request.put("rpt_ip", serverProps.getIp());
		request.put("rpt_port", serverProps.getPort());
		request.put("rpt_reboot_url", "/api/server/cs/channel/alarm/reboot");
		try {
			alarmFeignClientService.subscribeAlarm("11070001", "/channel/feign/alarm/reboot", true);
//			HttpRequestUtil.httpPost("http://" + adapter.getServerUrl(BroadWay.ABILITY_BROAD), request);
//			channelService.rebootServer(ChannelServerType.CS_LOCAL_SERVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
