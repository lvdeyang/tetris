package com.sumavision.tetris.bvc.cascade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Node;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.sumavision.bvc.command.group.dao.AgendaForwardDemandDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.enumeration.EditStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.forward.AgendaForwardDemandPO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.CommandForwardServiceImpl;
import com.sumavision.bvc.device.command.basic.silence.CommandSilenceServiceImpl;
import com.sumavision.bvc.device.command.cascade.CommandCascadeServiceImpl;
import com.sumavision.bvc.device.command.cooperate.CommandCooperateServiceImpl;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.bvc.device.command.message.CommandMessageServiceImpl;
import com.sumavision.tetris.auth.login.LoginService;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupForwardService;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.bvc.business.group.function.GroupFunctionService;
import com.sumavision.tetris.bvc.business.group.secret.GroupSecretService;
import com.sumavision.tetris.bvc.business.group.speak.GroupSpeakService;
import com.sumavision.tetris.bvc.business.special.authorize.GroupAuthorizeService;
import com.sumavision.tetris.bvc.business.special.cooperate.GroupCooperateService;
import com.sumavision.tetris.bvc.business.special.cross.GroupCrossService;
import com.sumavision.tetris.bvc.business.special.replace.GroupReplaceService;
import com.sumavision.tetris.bvc.cascade.bo.AuthCommandBO;
import com.sumavision.tetris.bvc.cascade.bo.CrossCommandBO;
import com.sumavision.tetris.bvc.cascade.bo.GroupBO;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.bvc.cascade.bo.ReplaceCommandBO;
import com.sumavision.tetris.bvc.cascade.bo.SecretCommandBO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.xml.XMLReader;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.wrapper.CopyHeaderHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class ProtocolParser {
	
	@Autowired
	private GroupForwardService groupForwardService;
	
	@Autowired
	private GroupCrossService groupCrossService;
	
	@Autowired
	private AgendaForwardDemandDAO agendaForwardDemandDao;
	
	@Autowired
	private GroupSecretService groupSecretService;
	
	@Autowired
	private GroupReplaceService groupReplaceService;
	
	@Autowired
	private GroupAuthorizeService groupAuthorizeService;

	@Autowired
	private CommandCascadeServiceImpl commandCascadeServiceImpl;

	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private GroupFunctionService groupFunctionService;
	
	@Autowired
	private CommandCooperateServiceImpl commandCooperateServiceImpl;
	
	@Autowired
	private CommandForwardServiceImpl commandForwardServiceImpl;
	
	@Autowired
	private CommandMeetingSpeakServiceImpl commandMeetingSpeakServiceImpl;

	@Autowired
	private CommandMessageServiceImpl commandMessageServiceImpl;

	@Autowired
	private CommandSilenceServiceImpl commandSilenceServiceImpl;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private GroupCooperateService groupCooperateService;

	@Autowired
	private GroupSpeakService groupSpeakService;
	
	/**
	 * 重新绑定当前线程的请求，写入用户登录信息爱<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月15日 下午5:49:38
	 * @param String srcNo 可能是操作用户号码，也可能是app号码
	 * @param String commandname 命令头 
	 */
	public void rebindRequest(String srcNo, String commandname) throws Exception{
		Long userId = 2l;
		if(srcNo!=null && !"".equals(srcNo) && "syncinfo".equals(commandname) && !"syncroutelink".equals(commandname) && !"authnotify".equals(commandname)){
			FolderUserMap map = folderUserMapDao.findByUserNo(srcNo);
			if(map != null) userId = map.getUserId();
		}
		
		String token = loginService.doUserIdLogin(userId);
		
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		final CopyHeaderHttpServletRequestWrapper request = new CopyHeaderHttpServletRequestWrapper(attributes.getRequest());
		request.addHeader(HttpConstant.HEADER_AUTH_TOKEN, token);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}
	
	/**
	 * 解析根节点节点名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:01:03
	 * @param String xml 协议
	 * @return String 根节点名称
	 */
	private String parseRootNodeName(String xml){
		String rootNodeName = null;
		if(xml.indexOf("<control") >= 0){
			rootNodeName = "control";
		}else if(xml.indexOf("<notify") >= 0){
			rootNodeName = "notify";
		}else if(xml.indexOf("<Control") >= 0){
			rootNodeName = "Control";
		}else if(xml.indexOf("<Notify") >= 0){
			rootNodeName = "Notify";
		}else if(xml.indexOf("<Response") >= 0){
			rootNodeName = "Response";
		}else if(xml.indexOf("<response") >= 0){
			rootNodeName = "response";
		}else if(xml.indexOf("<Request") >= 0){
			rootNodeName = "Request";
		}else if(xml.indexOf("<request") >= 0){
			rootNodeName = "request";
		}
		return rootNodeName;
	}
	
	/**
	 * 解析大白本xml协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午10:37:19
	 * @param String srcNo 操作用户号码
	 * @param String xml 大白本协议
	 */
	public synchronized void parse(String srcNo, String xml) throws Exception{
		String rootNodeName = parseRootNodeName(xml);
//		if(true){
//			return;
//		}
		XMLReader reader = new XMLReader(xml);
		String commandname = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".commandname").toString());
		rebindRequest(srcNo, commandname);
		if("syncinfo".equals(commandname) || "syncroutelink".equals(commandname) || "authnotify".equals(commandname)){
			resourceRemoteService.notifyXml(commandname, xml);
		}else{
			String operation = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".operation").toString());
			if("insmsg".equals(commandname) && "send".equals(operation)){
				//私有协议
				sendInstantMessage(reader, rootNodeName);
			}else if("group".equals(commandname) && "create".equals(operation)){
				String biztype = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.biztype").toString());
				if("cmd".equals(biztype)){
					createCommand(reader, rootNodeName, srcNo);
				}else if("cnf".equals(biztype)){
					createConference(reader, rootNodeName, srcNo);
				}
			}else if("group".equals(commandname) && "update".equals(operation)){
				String biztype = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.biztype").toString());
				if("cmd".equals(biztype)){
					updateCommand(reader, rootNodeName, srcNo, GroupType.BASIC);
				}else if("cnf".equals(biztype)){
					updateConference(reader, rootNodeName, srcNo, GroupType.MEETING);
				}
			}else if("group".equals(commandname) && "destroy".equals(operation)){
				deleteCommand(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "silencehigherstart".equals(operation)){
				becomeSilence(reader, rootNodeName);
			}else if("bizcmd".equals(commandname) && "silencehigherstop".equals(operation)){
				becomeSilence(reader, rootNodeName);
			}else if("bizcmd".equals(commandname) && "silencelowerstart".equals(operation)){
				becomeSilence(reader, rootNodeName);
			}else if("bizcmd".equals(commandname) && "silencelowerstop".equals(operation)){
				becomeSilence(reader, rootNodeName);
			}else if("bizcmd".equals(commandname) && "start".equals(operation)){
				startCommand(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "maddfull".equals(operation)){
				infoCommand(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "stop".equals(operation)){
				stopCommand(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "pause".equals(operation)){
				pauseCommand(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "resume".equals(operation)){
				resumeCommand(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "maddinc".equals(operation)){
				joinCommand(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "mquit".equals(operation)){
				String quittype = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".quittype").toString());
				if("r".equals(quittype)){
					exitCommand(reader, rootNodeName, srcNo);
				}else if("p".equals(quittype)){
					kikoutCommand(reader, rootNodeName, srcNo);
				}
			}else if("bizcmd".equals(commandname) && "mquitreq".equals(operation)){
				exitCommandRequest(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "mquitres".equals(operation)){
				exitCommandResponse(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "corpstart".equals(operation)){
				cooperationStart(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "corpstop".equals(operation)){
				cooperationStop(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "secretstart".equals(operation)){
				startSecret(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "secretstop".equals(operation)){
				stopSecret(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "crossstart".equals(operation)){
				startCross(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "crossstop".equals(operation)){
				stopCross(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "replacestart".equals(operation)){
				startReplace(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "replacestop".equals(operation)){
				stopReplace(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "authstart".equals(operation)){
				startAuthorize(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "authstop".equals(operation)){
				stopAuthorize(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "pullmediastart".equals(operation)){
				startDeviceForwardInCommand(reader, rootNodeName, srcNo);
			}else if("bizcmd".equals(commandname) && "pullmediastop".equals(operation)){
				stopDeviceForwardInCommand(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "start".equals(operation)){
				startConference(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "maddfull".equals(operation)){
				infoConference(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "stop".equals(operation)){
				stopConference(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "pause".equals(operation)){
				pauseConference(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "resume".equals(operation)){
				resumeConference(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "maddinc".equals(operation)){
				joinConference(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "mquit".equals(operation)){
				String quittype = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".quittype").toString());
				if("r".equals(quittype)){
					exitConference(reader, rootNodeName, srcNo);
				}else if("p".equals(quittype)){
					kikoutConference(reader, rootNodeName, srcNo);
				}
			}else if("bizcnf".equals(commandname) && "mquitreq".equals(operation)){
				exitConferenceRequest(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "mquitres".equals(operation)){
				exitConferenceResponse(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "pullmediastart".equals(operation)){
				startDeviceForwardInConference(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "pullmediastop".equals(operation)){
				stopDeviceForwardInConference(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "spkset".equals(operation)){
				String spktype = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".spktype").toString());
				if("p".equals(spktype)){
					speakerSetByChairman(reader, rootNodeName, srcNo);
				}else if("r".equals(spktype)){
					speakerSetByMember(reader, rootNodeName, srcNo);
				}
			}else if("bizcnf".equals(commandname) && "spkcal".equals(operation)){
				speakerCancel(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "spkreq".equals(operation)){
				speakerSetRequest(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "spkres".equals(operation)){
				speakerSetResponse(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "discstart".equals(operation)){
				discussStart(reader, rootNodeName, srcNo);
			}else if("bizcnf".equals(commandname) && "discstop".equals(operation)){
				discussStop(reader, rootNodeName, srcNo);
			}
		}
		
	}
	
	/**
	 * 发送实时消息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午2:06:03
	 * @param reader
	 * @param rootNodeName
	 * @throws Exception
	 */
	public void sendInstantMessage(XMLReader reader, String rootNodeName) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String content = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".content").toString());
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO user = userQuery.findByUserno(op);
		
		commandMessageServiceImpl.broadcastInstantMessage(user.getId(), user.getUsername(), groupId, content);
	}
	
	/**************
	 ****指挥相关****
	 **************/
		
	/**
	 * 静默<br/>
	 * <p>包含指挥/会议的对上对下、开始停止</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午3:10:15
	 * @param reader
	 * @param rootNodeName
	 * @throws Exception
	 */
	public void becomeSilence(XMLReader reader, String rootNodeName) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String operation = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".operation").toString());
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO user = userQuery.findByUserno(op);
		
		if("silencehigherstart".equals(operation)){
			commandSilenceServiceImpl.startSilence(groupId, user.getId(), true, false);
		}else if("silencehigherstop".equals(operation)){
			commandSilenceServiceImpl.stopSilence(groupId, user.getId(), true, false);
		}else if("silencelowerstart".equals(operation)){
			commandSilenceServiceImpl.startSilence(groupId, user.getId(), false, true);
		}else if("silencelowerstop".equals(operation)){
			commandSilenceServiceImpl.stopSilence(groupId, user.getId(), false, true);
		}
		
	}

	/**
	 * 创建指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:07:02
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void createCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		String subject = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".subject").toString());
		String bizname = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.bizname").toString());
		String creatorid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.creatorid").toString());
//		String topid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.topid").toString());
		List<MinfoBO> minfos = new ArrayList<MinfoBO>();
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.mlist.minfo").toString());
		if(nodes!=null && nodes.size()>0){
			for(Node node:nodes){
				MinfoBO minfo = new MinfoBO().setMid(reader.readString("mid", node))
											 .setMname(reader.readString("mname", node))
											 .setMtype(reader.readString("mtype", node))
											 .setPid(reader.readString("pid", node));
				minfos.add(minfo);
			}
		}
		
		Set<String> usernos = new HashSet<String>();
		usernos.add(creatorid);
		String chairmanNo = null;
		for(MinfoBO minfo:minfos){
			usernos.add(minfo.getMid());
			if(minfo.getPid()==null || "".equals(minfo.getPid())){
				chairmanNo = minfo.getMid();
			}
		}
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		UserVO chairman = null;
		UserVO creator = null;
		List<Long> userIds = new ArrayList<Long>();
		List<Long> hallIds=new ArrayList<>();
		List<String> bundleIds=new ArrayList<>();
		for(UserVO user:users){
			userIds.add(user.getId());
			if(user.getUserno().equals(creatorid)){
				creator = user;
			}
			if(user.getUserno().equals(chairmanNo)){
				chairman = user;
			}
		}
		
		//commandBasicServiceImpl.save(creator.getId(), chairman.getId(), creator.getNickname(), bizname, subject, GroupType.BASIC, OriginType.OUTER, userIds, gid);
		groupService.saveCommand_TS(creator.getId(),creator.getUsername(),"user",chairman.getId()+"",bizname,
				subject, BusinessType.COMMAND, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIds,hallIds,bundleIds,gid,
				null,null,null,null,null);
	}
	
	/**
	 * 更新指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 上午10:32:52
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 * @param GroupType type 因为与指挥会议公用了此方法，所以需要传入BASIC/MEETING
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateCommand(XMLReader reader, String rootNodeName, String srcNo, GroupType type) throws Exception{
		GroupBO group = new GroupBO();
		group.setGid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString()))
			 .setSubject(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".subject").toString()))
			 .setBizname(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.bizname").toString()))
			 .setCreatorid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.creatorid").toString()))
			 .setTopid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.topid").toString()));
		
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.mlist.minfo").toString());
		if(nodes!=null && nodes.size()>0){
			group.setMlist(new ArrayList<MinfoBO>());
			for(Node node:nodes){
				MinfoBO minfo = new MinfoBO();
				minfo.setMid(reader.readString("mid", node))
					 .setMname(reader.readString("mname", node))
					 .setMtype(reader.readString("mtype", node))
					 .setMstatus(reader.readString("mstatus", node))
					 .setPid(reader.readString("pid", node));
				group.getMlist().add(minfo);
			}
		}

		Set<String> usernos = new HashSet<String>();
		usernos.add(group.getCreatorid());
		String chairmanNo = null;
		for(MinfoBO minfo:group.getMlist()){
			usernos.add(minfo.getMid());
			if(minfo.getPid()==null || "".equals(minfo.getPid())){
				chairmanNo = minfo.getMid();
			}
		}
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		UserVO chairman = null;
		UserVO creator = null;
		List<Long> userIds = new ArrayList<Long>();
		List<Long> hallIds=new ArrayList<>();
		List<String> bundleIds=new ArrayList<>();
		for(UserVO user:users){
			userIds.add(user.getId());
			if(user.getUserno().equals(group.getCreatorid())){
				creator = user;
			}
			if(user.getUserno().equals(chairmanNo)){
				chairman = user;
			}
		}
		
		GroupPO groupPO=groupDAO.findByUuid(group.getGid());
		if(groupPO != null){
			groupService.remove(creator.getId(),new ArrayListWrapper<Long>().add(groupPO.getId()).getList());
		}
		
        if(GroupType.BASIC.equals(type)){
			groupService.saveCommand(creator.getId(),creator.getUsername(),"user",chairman.getId()+"",group.getBizname(),
					group.getSubject(), BusinessType.COMMAND, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIds,hallIds,bundleIds,group.getGid(),
					null,null,null,null,null);
		}else if(GroupType.MEETING.equals(type)){
			groupService.saveCommand(creator.getId(),creator.getUsername(),"user",chairman.getId()+"",group.getBizname(),
					group.getSubject(), BusinessType.MEETING_BVC, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIds,hallIds,bundleIds,group.getGid(),
					null,null,null,null,null);
		}

	}
	
	/**
	 * 删除指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:07:02
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void deleteCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		UserVO user = userQuery.findByUserno(srcNo);
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		GroupPO groupPO = groupDAO.findByUuid(gid);
		//commandBasicServiceImpl.remove(user.getId(), new ArrayListWrapper<Long>().add(groupId).getList());
		groupService.remove(user.getId(), new ArrayListWrapper<Long>().add(groupPO.getId()).getList());
	}
	
	/**
	 * 开始指挥/临时指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:08:25
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void startCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		
		String groupType = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".grouptype").toString());
		if(groupType == null || EditStatus.NORMAL.getCode().equals(groupType)){
			String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
			GroupPO groupPO=groupDAO.findByUuid(gid);
			groupService.start(groupPO.getId(),-1);
		}else if(EditStatus.TEMP.getCode().equals(groupType)){
			String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
			String subject = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".subject").toString());
			String bizname = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.bizname").toString());
			String creatorid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.creatorid").toString());
			List<MinfoBO> minfos = new ArrayList<MinfoBO>();
			List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.mlist.minfo").toString());
			if(nodes!=null && nodes.size()>0){
				for(Node node:nodes){
					MinfoBO minfo = new MinfoBO().setMid(reader.readString("mid", node))
												 .setMname(reader.readString("mname", node))
												 .setMtype(reader.readString("mtype", node))
												 .setPid(reader.readString("pid", node));
					minfos.add(minfo);
				}
			}
			
			Set<String> usernos = new HashSet<String>();
			usernos.add(creatorid);
			String chairmanNo = null;
			for(MinfoBO minfo:minfos){
				usernos.add(minfo.getMid());
				if(minfo.getPid()==null || "".equals(minfo.getPid())){
					chairmanNo = minfo.getMid();
				}
			}
			List<UserVO> users = userQuery.findByUsernoIn(usernos);
			UserVO chairman = null;
			UserVO creator = null;
			List<Long> userIds = new ArrayList<Long>();
			List<Long> hallIds=new ArrayList<>();
			List<String> bundleIds=new ArrayList<>();
			for(UserVO user:users){
				userIds.add(user.getId());
				if(user.getUserno().equals(creatorid)){
					creator = user;
				}
				if(user.getUserno().equals(chairmanNo)){
					chairman = user;
				}
			}
			
			String biztype = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.biztype").toString());
			if("cmd".equals(biztype)){
				groupService.saveAndStartTemporary(creator.getId(),creator.getUsername(),"user",chairman.getId()+"",bizname,
						subject, BusinessType.COMMAND, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIds,hallIds,bundleIds,gid,
						null,null,null,null);
			}else if("cnf".equals(biztype)){
				groupService.saveAndStartTemporary(creator.getId(),creator.getUsername(),"user",chairman.getId()+"",bizname,
						subject, BusinessType.MEETING_BVC, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIds,hallIds,bundleIds,gid,
						null,null,null,null);
			}
			
		}
	}
	
	/**
	 * 全量信息同步<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 上午9:56:11
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void infoCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		GroupBO group = new GroupBO();
		group.setGid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString()))
			.setOp(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString()))
			.setGroupType(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".grouptype").toString()))
			.setSubject(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".subject").toString()))
			.setStime(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".stime").toString()))
			.setBizname(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.bizname").toString()))
			.setCreatorid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.creatorid").toString()))
			.setTopid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.topid").toString()))
			.setStatus(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".cmdstatus.status").toString()));
		
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.mlist.minfo").toString());
		if(nodes!=null && nodes.size()>0){
			group.setmAddList(new ArrayList<MinfoBO>());
			for(Node node:nodes){
				MinfoBO minfo = new MinfoBO();
				minfo.setMid(reader.readString("mid", node))
					 .setMname(reader.readString("mname", node))
					 .setMtype(reader.readString("mtype", node))
					 .setMstatus(reader.readString("mstatus", node))
					 .setPid(reader.readString("pid", node));
				group.getmAddList().add(minfo);
			}
		}
		
		nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".cmdstatus.authitem").toString());
		if(nodes!=null && nodes.size()>0){
			AuthCommandBO authitem = new AuthCommandBO();
			authitem.setOp(reader.readString("op", nodes.get(0)))
					.setAccepauthid(reader.readString("accepauthid", nodes.get(0)))
					.setCmdedid(reader.readString("cmdedid", nodes.get(0)));
			group.setAuthitem(authitem);
		}
		
		nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".cmdstatus.replaceitem").toString());
		if(nodes!=null && nodes.size()>0){
			ReplaceCommandBO replaceitem = new ReplaceCommandBO();
			replaceitem.setOp(reader.readString("op", nodes.get(0)))
					   .setTargid(reader.readString("targid", nodes.get(0)));
			group.setReplaceitem(replaceitem);
		}
		
		nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".cmdstatus.secretlist.secretitem").toString());
		if(nodes!=null && nodes.size()>0){
			group.setSecretlist(new ArrayList<SecretCommandBO>());
			for(Node node:nodes){
				SecretCommandBO secretitem = new SecretCommandBO();
				secretitem.setUpid(reader.readString("upid", node))
						  .setDownid(reader.readString("downid", node));
				group.getSecretlist().add(secretitem);
			}
		}
		
		nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".cmdstatus.croplist.cropitem").toString());
		if(nodes!=null && nodes.size()>0){
			group.setCroplist(new ArrayList<String>());
			for(Node node:nodes){
				group.getCroplist().add(reader.readString("mid", node));
			}
		}
		
		nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".cmdstatus.croslist.crossitem").toString());
		if(nodes!=null && nodes.size()>0){
			group.setCroslist(new ArrayList<CrossCommandBO>());
			for(Node node:nodes){
				CrossCommandBO crossitem = new CrossCommandBO();
				crossitem.setUpid(reader.readString("upid", node))
						 .setDownid(reader.readString("downid", node));
				group.getCroslist().add(crossitem);
			}
		}
		
		//TODO:commandCascadeServiceImpl.memberAddFull(group, GroupType.BASIC);
		groupService.memberAddFull(group, GroupType.BASIC);

	}
	
	/**
	 * 停止指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:09:06
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void stopCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		//Long groupId = commandGroupDao.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO user = userQuery.findByUserno(op);
		//commandBasicServiceImpl.stop(user.getId(), groupId, 0);
		GroupPO groupPO=groupDAO.findByUuid(gid);
		groupService.stop(user.getId(),groupPO.getId(),0);
	}
	
	/**
	 * 暂停指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:09:48
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void pauseCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		groupFunctionService.pause(groupId);
	}
	
	/**
	 * 恢复指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:09:48
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void resumeCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		groupFunctionService.pauseRecover(groupId);
	}
	
	/**
	 * 进入指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:09:48
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void joinCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		List<MinfoBO> minfos = new ArrayList<MinfoBO>();
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".mlist.minfo").toString());
		if(nodes!=null && nodes.size()>0){
			for(Node node:nodes){
				MinfoBO minfo = new MinfoBO().setMid(reader.readString("mid", node))
											 .setMname(reader.readString("mname", node))
											 .setMtype(reader.readString("mtype", node))
											 .setPid(reader.readString("pid", node));
				minfos.add(minfo);
			}
		}
		
		Set<String> usernos = new HashSet<String>();
		for(MinfoBO minfo:minfos){
			usernos.add(minfo.getMid());
		}
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		List<Long> userIds = new ArrayList<Long>();
		List<Long> hallIds = new ArrayList<Long>();
		List<String> bundleIds = new ArrayList<String>();
		for(UserVO user:users){
			userIds.add(user.getId());
		}
		//TODO:commandBasicServiceImpl.addOrEnterMembers(groupId, userIds);
		groupService.addOrEnterMembers(groupId,userIds,hallIds,bundleIds);
	}
	
	/**
	 * 退出指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:09:48
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void exitCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO user = userQuery.findByUserno(mid);
		commandBasicServiceImpl.removeMembers2(groupId, new ArrayListWrapper<Long>().add(user.getId()).getList(), 0);
	}
	
	/**
	 * 退出指挥请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:09:48
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void exitCommandRequest(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO user = userQuery.findByUserno(op);
		commandBasicServiceImpl.exitApply(user.getId(), groupId);
	}
	
	/**
	 * 退出指挥响应<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:09:48
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void exitCommandResponse(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String code = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".code").toString());
		if("0".equals(code)){
			String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
			Long groupId = groupDAO.findIdByUuid(gid);
			String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
			UserVO oUser = userQuery.findByUserno(op);
			String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
			UserVO mUser = userQuery.findByUserno(mid);
			commandBasicServiceImpl.exitApplyDisagree(oUser.getId(), groupId, new ArrayListWrapper<Long>().add(mUser.getId()).getList());
		}
	}
	
	/**
	 * 踢出指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:09:48
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void kikoutCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO user = userQuery.findByUserno(mid);
		//commandBasicServiceImpl.removeMembers2(groupId, new ArrayListWrapper<Long>().add(user.getId()).getList(), 2);
        groupService.removeMembersU(groupId,new ArrayListWrapper<Long>().add(user.getId()).getList(),2);
	}


	/**
	 * 开始协同指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:12:01
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void cooperationStart(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".mlist.mid").toString());
		Set<String> usernos = new HashSet<String>();
		if(nodes!=null && nodes.size()>0){
			for(Node node:nodes){
				usernos.add(node.getTextContent());
			}
		}
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		List<Long> userIds = new ArrayList<Long>();
		for(UserVO user:users){
			userIds.add(user.getId());
		}
		//TODO:commandCooperateServiceImpl.start(groupId, userIds);
		groupCooperateService.startU(groupId,userIds);
	}
	
	/**
	 * 停止协同指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:12:01
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void cooperationStop(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".mlist.mid").toString());
		Set<String> usernos = new HashSet<String>();
		if(nodes!=null && nodes.size()>0){
			for(Node node:nodes){
				usernos.add(node.getTextContent());
			}
		}
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		List<Long> userIds = new ArrayList<Long>();
		for(UserVO user:users){
			userIds.add(user.getId());
		}
		//commandCooperateServiceImpl.revokeBatch(userIds, groupId);
		groupCooperateService.revokeBatchU(groupId,userIds);
	}
	
	/**
	 * 开始指挥设备转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:12:01
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void startDeviceForwardInCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".mlist.mid").toString());
		Set<String> usernos = new HashSet<String>();
		if(nodes!=null && nodes.size()>0){
			for(Node node:nodes){
				usernos.add(node.getTextContent());
			}
		}
		
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		List<Long> userIds = new ArrayList<Long>();
		for(UserVO user:users){
			userIds.add(user.getId());
		}
		
		nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".medialist.mediasrcid").toString());
		Set<String> mediaIds = new HashSet<String>();
		if(nodes!=null && nodes.size()>0){
			for(Node node:nodes){
				mediaIds.add(node.getTextContent());
			}
		}
		
		List<String> bundleIds = bundleDao.findBundleIdByUsernameIn(mediaIds);
		
		users = userQuery.findByUsernoIn(mediaIds);
		List<Long> srcUserIds = new ArrayList<Long>();
		if(users!=null && users.size()>0){
			for(UserVO user:users){
				srcUserIds.add(user.getId());
			}
		}
		//TODO:commandForwardServiceImpl.forward(groupId, srcUserIds, bundleIds, userIds);
		groupForwardService.forward(groupId, srcUserIds, bundleIds, userIds);
	}
	
	/**
	 * 停止指挥设备转发(源和目的集合里只有一个源一个目的)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:12:01
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void stopDeviceForwardInCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO opUser = userQuery.findByUserno(op);
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".mlist.mid").toString());
		
		List<String> usernos = new ArrayList<String>();
		if(nodes!=null && nodes.size()>0){
			for(Node node:nodes){
				usernos.add(node.getTextContent());
			}
		}
		
		nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".medialist.mediasrcid").toString());
		List<String> mediaIds = new ArrayList<String>();
		if(nodes!=null && nodes.size()>0){
			for(Node node:nodes){
				mediaIds.add(node.getTextContent());
			}
		}
		
		if(usernos.size()>0 && mediaIds.size()>0){
			for(int i = 0; i < mediaIds.size(); i++){
				for(int j = 0; j < mediaIds.size(); j++){
					//如果有相同的媒体转发，随便停止一个
					List<AgendaForwardDemandPO> demands = agendaForwardDemandDao.findBySrcCodeAndDstCodeAndBusinessId(mediaIds.get(i), usernos.get(j), groupId);
					if(demands.size() > 0){
						groupForwardService.stop(opUser.getId(), groupId, new ArrayListWrapper<Long>().add(demands.get(0).getId()).getList());
					}
				}
			}
		}
	}
	
	/**************
	 ****会议相关****
	 **************/
	
	/**
	 * 创建会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:23:36
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void createConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		String subject = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".subject").toString());
		String bizname = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.bizname").toString());
		String creatorid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.creatorid").toString());
//		String topid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.topid").toString());
		List<MinfoBO> minfos = new ArrayList<MinfoBO>();
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.mlist.minfo").toString());
		if(nodes!=null && nodes.size()>0){
			for(Node node:nodes){
				MinfoBO minfo = new MinfoBO().setMid(reader.readString("mid", node))
											 .setMname(reader.readString("mname", node))
											 .setMtype(reader.readString("mtype", node))
											 .setPid(reader.readString("pid", node));
				minfos.add(minfo);
			}
		}
		
		Set<String> usernos = new HashSet<String>();
		usernos.add(creatorid);
		String chairmanNo = null;
		for(MinfoBO minfo:minfos){
			usernos.add(minfo.getMid());
			if(minfo.getPid()==null || "".equals(minfo.getPid())){
				chairmanNo = minfo.getMid();
			}
		}
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		UserVO chairman = null;
		UserVO creator = null;
		List<Long> userIds = new ArrayList<Long>();
		List<Long> hallIds=new ArrayList<>();
		List<String> bundleIds=new ArrayList<>();
		for(UserVO user:users){
			userIds.add(user.getId());
			if(user.getUserno().equals(creatorid)){
				creator = user;
			}
			if(user.getUserno().equals(chairmanNo)){
				chairman = user;
			}
		}
		
		//commandBasicServiceImpl.save(creator.getId(), chairman.getId(), creator.getNickname(), bizname, subject, GroupType.MEETING, OriginType.OUTER, userIds, gid);
		groupService.saveCommand_TS(creator.getId(),creator.getUsername(),"user",chairman.getId()+"",bizname,
				subject, BusinessType.MEETING_BVC, com.sumavision.tetris.bvc.business.OriginType.OUTER,userIds,hallIds,bundleIds,gid,
				null,null,null,null,null);
	}
	
	/**
	 * 更新会议信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 上午10:34:19
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 * @param GroupType type 类型
	 */
	public void updateConference(XMLReader reader, String rootNodeName, String srcNo, GroupType type) throws Exception{
		updateCommand(reader, rootNodeName, srcNo, type);
	}
	
	/**
	 * 删除会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:23:36
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void deleteConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		deleteCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 开始会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:23:36
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void startConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		startCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 会议全量信息同步<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 上午10:39:45
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void infoConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		GroupBO group = new GroupBO();
		group.setGid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString()))
			.setOp(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString()))
			.setSubject(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".subject").toString()))
			.setStime(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".stime").toString()))
			.setBizname(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.bizname").toString()))
			.setCreatorid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.creatorid").toString()))
			.setTopid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.topid").toString()))
			.setMode(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".cnfstatus.mode").toString()))
			.setStatus(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".cnfstatus.status").toString()))
			.setSpkid(reader.readString(new StringBufferWrapper().append(rootNodeName).append(".cnfstatus.spkid").toString()));
		
		List<Node> nodes = reader.readNodeList(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.mlist.minfo").toString());
		if(nodes!=null && nodes.size()>0){
			group.setmAddList(new ArrayList<MinfoBO>());
			for(Node node:nodes){
				MinfoBO minfo = new MinfoBO();
				minfo.setMid(reader.readString("mid", node))
					 .setMname(reader.readString("mname", node))
					 .setMtype(reader.readString("mtype", node))
					 .setMstatus(reader.readString("mstatus", node))
					 .setPid(reader.readString("pid", node));
				group.getmAddList().add(minfo);
			}
		}
		
		groupService.memberAddFull(group, GroupType.BASIC);
	}
	
	/**
	 * 停止会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:23:36
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void stopConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		stopCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 暂停会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:23:36
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void pauseConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		pauseCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 恢复会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:23:36
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void resumeConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		resumeCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 进入会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:23:36
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void joinConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		joinCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 退出会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:23:36
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void exitConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		exitCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 退出会议请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 上午9:53:52
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void exitConferenceRequest(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		exitCommandRequest(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 退出会议响应<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 上午9:53:52
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void exitConferenceResponse(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		exitCommandResponse(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 踢出会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:23:36
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void kikoutConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		kikoutCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 开始会议设备转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:12:01
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void startDeviceForwardInConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		startDeviceForwardInCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 停止会议设备转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:12:01
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void stopDeviceForwardInConference(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		stopDeviceForwardInCommand(reader, rootNodeName, srcNo);
	}
	
	/**
	 * 主席设置发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:12:01
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void speakerSetByChairman(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operation = userQuery.findByUserno(op);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO speaker = userQuery.findByUserno(mid);
		//commandMeetingSpeakServiceImpl.speakAppoint(operation.getId(), groupId, new ArrayListWrapper<Long>().add(speaker.getId()).getList());
		groupSpeakService.speakAppointU(groupId,new ArrayListWrapper<Long>().add(speaker.getId()).getList());
	}
	
	/**
	 * 成员主动发言通知<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:31:09
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void speakerSetByMember(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO speaker = userQuery.findByUserno(mid);
		//commandMeetingSpeakServiceImpl.speakApplyAgree(operator.getId(), groupId, new ArrayListWrapper<Long>().add(speaker.getId()).getList());
	    groupSpeakService.speakApplyAgreeU(groupId,new ArrayListWrapper<Long>().add(speaker.getId()).getList());
	}
	
	/**
	 * 关闭发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:31:09
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void speakerCancel(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO speaker = userQuery.findByUserno(mid);
		//commandMeetingSpeakServiceImpl.speakStopByMember(speaker.getId(), groupId);
		groupSpeakService.speakStopByChairmanU(groupId,new ArrayListWrapper<Long>().add(speaker.getId()).getList());
	}
	 
	/**
	 * 申请发言请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:35:51
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
 	 * @param String srcNo 操作用户号码
	 */
	public void speakerSetRequest(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO speaker = userQuery.findByUserno(mid);
		//commandMeetingSpeakServiceImpl.speakApply(speaker.getId(), groupId);
		groupSpeakService.speakApply(speaker.getId(),groupId);
	}
	
	/**
	 * 申请发言响应<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:35:51
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void speakerSetResponse(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO speaker = userQuery.findByUserno(mid);
		String code = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".code").toString());
		if("0".equals(code)){
			//commandMeetingSpeakServiceImpl.speakApplyDisagree(operator.getId(), groupId, new ArrayListWrapper<Long>().add(speaker.getId()).getList());
		    groupSpeakService.speakApplyDisagreeU(operator.getId(),groupId,new ArrayListWrapper<Long>().add(speaker.getId()).getList());
		}else if("1".equals(code)){
			//commandMeetingSpeakServiceImpl.speakApplyAgree(operator.getId(), groupId, new ArrayListWrapper<Long>().add(speaker.getId()).getList());
		}
	}
	
	/**
	 * 开启讨论模式<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午4:52:07
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void discussStart(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		//TODO:commandMeetingSpeakServiceImpl.discussStart(operator.getId(), groupId);
		groupSpeakService.discussStart(operator.getId(),groupId);
	}
	
	/**
	 * 关闭讨论模式<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午4:52:07
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void discussStop(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		//commandMeetingSpeakServiceImpl.discussStop(operator.getId(), groupId);
		groupSpeakService.discussStop(operator.getId(),groupId);
	}
	
	/**
	 * 开始专向会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午4:52:07
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void startSecret(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String topid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".upid").toString());
		String downid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".downid").toString());
		UserVO topUser = userQuery.findByUserno(topid);
		UserVO downUser = userQuery.findByUserno(downid);
		groupSecretService.startSecret(groupId, operator.getId(), topUser.getId(), downUser.getId());
	}
	
	/**
	 * 停止专向会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午4:52:07
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void stopSecret(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String topid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".topid").toString());
		String downid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".downid").toString());
		groupSecretService.stopSecret(groupId, operator.getId());
	}
	
	/**
	 * 开始接替指挥<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 下午6:36:01
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 * @throws Exception
	 */
	public void startReplace(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String targid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".targid").toString());
		UserVO targetUser = userQuery.findByUserno(targid);
		groupReplaceService.startU(groupId, operator.getId(), targetUser.getId());
	}

	/**
	 * 停止接替指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 下午6:37:39
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 * @throws Exception
	 */
	public void stopReplace(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		/*String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String targid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".targid").toString());
		UserVO targetUser = userQuery.findByUserno(targid);*/
		groupReplaceService.stop(groupId);
	}
	
	/**
	 * 开始授权指挥<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 下午6:36:01
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 * @throws Exception
	 */
	public void startAuthorize(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String accepauthid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".accepauthid").toString());
		UserVO acceptUser = userQuery.findByUserno(accepauthid);
		String cmdedid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".cmdedid").toString());
		UserVO cmdUser = userQuery.findByUserno(cmdedid);
		groupAuthorizeService.startU(groupId, operator.getId(), acceptUser.getId(), cmdUser.getId());
	}
	
	/**
	 * 停止授权指挥<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 下午6:36:01
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 * @throws Exception
	 */
	public void stopAuthorize(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		/*String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String accepauthid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".accepauthid").toString());
		UserVO acceptUser = userQuery.findByUserno(accepauthid);
		String cmdedid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".cmdedid").toString());
		UserVO cmdUser = userQuery.findByUserno(cmdedid);*/
		groupAuthorizeService.stop(groupId);
	}
	
	/**
	 * 开始越级会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午4:52:07
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void startCross(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String topid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".upid").toString());
		String downid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".downid").toString());
		UserVO topUser = userQuery.findByUserno(topid);
		UserVO downUser = userQuery.findByUserno(downid);
		List<Long> userIds = new ArrayListWrapper<Long>().add(topUser.getId()).add(downUser.getId()).getList();
		
		groupCrossService.startU(groupId, userIds);
//		groupSecretService.startSecret(groupId, operator.getId(), topUser.getId(), downUser.getId());
	}
	
	/**
	 * 停止越级会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午4:52:07
	 * @param XMLReader reader 协议 
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void stopCross(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = groupDAO.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String topid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".topid").toString());
		String downid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".downid").toString());
		UserVO topUser = userQuery.findByUserno(topid);
		UserVO downUser = userQuery.findByUserno(downid);
		List<Long> userIds = new ArrayListWrapper<Long>().add(topUser.getId()).add(downUser.getId()).getList();
		
		groupCrossService.revokeBatchU(groupId, userIds);
	}
	
}
