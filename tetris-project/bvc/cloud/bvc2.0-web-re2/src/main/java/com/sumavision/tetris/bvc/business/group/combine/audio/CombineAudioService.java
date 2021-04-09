package com.sumavision.tetris.bvc.business.group.combine.audio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineAudioDAO;
import com.sumavision.tetris.bvc.business.dao.CombineAudioPermissionDAO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.CombineAudioPermissionPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.AudioPriority;
import com.sumavision.tetris.bvc.model.agenda.CustomAudioDAO;
import com.sumavision.tetris.bvc.model.agenda.CustomAudioPO;
import com.sumavision.tetris.bvc.model.agenda.CustomAudioPermissionType;
import com.sumavision.tetris.bvc.model.agenda.SourceType;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.role.RoleUserMappingType;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelType;
import com.sumavision.tetris.bvc.util.AgendaRoleMemberUtil;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * AgendaExecuteService<br/>
 * <p>执行议程、修改角色</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月22日 上午10:03:43
 */
@Slf4j
@Service
public class CombineAudioService {
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private AgendaRoleMemberUtil agendaRoleMemberUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private BusinessCombineAudioDAO businessCombineAudioDao;
	
	@Autowired
	private CombineAudioPermissionDAO combineAudioPermissionDao;
	
	@Autowired
	private CustomAudioDAO customAudioDao;
	
	/** 级联使用：联网接入层id */
	private String localLayerId = null;
	
	/**CustomAudioPO中属性的分隔符*/
	private String propertySeparator = "-";
	
	/**CustomAudioPO属性集合的分隔符 */
	private String separator = "_";
	
