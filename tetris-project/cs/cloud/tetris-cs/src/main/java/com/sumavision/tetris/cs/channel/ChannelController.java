package com.sumavision.tetris.cs.channel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/cs/channel")
public class ChannelController {

	@Autowired
	private ChannelQuery channelQuery;

	@Autowired
	private ChannelDAO channelDao;

	@Autowired
	private ChannelService channelService;

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object channelList(Integer currentPage, Integer pageSize, HttpServletRequest request) throws Exception {
		List<ChannelPO> entities = channelQuery.findAll(currentPage, pageSize);

		List<ChannelVO> channels = ChannelVO.getConverter(ChannelVO.class).convert(entities, ChannelVO.class);

		Long total = channelDao.count();

		return new HashMapWrapper<String, Object>().put("rows", channels).put("total", total).getMap();
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(String name, String date, String broadWay, String previewUrlIp, String previewUrlPort, String remark, HttpServletRequest request) throws Exception {

		ChannelPO channel = channelService.add(name, date, broadWay, previewUrlIp, previewUrlPort, remark);

		return new ChannelVO().set(channel);
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object rename(Long id, String name, String previewUrlIp, String previewUrlPort, String remark, HttpServletRequest request) throws Exception {

		ChannelPO channel = channelService.edit(id, name, previewUrlIp, previewUrlPort, remark);

		return new ChannelVO().set(channel);
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(Long id, HttpServletRequest request) throws Exception {

		channelService.remove(id);

		return "";
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/start")
	public Object broadcastStart(Long channelId, HttpServletRequest request) throws Exception {

		ChannelPO channelPO = channelDao.findOne(channelId);
		if (channelPO == null) return null;
		
		if (channelPO.getBroadWay().equals(BroadWay.ABILITY_BROAD.getName())) {
			return channelService.startAbilityBroadcast(channelId);
		}else {
			return channelService.startTerminalBroadcast(channelId);
		}
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/stop")
	public Object broadcastStop(Long channelId, HttpServletRequest request) throws Exception {
		
		ChannelPO channelPO = channelDao.findOne(channelId);
		if (channelPO == null) return null;
		
		if (channelPO.getBroadWay().equals(BroadWay.ABILITY_BROAD.getName())) {
			return channelService.stopAbilityBroadcast(channelId);
		}else {
			return channelService.stopTerminalBroadcast(channelId);
		}
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/status")
	public Object broadcastStatus(Long channelId, HttpServletRequest request) throws Exception {

		return channelService.getChannelBroadstatus(channelId);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/restart")
	public Object broadcastRestart(Long channelId, HttpServletRequest request) throws Exception {

		return channelService.restartBroadcast(channelId);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broad/test")
	public Object broadTest(Long channelId, HttpServletRequest request) throws Exception {
		
		channelService.testBroad(channelId);
		
		return null;
	}
}
