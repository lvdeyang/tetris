package com.sumavision.tetris.bvc.business.group.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordFragmentDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.ExecuteStatus;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.bo.RecordSourceBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeIpMissingException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodeNotExistException;
import com.sumavision.bvc.device.monitor.playback.exception.AccessNodePortMissionException;
import com.sumavision.bvc.device.monitor.playback.exception.ResourceNotExistException;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO.ResultDstBO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDemandDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.vod.DstType;
import com.sumavision.tetris.bvc.business.vod.VodPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaService;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

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
	private AgendaDAO agendaDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
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
	
	@Autowired
	private AgendaService agendaService;
	
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
	private ExecuteBusinessProxy executeBusiness;	
	
	/**
	 * 会议开始录制<br/>
	 * <p>所有录制都不呼叫编码器，停止的时候也不挂断编码器</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月22日 上午8:17:59
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
			if(!group.getUserId().equals(userId)){
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
			
			CommandGroupRecordPO record = new CommandGroupRecordPO();
			Date startTime = new Date();
			record.setGroupInfo(group);
			record.setRecordUserId(userId);
			record.setStartTime(startTime);
//			CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
//			record.setPlayerSplitLayout(userInfo.obtainUsingScheme().getPlayerSplitLayout());
			record.setFragments(new ArrayList<CommandGroupRecordFragmentPO>());			
			
			List<GroupMemberPO> members = groupMemberDao.findByGroupId(groupId);
			List<SourceBO> sourceBOs = agendaService.obtainSource(members, groupId.toString(), BusinessInfoType.BASIC_COMMAND);
			
			GroupMemberPO thisMember = tetrisBvcQueryUtil.queryMemberByUserId(members, userId);
			for(GroupMemberPO member : members){
				
				if(member.getIsAdministrator()) continue;
				if(!member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)) continue;
				
				List<SourceBO> sources = tetrisBvcQueryUtil.querySourceBOsByMemberIds(sourceBOs, member.getId());
				for(SourceBO source : sources){
				
					CommandGroupRecordFragmentPO fragment = new CommandGroupRecordFragmentPO().setByMemberSource(member, source, startTime);
					fragment.setInfo(member.getName());
					//拼预览地址
					String urlIndex = groupId + "-" + userId + "-" + Integer.toString(record.getUuid().hashCode()).replace("-", "m") + Integer.toString(fragment.getUuid().hashCode()).replace("-", "m") + "/video";
					String previewUrl = urlIndex + ".m3u8";
					fragment.setPreviewUrl(previewUrl);
					
					fragment.setRecord(record);
					record.getFragments().add(fragment);
					
					logic.merge(startRecord(fragment, codec, adminUserId));

				}
			}
			
			commandGroupRecordDao.save(record);
			
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
		if(mode==0 && !group.getUserId().equals(userId)){
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
			record.setPlayerSplitLayout(userInfo.obtainUsingScheme().getPlayerSplitLayout());
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
				
				ExecuteStatus executeStatus = forward.getExecuteStatus();
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
		return logic;
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
		List<CommandGroupRecordPO> records = commandGroupRecordDao.findByGroupIdAndRun(groupId, true);
		for(ResultDstBO result : returnBO.getOutConnMediaMuxSet()){
			for(CommandGroupRecordPO record : records){
				if(record.isRun()){
					for(CommandGroupRecordFragmentPO fragment : record.getFragments()){
						if(fragment.getUuid().equals(result.getUuid())){
							fragment.setStoreLayerId(result.getLayerId());
							break;
						}
					}
				}
			}
		}
		commandGroupRecordDao.save(records);
	}

	/**
	 * 会议停止录制<br/>
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
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		if(userId!=null && !group.getUserId().equals(userId)){
			throw new BaseException(StatusCode.FORBIDDEN, "只有主席才能进行操作");
		}
		Date endTime = new Date();
		LogicBO logic = new LogicBO();
		
		String tempStr = "全部";
		if(userId != null){
			tempStr = "操作用户id：" + userId; 
		}
		
		List<CommandGroupRecordPO> records = new ArrayList<CommandGroupRecordPO>();
		if(userId == null){
			//停止全部录制
			records = commandGroupRecordDao.findByGroupIdAndRun(groupId, true);
		}else{
			//停止该用户的录制
			records = commandGroupRecordDao.findByGroupIdAndRecordUserIdAndRun(groupId, userId, true);
		}
		
		if(records.size() > 0){
			log.info(group.getName() + "停止录制" + tempStr);
		}else{
			return logic;
		}
		
//		UserBO admin = resourceService.queryUserInfoByUsername(CommandCommonConstant.USER_NAME);
		UserBO admin = new UserBO(); admin.setId(-1L);
		String adminUserId = admin.getId().toString();
		for(CommandGroupRecordPO record : records){
			record.setRun(false);
			record.setEndTime(endTime);
			for(CommandGroupRecordFragmentPO fragment : record.getFragments()){
				//停止录制时，此时run字段为false表示录制暂停，但没有停止，所以所有的fragment都下协议
				fragment.setRun(false);
				fragment.setEndTime(endTime);
				//生成协议
				logic.merge(stopRecord(fragment, adminUserId));
			}
		}
		
		commandGroupRecordDao.save(records);
		
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
	public void startPlayGroupRecord(Long userId, Long recordId) throws Exception{
		
		CommandGroupRecordPO record = commandGroupRecordDao.findOne(recordId);
		List<CommandGroupRecordFragmentPO> fragments = record.getFragments();
		
		playFragments(userId, fragments);
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
	public void startPlayFragments(Long userId, List<Long> fragmentIds) throws Exception{
		
		if(fragmentIds==null || fragmentIds.size()==0){
			return;// new ArrayList<CommandGroupUserPlayerPO>();
		}
		
		List<CommandGroupRecordFragmentPO> fragments = commandGroupRecordFragmentDao.findByIdIn(fragmentIds);
		
		playFragments(userId, fragments);
	}
	
	/** 重构播放片段 */
	private void playFragments(Long userId, List<CommandGroupRecordFragmentPO> fragments) throws Exception{
		
		List<PageTaskPO> newTasks = new ArrayList<PageTaskPO>();
		for(CommandGroupRecordFragmentPO fragment : fragments){
			
			PageTaskPO task = new PageTaskPO();
			//recordId-fragmentId@@UUID 该规则不需要记录
			task.setBusinessId(fragment.getRecord().getId().toString() + "-" + fragment.getId().toString() + "@@" + UUID.randomUUID().toString().replaceAll("-", ""));
			//录像：xxx会议，xx成员，开始时间
			task.setBusinessName("录像：" + fragment.getInfo() + " " + DateUtil.format(fragment.getStartTime(), DateUtil.dateTimePattern));
			task.setBusinessInfoType(BusinessInfoType.PLAY_COMMAND_RECORD);
			
			
			//拼接播放地址
			AccessNodeBO targetLayer = null;
			List<AccessNodeBO> layers = resourceService.queryAccessNodeByNodeUids(new ArrayListWrapper<String>().add(fragment.getStoreLayerId()).getList());
			if(layers==null || layers.size()<=0){
				throw new AccessNodeNotExistException(fragment.getStoreLayerId());
			}
			targetLayer = layers.get(0);
			if(targetLayer.getIp() == null){
				throw new AccessNodeIpMissingException(fragment.getStoreLayerId());
			}
			if(targetLayer.getPort() == null){
				throw new AccessNodePortMissionException(fragment.getStoreLayerId());
			}
			String playeUrl = new StringBufferWrapper().append("http://")
					   .append(targetLayer.getIp())
					   .append(":")
					   .append(targetLayer.getPort())
					   .append("/")
					   .append(fragment.getPreviewUrl())
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
	 * @param fragment
	 * @param codec
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private LogicBO startRecord(
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
		
		if(fragment.getVideoBundleId() != null){
			RecordSourceBO videoSource = new RecordSourceBO().setType("channel")
														     .setBundle_id(fragment.getVideoBundleId())
														     .setLayer_id(fragment.getVideoLayerId())
														     .setChannel_id(fragment.getVideoChannelId());
			recordSet.setVideo_source(videoSource);
		}
		
		if(fragment.getAudioBundleId() != null){
			RecordSourceBO audioSource = new RecordSourceBO().setType("channel")
														     .setBundle_id(fragment.getAudioBundleId())
														     .setLayer_id(fragment.getAudioLayerId())
														     .setChannel_id(fragment.getAudioChannelId());
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
	 * @param fragment
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private LogicBO stopRecord(CommandGroupRecordFragmentPO fragment, String userId) throws Exception{
		LogicBO logic = new LogicBO().setUserId(userId);
		RecordSetBO recordDel = new RecordSetBO().setUuid(fragment.getUuid());
		logic.setRecordDel(new ArrayListWrapper<RecordSetBO>().add(recordDel).getList());
		return logic;
	}
}
