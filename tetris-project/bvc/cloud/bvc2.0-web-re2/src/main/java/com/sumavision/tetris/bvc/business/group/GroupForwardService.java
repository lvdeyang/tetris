package com.sumavision.tetris.bvc.business.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.AgendaForwardDemandDAO;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
import com.sumavision.bvc.command.group.forward.AgendaForwardDemandPO;
import com.sumavision.bvc.device.command.basic.forward.ForwardReturnBO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.GroupCascadeUtil;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.monitor.playback.exception.ResourceNotExistException;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.bo.MemberTerminalBO;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.AudioType;
import com.sumavision.tetris.bvc.model.agenda.DestinationType;
import com.sumavision.tetris.bvc.model.agenda.LayoutPositionSelectionType;
import com.sumavision.tetris.bvc.model.agenda.LayoutSelectionType;
import com.sumavision.tetris.bvc.model.agenda.SourceType;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelType;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.mvc.util.BaseUtils;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;

import lombok.extern.slf4j.Slf4j;

/** 媒体转发 */
@Slf4j
@Service
public class GroupForwardService {
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";
	
	/** synchronized锁的前缀 */
	private static final String lockPrefix = "group-demand-";
	
	private static final String lockFilePrefix = "group-demand-file-";
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private GroupCascadeUtil groupCascadeUtil;
	
	@Autowired
	private AgendaForwardDemandDAO agendaForwardDemandDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;

	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;

	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private GroupMemberService groupMemberService;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private CommandCascadeService commandCascadeService;
	
	@Autowired
	private ConferenceCascadeService conferenceCascadeService;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 媒体转发，转发设备、用户，强制同意，不需要选择<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 下午2:15:58
	 * @param groupId
	 * @param srcUserIds 源用户
	 * @param bundleIds 源设备
	 * @param groupMemberIds 成员id
	 * @return 
	 */
	public List<AgendaForwardDemandPO> forwardM(Long groupId, List<Long> srcUserIds, List<String> bundleIds, List<Long> groupMemberIds) throws Exception {
		List<BusinessGroupMemberPO> dstMembers = businessGroupMemberDao.findAll(groupMemberIds);
		List<Long> dstUserIds = dstMembers.stream().map(BusinessGroupMemberPO::getOriginId).map(Long::valueOf).collect(Collectors.toList());
		return forward(groupId, srcUserIds, bundleIds, dstUserIds);
	}