	/**
	 * 创建音频模板的实例数据。包含持久化<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午2:48:49
	 * @param group
	 * @param agendaId
	 * @return （暂时没有使用）所有新建的“总的”音频的list（也就是allAudio。memberAudio被关联在allAudio中）
	 */
	public List<BusinessCombineAudioPO> agendaAudio(GroupPO group, Long agendaId, BusinessDeliverBO businessDeliverBO){
		
		//这里成员取目的成员：通过议程找议程转发再找目的成员
		List<AgendaForwardPO> agendaForwardList = agendaForwardDao.findByAgendaId(agendaId);
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardAndBusienssGroupMemberMap = agendaRoleMemberUtil.obtainAgendaForwardDstMemberMap(agendaForwardList, group.getMembers());
		Set<BusinessGroupMemberPO> members = agendaForwardAndBusienssGroupMemberMap.values().stream().flatMap( memberList->memberList.stream() ).collect(Collectors.toSet());
		
		AgendaPO agendaPo = agendaDao.findOne(agendaId);
		List<BusinessCombineAudioPO> newAllAudios = new ArrayList<BusinessCombineAudioPO>();
		
		//根据议程创建音频
		if(Boolean.TRUE.equals(agendaPo.getGlobalCustomAudio())){
			//Agenda
			CombineAudioPermissionPO p = combineAudioPermissionDao.findByGroupIdAndPermissionIdAndPermissionType(group.getId(), agendaId, CustomAudioPermissionType.AGENDA);
			if(p == null){
				p = new CombineAudioPermissionPO();
				p.setGroupId(group.getId());
				p.setPermissionType(CustomAudioPermissionType.AGENDA);
				p.setPermissionId(agendaId);
				p.setAgendaId(agendaId);
				combineAudioPermissionDao.save(p);
				
				//源
				List<CustomAudioPO> customAudioList = customAudioDao.findByPermissionIdInAndPermissionType(new ArrayListWrapper<Long>().add(agendaId).getList(), CustomAudioPermissionType.AGENDA);
				//新建混音
				BusinessCombineAudioPO combineAudio = new BusinessCombineAudioPO();
				newAllAudios.add(combineAudio);
				combineAudio.setSrcs(new ArrayList<BusinessCombineAudioSrcPO>());
				combineAudio.setMemberAudios(new ArrayList<BusinessCombineAudioPO>());
				combineAudio.setCombineAudioPermissions(new HashSet<CombineAudioPermissionPO>());
				combineAudio.getCombineAudioPermissions().add(p);
				//唯一判定：但是如果是根据议程配置音频实际只能有一种音频集合
				String audioUid = generateAudioUid(group.getId(), customAudioList);
				combineAudio.setAudioUid(audioUid);
				p.setAllAudio(combineAudio);
			
				businessCombineAudioDao.save(combineAudio);
				
				//拥有音频输出的成员都有哪些音频集合 
				Map<Long, List<BusinessCombineAudioSrcPO>> memberIdAndBusinessCombineAudioSrcPOMap = new HashMap<Long, List<BusinessCombineAudioSrcPO>>(); 
				for(CustomAudioPO audio: customAudioList){
					BusinessCombineAudioSrcPO audioSrc = new BusinessCombineAudioSrcPO();
					BusinessGroupMemberTerminalChannelPO terminalChannel = agendaRoleMemberUtil.obtainMemberTerminalChannelFromAgendaForwardSource(audio.getSourceId(), audio.getSourceType(), group.getMembers());
					audioSrc.setSourceId(audio.getSourceId());
					audioSrc.setSourceType(audio.getSourceType());
					audioSrc.setCombineAudio(combineAudio);
					audioSrc.set(terminalChannel);
					combineAudio.getSrcs().add(audioSrc);
					
					List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(audioSrc.getMemberId());
					if(audioSrcList==null){
						audioSrcList = new ArrayList<BusinessCombineAudioSrcPO>();
						memberIdAndBusinessCombineAudioSrcPOMap.put(audioSrc.getMemberId(), audioSrcList);
					}
					audioSrcList.add(audioSrc);
				}
				
				//如果没有声音源就不用判断每个成员应该听什么
				if(combineAudio.getSrcs().size() <= 0){
					combineAudio.setHasSource(false);
					log.info(group.getName()+"的"+agendaPo.getName()+"议程"+"不需要听任何声音");
				}else{
					if(combineAudio.getSrcs().size() > 1){
						combineAudio.setMix(true);
					}
					
					for (BusinessGroupMemberPO member : members) {
						List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(member.getId());
						if(audioSrcList != null){
							//这个成员应该听那些音频
							BusinessCombineAudioPO memberCombineAudio = new BusinessCombineAudioPO();
							memberCombineAudio.setIsAll(false);
							//每个成员的音频唯一标识不需要加
							memberCombineAudio.setDstMemberId(member.getId());
							memberCombineAudio.setAllAudio(combineAudio);
							combineAudio.getMemberAudios().add(memberCombineAudio);
							
							//去除自己的声音 
							List<BusinessCombineAudioSrcPO> myTempSrcs = new ArrayList<BusinessCombineAudioSrcPO>(combineAudio.getSrcs());
							myTempSrcs.removeAll(audioSrcList);
							List<BusinessCombineAudioSrcPO> mySrcs = new ArrayList<BusinessCombineAudioSrcPO>();
							for(BusinessCombineAudioSrcPO src : myTempSrcs){
								BusinessCombineAudioSrcPO mySrcPo = new BusinessCombineAudioSrcPO();
								mySrcPo.set(src);
								mySrcPo.setCombineAudio(memberCombineAudio);
								mySrcs.add(mySrcPo);
							}
							memberCombineAudio.setSrcs(mySrcs);
							
							if(memberCombineAudio.getSrcs().size() <= 0){
								log.info(member.getName() + "不需要听任何声音");
								memberCombineAudio.setHasSource(false);
							}else if(memberCombineAudio.getSrcs().size() >1){
								memberCombineAudio.setMix(true);
							}
						}
					}
				}
				businessCombineAudioDao.save(combineAudio);
				//看一看
				businessDeliverBO.getStartCombineAudios().add(combineAudio);
				if(combineAudio.getMemberAudios() != null){
					businessDeliverBO.getStartCombineAudios().addAll(combineAudio.getMemberAudios());
				}
			}else{
				//已经存在，不需处理
			}
		}
		//根据议程转发创建音频
		else if(Boolean.FALSE.equals(agendaPo.getGlobalCustomAudio())){
			//AgendaForward
			//将CustomAudioPO中对应该议程下所有议程转发筛选组合为map,其中对应map<agendaForwardId, CustomAudioPO集合>。
			List<AgendaForwardPO> forwards = agendaForwardDao.findByAgendaId(agendaId);
			List<Long> forwardIds = forwards.stream().map(forward->{return forward.getId();}).collect(Collectors.toList());
			//该议程下所有议程转发对应的源
			List<CustomAudioPO> allCustomAudioList= customAudioDao.findByPermissionIdInAndPermissionType(forwardIds, CustomAudioPermissionType.AGENDA_FORWARD);
			//将源组合为map
			Map<Long, List<CustomAudioPO>> agendaForwardIdAndCombineAudioMap = allCustomAudioList.stream().collect(Collectors.groupingBy(CustomAudioPO::getPermissionId));
			//将BusinessCombineAudioPO中所有的audioUid标识符取出，便于对比
			List<CombineAudioPermissionPO> allCombineAudioPermissionList = combineAudioPermissionDao.findByGroupIdAndPermissionIdInAndPermissionType(group.getId(), forwardIds, CustomAudioPermissionType.AGENDA_FORWARD);
			Map<String, BusinessCombineAudioPO> audioUidAndCombineAudioMap = allCombineAudioPermissionList.stream().filter(p->{
				BusinessCombineAudioPO audio= p.getAllAudio();
				if(audio == null || !BaseUtils.stringIsNotBlank(audio.getAudioUid())){
					return false;
				}
				return true;
			}).map(p->{
				return p.getAllAudio();
			}).collect(Collectors.toMap(BusinessCombineAudioPO::getAudioUid, Function.identity()));
			
			//开始构建真实数据，组件关系//TODO 有bug
			for (AgendaForwardPO forward : forwards) {
				
				CombineAudioPermissionPO p = null;
				for(CombineAudioPermissionPO permission: allCombineAudioPermissionList){
					if(forward.getId().equals(permission.getPermissionId())){
						p = permission;
					}
				}
				
				if(p == null){
					p = new CombineAudioPermissionPO();
					p.setGroupId(group.getId());
					p.setPermissionType(CustomAudioPermissionType.AGENDA_FORWARD);
					p.setPermissionId(forward.getId());
					p.setAgendaId(agendaId);
					p.setLayoutId(forward.getLayoutId());
					combineAudioPermissionDao.save(p);
					
					//每个议程转发下的源
					List<CustomAudioPO> customAudioList = agendaForwardIdAndCombineAudioMap.get(forward.getId());
					//混音判重
					String audioUid = generateAudioUid(group.getId(), customAudioList);
					BusinessCombineAudioPO combineAudio = audioUidAndCombineAudioMap.get(audioUid);
					if(combineAudio != null){
						p.setAllAudio(combineAudio);
						combineAudio.getCombineAudioPermissions().add(p);
						businessCombineAudioDao.save(combineAudio);
					}else{
						//新建混音
						//源是空的时候创建一个空壳BusinessCombineAudioPO
						if(customAudioList == null){
							combineAudio = new BusinessCombineAudioPO();
							combineAudio.setSrcs(new ArrayList<BusinessCombineAudioSrcPO>());
							combineAudio.setMemberAudios(new ArrayList<BusinessCombineAudioPO>());
							combineAudio.setCombineAudioPermissions(new HashSet<CombineAudioPermissionPO>());
							combineAudio.getCombineAudioPermissions().add(p);
							combineAudio.setAudioUid(audioUid);
							combineAudio.setHasSource(false);
							p.setAllAudio(combineAudio);
							
							businessCombineAudioDao.save(combineAudio);
							return newAllAudios;
						}
						
						combineAudio = new BusinessCombineAudioPO();
						newAllAudios.add(combineAudio);
						combineAudio.setSrcs(new ArrayList<BusinessCombineAudioSrcPO>());
						combineAudio.setMemberAudios(new ArrayList<BusinessCombineAudioPO>());
						combineAudio.setCombineAudioPermissions(new HashSet<CombineAudioPermissionPO>());
						combineAudio.getCombineAudioPermissions().add(p);
						combineAudio.setAudioUid(audioUid);
						p.setAllAudio(combineAudio);
						businessCombineAudioDao.save(combineAudio);//save，否则会报错
						
						//拥有音频输出的成员都有哪些音频集合
						Map<Long, List<BusinessCombineAudioSrcPO>> memberIdAndBusinessCombineAudioSrcPOMap = new HashMap<Long, List<BusinessCombineAudioSrcPO>>(); 
						for(CustomAudioPO audio: customAudioList){
							BusinessCombineAudioSrcPO audioSrc = new BusinessCombineAudioSrcPO();
							BusinessGroupMemberTerminalChannelPO terminalChannel = agendaRoleMemberUtil.obtainMemberTerminalChannelFromAgendaForwardSource(
									audio.getSourceId(), audio.getSourceType(), group.getMembers());
							audioSrc.setSourceId(audio.getSourceId());
							audioSrc.setSourceType(audio.getSourceType());
							audioSrc.setCombineAudio(combineAudio);
							audioSrc.set(terminalChannel);
							combineAudio.getSrcs().add(audioSrc);
							
							//拥有音频输出的成员都有哪些音频集合构建
							List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(audioSrc.getMemberId());
							if(audioSrcList==null){
								audioSrcList = new ArrayList<BusinessCombineAudioSrcPO>();
								memberIdAndBusinessCombineAudioSrcPOMap.put(audioSrc.getMemberId(), audioSrcList);
							}
							audioSrcList.add(audioSrc);
						}
						
						//如果没有声音源就不用判断每个成员应该听什么
						if(combineAudio.getSrcs().size() <= 0){
							combineAudio.setHasSource(false);
							log.info(group.getName()+"的"+agendaPo.getName()+"议程"+"不需要听任何声音");
						}else{
							if(combineAudio.getSrcs().size() > 1){
								combineAudio.setMix(true);
							}
							
							for (BusinessGroupMemberPO member : agendaForwardAndBusienssGroupMemberMap.get(forward)) {
								List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(member.getId());
								if(audioSrcList != null){
									//这个成员应该听那些音频
									BusinessCombineAudioPO memberCombineAudio = new BusinessCombineAudioPO();
									memberCombineAudio.setIsAll(false);
									//每个成员的音频唯一标识不需要加
									memberCombineAudio.setDstMemberId(member.getId());
									memberCombineAudio.setAllAudio(combineAudio);
									combineAudio.getMemberAudios().add(memberCombineAudio);
									
									//去除自己的声音 
									List<BusinessCombineAudioSrcPO> myTempSrcs = new ArrayList<BusinessCombineAudioSrcPO>(combineAudio.getSrcs());
									myTempSrcs.removeAll(audioSrcList);
									List<BusinessCombineAudioSrcPO> mySrcs = new ArrayList<BusinessCombineAudioSrcPO>();
									for(BusinessCombineAudioSrcPO src : myTempSrcs){
										BusinessCombineAudioSrcPO mySrcPo = new BusinessCombineAudioSrcPO();
										mySrcPo.set(src);
										mySrcPo.setCombineAudio(memberCombineAudio);
										mySrcs.add(mySrcPo);
									}
									memberCombineAudio.setSrcs(mySrcs);
									if(memberCombineAudio.getSrcs().size() <= 0){
										log.info(member.getName() + "不需要听任何声音");
										memberCombineAudio.setHasSource(false);
									}else if(memberCombineAudio.getSrcs().size() >1){
										memberCombineAudio.setMix(true);
									}
								}
							}
						}
						businessCombineAudioDao.save(combineAudio);

						businessDeliverBO.getStartCombineAudios().add(combineAudio);
						if(combineAudio.getMemberAudios() != null){
							businessDeliverBO.getStartCombineAudios().addAll(combineAudio.getMemberAudios());
						}						
					}
				}else{
					BusinessCombineAudioPO combineAudio = p.getAllAudio();
					
					businessDeliverBO.getStartCombineAudios().add(combineAudio);
					if(combineAudio.getMemberAudios() != null){
						businessDeliverBO.getStartCombineAudios().addAll(combineAudio.getMemberAudios());
					}
				}
			}
		}
		return newAllAudios;
	}
	
