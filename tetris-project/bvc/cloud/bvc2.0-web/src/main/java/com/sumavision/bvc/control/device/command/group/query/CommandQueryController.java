package com.sumavision.bvc.control.device.command.group.query;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.suma.venus.resource.dao.EncoderDecoderUserMapDAO;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.SerNodePO.ConnectionStatus;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupDecoderScreenDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupForwardDemandDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.MemberStatus;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderSchemePO;
import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderScreenPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.config.ServerProps;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeIcon;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeType;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.basic.forward.ForwardReturnBO;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.time.CommandFightTimeServiceImpl;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.CommonQueryUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberStatus;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallPO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallRolePermissionDAO;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.system.role.SystemRoleQuery;
import com.sumavision.tetris.system.role.SystemRoleVO;

@Controller
@RequestMapping(value = "/command/query")
public class CommandQueryController {

	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ServerProps serverProps;

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private CommonQueryUtil commonQueryUtil;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ExtraInfoService extraInfoService;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private GroupDAO groupDAO;
	
	@Autowired
	private RoleDAO roleDAO;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDAO;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private CommandGroupMemberDAO commandGroupMemberDao;
	
	@Autowired
	private CommandGroupForwardDemandDAO commandGroupForwardDemandDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandGroupRecordDAO commandGroupRecordDao;
	
	@Autowired
	private EncoderDecoderUserMapDAO encoderDecoderUserMapDao;
	
	@Autowired
	private CommandGroupDecoderScreenDAO commandGroupDecoderScreenDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private CommandFightTimeServiceImpl commandFightTimeServiceImpl;
	
	@Autowired
	private ConferenceHallRolePermissionDAO conferenceHallRolePermissionDao;
	
	@Autowired
	private ConferenceHallDAO conferenceHallDao;
	
	@Autowired
	private SystemRoleQuery systemRoleQuery;
	
