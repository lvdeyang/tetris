package com.sumavision.bvc.device.command.cast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.sumavision.bvc.command.group.dao.CommandGroupDecoderScreenDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandVodDAO;
import com.sumavision.bvc.command.group.enumeration.MediaType;
import com.sumavision.bvc.command.group.enumeration.SrcType;
import com.sumavision.bvc.command.group.enumeration.VodType;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderScreenPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.basic.osd.CommandOsdServiceImpl;
import com.sumavision.bvc.device.command.bo.PlayerInfoBO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.UserHasNoAvailableEncoderException;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardDelBO;
import com.sumavision.bvc.device.group.bo.ForwardSetBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.MediaPushSetBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandDecoderServiceImpl 
* @Description: 解码器上屏业务。不在class上加事物
* @author zsy
* @date 2020年5月12日 上午10:56:48
*
 */
@Slf4j
//@Transactional(rollbackFor = Exception.class)
@Service
public class CommandDecoderServiceImpl {
	
	@Autowired
	private CommandGroupDecoderScreenDAO commandGroupDecoderScreenDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
		
	@Autowired
	private BundleDao bundleDao;
		
	@Autowired
	private CommandVodDAO commandVodDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private CommandVodService commandVodService;
	
	@Autowired
	private CommandOsdServiceImpl commandOsdServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private CommonQueryUtil commonQueryUtil;

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
		
	/**
	 * 从播放器给分屏上屏<br/>
	 * <p>拖动一个播放器到分屏区域</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:51:42
	 * @param userId
	 * @param locationIndex 播放器序号
	 * @param screenId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public CommandGroupDecoderScreenPO fromPlayerToScreen(Long userId, int locationIndex, Long screenId) throws Exception{
		
		CommandGroupDecoderScreenPO screen = commandGroupDecoderScreenDao.findOne(screenId);
		if(screen == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到分屏，id: " + screenId);
		}
		List<CommandGroupUserPlayerCastDevicePO> castDevices = screen.getCastDevices();
		if(castDevices==null || castDevices.size()==0){
			screen.setCastDevices(new ArrayList<CommandGroupUserPlayerCastDevicePO>());
			castDevices = screen.getCastDevices();
			throw new BaseException(StatusCode.FORBIDDEN, "该分屏没有绑定解码设备");
		}
		
		List<String> bundleIds = new ArrayList<String>();
		for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
			bundleIds.add(castDevice.getDstBundleId());
		}
		
		//查出播放器和当前业务
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserPlayerPO player = commandCommonUtil.queryPlayerByLocationIndex(userInfo.getPlayers(), locationIndex);		
		PlayerInfoBO playerInfoBO = commandCastServiceImpl.changeCastDevices2(player, null, null, false, true, true);
		
//		//创建点播任务
		startFromPlayer(userId, player, playerInfoBO, bundleIds);
//		
//		//字幕
		Long osdId = playerInfoBO.getOsdId();
		if(osdId != null){
			commandOsdServiceImpl.setOsd(castDevices, playerInfoBO.getSrcCode(), playerInfoBO.getSrcInfo(), osdId);
		}else{
			commandOsdServiceImpl.clearOsd(castDevices);
		}
		
		//给screen补充参数
		screen.setSrcCode(playerInfoBO.getSrcCode());
		screen.setSrcInfo(playerInfoBO.getSrcInfo());
		screen.setBusinessInfo(playerInfoBO.getSrcInfo());
		screen.setBusinessType(playerInfoBO.getSrcType());
		screen.setOsdId(osdId);
		screen.setOsdName(playerInfoBO.getOsdName());
		screen.setPlayUrl(playerInfoBO.getPlayUrl());
		
		commandGroupDecoderScreenDao.save(screen);
		return screen;
	}
	
	/**
	 * 从播放器给解码器上屏<br/>
	 * <p>与“分屏”无关</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:53:18
	 * @param userId
	 * @param player
	 * @param playerInfoBO
	 * @param bundleIds 解码器id列表
	 * @return
	 * @throws Exception
	 */
	private List<CommandVodPO> startFromPlayer(Long userId, CommandGroupUserPlayerPO player, PlayerInfoBO playerInfoBO, List<String> bundleIds) throws Exception{
		
		log.info(player.getLocationIndex() + "序号的播放器进行设备上屏，解码器bundleId个数：" + bundleIds.size());
		
		UserBO userBO = userUtils.queryUserById(userId);
		
		//从播放器找当前业务
//		PlayerInfoBO playerInfoBO = commandCastServiceImpl.changeCastDevices2(player, null, null, false, true, true);
		String code = playerInfoBO.getSrcCode();
		if(!playerInfoBO.isHasBusiness()){
			throw new BaseException(StatusCode.FORBIDDEN, "没有可以上屏的内容");
		}
		
		//各种已有任务都停止，但不挂断解码器.（编码器也不会被close，因为播放器在播放，计数大于1）
//		boolean hasSame = false;
		List<CommandVodPO> vods1 = commandVodDao.findByDstBundleIdIn(bundleIds);
		stopVods(userBO, vods1, false);
//		for(CommandVodPO vod : vods1){
//			if(SrcType.FILE.equals(playerInfoBO.getSrcType())){
//				if(vod.getPlayUrl().equals(playerInfoBO.getPlayUrl())){
//					//不变
//					hasSame = true;
//					break;
//				}else{
//					//停止（考虑改成批量）
//					resourceVodStop(userBO, vod, false);
//				}
//			}else if(SrcType.USER.equals(playerInfoBO.getSrcType())){
//				if(vod.getSourceNo().equals(playerInfoBO.getSrcCode())){
//					//不变
//					hasSame = true;
//					break;
//				}else{
//					//停止（考虑改成批量）
//					userStop(userBO, vod, false);
//				}
//			}else if(SrcType.DEVICE.equals(playerInfoBO.getSrcType())){
//				if(vod.getSourceNo().equals(playerInfoBO.getSrcCode())){
//					//不变
//					hasSame = true;
//					break;
//				}else{
//					//停止（考虑改成批量）
//					deviceStop(userBO, vod, false);
//				}
//			}
//		}
//		if(hasSame) return vods1;
		
		//文件上屏
		if(SrcType.FILE.equals(playerInfoBO.getSrcType())){			
			List<CommandVodPO> vods = resourceVodStart(userBO, bundleIds, playerInfoBO.getPlayUrl(), playerInfoBO.getSrcInfo());			
			return vods;
		}
		
		UserBO srcUser = userUtils.queryUserByUserno(code);
		UserBO admin = new UserBO(); admin.setId(-1L);
		if(srcUser != null){
			//用户上屏Long id = userUtils.getUserIdFromSession(request);			
			List<CommandVodPO> vods = userStart(userBO, bundleIds, srcUser);
			return vods;
		}else{
			//设备上屏
			BundlePO bundle = bundleDao.findByUsername(code);
			String bundleId = bundle.getBundleId();
			List<CommandVodPO> vods = deviceStart(userBO, bundleIds, bundleId);
			return vods;
			//TODO:鉴权
		}
		
//		return null;
		//下发字幕放在上层调用中了

	}
	
