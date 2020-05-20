package com.sumavision.bvc.control.device.command.group.basic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.OriginType;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserPlayerSettingVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeIcon;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeType;
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
import com.sumavision.bvc.device.command.exception.UserHasNoFolderException;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/basic")
public class CommandBasicController {

	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;

	@Autowired
	private CommandForwardServiceImpl commandForwardServiceImpl;

	@Autowired
	private CommandSilenceServiceImpl commandSilenceServiceImpl;

	@Autowired
	private CommandRemindServiceImpl commandRemindServiceImpl;

	@Autowired
	private CommandSilenceLocalServiceImpl commandSilenceLocalServiceImpl;

	@Autowired
	private CommandPageServiceImpl commandPageServiceImpl;

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
	private ResourceService resourceService;
	
	@Autowired
	private CommandOsdServiceImpl commandOsdServiceImpl;
	
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
		
		CommandGroupPO group = commandGroupDao.findOne(Long.parseLong(id));
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
			HttpServletRequest request) throws Exception{
		
//		UserVO user = userUtils.getUserFromSession(request);
		
		CommandGroupPO group = commandGroupDao.findOne(Long.parseLong(id));
		
		JSONObject info = new JSONObject();
		info.put("id", group.getId().toString());
		info.put("name", group.getName());
		info.put("status", group.getStatus().getCode());
		info.put("commander", group.getUserId());
		info.put("creator", group.getUserId());
		Object membersArray = treeUtils.queryGroupTree(group.getId());
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
	 * <p>详细描述</p>
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
		
		CommandGroupPO group = commandGroupDao.findOne(Long.parseLong(id));
		
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
		
		List<CommandGroupMemberPO> members = group.getMembers();
		List<UserBO> userBOs = commandCommonUtil.queryUsersByMembers(members);
		
		//成员列表节点
		TreeNodeVO commandRoot = new TreeNodeVO().setId(String.valueOf(TreeNodeVO.FOLDERID_COMMAND))
											     .setName("成员列表")
											     .setType(TreeNodeType.FOLDER)
											     .setKey()
											     .setIcon(TreeNodeIcon.FOLDER.getName())
											     .setChildren(new ArrayList<TreeNodeVO>());
		
		List<FolderPO> totalFolders = resourceService.queryAllFolders();		
		
		//先放入主席
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		UserBO chairmanUserBO = queryUtil.queryUserById(userBOs, chairmanMember.getUserId());
		FolderPO chairmanFolder = queryUtil.queryFolderPOById(totalFolders, chairmanMember.getFolderId());
		TreeNodeVO chairmanMemberTree = new TreeNodeVO().setWithInfo(chairmanMember, chairmanUserBO, chairmanFolder);
		commandRoot.getChildren().add(chairmanMemberTree);
		for(CommandGroupMemberPO member : members){
			if(member.isAdministrator()) continue;
			UserBO userBO = queryUtil.queryUserById(userBOs, member.getUserId());
			FolderPO folder = queryUtil.queryFolderPOById(totalFolders, member.getFolderId());
			TreeNodeVO commandTree = new TreeNodeVO().setWithInfo(member, userBO, folder);
			commandRoot.getChildren().add(commandTree);
		}
		List<TreeNodeVO> rootList = new ArrayList<TreeNodeVO>();
		rootList.add(commandRoot);
		info.put("members", rootList);
		
		return info;		
	}
		
	/**
	 * 新建会议<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年9月26日
	 * @param userIdList json格式的用户id列表
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save")
	public Object save(
			String members,
			String name,
			HttpServletRequest request) throws Exception{
		//考虑区分创建者和主席
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
		String subject = name;
		
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		
		CommandGroupPO group = null;
		try{
			group = commandBasicServiceImpl.save(user.getId(), user.getId(), user.getName(), name, subject, GroupType.BASIC, OriginType.INNER, userIdArray);
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
		Object membersArray = treeUtils.queryGroupTree(group.getId());
		info.put("members", membersArray);
				
		return info;
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

		Long userId = userUtils.getUserIdFromSession(request);
		
		commandBasicServiceImpl.modifyName(userId, Long.parseLong(id), name);
		
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
		
		commandBasicServiceImpl.remove(userId, groupIds);
		
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
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		JSONArray idsArray = JSON.parseArray(ids);
		List<Long> groupIds = new ArrayList<Long>();
		for(int i=0; i<idsArray.size(); i++){
			Long id = Long.parseLong(idsArray.getString(i));
			groupIds.add(id);
		}		
		
		JSONArray groupInfos = commandBasicServiceImpl.enter(userId, groupIds);
		
		for(int i=0; i<groupInfos.size(); i++){
			JSONObject groupInfo = groupInfos.getJSONObject(i);
			Long groupId = Long.parseLong(groupInfo.getString("id"));
			Object members = treeUtils.queryGroupTree(groupId);
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

		Long userId = userUtils.getUserIdFromSession(request);
		
		commandBasicServiceImpl.refuse(userId, Long.parseLong(id));
		return null;
	}
	
	
	//查询某个会议的所有成员及状态（方法名暂定）
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/tree/{groupId}", method = RequestMethod.GET)
	public Object queryTree(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		return null;
	}
	
	
	//查询当前会议外的所有成员
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/members/except/group/{groupId}")
	@Deprecated
	public Object queryMembersExceptGroup(
			HttpServletRequest request) throws Exception{
		return null;
	}
	
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
		
		Object result = commandBasicServiceImpl.start(Long.parseLong(id), -1);
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
		Object chairSplits = commandBasicServiceImpl.stop(userId, Long.parseLong(id), 0);
		return chairSplits;
	}
	
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/pause")
	public Object pause(
			String id,
			HttpServletRequest request) throws Exception{
		
		JSONArray chairSplits = commandBasicServiceImpl.pause(Long.parseLong(id));
		return chairSplits;
	}
	
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/pause/recover")
	public Object pauseRecover(
			String id,
			HttpServletRequest request) throws Exception{
		
		JSONArray chairSplits = commandBasicServiceImpl.pauseRecover(Long.parseLong(id));
		return chairSplits;
	}	
	
	
	/** 成员主动退出（已废弃，改为申请） */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/exit")
	public Object exit(
			String id,
			HttpServletRequest request) throws Exception{
		
//		Long userId = userUtils.getUserIdFromSession(request);
//		List<Long> userIdList = new ArrayListWrapper<Long>().add(userId).getList();
//		Object splits = commandBasicServiceImpl.removeMembers(Long.parseLong(id), userIdList, 0);
//		return splits;
		throw new BaseException(StatusCode.FORBIDDEN, "请向主席申请退出");
	}	
	
	/** 成员申请退出 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/exit/apply")
	public Object exitApply(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		commandBasicServiceImpl.exitApply(userId, Long.parseLong(id));
		return null;
	}
	
	/** 主席同意成员退出 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/exit/apply/agree")
	public Object exitApplyAgree(
			String id,
			String userIds,
			HttpServletRequest request) throws Exception{
		
//		UserVO user = userUtils.getUserFromSession(request);
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		Object splits = commandBasicServiceImpl.removeMembers2(Long.parseLong(id), userIdArray, 0);
		
		return splits;
	}
	
	/** 主席不同意成员退出 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/exit/apply/disagree")
	public Object exitApplyDisagree(
			String id,
			String userIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		commandBasicServiceImpl.exitApplyDisagree(user.getId(), Long.parseLong(id), userIdArray);
		
		return null;
	}

	//添加成员
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/add/members")
	public Object addMembers(
			String id,
			String members,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		Object splits = commandBasicServiceImpl.addOrEnterMembers(Long.parseLong(id), userIdArray);
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
		Object splits = commandBasicServiceImpl.removeMembers2(Long.parseLong(id), userIdArray, 2);
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
		
		commandSilenceServiceImpl.startSilence(Long.parseLong(id), userId, true, false);
		
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
		
		commandSilenceServiceImpl.stopSilence(Long.parseLong(id), userId, true, false);
		
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
		
		commandSilenceServiceImpl.startSilence(Long.parseLong(id), userId, false, true);
		
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
		
		commandSilenceServiceImpl.stopSilence(Long.parseLong(id), userId, false, true);
		
		return null;
	}
	
	/**
	 * 主席点播成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 下午1:51:11
	 * @param id
	 * @param userId 被点播成员的userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/vod/member/start")
	public Object vodMemberStart(
			String id,
			String userId,
			HttpServletRequest request) throws Exception{
		
		Long dstUserId = userUtils.getUserIdFromSession(request);
		UserBO userBO = userUtils.queryUserById(dstUserId);
		
		CommandGroupUserPlayerPO player = commandBasicServiceImpl.vodMemberStart(userBO, Long.parseLong(id), Long.parseLong(userId));
		
		return new CommandGroupUserPlayerSettingVO().set(player);
	}
	
	/**
	 * 主席关闭点播成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 下午1:50:42
	 * @param businessId 会议id-成员userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/vod/member/stop")
	public Object vodMemberStop(
			String businessId,
			HttpServletRequest request) throws Exception{
		
		Long dstUserId = userUtils.getUserIdFromSession(request);
		String groupId = businessId.split("-")[0];
		String srcUserId = businessId.split("-")[1];
		
		CommandGroupUserPlayerPO player = commandBasicServiceImpl.vodMemberStop(dstUserId, Long.parseLong(groupId), Long.parseLong(srcUserId));
		
		if(player != null){
			return JSON.toJSONString(new HashMapWrapper<String, Object>().put("serial", player.getLocationIndex()).getMap());
		}else{
			return null;
		}
	}	
	
	/**
	 * 主席换一批观看成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月10日 下午6:34:36
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/roll/all/vod/members")
	public Object rollAllVodMembers(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<CommandGroupUserPlayerPO> players = commandPageServiceImpl.rollAllVodMembers(userId, Long.parseLong(id));
		List<CommandGroupUserPlayerSettingVO> playerVOs = new ArrayList<CommandGroupUserPlayerSettingVO>();
		for(CommandGroupUserPlayerPO player : players){
			CommandGroupUserPlayerSettingVO playerVO = new CommandGroupUserPlayerSettingVO().set(player);
			playerVOs.add(playerVO);
		}
		
		return JSON.toJSONString(new HashMapWrapper<String, Object>()
				.put("splits", playerVOs)
				.getMap());
	}
	
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
		List<Long> userIds = JSONArray.parseArray(dst, Long.class);
		
		List<ForwardReturnBO> result = commandForwardServiceImpl.forward(Long.parseLong(id), srcUserIds, null, userIds);
		
		return result;
	}

	/**
	 * 会议转发设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午1:59:58
	 * @param id 会议id
	 * @param src 源bundleId数组
	 * @param dst 目的用户id数组
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
		List<Long> userIds = JSONArray.parseArray(dst, Long.class);
		
		List<ForwardReturnBO> result = commandForwardServiceImpl.forward(Long.parseLong(id), null, bundleIds, userIds);
		
		return result;
	}
	
	/**
	 * 成员同意转发设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:00:14
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/device/agree")
	public Object forwardDeviceAgree(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		List<Object> result = commandForwardServiceImpl.forwardAgree(userId, Long.parseLong(id), demandIds);
		
		return result;
	}
	
	/**
	 * 成员拒绝转发设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午11:37:43
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/device/refuse")
	public Object forwardDeviceRefuse(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		commandForwardServiceImpl.forwardRefuse(Long.parseLong(id), demandIds);
		
		return null;
	}
	
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
			String id,
			String src,
			String dst,
			HttpServletRequest request) throws Exception{

		List<String> resourceIds = JSONArray.parseArray(src, String.class);
		List<Long> userIds = JSONArray.parseArray(dst, Long.class);
		
		List<ForwardReturnBO> result = commandForwardServiceImpl.forwardFile(Long.parseLong(id), resourceIds, userIds);
		
		return result;
	}
	
	/**
	 * 成员同意转发文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:00:43
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/file/agree")
	public Object forwardFileAgree(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		List<Object> result = commandForwardServiceImpl.forwardAgree(userId, Long.parseLong(id), demandIds);
		
		return result;
	}
	
	/**
	 * 成员拒绝转发文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午11:38:13
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/file/refuse")
	public Object forwardFileRefuse(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		commandForwardServiceImpl.forwardRefuse(Long.parseLong(id), demandIds);
		
		return null;
	}
	
	/**
	 * 主席停止一个会议中的多个转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:01:05
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/stop/by/chairman")
	public Object forwardStopByChairman(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		commandForwardServiceImpl.stopByChairman(Long.parseLong(id), demandIds);
		
		return null;
	}
	
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
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		Long demandId = Long.parseLong(businessId.split("-")[1]);
		List<Long> demandIds = new ArrayListWrapper<Long>().add(demandId).getList();
		
		JSONArray splits = commandForwardServiceImpl.stopByMember(userId, demandIds);
		
		return splits;
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
		
		JSONObject chairSplit = commandRemindServiceImpl.remind(Long.parseLong(id));
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
		
		JSONObject chairSplit = commandRemindServiceImpl.remindStop(Long.parseLong(id));
		return chairSplit;
	}
	
	/**
	 * 停止用户本地视频发送<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月20日 下午4:05:08
	 * @param userIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop/user/video/send")
	public Object stopUsersVideoSend(
			String userIds,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		UserBO user = userUtils.queryUserById(userId);
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		commandSilenceLocalServiceImpl.operate(user, userIdArray, 1, 2);
		
		return null;
	}
	
	/**
	 * 停止用户本地音频发送<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月20日 下午4:05:40
	 * @param userIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop/user/audio/send")
	public Object stopUsersAudioSend(
			String userIds,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		UserBO user = userUtils.queryUserById(userId);
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		commandSilenceLocalServiceImpl.operate(user, userIdArray, 2, 2);
		
		return null;
	}
	
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
		
		commandOsdServiceImpl.clearOsd(serial);
		return null;
	}
	
}
