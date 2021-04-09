package com.sumavision.bvc.control.device.command.group.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.command.group.vo.BusinessPlayerVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.common.CommandCommonConstant;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/command/voice/intercom")
public class CommandUserVoiceController {
	
	/** 发起业务时，synchronized锁的前缀 */
	private static final String lockStartPrefix = "controller-vod-or-call-userId-";
	
	/** 响应、停止业务时，synchronized锁的前缀 */
	private static final String lockProcessPrefix = "controller-vod-or-call-businessId-";
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private ResourceService resourceService;

	/**
	 * 语音对讲<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 上午10:52:38
	 * @param Long userId 被对讲用户 id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start")
	public Object start(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		Long callUserId = userUtils.getUserIdFromSession(request);
		synchronized (new StringBuffer().append(lockStartPrefix).append(callUserId).toString().intern()) {
			/*UserBO callUser = userUtils.queryUserById(callUserId);
			UserBO calledUser = userUtils.queryUserById(userId);
			
			CommandGroupUserPlayerPO player = commandUserServiceImpl.userVoiceUser(callUser, calledUser, -1);
			
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			
			return _player;*/
			return null;
		}
	}
	
	/**
	 * 指定播放器，语音对讲<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:48:21
	 * @param userId 被对讲用户 id
	 * @param serial 播放器序号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/player")
	public Object startSpecifyPlayer(
			Long userId,
			int serial,
			HttpServletRequest request) throws Exception{
	
		/*Long callUserId = userUtils.getUserIdFromSession(request);
		synchronized (new StringBuffer().append(lockStartPrefix).append(callUserId).toString().intern()) {
			UserBO callUser = userUtils.queryUserById(callUserId);
			UserBO calledUser = userUtils.queryUserById(userId);
			
			CommandGroupUserPlayerPO player = commandUserServiceImpl.userVoiceUser(callUser, calledUser, serial);
			
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			
			return _player;
		}*/
		return null;
	}
	
	/**
	 * 批量语音对讲<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 下午5:19:48
	 * @param userIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/batch")
	public Object startBatch(
			String userIds,
			HttpServletRequest request) throws Exception{
		
		/*Long callUserId = userUtils.getUserIdFromSession(request);
		synchronized (new StringBuffer().append(lockStartPrefix).append(callUserId).toString().intern()) {
			UserBO callUser = userUtils.queryUserById(callUserId);
			
			List<Long> userIdList = JSON.parseArray(userIds, Long.class);
			List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
			
			for(Long userId : userIdList){
				UserBO calledUser = userUtils.queryUserById(userId);
				try{
					CommandGroupUserPlayerPO player = commandUserServiceImpl.userVoiceUser(callUser, calledUser, -1);
					BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
					playerVOs.add(_player);
				}catch(Exception e){
					log.info(callUser.getName() + "一键语音呼叫用户 " + userIds + " 部分失败，失败userId: " + userId);
					e.printStackTrace();
				}
			}
	
			return playerVOs;
		}*/
		return null;
	}
	
	/**
	 * <p>同意语音对讲</p>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 下午2:27:29
	 * @param Long businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/accept")
	public Object accept(
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
			Long callUserId = userUtils.getUserIdFromSession(request);
			UserBO callUser = userUtils.queryUserById(callUserId);
			
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			CommandGroupUserPlayerPO player = commandUserServiceImpl.acceptVoice(callUser, businessId, admin);
			
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			
			return _player;
		}
	}
	
	/**
	 * 拒绝语音对讲<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 下午2:28:29
	 * @param Long businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/refuse")
	public Object refuse(
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
			Long callUserId = userUtils.getUserIdFromSession(request);
			UserBO callUser = userUtils.queryUserById(callUserId);
			
//			commandUserServiceImpl.refuseVoice(callUser, businessId);
			
			return null;
		}
	}
	
	/**
	 * 停止语音对讲<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 下午2:29:19
	 * @param Long businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
			/*Long userId = userUtils.getUserIdFromSession(request);
			UserBO user = userUtils.queryUserById(userId);
			
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			CommandGroupUserPlayerPO player = commandUserServiceImpl.stopVoice(user, businessId, admin);
			
			return new HashMapWrapper<String, Object>().put("serial", player.getLocationIndex())
													   .getMap();*/
			return null;
		}
	}
	
}
