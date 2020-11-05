package com.sumavision.bvc.control.device.command.emergent.broadcast;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.api.controller.EmergentAlarmVO;
import com.sumavision.bvc.command.emergent.broadcast.CommandBroadcastAlarmPO;
import com.sumavision.bvc.command.emergent.broadcast.CommandBroadcastSpeakPO;
import com.sumavision.bvc.command.group.dao.CommandBroadcastAlarmDAO;
import com.sumavision.bvc.device.command.emergent.broadcast.CommandEmergentBroadcastServiceImpl;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/emergent/broadcast")
public class CommandEmergentBroadcastController {

	@Autowired
	private CommandBroadcastAlarmDAO commandBroadcastAlarmDao;

	@Autowired
	private CommandEmergentBroadcastServiceImpl commandEmergentBroadcastServiceImpl;
		
	/**
	 * 开始喊话<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午10:46:14
	 * @param bundleIds
	 * @param eventLevel 事件级别 0一般，1较大，2，重大，3特别重大
	 * @param eventType 事件类型，参考《国家应急平台体系信息资源分类与编码规范》，默认使用11000（自然灾害）
	 * @param unifiedId 消息预警唯一标识
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/speak/start")
	public Object speakStart(
			String bundleIds,
			int eventLevel,
			String eventType,
			String unifiedId,
			HttpServletRequest request) throws Exception{
		
		List<String> bundleIdsList = JSONArray.parseArray(bundleIds, String.class);
		
		CommandBroadcastSpeakPO speak = commandEmergentBroadcastServiceImpl.speakStart(bundleIdsList, eventLevel, eventType, unifiedId);		
		//此时列表中应该有新增的喊话任务
		
		JSONObject list = commandEmergentBroadcastServiceImpl.queryAllSpeakList();
		JSONObject result = new JSONObject();
		result.put("id", speak.getId().toString());
		result.put("targetUdp", speak.getTargetUdp());
		list.put("result", result);
		
		return list;
	}
	
	/**
	 * 停止喊话<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午10:46:38
	 * @param serial
	 * @param direction
	 * @param speed
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/speak/stop")
	public Object speakStop(
			String id,
			HttpServletRequest request) throws Exception{
		
		commandEmergentBroadcastServiceImpl.speakStop(Long.parseLong(id));		
		return null;
	}
	
	/**
	 * 查询喊话列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午10:46:59
	 * @param serial
	 * @param direction
	 * @param speed
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/speak/list")
	public Object querySpeakList(
			HttpServletRequest request) throws Exception{
		
		JSONObject list = commandEmergentBroadcastServiceImpl.queryAllSpeakList();
		
		return list;
	}
	
	/**
	 * 询所有记录的告警信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月22日 下午4:44:47
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/alarm/list")
	public Object queryAlarmList(
			HttpServletRequest request) throws Exception{
		
		List<CommandBroadcastAlarmPO> alarms = commandBroadcastAlarmDao.findAll();
		List<EmergentAlarmVO> alarmVOs = new ArrayList<EmergentAlarmVO>();
		for(CommandBroadcastAlarmPO alarm : alarms){
			EmergentAlarmVO alarmVO = new EmergentAlarmVO().set(alarm);
			alarmVOs.add(alarmVO);
		}
		
		return alarmVOs;
	}
	
	/**
	 * 批量删除告警记录<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月22日 下午3:56:39
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/alarms")
	public Object deleteAlarms(
			String ids,
			HttpServletRequest request) throws Exception{
		
		List<Long> idArray = JSONArray.parseArray(ids, Long.class);
		commandEmergentBroadcastServiceImpl.deleteAlarmRecords(idArray);
		
		return null;
	}
	
}