	/**
	 * 为1：1角色自动生成混音。<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月29日 上午10:21:31
	 * @param group 业务组
	 * @param businessDeliver 命令合并工具
	 * @return
	 */
	public BusinessCombineAudioPO generateAutoCombineAudio(GroupPO group, Long agendaId,BusinessDeliverBO businessDeliverBO){
		
		//通过会议组找到当前会议组中所有1:1的角色，角色找到成员，成员找到音频源集合。
		//将音频源集合包装为混音。
		List<RolePO> roleList = roleDao.findByBusinessIdAndRoleUserMappingType(group.getId(), RoleUserMappingType.ONE_TO_ONE);
		List<InternalRoleType> internalRoleTypeList = Stream.of(InternalRoleType.values()).collect(Collectors.toList());
		List<RolePO> internalRoleList = roleDao.findByRoleUserMappingTypeAndInternalRoleTypeIn(RoleUserMappingType.ONE_TO_ONE, internalRoleTypeList);
		roleList.addAll(internalRoleList);
		List<BusinessGroupMemberPO> memberList = group.getMembers();
		List<BusinessGroupMemberPO> audioSrcMemberList = new ArrayList<BusinessGroupMemberPO>();
		
		for(BusinessGroupMemberPO member : memberList){
			for(RolePO role : roleList){
				if(role.getId().equals(member.getRoleId())){
					audioSrcMemberList.add(member);
				}
			}
		}
		
		//全局混音逻辑上与议程、议程转发混音有区别，也不符合判重的标准。建议每次更改把之前的删除。
		CombineAudioPermissionPO p = new CombineAudioPermissionPO();
		p = new CombineAudioPermissionPO();
		p.setGroupId(group.getId());
		p.setPermissionType(CustomAudioPermissionType.GROUP);
		p.setPermissionId(group.getId());
		p.setAgendaId(agendaId);
		combineAudioPermissionDao.save(p);
		
		BusinessCombineAudioPO combineAudio = new BusinessCombineAudioPO();
		combineAudio.setSrcs(new ArrayList<BusinessCombineAudioSrcPO>());
		combineAudio.setMemberAudios(new ArrayList<BusinessCombineAudioPO>());
		combineAudio.setCombineAudioPermissions(new HashSet<CombineAudioPermissionPO>());
		String uid = audioSrcMemberList.stream().map(member-> member.getId().toString()).sorted().collect(Collectors.joining("-", "(", ")"));
		combineAudio.setAudioUid(group.getId()+"_auto"+uid);
		
		p.setAllAudio(combineAudio);
		combineAudio.getCombineAudioPermissions().add(p);
		businessCombineAudioDao.save(combineAudio);
		
		List<BusinessCombineAudioSrcPO> combineAudioSrcList = new ArrayList<BusinessCombineAudioSrcPO>();
		Map<Long, List<BusinessCombineAudioSrcPO>> memberIdAndBusinessCombineAudioSrcPOMap = new HashMap<Long, List<BusinessCombineAudioSrcPO>>(); 
		for(BusinessGroupMemberPO audioSrcMember : audioSrcMemberList){
			List<BusinessGroupMemberTerminalChannelPO> channels = audioSrcMember.getChannels();
			if(channels != null){
				for(BusinessGroupMemberTerminalChannelPO channel : channels){
					BusinessGroupMemberTerminalChannelPO audioEncodeChannel = TerminalChannelType.AUDIO_ENCODE.equals(channel.getTerminalChannelType())?channel : null;
					if(audioEncodeChannel != null){
						BusinessCombineAudioSrcPO combineAudioSrc = new BusinessCombineAudioSrcPO();
						combineAudioSrc.set(audioEncodeChannel);
						combineAudioSrc.setSourceId(audioSrcMember.getId());
						combineAudioSrc.setSourceType(SourceType.ROLE_CHANNEL);
						combineAudioSrc.setCombineAudio(combineAudio);
						combineAudioSrcList.add(combineAudioSrc);
						
						List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(combineAudioSrc.getMemberId());
						if(audioSrcList==null){
							audioSrcList = new ArrayList<BusinessCombineAudioSrcPO>();
							memberIdAndBusinessCombineAudioSrcPOMap.put(combineAudioSrc.getMemberId(), audioSrcList);
						}
						audioSrcList.add(combineAudioSrc);
					}
				}
			}
		}
		
		List<AgendaForwardPO> agendaForwardList = agendaForwardDao.findByAgendaId(agendaId);
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardAndBusienssGroupMemberMap = agendaRoleMemberUtil.obtainAgendaForwardDstMemberMap(agendaForwardList, group.getMembers());
		Set<BusinessGroupMemberPO> members = agendaForwardAndBusienssGroupMemberMap.values().stream().flatMap(forwardMembers->forwardMembers.stream() ).collect(Collectors.toSet());
		
		if(combineAudioSrcList.size() > 0){
			combineAudio.getSrcs().addAll(combineAudioSrcList);
			if(combineAudioSrcList.size() > 1){
				combineAudio.setMix(true);
			}
			
			for (BusinessGroupMemberPO member : members) {
				List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(member.getId());
				if(audioSrcList != null){
					//这个成员应该听那些音频
					BusinessCombineAudioPO memberCombineAudio = new BusinessCombineAudioPO();
					memberCombineAudio.setIsAll(false);
					//每个成员的音频唯一标识不需要加
					memberCombineAudio.setDstMemberId(member.getId());
					memberCombineAudio.setAllAudio(combineAudio);
					
					//去除自己的声音
					List<BusinessCombineAudioSrcPO> myTempSrcs = new ArrayList<BusinessCombineAudioSrcPO>(combineAudio.getSrcs());
					myTempSrcs.removeAll(audioSrcList);
					List<BusinessCombineAudioSrcPO> mySrcs = new ArrayList<BusinessCombineAudioSrcPO>();
					for(BusinessCombineAudioSrcPO src : myTempSrcs){
						BusinessCombineAudioSrcPO mySrcPo = new BusinessCombineAudioSrcPO();
						mySrcPo.set(src);
						mySrcPo.setCombineAudio(memberCombineAudio);
						mySrcs.add(mySrcPo);
					}
					memberCombineAudio.setSrcs(mySrcs);
					if(memberCombineAudio.getSrcs().size() <= 0){
						log.info(member.getName() + "不需要听任何声音");
						memberCombineAudio.setHasSource(false);
					}else if(memberCombineAudio.getSrcs().size() >= 1){
						if(memberCombineAudio.getSrcs().size() == 1){
							memberCombineAudio.setMix(false);
						}else{
							memberCombineAudio.setMix(true);
						}
						combineAudio.getMemberAudios().add(memberCombineAudio);
					}
				}
			}
		}else{
			combineAudio.setHasSource(false);
			log.info(group.getName()+"的自动配置不需要听任何声音");
		}
		combineAudio.setAudioUid(generateAutoAudioUid(group.getId(),combineAudioSrcList));
		businessCombineAudioDao.save(combineAudio);
		
		businessDeliverBO.getStartCombineAudios().add(combineAudio);
		if(combineAudio.getMemberAudios() != null){
			businessDeliverBO.getStartCombineAudios().addAll(combineAudio.getMemberAudios());
		}
		return combineAudio;
	}
	