	@Autowired
	private SerNodeDao serNodeDao;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	/**
	 * 查询组织机构及用户<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月25日
	 * @param @PathVariable boolean filterMode 过滤器模式 0全部，1在线，2离线
	 * @return total int 总数据量
	 * @return rows List<ExternalUserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/user/filter/{filterMode}")
	public Object findInstitutionTreeUser(@PathVariable int filterMode, HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询有权限的用户
		List<UserBO> users = resourceService.queryUserresByUserId(userId, TerminalType.QT_ZK);
		Collections.sort(users, new UserBO.UserComparatorFromFolderIndex());
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user : users){
			userIds.add(user.getId());
		}
		if(users!=null && users.size()>0){
			for(UserBO user:users){
				if(user.getId().equals(userId)) continue;
				String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
				if("ldap".equals(user.getCreater()) ||
				   (!"ldap".equals(user.getCreater()) && encoderId!=null)){// && user.getDecoderId()!=null)){
//					(!"ldap".equals(user.getCreater()) && userMap!=null && userMap.getEncodeBundleId()!=null) && !userMap.getEncodeBundleId().equals("")){//过滤掉编码器uuid为null和空字符串，其它情况无法过滤
					if(filterMode == 0
							|| filterMode == 1 && user.isLogined()
							|| filterMode == 2 && !user.isLogined()){
						filteredUsers.add(user);
					}
				}
			}
		}
		
		//查询有权限的会场
		List<SystemRoleVO> roles = systemRoleQuery.queryUserRoles(userId.toString());
		Set<Long> roleIds = new HashSet<Long>();
		if(roles!=null && roles.size()>0){
			for(SystemRoleVO role:roles){
				roleIds.add(Long.valueOf(role.getId()));
			}
		}
		List<ConferenceHallPO> conferenceHalls = null;
		if(roleIds.size() > 0){
			List<Object> result = conferenceHallRolePermissionDao.findDistinctConferenceHallIdByRoleIdIn(roleIds);
			List<Long> conferenceHallIds = new ArrayList<Long>();
			for(Object id:result){
				conferenceHallIds.add(Long.valueOf(id.toString()));
			}
			conferenceHalls = conferenceHallDao.findAll(conferenceHallIds);
		}
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, null, null, filteredUsers, conferenceHalls);
		}
		
		return _roots;
	}
	
	/**
	 * 查询会议中所有被授权协同会议的人<br/>
	 * <p>包括接听和未接听的</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午6:17:05
	 * @param id business_group对应的id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/user/command/cooperation")
	public Object findInstitutionTreeUserCommandCooperation(String id, HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
				
//		List<Long> memberUserIds = commandGroupMemberDao.findUserIdsByGroupId(Long.parseLong(id));
//		CommandGroupPO group = commandGroupDao.findOne(Long.parseLong(id));
		//List<CommandGroupMemberPO> members = group.getMembers();
//		GroupPO group = groupDao.findOne(Long.valueOf(id));
		List<GroupMemberPO> members =groupMemberDao.findByGroupId(Long.parseLong(id));
		
		//查询有权限的用户
		List<UserBO> users = resourceService.queryUserresByUserId(userId, TerminalType.QT_ZK);//TODO 应该选出组中所有用户
//		List<Long> userIds = new ArrayList<Long>();
//		for(UserBO user : users){
//			userIds.add(user.getId());
//		}
		
		if(users!=null && users.size()>0){
			for(UserBO user:users){
				if(user.getId().equals(userId)) continue;
				String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
				if("ldap".equals(user.getCreater()) ||
				   (!"ldap".equals(user.getCreater()) && encoderId!=null)){// && user.getDecoderId()!=null)){
					GroupMemberPO member = tetrisBvcQueryUtil.queryMemberByUserId(members, user.getId());
					if(member!=null 
							&& groupMemberRolePermissionDAO.findByRoleIdAndGroupMemberId((roleDAO.findByInternalRoleType(InternalRoleType.COMMAND_COOPERATION)).getId(), member.getId())!=null
							&& (member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECT) || member.getGroupMemberStatus().equals(GroupMemberStatus.CONNECTING))){
						filteredUsers.add(user);
					}
				}
			}
		}
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, null, null, filteredUsers, null);
		}
		
		return _roots;
	}

	/**
	 * 查询会议中所有被授权协同会议的人<br/>
	 * <p>包括接听和未接听的</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午6:17:05
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/command/cooperation/list")
	public Object findInstitutionTreeUserCommandCooperationList(String id, HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
				
//		List<Long> memberUserIds = commandGroupMemberDao.findUserIdsByGroupId(Long.parseLong(id));
		CommandGroupPO group = commandGroupDao.findOne(Long.parseLong(id));
		List<CommandGroupMemberPO> members = group.getMembers();
		
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
		
		TreeNodeVO commandRoot = new TreeNodeVO().setId(String.valueOf(TreeNodeVO.FOLDERID_COMMAND))
			     .setName("发言成员列表")
			     .setType(TreeNodeType.FOLDER)
			     .setKey()
			     .setIcon(TreeNodeIcon.FOLDER.getName())
			     .setChildren(new ArrayList<TreeNodeVO>());
		
		//查询用户
//		List<UserBO> users = resourceService.queryUserresByUserId(userId);
		List<Long> userIdList = new ArrayList<Long>();
		for(CommandGroupMemberPO member : members){
			userIdList.add(member.getUserId());
		}
		String userIdListStr = StringUtils.join(userIdList.toArray(), ",");
		List<UserBO> users = resourceService.queryUserListByIds(userIdListStr, TerminalType.QT_ZK);
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user : users){
			userIds.add(user.getId());
		}
		
		//先放入主席
		CommandGroupMemberPO chairmanMember = commandCommonUtil.queryChairmanMember(members);
		UserBO chairmanUserBO = queryUtil.queryUserById(users, chairmanMember.getUserId());
		chairmanUserBO.setName(chairmanUserBO.getName() + " [主席]");
		TreeNodeVO chairmanUserNode = new TreeNodeVO().set(chairmanUserBO, null);//查询列表不需要给出decoderId
		commandRoot.getChildren().add(chairmanUserNode);
				
		if(users!=null && users.size()>0){
			for(UserBO user:users){
				if(user.getId().equals(chairmanUserBO.getId())) continue;
				String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
				if("ldap".equals(user.getCreater()) ||
						   (!"ldap".equals(user.getCreater()) && encoderId!=null)){// && user.getDecoderId()!=null)){
					CommandGroupMemberPO member = commandCommonUtil.queryMemberByUserId(members, user.getId());
					if(member!=null
							&& (member.getCooperateStatus().equals(MemberStatus.CONNECT) || member.getCooperateStatus().equals(MemberStatus.CONNECTING))){
						filteredUsers.add(user);
						TreeNodeVO userNode = new TreeNodeVO().set(user, null);//查询列表不需要给出decoderId
						commandRoot.getChildren().add(userNode);
					}
				}
			}
		}		
		_roots.add(commandRoot);
		info.put("members", _roots);
		
		return info;
	}

	/**
	 * 查询当前会议外的所有用户<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月14日
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<ExternalUserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/user/except/command")
	public Object findInstitutionTreeUserExceptCommand(String id, HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
				
		List<GroupMemberPO> groupMembers = groupMemberDao.findByGroupId(Long.parseLong(id));
		List<Long> existUserIds = new ArrayList<Long>();
		List<Long> existHallIds = new ArrayList<Long>();
		for(GroupMemberPO groupMember:groupMembers){
			if(GroupMemberType.MEMBER_USER.equals(groupMember.getGroupMemberType())){
				existUserIds.add(Long.valueOf(groupMember.getOriginId()));
			}else if(GroupMemberType.MEMBER_HALL.equals(groupMember.getGroupMemberType())){
				existHallIds.add(Long.valueOf(groupMember.getOriginId()));
			}
		}
		
		//查询有权限的用户
		List<UserBO> users = resourceService.queryUserresByUserId(userId, TerminalType.QT_ZK);
		Collections.sort(users, new UserBO.UserComparatorFromFolderIndex());
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user : users){
			userIds.add(user.getId());
		}
//		List<EncoderDecoderUserMap> userMaps = encoderDecoderUserMapDao.findByUserIdIn(userIds);
		if(users!=null && users.size()>0){
			for(UserBO user:users){
				if(user.getId().equals(userId)) continue;
				String encoderId = commonQueryUtil.queryExternalOrLocalEncoderIdFromUserBO(user);
				if("ldap".equals(user.getCreater()) ||
				   (!"ldap".equals(user.getCreater()) && encoderId!=null) && !encoderId.equals("")){// && user.getDecoderId()!=null)){
					if(!existUserIds.contains(user.getId())){
						filteredUsers.add(user);
					}
				}
			}
		}
		
		List<SystemRoleVO> roles = systemRoleQuery.queryUserRoles(userId.toString());
		Set<Long> roleIds = new HashSet<Long>();
		if(roles!=null && roles.size()>0){
			for(SystemRoleVO role:roles){
				roleIds.add(Long.valueOf(role.getId()));
			}
		}
		List<ConferenceHallPO> conferenceHalls = null;
		if(roleIds.size() > 0){
			List<Object> result = conferenceHallRolePermissionDao.findDistinctConferenceHallIdByRoleIdIn(roleIds);
			List<Long> conferenceHallIds = new ArrayList<Long>();
			for(Object rId:result){
				Long lId = Long.valueOf(rId.toString());
				if(!existHallIds.contains(lId)) conferenceHallIds.add(lId);
			}
			conferenceHalls = conferenceHallDao.findAll(conferenceHallIds);
		}
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, null, null, filteredUsers, conferenceHalls);
		}
		
		return _roots;
	}

	/**
	 * 查询组织机构下所有的设备<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月25日
	 * @param @PathVariable int type 通道类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
//	 * @param @PathVariable int type 节点类型类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
	 * @param @PathVariable boolean withChannel 是否要查询通道
	 * @param @PathVariable boolean filterMode 过滤器模式 0全部，1在线，2离线
	 * @param privilegesStr 数组类型的权限{@Code BUSINESS_OPR_TYPE的value}
	 * @param satisfyAll true需要满足privilegesStr全部权限，false只需满足一个即可。为null的时候按默认查询
	 * @param returnBundleList true返回设备集合，false返回设备树
	 * @return List<TreeNodeVO> 设备通道树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/bundle/{type}/{withChannel}/{filterMode}")
	public Object findInstitutionTreeBundle(
			@PathVariable int type,
			@PathVariable boolean withChannel,
			@PathVariable int filterMode,
			String privilegesStr,
			Boolean satisfyAll,
			Boolean returnBundleList,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<String> privileges = JSONArray.parseArray(privilegesStr,String.class);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//查询有权限的设备
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId,privileges,satisfyAll);
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundleBody:queryBundles){
			//过滤在线离线
			if(filterMode == 0
					|| filterMode == 1 && bundleBody.getOnlineStatus().equals(ONLINE_STATUS.ONLINE)
					|| filterMode == 2 && bundleBody.getOnlineStatus().equals(ONLINE_STATUS.OFFLINE)){
				
				if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
					BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
							.setName(bundleBody.getBundleName())
							.setFolderId(bundleBody.getFolderId())
							.setBundleId(bundleBody.getBundleId())
							.setModel(bundleBody.getDeviceModel())
							.setNodeUid(bundleBody.getAccessNodeUid())
							.setOnlineStatus(bundleBody.getOnlineStatus().toString())
							.setLockStatus(bundleBody.getLockStatus())
							.setType(bundleBody.getBundleType())
							.setEquipFactInfo(bundleBody.getEquipFactInfo())
							.setRealType(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())?BundleBO.BundleRealType.XT.toString():BundleBO.BundleRealType.BVC.toString());
	
					bundles.add(bundle);
					
					bundleIds.add(bundleBody.getBundleId());
				}
			}
		}
		
		//根据bundleIds从资源层查询channels
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(bundleIds, type);
		if(queryChannels != null){
			for(ChannelSchemeDTO channel:queryChannels){
				ChannelBO channelBO = new ChannelBO().setChannelId(channel.getChannelId())
													 //起别名
												     .setName(ChannelType.transChannelName(channel.getChannelId()))
													 .setBundleId(channel.getBundleId())
													 .setChannelName(channel.getChannelName())
													 .setChannelType(channel.getBaseType());
	
				channels.add(channelBO);
			}
		}
		
		//过滤无通道设备
		Set<String> filteredBundleIds = new HashSet<String>();
		for(ChannelBO channel:channels){
			filteredBundleIds.add(channel.getBundleId());
		}
		List<BundleBO> filteredBundles = new ArrayList<BundleBO>();
		for(BundleBO bundle:bundles){
			if(filteredBundleIds.contains(bundle.getBundleId())){
				filteredBundles.add(bundle);
			}
		}
		
		//没有文件的文件夹不显示
		/*1.通过设备集合拿到文件夹id
		 * 2.通过文件夹id找文件夹路径，分离出父文件夹id
		 * 3找到所有不为空文件夹给folders
		 * */
		Set<Long> folderIds=new HashSet<Long>();//所有文件夹id
		Map<Long,FolderBO> folderMap=folders.stream().collect(Collectors.toMap(FolderBO::getId, Function.identity()));
		filteredBundles.stream().map(BundleBO::getFolderId).collect(Collectors.toSet()).stream().map(folderId->{
			Optional<FolderBO> folderBo=Optional.ofNullable(folderMap).map(folderMAP->{return folderMAP.get(folderId);});
			if(folderBo.isPresent()){//空值校验
				folderIds.add(folderId);
				String parentPath=folderBo.get().getParentPath();
				if(parentPath!=null && !"".equals(parentPath)){
					folderIds.addAll(Arrays.asList(parentPath.replaceFirst("/", "").split("/")).stream().map(Long::valueOf).collect(Collectors.toList()));
				} 
			}
			return  folderId;
		}).collect(Collectors.toSet());
		
