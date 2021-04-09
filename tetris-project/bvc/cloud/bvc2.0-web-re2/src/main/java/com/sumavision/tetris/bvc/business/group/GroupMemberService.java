package com.sumavision.tetris.bvc.business.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.querydsl.core.support.QueryMixin.Role;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.MediaPushSetBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.XtBusinessPassByContentBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAvtplDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.MemberTerminalBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDemandDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.group.record.GroupRecordService;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalAudioOutputPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundlePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalScreenPO;
import com.sumavision.tetris.bvc.business.po.member.dao.BusinessGroupMemberTerminalBundleDAO;
import com.sumavision.tetris.bvc.business.po.member.dao.BusinessGroupMemberTerminalChannelDAO;
import com.sumavision.tetris.bvc.business.po.member.dao.BusinessGroupMemberTerminalScreenDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallPO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallService;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionPO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionPO;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.CustomAudioDAO;
import com.sumavision.tetris.bvc.model.agenda.CustomAudioPO;
import com.sumavision.tetris.bvc.model.agenda.CustomAudioPermissionType;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelType;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalEncodeAudioVideoChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalEncodeAudioVideoChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.channel.ChannelParamsType;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelType;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenDAO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;
import com.sumavision.tetris.websocket.message.WebsocketMessageVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GroupMemberService {
	
	/** synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";
	
	private String localLayerId = null;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private ConferenceHallService conferenceHallService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private AvtplDAO sysAvtplDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;

	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private RoleDAO roleDao;

	@Autowired
	private TerminalBundleDAO terminalBundleDao;

	@Autowired
	private ResourceChannelDAO resourceChannelDao;

	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDao;

	@Autowired
	private TerminalDAO terminalDao;

	@Autowired
	private TerminalChannelDAO terminalChannelDao;

	@Autowired
	private TerminalChannelBundleChannelPermissionDAO terminalChannelBundleChannelPermissionDao;
	
	@Autowired
	private TerminalPhysicalScreenDAO terminalPhysicalScreenDao;
	
	@Autowired
	private TerminalPhysicalScreenChannelPermissionDAO terminalPhysicalScreenChannelPermissionDao;
	
	@Autowired
	private TerminalAudioOutputDAO terminalAudioOutputDao;
	
	@Autowired
	private TerminalAudioOutputChannelPermissionDAO terminalAudioOutputChannelPermissionDao;
	
	@Autowired
	private TerminalEncodeAudioVideoChannelPermissionDAO terminalEncodeAudioVideoChannelPermissionDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private PageInfoDAO pageInfoDAO;
	
	@Autowired
	private ConferenceHallDAO conferenceHallDao;

	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private BusinessGroupMemberTerminalBundleDAO businessGroupMemberTerminalBundleDao;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private MulticastService multicastService;
	
	@Autowired
	private GroupDemandDAO groupDemandDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private GroupRecordService groupRecordService;
	
	@Autowired
	private CommandCascadeUtil commandCascadeUtil;
	
	@Autowired
	private CommandCascadeService commandCascadeService;
	
	@Autowired
	private ConferenceCascadeService conferenceCascadeService;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;
	
	@Autowired
	private CustomAudioDAO customAudioDao;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private BusinessGroupMemberTerminalScreenDAO businessGroupMemberTerminalScreenDao;

	@Autowired
	private DeviceGroupAvtplDAO deviceGroupAvtplDAO;
	
	@Autowired
	private BusinessGroupMemberTerminalChannelDAO businessGroupMemberTerminalChannelDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	/**
	 * 根据用户id和bundleType查找会场id<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 下午4:06:58
	 * @param userId
	 * @param bundleType 对应BundlePO的deviceModel，取值如tvos
	 * @return 会场id
	 */
	private Long findHallId(Long userId, String bundleType) throws BaseException{
		BundlePO bundle = bundleDao.findByUserIdAndDeviceModel(userId, bundleType);
		String bundleId = bundle.getBundleId();
		List<TerminalBundleConferenceHallPermissionPO> ps = terminalBundleConferenceHallPermissionDao.findByBundleTypeAndBundleId(bundleType, bundleId);
		if(ps.size() == 0){
			throw new BaseException(StatusCode.FORBIDDEN, "请配置您的设备");
		}
		if(ps.size() > 1){
			throw new BaseException(StatusCode.FORBIDDEN, "您的设备配置有重复，请修改");
		}
		TerminalBundleConferenceHallPermissionPO p = ps.get(0);
		Long hallId = p.getConferenceHallId();
		return hallId;
	}
	
	
	/**
	 * 根据终端重新构建终端下的信息<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月10日 下午2:40:41
	 */
	public void fullfillGroupMember(List<BusinessGroupMemberPO> groupMembers) throws Exception{
		
		//方法还可以优化
		for(BusinessGroupMemberPO member:groupMembers){
			
			List<BusinessGroupMemberTerminalBundlePO> terminalBundles = new ArrayList<BusinessGroupMemberTerminalBundlePO>();
			List<BusinessGroupMemberTerminalChannelPO> terminalChannels = new ArrayList<BusinessGroupMemberTerminalChannelPO>();
			List<BusinessGroupMemberTerminalScreenPO> screens = new ArrayList<BusinessGroupMemberTerminalScreenPO>();
			List<BusinessGroupMemberTerminalAudioOutputPO> audioOutputs = new ArrayList<BusinessGroupMemberTerminalAudioOutputPO>();
			
			//初始化状态
			member.setSilenceToHigher(Boolean.FALSE);
			member.setSilenceToLower(Boolean.FALSE);
			member.setMyAudio(Boolean.TRUE);
			member.setMyVideo(Boolean.TRUE);

			TerminalPO terminal210 = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.JV210);
			
			//可以提供公用方法
			//用户成员
			if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){

				//外域的成员
				if(OriginType.OUTER.equals(member.getOriginType())){
					
					member.setTerminalId(terminal210.getId());//将outer成员统一为210类型（qt类型疑似有bug）
					member.setTerminalname(terminal210.getName());
					member.setTerminalType(terminal210.getType());
					
					List<TerminalBundlePO> terminalBundlePoList = terminalBundleDao.findByTerminalId(member.getTerminalId());
					List<BusinessGroupMemberTerminalBundleChannelPO> totalMemberTerminalBundleChannels = new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>();
					
					for(TerminalBundlePO terminalBundlePo : terminalBundlePoList){
						BusinessGroupMemberTerminalBundlePO terminalBundle= new BusinessGroupMemberTerminalBundlePO();
						terminalBundle.setBundleId(member.getUuid());
						terminalBundle.setBundleName("outdevice-"+member.getName());
						terminalBundle.setUsername("outuser-"+member.getName());
						terminalBundle.setDeviceModel("jv210");
						terminalBundle.setBundleType("VenusTerminal");
						if(localLayerId==null)localLayerId= resourceRemoteService.queryLocalLayerId();
						terminalBundle.setLayerId(localLayerId);
						terminalBundle.setFolderId(member.getFolderId());
						terminalBundle.setMember(member);
					
						terminalBundle.setTerminalBundleId(terminalBundlePo.getId().toString());
						terminalBundle.setTerminalBundleName(terminalBundlePo.getName());
						terminalBundle.setTerminalBundleType(terminalBundlePo.getType());
						terminalBundle.setTerminalId(terminal210.getId());
						
						businessGroupMemberDao.save(member);
						businessGroupMemberTerminalBundleDao.save(terminalBundle);
						
						//选一个视频编码通道和一个音频编码通道
						List<TerminalBundleChannelPO> allTerminalBundleChannelPoList = terminalBundleChannelDao.findByTerminalBundleId(terminalBundlePo.getId());
//						List<ChannelSchemeDTO> channelSchemeDtoList= resourceChannelDao.findByBundleIds(new ArrayListWrapper<String>().add(bundleId).getList());
						List<TerminalBundleChannelPO> terminalBundleChannelPoList = new ArrayList<TerminalBundleChannelPO>();
						boolean hasAudioChannel = false;
						boolean hasVideoChannel = false;
						for(TerminalBundleChannelPO terminalBundleChannel : allTerminalBundleChannelPoList){
							if(!hasAudioChannel && TerminalBundleChannelType.AUDIO_ENCODE.equals(terminalBundleChannel.getType())){
								terminalBundleChannelPoList.add(terminalBundleChannel);
								hasAudioChannel = true;
							}
							if(!hasVideoChannel && TerminalBundleChannelType.VIDEO_ENCODE.equals(terminalBundleChannel.getType())){
								terminalBundleChannelPoList.add(terminalBundleChannel);
								hasVideoChannel = true;
							}
						}
						
						/** 所拥有的终端设备通道 */
						List<BusinessGroupMemberTerminalBundleChannelPO> memberTerminalBundleChannels = new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>();
						
						for(TerminalBundleChannelPO terminalBundleChannelPo : terminalBundleChannelPoList){
							BusinessGroupMemberTerminalBundleChannelPO businessGroupMemberTerminalBundleChannelPo = new BusinessGroupMemberTerminalBundleChannelPO();
							if(TerminalBundleChannelType.AUDIO_ENCODE.equals(terminalBundleChannelPo.getType())){
								businessGroupMemberTerminalBundleChannelPo.setChannelId("VenusAudioIn_1");
							}else if(TerminalBundleChannelType.VIDEO_ENCODE.equals(terminalBundleChannelPo.getType())){
								businessGroupMemberTerminalBundleChannelPo.setChannelId("VenusVideoIn_1");
							}
							businessGroupMemberTerminalBundleChannelPo.setBundleId(member.getUuid());
							businessGroupMemberTerminalBundleChannelPo.setBundleName("outdevice-"+member.getName());
							businessGroupMemberTerminalBundleChannelPo.setMemberTerminalBundle(terminalBundle);
							businessGroupMemberTerminalBundleChannelPo.setTerminalBundleChannelId(terminalBundleChannelPo.getId());
							businessGroupMemberTerminalBundleChannelPo.setTerminalBundleChannelType(terminalBundleChannelPo.getType());
							TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPo = 
									terminalChannelBundleChannelPermissionDao.findByTerminalIdAndTerminalBundleIdAndTerminalBundleChannelId(member.getTerminalId(), terminalBundlePo.getId(), terminalBundleChannelPo.getId());
							if(terminalChannelBundleChannelPermissionPo != null){
								businessGroupMemberTerminalBundleChannelPo.setChannelParamsType(terminalChannelBundleChannelPermissionPo.getChannelParamsType());
							}
							businessGroupMemberTerminalBundleChannelPo.setTerminalBundleId(terminalBundleChannelPo.getTerminalBundleId());
							memberTerminalBundleChannels.add(businessGroupMemberTerminalBundleChannelPo);
						}
						 
						terminalBundle.setMemberTerminalBundleChannels(memberTerminalBundleChannels);
						
						terminalBundles.add(terminalBundle);
						totalMemberTerminalBundleChannels.addAll(memberTerminalBundleChannels);
						
					}
					member.overWriteSetTerminalBundle(terminalBundles);

					//构造BusinessGroupMemberTerminalChannelPO终端通道
					List<TerminalEncodeAudioVideoChannelPermissionPO> terminalEncodeAudioVideoChannelPermissionPoList = terminalEncodeAudioVideoChannelPermissionDao.findByTerminalId(member.getTerminalId());
					for(TerminalChannelPO terminalChannelPo:terminalChannelDao.findByTerminalId(member.getTerminalId())){
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = new BusinessGroupMemberTerminalChannelPO();
						businessGroupMemberTerminalChannelPo.setMember(member);
						businessGroupMemberTerminalChannelPo.setMemberTerminalBundleChannels(new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>());
						businessGroupMemberTerminalChannelPo.setTerminalChannelId(terminalChannelPo.getId());
						businessGroupMemberTerminalChannelPo.setTerminalChannelname(terminalChannelPo.getName());
						businessGroupMemberTerminalChannelPo.setTerminalChannelType(terminalChannelPo.getType());
						businessGroupMemberTerminalChannelPo.setTerminalId(terminalChannelPo.getTerminalId());
						terminalChannels.add(businessGroupMemberTerminalChannelPo);
					}
					member.overWriteSetTerminalChannel(terminalChannels);
					businessGroupMemberDao.save(member);

					businessGroupMemberTerminalChannelDao.save(terminalChannels);
					//终端通道中视频通道与音频通道的对应关系
					Map<Long, BusinessGroupMemberTerminalChannelPO> businessGroupMemberTerminalChannelPoMap = terminalChannels.stream().collect(Collectors.toMap(BusinessGroupMemberTerminalChannelPO::getTerminalChannelId, Function.identity()));
					for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo :terminalChannels){
						if(TerminalChannelType.VIDEO_ENCODE.equals(businessGroupMemberTerminalChannelPo.getTerminalChannelType())){
							for(TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionPo :terminalEncodeAudioVideoChannelPermissionPoList){
								if(terminalEncodeAudioVideoChannelPermissionPo.getTerminalVideoChannelId().equals(businessGroupMemberTerminalChannelPo.getTerminalChannelId())){
									BusinessGroupMemberTerminalChannelPO audioEncodeChannel = businessGroupMemberTerminalChannelPoMap.get(terminalEncodeAudioVideoChannelPermissionPo.getTerminalAudioChannelId());
									businessGroupMemberTerminalChannelPo.setAudioEncodeChannel(audioEncodeChannel);
									break;
								}
							}
						}else if(TerminalChannelType.AUDIO_ENCODE.equals(businessGroupMemberTerminalChannelPo.getTerminalChannelType())){
							List<BusinessGroupMemberTerminalChannelPO> videoEncodeChannelList = new ArrayList<BusinessGroupMemberTerminalChannelPO>();
							for(TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionPo :terminalEncodeAudioVideoChannelPermissionPoList){
								if(terminalEncodeAudioVideoChannelPermissionPo.getTerminalAudioChannelId().equals(businessGroupMemberTerminalChannelPo.getTerminalChannelId())){
									videoEncodeChannelList.add(businessGroupMemberTerminalChannelPoMap.get(terminalEncodeAudioVideoChannelPermissionPo.getTerminalVideoChannelId()));
								}
							}
							businessGroupMemberTerminalChannelPo.setVideoEncodeChannels(videoEncodeChannelList);
						}
					}
					businessGroupMemberDao.save(member);
					
					//终端通道与终端设备通道的对应关系
					List<TerminalChannelBundleChannelPermissionPO> terminalChannelBundleChannelPermissionPoList = terminalChannelBundleChannelPermissionDao.findByTerminalId(member.getTerminalId());
					for(TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPo : terminalChannelBundleChannelPermissionPoList){
						BusinessGroupMemberTerminalBundleChannelPO businessGroupMemberTerminalBundleChannelPo = null;
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = null;
						for(BusinessGroupMemberTerminalBundleChannelPO terminalBundleChannelRelated : totalMemberTerminalBundleChannels){
							if(terminalChannelBundleChannelPermissionPo.getTerminalBundleId().equals(terminalBundleChannelRelated.getTerminalBundleId())
									&& terminalChannelBundleChannelPermissionPo.getTerminalBundleChannelId().equals(terminalBundleChannelRelated.getTerminalBundleChannelId())){
								businessGroupMemberTerminalBundleChannelPo = terminalBundleChannelRelated;
								break;
							}
						}
						
						for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelRelated:terminalChannels){
							if(terminalChannelBundleChannelPermissionPo.getTerminalChannelId().equals(businessGroupMemberTerminalChannelRelated.getTerminalChannelId())){
								businessGroupMemberTerminalChannelPo = businessGroupMemberTerminalChannelRelated;
								break;
							}
						}
						
						if(businessGroupMemberTerminalBundleChannelPo != null && businessGroupMemberTerminalChannelPo != null){
							businessGroupMemberTerminalBundleChannelPo.setMemberTerminalChannel(businessGroupMemberTerminalChannelPo);
							businessGroupMemberTerminalChannelPo.getMemberTerminalBundleChannels().add(businessGroupMemberTerminalBundleChannelPo);
						}
					}
					
				}else if(OriginType.INNER.equals(member.getOriginType())){
					
					//根据模板查找对应关系
					//根据关系赋值真实数据
		//				构造BusinessGroupMemberTerminalBundlePO终端设备
					List<TerminalBundlePO> terminalBundlePoList = terminalBundleDao.findByTerminalId(member.getTerminalId());
					//获取用户与设备对应关系
		//				List<String> originIdList = groupMembers.stream().map(BusinessGroupMemberPO::getOriginId).collect(Collectors.toList());
					Map<Long, TerminalBundleUserPermissionPO> terminalBundleUserPermissionMap= terminalBundleUserPermissionDao.findByUserIdAndTerminalId(member.getOriginId(), member.getTerminalId())
																					.stream().collect(Collectors.toMap(TerminalBundleUserPermissionPO::getTerminalBundleId, Function.identity()));
					
					List<BusinessGroupMemberTerminalBundleChannelPO> totalMemberTerminalBundleChannels = new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>();
					
					for(TerminalBundlePO terminalBundlePo:terminalBundlePoList){
						if(terminalBundleUserPermissionMap.get(terminalBundlePo.getId()) != null){
							String bundleId = terminalBundleUserPermissionMap.get(terminalBundlePo.getId()).getBundleId();
							BundlePO bundlePo = bundleDao.findByBundleId(bundleId);
							
							BusinessGroupMemberTerminalBundlePO terminalBundle= new BusinessGroupMemberTerminalBundlePO();
							terminalBundle.setBundleId(bundlePo.getBundleId());
							terminalBundle.setBundleName(bundlePo.getBundleName());
							terminalBundle.setUsername(bundlePo.getUsername());
							terminalBundle.setDeviceModel(bundlePo.getBundleType());
							terminalBundle.setBundleType(bundlePo.getBundleType());
							terminalBundle.setLayerId(bundlePo.getAccessNodeUid());
							terminalBundle.setFolderId(bundlePo.getFolderId());
							terminalBundle.setMember(member);
						
							terminalBundle.setTerminalBundleId(terminalBundlePo.getId().toString());
							terminalBundle.setTerminalBundleName(terminalBundlePo.getName());
							terminalBundle.setTerminalBundleType(terminalBundlePo.getType());
							terminalBundle.setTerminalId(terminalBundlePo.getTerminalId());
							
							businessGroupMemberDao.save(member);
							businessGroupMemberTerminalBundleDao.save(terminalBundle);
							//构造BusinessGroupMemberTerminalBundleChannelPO
							List<TerminalBundleChannelPO> terminalBundleChannelPoList = terminalBundleChannelDao.findByTerminalBundleId(terminalBundlePo.getId());
							List<ChannelSchemeDTO> channelSchemeDtoList= resourceChannelDao.findByBundleIds(new ArrayListWrapper<String>().add(bundleId).getList());
							
							/** 所拥有的终端设备通道 */
							List<BusinessGroupMemberTerminalBundleChannelPO> memberTerminalBundleChannels = new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>();
							
							//需要判空
							for(TerminalBundleChannelPO terminalBundleChannelPo : terminalBundleChannelPoList){
								for(ChannelSchemeDTO channelSchemeDto : channelSchemeDtoList){
									if(terminalBundleChannelPo.getChannelId().equals(channelSchemeDto.getChannelId())){
										BusinessGroupMemberTerminalBundleChannelPO businessGroupMemberTerminalBundleChannelPo = new BusinessGroupMemberTerminalBundleChannelPO();
										businessGroupMemberTerminalBundleChannelPo.setName(channelSchemeDto.getChannelName());
										businessGroupMemberTerminalBundleChannelPo.setType(ChannelType.fromChannelType(channelSchemeDto.getChannelId().replace("_", "-")));
										businessGroupMemberTerminalBundleChannelPo.setChannelId(channelSchemeDto.getChannelId());
										businessGroupMemberTerminalBundleChannelPo.setChannelName(channelSchemeDto.getChannelName());
										businessGroupMemberTerminalBundleChannelPo.setBaseType(channelSchemeDto.getBaseType());
										businessGroupMemberTerminalBundleChannelPo.setBundleId(bundlePo.getBundleId());
										businessGroupMemberTerminalBundleChannelPo.setBundleName(bundlePo.getBundleName());
										businessGroupMemberTerminalBundleChannelPo.setDeviceModel(channelSchemeDto.getDeviceModel());
										businessGroupMemberTerminalBundleChannelPo.setBundleType(channelSchemeDto.getBundleType());
										businessGroupMemberTerminalBundleChannelPo.setMemberTerminalBundle(terminalBundle);
										businessGroupMemberTerminalBundleChannelPo.setTerminalBundleChannelId(terminalBundleChannelPo.getId());
										businessGroupMemberTerminalBundleChannelPo.setTerminalBundleChannelType(terminalBundleChannelPo.getType());
										TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPo = 
												terminalChannelBundleChannelPermissionDao.findByTerminalIdAndTerminalBundleIdAndTerminalBundleChannelId(member.getTerminalId(), terminalBundlePo.getId(), terminalBundleChannelPo.getId());
										if(terminalChannelBundleChannelPermissionPo != null){
											businessGroupMemberTerminalBundleChannelPo.setChannelParamsType(terminalChannelBundleChannelPermissionPo.getChannelParamsType());
										}
										businessGroupMemberTerminalBundleChannelPo.setTerminalBundleId(terminalBundleChannelPo.getTerminalBundleId());
										memberTerminalBundleChannels.add(businessGroupMemberTerminalBundleChannelPo);
									}
								}
								
							}
							 
							terminalBundle.setMemberTerminalBundleChannels(memberTerminalBundleChannels);
							
							terminalBundles.add(terminalBundle);
							totalMemberTerminalBundleChannels.addAll(memberTerminalBundleChannels);
						}
					}
					member.overWriteSetTerminalBundle(terminalBundles);
					
					//构造BusinessGroupMemberTerminalChannelPO终端通道
					List<TerminalEncodeAudioVideoChannelPermissionPO> terminalEncodeAudioVideoChannelPermissionPoList = terminalEncodeAudioVideoChannelPermissionDao.findByTerminalId(member.getTerminalId());
					for(TerminalChannelPO terminalChannelPo:terminalChannelDao.findByTerminalId(member.getTerminalId())){
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = new BusinessGroupMemberTerminalChannelPO();
						businessGroupMemberTerminalChannelPo.setMember(member);
		//					businessGroupMemberTerminalChannelPo.setScreen();
						businessGroupMemberTerminalChannelPo.setMemberTerminalBundleChannels(new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>());
		//					
		//					businessGroupMemberTerminalChannelPo.setAudioOutput();
						
						businessGroupMemberTerminalChannelPo.setTerminalChannelId(terminalChannelPo.getId());
						businessGroupMemberTerminalChannelPo.setTerminalChannelname(terminalChannelPo.getName());
						businessGroupMemberTerminalChannelPo.setTerminalChannelType(terminalChannelPo.getType());
						businessGroupMemberTerminalChannelPo.setTerminalId(terminalChannelPo.getTerminalId());
						
						terminalChannels.add(businessGroupMemberTerminalChannelPo);
					}
					member.overWriteSetTerminalChannel(terminalChannels);
					businessGroupMemberDao.save(member);
					
					businessGroupMemberTerminalChannelDao.save(terminalChannels);
					//终端通道中视频通道与音频通道的对应关系
					Map<Long, BusinessGroupMemberTerminalChannelPO> businessGroupMemberTerminalChannelPoMap = terminalChannels.stream().collect(Collectors.toMap(BusinessGroupMemberTerminalChannelPO::getTerminalChannelId, Function.identity()));
					for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo :terminalChannels){
						if(TerminalChannelType.VIDEO_ENCODE.equals(businessGroupMemberTerminalChannelPo.getTerminalChannelType())){
							for(TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionPo :terminalEncodeAudioVideoChannelPermissionPoList){
								if(terminalEncodeAudioVideoChannelPermissionPo.getTerminalVideoChannelId().equals(businessGroupMemberTerminalChannelPo.getTerminalChannelId())){
									BusinessGroupMemberTerminalChannelPO audioEncodeChannel = businessGroupMemberTerminalChannelPoMap.get(terminalEncodeAudioVideoChannelPermissionPo.getTerminalAudioChannelId());
									businessGroupMemberTerminalChannelPo.setAudioEncodeChannel(audioEncodeChannel);
									break;
								}
							}
						}else if(TerminalChannelType.AUDIO_ENCODE.equals(businessGroupMemberTerminalChannelPo.getTerminalChannelType())){
							List<BusinessGroupMemberTerminalChannelPO> videoEncodeChannelList = new ArrayList<BusinessGroupMemberTerminalChannelPO>();
							for(TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionPo :terminalEncodeAudioVideoChannelPermissionPoList){
								if(terminalEncodeAudioVideoChannelPermissionPo.getTerminalAudioChannelId().equals(businessGroupMemberTerminalChannelPo.getTerminalChannelId())){
									videoEncodeChannelList.add(businessGroupMemberTerminalChannelPoMap.get(terminalEncodeAudioVideoChannelPermissionPo.getTerminalVideoChannelId()));
								}
							}
							businessGroupMemberTerminalChannelPo.setVideoEncodeChannels(videoEncodeChannelList);
						}
					}
					businessGroupMemberDao.save(member);
					
					//构造BusinessGroupMemberTerminalScreenPO物理屏幕
					List<TerminalPhysicalScreenPO> terminalPhysicalScreenPoList= terminalPhysicalScreenDao.findByTerminalId(member.getTerminalId());
					for(TerminalPhysicalScreenPO terminalPhysicalScreenPo:terminalPhysicalScreenPoList){
						BusinessGroupMemberTerminalScreenPO businessGroupMemberTerminalScreenPo = new BusinessGroupMemberTerminalScreenPO();
						businessGroupMemberTerminalScreenPo.setMember(member);
		//					businessGroupMemberTerminalScreenPo.setAudioOutput();
						businessGroupMemberTerminalScreenPo.setChannels(new ArrayList<BusinessGroupMemberTerminalChannelPO>());
						
						businessGroupMemberTerminalScreenPo.setTerminalPhysicalScreenId(terminalPhysicalScreenPo.getId());
						businessGroupMemberTerminalScreenPo.setName(terminalPhysicalScreenPo.getName());
						businessGroupMemberTerminalScreenPo.setTerminalId(terminalPhysicalScreenPo.getTerminalId());
						businessGroupMemberTerminalScreenPo.setTerminalAudioOutputId(terminalPhysicalScreenPo.getTerminalAudioOutputId());
						businessGroupMemberTerminalScreenPo.setX(terminalPhysicalScreenPo.getX());
						businessGroupMemberTerminalScreenPo.setY(terminalPhysicalScreenPo.getY());
						businessGroupMemberTerminalScreenPo.setWidth(terminalPhysicalScreenPo.getWidth());
						businessGroupMemberTerminalScreenPo.setHeight(terminalPhysicalScreenPo.getHeight());
						
						screens.add(businessGroupMemberTerminalScreenPo);
					}
					member.overWriteSetScreen(screens);
					
					//构造BusinessGroupMemberTerminalAudioOutputPO音频输出
					List<TerminalAudioOutputPO> terminalAudioOutputPoList = terminalAudioOutputDao.findByTerminalId(member.getTerminalId());
					for(TerminalAudioOutputPO terminalAudioOutputPo :terminalAudioOutputPoList){
						BusinessGroupMemberTerminalAudioOutputPO businessGroupMemberTerminalAudioOutputPo = new BusinessGroupMemberTerminalAudioOutputPO();
						businessGroupMemberTerminalAudioOutputPo.setMember(member);
		//					businessGroupMemberTerminalAudioOutputPo.setChannels();
						businessGroupMemberTerminalAudioOutputPo.setScreens(new ArrayList<BusinessGroupMemberTerminalScreenPO>());
						
						businessGroupMemberTerminalAudioOutputPo.setTerminalAudioOutputId(terminalAudioOutputPo.getId());
						businessGroupMemberTerminalAudioOutputPo.setTerminalId(terminalAudioOutputPo.getTerminalId());
						businessGroupMemberTerminalAudioOutputPo.setName(terminalAudioOutputPo.getName());
						
						audioOutputs.add(businessGroupMemberTerminalAudioOutputPo);
					}
					member.overWriteSetAudioOutputs(audioOutputs);
					
					businessGroupMemberTerminalBundleDao.save(terminalBundles);
					businessGroupMemberDao.save(member);
					
					//终端通道与终端设备通道的对应关系
					List<TerminalChannelBundleChannelPermissionPO> terminalChannelBundleChannelPermissionPoList = terminalChannelBundleChannelPermissionDao.findByTerminalId(member.getTerminalId());
					for(TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPo : terminalChannelBundleChannelPermissionPoList){
						BusinessGroupMemberTerminalBundleChannelPO businessGroupMemberTerminalBundleChannelPo = null;
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = null;
						for(BusinessGroupMemberTerminalBundleChannelPO terminalBundleChannelRelated : totalMemberTerminalBundleChannels){
							if(terminalChannelBundleChannelPermissionPo.getTerminalBundleId().equals(terminalBundleChannelRelated.getTerminalBundleId())
									&& terminalChannelBundleChannelPermissionPo.getTerminalBundleChannelId().equals(terminalBundleChannelRelated.getTerminalBundleChannelId())){
								businessGroupMemberTerminalBundleChannelPo = terminalBundleChannelRelated;
								break;
							}
						}
						
						for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelRelated:terminalChannels){
							if(terminalChannelBundleChannelPermissionPo.getTerminalChannelId().equals(businessGroupMemberTerminalChannelRelated.getTerminalChannelId())){
								businessGroupMemberTerminalChannelPo = businessGroupMemberTerminalChannelRelated;
								break;
							}
						}
						
						if(businessGroupMemberTerminalBundleChannelPo != null && businessGroupMemberTerminalChannelPo != null){
							businessGroupMemberTerminalBundleChannelPo.setMemberTerminalChannel(businessGroupMemberTerminalChannelPo);
							businessGroupMemberTerminalChannelPo.getMemberTerminalBundleChannels().add(businessGroupMemberTerminalBundleChannelPo);
						}
					}
					
					//物理屏幕与音频输出的对应关系
					for(BusinessGroupMemberTerminalScreenPO businessGroupMemberTerminalScreenRelated : screens){
						for(BusinessGroupMemberTerminalAudioOutputPO businessGroupMemberTerminalAudioOutputRelated : audioOutputs){
							if(businessGroupMemberTerminalScreenRelated.getTerminalAudioOutputId().equals(businessGroupMemberTerminalAudioOutputRelated.getTerminalAudioOutputId())){
								businessGroupMemberTerminalScreenRelated.setAudioOutput(businessGroupMemberTerminalAudioOutputRelated);
								businessGroupMemberTerminalAudioOutputRelated.getScreens().add(businessGroupMemberTerminalScreenRelated);
							}
						}
					}
					
					//音频输出与终端通道的关系
					List<Long> terminalChannelIds = terminalChannels.stream().map(BusinessGroupMemberTerminalChannelPO::getTerminalChannelId).collect(Collectors.toList());
					List<Long> audioOutputIds = audioOutputs.stream().map(BusinessGroupMemberTerminalAudioOutputPO::getTerminalAudioOutputId).collect(Collectors.toList());
					List<TerminalAudioOutputChannelPermissionPO> terminalAudioOutputChannelPermissionPoList = 
							terminalAudioOutputChannelPermissionDao.findByTerminalAudioChannelIdInAndTerminalAudioOutputIdIn(terminalChannelIds, audioOutputIds);
					for(TerminalAudioOutputChannelPermissionPO terminalAudioOutputChannelPermissionPo : terminalAudioOutputChannelPermissionPoList){
						BusinessGroupMemberTerminalAudioOutputPO businessGroupMemberTerminalAudioOutputPo = null;
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = null;
						for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPoRelated : terminalChannels){
							if(terminalAudioOutputChannelPermissionPo.getTerminalAudioChannelId().equals(businessGroupMemberTerminalChannelPoRelated.getTerminalChannelId())){
								businessGroupMemberTerminalChannelPo = businessGroupMemberTerminalChannelPoRelated;
								break;
							}
						}
						
						for(BusinessGroupMemberTerminalAudioOutputPO businessGroupMemberTerminalAudioOutputPoRelated: audioOutputs){
							if(terminalAudioOutputChannelPermissionPo.getTerminalAudioOutputId().equals(businessGroupMemberTerminalAudioOutputPoRelated.getTerminalAudioOutputId())){
								businessGroupMemberTerminalAudioOutputPo = businessGroupMemberTerminalAudioOutputPoRelated;
								break;
							}
						}
						
						if(businessGroupMemberTerminalAudioOutputPo != null && businessGroupMemberTerminalChannelPo != null){
							if(businessGroupMemberTerminalAudioOutputPo.getChannels() == null){
								businessGroupMemberTerminalAudioOutputPo.setChannels(new ArrayList<BusinessGroupMemberTerminalChannelPO>());
							}
							businessGroupMemberTerminalAudioOutputPo.getChannels().add(businessGroupMemberTerminalChannelPo);
							businessGroupMemberTerminalChannelPo.setAudioOutput(businessGroupMemberTerminalAudioOutputPo);
						}
					}
					
					//物理屏幕与终端通道的对应关系
					List<Long> screenIdList = screens.stream().map(BusinessGroupMemberTerminalScreenPO::getTerminalPhysicalScreenId).collect(Collectors.toList());
					List<TerminalPhysicalScreenChannelPermissionPO> terminalPhysicalScreenChannelPermissionPoList = terminalPhysicalScreenChannelPermissionDao.findByTerminalPhysicalScreenIdIn(screenIdList);
					for(TerminalPhysicalScreenChannelPermissionPO terminalPhysicalScreenChannelPermissionPo:terminalPhysicalScreenChannelPermissionPoList){
						BusinessGroupMemberTerminalScreenPO businessGroupMemberTerminalScreenPo = null;
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = null;
						for(BusinessGroupMemberTerminalScreenPO businessGroupMemberTerminalScreenRelated:screens){
							if(terminalPhysicalScreenChannelPermissionPo.getTerminalPhysicalScreenId().equals(businessGroupMemberTerminalScreenRelated.getTerminalPhysicalScreenId())){
								businessGroupMemberTerminalScreenPo = businessGroupMemberTerminalScreenRelated;
								break;
							}
						}
						
						for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPoRelated:terminalChannels){
							if(terminalPhysicalScreenChannelPermissionPo.getTerminalChannelId().equals(businessGroupMemberTerminalChannelPoRelated.getTerminalChannelId())){
								businessGroupMemberTerminalChannelPo = businessGroupMemberTerminalChannelPoRelated;
								break;
							}
						}
						
						if(businessGroupMemberTerminalScreenPo != null && businessGroupMemberTerminalChannelPo != null){
							businessGroupMemberTerminalChannelPo.setScreen(businessGroupMemberTerminalScreenPo);
							businessGroupMemberTerminalScreenPo.getChannels().add(businessGroupMemberTerminalChannelPo);
						}
						
					}
				}
			}else if(GroupMemberType.MEMBER_HALL.equals(member.getGroupMemberType())
					|| GroupMemberType.MEMBER_DEVICE.equals(member.getGroupMemberType())){
				
				if(OriginType.OUTER.equals(member.getOriginType())){
					
					member.setTerminalId(terminal210.getId());//将outer成员统一为210类型（qt类型疑似有bug）
					member.setTerminalname(terminal210.getName());
					member.setTerminalType(terminal210.getType());
					
					String bundleId = member.getBundleId();
					BundlePO bundlePo = bundleDao.findByBundleId(bundleId);
					
					List<TerminalBundlePO> terminalBundlePoList = terminalBundleDao.findByTerminalId(terminal210.getId());
					List<BusinessGroupMemberTerminalBundleChannelPO> totalMemberTerminalBundleChannels = new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>();
					
					for(TerminalBundlePO terminalBundlePo : terminalBundlePoList){
						BusinessGroupMemberTerminalBundlePO terminalBundle= new BusinessGroupMemberTerminalBundlePO();
						terminalBundle.setBundleId(bundlePo.getBundleId());
						terminalBundle.setBundleName(bundlePo.getBundleName());
						terminalBundle.setUsername(bundlePo.getUsername());
						terminalBundle.setDeviceModel(bundlePo.getBundleType());
						terminalBundle.setBundleType(bundlePo.getBundleType());
//						terminalBundle.setLayerId(bundlePo.getAccessNodeUid());
						terminalBundle.setFolderId(bundlePo.getFolderId());
						terminalBundle.setMember(member);
						
						if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
						terminalBundle.setLayerId(localLayerId);
					
						terminalBundle.setTerminalBundleId(terminalBundlePo.getId().toString());
						terminalBundle.setTerminalBundleName(terminalBundlePo.getName());
						terminalBundle.setTerminalBundleType(terminalBundlePo.getType());
						terminalBundle.setTerminalId(terminal210.getId());
						
						//选一个视频编码通道和一个音频编码通道
						List<TerminalBundleChannelPO> allTerminalBundleChannelPoList = terminalBundleChannelDao.findByTerminalBundleId(terminalBundlePo.getId());
//						List<ChannelSchemeDTO> channelSchemeDtoList= resourceChannelDao.findByBundleIds(new ArrayListWrapper<String>().add(bundleId).getList());
						List<TerminalBundleChannelPO> terminalBundleChannelPoList = new ArrayList<TerminalBundleChannelPO>();
						boolean hasAudioChannel = false;
						boolean hasVideoChannel = false;
						for(TerminalBundleChannelPO terminalBundleChannel : allTerminalBundleChannelPoList){
							if(!hasAudioChannel && TerminalBundleChannelType.AUDIO_ENCODE.equals(terminalBundleChannel.getType())){
								terminalBundleChannelPoList.add(terminalBundleChannel);
								hasAudioChannel = true;
							}
							if(!hasVideoChannel && TerminalBundleChannelType.VIDEO_ENCODE.equals(terminalBundleChannel.getType())){
								terminalBundleChannelPoList.add(terminalBundleChannel);
								hasVideoChannel = true;
							}
						}
						
						/** 所拥有的终端设备通道 */
						List<BusinessGroupMemberTerminalBundleChannelPO> memberTerminalBundleChannels = new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>();
						
						for(TerminalBundleChannelPO terminalBundleChannelPo : terminalBundleChannelPoList){
							BusinessGroupMemberTerminalBundleChannelPO businessGroupMemberTerminalBundleChannelPo = new BusinessGroupMemberTerminalBundleChannelPO();
							if(TerminalBundleChannelType.AUDIO_ENCODE.equals(terminalBundleChannelPo.getType())){
								businessGroupMemberTerminalBundleChannelPo.setChannelId("VenusAudioIn_1");
							}else if(TerminalBundleChannelType.VIDEO_ENCODE.equals(terminalBundleChannelPo.getType())){
								businessGroupMemberTerminalBundleChannelPo.setChannelId("VenusVideoIn_1");
							}
							businessGroupMemberTerminalBundleChannelPo.setBundleId(bundlePo.getBundleId());
							businessGroupMemberTerminalBundleChannelPo.setBundleName(bundlePo.getBundleName());
							businessGroupMemberTerminalBundleChannelPo.setMemberTerminalBundle(terminalBundle);
							//为两个集合最后相互处理
//									businessGroupMemberTerminalBundleChannelPo.setMemberTerminalChannel();
							businessGroupMemberTerminalBundleChannelPo.setTerminalBundleChannelId(terminalBundleChannelPo.getId());
							businessGroupMemberTerminalBundleChannelPo.setTerminalBundleChannelType(terminalBundleChannelPo.getType());
							TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPo = 
									terminalChannelBundleChannelPermissionDao.findByTerminalIdAndTerminalBundleIdAndTerminalBundleChannelId(member.getTerminalId(), terminalBundlePo.getId(), terminalBundleChannelPo.getId());
							if(terminalChannelBundleChannelPermissionPo != null){
								businessGroupMemberTerminalBundleChannelPo.setChannelParamsType(terminalChannelBundleChannelPermissionPo.getChannelParamsType());
							}
							businessGroupMemberTerminalBundleChannelPo.setTerminalBundleId(terminalBundleChannelPo.getTerminalBundleId());
							memberTerminalBundleChannels.add(businessGroupMemberTerminalBundleChannelPo);
						}
						 
						terminalBundle.setMemberTerminalBundleChannels(memberTerminalBundleChannels);
						
						terminalBundles.add(terminalBundle);
						totalMemberTerminalBundleChannels.addAll(memberTerminalBundleChannels);
						
						
					}
					
					member.overWriteSetTerminalBundle(terminalBundles);
					
					//构造BusinessGroupMemberTerminalChannelPO终端通道
					List<TerminalEncodeAudioVideoChannelPermissionPO> terminalEncodeAudioVideoChannelPermissionPoList = terminalEncodeAudioVideoChannelPermissionDao.findByTerminalId(member.getTerminalId());
					for(TerminalChannelPO terminalChannelPo:terminalChannelDao.findByTerminalId(member.getTerminalId())){
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = new BusinessGroupMemberTerminalChannelPO();
						businessGroupMemberTerminalChannelPo.setMember(member);
						businessGroupMemberTerminalChannelPo.setMemberTerminalBundleChannels(new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>());
						businessGroupMemberTerminalChannelPo.setTerminalChannelId(terminalChannelPo.getId());
						businessGroupMemberTerminalChannelPo.setTerminalChannelname(terminalChannelPo.getName());
						businessGroupMemberTerminalChannelPo.setTerminalChannelType(terminalChannelPo.getType());
						businessGroupMemberTerminalChannelPo.setTerminalId(terminalChannelPo.getTerminalId());
						terminalChannels.add(businessGroupMemberTerminalChannelPo);
					}
					member.overWriteSetTerminalChannel(terminalChannels);

					businessGroupMemberDao.save(member);
					businessGroupMemberTerminalChannelDao.save(terminalChannels);
					//终端通道中视频通道与音频通道的对应关系
					Map<Long, BusinessGroupMemberTerminalChannelPO> businessGroupMemberTerminalChannelPoMap = terminalChannels.stream().collect(Collectors.toMap(BusinessGroupMemberTerminalChannelPO::getTerminalChannelId, Function.identity()));
					for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo :terminalChannels){
						if(TerminalChannelType.VIDEO_ENCODE.equals(businessGroupMemberTerminalChannelPo.getTerminalChannelType())){
							for(TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionPo :terminalEncodeAudioVideoChannelPermissionPoList){
								if(terminalEncodeAudioVideoChannelPermissionPo.getTerminalVideoChannelId().equals(businessGroupMemberTerminalChannelPo.getTerminalChannelId())){
									BusinessGroupMemberTerminalChannelPO audioEncodeChannel = businessGroupMemberTerminalChannelPoMap.get(terminalEncodeAudioVideoChannelPermissionPo.getTerminalAudioChannelId());
									businessGroupMemberTerminalChannelPo.setAudioEncodeChannel(audioEncodeChannel);
									break;
								}
							}
						}else if(TerminalChannelType.AUDIO_ENCODE.equals(businessGroupMemberTerminalChannelPo.getTerminalChannelType())){
							List<BusinessGroupMemberTerminalChannelPO> videoEncodeChannelList = new ArrayList<BusinessGroupMemberTerminalChannelPO>();
							for(TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionPo :terminalEncodeAudioVideoChannelPermissionPoList){
								if(terminalEncodeAudioVideoChannelPermissionPo.getTerminalAudioChannelId().equals(businessGroupMemberTerminalChannelPo.getTerminalChannelId())){
									videoEncodeChannelList.add(businessGroupMemberTerminalChannelPoMap.get(terminalEncodeAudioVideoChannelPermissionPo.getTerminalVideoChannelId()));
								}
							}
							businessGroupMemberTerminalChannelPo.setVideoEncodeChannels(videoEncodeChannelList);
						}
					}
					businessGroupMemberDao.save(member);
					
					//终端通道与终端设备通道的对应关系

					List<TerminalChannelBundleChannelPermissionPO> terminalChannelBundleChannelPermissionPoList = terminalChannelBundleChannelPermissionDao.findByTerminalId(member.getTerminalId());
					for(TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPo : terminalChannelBundleChannelPermissionPoList){
						BusinessGroupMemberTerminalBundleChannelPO businessGroupMemberTerminalBundleChannelPo = null;
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = null;
						for(BusinessGroupMemberTerminalBundleChannelPO terminalBundleChannelRelated : totalMemberTerminalBundleChannels){
							if(terminalChannelBundleChannelPermissionPo.getTerminalBundleId().equals(terminalBundleChannelRelated.getTerminalBundleId())
									&& terminalChannelBundleChannelPermissionPo.getTerminalBundleChannelId().equals(terminalBundleChannelRelated.getTerminalBundleChannelId())){
								businessGroupMemberTerminalBundleChannelPo = terminalBundleChannelRelated;
								break;
							}
						}
						
						for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelRelated:terminalChannels){
							if(terminalChannelBundleChannelPermissionPo.getTerminalChannelId().equals(businessGroupMemberTerminalChannelRelated.getTerminalChannelId())){
								businessGroupMemberTerminalChannelPo = businessGroupMemberTerminalChannelRelated;
								break;
							}
						}
						
						if(businessGroupMemberTerminalBundleChannelPo != null && businessGroupMemberTerminalChannelPo != null){
							businessGroupMemberTerminalBundleChannelPo.setMemberTerminalChannel(businessGroupMemberTerminalChannelPo);
							businessGroupMemberTerminalChannelPo.getMemberTerminalBundleChannels().add(businessGroupMemberTerminalBundleChannelPo);
						}
					}
				
				}else if(OriginType.INNER.equals(member.getOriginType())){
					
					//获取会场与设备对应关系
					List<TerminalBundleConferenceHallPermissionPO> terminalBundleConferenceHallPermissionList = terminalBundleConferenceHallPermissionDao.findByConferenceHallId(Long.valueOf(member.getOriginId()))
																					.stream().collect(Collectors.toList());
					
					//用于后面的终端通道与终端设备通道关联
					List<BusinessGroupMemberTerminalBundleChannelPO> totalMemberTerminalBundleChannels = new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>();
					
					
					for(TerminalBundleConferenceHallPermissionPO p : terminalBundleConferenceHallPermissionList){
	//					if(terminalBundleConferenceHallPermissionMap.get(terminalBundlePo.getId()) != null){
						    TerminalBundlePO terminalBundlePo = terminalBundleDao.findOne(p.getTerminalBundleId());
							String bundleId = p.getBundleId();
							BundlePO bundlePo = bundleDao.findByBundleId(bundleId);
							
							BusinessGroupMemberTerminalBundlePO terminalBundle= new BusinessGroupMemberTerminalBundlePO();
							terminalBundle.setBundleId(bundlePo.getBundleId());
							terminalBundle.setBundleName(bundlePo.getBundleName());
							terminalBundle.setUsername(bundlePo.getUsername());
							terminalBundle.setDeviceModel(bundlePo.getBundleType());
							terminalBundle.setBundleType(bundlePo.getBundleType());
							terminalBundle.setLayerId(bundlePo.getAccessNodeUid());
							terminalBundle.setFolderId(bundlePo.getFolderId());
							terminalBundle.setMember(member);
						
							terminalBundle.setTerminalBundleId(terminalBundlePo.getId().toString());
							terminalBundle.setTerminalBundleName(terminalBundlePo.getName());
							terminalBundle.setTerminalBundleType(terminalBundlePo.getType());
							terminalBundle.setTerminalId(terminalBundlePo.getTerminalId());
							
							//构造BusinessGroupMemberTerminalBundleChannelPO
							List<TerminalBundleChannelPO> terminalBundleChannelPoList = terminalBundleChannelDao.findByTerminalBundleId(terminalBundlePo.getId());
							List<ChannelSchemeDTO> channelSchemeDtoList= resourceChannelDao.findByBundleIds(new ArrayListWrapper<String>().add(bundleId).getList());
							
							/** 所拥有的终端设备通道 */
							List<BusinessGroupMemberTerminalBundleChannelPO> memberTerminalBundleChannels = new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>();
							
							//需要判空
							for(TerminalBundleChannelPO terminalBundleChannelPo : terminalBundleChannelPoList){
								for(ChannelSchemeDTO channelSchemeDto : channelSchemeDtoList){
									if(terminalBundleChannelPo.getChannelId().equals(channelSchemeDto.getChannelId())){
										BusinessGroupMemberTerminalBundleChannelPO businessGroupMemberTerminalBundleChannelPo = new BusinessGroupMemberTerminalBundleChannelPO();
										businessGroupMemberTerminalBundleChannelPo.setName(channelSchemeDto.getChannelName());
										businessGroupMemberTerminalBundleChannelPo.setType(ChannelType.fromChannelType(channelSchemeDto.getChannelId().replace("_", "-")));
										businessGroupMemberTerminalBundleChannelPo.setChannelId(channelSchemeDto.getChannelId());
										businessGroupMemberTerminalBundleChannelPo.setChannelName(channelSchemeDto.getChannelName());
										businessGroupMemberTerminalBundleChannelPo.setBaseType(channelSchemeDto.getBaseType());
										businessGroupMemberTerminalBundleChannelPo.setBundleId(bundlePo.getBundleId());
										businessGroupMemberTerminalBundleChannelPo.setBundleName(bundlePo.getBundleName());
										businessGroupMemberTerminalBundleChannelPo.setDeviceModel(channelSchemeDto.getDeviceModel());
										businessGroupMemberTerminalBundleChannelPo.setBundleType(channelSchemeDto.getBundleType());
										businessGroupMemberTerminalBundleChannelPo.setMemberTerminalBundle(terminalBundle);
										//为两个集合最后相互处理
	//									businessGroupMemberTerminalBundleChannelPo.setMemberTerminalChannel();
										businessGroupMemberTerminalBundleChannelPo.setTerminalBundleChannelId(terminalBundleChannelPo.getId());
										businessGroupMemberTerminalBundleChannelPo.setTerminalBundleChannelType(terminalBundleChannelPo.getType());
										TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPo = 
												terminalChannelBundleChannelPermissionDao.findByTerminalIdAndTerminalBundleIdAndTerminalBundleChannelId(member.getTerminalId(), terminalBundlePo.getId(), terminalBundleChannelPo.getId());
										if(terminalChannelBundleChannelPermissionPo != null){
											businessGroupMemberTerminalBundleChannelPo.setChannelParamsType(terminalChannelBundleChannelPermissionPo.getChannelParamsType());
										}
										businessGroupMemberTerminalBundleChannelPo.setTerminalBundleId(terminalBundleChannelPo.getTerminalBundleId());
										memberTerminalBundleChannels.add(businessGroupMemberTerminalBundleChannelPo);
									}
								}
								
							}
							 
							terminalBundle.setMemberTerminalBundleChannels(memberTerminalBundleChannels);
							
							terminalBundles.add(terminalBundle);
							totalMemberTerminalBundleChannels.addAll(memberTerminalBundleChannels);
	//					}
					}
					member.overWriteSetTerminalBundle(terminalBundles);
					
					
					//构造BusinessGroupMemberTerminalChannelPO终端通道
					List<TerminalEncodeAudioVideoChannelPermissionPO> terminalEncodeAudioVideoChannelPermissionPoList = terminalEncodeAudioVideoChannelPermissionDao.findByTerminalId(member.getTerminalId());
					for(TerminalChannelPO terminalChannelPo:terminalChannelDao.findByTerminalId(member.getTerminalId())){
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = new BusinessGroupMemberTerminalChannelPO();
						businessGroupMemberTerminalChannelPo.setMember(member);
	//					businessGroupMemberTerminalChannelPo.setScreen();
						businessGroupMemberTerminalChannelPo.setMemberTerminalBundleChannels(new ArrayList<BusinessGroupMemberTerminalBundleChannelPO>());
	//					
	//					businessGroupMemberTerminalChannelPo.setAudioOutput();
						
						businessGroupMemberTerminalChannelPo.setTerminalChannelId(terminalChannelPo.getId());
						businessGroupMemberTerminalChannelPo.setTerminalChannelname(terminalChannelPo.getName());
						businessGroupMemberTerminalChannelPo.setTerminalChannelType(terminalChannelPo.getType());
						businessGroupMemberTerminalChannelPo.setTerminalId(terminalChannelPo.getTerminalId());
						
						terminalChannels.add(businessGroupMemberTerminalChannelPo);
					}
					member.overWriteSetTerminalChannel(terminalChannels);
					
					businessGroupMemberDao.save(member);
					businessGroupMemberTerminalChannelDao.save(terminalChannels);
					//终端通道中视频通道与音频通道的对应关系
					Map<Long, BusinessGroupMemberTerminalChannelPO> businessGroupMemberTerminalChannelPoMap = terminalChannels.stream().collect(Collectors.toMap(BusinessGroupMemberTerminalChannelPO::getTerminalChannelId, Function.identity()));
					for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo :terminalChannels){
						if(TerminalChannelType.VIDEO_ENCODE.equals(businessGroupMemberTerminalChannelPo.getTerminalChannelType())){
							for(TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionPo :terminalEncodeAudioVideoChannelPermissionPoList){
								if(terminalEncodeAudioVideoChannelPermissionPo.getTerminalVideoChannelId().equals(businessGroupMemberTerminalChannelPo.getTerminalChannelId())){
									BusinessGroupMemberTerminalChannelPO audioEncodeChannel = businessGroupMemberTerminalChannelPoMap.get(terminalEncodeAudioVideoChannelPermissionPo.getTerminalAudioChannelId());
									businessGroupMemberTerminalChannelPo.setAudioEncodeChannel(audioEncodeChannel);
									break;
								}
							}
						}else if(TerminalChannelType.AUDIO_ENCODE.equals(businessGroupMemberTerminalChannelPo.getTerminalChannelType())){
							List<BusinessGroupMemberTerminalChannelPO> videoEncodeChannelList = new ArrayList<BusinessGroupMemberTerminalChannelPO>();
							for(TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionPo :terminalEncodeAudioVideoChannelPermissionPoList){
								if(terminalEncodeAudioVideoChannelPermissionPo.getTerminalAudioChannelId().equals(businessGroupMemberTerminalChannelPo.getTerminalChannelId())){
									videoEncodeChannelList.add(businessGroupMemberTerminalChannelPoMap.get(terminalEncodeAudioVideoChannelPermissionPo.getTerminalVideoChannelId()));
								}
							}
							businessGroupMemberTerminalChannelPo.setVideoEncodeChannels(videoEncodeChannelList);
						}
					}
					businessGroupMemberDao.save(member);
					
					//构造BusinessGroupMemberTerminalScreenPO物理屏幕
					List<TerminalPhysicalScreenPO> terminalPhysicalScreenPoList= terminalPhysicalScreenDao.findByTerminalId(member.getTerminalId());
					for(TerminalPhysicalScreenPO terminalPhysicalScreenPo:terminalPhysicalScreenPoList){
						BusinessGroupMemberTerminalScreenPO businessGroupMemberTerminalScreenPo = new BusinessGroupMemberTerminalScreenPO();
						businessGroupMemberTerminalScreenPo.setMember(member);
	//					businessGroupMemberTerminalScreenPo.setAudioOutput();
						businessGroupMemberTerminalScreenPo.setChannels(new ArrayList<BusinessGroupMemberTerminalChannelPO>());
						
						businessGroupMemberTerminalScreenPo.setTerminalPhysicalScreenId(terminalPhysicalScreenPo.getId());
						businessGroupMemberTerminalScreenPo.setName(terminalPhysicalScreenPo.getName());
						businessGroupMemberTerminalScreenPo.setTerminalId(terminalPhysicalScreenPo.getTerminalId());
						businessGroupMemberTerminalScreenPo.setTerminalAudioOutputId(terminalPhysicalScreenPo.getTerminalAudioOutputId());
						businessGroupMemberTerminalScreenPo.setX(terminalPhysicalScreenPo.getX());
						businessGroupMemberTerminalScreenPo.setY(terminalPhysicalScreenPo.getY());
						businessGroupMemberTerminalScreenPo.setWidth(terminalPhysicalScreenPo.getWidth());
						businessGroupMemberTerminalScreenPo.setHeight(terminalPhysicalScreenPo.getHeight());
						
						screens.add(businessGroupMemberTerminalScreenPo);
					}
					member.overWriteSetScreen(screens);
					
					//构造BusinessGroupMemberTerminalAudioOutputPO音频输出
					List<TerminalAudioOutputPO> terminalAudioOutputPoList = terminalAudioOutputDao.findByTerminalId(member.getTerminalId());
					for(TerminalAudioOutputPO terminalAudioOutputPo :terminalAudioOutputPoList){
						BusinessGroupMemberTerminalAudioOutputPO businessGroupMemberTerminalAudioOutputPo = new BusinessGroupMemberTerminalAudioOutputPO();
						businessGroupMemberTerminalAudioOutputPo.setMember(member);
	//					businessGroupMemberTerminalAudioOutputPo.setChannels();
						businessGroupMemberTerminalAudioOutputPo.setScreens(new ArrayList<BusinessGroupMemberTerminalScreenPO>());
						
						businessGroupMemberTerminalAudioOutputPo.setTerminalAudioOutputId(terminalAudioOutputPo.getId());
						businessGroupMemberTerminalAudioOutputPo.setTerminalId(terminalAudioOutputPo.getTerminalId());
						businessGroupMemberTerminalAudioOutputPo.setName(terminalAudioOutputPo.getName());
						
						audioOutputs.add(businessGroupMemberTerminalAudioOutputPo);
					}
					
					member.overWriteSetAudioOutputs(audioOutputs);
					
					businessGroupMemberTerminalBundleDao.save(terminalBundles);
					businessGroupMemberDao.save(member);
					
					//终端通道与终端设备通道的对应关系
					List<TerminalChannelBundleChannelPermissionPO> terminalChannelBundleChannelPermissionPoList = terminalChannelBundleChannelPermissionDao.findByTerminalId(member.getTerminalId());
					for(TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPo : terminalChannelBundleChannelPermissionPoList){
						BusinessGroupMemberTerminalBundleChannelPO businessGroupMemberTerminalBundleChannelPo = null;
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = null;
						for(BusinessGroupMemberTerminalBundleChannelPO terminalBundleChannelRelated : totalMemberTerminalBundleChannels){
							if(terminalChannelBundleChannelPermissionPo.getTerminalBundleId().equals(terminalBundleChannelRelated.getTerminalBundleId())
									&& terminalChannelBundleChannelPermissionPo.getTerminalBundleChannelId().equals(terminalBundleChannelRelated.getTerminalBundleChannelId())){
								businessGroupMemberTerminalBundleChannelPo = terminalBundleChannelRelated;
								break;
							}
						}
						
						for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelRelated : terminalChannels){
							if(terminalChannelBundleChannelPermissionPo.getTerminalChannelId().equals(businessGroupMemberTerminalChannelRelated.getTerminalChannelId())){
								businessGroupMemberTerminalChannelPo = businessGroupMemberTerminalChannelRelated;
								break;
							}
						}
						
						if(businessGroupMemberTerminalBundleChannelPo != null && businessGroupMemberTerminalChannelPo != null){
							businessGroupMemberTerminalBundleChannelPo.setMemberTerminalChannel(businessGroupMemberTerminalChannelPo);
							businessGroupMemberTerminalChannelPo.getMemberTerminalBundleChannels().add(businessGroupMemberTerminalBundleChannelPo);
						}
					}
					
					//物理屏幕与音频输出的对应关系
					for(BusinessGroupMemberTerminalScreenPO businessGroupMemberTerminalScreenRelated : screens){
						for(BusinessGroupMemberTerminalAudioOutputPO businessGroupMemberTerminalAudioOutputRelated : member.getAudioOutputs()){
							if(businessGroupMemberTerminalScreenRelated.getTerminalAudioOutputId().equals(businessGroupMemberTerminalAudioOutputRelated.getTerminalAudioOutputId())){
								businessGroupMemberTerminalScreenRelated.setAudioOutput(businessGroupMemberTerminalAudioOutputRelated);
								businessGroupMemberTerminalAudioOutputRelated.getScreens().add(businessGroupMemberTerminalScreenRelated);
							}
						}
					}
					
					//音频输出与终端通道的关系
					List<Long> terminalChannelIds = terminalChannels.stream().map(BusinessGroupMemberTerminalChannelPO::getTerminalChannelId).collect(Collectors.toList());
					List<Long> audioOutputIds = member.getAudioOutputs().stream().map(BusinessGroupMemberTerminalAudioOutputPO::getTerminalAudioOutputId).collect(Collectors.toList());
					List<TerminalAudioOutputChannelPermissionPO> terminalAudioOutputChannelPermissionPoList = 
							terminalAudioOutputChannelPermissionDao.findByTerminalAudioChannelIdInAndTerminalAudioOutputIdIn(terminalChannelIds, audioOutputIds);
					
					for(TerminalAudioOutputChannelPermissionPO terminalAudioOutputChannelPermissionPo : terminalAudioOutputChannelPermissionPoList){
						BusinessGroupMemberTerminalAudioOutputPO businessGroupMemberTerminalAudioOutputPo = null;
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = null;
						for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPoRelated : terminalChannels){
							if(terminalAudioOutputChannelPermissionPo.getTerminalAudioChannelId().equals(businessGroupMemberTerminalChannelPoRelated.getTerminalChannelId())){
								businessGroupMemberTerminalChannelPo = businessGroupMemberTerminalChannelPoRelated;
								break;
							}
						}
						
						for(BusinessGroupMemberTerminalAudioOutputPO businessGroupMemberTerminalAudioOutputPoRelated: member.getAudioOutputs()){
							if(terminalAudioOutputChannelPermissionPo.getTerminalAudioOutputId().equals(businessGroupMemberTerminalAudioOutputPoRelated.getTerminalAudioOutputId())){
								businessGroupMemberTerminalAudioOutputPo = businessGroupMemberTerminalAudioOutputPoRelated;
								break;
							}
						}
						
						if(businessGroupMemberTerminalAudioOutputPo != null && businessGroupMemberTerminalChannelPo != null){
							if(businessGroupMemberTerminalAudioOutputPo.getChannels() == null){
								businessGroupMemberTerminalAudioOutputPo.setChannels(new ArrayList<BusinessGroupMemberTerminalChannelPO>());
							}
							businessGroupMemberTerminalAudioOutputPo.getChannels().add(businessGroupMemberTerminalChannelPo);
							businessGroupMemberTerminalChannelPo.setAudioOutput(businessGroupMemberTerminalAudioOutputPo);
						}
					}
				
					//物理屏幕与终端通道的对应关系
					List<Long> screenIdList = screens.stream().map(BusinessGroupMemberTerminalScreenPO::getTerminalPhysicalScreenId).collect(Collectors.toList());
					List<TerminalPhysicalScreenChannelPermissionPO> terminalPhysicalScreenChannelPermissionPoList = terminalPhysicalScreenChannelPermissionDao.findByTerminalPhysicalScreenIdIn(screenIdList);
					for(TerminalPhysicalScreenChannelPermissionPO terminalPhysicalScreenChannelPermissionPo:terminalPhysicalScreenChannelPermissionPoList){
						BusinessGroupMemberTerminalScreenPO businessGroupMemberTerminalScreenPo = null;
						BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPo = null;
						for(BusinessGroupMemberTerminalScreenPO businessGroupMemberTerminalScreenRelated:screens){
							if(terminalPhysicalScreenChannelPermissionPo.getTerminalPhysicalScreenId().equals(businessGroupMemberTerminalScreenRelated.getTerminalPhysicalScreenId())){
								businessGroupMemberTerminalScreenPo = businessGroupMemberTerminalScreenRelated;
								break;
							}
						}
						
						for(BusinessGroupMemberTerminalChannelPO businessGroupMemberTerminalChannelPoRelated:terminalChannels){
							if(terminalPhysicalScreenChannelPermissionPo.getTerminalChannelId().equals(businessGroupMemberTerminalChannelPoRelated.getTerminalChannelId())){
								businessGroupMemberTerminalChannelPo = businessGroupMemberTerminalChannelPoRelated;
								break;
							}
						}
						
						if(businessGroupMemberTerminalScreenPo != null && businessGroupMemberTerminalChannelPo != null){
							businessGroupMemberTerminalChannelPo.setScreen(businessGroupMemberTerminalScreenPo);
							businessGroupMemberTerminalScreenPo.getChannels().add(businessGroupMemberTerminalChannelPo);
						}
						
					}
				}
			}
			