	public BusinessCombineAudioPO queryAudioForMember(
			Collection<CombineAudioPermissionPO> audioPers, AgendaPO agenda, Long agendaForwardId, Long memberId, GroupPO group){
		CustomAudioPermissionType permissionType = null;
		Long permissionId = null;
		AudioPriority audioPriority = agenda.getAudioPriority();
		Boolean globalCustomAudio = agenda.getGlobalCustomAudio();
		if(AudioPriority.GLOBAL_FIRST.equals(audioPriority)){
			permissionType = CustomAudioPermissionType.GROUP;
			permissionId = group.getId();
		}else if(globalCustomAudio){
			permissionType = CustomAudioPermissionType.AGENDA;
			permissionId = agenda.getId();
		}else{
			permissionType = CustomAudioPermissionType.AGENDA_FORWARD;
			permissionId = agendaForwardId;
		}
		CombineAudioPermissionPO audioPer = tetrisBvcQueryUtil.queryCombineAudioPermissionByPermissionTypeAndPermissionId(
				audioPers, permissionType, permissionId);
		BusinessCombineAudioPO audioPO = queryAudioForMember(audioPer, memberId);
		
		return audioPO;
	}
	
	private BusinessCombineAudioPO queryAudioForMember(CombineAudioPermissionPO audioPer, Long memberId){
		if(audioPer == null) return null;
		
		BusinessCombineAudioPO allAudio = audioPer.getAllAudio();
		if(allAudio == null) return null;
		
		//成员音频中如果有这个成员的，就返回
		List<BusinessCombineAudioPO> memberAudios = allAudio.getMemberAudios();
		if(memberAudios == null) memberAudios = new ArrayList<BusinessCombineAudioPO>();
		for(BusinessCombineAudioPO memberAudio : memberAudios){
			if(memberId.equals(memberAudio.getDstMemberId())){
				return memberAudio;
			}
		}
		
		//如果没有，就返回“总的音频”
		return allAudio;
	}

	/**
	 * 通过CustomAudioPO集合生成该集合的唯一标识字符串
	 * 举例：propertySeparator="-";separator="_"
	 * 生成的标识符就是：groudId_(sourceId-sourceType)_(...)_(...)<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 下午2:21:52
	 * @param customAudioList CustomAudioPO集合
	 * @return
	 */
	public String generateAudioUid(Long groupId, List<CustomAudioPO> customAudioList){
		
		StringBuilder audioUid = new StringBuilder().append(groupId.toString());
		
		if(BaseUtils.collectionIsNotBlank(customAudioList)){
			List<CustomAudioPO> sortedList = customAudioList.stream().sorted(Comparator.comparing(CustomAudioPO::getSourceId)).collect(Collectors.toList());
			String extendUid = sortedList.stream().map(audio->{
				StringBuilder audioStr = new StringBuilder();
				audioStr.append("(")
						.append(audio.getSourceId().toString())
						.append(propertySeparator)
						.append(audio.getSourceType().toString())
						.append(")");
				 return audioStr.toString();
			}).sorted().collect(Collectors.joining(separator));
			
			audioUid.append("_").append(extendUid);
		}
		
		return audioUid.toString();
	}
	
	/**
	 * 为自动生成的混音添加唯一标识符(只能是会场音频通道)
	 * 举例：propertySeparator="-";separator="_"
	 * 生成的标识符就是：auto_groudId_(sourceId-sourceType)_(...)_(...)<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 下午2:21:52
	 * @param customAudioList CustomAudioPO集合
	 * @return
	 */
	public String generateAutoAudioUid(Long groupId, List<BusinessCombineAudioSrcPO> audioSrcList){
		
		StringBuilder audioUid = new StringBuilder().append(CustomAudioPO.autoAudioUidPrefix).append(separator).append(groupId.toString());
		
		if(BaseUtils.collectionIsNotBlank(audioSrcList)){
//			//如果是角色要找到角色音频对应的角色对应的会场通道
//			List<RolePO>
//			List<BusinessGroupMemberPO> memberOfRole = new ArrayList<BusinessGroupMemberPO>();
//			List<BusinessGroupMemberTerminalChannelPO>  audioChannels = memberOfRole.stream().flatMap(member->{
//				return member.getChannels().stream();
//			}).collect(Collectors.toList());
//			
//			List<String> memberChannelUidOfRole = audioChannels.stream().map(channel->{
//				StringBuilder audioStr = new StringBuilder();
//				audioStr.append("(")
//						.append(channel.getId().toString())
//						.append(propertySeparator)
//						.append(SourceType.GROUP_MEMBER_CHANNEL.toString())
//						.append(")");
//				 return audioStr.toString();
//			}).collect(Collectors.toList());
			
			List<BusinessCombineAudioSrcPO> sortedSrcList = audioSrcList.stream().sorted(Comparator.comparing(BusinessCombineAudioSrcPO::getSourceId)).collect(Collectors.toList());
			String extendUid = sortedSrcList.stream().map(audio->{
				StringBuilder audioStr = new StringBuilder();
				audioStr.append("(")
						.append(audio.getSourceId().toString())
						.append(propertySeparator)
						.append(audio.getSourceType().toString())
						.append(")");
				 return audioStr.toString();
			}).sorted().collect(Collectors.joining(separator));
			
			audioUid.append("_").append(extendUid);
		}
		
		return audioUid.toString();
	}
	
	/**
	 * 清理混音<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月14日 下午4:35:15
	 * @param group 业务组id
	 * @param agendaId 议程id
	 * @param businessDeliverBO 
	 */
	public void clearCombineAudio(GroupPO group, BusinessDeliverBO businessDeliverBO){
		
		//删音频
		List<CombineAudioPermissionPO> audioPers = combineAudioPermissionDao.findByGroupId(group.getId());
		Set<Long> aIds = new HashSet<Long>();
		for(CombineAudioPermissionPO audioPer : audioPers){
			BusinessCombineAudioPO allAudio = audioPer.getAllAudio();
			if(allAudio != null){
				businessDeliverBO.getStopCombineAudios().add(allAudio);
				aIds.add(allAudio.getId());
				List<BusinessCombineAudioPO> memberAudios = allAudio.getMemberAudios();
				if(memberAudios != null){
					businessDeliverBO.getStopCombineAudios().addAll(memberAudios);
				}
			}
		}
		businessCombineAudioDao.deleteByIdIn(aIds);
	}
	
	/**
	 * 清理议程转发混音<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月14日 下午4:35:15
	 * @param group 业务组id
	 * @param agendaId 议程id
	 * @param businessDeliverBO 
	 */
	public void clearAgendaForwardCombineAudio(GroupPO group, List<AgendaForwardPO> agendaForwards, BusinessDeliverBO businessDeliverBO){
		
//		//通过议程id或者议程转发id查找所有的混音/单音频。
		List<Long> permissionIdList = new ArrayList<Long>();
		List<Long> agendaForwardIdList = agendaForwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
		permissionIdList.addAll(agendaForwardIdList);
		
		//所有的混音
		List<BusinessCombineAudioPO> combineAudioList = combineAudioPermissionDao.findByGroupIdAndPermissionIdInAndPermissionType(group.getId(), permissionIdList, CustomAudioPermissionType.AGENDA_FORWARD)
																				 .stream().map(CombineAudioPermissionPO::getAllAudio)
																				 .filter(combineAudio -> combineAudio != null).collect(Collectors.toList());
		
		businessDeliverBO.getStopCombineAudios().addAll(combineAudioList);
		businessCombineAudioDao.deleteByIdIn(combineAudioList.stream().map(BusinessCombineAudioPO::getId).collect(Collectors.toList()));
	}
	
