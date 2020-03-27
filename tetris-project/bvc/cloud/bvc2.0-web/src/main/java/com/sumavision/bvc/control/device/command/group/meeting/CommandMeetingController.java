package com.sumavision.bvc.control.device.command.group.meeting;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.control.utils.TreeUtils;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.cooperate.CommandCooperateServiceImpl;
import com.sumavision.bvc.device.command.exception.CommandGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/meeting")
public class CommandMeetingController {
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private CommandMeetingSpeakServiceImpl commandMeetingSpeakServiceImpl;
	
	@Autowired
	private CommandCooperateServiceImpl commandCooperateServiceImpl;
	
	@Autowired
	private UserUtils userUtils;

	@Autowired
	private TreeUtils treeUtils;
	
	/**
	 * 新建会议<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年9月26日
	 * @param userIdList json格式的用户id列表
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save")
	public Object save(
			String members,
			String name,
			HttpServletRequest request) throws Exception{
		//考虑区分创建者和主席
		UserVO user = userUtils.getUserFromSession(request);
		
		Date date = new Date();
		String createTime = DateUtil.format(date, DateUtil.dateTimePattern);
		//未输入则生成一个name
		if(name==null || name.equals("")){
			name = new StringBuilder().append(user.getName())
				   .append(" ")
				   .append(createTime)
				   .append(" ")
				   .append("会议")
				   .toString();
		}
		
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		
		CommandGroupPO group = null;
		try{
			group = commandBasicServiceImpl.save(user.getId(), user.getId(), user.getName(), name, GroupType.MEETING, userIdArray);
		}catch(CommandGroupNameAlreadyExistedException e){
			//重名
			JSONObject info = new JSONObject();
			info.put("status", "error");
			JSONObject errorInfo = new JSONObject();
			errorInfo.put("type", "");
			errorInfo.put("msg", "名称已被使用");
			errorInfo.put("recommendedName", e.getRecommendedName());
			info.put("errorInfo", errorInfo);
			return info;
		}
		
		JSONObject info = new JSONObject();
		info.put("id", group.getId().toString());
		info.put("name", group.getName());
		info.put("status", "状态得扩展");
		info.put("commander", group.getUserId());
		info.put("creator", group.getUserId());
		Object membersArray = treeUtils.queryGroupTree(group.getId());
		info.put("members", membersArray);
				
		return info;
	}
	
	/**
	 * 指定发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:38:45
	 * @param id
	 * @param userIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/speak/appoint")
	public Object speakAppoint(
			String id,
			String userIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		commandMeetingSpeakServiceImpl.speakAppoint(user.getId(), Long.parseLong(id), userIdArray);
		
		return null;
	}
	
	/**
	 * 申请发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:40:00
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/speak/apply")
	public Object speakApplyAgree(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandMeetingSpeakServiceImpl.speakApply(userId, Long.parseLong(id));
		
		return null;
	}
	
	/**
	 * 主席同意成员的申请发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:42:58
	 * @param id
	 * @param userIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/speak/apply/agree")
	public Object speakApplyAgree(
			String id,
			String userIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		commandMeetingSpeakServiceImpl.speakApplyAgree(user.getId(), Long.parseLong(id), userIdArray);
		
		return null;
	}
	
	/**
	 * 主席拒绝成员的申请发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:43:10
	 * @param id
	 * @param userIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/speak/apply/disagree")
	public Object speakApplyDisagree(
			String id,
			String userIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		commandMeetingSpeakServiceImpl.speakApplyDisagree(user.getId(), Long.parseLong(id), userIdArray);
		
		return null;
	}
	
	/**
	 * 成员停止发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:45:40
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/speak/stop/by/member")
	public Object speakStopByMember(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandMeetingSpeakServiceImpl.speakStopByMember(userId, Long.parseLong(id));
		
		return null;
	}
	
	/**
	 * 主席停止多个成员发言<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:49:36
	 * @param id
	 * @param userIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/speak/stop/by/chairman")
	public Object speakStopByChairman(
			String id,
			String userIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		commandMeetingSpeakServiceImpl.speakStopByChairman(user.getId(), Long.parseLong(id), userIdArray);
		
		return null;
	}
	
	/**
	 * 开始讨论（全员互看）<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:52:11
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/discuss/start")
	public Object discussStart(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandMeetingSpeakServiceImpl.discussStart(userId, Long.parseLong(id));
		
		return null;
	}
	
	/**
	 * 停止讨论<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月27日 上午8:52:38
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/discuss/stop")
	public Object discussStop(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandMeetingSpeakServiceImpl.discussStop(userId, Long.parseLong(id));
		
		return null;
	}
	
}
