package com.sumavision.tetris.zoom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.agent.service.TerminalAgentService;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserService;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.zoom.exception.UserNotFoundException;
import com.sumavision.tetris.zoom.exception.ZoomMemberNotFoundException;
import com.sumavision.tetris.zoom.exception.ZoomNotFoundException;
import com.sumavision.tetris.zoom.exception.ZoomStopedException;
import com.sumavision.tetris.zoom.jv220.Jv220UserAllocationQuery;
import com.sumavision.tetris.zoom.webrtc.WebRtcRoomInfoQuery;
import com.sumavision.tetris.zoom.webrtc.WebRtcRoomInfoService;
import com.sumavision.tetris.zoom.webrtc.WebRtcVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class ZoomService {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ZoomDAO zoomDao;
	
	@Autowired
	private ZoomQuery zoomQuery;
	
	@Autowired
	private WebRtcRoomInfoService webRtcRoomInfoService;
	
	@Autowired
	private WebRtcRoomInfoQuery webRtcRoomInfoQuery;

	@Autowired
	private ZoomMemberDAO zoomMemberDao;
	
	@Autowired
	private ZoomMessageDAO zoomMessageDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	//@Autowired
	//private HistoryService historyService;
	
	@Autowired
	private TerminalAgentService terminalAgentService;
	
	@Autowired
	private Jv220UserAllocationQuery jv220UserAllocationQuery;
	
	/**
	 * 邀人建会<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月20日 上午8:41:52
	 * @param String name 会议名称
	 * @param ZoomMode mode 会议模式
	 * @param String rename 主席入会名称
	 * @param Boolean myAudio 是否开启音频
	 * @param Boolean myVideo 是否开启视频
	 * @param ZoomSecretLevel secretLevel 秘密等级
	 * @param ZoomMemberType type 成员类型
	 * @param Collection<String> usernos 被邀成员用户号码列表
	 * @return ZoomVO 会议信息
	 */
	public ZoomVO create(
			String name,
			ZoomMode mode,
			String rename,
			Boolean myAudio,
			Boolean myVideo,
			ZoomSecretLevel secretLevel,
			ZoomMemberType type,
			Collection<String> usernos) throws Exception{
		
		UserVO self = userQuery.current();
		
		ZoomPO zoom = new ZoomPO();
		zoom.setName(name);
		zoom.setMode(mode);
		zoom.setStatus(ZoomStatus.START);
		zoom.setSecretLevel(secretLevel);
		zoom.setCreatorUserId(self.getId());
		zoom.setCreatorUserNickname(self.getNickname());
		zoom.setCreatorUserRename(rename);
		zoom.setUpdateTime(new Date());
		zoomDao.save(zoom);
		zoom.setCode();
		zoomDao.save(zoom);
		
		WebRtcVO webRtc = webRtcRoomInfoService.createRoom(zoom.getId(), zoom.getCode());
		
		ZoomMemberPO chairman = new ZoomMemberPO();
		chairman.setUserId(self.getId().toString());
		chairman.setUserNickname(self.getNickname());
		chairman.setUserno(self.getUserno());
		chairman.setRename(rename);
		chairman.setTourist(false);
		chairman.setZoomId(zoom.getId());
		chairman.setChairman(true);
		chairman.setSpokesman(false);
		chairman.setJoin(true);
		chairman.setMyAudio(myAudio);
		chairman.setMyVideo(myVideo);
		chairman.setTheirAudio(true);
		chairman.setTheirVideo(true);
		chairman.setUpdateTime(new Date());
		chairman.setType(type);
		zoomMemberDao.save(chairman);
		
		//发送通知
		List<UserVO> users = userQuery.findByUsernoIn(usernos);
		List<Long> userIds = new ArrayList<Long>();
		for(UserVO user:users){
			userIds.add(user.getId());
		}
		List<Long> jv220UserIds = jv220UserAllocationQuery.filterJv220(userIds);
		JSONObject content = new JSONObject();
		content.put("code", zoom.getCode());
		content.put("name", zoom.getName());
		String businessId = "zoomInvitation";
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		for(UserVO user:users){
			if(jv220UserIds.contains(user.getId())){
				jv220s.add(new ZoomMemberVO().setUserId(user.getId().toString())
											 .setUserno(user.getUserno())
											 .setUserNickname(user.getNickname()));
			}else{
				websocketMessageService.push(user.getId().toString(), businessId, content, chairman.getUserId(), chairman.getRename());
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		ZoomMemberVO me = new ZoomMemberVO().set(chairman);
		
		return zoomQuery.queryBundleInfo(new ZoomVO().set(zoom)
					    .setWebRtc(webRtc)
					    .setMe(me)
					    .setChairman(me)
					    .setTotalMembers(1l)
					    .addMember(me));
	}
	
	/**
	 * 创建会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 上午11:43:48
	 * @param String name 会议名称
	 * @param ZoomMode mode 会议模式
	 * @param String rename 入会名称
	 * @param Boolean myAudio 是否开启音频
	 * @param Boolean myVideo 是否开启视频
	 * @param ZoomSecretLevel secretLevel 保密等级
	 * @param ZoomMemberType type 成员类型
	 * @return ZoomVO 会议信息
	 */
	public ZoomVO create(
			String name,
			ZoomMode mode,
			String rename,
			Boolean myAudio,
			Boolean myVideo,
			ZoomSecretLevel secretLevel,
			ZoomMemberType type) throws Exception{
		
		UserVO self = userQuery.current();
		
		ZoomPO zoom = new ZoomPO();
		zoom.setName(name);
		zoom.setMode(mode);
		zoom.setStatus(ZoomStatus.START);
		zoom.setSecretLevel(secretLevel);
		zoom.setCreatorUserId(self.getId());
		zoom.setCreatorUserNickname(self.getNickname());
		zoom.setCreatorUserRename(rename);
		zoom.setUpdateTime(new Date());
		zoomDao.save(zoom);
		zoom.setCode();
		zoomDao.save(zoom);
		
		WebRtcVO webRtc = webRtcRoomInfoService.createRoom(zoom.getId(), zoom.getCode());
		
		ZoomMemberPO chairman = new ZoomMemberPO();
		chairman.setUserId(self.getId().toString());
		chairman.setUserNickname(self.getNickname());
		chairman.setUserno(self.getUserno());
		chairman.setRename(rename);
		chairman.setTourist(false);
		chairman.setZoomId(zoom.getId());
		chairman.setChairman(true);
		chairman.setSpokesman(false);
		chairman.setJoin(true);
		chairman.setMyAudio(myAudio);
		chairman.setMyVideo(myVideo);
		chairman.setTheirAudio(true);
		chairman.setTheirVideo(true);
		chairman.setUpdateTime(new Date());
		chairman.setType(type);
		zoomMemberDao.save(chairman);
		
		ZoomMemberVO me = new ZoomMemberVO().set(chairman);
		return zoomQuery.queryBundleInfo(new ZoomVO().set(zoom)
												     .setWebRtc(webRtc)
												     .setMe(me)
												     .setChairman(me)
												     .setTotalMembers(1l)
												     .addMember(me));
	}
	
	/**
	 * 加入会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午3:43:56
	 * @param String code 会议号码
	 * @param String rename 入会名称
	 * @param Boolean myAudio 是否开启音频
	 * @param Boolean myVideo 是否开启视频
	 * @param ZoomMemberType type 终端类型
	 * @return ZoomVO 会议信息
	 */
	public ZoomVO join(
			String code,
			String rename,
			Boolean myAudio,
			Boolean myVideo,
			ZoomMemberType type) throws Exception{
		
		ZoomPO zoom = zoomDao.findByCode(code);
		if(zoom == null){
			throw new ZoomNotFoundException(code); 
		}else if(zoom.getStatus().equals(ZoomStatus.STOP)){
			throw new ZoomStopedException();
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		UserVO self = null;
		boolean tourist = true;
		try{
			self = userQuery.current();
			if(self != null){
				tourist = false;
			}
		}catch(Exception e){
			self = userService.addTourist(rename);
		}
		if(self == null) self = userService.addTourist(rename);
		
		ZoomMemberPO member = zoomMemberDao.findByZoomIdAndUserId(zoom.getId(), self.getId().toString());
		if(member == null){
			member = new ZoomMemberPO();
			member.setUserId(self.getId().toString());
			member.setUserNickname(self.getNickname());
			member.setUserno(self.getUserno());
			member.setRename(rename);
			member.setTourist(tourist);
			member.setZoomId(zoom.getId());
			member.setChairman(false);
			member.setSpokesman(false);
			member.setJoin(true);
			member.setMyAudio(myAudio);
			member.setMyVideo(myVideo);
			member.setTheirAudio(true);
			member.setTheirVideo(true);
			member.setUpdateTime(new Date());
			member.setType(type);
			zoomMemberDao.save(member);
		}else{
			member.setRename(rename);
			member.setChairman(false);
			member.setSpokesman(false);
			member.setJoin(true);
			member.setMyAudio(myAudio);
			member.setMyVideo(myVideo);
			member.setTheirAudio(true);
			member.setTheirVideo(true);
			member.setType(type);
			zoomMemberDao.save(member);
		}
		
		//添加历史
		/*if(!tourist){
			String remark = new StringBufferWrapper().append("会议名称：").append(zoom.getName()).append("@@").append("创建者：").append(zoom.getCreatorUserRename()).toString();
			historyService.addZoomHistory(zoom.getCode(), self.getId().toString(), remark);
		}*/
		
		//数据转换
		List<ZoomMemberPO> members = zoomMemberDao.findByZoomId(zoom.getId());
		ZoomMemberVO me = new ZoomMemberVO().set(member);
		ZoomVO zoomInfo = new ZoomVO().set(zoom).setMe(me).setWebRtc(webRtc);
		zoomInfo.setTotalMembers(Long.valueOf(members.size()));
		for(ZoomMemberPO m:members){
			ZoomMemberVO m1 = new ZoomMemberVO().set(m);
			if(m.getChairman()) zoomInfo.setChairman(m1);
			zoomInfo.addMember(m1);
			if(m.getJoin() && m.getSpokesman()) zoomInfo.addSpokesman(m1);
		}
		zoomInfo = zoomQuery.queryBundleInfo(zoomInfo);
		
		//通知会议成员
		String businessId = "zoomJoin";
		JSONObject content = JSON.parseObject(JSON.toJSONString(me));
		List<ZoomMemberVO> jv220Members = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberVO m:zoomInfo.getMembers()){
			if(m.getId().equals(member.getId())) continue;
			if(!m.getJoin()) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, self.getId().toString(), rename);
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220Members.add(m);
			}
		}
		if(jv220Members.size() > 0){
			terminalAgentService.push(zoomInfo.getCode(), content, jv220Members, businessId);
		}
		
		return zoomInfo;
	}
	
	/**
	 * 开始会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午11:50:22
	 * @param String code 会议号码
	 * @param String rename 入会名称
	 * @param Boolean myAudio 是否开启音频
	 * @param Boolean myVideo 是否开启视频
	 * @return ZoomVO 会议信息
	 */
	public ZoomVO start(
			String code,
			String rename,
			Boolean myAudio,
			Boolean myVideo,
			ZoomMemberType type) throws Exception{
		
		UserVO self = null;
		boolean tourist = true;
		try{
			self = userQuery.current();
			tourist = false;
		}catch(Exception e){
			self = userService.addTourist(rename);
		}
		
		ZoomPO zoom = zoomDao.findByCode(code);
		if(zoom == null){
			throw new ZoomNotFoundException(code); 
		}
		zoom.setStatus(ZoomStatus.START);
		zoomDao.save(zoom);
		
		WebRtcVO webRtc = webRtcRoomInfoService.createRoom(zoom.getId(), zoom.getCode());
		
		ZoomMemberPO selfMember = null;
		if(tourist){
			selfMember = new ZoomMemberPO();
			selfMember.setUserId(self.getId().toString());
			selfMember.setUserNickname(self.getNickname());
			selfMember.setRename(rename);
			selfMember.setTourist(tourist);
			selfMember.setZoomId(zoom.getId());
			selfMember.setChairman(true);
			selfMember.setSpokesman(false);
			selfMember.setJoin(true);
			selfMember.setMyAudio(myAudio);
			selfMember.setMyVideo(myVideo);
			selfMember.setTheirAudio(true);
			selfMember.setTheirVideo(true);
			selfMember.setUpdateTime(new Date());
			selfMember.setType(type);
			zoomMemberDao.save(selfMember);
		}else{
			selfMember = zoomMemberDao.findByZoomIdAndUserId(zoom.getId(), self.getId().toString());
			selfMember.setRename(rename);
			selfMember.setChairman(true);
			selfMember.setJoin(true);
			selfMember.setMyAudio(myAudio);
			selfMember.setMyVideo(myVideo);
			selfMember.setTheirAudio(true);
			selfMember.setTheirVideo(true);
			selfMember.setType(type);
			zoomMemberDao.save(selfMember);
		}
		
		//设置主席
		List<ZoomMemberPO> members = zoomMemberDao.findByZoomId(zoom.getId());
		for(ZoomMemberPO m:members){
			if(m.getId().equals(selfMember.getId())) continue;
			m.setChairman(false);
		}
		zoomMemberDao.save(members);
		
		//数据转换
		ZoomMemberVO me = new ZoomMemberVO().set(selfMember);
		ZoomVO zoomInfo = new ZoomVO().set(zoom).setWebRtc(webRtc).setMe(me).setChairman(me);
		zoomInfo.setTotalMembers(Long.valueOf(members.size()));
		for(ZoomMemberPO m:members){
			zoomInfo.addMember(new ZoomMemberVO().set(m));
		}
		zoomInfo = zoomQuery.queryBundleInfo(zoomInfo);
		
		//通知会议成员
		String businessId = "zoomStart";
		JSONObject content = new JSONObject();
		content.put("code", zoom.getCode());
		content.put("name", zoom.getName());
		content.put("chairman", rename);
		List<ZoomMemberVO> jv220Members = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberVO m:zoomInfo.getMembers()){
			if(m.getId().equals(selfMember.getId())) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, self.getId().toString(), rename);
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220Members.add(m);
			}
		}
		
		if(jv220Members.size() > 0){
			terminalAgentService.push(zoomInfo.getCode(), content, jv220Members, businessId);
		}
		
		return zoomInfo;
	}
	
	/**
	 * 停止会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:04:23
	 * @param String code 会议号码
	 */
	public void stop(String code) throws Exception{
		
		ZoomPO zoom = zoomDao.findByCode(code);
		if(zoom == null){
			throw new ZoomNotFoundException(code); 
		}
		zoom.setStatus(ZoomStatus.STOP);
		zoomDao.save(zoom);
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		webRtcRoomInfoService.destroyRoom(zoom.getId());
		
		//通知会议成员
		List<ZoomMemberPO> members = zoomMemberDao.findByZoomId(zoom.getId());
		ZoomMemberPO chairman = null;
		for(ZoomMemberPO m:members){
			if(m.getChairman()){
				chairman = m;
				break;
			}
		}
		List<ZoomMemberPO> logined = new ArrayList<ZoomMemberPO>();
		List<ZoomMemberPO> tourists = new ArrayList<ZoomMemberPO>();
		List<ZoomMemberPO> jv220s = new ArrayList<ZoomMemberPO>();
		JSONObject content = new JSONObject();
		content.put("code", zoom.getCode());
		content.put("name", zoom.getName());
		String businessId = "zoomStop";
		for(ZoomMemberPO m:members){
			if(m.getTourist()){
				tourists.add(m);
			}else{
				logined.add(m);
			}
			if(m.getJoin()) continue;
			if(m.getChairman()) continue;
			if(ZoomMemberType.JV220.equals(m.getType()) && m.getJoin()){
				jv220s.add(m);
			}else{
				websocketMessageService.push(m.getUserId(), businessId, content, chairman.getUserId(), chairman.getRename());
			}
		}
		if(jv220s.size() > 0){
			List<ZoomMemberVO> targets = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(jv220s, ZoomMemberVO.class);
			terminalAgentService.push(zoom.getCode(), content, zoomQuery.queryBundleInfo(targets, webRtc), businessId);
		}
		
		//处理游客
		if(tourists.size() > 0){
			List<Long> touristIds = new ArrayList<Long>();
			for(ZoomMemberPO tourist:tourists){
				touristIds.add(Long.valueOf(tourist.getUserId()));
			}
			userService.removeTouristBatch(touristIds);
			zoomMemberDao.deleteInBatch(tourists);
		}
		
		//处理非游客
		if(logined.size() > 0){
			for(ZoomMemberPO member:logined){
				member.setSpokesman(false);
				member.setJoin(false);
			}
			zoomMemberDao.save(logined);
		}
		
	}
	
	/**
	 * 退出会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午3:50:17
	 * @param Long zoomMemberId 会议成员id
	 * @return Long zoomId 会议id
	 */
	public Map<String, Object> exit(Long zoomMemberId) throws Exception{
		
		ZoomMemberPO selfMember = zoomMemberDao.findOne(zoomMemberId);
		if(selfMember == null){
			throw new ZoomMemberNotFoundException(zoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(selfMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(selfMember.getZoomId()); 
		}
		
		if(selfMember.getChairman()){
			return new HashMapWrapper<String, Object>().put("zoomId", zoom.getId())
													   .put("zoomCode", zoom.getCode())
													   .getMap();
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), zoomMemberId);
		
		if(selfMember.getTourist()){
			userService.removeTourist(Long.valueOf(selfMember.getUserId()));
			zoomMemberDao.delete(selfMember);
		}else{
			selfMember.setChairman(false);
			selfMember.setJoin(false);
			selfMember.setSpokesman(false);
			zoomMemberDao.save(selfMember);
		}
		
		//通知成员
		if(others!=null && others.size()>0){
			ZoomMemberVO exitMember = new ZoomMemberVO().set(selfMember);
			List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
			members.add(exitMember);
			zoomQuery.queryBundleInfo(members, webRtc);
			JSONObject content = JSON.parseObject(JSON.toJSONString(exitMember));
			String businessId = "zoomExit";
			List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
			for(ZoomMemberVO m:members){
				if(!m.getJoin()) continue;
				if(m.getId().equals(exitMember.getId())) continue;
				if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
					websocketMessageService.push(m.getUserId(), businessId, content, selfMember.getUserId(), selfMember.getRename());
				}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
					jv220s.add(m);
				}
			}
			if(jv220s.size() > 0){
				terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
			}
		}
		
		return null;
	}
	
	/**
	 * 移交主席权限<br/>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午9:27:05
	 * @param Long targetZoomMemberId 目标主席成员id
	 * @return old ZoomMemberVO 旧的主席成员
	 * @return new ZoomMemberVO 新主席成员
	 */
	public Map<String, Object> changeChairman(Long targetZoomMemberId) throws Exception{
		
		ZoomMemberPO newChairmanEntity = zoomMemberDao.findOne(targetZoomMemberId);
		if(newChairmanEntity == null){
			throw new ZoomMemberNotFoundException(targetZoomMemberId);
		}
		ZoomPO zoom = zoomDao.findOne(newChairmanEntity.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(newChairmanEntity.getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		ZoomMemberPO oldchairmanEntity = zoomMemberDao.findByZoomIdAndChairman(zoom.getId(), true);
		oldchairmanEntity.setChairman(false);
		zoomMemberDao.save(oldchairmanEntity);
		ZoomMemberVO oldChairman = new ZoomMemberVO().set(oldchairmanEntity);
		
		newChairmanEntity.setChairman(true);
		zoomMemberDao.save(newChairmanEntity);
		ZoomMemberVO newChairman = new ZoomMemberVO().set(newChairmanEntity);
		
		//通知成员
		List<Long> exceptIds = new ArrayListWrapper<Long>().add(newChairmanEntity.getId()).add(oldchairmanEntity.getId()).getList();
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNotIn(zoom.getId(), exceptIds);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.add(newChairman);
		members.add(oldChairman);
		zoomQuery.queryBundleInfo(members, webRtc);
		
		JSONObject content = new JSONObject();
		content.put("old", oldChairman);
		content.put("new", newChairman);
		String businessId = "zoomChangeChairman";
		String fromUserId = oldChairman.getUserId();
		String fromUserRename = oldChairman.getRename();
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberVO m:members){
			if(!m.getJoin()) continue;
			if(m.getId().equals(oldChairman.getId())) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, fromUserId, fromUserRename);
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220s.add(m);
			}
		}
		
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return new HashMapWrapper<String, Object>().put("old", oldChairman)
												   .put("new", newChairman)
												   .getMap();
	}
	
	/**
	 * 添加发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午10:40:13
	 * @param Collection<Long> targetZoomMemberIds 目标发言人成员id列表
	 * @return List<ZoomMemberVO> 发言人列表
	 */
	public List<ZoomMemberVO> addSpokesman(Collection<Long> targetZoomMemberIds) throws Exception{
		
		List<ZoomMemberPO> targetMembers = zoomMemberDao.findAll(targetZoomMemberIds);
		if(targetMembers==null || targetMembers.size()<=0){
			throw new ZoomMemberNotFoundException(targetZoomMemberIds.toString());
		}
		
		ZoomPO zoom = zoomDao.findOne(targetMembers.get(0).getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(targetMembers.get(0).getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		for(ZoomMemberPO targetMember:targetMembers){
			targetMember.setSpokesman(true);
		}
		zoomMemberDao.save(targetMembers);
		
		List<ZoomMemberVO> spokesmen = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberPO targetMember:targetMembers){
			spokesmen.add(new ZoomMemberVO().set(targetMember));
		}
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNotIn(zoom.getId(), targetZoomMemberIds);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.addAll(spokesmen);
		zoomQuery.queryBundleInfo(members, webRtc);
		JSONObject content = JSON.parseObject(JSON.toJSONString(spokesmen));
		String businessId = "zoomAddSpokesman";
		String fromUserId = null;
		String fromUserRename = null;
		for(ZoomMemberVO m:members){
			if(m.getChairman()){
				fromUserId = m.getUserId();
				fromUserRename = m.getRename();
				break;
			}
		}
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberVO m:members){
			if(!m.getJoin()) continue;
			if(m.getChairman()) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, fromUserId, fromUserRename);
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220s.add(m);
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return spokesmen;
	}
	
	/**
	 * 删除发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午10:54:16
	 * @param Collection<Long> targetZoomMemberIds 目标发言人成员id列表
	 * @return List<ZoomMemberVO> 发言人列表
	 */
	public List<ZoomMemberVO> removeSpokesman(Collection<Long> targetZoomMemberIds) throws Exception{
		
		List<ZoomMemberPO> targetMembers = zoomMemberDao.findAll(targetZoomMemberIds);
		if(targetMembers==null || targetMembers.size()<=0){
			throw new ZoomMemberNotFoundException(targetZoomMemberIds.toString());
		}
		
		ZoomPO zoom = zoomDao.findOne(targetMembers.get(0).getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(targetMembers.get(0).getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		for(ZoomMemberPO targetMember:targetMembers){
			targetMember.setSpokesman(false);
		}
		zoomMemberDao.save(targetMembers);
		
		List<ZoomMemberVO> removedSpokesmen = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberPO targetMember:targetMembers){
			removedSpokesmen.add(new ZoomMemberVO().set(targetMember));
		}
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNotIn(zoom.getId(), targetZoomMemberIds);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.addAll(removedSpokesmen);
		zoomQuery.queryBundleInfo(members, webRtc);
		JSONObject content = JSON.parseObject(JSON.toJSONString(removedSpokesmen));
		String businessId = "zoomRemoveSpokesman";
		String fromUserId = null;
		String fromUserRename = null;
		for(ZoomMemberVO m:members){
			if(m.getChairman()){
				fromUserId = m.getUserId();
				fromUserRename = m.getRename();
				break;
			}
		}
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberVO m:members){
			if(!m.getJoin()) continue;
			if(m.getChairman()) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, fromUserId, fromUserRename);
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220s.add(m);
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return removedSpokesmen;
	}
	
	/**
	 * 邀请入会<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:23:01
	 * @param String code 会议号码
	 * @param Collection<Long> userIds 目标用户id列表
	 */
	public void invitation(String code, Collection<Long> userIds) throws Exception{
		
		ZoomPO zoom = zoomDao.findByCode(code);
		if(zoom == null){
			throw new ZoomNotFoundException(code); 
		}
		
		List<UserVO> users = userQuery.findByIdIn(userIds);
		if(users==null || users.size()<=0){
			throw new UserNotFoundException(userIds.toString());
		}
		
		//发起邀请
		List<Long> jv220UserIds = jv220UserAllocationQuery.filterJv220(userIds);
		ZoomMemberPO chairman = zoomMemberDao.findByZoomIdAndChairman(zoom.getId(), true);
		JSONObject content = new JSONObject();
		content.put("code", zoom.getCode());
		content.put("name", zoom.getName());
		String businessId = "zoomInvitation";
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		for(UserVO user:users){
			if(jv220UserIds.contains(user.getId())){
				jv220s.add(new ZoomMemberVO().setUserId(user.getId().toString())
											 .setUserno(user.getUserno())
											 .setUserNickname(user.getNickname()));
			}else{
				websocketMessageService.push(user.getId().toString(), businessId, content, chairman.getUserId(), chairman.getRename());
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
	}
	
	/**
	 * 踢出成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:35:12
	 * @param Collection<Long> targetZoomMemberIds 目标会议成员id列表
	 * @return List<ZoomMemberVO> 被踢出的会议成员列表
	 */
	public List<ZoomMemberVO> kickOut(Collection<Long> targetZoomMemberIds) throws Exception{
		
		List<ZoomMemberPO> targetMembers = zoomMemberDao.findAll(targetZoomMemberIds);
		if(targetMembers==null || targetMembers.size()<=0){
			throw new ZoomMemberNotFoundException(targetZoomMemberIds.toString());
		}
		
		ZoomPO zoom = zoomDao.findOne(targetMembers.get(0).getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(targetMembers.get(0).getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		List<ZoomMemberVO> kickOutMembers = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberPO targetMember:targetMembers){
			kickOutMembers.add(new ZoomMemberVO().set(targetMember));
		}
		
		//处理成员状态
		List<Long> touristUserIds = new ArrayList<Long>();
		List<ZoomMemberPO> tourists = new ArrayList<ZoomMemberPO>();
		List<ZoomMemberPO> joins = new ArrayList<ZoomMemberPO>();
		for(ZoomMemberPO targetMember:targetMembers){
			if(targetMember.getTourist()){
				touristUserIds.add(Long.valueOf(targetMember.getUserId()));
				tourists.add(targetMember);
			}else{
				targetMember.setChairman(false);
				targetMember.setJoin(false);
				targetMember.setSpokesman(false);
				joins.add(targetMember);
			}
		}
		if(touristUserIds.size() > 0){
			userService.removeTouristBatch(touristUserIds);
			zoomMemberDao.deleteInBatch(tourists);
		}
		if(joins.size() > 0){
			zoomMemberDao.save(joins);
		}
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNotIn(zoom.getId(), targetZoomMemberIds);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.addAll(kickOutMembers);
		zoomQuery.queryBundleInfo(members, webRtc);
		JSONObject content = JSON.parseObject(JSON.toJSONString(kickOutMembers));
		String fromUserId = null;
		String fromUserRename = null;
		if(others!=null && others.size()>0){
			for(ZoomMemberPO other:others){
				if(other.getChairman()){
					fromUserId = other.getUserId();
					fromUserRename = other.getRename();
					break;
				}
			}
		}
		String businessId = "zoomKickOut";
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberVO m:members){
			if(!m.getJoin() && !targetZoomMemberIds.contains(m.getId())) continue;
			if(m.getChairman()) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, fromUserId, fromUserRename);
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220s.add(m);
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return kickOutMembers;
	}
	
	/**
	 * 打开共享屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午3:24:07
	 * @param Long myZoomMemberId 当前会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	public ZoomMemberVO openShareScreen(Long myZoomMemberId) throws Exception{
		
		ZoomMemberPO selfMember = zoomMemberDao.findOne(myZoomMemberId);
		if(selfMember == null){
			throw new ZoomMemberNotFoundException(myZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(selfMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(selfMember.getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		selfMember.setShareScreen(true);
		zoomMemberDao.save(selfMember);
		ZoomMemberVO openShareScreenMember = new ZoomMemberVO().set(selfMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), myZoomMemberId);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.add(openShareScreenMember);
		zoomQuery.queryBundleInfo(members, webRtc);
		String businessId = "zoomOpenShareScreen";
		JSONObject content = JSON.parseObject(JSON.toJSONString(openShareScreenMember));
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberVO m:members){
			if(!m.getJoin()) continue;
			if(m.getId().equals(openShareScreenMember.getId())) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m)){
				websocketMessageService.push(m.getUserId(), businessId, content, selfMember.getUserId(), selfMember.getRename());
			}else if(ZoomMemberType.JV220.toString().equals(m)){
				jv220s.add(m);
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return openShareScreenMember;
	}
	
	/**
	 * 关闭共享屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午3:24:07
	 * @param Long myZoomMemberId 当前会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	public ZoomMemberVO closeShareScreen(Long myZoomMemberId) throws Exception{
		
		ZoomMemberPO selfMember = zoomMemberDao.findOne(myZoomMemberId);
		if(selfMember == null){
			throw new ZoomMemberNotFoundException(myZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(selfMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(selfMember.getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		selfMember.setShareScreen(false);
		zoomMemberDao.save(selfMember);
		ZoomMemberVO closeShareScreenMember = new ZoomMemberVO().set(selfMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), myZoomMemberId);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.add(closeShareScreenMember);
		zoomQuery.queryBundleInfo(members, webRtc);
		JSONObject content = JSON.parseObject(JSON.toJSONString(closeShareScreenMember));
		String businessId = "zoomCloseShareScreen";
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		for(ZoomMemberVO m:members){
			if(!m.getJoin()) continue;
			if(m.getId().equals(closeShareScreenMember.getId())) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, selfMember.getUserId(), selfMember.getRename());
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220s.add(m);
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return closeShareScreenMember;
	}
	
	/**
	 * 成员开启视频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午2:23:12
	 * @param Long myZoomMemberId 会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	public ZoomMemberVO openVideo(Long myZoomMemberId) throws Exception{
		ZoomMemberPO selfMember = zoomMemberDao.findOne(myZoomMemberId);
		if(selfMember == null){
			throw new ZoomMemberNotFoundException(myZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(selfMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(selfMember.getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		selfMember.setMyVideo(true);
		zoomMemberDao.save(selfMember);
		ZoomMemberVO openVideoMember = new ZoomMemberVO().set(selfMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), myZoomMemberId);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.add(openVideoMember);
		zoomQuery.queryBundleInfo(members, webRtc);
		JSONObject content = JSON.parseObject(JSON.toJSONString(openVideoMember));
		String businessId = "zoomOpenVideo";
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		
		for(ZoomMemberVO m:members){
			if(!m.getJoin()) continue;
			if(m.getId().equals(openVideoMember.getId())) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, selfMember.getUserId(), selfMember.getRename());
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220s.add(m);
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return openVideoMember;
	}
	
	/**
	 * 成员关闭视频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午2:29:12
	 * @param Long myZoomMemberId 会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	public ZoomMemberVO closeVideo(Long myZoomMemberId) throws Exception{
		ZoomMemberPO selfMember = zoomMemberDao.findOne(myZoomMemberId);
		if(selfMember == null){
			throw new ZoomMemberNotFoundException(myZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(selfMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(selfMember.getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		selfMember.setMyVideo(false);
		zoomMemberDao.save(selfMember);
		ZoomMemberVO closeVideoMember = new ZoomMemberVO().set(selfMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), myZoomMemberId);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.add(closeVideoMember);
		zoomQuery.queryBundleInfo(members, webRtc);
		JSONObject content = JSON.parseObject(JSON.toJSONString(closeVideoMember));
		String businessId = "zoomCloseVideo";
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		
		for(ZoomMemberVO m:members){
			if(!m.getJoin()) continue;
			if(m.getId().equals(closeVideoMember.getId())) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, selfMember.getUserId(), selfMember.getRename());
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220s.add(m);
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return closeVideoMember;
	}
	
	/**
	 * 成员开启音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午2:30:53
	 * @param Long myZoomMemberId 会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	public ZoomMemberVO openAudio(Long myZoomMemberId) throws Exception{
		ZoomMemberPO selfMember = zoomMemberDao.findOne(myZoomMemberId);
		if(selfMember == null){
			throw new ZoomMemberNotFoundException(myZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(selfMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(selfMember.getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		selfMember.setMyAudio(true);
		zoomMemberDao.save(selfMember);
		ZoomMemberVO openAudioMember = new ZoomMemberVO().set(selfMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), myZoomMemberId);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.add(openAudioMember);
		zoomQuery.queryBundleInfo(members, webRtc);
		JSONObject content = JSON.parseObject(JSON.toJSONString(openAudioMember));
		String businessId = "zoomOpenAudio";
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		
		for(ZoomMemberVO m:members){
			if(!m.getJoin()) continue;
			if(m.getId().equals(openAudioMember.getId())) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, selfMember.getUserId(), selfMember.getRename());
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220s.add(m);
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return openAudioMember;
	}
	
	/**
	 * 成员关闭音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午2:30:53
	 * @param Long myZoomMemberId 会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	public ZoomMemberVO closeAudio(Long myZoomMemberId) throws Exception{
		ZoomMemberPO selfMember = zoomMemberDao.findOne(myZoomMemberId);
		if(selfMember == null){
			throw new ZoomMemberNotFoundException(myZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(selfMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(selfMember.getZoomId()); 
		}
		
		WebRtcVO webRtc = webRtcRoomInfoQuery.findZoomWebRtc(zoom.getId());
		
		selfMember.setMyAudio(false);
		zoomMemberDao.save(selfMember);
		ZoomMemberVO closeAudioMember = new ZoomMemberVO().set(selfMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), myZoomMemberId);
		List<ZoomMemberVO> members = ZoomMemberVO.getConverter(ZoomMemberVO.class).convert(others, ZoomMemberVO.class);
		members.add(closeAudioMember);
		zoomQuery.queryBundleInfo(members, webRtc);
		JSONObject content = JSON.parseObject(JSON.toJSONString(closeAudioMember));
		String businessId = "zoomCloseAudio";
		List<ZoomMemberVO> jv220s = new ArrayList<ZoomMemberVO>();
		
		for(ZoomMemberVO m:members){
			if(!m.getJoin()) continue;
			if(m.getId().equals(closeAudioMember.getId())) continue;
			if(ZoomMemberType.TERMINl.toString().equals(m.getType())){
				websocketMessageService.push(m.getUserId(), businessId, content, selfMember.getUserId(), selfMember.getRename());
			}else if(ZoomMemberType.JV220.toString().equals(m.getType())){
				jv220s.add(m);
			}
		}
		if(jv220s.size() > 0){
			terminalAgentService.push(zoom.getCode(), content, jv220s, businessId);
		}
		
		return closeAudioMember;
	}
	
	/**
	 * 删除会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午4:28:56
	 * @param String code 会议号码
	 */
	public void remove(String code) throws Exception{
		
		ZoomPO zoom = zoomDao.findByCode(code);
		if(zoom == null){
			throw new ZoomNotFoundException(code); 
		}
		
		List<ZoomMemberPO> members = zoomMemberDao.findByZoomId(zoom.getId());
		if(members!=null && members.size()>0){
			zoomMemberDao.deleteInBatch(members);
		}
		
		List<ZoomMessagePO> messages = zoomMessageDao.findByZoomId(zoom.getId());
		if(messages!=null && messages.size()>0){
			zoomMessageDao.deleteInBatch(messages);
		}
		
		zoomDao.delete(zoom);
	}
	
}