	/**
	 * 清理议程全局混音<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月30日 下午3:58:55
	 * @param groupId 业务组id
	 */
	public void clearAutoCombineAudio(Long groupId){
		CombineAudioPermissionPO p = combineAudioPermissionDao.findByGroupIdAndPermissionIdAndPermissionType(groupId, groupId, CustomAudioPermissionType.GROUP);
		if(p != null){
			if(p.getAllAudio() != null){
				businessCombineAudioDao.delete(p.getAllAudio());
			}
		}
	}
	
	/**
	 * 更新混音：最消耗性能的是新建混音，尽量复用混音。<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月15日 下午1:10:10
	 * @param group
	 * @param nextAgenda
	 * @param businessDeliverBO
	 */
	//TODO 最后需要再次梳理
	public void updateCombineAudio(GroupPO group, AgendaPO nextAgenda, BusinessDeliverBO businessDeliverBO, Long oldAgendaId){
	
		if(businessDeliverBO == null) businessDeliverBO = new BusinessDeliverBO().setGroup(group);
		List<AgendaForwardPO> agendaForwards = agendaForwardDao.findByAgendaId(nextAgenda.getId());
		List<AgendaForwardPO> oldAgendaForwards = agendaForwardDao.findByAgendaId(oldAgendaId);

		List<CombineAudioPermissionPO> oldCombineAudioPermissionList = combineAudioPermissionDao.findByGroupId(group.getId());
		CustomAudioPermissionType oldCustomAudioPermissionType = null;
		
		if(oldCombineAudioPermissionList.size()>0){
			oldCustomAudioPermissionType = oldCombineAudioPermissionList.get(0).getPermissionType();
		}else {
			//议程执行前没有声音，处理新建音频
			if(AudioPriority.AGENDA_FIRST.equals(nextAgenda.getAudioPriority())){
				agendaAudio(group, nextAgenda.getId(), businessDeliverBO);
			}else if(AudioPriority.GLOBAL_FIRST.equals(nextAgenda.getAudioPriority())){
				generateAutoCombineAudio(group, nextAgenda.getId(), businessDeliverBO);
			}
			return;
		}
		
		AudioPriority oldAudioPriority = null;
		Boolean oldGlobalCustomAudio = null;
		if (CustomAudioPermissionType.GROUP.equals(oldCustomAudioPermissionType)) {
			oldAudioPriority = AudioPriority.GLOBAL_FIRST;
		}else if(CustomAudioPermissionType.AGENDA.equals(oldCustomAudioPermissionType)){
			oldAudioPriority = AudioPriority.AGENDA_FIRST;
			oldGlobalCustomAudio = true;
		}else if(CustomAudioPermissionType.AGENDA_FORWARD.equals(oldCustomAudioPermissionType)){
			oldAudioPriority = AudioPriority.AGENDA_FIRST;
			oldGlobalCustomAudio = false;
		}
		
		if((oldAudioPriority.equals(nextAgenda.getAudioPriority()) && oldGlobalCustomAudio == null) || 
				(oldAudioPriority.equals(nextAgenda.getAudioPriority()) && oldGlobalCustomAudio.equals(nextAgenda.getGlobalCustomAudio()))){
			//TODO：对比之前的关系，对于不相同的uid做更新
			if(AudioPriority.GLOBAL_FIRST.equals(oldAudioPriority)){
				List<RolePO> roleList = roleDao.findByBusinessIdAndRoleUserMappingType(group.getId(), RoleUserMappingType.ONE_TO_ONE);
				List<InternalRoleType> internalRoleTypeList = Stream.of(InternalRoleType.values()).collect(Collectors.toList());
				List<RolePO> internalRoleList = roleDao.findByRoleUserMappingTypeAndInternalRoleTypeIn(RoleUserMappingType.ONE_TO_ONE, internalRoleTypeList);
				roleList.addAll(internalRoleList);
				List<BusinessGroupMemberPO> memberList = group.getMembers();
				List<BusinessGroupMemberPO> audioSrcMemberList = new ArrayList<BusinessGroupMemberPO>();
				
				for(BusinessGroupMemberPO member : memberList){
					for(RolePO role : roleList){
						if(role.getId().equals(member.getRoleId())){
							audioSrcMemberList.add(member);
						}
					}
				}
				
				String uid = audioSrcMemberList.stream().map(member-> {
					return new StringBuffer().append(member.getId().toString()).append(propertySeparator).append(SourceType.ROLE_CHANNEL.toString()).toString();
				}).sorted().collect(Collectors.joining(separator, "(", ")"));
				String newUid = CustomAudioPO.autoAudioUidPrefix+separator+group.getId()+separator+uid;
				BusinessCombineAudioPO oldBusinessCombineAudio = oldCombineAudioPermissionList.get(0).getAllAudio();
				String oldAudioUid = oldBusinessCombineAudio.getAudioUid();
				if(newUid.equals(oldAudioUid)){
					System.out.println("混音源相同");
					hasSameCombineAudioUid(group, oldBusinessCombineAudio, businessDeliverBO, oldAgendaId, agendaForwards);
				}else{
					rebuildCombineAudio(group, nextAgenda, oldCombineAudioPermissionList, businessDeliverBO);
				}
				
			}else if(AudioPriority.AGENDA_FIRST.equals(oldAudioPriority) && oldGlobalCustomAudio.equals(true)){
				
				List<CustomAudioPO> customAudioList = customAudioDao.findByPermissionIdInAndPermissionType(new ArrayListWrapper<Long>().add(nextAgenda.getId()).getList(), CustomAudioPermissionType.AGENDA);
				String newAudioUid = generateAudioUid(group.getId(), customAudioList);
				BusinessCombineAudioPO oldBusinessCombineAudio = oldCombineAudioPermissionList.get(0).getAllAudio();
				String oldAudioUid = oldCombineAudioPermissionList.get(0).getAllAudio().getAudioUid();
				if(newAudioUid.equals(oldAudioUid)){
					hasSameCombineAudioUid(group, oldBusinessCombineAudio, businessDeliverBO, oldAgendaId, agendaForwards);
//					//处理议程转发目的发生变化。只需要考虑原来混音和现在混音有没有发生变化。
//					List<Long> agendaForwardIdList = agendaForwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
//					if(agendaForwardIdList != null){
//						
//						//现在和以前一致的声音源。然后先将现在配置的目的成员不是声音源的去除
//						//现在需要单独混音的目的成员id。
//						Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardAndBusienssGroupMemberMap = agendaRoleMemberUtil.obtainAgendaForwardDstMemberMap(agendaForwards, group.getMembers());
//						Set<BusinessGroupMemberPO> members = agendaForwardAndBusienssGroupMemberMap.values().stream().flatMap( memberList->memberList.stream() ).collect(Collectors.toSet());
////						Map<Long, BusinessGroupMemberPO> memberIdAndMemberList = members.stream().collect(Collectors.toMap(BusinessGroupMemberPO::getId, Function.identity()));
//						List<Long> memberIdList = members.stream().map(BusinessGroupMemberPO::getId).collect(Collectors.toList());
//						List<BusinessCombineAudioSrcPO> combineAudioSrcList = oldBusinessCombineAudio.getSrcs();
//						
//						//原来需要单独混音的目的成员id
//						List<Long> oldDstmemberIdList = new ArrayList<Long>();
//						if(oldBusinessCombineAudio != null){
//							oldDstmemberIdList = oldBusinessCombineAudio.getMemberAudios().stream().map(BusinessCombineAudioPO::getDstMemberId).collect(Collectors.toList());
//						}
//						
//						//现在需要单独混音的目的成员id
//						if(combineAudioSrcList != null){
//							List<Long> srcMemberIdList = combineAudioSrcList.stream().map(BusinessCombineAudioSrcPO::getMemberId).collect(Collectors.toList());
//							memberIdList.retainAll(srcMemberIdList);
//						}
//						
//						//将要删除的目的对应的混音和要添加的目的对应的混音添加
//						List<Long> sameMemberIdList = new ArrayList<Long>(memberIdList);
//						sameMemberIdList.retainAll(oldDstmemberIdList);
//						memberIdList.removeAll(sameMemberIdList);
//						oldDstmemberIdList.removeAll(sameMemberIdList);
//						
//						//可以复用的混音，多余后边删除
//						List<BusinessCombineAudioPO> couldReuseCombineAudioList = new ArrayList<BusinessCombineAudioPO>();
//						Map<Long, BusinessCombineAudioPO> memberIdAndCombineAudio = oldBusinessCombineAudio.getMemberAudios().stream().collect(Collectors.toMap(BusinessCombineAudioPO::getDstMemberId, Function.identity()));
//						for(Long memberId :oldDstmemberIdList){
//							couldReuseCombineAudioList.add(memberIdAndCombineAudio.get(memberId));
//						}
//						
//						//处理将要删除和新建的混音
//						Map<Long, List<BusinessCombineAudioSrcPO>> memberIdAndBusinessCombineAudioSrcPOMap = combineAudioSrcList.stream().collect(Collectors.groupingBy(BusinessCombineAudioSrcPO::getMemberId));
//						List<BusinessCombineAudioPO> newMemberCombineAudioList = new ArrayList<BusinessCombineAudioPO>();
//						//建立成员混音与全部混音的单向连接
//						for (Long memberId : memberIdList) {
//							List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(memberId);
//							if(audioSrcList != null){
//								//这个成员应该听那些音频
//								BusinessCombineAudioPO memberCombineAudio = new BusinessCombineAudioPO();
//								memberCombineAudio.setIsAll(false);
//								//每个成员的音频唯一标识不需要加
//								memberCombineAudio.setDstMemberId(memberId);
//								memberCombineAudio.setAllAudio(oldBusinessCombineAudio);
//								
//								//去除自己的声音
//								List<BusinessCombineAudioSrcPO> myTempSrcs = new ArrayList<BusinessCombineAudioSrcPO>(oldBusinessCombineAudio.getSrcs());
//								myTempSrcs.removeAll(audioSrcList);
//								List<BusinessCombineAudioSrcPO> mySrcs = new ArrayList<BusinessCombineAudioSrcPO>();
//								for(BusinessCombineAudioSrcPO src : myTempSrcs){
//									BusinessCombineAudioSrcPO mySrcPo = new BusinessCombineAudioSrcPO();
//									mySrcPo.set(src);
//									mySrcPo.setCombineAudio(memberCombineAudio);
//									mySrcs.add(mySrcPo);
//								}
//								memberCombineAudio.setSrcs(mySrcs);
//								if(memberCombineAudio.getSrcs().size() <= 0){
//									log.info("成员id： "+ memberId + " 不需要听任何声音");
//									memberCombineAudio.setHasSource(false);
//								}else if(memberCombineAudio.getSrcs().size() >= 1){
//									if(memberCombineAudio.getSrcs().size() == 1){
//										memberCombineAudio.setMix(false);
//									}else{
//										memberCombineAudio.setMix(true);
//									}
//								}
//								newMemberCombineAudioList.add(memberCombineAudio);
//							}
//						}
//						changeCombineAudio(couldReuseCombineAudioList, newMemberCombineAudioList, businessDeliverBO);
//					}else {
//						//将所有的混音都清理
//						combineAudioService.clearCombineAudio(group, oldAgendaId, businessDeliverBO);
//					}
				}else{
					rebuildCombineAudio(group, nextAgenda, oldCombineAudioPermissionList, businessDeliverBO);
//					//处理议程转发源和目的都发生变化。将之前的所有混音都设置为可以复用，再创建新的混音（所有的成员混音要分开处理）
//					List<BusinessCombineAudioPO> couldReuseCombineAudios = new ArrayList<BusinessCombineAudioPO>(); 
//					couldReuseCombineAudios.add(oldBusinessCombineAudio);
//					couldReuseCombineAudios.addAll(oldBusinessCombineAudio.getMemberAudios());
//					//TODO 以后还要优化
//					clearCombineAudio(group, oldAgendaId, businessDeliverBO);
//					agendaAudio(group, nextAgenda.getId(), businessDeliverBO);
				}
			}else if(AudioPriority.AGENDA_FIRST.equals(oldAudioPriority) && oldGlobalCustomAudio.equals(false)){
				//先对比议程转发是否是之前的,新建的要另外处理
				List<AgendaForwardPO> sameAgendaForwards = new ArrayList<AgendaForwardPO>(agendaForwards);
				sameAgendaForwards.retainAll(oldAgendaForwards);
				
				//循环议程模式
				for(AgendaForwardPO agendaForward : sameAgendaForwards){
					List<CustomAudioPO> customAudioList = customAudioDao.findByPermissionIdInAndPermissionType(new ArrayListWrapper<Long>().add(agendaForward.getId()).getList(), CustomAudioPermissionType.AGENDA_FORWARD);
					String newAudioUid = generateAudioUid(group.getId(), customAudioList);
					BusinessCombineAudioPO oldBusinessCombineAudio = null;
					for(CombineAudioPermissionPO p : oldCombineAudioPermissionList){
						if(p.getPermissionId().equals(agendaForward.getId())){
							oldBusinessCombineAudio = p.getAllAudio();
							break;
						}
					}
					//为空说明该议程转发是新建的,需要单独处理
					if(oldBusinessCombineAudio == null) continue;
					String oldAudioUid = oldBusinessCombineAudio.getAudioUid();
					if(oldAudioUid.equals(newAudioUid)){
						hasSameCombineAudioUid(group, oldBusinessCombineAudio, businessDeliverBO, oldAgendaId, new ArrayListWrapper<AgendaForwardPO>().add(agendaForward).getList());
					}else{
						//这里应该是重新构建议程转发的混音
						rebuildAgendaForwardCombineAudio(group, agendaForward, businessDeliverBO);
					}
				}
				
				//需要新建的议程转发
				List<AgendaForwardPO> addNewAgendaForwardsAgendaForwards = new ArrayList<AgendaForwardPO>(agendaForwards);
				addNewAgendaForwardsAgendaForwards.removeAll(sameAgendaForwards);
				for(AgendaForwardPO agendaForward : addNewAgendaForwardsAgendaForwards){
					rebuildAgendaForwardCombineAudio(group, agendaForward, businessDeliverBO);
				}
				
				//需要删除的议程转发
				List<AgendaForwardPO> oldDeleteAgendaForwards = new ArrayList<AgendaForwardPO>(oldAgendaForwards);
				oldDeleteAgendaForwards.removeAll(sameAgendaForwards);
				clearAgendaForwardCombineAudio(group, oldDeleteAgendaForwards, businessDeliverBO);
			} 
		}else {
			rebuildCombineAudio(group, nextAgenda, oldCombineAudioPermissionList, businessDeliverBO);
		}
		
	}
	