	/**
	 * 停止一个分屏的全部上屏<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:54:29
	 * @param userId
	 * @param screenId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public CommandGroupDecoderScreenPO stopScreen(Long userId, Long screenId) throws Exception{
		
		CommandGroupDecoderScreenPO screen = commandGroupDecoderScreenDao.findOne(screenId);
		if(screen == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到分屏，id: " + screenId);
		}
		SrcType businessType = screen.getBusinessType();
		if(SrcType.NONE.equals(businessType)){
			throw new BaseException(StatusCode.FORBIDDEN, "当前分屏没有上屏任务");
		}
		
		List<CommandGroupUserPlayerCastDevicePO> castDevices = screen.getCastDevices();
		
		List<String> bundleIds = new ArrayList<String>();
		for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
			bundleIds.add(castDevice.getDstBundleId());
		}		
		
		//停止相关的点播任务
		UserBO user = userUtils.queryUserById(userId);
		List<CommandVodPO> vods = commandVodDao.findByDstBundleIdIn(bundleIds);
		stopVods(user, vods, true);
		
		//字幕
		Long osdId = screen.getOsdId();
		if(osdId != null){
			commandOsdServiceImpl.clearOsd(castDevices);
		}
		
		screen.setFree();
		commandGroupDecoderScreenDao.save(screen);
		
		return screen;
	}
	
	/**
	 * 停止多个点播任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:54:49
	 * @param user
	 * @param vods
	 * @param closeDecoder 默认true；当换源时使用false，不挂断解码器
	 * @throws Exception
	 */
	public void stopVods(UserBO user, Collection<CommandVodPO> vods, boolean closeDecoder) throws Exception{
		
		if(vods == null) return;
		
		for(CommandVodPO vod : vods){
			VodType vodType = vod.getVodType();
			if(VodType.FILE.equals(vodType)){
				resourceVodStop(user, vod, closeDecoder);
			}else if(VodType.USER.equals(vodType) || VodType.LOCAL_SEE_OUTER_USER.equals(vodType)){
				userStop(user, vod, closeDecoder);
			}else if(VodType.DEVICE.equals(vodType) || VodType.LOCAL_SEE_OUTER_DEVICE.equals(vodType)){
				deviceStop(user, vod, closeDecoder);
			}
		}		
	}
	