		folders =folderIds.stream().map(folderId->{
			return folderMap.get(folderId);
		}).collect(Collectors.toList());
		
		Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
		
		 Collections.sort(folders, (item1, item2) -> {
            return comparator.compare(item1.getName(), item2.getName());
        });
		
//		Collections.sort(folders, Comparator.comparing(FolderBO::getId));
//		Collections.sort(folders, Comparator.comparing(FolderBO::getFolderIndex));
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		
		//处理：返回设备集合  
		if(Boolean.TRUE.equals(returnBundleList)){
			
			JSONObject info =new JSONObject();
			List<ExtraInfoPO> allExtraInfos = extraInfoService.findByBundleIdIn(bundleIds);
			
			for(FolderBO root:roots){
				TreeNodeVO _root = new TreeNodeVO().set(root).setBundleList(new ArrayList<TreeNodeVO>());
				filteredBundles.stream().forEach(bundle->{
					List<ExtraInfoPO> extraInfos = extraInfoService.queryExtraInfoBundleId(allExtraInfos, bundle.getBundleId());
					TreeNodeVO bundleNode = new TreeNodeVO().set(bundle, extraInfos);
					_root.getBundleList().add(bundleNode);
					
				});
				
				if(_root.getBundleList().size()>0){
					_roots.add(_root);
				}
				
			}
			
			return _roots;
		}
		
