package com.sumavision.tetris.cs.schedule.api.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.cs.program.ProgramService;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.cs.schedule.ScheduleService;
import com.sumavision.tetris.cs.schedule.ScheduleVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/cs/schedule")
public class ApiServerScheduleController {
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ProgramService programService;
	
	/**
	 * 添加排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param channelId 频道id
	 * @param broadData 播发日期
	 * @return ScheduleVO 排期信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(Long channelId, String schedules, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (schedules == null) return null;
		
		List<ApiServerScheduleVO> scheduleList = JSON.parseArray(schedules, ApiServerScheduleVO.class);
		
		return scheduleService.addSchedules(channelId, scheduleList);
	}
	
	/**
	 * 删除排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param scheduleId 排期id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(String ids, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		List<Long> scheduleIds = JSON.parseArray(ids, Long.class);
		
		return scheduleService.removeInBatch(scheduleIds);
	}
	
	/**
	 * 单排期修改<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param scheduleId 排期id
	 * @param broadDate 播发日期
	 * @param remark 备注
	 * @return ScheduleVO 排期信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(Long id, String schedule, String remark, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (schedule == null) return null;

		ApiServerScheduleVO apiServerScheduleVO = JSON.parseObject(schedule, ApiServerScheduleVO.class);
		
		ScheduleVO scheduleVO = scheduleService.edit(id, apiServerScheduleVO.getBroadDate(), null, remark);
		
		Date date = new Date();
		List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
		List<ScreenVO> assetScreens = apiServerScheduleVO.getAssetScreens();
		for (int i = 0; i < assetScreens.size(); i++) {
			ScreenVO screen = new ScreenVO();
			screen.setUpdateTime(date);
			screen.setSerialNum(1l);
			screen.setIndex((long)(i+1));
			screen.setPreviewUrl(assetScreens.get(i).getPreviewUrl());
			screenVOs.add(screen);
		}
		ProgramVO program = new ProgramVO();
		program.setScheduleId(scheduleVO.getId());
		program.setScreenNum(1l);
		program.setUpdateTime(date);
		program.setScreenInfo(screenVOs);
		
		scheduleVO.setProgram(programService.setProgram(program));
		
		return scheduleVO;
	}
	
	/**
	 * 获取排期列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param Long channelId 频道id
	 * @param int currentPage 分页当前页
	 * @param int pageSize 分页大小
	 * @return total 列表总数
	 * @return List<ScheduleVO> 排期信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get")
	public Object get(Long channelId, int currentPage, int pageSize, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		return scheduleQuery.getByChannelId(channelId, currentPage, pageSize);
	}
}