	/**
	 * 将文件给多个解码器上屏<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:55:40
	 * @param user 操作人
	 * @param bundleIds 解码器
	 * @param playUrl 播放地址
	 * @param name 文件名，用于显示
	 * @return
	 * @throws Exception
	 */
	public List<CommandVodPO> resourceVodStart(UserBO user, List<String> bundleIds, String playUrl, String name) throws Exception{
		
		//解码器
		List<BundlePO> dstBundleEntities = resourceBundleDao.findByBundleIds(bundleIds);
		if(dstBundleEntities == null) dstBundleEntities = new ArrayList<BundlePO>();
		
		List<CommandVodPO> fileVods = new ArrayList<CommandVodPO>();
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO logic = new LogicBO().setUserId("-1")
		 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
		 		 .setForwardSet(new ArrayList<ForwardSetBO>())
		 		 .setForwardDel(new ArrayList<ForwardDelBO>())
		 		 .setMediaPushSet(new ArrayList<MediaPushSetBO>());
		
		for(BundlePO dstBundle : dstBundleEntities){
			CommandVodPO fileVod = new CommandVodPO(
					VodType.FILE, null, null, name, 
					null, name, "file_no_bundle_type",
					null, null, "file_no_base_type", 
					null, "file_no_base_type", 
					user.getId(), user.getUserNo(), user.getName(), dstBundle.getBundleId(), 
					dstBundle.getBundleName(), dstBundle.getBundleType(), dstBundle.getAccessNodeUid(),
					ChannelType.VIDEODECODE1.getChannelId(), "VenusVideoOut", ChannelType.AUDIODECODE1.getChannelId(),
					"VenusAudioOut");
			
			fileVod.setPlayUrl(playUrl);
			fileVod.setVodType(VodType.FILE);
			fileVods.add(fileVod);
			
			//转发文件，使用mediaPush（vod模块负责运行）这个uuid要在关闭时一致
			String mediaPushUuid = fileVod.getUuid();
			
			ForwardSetBO forwardVideo = new ForwardSetBO()
					.setByMediapushAndDstBundle(mediaPushUuid, dstBundle, codec, MediaType.VIDEO);
			
			ForwardSetBO forwardAudio = new ForwardSetBO()
					.setByMediapushAndDstBundle(mediaPushUuid, dstBundle, codec, MediaType.AUDIO);
			
			logic.getForwardSet().add(forwardVideo);
			logic.getForwardSet().add(forwardAudio);
			
			//mediaPushSet协议
			MediaPushSetBO mediaPushSet = new MediaPushSetBO()
					.setUuid(mediaPushUuid)
					.setFile_source(playUrl)
					.setCodec_param(codec);
			logic.getMediaPushSet().add(mediaPushSet);
		}
		
		commandVodDao.save(fileVods);
		
		executeBusiness.execute(logic, user.getName() + "把“" + name + "”文件上屏到解码器：" + bundleIds);
		
		return fileVods;
	}
	
	/**
	 * 停止文件上屏任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:56:54
	 * @param user 操作人
	 * @param vod
	 * @param closeDecoder
	 * @throws Exception
	 */
	public void resourceVodStop(UserBO user, CommandVodPO vod, boolean closeDecoder) throws Exception{
		
		//参数模板
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		
		//这里只生成挂断解码器的协议 TODO:测试
		LogicBO logic = commandVodService.closeBundle(vod, codec, -1L, closeDecoder);
		
		//生成一个删除mediaPush的
		String mediaPushUuid = vod.getUuid();
		logic.getMediaPushDel().add(new MediaPushSetBO().setUuid(mediaPushUuid));
		
		executeBusiness.execute(logic, user.getName() + "停止上屏任务：点播文件 " + vod.getSourceUserName());
		
		commandVodDao.delete(vod);
	}
	
