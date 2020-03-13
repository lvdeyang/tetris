package com.sumavision.tetris.agent.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.agent.service.TerminalAgentService;
import com.sumavision.tetris.agent.vo.PassByVO;
import com.sumavision.tetris.agent.vo.ResponseResourceVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

@Controller
@RequestMapping(value = "/api/zoom/jv220/terminal/agent")
public class TerminalAgentController {
	
	@Autowired
	private TerminalAgentService terminalAgentService;

	/**
	 * 获取能看的资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午9:06:02
	 * @param String identify_id http标识
	 * @param String local_user_no 用户号码
	 * @return ResponseResourceVO 资源信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/resource")
	public Object queryResource(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String identify_id = requestWrapper.getString("identify_id");
		String local_user_no = requestWrapper.getString("local_user_no");
		
		ResponseResourceVO response = terminalAgentService.queryResources();
		response.setIdentify_id(identify_id)
		   		.setOperate("get_auth_resources")
		   		.setLocal_user_no(local_user_no);

		return response;
	}
	
	/**
	 * Jv220拉人入会<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午3:33:18
	 * @param String identify_id http标识
	 * @param String local_user_no 用户号码
	 * @param String remote_user_no 对方用户号码
	 * @return PassByVO 协议
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/call/user")
	public Object callUser(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String identify_id = requestWrapper.getString("identify_id");
		String local_user_no = requestWrapper.getString("local_user_no");
		String remote_user_no = requestWrapper.getString("remote_user_no");

		PassByVO passBy = terminalAgentService.callUser(remote_user_no);
		passBy.setIdentify_id(identify_id);
		
		return passBy;
	}
	
	/**
	 * Jv220加入会议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午3:43:22
	 * @param String identify_id http标识
	 * @param String local_user_no 用户号码
	 * @param String remote_meeting_no 会议号码
	 * @return PassByVO 协议
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/call/meeting")
	public Object callMeeting(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String identify_id = requestWrapper.getString("identify_id");
		String local_user_no = requestWrapper.getString("local_user_no");
		String remote_meeting_no = requestWrapper.getString("remote_meeting_no");
		
		PassByVO passBy = terminalAgentService.callMeeting(remote_meeting_no);
		passBy.setIdentify_id(identify_id);
		
		return passBy;
	}
	
	/**
	 * jv220退会<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 上午9:06:13
	 * @param String identify_id http标识
	 * @param String local_user_no 用户号码
	 * @param String remote_meeting_no 会议号码
	 * @param String local_user_identify 用户在会议中标识
	 * @return Map<String, String> 标识
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/exit/meeting")
	public Object exitMeeting(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String identify_id = requestWrapper.getString("identify_id");
		String local_user_no = requestWrapper.getString("local_user_no");
		String remote_meeting_no = requestWrapper.getString("remote_meeting_no");
		String local_user_identify = requestWrapper.getString("local_user_identify");
		
		terminalAgentService.exit(Long.valueOf(local_user_identify));
		
		return new HashMapWrapper<String, String>().put("identify_id", identify_id)
				 								   .put("operate", "220_bye_meeting")
				 								   .getMap();
	}
	
	/**
	 * jv220挂断<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 上午9:16:40
	 * @param String identify_id http标识
	 * @param String local_user_no 用户号码
	 * @return Map<String, String> 标识
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bye")
	public Object bye(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String identify_id = requestWrapper.getString("identify_id");
		String local_user_no = requestWrapper.getString("local_user_no");

		terminalAgentService.bye(local_user_no);
		
		return new HashMapWrapper<String, String>().put("identify_id", identify_id)
				 								   .put("operate", "220_bye_user")
				 								   .getMap();
		
	}
	
	/**
	 * jv220恢复<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 上午9:34:15
	 * @param String identify_id http标识
	 * @param String local_user_no 用户号码
	 * @return List<PassByVO> 协议
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resume")
	public Object resume(HttpServletRequest request) throws Exception{
		
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String identify_id = requestWrapper.getString("identify_id");
		String local_user_no = requestWrapper.getString("local_user_no");
		
		List<PassByVO> passBys = terminalAgentService.resume(local_user_no);
		for(PassByVO passBy: passBys){
			passBy.setIdentify_id(identify_id);
		}
		
		return passBys;
	}
}
