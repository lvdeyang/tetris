package com.sumavision.tetris.bvc.cascade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.service.ResourceRemoteService;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.enumeration.OriginType;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.CommandForwardServiceImpl;
import com.sumavision.bvc.device.command.cooperate.CommandCooperateServiceImpl;
import com.sumavision.bvc.device.command.meeting.CommandMeetingSpeakServiceImpl;
import com.sumavision.tetris.bvc.cascade.bo.MinfoBO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.xml.XMLReader;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class ProtocolParser {

	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;
	
	@Autowired
	private CommandCooperateServiceImpl commandCooperateServiceImpl;
	
	@Autowired
	private CommandForwardServiceImpl commandForwardServiceImpl;
	
	@Autowired
	private CommandMeetingSpeakServiceImpl commandMeetingSpeakServiceImpl;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private CommandGroupDAO commandGroupDao;
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
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
		if(xml.startsWith("<control")){
			rootNodeName = "control";
		}else if(xml.startsWith("<notify")){
			rootNodeName = "notify";
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
	public void parse(String srcNo, String xml) throws Exception{
		String rootNodeName = parseRootNodeName(xml);
		XMLReader reader = new XMLReader(xml);
		String commandname = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".commandname").toString());
		String operation = reader.readString(new StringBufferWrapper().append(rootNodeName).append("operation").toString());
		if("group".equals(commandname) && "create".equals(operation)){
			String biztype = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.biztype").toString());
			if("cmd".equals(biztype)){
				createCommand(reader, rootNodeName, srcNo);
			}else if("cnf".equals(biztype)){
				createConference(reader, rootNodeName, srcNo);
			}
		}else if("group".equals(commandname) && "destroy".equals(operation)){
			deleteCommand(reader, rootNodeName, srcNo);
		}else if("bizcmd".equals(commandname) && "start".equals(operation)){
			startCommand(reader, rootNodeName, srcNo);
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
		}else if("bizcmd".equals(commandname) && "corpstart".equals(operation)){
			cooperationStart(reader, rootNodeName, srcNo);
		}else if("bizcmd".equals(commandname) && "corpstop".equals(operation)){
			cooperationStop(reader, rootNodeName, srcNo);
		}else if("bizcmd".equals(commandname) && "pullmediastart".equals(operation)){
			startDeviceForwardInCommand(reader, rootNodeName, srcNo);
		}else if("bizcmd".equals(commandname) && "pullmediastop".equals(operation)){
			stopDeviceForwardInCommand(reader, rootNodeName, srcNo);
		}else if("bizcnf".equals(commandname) && "start".equals(operation)){
			startConference(reader, rootNodeName, srcNo);
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
		}else if("syncinfo".equals(commandname) || "syncroutelink".equals(commandname)){
			resourceRemoteService.notifyXml(commandname, xml);
		}
	}
	
	/**************
	 ****指挥相关****
	 **************/
	
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
		String topid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.topid").toString());
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
		for(UserVO user:users){
			userIds.add(user.getId());
			if(user.getUserno().equals(creatorid)){
				creator = user;
			}
			if(user.getUserno().equals(chairmanNo)){
				chairman = user;
			}
		}
		
		commandBasicServiceImpl.save(creator.getId(), chairman.getId(), creator.getNickname(), bizname, subject, GroupType.BASIC, OriginType.OUTER, userIds);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		commandBasicServiceImpl.remove(user.getId(), new ArrayListWrapper<Long>().add(groupId).getList());
	}
	
	/**
	 * 开始指挥<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:08:25
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void startCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		String gid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".gid").toString());
		Long groupId = commandGroupDao.findIdByUuid(gid);
		commandBasicServiceImpl.start(groupId, -1);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO user = userQuery.findByUserno(op);
		commandBasicServiceImpl.stop(user.getId(), groupId, 0);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		commandBasicServiceImpl.pause(groupId);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		commandBasicServiceImpl.pauseRecover(groupId);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
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
		for(UserVO user:users){
			userIds.add(user.getId());
		}
		for(Long userId:userIds){
			commandBasicServiceImpl.enter(userId, new ArrayListWrapper<Long>().add(groupId).getList());
		}
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO user = userQuery.findByUserno(mid);
		commandBasicServiceImpl.removeMembers(groupId, new ArrayListWrapper<Long>().add(user.getId()).getList(), 0);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO user = userQuery.findByUserno(mid);
		commandBasicServiceImpl.removeMembers(groupId, new ArrayListWrapper<Long>().add(user.getId()).getList(), 1);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
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
		commandCooperateServiceImpl.start(groupId, userIds);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
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
		commandCooperateServiceImpl.revokeBatch(userIds, groupId);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
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
		
		List<String> bundleIds = bundleDao.findBundleIdByBundleNumIn(mediaIds);
		commandForwardServiceImpl.forwardDevice(groupId, bundleIds, userIds);
	}
	
	/**
	 * 停止指挥设备转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午11:12:01
	 * @param XMLReader reader 协议
	 * @param String rootNodeName 协议根节点名称
	 * @param String srcNo 操作用户号码
	 */
	public void stopDeviceForwardInCommand(XMLReader reader, String rootNodeName, String srcNo) throws Exception{
		
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
		String topid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".bizinfo.topid").toString());
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
		for(UserVO user:users){
			userIds.add(user.getId());
			if(user.getUserno().equals(creatorid)){
				creator = user;
			}
			if(user.getUserno().equals(chairmanNo)){
				chairman = user;
			}
		}
		
		commandBasicServiceImpl.save(creator.getId(), chairman.getId(), creator.getNickname(), bizname, subject, GroupType.MEETING, OriginType.OUTER, userIds);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operation = userQuery.findByUserno(op);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO speaker = userQuery.findByUserno(mid);
		commandMeetingSpeakServiceImpl.speakAppoint(operation.getId(), groupId, new ArrayListWrapper<Long>().add(speaker.getId()).getList());
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
		speakerSetByChairman(reader, rootNodeName, srcNo);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO speaker = userQuery.findByUserno(mid);
		commandMeetingSpeakServiceImpl.speakStopByMember(speaker.getId(), groupId);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO speaker = userQuery.findByUserno(mid);
		commandMeetingSpeakServiceImpl.speakApply(speaker.getId(), groupId);
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
		Long groupId = commandGroupDao.findIdByUuid(gid);
		String op = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".op").toString());
		UserVO operator = userQuery.findByUserno(op);
		String mid = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".mid").toString());
		UserVO speaker = userQuery.findByUserno(mid);
		String code = reader.readString(new StringBufferWrapper().append(rootNodeName).append(".code").toString());
		if("0".equals(code)){
			commandMeetingSpeakServiceImpl.speakApplyDisagree(operator.getId(), groupId, new ArrayListWrapper<Long>().add(speaker.getId()).getList());
		}else if("1".equals(code)){
			commandMeetingSpeakServiceImpl.speakApplyAgree(operator.getId(), groupId, new ArrayListWrapper<Long>().add(speaker.getId()).getList());
		}
	}
	
}
