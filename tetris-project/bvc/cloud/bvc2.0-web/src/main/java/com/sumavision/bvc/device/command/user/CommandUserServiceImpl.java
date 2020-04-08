package com.sumavision.bvc.device.command.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.PlayerBundleBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.dao.UserLiveCallDAO;
import com.sumavision.bvc.command.group.enumeration.CallStatus;
import com.sumavision.bvc.command.group.enumeration.CallType;
import com.sumavision.bvc.command.group.enumeration.SchemeType;
import com.sumavision.bvc.command.group.enumeration.UserCallType;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.UserLiveCallPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.user.layout.scheme.CommandGroupUserLayoutShemePO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.device.command.common.CommandCommonConstant;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.UserDoesNotLoginException;
import com.sumavision.bvc.device.command.exception.UserHasNoAvailableEncoderException;
import com.sumavision.bvc.device.command.exception.UserNotMatchBusinessException;
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
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

@Transactional(rollbackFor = Exception.class)
@Service
public class CommandUserServiceImpl {
	
	/** 发起业务时，synchronized锁的前缀 */
	private static final String lockStartPrefix = "vod-or-call-userId-";
	
	/** 响应、停止业务时，synchronized锁的前缀 */
	private static final String lockProcessPrefix = "vod-or-call-businessId-";
	
	@Autowired 
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired 
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired 
	private ResourceService resourceService;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private UserLiveCallDAO userLiveCallDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/**
	 * 生成一个默认的用户信息，包含一个默认的显示屏幕方案<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年9月26日
	 * @param userId
	 * @param userName 如果不输入，则会向用户管理查询
	 * @param doPersistence 是否持久化（通常为true）
	 * @return CommandGroupUserInfoPO 
	 */
	public CommandGroupUserInfoPO generateDefaultUserInfo(Long userId, String userName, boolean doPersistence) throws Exception{
		
		if(userName==null || userName.equals("")){
			try{
				UserBO userBo = resourceService.queryUserById(userId, TerminalType.QT_ZK);
				userName = userBo.getName();
			}catch(Exception e){
				userName = "未获取";
			}			
		}
		
		CommandGroupUserInfoPO userInfo = new CommandGroupUserInfoPO();
		userInfo.setUserId(userId);
		userInfo.setUserName(userName);
		userInfo.setLayoutSchemes(new ArrayList<CommandGroupUserLayoutShemePO>());
		
		//从资源层获取播放器
		List<PlayerBundleBO> allPlayers = resourceQueryUtil.queryPlayerBundlesByUserId(userId);
		
		if(userInfo.getPlayers() == null) userInfo.setPlayers(new ArrayList<CommandGroupUserPlayerPO>());
		for(int i = 0; i < PlayerSplitLayout.SPLIT_16.getPlayerCount(); i++){
			CommandGroupUserPlayerPO player = new CommandGroupUserPlayerPO().set(allPlayers.get(i));
			player.setLocationIndex(i);
			player.setUserInfo(userInfo);
//			player.setLayoutScheme(layoutScheme);
			userInfo.getPlayers().add(player);
		}
		
		//生成一个16分屏的方案。可以考虑在此生成所有的分屏方案
		CommandGroupUserLayoutShemePO layoutScheme = generateUserLayoutScheme(userInfo, PlayerSplitLayout.SPLIT_16);
		layoutScheme.setIsUsing(true);
		layoutScheme.setType(SchemeType.DEFAULT);
		
		userInfo.getLayoutSchemes().add(layoutScheme);
		
		if(doPersistence){
			commandGroupUserInfoDao.save(userInfo);
		}
		
		return userInfo;
	}
	

	public CommandGroupUserInfoPO updateUserInfoPlayers(CommandGroupUserInfoPO userInfo, boolean doPersistence) throws Exception{
		
		//该用户已保存的播放器
		List<CommandGroupUserPlayerPO> userPlayers = userInfo.getPlayers();
		
		//从资源层获取播放器
		List<PlayerBundleBO> allPlayers = resourceQueryUtil.queryPlayerBundlesByUserId(userInfo.getUserId());
		
		for(PlayerBundleBO playerBO : allPlayers){
			CommandGroupUserPlayerPO userPlayer = commandCommonUtil.queryPlayerByCode(userPlayers, playerBO.getBundleNum());
			if(userPlayer != null){
				userPlayer.set(playerBO);
			}
		}
		
		if(doPersistence){
			commandGroupUserInfoDao.save(userInfo);
		}
		
		return userInfo;
	}
	
	/**
	 * 根据分屏类型，生成一个显示方案<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 上午9:22:32
	 * @param userInfo
	 * @param playerSplitLayout
	 * @return
	 */
	public CommandGroupUserLayoutShemePO generateUserLayoutScheme(CommandGroupUserInfoPO userInfo, PlayerSplitLayout playerSplitLayout){
		CommandGroupUserLayoutShemePO layoutScheme = new CommandGroupUserLayoutShemePO();
		layoutScheme.setName("默认方案");
		layoutScheme.setIsUsing(false);
		layoutScheme.setPlayerSplitLayout(playerSplitLayout);
		layoutScheme.setUserInfo(userInfo);
		
		return layoutScheme;
	}
	
	/**
	 * 用户呼叫用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午9:57:31
	 * @param UserBO callUser 呼叫方用户
	 * @param UserBO calledUser 被呼叫用户
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @return CommandGroupUserPlayerPO 播放器占用信息
	 */
	public CommandGroupUserPlayerPO userCallUser(UserBO callUser, UserBO calledUser, int locationIndex) throws Exception{
		
		if(callUser.getId().equals(calledUser.getId())){
			throw new BaseException(StatusCode.FORBIDDEN, "请选择其他成员进行呼叫");
		}
		
		//被呼叫校验
		if(!calledUser.isLogined()){
			throw new UserDoesNotLoginException(calledUser.getName());
		}
		
		//校验呼叫业务是否已经存在
		UserLiveCallPO exsitCall = userLiveCallDao.findByCalledUserIdAndCallUserId(calledUser.getId(), callUser.getId());
		if(exsitCall != null){
			
			if(exsitCall.getType().equals(UserCallType.VOICE)){
				throw new BaseException(StatusCode.FORBIDDEN, "双方正在语音对讲");
			}
			
			//消息重发
			websocketMessageService.resend(exsitCall.getMessageId());
			
			CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(exsitCall.getCallUserId());
			
			CommandGroupUserPlayerPO exsitCallUserPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_CALL, exsitCall.getId().toString());
			
			return exsitCallUserPlayer;
		}
		
