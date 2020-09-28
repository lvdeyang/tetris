package com.sumavision.bvc.control.device.command.group.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.base.bo.EncoderBO;
import com.suma.venus.resource.base.bo.PlayerBundleBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.config.ServerProps;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserLayoutShemeVO;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserPlayerSettingVO;
import com.sumavision.bvc.control.device.monitor.device.MonitorDeviceController;
import com.sumavision.bvc.control.device.monitor.device.WebSipPlayerVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonConstant;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskQueryService;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * 用户信息，播放器布局方案等<br/>
 * <b>作者:</b>zsy<br/>
 * <b>日期：</b>2019年10月8日
 */
@Slf4j
@Controller
@RequestMapping(value = "/command/user/info")
public class CommandUserInfoController {
	
	@Autowired
	private ServerProps serverProps;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandVodService commandVodService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private PageTaskQueryService pageTaskQueryService;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 获取并恢复当前用户的所有屏幕播放情况<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年10月8日
	 * @param 
	 * @return CommandGroupUserLayoutShemeVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/current")
	public Object getCurrent(
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		//重构的分页信息
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(user.getId().toString(), terminal.getId(), GroupMemberType.MEMBER_USER);
		if(null == pageInfo){
			pageInfo = new PageInfoPO(user.getId().toString(), terminal.getId(), GroupMemberType.MEMBER_USER);
			pageInfo.setPageSize(16);
			pageInfoDao.save(pageInfo);
		}
		
		//查找该用户配置信息
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		if(null == userInfo){
			//如果没有则建立默认
			userInfo = commandUserServiceImpl.generateDefaultUserInfo(user.getId(), user.getName(), true);
		}else{
			//如果有，则更新播放器信息
			commandUserServiceImpl.updateUserInfoPlayers(userInfo, true);
		}
		
		CommandGroupUserLayoutShemeVO schemeVO = pageTaskService.generateSchemeVO(userInfo, pageInfo);		
		return schemeVO;
	}
	