//			businessGroupMemberDao.save(member);
		}
		
	}

	//TODO:
	public List<MemberTerminalBO> generateMemberTerminalBOs(Long groupId, List<Long> userIdList, List<Long> hallIds, List<String> bundleIdList) throws Exception{
		
		if(userIdList == null) userIdList = new ArrayList<Long>();
		if(hallIds == null) hallIds = new ArrayList<Long>();
		if(bundleIdList == null) bundleIdList = new ArrayList<String>();
		
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		
		//用户
		List<MemberTerminalBO> memberTerminalBOs = new ArrayList<MemberTerminalBO>();
		for(Long userId : userIdList){
			MemberTerminalBO memberBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_USER)
					.setOriginId(userId.toString())
					.setTerminalId(terminal.getId());
			memberTerminalBOs.add(memberBO);
		}
		
		//设备
		List<BundlePO> bundlePOs = bundleDao.findByBundleIdIn(bundleIdList);
		List<ConferenceHallPO> bundleHalls = conferenceHallService.bundleExchangeToHall(bundlePOs);
		for(ConferenceHallPO hall : bundleHalls){
			MemberTerminalBO memberBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_DEVICE)
					.setOriginId(hall.getId().toString())
					.setTerminalId(hall.getTerminalId())
					.setFromDevice(hall.getFromDevice());
			memberTerminalBOs.add(memberBO);
		}		

		//会场
		List<ConferenceHallPO> halls = conferenceHallDao.findAll(hallIds);
		for(ConferenceHallPO hall : halls){
			MemberTerminalBO memberBO = new MemberTerminalBO()
					.setGroupMemberType(GroupMemberType.MEMBER_HALL)
					.setOriginId(hall.getId().toString())
					.setTerminalId(hall.getTerminalId());
			memberTerminalBOs.add(memberBO);
		}
		
		return memberTerminalBOs;
	}

	@Autowired
	TerminalAudioOutputChannelPermissionDAO terminalAudioOutputChannelPermissionDAO;
	/**
	 * 为会议创建成员<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 下午3:20:30
	 * @param group
	 * @param memberTerminalBOs
	 * @param chairmanBO 用来把主席的memberPO标识为isAdministrator，如果memberTerminalBOs中没有主席，则可以为null
	 * @return
	 */
	public List<BusinessGroupMemberPO> generateMembers(GroupPO group, List<MemberTerminalBO> memberTerminalBOs, MemberTerminalBO chairmanBO) throws Exception{
		return generateMembers(group, memberTerminalBOs, chairmanBO, true);
	}
	public List<BusinessGroupMemberPO> generateMembers(GroupPO group, List<MemberTerminalBO> memberTerminalBOs, MemberTerminalBO chairmanBO, boolean relateMembersToGroup) throws Exception{
		
		List<Long> userIdList = new ArrayList<Long>();
		List<Long> bundleHallIdList = new ArrayList<Long>();
		List<Long> hallIdList = new ArrayList<Long>();
		for(MemberTerminalBO userTerminalBO : memberTerminalBOs){
			GroupMemberType groupMemberType = userTerminalBO.getGroupMemberType();
			if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
				userIdList.add(Long.parseLong(userTerminalBO.getOriginId()));
			}else if(GroupMemberType.MEMBER_HALL.equals(groupMemberType)){
				hallIdList.add(Long.parseLong(userTerminalBO.getOriginId()));
			}else if(GroupMemberType.MEMBER_DEVICE.equals(groupMemberType)){
				bundleHallIdList.add(Long.valueOf(userTerminalBO.getOriginId()));
			}
		}
		
		List<TerminalBundleConferenceHallPermissionPO> bundleHallPermissionList = terminalBundleConferenceHallPermissionDao.findByConferenceHallIdIn(bundleHallIdList);
		List<String> memberBundleIds = bundleHallPermissionList.stream().map(TerminalBundleConferenceHallPermissionPO::getBundleId).collect(Collectors.toList());
		List<BundlePO> memberBundles = resourceBundleDao.findByBundleIds(memberBundleIds);
		Map<String, BundlePO> hallIdAndBundleMap = hallIdAndBundleIdMap(bundleHallPermissionList, memberBundles);
		
		String userIdListStr = StringUtils.join(userIdList.toArray(), ",");
		List<UserBO> memberUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		if(memberUserBos == null) memberUserBos = new ArrayList<UserBO>();
		List<ConferenceHallPO> halls = conferenceHallDao.findAll(hallIdList);
		List<FolderPO> allFolders = resourceService.queryAllFolders();
		
		List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(userIdList);
		
		Map<Long, Integer> userIdAndLevelMap = userIdAndLevelMap(folderUserMaps);
		
		//从List<UserBO>取出bundleId列表，注意判空；给UserBO中的folderId赋值
		for(UserBO user : memberUserBos){
			for(FolderPO folder : allFolders){
				if(folder.getUuid().equals(user.getFolderUuid())){
					user.setFolderId(folder.getId());
					user.setLevel(userIdAndLevelMap.get(user.getId()));
					break;
				}
			}
		}
		
		List<BusinessGroupMemberPO> memberList = new ArrayList<BusinessGroupMemberPO>();
		
		for(MemberTerminalBO memberTerminalBO : memberTerminalBOs){
			BusinessGroupMemberPO businessGroupMemberPo = new BusinessGroupMemberPO();
			
			if(memberTerminalBO.equals(chairmanBO)){
				businessGroupMemberPo.setIsAdministrator(true);
				//关联主席角色
			}else{
				//关联会议员角色
			}
						
			GroupMemberType groupMemberType = memberTerminalBO.getGroupMemberType();
			businessGroupMemberPo.setGroupMemberType(groupMemberType);
			businessGroupMemberPo.setOriginId(memberTerminalBO.getOriginId());
			businessGroupMemberPo.setTerminalId(memberTerminalBO.getTerminalId());
			if(memberTerminalBO.getTerminalId() != null){
				TerminalPO terminal = terminalDao.findOne(memberTerminalBO.getTerminalId());
				if(terminal != null){
					businessGroupMemberPo.setTerminalname(terminal.getName());
					businessGroupMemberPo.setTerminalType(terminal.getType());
				}
			}
			
			if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
				UserBO user = queryUtil.queryUserById(memberUserBos, Long.parseLong(memberTerminalBO.getOriginId()));
				if(user == null) continue;
				businessGroupMemberPo.setName(user.getName());
				businessGroupMemberPo.setCode(user.getUserNo());
				businessGroupMemberPo.setFolderId(user.getFolderId());
				businessGroupMemberPo.setLevel(user.getLevel());
				if(queryUtil.isLdapUser(user, folderUserMaps)){
					businessGroupMemberPo.setOriginType(OriginType.OUTER);
				}				
			}else if(GroupMemberType.MEMBER_HALL.equals(groupMemberType)){
				ConferenceHallPO hall = queryUtil.queryHallById(halls, Long.parseLong(memberTerminalBO.getOriginId()));
				if(hall == null) continue;
				businessGroupMemberPo.setName(hall.getName());
				businessGroupMemberPo.setCode("halldefault");
				businessGroupMemberPo.setFolderId(hall.getFolderId());
				businessGroupMemberPo.setFromDevice(hall.getFromDevice());
//				businessGroupMemberPo.setTerminalType(terminalType);
				
			}else if(GroupMemberType.MEMBER_DEVICE.equals(groupMemberType)){
				BundlePO bundle = hallIdAndBundleMap.get(memberTerminalBO.getOriginId());
//						queryUtil.queryBundlePOByBundleId(memberBundles, memberTerminalBO.getOriginId());
				if(bundle == null) continue;
				businessGroupMemberPo.setName(bundle.getBundleName());
				if(!BaseUtils.stringIsNotBlank(bundle.getUsername())){
					throw new BaseException(StatusCode.FORBIDDEN, bundle.getBundleName()+" 没有设备号码");
				}
				businessGroupMemberPo.setCode(bundle.getUsername());
				businessGroupMemberPo.setFolderId(bundle.getFolderId());
				businessGroupMemberPo.setBundleId(bundle.getBundleId());
				if(queryUtil.isLdapBundle(bundle)){
					businessGroupMemberPo.setOriginType(OriginType.OUTER);
				}
			}
			
			if(relateMembersToGroup){
				businessGroupMemberPo.setGroup(group);
				if(group.getMembers() == null){
					group.setMembers(new ArrayList<BusinessGroupMemberPO>());
				}
				group.getMembers().add(businessGroupMemberPo);
			}
			memberList.add(businessGroupMemberPo);
		}
		
		return memberList;
	}
	
	/**
	 * 给所有本系统的成员，没有PageInfoPO的，建立PageInfoPO<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月10日 上午9:41:07
	 * @param memberTerminalBOs
	 */
	@Deprecated
	public void generateMembersPageInfo_deprected(List<MemberTerminalBO> memberTerminalBOs){
		
		//查出所有可能的成员的PageInfoPO
		Set<String> originIds = new HashSet<String>();
		for(MemberTerminalBO memberTerminalBO : memberTerminalBOs){
			originIds.add(memberTerminalBO.getOriginId());
		}
		List<PageInfoPO> pageInfos = pageInfoDAO.findByOriginIdIn(originIds);
		
		//给没有PageInfoPO新建，保存
		List<PageInfoPO> newPageInfos = new ArrayList<PageInfoPO>(); 
		for(MemberTerminalBO memberTerminalBO : memberTerminalBOs){
			PageInfoPO pageInfo = tetrisBvcQueryUtil.queryPageInfoByOriginIdAndTerminalId(
					pageInfos, memberTerminalBO.getOriginId(), memberTerminalBO.getTerminalId());
			if(pageInfo == null){
				pageInfo = new PageInfoPO(memberTerminalBO.getOriginId(), memberTerminalBO.getTerminalId(), memberTerminalBO.getGroupMemberType());
				newPageInfos.add(pageInfo);
			}
		}
		pageInfoDAO.save(newPageInfos);
	}
	public void generateMembersPageInfo(List<BusinessGroupMemberPO> members){
		
		//查出所有可能的成员的PageInfoPO
		Set<String> originIds = new HashSet<String>();
		for(BusinessGroupMemberPO member : members){
			if(OriginType.INNER.equals(member.getOriginType())){
				originIds.add(member.getOriginId());
			}
		}
		List<PageInfoPO> pageInfos = pageInfoDAO.findByOriginIdIn(originIds);
		
		//给没有PageInfoPO新建，保存
		List<PageInfoPO> newPageInfos = new ArrayList<PageInfoPO>(); 
		for(BusinessGroupMemberPO member : members){
			if(!OriginType.INNER.equals(member.getOriginType())) continue;
			PageInfoPO pageInfo = tetrisBvcQueryUtil.queryPageInfoByOriginIdAndTerminalId(
					pageInfos, member.getOriginId(), member.getTerminalId());
			if(pageInfo == null){
				pageInfo = new PageInfoPO(member.getOriginId(), member.getTerminalId(),
						member.getGroupMemberType().equals(GroupMemberType.MEMBER_DEVICE)?GroupMemberType.MEMBER_HALL:member.getGroupMemberType());
				newPageInfos.add(pageInfo);
			}
		}
		pageInfoDAO.save(newPageInfos);
	}
	
	/**
	 * 重置成员信息<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月31日 上午9:20:00
	 * @param groupId 会议组id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void resetGroupMember(Long groupId) throws Exception{

		//将会议组成员以及会议组成员下的layerId和folderId刷新。通过会议组成员中标志字段查找成员的来源，更新会议组成员对应字段。不考虑设备作为成员的情况。
		GroupPO group = groupDao.findOne(groupId);
		
		if(group == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到会议组");
		}
		
		if(!group.getStatus().equals(GroupStatus.STOP)){
			throw new BaseException(StatusCode.FORBIDDEN, "请先停止会议刷新成员");
		}
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<BusinessGroupMemberPO> userMembers = new ArrayList<BusinessGroupMemberPO>();
		List<BusinessGroupMemberPO> hallMembers = new ArrayList<BusinessGroupMemberPO>();
		
		for(BusinessGroupMemberPO member : members){
			if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
				userMembers.add(member);
			}else if(GroupMemberType.MEMBER_HALL.equals(member.getGroupMemberType())
					|| GroupMemberType.MEMBER_DEVICE.equals(member.getGroupMemberType())){
				hallMembers.add(member);
			}
		}
		
		Map<InternalRoleType, RolePO> roleTypeAndRoleMap = roleDao.findAll().stream().collect(Collectors.toMap(r-> r.getInternalRoleType(), r->r));
		
		//用户的处理
		String userIdListStr = userMembers.stream().map(BusinessGroupMemberPO::getOriginId).collect(Collectors.joining(","));
		List<UserBO> memberUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		if(memberUserBos == null) memberUserBos = new ArrayList<UserBO>();
		List<FolderPO> allFolders = resourceService.queryAllFolders();
		List<Long> userIdList = userMembers.stream().map(member ->{
			return Long.valueOf(member.getOriginId());
		}).collect(Collectors.toList());
		List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(userIdList);
		Map<Long, Integer> userIdAndLevelMap = userIdAndLevelMap(folderUserMaps);
		for(UserBO user : memberUserBos){
			for(FolderPO folder : allFolders){
				if(folder.getUuid().equals(user.getFolderUuid())){
					user.setFolderId(folder.getId());
					user.setLevel(userIdAndLevelMap.get(user.getId()));
					break;
				}
			}
		}
		
		for(BusinessGroupMemberPO userMember : userMembers){
			UserBO user = queryUtil.queryUserById(memberUserBos, Long.parseLong(userMember.getOriginId()));
			if(user == null) continue;
			userMember.setFolderId(user.getFolderId());
			if(BusinessType.COMMAND.equals(group.getBusinessType())){
				userMember.setLevel(user.getLevel());
				InternalRoleType roleType = InternalRoleType.fromLevel(String.valueOf(user.getLevel()));
				RolePO role = roleTypeAndRoleMap.get(roleType);
				userMember.setRoleId(role.getId());
				userMember.setRoleName(role.getName());
			}
		}
		
		List<Long> hallIdList = hallMembers.stream().map(BusinessGroupMemberPO::getOriginId).map(Long::valueOf).collect(Collectors.toList());
		List<ConferenceHallPO> hallList = conferenceHallDao.findAll(hallIdList);
		//会场的处理
		for(BusinessGroupMemberPO hallMember : hallMembers){
			ConferenceHallPO hall = queryUtil.queryHallById(hallList, Long.parseLong(hallMember.getOriginId()));
			if(hall == null) continue;
			hallMember.setFolderId(hall.getFolderId());
		}
		
		//成员下设备处理
		List<String> memberBundleIds = 
				members.stream().flatMap(member -> member.getTerminalBundles().stream()).map(BusinessGroupMemberTerminalBundlePO::getBundleId).collect(Collectors.toList());
		
		Map<String, BundlePO> bundleIdAndBundleMap = bundleDao.findByBundleIdIn(memberBundleIds).stream().collect(Collectors.toMap(BundlePO::getBundleId, Function.identity()));
		for(BusinessGroupMemberPO member : members){
			if(member.getTerminalBundles() == null) continue;
			for(BusinessGroupMemberTerminalBundlePO terminalBundle :member.getTerminalBundles()){
				BundlePO bundle = bundleIdAndBundleMap.get(terminalBundle.getBundleId());
				if(bundle == null) continue;
				terminalBundle.setFolderId(bundle.getFolderId());
				terminalBundle.setLayerId(bundle.getAccessNodeUid());
			}
		}
		
		businessGroupMemberDao.save(members);
		log.info("数据刷新成功");
	}
	
	/**
	 * 刷新成员信息(删除重建)<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月31日 上午9:20:00
	 * @param groupId 会议组id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void refreshGroupMember(Long groupId)throws Exception{
		
		//找到会议组成员，将除成员级联的信息，以及配置出来的CustomAudioPO，AgendaForwardSourcePO信息删除。
		GroupPO group = groupDao.findOne(groupId);
		
		if(group == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到会议组");
		}
		
		if(!group.getStatus().equals(GroupStatus.STOP)){
			throw new BaseException(StatusCode.FORBIDDEN, "请先停止会议刷新成员");
		}
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		for(BusinessGroupMemberPO member : members){
			member.getTerminalBundles().clear();
			member.getScreens().clear();
			member.getAudioOutputs().clear();
			member.getChannels().clear();
		}
		
		businessGroupMemberDao.save(members);
		fullfillGroupMember(members);
		
		//查找会议组对应所有议程的所有音视频源配置数据删除。
		//自定义议程的视频源只与议程转发有关联
		List<Long> allAgendaIdList = agendaDao.findByBusinessId(groupId).stream().map(AgendaPO :: getId).collect(Collectors.toList());
		List<Long> allAgendaForwardIdList = agendaForwardDao.findByAgendaIdIn(allAgendaIdList).stream().map(AgendaForwardPO :: getId).collect(Collectors.toList());
		List<AgendaForwardSourcePO> agendaForwardSourceList = agendaForwardSourceDao.findByAgendaForwardIdIn(allAgendaForwardIdList);
		agendaForwardSourceDao.deleteInBatch(agendaForwardSourceList);
		//自定义议程的音频源可能有议程以及议程转发的
		List<CustomAudioPO> customAudioOfAgenda = customAudioDao.findByPermissionIdInAndPermissionType(allAgendaIdList, CustomAudioPermissionType.AGENDA);
		List<CustomAudioPO> customAudioOfAgendaForward = customAudioDao.findByPermissionIdInAndPermissionType(allAgendaForwardIdList, CustomAudioPermissionType.AGENDA_FORWARD);
		
		customAudioDao.deleteInBatch(customAudioOfAgenda);
		customAudioDao.deleteInBatch(customAudioOfAgendaForward);
		
		log.info("数据刷新成功");
	}
	
	/**
	 * 刷新成员以及成员下的[folderId、layerId]字段<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月31日 上午9:20:00
	 * @param groupId 会议组id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void refreshGroupMemberFields(Long groupId)throws Exception{
		
		//将会议组成员以及会议组成员下的layerId和folderId刷新。通过会议组成员中标志字段查找成员的来源，更新会议组成员对应字段。不考虑设备作为成员的情况。
		GroupPO group = groupDao.findOne(groupId);
		
		if(group == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到会议组");
		}
		
		if(!group.getStatus().equals(GroupStatus.STOP)){
			throw new BaseException(StatusCode.FORBIDDEN, "请先停止会议刷新成员");
		}
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<BusinessGroupMemberPO> userMembers = new ArrayList<BusinessGroupMemberPO>();
		List<BusinessGroupMemberPO> hallMembers = new ArrayList<BusinessGroupMemberPO>();
		
		for(BusinessGroupMemberPO member : members){
			if(GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
				userMembers.add(member);
			}else if(GroupMemberType.MEMBER_HALL.equals(member.getGroupMemberType())){
				hallMembers.add(member);
			}
		}
		
		//用户的处理
		String userIdListStr = userMembers.stream().map(BusinessGroupMemberPO::getOriginId).collect(Collectors.joining(","));
		List<UserBO> memberUserBos = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		if(memberUserBos == null) memberUserBos = new ArrayList<UserBO>();
		for(BusinessGroupMemberPO userMember : userMembers){
			UserBO user = queryUtil.queryUserById(memberUserBos, Long.parseLong(userMember.getOriginId()));
			if(user == null) continue;
			userMember.setFolderId(user.getFolderId());
		}
		
		List<Long> hallIdList = hallMembers.stream().map(BusinessGroupMemberPO::getOriginId).map(Long::valueOf).collect(Collectors.toList());
		List<ConferenceHallPO> hallList = conferenceHallDao.findAll(hallIdList);
		//会场的处理
		for(BusinessGroupMemberPO hallMember : hallMembers){
			ConferenceHallPO hall = queryUtil.queryHallById(hallList, Long.parseLong(hallMember.getOriginId()));
			if(hall == null) continue;
			hallMember.setFolderId(hall.getFolderId());
		}
		
		//成员下设备处理
		List<String> memberBundleIds = 
				members.stream().flatMap(member -> member.getTerminalBundles().stream()).map(BusinessGroupMemberTerminalBundlePO::getBundleId).collect(Collectors.toList());
		
		Map<String, BundlePO> bundleIdAndBundleMap = bundleDao.findByBundleIdIn(memberBundleIds).stream().collect(Collectors.toMap(BundlePO::getBundleId, Function.identity()));
		for(BusinessGroupMemberPO member : members){
			if(member.getTerminalBundles() == null) continue;
			for(BusinessGroupMemberTerminalBundlePO terminalBundle :member.getTerminalBundles()){
				BundlePO bundle = bundleIdAndBundleMap.get(terminalBundle.getBundleId());
				if(bundle == null) continue;
				terminalBundle.setFolderId(bundle.getFolderId());
				terminalBundle.setLayerId(bundle.getAccessNodeUid());
			}
		}
		
		log.info("数据刷新成功");
	}
	
	/**
	 * 批量处理成员的“接听”和“拒绝”<br/>
	 * <p>注意不能选择自己看自己的播放器，例如主席看主席</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:13:43
	 * @param group 业务组
	 * @param allMembers 全体成员
	 * @param acceptMembers 接受的成员
	 */
	public void membersResponse(
			GroupPO group, 
			List<BusinessGroupMemberPO> allMembers, 
			List<BusinessGroupMemberPO> acceptMembers) throws Exception{
		
		if(null == acceptMembers) acceptMembers = new ArrayList<BusinessGroupMemberPO>();
		List<Long> consumeIds = new ArrayList<Long>();
		List<MessageSendCacheBO> messageCaches = new ArrayList<MessageSendCacheBO>();
		
		//考虑如果停会之后执行，有没有问题
		
		//判断是否在进行
		if(GroupStatus.STOP.equals(group.getStatus())) {
			return;
		}
		
		//处理同意用户，呼叫转发目标成员的播放器
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(allMembers);
		List<Long> acceptMemberIds = new ArrayList<Long>();
//		List<CommandGroupUserPlayerPO> allPlayers = new ArrayList<CommandGroupUserPlayerPO>();
		
		List<String> acceptMemberNamesList = businessCommonService.obtainMemberNames(acceptMembers);
		
		//自动接听：给新进的人发消息，通知其开会。在会议开启时，也会给主席发，通知split信息。【专向指挥除外】
		if(GroupService.autoEnter && !group.getBusinessType().equals(GroupType.SECRET)){
			for(BusinessGroupMemberPO acceptMember : acceptMembers){
				
				if(OriginType.OUTER.equals(acceptMember.getOriginType())){
					continue;
				}
				
				if(!GroupMemberType.MEMBER_USER.equals(acceptMember.getGroupMemberType())){
					continue;
				}
				
				JSONObject message = new JSONObject();
				message.put("id", group.getId());
				message.put("name", group.getName());
				message.put("status", group.getStatus().getCode());
				message.put("commander", chairmanMember.getOriginId());
				message.put("creator", chairmanMember.getOriginId());
				message.put("splits", new JSONArray());
				message.put("businessId", group.getId().toString());
				String businessType = null;
				if(BusinessType.MEETING_QT.equals(group.getBusinessType())
						|| BusinessType.MEETING_BVC.equals(businessType)){
					businessType = "meetingStartNow";//自动接听
				}else{
					businessType = "commandStartNow";//自动接听
				}
				message.put("businessType", businessType);
				message.put("businessInfo", group.getName() + " 开始了，主席：" + chairmanMember.getName());
				messageCaches.add(new MessageSendCacheBO(Long.parseLong(acceptMember.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			}
		}		
		
		//给已经进会的人发送消息
		String acceptMemberNames = StringUtils.join(acceptMemberNamesList.toArray(), ",");
		if(acceptMembers.size() > 0){
			for(BusinessGroupMemberPO member : allMembers){
				
				if(OriginType.OUTER.equals(member.getOriginType())){
					continue;
				}
				
				if(!GroupMemberStatus.CONNECT.equals(member.getGroupMemberStatus())){
					continue;
				}
				
				if(!GroupMemberType.MEMBER_USER.equals(member.getGroupMemberType())){
					continue;
				}
				
				//接听成员有主席时（通常是开启会议时），不给主席发
				if(acceptMemberIds.contains(chairmanMember.getId()) && member.getIsAdministrator()){
					continue;
				}
				
				JSONObject message = new JSONObject();
				message.put("businessType", "commandMemberOnline");
				message.put("businessId", group.getId().toString());
				message.put("businessInfo", acceptMemberNames + " 进入" + group.getName());
				message.put("splits", new JSONArray());
				messageCaches.add(new MessageSendCacheBO(Long.parseLong(member.getOriginId()), message.toJSONString(), WebsocketMessageType.COMMAND));
			}
		}
		
		//发完消息再把成员状态置为CONNECT
		for(BusinessGroupMemberPO acceptMember : acceptMembers){
			acceptMember.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		}		
		businessGroupMemberDao.save(acceptMembers);
		
//		//查询接听用户的转发：源和目的成员都CONNECT的，且状态UNDONE的，生成logic.forwardSet
//		Set<CommandGroupForwardPO> relativeForwards = commandCommonUtil.queryForwardsByMemberIds(forwards, acceptMemberIds, null, ExecuteStatus.UNDONE);
//		Set<CommandGroupForwardPO> needForwards = commandCommonUtil.queryForwardsReadyAndCanBeDone(members, relativeForwards);
//		
//		for(CommandGroupForwardPO needForward : needForwards){
//			needForward.setExecuteStatus(ExecuteStatus.DONE);
//		}
//		
//		commandGroupDao.save(group);
		
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		DeviceGroupAvtplPO deviceGroupAvtplPO = group.getAvtpl();
		LogicBO logic = openEncoder(acceptMembers, deviceGroupAvtplPO, -1L, chairmanMember.getCode());
		
		//执行logic，打开编码通道
		if(businessReturnService.getSegmentedExecute()){
			businessReturnService.add(logic, null, null);
		}else{
			executeBusiness.execute(logic, group.getName() + " 会议成员进会，打开编码");
		}
		
		//生成connectBundle和disconnectBundle，携带转发信息
//		CommandGroupAvtplGearsPO currentGear = commandCommonUtil.queryCurrentGear(group);
//		CodecParamBO codec = new CodecParamBO().set(group.getAvtpl(), currentGear);
//		LogicBO logic = openBundle(acceptMembers, null, allPlayers, needForwards, null, codec, chairmanMember.getUserNum());
//		LogicBO logicDis = closeBundle(null, null, needFreePlayers, codec, chairmanMember.getUserNum());
//		LogicBO logicCastDevice = commandCastServiceImpl.openBundleCastDevice(null, null, needForwards, null, null, null, codec, group.getUserId());
//		logic.merge(logicDis);
//		logic.merge(logicCastDevice);
		
		//授予角色
//		RolePO memberRole = businessCommonService.queryGroupMemberRole(group);
		List<GroupMemberRolePermissionPO> ps = new ArrayList<GroupMemberRolePermissionPO>();
		for(BusinessGroupMemberPO acceptMember : acceptMembers){
//			List<Long> addRoleIds = new ArrayListWrapper<Long>().add(memberRole.getId()).getList();
			
			/*//为主席添加观众角色。角色与成员一对一，就不需要
			if(acceptMember.getIsAdministrator()){
				RolePO chairmanRolePO = businessCommonService.queryGroupChairmanRole(group);
				addRoleIds.add(chairmanRolePO.getId());
			}*/
			
			//主席成员只有主席角色
			if(acceptMember.getIsAdministrator()){
//				RolePO chairmanRolePO = businessCommonService.queryGroupChairmanRole(group);
//				agendaExecuteService.modifySoleMemberRole(group.getId(), acceptMember.getId(), chairmanRolePO.getId(), false, false);
			}else{
//				agendaExecuteService.modifySoleMemberRole(group.getId(), acceptMember.getId(), memberRole.getId(), false, false);
				//置为CONNECT，绑定观众角色
				if(acceptMember.getRoleId() == null){
					acceptMember.setGroupMemberStatus(GroupMemberStatus.CONNECT);
//					acceptMember.setRoleId(memberRole.getId());
//					acceptMember.setRoleName(memberRole.getName());
//					GroupMemberRolePermissionPO memberRolePermission = new GroupMemberRolePermissionPO(memberRole.getId(), acceptMember.getId());
//					groupMemberRolePermissionDao.save(memberRolePermission);
				}
			}
		}
//		agendaExecuteService.executeToFinal(group.getId());
		groupDao.save(group);
		
		
		//停止其它业务观看专向会议的2个成员，在 CommandSecretServiceImpl.accept() 中
		
		//录制更新
//		LogicBO logicRecord = commandRecordServiceImpl.update(group.getUserId(), group, 1, false);
//		logic.merge(logicRecord);
//		
//		ExecuteBusinessReturnBO returnBO = executeBusiness.execute(logic, group.getName() + " 会议成员接听和拒绝");
//		commandRecordServiceImpl.saveStoreInfo(returnBO, group.getId());

		//发消息
		if(businessReturnService.getSegmentedExecute()){
			for(MessageSendCacheBO cache : messageCaches){
				businessReturnService.add(null, cache, null);
			}
		}else{
			for(MessageSendCacheBO cache : messageCaches){
				WebsocketMessageVO ws = websocketMessageService.send(cache.getUserId(), cache.getMessage(), cache.getType(), cache.getFromUserId(), cache.getFromUsername());
				consumeIds.add(ws.getId());
			}
			websocketMessageService.consumeAll(consumeIds);
		}
		
	}

	/**
	 * 打开编码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月20日 下午3:23:26
	 * @param group
	 * @param codec
	 * @param userId
	 * @param chairmanUserNum 主席的号码
	 * @return
	 * @throws Exception
	 */
	public LogicBO openEncoder(
			List<BusinessGroupMemberPO> groupMemberList,
			DeviceGroupAvtplPO deviceGroupAvtplPO,
			Long userId,
			String chairmanUserNum) throws Exception{
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
				 			 		 .setPass_by(new ArrayList<PassByBO>());

		List<DeviceGroupAvtplGearsPO> gears = deviceGroupAvtplPO.getGears();
		Map<ChannelParamsType,DeviceGroupAvtplGearsPO> gearsMap = gears.stream().
				collect(Collectors.toMap(DeviceGroupAvtplGearsPO::getChannelParamsType,(p)->p));
		
		//媒体转发等特殊业务，通过member的businessId找到group
		GroupPO group = findGroupByBusinessId(groupMemberList);
		
		for(BusinessGroupMemberPO groupMember : groupMemberList){
			//本系统
			if(OriginType.INNER.equals(groupMember.getOriginType())){
				//成员用户下的哪些设备有编码通道，就要将对应设备的每个编码通道都呼起来
				for(BusinessGroupMemberTerminalBundlePO terminalBundle : groupMember.getTerminalBundles()){
					
					if(group == null){
						group = groupMember.getGroup();
					}
					PassByBO passBy = new PassByBO().setIncomingCall(group, terminalBundle.getBundleId() , terminalBundle.getLayerId());
					ConnectBundleBO connectEncoderBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
				            													.setOperateType(ConnectBundleBO.OPERATE_TYPE)
				            													.setLock_type("write")
				            													.setBundleId(terminalBundle.getBundleId())
				            													.setLayerId(terminalBundle.getLayerId())
				            													.setBundle_type(terminalBundle.getBundleType())
				            													.setPass_by_str(passBy);
					
					for(BusinessGroupMemberTerminalBundleChannelPO memberTerminalBundleChannel: terminalBundle.getMemberTerminalBundleChannels()){
						//根据编码通道编码类型选择参数呼叫编码器
						DeviceGroupAvtplGearsPO gearsPO = gearsMap.get(memberTerminalBundleChannel.getChannelParamsType());
						if(gearsPO == null){
							gearsPO = gearsMap.get(ChannelParamsType.HD);
						}
						CodecParamBO codec=new CodecParamBO().set(gearsPO);
						if(TerminalBundleChannelType.VIDEO_ENCODE.equals(memberTerminalBundleChannel.getTerminalBundleChannelType())){
							ConnectBO connectEncoderVideoChannel = new ConnectBO().setChannelId(memberTerminalBundleChannel.getChannelId())
								      .setChannel_status("Open")
								      .setBase_type(memberTerminalBundleChannel.getBaseType())
								      .setCodec_param(codec);
							connectEncoderBundle.getChannels().add(connectEncoderVideoChannel);
						}else if(TerminalBundleChannelType.AUDIO_ENCODE.equals(memberTerminalBundleChannel.getTerminalBundleChannelType())){
							ConnectBO connectEncoderAudioChannel = new ConnectBO().setChannelId(memberTerminalBundleChannel.getChannelId())
								      .setChannel_status("Open")
								      .setBase_type(memberTerminalBundleChannel.getBaseType())
								      .setCodec_param(codec);
							if(Boolean.TRUE.equals(terminalBundle.getMulticastEncode())){
								String audioAddr = multicastService.addrAddPort(terminalBundle.getMulticastEncodeAddr(), 4);
								connectEncoderAudioChannel.setMode(TransmissionMode.MULTICAST.getCode()).setMulti_addr(audioAddr).setSrc_multi_ip(terminalBundle.getMulticastSourceIp());
							}
							connectEncoderBundle.getChannels().add(connectEncoderAudioChannel);
						}
					}
					
					if(connectEncoderBundle.getChannels().size()>0){
						logic.getConnectBundle().add(connectEncoderBundle);
					}
				}
			}
			//外部系统
			else{
				GroupMemberType groupMemberType = groupMember.getGroupMemberType();
				
				DeviceGroupAvtplGearsPO gearsPO = gearsMap.get(ChannelParamsType.HD);
				CodecParamBO codec=new CodecParamBO().set(gearsPO);
				
				//外部设备；外部会场（可能没意义）
				if(GroupMemberType.MEMBER_HALL.equals(groupMemberType) || GroupMemberType.MEMBER_DEVICE.equals(groupMemberType)){
					//点播外部设备，passby拉流
					if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
//					BundlePO bundlePO = sourceBO.getVideoBundle();
//					ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//					ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
					XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
										 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
										 .setUuid(groupMember.getUuid())
										 .setSrc_user(chairmanUserNum)//发起人、目的号码
										 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
												 											.put("bundleid", groupMember.getBundleId())
												 											.put("video_channelid", ChannelType.VIDEOENCODE1.getChannelId())
												 											.put("audio_channelid", ChannelType.AUDIOENCODE1.getChannelId())
												 											.getMap())
										 .setDst_number(groupMember.getCode())//被点播、源号码
										 .setVparam(codec);
					
					PassByBO passby = new PassByBO().setLayer_id(localLayerId)
					.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
					.setPass_by_content(passByContent);
					
					logic.getPass_by().add(passby);
				}else if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
					//点播外部用户，passby拉流
					if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
					XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
										 .setOperate(XtBusinessPassByContentBO.OPERATE_START)
										 .setUuid(groupMember.getUuid())
										 .setSrc_user(chairmanUserNum)//发起人、目的号码
										 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
												 											.put("bundleid", groupMember.getUuid())
												 											.put("video_channelid", ChannelType.VIDEOENCODE1.getChannelId())
												 											.put("audio_channelid", ChannelType.AUDIOENCODE1.getChannelId())
												 											.getMap())
										 .setDst_number(groupMember.getCode())//被点播、源号码
										 .setVparam(codec);
					
					PassByBO passby = new PassByBO().setLayer_id(localLayerId)
					.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
					.setPass_by_content(passByContent);
					
					logic.getPass_by().add(passby);
				}
			}
		}
		
		return logic;
	}
	
	/**
	 * 关闭编码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月20日 下午3:23:44
	 * @param group
	 * @param codec
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	//TODO:检索设备或通道是否还在使用
	public LogicBO closeEncoder(
			List<BusinessGroupMemberPO> groupMemberList,
			CodecParamBO codec,
			Long userId,
			String chairmanUserNum) throws Exception{
		
		
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
									 .setMediaPushDel(new ArrayList<MediaPushSetBO>())
									 .setPass_by(new ArrayList<PassByBO>());
		
		GroupPO group = findGroupByBusinessId(groupMemberList);
		
		for(BusinessGroupMemberPO groupMember: groupMemberList){
			//本系统
			if(OriginType.INNER.equals(groupMember.getOriginType())){
				for(BusinessGroupMemberTerminalBundlePO terminalBundle : groupMember.getTerminalBundles()){
					for(BusinessGroupMemberTerminalBundleChannelPO memberTerminalBundleChannel: terminalBundle.getMemberTerminalBundleChannels()){
						if(TerminalBundleChannelType.VIDEO_ENCODE.equals(memberTerminalBundleChannel.getTerminalBundleChannelType())||
								TerminalBundleChannelType.AUDIO_ENCODE.equals(memberTerminalBundleChannel.getTerminalBundleChannelType())){
							BundlePO bundlePO = bundleDao.findByBundleId(terminalBundle.getBundleId());
							
							if(group == null){
								group = groupMember.getGroup();
							}
							
							PassByBO passBy = new PassByBO().setHangUp(group, terminalBundle.getBundleId() , bundlePO.getAccessNodeUid());
							DisconnectBundleBO disconnectEncoderBundle = new DisconnectBundleBO().setBusinessType(DisconnectBundleBO.BUSINESS_TYPE_VOD)
									             .setOperateType(DisconnectBundleBO.OPERATE_TYPE)
									             .setBundleId(terminalBundle.getBundleId())
									             .setBundle_type(bundlePO.getBundleType())
									             .setLayerId(bundlePO.getAccessNodeUid())
									             .setPass_by_str(passBy);
							logic.getDisconnectBundle().add(disconnectEncoderBundle);
							break;
						}
					}
				}
			}
			//外部系统
			else{
				GroupMemberType groupMemberType = groupMember.getGroupMemberType();
				//外部设备；外部会场（可能没意义）
				if(GroupMemberType.MEMBER_DEVICE.equals(groupMemberType) || GroupMemberType.MEMBER_HALL.equals(groupMemberType)){
					if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
//					BundlePO bundlePO = sourceBO.getVideoBundle();
//					ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//					ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
					XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
										 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
										 .setUuid(groupMember.getUuid())
										 .setSrc_user(chairmanUserNum)//发起人、目的号码
										 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
												 											.put("bundleid", groupMember.getBundleId())
												 											.put("video_channelid", ChannelType.VIDEOENCODE1.getChannelId())
												 											.put("audio_channelid", ChannelType.AUDIOENCODE1.getChannelId())
												 											.getMap())
										 .setDst_number(groupMember.getCode())//被点播、源号码
										 .setVparam(codec);
					
					PassByBO passby = new PassByBO().setLayer_id(localLayerId)
					.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER)
					.setPass_by_content(passByContent);
					
					logic.getPass_by().add(passby);
				}else if(GroupMemberType.MEMBER_USER.equals(groupMemberType)){
					if(localLayerId == null) localLayerId = resourceRemoteService.queryLocalLayerId();
//					BundlePO bundlePO = sourceBO.getVideoBundle();
//					ChannelSchemeDTO video = sourceBO.getVideoSourceChannel();
//					ChannelSchemeDTO audio = sourceBO.getAudioSourceChannel();
					XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
										 .setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
										 .setUuid(groupMember.getUuid())
										 .setSrc_user(chairmanUserNum)//发起人、目的号码
										 .setXt_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
												 											.put("bundleid", groupMember.getUuid())
												 											.put("video_channelid", ChannelType.VIDEOENCODE1.getChannelId())
												 											.put("audio_channelid", ChannelType.AUDIOENCODE1.getChannelId())
												 											.getMap())
										 .setDst_number(groupMember.getCode())//被点播、源号码
										 .setVparam(codec);
					
					PassByBO passby = new PassByBO().setLayer_id(localLayerId)
					.setType(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER)
					.setPass_by_content(passByContent);
					
					logic.getPass_by().add(passby);
				}
			}
		}
		
		return logic;
	
	}
	
	/**
	 * 通过businessId的属性查找到GroupPO<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月22日 上午11:20:51
	 * @return
	 */
	private GroupPO findGroupByBusinessId(List<BusinessGroupMemberPO> groupMembers){
		
		BusinessGroupMemberPO groupMember = null;
		for(BusinessGroupMemberPO member : groupMembers){
			if(member.getBusinessId() != null){
				groupMember = member;
				break;
			}
		}
		
		if(groupMember != null){
			GroupPO group = groupDao.findOne(Long.valueOf(groupMember.getBusinessId()));
			return group;
		}
		
		return null;
	}
	
	private Map<String, BundlePO> hallIdAndBundleIdMap(List<TerminalBundleConferenceHallPermissionPO> bundleHallPermissionList, List<BundlePO> memberBundles){
		
		if(bundleHallPermissionList == null || memberBundles == null){
			return null;
		}
		Map<String, BundlePO> idAndBundleMap = memberBundles.stream().collect(Collectors.toMap(BundlePO::getBundleId, Function.identity()));
		Map<String, BundlePO> hallIdAndBundleMap = new HashMap<String, BundlePO>();
		
		for(TerminalBundleConferenceHallPermissionPO p : bundleHallPermissionList){
			BundlePO bundle = idAndBundleMap.get(p.getBundleId());
			if(bundle != null){
				hallIdAndBundleMap.put(p.getId().toString(), bundle);
			}
		}
		
		return hallIdAndBundleMap;
	}
	
	/**
	 * 获取用户id和等级<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月26日 下午4:56:57
	 * @param folderUserMaps 文件夹与用户的map
	 */
	private Map<Long, Integer> userIdAndLevelMap(List<FolderUserMap> folderUserMaps) throws Exception{
		
		List<Long> folderIds = folderUserMaps.stream().map(map -> {
			return map.getFolderId();
		}).collect(Collectors.toList());
		Map<Long, FolderPO> folderIdAndFolderMap = folderDao.findAll(folderIds).stream().collect(Collectors.toMap(FolderPO::getId, Function.identity()));
		Map<Long, Integer> userIdAndLevelMap = new HashMap<Long, Integer>();
		for(FolderUserMap folderUser : folderUserMaps){
			FolderPO folder = folderIdAndFolderMap.get(folderUser.getFolderId());
			//空处理
			if(folder != null){
				String parentPath = folder.getParentPath();
				if(parentPath != null && !parentPath.equals("")){
					int level  = parentPath.split("/").length-1;
					userIdAndLevelMap.put(folderUser.getUserId(), level);
				}else {
					userIdAndLevelMap.put(folderUser.getUserId(), 0);
				}
			}else{
				throw new BaseException(StatusCode.FORBIDDEN, "没有找到用户--"+folderUser.getUserName()+" 所属文件夹");
			}
		}
		return userIdAndLevelMap;
	}
}