	/**
	 * 将用户上屏给多个解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:57:28
	 * @param user 操作人
	 * @param bundleIds 解码器
	 * @param vodUser 被观看的用户
	 * @return
	 * @throws Exception
	 */
	public List<CommandVodPO> userStart(UserBO user, List<String> bundleIds, UserBO vodUser) throws Exception{
		
		//对bundleIds做去空、去重处理
		
		FolderUserMap vodUserfolderUserMap = folderUserMapDao.findByUserId(vodUser.getId());
		boolean bVodUserLdap = queryUtil.isLdapUser(user, vodUserfolderUserMap);
		
		//参数模板
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		
		//解码器
		List<BundlePO> dstBundleEntities = resourceBundleDao.findByBundleIds(bundleIds);
		if(dstBundleEntities == null) dstBundleEntities = new ArrayList<BundlePO>();
		
		List<CommandVodPO> userVods = new ArrayList<CommandVodPO>();
		
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

			for(BundlePO dstBundle : dstBundleEntities){
				
				CommandVodPO userVod = new CommandVodPO(
						VodType.USER, vodUser.getId(), vodUser.getUserNo(), vodUser.getName(), 
						encoderBundleEntity.getBundleId(), encoderBundleEntity.getBundleName(), encoderBundleEntity.getBundleType(),
						encoderBundleEntity.getAccessNodeUid(), encoderVideoChannel.getChannelId(), encoderVideoChannel.getBaseType(), 
						encoderAudioChannel.getChannelId(), encoderAudioChannel.getBaseType(), 
						user.getId(), user.getUserNo(), user.getName(), dstBundle.getBundleId(), 
						dstBundle.getBundleName(), dstBundle.getBundleType(), dstBundle.getAccessNodeUid(),
						ChannelType.VIDEODECODE1.getChannelId(), "VenusVideoOut", ChannelType.AUDIODECODE1.getChannelId(),
						"VenusAudioOut");
				
				userVod.setDstDeviceType(DstDeviceType.DEVICE);
				userVods.add(userVod);
			}
		}else{
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			String bundleId = UUID.randomUUID().toString().replace("-", "");
			String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
			String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
			
			for(BundlePO dstBundle : dstBundleEntities){
				CommandVodPO userVod = new CommandVodPO(
					VodType.LOCAL_SEE_OUTER_USER, vodUser.getId(), vodUser.getUserNo(), vodUser.getName(), 
					bundleId, vodUser.getName()+"用户源", "outer_no_bundle_type",
					localLayerId, videoChannelId, "outer_no_base_type", 
					audioChannelId, "outer_no_base_type", 
					user.getId(), user.getUserNo(), user.getName(), dstBundle.getBundleId(), 
					dstBundle.getBundleName(), dstBundle.getBundleType(), dstBundle.getAccessNodeUid(),
					ChannelType.VIDEODECODE1.getChannelId(), "VenusVideoOut", ChannelType.AUDIODECODE1.getChannelId(),
					"VenusAudioOut");
				
				userVod.setDstDeviceType(DstDeviceType.DEVICE);
				userVods.add(userVod);
			}
		}
		
		commandVodDao.save(userVods);
		
		//点播协议
		LogicBO logic = new LogicBO();
		for(CommandVodPO userVod : userVods){
			LogicBO logic1 = commandVodService.connectBundle(userVod, codec, -1L);
			logic.merge(logic1);
		}
		
		executeBusiness.execute(logic, user.getName() + "把" + vodUser.getName() + "用户上屏到解码器：" + bundleIds);
		
		return userVods;
	}
	
	/**
	 * 停止用户上屏任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:58:27
	 * @param user
	 * @param vod
	 * @param closeDecoder
	 * @throws Exception
	 */
	public void userStop(UserBO user, CommandVodPO vod, boolean closeDecoder) throws Exception{
		
		//参数模板
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO logic = commandVodService.closeBundle(vod, codec, -1L, closeDecoder);
		
		executeBusiness.execute(logic, user.getName() + "停止上屏任务：点播用户 " + vod.getSourceUserName());
		
		commandVodDao.delete(vod);
	}
	
	/**
	 * 将设备上屏给多个解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:58:47
	 * @param user 操作人
	 * @param bundleIds 解码器
	 * @param bundleId 被观看的设备
	 * @return
	 * @throws Exception
	 */
	public List<CommandVodPO> deviceStart(UserBO user, List<String> bundleIds, String bundleId) throws Exception{
		
//		commandCommonServiceImpl.authorizeBundle(bundleId, user.getId(), BUSINESS_OPR_TYPE.DIANBO);
		
		//参数模板
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		
		//解码器
		List<BundlePO> dstBundleEntities = resourceBundleDao.findByBundleIds(bundleIds);
		if(dstBundleEntities == null) dstBundleEntities = new ArrayList<BundlePO>();
		
		//被点播--编码
		List<BundlePO> encoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(bundleId).getList());
		BundlePO encoderBundleEntity = encoderBundleEntities.get(0);
		
