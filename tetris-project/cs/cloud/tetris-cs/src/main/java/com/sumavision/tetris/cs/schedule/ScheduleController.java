package com.sumavision.tetris.cs.schedule;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cs/schedule")
public class ScheduleController {
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(Long channelId, String broadDate, String remark, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		return scheduleService.add(channelId, broadDate, remark);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(Long id, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		return scheduleService.remove(id);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(Long id, String broadDate, String remark, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		return scheduleService.edit(id, broadDate, remark);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get")
	public Object get(Long channelId, int currentPage, int pageSize, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		return scheduleQuery.getByChannelId(channelId, currentPage, pageSize);
	}
}
