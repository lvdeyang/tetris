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
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/meeting")
public class CommandMeetingController {
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
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
	 * 发起协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月29日 上午11:04:50
	 * @param id 指挥id
	 * @param members 协同成员
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/grant")
	public Object start(
			String id,
			String userIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		commandCooperateServiceImpl.start(Long.parseLong(id), userIdArray);
		
		return null;
	}
	
	/**
	 * 同意协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:29:44
	 * @param id 指挥id
	 * @return
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/agree")
	public Object agree(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandCooperateServiceImpl.agree(userId, Long.parseLong(id));
		
		return null;
	}
	
	/**
	 * 拒绝协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:34:04
	 * @param id 指挥id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/refuse")
	public Object refuse(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandCooperateServiceImpl.refuse(userId, Long.parseLong(id));
		
		return null;
	}
	
	/**
	 * 撤销授权协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:36:49
	 * @param id 指挥id
	 * @param businessId 指挥id-成员id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/revoke")
	public Object revoke(
			String id,
			String businessId,
			HttpServletRequest request) throws Exception{
		
		String userIdString = businessId.split("-")[1];
		
		commandCooperateServiceImpl.revoke(Long.parseLong(userIdString), Long.parseLong(id));
		
		return null;
	}
	
}
