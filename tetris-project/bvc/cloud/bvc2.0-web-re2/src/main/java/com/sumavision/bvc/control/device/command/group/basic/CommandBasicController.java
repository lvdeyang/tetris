package com.sumavision.bvc.control.device.command.group.basic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeIcon;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeType;
import com.sumavision.tetris.bvc.business.dao.BusinessGroupMemberDAO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.special.GroupSpecialCommonService;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.forward.AgendaForwardDemandPO;
import com.sumavision.bvc.command.group.forward.AgendaForwardDemandVO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.control.utils.TreeUtils;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.CommandForwardServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.ForwardReturnBO;
import com.sumavision.bvc.device.command.basic.osd.CommandOsdServiceImpl;
import com.sumavision.bvc.device.command.basic.page.CommandPageServiceImpl;
import com.sumavision.bvc.device.command.basic.remind.CommandRemindServiceImpl;
import com.sumavision.bvc.device.command.basic.silence.CommandSilenceLocalServiceImpl;
import com.sumavision.bvc.device.command.basic.silence.CommandSilenceServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.CommandGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupForwardService;
import com.sumavision.tetris.bvc.business.group.GroupMemberService;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupQuery;
import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.bvc.business.group.GroupVO;
//import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.bvc.business.group.function.GroupFunctionService;
import com.sumavision.tetris.bvc.business.group.remind.GroupRemindService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import javassist.expr.NewArray;

@Controller
@RequestMapping(value = "/command/basic")
public class CommandBasicController {
	
	@Autowired
	private BusinessCommonService businessCommonService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupMemberService groupMemberService;
	
	@Autowired
	private GroupForwardService groupForwardService;
	
	@Autowired
	private GroupFunctionService groupFunctionService;

	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;

	@Autowired
	private CommandForwardServiceImpl commandForwardServiceImpl;

	@Autowired
	private CommandSilenceServiceImpl commandSilenceServiceImpl;

	@Autowired
	private CommandRemindServiceImpl commandRemindServiceImpl;

	@Autowired
	private GroupRemindService groupRemindService;

	@Autowired
	private GroupSpecialCommonService groupSpecialCommonService;

	@Autowired
	private CommandSilenceLocalServiceImpl commandSilenceLocalServiceImpl;

	@Autowired
	private CommandPageServiceImpl commandPageServiceImpl;

	@Autowired
	private GroupDAO groupDao;

	@Autowired
	private GroupMemberDAO groupMemberDao;

	@Autowired
	private CommandGroupDAO commandGroupDao;

	@Autowired
	private CommandGroupRecordDAO commandGroupRecordDao;

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private TreeUtils treeUtils;

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommandOsdServiceImpl commandOsdServiceImpl;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	@Autowired
	private GroupQuery groupQuery;

	@Autowired
	private BusinessGroupMemberDAO businessGroupMemberDAO;
	
