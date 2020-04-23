package com.sumavision.bvc.device.command.cascade.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.bo.DeviceInfoBO;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.commons.util.date.DateUtil;

import freemarker.template.Template;

@Service
public class CommandCascadeUtil {
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
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
	/** maddinc 指挥进行中的时候加人或进入 */
	public GroupBO joinCommand(CommandGroupPO group, List<CommandGroupMemberPO> acceptMembers){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		List<MinfoBO> mAddList = generateMinfoBOList(acceptMembers);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist)
				.setmAddList(mAddList);
		
		return groupBO;
	}
	
	/** 按协议，非批量 */
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
	
	/** 申请退出 */
	public GroupBO exitCommandRequest(CommandGroupPO group, CommandGroupMemberPO exitMember){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
//		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(exitMember.getUserNum())
				.setMid(exitMember.getUserNum())
				.setMlist(mlist);
		
		return groupBO;
	}

	/**
	 * 申请退出响应<br/>
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
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
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
	
	/** 还需要支持转发用户 */
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
//				.setOp(chairmanMember.getUserNum())
				.setSubject(group.getSubject())
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getUserNum())
				.setTopid(chairmanMember.getUserNum())
				.setMlist(mlist);
		return groupBO;
	}
	
	public GroupBO deleteMeeting(CommandGroupPO group){
		List<CommandGroupMemberPO> members = group.getMembers();
		List<MinfoBO> mlist = generateMinfoBOList(members);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
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
	/** maddinc 会议进行中的时候加人或进入 */
	public GroupBO joinMeeting(CommandGroupPO group, List<CommandGroupMemberPO> acceptMembers){
		List<CommandGroupMemberPO> members = group.getMembers();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members);
		List<MinfoBO> mAddList = generateMinfoBOList(acceptMembers);
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMlist(mlist)
				.setmAddList(mAddList);
		
		return groupBO;
	}
	
	/** 按协议，非批量 */
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

	/** 指定发言，单个 */
	public GroupBO speakerSetByChairman(CommandGroupPO group, CommandGroupMemberPO speakMember){
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
		List<MinfoBO> mlist = generateMinfoBOList(members);
//		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
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
		List<MinfoBO> mlist = generateMinfoBOList(members);
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getUserNum())
				.setMid(speakMember.getUserNum())
				.setCode(code)
				.setMlist(mlist);
		
		return groupBO;
	}
	
	private List<MinfoBO> generateMinfoBOList(Collection<CommandGroupMemberPO> members){
		List<MinfoBO> mlist = new ArrayList<MinfoBO>();
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		for(CommandGroupMemberPO member : members){
			MinfoBO minfo = setMember(member, chairmanMember);
			mlist.add(minfo);
		}
		return mlist;
	}
	
	private MinfoBO setMember(CommandGroupMemberPO member, CommandGroupMemberPO chairmanMember){
		MinfoBO minfo = new MinfoBO()
				.setMid(member.getUserNum())
				.setMname(member.getUserName())
				.setMtype(member.getMemberType().getProtocalId());
		if(!member.getUuid().equals(chairmanMember.getUuid())){
			minfo.setPid(chairmanMember.getUserNum());
		}
		//TODO:pid 上级成员ID；会议组成员的上级成员ID为主席；ZH组成员的上级成员为其直接上级；主席 和最高级ZH员无上级不应显示此标签
		return minfo;
	}
	
	private void filter(
			GroupBO group,
			String type,
			Template template) throws Exception{
		
		//根据用户号码或设备号码查询隶属应用号码列表（过滤本应用）
		List<MinfoBO> mlist = group.getMlist();
		List<DeviceInfoBO> devices = new ArrayList<DeviceInfoBO>();
		for(MinfoBO m:mlist){
			DeviceInfoBO device = new DeviceInfoBO();
			device.setCode(m.getMid());
			device.setType(m.getMtype().equals("usr")?"user":"device");
			devices.add(device);
		}
		List<String> dstnos = resourceRemoteService.querySerNodeList(devices);
		
		
		
		
		List<String> userCodes = new ArrayList<String>();
		List<MinfoBO> newMlist = group.getMlist();
		List<DeviceInfoBO> newDevices = new ArrayList<DeviceInfoBO>();
		for(MinfoBO m:newMlist){
			DeviceInfoBO device = new DeviceInfoBO();
			device.setCode(m.getMid());
			device.setType(m.getMtype().equals("usr")?"user":"device");
			newDevices.add(device);
			userCodes.add(m.getMid());
			
			if(dstnos.contains(m.getMid())){
				
			}else{
				
			}
		}
		List<String> newDstnos = resourceRemoteService.querySerNodeList(newDevices);
		if(userCodes.size() > 0){
			List<FolderUserMap> userMaps = folderUserMapDao.findByUserNoIn(userCodes);
//			FolderUserMap userMap = queryUtil.queryUserMapByUserId(userMaps, )
		}
		
		
		
		if(dstnos.size() == 0){
			return;
		}
		
		StringWriter writer = new StringWriter();
		template.process(JSON.parseObject(JSON.toJSONString(group)), writer);
		String protocol = writer.toString();
		System.out.println(protocol);
		
		//查询本联网layerid
//		String localLayerId = resourceRemoteService.queryLocalLayerId();
		
		List<PassByBO> passbyBOs = new ArrayList<PassByBO>();
		for(String dstno : dstnos){
			JSONObject passbyContent = new JSONObject();
			passbyContent.put("cmd", "send_node_message");
			passbyContent.put("src_user", group.getOp());
			passbyContent.put("type", type);
			passbyContent.put("dst_no", dstno);
			passbyContent.put("content", protocol);
			
//			PassByBO passBy = new PassByBO().setLayer_id(localLayerId)
//											.setPass_by_content(passbyContent);
//			passbyBOs.add(passBy);
		}
		
	}

	private String generateUuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	private String generateTimestamp(){
		return String.valueOf(System.currentTimeMillis());
	}
	
}