	/**
	 * 获取用户信息及16个播放器<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年10月8日
	 * @param 
	 * @return UserVO 和 List<WebSipPlayerVO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/user/and/players")
	public Object getUserAndPlayers(
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		
		//获取播放器
		List<PlayerBundleBO> entities = resourceQueryUtil.queryPlayerBundlesByUserId(user.getId());
		
		String clientIp = request.getHeader("X-Real-IP");
		clientIp = (clientIp==null||"".equals(clientIp))?request.getRemoteAddr():clientIp;
		
		String port = getPlayerPort();
		
		List<WebSipPlayerVO> players = new ArrayList<WebSipPlayerVO>();
		for(PlayerBundleBO entity:entities){
			players.add(new WebSipPlayerVO().set(entity));
		}
		
		for(WebSipPlayerVO player:players){
			player.setIp(clientIp)
				  .setPort(port);
		}
		
		//获取本地编码器（通常是摄像头）
		WebSipPlayerVO encoderBundleVO = null;
		try{
			UserBO userBo = resourceService.queryUserById(user.getId(), TerminalType.QT_ZK);
			EncoderBO localEncoder = userBo.getLocal_encoder();
			String bundleId = localEncoder.getEncoderId();
//			log.info("bundleId: " + bundleId);
//			List<BundlePO> playerBundlePOs = bundleDao.findByUserIdAndDeviceModel(user.getId(), "encoder");
			List<BundlePO> encoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(bundleId).getList());
			BundlePO encoderBundlePO = encoderBundleEntities.get(0);
//			log.info("encoderBundlePO: " + encoderBundlePO);
			PlayerBundleBO encoderBundleBO = resourceQueryUtil.generateByBundlePO(encoderBundlePO);
//			log.info("encoderBundleBO: " + encoderBundleBO);
			encoderBundleVO = new WebSipPlayerVO().set(encoderBundleBO);
			encoderBundleVO.setIp(clientIp).setPort(port);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Map<String, Object> map = new HashMapWrapper<String, Object>()
				.put("user", user)
				.put("players", players)
				.put("localEncoder", encoderBundleVO)
				.getMap();
		log.info("getUserAndPlayers: " + JSON.toJSON(map));
		
		return map;
	}
	
	/**
	 * 查询播放器关联的上屏设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:35:52
	 * @param serial
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/player/cast/devices")
	public Object queryPlayerCastDevices(
			int serial,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		JSONArray result = new JSONArray();
//		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
//		CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(userInfo.getPlayers(), serial);
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		PageTaskPO task = pageTaskQueryService.queryPageTask(user.getId().toString(), terminal.getId(), serial);
		if(task == null) return result;
		List<CommandGroupUserPlayerCastDevicePO> castDevices = task.getCastDevices();
		for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
			JSONObject device = new JSONObject();
			device.put("bundleId", castDevice.getDstBundleId());
			device.put("name", castDevice.getDstBundleName());
			result.add(device);
		}
		
		return result;
	}
	
	/**
	 * 全量设置播放器关联的上屏设备<br/>
	 * <p>该接口没有被前端使用</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:36:12
	 * @param serial
	 * @param bundleIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/player/cast/devices")
	public Object setPlayerCastDevices(
			int serial,
			String bundleIds,
			HttpServletRequest request) throws Exception{
		
//		throw new BaseException(StatusCode.FORBIDDEN, "不能在窗口上绑定");
		
		UserVO user = userUtils.getUserFromSession(request);
//		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
//		CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(userInfo.getPlayers(), serial);
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		PageTaskPO task = pageTaskQueryService.queryPageTask(user.getId().toString(), terminal.getId(), serial);
		List<String> bundleIdsList = JSONArray.parseArray(bundleIds, String.class);
		commandCastServiceImpl.setCastDevices(task, bundleIdsList);
		
		CommandGroupUserPlayerSettingVO playerVO = new CommandGroupUserPlayerSettingVO().set(task);
		
		return playerVO;
	}
	
	/**
	 * 增加一个播放器绑定的解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午2:48:56
	 * @param serial
	 * @param bundleId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/player/cast/device")
	public Object addPlayerCastDevice(
			int serial,
			String bundleId,
			HttpServletRequest request) throws Exception{
		
//		throw new BaseException(StatusCode.FORBIDDEN, "不能在窗口上绑定");
		
		UserVO user = userUtils.getUserFromSession(request);
//		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
//		CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(userInfo.getPlayers(), serial);
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		PageTaskPO task = pageTaskQueryService.queryPageTask(user.getId().toString(), terminal.getId(), serial);
		commandCastServiceImpl.editCastDevices(task, new ArrayListWrapper<String>().add(bundleId).getList(), null);
		
		CommandGroupUserPlayerSettingVO playerVO = new CommandGroupUserPlayerSettingVO().set(task);
		
		return playerVO;
	}
	
	/**
	 * 删除一个播放器绑定的解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午2:48:35
	 * @param serial
	 * @param bundleId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/player/cast/device")
	public Object removePlayerCastDevice(
			int serial,
			String bundleId,
			HttpServletRequest request) throws Exception{
		
//		throw new BaseException(StatusCode.FORBIDDEN, "不能在窗口上绑定");
		
		UserVO user = userUtils.getUserFromSession(request);
//		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
//		CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(userInfo.getPlayers(), serial);
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		PageTaskPO task = pageTaskQueryService.queryPageTask(user.getId().toString(), terminal.getId(), serial);
		commandCastServiceImpl.editCastDevices(task, null, new ArrayListWrapper<String>().add(bundleId).getList());
		
		CommandGroupUserPlayerSettingVO playerVO = new CommandGroupUserPlayerSettingVO().set(task);
		
		return playerVO;
	}	

	/**
	 * 通过播放器序号校验用户是否有权限<br/>
	 * <p>如果是云台权限，根据业务额外校验</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月28日 下午2:33:04
	 * @param serial 播放器序号（0开头，0~15）
	 * @param type 权限类型，DIANBO(点播)|RECORD(录制)|CLOUD(云台)|CALL(呼叫)|ZK(指挥)|HY(会议)|LR(本地录制)|DOWNLOAD(下载)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/check/player/privilege")
	public Object checkPlayerPrivilege(
			int serial,
			String type,
			HttpServletRequest request) throws Exception{
		Long userId = userUtils.getUserIdFromSession(request);
		BundleBO bundle = null;
		if(type.equals("CLOUD")){
			//如果是云台权限，根据业务额外校验
			bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, serial);
		}else{
			bundle = commandCommonServiceImpl.queryBundleByPlayerIndex(userId, serial);
		}
		commandCommonServiceImpl.authorizeBundle(bundle.getBundleId(), userId, BUSINESS_OPR_TYPE.valueOf(type));
		return null;
	}

	/**
	 * 通过设备id校验用户是否有权限<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月28日 下午2:36:04
	 * @param bundleId
	 * @param type 权限类型，DIANBO(点播)|RECORD(录制)|CLOUD(云台)|CALL(呼叫)|ZK(指挥)|HY(会议)|LR(本地录制)|DOWNLOAD(下载)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/check/bundle/privilege")
	public Object checkBundlePrivilege(
			String bundleId,
			String type,
			HttpServletRequest request) throws Exception{
		Long userId = userUtils.getUserIdFromSession(request);
		commandCommonServiceImpl.authorizeBundle(bundleId, userId, BUSINESS_OPR_TYPE.valueOf(type));
		return null;
	}
	
	/**
	 * 从配置文件中获取播放器端口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月6日 下午2:09:14
	 * @return String 端口
	 */
	private String getPlayerPort() throws Exception{
		Properties prop = new Properties();
		prop.load(MonitorDeviceController.class.getClassLoader().getResourceAsStream("properties/player.properties"));
		return prop.getProperty("port");
	}
}
