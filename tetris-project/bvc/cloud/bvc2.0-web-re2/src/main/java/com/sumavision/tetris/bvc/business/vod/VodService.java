package com.sumavision.tetris.bvc.business.vod;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sumavision.tetris.bvc.model.terminal.channel.ChannelParamsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.suma.venus.resource.dao.BundleDao;
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
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.MediaPushSetBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.XtBusinessPassByContentBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.monitor.live.exception.UserHasNoPermissionForBusinessException;
import com.sumavision.bvc.device.monitor.playback.exception.ResourceNotExistException;
import com.sumavision.bvc.feign.ResourceServiceClient;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.dao.VodDAO;
import com.sumavision.tetris.bvc.business.deliver.DeliverExecuteService;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberService;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoDAO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallPO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallService;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskQueryService;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.AgendaRoleMemberUtil;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
//@Transactional(rollbackFor = Exception.class)
public class VodService {
	
	/** 发起业务时，synchronized锁的前缀 */
	private static final String lockStartPrefix = "group-userId-";
	
	/** 响应、停止业务时，synchronized锁的前缀 */
	private static final String lockProcessPrefix = "tetris-group-";

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private GroupCommandInfoDAO groupCommandInfoDao;

	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private AgendaRoleMemberUtil agendaRoleMemberUtil;

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private CommonForwardDAO commonForwardDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private CommandVodDAO commandVodDao;
	
	@Autowired
	private VodDAO vodDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private ConferenceHallService conferenceHallService;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private ResourceServiceClient resourceServiceClient;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private GroupMemberService groupMemberService;
	
	@Autowired
	private DeliverExecuteService deliverExecuteService;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private PageTaskQueryService pageTaskQueryService;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	/**
	 * zsy<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 上午11:07:57
	 * @param user 用户
	 * @param resourceId 资源文件id
	 * @param locationIndex 指定播放器序号，序号从0起始；-1为自动选择
	 * @throws Exception
	 */
	public void resourceVodStart(UserBO user, String resourceId, Integer locationIndex) throws Exception{
		
		TerminalPO terminal = terminalDao.findByType(TerminalType.QT_ZK);
		
		JSONObject file = resourceService.queryOnDemandResourceById(resourceId);
		if(file == null) throw new ResourceNotExistException(resourceId);
		
		String resourceName = file.getString("name");
		String previewUrl = file.getString("previewUrl");
		
		//创建group、vod、PageTaskPO。不建member、agenda
		GroupPO group = new GroupPO();
		group.setUserId(user.getId());		
		group.setUserName(user.getName());
		group.setUserCode(user.getUserNo());
		group.setName(user.getName() + "点播" + resourceName + "文件");
		group.setCreatetime(new Date());
		group.setStartTime(group.getCreatetime());
		group.setBusinessType(BusinessType.VOD);
		group.setStatus(GroupStatus.START);
		if(locationIndex!=null) group.setLocationIndex(locationIndex);
		DeviceGroupAvtplPO g_avtpl = groupService.generateDeviceGroupAvtpl();
		group.setAvtpl(g_avtpl);
		g_avtpl.setBusinessGroup(group);
		groupDao.save(group);
		
		VodPO vod = new VodPO();
		vod.setResourceId(resourceId);
		vod.setUserId(user.getId());
		vod.setUserName(user.getName());
		vod.setVodType(com.sumavision.tetris.bvc.business.vod.VodType.FILE);
		vod.setSrcName(resourceName);
		vod.setDstType(DstType.USER);
		vod.setGroupId(group.getId());
		vodDao.save(vod);
		
		PageTaskPO task = new PageTaskPO();
		task.setBusinessInfoType(BusinessInfoType.PLAY_FILE);
		task.setBusinessName("点播" + resourceName + "文件");
		task.setBusinessId(group.getId().toString());
		task.setPlayUrl(previewUrl);
		
		BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(user.getId().toString(), terminal.getId(), GroupMemberType.MEMBER_USER);
		pageTaskService.addAndRemoveTasks(pageInfo, new ArrayListWrapper<PageTaskPO>().add(task).getList(), null);
		
		deliverExecuteService.execute(businessDeliverBO, "点播" + resourceName + "文件", true);
	}
	
	/**
	 * 停止点播文件资源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月25日 上午11:08:38
	 * @param user 用户
	 * @param businessId groupId
	 * @return
	 * @throws Exception
	 */
	public void resourceVodStop(UserBO user, String businessId) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(Long.parseLong(businessId));
			if(group == null){
				log.warn("停止点播文件，任务不存在，id: " + businessId);
				return;
			}
			
			//查出PO
			VodPO vod = vodDao.findByGroupId(group.getId());
			
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			
//			List<PageTaskPO> tasks = pageTaskDao.findByBusinessId(businessId);
			
			//停止pageTask
			agendaExecuteService.stopAllSrcAndDst(group, businessDeliverBO);
			