	/**
	 * 查询会议或指挥名称<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月18日 下午4:45:57
	 * @param Long id 会议或指挥id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/group")
	public Object queryGroup(
			String id,
			HttpServletRequest request) throws Exception{
		
		GroupPO group = groupDao.findOne(Long.parseLong(id));
		if(group == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到指挥或会议，id: " + id);
		}
		
		Map<String, Object> map = new HashMapWrapper<String, Object>()
				.put("name", group.getName())
				.getMap();
		
		return map;
	}

	/**
	 * 查询会议的所有成员及状态<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:20:15
	 * @param id 会议组id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/members")
	public Object queryMembers(
			String id,
			Boolean withChannel,
			String channelTypeList,
			HttpServletRequest request) throws Exception{

//		UserVO user = userUtils.getUserFromSession(request);
		GroupPO group = groupDao.findOne(Long.parseLong(id));

		JSONObject info = new JSONObject();
		info.put("id", group.getId().toString());
		info.put("name", group.getName());
		info.put("status", group.getStatus().getCode());
		info.put("commander", group.getUserId());
		info.put("creator", group.getUserId());
		if(withChannel == null){ 
			withChannel = false;
		}
		Object membersArray = treeUtils.queryGroupTree(group.getId(),withChannel,channelTypeList);
		info.put("members", membersArray);
//		List<CommandGroupRecordPO> runningRecords = commandGroupRecordDao.findByGroupIdAndRun(group.getId(), true);
//		if(runningRecords.size() > 0){
//			info.put("isRecord", true);
//		}else{
//			info.put("isRecord", false);
//		}
		info.put("isRecord", group.getIsRecord());
		info.put("isPublishStream", group.getIsPublishment());
		
		//特殊指挥
		String specialCommand = groupSpecialCommonService.querySpecialCommandString(group);
		info.put("specialCommand", specialCommand);

		return info;

//		return queryMembersList(id, request);
	}


	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/cooperate/members")
	public Object queryCooperateMembers(
			String id,
			HttpServletRequest request) throws Exception{

//		UserVO user = userUtils.getUserFromSession(request);

		GroupPO group = groupDao.findOne(Long.parseLong(id));

		JSONObject info = new JSONObject();
		info.put("id", group.getId().toString());
		info.put("name", group.getName());
		info.put("status", group.getStatus().getCode());
		info.put("commander", group.getUserId());
		info.put("creator", group.getUserId());
		Object membersArray = treeUtils.queryCoorateGroupTree(group.getId(),false,"");
		info.put("members", membersArray);
		List<CommandGroupRecordPO> runningRecords = commandGroupRecordDao.findByGroupIdAndRun(group.getId(), true);
		if(runningRecords.size() > 0){
			info.put("isRecord", true);
		}else{
			info.put("isRecord", false);
		}

		return info;

//		return queryMembersList(id, request);
	}

	/**
	 * 查询会议的所有成员及状态，列表呈现，不再是树<br/>
	 * <p>目前只支持用户成员</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午11:34:06
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/members/list")
	public Object queryMembersList(
			String id,
			HttpServletRequest request) throws Exception{
		
		if(id == null){
			return null;
		}
		GroupPO group = groupDao.findOne(Long.parseLong(id));
		
		JSONObject info = new JSONObject();
		info.put("id", group.getId().toString());
		info.put("name", group.getName());
		info.put("status", group.getStatus().getCode());
		info.put("commander", group.getUserId());
		info.put("creator", group.getUserId());
		List<CommandGroupRecordPO> runningRecords = commandGroupRecordDao.findByGroupIdAndRun(group.getId(), true);
		if(runningRecords.size() > 0){
			info.put("isRecord", true);
		}else{
			info.put("isRecord", false);
		}
		
		List<BusinessGroupMemberPO> members =businessGroupMemberDAO.findByGroupId(group.getId());
		List<UserBO> userBOs = tetrisBvcQueryUtil.queryUsersByMembers(members);
		
		//成员列表节点
		TreeNodeVO commandRoot = new TreeNodeVO().setId(String.valueOf(TreeNodeVO.FOLDERID_COMMAND))
											     .setName("成员列表")
											     .setType(TreeNodeType.FOLDER)
											     .setKey()
											     .setIcon(TreeNodeIcon.FOLDER.getName())
											     .setChildren(new ArrayList<TreeNodeVO>());
		
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		
		//先放入主席
		BusinessGroupMemberPO chairmanMember = tetrisBvcQueryUtil.queryChairmanMember(members);
		UserBO chairmanUserBO = queryUtil.queryUserById(userBOs, Long.parseLong(chairmanMember.getOriginId()));
		FolderPO chairmanFolder = queryUtil.queryFolderPOById(totalFolders, chairmanMember.getFolderId());
		TreeNodeVO chairmanMemberTree = new TreeNodeVO().setWithInfo(chairmanMember, chairmanUserBO, chairmanFolder);
		commandRoot.getChildren().add(chairmanMemberTree);
		for(BusinessGroupMemberPO member : members){
			if(member.getIsAdministrator()) continue;
			UserBO userBO = queryUtil.queryUserById(userBOs, Long.parseLong(member.getOriginId()));
			FolderPO folder = queryUtil.queryFolderPOById(totalFolders, member.getFolderId());
			TreeNodeVO commandTree = new TreeNodeVO().setWithInfo(member, userBO, folder);
			commandRoot.getChildren().add(commandTree);
		}
		List<TreeNodeVO> rootList = new ArrayList<TreeNodeVO>();
		rootList.add(commandRoot);
		info.put("members", rootList);
		
		//特殊指挥
		String specialCommand = groupSpecialCommonService.querySpecialCommandString(group);
		info.put("specialCommand", specialCommand);
		
		return info;
	}
	
	/**
	 * 重构新建会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月7日 上午11:34:54
	 * @param members json格式的用户id列表
	 * @param hallIds json格式的会场id列表
	 * @param chairmanType null或self为默认，以创建人的用户作为主席；user指定用户为主席；hall指定会场为主席；tvosOfUser以创建人的机顶盒为主席
	 * @param chairmanId 用户id/会场id
	 * @param name 会议名称
	 * @param orderGroupType 预约会议类型BusinessOrderGroupType
	 * @param orderBeginTime 预约开始时间
	 * @param orderEndTime 预约结束时间
	 * @param groupLock 锁定会议 GroupLock
	 * @param Boolean temporary 是否是临时会议,true：临时会议
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save")
	public Object save(
			String members,
			String hallIds,
			String chairmanType,
			String chairmanId,
			String name,
			String orderGroupType,
			String orderBeginTime,
			String orderEndTime,
			String groupLock,
			Boolean temporary,
			HttpServletRequest request) throws Exception{
		
		//throw new BaseException(StatusCode.FORBIDDEN, "暂不支持，请新建会议");
		
	
		UserVO user = userUtils.getUserFromSession(request);
		
		Date date = new Date();
		String createTime = DateUtil.format(date, DateUtil.dateTimePattern);
		//未输入则生成一个name
		if(name==null || name.equals("")){
			String commandString = commandCommonUtil.generateCommandString(GroupType.BASIC);
			name = new StringBuilder().append(user.getName())
				   .append(" ")
				   .append(createTime)
				   .append(" ")
				   .append(commandString)
				   .toString();
		}
		
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		List<Long> hallIdArray = JSONArray.parseArray(hallIds, Long.class);
		List<String> bundleIdArray = new ArrayList<String>();
		
		GroupPO group = null;
		try{
			if(temporary != null && temporary){
				group = groupService.saveAndStartTemporary(user.getId(), user.getName(), chairmanType, chairmanId, name, name, BusinessType.COMMAND, com.sumavision.tetris.bvc.business.OriginType.INNER, userIdArray,
						hallIdArray, bundleIdArray, null, orderGroupType, orderBeginTime, orderEndTime, groupLock);
			}else {
				group = groupService.saveCommand_TS(user.getId(), user.getName(), chairmanType, chairmanId, name, name, BusinessType.COMMAND, com.sumavision.tetris.bvc.business.OriginType.INNER, userIdArray,
						hallIdArray, bundleIdArray, null, orderGroupType, orderBeginTime, orderEndTime, groupLock, null);
			}
			
		}catch(CommandGroupNameAlreadyExistedException e){
			//重名
			JSONObject info = new JSONObject();
			info.put("status", "error");
			JSONObject errorInfo = new JSONObject();
			errorInfo.put("type", "");
			errorInfo.put("msg", "名称已被使用");
			errorInfo.put("recommendedName", e.getRecommendedName());
			info.put("errorInfo", errorInfo);
			return info;
		}
		
		JSONObject info = new JSONObject();
		info.put("id", group.getId().toString());
		info.put("name", group.getName());
		info.put("status", group.getStatus().getCode());
		info.put("commander", group.getUserId());
		info.put("creator", group.getUserId());
		info.put("group", new GroupVO().set(group));
//		Object membersArray = treeUtils.queryGroupTree(group.getId());
//		info.put("members", membersArray);
				
		return info;
	}

	/**
	 * 修改名称
	 * @param id 会议组Id
	 * @param name 会议名称
	 * @param groupLock GroupPO里面的GroupLock的枚举类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			long id,
			String name,
			String groupLock,
			HttpServletRequest request) throws Exception{
		GroupPO groupPO= groupService.edit(id,name,groupLock);
		return groupPO.getId();

	}
	
	/**
	 * 修改会议名称<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月16日 上午9:36:17
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/modify/name")
	public Object modifyName(
			String id,
			String name,
			HttpServletRequest request) throws Exception{

		groupService.edit(Long.valueOf(id), name, null);
//		Long userId = userUtils.getUserIdFromSession(request);
//		
//		commandBasicServiceImpl.modifyName(userId, Long.parseLong(id), name);
		
		return null;
	}
	
	/**
	 * 删除会议（批量）<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年9月26日
	 * @param ids
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			String ids,
			HttpServletRequest request) throws Exception{

		Long userId = userUtils.getUserIdFromSession(request);
		
		JSONArray idsArray = JSON.parseArray(ids);
		List<Long> groupIds = new ArrayList<Long>();
		for(int i=0; i<idsArray.size(); i++){
			Long id = Long.parseLong(idsArray.getString(i));
			groupIds.add(id);
		}		
		
		groupService.remove(userId, groupIds);
		
		return null;
	}
		
	/**
	 * 进入会议，获取会议信息（批量）<br/>
	 * <p>非主席的成员会按照“接听”处理，主席不处理</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:48:08
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/enter")
	public Object enter(
			String ids,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		Long userId = userUtils.getUserIdFromSession(request);
		
		JSONArray idsArray = JSON.parseArray(ids);
		List<Long> groupIds = new ArrayList<Long>();
		for(int i=0; i<idsArray.size(); i++){
			Long id = Long.parseLong(idsArray.getString(i));
			groupIds.add(id);
		}		
		
		JSONArray groupInfos = groupService.enterGroups(userId, groupIds);
		
		for(int i=0; i<groupInfos.size(); i++){
			JSONObject groupInfo = groupInfos.getJSONObject(i);
			Long groupId = Long.parseLong(groupInfo.getString("id"));
			Object members = treeUtils.queryGroupTree(groupId, false, null);
			groupInfo.put("members", members);
		}
		
		return groupInfos;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/refuse")
	public Object refuse(
			String id,
			HttpServletRequest request) throws Exception{

		businessReturnService.init(Boolean.TRUE);
		/*Long userId = userUtils.getUserIdFromSession(request);
		
		commandBasicServiceImpl.refuse(userId, Long.parseLong(id));*/
		return null;
	}