	public void hasSameCombineAudioUid(GroupPO group,
									   BusinessCombineAudioPO oldBusinessCombineAudio, 
									   BusinessDeliverBO businessDeliverBO, 
									   Long oldAgendaId, 
									   List<AgendaForwardPO> agendaForwards){

		//处理议程转发目的发生变化。只需要考虑原来混音和现在混音有没有发生变化。
		List<Long> agendaForwardIdList = agendaForwards.stream().map(AgendaForwardPO::getId).collect(Collectors.toList());
		if(agendaForwardIdList != null){
			
			//现在和以前一致的声音源。然后先将现在配置的目的成员不是声音源的去除
			//现在需要单独混音的目的成员id。
			Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardAndBusienssGroupMemberMap = agendaRoleMemberUtil.obtainAgendaForwardDstMemberMap(agendaForwards, group.getMembers());
			Set<BusinessGroupMemberPO> members = agendaForwardAndBusienssGroupMemberMap.values().stream().flatMap( memberList->memberList.stream() ).collect(Collectors.toSet());
//			Map<Long, BusinessGroupMemberPO> memberIdAndMemberList = members.stream().collect(Collectors.toMap(BusinessGroupMemberPO::getId, Function.identity()));
			List<Long> memberIdList = members.stream().map(BusinessGroupMemberPO::getId).collect(Collectors.toList());
			List<BusinessCombineAudioSrcPO> combineAudioSrcList = oldBusinessCombineAudio.getSrcs();
			
			//原来需要单独混音的目的成员id
			List<Long> oldDstmemberIdList = new ArrayList<Long>();
			if(oldBusinessCombineAudio != null){
				oldDstmemberIdList = oldBusinessCombineAudio.getMemberAudios().stream().map(BusinessCombineAudioPO::getDstMemberId).collect(Collectors.toList());
			}
			
			//现在需要单独混音的目的成员id
			if(combineAudioSrcList != null){
				List<Long> srcMemberIdList = combineAudioSrcList.stream().map(BusinessCombineAudioSrcPO::getMemberId).collect(Collectors.toList());
				memberIdList.retainAll(srcMemberIdList);
			}
			
			//将要删除的目的对应的混音和要添加的目的对应的混音添加
			List<Long> sameMemberIdList = new ArrayList<Long>(memberIdList);
			sameMemberIdList.retainAll(oldDstmemberIdList);
			memberIdList.removeAll(sameMemberIdList);
			oldDstmemberIdList.removeAll(sameMemberIdList);
			
			//可以复用的混音，多余后边删除
			List<BusinessCombineAudioPO> couldReuseCombineAudioList = new ArrayList<BusinessCombineAudioPO>();
			Map<Long, BusinessCombineAudioPO> memberIdAndCombineAudio = oldBusinessCombineAudio.getMemberAudios().stream().collect(Collectors.toMap(BusinessCombineAudioPO::getDstMemberId, Function.identity()));
			for(Long memberId :oldDstmemberIdList){
				couldReuseCombineAudioList.add(memberIdAndCombineAudio.get(memberId));
			}
			
			//处理将要删除和新建的混音
			Map<Long, List<BusinessCombineAudioSrcPO>> memberIdAndBusinessCombineAudioSrcPOMap = combineAudioSrcList.stream().
					filter(srcPo->srcPo.getMemberId()!=null).collect(Collectors.groupingBy(BusinessCombineAudioSrcPO::getMemberId));
			List<BusinessCombineAudioPO> newMemberCombineAudioList = new ArrayList<BusinessCombineAudioPO>();
			//建立成员混音与全部混音的单向连接
			for (Long memberId : memberIdList) {
				List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(memberId);
				if(audioSrcList != null){
					//这个成员应该听那些音频
					BusinessCombineAudioPO memberCombineAudio = new BusinessCombineAudioPO();
					memberCombineAudio.setIsAll(false);
					//每个成员的音频唯一标识不需要加
					memberCombineAudio.setDstMemberId(memberId);
					memberCombineAudio.setAllAudio(oldBusinessCombineAudio);
					
					//去除自己的声音
					List<BusinessCombineAudioSrcPO> myTempSrcs = new ArrayList<BusinessCombineAudioSrcPO>(oldBusinessCombineAudio.getSrcs());
					myTempSrcs.removeAll(audioSrcList);
					List<BusinessCombineAudioSrcPO> mySrcs = new ArrayList<BusinessCombineAudioSrcPO>();
					for(BusinessCombineAudioSrcPO src : myTempSrcs){
						BusinessCombineAudioSrcPO mySrcPo = new BusinessCombineAudioSrcPO();
						mySrcPo.set(src);
						mySrcPo.setCombineAudio(memberCombineAudio);
						mySrcs.add(mySrcPo);
					}
					memberCombineAudio.setSrcs(mySrcs);
					if(memberCombineAudio.getSrcs().size() <= 0){
						log.info("成员id： "+ memberId + " 不需要听任何声音");
						memberCombineAudio.setHasSource(false);
					}else if(memberCombineAudio.getSrcs().size() >= 1){
						if(memberCombineAudio.getSrcs().size() == 1){
							memberCombineAudio.setMix(false);
						}else{
							memberCombineAudio.setMix(true);
						}
					}
					newMemberCombineAudioList.add(memberCombineAudio);
				}
			}
			changeCombineAudio(couldReuseCombineAudioList, newMemberCombineAudioList, businessDeliverBO);
		}else {
			//将所有的混音都清理
			clearCombineAudio(group, businessDeliverBO);
		}
	}
	
