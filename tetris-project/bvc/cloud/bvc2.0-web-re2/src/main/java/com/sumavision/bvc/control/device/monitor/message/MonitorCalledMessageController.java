package com.sumavision.bvc.control.device.monitor.message;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.monitor.message.MonitorCalledMessageDAO;
import com.sumavision.bvc.device.monitor.message.MonitorCalledMessagePO;
import com.sumavision.bvc.device.monitor.message.MonitorCalledMessageService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/user/called/message")
public class MonitorCalledMessageController {

	@Autowired
	private MonitorCalledMessageDAO monitorCalledMessageDao;
	
	@Autowired
	private MonitorCalledMessageService monitorCalledMessageService;
	
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * 查询用户收到的双向通信消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午9:06:54
	 * @return MonitorCalledMessageVO 消息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		MonitorCalledMessagePO message = monitorCalledMessageDao.findByReceiveUser(user.getName());
		
		if(message == null) return null;
		
		return new MonitorCalledMessageVO().set(message);
	}
	
	/**
	 * 被叫用户接受通话请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午9:32:55
	 * @param @PathVariable Long id 被叫消息id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/accept/{id}")
	public Object accept(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorCalledMessageService.accept(id, userId);
		
		return null;
	}
	
	/**
	 * 被叫用户拒绝通话请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午9:32:55
	 * @param @PathVariable Long id 被叫消息id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/refuse/{id}")
	public Object refuse(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorCalledMessageService.refuse(id, userId);
		
		return null;
	}
	
}
