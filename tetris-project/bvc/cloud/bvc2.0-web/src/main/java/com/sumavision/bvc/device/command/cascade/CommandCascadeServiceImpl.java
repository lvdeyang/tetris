package com.sumavision.bvc.device.command.cascade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.OriginType;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.cascade.util.CommandCascadeUtil;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.cooperate.CommandCooperateServiceImpl;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.command.time.CommandFightTimeServiceImpl;
import com.sumavision.bvc.device.command.vod.CommandVodService;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.tetris.bvc.cascade.CommandCascadeService;
import com.sumavision.tetris.bvc.cascade.ConferenceCascadeService;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandCascadeServiceImpl 
* @Description: 不加事物。级联业务，直接用来对接级联协议，大多是与原有业务不符的部分
* @author zsy
* @date 2020年1月12日 上午10:56:48 
*
 */
@Slf4j
//@Transactional(rollbackFor = Exception.class)
@Service
public class CommandCascadeServiceImpl {
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private CommandGroupMemberDAO commandGroupMemberDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private CommandCooperateServiceImpl commandCooperateServiceImpl;
	
	@Autowired
	private CommandMeetingSpeakServiceImpl commandMeetingSpeakServiceImpl;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private CommandRecordServiceImpl commandRecordServiceImpl;
	
	@Autowired
	private CommandFightTimeServiceImpl commandFightTimeServiceImpl;
	
	@Autowired
	private CommandVodService commandVodService;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;

	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
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
		
	/**
	 * 全量信息同步<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午4:14:23
	 * @param groupBO
	 * @param type
	 * @throws Exception
	 */
	public void memberAddFull(GroupBO groupBO, GroupType type) throws Exception{

		UserVO user = userQuery.findByUserno(groupBO.getOp());
		String uuid = groupBO.getGid();
		CommandGroupPO group = commandGroupDao.findByUuid(uuid);
		if(group != null){
			//如果有之前没删掉的，则停会，删会
			log.warn(uuid + " 全量信息同步，可能是重启恢复中，查到uuid相同的会议/指挥，删除重建：" + group.getName());
			commandBasicServiceImpl.stop(user.getId(), group.getId(), 0);
			commandBasicServiceImpl.remove(user.getId(), new ArrayListWrapper<Long>().add(group.getId()).getList());
		}
		
		
		//建会
		List<MinfoBO> mAddList = groupBO.getmAddList();
		Set<String> usernos = new HashSet<String>();
		Set<String> enterUsernos = new HashSet<String>();
		for(MinfoBO m : mAddList){
			//“已退出”的成员不用加入成员列表。1表示正在业务、2表示暂时离开、3表示已退出
			if("1".equals(m.getMstatus()) || "2".equals(m.getMstatus())){
				usernos.add(m.getMid());
			}
			//统计进会人
			if("1".equals(m.getMstatus())){
				enterUsernos.add(m.getMid());
			}
		}
		//成员id列表
		List<Long> userIdList = commandCascadeUtil.fromUserNosToIds(usernos);
		group = commandBasicServiceImpl.save(user.getId(), user.getId(), user.getUsername(), groupBO.getBizname(), groupBO.getSubject(), type, OriginType.OUTER, userIdList, uuid);
		
		
		//如果有开始时间，则开会。否则认为停会
		//开会，“正在业务”的成员才能进入
		String mtime = groupBO.getStime();
		String status = groupBO.getStatus();
		if(mtime==null || "".equals(mtime)){
			//认为已停会
			log.info(type + " 业务全量同步，业务停止状态，成员总数 " + userIdList.size());
		}else{
			GroupStatus groupStatus = null;
			if("1".equals(status)){
				groupStatus = GroupStatus.PAUSE;
			}else{
				groupStatus = GroupStatus.START;
			}
			Date startTime = DateUtil.parse(mtime, DateUtil.dateTimePattern);
			List<Long> enterUserIdList = commandCascadeUtil.fromUserNosToIds(enterUsernos);
			//测测能否暂停
			commandBasicServiceImpl.start(group.getId(), -1, false, enterUserIdList, startTime, groupStatus);
			log.info(type + " 业务全量同步，业务开始，状态：" + groupStatus + " , 成员总数 " + userIdList.size() + " , 进入人数 " + enterUserIdList.size());
		}
		
		
		//协同或发言、讨论（持久化，logic协议）
		if(GroupType.BASIC.equals(type)){
			//获取协同列表
			List<String> croplist = groupBO.getCroplist();
			if(croplist!=null && croplist.size()>0){
				List<Long> userIdArray = commandCascadeUtil.fromUserNosToIds(croplist);
				//开始协同
				commandCooperateServiceImpl.start(group.getId(), userIdArray);
				log.info("全量同步，开始协同，协同人数：" + userIdArray.size());
			}
		}else if(GroupType.MEETING.equals(type)){
			//获取发言列表。后续改成只能1人发言
			//TODO:讨论，0表示主席模式、1表示讨论模式
			String mode = groupBO.getMode();
			if("1".equals(mode)){
				commandMeetingSpeakServiceImpl.discussStart(group.getUserId(), group.getId());
				log.info("全量同步，开始全员讨论");
			}else{
				String spkid = groupBO.getSpkid();
				if(spkid!=null && !"".equals(spkid)){
					String[] s = spkid.split(",");//这里早期写成逗号分隔的多个成员，实际在业务加了限制，最多只会有一个
					List<String> spkidList = Arrays.asList(s);
					List<Long> userIdArray = commandCascadeUtil.fromUserNosToIds(spkidList);
					if(userIdArray.size() > 0){
						//开始发言
						commandMeetingSpeakServiceImpl.speakAppoint(group.getUserId(), group.getId(), userIdArray);
						log.info("全量同步，开始发言，发言人数：" + userIdArray.size());
					}
				}
			}
		}
		
		//媒体转发
		//后续添加的流程，如其它指挥
		
	}
	
	/** 静态信息同步，要支持成员变更，名称，类型，主题等 */
	public void groupUpdate(GroupBO groupBO, GroupType type) throws Exception{
		
		UserVO user = userQuery.findByUserno(groupBO.getCreatorid());		
		String uuid = groupBO.getGid();
		CommandGroupPO group = commandGroupDao.findByUuid(uuid);
		if(group == null){
			log.info(uuid + " 静态组信息更新，没有找到uuid相同的会议/指挥，新建：" + groupBO.getBizname());
		}else{
			//如果已经存在，则停会，删会
			log.warn(uuid + " 静态组信息更新，查到uuid相同的会议/指挥，删除重建：" + group.getName());
			if(!GroupStatus.STOP.equals(group.getStatus())){
				commandBasicServiceImpl.stop(group.getUserId(), group.getId(), 0);
			}
			commandBasicServiceImpl.remove(group.getUserId(), new ArrayListWrapper<Long>().add(group.getId()).getList());
		}		
		
		//建会
		List<MinfoBO> mlist = groupBO.getMlist();
		Set<String> usernos = new HashSet<String>();
		for(MinfoBO m : mlist){
			usernos.add(m.getMid());
		}
		//成员id列表
		List<Long> userIdList = commandCascadeUtil.fromUserNosToIds(usernos);
		group = commandBasicServiceImpl.save(user.getId(), user.getId(), user.getUsername(), groupBO.getBizname(), groupBO.getSubject(), type, OriginType.OUTER, userIdList, uuid);
		
		log.info(type + " 静态组信息更新，成员总数 " + userIdList.size());		
	}
}
