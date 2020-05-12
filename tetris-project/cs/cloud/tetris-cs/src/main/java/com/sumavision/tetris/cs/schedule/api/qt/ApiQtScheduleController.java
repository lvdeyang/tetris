package com.sumavision.tetris.cs.schedule.api.qt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityQuery;
import com.sumavision.tetris.cs.channel.broad.file.BroadFileService;
import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/qt/cs/schedule")
public class ApiQtScheduleController {
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private BroadFileService broadFileService;
	
	@Autowired
	private BroadAbilityQuery broadAbilityQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 根据id数列请求排期表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:50:10
	 * @param ids 排期id数列
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest")
	public Object questSchedules(Long channelId, HttpServletRequest request) throws Exception {
		String userIp = request.getHeader(HttpConstant.HEADER_REAL_IP_FROM_ZUUL);
		if (channelId == null) {
			UserVO userVO = userQuery.current();
			Long userId = userVO.getId();
			if (userId != null) {
				channelId = broadFileService.getChannelIdFromUser(userId);
				if (channelId == null) channelId = broadAbilityQuery.getChannelIdFromUser(userId);
			}
		}
		if (channelId == null) return null;
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		if (channel.getBroadWay().equals(BroadWay.FILE_DOWNLOAD_BROAD.getName())) {
			return broadFileService.getNewBroadJSON(channelId, false, false);
		}
//		return scheduleQuery.questSchedulesByChannelId(channelId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("schedules", scheduleQuery.questJSONSchedulesByChannelId(channelId, userIp));
		return jsonObject;
	}
}