			//删除PO
			groupDao.delete(group);
			vodDao.delete(vod);
			
			deliverExecuteService.execute(businessDeliverBO, "停止点播文件：" + group.getName(), true);
		}
		return;
	}
	
	/**
	 * 重构点播用户<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月24日 下午3:45:59
	 * @param user
	 * @param vodUser 被点播设备
	 * @param serial
	 * @param uuid 被外部系统点播时不为null，由调用方（联网）生成
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void userStart(UserBO user, UserBO vodUser,Integer serial,String uuid) throws Exception{
		
		//commandCommonServiceImpl.authorizeUser(vodUser.getId(), user.getId(), BUSINESS_OPR_TYPE.DIANBO);
//		commandCommonServiceImpl.authorizeUsers(new ArrayListWrapper<Long>().add(vodUser.getId()).getList(), user.getId(), BUSINESS_OPR_TYPE.DIANBO);
		
		alreadySecret(vodUser);
		
		TerminalPO terminal = terminalDao.findByType(TerminalType.QT_ZK);

		GroupPO group = new GroupPO();
		group.setUserId(user.getId());		
		group.setUserName(user.getName());
		group.setUserCode(user.getUserNo());
		group.setName(user.getName() + "点播" + vodUser.getName() + "用户");
		group.setCreatetime(new Date());
		group.setStartTime(group.getCreatetime());
		group.setBusinessType(BusinessType.VOD);
		group.setStatus(GroupStatus.START);
		if(serial!=null) group.setLocationIndex(serial);
		DeviceGroupAvtplPO g_avtpl = groupService.generateDeviceGroupAvtpl();
		group.setAvtpl(g_avtpl);
		g_avtpl.setBusinessGroup(group);
		groupDao.save(group);
		
		VodPO vod = new VodPO();
		vod.setUserId(user.getId());
		vod.setUserName(user.getName());
		vod.setVodType(com.sumavision.tetris.bvc.business.vod.VodType.USER);
		vod.setSrcName(vodUser.getName());
		vod.setDstType(DstType.USER);
		vod.setGroupId(group.getId());
		if(uuid != null) vod.setUuid(uuid);
//		vodDao.save(vod);
		
		//点播用户作为成员
		FolderUserMap userfolderUserMap = folderUserMapDao.findByUserId(user.getId());
		boolean bUserLdap = queryUtil.isLdapUser(user, userfolderUserMap);
		BusinessGroupMemberPO userMemberPO = new BusinessGroupMemberPO();
		userMemberPO.setName(user.getName());
		userMemberPO.setCode(user.getUserNo());
		userMemberPO.setGroupMemberType(GroupMemberType.MEMBER_USER);
		userMemberPO.setOriginId(user.getId().toString());
		userMemberPO.setTerminalId(terminal.getId());
		userMemberPO.setFolderId(user.getFolderId());
		userMemberPO.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		userMemberPO.setGroup(group);
		if(bUserLdap || uuid!=null) userMemberPO.setOriginType(OriginType.OUTER);
		//这里有一种特殊情况，当外部系统开会，外部系统会以主席身份来拉流，被误判为“本系统发起的点播”，此时只能通过uuid!=null来判断
//		groupMemberDao.save(userMemberPO);
		
		//被点播用户作为成员
		FolderUserMap vodUserfolderUserMap = folderUserMapDao.findByUserId(vodUser.getId());
		boolean bVodUserLdap = queryUtil.isLdapUser(vodUser, vodUserfolderUserMap);
		BusinessGroupMemberPO vodUserMemberPO = new BusinessGroupMemberPO();
		vodUserMemberPO.setName(vodUser.getName());
		vodUserMemberPO.setCode(vodUser.getUserNo());
		vodUserMemberPO.setGroupMemberType(GroupMemberType.MEMBER_USER);
		vodUserMemberPO.setOriginId(vodUser.getId().toString());
		if(bVodUserLdap){
			TerminalPO jv210Terminal = terminalDao.findByType(TerminalType.JV210);
			vodUserMemberPO.setTerminalId(jv210Terminal.getId());
		}else{
			vodUserMemberPO.setTerminalId(terminal.getId());
		}
		vodUserMemberPO.setFolderId(vodUser.getFolderId());
		vodUserMemberPO.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		vodUserMemberPO.setGroup(group);
		if(bVodUserLdap) vodUserMemberPO.setOriginType(OriginType.OUTER);
//		groupMemberDao.save(vodUserMemberPO);
		
		List<BusinessGroupMemberPO> members = new ArrayListWrapper<BusinessGroupMemberPO>().add(userMemberPO).add(vodUserMemberPO).getList();
		businessGroupMemberDao.save(members);
		group.setMembers(new ArrayList<BusinessGroupMemberPO>());
		group.getMembers().addAll(members);
		groupDao.save(group);
		
		//给成员授权角色
		RolePO userRole = roleDao.findByInternalRoleType(InternalRoleType.VOD_DST);
		GroupMemberRolePermissionPO userRolePermission = new GroupMemberRolePermissionPO(userRole.getId(), userMemberPO.getId());
		groupMemberRolePermissionDao.save(userRolePermission);
		RolePO srcRole = roleDao.findByInternalRoleType(InternalRoleType.VOD_SRC);
		GroupMemberRolePermissionPO srcRolePermission = new GroupMemberRolePermissionPO(srcRole.getId(), vodUserMemberPO.getId());
		groupMemberRolePermissionDao.save(srcRolePermission);
		
		groupMemberService.fullfillGroupMember(members);
		userMemberPO.setRoleId(userRole.getId());
		vodUserMemberPO.setRoleId(srcRole.getId());
		groupDao.save(group);
		
		vod.setSrcMemberId(vodUserMemberPO.getId());
		vod.setDstMemberId(userMemberPO.getId());
		vodDao.save(vod);
		
		//呼叫被点播的编码
		LogicBO logic = groupMemberService.openEncoder(new ArrayListWrapper<BusinessGroupMemberPO>().add(vodUserMemberPO).getList(),
				g_avtpl, user.getId(), userMemberPO.getCode());

		BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
		AgendaPO agenda = agendaDao.findByBusinessInfoType(BusinessInfoType.PLAY_USER);

		if(bUserLdap || uuid!=null){
			//外部系统点播本系统用户。这里有一种特殊情况，当外部系统开会，外部系统会以主席身份来拉流，被误判为“本系统发起的点播”，此时只能通过uuid!=null来判断
			List<DeviceGroupAvtplGearsPO> gears = g_avtpl.getGears();
			Map<ChannelParamsType,DeviceGroupAvtplGearsPO> gearsMap = gears.stream().
					collect(Collectors.toMap(DeviceGroupAvtplGearsPO::getChannelParamsType,(p)->p));
			//多码率支持扩展
			DeviceGroupAvtplGearsPO gearsPO = null;
			if(gearsPO == null){
				gearsPO = gearsMap.get(ChannelParamsType.HD);
			}
			CodecParamBO codec=new CodecParamBO().set(gearsPO);
			String localLayerId = resourceRemoteService.queryLocalLayerId();
			logic.setPass_by(new ArrayList<PassByBO>());
			
			//找到被叫用户的编码通道
			BusinessGroupMemberTerminalBundleChannelPO videoEncodeBundleChannel = agendaRoleMemberUtil.obtainEncodeChannel(agenda.getId(), srcRole, group.getMembers());
			//音频应该是：videoEncodeBundleChannel.getMemberTerminalChannel().getAudioEncodeChannel().getMemberTerminalBundleChannels().get(0)
			
			if(videoEncodeBundleChannel == null){
				throw new BaseException(StatusCode.FORBIDDEN, vodUser.getName() + "没有正确设置编码器");
			}
			
			XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER)
					.setOperate(XtBusinessPassByContentBO.OPERATE_START)
					.setUuid(uuid!=null?uuid:vod.getUuid())
					.setSrc_user(user.getUserNo())
					.setLocal_encoder(new HashMapWrapper<String, String>()
							.put("layerid", videoEncodeBundleChannel.getMemberTerminalBundle().getLayerId())
							.put("bundleid", videoEncodeBundleChannel.getBundleId())
							.put("video_channelid", videoEncodeBundleChannel.getChannelId())
							.put("audio_channelid", ChannelType.AUDIOENCODE1.getChannelId())//音频先写死
							.getMap())
					.setDst_number(vodUser.getUserNo())
					.setVparam(codec);
			PassByBO passby = new PassByBO().setLayer_id(localLayerId)
					.setType(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER)
					.setPass_by_content(passByContent);
			logic.getPass_by().add(passby);
		}else{
			//校验能否找到被叫用户的编码通道
			BusinessGroupMemberTerminalBundleChannelPO videoEncodeBundleChannel = agendaRoleMemberUtil.obtainEncodeChannel(agenda.getId(), srcRole, group.getMembers());
			if(videoEncodeBundleChannel == null){
				throw new BaseException(StatusCode.FORBIDDEN, vodUser.getName() + " 没有正确设置编码器");
			}
			//本地看本地 或 本地看外部，执行议程
			agendaExecuteService.runAgenda(group.getId(), agenda.getId(), businessDeliverBO, false);
		}
		
//		if(businessReturnService.getSegmentedExecute()){
//			businessReturnService.add(logic, null, null);
//		}else{
//			executeBusiness.execute(logic, group.getName() + "，打开编码");
//		}
		businessReturnService.dealLogic(logic, group.getName() + "，打开编码", null);

		deliverExecuteService.execute(businessDeliverBO, userMemberPO.getOriginType().getName() + "开始点播用户：" + group.getName() + vodUserMemberPO.getOriginType().getName(), true);
	}
	
	/**
	 * 批处理点播用户<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月9日 下午2:46:52
	 * @param user
	 * @param userIdList
	 * @throws Exception
	 */
	public void userStartBatch(UserBO user,List <UserBO> userIdList) throws Exception{
		
		List<String> userBOs=new ArrayList<String>();
		
		for(UserBO vodUser : userIdList){
			boolean success=false;
			try{
				userStart(user, vodUser,null,null);
				success=true;
//				CommandGroupUserPlayerPO player = commandVodService.userStart_Cascade(user, vodUser, admin, -1);
//				BusinessPlayerVO _player = new BusinessPlayerVO().set(player);
//				playerVOs.add(_player);
			}catch(Exception e){
				log.info(user.getName() + "一键点播用户 "	 + " 部分失败，失败userId: " + vodUser.getId());
				e.printStackTrace();
			}finally{
				if(!success){
					userBOs.add(vodUser.getName());
				}
			}
		}
		
		if(userBOs.size()>0){
			throw new UserHasNoPermissionForBusinessException(BUSINESS_OPR_TYPE.DIANBO, userBOs.toString(),1);
		}
	}
	
	/**
	 * 检查被点播的用户是否在专向指挥中<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月24日 下午3:53:57
	 * @param vodUser
	 * @throws Exception
	 */
	private void alreadySecret(UserBO vodUser) throws Exception{
		
		List<GroupCommandInfoPO> infos = groupCommandInfoDao.findByHasSecret(true);
		Set<Long> businessGroupMemberIds = new HashSet<Long>();
		for(GroupCommandInfoPO info : infos){
			businessGroupMemberIds.add(info.getSecretHighMemberId());
			businessGroupMemberIds.add(info.getSecretLowMemberId());
		}
		
		List<BusinessGroupMemberPO> businessGroupMembers = businessGroupMemberDao.findAll(businessGroupMemberIds);
		List<String> userIds = businessGroupMembers.stream().map(BusinessGroupMemberPO::getOriginId).collect(Collectors.toList());
		
		if(userIds.contains(vodUser.getId().toString())){
			throw new BaseException(StatusCode.FORBIDDEN, "被点播用户："+vodUser.getName()+"正在私密通话中，不能点播");
		}
	}
	
	/** 重构停止点播用户 */
	@Transactional(rollbackFor = Exception.class)
	public void userStop(Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(group == null){
				log.warn("停止点播用户，任务不存在，id: " + groupId);
				return;
			}
			
			//查出PO
			VodPO vod = vodDao.findByGroupId(group.getId());
			List<BusinessGroupMemberPO> members = group.getMembers();
			List<Long> memberIds = members.stream().map(BusinessGroupMemberPO::getId).collect(Collectors.toList());
			BusinessGroupMemberPO vodUserMemberPO = tetrisBvcQueryUtil.queryMemberById(members, vod.getSrcMemberId());//被点播人
			BusinessGroupMemberPO userMemberPO = tetrisBvcQueryUtil.queryMemberById(members, vod.getDstMemberId());//点播发起人
			
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			
			//停止转发，删除议程记录
			agendaExecuteService.stopAllSrcAndDst(group, businessDeliverBO);
			RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
			if(runningAgenda!=null){
				runningAgendaDao.delete(runningAgenda);
			}

			//关闭编码
			CodecParamBO codec = new CodecParamBO().set(group.getAvtpl());// commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			LogicBO logic = groupMemberService.closeEncoder(new ArrayListWrapper<BusinessGroupMemberPO>().add(vodUserMemberPO).getList(), codec, -1L, userMemberPO.getCode());
			if(OriginType.OUTER.equals(userMemberPO.getOriginType())){

				String localLayerId = resourceRemoteService.queryLocalLayerId();
				logic.setPass_by(new ArrayList<PassByBO>());
				XtBusinessPassByContentBO passByContent = new XtBusinessPassByContentBO().setCmd(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER)
						.setOperate(XtBusinessPassByContentBO.OPERATE_STOP)
						.setUuid(vod.getUuid())
						.setSrc_user(userMemberPO.getCode())
						.setLocal_encoder(new HashMapWrapper<String, String>().put("layerid", localLayerId)
								.put("bundleid", vodUserMemberPO.getUuid())
								.put("video_channelid", "VenusVideoIn_1")
								.put("audio_channelid", "VenusAudioIn_1")
								.getMap())
						.setDst_number(vodUserMemberPO.getCode())
						.setVparam(codec);
				PassByBO passby = new PassByBO().setLayer_id(localLayerId)
						.setType(XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER)
						.setPass_by_content(passByContent);
				logic.getPass_by().add(passby);
			}



			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.add(logic, null, null);
			}else{
				executeBusiness.execute(logic, group.getName() + " 挂断用户编码器");
			}
			
			//删除PO
			groupDao.delete(group);
			vodDao.delete(vod);
			groupMemberRolePermissionDao.deleteByGroupMemberIdIn(memberIds);
			
			deliverExecuteService.execute(businessDeliverBO, "停止点播用户：" + group.getName(), true);
		}
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
	/*public CommandGroupUserPlayerPO userStop(UserBO user, Long businessId, UserBO admin) throws Exception{
		
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
		if(vod.getVodType().equals(VodType.USER) || vod.getVodType().equals(VodType.LOCAL_SEE_OUTER_USER)){
			player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.PLAY_USER, businessId.toString());
		}else if(vod.getVodType().equals(VodType.USER_ONESELF)){
			player = commandCommonServiceImpl.queryPlayerByBusiness(userInfo, PlayerBusinessType.PLAY_USER_ONESELF, businessId.toString());
		}
		player.setFree();
		commandGroupUserPlayerDao.save(player);
		
		commandVodDao.delete(vod);
		
		LogicBO logicCast = commandCastServiceImpl.closeBundleCastDevice(
				null, new ArrayListWrapper<CommandVodPO>().add(vod).getList(), null, 
				new ArrayListWrapper<CommandGroupUserPlayerPO>().add(player).getList(), null, user.getId());
		logic.merge(logicCast);
		executeBusiness.execute(logic, user.getName() + "停止点播用户");
		
		return player;
		
	}*/
	
	/** 重构点播设备 */
	@Transactional(rollbackFor = Exception.class)
	public void deviceStart(UserBO user, String bundleId,Integer serial) throws Exception{
		
		TerminalPO userTerminal = terminalDao.findByType(TerminalType.QT_ZK);
		
		//被点播--编码设备
		List<BundlePO> encoderBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(bundleId).getList());
		BundlePO encoderBundleEntity = encoderBundleEntities.get(0);
		
		GroupPO group = new GroupPO();
		group.setUserId(user.getId());
		group.setUserName(user.getName());
		group.setUserCode(user.getUserNo());
		group.setName(user.getName() + "点播" + encoderBundleEntity.getBundleName() + "设备");
		group.setCreatetime(new Date());
		group.setStartTime(group.getCreatetime());
		group.setBusinessType(BusinessType.VOD);
		group.setStatus(GroupStatus.START);
		if(serial!=null)group.setLocationIndex(serial);		
		DeviceGroupAvtplPO g_avtpl = groupService.generateDeviceGroupAvtpl();
		group.setAvtpl(g_avtpl);
		g_avtpl.setBusinessGroup(group);
		groupDao.save(group);
		
		VodPO vod = new VodPO();
		vod.setUserId(user.getId());
		vod.setUserName(user.getName());
		vod.setVodType(com.sumavision.tetris.bvc.business.vod.VodType.USER);
		vod.setSrcName(encoderBundleEntity.getBundleName());
		vod.setDstType(DstType.USER);
		vod.setGroupId(group.getId());
