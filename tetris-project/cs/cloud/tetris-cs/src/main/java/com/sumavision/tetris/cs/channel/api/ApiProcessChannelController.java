package com.sumavision.tetris.cs.channel.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.ChannelType;
import com.sumavision.tetris.cs.config.ServerProps;
import com.sumavision.tetris.cs.schedule.ScheduleService;
import com.sumavision.tetris.cs.schedule.api.ApiServerScheduleVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/process/cs/channel")
public class ApiProcessChannelController {
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(String file_fileToStreamInfo, String file_streamTranscodingInfo, String file_recordInfo, HttpServletRequest request) throws Exception{
		//本地地址和端口，让push的流推到本地，再转码
		FileToStreamVO vo = JSON.parseObject(file_fileToStreamInfo, FileToStreamVO.class);
		
		HashMapWrapper<String, Object> map = new HashMapWrapper<String, Object>();
		
		if (vo.isNeed()) {
			String ip = vo.getToolIp();
			String port = channelQuery.queryLocalPort(Long.parseLong(vo.getStartPort()));
			
			ChannelPO channel = channelService.add("remote_udp", DateUtil.now(), "轮播能力", ip, port, "", ChannelType.REMOTE);
			
			if (channel != null) {
				ApiServerScheduleVO scheduleVO = new ApiServerScheduleVO();
				List<String> assetPaths = new ArrayList<String>();
				for (int i = 0; i < vo.getPlayCount(); i++) {
					assetPaths.add(vo.getFileUrl());
				}
				scheduleVO.setAssetPaths(assetPaths);
				scheduleVO.setBroadDate(DateUtil.getDateByMillisecond(DateUtil.getLongDate()+1000).toString());
				
				scheduleService.addSchedules(channel.getId(), new ArrayListWrapper<ApiServerScheduleVO>().add(scheduleVO).getList());
				
				channelService.startBroad(channel.getId());
				
				map.put("assetPath", new StringBufferWrapper().append("udp://").append(ip).append(":").append(port));
			}
		}
		
		return map.put("transcode_streamTranscodingInfo", file_streamTranscodingInfo)
				.put("transcode_recordInfo", file_recordInfo)
				.getMap();
	}
	
	
	//回调
}