		//呼叫方
		//解码--播放器
		CommandGroupUserPlayerPO callUserPlayer = null;
		if(locationIndex == -1){
			callUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(callUser.getId(), PlayerBusinessType.USER_CALL);
		}else{
			callUserPlayer = commandCommonServiceImpl.userChosePlayerByLocationIndex(callUser.getId(), PlayerBusinessType.USER_CALL, locationIndex);
		}
		
		//被呼叫
		//解码--播放器
		CommandGroupUserPlayerPO calledUserPlayer = null;
		try{
			calledUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(calledUser.getId(), PlayerBusinessType.USER_CALL);
		}catch(Exception e){
			throw new BaseException(StatusCode.FORBIDDEN, "对方用户没有可用的播放器");
		}
		
		//被呼叫
		//编码
		List<BundlePO> calledEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(calledUser)).getList());
		if(calledEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
		BundlePO calledEncoderBundleEntity = calledEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> calledEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		if(calledEncoderVideoChannels.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
		ChannelSchemeDTO calledEncoderVideoChannel = calledEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> calledEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		if(calledEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
		ChannelSchemeDTO calledEncoderAudioChannel = calledEncoderAudioChannels.get(0);
		
		//呼叫方
		//编码
		List<BundlePO> callEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(callUser)).getList());
		if(callEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
		BundlePO callEncoderBundleEntity = callEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> callEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		if(callEncoderVideoChannels.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
		ChannelSchemeDTO callEncoderVideoChannel = callEncoderVideoChannels.get(0);
		
		List<ChannelSchemeDTO> callEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		if(callEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
		ChannelSchemeDTO callEncoderAudioChannel = callEncoderAudioChannels.get(0);
		
		UserLiveCallPO business = new UserLiveCallPO(
				calledUser.getId(), calledUser.getUserNo(), calledUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(),
				calledEncoderBundleEntity.getAccessNodeUid(), calledEncoderVideoChannel.getChannelId(), calledEncoderVideoChannel.getBaseType(),
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(), calledUserPlayer.getBundleId(),
				calledUserPlayer.getBundleName(), calledUserPlayer.getBundleType(), calledUserPlayer.getLayerId(),
				calledUserPlayer.getVideoChannelId(), calledUserPlayer.getVideoBaseType(), calledUserPlayer.getAudioChannelId(),
				calledUserPlayer.getAudioBaseType(), callUser.getId(), callUser.getUserNo(),
				callUser.getName(),callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(),
				callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(), callEncoderVideoChannel.getChannelId(),
				callEncoderVideoChannel.getBaseType(), callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				callUserPlayer.getBundleId(), callUserPlayer.getBundleName(), callUserPlayer.getBundleType(),
				callUserPlayer.getLayerId(), callUserPlayer.getVideoChannelId(), callUserPlayer.getVideoBaseType(),
				callUserPlayer.getAudioChannelId(), callUserPlayer.getAudioBaseType());
		
		business.setType(UserCallType.CALL);
		userLiveCallDao.save(business);
		
		calledUserPlayer.setBusinessId(business.getId().toString());
		calledUserPlayer.setBusinessName(callUser.getName() + "邀请你视频通话");
		commandGroupUserPlayerDao.save(calledUserPlayer);
		
		callUserPlayer.setBusinessId(business.getId().toString());
		callUserPlayer.setBusinessName("正在与" + calledUser.getName() + "双向通话");
		commandGroupUserPlayerDao.save(callUserPlayer);
		
		JSONObject message = new JSONObject();
		message.put("businessType", CommandCommonConstant.MESSAGE_CALL);
		message.put("fromUserId", callUser.getId());
		message.put("fromUserName", callUser.getName());
		message.put("businessId", business.getId());
		message.put("businessInfo", calledUserPlayer.getBusinessName());
		
		//发送呼叫消息
		WebsocketMessageVO ws = websocketMessageService.send(calledUser.getId(), message.toJSONString(), WebsocketMessageType.COMMAND, callUser.getId(), callUser.getName());
		business.setMessageId(ws.getId());
		userLiveCallDao.save(business);
		
		return callUserPlayer;
	}
	
	/**
	 * 用户呼叫用户 - 支持级联<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午3:43:19
	 * @param callUser 呼叫方用户
	 * @param calledUser 被呼叫用户
	 * @param locationIndex 仅当被叫用户为本地用户时，可以指定播放器序号，序号从0起始；-1为自动选择，当被叫用户为外部系统用户时使用-1
	 * @param uuid 外部系统呼入时，联网生成uuid；内部系统使用必须为null
	 * @return CommandGroupUserPlayerPO 播放器占用信息
	 * @throws Exception
	 */
	public CommandGroupUserPlayerPO userCallUser_Cascade(UserBO callUser, UserBO calledUser, int locationIndex, String uuid) throws Exception{
				
		if(callUser.getId().equals(calledUser.getId())){
			throw new BaseException(StatusCode.FORBIDDEN, "请选择其他成员进行呼叫");
		}
		
		//被呼叫校验
		if(!calledUser.isLogined()){
			throw new UserDoesNotLoginException(calledUser.getName());
		}
		
		FolderUserMap callUserfolderUserMap = folderUserMapDao.findByUserId(callUser.getId());
		boolean bCallUserLdap = queryUtil.isLdapUser(callUser, callUserfolderUserMap);
		
		FolderUserMap calledUserfolderUserMap = folderUserMapDao.findByUserId(calledUser.getId());
		boolean bCalledUserLdap = queryUtil.isLdapUser(callUser, calledUserfolderUserMap);
		
		CallType callType = null;
		if(!bCallUserLdap && !bCalledUserLdap){
			callType = CallType.LOCAL_LOCAL;
		}else if(!bCallUserLdap && bCalledUserLdap){
			callType = CallType.LOCAL_OUTER;
		}else if(bCallUserLdap && !bCalledUserLdap){
			callType = CallType.OUTER_LOCAL;//外部系统呼入
		}else{
			throw new BaseException(StatusCode.FORBIDDEN, "不支持外部用户呼叫外部用户");
		}
				
		//校验呼叫业务是否已经存在
		UserLiveCallPO exsitCall = userLiveCallDao.findByCalledUserIdAndCallUserId(calledUser.getId(), callUser.getId());
		if(exsitCall != null){
			
			if(exsitCall.getType().equals(UserCallType.VOICE)){
				throw new BaseException(StatusCode.FORBIDDEN, "双方正在语音对讲！");
			}
			
			//消息重发
			if(exsitCall.getMessageId() != null){
				websocketMessageService.resend(exsitCall.getMessageId());
			}
			
			CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(exsitCall.getCallUserId());
			
			CommandGroupUserPlayerPO exsitCallUserPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_CALL, exsitCall.getId().toString());
			
			return exsitCallUserPlayer;
		}
		
		//呼叫方
		//解码--播放器
		CommandGroupUserPlayerPO callUserPlayer = null;
		BundlePO callEncoderBundleEntity = null;
		ChannelSchemeDTO callEncoderVideoChannel = null;
		ChannelSchemeDTO callEncoderAudioChannel = null;
		if(!bCallUserLdap){
			if(locationIndex == -1){
				callUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(callUser.getId(), PlayerBusinessType.USER_CALL);
			}else{
				callUserPlayer = commandCommonServiceImpl.userChosePlayerByLocationIndex(callUser.getId(), PlayerBusinessType.USER_CALL, locationIndex);
			}
			
			//呼叫方
			//编码
			List<BundlePO> callEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(callUser)).getList());
			if(callEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
			callEncoderBundleEntity = callEncoderBundleEntities.get(0);
			
			List<ChannelSchemeDTO> callEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
			if(callEncoderVideoChannels.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
			callEncoderVideoChannel = callEncoderVideoChannels.get(0);
			
			List<ChannelSchemeDTO> callEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
			if(callEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
			callEncoderAudioChannel = callEncoderAudioChannels.get(0);
			
		}else{
			//TODO:new一个，补充必要参数
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			callUserPlayer = new CommandGroupUserPlayerPO();
			callEncoderBundleEntity = new BundlePO();
			callEncoderBundleEntity.setBundleId(UUID.randomUUID().toString().replace("-", ""));
			callEncoderBundleEntity.setAccessNodeUid(localLayerId);
			callEncoderVideoChannel = new ChannelSchemeDTO().setChannelId(ChannelType.VIDEOENCODE1.getChannelId());
			callEncoderAudioChannel = new ChannelSchemeDTO().setChannelId(ChannelType.AUDIOENCODE1.getChannelId());
		}
		
		//被呼叫
		//解码--播放器
		CommandGroupUserPlayerPO calledUserPlayer = null;
		BundlePO calledEncoderBundleEntity = null;
		ChannelSchemeDTO calledEncoderVideoChannel = null;
		ChannelSchemeDTO calledEncoderAudioChannel = null;
		if(!bCalledUserLdap){
			try{
				calledUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(calledUser.getId(), PlayerBusinessType.USER_CALL);
			}catch(Exception e){
				throw new BaseException(StatusCode.FORBIDDEN, "对方用户没有可用的播放器");
			}
			
			//被呼叫
			//编码
			List<BundlePO> calledEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(calledUser)).getList());
			if(calledEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
			calledEncoderBundleEntity = calledEncoderBundleEntities.get(0);
			
			List<ChannelSchemeDTO> calledEncoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
			if(calledEncoderVideoChannels.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
			calledEncoderVideoChannel = calledEncoderVideoChannels.get(0);
			
			List<ChannelSchemeDTO> calledEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
			if(calledEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
			calledEncoderAudioChannel = calledEncoderAudioChannels.get(0);
			
		}else{
			//TODO:new一个
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			calledUserPlayer = new CommandGroupUserPlayerPO();
			calledEncoderBundleEntity = new BundlePO();
			callEncoderBundleEntity.setBundleId(UUID.randomUUID().toString().replace("-", ""));
			callEncoderBundleEntity.setAccessNodeUid(localLayerId);
			calledEncoderVideoChannel = new ChannelSchemeDTO().setChannelId(ChannelType.VIDEOENCODE1.getChannelId());
			calledEncoderAudioChannel = new ChannelSchemeDTO().setChannelId(ChannelType.AUDIOENCODE1.getChannelId());
		}
		
		UserLiveCallPO business = new UserLiveCallPO(
				calledUser.getId(), calledUser.getUserNo(), calledUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(),
				calledEncoderBundleEntity.getAccessNodeUid(), calledEncoderVideoChannel.getChannelId(), calledEncoderVideoChannel.getBaseType(),
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(), calledUserPlayer.getBundleId(),
				calledUserPlayer.getBundleName(), calledUserPlayer.getBundleType(), calledUserPlayer.getLayerId(),
				calledUserPlayer.getVideoChannelId(), calledUserPlayer.getVideoBaseType(), calledUserPlayer.getAudioChannelId(),
				calledUserPlayer.getAudioBaseType(), callUser.getId(), callUser.getUserNo(),
				callUser.getName(),callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(),
				callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(), callEncoderVideoChannel.getChannelId(),
				callEncoderVideoChannel.getBaseType(), callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				callUserPlayer.getBundleId(), callUserPlayer.getBundleName(), callUserPlayer.getBundleType(),
				callUserPlayer.getLayerId(), callUserPlayer.getVideoChannelId(), callUserPlayer.getVideoBaseType(),
				callUserPlayer.getAudioChannelId(), callUserPlayer.getAudioBaseType());
		
		business.setCallType(callType);
		business.setType(UserCallType.CALL);
		if(uuid != null){
			business.setUuid(uuid);
		}
		userLiveCallDao.save(business);
		
		if(!bCallUserLdap){		
			callUserPlayer.setBusinessId(business.getId().toString());
			callUserPlayer.setBusinessName("与" + calledUser.getName() + "双向通话");
			commandGroupUserPlayerDao.save(callUserPlayer);
		}
		
		if(!bCalledUserLdap){
			calledUserPlayer.setBusinessId(business.getId().toString());
			calledUserPlayer.setBusinessName(callUser.getName() + "邀请你视频通话");
			commandGroupUserPlayerDao.save(calledUserPlayer);
			
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_CALL);
			message.put("fromUserId", callUser.getId());
			message.put("fromUserName", callUser.getName());
			message.put("businessId", business.getId());
			message.put("businessInfo", calledUserPlayer.getBusinessName());
		
			//发送呼叫消息
			WebsocketMessageVO ws = websocketMessageService.send(calledUser.getId(), message.toJSONString(), WebsocketMessageType.COMMAND, callUser.getId(), callUser.getName());
			business.setMessageId(ws.getId());
			userLiveCallDao.save(business);
		}
		
		return callUserPlayer;
	}

	/**
	 * 用户语音对讲<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 上午11:42:19
	 * @param callUser
	 * @param calledUser
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	public CommandGroupUserPlayerPO userVoiceUser(UserBO callUser, UserBO calledUser, int locationIndex) throws Exception{
		
		if(callUser.getId().equals(calledUser.getId())){
			throw new BaseException(StatusCode.FORBIDDEN, "请选择其他成员进行呼叫");
		}
		
		//被呼叫校验
		if(!calledUser.isLogined()){
			throw new UserDoesNotLoginException(calledUser.getName());
		}
		
		//校验呼叫业务是否已经存在
		UserLiveCallPO exsitCall = userLiveCallDao.findByCalledUserIdAndCallUserId(calledUser.getId(), callUser.getId());
		if(exsitCall != null){
			
			if(exsitCall.getType().equals(UserCallType.CALL)){
				throw new BaseException(StatusCode.FORBIDDEN, "双方正在视频通话！");
			}
			
			//消息重发
			websocketMessageService.resend(exsitCall.getMessageId());
			
			CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(exsitCall.getCallUserId());
			
			CommandGroupUserPlayerPO exsitCallUserPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_VOICE, exsitCall.getId().toString());
			
			return exsitCallUserPlayer;
		}
		
		FolderUserMap calledUserfolderUserMap = folderUserMapDao.findByUserId(calledUser.getId());
		boolean bCalledUserLdap = queryUtil.isLdapUser(callUser, calledUserfolderUserMap);
		if(bCalledUserLdap){
			throw new BaseException(StatusCode.FORBIDDEN, "跨系统用户请使用“呼叫”功能");
		}
		
		//呼叫方
		//解码--播放器
		CommandGroupUserPlayerPO callUserPlayer = null;
		if(locationIndex == -1){
			callUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(callUser.getId(), PlayerBusinessType.USER_VOICE);
		}else{
			callUserPlayer = commandCommonServiceImpl.userChosePlayerByLocationIndex(callUser.getId(), PlayerBusinessType.USER_VOICE, locationIndex);
		}
		
		//被呼叫
		//解码--播放器
		CommandGroupUserPlayerPO calledUserPlayer = null;
		try{
			calledUserPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(calledUser.getId(), PlayerBusinessType.USER_VOICE);
		}catch(Exception e){
			throw new BaseException(StatusCode.FORBIDDEN, "对方用户没有可用的播放器");
		}
		
		//被呼叫
		//编码
		List<BundlePO> calledEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(calledUser)).getList());
		if(calledEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
		BundlePO calledEncoderBundleEntity = calledEncoderBundleEntities.get(0);
		
		List<ChannelSchemeDTO> calledEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(calledEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		if(calledEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(calledUser.getName());
		ChannelSchemeDTO calledEncoderAudioChannel = calledEncoderAudioChannels.get(0);
		
		//呼叫方
		//编码
		List<BundlePO> callEncoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(callUser)).getList());
		if(callEncoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
		BundlePO callEncoderBundleEntity = callEncoderBundleEntities.get(0);
				
		List<ChannelSchemeDTO> callEncoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(callEncoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		if(callEncoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(callUser.getName());
		ChannelSchemeDTO callEncoderAudioChannel = callEncoderAudioChannels.get(0);
		
		UserLiveCallPO business = new UserLiveCallPO(
				calledUser.getId(), calledUser.getUserNo(), calledUser.getName(),
				calledEncoderBundleEntity.getBundleId(), calledEncoderBundleEntity.getBundleName(), calledEncoderBundleEntity.getBundleType(),
				calledEncoderBundleEntity.getAccessNodeUid(), null, null,
				calledEncoderAudioChannel.getChannelId(), calledEncoderAudioChannel.getBaseType(), calledUserPlayer.getBundleId(),
				calledUserPlayer.getBundleName(), calledUserPlayer.getBundleType(), calledUserPlayer.getLayerId(),
				null, null, calledUserPlayer.getAudioChannelId(),
				calledUserPlayer.getAudioBaseType(), callUser.getId(), callUser.getUserNo(),
				callUser.getName(),callEncoderBundleEntity.getBundleId(), callEncoderBundleEntity.getBundleName(),
				callEncoderBundleEntity.getBundleType(), callEncoderBundleEntity.getAccessNodeUid(), null,
				null, callEncoderAudioChannel.getChannelId(), callEncoderAudioChannel.getBaseType(),
				callUserPlayer.getBundleId(), callUserPlayer.getBundleName(), callUserPlayer.getBundleType(),
				callUserPlayer.getLayerId(), null, null,
				callUserPlayer.getAudioChannelId(), callUserPlayer.getAudioBaseType());
		
		business.setType(UserCallType.VOICE);
		userLiveCallDao.save(business);
		
		calledUserPlayer.setBusinessId(business.getId().toString());
		calledUserPlayer.setBusinessName(callUser.getName() + "邀请你语音对讲");
		commandGroupUserPlayerDao.save(calledUserPlayer);
		
		callUserPlayer.setBusinessId(business.getId().toString());
		callUserPlayer.setBusinessName("与" + calledUser.getName() + "语音对讲");
		commandGroupUserPlayerDao.save(callUserPlayer);
		
		JSONObject message = new JSONObject();
		message.put("businessType", CommandCommonConstant.MESSAGE_VOICE);
		message.put("fromUserId", callUser.getId());
		message.put("fromUserName", callUser.getName());
		message.put("businessId", business.getId());
		message.put("businessInfo", calledUserPlayer.getBusinessName());
		
		//发送呼叫消息
		WebsocketMessageVO ws = websocketMessageService.send(calledUser.getId(), message.toJSONString(), WebsocketMessageType.COMMAND, callUser.getId(), callUser.getName());
		business.setMessageId(ws.getId());
		userLiveCallDao.save(business);
		
		return callUserPlayer;
	}
	
	/**
	 * 同意呼叫<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午11:18:13
	 * @param Long businessId 业务id
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	public CommandGroupUserPlayerPO acceptCall(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断");
		}
		
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		//消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		
		call.setStatus(CallStatus.ONGOING);
		userLiveCallDao.save(call);
		
		//协议
		LogicBO logic = openBundle(call, codec, admin.getId());
		
		executeBusiness.execute(logic, call.getCalledUsername() + "接听与" + call.getCallUsername() + "通话");
		
		return player;
	}
	
	/** 通常在外部系统接听时调用 */
	public CommandGroupUserPlayerPO acceptCallByUuid(UserBO user, String uuid) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findByUuid(uuid);
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "呼叫不存在！");
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(call.getId()).toString().intern()) {
			UserBO admin = new UserBO(); admin.setId(-1L);
			return acceptCall_Cascade(user, call.getId(), admin);
		}
	}
	
	/**
	 * 同意呼叫<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午5:18:35
	 * @param user 操作人
	 * @param businessId 业务id
	 * @param admin
	 * @return CommandGroupUserPlayerPO 播放器信息
	 * @throws Exception
	 */
	public CommandGroupUserPlayerPO acceptCall_Cascade(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断");
		}
		
		//校验操作人是否是被叫用户
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		//消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		
		call.setStatus(CallStatus.ONGOING);
		userLiveCallDao.save(call);
		
		//协议
		LogicBO logic = openBundle(call, codec, admin.getId());
		
		executeBusiness.execute(logic, call.getCalledUsername() + "接听与" + call.getCallUsername() + "通话");
		
		return player;
	}


	/**
	 * 拒绝呼叫<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午11:25:25
	 * @param Long businessId 业务id
	 */
	public void refuseCall(UserBO user, Long businessId) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断");
		}
		
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		//呼叫方发送的消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//被呼叫方
		CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		calledPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		calledPlayer.setBusinessId(null);
		calledPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(calledPlayer);
		
		//呼叫方
		CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
		CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		callPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		callPlayer.setBusinessId(null);
		callPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(callPlayer);
		
		userLiveCallDao.delete(call);
		
		//被叫方给呼叫方发消息
		JSONObject message = new JSONObject();
		message.put("businessType", CommandCommonConstant.MESSAGE_CALL_REFUSE);
		message.put("fromUserId", user.getId());
		message.put("fromUserName", user.getName());
		message.put("businessInfo", user.getName() + "拒绝你视频通话");
		message.put("serial", callPlayer.getLocationIndex());
		
		WebsocketMessageVO ws = websocketMessageService.send(callUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
		websocketMessageService.consume(ws.getId());
		
	}
	
	/**
	 * 停止呼叫<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 下午2:13:13
	 * @param UserBO user 用户信息
	 * @param Long businessId 业务id
	 * @param UserBO admin admin
	 * @return CommandGroupUserPlayerPO 用户占用播放器信息
	 */
	public CommandGroupUserPlayerPO stopCall(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "呼叫不存在！");
		}
		
		if(!user.getId().equals(call.getCalledUserId()) && !user.getId().equals(call.getCallUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())){
			LogicBO logic = closeBundle(call, codec, admin.getId());
			executeBusiness.execute(logic, user.getName() + "停止通话");
		}
		
		//被呼叫方
		CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		calledPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		calledPlayer.setBusinessId(null);
		calledPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(calledPlayer);
		
		//呼叫方
		CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
		CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		callPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		callPlayer.setBusinessId(null);
		callPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(callPlayer);
		
		userLiveCallDao.delete(call);
		
		CommandGroupUserPlayerPO returnPlayer = null;
		
		//如果用户是被呼叫方
		if(user.getId().equals(calledUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了视频通话");
			message.put("serial", callPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(callUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = calledPlayer;
		}
		
		//如果用户是呼叫方
		if(user.getId().equals(callUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了视频通话");
			message.put("serial", calledPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(calledUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = callPlayer;
		}
		
		return returnPlayer;
		
	}
	
	public CommandGroupUserPlayerPO stopCallById(UserBO user, Long businessId, UserBO admin) throws Exception{
		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
			return stopCall_Cascade(user, businessId, admin);
		}
	}


	/**  */
	public CommandGroupUserPlayerPO stopCallByUuid(UserBO user, String uuid) throws Exception{
		UserLiveCallPO call = userLiveCallDao.findByUuid(uuid);
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "呼叫不存在！");
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(call.getId()).toString().intern()) {
			UserBO admin = new UserBO(); admin.setId(-1L);
			return stopCall_Cascade(user, call.getId(), admin);
		}
	}


	/**
	 * 停止呼叫 - 支持级联<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午3:45:53
	 * @param user 操作人
	 * @param businessId 业务id
	 * @param admin
	 * @return CommandGroupUserPlayerPO 用户释放播放器信息
	 * @throws Exception
	 */
	private CommandGroupUserPlayerPO stopCall_Cascade(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "呼叫不存在！");
		}
				
		//判断发起人是不是通话中的任何一个人
		if(!user.getId().equals(call.getCalledUserId()) && !user.getId().equals(call.getCallUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_CALL.getName());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())){
			LogicBO logic = closeBundle(call, codec, admin.getId());
			executeBusiness.execute(logic, user.getName() + "停止通话");
		}
		
		//被呼叫方
		CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		calledPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		calledPlayer.setBusinessId(null);
		calledPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(calledPlayer);
		
		//呼叫方
		CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
		CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_CALL, businessId.toString());
		callPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		callPlayer.setBusinessId(null);
		callPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(callPlayer);
		
		userLiveCallDao.delete(call);
		
		CommandGroupUserPlayerPO returnPlayer = null;
		
		//如果用户是被呼叫方
		if(user.getId().equals(calledUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了视频通话");
			message.put("serial", callPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(callUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = calledPlayer;
		}
		
		//如果用户是呼叫方
		if(user.getId().equals(callUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_CALL_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了视频通话");
			message.put("serial", calledPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(calledUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = callPlayer;
		}
		
		return returnPlayer;
		
	}


	/**
	 * 同意语音对讲<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 下午4:30:30
	 * @param UserBO user 用户信息
	 * @param Long businessId 业务id
	 * @param UserBO admin 管理员
	 * @return CommandGroupUserPlayerPO 播放器信息
	 */
	public CommandGroupUserPlayerPO acceptVoice(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断");
		}
		
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_VOICE.getName());
		}
		
		//消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		
		call.setStatus(CallStatus.ONGOING);
		userLiveCallDao.save(call);
		
		//协议
		LogicBO logic = openBundle(call, codec, admin.getId());
		
		executeBusiness.execute(logic, call.getCalledUsername() + "接听与" + call.getCallUsername() + "语音对讲");
		
		return player;
	}
	
	/**
	 * 拒绝语音对讲<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 下午4:29:32
	 * @param UserBO user 用户
	 * @param Long businessId 业务id
	 */
	public void refuseVoice(UserBO user, Long businessId) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断！");
		}
		
		if(!user.getId().equals(call.getCalledUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_VOICE.getName());
		}
		
		//呼叫方发送的消息消费
		try{
			websocketMessageService.consume(call.getMessageId());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("消息消费异常，id：" + call.getMessageId());
		}
		
		//被呼叫方
		CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		calledPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		calledPlayer.setBusinessId(null);
		calledPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(calledPlayer);
		
		//呼叫方
		CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
		CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		callPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		callPlayer.setBusinessId(null);
		callPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(callPlayer);
		
		userLiveCallDao.delete(call);
		
		//被叫方给呼叫方发消息
		JSONObject message = new JSONObject();
		message.put("businessType", CommandCommonConstant.MESSAGE_VOICE_REFUSE);
		message.put("fromUserId", user.getId());
		message.put("fromUserName", user.getName());
		message.put("businessInfo", user.getName() + "拒绝你语音对讲");
		message.put("serial", callPlayer.getLocationIndex());
		
		WebsocketMessageVO ws = websocketMessageService.send(callUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
		websocketMessageService.consume(ws.getId());
		
	}
	
	/**
	 * 停止语音对讲<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月20日 下午3:40:03
	 * @param UserBO user 用户
	 * @param Long businessId 业务id
	 * @param UserBO admin 管理员用户
	 */
	public CommandGroupUserPlayerPO stopVoice(UserBO user, Long businessId, UserBO admin) throws Exception{
		
		UserLiveCallPO call = userLiveCallDao.findOne(businessId);
		
		if(call == null){
			throw new BaseException(StatusCode.FORBIDDEN, "对方已挂断！");
		}
		
		if(!user.getId().equals(call.getCalledUserId()) && !user.getId().equals(call.getCallUserId())){
			throw new UserNotMatchBusinessException(user.getName(), businessId, PlayerBusinessType.USER_VOICE.getName());
		}
		
		//参数模板
		Map<String, Object> result = commandCommonServiceImpl.queryDefaultAvCodec();
		AvtplPO targetAvtpl = (AvtplPO)result.get("avtpl");
		AvtplGearsPO targetGear = (AvtplGearsPO)result.get("gear");
		CodecParamBO codec = new CodecParamBO().set(new DeviceGroupAvtplPO().set(targetAvtpl), new DeviceGroupAvtplGearsPO().set(targetGear));
		
		if(CallStatus.ONGOING.equals(call.getStatus()) || CallStatus.PAUSE.equals(call.getStatus())){
			LogicBO logic = closeBundle(call, codec, admin.getId());
			executeBusiness.execute(logic, user.getName() + "停止语音对讲");
		}
		
		//被呼叫方
		CommandGroupUserInfoPO calledUserInfo = commandGroupUserInfoDao.findByUserId(call.getCalledUserId());
		CommandGroupUserPlayerPO calledPlayer = commandCommonServiceImpl.queryPlayerByBusiness(calledUserInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		calledPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		calledPlayer.setBusinessId(null);
		calledPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(calledPlayer);
		
		//呼叫方
		CommandGroupUserInfoPO callUserInfo = commandGroupUserInfoDao.findByUserId(call.getCallUserId());
		CommandGroupUserPlayerPO callPlayer = commandCommonServiceImpl.queryPlayerByBusiness(callUserInfo, PlayerBusinessType.USER_VOICE, businessId.toString());
		callPlayer.setPlayerBusinessType(PlayerBusinessType.NONE);
		callPlayer.setBusinessId(null);
		callPlayer.setBusinessName(null);
		commandGroupUserPlayerDao.save(callPlayer);
		
		userLiveCallDao.delete(call);
		
		CommandGroupUserPlayerPO returnPlayer = null;
		
		//如果用户是被呼叫方
		if(user.getId().equals(calledUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_VOICE_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了语音对讲");
			message.put("serial", callPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(callUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = calledPlayer;
		}
		
		//如果用户是呼叫方
		if(user.getId().equals(callUserInfo.getUserId())){
			//被叫方给呼叫方发消息
			JSONObject message = new JSONObject();
			message.put("businessType", CommandCommonConstant.MESSAGE_VOICE_STOP);
			message.put("fromUserId", user.getId());
			message.put("fromUserName", user.getName());
			message.put("businessInfo", user.getName() + "停止了语音对讲");
			message.put("serial", calledPlayer.getLocationIndex());
			
			WebsocketMessageVO ws = websocketMessageService.send(calledUserInfo.getUserId(), message.toJSONString(), WebsocketMessageType.COMMAND, user.getId(), user.getName());
			websocketMessageService.consume(ws.getId());
			
			returnPlayer = callPlayer;
		}
		
		return returnPlayer;
		
	}
	
	/**
	 * 用户通话协议处理 -- 业务数据库可以控制音视频<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 上午9:55:27
	 * @param UserLiveCallPO call 用户呼叫业务信息
	 * @param CodecParamBO codec 参数
	 * @param Long userId 用户
	 * @return LogicBO 协议
	 */
	private LogicBO openBundle(
			UserLiveCallPO call, 
			CodecParamBO codec,
			Long userId) throws Exception{		
		
		CallType callType = call.getCallType();
				
		//呼叫设备
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
				 			 		 .setPass_by(new ArrayList<PassByBO>());
		
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.OUTER_LOCAL)){
			
			//呼叫被叫编码
			ConnectBundleBO connectCalledEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																	          .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			  .setLock_type("write")
																			  .setBundleId(call.getCalledEncoderBundleId())
																			  .setLayerId(call.getCalledEncoderLayerId())
																			  .setBundle_type(call.getCalledEncoderBundleType());
			
			List<ConnectBO> calledEncodeConnectBOs = new ArrayList<ConnectBO>();
			if(call.getCalledEncoderVideoChannelId() != null){
				ConnectBO connectCalledEncoderVideoChannel = new ConnectBO().setChannelId(call.getCalledEncoderVideoChannelId())
																		    .setChannel_status("Open")
																		    .setBase_type(call.getCalledEncoderVideoBaseType())
																		    .setCodec_param(codec);
				calledEncodeConnectBOs.add(connectCalledEncoderVideoChannel);
			}

			if(call.getCalledEncoderAudioChannelId() != null){
				ConnectBO connectCalledEncoderAudioChannel = new ConnectBO().setChannelId(call.getCalledEncoderAudioChannelId())
																		    .setChannel_status("Open")
																		    .setBase_type(call.getCalledEncoderAudioBaseType())
																		    .setCodec_param(codec);
				calledEncodeConnectBOs.add(connectCalledEncoderAudioChannel);
			}

			
			connectCalledEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().addAll(calledEncodeConnectBOs).getList());
			logic.getConnectBundle().add(connectCalledEncoderBundle);
			
			//呼叫被叫解码,看主叫编码
			ConnectBundleBO connectCalledDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//					  													      .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			  .setLock_type("write")
																		      .setBundleId(call.getCalledDecoderBundleId())
																		      .setLayerId(call.getCalledDecoderLayerId())
																		      .setBundle_type(call.getCalledDecoderBundleType());
			List<ConnectBO> calledDecodeConnectBOs = new ArrayList<ConnectBO>();
			if(call.getCalledDecoderVideoChannelId() != null){
				ForwardSetSrcBO calledDecoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
																			 	    .setBundleId(call.getCallEncoderBundleId())
																			 	    .setLayerId(call.getCallEncoderLayerId())
																			 	    .setChannelId(call.getCallEncoderVideoChannelId());
				ConnectBO connectCalledDecoderVideoChannel = new ConnectBO().setChannelId(call.getCalledDecoderVideoChannelId())
																	        .setChannel_status("Open")
																	        .setBase_type(call.getCalledDecoderVideoBaseType())
																	        .setCodec_param(codec)
																	        .setSource_param(calledDecoderVideoForwardSet);
				calledDecodeConnectBOs.add(connectCalledDecoderVideoChannel);
			}

			if(call.getCalledDecoderAudioChannelId() != null){
				ForwardSetSrcBO calledDecoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
																			 	    .setBundleId(call.getCallEncoderBundleId())
																			 	    .setLayerId(call.getCallEncoderLayerId())
																			 	    .setChannelId(call.getCallEncoderAudioChannelId());
				ConnectBO connectCalledDecoderAudioChannel = new ConnectBO().setChannelId(call.getCalledDecoderAudioChannelId())
																	        .setChannel_status("Open")
																	        .setBase_type(call.getCalledDecoderAudioBaseType())
																	        .setCodec_param(codec)
																	        .setSource_param(calledDecoderAudioForwardSet);
				calledDecodeConnectBOs.add(connectCalledDecoderAudioChannel);
			}

			
			connectCalledDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().addAll(calledDecodeConnectBOs).getList());
			logic.getConnectBundle().add(connectCalledDecoderBundle);
		}else{
			//LOCAL_OUTER，被叫是外部用户，生成passby
			//查询本联网layerid
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
								 .setUuid(call.getUuid())
								 .setSrc_user(call.getCallUserno())
								 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCallEncoderLayerId())
										 											   .put("bundleid", call.getCallEncoderBundleId())
										                         					   .put("video_channelid", call.getCallEncoderVideoChannelId())
										                         					   .put("audio_channelid", call.getCallEncoderAudioChannelId())
										 											   .getMap())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCalledEncoderLayerId())
										 											.put("bundleid", call.getCalledEncoderBundleId())
										 											.put("video_channelid", call.getCalledEncoderVideoChannelId())
										 											.put("audio_channelid", call.getCalledEncoderAudioChannelId())
										 											.getMap())
								 .setDst_number(call.getCalledUserno())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
		}
		
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.LOCAL_OUTER)){
			
			//呼叫主叫编码
			ConnectBundleBO connectCallEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																            .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																		    .setLock_type("write")
																		    .setBundleId(call.getCallEncoderBundleId())
																		    .setLayerId(call.getCallEncoderLayerId())
																		    .setBundle_type(call.getCallEncoderBundleType());
			List<ConnectBO> callEncodeConnectBOs = new ArrayList<ConnectBO>();
			if(call.getCallEncoderVideoChannelId() != null){
				ConnectBO connectCallEncoderVideoChannel = new ConnectBO().setChannelId(call.getCallEncoderVideoChannelId())
																		  .setChannel_status("Open")
																		  .setBase_type(call.getCallEncoderVideoBaseType())
																		  .setCodec_param(codec);
				callEncodeConnectBOs.add(connectCallEncoderVideoChannel);
			}
			if(call.getCallEncoderAudioChannelId() != null){
				ConnectBO connectCallEncoderAudioChannel = new ConnectBO().setChannelId(call.getCallEncoderAudioChannelId())
																	      .setChannel_status("Open")
																	      .setBase_type(call.getCallEncoderAudioBaseType())
																	      .setCodec_param(codec);
				callEncodeConnectBOs.add(connectCallEncoderAudioChannel);
			}
			
			connectCallEncoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().addAll(callEncodeConnectBOs).getList());
			logic.getConnectBundle().add(connectCallEncoderBundle);

			//呼叫主叫解码，看被叫编码
			ConnectBundleBO connectCallDecoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