		//组件文件夹
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			if(withChannel){
				recursionFolder(_root, folders, filteredBundles, channels, null, null);
			}else{
				recursionFolder(_root, folders, filteredBundles, null, null, null);
			}
		}
		
		//将同一个文件夹下的设备按照名称排序
		orderBundleByName(_roots);
		
		//筛选 //
		List<SerNodePO> serNodeEntities = serNodeDao.findAll();
		List<String> existSerNodeName = new ArrayList<String>();
		if(serNodeEntities!=null && serNodeEntities.size()>0){
			Iterator<TreeNodeVO> i = _roots.iterator();
			while(i.hasNext()){
				TreeNodeVO _root = i.next();
				TreeNodeVO nodeVO = findFirstDeviceNode(_root);
				if(nodeVO == null) continue;
				String serName = JSON.parseObject(nodeVO.getParam()).getString("equipFactInfo"); 
				SerNodePO targetSerNode = null;
				for(SerNodePO serNode:serNodeEntities){
					if(serName == null && SOURCE_TYPE.SYSTEM.equals(serNode.getSourceType())){
						targetSerNode = serNode;
						break;
					}else if(serNode.getNodeName().equals(serName)){
						targetSerNode = serNode;
						break;
					}
				}
				if(SOURCE_TYPE.SYSTEM.equals(targetSerNode.getSourceType())){
					serName = targetSerNode.getNodeName();
//							_root.getName();
				}
//				if(SOURCE_TYPE.SYSTEM.equals(targetSerNode.getSourceType()) || 
				if(!ConnectionStatus.OFF.equals(targetSerNode.getStatus())){
					if(SOURCE_TYPE.SYSTEM.equals(targetSerNode.getSourceType())){
						_root.setName(new StringBufferWrapper().append(serName)
													   .toString());
						existSerNodeName.add(targetSerNode.getNodeName());
					}else{
						_root.setName(new StringBufferWrapper().append(serName)
								   .append("(")
								   .append("在线")
								   .append(")")
								   .toString());
						existSerNodeName.add(targetSerNode.getNodeName());
					}
				}else{
					i.remove();
				}
			}
			for(SerNodePO serNodeEntity:serNodeEntities){
				boolean finded = false;
				for(String name:existSerNodeName){
					if(name.equals(serNodeEntity.getNodeName())){
						finded = true;
						break;
					}
				}
				if(finded) continue;
				String name = null;
				if(!ConnectionStatus.OFF.equals(serNodeEntity.getStatus())){
					if(SOURCE_TYPE.SYSTEM.equals(serNodeEntity.getSourceType())){
						name = serNodeEntity.getNodeName();
					}else{
						name = serNodeEntity.getNodeName()+"(在线)";
					}
					
				}else{
					name = new StringBufferWrapper().append(serNodeEntity.getNodeName()).append("(").append("离线").append(")").toString();
				}
				_roots.add(new TreeNodeVO().setId(new StringBufferWrapper().append("offline-sernode-").append(serNodeEntity.getId()).toString())
										   .setName(name)
										   .setType(TreeNodeType.FOLDER)
										   .setIcon(TreeNodeIcon.FOLDER.getName())
										   .setKey(serNodeEntity.getId().toString()));
			}
		}
		return _roots;
		//tttest
	}
	
	/**
	 * 查询组织机构下所有的设备<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月25日
	 * @param @PathVariable int type 通道类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
	 * @param @PathVariable int type 节点类型类型，0编码，1解码，2视频编码，3音频编码，4视频解码，5音频解码
	 * @param @PathVariable boolean withChannel 是否要查询通道
	 * @param @PathVariable boolean filterMode 过滤器模式 0全部，1在线，2离线
	 * @param privilegesStr 数组类型的权限{@Code BUSINESS_OPR_TYPE的value}
	 * @param satisfyAll true需要满足privilegesStr全部权限，false只需满足一个即可。为null的时候按默认查询
	 * @param returnBundleList true返回设备集合，false返回设备树
	 * @return TreeNodeVO 设备通道树节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/inner/bundle/{type}/{withChannel}/{filterMode}")
	public Object findInstitutionTreeInnerBundle(
			@PathVariable int type,
			@PathVariable boolean withChannel,
			@PathVariable int filterMode,
			String privilegesStr,
			Boolean satisfyAll,
			Boolean returnBundleList,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<String> privileges = JSONArray.parseArray(privilegesStr,String.class);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//本域域名
		List<SerNodePO> serNodeEntities = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);
		SerNodePO localSerNode = serNodeEntities.get(0);
		
		//查询有权限的设备
		List<BundlePO> queryAllBundles = resourceQueryUtil.queryUseableBundles(userId,privileges,satisfyAll);
		if(queryAllBundles == null || queryAllBundles.size() <= 0){
			
			List<FolderBO> roots = findRoots(folders);
			for(FolderBO root: roots){
				TreeNodeVO _root = new TreeNodeVO().set(root)
						   .setChildren(new ArrayList<TreeNodeVO>());
				_root.setName(localSerNode.getNodeName()+"(本域)");
				_roots.add(_root);
			}
			return _roots;
		} 
		List<BundlePO> queryBundles = queryAllBundles.stream().filter(bundle -> {
			return SOURCE_TYPE.SYSTEM.equals(bundle.getSourceType());
		}).collect(Collectors.toList());
		System.out.println("本域的设备有：" + queryBundles);
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundleBody:queryBundles){
			//过滤在线离线
			if(filterMode == 0
					|| filterMode == 1 && bundleBody.getOnlineStatus().equals(ONLINE_STATUS.ONLINE)
					|| filterMode == 2 && bundleBody.getOnlineStatus().equals(ONLINE_STATUS.OFFLINE)){
				
				if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
					BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
							.setName(bundleBody.getBundleName())
							.setFolderId(bundleBody.getFolderId())
							.setBundleId(bundleBody.getBundleId())
							.setModel(bundleBody.getDeviceModel())
							.setNodeUid(bundleBody.getAccessNodeUid())
							.setOnlineStatus(bundleBody.getOnlineStatus().toString())
							.setLockStatus(bundleBody.getLockStatus())
							.setType(bundleBody.getBundleType())
							.setEquipFactInfo(bundleBody.getEquipFactInfo())
							.setRealType(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())?BundleBO.BundleRealType.XT.toString():BundleBO.BundleRealType.BVC.toString());
	
					bundles.add(bundle);
					
					bundleIds.add(bundleBody.getBundleId());
				}
			}
		}
		
		//根据bundleIds从资源层查询channels
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(bundleIds, type);
		if(queryChannels != null){
			for(ChannelSchemeDTO channel:queryChannels){
				ChannelBO channelBO = new ChannelBO().setChannelId(channel.getChannelId())
													 //起别名
												     .setName(ChannelType.transChannelName(channel.getChannelId()))
													 .setBundleId(channel.getBundleId())
													 .setChannelName(channel.getChannelName())
													 .setChannelType(channel.getBaseType());
	
				channels.add(channelBO);
			}
		}
		
		//过滤无通道设备
		Set<String> filteredBundleIds = new HashSet<String>();
		for(ChannelBO channel:channels){
			filteredBundleIds.add(channel.getBundleId());
		}
		List<BundleBO> filteredBundles = new ArrayList<BundleBO>();
		for(BundleBO bundle:bundles){
			if(filteredBundleIds.contains(bundle.getBundleId())){
				filteredBundles.add(bundle);
			}
		}
		
		//没有文件的文件夹不显示
		/*1.通过设备集合拿到文件夹id
		 * 2.通过文件夹id找文件夹路径，分离出父文件夹id
		 * 3找到所有不为空文件夹给folders
		 * */
		Set<Long> folderIds=new HashSet<Long>();//所有文件夹id
		Map<Long,FolderBO> folderMap=folders.stream().collect(Collectors.toMap(FolderBO::getId, Function.identity()));
		filteredBundles.stream().map(BundleBO::getFolderId).collect(Collectors.toSet()).stream().map(folderId->{
			Optional<FolderBO> folderBo=Optional.ofNullable(folderMap).map(folderMAP->{return folderMAP.get(folderId);});
			if(folderBo.isPresent()){//空值校验
				folderIds.add(folderId);
				String parentPath=folderBo.get().getParentPath();
				if(parentPath!=null && !"".equals(parentPath)){
					folderIds.addAll(Arrays.asList(parentPath.replaceFirst("/", "").split("/")).stream().map(Long::valueOf).collect(Collectors.toList()));
				} 
			}
			return  folderId;
		}).collect(Collectors.toSet());
		
		folders =folderIds.stream().map(folderId->{
			return folderMap.get(folderId);
		}).collect(Collectors.toList());
		
		Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
		
		 Collections.sort(folders, (item1, item2) -> {
           return comparator.compare(item1.getName(), item2.getName());
        });
		
