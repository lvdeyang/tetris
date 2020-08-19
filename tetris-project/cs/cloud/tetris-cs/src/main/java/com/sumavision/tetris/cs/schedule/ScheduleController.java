package com.sumavision.tetris.cs.schedule;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalBroadInfoService;
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
	
	@Autowired
	private BroadTerminalBroadInfoService broadTerminalBroadInfoService;
	
	@Autowired
	private ScheduleDAO scheduleDao;
	
	/**
	 * 添加排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param channelId 频道id
	 * @param broadDate 播发日期
	 * @param remark 备注
	 * @return ScheduleVO 排期信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(Long channelId, String broadDate, String endDate, String remark, boolean mono, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if(mono){
			broadDate = "default";
			SchedulePO schedule = scheduleDao.findByBroadDate(broadDate);
			if(schedule != null){
				throw new BaseException(StatusCode.FORBIDDEN, "已存在默认排期");
			}
		}
		
		return scheduleService.add(channelId, broadDate, endDate, remark);
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
	public Object remove(Long id, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		return scheduleService.remove(id);
	}
	
	/**
	 * 编辑排期<br/>
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
	public Object edit(Long id, String broadDate, String endDate, String remark, boolean mono, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if(mono){
			broadDate = "default";
			endDate = "";
			SchedulePO schedule = scheduleDao.findByBroadDateAndIdNot(broadDate, id);
			if(schedule != null){
				throw new BaseException(StatusCode.FORBIDDEN, "已存在默认排期");
			}
		}
		
		return scheduleService.edit(id, broadDate, endDate, remark);
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
	
	/**
	 * 设置排期单总停止时间(目前仅终端播发使用，用于周期播放)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 上午10:25:28
	 * @param Long channelId 频道id
	 * @param String endDate 停止时间
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/total/endTime")
	public Object setTotalEndTime(Long channelId, String endDate, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		return broadTerminalBroadInfoService.setEndDate(channelId, endDate);
	}
}
