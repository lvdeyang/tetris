package com.sumavision.tetris.cs.channel.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.broad.ChannelServerType;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/channel/feign")
public class ApiChannelFeignController {
	@Autowired
	private ChannelService channelService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/alarm/reboot")
	public Object alarmRebootRecieve(String ip,HttpServletRequest request) throws Exception {
		System.out.println("推流能力重启");
		channelService.rebootServer(ChannelServerType.ABILITY_STREAM, ip);
		return null;
	}
}
