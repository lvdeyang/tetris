package com.sumavision.tetris.bvc.business.group.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netflix.infix.lang.infix.antlr.EventFilterParser.boolean_expr_return;
import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordFragmentDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.PublishStreamSetBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.bo.RecordSourceBO;
import com.sumavision.bvc.device.group.dao.GroupPublishStreamDAO;
import com.sumavision.bvc.device.group.dao.GroupRecordDAO;
import com.sumavision.bvc.device.group.dao.GroupRecordInfoDAO;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.bvc.device.group.po.GroupPublishStreamPO;
import com.sumavision.bvc.device.group.po.GroupRecordInfoPO;
import com.sumavision.bvc.device.group.po.GroupRecordPO;
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeIpMissingException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeNotExistException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodePortMissionException;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO.ResultDstBO;
import com.sumavision.bvc.meeting.logic.record.mims.MimsService;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CombineAudioPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDemandDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.CombineAudioPermissionPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundlePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.AudioPriority;
import com.sumavision.tetris.bvc.model.agenda.CustomAudioPermissionType;
import com.sumavision.tetris.bvc.model.agenda.DestinationType;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.model.terminal.channel.ChannelParamsType;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.AgendaRoleMemberUtil;
import com.sumavision.tetris.bvc.util.MultiRateUtil;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: GroupRecordService 
* @Description: 会议录制业务
* @author zsy
* @date 2019年11月18日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class GroupRecordService {
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private GroupRecordDAO groupRecordDao;
	
	//@Autowired
	//private AgendaDAO agendaDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private CommonForwardDAO commonForwardDAO;
	
	@Autowired
	private BusinessGroupMemberDAO groupMemberDao;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private GroupDemandDAO groupDemandDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
		
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupRecordDAO commandGroupRecordDao;
	
	@Autowired
	private CommandGroupRecordFragmentDAO commandGroupRecordFragmentDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	//@Autowired
	//private AgendaExecuteService agendaExecuteService;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private MimsService mimsService;

	@Autowired
	private ExecuteBusinessProxy executeBusiness;	
	
	@Autowired
	private GroupPublishStreamDAO groupPublishStreamDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private AgendaRoleMemberUtil agendaRoleMemberUtil;

	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private CombineTemplateGroupAgendeForwardPermissionDAO combineTemplateGroupAgendeForwardPermissiondao;
	
	@Autowired
	private MultiRateUtil multiRateUtil;
	
	@Autowired
	private GroupRecordInfoDAO groupRecordInfoDao;
	
	@Autowired
	private CombineAudioPermissionDAO combineAudioPermissionDao;
	
	/**
	 * 会议开始录制<br/>
	 * <p>所有录制都不呼叫编码器，停止的时候也不挂断编码器</p>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月27日 下午4:26:13
	 * @param userId 操作人userId
	 * @param groupId
	 * @param name
	 * @throws Exception
	 */
	public void start(
			Long userId,
			Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(group.getMembers());
			String chairmanUserId = chairmanMember.getOriginId();
			
			if(userId == null){
				throw new BaseException(StatusCode.FORBIDDEN, "获取不到操作人的用户id");
			}
			
			if(!chairmanUserId.equals(userId.toString())){
				throw new BaseException(StatusCode.FORBIDDEN, "只有主席才能进行录制");
			}
			if(group.getStatus().equals(GroupStatus.STOP)){
				throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
			}
			String logStr = group.getName() + " 开始录制，操作人userId：" + userId;
			log.info(logStr);
			
			UserBO admin = new UserBO(); admin.setId(-1L);
			String adminUserId = admin.getId().toString();
//			CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();						
			LogicBO logic = new LogicBO();
			
			GroupRecordInfoPO recordInfo = new GroupRecordInfoPO();
			Date startTime = new Date();
			recordInfo.setGroupInfo(group);
			recordInfo.setRecordUserId(userId);
			recordInfo.setStartTime(startTime);
			recordInfo.setRun(true);
//			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
//			record.setPlayerSplitLayout(userInfo.obtainUsingGroup().getPlayerSplitLayout());
			recordInfo.setRecords(new ArrayList<GroupRecordPO>());
//			record.setFragments(new ArrayList<CommandGroupRecordFragmentPO>());			
			
			//开始修改
			List<BusinessGroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			//1.获取PageInfo里边有当前分页页面包含的各种信息（终端类型、用户id、页面规格、用户成员类型）
			//2.获取Pagetask里边的页面转发相关的关系（需要筛选出指挥对应的     BusinessInfoType.BASIC_COMMAND）
			TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
			PageInfoPO  pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(userId.toString(), terminal.getId(), GroupMemberType.MEMBER_USER);
			
			List<PageTaskPO> pageTasks = null;
			if(group.getBusinessType().equals(BusinessType.COMMAND)){
				pageTasks=pageTaskDao.findByBusinessInfoTypeAndPageInfoId(BusinessInfoType.BASIC_COMMAND,pageInfo.getId());
			}else if(group.getBusinessType().equals(BusinessType.MEETING_QT)
					|| group.getBusinessType().equals(BusinessType.MEETING_BVC)){
				pageTasks=pageTaskDao.findByBusinessInfoTypeAndPageInfoId(BusinessInfoType.BASIC_MEETING,pageInfo.getId());
			}
			
			if(pageTasks!=null){
				for(PageTaskPO pageTask:pageTasks){
					//判断的是音频或者视频只要一个是执行状态
					if(pageTask != null && (pageTask.getAudioStatus().equals(ExecuteStatus.DONE)||pageTask.getVideoStatus().equals(ExecuteStatus.DONE))){
						
						GroupRecordPO groupRecord = new GroupRecordPO();
						groupRecord.generateInformation(pageTask);
						groupRecord.setInfo(chairmanMember.getName());
						
						//拼预览地址
						String urlIndex = groupId + "-" + userId + "-" + Integer.toString(recordInfo.getUuid().hashCode()).replace("-", "m") + Integer.toString(groupRecord.getUuid().hashCode()).replace("-", "m") + "/video";
						String previewUrl = urlIndex + ".m3u8";
						groupRecord.setPreviewUrl(previewUrl);
						
						groupRecord.setRecordInfo(recordInfo);
						recordInfo.getRecords().add(groupRecord);
						
						logic.merge(startRecord(groupRecord, codec, adminUserId));
					}
				}
			}
			
//			GroupMemberPO thisMember = tetrisBvcQueryUtil.queryMemberByUserId(members, userId);
//			for(GroupMemberPO member : members){
//				
//				if(member.getIsAdministrator()) continue;
//				if(!member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)) continue;
//				
//				List<SourceBO> sources = tetrisBvcQueryUtil.querySourceBOsByMemberIds(sourceBOs, member.getId());
//				for(SourceBO source : sources){
//				
//					CommandGroupRecordFragmentPO fragment = new CommandGroupRecordFragmentPO().setByMemberSource(member, source, startTime);
//					fragment.setInfo(member.getName());
//					//拼预览地址
//					String urlIndex = groupId + "-" + userId + "-" + Integer.toString(record.getUuid().hashCode()).replace("-", "m") + Integer.toString(fragment.getUuid().hashCode()).replace("-", "m") + "/video";
//					String previewUrl = urlIndex + ".m3u8";
//					fragment.setPreviewUrl(previewUrl);
//					
//					fragment.setRecord(record);
//					record.getFragments().add(fragment);
//					
//					logic.merge(startRecord(fragment, codec, adminUserId));
//
//				}
//			}
			
			groupRecordInfoDao.save(recordInfo);
			
			//发协议logic，把返回的layerId写入数据库
			businessReturnService.dealLogic(logic, logStr, groupId);
//			ExecuteBusinessReturnBO response = executeBusiness.execute(logic, logStr);
//			for(ResultDstBO result : response.getOutConnMediaMuxSet()){
//				for(GroupRecordPO groupRecord : recordInfo.getRecords()){
//					if(groupRecord.getUuid().equals(result.getUuid())){
//						groupRecord.setStoreLayerId(result.getLayerId());
//						break;
//					}
//				}
//			}
			groupRecordInfoDao.save(recordInfo);		
		}
	}
	
	/**
	 * 更新录制<br/>
	 * <p>线程不安全</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月9日 下午3:31:11
	 * @param userId 操作人userId，当mode为0时需要。目前userId必须为主席的，如果其他人也进行录制，需要修改成批量
	 * @param group
	 * @param mode 0新建录制（给操作人），1更新会议中的所有录制
	 * @param doProtocol
	 * @return LogicBO
	 * @throws Exception
	 */
	public LogicBO update(
			Long userId,
			CommandGroupPO group,
			int mode,
			boolean doProtocol) throws Exception{
		/*TODO if(mode==0 && !group.getUserId().equals(userId)){
			throw new BaseException(StatusCode.FORBIDDEN, "只有主席才能进行录制");
		}
		if(group.getStatus().equals(GroupStatus.STOP)){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName() + " 已停止，无法操作，id: " + group.getId());
		}
		
		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);						
		LogicBO logic = new LogicBO();				

		Date startTime = new Date();
		CommandGroupRecordPO record;
		List<CommandGroupRecordPO> records = commandGroupRecordDao.findByGroupIdAndRun(group.getId(), true);
		if(records.size() > 0){
			//目前只处理主席的录制
			record = records.get(0);
		}else{
			if(mode == 1){
				//没有录制需要更新
				return logic;
			}
			record = new CommandGroupRecordPO();
			record.setGroupInfo(group);
			record.setRecordUserId(userId);
			record.setStartTime(startTime);
			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
			record.setPlayerSplitLayout(userInfo.obtainUsingGroup().getPlayerSplitLayout());
			record.setFragments(new ArrayList<CommandGroupRecordFragmentPO>());
		}
		String logStr = group.getName() + " 更新录制，操作人userId：" + userId;
		log.info(logStr);
		
//		UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
		UserBO admin = new UserBO(); admin.setId(-1L);
		String adminUserId = admin.getId().toString();
		List<CommandGroupMemberPO> members = group.getMembers();
		List<CommandGroupForwardPO> forwards = group.getForwards();
//		List<CommandGroupForwardDemandPO> demands = group.getForwardDemands();
		CommandGroupMemberPO thisMember = commandCommonUtil.queryMemberByUserId(members, userId);
		List<CommandGroupUserPlayerPO> allPlayers = thisMember.getPlayers();
		
		//停止多余的录制
		for(CommandGroupRecordFragmentPO fragment : record.getFragments()){
			if(fragment.isRun()){
				CommandGroupForwardPO forward = commandCommonUtil.queryForwardBySrcAndDstMemberId(forwards, fragment.getSrcMemberId(), thisMember.getId());
				if(forward == null || !forward.getExecuteStatus().equals(ExecuteStatus.DONE)){
					//录制已失效，停止
					fragment.setRun(false);
					//生成协议
					logic.merge(pauseRecord(fragment, codec, adminUserId));
				}
			}
		}
		
		//通过播放器查forward，与当前的需求相符。也可以使用dstMember作为目的来查forward
		for(CommandGroupUserPlayerPO player : allPlayers){
			
			//找该播放器的转发
			CommandGroupForwardPO forward = commandCommonUtil.queryForwardByDstVideoBundleId(forwards, player.getBundleId());
			if(forward != null){// && forward.getExecuteStatus().equals(ExecuteStatus.DONE)){
				
				com.sumavision.bvc.command.group.enumeration.ExecuteStatus executeStatus = forward.getExecuteStatus();
				CommandGroupRecordFragmentPO fragment;
				fragment = commandCommonUtil.queryFragmentBySrcMemberId(record, forward.getSrcMemberId());
				if(fragment == null){
					//在某个用户被从会议删除之后，又加入到会议，那么其memberId会改变，使用info即用户名查询
					CommandGroupMemberPO srcMember = commandCommonUtil.queryMemberById(members, forward.getSrcMemberId());
					if(srcMember != null){
						fragment = commandCommonUtil.queryFragmentByInfo(record, srcMember.getUserName());
						if(fragment != null){
							fragment.setSrcMemberId(srcMember.getId());							
						}
					}
				}
				if(fragment != null){
					//找到片段
					if(executeStatus.equals(ExecuteStatus.DONE) && !fragment.isRun()){
						//更新源
						fragment.setRun(true);
						logic.merge(startRecord(fragment, codec, adminUserId));
					}else if(!executeStatus.equals(ExecuteStatus.DONE) && fragment.isRun()){
						//删除源（暂停）
						fragment.setRun(false);
						logic.merge(pauseRecord(fragment, codec, adminUserId));
					}
				}else{
					if(executeStatus.equals(ExecuteStatus.DONE)){
						//新建fragment
						fragment = new CommandGroupRecordFragmentPO().setByForward(forward, player, startTime);
						CommandGroupMemberPO srcMember = commandCommonUtil.queryMemberById(members, forward.getSrcMemberId());
						fragment.setInfo(srcMember.getUserName());
						//拼预览地址
						String urlIndex = group.getId() + "-" + userId + "-" + Integer.toString(record.getUuid().hashCode()).replace("-", "m") + Integer.toString(fragment.getUuid().hashCode()).replace("-", "m") + "/video";
						String previewUrl = urlIndex + ".m3u8";
						fragment.setPreviewUrl(previewUrl);
						
						fragment.setRecord(record);
						record.getFragments().add(fragment);
						
						logic.merge(startRecord(fragment, codec, adminUserId));
					}
				}
			}
			
//			//找DONE的转发点播（暂时不要）
//			CommandGroupForwardDemandPO demand = commandCommonUtil.queryForwardByDstVideoBundleId(demands, player.getBundleId());
//			if(demand != null && demand.getExecuteStatus().equals(ForwardDemandStatus.DONE)){
//				CommandGroupRecordFragmentPO fragment = new CommandGroupRecordFragmentPO().setByDemand(demand, player, startTime);
//				fragment.setInfo(demand.getVideoBundleName());
//				fragment.setRecord(record);
//				record.getFragments().add(fragment);
//			}
		}
		
		commandGroupRecordDao.save(record);
		
		if(doProtocol){			
			//发协议logic，把返回的layerId写入数据库
			ExecuteBusinessReturnBO response = executeBusiness.execute(logic, logStr);
			for(ResultDstBO result : response.getOutConnMediaMuxSet()){
				for(CommandGroupRecordFragmentPO fragment : record.getFragments()){
					if(fragment.getUuid().equals(result.getUuid())){
						fragment.setStoreLayerId(result.getLayerId());
						break;
					}
				}
			}
			commandGroupRecordDao.save(record);
		}
		return logic;*/
		return null;
	}
	
	/**
	 * 把执行logic返回的layerId写入数据库<br/>
	 * <p>在update方法得到logic并执行之后使用</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月9日 下午3:33:40
	 * @param returnBO
	 * @param groupId
	 */
	public void saveStoreInfo(ExecuteBusinessReturnBO returnBO, Long groupId){
		GroupRecordInfoPO recordInfo = groupRecordInfoDao.findByGroupIdAndRun(groupId, true);
		List<GroupRecordPO> records = recordInfo.getRecords();
		for(ResultDstBO result : returnBO.getOutConnMediaMuxSet()){
			for(GroupRecordPO record : records){
					if(record.getUuid().equals(result.getUuid())){
						record.setStoreLayerId(result.getLayerId());
					}
			}
		}
		groupRecordDao.save(records);
	}

	/**
	 * (待修改)会议停止录制<br/>
	 * <p>所有录制都不呼叫编码器，停止的时候也不挂断编码器</p>
	 * <p>该方法不会修改group，只读取groupName，因此不需要处理group的同步</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月22日 上午8:16:48
	 * @param groupId
	 * @param userId 操作人userId，如果为null则是停止该会议的全部录制
	 * @param doProtocol 是否下发协议
	 * @throws Exception
	 */
	public LogicBO stop(Long userId, Long groupId, boolean doProtocol) throws Exception{
		
		GroupPO group = groupDao.findOne(groupId);
		if(userId!=null && !group.getUserId().equals(userId)){
			throw new BaseException(StatusCode.FORBIDDEN, "只有主席才能进行操作");
		}
		Date endTime = new Date();
		LogicBO logic = new LogicBO();
		
		String tempStr = "全部";
		if(userId != null){
			tempStr = "操作用户id：" + userId; 
		}
		
		GroupRecordInfoPO recordInfo = null;
		if(userId == null){
			//停止全部录制
			recordInfo = groupRecordInfoDao.findByGroupIdAndRun(groupId, true);
		}else{
			//停止该用户的录制
			recordInfo = groupRecordInfoDao.findByGroupIdAndRecordUserIdAndRun(groupId, userId, true);
		}
		
		if(recordInfo != null){
			log.info(group.getName() + "停止录制" + tempStr);
		}else{
			return logic;
		}
		
//		UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
		UserBO admin = new UserBO(); admin.setId(-1L);
		String adminUserId = admin.getId().toString();
		recordInfo.setRun(false);
		recordInfo.setEndTime(endTime);
		for(GroupRecordPO groupRecord : recordInfo.getRecords()){
			//停止录制时，此时run字段为false表示录制暂停，但没有停止，所以所有的fragment都下协议
			groupRecord.setRun(false);
			groupRecord.setEndTime(endTime);
			//生成协议
			logic.merge(stopRecord(groupRecord, adminUserId));
		}
		
		//清除直播
		GroupPublishStreamPO needDeletePublishStreams = groupPublishStreamDao.findByGroupId(groupId);
		logic.merge(stopPublishStream(needDeletePublishStreams, adminUserId));
		
		mimsService.generateTetrisMimsResource(recordInfo.getRecords());
		
		groupRecordInfoDao.save(recordInfo);
		
		//发协议logic
		if(doProtocol){
			executeBusiness.execute(logic, group.getName() + " 停止录制，操作用户id：" + userId);
		}
		
		return logic;
	}
	
	public void remove(Long recordId) throws Exception{
		
		CommandGroupRecordPO record = commandGroupRecordDao.findOne(recordId);
		if(record.isRun()){
			throw new BaseException(StatusCode.FORBIDDEN, "录制未停止，无法删除，录制id: " + record);
		}
		
		//向cdn发送删除文件命令
		LogicBO logic = new LogicBO().setUserId("-1")
				.setPass_by(new ArrayList<PassByBO>());
		List<CommandGroupRecordFragmentPO> fragments = record.getFragments();
		if(fragments!=null && fragments.size()>0){
			
			//统计每个cdn接入下需要删除的文件目录
			Map<String, List<String>> layerMap = new HashMap<String, List<String>>();
			for(CommandGroupRecordFragmentPO fragment : fragments){
				String fileName = null;
				if(fragment.getPreviewUrl() != null){
					fileName = fragment.getPreviewUrl().split("/")[0];
				}
				String layerId = fragment.getStoreLayerId();
				if(fileName!=null && !fileName.equals("")
						&& layerId!=null && !layerId.equals("")){
					if(layerMap.get(layerId) == null){
						layerMap.put(layerId, new ArrayList<String>());
					}
					List<String> files = layerMap.get(layerId);
					files.add(fileName);
				}
			}
			
			//给每个cdn的layerId各生成一个passby命令
			for(String layerId : layerMap.keySet()){
				List<String> files = layerMap.get(layerId);
				PassByBO passByBO = new PassByBO()
						.setBundle_id("")
						.setLayer_id(layerId)
						.setType("delete_record_file")
						.setPass_by_content(new HashMapWrapper<String, Object>().put("files", files).getMap());
				logic.getPass_by().add(passByBO);
			}
			
			//执行
			executeBusiness.execute(logic, "指挥系统：删除录制及文件：" + record.getGroupName() + " " + DateUtil.format(record.getStartTime(), DateUtil.dateTimePattern));
		}
		
		commandGroupRecordDao.delete(record);
	}
	
	/**
	 * 开始播放会议录像<br/>
	 * <p>完整的一段会议录像，包含多个片段</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:20:14
	 * @param userId
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public void startPlayGroupRecord(Long userId, Long recordInfoId) throws Exception{
		
		GroupRecordInfoPO recordInfo = groupRecordInfoDao.findOne(recordInfoId);
		List<GroupRecordPO> groupRecords = recordInfo.getRecords();
		
		playFragments(userId, groupRecords);
	}
	
	/**
	 * 播放多段会议录像<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 下午3:36:18
	 * @param userId
	 * @param ids 片段fragment的id
	 * @return
	 * @throws Exception
	 */
	public void startPlayFragments(Long userId, List<Long> groupRecordIds) throws Exception{
		
		if(groupRecordIds==null || groupRecordIds.size()==0){
			return;// new ArrayList<CommandGroupUserPlayerPO>();
		}
		
		List<GroupRecordPO> groupRecords = groupRecordDao.findAll(groupRecordIds);
		
		playFragments(userId, groupRecords);
	}
	
	/** 重构播放片段 */
	private void playFragments(Long userId, List<GroupRecordPO> groupRecords) throws Exception{
		
		List<PageTaskPO> newTasks = new ArrayList<PageTaskPO>();
		for(GroupRecordPO groupRecord : groupRecords){
			
			PageTaskPO task = new PageTaskPO();
			//recordId-fragmentId@@UUID 该规则不需要记录
			task.setBusinessId(groupRecord.getRecordInfo().getId().toString() + "-" + groupRecord.getId().toString() + "@@" + UUID.randomUUID().toString().replaceAll("-", ""));
			//录像：xxx会议，xx成员，开始时间
			task.setBusinessName("录像：" + groupRecord.getInfo() + " " + DateUtil.format(groupRecord.getStartTime(), DateUtil.dateTimePattern));
			task.setBusinessInfoType(BusinessInfoType.PLAY_COMMAND_RECORD);
			
			
			//拼接播放地址
			AccessNodeBO targetLayer = null;
			List<AccessNodeBO> layers = resourceService.queryAccessNodeByNodeUids(new ArrayListWrapper<String>().add(groupRecord.getStoreLayerId()).getList());
			if(layers==null || layers.size()<=0){
				throw new AccessNodeNotExistException(groupRecord.getStoreLayerId());
			}
			targetLayer = layers.get(0);
			if(targetLayer.getIp() == null){
				throw new AccessNodeIpMissingException(groupRecord.getStoreLayerId());
			}
			if(targetLayer.getPort() == null){
				throw new AccessNodePortMissionException(groupRecord.getStoreLayerId());
			}
			String playeUrl = new StringBufferWrapper().append("http://")
					   .append(targetLayer.getIp())
					   .append(":")
					   .append(targetLayer.getPort())
					   .append("/")
					   .append(groupRecord.getPreviewUrl())
					   .toString();
			
			task.setPlayUrl(playeUrl);
			newTasks.add(task);
		}
		
		String originId = userId.toString();
		Long terminalId = terminalDao.findByType(TerminalType.QT_ZK).getId();//TODO
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(originId, terminalId, GroupMemberType.MEMBER_USER);
		pageTaskService.addAndRemoveTasks(pageInfo, newTasks, null);
	}

	/**
	 * 停止多个片段播放<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:21:05
	 * @param userId
	 * @param businessIds
	 * @return
	 * @throws Exception
	 */
	public void stopPlayFragments(Long userId, List<String> businessIds) throws Exception{
				
		List<PageTaskPO> removeTasks = pageTaskDao.findByBusinessIdIn(businessIds);
		
		String originId = userId.toString();
		Long terminalId = terminalDao.findByType(TerminalType.QT_ZK).getId();//TODO
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(originId, terminalId, GroupMemberType.MEMBER_USER);
		pageTaskService.addAndRemoveTasks(pageInfo, null, removeTasks);
		
	}
	
	/**
	 * 生成开始录制的协议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:21:55
	 * @param groupRecord
	 * @param codec
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private LogicBO startRecord(
			GroupRecordPO groupRecord,
			CodecParamBO codec,
			String userId) throws Exception{
		
		LogicBO logic = new LogicBO().setUserId(userId);
		
		RecordSetBO recordSet = new RecordSetBO()//.setGroupUuid(task.getUuid())
												 .setUuid(groupRecord.getUuid())
												 .setVideoType("2")
												 .setVideoName(groupRecord.getInfo())
												 .setUrl(groupRecord.getPreviewUrl().replace(".m3u8", ""))
												 .setPlayUrl(groupRecord.getPreviewUrl())
												 .setCodec_param(codec);
		
		if(groupRecord.getVideoBundleId() != null){
			RecordSourceBO videoSource = new RecordSourceBO().setType("channel")
														     .setBundle_id(groupRecord.getVideoBundleId())
														     .setLayer_id(groupRecord.getVideoLayerId())
														     .setChannel_id(groupRecord.getVideoChannelId());
			recordSet.setVideo_source(videoSource);
		}
		
		if(groupRecord.getAudioBundleId() != null){
			RecordSourceBO audioSource = new RecordSourceBO().setType("channel")
														     .setBundle_id(groupRecord.getAudioBundleId())
														     .setLayer_id(groupRecord.getAudioLayerId())
														     .setChannel_id(groupRecord.getAudioChannelId());
			recordSet.setAudio_source(audioSource);
		}
		logic.setRecordSet(new ArrayListWrapper<RecordSetBO>().add(recordSet).getList());
		
		return logic;
	}
	
	/**
	 * 生成暂停录制的协议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月11日 下午2:49:59
	 * @param fragment
	 * @param codec
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private LogicBO pauseRecord(
			CommandGroupRecordFragmentPO fragment,
			CodecParamBO codec,
			String userId) throws Exception{
		
		LogicBO logic = new LogicBO().setUserId(userId);
		
		RecordSetBO recordSet = new RecordSetBO()//.setGroupUuid(task.getUuid())
												 .setUuid(fragment.getUuid())
												 .setVideoType("2")
												 .setVideoName(fragment.getInfo())
												 .setUrl(fragment.getPreviewUrl().replace(".m3u8", ""))
												 .setPlayUrl(fragment.getPreviewUrl())
												 .setCodec_param(codec);
		logic.setRecordUpdate(new ArrayListWrapper<RecordSetBO>().add(recordSet).getList());
		
		return logic;
	}
	
	/**
	 * 生成停止录制的协议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:22:18
	 * @param groupRecord
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private LogicBO stopRecord(GroupRecordPO groupRecord, String userId) throws Exception{
		LogicBO logic = new LogicBO().setUserId(userId);
		RecordSetBO recordDel = new RecordSetBO().setUuid(groupRecord.getUuid());
		logic.setRecordDel(new ArrayListWrapper<RecordSetBO>().add(recordDel).getList());
		return logic;
	}
	
	/**
	 * 生成停止直播的协议<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月26日 下午3:45:03
	 * @param groupRecord 直播po
	 * @param userId 
	 * @return
	 */
	private LogicBO stopPublishStream(GroupPublishStreamPO publishStream, String userId){
		LogicBO logic = new LogicBO().setUserId(userId);
		PublishStreamSetBO publishStreamDel = new PublishStreamSetBO().setUuid(publishStream.getUuid());
		logic.setPublishStreamDel(new ArrayListWrapper<PublishStreamSetBO>().add(publishStreamDel).getList());
		return logic;
	}
	
	public List<GroupRecordPO> runRecordGroup(Long groupId) throws Exception{
		GroupPO group = groupDao.findOne(groupId);
		if(group == null){
			throw new BaseException(StatusCode.FORBIDDEN,"没有找到会议组");
		}
		return runRecordGroup(group, null, true, true);
	}
	
	/**
	 * @Title: 执行录制方案<br/> 
	 * @param group 设备组
	 * @param doProtocal 是否调用逻辑层
	 * @param doExecute 是否合并命令下发
	 * @param recordType 在发布直播(rtmp)的时候需要设置为RecordType.PUBLISH，其他时候不需要
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<GroupRecordPO> runRecordGroup(
			GroupPO group,
			RecordType recordType,
			boolean doPersistence,
			boolean doProtocal) throws Exception{
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new BaseException(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前会议组：")
															 .append(group.getName())
															 .append("(id:")
															 .append(group.getId())
															 .append("), 已停止，请先执行开始操作！")
															 .toString());
		}
		
		RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(group.getId());
		if(runningAgenda == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有正在执行的议程");
		}
		
		AgendaPO agenda = agendaDao.findOne(runningAgenda.getAgendaId());
		if(agenda == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有正在执行的议程");
		}
		
		UserVO user = userQuery.current();
		
		GroupRecordInfoPO recordInfo = new GroupRecordInfoPO();
		recordInfo.setRun(true)
				  .setRecordUserId(user.getId())
				  .setGroupUserId(group.getUserId())
				  .setGroupId(group.getId())
				  .setGroupName(group.getName())
				  .setStartTime(new Date())
				  .setRecords(new ArrayList<GroupRecordPO>());
		
		BusinessDeliverBO businessDeliver = new BusinessDeliverBO().setGroup(group).setUserId(group.getUserId().toString());
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//找到要录制的角色,查找角色作为目的是否有要看的东西，要听的东西
		//要看的东西是否已经有机顶盒对应的合屏。
		RolePO rolePO = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
		List<RolePO> recordRoleList = new ArrayList<RolePO>();
		recordRoleList.add(rolePO);
		Map<Long, RolePO> roleIdAndRoleMap = recordRoleList.stream().collect(Collectors.toMap(RolePO::getId, Function.identity()));
		
		List<AgendaForwardPO> forwards = agendaForwardDao.findByAgendaId(agenda.getId());
		Map<Long, AgendaForwardPO> forwardIdAndForwardMap = forwards.stream().collect(Collectors.toMap(AgendaForwardPO :: getId, Function.identity()));
		List<Long> forwardIds = forwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
		List<AgendaForwardDestinationPO> allForwardDst = agendaForwardDestinationDao.findByAgendaForwardIdIn(forwardIds);
		List<AgendaForwardDestinationPO> needRecordForwardDst = allForwardDst.stream().filter(forwardDst -> {
			if(DestinationType.ROLE.equals(forwardDst.getDestinationType()) && roleIdAndRoleMap.get(forwardDst.getDestinationId()) != null){
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		
		Map<RolePO, AgendaForwardPO> roleAndAgendaForwardMap = needRecordForwardDst.stream().collect(Collectors.toMap(forwardDst->roleIdAndRoleMap.get(forwardDst.getDestinationId()), forwardDst->forwardIdAndForwardMap.get(forwardDst.getAgendaForwardId())));
		TerminalPO terminal = terminalDao.findByType(TerminalType.ANDROID_TVOS);
		List<CombineTemplateGroupAgendeForwardPermissionPO> permissionList = combineTemplateGroupAgendeForwardPermissiondao.findByGroupIdAndTerminalId(group.getId(), terminal.getId());
		Map<AgendaForwardPO, CombineTemplateGroupAgendeForwardPermissionPO> forwardAndCombinePermissionMap = permissionList.stream().collect(Collectors.toMap(p->forwardIdAndForwardMap.get(p.getAgendaForwardId()), p -> p));
		Map<CombineTemplateGroupAgendeForwardPermissionPO, BusinessCombineVideoPO> combinePermissionAndCombineVideoMap = permissionList.stream().collect(Collectors.toMap(p->p, p->p.getCombineVideo()));
		
		//混音如果是全局自动配置混音或者议程转发混音先查出来保存。否则为议程转发创建map。单音频要找出源src
		Map<AgendaForwardPO, CombineAudioOrAudioSrc> forwardAndAudioSrcMap = new HashMap<AgendaForwardPO, CombineAudioOrAudioSrc>();
		generateForwardAndAudioSrcMap(group, agenda, forwards, forwardAndAudioSrcMap);
		
		for(RolePO role : recordRoleList){
			AgendaForwardPO forward = roleAndAgendaForwardMap.get(role);
			if(forward == null){
				GroupRecordPO groupRecord = new GroupRecordPO();
				groupRecord.setRun(true)
						   .setInfo(group.getName()+":"+role.getName()+"视角")
						   .setStartTime(new Date())
						   .setRecordInfo(recordInfo)
						   .setRoleId(role.getId());
				if(RecordType.PUBLISH.equals(recordType)){
					groupRecord.setType(RecordType.PUBLISH);
				}else{
					groupRecord.setType(RecordType.SCHEME);
				}
				recordInfo.getRecords().add(groupRecord);
			}else{
				//在需要录制的议程转发里查出对应的机顶盒的合屏，没有的话需要自己创建。
				CombineTemplateGroupAgendeForwardPermissionPO permission = forwardAndCombinePermissionMap.get(forward);
				if(permission != null){
					BusinessCombineVideoPO combineVideo = combinePermissionAndCombineVideoMap.get(permission);
					GroupRecordPO groupRecord = new GroupRecordPO();
					CombineAudioOrAudioSrc combineAudioOrAudioSrc = forwardAndAudioSrcMap.get(forward);
					
					groupRecord.setRun(true)
							   .setInfo(group.getName()+":"+role.getName()+"视角")
							   .setStartTime(new Date())
							   .setRecordInfo(recordInfo)
							   .setRoleId(role.getId())
							   .setVideoType(SourceType.COMBINEVIDEO)
							   .setCombineVideoUuid(combineVideo.getUuid());
					if(RecordType.PUBLISH.equals(recordType)){
						groupRecord.setType(RecordType.PUBLISH);
					}else{
						groupRecord.setType(RecordType.SCHEME);
					}
					
					if(combineAudioOrAudioSrc.getCombineAudio() != null){
						groupRecord.setAudioType(SourceType.COMBINEAUDIO)
								   .setCombineAudioUuid(combineAudioOrAudioSrc.getCombineAudio().getUuid());
					}else if(combineAudioOrAudioSrc.getBusinessCombineAudioSrcPO() != null){
						BusinessCombineAudioSrcPO combineAudioSrc = combineAudioOrAudioSrc.getBusinessCombineAudioSrcPO();
						groupRecord.setAudioType(SourceType.CHANNEL)
								   .setAudioMemberId(combineAudioSrc.getMemberId())
								   .setAudioMemberChannelId(combineAudioSrc.getMemberTerminalChannelId())
								   .setAudioBundleId(combineAudioSrc.getBundleId())
								   .setAudioLayerId(combineAudioSrc.getLayerId())
								   .setAudioChannelId(combineAudioSrc.getChannelId());
							
					}
					
					recordInfo.getRecords().add(groupRecord);
				}else {
					List<AgendaForwardSourcePO> sources = agendaForwardSourceDao.findByAgendaForwardId(forward.getId());
					
					if(sources.size() > 1){
						new BaseException(StatusCode.FORBIDDEN, "没有找到源或者应该建立合屏");
					}
					if(sources.size() == 1){
						AgendaForwardSourcePO source = sources.get(0);
						BusinessGroupMemberTerminalChannelPO videoEncodeTerminalChannel = agendaRoleMemberUtil.obtainMemberTerminalChannelFromAgendaForwardSource(
								source.getSourceId(), source.getSourceType(), group.getMembers());
						BusinessGroupMemberTerminalBundleChannelPO videoEncodeBundleChannel = multiRateUtil.queryEncodeChannel(videoEncodeTerminalChannel.getMemberTerminalBundleChannels(),ChannelParamsType.HD);
						BusinessGroupMemberTerminalBundlePO memberTerminalBundle = videoEncodeBundleChannel.getMemberTerminalBundle();
						CombineAudioOrAudioSrc combineAudioOrAudioSrc = forwardAndAudioSrcMap.get(forward);
						
						GroupRecordPO groupRecord = new GroupRecordPO();
						groupRecord.setRun(true)
								   .setInfo(group.getName()+":"+role.getName()+"视角")
								   .setStartTime(new Date())
								   .setRecordInfo(recordInfo)
								   .setRoleId(role.getId())
								   .setVideoType(SourceType.CHANNEL)
								   .setVideoMemberId(videoEncodeTerminalChannel.getMember().getId())
								   .setVideoMemberChannelId(videoEncodeTerminalChannel.getId())
								   .setVideoBundleId(memberTerminalBundle.getBundleId())
								   .setVideoLayerId(memberTerminalBundle.getLayerId())
								   .setVideoChannelId(videoEncodeBundleChannel.getChannelId());
						
						if(RecordType.PUBLISH.equals(recordType)){
							groupRecord.setType(RecordType.PUBLISH);
						}else{
							groupRecord.setType(RecordType.SCHEME);
						}
						
						if(combineAudioOrAudioSrc.getCombineAudio() != null){
							groupRecord.setAudioType(SourceType.COMBINEAUDIO)
									   .setCombineAudioUuid(combineAudioOrAudioSrc.getCombineAudio().getUuid());
						}else if(combineAudioOrAudioSrc.getBusinessCombineAudioSrcPO() != null){
							BusinessCombineAudioSrcPO combineAudioSrc = combineAudioOrAudioSrc.getBusinessCombineAudioSrcPO();
							groupRecord.setAudioType(SourceType.CHANNEL)
									   .setAudioMemberId(combineAudioSrc.getMemberId())
									   .setAudioMemberChannelId(combineAudioSrc.getMemberTerminalChannelId())
									   .setAudioBundleId(combineAudioSrc.getBundleId())
									   .setAudioLayerId(combineAudioSrc.getLayerId())
									   .setAudioChannelId(combineAudioSrc.getChannelId());
								
						}
						
						recordInfo.getRecords().add(groupRecord);
					}
				}
			}
		}
		if(doPersistence)groupRecordInfoDao.save(recordInfo);
		List<RecordSetBO> recordSetList = new ArrayList<RecordSetBO>();
		//参数模板
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		
		//添加预览地址
		for(GroupRecordPO record : recordInfo.getRecords()){
			String urlIndex = record.getId() + "-" + Integer.toString(record.getUuid().hashCode()).replace("-", "m") + "/video";
			String previewUrl = urlIndex + ".m3u8";
			record.setPreviewUrl(previewUrl);
			recordSetList.add(new RecordSetBO().set(record, codec, group));
		}
		if(doPersistence)groupRecordInfoDao.save(recordInfo);
		//修改录制状态
		if(RecordType.PUBLISH.equals(recordType)){
			group.setIsPublishment(true);
		}else{
			group.setIsRecord(true);
		}
		groupDao.save(group);
		
		//创建录制协议
		logic.setRecordSet(recordSetList);
		
		//执行还是放在BusinessDeliverBO中。还是给返回
		if(doProtocal){
			if(RecordType.PUBLISH.equals(recordType)){
				businessReturnService.dealLogic(logic, "开始发布直播：", group.getId());
			}else{
				businessReturnService.dealLogic(logic, "开始录制：", group.getId());
			}
		}
		
		return recordInfo.getRecords();
	}
	
	public LogicBO stopRecordGroup(Long groupId, boolean doProtocal, BusinessDeliverBO businessDeliver) throws Exception{
		GroupPO group = groupDao.findOne(groupId);
		if(group == null){
			throw new BaseException(StatusCode.FORBIDDEN,"没有找到会议组");
		}
		return stopRecordGroup(group, doProtocal, businessDeliver);
	}
	
	
	/**
	 * @Title: 停止设备组录制方案<br/> 
	 * @param group 设备组
	 * @param doProtocal 是否调用逻辑层
	 */
	public LogicBO stopRecordGroup(
			GroupPO group,
			boolean doProtocal,
			BusinessDeliverBO businessDeliver) throws Exception{
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new BaseException(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前会议组：")
					 															   .append(group.getName())
					 															   .append("(id:")
					 															   .append(group.getId())
					 															   .append("), 已停止，请先执行开始操作！")
					 															   .toString());
		}
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		GroupRecordInfoPO recordInfo = groupRecordInfoDao.findByGroupIdAndRun(group.getId(), true);
		
		List<GroupRecordPO> needStopRecords = new ArrayList<GroupRecordPO>();
		if(recordInfo!=null && recordInfo.getRecords() != null && recordInfo.getRecords().size()>0){
//			for(GroupRecordPO record : recordInfo.getRecords()){
//				record.setRun(false);
//				needStopRecords.add(record);
//				
//				if(SourceType.COMBINEVIDEO.equals(record.getVideoType())){
//					String combineVideoUuid = record.getCombineVideoUuid();
//					boolean hasForward = queryUtil.hasCombineVideoForward(group, combineVideoUuid);
//					if(!hasForward){
//						CombineVideoPO combineVideo = queryUtil.queryCombineVideo(group, combineVideoUuid);
//						if(combineVideo != null){
//							combineVideo.setGroup(null);
//							group.getCombineVideos().remove(combineVideo);
//							needDeleteCombineVideo.add(combineVideo);
//						}
//					}
//				}
//				
//				//录制方案中所有录制中的音频是相同的
//				if(needDeleteCombineAudio.size()==0 && SourceType.COMBINEAUDIO.equals(record.getAudioType())){
//					String combineAudioUuid = record.getCombineAudioUuid();
//					boolean hasForward = queryUtil.hasCombineAudioForward(group, combineAudioUuid);
//					if(!hasForward){
//						CombineAudioPO combineAudio = queryUtil.queryCombineAudio(group, combineAudioUuid);
//						if(combineAudio != null){
//							combineAudio.setGroup(null);
//							group.getCombineAudios().remove(combineAudio);
//							needDeleteCombineAudio.add(combineAudio);
//						}
//					}
//				}
//			}
			for(GroupRecordPO record : recordInfo.getRecords()){
				record.setRun(false)
					  .setEndTime(new Date());
			}
			
			recordInfo.setRun(false);
			groupRecordInfoDao.save(recordInfo);
			needStopRecords.addAll(recordInfo.getRecords());
		}
		
		group.setIsRecord(false);
		groupDao.save(group);
		
		//创建协议
		String transferToVod = "0";
		List<RecordSetBO> delRecordSets = new ArrayList<RecordSetBO>();
		for(GroupRecordPO record : needStopRecords){
			RecordSetBO protocalRecord = new RecordSetBO().setUuid(record.getUuid());
			protocalRecord.setTransferToVod(transferToVod);
			protocalRecord.setGroupUuid(group.getUuid());
			delRecordSets.add(protocalRecord);
		}
