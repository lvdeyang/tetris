package com.sumavision.bvc.device.command.vod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.dao.CommandVodDAO;
import com.sumavision.bvc.command.group.enumeration.VodType;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.PlayerIsBeingUsedException;
import com.sumavision.bvc.device.command.exception.UserHasNoAvailableEncoderException;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.XtBusinessPassByContentBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.monitor.playback.exception.ResourceNotExistException;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CommandVodService {

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private CommandVodDAO commandVodDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/**
	 * 点播文件资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午10:13:53
	 * @param UserBO user 用户
	 * @param String resourceId 资源文件id
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @return CommandGroupUserPlayerPO 播放器
	 */
	public CommandGroupUserPlayerPO resourceVodStart(UserBO user, String resourceId, int locationIndex) throws Exception{
		
		//占用播放器
		CommandGroupUserPlayerPO player = null;
		if(locationIndex == -1){
			player = commandCommonServiceImpl.userChoseUsefulPlayer(user.getId(), PlayerBusinessType.PLAY_FILE);
		}else{
			player = commandCommonServiceImpl.userChosePlayerByLocationIndex(user.getId(), PlayerBusinessType.PLAY_FILE, locationIndex);
		}
		
		JSONObject file = resourceService.queryOnDemandResourceById(resourceId);
		if(file == null) throw new ResourceNotExistException(resourceId);
		
		String resourceName = file.getString("name");
		String previewUrl = file.getString("previewUrl");
		
		player.setBusinessId(resourceId + "@@" + UUID.randomUUID().toString().replaceAll("-", ""));
		player.setPlayUrl(previewUrl);
		player.setBusinessName("点播" + resourceName + "文件");
		
		commandGroupUserPlayerDao.save(player);
		
		return player;
	}
	
	/**
	 * 停止点播文件资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午10:49:27
	 * @param UserBO user 用户
	 * @param String businessId 业务id
	 */
	public CommandGroupUserPlayerPO resourceVodStop(UserBO user, String businessId) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		CommandGroupUserPlayerPO player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.PLAY_FILE, businessId);
		player.setPlayerBusinessType(PlayerBusinessType.NONE);
		player.setBusinessId(null);
		player.setBusinessName(null);
		player.setPlayUrl(null);
		commandGroupUserPlayerDao.save(player);
		
		return player;
		
	}
	
	/**
	 * 点播用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 下午5:21:56
	 * @param UserBO user 用户
	 * @param UserBO vodUser 被点播用户
	 * @param UserBO admin admin
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @return CommandGroupUserPlayerPO 播放器
	 */
	public CommandGroupUserPlayerPO userStart(UserBO user, UserBO vodUser, UserBO admin, int locationIndex) throws Exception{
				
		//点播--解码（播放器）
		CommandGroupUserPlayerPO decoderUserPlayer = null;
		if(locationIndex == -1){
			decoderUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(user.getId(), PlayerBusinessType.PLAY_USER);
		}else{
			decoderUserPlayer = commandCommonServiceImpl.userChosePlayerByLocationIndex(user.getId(), PlayerBusinessType.PLAY_USER, locationIndex);
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//被点播--编码
		List<BundlePO> encoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(vodUser)).getList());
		if(encoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(vodUser.getName());
		BundlePO encoderBundleEntity = encoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> encoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		if(encoderVideoChannels.size() == 0) throw new UserHasNoAvailableEncoderException(vodUser.getName());
		ChannelSchemeDTO encoderVideoChannel = encoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> encoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		if(encoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(vodUser.getName());
		ChannelSchemeDTO encoderAudioChannel = encoderAudioChannels.get(0);		
		
		CommandVodPO userVod = new CommandVodPO(
				VodType.USER, vodUser.getId(), vodUser.getUserNo(), vodUser.getName(), 
				encoderBundleEntity.getBundleId(), encoderBundleEntity.getBundleName(), encoderBundleEntity.getBundleType(),
				encoderBundleEntity.getAccessNodeUid(), encoderVideoChannel.getChannelId(), encoderVideoChannel.getBaseType(), 
				encoderAudioChannel.getChannelId(), encoderAudioChannel.getBaseType(), 
				user.getId(), user.getUserNo(), user.getName(), decoderUserPlayer.getBundleId(), 
				decoderUserPlayer.getBundleName(), decoderUserPlayer.getBundleType(), decoderUserPlayer.getLayerId(),
				decoderUserPlayer.getVideoChannelId(), decoderUserPlayer.getVideoBaseType(), decoderUserPlayer.getAudioChannelId(),
				decoderUserPlayer.getAudioBaseType());
		
		commandGroupUserPlayerDao.save(decoderUserPlayer);
		commandVodDao.save(userVod);
		
		decoderUserPlayer.setBusinessId(userVod.getId().toString());
		decoderUserPlayer.setBusinessName("正在点播" + vodUser.getName() + "用户");
		
		//点播协议
		LogicBO logic = connectBundle(userVod, codec, admin.getId());
		LogicBO logicCast = commandCastServiceImpl.openBundleCastDevice(null, null, null, new ArrayListWrapper<CommandVodPO>().add(userVod).getList(), null, codec, user.getId());
		logic.merge(logicCast);
		
		executeBusiness.execute(logic, user.getName() + "点播" + vodUser.getName() + "用户：");
		
		return decoderUserPlayer;
	}
	
	/**
	 * 点播用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 下午5:21:56
	 * @param UserBO user 用户
	 * @param UserBO vodUser 被点播用户
	 * @param UserBO admin admin
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @return CommandGroupUserPlayerPO 播放器
	 */
	public CommandGroupUserPlayerPO userStart_Cascade(UserBO user, UserBO vodUser, UserBO admin, int locationIndex) throws Exception{
		
		FolderUserMap vodUserfolderUserMap = folderUserMapDao.findByUserId(vodUser.getId());
		boolean bVodUserLdap = queryUtil.isLdapUser(user, vodUserfolderUserMap);
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		LogicBO logicCloseOld = null;
		
		//点播--解码（播放器）
		CommandGroupUserPlayerPO decoderUserPlayer = null;
		if(locationIndex == -1){
			decoderUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(user.getId(), PlayerBusinessType.PLAY_USER);
		}else{
			decoderUserPlayer = commandCommonServiceImpl.userChosePlayerByLocationIndex(user.getId(), PlayerBusinessType.PLAY_USER, locationIndex, true);
			
			//先判断该播放器是否已被使用
			String oldBusinessId = decoderUserPlayer.getBusinessId();
			if(oldBusinessId != null){
				
				//播放器已被使用。判断原业务是否是点播
				PlayerBusinessType oldType = decoderUserPlayer.getPlayerBusinessType();
				if(PlayerBusinessType.PLAY_DEVICE.equals(oldType)
						|| PlayerBusinessType.PLAY_USER.equals(oldType)){
					
					//是点播，先判断新旧是否一样
					CommandVodPO oldVod = commandVodDao.findOne(Long.parseLong(oldBusinessId));					
					if(PlayerBusinessType.PLAY_USER.equals(oldType)
							&& oldVod.getSourceNo().equals(vodUser.getUserNo())){
						
						log.info("点播用户换源时与原有业务相同，不需处理");
						return decoderUserPlayer;
					}
					
					//停止旧的，点播新的
					decoderUserPlayer.setPlayerBusinessType(PlayerBusinessType.PLAY_USER);
					logicCloseOld = closeBundle(oldVod, codec, admin.getId(), false);
					commandVodDao.delete(oldVod);
					
					
				}else{
					//不是点播业务，不允许
					throw new PlayerIsBeingUsedException();
				}
			}
		}
		
		CommandVodPO userVod = null;
		
		//被点播--编码
		if(!bVodUserLdap){
			List<BundlePO> encoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(vodUser)).getList());
			if(encoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(vodUser.getName());
			BundlePO encoderBundleEntity = encoderBundleEntities.get(0);
			
			List<ChannelSchemeDTO> encoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
			if(encoderVideoChannels.size() == 0) throw new UserHasNoAvailableEncoderException(vodUser.getName());
			ChannelSchemeDTO encoderVideoChannel = encoderVideoChannels.get(0);
			
			List<ChannelSchemeDTO> encoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
			if(encoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(vodUser.getName());
			ChannelSchemeDTO encoderAudioChannel = encoderAudioChannels.get(0);		
			
			userVod = new CommandVodPO(
					VodType.USER, vodUser.getId(), vodUser.getUserNo(), vodUser.getName(), 
					encoderBundleEntity.getBundleId(), encoderBundleEntity.getBundleName(), encoderBundleEntity.getBundleType(),
					encoderBundleEntity.getAccessNodeUid(), encoderVideoChannel.getChannelId(), encoderVideoChannel.getBaseType(), 
					encoderAudioChannel.getChannelId(), encoderAudioChannel.getBaseType(), 
					user.getId(), user.getUserNo(), user.getName(), decoderUserPlayer.getBundleId(), 
					decoderUserPlayer.getBundleName(), decoderUserPlayer.getBundleType(), decoderUserPlayer.getLayerId(),
					decoderUserPlayer.getVideoChannelId(), decoderUserPlayer.getVideoBaseType(), decoderUserPlayer.getAudioChannelId(),
					decoderUserPlayer.getAudioBaseType());
		}else{
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			String bundleId = UUID.randomUUID().toString().replace("-", "");
			String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
			String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
			
			userVod = new CommandVodPO(
					VodType.LOCAL_SEE_OUTER_USER, vodUser.getId(), vodUser.getUserNo(), vodUser.getName(), 
					bundleId, vodUser.getName()+"用户源", "outer_no_bundle_type",
					localLayerId, videoChannelId, "outer_no_base_type", 
					audioChannelId, "outer_no_base_type", 
					user.getId(), user.getUserNo(), user.getName(), decoderUserPlayer.getBundleId(), 
					decoderUserPlayer.getBundleName(), decoderUserPlayer.getBundleType(), decoderUserPlayer.getLayerId(),
					decoderUserPlayer.getVideoChannelId(), decoderUserPlayer.getVideoBaseType(), decoderUserPlayer.getAudioChannelId(),
					decoderUserPlayer.getAudioBaseType());
		}
		
		commandGroupUserPlayerDao.save(decoderUserPlayer);
		commandVodDao.save(userVod);
		
		decoderUserPlayer.setBusinessId(userVod.getId().toString());
		decoderUserPlayer.setBusinessName("正在点播" + vodUser.getName() + "用户");
		
		//点播协议
		LogicBO logic = connectBundle(userVod, codec, admin.getId());
		LogicBO logicCast = commandCastServiceImpl.openBundleCastDevice(null, null, null, new ArrayListWrapper<CommandVodPO>().add(userVod).getList(), null, codec, user.getId());
		logic.merge(logicCast);
		if(logicCloseOld != null) logic.merge(logicCloseOld);
		
		executeBusiness.execute(logic, user.getName() + "点播" + vodUser.getName() + "用户：");
		
		return decoderUserPlayer;
	}

	/**
	 * 停止点播用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 上午9:17:20
	 * @param UserBO user 用户
	 * @param Long businessId 业务id
	 * @param UserBO admin 管理员
	 */
	public CommandGroupUserPlayerPO userStop(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		if(businessId == 0L){
			//释放摄像头预览
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
			CommandGroupUserPlayerPO selfPlayer = commandCommonUtil.queryPlayerByPlayerBusinessType(userInfo.obtainUsingSchemePlayers(), PlayerBusinessType.PLAY_USER_ONESELF);
			if(selfPlayer != null){
				selfPlayer.setFree();
				commandGroupUserPlayerDao.save(selfPlayer);
				return selfPlayer;
			}
		}
		
		CommandVodPO vod = commandVodDao.findOne(businessId);
		
		if(vod == null){
			throw new BaseException(StatusCode.FORBIDDEN, "用户点播不存在！");
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		LogicBO logic = closeBundle(vod, codec, admin.getId(), true);
		
		//该点播可能是普通点播，也可能是用户的本地视频VodType.USER_ONESELF（自己看自己）
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(vod.getDstUserId());
		CommandGroupUserPlayerPO player = null;
		if(vod.getVodType().equals(VodType.USER)){
			player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.PLAY_USER, businessId.toString());
		}else if(vod.getVodType().equals(VodType.USER_ONESELF)){
			player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.PLAY_USER_ONESELF, businessId.toString());
		}
		player.setPlayerBusinessType(PlayerBusinessType.NONE);
		player.setBusinessId(null);
		player.setBusinessName(null);
		commandGroupUserPlayerDao.save(player);
		
		commandVodDao.delete(vod);
		
		LogicBO logicCast = commandCastServiceImpl.closeBundleCastDevice(
				null, new ArrayListWrapper<CommandVodPO>().add(vod).getList(), null, 
				new ArrayListWrapper<CommandGroupUserPlayerPO>().add(player).getList(), null, user.getId());
		logic.merge(logicCast);
		executeBusiness.execute(logic, user.getName() + "停止点播用户");
		
		return player;
		
	}
	
	/**
	 * 点播设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 下午2:32:25
	 * @param UserBO user 用户信息
	 * @param String bundleId 设备id
	 * @param UserBO admin 管理员
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	public CommandGroupUserPlayerPO deviceStart(UserBO user, String bundleId, UserBO admin, int locationIndex) throws Exception{
		
		//点播--解码（播放器）
		CommandGroupUserPlayerPO decoderUserPlayer = null;
		if(locationIndex == -1){
			decoderUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(user.getId(), PlayerBusinessType.PLAY_DEVICE);
		}else{
			decoderUserPlayer = commandCommonServiceImpl.userChosePlayerByLocationIndex(user.getId(), PlayerBusinessType.PLAY_DEVICE, locationIndex);
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//被点播--编码
		List<BundlePO> encoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(bundleId).getList());
		BundlePO encoderBundleEntity = encoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> encoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO encoderVideoChannel = encoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> encoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO encoderAudioChannel = encoderAudioChannels.get(0);
				
		CommandVodPO userVod = new CommandVodPO(
				VodType.DEVICE, null, encoderBundleEntity.getUsername(), null, 
				encoderBundleEntity.getBundleId(), encoderBundleEntity.getBundleName(), encoderBundleEntity.getBundleType(),
				encoderBundleEntity.getAccessNodeUid(), encoderVideoChannel.getChannelId(), encoderVideoChannel.getBaseType(), 
				encoderAudioChannel.getChannelId(), encoderAudioChannel.getBaseType(), 
				user.getId(), user.getUserNo(), user.getName(), decoderUserPlayer.getBundleId(), 
				decoderUserPlayer.getBundleName(), decoderUserPlayer.getBundleType(), decoderUserPlayer.getLayerId(),
				decoderUserPlayer.getVideoChannelId(), decoderUserPlayer.getVideoBaseType(), decoderUserPlayer.getAudioChannelId(),
				decoderUserPlayer.getAudioBaseType());
		
		commandGroupUserPlayerDao.save(decoderUserPlayer);
		commandVodDao.save(userVod);
		
		decoderUserPlayer.setBusinessId(userVod.getId().toString());
		decoderUserPlayer.setBusinessName("正在点播" + encoderBundleEntity.getBundleName() + "设备");
		
		//点播协议
		LogicBO logic = connectBundle(userVod, codec, admin.getId());
		LogicBO logicCast = commandCastServiceImpl.openBundleCastDevice(null, null, null, new ArrayListWrapper<CommandVodPO>().add(userVod).getList(), null, codec, user.getId());
		logic.merge(logicCast);
		
		executeBusiness.execute(logic, user.getName() + "点播" + encoderBundleEntity.getBundleName() + "设备：");
		
		return decoderUserPlayer;
	}
	
	/**
	 * 点播设备<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 下午2:32:25
	 * @param UserBO user 用户信息
	 * @param String bundleId 设备id
	 * @param UserBO admin 管理员
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	public CommandGroupUserPlayerPO deviceStart_Cascade(UserBO user, String bundleId, UserBO admin, int locationIndex) throws Exception{
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		LogicBO logicCloseOld = null;
		
		//点播--解码（播放器）
		CommandGroupUserPlayerPO decoderUserPlayer = null;
		if(locationIndex == -1){
			decoderUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(user.getId(), PlayerBusinessType.PLAY_DEVICE);
		}else{
			decoderUserPlayer = commandCommonServiceImpl.userChosePlayerByLocationIndex(user.getId(), PlayerBusinessType.PLAY_DEVICE, locationIndex, true);
			
			//先判断该播放器是否已被使用
			String oldBusinessId = decoderUserPlayer.getBusinessId();
			if(oldBusinessId != null){
				
				//播放器已被使用。判断原业务是否是点播
				PlayerBusinessType oldType = decoderUserPlayer.getPlayerBusinessType();
				if(PlayerBusinessType.PLAY_DEVICE.equals(oldType)
						|| PlayerBusinessType.PLAY_USER.equals(oldType)){
					
					//是点播，先判断新旧是否一样
					CommandVodPO oldVod = commandVodDao.findOne(Long.parseLong(oldBusinessId));					
					if(PlayerBusinessType.PLAY_DEVICE.equals(oldType)
							&& oldVod.getSourceBundleId().equals(bundleId)){
						
						log.info("点播设备换源时与原有业务相同，不需处理");
						return decoderUserPlayer;
					}
					
					//停止旧的，点播新的
					decoderUserPlayer.setPlayerBusinessType(PlayerBusinessType.PLAY_DEVICE);
					logicCloseOld = closeBundle(oldVod, codec, admin.getId(), false);
					commandVodDao.delete(oldVod);
					
					
				}else{
					//不是点播业务，不允许
					throw new PlayerIsBeingUsedException();
				}
			}
		}		
		
		//被点播--编码
		List<BundlePO> encoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(bundleId).getList());
		BundlePO encoderBundleEntity = encoderBundleEntities.get(0);
		
		CommandVodPO userVod = null;
		boolean bVodDeviceLdap =  queryUtil.isLdapBundle(encoderBundleEntity);
		
		if(!bVodDeviceLdap){
			List<ChannelSchemeDTO> encoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
			ChannelSchemeDTO encoderVideoChannel = encoderVideoChannels.get(0);
			
			List<ChannelSchemeDTO> encoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
			ChannelSchemeDTO encoderAudioChannel = encoderAudioChannels.get(0);
					
			userVod = new CommandVodPO(
					VodType.DEVICE, null, encoderBundleEntity.getUsername(), null, 
					encoderBundleEntity.getBundleId(), encoderBundleEntity.getBundleName(), encoderBundleEntity.getBundleType(),
					encoderBundleEntity.getAccessNodeUid(), encoderVideoChannel.getChannelId(), encoderVideoChannel.getBaseType(), 
					encoderAudioChannel.getChannelId(), encoderAudioChannel.getBaseType(), 
					user.getId(), user.getUserNo(), user.getName(), decoderUserPlayer.getBundleId(), 
					decoderUserPlayer.getBundleName(), decoderUserPlayer.getBundleType(), decoderUserPlayer.getLayerId(),
					decoderUserPlayer.getVideoChannelId(), decoderUserPlayer.getVideoBaseType(), decoderUserPlayer.getAudioChannelId(),
					decoderUserPlayer.getAudioBaseType());
		}else{
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			String useBundleId = new StringBufferWrapper().append(bundleId).append("_").append(bundleId).toString();
			String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
			String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
			
			userVod = new CommandVodPO(
					VodType.LOCAL_SEE_OUTER_DEVICE, null, encoderBundleEntity.getUsername(), null, 
					useBundleId, encoderBundleEntity.getBundleName(), encoderBundleEntity.getBundleType(),
					localLayerId, videoChannelId, "outer_no_base_type", 
					audioChannelId, "outer_no_base_type", 
					user.getId(), user.getUserNo(), user.getName(), decoderUserPlayer.getBundleId(), 
					decoderUserPlayer.getBundleName(), decoderUserPlayer.getBundleType(), decoderUserPlayer.getLayerId(),
					decoderUserPlayer.getVideoChannelId(), decoderUserPlayer.getVideoBaseType(), decoderUserPlayer.getAudioChannelId(),
					decoderUserPlayer.getAudioBaseType());
		}
		
		commandGroupUserPlayerDao.save(decoderUserPlayer);
		commandVodDao.save(userVod);
		
		decoderUserPlayer.setBusinessId(userVod.getId().toString());
		decoderUserPlayer.setBusinessName("正在点播" + encoderBundleEntity.getBundleName() + "设备");
		
		//点播协议
		LogicBO logic = connectBundle(userVod, codec, admin.getId());
		LogicBO logicCast = commandCastServiceImpl.openBundleCastDevice(null, null, null, new ArrayListWrapper<CommandVodPO>().add(userVod).getList(), null, codec, user.getId());
		logic.merge(logicCast);
		if(logicCloseOld != null) logic.merge(logicCloseOld);
		
		executeBusiness.execute(logic, user.getName() + "点播" + encoderBundleEntity.getBundleName() + "设备：");
		
		return decoderUserPlayer;
	}

	/**
	 * 停止点播设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月25日 下午2:39:20
	 * @param UserBO user 用户
	 * @param Long businessId 业务id
	 * @param UserBO admin 管理员
//	 * @param changeSource 慎用！一般都用false；换源业务时使用true，不改播放器，不挂播放器
	 */
	public CommandGroupUserPlayerPO deviceStop(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		CommandVodPO vod = commandVodDao.findOne(businessId);
		
		if(vod == null){
			throw new BaseException(StatusCode.FORBIDDEN, "设备点播不存在！");
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		LogicBO logic = closeBundle(vod, codec, admin.getId(), true);
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(vod.getDstUserId());
		CommandGroupUserPlayerPO player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.PLAY_DEVICE, businessId.toString());
		player.setPlayerBusinessType(PlayerBusinessType.NONE);
		player.setBusinessId(null);
		player.setBusinessName(null);
		commandGroupUserPlayerDao.save(player);
		
		commandVodDao.delete(vod);		

		LogicBO logicCast = commandCastServiceImpl.closeBundleCastDevice(
				null, new ArrayListWrapper<CommandVodPO>().add(vod).getList(), null, 
				new ArrayListWrapper<CommandGroupUserPlayerPO>().add(player).getList(), null, user.getId());
		logic.merge(logicCast);
		executeBusiness.execute(logic, user.getName() + "停止点播设备");
		
		return player;
		
	}
	
	/**
	 * 用户看自己的编码器<br/>
	 * <p>与“点播用户”userStart()方法基本相同，区别是选用最后一个可用的播放器</p>
	 * <p>VodType为USER_ONESELF，PlayerBusinessType为PLAY_USER_ONESELF，都是为了查询</p>
	 * <p>会先校验该用户是否有播放器已经在观看本地视频，如果有则抛错</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月29日 上午9:10:26
	 * @param user
	 * @param admin
	 * @return
	 * @throws Exception 如果已经存在“观看本地视频”的业务，则会抛错
	 */
	public CommandGroupUserPlayerPO seeOneselfUserStart(UserBO user, UserBO admin) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		CommandGroupUserPlayerPO selfPlayer = commandCommonUtil.queryPlayerByPlayerBusinessType(userInfo.obtainUsingSchemePlayers(), PlayerBusinessType.PLAY_USER_ONESELF);
		if(selfPlayer != null){
			throw new BaseException(StatusCode.FORBIDDEN, "已经在观看本地视频");
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		//被点播--编码
		List<BundlePO> encoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user)).getList());
		BundlePO encoderBundleEntity = encoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> encoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO encoderVideoChannel = encoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> encoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO encoderAudioChannel = encoderAudioChannels.get(0);
		
		//选最后一个播放器，选不到则抛错
		CommandGroupUserPlayerPO decoderUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(user.getId(), PlayerBusinessType.PLAY_USER_ONESELF, -1, false);
		
		CommandVodPO userVod = new CommandVodPO(
				VodType.USER_ONESELF, user.getId(), user.getUserNo(), user.getName(), 
				encoderBundleEntity.getBundleId(), encoderBundleEntity.getBundleName(), encoderBundleEntity.getBundleType(),
				encoderBundleEntity.getAccessNodeUid(), encoderVideoChannel.getChannelId(), encoderVideoChannel.getBaseType(), 
				encoderAudioChannel.getChannelId(), encoderAudioChannel.getBaseType(), 
				user.getId(), user.getUserNo(), user.getName(), decoderUserPlayer.getBundleId(), 
				decoderUserPlayer.getBundleName(), decoderUserPlayer.getBundleType(), decoderUserPlayer.getLayerId(),
				decoderUserPlayer.getVideoChannelId(), decoderUserPlayer.getVideoBaseType(), decoderUserPlayer.getAudioChannelId(),
				decoderUserPlayer.getAudioBaseType());
		
		commandVodDao.save(userVod);
		
		decoderUserPlayer.setBusinessId(userVod.getId().toString());
		decoderUserPlayer.setBusinessName("本地视频预览");
		commandGroupUserPlayerDao.save(decoderUserPlayer);
		
		//点播协议
		LogicBO logic = connectBundle(userVod, codec, admin.getId());
		LogicBO logicCast = commandCastServiceImpl.openBundleCastDevice(null, null, null, new ArrayListWrapper<CommandVodPO>().add(userVod).getList(), null, codec, user.getId());
		logic.merge(logicCast);
		
		executeBusiness.execute(logic, user.getName() + "用户观看自己");
		
		return decoderUserPlayer;
	}

	/**
	 * 用户看自己的编码器<br/>
	 * <p>与“点播用户”userStart()方法基本相同，区别是选用最后一个可用的播放器</p>
	 * <p>VodType为USER_ONESELF，PlayerBusinessType为PLAY_USER_ONESELF，都是为了查询</p>
	 * <p>会先校验该用户是否有播放器已经在观看本地视频，如果有则抛错</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月29日 上午9:10:26
	 * @param user
	 * @param admin
	 * @return
	 * @throws Exception 如果已经存在“观看本地视频”的业务，则会抛错
	 */
	public CommandGroupUserPlayerPO seeOneselfLocalStart(UserBO user) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		CommandGroupUserPlayerPO selfPlayer = commandCommonUtil.queryPlayerByPlayerBusinessType(userInfo.obtainUsingSchemePlayers(), PlayerBusinessType.PLAY_USER_ONESELF);
		if(selfPlayer != null){
			throw new BaseException(StatusCode.FORBIDDEN, "已经存在本地");
		}
		
		//选最后一个播放器，选不到则抛错
		selfPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(user.getId(), PlayerBusinessType.PLAY_USER_ONESELF, -1, false);		
		selfPlayer.setBusinessId("0");//需要写一个假的businessId
		selfPlayer.setBusinessName("本地视频预览");
		commandGroupUserPlayerDao.save(selfPlayer);
		
		return selfPlayer;
	}
	
	public void seeOneselfLocalStop(UserBO user) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		CommandGroupUserPlayerPO selfPlayer = commandCommonUtil.queryPlayerByPlayerBusinessType(userInfo.obtainUsingSchemePlayers(), PlayerBusinessType.PLAY_USER_ONESELF);
		if(selfPlayer == null){
			throw new BaseException(StatusCode.FORBIDDEN, "已经在观看本地视频");
		}
		
		selfPlayer = commandCommonUtil.queryPlayerByPlayerBusinessType(userInfo.obtainUsingSchemePlayers(), PlayerBusinessType.PLAY_USER_ONESELF);
		if(selfPlayer == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有本地视频预览");
		}
		
		selfPlayer.setFree();
		commandGroupUserPlayerDao.save(selfPlayer);
	}
	
	public CommandGroupUserPlayerPO recordVodStart(UserBO user, String businessType, String businessInfo, String url, int locationIndex) throws Exception{
		
		//占用播放器
		CommandGroupUserPlayerPO player = null;
		if(locationIndex == -1){
			player = commandCommonServiceImpl.userChoseUsefulPlayer(user.getId(), PlayerBusinessType.PLAY_RECORD);
		}else{
			player = commandCommonServiceImpl.userChosePlayerByLocationIndex(user.getId(), PlayerBusinessType.PLAY_RECORD, locationIndex);
		}
		
		player.setBusinessName(businessInfo);
		player.setPlayUrl(url);		
		player.setBusinessId("-1");
		
		commandGroupUserPlayerDao.save(player);
		
		return player;
	}

	
	public CommandGroupUserPlayerPO recordVodStop(UserBO user, int serial) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(userInfo.getPlayers(), serial);		
		player.setFree();
		commandGroupUserPlayerDao.save(player);
		
		return player;		
	}

	/**
	 * 点播呼叫协议生成<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 下午3:12:16
	 * @param CommandVodPO vod 点播信息
	 * @param CodecParamBO codec 参数
	 * @param Long userId 用户id
	 * @return LogicBO 协议
	 */
	private LogicBO connectBundle(
			CommandVodPO vod,
			CodecParamBO codec,
			Long userId) throws Exception{		
		
		VodType vodType = vod.getVodType();
				
		//呼叫设备
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
				 			 		 .setPass_by(new ArrayList<PassByBO>());
		
		if(vodType==null || vodType.equals(VodType.USER) || vodType.equals(VodType.DEVICE)){			
			//呼叫编码
			ConnectBundleBO connectEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
															            .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																	    .setLock_type("write")
																	    .setBundleId(vod.getSourceBundleId())
																	    .setLayerId(vod.getSourceLayerId())
																	    .setBundle_type(vod.getSourceBundleType());
			ConnectBO connectEncoderVideoChannel = new ConnectBO().setChannelId(vod.getSourceVideoChannelId())
															      .setChannel_status("Open")
															      .setBase_type(vod.getSourceVideoBaseType())
															      .setCodec_param(codec);
			ConnectBO connectEncoderAudioChannel = new ConnectBO().setChannelId(vod.getSourceAudioChannelId())
															      .setChannel_status("Open")
															      .setBase_type(vod.getSourceAudioBaseType())
															      .setCodec_param(codec);
			
			connectEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectEncoderVideoChannel).add(connectEncoderAudioChannel).getList());
			logic.getConnectBundle().add(connectEncoderBundle);			
		}else if(vodType.equals(VodType.LOCAL_SEE_OUTER_USER)){
			//点播外部用户，passby拉流
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
								 .setUuid(vod.getUuid())
								 .setSrc_user(vod.getDstUserNo())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", vod.getSourceLayerId())
										 											.put("bundleid", vod.getSourceBundleId())
										 											.put("video_channelid", vod.getSourceVideoChannelId())
										 											.put("audio_channelid", vod.getSourceAudioChannelId())
										 											.getMap())
								 .setDst_number(vod.getSourceNo())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
		}else if(vodType.equals(VodType.LOCAL_SEE_OUTER_DEVICE)){
			//点播外部设备，passby拉流
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
								 .setUuid(vod.getUuid())
								 .setSrc_user(vod.getDstUserNo())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", vod.getSourceLayerId())
										 											.put("bundleid", vod.getSourceBundleId())
										 											.put("video_channelid", vod.getSourceVideoChannelId())
										 											.put("audio_channelid", vod.getSourceAudioChannelId())
										 											.getMap())
								 .setDst_number(vod.getSourceNo())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
		}
		
		//呼叫解码
		ConnectBundleBO connectDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//		  													        .setOperateType(ConnectBundleBO.OPERATE_TYPE)
		  													        .setLock_type("write")
		  													        .setBundleId(vod.getDstBundleId())
		  													        .setLayerId(vod.getDstLayerId())
		  													        .setBundle_type(vod.getDstBundleType());
		ForwardSetSrcBO decoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
																      .setBundleId(vod.getSourceBundleId())
																      .setLayerId(vod.getSourceLayerId())
																      .setChannelId(vod.getSourceVideoChannelId());
		ConnectBO connectDecoderVideoChannel = new ConnectBO().setChannelId(vod.getDstVideoChannelId())
													          .setChannel_status("Open")
													          .setBase_type(vod.getDstVideoBaseType())
													          .setCodec_param(codec)
													          .setSource_param(decoderVideoForwardSet);
		ForwardSetSrcBO decoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
														 	    	  .setBundleId(vod.getSourceBundleId())
														 	    	  .setLayerId(vod.getSourceLayerId())
														 	    	  .setChannelId(vod.getSourceAudioChannelId());
		ConnectBO connectDecoderAudioChannel = new ConnectBO().setChannelId(vod.getDstAudioChannelId())
															  .setChannel_status("Open")
															  .setBase_type(vod.getDstAudioBaseType())
															  .setCodec_param(codec)
															  .setSource_param(decoderAudioForwardSet);
		
		connectDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDecoderVideoChannel).add(connectDecoderAudioChannel).getList());
		logic.getConnectBundle().add(connectDecoderBundle);
		
		return logic;
	}
	
	/**
	 * 点播挂断协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 下午5:23:22
	 * @param vod 点播信息
	 * @param codec 点播信息
	 * @param userId adminId
	 * @param closePlayer 是否关闭播放器，通常true，在换源业务时使用false
	 * @return LogicBO 协议
	 */
	private LogicBO closeBundle(
			CommandVodPO vod,
			CodecParamBO codec,
			Long userId,
			boolean closePlayer) throws Exception{
	
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
									 .setPass_by(new ArrayList<PassByBO>());
		
		VodType vodType = vod.getVodType();
		if(vodType == null || vodType.equals(VodType.DEVICE)
				|| vodType.equals(VodType.USER)
				|| vodType.equals(VodType.USER_ONESELF)){
			//关闭被叫用户设备
			DisconnectBundleBO disconnectEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																	             .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																	             .setBundleId(vod.getSourceBundleId())
																	             .setBundle_type(vod.getSourceBundleType())
																	             .setLayerId(vod.getSourceLayerId());
			logic.getDisconnectBundle().add(disconnectEncoderBundle);
		}else if(vodType.equals(VodType.LOCAL_SEE_OUTER_DEVICE)){
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
								 .setUuid(vod.getUuid())
								 .setSrc_user(vod.getDstUserNo())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", vod.getSourceLayerId())
										 											.put("bundleid", vod.getSourceBundleId())
										 											.put("video_channelid", vod.getSourceVideoChannelId())
										 											.put("audio_channelid", vod.getSourceAudioChannelId())
										 											.getMap())
								 .setDst_number(vod.getSourceNo())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
		}else if(vodType.equals(VodType.LOCAL_SEE_OUTER_USER)){
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
								 .setUuid(vod.getUuid())
								 .setSrc_user(vod.getDstUserNo())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", vod.getSourceLayerId())
										 											.put("bundleid", vod.getSourceBundleId())
										 											.put("video_channelid", vod.getSourceVideoChannelId())
										 											.put("audio_channelid", vod.getSourceAudioChannelId())
										 											.getMap())
								 .setDst_number(vod.getSourceNo())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
		}
		
		if(closePlayer){
			//解码端一定是本地播放器
			DisconnectBundleBO disconnectDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
	//																           	 .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																	           	 .setBundleId(vod.getDstBundleId())
																	           	 .setBundle_type(vod.getDstBundleType())
																	           	 .setLayerId(vod.getDstLayerId());
			
			
			logic.getDisconnectBundle().add(disconnectDecoderBundle);
		}
		
		return logic;
	
	}
}