//					  													    .setOperateType(ConnectBundleBO.OPERATE_TYPE)
																			.setLock_type("write")
																		    .setBundleId(call.getCallDecoderBundleId())
																		    .setLayerId(call.getCallDecoderLayerId())
																		    .setBundle_type(call.getCallDecoderBundleType());
			List<ConnectBO> callDecodeConnectBOs = new ArrayList<ConnectBO>();
			if(call.getCallDecoderVideoChannelId() != null){
				ForwardSetSrcBO callDecoderVideoForwardSet = new ForwardSetSrcBO().setType("channel")
																			 	  .setBundleId(call.getCalledEncoderBundleId())
																			 	  .setLayerId(call.getCalledEncoderLayerId())
																			 	  .setChannelId(call.getCalledEncoderVideoChannelId());
				ConnectBO connectCallDecoderVideoChannel = new ConnectBO().setChannelId(call.getCallDecoderVideoChannelId())
																	      .setChannel_status("Open")
																	      .setBase_type(call.getCallDecoderVideoBaseType())
																	      .setCodec_param(codec)
																	      .setSource_param(callDecoderVideoForwardSet);
				callDecodeConnectBOs.add(connectCallDecoderVideoChannel);
			}
			if(call.getCallDecoderAudioChannelId() != null){
				ForwardSetSrcBO callDecoderAudioForwardSet = new ForwardSetSrcBO().setType("channel")
																			 	  .setBundleId(call.getCalledEncoderBundleId())
																			 	  .setLayerId(call.getCalledEncoderLayerId())
																			 	  .setChannelId(call.getCalledEncoderAudioChannelId());
				ConnectBO connectCallDecoderAudioChannel = new ConnectBO().setChannelId(call.getCallDecoderAudioChannelId())
																	      .setChannel_status("Open")
																	      .setBase_type(call.getCallDecoderAudioBaseType())
																	      .setCodec_param(codec)
																	      .setSource_param(callDecoderAudioForwardSet);
				callDecodeConnectBOs.add(connectCallDecoderAudioChannel);
			}
			
			connectCallDecoderBundle.setChannels(new ArrayListWrapper<ConnectBO>().addAll(callDecodeConnectBOs).getList());
			logic.getConnectBundle().add(connectCallDecoderBundle);
		}else{
			//OUTER_LOCAL，主叫是外部用户，生成passby
			//查询本联网layerid
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
								 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
								 .setUuid(call.getUuid())
								 .setSrc_user(call.getCallUserno())
								 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCalledEncoderLayerId())
																					   .put("bundleid", call.getCalledEncoderBundleId())
											                     					   .put("video_channelid", call.getCalledEncoderVideoChannelId())
											                     					   .put("audio_channelid", call.getCalledEncoderAudioChannelId())
										 											   .getMap())
								 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCallEncoderLayerId())
																					.put("bundleid", call.getCallEncoderBundleId())
																					.put("video_channelid", call.getCallEncoderVideoChannelId())
																					.put("audio_channelid", call.getCallEncoderAudioChannelId())
										                                            .getMap())
								 .setDst_number(call.getCalledUserno())
								 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
		}
			
		return logic;
	}
	
	/**
	 * 用户通话挂断协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 下午2:02:19
	 * @param UserLiveCallPO call 用户呼叫业务信息
	 * @param Long userId 用户id
	 * @return LogicBO 协议
	 */
	private LogicBO closeBundle(
				UserLiveCallPO call, 
				CodecParamBO codec,
				Long userId) throws Exception{
				
		CallType callType = call.getCallType();
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
									 .setPass_by(new ArrayList<PassByBO>());
		
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.OUTER_LOCAL)){
			//关闭被叫用户设备
			DisconnectBundleBO disconnectCalledEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																			           .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				       .setBundleId(call.getCalledEncoderBundleId())
																				       .setBundle_type(call.getCalledEncoderBundleType())
																				       .setLayerId(call.getCalledEncoderLayerId());
			DisconnectBundleBO disconnectCalledDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
	//																		           .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				       .setBundleId(call.getCalledDecoderBundleId())
																				       .setBundle_type(call.getCalledDecoderBundleType())
																				       .setLayerId(call.getCalledDecoderLayerId());
			
			logic.getDisconnectBundle().add(disconnectCalledEncoderBundle);
			logic.getDisconnectBundle().add(disconnectCalledDecoderBundle);
		}else{
			//LOCAL_OUTER，生成passby
			//查询本联网layerid
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
																					 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
																					 .setUuid(call.getUuid())
																					 .setSrc_user(call.getCallUserno())//主叫用户号码
																					 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCallEncoderLayerId())
																							 											   .put("bundleid", call.getCallEncoderBundleId())
																							                         					   .put("video_channelid", call.getCallEncoderVideoChannelId())
																							                         					   .put("audio_channelid", call.getCallEncoderAudioChannelId())
																							 											   .getMap())
																					 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
																							 											.put("bundleid", call.getCalledEncoderBundleId())
																							 											.put("video_channelid", call.getCalledEncoderVideoChannelId())
																							 											.put("audio_channelid", call.getCalledEncoderAudioChannelId())
																							 											.getMap())
																					 .setDst_number(call.getCalledUserno())
																					 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
											.setType(XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER)
											.setPass_by_content(passByContent);

			logic.getPass_by().add(passby);
		}
		
		if(callType==null || callType.equals(CallType.LOCAL_LOCAL) || callType.equals(CallType.LOCAL_OUTER)){
			//关闭主叫用户设备
			DisconnectBundleBO disconnectCallEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
																			         .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				     .setBundleId(call.getCallEncoderBundleId())
																				     .setBundle_type(call.getCallEncoderBundleType())
																				     .setLayerId(call.getCallEncoderLayerId());
			DisconnectBundleBO disconnectCallDecoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
	//																		         .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
																				     .setBundleId(call.getCallDecoderBundleId())
																				     .setBundle_type(call.getCallDecoderBundleType())
																				     .setLayerId(call.getCallDecoderLayerId());
			logic.getDisconnectBundle().add(disconnectCallEncoderBundle);
			logic.getDisconnectBundle().add(disconnectCallDecoderBundle);
		}else{
			//OUTER_LOCAL，生成passby
			//查询本联网layerid
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
					 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
					 .setUuid(call.getUuid())
					 .setSrc_user(call.getCallUserno())
					 .setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", call.getCalledEncoderLayerId())
							 											   .put("bundleid", call.getCalledEncoderBundleId())
							 											   .put("video_channelid", call.getCalledEncoderVideoChannelId())
							 											   .put("audio_channelid", call.getCalledEncoderAudioChannelId())
							 											   .getMap())
					 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
							 											.put("bundleid", call.getCallEncoderBundleId())
							 											.put("video_channelid", call.getCallEncoderVideoChannelId())
							 											.put("audio_channelid", call.getCallEncoderAudioChannelId())
							                                            .getMap())
					 .setDst_number(call.getCalledUserno())
					 .setVparam(codec);
			
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
			.setType(XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER)
			.setPass_by_content(passByContent);
			
			logic.getPass_by().add(passby);
			
		}
		
		return logic;
	}
}