	/**
	 * 更新混音，可复用的混音就复用，不够就新建，多余就删除<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月15日 下午5:22:51
	 * @param couldReuseCombineAudioList
	 * @param newMemberCombineAudio
	 */
	public void changeCombineAudio(List<BusinessCombineAudioPO> couldReuseCombineAudioList, List<BusinessCombineAudioPO> newMemberCombineAudioList, BusinessDeliverBO businessDeliverBO){
		
		List<BusinessCombineAudioPO> needDeleteCombineAudios = new ArrayList<BusinessCombineAudioPO>(couldReuseCombineAudioList);
		List<BusinessCombineAudioPO> needAddCombineAudios = new ArrayList<BusinessCombineAudioPO>(newMemberCombineAudioList);
		List<BusinessCombineAudioPO> needUpdateCombineAudios = new ArrayList<BusinessCombineAudioPO>();
		
		for(int i = 0 ; i < couldReuseCombineAudioList.size() && i < newMemberCombineAudioList.size(); i++){
			BusinessCombineAudioPO reuseCombineAudio = couldReuseCombineAudioList.get(i);
			BusinessCombineAudioPO newMemberCombineAudio =  newMemberCombineAudioList.get(i);
			needDeleteCombineAudios.remove(reuseCombineAudio);
			needAddCombineAudios.remove(newMemberCombineAudio);
			
			reuseCombineAudio.set(newMemberCombineAudio);
			needUpdateCombineAudios.add(reuseCombineAudio);
		}
		if(needDeleteCombineAudios.size()>0){
			businessDeliverBO.getStopCombineAudios().addAll(needDeleteCombineAudios);
			for(BusinessCombineAudioPO combineAudio : needDeleteCombineAudios){
				if(!combineAudio.getIsAll()){
					BusinessCombineAudioPO allAudioPO = combineAudio.getAllAudio();
					allAudioPO.getMemberAudios().remove(combineAudio);
				}
			}
		}
		List<BusinessCombineAudioPO> allAudioList = new ArrayList<BusinessCombineAudioPO>();
		if(newMemberCombineAudioList.size()>0){
			//建立连接
			for(BusinessCombineAudioPO addCombineAudio : needAddCombineAudios){
				BusinessCombineAudioPO allAudio = addCombineAudio.getAllAudio();
				allAudio.getMemberAudios().add(addCombineAudio);
				allAudioList.add(allAudio);
			}
			businessDeliverBO.getStartCombineAudios().addAll(needAddCombineAudios);
		}
		businessDeliverBO.getUpdateCombineAudios().addAll(needUpdateCombineAudios);
//		combineAudioDao.save(allAudioList);
	}
	
