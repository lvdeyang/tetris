package com.sumavision.bvc.control.device.monitor.live;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.monitor.live.MonitorUserCallDAO;
import com.sumavision.bvc.device.monitor.live.MonitorUserCallService;
import com.sumavision.bvc.device.monitor.live.call.MonitorLiveCallDAO;
import com.sumavision.bvc.device.monitor.live.call.MonitorLiveCallPO;
import com.sumavision.bvc.device.monitor.live.call.MonitorLiveCallService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/user/call")
public class MonitorUserCallController {
	
	@Autowired
	private MonitorUserCallService monitorUserCallService;
	
	@Autowired
	private MonitorLiveCallDAO monitorLiveCallDao;
	
	@Autowired
	private MonitorLiveCallService monitorLiveCallService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorUserCallDAO monitorUserCallDao;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 分页查询外部用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午2:12:51
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<ExternalUserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/external/users")
	public Object loadExternalUsers(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		List<ExternalUserVO> rows = new ArrayList<ExternalUserVO>();
		
		List<UserBO> ldapUsers = resourceService.queryLdapUserWithEncoderDecoder();
		if(ldapUsers!=null && ldapUsers.size()>0){
			for(UserBO ldapUser:ldapUsers){
				rows.add(new ExternalUserVO().setName(ldapUser.getName()).setUsername(ldapUser.getUserNo()).setLogined(ldapUser.isLogined()));
			}
		}
		
		int total = rows.size();
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 查询当前用户的通话任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午2:56:07
	 * @return MonitorUserCallVO 通话任务
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<MonitorLiveCallPO> tasks = monitorLiveCallDao.findByCallUserIdOrCalledUserId(userId);
		
		if(tasks==null || tasks.size()<=0) return null;
		
		//取第一显示
		MonitorLiveCallPO task = tasks.get(0);
		String targetUser = null;
		if(task.getCallUserId().equals(userId)){
			targetUser = task.getCalledUsername();
		}else{
			targetUser = task.getCallUsername();
		}
		
		return new MonitorUserCallVO().setId(task.getId()).setTargetUsername(targetUser);
	}
	
	/**
	 * 业务系统发起用户双向通话<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午2:19:33
	 * @param String targetUser 目标（xt）用户号码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start")
	public Object start(
			String targetUser,
			HttpServletRequest request) throws Exception{
		
		Long localUserId = userUtils.getUserIdFromSession(request);
		
		UserBO localUser = userUtils.queryUserById(localUserId);
		
		UserBO xtUser = userUtils.queryUserByUserno(targetUser);
		
		MonitorLiveCallPO live = null;
		
		if("ldap".equals(xtUser.getCreater())){
			live = monitorLiveCallService.startLocalCallXt(xtUser, localUser);
		}else{
			live = monitorLiveCallService.startLocalCallLocal(xtUser, localUser);
		}
		
		String targetUserName = null;
		if(live.getCallUserId().equals(localUserId)){
			targetUserName = live.getCalledUsername();
		}else{
			targetUserName = live.getCallUsername();
		}
		
		return new MonitorUserCallVO().setId(live.getId()).setTargetUsername(targetUserName);
	}
	
	/**
	 * 业务停止主动发起的通话请求（未接听）<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午2:23:16
	 * @param String targetUser 目标（xt）用户号码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/top")
	public Object stop(
			String targetUser,
			HttpServletRequest request) throws Exception{
		
		UserVO localUser = userUtils.getUserFromSession(request);
		
		UserBO xtUser = userUtils.queryUserByUserno(targetUser);
		
		//monitorUserCallService.sendUserTwoSideCallStopWithPassBy(localUser.getId(), localUser.getName(), localUser.getUserno(), xtUser);
		
		return null;
	}
	
	/**
	 * 停止已经建立的通话<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午2:24:27
	 * @param @PathVariable Long id 通话任务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop/exist/{id}")
	public Object stopExist(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorLiveCallService.stop(id, userId);

		return null;
	}
	
}
