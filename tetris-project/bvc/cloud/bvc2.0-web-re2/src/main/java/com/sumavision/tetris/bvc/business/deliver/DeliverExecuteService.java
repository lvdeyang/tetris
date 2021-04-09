package com.sumavision.tetris.bvc.business.deliver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.dao.CombineAudioDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.bo.BusinessReturnBO;
import com.sumavision.tetris.bvc.business.bo.MemberChangedTaskBO;
//import com.sumavision.tetris.bvc.business.bo.SourceBO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.common.MulticastService;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineAudioDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineAudioSrcDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessCombineVideoSrcDAO;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.CombineAudioPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.forward.CommonForwardPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberRolePermissionPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.group.record.GroupRecordService;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.LayoutSelectionType;
import com.sumavision.tetris.bvc.model.agenda.SourceType;
import com.sumavision.tetris.bvc.model.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardDAO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardPO;
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
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * execute()最终执行businessDeliverBO，以及由 BusinessReturnService 缓存的logic和websocket<br/>
 * <p>BusinessReturnService处理logic和websocket，但应该由DeliverExecuteService来最终执行</p>
 * <p>参看BusinessReturnService.java</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年12月3日 上午11:58:23
 */
@Slf4j
@Service
public class DeliverExecuteService {
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;

	@Autowired
	private MultiRateUtil multiRateUtil;

	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private BusinessCombineVideoDAO businessCombineVideoDao;
	
	@Autowired
	private BusinessCombineAudioDAO businessCombineAudioDao;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private GroupRecordService groupRecordService;
	