//		CommandVodPO userVod = null;
		List<CommandVodPO> deviceVods = new ArrayList<CommandVodPO>();
		boolean bVodDeviceLdap =  queryUtil.isLdapBundle(encoderBundleEntity);
		
		if(!bVodDeviceLdap){
			List<ChannelSchemeDTO> encoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
			ChannelSchemeDTO encoderVideoChannel = encoderVideoChannels.get(0);
			
			List<ChannelSchemeDTO> encoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
			ChannelSchemeDTO encoderAudioChannel = encoderAudioChannels.get(0);

			for(BundlePO dstBundle : dstBundleEntities){
				CommandVodPO deviceVod = new CommandVodPO(
					VodType.DEVICE, null, encoderBundleEntity.getUsername(), null, 
					encoderBundleEntity.getBundleId(), encoderBundleEntity.getBundleName(), encoderBundleEntity.getBundleType(),
					encoderBundleEntity.getAccessNodeUid(), encoderVideoChannel.getChannelId(), encoderVideoChannel.getBaseType(), 
					encoderAudioChannel.getChannelId(), encoderAudioChannel.getBaseType(), 
					user.getId(), user.getUserNo(), user.getName(), dstBundle.getBundleId(), 
					dstBundle.getBundleName(), dstBundle.getBundleType(), dstBundle.getAccessNodeUid(),
					ChannelType.VIDEODECODE1.getChannelId(), "VenusVideoOut", ChannelType.AUDIODECODE1.getChannelId(),
					"VenusAudioOut");
				
				deviceVod.setDstDeviceType(DstDeviceType.DEVICE);
				deviceVods.add(deviceVod);
			}
		}else{
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			String useBundleId = new StringBufferWrapper().append(bundleId).append("_").append(bundleId).toString();
			String videoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
			String audioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
			
			for(BundlePO dstBundle : dstBundleEntities){
				CommandVodPO deviceVod = new CommandVodPO(
						VodType.LOCAL_SEE_OUTER_DEVICE, null, encoderBundleEntity.getUsername(), null, 
						useBundleId, encoderBundleEntity.getBundleName(), encoderBundleEntity.getBundleType(),
						localLayerId, videoChannelId, "outer_no_base_type", 
						audioChannelId, "outer_no_base_type", 
						user.getId(), user.getUserNo(), user.getName(), dstBundle.getBundleId(), 
						dstBundle.getBundleName(), dstBundle.getBundleType(), dstBundle.getAccessNodeUid(),
						ChannelType.VIDEODECODE1.getChannelId(), "VenusVideoOut", ChannelType.AUDIODECODE1.getChannelId(),
						"VenusAudioOut");
				
				deviceVod.setDstDeviceType(DstDeviceType.DEVICE);
				deviceVods.add(deviceVod);
			}
		}
		
		commandVodDao.save(deviceVods);
		
		//点播协议
		LogicBO logic = new LogicBO();
		for(CommandVodPO deviceVod : deviceVods){
			LogicBO logic1 = commandVodService.connectBundle(deviceVod, codec, -1L);
			logic.merge(logic1);
		}
		
		executeBusiness.execute(logic, user.getName() + "把" + encoderBundleEntity.getBundleName() + "设备上屏到解码器：");
		
		return deviceVods;
	}
	
	/**
	 * 停止设备上屏任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:59:26
	 * @param user 操作人
	 * @param vod
	 * @param closeDecoder
	 * @throws Exception
	 */
	public void deviceStop(UserBO user, CommandVodPO vod, boolean closeDecoder) throws Exception{
		
		//参数模板
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		LogicBO logic = commandVodService.closeBundle(vod, codec, -1L, closeDecoder);
		
		executeBusiness.execute(logic, user.getName() + "停止上屏任务：点播设备 " + vod.getSourceBundleName());
		
		commandVodDao.delete(vod);
	}
}
