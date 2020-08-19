package com.sumavision.bvc.device.command.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.base.bo.PlayerBundleBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.dao.CommandVodDAO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.basic.silence.CommandSilenceServiceImpl;
import com.sumavision.bvc.device.command.exception.HasNotUsefulPlayerException;
import com.sumavision.bvc.device.command.exception.PlayerIsBeingUsedException;
import com.sumavision.bvc.device.command.secret.CommandSecretServiceImpl;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.enumeration.CodecParamType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.util.MeetingUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.exception.AvtplNotFoundException;
import com.sumavision.bvc.device.monitor.live.exception.UserHasNoPermissionForBusinessException;
import com.sumavision.bvc.device.system.AvtplService;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class CommandCommonServiceImpl {
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private CommandVodDAO commandVodDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private CommandSecretServiceImpl commandSecretServiceImpl;
	
	@Autowired
	private CommandSilenceServiceImpl commandSilenceServiceImpl;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private AvtplService avtplService;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private MeetingUtil meetingUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private static ResourceService resourceServiceStatic;
	
	/**
	 * 获取系统默认参数模板<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年9月25日
	 * @return avtpl AvtplPO 参数模板
	 * @return gear AvtplGearsPO 参数档位
	 */
	public Map<String, Object> queryDefaultAvCodec() throws Exception{
		
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		
		//查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.COMMAND);
		if(avTpls==null || avTpls.size()<=0){
			avtplService.generateDefaultAvtpls();
			avTpls = avtplDao.findByUsageType(AvtplUsageType.COMMAND);
			AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "COMMAND1");
			avTpls.add(sys_avtpl);
//			throw new AvtplNotFoundException("缺少会议.系统参数模板！");
		}
		targetAvtpl = avTpls.get(0);
		//查询codec模板档位
		Set<AvtplGearsPO> gears = targetAvtpl.getGears();
		for(AvtplGearsPO gear:gears){
			targetGear = gear;
			break;
		}
		
		if(targetGear == null){
			throw new AvtplNotFoundException("会议.系统参数模板没有创建档位！");
		}
		
		return new HashMapWrapper<String, Object>().put("avtpl", targetAvtpl)
												   .put("gear", targetGear)
												   .getMap();
	}
	
	/**
	 * 取系统默认参数模板BO<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月15日 下午1:26:33
	 * @return
	 * @throws Exception
	 */
	public CodecParamBO queryDefaultAvCodecParamBO() throws Exception{
		
		Map<String, Object> result = queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		return codec;
	}

	/**
	 * 根据业务用户占用播放器--用户操作加锁<br/>
	 * <b>负载均衡满足不了，得用redis<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月16日 下午8:30:42
	 * @param userId 用户id
	 * @param businessType 业务类型
	 * @return CommandGroupUserPlayerPO 播放器
	 */
	public CommandGroupUserPlayerPO userChoseUsefulPlayer(Long userId, PlayerBusinessType businessType) throws Exception{
		return userChoseUsefulPlayer(userId, businessType, 0, false);
	}
	
	/**
	 * 根据业务用户占用播放器--用户操作加锁<br/>
	 * <b>负载均衡满足不了，得用redis<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月16日 下午8:30:42
	 * @param userId 用户id
	 * @param businessType 业务类型
	 * @param dontException 找不到可用播放器时，true则返回null，false则抛错。因为事物嵌套中的抛错，即使try-catch，也会在最终无法提交JPA，所以尽量使用true然后判空
	 * @return CommandGroupUserPlayerPO 播放器
	 */
	public CommandGroupUserPlayerPO userChoseUsefulPlayer(Long userId, PlayerBusinessType businessType, boolean dontException) throws Exception{		
		return userChoseUsefulPlayer(userId, businessType, 0, dontException);
	}
	
	/**
	 * 根据业务用户占用播放器--用户操作加锁<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月28日 下午5:41:21
	 * @param userId
	 * @param businessType
	 * @param mode 0选取第一个可用播放器，-1选取最后一个可用播放器
	 * @param dontException 找不到可用播放器时，true则返回null，false则抛错。因为事物嵌套中的抛错，即使try-catch，也会在最终无法提交JPA，所以尽量使用true然后判空
	 * @return
	 * @throws Exception
	 */
	public CommandGroupUserPlayerPO userChoseUsefulPlayer(Long userId, PlayerBusinessType businessType, int mode, boolean dontException) throws Exception{
		
		synchronized (userId) {
			
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
			if(null == userInfo){
				//如果没有则建立默认
				userInfo = commandUserServiceImpl.generateDefaultUserInfo(userId, null, true);
			}

			CommandGroupUserLayoutShemePO userUsingScheme = queryIsUsingScheme(userInfo);
			
			CommandGroupUserPlayerPO userPlayer = null;
			if(mode == 0){
				userPlayer = queryUsefulPlayer(userUsingScheme, dontException);
			}else if(mode == -1){
				userPlayer = queryLastUsefulPlayer(userUsingScheme, dontException);
			}
			
			if(userPlayer != null){
				userPlayer.setPlayerBusinessType(businessType);
				commandGroupUserPlayerDao.save(userPlayer);
			}			
			
			return userPlayer;
		}
		
	}
	
	public CommandGroupUserPlayerPO userChosePlayerByLocationIndex(Long userId, PlayerBusinessType businessType, int locationIndex) throws Exception{
		return userChosePlayerByLocationIndex(userId, businessType, locationIndex, false);
	}
	
	/**
	 * 指定播放器序号，根据业务用户占用播放器--用户操作加锁<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午4:35:06
	 * @param userId
	 * @param businessType
	 * @param locationIndex 播放器序号
	 * @param ignorePlayerBeingUsed 当前播放器正在被使用时，true则【不修改播放器信息】并返回该播放器，false则抛错。
	 * @return
	 * @throws Exception 指定的播放器已经被别的业务使用时抛PlayerIsBeingUsedException
	 */
	public CommandGroupUserPlayerPO userChosePlayerByLocationIndex(Long userId, PlayerBusinessType businessType, int locationIndex, boolean ignorePlayerBeingUsed) throws Exception{
		
		synchronized (userId) {
			
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
			if(null == userInfo){
				//如果没有则建立默认
				userInfo = commandUserServiceImpl.generateDefaultUserInfo(userId, null, true);
			}
			CommandGroupUserLayoutShemePO userUsingScheme = queryIsUsingScheme(userInfo);			
			List<CommandGroupUserPlayerPO> players = userUsingScheme.obtainPlayers();			
			CommandGroupUserPlayerPO userPlayer = commandCommonUtil.queryPlayerByLocationIndex(players, locationIndex);
			
			if(userPlayer == null){
				throw new BaseException(StatusCode.FORBIDDEN, "当前分屏未找到该播放器，序号：" + locationIndex);
			}
			
			if(!userPlayer.getPlayerBusinessType().equals(PlayerBusinessType.NONE)){
				if(ignorePlayerBeingUsed){
					return userPlayer;
				}else{
					throw new PlayerIsBeingUsedException();
				}
			}
						
			userPlayer.setPlayerBusinessType(businessType);			
			commandGroupUserPlayerDao.save(userPlayer);
			
			return userPlayer;
		}
		
	}
	
	/**
	 * 根据业务用户占用多个播放器--用户操作加锁<br/>
	 * <b>负载均衡满足不了，得用redis<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月22日
	 * @param Long userId 用户id
	 * @param SchemeBusinessType businessType 业务类型
	 * @param int count 需要的播放器数量
	 * @param boolean mustAll 是否必须查询到count个播放器，如果没有这么多可用，则返回空列表
	 * @return List<CommandGroupUserPlayerPO> 播放器列表
	 */
	public List<CommandGroupUserPlayerPO> userChoseUsefulPlayers(Long userId, PlayerBusinessType businessType, int count, boolean mustAll) throws Exception{
	
		if(count <= 0){
			return new ArrayList<CommandGroupUserPlayerPO>();
		}
		
		synchronized (userId) {
			
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
			if(null == userInfo){
				//如果没有则建立默认
				userInfo = commandUserServiceImpl.generateDefaultUserInfo(userId, null, true);
			}

			CommandGroupUserLayoutShemePO userUsingScheme = queryIsUsingScheme(userInfo);
			
			List<CommandGroupUserPlayerPO> userPlayers = queryUsefulPlayers(userUsingScheme, count);
			
			if(mustAll && userPlayers.size()!=count){
				return new ArrayList<CommandGroupUserPlayerPO>();
			}
			
			for(CommandGroupUserPlayerPO player : userPlayers){
				player.setPlayerBusinessType(businessType);
			}
			
			commandGroupUserPlayerDao.save(userPlayers);
			
			return userPlayers;
		}
		
	}
	
	/**
	 * 查询用户正在使用的布局方案<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月16日 下午6:26:54
	 * @param CommandGroupUserInfoPO userInfo 用户信息
	 * @return CommandGroupUserLayoutShemePO 方案
	 */
	public CommandGroupUserLayoutShemePO queryIsUsingScheme(CommandGroupUserInfoPO userInfo) throws Exception{
		List<CommandGroupUserLayoutShemePO> schemes = userInfo.getLayoutSchemes();
		for(CommandGroupUserLayoutShemePO scheme: schemes){
			if(scheme.getIsUsing()){
				return scheme;
			}
		}
		return null;
	}
	
	/**
	 * 查询布局方案中可用的播放器--线程不安全<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月16日 下午7:03:25
	 * @param CommandGroupUserLayoutShemePO scheme 布局方案
	 * @param boolean dontException 找不到可用播放器时，true则返回null，false则抛错
	 * @return CommandGroupUserPlayerPO 播放器，如果查询不到则会抛错
	 */
	private CommandGroupUserPlayerPO queryUsefulPlayer(CommandGroupUserLayoutShemePO scheme, boolean dontException) throws Exception{
		List<CommandGroupUserPlayerPO> players = scheme.obtainPlayers();
		Collections.sort(players, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
		for(CommandGroupUserPlayerPO player: players){
			if(player.getPlayerBusinessType().equals(PlayerBusinessType.NONE)){
				return player;
			}
		}
		if(dontException){
			return null;
		}else{
			throw new HasNotUsefulPlayerException();
		}
	}
	
	/**
	 * 查询布局方案中最后一个可用的播放器--线程不安全<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月29日 上午9:19:17
	 * @param scheme
	 * @return CommandGroupUserPlayerPO 播放器信息，如果查询不到则会抛错
	 * @param boolean dontException 找不到可用播放器时，true则返回null，false则抛错
	 * @throws Exception
	 */
	private CommandGroupUserPlayerPO queryLastUsefulPlayer(CommandGroupUserLayoutShemePO scheme, boolean dontException) throws Exception{
		List<CommandGroupUserPlayerPO> players = scheme.obtainPlayers();
		Collections.sort(players, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
		for(int i=players.size()-1 ; i>=0; i--){
			CommandGroupUserPlayerPO player = players.get(i);
			if(player.getPlayerBusinessType().equals(PlayerBusinessType.NONE)){
				return player;
			}
		}
		if(dontException){
			return null;
		}else{
			throw new HasNotUsefulPlayerException();
		}
	}
	/**
	 * 查询布局方案中可用的多个播放器--线程不安全<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月22日
	 * @param CommandGroupUserLayoutShemePO scheme 布局方案
	 * @param int count 查询总数
	 * @return CommandGroupUserPlayerPO 播放器列表信息，如果查询不到count个，则会返回所有可用的播放器，不会抛错
	 */
	private List<CommandGroupUserPlayerPO> queryUsefulPlayers(CommandGroupUserLayoutShemePO scheme, int count){
		List<CommandGroupUserPlayerPO> players = scheme.obtainPlayers();
		List<CommandGroupUserPlayerPO> usefulPlayers = new ArrayList<CommandGroupUserPlayerPO>();
		Collections.sort(players, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
		for(CommandGroupUserPlayerPO player: players){
			if(player.getPlayerBusinessType().equals(PlayerBusinessType.NONE)){				
				usefulPlayers.add(player);
				if(usefulPlayers.size() == count){
					return usefulPlayers;
				}
			}
		}
		return usefulPlayers;
	}
	
	/**
	 *根据业务信息查询正在使用的播放器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午11:14:11
	 * @param CommandGroupUserLayoutShemePO scheme 用户方案
	 * @param SchemeBusinessType businessType 业务类型
	 * @param Long businessId 业务id
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	public CommandGroupUserPlayerPO queryPlayerByBusiness(CommandGroupUserLayoutShemePO scheme, PlayerBusinessType businessType, String businessId) throws Exception{
		List<CommandGroupUserPlayerPO> players = scheme.obtainPlayers();
		Collections.sort(players, new CommandGroupUserPlayerPO.PlayerComparatorFromIndex());
		for(CommandGroupUserPlayerPO player: players){
			if(player.getPlayerBusinessType().equals(businessType) && player.getBusinessId().equals(businessId)){
				return player;
			}
		}
		throw new HasNotUsefulPlayerException();
	}
	
	/**
	 * 根据业务信息查询用户所占用播放器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午11:11:00
	 * @param CommandGroupUserInfoPO userInfo 用户信息
	 * @param SchemeBusinessType businessType 业务类型
	 * @param Long businessId 业务id
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	public CommandGroupUserPlayerPO queryPlayerByBusiness(CommandGroupUserInfoPO userInfo, PlayerBusinessType businessType, String businessId) throws Exception{
		
		CommandGroupUserLayoutShemePO userUsingScheme = queryIsUsingScheme(userInfo);
		
		CommandGroupUserPlayerPO userPlayer = queryPlayerByBusiness(userUsingScheme, businessType, businessId);
		
		return userPlayer;
		
	}
	
	/**
	 * 根据播放器索引号，查找源设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 上午10:52:17
	 * @param userId
	 * @param index 播放器索引号locationIndex
	 * @return
	 * @throws Exception
	 */
	public BundleBO queryBundleByPlayerIndexForCloudControl(Long userId, int index) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(userInfo.getPlayers(), index);
		switch (player.getPlayerBusinessType()) {
		case PLAY_USER_ONESELF:
		case PLAY_DEVICE:
			CommandVodPO vod = commandVodDao.findByDstBundleId(player.getBundleId());
			return new BundleBO()
					.setBundleId(vod.getSourceBundleId())
					.setNodeUid(vod.getSourceLayerId())
					.setName(vod.getSourceBundleName());
		case NONE:
		case COMMAND_FORWARD_FILE:
		case PLAY_COMMAND_RECORD:
		case PLAY_FILE:
		case PLAY_RECORD:
			throw new BaseException(StatusCode.FORBIDDEN, "没有云台可以控制");
		default:
			throw new BaseException(StatusCode.FORBIDDEN, "不能控制对方的云台");
		}
		
	}
	
	/**
	 * 紧急情况使用：重置所有播放器<br/>
	 * <p>清空所有播放状态，如果播放器数量少于16，则重建</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 下午4:43:09
	 * @param keepSelf 是否保留观看自己的播放器，建议true
	 * @param userName
	 * @throws Exception
	 */
	public void resetAllPlayers(boolean keepSelf, String userName) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserName(userName);
		if(userInfo == null){
			throw new BaseException(StatusCode.FORBIDDEN, "用户不存在");
		}
		Long userId = userInfo.getUserId();
		
		synchronized (userId) {
			
			List<CommandGroupUserPlayerPO> players = userInfo.getPlayers();
			CommandGroupUserPlayerPO selfPlayer = null;
			if(keepSelf){
				for(CommandGroupUserPlayerPO player : players){
					if(player.getPlayerBusinessType().equals(PlayerBusinessType.PLAY_USER_ONESELF)){
						selfPlayer = player;
					}
				}
			}
			if(players.size() < PlayerSplitLayout.SPLIT_16.getPlayerCount()){
				userInfo.getPlayers().clear();
				commandGroupUserPlayerDao.deleteInBatch(players);
				
				//重新从资源层获取播放器
				List<PlayerBundleBO> allPlayers = resourceQueryUtil.queryPlayerBundlesByUserId(userId);				
				if(userInfo.getPlayers() == null) userInfo.setPlayers(new ArrayList<CommandGroupUserPlayerPO>());
				for(int i = 0; i < PlayerSplitLayout.SPLIT_16.getPlayerCount(); i++){
					CommandGroupUserPlayerPO player = new CommandGroupUserPlayerPO().set(allPlayers.get(i));
					player.setLocationIndex(i);
					player.setUserInfo(userInfo);
					userInfo.getPlayers().add(player);
				}				
//				if(selfPlayer != null){
//					CommandGroupUserPlayerPO lastPlayer = userInfo.getPlayers().get(PlayerSplitLayout.SPLIT_16.getPlayerCount() - 1);
//					lastPlayer.setpla
//				}
			}else{
				for(CommandGroupUserPlayerPO player : players){
					if(player.getPlayerBusinessType().equals(PlayerBusinessType.PLAY_USER_ONESELF) && keepSelf){
						continue;
					}
					player.setFree();
				}
			}			
			commandGroupUserInfoDao.save(userInfo);
		}		
	}
	
	/**
	 * 一条转发能否被执行（或从暂停中恢复）<br/>
	 * <p>该转发没有因为会议暂停、专向会议、静默操作而停止时，可以被执行。需要另行判断转发的执行状态ExecuteStatus</p>
	 * <p>搭配 CommandCommonUtil 的 queryForwardsReadyToBeDone 方法一起使用</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 下午3:52:19
	 * @param forward
	 * @return
	 */
	public boolean whetherCanBeDone(CommandGroupForwardPO forward){
		if(commandBasicServiceImpl.whetherStopForCommandPause(forward)){
			return false;
		}else if(commandSecretServiceImpl.whetherStopForSecret(forward)){
			return false;
		}else if(commandSilenceServiceImpl.whetherStopForSilence(forward)){
			return false;
		}
		return true;
	}
	
	/**
	 * 用户对用户操作的权限校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午2:20:34
	 * @param Long targetUserId 呼叫目标用户
	 * @param Long userId 操作业务用户
	 */
	public void authorizeUser(Long targetUserId, Long userId, BUSINESS_OPR_TYPE type) throws Exception{
		boolean authorized = resourceService.hasPrivilegeOfUser(userId, targetUserId, type);
		if(!authorized){
			throw new UserHasNoPermissionForBusinessException(type, 1);
		}
	}
	
	/**
	 * 用户对多个用户操作的权限校验<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月1日 上午11:27:26
	 * @param targetUserIds
	 * @param userId
	 * @param type
	 * @throws Exception
	 */
	public void authorizeUsers(List<Long> targetUserIds, Long userId, BUSINESS_OPR_TYPE type) throws Exception{
		UserBO noAuthUser = resourceService.hasNoPrivilegeOfUsers(userId, targetUserIds, type);
		if(noAuthUser != null){
			throw new UserHasNoPermissionForBusinessException(type, noAuthUser.getName(), 1);
		}
	}
	
	/**
	 * 用户对设备的权限校验<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午2:15:11
	 * @param String bundleId 音频设备id
	 * @param Long userId 业务用户id
	 */
	public void authorizeBundle(String bundleId, Long userId, BUSINESS_OPR_TYPE type) throws Exception{
		boolean authorized = resourceService.hasPrivilegeOfBundle(userId, bundleId, type);
		if(!authorized){
			throw new UserHasNoPermissionForBusinessException(type, 0);
		}
	}
	
	/**
	 * 比较层级路径的高低<br/>
	 * <p>path1高则返回1，低则返回-1，平级则返回0</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月5日 上午11:20:34
	 * @param path1
	 * @param path2
	 * @return
	 */
	private static int compareLevelByParentPath(String path1, String path2){
		if(path1 == null && path2 == null){
			return 0;
		}else if(path1 ==null){
			return 1;
		}else if(path2 ==null){
			return -1;
		}
		
		int levelCount1 = path1.split("/").length;
		int levelCount2 = path2.split("/").length;
		if(levelCount1 == levelCount2){
			return 0;
		}else if(levelCount1 < levelCount2){
			return 1;
		}else if(levelCount1 > levelCount2){
			return -1;
		}

		return 0;
	}
	
	public int compareLevelByUserId(Long userId1, Long userId2) {
		
		try {
			UserBO user1 = resourceService.queryUserById(userId1, TerminalType.QT_ZK);
			UserBO user2 = resourceService.queryUserById(userId2, TerminalType.QT_ZK);
			
			Set<Long> folderIds = new HashSetWrapper<Long>().add(user1.getFolderId()).add(user2.getFolderId()).getSet();
			Map<Long, FolderPO> folderMap = resourceService.queryFoleders(folderIds);
			FolderPO folder1 = folderMap.get(user1.getFolderId());
			FolderPO folder2 = folderMap.get(user2.getFolderId());
			String path1 = null;
			if(folder1 != null){
				path1 = folder1.getParentPath();
			}
			String path2 = null;
			if(folder2 != null){
				path2 = folder2.getParentPath();
			}
			
			return compareLevelByParentPath(path1, path2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int compareLevelByMember(CommandGroupMemberPO member1, CommandGroupMemberPO member2) {
		
		Set<Long> folderIds = new HashSetWrapper<Long>().add(member1.getFolderId()).add(member2.getFolderId()).getSet();
		Map<Long, FolderPO> folderMap = resourceService.queryFoleders(folderIds);
		FolderPO folder1 = folderMap.get(member1.getFolderId());
		FolderPO folder2 = folderMap.get(member2.getFolderId());
		String path1 = null;
		if(folder1 != null){
			path1 = folder1.getParentPath();
		}
		String path2 = null;
		if(folder2 != null){
			path2 = folder2.getParentPath();
		}

		return compareLevelByParentPath(path1, path2);
	}
	
	/** 比较2个成员的层级高低，主席的高，没有主席则平级 */
	public int compareLevelByMemberIsChairman(CommandGroupMemberPO member1, CommandGroupMemberPO member2) {		
		if(member1.isAdministrator()) return 1;
		else if(member2.isAdministrator()) return -1;
		else return 0;
	}

	/**
	* @ClassName: UserLevelComparator 
	* @Description: 比较2个用户的层级高低
	* @Detail: userId1比userId2层级高，返回1；反之返回-1；同级返回0。对于找不到组织结构的user，暂时认为其在最高层级
	* @author zsy
	* @date 2019年11月5日 下午3:36:08 
	*
	 */
	public static final class UserLevelComparator implements Comparator<Long>{
		
		@Override
		public int compare(Long userId1, Long userId2) {
			
			try {
				UserBO user1 = resourceServiceStatic.queryUserById(userId1, TerminalType.QT_ZK);
				UserBO user2 = resourceServiceStatic.queryUserById(userId2, TerminalType.QT_ZK);
				
				Set<Long> folderIds = new HashSetWrapper<Long>().add(user1.getFolderId()).add(user2.getFolderId()).getSet();
				Map<Long, FolderPO> folderMap = resourceServiceStatic.queryFoleders(folderIds);
				FolderPO folder1 = folderMap.get(user1.getFolderId());
				FolderPO folder2 = folderMap.get(user2.getFolderId());
				String path1 = null;
				if(folder1 != null){
					path1 = folder1.getParentPath();
				}
				String path2 = null;
				if(folder2 != null){
					path2 = folder2.getParentPath();
				}
				
				return compareLevelByParentPath(path1, path2);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
		
	}
	
	/**
	 * 
	* @ClassName: MemberLevelComparator 
	* @Description: 比较2个指挥成员的层级高低。
	* @Detail: userId1比userId2层级高，返回1；反之返回-1；同级返回0。对于找不到组织结构的member，暂时认为其在最高层级
	* @author zsy
	* @date 2019年11月7日 下午4:17:08 
	*
	 */
	public static final class MemberLevelComparator implements Comparator<CommandGroupMemberPO>{
		
		@Override
		public int compare(CommandGroupMemberPO member1, CommandGroupMemberPO member2) {
			
			Set<Long> folderIds = new HashSetWrapper<Long>().add(member1.getFolderId()).add(member2.getFolderId()).getSet();
			Map<Long, FolderPO> folderMap = resourceServiceStatic.queryFoleders(folderIds);
			FolderPO folder1 = folderMap.get(member1.getFolderId());
			FolderPO folder2 = folderMap.get(member2.getFolderId());
			String path1 = null;
			if(folder1 != null){
				path1 = folder1.getParentPath();
			}
			String path2 = null;
			if(folder2 != null){
				path2 = folder2.getParentPath();
			}

			return compareLevelByParentPath(path1, path2);
		}
	}
	
}