	/**
	 * 媒体转发，转发设备、用户，强制同意，不需要选择<br/>
	 * 对于目的是outer的，把源member标记为DISCONNECT，后边不打开也不关闭源membe的编码器r<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月19日 上午9:25:48
	 * @param groupId
	 * @param srcUserIds 源用户
	 * @param bundleIds 源设备
	 * @param dstUserIds 目的
	 * @return 
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<AgendaForwardDemandPO> forward(Long groupId, List<Long> srcUserIds, List<String> bundleIds, List<Long> dstUserIds) throws Exception{
		if(groupId == null || "".equals(groupId)){
			log.warn("媒体转发，会议id有误");
			return new ArrayList<AgendaForwardDemandPO>();
		}
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			if(srcUserIds == null) srcUserIds = new ArrayList<Long>();
			if(bundleIds == null) bundleIds = new ArrayList<String>();
			
			GroupPO group = groupDao.findOne(groupId);
			if(group == null){
				log.info("进行媒体转发的会议不存在，id：" + groupId);
				return new ArrayList<AgendaForwardDemandPO>();
			}
			if(group.getStatus().equals(GroupStatus.STOP)){
				if(group.getOriginType().equals(OriginType.INNER)){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
				}else{
					return new ArrayList<AgendaForwardDemandPO>();
				}
			}
			
			//			GroupType groupType = group.getType();
			List<BusinessGroupMemberPO> members = group.getMembers();
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
			group.getUserId();
			DeviceGroupAvtplPO g_avtpl = group.getAvtpl();
			new CodecParamBO().set(g_avtpl);
			
			//校验转发目的成员是否进会
			List<BusinessGroupMemberPO> dstMembers = new ArrayList<BusinessGroupMemberPO>();
			List<BusinessGroupMemberPO> _dstMembers = tetrisBvcQueryUtil.queryMembersByUserIds(members, dstUserIds);
			for(BusinessGroupMemberPO dstMember : _dstMembers){
				if(!dstMember.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)){
					if(!OriginType.OUTER.equals(group.getOriginType())){
						throw new BaseException(StatusCode.FORBIDDEN, dstMember.getName() + " 还未进入");
					}
				}else{
					dstMembers.add(dstMember);
				}
			}
			
			//描述源的BO
			List<MemberTerminalBO> bos = groupMemberService.generateMemberTerminalBOs(groupId, srcUserIds, null, bundleIds);
			
			//取出一分屏
			List<LayoutPO> layoutList = layoutDao.findByPositionNum(1L);
			if(layoutList.size() < 1){
				throw new BaseException(StatusCode.FORBIDDEN, "没有配置一分屏虚拟源");
			}
			LayoutPO singleLayout = layoutList.get(0);
			
			List<AgendaForwardPO> agendaForwardList = new ArrayList<AgendaForwardPO>();
			List<AgendaForwardSourcePO> agendaForwardSourceList = new ArrayList<AgendaForwardSourcePO>();
			List<AgendaForwardDestinationPO> agendaForwardDestinationList = new ArrayList<AgendaForwardDestinationPO>();
			List<AgendaForwardDemandPO> agendaForwardDemandList = new ArrayList<AgendaForwardDemandPO>();
			List<BusinessGroupMemberPO> onlineForwardMembers = new ArrayList<BusinessGroupMemberPO>();
			
			Integer needCreateAgendaForwardSizeLong = bos.size() * dstMembers.size();
			for(int i = 0; i < needCreateAgendaForwardSizeLong; i++){
				//创建议程转发。
				AgendaForwardPO agendaForward = new AgendaForwardPO();
				agendaForward.setName("媒体转发");
				agendaForward.setLayoutId(singleLayout.getId());
				agendaForward.setLayoutSelectionType(LayoutSelectionType.CONFIRM);
				agendaForward.setAudioType(AudioType.CUSTOM);
				agendaForward.setVolume(50);
				agendaForward.setBusinessId(groupId);
				agendaForward.setAgendaForwardBusinessType(BusinessInfoType.COMMAND_FORWARD);
				agendaForwardList.add(agendaForward);
			}
			agendaForwardDao.save(agendaForwardList);
			
			int count = 0;
			Map<Long, StringBuffer> dstMemberIdAndMessage = new HashMap<Long, StringBuffer>();
//			for(BusinessGroupMemberPO srcMember : forwardMembers){
			for(BusinessGroupMemberPO dstMember : dstMembers){
				
				//被点播的资源封装成member
//				List<MemberTerminalBO> bos = groupMemberService.generateMemberTerminalBOs(groupId, srcUserIds, null, bundleIds);
				List<BusinessGroupMemberPO> forwardMembers = groupMemberService.generateMembers(null, bos, null, false);
//				businessGroupMemberDao.save(forwardMembers);
				groupMemberService.fullfillGroupMember(forwardMembers);
				businessGroupMemberDao.save(forwardMembers);
				for(BusinessGroupMemberPO forwardMember : forwardMembers){
					forwardMember.setBusinessId(groupId.toString());
					//【对于目的是outer的，把源member标记为DISCONNECT，后边不打开也不关闭源member的编码器】
					if(OriginType.INNER.equals(dstMember.getOriginType())){
						forwardMember.setGroupMemberStatus(GroupMemberStatus.CONNECT);
						onlineForwardMembers.add(forwardMember);
					}
				}
				//save
				businessGroupMemberDao.save(forwardMembers);
				
				for(BusinessGroupMemberPO srcMember : forwardMembers){
					
					log.info(group.getName() + " 添加了媒体转发：" + srcMember.getName() + " 转发给 " + dstMember.getName());
					
					AgendaForwardPO agendaForward = agendaForwardList.get(count++);
					agendaForward.setName(srcMember.getName() +" 媒体转发给 ：" + dstMember.getName());
					String message = srcMember.getName() +"开始媒体转发给:" + dstMember.getName()+"； ";
					if(dstMemberIdAndMessage.get(dstMember.getId()) == null){
						dstMemberIdAndMessage.put(dstMember.getId(), new StringBuffer(message));
					}else{
						dstMemberIdAndMessage.get(dstMember.getId()).append(message);
					}
					
					//创建议程转发源
					List<BusinessGroupMemberTerminalChannelPO> channels = srcMember.getChannels();
					Optional<BusinessGroupMemberTerminalChannelPO> videoEncodeChannelOptional = channels.stream().filter(channl->{
						return TerminalChannelType.VIDEO_ENCODE.equals(channl.getTerminalChannelType());
					}).findFirst();
					
					AgendaForwardSourcePO source = new AgendaForwardSourcePO();
					source.setSourceType(SourceType.GROUP_MEMBER_CHANNEL);
					source.setLayoutPositionSelectionType(LayoutPositionSelectionType.AUTO_INCREMENT);
					source.setIsLoop(false);
					source.setSerialNum(1);//这是一分屏的position的serialNum
					source.setAgendaForwardId(agendaForward.getId());
					if(videoEncodeChannelOptional.isPresent()){
						Long sourceId = videoEncodeChannelOptional.get().getId();
						source.setSourceId(sourceId);
					}
					agendaForwardSourceList.add(source);
					
					//创建议程转发目的
					AgendaForwardDestinationPO destination = new AgendaForwardDestinationPO();
					destination.setDestinationId(dstMember.getId());
					destination.setDestinationType(DestinationType.GROUP_MEMBER);
					destination.setAgendaForwardId(agendaForward.getId());
					destination.setUpdateTime(new Date());
					agendaForwardDestinationList.add(destination);
					
					//创建媒体转发的扩展信息
					AgendaForwardDemandPO demand = new AgendaForwardDemandPO();
					if(GroupMemberType.MEMBER_DEVICE.equals(srcMember.getGroupMemberType())){
						demand.setForwardType(ForwardDemandBusinessType.FORWARD_DEVICE);
					}else if(GroupMemberType.MEMBER_USER.equals(srcMember.getGroupMemberType())){
						demand.setForwardType(ForwardDemandBusinessType.FORWARD_USER);
					}
					demand.setExecuteStatus(ForwardDemandStatus.DONE);
					demand.setBeginForwardTime(new Date());
					demand.setDstCode(dstMember.getCode());
					demand.setDstUserName(dstMember.getName());
					demand.setSrcCode(srcMember.getCode());
					demand.setSrcName(srcMember.getName());
					demand.setAgendaForwardId(agendaForward.getId());
					demand.setBusinessId(groupId);
					agendaForwardDemandList.add(demand);
				}
			}
//			}
			
			//保存
			agendaForwardDao.save(agendaForwardList);
			agendaForwardSourceDao.save(agendaForwardSourceList);
			agendaForwardDestinationDao.save(agendaForwardDestinationList);
			agendaForwardDemandDao.save(agendaForwardDemandList);
			
			//给成员推送message
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			JSONArray splits = new JSONArray();
			JSONObject message = new JSONObject();
			message.put("businessType", "commandForward");
//			message.put("businessInfo", group.getName() + " 开始媒体转发");
			message.put("businessId", group.getId().toString());
			message.put("splits", splits);
			List<BusinessGroupMemberPO> connectMembers = dstMembers.stream().filter(m->m.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)).collect(Collectors.toList());
			for(BusinessGroupMemberPO member : connectMembers){
				message.put("businessInfo", dstMemberIdAndMessage.get(member.getId()));
				messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			}
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				businessReturnService.add(null, cache, null);
			}
			
			//呼叫 connect的源
			DeviceGroupAvtplPO deviceGroupAvtplPO = group.getAvtpl();
			LogicBO logic = groupMemberService.openEncoder(onlineForwardMembers, deviceGroupAvtplPO, -1L, chairmanMember.getCode());
			businessReturnService.add(logic, null, "媒体转发呼叫编码器");
			
			//级联
			List<UserBO> userBos = new ArrayList<UserBO>();
			List<UserVO> userVos = userQuery.findByIdIn(srcUserIds);
			for(UserVO useVo : userVos){
				UserBO userBo = new UserBO();
				userBo.setUser(useVo);
				userBo.setUserNo(useVo.getUserno());
				userBos.add(userBo);
			}
			List<BundlePO> bundlePOs = bundleDao.findByBundleIdIn(bundleIds);
			BusinessType businessType = group.getBusinessType();
			if(!OriginType.OUTER.equals(group.getOriginType())){
				GroupBO groupBO = groupCascadeUtil.startDeviceForward(group, bundlePOs, userBos, dstMembers);
				if(BusinessType.COMMAND.equals(businessType)){
					commandCascadeService.startDeviceForward(groupBO);
				}else if(BusinessType.MEETING_BVC.equals(businessType)){
					conferenceCascadeService.startDeviceForward(groupBO);
				}
			}
			
			log.info(group.getName() + " 共添加了" + agendaForwardList.size() + "条媒体转发");
			RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
			if(runningAgenda == null){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName()+"没有正在执行的议程");
			}
			agendaExecuteService.runAgenda(groupId, runningAgenda.getAgendaId(), null, true);
			
			return agendaForwardDemandList;
		}
	}
	
	/**
	 * 停止一个会议中的多个转发<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午5:02:06
	 * @param groupId 操作人
	 * @param groupId 议程转发
	 * @param demandIds AgendaForwardDemandPO的id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void stop(Long userId, Long groupId, List<Long> demandIds) throws Exception{
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
		
			GroupPO group = groupDao.findOne(groupId);
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(group.getMembers());
			BusinessGroupMemberPO opMember = tetrisBvcQueryUtil.queryBusinessMemberByUserId(group.getMembers(), userId);
			
			//清理议程转发、议程转发源、议程转发目的、转发扩展信息
			List<AgendaForwardDemandPO> allStopDemands = agendaForwardDemandDao.findAll(demandIds);
			List<AgendaForwardDemandPO> fileStopDemands = allStopDemands.stream().filter(demand ->{
				return ForwardDemandBusinessType.FORWARD_FILE.equals(demand.getForwardType());
			}).collect(Collectors.toList());
			//设备和成员的转发
			List<AgendaForwardDemandPO> otherStopDemands = new ArrayList<AgendaForwardDemandPO>(allStopDemands);
			otherStopDemands.removeAll(fileStopDemands);
			
			if(fileStopDemands.size() > 0){

				log.info(group.getName() + " 停止文件媒体转发，个数：" + fileStopDemands.size());
				
				//清理转发扩展信息
				List<String> dstCodeList = fileStopDemands.stream().map(AgendaForwardDemandPO::getDstCode).collect(Collectors.toList());
				List<BusinessGroupMemberPO> dstGroupMemberList = businessGroupMemberDao.findByGroupIdAndCodeIn(groupId, dstCodeList);
				agendaForwardDemandDao.deleteInBatch(fileStopDemands);
				
				BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
				
				//停止pageTask
				List<String> businessIdList = fileStopDemands.stream().map(stopDemand ->{
					return groupId.toString()+"-"+stopDemand.getId().toString();
				}).collect(Collectors.toList());
				List<PageTaskPO> tasks = pageTaskDao.findByBusinessIdInAndBusinessInfoType(businessIdList, BusinessInfoType.COMMAND_FORWARD_FILE);
				businessDeliverBO.getStopPageTasks().addAll(tasks);
				
				//给成员推送message
				List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
				JSONArray splits = new JSONArray();
				JSONObject message = new JSONObject();
				message.put("businessType", "commandForwardStop");
				message.put("businessInfo", group.getName() + " 停止文件转发转发");
				message.put("businessId", group.getId().toString());
				message.put("splits", splits);
				List<BusinessGroupMemberPO> connectMembers = dstGroupMemberList.stream().filter(m->m.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)).collect(Collectors.toList());
				for(BusinessGroupMemberPO member : connectMembers){
					messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
				}
				
				//发消息
				for(MessageSendCacheBO cache : messageCaches){
					businessReturnService.add(null, cache, null);
				}
				
				deliverExecuteService.execute(businessDeliverBO, "停止点播文件：" + group.getName(), true);
			}
			
			if(otherStopDemands.size() > 0){
				
				log.info(group.getName() + " 停止媒体转发，个数：" + otherStopDemands.size());
				
				List<Long> idList = otherStopDemands.stream().map(AgendaForwardDemandPO::getAgendaForwardId).collect(Collectors.toList());
				List<AgendaForwardPO> needStopforwards = agendaForwardDao.findAll(idList);
				List<Long> forwardIds = needStopforwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
				
				List<String> dstCodeList = otherStopDemands.stream().map(AgendaForwardDemandPO::getDstCode).collect(Collectors.toList());
				List<BusinessGroupMemberPO> dstGroupMemberList = businessGroupMemberDao.findByGroupIdAndCodeIn(groupId, dstCodeList);
				
				List<AgendaForwardSourcePO> sources = agendaForwardSourceDao.findByAgendaForwardIdIn(forwardIds);
				List<AgendaForwardDestinationPO> destinations = agendaForwardDestinationDao.findByAgendaForwardIdIn(forwardIds);
				
				List<Long> srcMemberChannelIds = sources.stream().map(AgendaForwardSourcePO::getSourceId).collect(Collectors.toList());
				List<BusinessGroupMemberPO> allSrcMember = businessGroupMemberDao.findByBusinessId(groupId.toString());
				Map<BusinessGroupMemberPO, List<Long>> memberAndChannelIdMap = 
						allSrcMember.stream().collect(Collectors.toMap(Function.identity(), BusinessGroupMemberPO::allChannelIds));
				
				
				List<BusinessGroupMemberPO> deleteGroupMemberList = new ArrayList<BusinessGroupMemberPO>();
				List<Long> deleteMemberIds = new ArrayList<Long>();
				for(Long srcChannelId :srcMemberChannelIds){
					for(BusinessGroupMemberPO member : allSrcMember){
						List<Long> channels = memberAndChannelIdMap.get(member);
						if(channels.contains(srcChannelId)){
							deleteGroupMemberList.add(member);
							deleteMemberIds.add(member.getId());
							break;
						}
					}
				}
				
				businessGroupMemberDao.deleteByIdIn(deleteMemberIds);
				agendaForwardDemandDao.deleteInBatch(otherStopDemands);
				agendaForwardSourceDao.deleteInBatch(sources);
				agendaForwardDestinationDao.deleteInBatch(destinations);
				agendaForwardDao.deleteByIdIn(forwardIds);
				
				RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
				if(runningAgenda == null){
					throw new BaseException(StatusCode.FORBIDDEN, group.getName()+"没有正在执行的议程");
				}
				//级联，循环发多个协议
				BusinessType businessType = group.getBusinessType();
				if(opMember!=null && !OriginType.OUTER.equals(opMember.getOriginType())){
					for(AgendaForwardDemandPO stopDemand : otherStopDemands){
						if(!BaseUtils.stringIsNotBlank(stopDemand.getSrcCode())){
							throw new BaseException(StatusCode.FORBIDDEN, stopDemand.getSrcName()+" 没有号码");
						}
						GroupBO groupBO = groupCascadeUtil.stopDeviceForward(group, stopDemand.getSrcCode(), stopDemand.getDstCode(), opMember);
						if(BusinessType.COMMAND.equals(businessType)){
							commandCascadeService.stopDeviceForward(groupBO);
						}else if(BusinessType.MEETING_BVC.equals(businessType)){
							conferenceCascadeService.stopDeviceForward(groupBO);
						}
					}
				}
				
				//给成员推送message
				List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
				JSONArray splits = new JSONArray();
				JSONObject message = new JSONObject();
				message.put("businessType", "commandForwardStop");
				message.put("businessInfo", group.getName() + " 停止媒体转发");
				message.put("businessId", group.getId().toString());
				message.put("splits", splits);
				List<BusinessGroupMemberPO> connectMembers = dstGroupMemberList.stream().filter(m->m.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)).collect(Collectors.toList());
				for(BusinessGroupMemberPO member : connectMembers){
					messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
				}
				
				//发消息
				for(MessageSendCacheBO cache : messageCaches){
					businessReturnService.add(null, cache, null);
				}
				
				//关闭 CONNECT的 编码器
				List<BusinessGroupMemberPO> onlineGroupMemberList = deleteGroupMemberList.stream()
						.filter(member -> member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)).collect(Collectors.toList());
				CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
				LogicBO logic = groupMemberService.closeEncoder(onlineGroupMemberList, codec, -1L, chairmanMember.getCode());
				businessReturnService.add(logic, null, "媒体转发关闭");
				
				agendaExecuteService.runAgenda(groupId, runningAgenda.getAgendaId(), null, true);
			}
		}
	}
	
	/**
	 * 成员停止多个转发<br/>
	 * <p>支持不同会议中的转发</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:07:38
	 * @param userId 操作人
	 * @param groupId
	 * @param demandIds AgendaForwardPO的id
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	@Transactional(rollbackFor = Exception.class)
	public JSONArray stopByMember(Long userId, Long groupId, List<Long> demandIds) throws Exception{
		
		synchronized (new StringBuffer().append(lockPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(group.getMembers());
			BusinessGroupMemberPO opMember = tetrisBvcQueryUtil.queryBusinessMemberByUserId(group.getMembers(), userId);
			
			//清理议程转发、议程转发源、议程转发目的、转发扩展信息
			List<AgendaForwardPO> needStopforwards = agendaForwardDao.findAll(demandIds);
			List<Long> idList = needStopforwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
			List<AgendaForwardDemandPO> stopDemands = agendaForwardDemandDao.findByAgendaForwardIdIn(idList);
			List<Long> forwardIds = needStopforwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
			
			List<String> dstCodeList = stopDemands.stream().map(AgendaForwardDemandPO::getDstCode).collect(Collectors.toList());
			List<BusinessGroupMemberPO> dstGroupMemberList = businessGroupMemberDao.findByGroupIdAndCodeIn(groupId, dstCodeList);
			
			List<AgendaForwardSourcePO> sources = agendaForwardSourceDao.findByAgendaForwardIdIn(forwardIds);
			List<AgendaForwardDestinationPO> destinations = agendaForwardDestinationDao.findByAgendaForwardIdIn(forwardIds);
			
			List<Long> srcMemberChannelIds = sources.stream().map(AgendaForwardSourcePO::getSourceId).collect(Collectors.toList());
			List<BusinessGroupMemberPO> allSrcMember = businessGroupMemberDao.findByBusinessId(groupId.toString());
			Map<BusinessGroupMemberPO, List<Long>> memberAndChannelIdMap = 
					allSrcMember.stream().collect(Collectors.toMap(Function.identity(), BusinessGroupMemberPO::allChannelIds));
			
			
			List<BusinessGroupMemberPO> deleteGroupMemberList = new ArrayList<BusinessGroupMemberPO>();
			List<Long> deleteMemberIds = new ArrayList<Long>();
			for(Long srcChannelId :srcMemberChannelIds){
				for(BusinessGroupMemberPO member : allSrcMember){
					List<Long> channels = memberAndChannelIdMap.get(member);
					if(channels.contains(srcChannelId)){
						deleteGroupMemberList.add(member);
						deleteMemberIds.add(member.getId());
						break;
					}
				}
			}
			
			businessGroupMemberDao.deleteByIdIn(deleteMemberIds);
			agendaForwardDemandDao.deleteInBatch(stopDemands);
			agendaForwardSourceDao.deleteInBatch(sources);
			agendaForwardDestinationDao.deleteInBatch(destinations);
			agendaForwardDao.deleteByIdIn(forwardIds);
			
			RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
			if(runningAgenda == null){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName()+"没有正在执行的议程");
			}
			//级联，循环发多个协议
			BusinessType businessType = group.getBusinessType();
			if(opMember!=null && !OriginType.OUTER.equals(group.getOriginType())){
				for(AgendaForwardDemandPO stopDemand : stopDemands){
					if(!BaseUtils.stringIsNotBlank(stopDemand.getSrcCode())){
						throw new BaseException(StatusCode.FORBIDDEN, stopDemand.getSrcName()+" 没有号码");
					}
					GroupBO groupBO = groupCascadeUtil.stopDeviceForward(group, stopDemand.getSrcCode(), stopDemand.getDstCode(), opMember);
					if(BusinessType.COMMAND.equals(businessType)){
						commandCascadeService.stopDeviceForward(groupBO);
					}else if(BusinessType.MEETING_BVC.equals(businessType)){
						conferenceCascadeService.stopDeviceForward(groupBO);
					}
				}
			}
			
//			//发消息
//			List<Long> consumeIds = new ArrayList<Long>();
//			for(Map.Entry<Long, JSONArray> entry: result.entrySet()){
//				System.out.println("Key: "+ entry.getKey()+ " Value: "+entry.getValue());
//				Long userId = entry.getKey();
//				JSONArray splits = new JSONArray();
//				JSONObject message = new JSONObject();
//				message.put("businessType", "commandForwardStop");
//				message.put("businessId", group.getId().toString());
//				message.put("businessInfo", group.getName() + " 停止了转发");
//				message.put("splits", splits);
//				WebsocketMessageVO ws = websocketMessageService.send(userId, message.toJSONString(), WebsocketMessageType.COMMAND, group.getUserId(), group.getUserName());
//				consumeIds.add(ws.getId());
//			}
//			websocketMessageService.consumeAll(consumeIds);
			
			//给成员推送message
			List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
			JSONArray splits = new JSONArray();
			JSONObject message = new JSONObject();
			message.put("businessType", "commandForwardStop");
			message.put("businessInfo", group.getName() + " 停止媒体转发");
			message.put("businessId", group.getId().toString());
			message.put("splits", splits);
			List<BusinessGroupMemberPO> connectMembers = dstGroupMemberList.stream().filter(m->m.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)).collect(Collectors.toList());
			for(BusinessGroupMemberPO member : connectMembers){
				messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			}
			
			//发消息
			for(MessageSendCacheBO cache : messageCaches){
				businessReturnService.add(null, cache, null);
			}
			
			//关闭编码器 
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			LogicBO logic = groupMemberService.closeEncoder(deleteGroupMemberList, codec, -1L, chairmanMember.getCode());
			businessReturnService.add(logic, null, "媒体转发关闭");
			
			agendaExecuteService.runAgenda(groupId, runningAgenda.getAgendaId(), null, true);
		}
		return null;
	}
	
	/**
	 * 会议转发文件，不需要成员同意<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月16日 下午2:35:38
	 * @param groupId
	 * @param resourceIds 源文件
	 * @param memberIds 目的
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<ForwardReturnBO> forwardFile(Long groupId, List<String> resourceIds, List<Long> memberIds) throws Exception{
		
		TerminalPO terminal = terminalDao.findByType(TerminalType.QT_ZK);
		
		synchronized (new StringBuffer().append(lockFilePrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(!GroupStatus.START.equals(group.getStatus())){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 未开始执行，无法操作");
			}
			
			//查找资源
			JSONArray files = new JSONArray();
			for(String resourceId : resourceIds){
				JSONObject file = resourceService.queryOnDemandResourceById(resourceId);
				if(file == null) throw new ResourceNotExistException(resourceId);
				files.add(file);				
			}
			
			//校验转发目的成员是否进会
			List<BusinessGroupMemberPO> members = group.getMembers();
			List<BusinessGroupMemberPO> dstMembers = members.stream().filter(member ->{
				return memberIds.contains(member.getId());
			}).filter(member ->{
				return GroupMemberStatus.CONNECT.equals(member.getGroupMemberStatus());
			}).collect(Collectors.toList());
			
			Map<String, List<PageTaskPO>> originIdAndPageTaskMap = new HashMap<String, List<PageTaskPO>>();
			Map<String, List<AgendaForwardDemandPO>> originIdAndAgendaForwardDemandMap = new HashMap<String, List<AgendaForwardDemandPO>>();
			
			//创建AgendaForwardDemandPO媒体转发扩展信息
			for(BusinessGroupMemberPO dstMember : dstMembers){
				
				if(OriginType.OUTER.equals(dstMember.getOriginType())){
					throw new BaseException(StatusCode.FORBIDDEN, dstMember.getName() + "是跨系统成员，无法转发文件");
				}
				
				List<AgendaForwardDemandPO> agendaForwardDemandList = new ArrayList<AgendaForwardDemandPO>();
				for(int i=0; i<files.size(); i++){
					JSONObject file = files.getJSONObject(i);
					String resourceId = file.getString("resourceId");
					String resourceName = file.getString("name");
					String previewUrl = file.getString("previewUrl");
					
					//创建媒体转发的扩展信息
					AgendaForwardDemandPO demand = new AgendaForwardDemandPO();
					demand.setForwardType(ForwardDemandBusinessType.FORWARD_FILE);
					demand.setExecuteStatus(ForwardDemandStatus.DONE);
					demand.setBeginForwardTime(new Date());
					demand.setDstCode(dstMember.getCode());
					demand.setDstUserName(dstMember.getName());
					demand.setSrcCode(resourceId);
					demand.setSrcName(resourceName);
					demand.setBusinessId(groupId);
					demand.setPreviewUrl(previewUrl);
					agendaForwardDemandList.add(demand);
				}
				originIdAndAgendaForwardDemandMap.put(dstMember.getOriginId(), agendaForwardDemandList);
			}
			List<AgendaForwardDemandPO> allAgendaForwardDemands = originIdAndAgendaForwardDemandMap.values().stream().flatMap(demandList -> demandList.stream()).collect(Collectors.toList());
			agendaForwardDemandDao.save(allAgendaForwardDemands);
			
			//创建PageTaskPO
			for(BusinessGroupMemberPO dstMember : dstMembers){
				
				List<AgendaForwardDemandPO> demands = originIdAndAgendaForwardDemandMap.get(dstMember.getOriginId());
				List<PageTaskPO> tasks = new ArrayList<PageTaskPO>();
				for(AgendaForwardDemandPO demand : demands){
					PageTaskPO task = new PageTaskPO();
					task.setBusinessInfoType(BusinessInfoType.COMMAND_FORWARD_FILE);
					task.setBusinessName("转发" + demand.getSrcName() + "文件");
					task.setBusinessId(groupId.toString() + "-" + demand.getId());
					task.setPlayUrl(demand.getPreviewUrl());
					tasks.add(task);
				}
				originIdAndPageTaskMap.put(dstMember.getOriginId(),tasks);
			}
			
			//发布信息
			for(BusinessGroupMemberPO dstMember : dstMembers){
				PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(dstMember.getOriginId(), terminal.getId(), GroupMemberType.MEMBER_USER);
				if(originIdAndPageTaskMap.get(dstMember.getOriginId()).size() > 0){
					pageTaskService.addAndRemoveTasks(pageInfo, originIdAndPageTaskMap.get(dstMember.getOriginId()), null);
				}
				
			}
			
			//发消息
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			deliverExecuteService.execute(businessDeliverBO, "转发文件", true);
			
		}
		return null;
	}
	
}
