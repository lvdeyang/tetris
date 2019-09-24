package com.sumavision.tetris.cs.channel.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.ChannelType;
import com.sumavision.tetris.cs.schedule.ScheduleService;
import com.sumavision.tetris.cs.schedule.api.ApiServerScheduleVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/process/cs/channel")
public class ApiProcessChannelController {
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(String assetPath, Integer cycleCnt, String taskInfo, HttpServletRequest request) throws Exception{
		//本地地址和端口，让push的流推到本地，再转码
		String ip = "192.165.56.85";
		String port = "5666";
		
		ChannelPO channel = channelService.add("remote_udp", DateUtil.now(), "轮播能力", ip, port, "", ChannelType.REMOTE);
		
		if (channel != null) {
			ApiServerScheduleVO scheduleVO = new ApiServerScheduleVO();
			List<String> assetPaths = new ArrayList<String>();
			for (int i = 0; i < cycleCnt; i++) {
				assetPaths.add(assetPath);
			}
			scheduleVO.setAssetPaths(assetPaths);
			scheduleVO.setBroadDate(DateUtil.getDateByMillisecond(DateUtil.getLongDate()+1000).toString());
			
			scheduleService.addSchedules(channel.getId(), new ArrayListWrapper<ApiServerScheduleVO>().add(scheduleVO).getList());
			
			channelService.startBroad(channel.getId());
		}
		
		return new HashMapWrapper<String, Object>().put("assetPath", new StringBufferWrapper().append("udp://" + ip + ":" + port))
				.put("taskInfo", taskInfo)
				.getMap();
	}
	
}
