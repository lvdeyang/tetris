package com.sumavision.bvc.device.command.cascade.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;

//参考CommandCascadeUtil
@Component
public class GroupCascadeUtil {
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
//	/** 必须保证members包含主席 */
//	public List<MinfoBO> generateMinfoBOList(Collection<BusinessGroupMemberPO> members){
//		return generateMinfoBOList(members, null);
//	}
	
	/**
	 * 构建MinfoBO信息<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午4:34:47
	 * @param members
	 * @param chairmanMember
	 * @return
	 */
	/** 当members可能不包含主席时，需要输入主席 */
	public List<MinfoBO> generateMinfoBOList(Collection<BusinessGroupMemberPO> members, BusinessGroupMemberPO chairmanMember){
		
		List<MinfoBO> mlist = new ArrayList<MinfoBO>();
		if(chairmanMember == null){
			chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		}
		for(BusinessGroupMemberPO member : members){
			MinfoBO minfo = setMember(member, chairmanMember);
			mlist.add(minfo);
		}
		return mlist;
	}
	
	private MinfoBO setMember(BusinessGroupMemberPO member, BusinessGroupMemberPO chairmanMember){
		
		String mstatus = MemberStatus.CONNECT.equals(member.getGroupMemberStatus())?"1":"2";//成员状态，1表示正在业务、2表示暂时离开、3表示已退出
		MinfoBO minfo = new MinfoBO()
				.setMid(member.getCode())
				.setMname(member.getName())
				.setMstatus(mstatus)
				.setMtype(member.getGroupMemberType().getProtocalId());
		//pid 上级成员ID；会议组成员的上级成员ID为主席；ZH组成员的上级成员为其直接上级；主席 和最高级ZH员无上级不应显示此标签
		if(!member.getUuid().equals(chairmanMember.getUuid())){
			minfo.setPid(chairmanMember.getCode());
		}
		return minfo;
	}

	/**
	 *构建GroupBO信息（会议暂停、指挥停止、会议暂停恢复、指挥暂停恢复）<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午3:56:52
	 * @param group
	 */
	public GroupBO generateGroupBo(GroupPO group){
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members, chairmanMember);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setMlist(mlist);
		
		return groupBO;
	}
	
	/**
	 * 指挥/会议共用，媒体转发设备，媒体转发用户<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月24日 上午9:25:12
	 * @param group 
	 * @param bundlePOs 
	 * @param srcUserBos UserBO集合，只需要里面的userNo属性
	 * @param dstMembers 
	 * @return
	 * @throws Exception
	 */
	public GroupBO startDeviceForward(GroupPO group, List<BundlePO> bundlePOs, List<UserBO> srcUserBos, List<BusinessGroupMemberPO> dstMembers) throws Exception{
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members, chairmanMember);
		
		//目的用户号码列表
		List<String> mediaForwardMlist = new ArrayList<String>();
		for(BusinessGroupMemberPO dstMember : dstMembers){
			mediaForwardMlist.add(dstMember.getCode());
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
				.setOp(chairmanMember.getCode())
				.setMediaForwardMlist(mediaForwardMlist)
				.setMedialist(medialist)
				.setMlist(mlist);
		
		return groupBO;
	}
	
	
	 /** 还需要支持转发用户，并支持通过号码来查询出转发任务进行删除 */
	 /** 按标准，这是批量接口，但是在bvc里目前只能单个调度 */
	 /**
	 * 停止调度转发(级联转发参数要有转发源和目的的对应关系，但是协议里面没有，所以只能单个转发停止)<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月24日 下午1:31:06
	 * @param group
	 * @param srcCode 源号码
	 * @param dstMemberCode 目的号码
	 * @param opMember 操作人
	 */
	public GroupBO stopDeviceForward(GroupPO group, String srcCode, String dstMemberCode, BusinessGroupMemberPO opMember) throws Exception{
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members, chairmanMember);
		
		//目的用户号码列表
		List<String> mediaForwardMlist = new ArrayList<String>();
		mediaForwardMlist.add(dstMemberCode);
		
		//源号码列表
		List<String> medialist = new ArrayList<String>();
		medialist.add(srcCode);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(opMember.getCode())
				.setMediaForwardMlist(mediaForwardMlist)
				.setMedialist(medialist)
				.setMlist(mlist);
		
		return groupBO;
	}
	
	/**
	 * 专向指挥生成groupBO<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月24日 下午5:12:11
	 * @param group
	 * @param highUserId 上级userId
	 * @param lowUserId 下级userId
	 * @return
	 */
	public GroupBO secretGroupBO(GroupPO group, String highCode, String lowCode){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members, chairmanMember);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setTopid(highCode)
				.setMid(lowCode)
				.setMlist(mlist);
		
		return groupBO;
		
	}
	
	public GroupBO replaceGroupBO(GroupPO group, String opCode, String targetCode){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members, chairmanMember);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setTopid(opCode)
				.setMid(targetCode)
				.setMlist(mlist);
		
		return groupBO;		
	}

	public GroupBO authorizeGroupBO(GroupPO group, BusinessGroupMemberPO acceptMember, BusinessGroupMemberPO cmdMember){
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members, chairmanMember);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setTopid(cmdMember.getCode())
				.setMid(acceptMember.getCode())
				.setMlist(mlist);
		
		return groupBO;		
	}

	/**
	 * 生成协同指挥groupPO<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月5日 下午1:46:13
	 * @param group 业务组
	 * @param members 协同指挥成员
	 * @return
	 */
	
	public GroupBO agendaCooperation(GroupPO group, List<BusinessGroupMemberPO> cooperateMembers) {
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members, chairmanMember);
		List<String> cropList = cooperateMembers.stream().map(BusinessGroupMemberPO::getCode).collect(Collectors.toList());
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setCroplist(cropList)
				.setMlist(mlist);
		return groupBO;
	}

	/**
	 * 生成越级指挥GroupBO<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月5日 下午2:07:20
	 * @param group
	 * @param crossMembers 
	 * @return
	 */
	public GroupBO agendaCross(GroupPO group, List<BusinessGroupMemberPO> crossMembers) {
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members, chairmanMember);
		
		BusinessGroupMemberPO highMember = null;
		BusinessGroupMemberPO lowMember = null;
		
		if(crossMembers.get(0).getLevel() > crossMembers.get(1).getLevel()){
			highMember = crossMembers.get(0);
			lowMember = crossMembers.get(1);
		}else {
			highMember = crossMembers.get(1);
			lowMember = crossMembers.get(0);
		}
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setTopid(highMember.getCode())
				.setMid(lowMember.getCode())
				.setMlist(mlist);
		
		return groupBO;
	}
	
	/**
	 * 生成临时会议/指挥的GroupBO<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月30日 下午2:18:16
	 * @param group 会议信息
	 * @return
	 */
	public GroupBO saveAndStartTemporary(GroupPO group){
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		List<MinfoBO> mlist = generateMinfoBOList(members, chairmanMember);
		
		GroupBO groupBO = new GroupBO()
				.setGid(group.getUuid())
				.setOp(chairmanMember.getCode())
				.setGroupType(group.getEditStatus().getCode())
				.setSubject(group.getSubject())
				.setStime(DateUtil.format(group.getStartTime(), DateUtil.dateTimePattern))
				.setBizname(group.getName())
				.setCreatorid(chairmanMember.getCode())
				.setTopid(chairmanMember.getCode())
				.setMlist(mlist);
		return groupBO;
	}
	
}
