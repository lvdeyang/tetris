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
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.command.group.vo.BusinessPlayerVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.tetris.bvc.business.call.UserCallService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户呼叫controller<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月16日 下午1:25:52
 */
@Slf4j
@Controller
@RequestMapping(value = "/command/call/user")
public class CommandUserCallController {
	
	/** 发起业务时，synchronized锁的前缀 */
	private static final String lockStartPrefix = "controller-vod-or-call-userId-";
	
	/** 响应、停止业务时，synchronized锁的前缀 */
	private static final String lockProcessPrefix = "controller-vod-or-call-businessId-";
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private UserCallService userCallService;
	
	/**
	 * 呼叫用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月16日 下午1:57:05
	 * @param Long userId 被呼叫用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start")
	public Object start(
			Long userId,
			HttpServletRequest request) throws Exception{

		Long callUserId = userUtils.getUserIdFromSession(request);
		synchronized (new StringBuffer().append(lockStartPrefix).append(callUserId).toString().intern()) {
			UserBO callUser = userUtils.queryUserById(callUserId);
			UserBO calledUser = userUtils.queryUserById(userId);
			
			userCallService.userCallUser(callUser, calledUser, -1, null, null);
			
//			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			BusinessPlayerVO _player = new BusinessPlayerVO();
	
			return _player;
		}
	}
	
	/**
	 * 指定播放器，呼叫用户<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:49:32
	 * @param userId 被呼叫用户id
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
		
		Long callUserId = userUtils.getUserIdFromSession(request);
		synchronized (new StringBuffer().append(lockStartPrefix).append(callUserId).toString().intern()) {
			UserBO callUser = userUtils.queryUserById(callUserId);
			UserBO calledUser = userUtils.queryUserById(userId);
			
			CommandGroupUserPlayerPO player = commandUserServiceImpl.userCallUser_Cascade(callUser, calledUser, serial, null);
			
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
		
			return _player;
		}
	}

	/**
	 * 批量呼叫用户<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 下午5:19:07
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
		
		Long callUserId = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(callUserId).toString().intern()) {
			
			UserBO callUser = userUtils.queryUserById(callUserId);
			
			List<Long> userIdList = JSON.parseArray(userIds, Long.class);
			List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
			
			for(Long userId : userIdList){
				UserBO calledUser = userUtils.queryUserById(userId);
				try{
					userCallService.userCallUser(callUser, calledUser, -1, null, null);
//					CommandGroupUserPlayerPO player = commandUserServiceImpl.userCallUser_Cascade(callUser, calledUser, -1, null);
//					BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
//					playerVOs.add(_player);
				}catch(Exception e){
					log.info(callUser.getName() + "一键呼叫用户 " + userIds + " 部分失败，失败userId: " + userId);
					e.printStackTrace();
				}
			}
	
			return playerVOs;
		}
	}
	
	/**
	 * 将点播用户转为呼叫用户<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月30日 下午4:29:10
	 * @param businessId
	 * @param serial
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/trans/vod/to/call")
	public Object transVodToCall(
			String businessId,
			int serial,
			HttpServletRequest request) throws Exception{
		
		throw new BaseException(StatusCode.FORBIDDEN, "暂不支持");
		/*
		Long callUserId = userUtils.getUserIdFromSession(request);
		synchronized (new StringBuffer().append(lockStartPrefix).append(callUserId).toString().intern()) {
			
			CommandGroupUserPlayerPO player = commandUserServiceImpl.transVodToCall(Long.parseLong(businessId), serial);
			
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
		
			return _player;
		}*/
	}
	
	/**
	 * 同意呼叫<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午10:32:16
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
			
			UserBO admin = new UserBO(); admin.setId(-1L);
			
//			CommandGroupUserPlayerPO player = commandUserServiceImpl.acceptCall_Cascade(callUser, businessId, null);
			
			userCallService.acceptCall(callUser, businessId, null);//.acceptC(callUser, calledUser, -1, null, null);
			
//			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			BusinessPlayerVO _player = new BusinessPlayerVO();
			
			return _player;
		}
	}
	
	/**
	 * 拒绝呼叫<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午10:32:16
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
			
//			commandUserServiceImpl.stopCall_Cascade(callUser, businessId, null);
			userCallService.stopCall(callUser, businessId, null);
			
			return null;
		}
	}
	
	/**
	 * 停止呼叫<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午10:32:16
	 * @param Long businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
			Long userId = userUtils.getUserIdFromSession(request);
			UserBO user = userUtils.queryUserById(userId);
			
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
//			UserBO admin = new UserBO(); 
//			admin.setId(-1L);
			
//			CommandGroupUserPlayerPO player = commandUserServiceImpl.stopCall_Cascade(user, businessId, null);
			userCallService.stopCall(user, businessId, null);
			
			return new HashMapWrapper<String, Object>().put("serial", 111)//player.getLocationIndex())
													   .getMap();
		}
	}
	
}