	/**
	 * 重新创建混音的关系<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月15日 下午1:06:38
	 * @param group
	 * @param nextAgenda
	 * @param oldCombineAudioPermissionList
	 * @param businessDeliverBO
	 */
	public void rebuildCombineAudio(GroupPO group ,AgendaPO nextAgenda,List<CombineAudioPermissionPO> oldCombineAudioPermissionList, BusinessDeliverBO businessDeliverBO){
		//将之前的关系全部删除重建。
		Set<Long> aIds = new HashSet<Long>();
		for(CombineAudioPermissionPO audioPer : oldCombineAudioPermissionList){
			BusinessCombineAudioPO allAudio = audioPer.getAllAudio();
			if(allAudio != null){
				businessDeliverBO.getStopCombineAudios().add(allAudio);
				aIds.add(allAudio.getId());
				List<BusinessCombineAudioPO> memberAudios = allAudio.getMemberAudios();
				if(memberAudios != null){
					businessDeliverBO.getStopCombineAudios().addAll(memberAudios);
				}
			}
		}
		businessCombineAudioDao.deleteByIdIn(aIds);	
		
		//处理音频。分为议程/议程转发和自动配置。
		if(AudioPriority.AGENDA_FIRST.equals(nextAgenda.getAudioPriority())){
			agendaAudio(group, nextAgenda.getId(), businessDeliverBO);
		}else if(AudioPriority.GLOBAL_FIRST.equals(nextAgenda.getAudioPriority())){
			generateAutoCombineAudio(group, nextAgenda.getId(), businessDeliverBO);
		}
	}
	
	/**
	 * 重新创建议程转发混音的关系<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月15日 下午1:06:38
	 * @param group
	 * @param nextAgenda
	 * @param oldCombineAudioPermissionList
	 * @param businessDeliverBO
	 */
	public void rebuildAgendaForwardCombineAudio(GroupPO group ,AgendaForwardPO agendaForward, BusinessDeliverBO businessDeliverBO){
		//将之前的议程转发关系全部删除重建。
		clearAgendaForwardCombineAudio(group, new ArrayListWrapper<AgendaForwardPO>().add(agendaForward).getList(), businessDeliverBO);
		
		Map<AgendaForwardPO, Set<BusinessGroupMemberPO>> agendaForwardAndBusienssGroupMemberMap = 
				agendaRoleMemberUtil.obtainAgendaForwardDstMemberMap(new ArrayListWrapper<AgendaForwardPO>().add(agendaForward).getList(), group.getMembers());
		//开始构建真实数据，组件关系
		CombineAudioPermissionPO p = new CombineAudioPermissionPO();
	
		p.setGroupId(group.getId());
		p.setPermissionType(CustomAudioPermissionType.AGENDA_FORWARD);
		p.setPermissionId(agendaForward.getId());
		p.setAgendaId(agendaForward.getAgendaId());
		p.setLayoutId(agendaForward.getLayoutId());
		combineAudioPermissionDao.save(p);
		
		List<CustomAudioPO> customAudioList = customAudioDao.findByPermissionIdAndPermissionType(agendaForward.getId(), CustomAudioPermissionType.AGENDA_FORWARD);
		BusinessCombineAudioPO combineAudio = new BusinessCombineAudioPO();
		String audioUid = generateAudioUid(group.getId(), customAudioList);
	
		//新建混音
		//源是空的时候创建一个空壳BusinessCombineAudioPO
		if(customAudioList == null){
			combineAudio.setSrcs(new ArrayList<BusinessCombineAudioSrcPO>());
			combineAudio.setMemberAudios(new ArrayList<BusinessCombineAudioPO>());
			combineAudio.setCombineAudioPermissions(new HashSet<CombineAudioPermissionPO>());
			combineAudio.getCombineAudioPermissions().add(p);
			combineAudio.setAudioUid(audioUid);
			combineAudio.setHasSource(false);
			p.setAllAudio(combineAudio);
			
			businessCombineAudioDao.save(combineAudio);
			return ;
		}
			
		combineAudio.setSrcs(new ArrayList<BusinessCombineAudioSrcPO>());
		combineAudio.setMemberAudios(new ArrayList<BusinessCombineAudioPO>());
		combineAudio.setCombineAudioPermissions(new HashSet<CombineAudioPermissionPO>());
		combineAudio.getCombineAudioPermissions().add(p);
		combineAudio.setAudioUid(audioUid);
		p.setAllAudio(combineAudio);
		businessCombineAudioDao.save(combineAudio);
		
		//拥有音频输出的成员都有哪些音频集合
		Map<Long, List<BusinessCombineAudioSrcPO>> memberIdAndBusinessCombineAudioSrcPOMap = new HashMap<Long, List<BusinessCombineAudioSrcPO>>(); 
		for(CustomAudioPO audio: customAudioList){
			BusinessCombineAudioSrcPO audioSrc = new BusinessCombineAudioSrcPO();
			BusinessGroupMemberTerminalChannelPO terminalChannel = agendaRoleMemberUtil.obtainMemberTerminalChannelFromAgendaForwardSource(
					audio.getSourceId(), audio.getSourceType(), group.getMembers());
			audioSrc.setSourceId(audio.getSourceId());
			audioSrc.setSourceType(audio.getSourceType());
			audioSrc.setCombineAudio(combineAudio);
			audioSrc.set(terminalChannel);
			combineAudio.getSrcs().add(audioSrc);
			
			//拥有音频输出的成员都有哪些音频集合构建
			List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(audioSrc.getMemberId());
			if(audioSrcList==null){
				audioSrcList = new ArrayList<BusinessCombineAudioSrcPO>();
				memberIdAndBusinessCombineAudioSrcPOMap.put(audioSrc.getMemberId(), audioSrcList);
			}
			audioSrcList.add(audioSrc);
		}
		
		//如果没有声音源就不用判断每个成员应该听什么
		if(combineAudio.getSrcs().size() <= 0){
			combineAudio.setHasSource(false);
			log.info(group.getName()+"的"+agendaForward.getName()+"议程转发"+"不需要听任何声音");
		}else{
			if(combineAudio.getSrcs().size() > 1){
				combineAudio.setMix(true);
			}
			
			for (BusinessGroupMemberPO member : agendaForwardAndBusienssGroupMemberMap.get(agendaForward)) {
				List<BusinessCombineAudioSrcPO> audioSrcList= memberIdAndBusinessCombineAudioSrcPOMap.get(member.getId());
				if(audioSrcList != null){
					//这个成员应该听那些音频
					BusinessCombineAudioPO memberCombineAudio = new BusinessCombineAudioPO();
					memberCombineAudio.setIsAll(false);
					//每个成员的音频唯一标识不需要加
					memberCombineAudio.setDstMemberId(member.getId());
					memberCombineAudio.setAllAudio(combineAudio);
					combineAudio.getMemberAudios().add(memberCombineAudio);
					
					//去除自己的声音 
					List<BusinessCombineAudioSrcPO> myTempSrcs = new ArrayList<BusinessCombineAudioSrcPO>(combineAudio.getSrcs());
					myTempSrcs.removeAll(audioSrcList);
					List<BusinessCombineAudioSrcPO> mySrcs = new ArrayList<BusinessCombineAudioSrcPO>();
					for(BusinessCombineAudioSrcPO src : myTempSrcs){
						BusinessCombineAudioSrcPO mySrcPo = new BusinessCombineAudioSrcPO();
						mySrcPo.set(src);
						mySrcPo.setCombineAudio(memberCombineAudio);
						mySrcs.add(mySrcPo);
					}
					memberCombineAudio.setSrcs(mySrcs);
					if(memberCombineAudio.getSrcs().size() <= 0){
						log.info(member.getName() + "不需要听任何声音");
						memberCombineAudio.setHasSource(false);
					}else if(memberCombineAudio.getSrcs().size() >1){
						memberCombineAudio.setMix(true);
					}
				}
			}
		}
		businessCombineAudioDao.save(combineAudio);

		businessDeliverBO.getStartCombineAudios().add(combineAudio);
		if(combineAudio.getMemberAudios() != null){
			businessDeliverBO.getStartCombineAudios().addAll(combineAudio.getMemberAudios());
		}						
	}
}