//		logic.setRecordDelByGroupRecord(needStopRecords, transferToVod, group);
		logic.getRecordDel().addAll(delRecordSets);
		
		if(doProtocal){
			executeBusiness.execute(logic, "结束录制方案录制：");
		}else{
			businessDeliver.getStopRecordSets().addAll(delRecordSets);
		}
		return logic;
	}
	
	public LogicBO updateRecord(GroupPO group, boolean doProtocal, BusinessDeliverBO businessDeliver) throws Exception{
		return updateRecordAndPublishStream(group, true, doProtocal, businessDeliver);
	}
	
	/**
	 * @Title: 配置视频变化时更新录制、更新直播<br/> 
	 * @param group 会议组
	 * @param forward 议程转发
	 * @param doPersistence 是否持久化数据
	 * @param doProtocal 是否调用逻辑层
	 * @throws Exception  
	 * @return LogicBO 协议数据
	 */
	public LogicBO updateRecordAndPublishStream(
			GroupPO group,
			boolean doPersistence,
			boolean doProtocal,
			BusinessDeliverBO businessDeliver) throws Exception{
		
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new BaseException(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前会议组：")
															 .append(group.getName())
															 .append("(id:")
															 .append(group.getId())
															 .append("), 已停止，请先执行开始操作！")
															 .toString());
		}
		
		RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(group.getId());
		if(runningAgenda == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有正在执行的议程");
		}
		
		AgendaPO agenda = agendaDao.findOne(runningAgenda.getAgendaId());
		if(agenda == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有正在执行的议程");
		}
		
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		//对比将需要更新的进行更新
		RolePO rolePO = roleDao.findByInternalRoleType(InternalRoleType.MEETING_AUDIENCE);
		List<RolePO> recordRoleList = new ArrayList<RolePO>();
		recordRoleList.add(rolePO);
		Map<Long, RolePO> roleIdAndRoleMap = recordRoleList.stream().collect(Collectors.toMap(RolePO::getId, Function.identity()));
		
		List<AgendaForwardPO> forwards = agendaForwardDao.findByAgendaId(agenda.getId());
		Map<Long, AgendaForwardPO> forwardIdAndForwardMap = forwards.stream().collect(Collectors.toMap(AgendaForwardPO :: getId, Function.identity()));
		List<Long> forwardIds = forwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
		List<AgendaForwardDestinationPO> allForwardDst = agendaForwardDestinationDao.findByAgendaForwardIdIn(forwardIds);
		List<AgendaForwardDestinationPO> needRecordForwardDst = allForwardDst.stream().filter(forwardDst -> {
			if(DestinationType.ROLE.equals(forwardDst.getDestinationType()) && roleIdAndRoleMap.get(forwardDst.getDestinationId()) != null){
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		Map<RolePO, AgendaForwardPO> roleAndAgendaForwardMap = needRecordForwardDst.stream().collect(Collectors.toMap(forwardDst->roleIdAndRoleMap.get(forwardDst.getDestinationId()), forwardDst->forwardIdAndForwardMap.get(forwardDst.getAgendaForwardId())));
		
		GroupRecordInfoPO recordInfo = groupRecordInfoDao.findByGroupIdAndRun(group.getId(), true);
		List<GroupRecordPO> recordList = recordInfo.getRecords();
		Map<RolePO, GroupRecordPO> roleAndRecordMap = recordList.stream().collect(Collectors.toMap(record -> roleIdAndRoleMap.get(record.getRoleId()), record -> record));
		Set<GroupRecordPO> needUpdateRecordList = new HashSet<GroupRecordPO>();
		
		Map<AgendaForwardPO, CombineAudioOrAudioSrc> forwardAndAudioSrcMap = new HashMap<AgendaForwardPO, CombineAudioOrAudioSrc>();
		generateForwardAndAudioSrcMap(group, agenda, forwards, forwardAndAudioSrcMap);
		
		TerminalPO terminal = terminalDao.findByType(TerminalType.ANDROID_TVOS);
		List<CombineTemplateGroupAgendeForwardPermissionPO> permissionList = combineTemplateGroupAgendeForwardPermissiondao.findByGroupIdAndTerminalId(group.getId(), terminal.getId());
		Map<AgendaForwardPO, CombineTemplateGroupAgendeForwardPermissionPO> forwardAndCombinePermissionMap = permissionList.stream().collect(Collectors.toMap(p->forwardIdAndForwardMap.get(p.getAgendaForwardId()), p -> p));
		Map<CombineTemplateGroupAgendeForwardPermissionPO, BusinessCombineVideoPO> combinePermissionAndCombineVideoMap = permissionList.stream().collect(Collectors.toMap(p->p, p->p.getCombineVideo()));
		
		//对直播的添加信息处理
		GroupPublishStreamPO publishStream = groupPublishStreamDao.findByGroupId(group.getId());
		List<GroupPublishStreamPO> needUpdatePublishStreamList = new ArrayList<GroupPublishStreamPO>();
		
		//最后要保存一下
		for(RolePO role : recordRoleList){
			AgendaForwardPO forward = roleAndAgendaForwardMap.get(role);
			GroupRecordPO record = roleAndRecordMap.get(role);
			
			if(forward == null){
				if(record.emptyAndClear()) needUpdateRecordList.add(record);
				if(InternalRoleType.MEETING_AUDIENCE.equals(role.getInternalRoleType())){
					if(publishStream.emptyAndClear()) needUpdatePublishStreamList.add(publishStream);
				}
			}else{
				CombineTemplateGroupAgendeForwardPermissionPO permission = forwardAndCombinePermissionMap.get(forward);
				if(permission != null){
					BusinessCombineVideoPO combineVideo = combinePermissionAndCombineVideoMap.get(permission);
					CombineAudioOrAudioSrc combineAudioOrAudioSrc = forwardAndAudioSrcMap.get(forward);
					
					if(!SourceType.COMBINEVIDEO.equals(record.getVideoType()) || combineVideo.getUuid().equals(record.getCombineVideoUuid())){
						record.clearVideoInfo();
						record.setVideoType(SourceType.COMBINEVIDEO)
						   	  .setCombineVideoUuid(combineVideo.getUuid());
						needUpdateRecordList.add(record);
						
						if(InternalRoleType.MEETING_AUDIENCE.equals(role.getInternalRoleType())){
							record.clearVideoInfo();
							record.setVideoType(SourceType.COMBINEVIDEO)
							   	  .setCombineVideoUuid(combineVideo.getUuid());
							needUpdateRecordList.add(record);
						}
					}
					
					if(combineAudioOrAudioSrc.getCombineAudio() != null){
						if(!SourceType.COMBINEAUDIO.equals(record.getAudioType()) || !combineAudioOrAudioSrc.getCombineAudio().getUuid().equals(record.getCombineAudioUuid())){
							record.clearAudioInfo();
							record.setAudioType(SourceType.COMBINEAUDIO)
							   	  .setCombineAudioUuid(combineAudioOrAudioSrc.getCombineAudio().getUuid());
							needUpdateRecordList.add(record);
							
							if(InternalRoleType.MEETING_AUDIENCE.equals(role.getInternalRoleType())){
								publishStream.clearAudioInfo();
								publishStream.setAudioType(SourceType.COMBINEAUDIO)
											 .setCombineAudioUuid(combineAudioOrAudioSrc.getCombineAudio().getUuid());
								needUpdatePublishStreamList.add(publishStream);
							}
						}
					}else if(combineAudioOrAudioSrc.getBusinessCombineAudioSrcPO() != null){
						BusinessCombineAudioSrcPO combineAudioSrc = combineAudioOrAudioSrc.getBusinessCombineAudioSrcPO();
						if(!SourceType.CHANNEL.equals(record.getAudioType()) 
								|| !combineAudioSrc.getMemberId().equals(record.getAudioMemberChannelId())
								|| !combineAudioSrc.getMemberTerminalChannelId().equals(record.getAudioMemberChannelId())
								|| !combineAudioSrc.getBundleId().equals(record.getAudioBundleId())
								|| !combineAudioSrc.getLayerId().equals(record.getAudioLayerId())
								|| !combineAudioSrc.getChannelId().equals(record.getAudioChannelId())){
							record.clearAudioInfo();
							record.setAudioType(SourceType.CHANNEL)
							   	  .setAudioMemberId(combineAudioSrc.getMemberId())
							   	  .setAudioMemberChannelId(combineAudioSrc.getMemberTerminalChannelId())
							   	  .setAudioBundleId(combineAudioSrc.getBundleId())
							   	  .setAudioLayerId(combineAudioSrc.getLayerId())
							   	  .setAudioChannelId(combineAudioSrc.getChannelId());
							needUpdateRecordList.add(record);
							
							if(InternalRoleType.MEETING_AUDIENCE.equals(role.getInternalRoleType())){
								publishStream.clearAudioInfo();
								publishStream.setAudioType(SourceType.CHANNEL)
								   	  .setAudioMemberId(combineAudioSrc.getMemberId())
								   	  .setAudioMemberChannelId(combineAudioSrc.getMemberTerminalChannelId())
								   	  .setAudioBundleId(combineAudioSrc.getBundleId())
								   	  .setAudioLayerId(combineAudioSrc.getLayerId())
								   	  .setAudioChannelId(combineAudioSrc.getChannelId());
								needUpdatePublishStreamList.add(publishStream);
							}
						}
					}
				}else {
					List<AgendaForwardSourcePO> sources = agendaForwardSourceDao.findByAgendaForwardId(forward.getId());
					
					if(sources.size() > 1){
						new BaseException(StatusCode.FORBIDDEN, "没有找到源或者应该建立合屏");
					}
					if(sources.size() == 1){
						AgendaForwardSourcePO source = sources.get(0);
						BusinessGroupMemberTerminalChannelPO videoEncodeTerminalChannel = agendaRoleMemberUtil.obtainMemberTerminalChannelFromAgendaForwardSource(
								source.getSourceId(), source.getSourceType(), group.getMembers());
						BusinessGroupMemberTerminalBundleChannelPO videoEncodeBundleChannel = multiRateUtil.queryEncodeChannel(videoEncodeTerminalChannel.getMemberTerminalBundleChannels(),ChannelParamsType.HD);
						BusinessGroupMemberTerminalBundlePO memberTerminalBundle = videoEncodeBundleChannel.getMemberTerminalBundle();
						CombineAudioOrAudioSrc combineAudioOrAudioSrc = forwardAndAudioSrcMap.get(forward);
						
						if(!SourceType.CHANNEL.equals(record.getVideoType())
								|| !videoEncodeTerminalChannel.getMember().getId().equals(record.getVideoMemberId())
								|| !videoEncodeTerminalChannel.getId().equals(record.getVideoMemberChannelId())
								|| !memberTerminalBundle.getBundleId().equals(record.getVideoBundleId())
								|| !memberTerminalBundle.getLayerId().equals(record.getVideoLayerId())
								|| !videoEncodeBundleChannel.getChannelId().equals(record.getVideoChannelId())){
							record.clearVideoInfo();
							record.setVideoType(SourceType.CHANNEL)
							   	  .setVideoMemberId(videoEncodeTerminalChannel.getMember().getId())
							   	  .setVideoMemberChannelId(videoEncodeTerminalChannel.getId())
							   	  .setVideoBundleId(memberTerminalBundle.getBundleId())
							   	  .setVideoLayerId(memberTerminalBundle.getLayerId())
							   	  .setVideoChannelId(videoEncodeBundleChannel.getChannelId());
							needUpdateRecordList.add(record);
							
							if(InternalRoleType.MEETING_AUDIENCE.equals(role.getInternalRoleType())){
								publishStream.clearVideoInfo();
								publishStream.setVideoType(SourceType.CHANNEL)
								   	  .setVideoMemberId(videoEncodeTerminalChannel.getMember().getId())
								   	  .setVideoMemberChannelId(videoEncodeTerminalChannel.getId())
								   	  .setVideoBundleId(memberTerminalBundle.getBundleId())
								   	  .setVideoLayerId(memberTerminalBundle.getLayerId())
								   	  .setVideoChannelId(videoEncodeBundleChannel.getChannelId());
								needUpdatePublishStreamList.add(publishStream);
							}
						}
								
						
						if(combineAudioOrAudioSrc.getCombineAudio() != null){
							if(!SourceType.COMBINEAUDIO.equals(record.getAudioType()) || !combineAudioOrAudioSrc.getCombineAudio().getUuid().equals(record.getCombineAudioUuid())){
								record.clearAudioInfo();
								record.setAudioType(SourceType.COMBINEAUDIO)
								   	  .setCombineAudioUuid(combineAudioOrAudioSrc.getCombineAudio().getUuid());
								needUpdateRecordList.add(record);
								
								if(InternalRoleType.MEETING_AUDIENCE.equals(role.getInternalRoleType())){
									publishStream.clearAudioInfo();
									publishStream.setAudioType(SourceType.COMBINEAUDIO)
									   	  .setCombineAudioUuid(combineAudioOrAudioSrc.getCombineAudio().getUuid());
									needUpdatePublishStreamList.add(publishStream);
								}
							}
						}else if(combineAudioOrAudioSrc.getBusinessCombineAudioSrcPO() != null){
							BusinessCombineAudioSrcPO combineAudioSrc = combineAudioOrAudioSrc.getBusinessCombineAudioSrcPO();
							if(!SourceType.CHANNEL.equals(record.getAudioType()) 
									|| !combineAudioSrc.getMemberId().equals(record.getAudioMemberChannelId())
									|| !combineAudioSrc.getMemberTerminalChannelId().equals(record.getAudioMemberChannelId())
									|| !combineAudioSrc.getBundleId().equals(record.getAudioBundleId())
									|| !combineAudioSrc.getLayerId().equals(record.getAudioLayerId())
									|| !combineAudioSrc.getChannelId().equals(record.getAudioChannelId())){
								record.clearAudioInfo();
								record.setAudioType(SourceType.CHANNEL)
								   	  .setAudioMemberId(combineAudioSrc.getMemberId())
								   	  .setAudioMemberChannelId(combineAudioSrc.getMemberTerminalChannelId())
								   	  .setAudioBundleId(combineAudioSrc.getBundleId())
								   	  .setAudioLayerId(combineAudioSrc.getLayerId())
								   	  .setAudioChannelId(combineAudioSrc.getChannelId());
								needUpdateRecordList.add(record);
								
								if(InternalRoleType.MEETING_AUDIENCE.equals(role.getInternalRoleType())){
									publishStream.clearAudioInfo();
									publishStream.setAudioType(SourceType.CHANNEL)
									   	  .setAudioMemberId(combineAudioSrc.getMemberId())
									   	  .setAudioMemberChannelId(combineAudioSrc.getMemberTerminalChannelId())
									   	  .setAudioBundleId(combineAudioSrc.getBundleId())
									   	  .setAudioLayerId(combineAudioSrc.getLayerId())
									   	  .setAudioChannelId(combineAudioSrc.getChannelId());
									needUpdatePublishStreamList.add(publishStream);
								}
							}
						}
					}
				}
			} 
		}
		
		groupRecordInfoDao.save(recordInfo);
		groupPublishStreamDao.save(publishStream);
		
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		List<RecordSetBO> recordUpdateList = new ArrayList<RecordSetBO>();
		for(GroupRecordPO updateRecord : needUpdateRecordList){
			recordUpdateList.add(new RecordSetBO().set(updateRecord, codec, group));
		}
			
		logic.setRecordUpdate(recordUpdateList);
		
		List<PublishStreamSetBO> publishStreamUpdateList = new ArrayList<PublishStreamSetBO>();
		for(GroupPublishStreamPO updatePublishStream : needUpdatePublishStreamList){
			publishStreamUpdateList.add(new PublishStreamSetBO().set(updatePublishStream, codec));
		}
		
		logic.setPublishStreamUpdate(publishStreamUpdateList);
		
		if(doProtocal){
			executeBusiness.execute(logic, "更新录制：");
		}else{
			businessDeliver.getUpdateRecordSet().addAll(recordUpdateList);
		}
		return logic;
	}
	
	private void generateForwardAndAudioSrcMap(GroupPO group, AgendaPO agenda, List<AgendaForwardPO> forwards, Map<AgendaForwardPO, CombineAudioOrAudioSrc> forwardAndAudioSrcMap){
		
		List<Long> forwardIds = forwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
		Map<Long, AgendaForwardPO> forwardIdAndForwardMap = forwards.stream().collect(Collectors.toMap(AgendaForwardPO :: getId, Function.identity()));
		
		if(AudioPriority.GLOBAL_FIRST.equals(agenda.getAudioPriority())){
			CombineAudioPermissionPO audioPermission = combineAudioPermissionDao.findByGroupIdAndPermissionIdAndPermissionType(group.getId(), group.getId(), CustomAudioPermissionType.GROUP);
			BusinessCombineAudioPO combineAudio = audioPermission.getAllAudio();
			CombineAudioOrAudioSrc combineAudioOrAudioSrc = new CombineAudioOrAudioSrc();
			
			if(combineAudio.isMix()){
				combineAudioOrAudioSrc.setCombineAudio(combineAudio);
			}else if(!combineAudio.isMix() && combineAudio.isHasSource()){
				BusinessCombineAudioSrcPO audioSrc = combineAudio.getSrcs().get(0);
				combineAudioOrAudioSrc.setBusinessCombineAudioSrcPO(audioSrc);
			}
			for(AgendaForwardPO forward : forwards){
				forwardAndAudioSrcMap.put(forward, combineAudioOrAudioSrc);
			}
			
		}else if(agenda.getGlobalCustomAudio()){
			CombineAudioPermissionPO audioPermission = combineAudioPermissionDao.findByGroupIdAndPermissionIdAndPermissionType(group.getId(), agenda.getId(), CustomAudioPermissionType.AGENDA);
			BusinessCombineAudioPO combineAudio = audioPermission.getAllAudio();
			CombineAudioOrAudioSrc combineAudioOrAudioSrc = new CombineAudioOrAudioSrc();
			
			if(combineAudio.isMix()){
				combineAudioOrAudioSrc.setCombineAudio(combineAudio);
			}else if(!combineAudio.isMix() && combineAudio.isHasSource()){
				BusinessCombineAudioSrcPO audioSrc = combineAudio.getSrcs().get(0);
				combineAudioOrAudioSrc.setBusinessCombineAudioSrcPO(audioSrc);
			}
			
			for(AgendaForwardPO forward : forwards){
				forwardAndAudioSrcMap.put(forward, combineAudioOrAudioSrc);
			}
		}else if(!agenda.getGlobalCustomAudio()){
			List<CombineAudioPermissionPO> audioPermissions = combineAudioPermissionDao.findByGroupIdAndPermissionIdInAndPermissionType(group.getId(), forwardIds, CustomAudioPermissionType.AGENDA_FORWARD);
			Map<CombineAudioPermissionPO, AgendaForwardPO> combineAudioPermissionAndForwardMap = audioPermissions.stream().collect(Collectors.toMap(p -> p, p -> forwardIdAndForwardMap.get(p.getPermissionId())));
			
			for(CombineAudioPermissionPO permission : audioPermissions){
				BusinessCombineAudioPO combineAudio = permission.getAllAudio();
				CombineAudioOrAudioSrc combineAudioOrAudioSrc = new CombineAudioOrAudioSrc();
				
				if(combineAudio.isMix()){
					combineAudioOrAudioSrc.setCombineAudio(combineAudio);
				}else if(!combineAudio.isMix() && combineAudio.isHasSource()){
					BusinessCombineAudioSrcPO audioSrc = combineAudio.getSrcs().get(0);
					combineAudioOrAudioSrc.setBusinessCombineAudioSrcPO(audioSrc);
				}
				
				forwardAndAudioSrcMap.put(combineAudioPermissionAndForwardMap.get(permission), combineAudioOrAudioSrc);
			}
		}
	}
	
}
class CombineAudioOrAudioSrc{
	
	private BusinessCombineAudioPO combineAudio;
	
	private BusinessCombineAudioSrcPO businessCombineAudioSrcPO;

	public BusinessCombineAudioPO getCombineAudio() {
		return combineAudio;
	}

	public CombineAudioOrAudioSrc setCombineAudio(BusinessCombineAudioPO combineAudio) {
		this.combineAudio = combineAudio;
		return this;
	}

	public BusinessCombineAudioSrcPO getBusinessCombineAudioSrcPO() {
		return businessCombineAudioSrcPO;
	}

	public CombineAudioOrAudioSrc setBusinessCombineAudioSrcPO(BusinessCombineAudioSrcPO businessCombineAudioSrcPO) {
		this.businessCombineAudioSrcPO = businessCombineAudioSrcPO;
		return this;
	}
	
}