	/**
	 * 最终执行businessDeliverBO，以及由 BusinessReturnService 缓存的logic和websocket<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月22日 上午11:38:43
	 * @param businessDeliverBO
	 * @param message
	 * @param doProtocol
	 * @throws Exception
	 */
	public void execute(BusinessDeliverBO businessDeliverBO, String message, boolean doProtocol) throws Exception{
		
		//参数模板
		CodecParamBO codec = null;		
		GroupPO group = businessDeliverBO.getGroup();
		if(group == null){
			codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		}else{
			DeviceGroupAvtplGearsPO gear = multiRateUtil.queryDefaultGear(group.getAvtpl().getGears());
			codec = new CodecParamBO().set(gear);
		}
		
		//无用的合屏关联关系、无用的合屏
		Set<CombineTemplateGroupAgendeForwardPermissionPO> unusefulVideoPers = businessDeliverBO.getUnusefulVideoPermissions();
		for(CombineTemplateGroupAgendeForwardPermissionPO unusefulVideoPer : unusefulVideoPers){
			BusinessCombineVideoPO combineVideo = unusefulVideoPer.getCombineVideo();
			if(combineVideo != null){
				Set<CombineTemplateGroupAgendeForwardPermissionPO> ps = combineVideo.getCombineTemplates();
				if(ps.size() == 1){
					log.info("虚拟源不再有成员观看，删除合屏");
					businessCombineVideoDao.delete(combineVideo);
					businessDeliverBO.getUpdateCombineVideos().remove(combineVideo);
					businessDeliverBO.getStopCombineVideos().add(combineVideo);
				}else{
					log.info("虚拟源不再有成员观看，删除合屏关联关系，不删合屏");
					ps.remove(unusefulVideoPer);
					businessCombineVideoDao.save(combineVideo);
				}
			}
		}
		
		LogicBO logic = new LogicBO().setUserId(businessDeliverBO.getUserId());
		
		//合屏
		Set<BusinessCombineVideoPO> startCombineVideos = businessDeliverBO.getStartCombineVideos();
		logic.setCombineVideoSet_recon(startCombineVideos, codec);
		
		Set<BusinessCombineVideoPO> updateCombineVideos = businessDeliverBO.getUpdateCombineVideos();
		logic.setCombineVideoUpdate_recon(updateCombineVideos, codec);
		
		Set<BusinessCombineVideoPO> stopCombineVideos = businessDeliverBO.getStopCombineVideos();
		logic.setCombineVideoDel_recon(stopCombineVideos);
		
		//混音
		Set<BusinessCombineAudioPO> startCombineAudios = businessDeliverBO.getStartCombineAudios();
		logic.setCombineAudioSet_recon(startCombineAudios, codec);
		
		Set<BusinessCombineAudioPO> updateCombineAudios = businessDeliverBO.getUpdateCombineAudios();
		logic.setCombineAudioUpdate_recon(updateCombineAudios, codec);
		
		Set<BusinessCombineAudioPO> stopCombineAudios = businessDeliverBO.getStopCombineAudios();
		logic.setCombineAudioDel_recon(stopCombineAudios);
		
		//录制
		Set<RecordSetBO> startRecordSets = businessDeliverBO.getStartRecordSets();
		if(startRecordSets != null){
			logic.getRecordSet().addAll(startRecordSets);
		}
		
		Set<RecordSetBO> updateRecordSets = businessDeliverBO.getUpdateRecordSet();
		if(updateRecordSets != null){
			logic.getRecordUpdate().addAll(updateRecordSets);
		}
		
		Set<RecordSetBO> stopRecordSets = businessDeliverBO.getStopRecordSets();
		if(stopRecordSets != null){
			logic.getRecordDel().addAll(stopRecordSets);
		} 
		
		//没有用的混音删除
		List<Long> stopCombineAudioIds = stopCombineAudios.stream().filter(BusinessCombineAudioPO::getIsAll).map(BusinessCombineAudioPO::getId).collect(Collectors.toList());
		businessCombineAudioDao.deleteByIdIn(stopCombineAudioIds);
		
		//分页任务
		//先整理成map
		Map<PageInfoPO, MemberChangedTaskBO> pageInfoChangedTaskMap = new HashMap<PageInfoPO, MemberChangedTaskBO>();
		for(PageTaskPO startTask : businessDeliverBO.getStartPageTasks()){
			PageInfoPO info = startTask.getPageInfo();
			MemberChangedTaskBO bo = pageInfoChangedTaskMap.get(info);
			if(bo == null){
				bo = new MemberChangedTaskBO();
				pageInfoChangedTaskMap.put(info, bo);
			}
			if(!checkPageTaskExist(bo.getAddTasks(), startTask)){
//					&& !checkPageTaskExist(info.getPageTasks(), startTask)){
				bo.getAddTasks().add(startTask);
			}
		}
		for(PageTaskPO stopTask : businessDeliverBO.getStopPageTasks()){
			PageInfoPO info = stopTask.getPageInfo();
			MemberChangedTaskBO bo = pageInfoChangedTaskMap.get(info);
			if(bo == null){
				bo = new MemberChangedTaskBO();
				pageInfoChangedTaskMap.put(info, bo);
			}
			bo.getRemoveTasks().add(stopTask);
		}
		
		//再处理分页任务，把删除和新增的相同task去掉
		for(PageInfoPO pageInfo : pageInfoChangedTaskMap.keySet()){
			MemberChangedTaskBO bo = pageInfoChangedTaskMap.get(pageInfo);
			GroupMemberType groupMemberType = pageInfo.getGroupMemberType();
			List<PageTaskPO> thisAddTasks = bo.getAddTasks();
			List<PageTaskPO> thisRemoveTasks = bo.getRemoveTasks();
			List<PageTaskPO> noAddTasks = new ArrayList<PageTaskPO>();
			List<PageTaskPO> noRemoveTasks = new ArrayList<PageTaskPO>();
			for(PageTaskPO addTask : thisAddTasks){
				boolean bEquals = false;
				for(PageTaskPO removeTask : thisRemoveTasks){
					if(addTask.equalsTask(removeTask)){
						noRemoveTasks.add(removeTask);
						bEquals = true;
						break;
					}
				}
				if(bEquals){
					noAddTasks.add(addTask);
				}
			}
			thisAddTasks.removeAll(noAddTasks);
			thisRemoveTasks.removeAll(noRemoveTasks);
			
			if(groupMemberType.equals(GroupMemberType.MEMBER_HALL)
					|| groupMemberType.equals(GroupMemberType.MEMBER_DEVICE)){
				
				//TODO:这里需要改成：去掉bundleId和videoChannelId相同的thisRemoveTasks，或者在addAndRemoveTasks里头做
				if(thisAddTasks.size() != 0){
					pageInfo.getPageTasks().removeAll(thisRemoveTasks);
					pageInfo.setCurrentPage(1);
					thisRemoveTasks.clear();
					pageInfoDao.save(pageInfo);					
				}
				//TODO:去掉两者相同的task
				pageTaskService.addAndRemoveTasks(pageInfo, thisAddTasks, thisRemoveTasks);
			}
			else if(groupMemberType.equals(GroupMemberType.MEMBER_USER)){
				//TODO:去掉两者相同的task
				pageTaskService.addAndRemoveTasks(pageInfo, thisAddTasks, thisRemoveTasks);
			}
		}
		
		if(doProtocol){
			if(businessReturnService.getSegmentedExecute()){
				BusinessReturnBO businessReturnBO = businessReturnService.get();
				LogicBO returnLogic = businessReturnBO.getLogic();
				logic.merge(returnLogic);
				ExecuteBusinessReturnBO response = executeBusiness.execute(logic, message);
				
				//处理录制
				Optional<String> groupUuidOptional = Optional.ofNullable(logic).map(logicT-> logicT.getRecordSet()).map(recordset -> {
					if(recordset.size()>0){
						return recordset.get(0);
					}
					return null;
				}).map(recordBo -> recordBo.getGroupUuid());
				if(groupUuidOptional.isPresent()){
					GroupPO recordGroup = groupDAO.findByUuid(groupUuidOptional.get());
					if(recordGroup != null){
						groupRecordService.saveStoreInfo(response, recordGroup.getId());
					}
				}
				
				businessReturnService.executeWebsocket();
				
				//清空
				businessReturnService.getAndRemove();
			}
		}
	}

	@Autowired
	GroupDAO groupDAO;
	private boolean checkPageTaskExist(List<PageTaskPO> list,PageTaskPO item){
		for (PageTaskPO task:list) {
			if(task.equalsTask(item)) return true;
			/*if(task.getBusinessId().equals(item.getBusinessId())
					&& task.getPageInfo().getId().equals(item.getPageInfo().getId())
					&& ((task.getAgendaForwardSourceId()==null&&item.getAgendaForwardSourceId()==null)||(task.getAgendaForwardSourceId()!=null&&task.getAgendaForwardSourceId().equals(item.getAgendaForwardSourceId())))
					&& (((task.getCombineVideoUuid()==null&&item.getCombineVideoUuid()==null)||(task.getCombineVideoUuid()!=null&&task.getCombineVideoUuid().equals(item.getCombineVideoUuid()))))){
				return true;
			}*/
		}
		return false;

	}
	
}