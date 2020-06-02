package com.sumavision.bvc.device.command.basic.forward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupForwardDemandDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
import com.sumavision.bvc.command.group.enumeration.ForwardDstType;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.enumeration.OriginType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.control.device.command.group.vo.BusinessPlayerVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.UserHasNoAvailableEncoderException;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.monitor.live.DstDeviceType;
import com.sumavision.bvc.device.monitor.playback.exception.ResourceNotExistException;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandForwardServiceImpl 
* @Description: 会议转发业务
* @author zsy
* @date 2019年11月14日 上午09:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandForwardServiceImpl {
	
	/** synchronized锁的前缀 */
	private static final String lockPrefix = "command-group-";
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private CommandGroupForwardDemandDAO commandGroupForwardDemandDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommandCascadeUtil commandCascadeUtil;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private CommandCascadeService commandCascadeService;
	
	@Autowired
	private ConferenceCascadeService conferenceCascadeService;

	/**
	 * 转发设备、用户，强制同意，不需要选择<br/>
	 * <p>1.0协议中另有一个“转发授权”的意义尚不明确</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月21日 上午8:45:57
	 * @param groupId
	 * @param srcUserIds 源用户【暂不支持，请传空数组或null】
	 * @param bundleIds 源设备
	 * @param userIds 目的
	 * @return 
	 * @throws Exception
	 */
	public List<ForwardReturnBO> forward(Long groupId, List<Long> srcUserIds, List<String> bundleIds, List<Long> userIds) throws Exception{
		
		if(groupId == null || "".equals(groupId)){
			log.warn("媒体转发，会议id有误");
			return new ArrayList<ForwardReturnBO>();
		}
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
			
			if(srcUserIds == null) srcUserIds = new ArrayList<Long>();
			if(bundleIds == null) bundleIds = new ArrayList<String>();
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group == null){
				log.info("进行媒体转发的会议不存在，id：" + groupId);
				return new ArrayList<ForwardReturnBO>();
			}
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			String localLayerId = null;//联网id
			
			GroupType groupType = group.getType();
			List<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			Long creatorUserId = chairmanMember.getUserId();
			CommandGroupAvtplPO g_avtpl = group.getAvtpl();
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			
			//校验转发目的成员是否进会
			List<CommandGroupMemberPO> dstMembers = new ArrayList<CommandGroupMemberPO>();
			List<CommandGroupMemberPO> _dstMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
			for(CommandGroupMemberPO dstMember : _dstMembers){
				if(!dstMember.getMemberStatus().equals(MemberStatus.CONNECT)){
					if(!OriginType.OUTER.equals(group.getOriginType())){
						throw new BaseException(StatusCode.FORBIDDEN, dstMember.getUserName() + " 还未进入");
					}
				}else{
					dstMembers.add(dstMember);
				}
			}
			
			List<CommandGroupForwardDemandPO> demands = group.getForwardDemands();
			List<CommandGroupForwardDemandPO> newDemands = new ArrayList<CommandGroupForwardDemandPO>();//新增的
			List<CommandGroupForwardDemandPO> needDemands = new ArrayList<CommandGroupForwardDemandPO>();//需要执行的
			List<CommandGroupUserPlayerPO> needPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			if(demands == null){
				group.setForwardDemands(new ArrayList<CommandGroupForwardDemandPO>());
				demands = group.getForwardDemands();
			}
			
			//从bundleId列表查询所有的bundlePO
			List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(bundleIds);
			if(srcBundleEntities == null) srcBundleEntities = new ArrayList<BundlePO>();
			
			//从bundleId列表查询所有的视频编码1通道
			List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEOENCODE1.getChannelId());
			if(videoEncode1Channels == null) videoEncode1Channels = new ArrayList<ChannelSchemeDTO>();
			
			//从bundleId列表查询所有的音频编码1通道
			List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIOENCODE1.getChannelId());
			if(audioEncode1Channels == null) audioEncode1Channels = new ArrayList<ChannelSchemeDTO>();
			
			//查询被转发的用户
			String userIdListStr = StringUtils.join(srcUserIds.toArray(), ",");
			List<UserBO> srcUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
			if(srcUserBos == null) srcUserBos = new ArrayList<UserBO>();
			List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(srcUserIds);
			
			//按目标成员循环，生成CommandGroupForwardDemandPO
			for(CommandGroupMemberPO dstMember : dstMembers){
				
				OriginType dstOriginType = dstMember.getOriginType()==null?OriginType.INNER:dstMember.getOriginType();
								
				for(BundlePO bundle : srcBundleEntities){
					
					String srcVideoChannelId = null;
					String srcAudioChannelId = null;
					String srcLayerId = null;
					OriginType srcOriginType = null;
					boolean bDeviceLdap =  queryUtil.isLdapBundle(bundle);
					if(!bDeviceLdap){
						srcOriginType = OriginType.INNER;
						srcLayerId = bundle.getAccessNodeUid();
						//遍历视频通道
						for(ChannelSchemeDTO videoChannel : videoEncode1Channels){
							if(bundle.getBundleId().equals(videoChannel.getBundleId())){
								srcVideoChannelId = videoChannel.getChannelId();
								break;
							}
						}
						//遍历音频通道
						for(ChannelSchemeDTO audioChannel : audioEncode1Channels){
							if(bundle.getBundleId().equals(audioChannel.getBundleId())){
								srcAudioChannelId = audioChannel.getChannelId();
								break;
							}
						}
					}else{
						//ldap设备，生成一套参数id
						srcOriginType = OriginType.OUTER;
						srcVideoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
						srcAudioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
						if(localLayerId == null){
							localLayerId = resourceRemoteService.queryLocalLayerId();
						}
						srcLayerId = localLayerId;
					}
					
					//建立 CommandGroupForwardDemandPO
					CommandGroupForwardDemandPO demand = new CommandGroupForwardDemandPO(
							srcOriginType,
							dstOriginType,
							ForwardDemandBusinessType.FORWARD_DEVICE,
							ForwardDemandStatus.UNDONE,
							ForwardDstType.USER,
							dstMember.getId(),
							dstMember.getUserName(),
							dstMember.getUserNum(),
							bundle.getUsername(),
							bundle.getBundleId(),
							bundle.getBundleName(),
							bundle.getBundleType(),
							srcLayerId,
							srcVideoChannelId,
							"VenusVideoIn",//videoBaseType,
							bundle.getBundleId(),
							bundle.getBundleName(),
							bundle.getBundleType(),
							srcLayerId,
							srcAudioChannelId,
							"VenusAudioIn",//String audioBaseType,
							null,//member.getDstBundleId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstVideoChannelId(),
							"VenusVideoOut",//String dstVideoBaseType,
							null,//member.getDstAudioChannelId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstAudioChannelId(),
							"VenusAudioOut",//String dstAudioBaseType,
							creatorUserId,
							g_avtpl.getId(),//Long avTplId,
							currentGear.getId(),//Long gearId,
							DstDeviceType.WEBSITE_PLAYER,
							null,//LiveType type,
							null,//Long osdId,
							null//String osdUsername);
							);
					demand.setGroup(group);
					newDemands.add(demand);
				}
				
				for(UserBO srcUserBo : srcUserBos){
					
					boolean bVodUserLdap = queryUtil.isLdapUser(srcUserBo, folderUserMaps);
					
					String srcBundleId = null;
					String srcVideoChannelId = null;
					String srcAudioChannelId = null;
					String srcLayerId = null;
					String bundleName = null;
					String bundleType = null;
					OriginType srcOriginType = null;
//					boolean bDeviceLdap =  queryUtil.isLdapBundle(bundle);
					if(!bVodUserLdap){
						srcOriginType = OriginType.INNER;
						
						List<BundlePO> encoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(srcUserBo)).getList());
						if(encoderBundleEntities.size() == 0) throw new UserHasNoAvailableEncoderException(srcUserBo.getName());
						BundlePO encoderBundleEntity = encoderBundleEntities.get(0);
						srcBundleId = encoderBundleEntity.getBundleId();
						srcLayerId = encoderBundleEntity.getAccessNodeUid();
						bundleName = encoderBundleEntity.getBundleName();
						bundleType = encoderBundleEntity.getBundleType();
						
						List<ChannelSchemeDTO> encoderVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
						if(encoderVideoChannels.size() == 0) throw new UserHasNoAvailableEncoderException(srcUserBo.getName());
						ChannelSchemeDTO encoderVideoChannel = encoderVideoChannels.get(0);
						srcVideoChannelId = encoderVideoChannel.getChannelId();
						
						List<ChannelSchemeDTO> encoderAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(encoderBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
						if(encoderAudioChannels.size() == 0) throw new UserHasNoAvailableEncoderException(srcUserBo.getName());
						ChannelSchemeDTO encoderAudioChannel = encoderAudioChannels.get(0);
						srcAudioChannelId = encoderAudioChannel.getChannelId();
						
//						//遍历视频通道
//						for(ChannelSchemeDTO videoChannel : videoEncode1Channels){
//							if(bundle.getBundleId().equals(videoChannel.getBundleId())){
//								srcVideoChannelId = videoChannel.getChannelId();
//								break;
//							}
//						}
//						//遍历音频通道
//						for(ChannelSchemeDTO audioChannel : audioEncode1Channels){
//							if(bundle.getBundleId().equals(audioChannel.getBundleId())){
//								srcAudioChannelId = audioChannel.getChannelId();
//								break;
//							}
//						}
					}else{
						//ldap设备，生成一套参数id
						srcOriginType = OriginType.OUTER;
						srcVideoChannelId = ChannelType.VIDEOENCODE1.getChannelId();
						srcAudioChannelId = ChannelType.AUDIOENCODE1.getChannelId();
						if(localLayerId == null){
							localLayerId = resourceRemoteService.queryLocalLayerId();
						}
						srcBundleId = UUID.randomUUID().toString().replace("-", "");
						srcLayerId = localLayerId;
						bundleName = srcUserBo.getName()+"用户源";
						bundleType = "outer_no_bundle_type";
					}
					
					//建立 CommandGroupForwardDemandPO
					CommandGroupForwardDemandPO demand = new CommandGroupForwardDemandPO(
							srcOriginType,
							dstOriginType,
							ForwardDemandBusinessType.FORWARD_USER,
							ForwardDemandStatus.UNDONE,
							ForwardDstType.USER,
							dstMember.getId(),
							dstMember.getUserName(),
							dstMember.getUserNum(),
							srcUserBo.getUserNo(),
							srcBundleId,
							bundleName,
							bundleType,
							srcLayerId,
							srcVideoChannelId,
							"VenusVideoIn",//videoBaseType,
							srcBundleId,
							bundleName,
							bundleType,
							srcLayerId,
							srcAudioChannelId,
							"VenusAudioIn",//String audioBaseType,
							null,//member.getDstBundleId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstVideoChannelId(),
							"VenusVideoOut",//String dstVideoBaseType,
							null,//member.getDstAudioChannelId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstAudioChannelId(),
							"VenusAudioOut",//String dstAudioBaseType,
							creatorUserId,
							g_avtpl.getId(),//Long avTplId,
							currentGear.getId(),//Long gearId,
							DstDeviceType.WEBSITE_PLAYER,
							null,//LiveType type,
							null,//Long osdId,
							null//String osdUsername);
							);
					demand.setResourceName(srcUserBo.getName());
					demand.setGroup(group);
					newDemands.add(demand);
				}
			}
			//save获得id
