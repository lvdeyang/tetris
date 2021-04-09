package com.sumavision.tetris.bvc.business.group.combine.video;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sumavision.tetris.bvc.model.role.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.group.enumeration.CombineVideoSrcType;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPositionPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.SourceType;
import com.sumavision.tetris.bvc.model.agenda.combine.VirtualSourceService;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionPO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.util.AgendaRoleMemberUtil;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 合屏<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月24日 下午1:02:43
 */
@Slf4j
@Service
public class CombineVideoService {
	
	@Autowired
	private AgendaDAO agendaDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private BusinessCombineVideoDAO combineVideoDao;

	@Autowired
	private AgendaRoleMemberUtil agendaRoleMemberUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private VirtualSourceService virtualSourceService;

	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDao;
	
	@Autowired
	private AgendaForwardSourceDAO agendaForwardSourceDao;
	
	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;
	
	@Autowired
	private BusinessCombineVideoDAO businessCombineVideoDao;
	
	@Autowired
	private RoleChannelTerminalChannelPermissionDAO roleChannelTerminalChannelPermissionDao;
	
	@Autowired
	private CombineTemplateGroupAgendeForwardPermissionDAO combineTemplateGroupAgendeForwardPermissionDao;
	
	/**属性的分隔符*/
	private final String propertySeparator = "-";
	
	/**属性集合的分隔符 */
	private final String separator = "_";
	
	/** 每个源标识的前置分割符*/
	private final String sourceBefore = "(";
	
	/** 每个源标识的后置置分割符*/
	private final String sourceAfter = ")";
	
	/** 每个位置标识的前置分割符*/
	private final String positionBefore = "[";
	
	/** 每个位置标识的后置分割符*/
	private final String positionAfter = "]";
	
