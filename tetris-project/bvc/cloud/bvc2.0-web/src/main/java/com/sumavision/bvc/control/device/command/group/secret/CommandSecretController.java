package com.sumavision.bvc.control.device.command.group.secret;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.secret.CommandSecretServiceImpl;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/secret")
public class CommandSecretController {

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	CommandBasicServiceImpl commandBasicServiceImpl;

	
	@Autowired
	CommandSecretServiceImpl commandSecretServiceImpl;
	
	/**
	 * 发起专向会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 下午4:22:01
	 * @param id 会议id
	 * @param userId 目标用户id
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start")
	public Object start(
			String userId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		String commandString = commandCommonUtil.generateCommandString(GroupType.SECRET);
		String name = new StringBuilder()
				.append(user.getName())
				.append("发起的专向" + commandString)
				.toString();
		
		Object chairSplits = commandSecretServiceImpl.start(user.getId(), user.getName(), name, Long.parseLong(userId), -1);

		return chairSplits;
	}
	
	/**
	 * 指定播放器，发起专向会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午4:26:32
	 * @param userId 目标用户id
	 * @param serial 播放器序号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/player")
	public Object startSpecifyPlayer(
			String userId,
			int serial,
			HttpServletRequest request) throws Exception{
		
		throw new BaseException(StatusCode.FORBIDDEN, "请从通讯录中发起专向");
		
//		UserVO user = userUtils.getUserFromSession(request);
//		
//		String commandString = commandCommonUtil.generateCommandString(GroupType.SECRET);
//		String name = new StringBuilder()
//				.append(user.getName())
//				.append("发起的专向" + commandString)
//				.toString();
//		
//		Object chairSplits = commandSecretServiceImpl.start(user.getId(), user.getName(), name, Long.parseLong(userId), serial);
//
//		return chairSplits;
	}
	
	/**
	 * 停止专向会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午3:15:50
	 * @param id 会议id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(
			String id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		JSONObject split = commandSecretServiceImpl.stop(user.getId(), Long.parseLong(id));
		return split;
		
	}
	
	/**
	 * 成员同意加入专向会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午3:16:22
	 * @param id 会议id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/accept")
	public Object accept(
			String id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		Object split = commandSecretServiceImpl.accept(user.getId(), Long.parseLong(id));
		return split;
		
	}
	
	/**
	 * 成员拒绝加入专向会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午3:16:48
	 * @param id 会议id
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
		
		UserVO user = userUtils.getUserFromSession(request);
		
		commandSecretServiceImpl.refuse(user.getId(), Long.parseLong(id));
		return null;
		
	}
	
}
