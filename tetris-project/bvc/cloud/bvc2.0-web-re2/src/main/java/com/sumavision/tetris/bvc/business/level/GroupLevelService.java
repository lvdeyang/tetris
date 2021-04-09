package com.sumavision.tetris.bvc.business.level;

import java.util.*;
import java.util.stream.Collectors;

import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.tetris.bvc.business.dao.*;
import com.sumavision.tetris.bvc.business.group.GroupMemberService;
import com.sumavision.tetris.bvc.business.group.combine.video.CombineVideoService;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.model.agenda.*;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.CommandGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.command.time.CommandFightTimeServiceImpl;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.dao.CombineAudioDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupProceedRecordDAO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.bo.MemberTerminalBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.process.GroupProceedRecordServiceImpl;
//import com.sumavision.tetris.bvc.business.group.demand.GroupDemandService;
import com.sumavision.tetris.bvc.business.group.record.GroupRecordService;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallPO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionPO;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RoleExecuteService;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理层级关系高低<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月22日 下午2:29:39
 */

@Slf4j
@Service
public class GroupLevelService {
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";

	@Autowired
	private com.sumavision.bvc.device.group.dao.CombineVideoDAO deviceGroupCombineVideoDao;
	
	@Autowired
	private DeviceGroupProceedRecordDAO deviceGroupProceedRecordDao;

	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;

	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;

	@Autowired
	private CustomAudioDAO customAudioDao;

	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;

	@Autowired
	private CombineVideoDAO combineVideoDao;

	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private PageInfoDAO pageInfoDAO;
	
	@Autowired
	private ConferenceHallDAO conferenceHallDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private RoleDAO roleDao;
	
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
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private AvtplDAO sysAvtplDao;
	
	@Autowired
	private CommandGroupMemberDAO commandGroupMemberDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	//@Autowired
	//private AutoCombineService autoCombineService;
	
	@Autowired
	private MulticastService multicastService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private GroupRecordService groupRecordService;
	
	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandRecordServiceImpl commandRecordServiceImpl;
	
	@Autowired
	private CommandFightTimeServiceImpl commandFightTimeServiceImpl;
	
	@Autowired
	private CommandMeetingSpeakServiceImpl commandMeetingSpeakServiceImpl;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandVodService commandVodService;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private GroupMemberService groupMemberService;
	
	@Autowired
	private ResourceServiceClient resourceServiceClient;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;

	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;	
	
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private CommandCascadeUtil commandCascadeUtil;
	
	@Autowired
	private CommandCascadeService commandCascadeService;
	
	@Autowired
	private ConferenceCascadeService conferenceCascadeService;
	
	@Autowired
	private RoleExecuteService roleExecuteService;
	
	@Autowired
	private BusinessReturnService businessReturnService;

	@Autowired
	private AgendaService agendaService;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;
	
	@Autowired
	private GroupProceedRecordServiceImpl groupProceedRecordServiceImpl;

	@Autowired
	private CombineVideoService combineVideoService;

	@Autowired
	private BusinessCombineVideoDAO combineVideoDAO;
	
	/** 比较2个成员的层级高低，主席的高，没有主席则平级 */
	public int compareLevelByMemberIsChairman(BusinessGroupMemberPO member1, BusinessGroupMemberPO member2) {		
		if(member1.getIsAdministrator()) return 1;
		else if(member2.getIsAdministrator()) return -1;
		else return 0;
	}
	
	/** 比较2个成员的层级高低，level小的层级高 */
	public int compareLevelByMemberLevel(BusinessGroupMemberPO member1, BusinessGroupMemberPO member2) {		
		if(member1.getLevel() == null) return 0;
		else if(member2.getLevel() == null) return 0;
		return member2.getLevel() - member1.getLevel();
	}
	
}