//			commandGroupUserPlayerDao.save(needPlayers);
			demands.addAll(newDemands);
			commandGroupForwardDemandDao.save(newDemands);
			commandGroupDao.save(group);
			
			//再循环一次，找播放器
			for(CommandGroupMemberPO dstMember : dstMembers){

				List<CommandGroupForwardDemandPO> memberDemands = commandCommonUtil.queryForwardDemandsByDstmemberIds(newDemands, new ArrayListWrapper<Long>().add(dstMember.getId()).getList(), null, null);
				
				if(OriginType.OUTER.equals(dstMember.getOriginType())){
					for(CommandGroupForwardDemandPO memberDemand : memberDemands){
						memberDemand.setExecuteStatus(ForwardDemandStatus.DONE);
					}
					continue;
				}
				
				List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
				int needPlayersCount = memberDemands.size();
				List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(
						dstMember.getUserId(), PlayerBusinessType.COMMAND_FORWARD_DEVICE, needPlayersCount, false);
				int usefulPlayersCount = players.size();
				log.info(new StringBufferWrapper().append("转发源数为 ").append(needPlayersCount)
						.append("， ").append(dstMember.getUserName()).append(" 可用的播放器为 ").append(usefulPlayersCount).toString());
								
				for(CommandGroupForwardDemandPO demand : memberDemands){
					
					//如果有播放器，则设置dst
					if(usefulPlayersCount > 0){
						demand.setExecuteStatus(ForwardDemandStatus.DONE);
						CommandGroupUserPlayerPO player = players.get(players.size() - usefulPlayersCount);
						player.setBusinessId(group.getId().toString() + "-" + demand.getId().toString());
						if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_DEVICE)){
							//转发设备
							player.setBusinessName(group.getName() + "转发" + demand.getVideoBundleName() + "设备");
							player.setPlayerBusinessType(PlayerBusinessType.COMMAND_FORWARD_DEVICE);
							BusinessPlayerVO playerVO = new BusinessPlayerVO().set(player);
							playerVOs.add(playerVO);
						}else if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_USER)){
							//TODO:转发用户
							player.setBusinessName(group.getName() + "转发" + demand.getVideoBundleName() + "用户");
							player.setPlayerBusinessType(PlayerBusinessType.COMMAND_FORWARD_USER);
							BusinessPlayerVO playerVO = new BusinessPlayerVO().set(player);
							playerVOs.add(playerVO);
						}
						
						//给转发设置目的
						demand.setDstPlayer(player);
						player.setMember(dstMember);
						usefulPlayersCount--;
						
						needDemands.add(demand);
						needPlayers.add(player);
					}else{
						demand.setExecuteStatus(ForwardDemandStatus.NO_AVAILABLE_PLAYER);
					}
				}
				dstMember.getPlayers().addAll(players);
				
				Map<String, Object> map = new HashMapWrapper<String, Object>()
						.put("businessType", "commandForward")
						.put("businessId", group.getId().toString())
						.put("businessInfo", group.getName() + " 进行了媒体转发")
						.put("splits", playerVOs)
						.getMap();
				messageCaches.add(new MessageSendCacheBO(dstMember.getUserId(), JSON.toJSONString(map), WebsocketMessageType.COMMAND));
				
			}
			commandGroupUserPlayerDao.save(needPlayers);
			commandGroupDao.save(group);
			
			//logic
			LogicBO logic = commandBasicServiceImpl.openBundle(null, needDemands, needPlayers, null, null, codec, chairmanMember.getUserNum());
			LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(needDemands, null, null, null, null, null, codec, group.getUserId());
			logic.merge(logicCastDevice);
			executeBusiness.execute(logic, group.getName() + " 进行了媒体转发");
			
			//级联
			if(!OriginType.OUTER.equals(group.getOriginType())){
				if(GroupType.BASIC.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.startCommandDeviceForward(group, srcBundleEntities, srcUserBos, dstMembers);
					commandCascadeService.startDeviceForward(groupBO);
				}else if(GroupType.MEETING.equals(groupType)){
					GroupBO groupBO = commandCascadeUtil.startCommandDeviceForward(group, srcBundleEntities, srcUserBos, dstMembers);
					conferenceCascadeService.startDeviceForward(groupBO);
				}
			}
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
			List<ForwardReturnBO> result = new ArrayList<ForwardReturnBO>();
			for(CommandGroupForwardDemandPO demand : newDemands){
				result.add(new ForwardReturnBO().setByDevice(demand));
			}
			return result;
		}
	}

	/**
	 * 会议转发设备，需要成员同意<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:02:44
	 * @param groupId
	 * @param bundleIds 源
	 * @param userIds 目的
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public List<ForwardReturnBO> forwardDevice(Long groupId, List<String> bundleIds, List<Long> userIds) throws Exception{
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			List<ForwardReturnBO> result = new ArrayList<ForwardReturnBO>();
			
			List<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			Long creatorUserId = chairmanMember.getUserId();
			CommandGroupAvtplPO g_avtpl = group.getAvtpl();
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			
			List<CommandGroupMemberPO> dstMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
			List<CommandGroupForwardDemandPO> demands = group.getForwardDemands();
			List<CommandGroupForwardDemandPO> newDemands = new ArrayList<CommandGroupForwardDemandPO>();
			if(demands == null){
				group.setForwardDemands(new ArrayList<CommandGroupForwardDemandPO>());
				demands = group.getForwardDemands();
			}
			
			//从bundleId列表查询所有的bundlePO
			List<BundlePO> srcBundleEntities = resourceBundleDao.findByBundleIds(bundleIds);
			
			//从bundleId列表查询所有的视频编码1通道
			List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.VIDEOENCODE1.getChannelId());
			
			//从bundleId列表查询所有的音频编码1通道
			List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(bundleIds, ChannelType.AUDIOENCODE1.getChannelId());
			
			for(CommandGroupMemberPO dstMember : dstMembers){
				for(BundlePO bundle : srcBundleEntities){
					String srcVideoChannelId = null;
					String srcAudioChannelId = null;
					//遍历视频通道
					for(ChannelSchemeDTO videoChannel : videoEncode1Channels){
						if(bundle.getBundleId().equals(videoChannel.getBundleId())){
							srcVideoChannelId = videoChannel.getChannelId();
							break;
						}
					}					
					//遍历音频通道
					for(ChannelSchemeDTO audioChannel : audioEncode1Channels){
						if(bundle.getBundleId().equals(audioChannel.getBundleId())){
							srcAudioChannelId = audioChannel.getChannelId();
							break;
						}
					}
					//建立 CommandGroupForwardDemandPO ，发送消息
					CommandGroupForwardDemandPO demand = new CommandGroupForwardDemandPO(
							OriginType.INNER,
							OriginType.INNER,
							ForwardDemandBusinessType.FORWARD_DEVICE,
							ForwardDemandStatus.WAITING_FOR_RESPONSE,
							ForwardDstType.USER,
							dstMember.getId(),
							dstMember.getUserName(),
							dstMember.getUserNum(),
							bundle.getUsername(),
							bundle.getBundleId(),
							bundle.getBundleName(),
							bundle.getBundleType(),
							bundle.getAccessNodeUid(),
							srcVideoChannelId,
							"VenusVideoIn",//videoBaseType,
							bundle.getBundleId(),
							bundle.getBundleName(),
							bundle.getBundleType(),
							bundle.getAccessNodeUid(),
							srcAudioChannelId,
							"VenusAudioIn",//String audioBaseType,
							null,//member.getDstBundleId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstVideoChannelId(),
							"VenusVideoOut",//String dstVideoBaseType,
							null,//member.getDstAudioChannelId(),
							null,//member.getDstBundleName(),
							null,//member.getDstBundleType(),
							null,//member.getDstLayerId(),
							null,//member.getDstAudioChannelId(),
							"VenusAudioOut",//String dstAudioBaseType,
							creatorUserId,
							g_avtpl.getId(),//Long avTplId,
							currentGear.getId(),//Long gearId,
							DstDeviceType.WEBSITE_PLAYER,
							null,//LiveType type,
							null,//Long osdId,
							null//String osdUsername);
							);
					demand.setGroup(group);
					newDemands.add(demand);
				}
			}
			//save获得id
			demands.addAll(newDemands);
			commandGroupForwardDemandDao.save(newDemands);
			commandGroupDao.save(group);
			
			for(CommandGroupForwardDemandPO demand : newDemands){
				result.add(new ForwardReturnBO().setByDevice(demand));
			}
			
			//发消息（立即消费？）
			List<Long> consumeIds = new ArrayList<Long>();
			for(CommandGroupMemberPO dstMember : dstMembers){
				List<Long> dstMemberIds = new ArrayListWrapper<Long>().add(dstMember.getId()).getList();
				//查出该用户的新点播转发
				List<CommandGroupForwardDemandPO> memberDemands = commandCommonUtil.queryForwardDemandsByDstmemberIds(newDemands, dstMemberIds, null, null);
				JSONArray forwards = new JSONArray();
				for(CommandGroupForwardDemandPO demand : memberDemands){
					JSONObject forward = new JSONObject();
					forward.put("id", demand.getId().toString());
					forward.put("name", demand.getVideoBundleName());
					forwards.add(forward);
				}
				Map<String, Object> map = new HashMapWrapper<String, Object>()
						.put("businessType", PlayerBusinessType.COMMAND_FORWARD_DEVICE.getCode())
						.put("businessId", group.getId().toString())
						.put("forwards", forwards)
						.put("businessInfo", group.getName() + "给你转发了直播视频")
						.getMap();
				
				WebsocketMessageVO ws = websocketMessageService.send(dstMember.getUserId(), JSON.toJSONString(map), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);//（立即消费？）
			
			return result;
		}
	}
	
	/**
	 * 会议转发文件，需要成员同意<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:03:20
	 * @param groupId
	 * @param resourceIds 源文件
	 * @param userIds 目的
	 * @return
	 * @throws Exception
	 */
	public List<ForwardReturnBO> forwardFile_old(Long groupId, List<String> resourceIds, List<Long> userIds) throws Exception{
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			//查找资源
			JSONArray files = new JSONArray();
			for(String resourceId : resourceIds){
				JSONObject file = resourceService.queryOnDemandResourceById(resourceId);
				if(file == null) throw new ResourceNotExistException(resourceId);
				files.add(file);				
			}
			
			List<ForwardReturnBO> result = new ArrayList<ForwardReturnBO>();
			
			List<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			Long creatorUserId = chairmanMember.getUserId();
			CommandGroupAvtplPO g_avtpl = group.getAvtpl();
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			
			List<CommandGroupMemberPO> dstMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
			List<CommandGroupForwardDemandPO> demands = group.getForwardDemands();
			List<CommandGroupForwardDemandPO> newDemands = new ArrayList<CommandGroupForwardDemandPO>();
			if(demands == null){
				group.setForwardDemands(new ArrayList<CommandGroupForwardDemandPO>());
				demands = group.getForwardDemands();
			}
			
			for(CommandGroupMemberPO dstMember : dstMembers){
				
				if(OriginType.OUTER.equals(dstMember.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, dstMember.getUserName() + "是跨系统成员，无法转发文件");
				}
				
				for(int i=0; i<files.size(); i++){
					JSONObject file = files.getJSONObject(i);
					String resourceId = file.getString("resourceId");
					String resourceName = file.getString("name");
					String previewUrl = file.getString("previewUrl");
					
					//建立 CommandGroupForwardDemandPO ，发送消息
					CommandGroupForwardDemandPO demand = new CommandGroupForwardDemandPO(
							ForwardDemandBusinessType.FORWARD_FILE,
							ForwardDemandStatus.WAITING_FOR_RESPONSE,
							ForwardDstType.USER,
							dstMember.getId(),
							dstMember.getUserName(),
							dstMember.getUserNum(),
							resourceId,
							resourceName,
							previewUrl,
							creatorUserId,
							g_avtpl.getId(),//Long avTplId,
							currentGear.getId(),//Long gearId,
							DstDeviceType.WEBSITE_PLAYER,
							null,//LiveType type,
							null,//Long osdId,
							null//String osdUsername);
							);
					demand.setGroup(group);
					newDemands.add(demand);
				}
			}
			//save获得id
			demands.addAll(newDemands);
			commandGroupForwardDemandDao.save(newDemands);
			commandGroupDao.save(group);
			
			for(CommandGroupForwardDemandPO demand : newDemands){
				result.add(new ForwardReturnBO().setByFile(demand));
			}
			
			//发消息（立即消费？）
			List<Long> consumeIds = new ArrayList<Long>();
			for(CommandGroupMemberPO dstMember : dstMembers){
				List<Long> dstMemberIds = new ArrayListWrapper<Long>().add(dstMember.getId()).getList();
				//查出该用户的新点播转发
				List<CommandGroupForwardDemandPO> memberDemands = commandCommonUtil.queryForwardDemandsByDstmemberIds(newDemands, dstMemberIds, null, null);
				JSONArray forwards = new JSONArray();
				for(CommandGroupForwardDemandPO demand : memberDemands){
					JSONObject forward = new JSONObject();
					forward.put("id", demand.getId().toString());
					forward.put("name", demand.getResourceName());
					forwards.add(forward);
				}
				Map<String, Object> map = new HashMapWrapper<String, Object>()
						.put("businessType", PlayerBusinessType.COMMAND_FORWARD_FILE.getCode())
						.put("businessId", group.getId().toString())
						.put("forwards", forwards)
						.put("businessInfo", group.getName() + "给你转发了文件视频")
						.getMap();
				
				WebsocketMessageVO ws = websocketMessageService.send(dstMember.getUserId(), JSON.toJSONString(map), WebsocketMessageType.COMMAND, chairmanMember.getUserId(), chairmanMember.getUserName());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);//（立即消费？）
			
			return result;
		}
	}

	/**
	 * 会议转发文件，不需要成员同意<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:03:20
	 * @param groupId
	 * @param resourceIds 源文件
	 * @param userIds 目的
	 * @return
	 * @throws Exception
	 */
	public List<ForwardReturnBO> forwardFile(Long groupId, List<String> resourceIds, List<Long> userIds) throws Exception{
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			List<Long> consumeIds = new ArrayList<Long>();
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			
			//查找资源
			JSONArray files = new JSONArray();
			for(String resourceId : resourceIds){
				JSONObject file = resourceService.queryOnDemandResourceById(resourceId);
				if(file == null) throw new ResourceNotExistException(resourceId);
				files.add(file);				
			}
			
			List<ForwardReturnBO> result = new ArrayList<ForwardReturnBO>();
			
			List<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			Long creatorUserId = chairmanMember.getUserId();
			CommandGroupAvtplPO g_avtpl = group.getAvtpl();
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			
			//校验转发目的成员是否进会
			List<CommandGroupMemberPO> dstMembers = commandCommonUtil.queryMembersByUserIds(members, userIds);
			for(CommandGroupMemberPO dstMember : dstMembers){
				if(!dstMember.getMemberStatus().equals(MemberStatus.CONNECT)){
					throw new BaseException(StatusCode.FORBIDDEN, dstMember.getUserName() + " 还未进入");
				}
			}
			
			List<CommandGroupForwardDemandPO> demands = group.getForwardDemands();
			List<CommandGroupForwardDemandPO> newDemands = new ArrayList<CommandGroupForwardDemandPO>();
			List<CommandGroupUserPlayerPO> needPlayers = new ArrayList<CommandGroupUserPlayerPO>();//需要保存的
			if(demands == null){
				group.setForwardDemands(new ArrayList<CommandGroupForwardDemandPO>());
				demands = group.getForwardDemands();
			}
			
			for(CommandGroupMemberPO dstMember : dstMembers){
				
				if(OriginType.OUTER.equals(dstMember.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, dstMember.getUserName() + "是跨系统成员，无法转发文件");
				}
				
				for(int i=0; i<files.size(); i++){
					JSONObject file = files.getJSONObject(i);
					String resourceId = file.getString("resourceId");
					String resourceName = file.getString("name");
					String previewUrl = file.getString("previewUrl");
					
					//建立 CommandGroupForwardDemandPO ，发送消息
					CommandGroupForwardDemandPO demand = new CommandGroupForwardDemandPO(
							ForwardDemandBusinessType.FORWARD_FILE,
							ForwardDemandStatus.WAITING_FOR_RESPONSE,
							ForwardDstType.USER,
							dstMember.getId(),
							dstMember.getUserName(),
							dstMember.getUserNum(),
							resourceId,
							resourceName,
							previewUrl,
							creatorUserId,
							g_avtpl.getId(),//Long avTplId,
							currentGear.getId(),//Long gearId,
							DstDeviceType.WEBSITE_PLAYER,
							null,//LiveType type,
							null,//Long osdId,
							null//String osdUsername);
							);
					demand.setGroup(group);
					newDemands.add(demand);
				}
			}
			//save获得id
			demands.addAll(newDemands);
			commandGroupForwardDemandDao.save(newDemands);
			commandGroupDao.save(group);
			
			//再循环一次，找播放器
			for(CommandGroupMemberPO dstMember : dstMembers){

				List<CommandGroupForwardDemandPO> memberDemands = commandCommonUtil.queryForwardDemandsByDstmemberIds(newDemands, new ArrayListWrapper<Long>().add(dstMember.getId()).getList(), null, null);
				
				if(OriginType.OUTER.equals(dstMember.getOriginType())){
					for(CommandGroupForwardDemandPO memberDemand : memberDemands){
						memberDemand.setExecuteStatus(ForwardDemandStatus.DONE);
					}
					continue;
				}
				
				List<BusinessPlayerVO> playerVOs = new ArrayList<BusinessPlayerVO>();
				int needPlayersCount = memberDemands.size();
				List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(
						dstMember.getUserId(), PlayerBusinessType.COMMAND_FORWARD_DEVICE, needPlayersCount, false);
				int usefulPlayersCount = players.size();
				log.info(new StringBufferWrapper().append("转文件数为 ").append(needPlayersCount)
						.append("， ").append(dstMember.getUserName()).append(" 可用的播放器为 ").append(usefulPlayersCount).toString());
								
				for(CommandGroupForwardDemandPO demand : memberDemands){
					
					//如果有播放器，则设置dst
					if(usefulPlayersCount > 0){
						demand.setExecuteStatus(ForwardDemandStatus.DONE);
						CommandGroupUserPlayerPO player = players.get(players.size() - usefulPlayersCount);
						player.setBusinessId(group.getId().toString() + "-" + demand.getId().toString());
						if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_FILE)){
							//转发设备
							player.setBusinessName(group.getName() + "转发" + demand.getResourceName() + "文件");
							player.setPlayerBusinessType(PlayerBusinessType.COMMAND_FORWARD_FILE);
							player.setPlayUrl(demand.getPlayUrl());
							BusinessPlayerVO playerVO = new BusinessPlayerVO().set(player);
							playerVOs.add(playerVO);
						}
						
						//给转发设置目的
						demand.setDstPlayer(player);
						player.setMember(dstMember);
						usefulPlayersCount--;
						
						needPlayers.add(player);
					}else{
						demand.setExecuteStatus(ForwardDemandStatus.NO_AVAILABLE_PLAYER);
					}
				}
				dstMember.getPlayers().addAll(players);
				
				Map<String, Object> map = new HashMapWrapper<String, Object>()
						.put("businessType", "commandForward")
						.put("businessId", group.getId().toString())
						.put("businessInfo", group.getName() + " 进行了媒体转发")
						.put("splits", playerVOs)
						.getMap();
				messageCaches.add(new MessageSendCacheBO(dstMember.getUserId(), JSON.toJSONString(map), WebsocketMessageType.COMMAND));
				
			}
			commandGroupUserPlayerDao.save(needPlayers);
			commandGroupDao.save(group);
			
			for(CommandGroupForwardDemandPO demand : newDemands){
				result.add(new ForwardReturnBO().setByFile(demand));
			}
			
			//生成logic下发协议
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, needPlayers, null, null, null, null, codec, group.getUserId());
			executeBusiness.execute(logicCastDevice, group.getName() + " 进行了文件转发");
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
			
			return result;
		}
	}

	/**
	 * 同意转发，包括设备和文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 上午9:10:22
	 * @param userId 后续优化：这个参数应该去掉，从demandIds里取出dstMemberId
	 * @param groupId
	 * @param demandIds
	 * @return ForwardDeviceAgreeReturnBO 或 ForwardFileAgreeReturnBO
	 * @throws Exception
	 */
	public List<Object> forwardAgree(Long userId, Long groupId, List<Long> demandIds) throws Exception{
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			
			List<Object> result = new ArrayList<Object>();
			
			List<CommandGroupMemberPO> members = group.getMembers();
			CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
			chairmanMember.getUserId();
			group.getAvtpl();
			
			CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberByUserId(members, userId);
			List<CommandGroupForwardDemandPO> demands = commandGroupForwardDemandDao.findAll(demandIds);
			
			//找播放器
			List<CommandGroupUserPlayerPO> players = commandCommonServiceImpl.userChoseUsefulPlayers(dstMember.getUserId(), PlayerBusinessType.COMMAND_FORWARD_DEVICE, demandIds.size(), true);
			int usefulPlayersCount = players.size();
			if(usefulPlayersCount < demandIds.size()){
				for(CommandGroupForwardDemandPO demand : demands){
					demand.setExecuteStatus(ForwardDemandStatus.NO_AVAILABLE_PLAYER);
				}
				commandGroupDao.save(group);
				throw new BaseException(StatusCode.FORBIDDEN, "没有足够的播放器");
			}
			
			List<CommandGroupForwardDemandPO> needDemands = new ArrayList<CommandGroupForwardDemandPO>();
			List<CommandGroupUserPlayerPO> needPlayers = new ArrayList<CommandGroupUserPlayerPO>();
			List<CommandGroupUserPlayerPO> playFilePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			for(CommandGroupForwardDemandPO demand : demands){
				if(usefulPlayersCount > 0){
					demand.setExecuteStatus(ForwardDemandStatus.DONE);
					CommandGroupUserPlayerPO player = players.get(players.size() - usefulPlayersCount);
					player.setBusinessId(group.getId().toString() + "-" + demand.getId().toString());
					if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_DEVICE)){
						//转发设备
						player.setBusinessName(group.getName() + "转发" + demand.getVideoBundleName() + "设备");
						player.setPlayerBusinessType(PlayerBusinessType.COMMAND_FORWARD_DEVICE);
						ForwardDeviceAgreeReturnBO bo = new ForwardDeviceAgreeReturnBO().set(player);
						result.add(bo);
					}else if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_FILE)){
						//转发文件
						playFilePlayers.add(player);
						player.setBusinessName(group.getName() + "转发" + demand.getResourceName() + "文件");
						player.setPlayerBusinessType(PlayerBusinessType.COMMAND_FORWARD_FILE);
						player.setPlayUrl(demand.getPlayUrl());
						ForwardFileAgreeReturnBO bo = new ForwardFileAgreeReturnBO().set(player);
						result.add(bo);
					}
					
					//给转发设置目的
					demand.setDstPlayer(player);
					player.setMember(dstMember);
					usefulPlayersCount--;
					
					needDemands.add(demand);
					needPlayers.add(player);
				}else{
					demand.setExecuteStatus(ForwardDemandStatus.NO_AVAILABLE_PLAYER);
				}
			}
			dstMember.getPlayers().addAll(players);
			
			commandGroupDao.save(group);
			commandGroupUserPlayerDao.save(players);
			
			//生成logic下发协议
			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
			LogicBO logic = commandBasicServiceImpl.openBundle(null, needDemands, needPlayers, null, null, codec, chairmanMember.getUserNum());
			LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(needDemands, playFilePlayers, null, null, null, null, codec, group.getUserId());
			logic.merge(logicCastDevice);
			
			StringBufferWrapper description = new StringBufferWrapper()
					.append(dstMember.getUserName()).append(" 同意接收转发");
			executeBusiness.execute(logic, description.toString());
			
			return result;
		}
	}
	
	/**
	 * 拒绝转发，包括设备和文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:05:17
	 * @param groupId
	 * @param demandIds
	 */
	public void forwardRefuse(Long groupId, List<Long> demandIds) {
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
			
			List<CommandGroupForwardDemandPO> demands = commandGroupForwardDemandDao.findAll(demandIds);
			for(CommandGroupForwardDemandPO demand : demands){
				demand.setExecuteStatus(ForwardDemandStatus.DENY);
			}
			commandGroupForwardDemandDao.save(demands);
			
		}
	}
	
	/**
	 * 这是一个级联被调用的方法，不再向外发送协议。按标准协议，根据源号码和目的号码，停止转发调度<br/>
	 * <p>相同[源-目的]的多个任务，1次只能停1个，需要调用2次来停止</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月4日 下午2:20:12
	 * @param userId 操作人，暂时没用
	 * @param groupUuid
	 * @param srcCodes
	 * @param dstCodes
	 * @throws Exception
	 */
	public void stopBySrcAndDstCodes(Long userId1, String groupUuid, List<String> srcCodes, List<String> dstCodes) throws Exception{
		
		CommandGroupPO group1 = commandGroupDao.findByUuid(groupUuid);
		if(group1 == null){
			log.info("停止媒体转发的会议不存在，uuid：" + groupUuid);
			return;
		}
		
		synchronized (new StringBuffer().append(lockPrefix).append(group1.getId()).toString().intern()) {
			
			CommandGroupPO group = commandGroupDao.findByUuid(groupUuid);
			log.info(group.getName() + " 停止转发，源号码：" + srcCodes + "，目的号码" + dstCodes);
			
			List<CommandGroupForwardDemandPO> demands = group.getForwardDemands();
			List<CommandGroupForwardDemandPO> stopDdemands = new ArrayList<CommandGroupForwardDemandPO>();
			for(String srcCode : srcCodes){
				for(String dstCode : dstCodes){
					CommandGroupForwardDemandPO demand = commandCommonUtil.queryForwardDemandBySrcAndDstCode(demands, srcCode, dstCode);
					if(demand != null){
						stopDdemands.add(demand);
					}
				}
			}
			
			//<userId, [{serial:屏幕序号}]>
			HashMap<Long, JSONArray> result = stopDemands(group, stopDdemands, false);
			
			//发消息
			List<Long> consumeIds = new ArrayList<Long>();
			for(Map.Entry<Long, JSONArray> entry: result.entrySet()){
				System.out.println("Key: "+ entry.getKey()+ " Value: "+entry.getValue());
				Long thisUserId = entry.getKey();
				JSONArray splits = entry.getValue();
				JSONObject message = new JSONObject();
				message.put("businessType", "commandForwardStop");
				message.put("businessId", group.getId().toString());
				message.put("businessInfo", group.getName() + " 停止了转发");
				message.put("splits", splits);
				WebsocketMessageVO ws = websocketMessageService.send(thisUserId, message.toJSONString(), WebsocketMessageType.COMMAND, group.getUserId(), group.getUserName());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
	}

	/**
	 * 主席停止一个会议中的多个转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:05:55
	 * @param groupId
	 * @param demandIds
	 * @throws Exception
	 */
	public void stopByChairman(Long groupId, List<Long> demandIds) throws Exception{
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
		
			CommandGroupPO group = commandGroupDao.findOne(groupId);
			List<CommandGroupForwardDemandPO> stopDemands = commandCommonUtil.queryForwardDemandsByIds(group.getForwardDemands(), demandIds);
			
			//<userId, [{serial:屏幕序号}]>
			HashMap<Long, JSONArray> result = stopDemands(group, stopDemands, true);
					
			//发消息
			List<Long> consumeIds = new ArrayList<Long>();
			for(Map.Entry<Long, JSONArray> entry: result.entrySet()){
				System.out.println("Key: "+ entry.getKey()+ " Value: "+entry.getValue());
				Long userId = entry.getKey();
				JSONArray splits = entry.getValue();
				JSONObject message = new JSONObject();
				message.put("businessType", "commandForwardStop");
				message.put("businessId", group.getId().toString());
				message.put("businessInfo", group.getName() + " 停止了转发");
				message.put("splits", splits);
				WebsocketMessageVO ws = websocketMessageService.send(userId, message.toJSONString(), WebsocketMessageType.COMMAND, group.getUserId(), group.getUserName());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
	}

	/**
	 * 成员停止多个转发<br/>
	 * <p>支持不同会议中的转发</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:07:38
	 * @param userId
	 * @param demandIds
	 * @return
	 * @throws Exception
	 */
	public JSONArray stopByMember(Long userId, List<Long> demandIds) throws Exception{
		
		JSONArray splits = new JSONArray();
		List<Long> groupIds = commandGroupDao.findAllIdsByDemandIds(demandIds);
		
		for(Long groupId : groupIds){
			
			synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
				
				//这里把demandIds全部传入，因为有groupId限制，所以demandIds传多了也没事
				CommandGroupPO group = commandGroupDao.findOne(groupId);
				List<CommandGroupForwardDemandPO> stopDemands = commandCommonUtil.queryForwardDemandsByIds(group.getForwardDemands(), demandIds);
				HashMap<Long, JSONArray> result = stopDemands(group, stopDemands, true);
				JSONArray splist1 = result.get(userId);
				if(splist1 != null){
					splits.addAll(splist1);
				}
				
			}
		}
		
		return splits;
	}	
	
	/**
	 * 停止一个会议中的多个转发点播<br/>
	 * <p>可能涉及多个成员用户，所以返回map</p>
	 * <p>包含级联</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 上午10:08:43
	 * @param group
	 * @param stopDemands
	 * @param cascade 是否给其它级联节点发送协议，主动操作的应使用true；被动响应的，例如被级联调用的应使用false
	 * @return HashMap<Long, JSONArray> 格式为：<userId, [{serial:屏幕序号}]>
	 * @throws Exception
	 */
	private HashMap<Long, JSONArray> stopDemands(CommandGroupPO group, List<CommandGroupForwardDemandPO> stopDemands, boolean cascade) throws Exception{
		
//		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
			
//			CommandGroupPO group = commandGroupDao.findOne(groupId);
		GroupType groupType = group.getType();
		if(group.getStatus().equals(GroupStatus.STOP)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
		}
		
		//<userId, [{serial:'屏幕序号'}]>
		HashMap<Long, JSONArray> result = new HashMap<Long, JSONArray>();
		List<CommandGroupForwardDemandPO> demands = group.getForwardDemands();
		List<CommandGroupMemberPO> members = group.getMembers();
		
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		chairmanMember.getUserId();
		group.getAvtpl();
		List<GroupBO> groupBOs = new ArrayList<GroupBO>();
		List<CommandGroupForwardDemandPO> needStopDemands = new ArrayList<CommandGroupForwardDemandPO>();
		
//			List<CommandGroupForwardDemandPO> stopDemands = commandCommonUtil.queryForwardDemandsByIds(demands, demandIds);
		List<CommandGroupUserPlayerPO> allNeedClosePlayers = new ArrayList<CommandGroupUserPlayerPO>();
		List<CommandGroupUserPlayerPO> playFilePlayers = new ArrayList<CommandGroupUserPlayerPO>();			
		for(CommandGroupForwardDemandPO demand : stopDemands){

			needStopDemands.add(demand);
			Long dstMemberId = demand.getDstMemberId();
			CommandGroupMemberPO dstMember = commandCommonUtil.queryMemberById(members, dstMemberId);
			
			//级联，这里仅生成groupBO
			if(cascade){
				List<CommandGroupMemberPO> dstMembers = new ArrayListWrapper<CommandGroupMemberPO>().add(dstMember).getList();
				if(GroupType.BASIC.equals(groupType)){
					if(ForwardDemandBusinessType.FORWARD_DEVICE.equals(demand.getDemandType())
							|| ForwardDemandBusinessType.FORWARD_USER.equals(demand.getDemandType())){
						GroupBO groupBO = commandCascadeUtil.stopCommandDeviceForward(group, demand, dstMembers);
						groupBOs.add(groupBO);
					}
				}else if(GroupType.MEETING.equals(groupType)){
					if(ForwardDemandBusinessType.FORWARD_DEVICE.equals(demand.getDemandType())
							|| ForwardDemandBusinessType.FORWARD_USER.equals(demand.getDemandType())){
						GroupBO groupBO = commandCascadeUtil.stopCommandDeviceForward(group, demand, dstMembers);
						groupBOs.add(groupBO);
					}
				}
			}
			
			if(OriginType.OUTER.equals(dstMember.getOriginType())){
				continue;
			}
			
			List<CommandGroupUserPlayerPO> needFreePlayers = new ArrayList<CommandGroupUserPlayerPO>();
			List<CommandGroupUserPlayerPO> players = dstMember.getPlayers();
			for(CommandGroupUserPlayerPO player : players){
				if(player.getBundleId().equals(demand.getDstVideoBundleId())){
					if(player.playingFile()){
						playFilePlayers.add(player);
					}
					player.setFree();
					needFreePlayers.add(player);
					JSONObject split = new JSONObject();
					split.put("serial", player.getLocationIndex());
					
					JSONArray dstMemberSplits = result.get(dstMember.getUserId());
					if(dstMemberSplits != null){
						dstMemberSplits.add(split);
					}else{
						JSONArray splits = new JSONArray();
						splits.add(split);
						result.put(dstMember.getUserId(), splits);
					}
					
					break;
				}
			}
			players.remove(needFreePlayers);
			allNeedClosePlayers.addAll(needFreePlayers);
//			demand.clearDst();
//			demand.setExecuteStatus(ForwardDemandStatus.UNDONE);
		}
		
		demands.removeAll(stopDemands);
		commandGroupDao.save(group);
		
		//发协议
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
		LogicBO logic = commandBasicServiceImpl.closeBundle(null, needStopDemands, allNeedClosePlayers, codec, chairmanMember.getUserNum());
		LogicBO logicCastDevice = commandCastServiceImpl.closeBundleCastDevice(playFilePlayers, null, null, allNeedClosePlayers, codec, group.getUserId());
		logic.merge(logicCastDevice);
		executeBusiness.execute(logic, group.getName() + " 会议停止多个转发，个数：" + needStopDemands.size());
		
		//级联，循环groupBOs发多个协议
		if(cascade){//TODO:
			for(GroupBO groupBO : groupBOs){
				if(GroupType.BASIC.equals(groupType)){
					commandCascadeService.stopDeviceForward(groupBO);
				}else if(GroupType.MEETING.equals(groupType)){
					conferenceCascadeService.stopDeviceForward(groupBO);
				}
			}
		}
		
		return result;			
//		}
	}
}