//		vodDao.save(vod);
		
		//点播用户作为成员
		BusinessGroupMemberPO userMemberPO = new BusinessGroupMemberPO();
		userMemberPO.setName(user.getName());
		userMemberPO.setCode(user.getUserNo());
		userMemberPO.setGroupMemberType(GroupMemberType.MEMBER_USER);
		userMemberPO.setOriginId(user.getId().toString());
		userMemberPO.setTerminalId(userTerminal.getId());
		userMemberPO.setFolderId(user.getFolderId());
		userMemberPO.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		userMemberPO.setGroup(group);
//		groupMemberDao.save(userMemberPO);
		
		//被点播设备，对应的会场作为成员
		ConferenceHallPO hall = conferenceHallService.bundleExchangeToHall(new ArrayListWrapper<BundlePO>().add(encoderBundleEntity).getList()).get(0);
		BusinessGroupMemberPO vodDeviceMemberPO = new BusinessGroupMemberPO();
		vodDeviceMemberPO.setName(hall.getName());
		vodDeviceMemberPO.setCode(encoderBundleEntity.getUsername());
		vodDeviceMemberPO.setBundleId(bundleId);
		vodDeviceMemberPO.setGroupMemberType(GroupMemberType.MEMBER_HALL);
		vodDeviceMemberPO.setOriginId(hall.getId().toString());
		vodDeviceMemberPO.setTerminalId(hall.getTerminalId());
		vodDeviceMemberPO.setFolderId(hall.getFolderId());
		vodDeviceMemberPO.setGroupMemberStatus(GroupMemberStatus.CONNECT);
		vodDeviceMemberPO.setGroup(group);
		if(queryUtil.isLdapBundle(encoderBundleEntity)) vodDeviceMemberPO.setOriginType(OriginType.OUTER);
		
		List<BusinessGroupMemberPO> members = new ArrayListWrapper<BusinessGroupMemberPO>().add(userMemberPO).add(vodDeviceMemberPO).getList();
		businessGroupMemberDao.save(members);
		group.setMembers(new ArrayList<BusinessGroupMemberPO>());
		group.getMembers().addAll(members);
		groupDao.save(group);
		
		//把成员授权给角色
		RolePO userRole = roleDao.findByInternalRoleType(InternalRoleType.VOD_DST);
		GroupMemberRolePermissionPO userRolePermission = new GroupMemberRolePermissionPO(userRole.getId(), userMemberPO.getId());
		groupMemberRolePermissionDao.save(userRolePermission);
		RolePO srcRole = roleDao.findByInternalRoleType(InternalRoleType.VOD_SRC);
		GroupMemberRolePermissionPO srcRolePermission = new GroupMemberRolePermissionPO(srcRole.getId(), vodDeviceMemberPO.getId());
		groupMemberRolePermissionDao.save(srcRolePermission);
		
		groupMemberService.fullfillGroupMember(members);
		userMemberPO.setRoleId(userRole.getId());
		vodDeviceMemberPO.setRoleId(srcRole.getId());
		groupDao.save(group);
		
		vod.setSrcMemberId(vodDeviceMemberPO.getId());
		vod.setDstMemberId(userMemberPO.getId());
		vodDao.save(vod);
		
		//呼叫被点播的编码
		LogicBO logic = groupMemberService.openEncoder(new ArrayListWrapper<BusinessGroupMemberPO>().add(vodDeviceMemberPO).getList(),
				g_avtpl, user.getId(), userMemberPO.getCode());
		if(businessReturnService.getSegmentedExecute()){
			businessReturnService.add(logic, null, null);
		}else{
			executeBusiness.execute(logic, group.getName() + "，打开编码");
		}
		
		//执行议程
		BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
		AgendaPO agenda = agendaDao.findByBusinessInfoType(BusinessInfoType.PLAY_DEVICE);
		agendaExecuteService.runAgenda(group.getId(), agenda.getId(), businessDeliverBO, false);
		
		deliverExecuteService.execute(businessDeliverBO, "开始点播设备：" + group.getName(), true);
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
	@Transactional(rollbackFor = Exception.class)
	public void deviceStop(Long groupId) throws Exception{
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(groupId).toString().intern()) {
			
			GroupPO group = groupDao.findOne(groupId);
			if(group == null){
				log.warn("停止点播设备，任务不存在，id: " + groupId);
				return;
			}
			VodPO vod = vodDao.findByGroupId(group.getId());
			List<BusinessGroupMemberPO> members = group.getMembers();
			List<Long> memberIds = members.stream().map(BusinessGroupMemberPO::getId).collect(Collectors.toList());
			BusinessGroupMemberPO vodDeviceMemberPO = null;
			BusinessGroupMemberPO userMemberPO = null;
			if(members.get(0).getId().equals(vod.getSrcMemberId())){
				vodDeviceMemberPO = members.get(0);
				userMemberPO = members.get(1);
			}else{
				vodDeviceMemberPO = members.get(1);
				userMemberPO = members.get(0);
			}
			
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			
			//停止转发，删除议程记录
			agendaExecuteService.stopAllSrcAndDst(group, businessDeliverBO);
			RunningAgendaPO runningAgenda = runningAgendaDao.findByGroupId(groupId);
			runningAgendaDao.delete(runningAgenda);
						
			//关闭编码
			CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
			LogicBO logic = groupMemberService.closeEncoder(new ArrayListWrapper<BusinessGroupMemberPO>().add(vodDeviceMemberPO).getList(), codec, -1L, userMemberPO.getCode());
			if(businessReturnService.getSegmentedExecute()){
				businessReturnService.add(logic, null, null);
			}else{
				executeBusiness.execute(logic, group.getName() + " 挂断编码器");
			}
						
			//删除PO
			groupDao.delete(group);
			vodDao.delete(vod);
			groupMemberRolePermissionDao.deleteByGroupMemberIdIn(memberIds);
			
			deliverExecuteService.execute(businessDeliverBO, "停止点播设备：" + group.getName(), true);
		}		
	}

	/**
	 * 用户看自己的编码器<br/>
	 * <p>与“点播用户”userStart()方法基本相同，区别是选用最后一个可用的播放器</p>
	 * <p>VodType为USER_ONESELF，PlayerBusinessType为PLAY_USER_ONESELF，都是为了查询</p>
	 * <p>会先校验该用户是否有播放器已经在观看本地视频，再校验用户的编码器是否改变，如果改变，则停止原来的，再正常建一个新的</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月29日 上午9:10:26
	 * @param user
	 * @param admin
	 * @param exceptionIfExist 已经存在观看自己，且编码器不变的情况下，true会抛错，false则直接返回
	 * @return
	 * @throws Exception
	 */
	/*public CommandGroupUserPlayerPO seeOneselfUserStart(UserBO user, UserBO admin, boolean exceptionIfExist) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		CommandGroupUserPlayerPO selfPlayer = commandCommonUtil.queryPlayerByPlayerBusinessType(userInfo.obtainUsingSchemePlayers(), PlayerBusinessType.PLAY_USER_ONESELF);
		if(selfPlayer != null){
//			if(selfPlayer.getBusinessId().equals("0")){
//				seeOneselfLocalStop(user);
//			}
			//检测用户绑定的编码器是否更新
			CommandVodPO vod = commandVodDao.findOne(Long.parseLong(selfPlayer.getBusinessId()));			
			if(vod == null){
				throw new BaseException(StatusCode.FORBIDDEN, "用户点播不存在！");//正常不会出现
			}
			String vodEncoderBundleId = vod.getSourceBundleId();			
			String encoderBundleId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
			if(encoderBundleId == null){
				throw new BaseException(StatusCode.FORBIDDEN, "用户没有绑定编码器！");
			}
			if(vodEncoderBundleId.equals(encoderBundleId)){
				//编码器不变，直接返回播放器
				if(exceptionIfExist){
					throw new BaseException(StatusCode.FORBIDDEN, "已经在观看本地视频");					
				}else{
					return selfPlayer;
				}
			}else{
				//编码器修改，执行停止
				userStop(user, vod.getId(), admin);
			}
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
		LogicBO logicCast = commandCastServiceImpl.openBundleCastDevice(null, null, null, null, new ArrayListWrapper<CommandVodPO>().add(userVod).getList(), null, codec, user.getId());
		logic.merge(logicCast);
		
		executeBusiness.execute(logic, user.getName() + "用户观看自己");
		
		return decoderUserPlayer;
	}*/

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
	/*public CommandGroupUserPlayerPO seeOneselfLocalStart(UserBO user) throws Exception{
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		CommandGroupUserPlayerPO selfPlayer = commandCommonUtil.queryPlayerByPlayerBusinessType(userInfo.obtainUsingSchemePlayers(), PlayerBusinessType.PLAY_USER_ONESELF);
		if(selfPlayer != null){
			throw new BaseException(StatusCode.FORBIDDEN, "已经存在本地预览");
		}
		
		//选最后一个播放器，选不到则抛错
		selfPlayer = commandCommonServiceImpl.userChoseUsefulPlayer(user.getId(), PlayerBusinessType.PLAY_USER_ONESELF, -1, false);		
		selfPlayer.setBusinessId("0");//需要写一个假的businessId
		selfPlayer.setBusinessName("本地视频预览");
		commandGroupUserPlayerDao.save(selfPlayer);
		
		return selfPlayer;
	}*/
	
	/*public void seeOneselfLocalStop(UserBO user) throws Exception{
		
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
	}*/
	
	public void recordVodStart(UserBO user, String businessType, String businessInfo, String url, Integer locationIndex) throws Exception{
		
		TerminalPO terminal = terminalDao.findByType(TerminalType.QT_ZK);
		
		//创建group、vod、PageTaskPO。不建member、agenda
		GroupPO group = new GroupPO();
		group.setUserId(user.getId());		
		group.setUserName(user.getName());
		group.setUserCode(user.getUserNo());
		group.setName(user.getName() + businessInfo);
		group.setCreatetime(new Date());
		group.setStartTime(group.getCreatetime());
		group.setBusinessType(BusinessType.VOD);
		group.setStatus(GroupStatus.START);
		if(locationIndex!=null) group.setLocationIndex(locationIndex);
		DeviceGroupAvtplPO g_avtpl = groupService.generateDeviceGroupAvtpl();
		group.setAvtpl(g_avtpl);
		g_avtpl.setBusinessGroup(group);
		groupDao.save(group);
		
		VodPO vod = new VodPO();
//		vod.setResourceId(resourceId);
		vod.setUserId(user.getId());
		vod.setUserName(user.getName());
		vod.setVodType(com.sumavision.tetris.bvc.business.vod.VodType.FILE);
		vod.setSrcName(businessInfo);
		vod.setDstType(DstType.USER);
		vod.setGroupId(group.getId());
		vodDao.save(vod);
		
		PageTaskPO task = new PageTaskPO();
		task.setBusinessInfoType(BusinessInfoType.PLAY_RECORD);
		task.setBusinessName(businessInfo);
		task.setBusinessId(group.getId().toString());
		task.setPlayUrl(url);
		
		BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(user.getId().toString(), terminal.getId(), GroupMemberType.MEMBER_USER);
		pageTaskService.addAndRemoveTasks(pageInfo, new ArrayListWrapper<PageTaskPO>().add(task).getList(), null);
		
		deliverExecuteService.execute(businessDeliverBO, businessInfo, true);
	}

	
	public void recordVodStop(UserBO user, int serial) throws Exception{
		
		TerminalPO terminal = terminalDao.findByType(TerminalType.QT_ZK);
		PageTaskPO removeTask = pageTaskQueryService.queryPageTask(user.getId().toString(), terminal.getId(), serial);
		if(removeTask == null){
			log.warn("停止点播录制文件，任务不存在，serial: " + serial);
			return;
		}
		String businessId = removeTask.getBusinessId();
		
		synchronized (new StringBuffer().append(lockProcessPrefix).append(businessId).toString().intern()) {
			
			//指挥录制关闭  businessId是纯数字并且有播放地址  
			if(!businessId.matches("\\d+") && removeTask.getPlayUrl() != null){
				PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(user.getId().toString(), terminal.getId(), GroupMemberType.MEMBER_USER);
				pageTaskService.addAndRemoveTasks(pageInfo, null, new ArrayListWrapper<PageTaskPO>().add(removeTask).getList());
				businessReturnService.execute();
				return;
			}
			
			GroupPO group = groupDao.findOne(Long.parseLong(businessId));
			
			//查出PO
			VodPO vod = vodDao.findByGroupId(group.getId());
			
			BusinessDeliverBO businessDeliverBO = new BusinessDeliverBO().setUserId(group.getUserId().toString()).setGroup(group);
			
//			List<PageTaskPO> tasks = pageTaskDao.findByBusinessId(businessId);
			
			//停止pageTask
			agendaExecuteService.stopAllSrcAndDst(group, businessDeliverBO);
			
			//删除PO
			groupDao.delete(group);
			vodDao.delete(vod);
			
			deliverExecuteService.execute(businessDeliverBO, "停止点播录制文件：" + group.getName(), true);
		}
		return;
	}

	/**
	 * 点播呼叫协议生成<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 下午3:12:16
	 * @param VodPO vod 点播信息
	 * @param CodecParamBO codec 参数
	 * @param Long userId 用户id
	 * @return LogicBO 协议
	 */
	public LogicBO connectBundle(
			CommandVodPO vod,
			CodecParamBO codec,
			Long userId) throws Exception{		
		
		VodType vodType = vod.getVodType();
				
		//呼叫设备
		LogicBO logic = new LogicBO().setUserId(userId.toString())
				 			 		 .setConnectBundle(new ArrayList<ConnectBundleBO>())
				 			 		 .setPass_by(new ArrayList<PassByBO>());
		
		if(vodType==null || vodType.equals(VodType.USER) || vodType.equals(VodType.USER_ONESELF) || vodType.equals(VodType.DEVICE)){
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
	 * @param closeDecoder 是否关闭播放器/解码器，通常true，在换源业务时使用false
	 * @return LogicBO 协议
	 */
	public LogicBO closeBundle(
			CommandVodPO vod,
			CodecParamBO codec,
			Long userId,
			boolean closeDecoder) throws Exception{
	
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setDisconnectBundle(new ArrayList<DisconnectBundleBO>())
									 .setMediaPushDel(new ArrayList<MediaPushSetBO>())
									 .setPass_by(new ArrayList<PassByBO>());
		
		VodType vodType = vod.getVodType();
		if(vodType == null || vodType.equals(VodType.DEVICE)
				|| vodType.equals(VodType.USER)
				|| vodType.equals(VodType.USER_ONESELF)){
			//关闭被点播用户或设备
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
		
		if(closeDecoder){
			
			//清除资源层上的字幕
			resourceServiceClient.removeLianwangPassby(vod.getDstBundleId());
			
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
	
	/** 把联网的passby存储或从资源层删除。该方法暂时没有使用 */
	public void saveOrRemoveLianwangPassbyToResource(LogicBO logic){
		List<PassByBO> passbys = logic.getPass_by();
		if(passbys == null) return;
		for(PassByBO passby : passbys){
			String type = passby.getType();
			if(XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_ENCODER.equals(type)
					|| XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_ENCODER.equals(type)
					|| XtBusinessPassByContentBO.CMD_LOCAL_SEE_XT_USER.equals(type)
					|| XtBusinessPassByContentBO.CMD_XT_SEE_LOCAL_USER.equals(type)
					|| XtBusinessPassByContentBO.CMD_LOCAL_CALL_XT_USER.equals(type)
					|| XtBusinessPassByContentBO.CMD_XT_CALL_LOCAL_USER.equals(type)){
				try{
					String uuid = ((XtBusinessPassByContentBO)passby.getPass_by_content()).getUuid();
					String operate = ((XtBusinessPassByContentBO)passby.getPass_by_content()).getOperate();
					if(XtBusinessPassByContentBO.OPERATE_START.equals(operate)){
						resourceServiceClient.coverLianwangPassby(
								uuid, 
								passby.getLayer_id(), 
								type, 
								JSON.toJSONString(passby));
					}else if(XtBusinessPassByContentBO.OPERATE_STOP.equals(operate)){
						resourceServiceClient.removeLianwangPassby(uuid);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
