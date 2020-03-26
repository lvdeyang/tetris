package com.sumavision.bvc.control.device.command.group.vod;

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
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/command/vod")
public class CommandVodController {
	
	/** 发起业务时，synchronized锁的前缀 */
	private static final String lockStartPrefix = "controller-vod-or-call-userId-";
	
	/** 响应、停止业务时，synchronized锁的前缀 */
	private static final String lockProcessPrefix = "controller-vod-or-call-businessId-";
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private CommandVodService commandVodService;
	
	@Autowired
	private ResourceService resourceService;

	/**
	 * 点播文件资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午10:41:01
	 * @param String resourceFileId 资源文件id
	 * @return BusinessPlayerVO 播放器业务信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/file/start")
	public Object resourceFileStart(
			String resourceFileId,
			HttpServletRequest request) throws Exception{
			
		Long userId = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(userId).toString().intern()) {
			UserBO user = userUtils.queryUserById(userId);
			CommandGroupUserPlayerPO player = commandVodService.resourceVodStart(user, resourceFileId, -1);
			
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			
			return _player;
		}		
	}
	
	/**
	 * 指定播放器，点播文件资源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:41:16
	 * @param resourceFileId 资源文件id
	 * @param serial 播放器序号
	 * @param request
	 * @return BusinessPlayerVO 播放器业务信息
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/file/start/player")
	public Object resourceFileStartSpecifyPlayer(
			String resourceFileId,
			int serial,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(userId).toString().intern()) {
			UserBO user = userUtils.queryUserById(userId);
			CommandGroupUserPlayerPO player = commandVodService.resourceVodStart(user, resourceFileId, serial);
			
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			
			return _player;
		}
	}

	/**
	 * 批量点播文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 下午5:22:23
	 * @param resourceFileIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/file/start/batch")
	public Object resourceFileStartBatch(
			String resourceFileIds,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(userId).toString().intern()) {
			UserBO user = userUtils.queryUserById(userId);
			
			List<String> resourceFileIdList = JSON.parseArray(resourceFileIds, String.class);
			List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
			for(String resourceFileId : resourceFileIdList){			
				try{
					CommandGroupUserPlayerPO player = commandVodService.resourceVodStart(user, resourceFileId, -1);
					BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
					playerVOs.add(_player);
				}catch(Exception e){
					log.info(user.getName() + "一键点播文件 " + resourceFileIds + " 部分失败，失败resourceFileId: " + resourceFileId);
					e.printStackTrace();
				}
			}
			
			return playerVOs;
		}
	}
	
	/**
	 * 停止点播文件资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:05:18
	 * @param String businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/file/stop")
	public Object resourceFileStop(
			String businessId,
			HttpServletRequest request) throws Exception{
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(businessId).toString().intern()) {
			Long userId = userUtils.getUserIdFromSession(request);
			UserBO user = userUtils.queryUserById(userId);
			
			CommandGroupUserPlayerPO player = commandVodService.resourceVodStop(user, businessId);
			
			return new HashMapWrapper<String, Object>().put("serial", player.getLocationIndex())
					   								   .getMap();
		}
	}
	
	/**
	 * 点播用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 上午9:29:16
	 * @param userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/start")
	public Object userStart(
			Long userId,
			HttpServletRequest request) throws Exception{		
		
		Long id = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(id).toString().intern()) {
		
			UserBO user = userUtils.queryUserById(id);
			UserBO vodUser = userUtils.queryUserById(userId);
		
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			CommandGroupUserPlayerPO player = commandVodService.userStart(user, vodUser, admin, -1);
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			
			return _player;		
		}
	}

	/**
	 * 指定播放器，点播用户<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午9:29:16
	 * @param userId
	 * @param serial 播放器序号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/start/player")
	public Object userStartSpecifyPlayer(
			Long userId,
			int serial,
			HttpServletRequest request) throws Exception{
		
		Long id = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);
			UserBO vodUser = userUtils.queryUserById(userId);
		
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			CommandGroupUserPlayerPO player = commandVodService.userStart(user, vodUser, admin, serial);
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			
			return _player;
		}
	}

	/**
	 * 批量点播用户<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 下午5:22:48
	 * @param userIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/start/batch")
	public Object userStartBatch(
			String userIds,
			HttpServletRequest request) throws Exception{
		
		Long id = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);
			
			List<Long> userIdList = JSON.parseArray(userIds, Long.class);
			List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			for(Long userId : userIdList){
				UserBO vodUser = userUtils.queryUserById(userId);
				try{
					CommandGroupUserPlayerPO player = commandVodService.userStart(user, vodUser, admin, -1);
					BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
					playerVOs.add(_player);
				}catch(Exception e){
					log.info(user.getName() + "一键点播用户 " + userIds + " 部分失败，失败userId: " + userId);
					e.printStackTrace();
				}
			}
			
			return playerVOs;
		}
	}
	
	/**
	 * 停止点播用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 下午1:55:25
	 * @param Long businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/stop")
	public Object userStop(
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
		
			Long id = userUtils.getUserIdFromSession(request);
			UserBO user = userUtils.queryUserById(id);
			
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			CommandGroupUserPlayerPO player = commandVodService.userStop(user, businessId, admin);
			
			return new HashMapWrapper<String, Object>().put("serial", player.getLocationIndex())
					   								   .getMap();		
		}
	}	
	
	/**
	 * 点播设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 下午4:03:45
	 * @param String deviceId 设备id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/start")
	public Object deviceStart(
			String deviceId,
			HttpServletRequest request) throws Exception{
		
		Long id = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);
		
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			CommandGroupUserPlayerPO player = commandVodService.deviceStart(user, deviceId, admin, -1);
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			
			return _player;
		}
	}
	
	/**
	 * 指定播放器，点播设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:43:20
	 * @param deviceId 设备id
	 * @param serial 播放器序号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/start/player")
	public Object deviceStartSpecifyPlayer(
			String deviceId,
			int serial,
			HttpServletRequest request) throws Exception{
		
		Long id = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);
		
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			CommandGroupUserPlayerPO player = commandVodService.deviceStart(user, deviceId, admin, serial);
			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			
			return _player;
		}
	}
	
	/**
	 * 批量点播设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 下午5:23:05
	 * @param deviceIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/start/batch")
	public Object deviceStartBatch(
			String deviceIds,
			HttpServletRequest request) throws Exception{
		
		Long id = userUtils.getUserIdFromSession(request);
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);
			
			List<String> deviceIdList = JSON.parseArray(deviceIds, String.class);
			List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			for(String deviceId : deviceIdList){
				try{
					CommandGroupUserPlayerPO player = commandVodService.deviceStart(user, deviceId, admin, -1);
					BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
					playerVOs.add(_player);
				}catch(Exception e){
					log.info(user.getName() + "一键点播设备 " + deviceIds + " 部分失败，失败bundleId: " + deviceId);
					e.printStackTrace();
				}
			}
			
			return playerVOs;
		}
	}


	/**
	 * 停止点播设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 下午4:16:21
	 * @param Long businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/stop")
	public Object deviceStop(
			Long businessId,
			HttpServletRequest request) throws Exception{
		
		synchronized (new StringBuffer().append(lockStartPrefix).append(businessId).toString().intern()) {
			
			Long id = userUtils.getUserIdFromSession(request);
			UserBO user = userUtils.queryUserById(id);
			
//			UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO(); admin.setId(-1L);
			
			CommandGroupUserPlayerPO player = commandVodService.deviceStop(user, businessId, admin);
			
			return new HashMapWrapper<String, Object>().put("serial", player.getLocationIndex())
					   								   .getMap();
		}
	}
	
}
