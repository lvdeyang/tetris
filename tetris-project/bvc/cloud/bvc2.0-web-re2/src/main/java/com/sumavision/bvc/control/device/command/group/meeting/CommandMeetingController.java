package com.sumavision.bvc.control.device.command.group.meeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.tetris.bvc.business.group.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.OriginType;
import com.sumavision.bvc.control.utils.TreeUtils;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.cooperate.CommandCooperateServiceImpl;
import com.sumavision.bvc.device.command.exception.CommandGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.bvc.business.group.speak.GroupSpeakService;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/meeting")
public class CommandMeetingController {

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupSpeakService groupSpeakService;
	
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
	
	@Autowired
	private BusinessReturnService businessReturnService;

    @Autowired
    private CommandCommonUtil commandCommonUtil;
	
	/**
	 * 新建会议<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年9月26日
	  * @param members json格式的用户id列表
	 * @param hallIds json格式的会场id列表
	 * @param chairmanType null或self为默认，以创建人的用户作为主席；user指定用户为主席；hall指定会场为主席；tvosOfUser以创建人的机顶盒为主席
	 * @param chairmanId 用户id/会场id
	 * @param name 会议名称
	 * @param orderGroupType 预约会议类型BusinessOrderGroupType
	 * @param orderBeginTime 预约开始时间
	 * @param orderEndTime 预约结束时间
	 * @param groupLock 锁定会议 GroupLock
	 * @param Boolean temporary 是否是临时会议,true：临时会议
	 */
    @JsonBody
    @ResponseBody
    @RequestMapping(value = "/save")
    public Object save(
            String members,
            String hallIds,
            String chairmanType,
            String chairmanId,
            String name,
            String orderGroupType,
            String orderBeginTime,
            String orderEndTime,
            String groupLock,
            Boolean temporary,
            HttpServletRequest request) throws Exception{

        //throw new BaseException(StatusCode.FORBIDDEN, "暂不支持，请新建会议");


        UserVO user = userUtils.getUserFromSession(request);

        Date date = new Date();
        String createTime = DateUtil.format(date, DateUtil.dateTimePattern);
        //未输入则生成一个name
        if(name==null || name.equals("")){
            String commandString = commandCommonUtil.generateCommandString(GroupType.BASIC);
            name = new StringBuilder().append(user.getName())
                    .append(" ")
                    .append(createTime)
                    .append(" ")
                    .append(commandString)
                    .toString();
        }

        List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
        List<Long> hallIdArray = JSONArray.parseArray(hallIds, Long.class);
        List<String> bundleIdArray = new ArrayList<String>();

        GroupPO group = null;
        try{
        	if(temporary != null && temporary){
				group = groupService.saveAndStartTemporary(user.getId(), user.getName(), chairmanType, chairmanId, name, name, BusinessType.MEETING_BVC, com.sumavision.tetris.bvc.business.OriginType.INNER, userIdArray,
						hallIdArray, bundleIdArray, null, orderGroupType, orderBeginTime, orderEndTime, groupLock);
			}else {
				group = groupService.saveCommand_TS(user.getId(), user.getName(), chairmanType, chairmanId, name, name, BusinessType.MEETING_BVC, com.sumavision.tetris.bvc.business.OriginType.INNER, userIdArray,
						hallIdArray, bundleIdArray, null, orderGroupType, orderBeginTime, orderEndTime, groupLock,null);
			}
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
        info.put("status", group.getStatus().getCode());
        info.put("commander", group.getUserId());
        info.put("creator", group.getUserId());
        info.put("group", new GroupVO().set(group));
//		Object membersArray = treeUtils.queryGroupTree(group.getId());
//		info.put("members", membersArray);

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
		
		businessReturnService.init(Boolean.TRUE);
		groupSpeakService.speakAppointU(Long.parseLong(id), userIdArray);
		
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
	public Object speakApply(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		//测试传递的会议组id固定548
		businessReturnService.init(Boolean.TRUE);
		groupSpeakService.speakApply(userId, Long.parseLong(id));
		
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
		
		businessReturnService.init(Boolean.TRUE);
		groupSpeakService.speakApplyAgreeM(Long.parseLong(id), userIdArray);
		
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
	
		businessReturnService.init(Boolean.TRUE);
		groupSpeakService.speakApplyDisagreeM(user.getId(), Long.parseLong(id), userIdArray);
		
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
		
		businessReturnService.init(Boolean.TRUE);
		//groupSpeakService.speakStopByMember(userId, Long.parseLong(id));
		
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
		
		businessReturnService.init(Boolean.TRUE);
		groupSpeakService.speakStopByChairmanU(Long.parseLong(id), userIdArray);
		
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
		
		businessReturnService.init(Boolean.TRUE);
		groupSpeakService.discussStart(userId, Long.parseLong(id));
		
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
		
		businessReturnService.init(Boolean.TRUE);
		groupSpeakService.discussStop(userId, Long.parseLong(id));
		
		return null;
	}
	
}