//		Collections.sort(folders, Comparator.comparing(FolderBO::getId));
//		Collections.sort(folders, Comparator.comparing(FolderBO::getFolderIndex));
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		
		//处理：返回设备集合  
		if(Boolean.TRUE.equals(returnBundleList)){
			
			JSONObject info =new JSONObject();
			List<ExtraInfoPO> allExtraInfos = extraInfoService.findByBundleIdIn(bundleIds);
			
			for(FolderBO root:roots){
				TreeNodeVO _root = new TreeNodeVO().set(root).setBundleList(new ArrayList<TreeNodeVO>());
				filteredBundles.stream().forEach(bundle->{
					List<ExtraInfoPO> extraInfos = extraInfoService.queryExtraInfoBundleId(allExtraInfos, bundle.getBundleId());
					TreeNodeVO bundleNode = new TreeNodeVO().set(bundle, extraInfos);
					_root.getBundleList().add(bundleNode);
					
				});
				
				if(_root.getBundleList().size()>0){
					_roots.add(_root);
				}
				
			}
			
			return _roots;
		}
		
		//组件文件夹
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_root.setName(localSerNode.getNodeName()+"(本域)");
			_roots.add(_root);
			if(withChannel){
				recursionFolder(_root, folders, filteredBundles, channels, null, null);
			}else{
				recursionFolder(_root, folders, filteredBundles, null, null, null);
			}
		}
		
		//将同一个文件夹下的设备按照名称排序
		orderBundleByName(_roots);
		return _roots;
	}
	
	/**
	 * 查询组织机构下所有的设备<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月25日
	 * @param returnBundleList true返回设备集合，false返回设备树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/foreign")
	public Object findInstitutionTreeForeign(
			HttpServletRequest request) throws Exception{
		List<SerNodePO> serNodeEntities = serNodeDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
		JSONArray JSONarray = new JSONArray();
		
		for(SerNodePO serNode : serNodeEntities){
			if(ConnectionStatus.ON.equals(serNode.getStatus())){
				JSONObject json = new JSONObject();
				json.put("name", serNode.getNodeName());
				json.put("status", "online");
				JSONarray.add(json);
			}else if(ConnectionStatus.OFF.equals(serNode.getStatus())){
				JSONObject json = new JSONObject();
				json.put("name", serNode.getNodeName());
				json.put("status", "online");
				JSONarray.add(json);
			}
		}
		
		System.out.println("外域的设备： "+JSONarray);
		return JSONarray;
	}
	
	/**
	 * 获取外域、目录、设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月2日 下午6:29:41
	 * @param folderPath 目录路径
	 * @param serNodeNamePath 域路径
	 * @param childType 请求类型
	 * @param permissionType 权限字段
	 * @param roleId 绑定的角色
	 * @param uuid 目录唯一标识符
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/foreign/bundle")
	public Object findInstitutionTreeForeignBundle(
			String folderPath,
			String serNodeNamePath,
			String childType,
			String permissionType,
			Long roleId,
			String uuid,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		Long userId = userUtils.getUserIdFromSession(request);
		
		if(uuid == null){
			uuid = UUID.randomUUID().toString().replace("-", "");
		}
		
		Map<String,String> passByContent = new HashMap<String, String>();
		passByContent.put("folderPath", folderPath);
		passByContent.put("serNodeNamePath", serNodeNamePath);
		passByContent.put("childType", childType.toString());
		passByContent.put("uuid", uuid);
		passByContent.put("userId", userId.toString());
		passByContent.put("cmd", "search_foreign");
		passByContent.put("permissionType", permissionType==null?"":permissionType);
		passByContent.put("roleId", roleId==null?"":roleId.toString());
		
		String localLayerId = resourceRemoteService.queryLocalLayerId();
		PassByBO passBy = new PassByBO();
		passBy.setPass_by_content(passByContent);
		passBy.setLayer_id(localLayerId);
		
		LogicBO logic = new LogicBO();
		logic.setPass_by(new ArrayListWrapper<PassByBO>().add(passBy).getList());
		executeBusiness.execute(logic, "请求外域信息");
		return null;
	}
	
	private TreeNodeVO findFirstDeviceNode(TreeNodeVO node) throws Exception{
		if(node.getType().equals(TreeNodeType.BUNDLE)) return node;
		if(node.getChildren()!=null && node.getChildren().size()>0){
			for (TreeNodeVO nodeVo:node.getChildren()) {
				TreeNodeVO dNodeVO = findFirstDeviceNode(nodeVo);
				if(dNodeVO != null) return dNodeVO;
			}
		}
		return null;
	}
	
	/**
	 * 查找可用于关联某一播放器的解码器<br/>
	 * <p>查询结果排除了该用户已经关联到其它播放器的解码器</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午1:12:11
	 * @param serial
	 * @param withChannel
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/bundle/for/player")
	public Object findInstitutionTreeBundleForPlayer(
			int serial,
			boolean withChannel,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//查询有权限的设备
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundleBody:queryBundles){
			if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
				BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
						.setName(bundleBody.getBundleName())
						.setFolderId(bundleBody.getFolderId())
						.setBundleId(bundleBody.getBundleId())
						.setModel(bundleBody.getDeviceModel())
						.setNodeUid(bundleBody.getAccessNodeUid())
						.setOnlineStatus(bundleBody.getOnlineStatus().toString())
						.setLockStatus(bundleBody.getLockStatus())
						.setType(bundleBody.getBundleType())
						.setRealType(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())?BundleBO.BundleRealType.XT.toString():BundleBO.BundleRealType.BVC.toString());

				bundles.add(bundle);
				
				bundleIds.add(bundleBody.getBundleId());
			}		
		}
		
		//根据bundleIds从资源层查询channels
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(bundleIds, 1);
		if(queryChannels != null){
			for(ChannelSchemeDTO channel:queryChannels){
				ChannelBO channelBO = new ChannelBO().setChannelId(channel.getChannelId())
													 //起别名
												     .setName(ChannelType.transChannelName(channel.getChannelId()))
													 .setBundleId(channel.getBundleId())
													 .setChannelName(channel.getChannelName())
													 .setChannelType(channel.getBaseType());
	
				channels.add(channelBO);
			}
		}
		
		//过滤已经被绑定的设备
//		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
//		List<CommandGroupUserPlayerPO> players = userInfo.getPlayers();		
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(userId.toString(), terminal.getId(), GroupMemberType.MEMBER_USER);
		Set<String> castedBundleIds = new HashSet<String>();
		for(PageTaskPO task : pageInfo.getPageTasks()){
			if(task.getCastDevices() == null) continue;
			for(CommandGroupUserPlayerCastDevicePO device : task.getCastDevices()){
				castedBundleIds.add(device.getDstBundleId());
			}
		}
		
		//过滤无通道设备
		Set<String> filteredBundleIds = new HashSet<String>();
		for(ChannelBO channel:channels){
			filteredBundleIds.add(channel.getBundleId());
		}
		List<BundleBO> filteredBundles = new ArrayList<BundleBO>();
		for(BundleBO bundle:bundles){
			if(filteredBundleIds.contains(bundle.getBundleId())
					&& !castedBundleIds.contains(bundle.getBundleId())){
				filteredBundles.add(bundle);
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			if(withChannel){
				recursionFolder(_root, folders, filteredBundles, channels, null, null);
			}else{
				recursionFolder(_root, folders, filteredBundles, null, null, null);
			}
		}
		
		return _roots;
		
	}
	
	/**
	 * 查找可用于关联某一播放器的解码器<br/>
	 * <p>查询结果排除了该用户已经关联到其它播放器的解码器</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午1:12:11
	 * @param serial
	 * @param withChannel
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/institution/tree/bundle/for/screen")
	public Object findInstitutionTreeBundleForScreen(
			String screenId,
			boolean withChannel,
			HttpServletRequest request) throws Exception{
		
		CommandGroupDecoderScreenPO screen = commandGroupDecoderScreenDao.findOne(Long.parseLong(screenId));
		if(screen == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到分屏，id: " + screenId);
		}
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//查询有权限的设备
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundleBody:queryBundles){
			if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
				BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
						.setName(bundleBody.getBundleName())
						.setFolderId(bundleBody.getFolderId())
						.setBundleId(bundleBody.getBundleId())
						.setModel(bundleBody.getDeviceModel())
						.setNodeUid(bundleBody.getAccessNodeUid())
						.setOnlineStatus(bundleBody.getOnlineStatus().toString())
						.setLockStatus(bundleBody.getLockStatus())
						.setType(bundleBody.getBundleType())
						.setRealType(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())?BundleBO.BundleRealType.XT.toString():BundleBO.BundleRealType.BVC.toString());

				bundles.add(bundle);
				
				bundleIds.add(bundleBody.getBundleId());
			}		
		}
		
		//根据bundleIds从资源层查询channels
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(bundleIds, 1);
		if(queryChannels != null){
			for(ChannelSchemeDTO channel:queryChannels){
				ChannelBO channelBO = new ChannelBO().setChannelId(channel.getChannelId())
													 //起别名
												     .setName(ChannelType.transChannelName(channel.getChannelId()))
													 .setBundleId(channel.getBundleId())
													 .setChannelName(channel.getChannelName())
													 .setChannelType(channel.getBaseType());
	
				channels.add(channelBO);
			}
		}
		
		//过滤已经被该方案绑定的设备
		Set<String> castedBundleIds = new HashSet<String>();
		CommandGroupDecoderSchemePO scheme = screen.getScheme();
		List<CommandGroupDecoderScreenPO> screens = scheme.getScreens();
		for(CommandGroupDecoderScreenPO screen1 : screens){
			if(screen1.getCastDevices() != null){
				for(CommandGroupUserPlayerCastDevicePO device : screen1.getCastDevices()){
					castedBundleIds.add(device.getDstBundleId());
				}
			}
		}		
		
		//过滤无通道设备
		Set<String> filteredBundleIds = new HashSet<String>();
		for(ChannelBO channel:channels){
			filteredBundleIds.add(channel.getBundleId());
		}
		List<BundleBO> filteredBundles = new ArrayList<BundleBO>();
		for(BundleBO bundle:bundles){
			if(filteredBundleIds.contains(bundle.getBundleId())
					&& !castedBundleIds.contains(bundle.getBundleId())){
				filteredBundles.add(bundle);
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			if(withChannel){
				recursionFolder(_root, folders, filteredBundles, channels, null, null);
			}else{
				recursionFolder(_root, folders, filteredBundles, null, null, null);
			}
		}
		
		return _roots;
		
	}

	/**
	 * 查询用户所在的会议列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月15日 下午2:37:19
	 * @param type 查询类型，command或meeting，如果null则认为command
	 * @return TreeNodeVO 会议树节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			String type,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		if(type == null) type = "command";
//		GroupType needType = null;
		BusinessType needBusinessType = null;
		switch (type){
		case "meeting":
//			needType = GroupType.MEETING;
			needBusinessType = BusinessType.MEETING_QT;
			break;
		case "command":
		default:
//			needType = GroupType.BASIC;
			needBusinessType = BusinessType.COMMAND;
			break;
		}
		
		List<GroupPO> commands = groupDAO.findByMemberOriginId(userId.toString());
		List<GroupPO> enteredCommands = groupDAO.findEnteredGroupByMemberOriginId(userId.toString());
		
		//加入会议组节点
		TreeNodeVO commandRoot = new TreeNodeVO().setId(String.valueOf(TreeNodeVO.FOLDERID_COMMAND))
											     .setName(serverProps.getCommandString() + "组列表")
											     .setType(TreeNodeType.FOLDER)
											     .setKey()
											     .setIcon(TreeNodeIcon.FOLDER.getName())
											     .setChildren(new ArrayList<TreeNodeVO>());
		
		for(GroupPO command: commands){
			BusinessType groupType = command.getBusinessType();
			if(!needBusinessType.equals(groupType)){
				continue;
			}
			
			boolean entered = false;
			if(command.getUserId().equals(userId)){
				//自己的指挥
				if(GroupStatus.STOP.equals(command.getStatus())){
					entered = false;
				}else{
					entered = true;
				}
			}else{
				//别人的指挥
				GroupPO enteredCommand = tetrisBvcQueryUtil.queryGroupById(enteredCommands, command.getId());
				if(enteredCommand != null){
					entered = true;
				}
			}
			TreeNodeVO commandTree = new TreeNodeVO().set(command, entered);
			commandRoot.getChildren().add(commandTree);
		}
		
		return (new ArrayListWrapper<TreeNodeVO>().add(commandRoot).getList());
		
	}
	
	/**
	 * 查询用户已经进入的会议<br/>
	 * <p>用户CONNECT的会议，以及用户作为主席的所有会议</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月12日 上午10:21:58
	 * @param type 查询类型，command或group，如果null则认为command
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/entered")
	public Object findEnterdCommandGroup(
			String type,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		JSONArray groups = new JSONArray();
		
		if(type == null) type = "command";
		BusinessType needType = null;
		switch (type){
		case "meeting":
			needType = BusinessType.MEETING_QT;
			break;
		case "command":
		default:
			needType = BusinessType.COMMAND;
			break;
		}
		
		List <GroupPO> commands = groupDAO.findEnteredGroupByMemberOriginId(userId.toString());
		
		for(GroupPO command : commands){
			BusinessType groupType = command.getBusinessType();
			if(!needType.equals(groupType)){
				continue;
			}
			JSONObject group = new JSONObject();
			group.put("id", command.getId().toString());
			group.put("name", command.getName());
			group.put("creator", command.getUserId().toString());
			if(groupType.equals(BusinessType.COMMAND)){// || type.equals(GroupType.COOPERATE) || type.equals(GroupType.SECRET)){
				group.put("type", "command");
			}else{
				group.put("type", "meeting");
			}
			GroupStatus groupStatus = command.getStatus();
			group.put("status", groupStatus.getCode());
			//已经启动的会议，添加作战时间
			/*if(!groupStatus.equals(GroupStatus.STOP)){
				Date fightTime = commandFightTimeServiceImpl.calculateCurrentFightTime(command);
				if(fightTime != null){
					group.put("fightTime", DateUtil.format(fightTime, DateUtil.dateTimePattern));
				}
			}*/
			groups.add(group);
		}
		
		return groups;
	}
	
	/**
	 * 查询当前会议内的所有转发点播<br/>
	 * <p>包括设备、文件</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午1:57:43
	 * @param id 会议id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/command/forward")
	public Object findCommandForwardDemand(
			String id,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
//		CommandGroupPO group = commandGroupDao.findOne(Long.parseLong(id));
		Page<CommandGroupForwardDemandPO> demands = commandGroupForwardDemandDao.findByGroupId(Long.parseLong(id), page);
		if(demands == null){
			return null;
		}
		
		List<ForwardReturnBO> rows = new ArrayList<ForwardReturnBO>();
		for(CommandGroupForwardDemandPO demand : demands){
			if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_DEVICE)){
				ForwardReturnBO row = new ForwardReturnBO().setByDevice(demand);
				rows.add(row);
			}else if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_USER)){
				ForwardReturnBO row = new ForwardReturnBO().setByUser(demand);
				rows.add(row);
			}else if(demand.getDemandType().equals(ForwardDemandBusinessType.FORWARD_FILE)){
				ForwardReturnBO row = new ForwardReturnBO().setByFile(demand);
				rows.add(row);
			}
		}
		
		return JSON.toJSONString(new HashMapWrapper<String, Object>()
				.put("total", demands.getTotalElements())
				.put("rows", rows)
				.getMap());
	}
	
	/**递归组文件夹层级*/
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles, 
			List<ChannelBO> channels,
			List<UserBO> users,
			List<ConferenceHallPO> conferenceHalls){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles, channels, users, conferenceHalls);
			}
		}
		
		//往里装设备
		if(bundles!=null && bundles.size()>0){
			
			//查到所有的扩展字段
			List<String> bundleIds = new ArrayList<String>();
			for(BundleBO bundle : bundles){
				bundleIds.add(bundle.getBundleId());
			}			
			List<ExtraInfoPO> allExtraInfos = extraInfoService.findByBundleIdIn(bundleIds);
			
			for(BundleBO bundle:bundles){
				if(bundle.getFolderId()!=null && root.getId().equals(bundle.getFolderId().toString())){
					List<ExtraInfoPO> extraInfos = extraInfoService.queryExtraInfoBundleId(allExtraInfos, bundle.getBundleId());
					TreeNodeVO bundleNode = new TreeNodeVO().set(bundle, extraInfos)
															.setChildren(new ArrayList<TreeNodeVO>());
					root.getChildren().add(bundleNode);
					if(!BundleBO.BundleRealType.XT.toString().equals(bundle.getRealType()) && channels!=null && channels.size()>0){
						for(ChannelBO channel:channels){
							if(channel.getBundleId().equals(bundleNode.getId())){
								TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
								bundleNode.getChildren().add(channelNode);
							}
						}
					}
				}
			}
		}
		
		//往里装用户
		if(users!=null && users.size()>0){
			List<Long> userIds = new ArrayList<Long>();
			for(UserBO user : users){
				userIds.add(user.getId());
			}
			List<EncoderDecoderUserMap> userMaps = encoderDecoderUserMapDao.findByUserIdIn(userIds);
			for(UserBO user:users){
				if(user.getFolderId()!=null && root.getId().equals(user.getFolderId().toString())){
					EncoderDecoderUserMap userMap = commonQueryUtil.queryUserMapById(userMaps, user.getId());
					TreeNodeVO userNode = new TreeNodeVO().set(user, userMap);
					root.getChildren().add(userNode);
				}
			}
		}
		
		//往里装会场
		if(conferenceHalls!=null && conferenceHalls.size()>0){
			for(ConferenceHallPO conferenceHall:conferenceHalls){
				if(conferenceHall.getFolderId()!=null && conferenceHall.getFolderId().toString().equals(root.getId())){
					TreeNodeVO hallNode = new TreeNodeVO().set(conferenceHall);
					root.getChildren().add(hallNode);
				}
			}
		}
		
	}
	
	/**
	 * 查找根节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:32:53
	 * @param List<FolderBO> folders 查找范围
	 * @return List<FolderBO> 根节点列表
	 */
	private List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
	/**
	 * 获取folderId对应的目录及其下级目录<br/>
	 * <p>方法写好了，没有测过。需求暂时不需要了</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 上午11:19:57
	 * @param folders
	 * @param folderId
	 * @return
	 */
	@Deprecated
	private List<FolderBO> queryThisAndLowerFolders(List<FolderBO> folders, Long folderId){
		
		List<FolderBO> filteredFolders = new ArrayList<FolderBO>();
		FolderBO thisFolder = queryUtil.queryFolderById(folders, folderId);
		
		//先把这个目录加进去，下边不能再加
		filteredFolders.add(thisFolder);
		
		//除了这个目录之外的，下级目录的parentPath
		String parentPath = null;
		String thisParentPath = thisFolder.getParentPath();
		if(thisParentPath == null){
			parentPath = "/" + thisFolder.getId().toString();
		}else{
			parentPath = thisParentPath + "/" + thisFolder.getId().toString();
		}
		for(FolderBO folder : folders){
			if(folder.getParentPath()!=null && folder.getParentPath().startsWith(parentPath)){
				filteredFolders.add(folder);
			}
		}
		
		return filteredFolders;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/privilege/of/user")
	public Object queryPrivilegeOfUser(
			Long dstUserId,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		Map<BUSINESS_OPR_TYPE,Boolean> privileges =resourceService.hasPrivilegesOfUser(userId, dstUserId, null);
		
		if(privileges == null || privileges.size()==0){
			return null;
		}
		
		Map <String,Object> privilegesWapper=new HashMap<String, Object>();
		
		privilegesWapper.put("auth", privileges);
		
		return privilegesWapper;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/privilege/of/bundle")
	public Object queryPrivilegeOfBundle(
			Long bundleId,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		Map<BUSINESS_OPR_TYPE,Boolean> privileges =resourceService.hasPrivilegesOfUser(userId, bundleId, null);
		
		if(privileges == null || privileges.size()==0){
			return null;
		}
		
		Map <String,Object> privilegesWapper=new HashMap<String, Object>();
		
		privilegesWapper.put("auth", privileges);
		
		return privilegesWapper;
	}
 	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/all/privilege")
	public Object queryAllPrivilege(HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		Map<String, List<com.suma.venus.resource.service.ResourceService.Relation>> privileges = resourceService.hasPrivilegesOfAll(userId,null);
		
		return privileges;
	}
	
	/**
	 * 将同一个文件夹下的设备按照名称排序<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午3:51:31
	 * @param _roots 树
	 */
	private void orderBundleByName(List<TreeNodeVO> roots){
		for(TreeNodeVO node:roots){
			List<TreeNodeVO> childTreeNodeList= node.getChildren();
			if(childTreeNodeList!=null){
				List<TreeNodeVO> folderTreeNodeList =childTreeNodeList.stream().filter(treeNode->{
					if(TreeNodeType.FOLDER.equals(treeNode.getType())){
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				
				List<TreeNodeVO> bunldeTreeNodeList = childTreeNodeList.stream().filter(treeNode->{
					if(TreeNodeType.BUNDLE.equals(treeNode.getType())){
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				Collections.sort(bunldeTreeNodeList, Comparator.comparing(TreeNodeVO::getName));
				node.getChildren().removeAll(bunldeTreeNodeList);
				node.getChildren().addAll(bunldeTreeNodeList);
				if(folderTreeNodeList != null && folderTreeNodeList.size() > 0){
					orderBundleByName(folderTreeNodeList);
				}
			}
		}
	}
	
}
