package com.sumavision.bvc.device.command.cascade.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandAuthorizePO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandCooperatePO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandCrossPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoDAO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandInfoPO;
import com.sumavision.tetris.bvc.business.po.info.GroupCommandReplacePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.dao.BusinessGroupMemberTerminalChannelDAO;
import com.sumavision.tetris.bvc.cascade.bo.AuthCommandBO;
import com.sumavision.tetris.bvc.cascade.bo.CrossCommandBO;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.bvc.cascade.bo.ReplaceCommandBO;
import com.sumavision.tetris.bvc.cascade.bo.SecretCommandBO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 级联util类，主要用来生成groupBO<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月22日 下午2:12:52
 */
@Service
public class CommandCascadeUtil {
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private UserQuery userQuery;

	@Autowired
	private RoleDAO roleDAO;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private BusinessGroupMemberTerminalChannelDAO businessGroupMemberTerminalChannelDao;
	
	@Autowired
	private AgendaForwardDestinationDAO agendaForwardDestinationDao;
	
	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private GroupCommandInfoDAO groupCommandInfoDao;
	
	/**
	 * 发送即时消息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午2:17:23
	 * @param group
	 * @param sender 发起人，发消息的人
	 * @param message 消息内容
	 * @return
	 */
	public GroupBO sendInstantMessage(CommandGroupPO group, CommandGroupMemberPO sender, String message){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(sender.getUserNum())
				.setSubject(message)
				.setMlist(mlist);
		return groupBO;
	}
	
	/**
	 * 静默<br/>
	 * <p>包含指挥/会议的对上对下、开始停止</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午2:36:11
	 * @param group
	 * @param member
	 * @param operation 开始对上/停止对上/开始对下/停止对下 silencehigherstart/silencehigherstop/silencelowerstart/silencelowerstop
	 * @return
	 */
	public GroupBO becomeSilence(CommandGroupPO group, CommandGroupMemberPO member, String operation){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(member.getUserNum())
				.setMlist(mlist);
		groupBO.setBiztype(group.getType().equals(GroupType.MEETING)?"bizcnf":"bizcmd");
		groupBO.setCode(operation);
		return groupBO;
	}

