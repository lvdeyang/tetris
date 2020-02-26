package com.sumavision.bvc.control.device.command.group.setting;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.setting.AutoManualMode;
import com.sumavision.bvc.command.group.user.setting.CastMode;
import com.sumavision.bvc.command.group.user.setting.CommandGroupUserSettingPO;
import com.sumavision.bvc.command.group.user.setting.OpenCloseMode;
import com.sumavision.bvc.command.group.user.setting.SecretMode;
import com.sumavision.bvc.command.group.user.setting.VisibleRange;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserSettingVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.secret.CommandSecretServiceImpl;
import com.sumavision.bvc.device.command.user.setting.CommandSettingServiceImpl;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/settings")
public class CommandSettingController {

	@Autowired
	private UserUtils userUtils;
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	CommandBasicServiceImpl commandBasicServiceImpl;

	
	@Autowired
	CommandSecretServiceImpl commandSecretServiceImpl;

	
	@Autowired
	CommandSettingServiceImpl commandSettingServiceImpl;

	/**
	 * 查询所有设置<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:53:00
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/all")
	public Object querySetting(HttpServletRequest request) throws Exception {
		
		UserVO user = userUtils.getUserFromSession(request);
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		CommandGroupUserSettingPO setting = null;
		if(userInfo == null){
			setting = new CommandGroupUserSettingPO();
		}else{
			setting = userInfo.getSetting();
		}
		return new CommandGroupUserSettingVO().set(setting);
		
	}
	
	/**
	 * 设置呼叫应答方式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:44:51
	 * @param mode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/call/response/mode")
	public Object setResponseMode(
			String mode,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		commandSettingServiceImpl.setResponseMode(user.getId(), AutoManualMode.fromCode(mode));
		return null;
	}
	
	/**
	 * 设置单播/组播方式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:44:51
	 * @param mode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/vod/mode")
	public Object setVodMode(
			String mode,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		commandSettingServiceImpl.setVodMode(user.getId(), CastMode.fromCode(mode));
		return null;
	}
	
	/**
	 * 设置视频转发应答方式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:44:51
	 * @param mode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/video/send/answer/mode")
	public Object setSendAnswerMode(
			String mode,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		commandSettingServiceImpl.setSendAnswerMode(user.getId(), AutoManualMode.fromCode(mode));
		return null;
	}
	
	/**
	 * 设置启动模式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:44:51
	 * @param mode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/command/meeting/auto/start")
	public Object setCommandMeetingAutoStart(
			String mode,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		commandSettingServiceImpl.setCommandMeetingAutoStart(user.getId(), AutoManualMode.fromCode(mode));
		return null;
	}
	
	/**
	 * 设置字幕设置<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:44:51
	 * @param mode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/subtitle")
	public Object setSubtitle(
			String mode,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		commandSettingServiceImpl.setSubtitle(user.getId(), OpenCloseMode.fromCode(mode));
		return null;
	}
	
	/**
	 * 设置可见范围<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:44:51
	 * @param mode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/visible/range")
	public Object setVisibleRange(
			String mode,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		commandSettingServiceImpl.setVisibleRange(user.getId(), VisibleRange.fromCode(mode));
		return null;
	}
	
	/**
	 * 设置录像模式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:44:51
	 * @param mode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/record/mode")
	public Object setRecordMode(
			String mode,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		commandSettingServiceImpl.setRecordMode(user.getId(), AutoManualMode.fromCode(mode));
		return null;
	}
	
	/**
	 * 设置专向方式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:44:51
	 * @param mode
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/dedicated/mode")
	public Object setDedicatedMode(
			String mode,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		commandSettingServiceImpl.setDedicatedMode(user.getId(), SecretMode.fromCode(mode));
		return null;
	}
	
}
