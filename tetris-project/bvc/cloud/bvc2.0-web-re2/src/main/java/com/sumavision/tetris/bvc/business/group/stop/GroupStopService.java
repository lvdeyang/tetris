package com.sumavision.tetris.bvc.business.group.stop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
//import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
import com.netflix.loadbalancer.ServerListUpdater.UpdateAction;
import com.querydsl.core.support.QueryMixin.Role;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.enumeration.OriginType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.ExecuteStatus;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.bo.MemberChangedTaskBO;
//import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineAudioDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineAudioSrcDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CombineAudioPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.group.TransmissionMode;
import com.sumavision.tetris.bvc.business.group.combine.audio.CombineAudioService;
import com.sumavision.tetris.bvc.business.group.combine.video.CombineVideoService;
import com.sumavision.tetris.bvc.business.group.record.GroupRecordService;
import com.sumavision.tetris.bvc.business.level.GroupLevelService;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoDAO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.CombineAudioPermissionPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundlePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardDAO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardSourceType;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
//import com.sumavision.tetris.bvc.model.agenda.combine.AutoCombineService;
//import com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioDAO;
//import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoDAO;
//import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPO;
//import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoUtil;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelPO;
import com.sumavision.tetris.bvc.model.role.RoleChannelTerminalChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelTerminalChannelPermissionPO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.role.RoleUserMappingType;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
//import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionDAO;
//import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionPO;
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
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 互斥暂停业务处理（暂停、专向、静默等）<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月19日 上午9:55:26
 */
@Slf4j
@Service
public class GroupStopService {
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";
	
	public static final int VIDEO = 1;
	
	public static final int AUDIO = 2;

	private static final String commandSecretServiceImpl = null;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private GroupCommandInfoDAO groupCommandInfoDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private CommonForwardDAO commonForwardDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private BusinessCombineVideoDAO combineVideoDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private MulticastService multicastService;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private MultiRateUtil multiRateUtil;

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private LayoutForwardDAO layoutForwardDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;
	
	@Autowired
	private BusinessCombineVideoDAO businessCombineVideoDao;
	
	@Autowired
	private BusinessCombineAudioDAO businessCombineAudioDao;
	
	@Autowired
	private RoleChannelTerminalChannelPermissionDAO roleChannelTerminalChannelPermissionDao;
	
	@Autowired
	private CombineAudioPermissionDAO combineAudioPermissionDao;
	
	@Autowired
	private CombineTemplateGroupAgendeForwardPermissionDAO combineTemplateGroupAgendeForwardPermissionDao;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private CombineVideoService combineVideoService;
	
	@Autowired
	private CombineAudioService combineAudioService;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;
	
	@Autowired
	private AgendaRoleMemberUtil agendaRoleMemberUtil;
	
	@Autowired
	private BusinessCombineAudioSrcDAO businessCombineAudioSrcDao;
	
	@Autowired
	private GroupRecordService groupRecordService;
	
	@Autowired
	private GroupLevelService groupLevelService;
	
	/** 级联使用：联网接入层id */
	private String localLayerId = null;
	
	public boolean whetherVideoCanBeDone(PageTaskPO task){
		if(whetherVideoStopForPause(task)){
			return false;
		}else if(whetherVideoStopForSilence(task)){
			return false;
		}else if(whetherVideoStopForSecret(task)){
			return false;
		}
		return true;
	}
	
	public boolean whetherAudioCanBeDone(PageTaskPO task){
		if(whetherAudioStopForPause(task)){
			return false;
		}else if(whetherAudioStopForSilence(task)){
			return false;
		}else if(whetherAudioStopForSecret(task)){
			return false;
		}
		return true;
	}
	
	/** 暂停状态下的BusinessType.CALL业务，也就是呼叫业务还没有接听，此时不能进行视频转发 */
	private boolean whetherVideoStopForPause(PageTaskPO task){
		GroupPO group = groupDao.findOne(task.obtainGroupId());
		if(group.getBusinessType().equals(BusinessType.CALL)
				&& group.getStatus().equals(GroupStatus.PAUSE)){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean whetherAudioStopForPause(PageTaskPO task){
		GroupPO group = groupDao.findOne(task.obtainGroupId());
		if(group.getStatus().equals(GroupStatus.PAUSE)){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean whetherVideoStopForSilence(PageTaskPO task){
		
		Long srcMemberId =  task.getSrcVideoMemberId();
		if(srcMemberId == null) return false;
		
		BusinessGroupMemberPO srcMember = businessGroupMemberDao.findOne(srcMemberId);
		if(!srcMember.getSilenceToHigher() && !srcMember.getSilenceToLower()){
			return false;
		}
		
		//自己转给自己的，false
		if(srcMember.getId().equals(task.getDstMemberId())){
			return false;
		}
		
		//源和目的的上下级关系
		BusinessGroupMemberPO dstMember = businessGroupMemberDao.findOne(task.getDstMemberId());
		int levelCompare = groupLevelService.compareLevelByMemberLevel(srcMember, dstMember);
		
		if(srcMember.getSilenceToHigher() && levelCompare<0){
			return true;
		}
		if(srcMember.getSilenceToLower() && levelCompare>0){
			return true;
		}		
		
		return false;
	}
	
	private boolean whetherAudioStopForSilence(PageTaskPO task){
		
		Long srcMemberId =  task.getSrcVideoMemberId();
		if(srcMemberId == null) return false;
		
		BusinessGroupMemberPO srcMember = businessGroupMemberDao.findOne(srcMemberId);
		if(!srcMember.getSilenceToHigher() && !srcMember.getSilenceToLower()){
			return false;
		}
		
		//自己转给自己的，false
		if(srcMember.getId().equals(task.getDstMemberId())){
			return false;
		}
		
		//源和目的的上下级关系
		BusinessGroupMemberPO dstMember = businessGroupMemberDao.findOne(task.getDstMemberId());
		int levelCompare = groupLevelService.compareLevelByMemberIsChairman(srcMember, dstMember);
		
		if(srcMember.getSilenceToHigher() && levelCompare<0){
			return true;
		}
		if(srcMember.getSilenceToLower() && levelCompare>0){
			return true;
		}		
		
		return false;
	}
	
	private boolean whetherVideoStopForSecret(PageTaskPO task){
		return false;
	}
	
	private boolean whetherAudioStopForSecret(PageTaskPO task){
		GroupCommandInfoPO groupCommandInfo = groupCommandInfoDao.findByGroupId(task.obtainGroupId());
		if(groupCommandInfo != null && groupCommandInfo.isHasSecret()){
			Long dstMemberId = task.getDstMemberId();
			Long secretHighMemberId =  groupCommandInfo.getSecretHighMemberId();
			Long secretLowMemberId = groupCommandInfo.getSecretLowMemberId();
			
			//如果是专向成员，则不暂停
			if(dstMemberId.equals(secretHighMemberId)
					|| dstMemberId.equals(secretLowMemberId)){
				return false;
			}			
			
			if(groupCommandInfo.getSecretHighMemberId().equals(task.getSrcAudioMemberId())){
				return true;
			}
			if(groupCommandInfo.getSecretLowMemberId().equals(task.getSrcAudioMemberId())){
				return true;
			}
		}
		return false;
	}
	
}