//	
//	
//	//查询某个会议的所有成员及状态（方法名暂定）
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/query/tree/{groupId}", method = RequestMethod.GET)
//	public Object queryTree(
//			@PathVariable Long groupId,
//			HttpServletRequest request) throws Exception{
//		return null;
//	}
//	
//	
//	//查询当前会议外的所有成员
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/query/members/except/group/{groupId}")
//	@Deprecated
//	public Object queryMembersExceptGroup(
//			HttpServletRequest request) throws Exception{
//		return null;
//	}
	
	/**
	 * 开始会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:49:25
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/start")
	public Object start(
			String id,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		Object result = groupService.start(Long.parseLong(id), -1);
		return result;		
	}
	
	/**
	 * 停止会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:49:57
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop")
	public Object stop(
			String id,
			HttpServletRequest request) throws Exception{

		Long userId = userUtils.getUserIdFromSession(request);
		
		businessReturnService.init(Boolean.TRUE);
		Object chairSplits = groupService.stop(userId, Long.parseLong(id), 0);
		return chairSplits;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/pause")
	public Object pause(
			String id,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		groupFunctionService.pause(Long.parseLong(id));
		return new JSONArray();
	}	
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/pause/recover")
	public Object pauseRecover(
			String id,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		groupFunctionService.pauseRecover(Long.parseLong(id));
		return new JSONArray();
	}
	
	
//	/** 成员主动退出（已废弃，改为申请） */
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/exit")
//	public Object exit(
//			String id,
//			HttpServletRequest request) throws Exception{
//		
////		Long userId = userUtils.getUserIdFromSession(request);
////		List<Long> userIdList = new ArrayListWrapper<Long>().add(userId).getList();
////		Object splits = commandBasicServiceImpl.removeMembers(Long.parseLong(id), userIdList, 0);
////		return splits;
//		throw new BaseException(StatusCode.FORBIDDEN, "请向主席申请退出");
//	}	
//	
//	/** 成员申请退出 */
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/exit/apply")
//	public Object exitApply(
//			String id,
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		
//		businessReturnService.init(Boolean.TRUE);
//		groupService.exitApply(userId, Long.parseLong(id));
//		return null;
//	}
//	
//	/** 主席同意成员退出 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/exit/apply/agree")
//	public Object exitApplyAgree(
//			String id,
//			String userIds,
//			HttpServletRequest request) throws Exception{
//		
////		UserVO user = userUtils.getUserFromSession(request);
//		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
//		
////		businessReturnService.init(Boolean.TRUE);
//		Object splits = groupService.removeMembersByMemberIds(Long.parseLong(id), userIdArray, 0);
//		
//		return splits;
//	}
//	
//	/** 主席不同意成员退出 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/exit/apply/disagree")
//	public Object exitApplyDisagree(
//			String id,
//			String userIds,
//			HttpServletRequest request) throws Exception{
//		
//		UserVO user = userUtils.getUserFromSession(request);
//		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
//		
//		businessReturnService.init(Boolean.TRUE);
//		groupService.exitApplyDisagree(user.getId(), Long.parseLong(id), userIdArray);
//		
//		return null;
//	}

	/**
	 * 添加成员<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月25日 上午10:58:55
	 * @param id 会议id
	 * @param members 成员的id集合
	 * @param hallIds 会场的id集合
	 * @return
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/add/members")
	public Object addMembers(
			String id,
			String members,
			String hallIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		List<Long> hallIdArray = JSONArray.parseArray(hallIds, Long.class);
		
		businessReturnService.init(Boolean.TRUE);
		Object splits = groupService.addOrEnterMembers(Long.parseLong(id), userIdArray, hallIdArray, null);
		return splits;
	}
		
	/**
	 * 强退成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午1:59:36
	 * @param id
	 * @param members
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/members")
	public Object removeMembers(
			String id,
			String members,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		
		businessReturnService.init(Boolean.TRUE);
		Object splits = groupService.removeMembersByMemberIds(Long.parseLong(id), userIdArray, 2);
		return splits;
	}	
	
	/**
	 * 会议中的一个成员开启对上静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:12:52
	 * @param id 会议id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/silence/up/start")
	public Object silenceToHigher(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		businessReturnService.init(Boolean.TRUE);
		groupFunctionService.startSilence(Long.parseLong(id), userId, true, false);
		
		return null;
	}
	
	
	/**
	 * 会议中的一个成员停止对上静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:13:25
	 * @param id 会议id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/silence/up/stop")
	public Object stopSilenceToHigher(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		businessReturnService.init(Boolean.TRUE);
		groupFunctionService.stopSilence(Long.parseLong(id), userId, true, false);
		
		return null;
	}
	
	
	/**
	 * 会议中的一个成员开启对下静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:13:42
	 * @param id 会议id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/silence/down/start")
	public Object silenceToLower(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		businessReturnService.init(Boolean.TRUE);
		groupFunctionService.startSilence(Long.parseLong(id), userId, false, true);
		
		return null;
	}
	
	
	/**
	 * 会议中的一个成员停止对下静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:13:56
	 * @param id 会议id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/silence/down/stop")
	public Object stopSilenceToLower(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		businessReturnService.init(Boolean.TRUE);
		groupFunctionService.stopSilence(Long.parseLong(id), userId, false, true);
		
		return null;
	}
	
//	/**
//	 * 主席点播成员<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年12月2日 下午1:51:11
//	 * @param id
//	 * @param userId 被点播成员的userId
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	/*@Deprecated
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/vod/member/start")
//	public Object vodMemberStart(
//			String id,
//			String userId,
//			HttpServletRequest request) throws Exception{
//		
//		Long dstUserId = userUtils.getUserIdFromSession(request);
//		UserBO userBO = userUtils.queryUserById(dstUserId);
//		
//		CommandGroupUserPlayerPO player = commandBasicServiceImpl.vodMemberStart(userBO, Long.parseLong(id), Long.parseLong(userId));
//		
//		return new CommandGroupUserPlayerSettingVO().set(player);
//	}*/
//	
//	/**
//	 * 主席关闭点播成员<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年12月2日 下午1:50:42
//	 * @param businessId 会议id-成员userId
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@Deprecated
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/vod/member/stop")
//	public Object vodMemberStop(
//			String businessId,
//			HttpServletRequest request) throws Exception{
//		
//		Long dstUserId = userUtils.getUserIdFromSession(request);
//		String groupId = businessId.split("-")[0];
//		String srcUserId = businessId.split("-")[1];
//		
//		CommandGroupUserPlayerPO player = commandBasicServiceImpl.vodMemberStop(dstUserId, Long.parseLong(groupId), Long.parseLong(srcUserId));
//		
//		if(player != null){
//			return JSON.toJSONString(new HashMapWrapper<String, Object>().put("serial", player.getLocationIndex()).getMap());
//		}else{
//			return null;
//		}
//	}	
//	
//	/**
//	 * 主席换一批观看成员<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年4月10日 下午6:34:36
//	 * @param id
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@Deprecated
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/roll/all/vod/members")
//	public Object rollAllVodMembers(
//			String id,
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		
//		List<CommandGroupUserPlayerPO> players = commandPageServiceImpl.rollAllVodMembers(userId, Long.parseLong(id));
//		List<CommandGroupUserPlayerSettingVO> playerVOs = new ArrayList<CommandGroupUserPlayerSettingVO>();
//		for(CommandGroupUserPlayerPO player : players){
//			CommandGroupUserPlayerSettingVO playerVO = new CommandGroupUserPlayerSettingVO().set(player);
//			playerVOs.add(playerVO);
//		}
//		
//		return JSON.toJSONString(new HashMapWrapper<String, Object>()
//				.put("splits", playerVOs)
//				.getMap());
//	}
	
	/**
	 * 会议转发用户<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月19日 下午5:09:03
	 * @param id 会议id
	 * @param src 源用户id数组
	 * @param dst 目的用户id数组
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/user")
	public Object forwardUser(
			String id,
			String src,
			String dst,
			HttpServletRequest request) throws Exception{

		List<Long> srcUserIds = JSONArray.parseArray(src, Long.class);
		List<Long> dstMemberId = JSONArray.parseArray(dst, Long.class);
		
		businessReturnService.init(Boolean.TRUE);
		List<AgendaForwardDemandPO> fs = groupForwardService.forwardM(Long.parseLong(id), srcUserIds, null, dstMemberId);
		
		List<AgendaForwardDemandVO> rows = new ArrayList<AgendaForwardDemandVO>();
		for(AgendaForwardDemandPO demand : fs){
			AgendaForwardDemandVO demandVo = new AgendaForwardDemandVO();
			demandVo.set(demand);
			rows.add(demandVo);
		}
		
		return rows;
	}

	/**
	 * 会议转发设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午1:59:58
	 * @param id 会议id
	 * @param src 源bundleId数组
	 * @param dst groupMemberId数组
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/device")
	public Object forwardDevice(
			String id,
			String src,
			String dst,
			HttpServletRequest request) throws Exception{

		List<String> bundleIds = JSONArray.parseArray(src, String.class);
		List<Long> groupMemberIds = JSONArray.parseArray(dst, Long.class);
		
		businessReturnService.init(Boolean.TRUE);
		List<AgendaForwardDemandPO> fs = groupForwardService.forwardM(Long.parseLong(id), null, bundleIds, groupMemberIds);
		
		List<AgendaForwardDemandVO> rows = new ArrayList<AgendaForwardDemandVO>();
		for(AgendaForwardDemandPO demand : fs){
			AgendaForwardDemandVO demandVo = new AgendaForwardDemandVO();
			demandVo.set(demand);
			rows.add(demandVo);
		}
		
		return rows;
	}
//	
//	/**
//	 * 成员同意转发设备<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年11月15日 下午2:00:14
//	 * @param id
//	 * @param forwardIds
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@Deprecated
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/forward/device/agree")
//	public Object forwardDeviceAgree(
//			String id,
//			String forwardIds,
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		
//		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
//		
//		businessReturnService.init(Boolean.FALSE);
//		List<Object> result = commandForwardServiceImpl.forwardAgree(userId, Long.parseLong(id), demandIds);
//		
//		return result;
//	}
//	
//	/**
//	 * 成员拒绝转发设备<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年12月3日 上午11:37:43
//	 * @param id
//	 * @param forwardIds
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/forward/device/refuse")
//	public Object forwardDeviceRefuse(
//			String id,
//			String forwardIds,
//			HttpServletRequest request) throws Exception{
//		
//		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
//		
//		businessReturnService.init(Boolean.FALSE);
//		commandForwardServiceImpl.forwardRefuse(Long.parseLong(id), demandIds);
//		
//		return null;
//	}
//	
	/**
	 * 会议转发文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:00:30
	 * @param id
	 * @param src
	 * @param dst
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/file")
	public Object forwardFile(
			Long id,
			String src,
			String dst,
			HttpServletRequest request) throws Exception{

		List<String> resourceIds = JSONArray.parseArray(src, String.class);
		List<Long> memberIds = JSONArray.parseArray(dst, Long.class);
		
		businessReturnService.init(Boolean.TRUE);
		groupForwardService.forwardFile(id, resourceIds, memberIds);
		
		return null;
	}
//	
//	/**
//	 * 成员同意转发文件<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年11月15日 下午2:00:43
//	 * @param id
//	 * @param forwardIds
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@Deprecated
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/forward/file/agree")
//	public Object forwardFileAgree(
//			String id,
//			String forwardIds,
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		
//		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
//		
//		businessReturnService.init(Boolean.FALSE);
//		List<Object> result = commandForwardServiceImpl.forwardAgree(userId, Long.parseLong(id), demandIds);
//		
//		return result;
//	}
//	
//	/**
//	 * 成员拒绝转发文件<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>Administrator<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年12月3日 上午11:38:13
//	 * @param id
//	 * @param forwardIds
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/forward/file/refuse")
//	public Object forwardFileRefuse(
//			String id,
//			String forwardIds,
//			HttpServletRequest request) throws Exception{
//		
//		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
//		
//		businessReturnService.init(Boolean.FALSE);
//		commandForwardServiceImpl.forwardRefuse(Long.parseLong(id), demandIds);
//		
//		return null;
//	}
//	
	
	/**
	 * 主席停止一个会议中的多个转发<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月20日 下午4:04:48
	 * @param id 会议组id
	 * @param forwardIds 议程转发id
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/stop/by/chairman")
	public Object forwardStopByChairman(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		Long userId = userUtils.getUserIdFromSession(request);
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		groupForwardService.stop(userId, Long.parseLong(id), demandIds);
		
		return null;
	}
	
//	/**
//	 * 主席停止一个会议中的多个转发<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年11月15日 下午2:01:05
//	 * @param id
//	 * @param forwardIds
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/forward/stop/by/chairman")
//	public Object forwardStopByChairman(
//			String id,
//			String forwardIds,
//			HttpServletRequest request) throws Exception{
//		
//		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
//		
//		businessReturnService.init(Boolean.FALSE);
//		commandForwardServiceImpl.stopByChairman(Long.parseLong(id), demandIds);
//		
//		return null;
//	}
	
	/**
	 * 成员停止多个转发<br/>
	 * <p>支持不同会议中的转发</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:01:28
	 * @param businessId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/stop/by/member")
	public Object forwardStopByMember(
			String businessId,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		Long userId = userUtils.getUserIdFromSession(request);
		
		Long groupId = Long.parseLong(businessId.split("-")[0]);
		Long demandId = Long.parseLong(businessId.split("-")[1]);
		List<Long> demandIds = new ArrayListWrapper<Long>().add(demandId).getList();
		
		groupForwardService.stop(userId, groupId, demandIds);
		
		return new JSONArray();
	}
	
	/**
	 * 开启会议提醒<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月20日 上午11:04:02
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remind")
	public Object remind(
			String id,
			HttpServletRequest request) throws Exception{
		
		JSONObject chairSplit = groupRemindService.remind(Long.parseLong(id));
		return chairSplit;
	}
	
	/**
	 * 关闭会议提醒<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月20日 上午11:04:16
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remind/stop")
	public Object remindStop(
			String id,
			HttpServletRequest request) throws Exception{
		
		JSONObject chairSplit = groupRemindService.remindStop(Long.parseLong(id));
		return chairSplit;
	}
	
//	/**
//	 * 停止用户本地视频发送<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年2月20日 下午4:05:08
//	 * @param userIds
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@Deprecated
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/stop/user/video/send")
//	public Object stopUsersVideoSend(
//			String userIds,
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		UserBO user = userUtils.queryUserById(userId);
//		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
//		
//		commandSilenceLocalServiceImpl.operate(user, userIdArray, 1, 2);
//		
//		return null;
//	}
//	
//	/**
//	 * 停止用户本地音频发送<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年2月20日 下午4:05:40
//	 * @param userIds
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@Deprecated
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/stop/user/audio/send")
//	public Object stopUsersAudioSend(
//			String userIds,
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		UserBO user = userUtils.queryUserById(userId);
//		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
//		
//		commandSilenceLocalServiceImpl.operate(user, userIdArray, 2, 2);
//		
//		return null;
//	}
	
	/**
	 * 设置osd<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 下午2:51:02
	 * @param Integer serial 布局序号
	 * @param Long osdId osd id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/osd")
	public Object setOsd(
			Integer serial,
			Long osdId,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		commandOsdServiceImpl.setOsd(serial, osdId);
		return null;
	}
	
	/**
	 * 清除字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 下午2:53:37
	 * @param Integer serial 布局序号
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/clear/osd")
	public Object clearOsd(
			Integer serial,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(Boolean.TRUE);
		commandOsdServiceImpl.clearOsd(serial);
		return null;
	}
	

	/**
	 * 查询当前会议成员所拥有的终端类型<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月22日 上午11:27:19
	 * @param groupId 会议的id
	 * @return Set<String>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/terminal/type/of/group")
	public Object queryTerminalTypeOfGroup(Long groupId) throws Exception{
		Map<Long, String> terminalIdAndTerminalNameMap = groupQuery.queryTerminalType(groupId);
		JSONArray jsonArray = new JSONArray();
		for(Entry<Long, String> entry : terminalIdAndTerminalNameMap.entrySet()){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("terminalId", entry.getKey());
			jsonObject.put("terminalName", entry.getValue());
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	
	/**
	 * 重置会议组的成员信息<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月31日 上午9:11:22
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/reset/group/member")
	public Object resetGroupMember(Long groupId) throws Exception{
		
		groupMemberService.resetGroupMember(groupId);
		return null;
	}
	
	/**
	 * 刷新会议组的成员信息<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月31日 上午11:15:16
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/refresh/group/member")
	public Object refreshGroupMember(Long groupId) throws Exception{
		
		groupMemberService.resetGroupMember(groupId);
		return null;
	}
	
}