	/**
	 * 根据位置和源，生成合屏数据。不包含持久化<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月4日 下午2:46:54
	 * @param group
	 * @param combineVideo 为空则会新建
	 * @param combineTemplatePositions null表示自动轮询
	 * @param agendaForwardSources
	 * @param roleChannelIds 所涉及到的角色通道id，用来进行过滤。为null则不过滤
	 * @param mode 1为普通合屏，2为自动轮询，3为虚拟源合屏
	 * @param businessDeliverBO 创建合屏时，如果虚拟源合屏不存在，则需要同时创建
	 * @return
	 * @throws Exception 
	 */
	public BusinessCombineVideoPO templateCombineVideo(
			GroupPO group,
			BusinessCombineVideoPO combineVideo,
			Collection<CombineTemplatePositionPO> combineTemplatePositions,
			Collection<AgendaForwardSourcePO> agendaForwardSources,
			List<Long> roleChannelIds,
			int mode,
			BusinessDeliverBO businessDeliverBO) throws Exception{
		
		if(combineVideo == null){
			combineVideo = new BusinessCombineVideoPO();
			combineVideo.setCombineTemplates(new HashSet<CombineTemplateGroupAgendeForwardPermissionPO>());
	//		combineVideo.getCombineTemplates().add(p);
	//		p.setCombineVideo(combineVideo);
			combineVideo.setPositions(new ArrayList<BusinessCombineVideoPositionPO>());
		}else{
			combineVideo.getPositions().clear();
		}
		
		String uid = generateCombineVideoUid(group.getId(), agendaForwardSources, combineTemplatePositions, mode);
		combineVideo.setCombineVideoUid(uid);
		
//		List<CombineTemplatePositionPO> combineTemplatePositions = combineTemplatePositionDao.findByCombineTemplateIdIn(new ArrayListWrapper<Long>().add(combineTemplateId).getList());
		if(combineTemplatePositions==null){//自动轮询
			AgendaForwardSourcePO sourcePO=agendaForwardSources.iterator().next();
			BusinessCombineVideoPositionPO position = new BusinessCombineVideoPositionPO();
			combineVideo.getPositions().add(position);
			position.setCombineVideo(combineVideo);
			position.setSrcs(new ArrayList<BusinessCombineVideoSrcPO>());
			position.setSerialnum(sourcePO.getSerialNum());
			position.setLayoutPositionSerialNum(sourcePO.getSerialNum());
			position.setX("0");
			position.setY("0");
			position.setW("10000");
			position.setH("10000");
			position.setzIndex("0");
			position.setPictureType(PictureType.POLLING);
			position.setPollingTime(sourcePO.getLoopTime()+"");

			RoleChannelPO channelPO=roleChannelDao.findOne(sourcePO.getSourceId());
			List<BusinessGroupMemberPO> businessGroupMemberPOS=businessGroupMemberDao.findByGroupIdAndRoleId(group.getId(),channelPO.getRoleId());
			for (BusinessGroupMemberPO member:businessGroupMemberPOS) {
				BusinessCombineVideoSrcPO src = new BusinessCombineVideoSrcPO();
				src.setAgendaForwardSourceId(sourcePO.getId());
				src.setHasSource(false);
				src.setPosition(position);
				src.setSourceId(sourcePO.getSourceId());
				src.setSourceType(sourcePO.getSourceType());
				position.getSrcs().add(src);

				//从AgendaForwardSourcePO去找真的源
				RoleChannelTerminalChannelPermissionPO per = roleChannelTerminalChannelPermissionDao.findByTerminalIdAndRoleChannelId(member.getTerminalId(), sourcePO.getSourceId());
				if(per != null){
					BusinessGroupMemberTerminalChannelPO srcChannel = tetrisBvcQueryUtil.queryBusinessGroupMemberTerminalChannelByTerminalChannelId(member.getChannels(), per.getTerminalChannelId());
					src.set(srcChannel);
				}
			}
		}else{//正常合屏
			for(CombineTemplatePositionPO tp : combineTemplatePositions){

				//从agendaForwardSources找相同的serialNum，可能多个
				List<AgendaForwardSourcePO> ss = tetrisBvcQueryUtil.queryAgendaForwardSourcesBySerialNum(agendaForwardSources, tp.getLayoutPositionSerialNum(), roleChannelIds);

				//生成分屏
				BusinessCombineVideoPositionPO position = new BusinessCombineVideoPositionPO();
				combineVideo.getPositions().add(position);
				position.setCombineVideo(combineVideo);
				position.setSrcs(new ArrayList<BusinessCombineVideoSrcPO>());
				position.setSerialnum(tp.getLayoutPositionSerialNum());
				position.setLayoutPositionSerialNum(tp.getLayoutPositionSerialNum());
				position.setX(tp.getX());
				position.setY(tp.getY());
				position.setW(tp.getWidth());
				position.setH(tp.getHeight());
				position.setzIndex(tp.getzIndex());
				if(ss.size() > 0){
					AgendaForwardSourcePO s1 = ss.get(0);
					position.setPictureType(s1.getIsLoop()?PictureType.POLLING:PictureType.STATIC);
					position.setPollingTime(s1.getLoopTime()==null?null:String.valueOf(s1.getLoopTime()));
				}else{
					position.setPictureType(PictureType.STATIC);
				}

				//遍历这个分屏中的N个源
				for(AgendaForwardSourcePO source : ss){

					//每个AgendaForwardSourcePO都生成1个BusinessCombineVideoSrcPO
					BusinessCombineVideoSrcPO src = new BusinessCombineVideoSrcPO();
					src.setAgendaForwardSourceId(source.getId());
					src.setHasSource(false);
					src.setPosition(position);
					src.setSourceId(source.getSourceId());
					src.setSourceType(source.getSourceType());
					position.getSrcs().add(src);

					//从AgendaForwardSourcePO去找真的源
					BusinessGroupMemberTerminalChannelPO memberTerminalChannel = agendaRoleMemberUtil.obtainMemberTerminalChannelFromAgendaForwardSource(
							source.getSourceId(), source.getSourceType(), group.getMembers());

					//这里必须设置源，即使srcChannel为null。如果源是虚拟源，则在后边设置
					src.set(memberTerminalChannel);
					
					//如果源是虚拟源，在这里设置设置
					if(source.getSourceType().equals(SourceType.COMBINE_VIDEO_VIRTUAL_SOURCE)){
												
						AgendaPO agenda = agendaDao.findOne(source.getSourceId());
						src.setHasSource(true);
						src.setType(CombineVideoSrcType.VIRTUAL);
						src.setVirtualUuid(agenda.getUuid());
						
						//如果找不到合屏，就创建
						BusinessCombineVideoPO virtualCombineVideo = combineVideoDao.findByUuid(agenda.getUuid());
						if(virtualCombineVideo == null){
							AgendaForwardPO agendaForward = agendaForwardDao.findByAgendaId(agenda.getId()).get(0);
							virtualSourceService.combineVideo(group, agenda, agendaForward, businessDeliverBO);
						}else{
							//这里必须建立一个拷贝，否则removeAll()无效
							Set<CombineTemplateGroupAgendeForwardPermissionPO> vcts = virtualCombineVideo.getCombineTemplates();
							Set<CombineTemplateGroupAgendeForwardPermissionPO> vcts_copy = new HashSet<CombineTemplateGroupAgendeForwardPermissionPO>(vcts);
							businessDeliverBO.getUnusefulVideoPermissions().removeAll(vcts_copy);
						}
					}
				}
			}
		}

		
		return combineVideo;
	}
	
