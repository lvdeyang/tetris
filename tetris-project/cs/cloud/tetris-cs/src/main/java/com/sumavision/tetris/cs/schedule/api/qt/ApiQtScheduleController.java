package com.sumavision.tetris.cs.schedule.api.qt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/qt/cs/schedule")
public class ApiQtScheduleController {
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
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
		if (channelId == null) return null;
		
		return scheduleQuery.questSchedulesByChannelId(channelId);
	}
}
