package com.sumavision.bvc.device.command.cascade.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
public class CommandCascadeUtil {
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private UserQuery userQuery;
	
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

	//TODO:group.getMembers()需要包含新成员
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
	
	/** 指挥/会议共用，转发设备，后续还需要支持转发用户 */
	public GroupBO startCommandDeviceForward(CommandGroupPO group, List<BundlePO> bundlePOs, List<CommandGroupMemberPO> dstMembers) throws Exception{
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
	
	//TODO:group.getMembers()需要包含新成员
	public GroupBO maddfullMeeting(CommandGroupPO group, List<MinfoBO> newNodeMemberInfos){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		String stime = DateUtil.format(group.getStartTime(), DateUtil.dateTimePattern);//如果getStartTime为空，会得到空字符串
		String mode = "0";//0表示主席模式、1表示讨论模式，后续支持
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
						if(oldMap.getUserNode().equals(newMap.getUserNode())){
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