	/**
	 * 根据需要执行的议程更新合屏<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月23日 下午5:13:22
	 * @param group
	 * @param nextAgenda 需要执行的议程
	 * @param roleChannelIds
	 * @param businessDeliverBO
	 * @throws Exception
	 */
	public void updateVideo(GroupPO group, AgendaPO nextAgenda, List<Long> roleChannelIds, BusinessDeliverBO businessDeliverBO) throws Exception {
		
		if(businessDeliverBO == null) businessDeliverBO = new BusinessDeliverBO().setGroup(group);
		
		List<AgendaForwardPO> agendaForwards = agendaForwardDao.findByAgendaId(nextAgenda.getId());

		//处理内置议程轮询情况
		for (AgendaForwardPO agendaForwardPO:agendaForwards) {
            //内置议程轮询，先删除：轮询时，source只有一个
			List<AgendaForwardSourcePO> sourcePOs=agendaForwardSourceDao.findByAgendaForwardId(agendaForwardPO.getId());
			if(sourcePOs.size()==1&&sourcePOs.get(0).getIsLoop()){
				AgendaForwardSourcePO sourcePO=sourcePOs.get(0);
				List<BusinessCombineVideoPO> combineVideoPOS=businessCombineVideoDao.
						findByCombineVideoUidLike(group.getId()+"_"+sourcePO.getId()+"%");
				for (BusinessCombineVideoPO combineVideo:combineVideoPOS) {
					if(!BaseUtils.collectionIsNotBlank(businessDeliverBO.getStopCombineVideos())){
						businessDeliverBO.setStopCombineVideos(new HashSet<>());
					}
					businessDeliverBO.getStopCombineVideos().add(combineVideo);
					businessCombineVideoDao.delete(combineVideo.getId());
				}
				List<PageTaskPO> pageTaskList = pageTaskDao.
						findByAgendaForwardSourceIdInAndBusinessId(new ArrayListWrapper<Long>().add(sourcePO.getId()).getList(), group.getId().toString());
				if(!BaseUtils.collectionIsNotBlank(businessDeliverBO.getStopPageTasks())){
					businessDeliverBO.setStopPageTasks(new HashSet<PageTaskPO>());
				}
				businessDeliverBO.getStopPageTasks().addAll(pageTaskList);
			}
		}

		//关联关系
		List<CombineTemplateGroupAgendeForwardPermissionPO> videoPers = combineTemplateGroupAgendeForwardPermissionDao.findByGroupId(group.getId());
		//【注意】这里会先把所有的关联关系放入“不再使用”列表，后边如果使用，会从这里剔除
		businessDeliverBO.getUnusefulVideoPermissions().addAll(videoPers);
		
		for(CombineTemplateGroupAgendeForwardPermissionPO videoPer : videoPers){
			
			//先确认是不是虚拟源
			Long agendaId = videoPer.getAgendaId();
			boolean isVirtualSource = false;
			AgendaPO agenda = agendaDao.findOne(agendaId);
			if(BusinessInfoType.COMBINE_VIDEO_VIRTUAL_SOURCE.equals(agenda.getBusinessInfoType())){
				isVirtualSource = true;
			}
			
			//先查agendaId看是否给当前议程用的。如果这videoPer对应的是虚拟源，那么也会continue
			if(!isVirtualSource){
				if(!nextAgenda.getId().equals(agendaId)){
					//不用更新BusinessCombineVideoPO，后续会删除这个videoPer
					continue;
				}
			}
			
//			再查 agendaForwardId layoutId 看是否当前仍有用
			Long agendaForwardId = videoPer.getAgendaForwardId();
			Long layoutId = videoPer.getLayoutId();
			AgendaForwardPO agendaForward = tetrisBvcQueryUtil.queryAgendaForwardByAgendaForwardIdAndLayoutId(agendaForwards, agendaForwardId, layoutId);
			if(!isVirtualSource){
				if(agendaForward == null){
					//不用更新BusinessCombineVideoPO，后续会删除这个videoPer
					continue;
				}
			}
			
			//如果是虚拟源，那么此时agendaForward是null，需要根据虚拟源重新查询
			if(isVirtualSource){
				agendaForward = agendaForwardDao.findByAgendaId(agendaId).get(0);
			}
			
			//计算uid
			List<AgendaForwardSourcePO> agendaForwardSources = agendaForwardSourceDao.findByAgendaForwardId(agendaForward.getId());
			List<CombineTemplatePositionPO> combineTemplatePositions = combineTemplatePositionDao.findByCombineTemplateIdIn(
					new ArrayListWrapper<Long>().add(videoPer.getCombineTemplateId()).getList());
			String uid = generateCombineVideoUid(group.getId(), agendaForwardSources, combineTemplatePositions, 1);
			
			//更新它这个合屏（也可能不需要更新，后边把它删了）
			BusinessCombineVideoPO combineVideo = videoPer.getCombineVideo();
			boolean uidEquals = combineVideo.getCombineVideoUid().equals(uid);
			log.info("原有合屏uid与当前需要的uid是否相等：" + uidEquals);
			
			if(!businessDeliverBO.getUpdateCombineVideos().contains(combineVideo)){
				
				
				//更新它：（后续优化：）如果uid没变，则更新一遍源；如果uid变了，则更新整个合屏（也可能不需要更新，后边把它删了）
				int mode = isVirtualSource?3:1;
				combineVideo = templateCombineVideo(group, combineVideo, combineTemplatePositions, agendaForwardSources, roleChannelIds, mode, businessDeliverBO);				
				combineVideoDao.save(combineVideo);
				
				//先放入“需要更新的合屏”，后边DeliverExecuteService.execute也可能被删掉
				businessDeliverBO.getUpdateCombineVideos().add(combineVideo);
			}
			else if(!uidEquals){
				
				//combineVideo已被更新，但videoPer的uid与combineVideo的不相同，表示该videoPer所服务的内容原本与另一个permission相同，但现在发生了变化（可能是合屏模板，也可能是议程转发变化等）
				log.info("combineVideo已被更新，但videoPer的uid与combineVideo的不相同，需要新建合屏");
								
				combineVideo.getCombineTemplates().remove(videoPer);
				combineVideoDao.save(combineVideo);
				
				//新建合屏
				int mode = isVirtualSource?3:1;
				BusinessCombineVideoPO newCombineVideo = templateCombineVideo(group, null, combineTemplatePositions, agendaForwardSources, roleChannelIds, mode, businessDeliverBO);
				//与合屏模板互相关联
				newCombineVideo.setCombineTemplates(new HashSet<CombineTemplateGroupAgendeForwardPermissionPO>());
				newCombineVideo.getCombineTemplates().add(videoPer);
				videoPer.setCombineVideo(newCombineVideo);					
				businessCombineVideoDao.save(newCombineVideo);
				
				businessDeliverBO.getStartCombineVideos().add(newCombineVideo);
			}
		}
	}
	