	public GroupBO createCommand(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setSubject(group.getSubject())
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getUserNum())
				.setTopid(chairmanMember.getUserNum())
				.setMlist(mlist);
		return groupBO;
	}

	public GroupBO createAgendaCommand(GroupPO group){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setSubject(group.getSubject())
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getCode())
				.setTopid(chairmanMember.getCode())
				.setMlist(mlist);
		return groupBO;
	}
	
	public GroupBO deleteCommand(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		return groupBO;
	}

	public GroupBO deleteAgendaCommand(GroupPO group){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMlist(mlist);
		return groupBO;
	}
	
	public GroupBO startCommand(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setSubject(group.getSubject())
				.setStime(DateUtil.format(group.getStartTime(), DateUtil.dateTimePattern))
				.setMlist(mlist);
		
		return groupBO;
	}
	public GroupBO startAgendaCommand(GroupPO group){
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setSubject(group.getSubject())
				.setStime(DateUtil.format(group.getStartTime(), DateUtil.dateTimePattern))
				.setMlist(mlist);

		return groupBO;
	}
	
	public GroupBO stopCommand(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO stopAgendaCommand(GroupPO group){
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMlist(mlist);

		return groupBO;
	}
	
	public GroupBO pauseCommand(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}
	
	public GroupBO resumeCommand(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}
	
	/**
	 * 指挥进行中的时候加人或进入<br/>
	 * <p>用于生成和发送maddinc协议</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月23日 下午4:40:25
	 * @param group
	 * @param oldMemberInfos 该参数决定了将maddinc协议发送给哪些节点，可以直接使用加人之前的原用户列表，避免发给新加入业务的节点。null则会使用group.getMembers的成员
	 * @param acceptMembers
	 * @return
	 */
	public GroupBO joinCommand(CommandGroupPO group, List<MinfoBO> oldMemberInfos, List<CommandGroupMemberPO> acceptMembers){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		if(oldMemberInfos == null){
			oldMemberInfos = generateMinfoBOList(members);
		}
		List<MinfoBO> mAddList = generateMinfoBOList(acceptMembers, chairmanMember);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(oldMemberInfos)
				.setmAddList(mAddList);
		
		return groupBO;
	}

	public GroupBO joinAgendaCommand(GroupPO group, List<MinfoBO> oldMemberInfos, List<BusinessGroupMemberPO> acceptMembers){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		if(oldMemberInfos == null){
			oldMemberInfos = generateAgendaMinfoBOList(members);
		}
		List<MinfoBO> mAddList = generateAgendaMinfoBOList(acceptMembers, chairmanMember);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMlist(oldMemberInfos)
				.setmAddList(mAddList);

		return groupBO;
	}
	
	/** 静态组信息同步 */
	public GroupBO updateCommand(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setSubject(group.getSubject())
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getUserNum())
				.setTopid(chairmanMember.getUserNum())
				.setmAddList(mlist)//成员列表
				.setMlist(mlist);//发送目标成员列表，与成员列表相同
		
		return groupBO;
	}

	public GroupBO updateAgendaCommand(GroupPO group){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setSubject(group.getSubject())
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getCode())
				.setTopid(chairmanMember.getCode())
				.setmAddList(mlist)//成员列表
				.setMlist(mlist);//发送目标成员列表，与成员列表相同

		return groupBO;
	}

	/**全量信息同步。注意：group.getMembers()需要包含新成员 */
	public GroupBO maddfullCommand(CommandGroupPO group, List<MinfoBO> newNodeMemberInfos){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		String stime = DateUtil.format(group.getStartTime(), DateUtil.dateTimePattern);//如果getStartTime为空，会得到空字符串
		String status = group.getStatus().equals(GroupStatus.PAUSE)?"1":"0";//0表示正常业务、1表示暂停业务
		List<String> croplist = new ArrayList<String>();
		for(CommandGroupMemberPO member : members){
			if(member.getCooperateStatus().equals(MemberStatus.CONNECT)){
				croplist.add(member.getUserNum());
			}
		}
		List<MinfoBO> mAddList = generateMinfoBOList(members);
//		List<MinfoBO> mAddList = generateMinfoBOList(acceptMembers, chairmanMember);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setSubject(group.getSubject())
				.setStime(stime)
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getUserNum())
				.setTopid(chairmanMember.getUserNum())
				.setmAddList(mAddList)//
				.setStatus(status)
				.setCroplist(croplist)//协同成员号码列表
				.setMlist(newNodeMemberInfos);//新节点的成员，用于确定发送目的节点
		
		return groupBO;
	}

	public GroupBO maddfullAgendaCommand(GroupPO group, List<MinfoBO> newNodeMemberInfos){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		String stime = DateUtil.format(group.getStartTime(), DateUtil.dateTimePattern);//如果getStartTime为空，会得到空字符串
		String status = group.getStatus().equals(GroupStatus.PAUSE)?"1":"0";//0表示正常业务、1表示暂停业务
		AuthCommandBO authitem = null;
		ReplaceCommandBO replaceitem = null;
		List<String> croplist = null;
		List<CrossCommandBO> croslist = null;
		List<SecretCommandBO> secretlist = null;
		
		//添加其他的议程业务
		GroupCommandInfoPO info = groupCommandInfoDao.findByGroupId(group.getId());
		if(info != null){
			//专向
			if(info.isHasSecret()){
				
				List<Long> memberIds = new ArrayListWrapper<Long>().add(info.getSecretHighMemberId()).add(info.getSecretLowMemberId()).getList();
				Map<Long, BusinessGroupMemberPO> idAndMemberMap = businessGroupMemberDao.findByIdIn(memberIds).stream().collect(Collectors.toMap(BusinessGroupMemberPO::getId, Function.identity()));
				SecretCommandBO secretCommand = new SecretCommandBO();
				secretCommand.setUpid(idAndMemberMap.get(info.getSecretHighMemberId()).getCode());
				secretCommand.setDownid(idAndMemberMap.get(info.getSecretLowMemberId()).getCode());
				if(secretlist == null){
					secretlist = new ArrayList<SecretCommandBO>();
				}
				secretlist.add(secretCommand);
			}
			//越级
			else if(info.isHasCross()){
				
				List<GroupCommandCrossPO> crosses = info.getCrosses();
				String upid = null;
				String downid = null;
				if(crosses.get(0).getMemberLevel() > crosses.get(1).getMemberLevel()){
					upid = crosses.get(0).getMemberCode();
					downid = crosses.get(1).getMemberCode();
				}else{
					upid = crosses.get(1).getMemberCode();
					downid = crosses.get(0).getMemberCode();
				}
				
				CrossCommandBO crossCommand = new CrossCommandBO();
				crossCommand.setUpid(upid);
				crossCommand.setDownid(downid);
				if(croslist == null){
					croslist = new ArrayList<CrossCommandBO>();
				}
				croslist.add(crossCommand);
			}
			//协同
			else if(info.isHasCooperate()){
				
				List<GroupCommandCooperatePO> cooperates = info.getCooperates();
				List<String> codeList = cooperates.stream().map(GroupCommandCooperatePO::getMemberCode).collect(Collectors.toList());
				if (croplist == null) {
					croplist = new ArrayList<String>();
				}
				croplist.addAll(codeList);
			}
			//接替
			else if(info.isHasReplace()){
				
				GroupCommandReplacePO replace = info.getReplaces().get(0);
				replaceitem = new ReplaceCommandBO();
				replaceitem.setOp(replace.getOpMemberCode());
				replaceitem.setTargid(replace.getTargetMemberCode());
			}
			//授权
			else if(info.isHasAuthorize()){
				
				GroupCommandAuthorizePO authorize = info.getAuthorizes().get(0);
				authitem = new AuthCommandBO();
				authitem.setOp(chairmanMember.getCode());
				authitem.setCmdedid(authorize.getCmdMemberCode());
				authitem.setAccepauthid(authorize.getAcceptMemberCode());
			}
		}
	
		List<MinfoBO> mAddList = generateAgendaMinfoBOList(members);
//		List<MinfoBO> mAddList = generateMinfoBOList(acceptMembers, chairmanMember);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setGroupType(group.getEditStatus().getCode())
				.setSubject(group.getSubject())
				.setStime(stime)
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getCode())
				.setTopid(chairmanMember.getCode())
				.setmAddList(mAddList)//
				.setStatus(status)
				.setAuthitem(authitem)//授权指挥
				.setCroplist(croplist)//协同指挥列表
				.setCroslist(croslist)//越级指挥列表
				.setSecretlist(secretlist)//专向指挥
				.setReplaceitem(replaceitem)//接替指挥
				.setMlist(newNodeMemberInfos);//新节点的成员，用于确定发送目的节点

		return groupBO;
	}
	
	/** 退出指挥通知，包括主动和被动 */
	public GroupBO exitCommand(CommandGroupPO group, CommandGroupMemberPO removeMember){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMid(removeMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}
	public GroupBO exitAgendaCommand(GroupPO group, BusinessGroupMemberPO removeMember){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMid(removeMember.getCode())
				.setMlist(mlist);

		return groupBO;
	}
	
	/** 申请退出指挥 */
	public GroupBO exitCommandRequest(CommandGroupPO group, CommandGroupMemberPO exitMember){
		
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		MinfoBO m = setMember(chairmanMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(m).getList();
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(exitMember.getUserNum())
				.setMid(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	/**
	 * 申请退出指挥响应<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月4日 下午1:38:55
	 * @param group
	 * @param speakMember
	 * @param code 响应，0表示不同意、1表示同意 
	 * @return
	 */
	public GroupBO exitCommandResponse(CommandGroupPO group, CommandGroupMemberPO exitMember, String code){		
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		MinfoBO exitM = setMember(exitMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(exitM).getList();
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMid(exitMember.getUserNum())
				.setCode(code)
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO exitAgendaCommandResponse(GroupPO group, BusinessGroupMemberPO exitMember, String code){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		MinfoBO exitM = setAgendaMember(exitMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(exitM).getList();

		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMid(exitMember.getCode())
				.setCode(code)
				.setMlist(mlist);

		return groupBO;
	}

	/** 主席将成员退出。按协议，非批量 */
	public GroupBO kikoutCommand(CommandGroupPO group, CommandGroupMemberPO removeMember){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMid(removeMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}
	
	public GroupBO startCooperation(CommandGroupPO group, List<CommandGroupMemberPO> cooperateMembers){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);

		List<String> croplist = new ArrayList<String>();
		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
			croplist.add(cooperateMember.getUserNum());
		}
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setCroplist(croplist)
				.setMlist(mlist);
		
		return groupBO;
	}


	public GroupBO startAgendaCooperation(GroupPO group, List<BusinessGroupMemberPO> cooperateMembers){
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);

		List<String> croplist = new ArrayList<String>();
		for(BusinessGroupMemberPO cooperateMember : cooperateMembers){
			croplist.add(cooperateMember.getCode());
		}

		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setCroplist(croplist)
				.setMlist(mlist);

		return groupBO;
	}
	
	public GroupBO stopCooperation(CommandGroupPO group, List<CommandGroupMemberPO> cooperateMembers){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);

		List<String> croplist = new ArrayList<String>();
		for(CommandGroupMemberPO cooperateMember : cooperateMembers){
			croplist.add(cooperateMember.getUserNum());
		}
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setCroplist(croplist)
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO stopAgendaCooperation(GroupPO group, List<BusinessGroupMemberPO> cooperateMembers){
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);

		List<String> croplist = new ArrayList<String>();
		for(BusinessGroupMemberPO cooperateMember : cooperateMembers){
			croplist.add(cooperateMember.getCode());
		}

		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setCroplist(croplist)
				.setMlist(mlist);

		return groupBO;
	}
	
	/** 指挥/会议共用，转发设备，后续还需要支持转发用户 */
	public GroupBO startCommandDeviceForward(CommandGroupPO group, List<BundlePO> bundlePOs, List<UserBO> srcUserBos, List<CommandGroupMemberPO> dstMembers) throws Exception{
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		//目的用户号码列表
		List<String> mediaForwardMlist = new ArrayList<String>();
		for(CommandGroupMemberPO dstMember : dstMembers){
			mediaForwardMlist.add(dstMember.getUserNum());
		}
		
		//源号码列表
		List<String> medialist = new ArrayList<String>();
		for(BundlePO bundlePO : bundlePOs){
			medialist.add(bundlePO.getUsername());
		}
		for(UserBO user : srcUserBos){
			medialist.add(user.getUserNo());
		}
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMediaForwardMlist(mediaForwardMlist)
				.setMedialist(medialist)
				.setMlist(mlist);
		
		return groupBO;
	}
	
	/** 还需要支持转发用户，并支持通过号码来查询出转发任务进行删除 */
	/** 按标准，这是批量接口，但是在bvc里目前只能单个调度 */
	/**
	 * 停止调度转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月4日 上午11:42:06
	 * @param group
	 * @param bundlePOs 这些源要在所有的目的上停止
	 * @param dstMembers 总共有n*m个任务
	 * @return
	 * @throws Exception
	 */
	public GroupBO stopCommandDeviceForward(CommandGroupPO group, CommandGroupForwardDemandPO demand, List<CommandGroupMemberPO> dstMembers) throws Exception{
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		//目的用户号码列表
		List<String> mediaForwardMlist = new ArrayList<String>();
		for(CommandGroupMemberPO dstMember : dstMembers){
			mediaForwardMlist.add(dstMember.getUserNum());
		}
		
		//源号码列表
		List<String> medialist = new ArrayList<String>();
		medialist.add(demand.getSrcCode());
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMediaForwardMlist(mediaForwardMlist)
				.setMedialist(medialist)
				.setMlist(mlist);
		
		return groupBO;
	}
	
	/** 还需要支持转发用户，并支持通过号码来查询出转发任务进行删除 */
	/** 按标准，这是批量接口，但是在bvc里目前只能单个调度 */
	/**
	 * 停止调度转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月4日 上午11:42:06
	 * @param group
	 * @param bundlePOs 这些源要在所有的目的上停止
	 * @param dstMembers 总共有n*m个任务
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public GroupBO stopCommandDeviceForward(CommandGroupPO group, List<BundlePO> bundlePOs, List<CommandGroupMemberPO> dstMembers) throws Exception{
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		//目的用户号码列表
		List<String> mediaForwardMlist = new ArrayList<String>();
		for(CommandGroupMemberPO dstMember : dstMembers){
			mediaForwardMlist.add(dstMember.getUserNum());
		}
		
		//源号码列表
		List<String> medialist = new ArrayList<String>();
		for(BundlePO bundlePO : bundlePOs){
			medialist.add(bundlePO.getUsername());
		}
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMediaForwardMlist(mediaForwardMlist)
				.setMedialist(medialist)
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO createMeeting(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setSubject(group.getSubject())
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getUserNum())
				.setTopid(chairmanMember.getUserNum())
				.setMlist(mlist);
		return groupBO;
	}
	
	public GroupBO deleteMeeting(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		return groupBO;
	}
	
	public GroupBO startMeeting(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setSubject(group.getSubject())
				.setStime(DateUtil.format(group.getStartTime(), DateUtil.dateTimePattern))
				.setMlist(mlist);
		
		return groupBO;
	}
	
	public GroupBO stopMeeting(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO stopAgendaMeeting(GroupPO group){
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMlist(mlist);

		return groupBO;
	}
	
	public GroupBO pauseMeeting(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}
	
	public GroupBO resumeMeeting(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}
	
	/**
	 * 会议进行中的时候加人或进入<br/>
	 * <p>用于生成和发送maddinc协议</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月23日 下午4:40:25
	 * @param group
	 * @param oldMemberInfos 该参数决定了将maddinc协议发送给哪些节点，可以直接使用加人之前的用户列表，避免发给新加入业务的节点。null则会使用group.getMembers的成员
	 * @param acceptMembers
	 * @return
	 */
	public GroupBO joinMeeting(CommandGroupPO group, List<MinfoBO> oldMemberInfos, List<CommandGroupMemberPO> acceptMembers){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		if(oldMemberInfos == null){
			oldMemberInfos = generateMinfoBOList(members);
		}
		List<MinfoBO> mAddList = generateMinfoBOList(acceptMembers, chairmanMember);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(oldMemberInfos)
				.setmAddList(mAddList);
		
		return groupBO;
	}

	public GroupBO joinAgendaMeeting(GroupPO group, List<MinfoBO> oldMemberInfos, List<BusinessGroupMemberPO> acceptMembers){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		if(oldMemberInfos == null){
			oldMemberInfos = generateAgendaMinfoBOList(members);
		}
		List<MinfoBO> mAddList = generateAgendaMinfoBOList(acceptMembers, chairmanMember);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMlist(oldMemberInfos)
				.setmAddList(mAddList);

		return groupBO;
	}
	
	/** 静态组信息同步。主席/讨论模式在哪里？ */
	public GroupBO updateMeeting(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setSubject(group.getSubject())
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getUserNum())
				.setTopid(chairmanMember.getUserNum())
				.setmAddList(mlist)//成员列表
				.setMlist(mlist);//发送目标成员列表，与成员列表相同
		
		return groupBO;
	}

	public GroupBO updateAgendaMeeting(GroupPO group){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setSubject(group.getSubject())
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getCode())
				.setTopid(chairmanMember.getCode())
				.setmAddList(mlist)//成员列表
				.setMlist(mlist);//发送目标成员列表，与成员列表相同

		return groupBO;
	}
	
	//TODO:group.getMembers()需要包含新成员
	public GroupBO maddfullMeeting(CommandGroupPO group, List<MinfoBO> newNodeMemberInfos){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		String stime = DateUtil.format(group.getStartTime(), DateUtil.dateTimePattern);//如果getStartTime为空，会得到空字符串
		String mode = group.getSpeakType().getProtocalId();//0表示主席模式、1表示讨论模式
		String status = group.getStatus().equals(GroupStatus.PAUSE)?"1":"0";//0表示正常业务、1表示暂停业务
		List<String> spkidlist = new ArrayList<String>();
		for(CommandGroupMemberPO member : members){
			if(member.getCooperateStatus().equals(MemberStatus.CONNECT)){
				spkidlist.add(member.getUserNum());
			}
		}
		String spkid = null;//发言人号码。多人使用逗号分隔，后续改成只能1人发言
		if(spkidlist.size() > 0){
			spkid = StringUtils.join(spkidlist.toArray(), ",");
		}
		List<MinfoBO> mAddList = generateMinfoBOList(members);
//		List<MinfoBO> mAddList = generateMinfoBOList(acceptMembers, chairmanMember);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setSubject(group.getSubject())
				.setStime(stime)
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getUserNum())
				.setTopid(chairmanMember.getUserNum())
				.setmAddList(mAddList)//
				.setMode(mode)
				.setStatus(status)
				.setSpkid(spkid)//协同成员号码列表
				.setMlist(newNodeMemberInfos);//新节点的成员，用于确定发送目的节点
		
		return groupBO;
	}
    @Autowired
	private RunningAgendaDAO runningAgendaDAO;
	@Autowired
	private AgendaDAO agendaDAO;
	public GroupBO maddfullAgendaMeeting(GroupPO group, List<MinfoBO> newNodeMemberInfos){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		String stime = DateUtil.format(group.getStartTime(), DateUtil.dateTimePattern);//如果getStartTime为空，会得到空字符串
		RunningAgendaPO runningAgenda=runningAgendaDAO.findByGroupId(group.getId());
		AgendaPO agendaPO=agendaDAO.findOne(runningAgenda.getAgendaId());
		String mode = "1";
		if(BusinessInfoType.BASIC_MEETING.equals(agendaPO.getBusinessInfoType())){
			mode = "0";
		}//0表示主席模式、1表示讨论模式
		String status = group.getStatus().equals(GroupStatus.PAUSE)?"1":"0";//0表示正常业务、1表示暂停业务
		List<String> spkidlist = new ArrayList<String>();
		RolePO role=roleDAO.findByInternalRoleType(InternalRoleType.MEETING_SPEAKER);
		for(BusinessGroupMemberPO member : members){
			if(member.getRoleId().equals(role.getId())){
				spkidlist.add(member.getCode());
			}
		}
		String spkid = null;//发言人号码。多人使用逗号分隔，后续改成只能1人发言
		if(spkidlist.size() > 0){
			spkid = StringUtils.join(spkidlist.toArray(), ",");
		}
		List<MinfoBO> mAddList = generateAgendaMinfoBOList(members);
//		List<MinfoBO> mAddList = generateMinfoBOList(acceptMembers, chairmanMember);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setGroupType(group.getEditStatus().getCode())
				.setSubject(group.getSubject())
				.setStime(stime)
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getCode())
				.setTopid(chairmanMember.getCode())
				.setmAddList(mAddList)//
				.setMode(mode)
				.setStatus(status)
				.setSpkid(spkid)//协同成员号码列表
				.setMlist(newNodeMemberInfos);//新节点的成员，用于确定发送目的节点

		return groupBO;
	}

	/** 退出会议通知，包括主动和被动 */
	public GroupBO exitMeeting(CommandGroupPO group, CommandGroupMemberPO removeMember){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMid(removeMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO exitAgendaMeeting(GroupPO group, BusinessGroupMemberPO removeMember){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMid(removeMember.getCode())
				.setMlist(mlist);

		return groupBO;
	}
	
	/** 申请退出会议 */
	public GroupBO exitMeetingRequest(CommandGroupPO group, CommandGroupMemberPO exitMember){
		
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		MinfoBO m = setMember(chairmanMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(m).getList();
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(exitMember.getUserNum())
				.setMid(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	/**
	 * 申请会议退出响应<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月4日 下午1:38:55
	 * @param group
	 * @param speakMember
	 * @param code 响应，0表示不同意、1表示同意 
	 * @return
	 */
	public GroupBO exitMeetingResponse(CommandGroupPO group, CommandGroupMemberPO exitMember, String code){		
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		MinfoBO exitM = setMember(exitMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(exitM).getList();
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMid(exitMember.getUserNum())
				.setCode(code)
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO exitAgendaMeetingResponse(GroupPO group, BusinessGroupMemberPO exitMember, String code){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		MinfoBO exitM = setAgendaMember(exitMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(exitM).getList();

		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMid(exitMember.getCode())
				.setCode(code)
				.setMlist(mlist);

		return groupBO;
	}
	
	/** 按协议，非批量 */
	public GroupBO kikoutMeeting(CommandGroupPO group, CommandGroupMemberPO removeMember){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMid(removeMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}
	
	/** 还需要支持转发用户 */
	public GroupBO startMeetingDeviceForward(CommandGroupPO group, List<BundlePO> bundlePOs, List<CommandGroupMemberPO> dstMembers) throws Exception{
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		//目的用户号码列表
		List<String> mediaForwardMlist = new ArrayList<String>();
		for(CommandGroupMemberPO dstMember : dstMembers){
			mediaForwardMlist.add(dstMember.getUserNum());
		}
		
		//源号码列表
		List<String> medialist = new ArrayList<String>();
		for(BundlePO bundlePO : bundlePOs){
			medialist.add(bundlePO.getUsername());
		}
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMediaForwardMlist(mediaForwardMlist)
				.setMedialist(medialist)
				.setMlist(mlist);
		
		return groupBO;
	}

	/** 还需要支持转发用户，并支持通过号码来查询出转发任务进行删除 */
	/** 按标准，这是批量接口，但是在bvc里目前只能单个调度 */
	/**
	 * 停止调度转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月4日 上午11:42:06
	 * @param group
	 * @param bundlePOs 这些源要在所有的目的上停止
	 * @param dstMembers 总共有n*m个任务
	 * @return
	 * @throws Exception
	 */
	public GroupBO stopMeetingDeviceForward(CommandGroupPO group, List<BundlePO> bundlePOs, List<CommandGroupMemberPO> dstMembers) throws Exception{
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		//目的用户号码列表
		List<String> mediaForwardMlist = new ArrayList<String>();
		for(CommandGroupMemberPO dstMember : dstMembers){
			mediaForwardMlist.add(dstMember.getUserNum());
		}
		
		//源号码列表
		List<String> medialist = new ArrayList<String>();
		for(BundlePO bundlePO : bundlePOs){
			medialist.add(bundlePO.getUsername());
		}
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMediaForwardMlist(mediaForwardMlist)
				.setMedialist(medialist)
				.setMlist(mlist);
		
		return groupBO;
	}

	/** 指定发言/申请发言被同意的通知，单个 */
	public GroupBO speakerSet(CommandGroupPO group, CommandGroupMemberPO speakMember){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMid(speakMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO speakerAgendaSet(GroupPO group, BusinessGroupMemberPO speakMember){
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);

		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMid(speakMember.getCode())
				.setMlist(mlist);

		return groupBO;
	}
	
	/** 取消发言。主席/成员取消都用这个？ */
	public GroupBO speakerSetCancel(String operateUserCode, CommandGroupPO group, CommandGroupMemberPO speakMember){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
//		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(operateUserCode)
				.setMid(speakMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO speakerAgendaSetCancel(String operateUserCode, GroupPO group, BusinessGroupMemberPO speakMember){
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
//		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);

		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(operateUserCode)
				.setMid(speakMember.getCode())
				.setMlist(mlist);

		return groupBO;
	}
	
	/** 申请发言 */
	public GroupBO speakerSetRequest(CommandGroupPO group, CommandGroupMemberPO speakMember){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		MinfoBO m = setMember(chairmanMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(m).getList();
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(speakMember.getUserNum())
				.setMid(speakMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO speakerAgendaSetRequest(GroupPO group, BusinessGroupMemberPO speakMember){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		MinfoBO m = setAgendaMember(chairmanMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(m).getList();

		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(speakMember.getCode())
				.setMid(speakMember.getCode())
				.setMlist(mlist);

		return groupBO;
	}
	
	/**
	 * 申请发言响应<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月4日 下午1:38:55
	 * @param group
	 * @param speakMember
	 * @param code 响应，0表示不同意、1表示同意 
	 * @return
	 */
	public GroupBO speakerSetResponse(CommandGroupPO group, CommandGroupMemberPO speakMember, String code){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		MinfoBO speakM = setMember(speakMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(speakM).getList();
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMid(speakMember.getUserNum())
				.setCode(code)
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO speakerAgendaSetResponse(GroupPO group, BusinessGroupMemberPO speakMember, String code){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		MinfoBO speakM = setAgendaMember(speakMember, chairmanMember);
		List<MinfoBO> mlist = new ArrayListWrapper<MinfoBO>().add(speakM).getList();

		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMid(speakMember.getCode())
				.setCode(code)
				.setMlist(mlist);

		return groupBO;
	}
	
	public GroupBO discussStart(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO discussAgendaStart(GroupPO group){
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMlist(mlist);

		return groupBO;
	}
	
	public GroupBO discussStop(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	public GroupBO discussAgendaStop(GroupPO group){
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateAgendaMinfoBOList(members);
		BusinessGroupMemberPO chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMlist(mlist);

		return groupBO;
	}
	
	/** 必须保证members包含主席 */
	public List<MinfoBO> generateMinfoBOList(Collection<CommandGroupMemberPO> members){
		return generateMinfoBOList(members, null);
	}
	
	/** 当members可能不包含主席时，需要输入主席 */
	public List<MinfoBO> generateMinfoBOList(Collection<CommandGroupMemberPO> members, CommandGroupMemberPO chairmanMember){
		List<MinfoBO> mlist = new ArrayList<MinfoBO>();
		if(chairmanMember == null){
			chairmanMember = commandCommonUtil.queryChairmanMember(members);
		}
		for(CommandGroupMemberPO member : members){
			MinfoBO minfo = setMember(member, chairmanMember);
			mlist.add(minfo);
		}
		return mlist;
	}
	private MinfoBO setMember(CommandGroupMemberPO member, CommandGroupMemberPO chairmanMember){
		String mstatus = member.getMemberStatus().equals(MemberStatus.CONNECT)?"1":"2";//成员状态，1表示正在业务、2表示暂时离开、3表示已退出
		MinfoBO minfo = new MinfoBO()
				.setMid(member.getUserNum())
				.setMname(member.getUserName())
				.setMstatus(mstatus)
				.setMtype(member.getMemberType().getProtocalId());
		//TODO:pid 上级成员ID；会议组成员的上级成员ID为主席；ZH组成员的上级成员为其直接上级；主席 和最高级ZH员无上级不应显示此标签
		if(!member.getUuid().equals(chairmanMember.getUuid())){
			minfo.setPid(chairmanMember.getUserNum());
		}
		return minfo;
	}

	public List<MinfoBO> generateAgendaMinfoBOList(Collection<BusinessGroupMemberPO> members){
		return generateAgendaMinfoBOList(members, null);
	}

	/** 当members可能不包含主席时，需要输入主席 */
	public List<MinfoBO> generateAgendaMinfoBOList(Collection<BusinessGroupMemberPO> members, BusinessGroupMemberPO chairmanMember){
		List<MinfoBO> mlist = new ArrayList<MinfoBO>();
		if(chairmanMember == null){
			chairmanMember = commandCommonUtil.queryAgendaChairmanMember(members);
		}
		for(BusinessGroupMemberPO member : members){
			MinfoBO minfo = setAgendaMember(member, chairmanMember);
			mlist.add(minfo);
		}
		return mlist;
	}
	private MinfoBO setAgendaMember(BusinessGroupMemberPO member, BusinessGroupMemberPO chairmanMember){
		String mstatus = member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT)?"1":"2";//成员状态，1表示正在业务、2表示暂时离开、3表示已退出
		MinfoBO minfo = new MinfoBO()
				.setMid(member.getCode())
				.setMname(member.getName())
				.setMstatus(mstatus)
				.setMtype(member.getGroupMemberType().getProtocalId());
		//TODO:pid 上级成员ID；会议组成员的上级成员ID为主席；ZH组成员的上级成员为其直接上级；主席 和最高级ZH员无上级不应显示此标签
		if(!member.getUuid().equals(chairmanMember.getUuid())){
			minfo.setPid(chairmanMember.getCode());
		}
		return minfo;
	}
	

	
	/**
	 * 比对查找新节点用户<br/>
	 * <p>查找出mewMembers比oldMembers新增节点上的用户，即查找mewMembers与oldMembers相比，mewMembers在oldMembers所在节点以外节点上的用户</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月23日 上午9:35:15
	 * @param List<MinfoBO> oldMembers 旧用户列表
	 * @param List<MinfoBO> mewMembers 新用户列表
	 * @return List<MinfoBO> 比对结果
	 */
	public List<MinfoBO> filterAddedNodeMinfo(List<MinfoBO> oldMembers, List<MinfoBO> mewMembers) throws Exception{
		List<String> oldMemberCodes = new ArrayList<String>();
		if(oldMembers!=null && oldMembers.size()>0){
			for(MinfoBO member:oldMembers){
				oldMemberCodes.add(member.getMid());
			}
		}
		List<String> newMemberCodes = new ArrayList<String>();
		if(mewMembers!=null && mewMembers.size()>0){
			for(MinfoBO member:mewMembers){
				newMemberCodes.add(member.getMid());
			}
		}
		List<FolderUserMap> oldUserMaps = null;
		if(oldMemberCodes.size() > 0){
			oldUserMaps = folderUserMapDao.findByUserNoIn(oldMemberCodes);
		}
		List<FolderUserMap> newUserMaps = null;
		if(oldMemberCodes.size() > 0){
			newUserMaps = folderUserMapDao.findByUserNoIn(newMemberCodes);
		}
		List<String> addedUserCodes = new ArrayList<String>();
		if(newUserMaps!=null && newUserMaps.size()>0){
			for(FolderUserMap newMap:newUserMaps){
				boolean finded = false;
				if(oldUserMaps!=null && oldUserMaps.size()>0){
					for(FolderUserMap oldMap:oldUserMaps){
						if((oldMap.getUserNode()==null && newMap.getUserNode()==null)
								|| (oldMap.getUserNode()!=null && oldMap.getUserNode().equals(newMap.getUserNode()))){
							finded = true;
							break;
						}
					}
				}
				if(!finded){
					addedUserCodes.add(newMap.getUserNo());
				}
			}
		}
		List<MinfoBO> addedNodeMinfo = new ArrayList<MinfoBO>();
		for(String code:addedUserCodes){
			for(MinfoBO member:mewMembers){
				if(code.equals(member.getMid())){
					addedNodeMinfo.add(member);
					break;
				}
			}
		}
		return addedNodeMinfo;
	}
	
	/**
	 * 将用户号码转为用户列表<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月23日 下午5:26:16
	 * @param usernos
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> fromUserNosToUsers(Collection<String> usernos) throws Exception{
		//成员id列表
		List<UserVO> userList = new ArrayList<UserVO>();
		if(usernos==null || usernos.size()==0) return userList;
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		return users;
	}

	/**
	 * 将用户号码转为用户id列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 上午10:44:30
	 * @param usernos
	 * @return
	 * @throws Exception
	 */
	public List<Long> fromUserNosToIds(Collection<String> usernos) throws Exception{
		//成员id列表
		List<Long> userIdList = new ArrayList<Long>();
		if(usernos==null || usernos.size()==0) return userIdList;
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		for(UserVO user:users){
			userIdList.add(user.getId());
		}
		return userIdList;
	}
	
	private String generateUuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	private String generateTimestamp(){
		return String.valueOf(System.currentTimeMillis());
	}
	
}
