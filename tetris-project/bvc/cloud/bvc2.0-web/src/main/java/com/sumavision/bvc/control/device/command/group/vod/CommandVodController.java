package com.sumavision.bvc.control.device.command.group.vod;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.suma.venus.resource.base.bo.UserBO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.command.group.vo.BusinessPlayerVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.vod.VodService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/command/vod")
public class CommandVodController {

	/** 发起业务时，synchronized锁的前缀 */
	private static final String lockUserPrefix = "controller-userId-";

	/** 响应、停止业务时，synchronized锁的前缀 */
	private static final String lockProcessPrefix = "controller-vod-or-call-businessId-";

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private CommandVodService commandVodService;

	@Autowired
	private VodService vodService;
	
	@Autowired 
	private BusinessReturnService businessReturnService;

	/**
	 * 通用方法，指定播放器，播放各种类型的资源<br/>
	 * <p>
	 * 通常用于从资源树中拖拽到播放器进行点播
	 * </p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月26日 上午9:35:20
	 * 
	 * @param type
	 *            取值为 file/user/device
	 * @param id
	 *            资源id，可能是文件、设备、用户的id
	 * @param serial
	 *            播放器序号
	 * @param request
	 * @return BusinessPlayerVO 播放器业务信息
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/from/player")
	public Object startFromPlayer(String type, String id, int serial, HttpServletRequest request) throws Exception {

		// throw new BaseException(StatusCode.FORBIDDEN, "请从通讯录发起");

		Long userId = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(userId).toString().intern()) {
			UserBO user = userUtils.queryUserById(userId);
			UserBO admin = new UserBO();
			admin.setId(-1L);
			CommandGroupUserPlayerPO player = null;
			if ("file".equals(type)) {
				throw new BaseException(StatusCode.FORBIDDEN, "暂不支持");
			} else if ("user".equals(type)) {
				UserBO vodUser = userUtils.queryUserById(Long.parseLong(id));
				businessReturnService.init(Boolean.TRUE);
				vodService.userStart(user, vodUser, serial);
			} else if ("device".equals(type)) {
				businessReturnService.init(Boolean.TRUE);
				vodService.deviceStart(user, id, serial);
			}

			BusinessPlayerVO _player = new BusinessPlayerVO();

			return _player;
		}
	}

	/**
	 * 点播文件资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午10:41:01
	 * 
	 * @param String
	 *            resourceFileId 资源文件id
	 * @return BusinessPlayerVO 播放器业务信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/file/start")
	public Object resourceFileStart(String resourceFileId, HttpServletRequest request) throws Exception {

		Long userId = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(userId).toString().intern()) {
			UserBO user = userUtils.queryUserById(userId);
			businessReturnService.init(Boolean.TRUE);
			CommandGroupUserPlayerPO player = commandVodService.resourceVodStart(user, resourceFileId, -1);

			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);

			return _player;
		}
	}

	/**
	 * 指定播放器，点播文件资源<br/>
	 * <p>
	 * 详细描述
	 * </p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:41:16
	 * 
	 * @param resourceFileId
	 *            资源文件id
	 * @param serial
	 *            播放器序号
	 * @param request
	 * @return BusinessPlayerVO 播放器业务信息
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/file/start/player")
	public Object resourceFileStartSpecifyPlayer(String resourceFileId, int serial, HttpServletRequest request)
			throws Exception {

		Long userId = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(userId).toString().intern()) {
			UserBO user = userUtils.queryUserById(userId);
			businessReturnService.init(Boolean.TRUE);
			CommandGroupUserPlayerPO player = commandVodService.resourceVodStart(user, resourceFileId, serial);

			BusinessPlayerVO _player = new BusinessPlayerVO().set(player);

			return _player;
		}
	}

	/**
	 * 批量点播文件<br/>
	 * <p>
	 * 详细描述
	 * </p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 下午5:22:23
	 * 
	 * @param resourceFileIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/file/start/batch")
	public Object resourceFileStartBatch(String resourceFileIds, HttpServletRequest request) throws Exception {

		Long userId = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(userId).toString().intern()) {
			UserBO user = userUtils.queryUserById(userId);

			businessReturnService.init(Boolean.TRUE);
			List<String> resourceFileIdList = JSON.parseArray(resourceFileIds, String.class);
			List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
			for (String resourceFileId : resourceFileIdList) {
				try {
					CommandGroupUserPlayerPO player = commandVodService.resourceVodStart(user, resourceFileId, -1);
					BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
					playerVOs.add(_player);
				} catch (Exception e) {
					log.info(
							user.getName() + "一键点播文件 " + resourceFileIds + " 部分失败，失败resourceFileId: " + resourceFileId);
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
	 * 
	 * @param String
	 *            businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/file/stop")
	public Object resourceFileStop(String businessId, HttpServletRequest request) throws Exception {

		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
			Long userId = userUtils.getUserIdFromSession(request);
			UserBO user = userUtils.queryUserById(userId);

			businessReturnService.init(Boolean.TRUE);
			CommandGroupUserPlayerPO player = commandVodService.resourceVodStop(user, businessId);

			return new HashMapWrapper<String, Object>().put("serial", player.getLocationIndex()).getMap();
		}
	}

	/**
	 * 点播用户自己<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月7日 上午9:29:16
	 * 
	 * @param userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/see/oneself/user/start")
	public Object seeOneselfUserStart(HttpServletRequest request) throws Exception {

		Long id = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(id).toString().intern()) {

			UserBO user = userUtils.queryUserById(id);
			UserBO admin = new UserBO();
			admin.setId(-1L);

			// CommandGroupUserPlayerPO player =
			// commandVodService.seeOneselfUserStart(user, admin, true);
			// BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			businessReturnService.init(Boolean.TRUE);
			vodService.userStart(user, user, null);
			BusinessPlayerVO _player = new BusinessPlayerVO();

			return _player;
		}
	}

	/**
	 * 点播用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 上午9:29:16
	 * 
	 * @param userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/start")
	public Object userStart(Long userId, HttpServletRequest request) throws Exception {

		Long id = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(id).toString().intern()) {

			UserBO user = userUtils.queryUserById(id);
			UserBO vodUser = userUtils.queryUserById(userId);

			// vodService.userStop(153L);//test
			businessReturnService.init(Boolean.TRUE);
			vodService.userStart(user, vodUser, null);

			BusinessPlayerVO _player = new BusinessPlayerVO();
			return _player;
		}
	}

	/**
	 * 指定播放器，点播用户<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午9:29:16
	 * 
	 * @param userId
	 * @param serial
	 *            播放器序号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/start/player")
	public Object userStartSpecifyPlayer(Long userId, int serial, HttpServletRequest request) throws Exception {

		// throw new BaseException(StatusCode.FORBIDDEN, "请从通讯录发起");
		Long id = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(id).toString().intern()) {

			UserBO user = userUtils.queryUserById(id);
			UserBO vodUser = userUtils.queryUserById(userId);

			// vodService.userStop(153L);//test

			businessReturnService.init(Boolean.TRUE);
			vodService.userStart(user, vodUser, serial);

			BusinessPlayerVO _player = new BusinessPlayerVO();
			return _player;
		}
		/*
		 * Long id = userUtils.getUserIdFromSession(request);
		 * 
		 * synchronized (new
		 * StringBuffer().append(lockStartPrefix).append(id).toString().intern()
		 * ) { UserBO user = userUtils.queryUserById(id); UserBO vodUser =
		 * userUtils.queryUserById(userId);
		 * 
		 * // UserBO admin =
		 * resourceService.queryUserInfoByUsername(CommandCommonConstant.
		 * USER_NAME); UserBO admin = new UserBO(); admin.setId(-1L);
		 * 
		 * CommandGroupUserPlayerPO player =
		 * commandVodService.userStart_Cascade(user, vodUser, admin, serial);
		 * BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
		 * 
		 * return _player; }
		 */
	}

	/**
	 * 批量点播用户<br/>
	 * <p>
	 * 详细描述
	 * </p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 下午5:22:48
	 * 
	 * @param userIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/start/batch")
	public Object userStartBatch(String userIds, HttpServletRequest request) throws Exception {

		Long id = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);

			List<Long> userIdList = JSON.parseArray(userIds, Long.class);
			List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
			// UserBO admin =
			// resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO();
			admin.setId(-1L);
	
			businessReturnService.init(Boolean.TRUE);
			List<UserBO> vodUsers=new ArrayList<UserBO>();
			for (Long userId : userIdList) {
				UserBO vodUser = userUtils.queryUserById(userId);
				vodUsers.add(vodUser);
			}
			
			vodService.userStartBatch(user,vodUsers);
			
//			for (Long userId : userIdList) {
//				UserBO vodUser = userUtils.queryUserById(userId);
//				try {
//					vodService.userStart(user, vodUser, null);
//					// CommandGroupUserPlayerPO player =
//					// commandVodService.userStart_Cascade(user, vodUser, admin,
//					// -1);
//					// BusinessPlayerVO _player = new
//					// BusinessPlayerVO().set(player);
//					// playerVOs.add(_player);
//				} catch (Exception e) {
//					log.info(user.getName() + "一键点播用户 " + userIds + " 部分失败，失败userId: " + userId);
//					e.printStackTrace();
//				}
//			}

			return playerVOs;
		}
	}

	/**
	 * 停止点播用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 下午1:55:25
	 * 
	 * @param Long businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/user/stop")
	public Object userStop(Long businessId, HttpServletRequest request) throws Exception {

		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {

			Long id = userUtils.getUserIdFromSession(request);
			UserBO user = userUtils.queryUserById(id);

			// UserBO admin =
			// resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO();
			admin.setId(-1L);

			// CommandGroupUserPlayerPO player =
			// commandVodService.userStop(user, businessId, admin);
			businessReturnService.init(Boolean.TRUE);
			vodService.userStop(businessId);

			return new HashMapWrapper<String, Object>().put("serial", 111)// player.getLocationIndex())
					.getMap();
		}
	}

	/**
	 * 点播设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 下午4:03:45
	 * 
	 * @param String
	 *            deviceId 设备id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/start")
	public Object deviceStart(
			String deviceId,
			@RequestParam(defaultValue="true")Boolean allowNewPage,
			HttpServletRequest request) throws Exception {

		Long id = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);

			// UserBO admin =
			// resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			// UserBO admin = new UserBO(); admin.setId(-1L);
			//
			// CommandGroupUserPlayerPO player =
			// commandVodService.deviceStart_Cascade(user, deviceId, admin, -1);
			// BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
			businessReturnService.init(Boolean.TRUE);
			if(allowNewPage){
				vodService.deviceStart(user, deviceId, -1);
			}else {
				vodService.deviceStart(user, deviceId, null);
			}

			BusinessPlayerVO _player = new BusinessPlayerVO();

			return _player;
		}
	}
	
//	/**
//	 * 点播外域设备<br/>
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2021年2月2日 下午5:34:13
//	 * @param deviceId 设备id
//	 * @param bundleName 设备名称
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/out/device/start")
//	public Object outDeviceStart(
//			String deviceId,
//			String bundleName,
//			HttpServletRequest request) throws Exception {
//
//		Long id = userUtils.getUserIdFromSession(request);
//
//		synchronized (new StringBuffer().append(lockUserPrefix).append(id).toString().intern()) {
//			UserBO user = userUtils.queryUserById(id);
//			
//			businessReturnService.init(Boolean.TRUE);
//			vodService.outDeviceStart(user, bundleName, deviceId, null);
//
//			BusinessPlayerVO _player = new BusinessPlayerVO();
//
//			return _player;
//		}
//	}

	/**
	 * 指定播放器，点播设备<br/>
	 * <p>
	 * 详细描述
	 * </p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:43:20
	 * 
	 * @param deviceId
	 *            设备id
	 * @param serial
	 *            播放器序号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/start/player")
	public Object deviceStartSpecifyPlayer(
			String deviceId, 
			@RequestParam(defaultValue="true")Boolean allowNewPage, 
			int serial, 
			HttpServletRequest request) throws Exception {

		// throw new BaseException(StatusCode.FORBIDDEN, "请从通讯录发起");

		Long id = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);

			// UserBO admin =
			// resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			// UserBO admin = new UserBO(); admin.setId(-1L);
			//
			// CommandGroupUserPlayerPO player =
			// commandVodService.deviceStart_Cascade(user, deviceId, admin, -1);
			// BusinessPlayerVO _player = new BusinessPlayerVO().set(player);

			businessReturnService.init(Boolean.TRUE);
			if(allowNewPage){
				vodService.deviceStart(user, deviceId, -1);
			}else {
				vodService.deviceStart(user, deviceId, serial);
			}

			BusinessPlayerVO _player = new BusinessPlayerVO();

			return _player;
		}
	}
		
	/**
	 * 点播外域设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月2日 下午5:33:20
	 * @param bundleId 设备id
	 * @param serial 播放器序号
	 * @param bundleName
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/foreign/device/start/player")
	public Object foreignDeviceStartSpecifyPlayer(
			String bundleId, 
			Integer serial, 
			String bundleName,
			HttpServletRequest request) throws Exception {

		Long id = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);
			
			businessReturnService.init(Boolean.TRUE);
			vodService.foreignDeviceStart(user, bundleName,bundleId, serial);

			BusinessPlayerVO _player = new BusinessPlayerVO();

			return _player;
		}
	}	


	/**
	 * 批量点播设备<br/>
	 * <p>
	 * 详细描述
	 * </p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 下午5:23:05
	 * 
	 * @param deviceIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/start/batch")
	public Object deviceStartBatch(String deviceIds, HttpServletRequest request) throws Exception {

		Long id = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(id).toString().intern()) {
			UserBO user = userUtils.queryUserById(id);

			List<String> deviceIdList = JSON.parseArray(deviceIds, String.class);
			List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
			// UserBO admin =
			// resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
			UserBO admin = new UserBO();
			admin.setId(-1L);

			businessReturnService.init(Boolean.TRUE);
			for (String deviceId : deviceIdList) {
				try {
					vodService.deviceStart(user, deviceId, null);
					// CommandGroupUserPlayerPO player =
					// commandVodService.deviceStart_Cascade(user, deviceId,
					// admin, -1);
					// BusinessPlayerVO _player = new
					// BusinessPlayerVO().set(player);
					// playerVOs.add(_player);
				} catch (Exception e) {
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
	 * 
	 * @param Long
	 *            businessId 业务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/device/stop")
	public Object deviceStop(Long businessId, HttpServletRequest request) throws Exception {
		
		synchronized (new StringBuffer().append(lockUserPrefix).append(businessId).toString().intern()) {
			synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {

				Long id = userUtils.getUserIdFromSession(request);
				UserBO user = userUtils.queryUserById(id);

				// UserBO admin =
				// resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
				UserBO admin = new UserBO();
				admin.setId(-1L);

				businessReturnService.init(Boolean.TRUE);
				vodService.deviceStop(user, businessId);

				return new HashMapWrapper<String, Object>().put("serial", 111)// player.getLocationIndex())
						.getMap();

				// CommandGroupUserPlayerPO player =
				// commandVodService.deviceStop(user, businessId, admin);
				//
				//// vodService.userStop(businessId);
				//
				// return new HashMapWrapper<String, Object>().put("serial",
				// player.getLocationIndex())
				// .getMap();
			}
		}
	}
	
//	/**
//	 * 停止点播外域设备<br/>
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2021年2月2日 下午6:38:26
//	 * @param businessId 业务组id
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/foreign/device/stop")
//	public Object foreignDeviceStop(Long businessId, HttpServletRequest request) throws Exception {
//		
//		synchronized (new StringBuffer().append(lockUserPrefix).append(businessId).toString().intern()) {
//			synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
//
//				Long id = userUtils.getUserIdFromSession(request);
//				UserBO user = userUtils.queryUserById(id);
//				
//				businessReturnService.init(Boolean.TRUE);
//				vodService.foreignDeviceStop(user, businessId);
//
//				return new HashMapWrapper<String, Object>().put("serial", 111).getMap();
//
//			}
//		}
//	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/record/file/start")
	public Object recordFileStart(
			String businessType, 
			String businessInfo, 
			String url, 
			@RequestParam(defaultValue="true")Boolean allowNewPage,
			HttpServletRequest request)
			throws Exception {

		Long userId = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(userId).toString().intern()) {
			UserBO user = userUtils.queryUserById(userId);
			
			businessReturnService.init(Boolean.TRUE);
			commandVodService.recordVodStart(user, businessType, businessInfo, url, -1, allowNewPage);

			BusinessPlayerVO _player = new BusinessPlayerVO();// .set(player);

			return _player;
		}
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/record/file/stop")
	public Object recordFileStop(int serial, HttpServletRequest request) throws Exception {

		Long userId = userUtils.getUserIdFromSession(request);

		synchronized (new StringBuffer().append(lockUserPrefix).append(userId).toString().intern()) {
			UserBO user = userUtils.queryUserById(userId);

			businessReturnService.init(Boolean.TRUE);
			CommandGroupUserPlayerPO player = commandVodService.recordVodStop(user, serial);

			return new HashMapWrapper<String, Object>().put("serial", player.getLocationIndex()).getMap();
		}
	}

}