	/**
	 * 通过AgendaForwardSourcePO集合以及CombineTemplatePositionPO集合生成该画面的唯一标识字符串<br/>
	 * 必须以“groupId_”开头，否则停会时无法正常停止<br/>
	 * 例如groupId_[x-y-w-h-z-(sourceId-sourceType)-()]_[...]_[...]其中的连接符都是自定义配置的
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月30日 下午4:29:10
	 * @param groupId
	 * @param forwardSourceList 视频源的集合
	 * @param positionList 视频源对应位置信息集合，null表示自动轮询，uid生成规则不同
	 * @param mode 1为普通合屏，2为自动轮询，3为虚拟源合屏
	 * @return
	 */
	public String generateCombineVideoUid(
			Long groupId,
			Collection<AgendaForwardSourcePO> forwardSourceList,
			Collection<CombineTemplatePositionPO> positionList,
			int mode){
		
		StringBuilder combineVideoUid = new StringBuilder().append(groupId);
		if(mode == 3){
			combineVideoUid.append("_virtualSource-").append(UUID.randomUUID().toString().replaceAll("-", ""));
		}else if(mode == 1){
//		}else if(positionList!=null){
			Map<Integer, List<AgendaForwardSourcePO>> serialNumAndAgendaForwardSourceList= forwardSourceList.stream().sorted(Comparator.comparing(AgendaForwardSourcePO::getSourceId)).collect(Collectors.groupingBy(AgendaForwardSourcePO::getSerialNum));
			String extendUid = positionList.stream().sorted(Comparator.comparing(CombineTemplatePositionPO::getLayoutPositionSerialNum)).map(position->{
				List<AgendaForwardSourcePO> sourceList= serialNumAndAgendaForwardSourceList.get(position.getLayoutPositionSerialNum());
				if(BaseUtils.collectionIsNotBlank(sourceList)){
					String uid = sourceList.stream().map(source->{
						return source.getSourceId().toString()+source.getSourceType();
					}).collect(Collectors.joining(propertySeparator, sourceBefore, sourceAfter));
					return position.getX()+propertySeparator+
							position.getY()+propertySeparator+
							position.getWidth()+propertySeparator+
							position.getHeight()+propertySeparator+
							position.getzIndex()+propertySeparator+
							uid;
				}
				return null;
			}).filter(uid->{
				return BaseUtils.stringIsNotBlank(uid);
			}).collect(Collectors.joining(separator, positionBefore, positionAfter));

			if(BaseUtils.stringIsNotBlank(extendUid)){
				combineVideoUid.append("_").append(extendUid);
			}
		}else if(mode == 2){
			Long sourceId=forwardSourceList.iterator().next().getSourceId();
			RoleChannelPO roleChannelPO=roleChannelDao.findOne(sourceId);
			List<BusinessGroupMemberPO> groupMemberPOs=businessGroupMemberDao.findByGroupIdAndRoleId(groupId,roleChannelPO.getRoleId());

			String extendUid = groupMemberPOs.stream().map(memberPO->{

				return memberPO.getId()+"";
			}).collect(Collectors.joining(separator, positionBefore, positionAfter));
			if(BaseUtils.stringIsNotBlank(extendUid)){
				combineVideoUid.append("_"+sourceId).append("_inner");
			}
		}

		
		return combineVideoUid.toString();
	}
	
}
