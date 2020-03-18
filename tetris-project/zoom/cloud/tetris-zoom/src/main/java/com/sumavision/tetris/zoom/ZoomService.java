package com.sumavision.tetris.zoom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserService;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.zoom.exception.UserNotFoundException;
import com.sumavision.tetris.zoom.exception.ZoomMemberNotFoundException;
import com.sumavision.tetris.zoom.exception.ZoomNotFoundException;
import com.sumavision.tetris.zoom.exception.ZoomStopedException;
import com.sumavision.tetris.zoom.history.HistoryService;

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
	private ZoomMemberDAO zoomMemberDao;
	
	@Autowired
	private ZoomMessageDAO zoomMessageDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private HistoryService historyService;
	
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
	 * @return ZoomVO 会议信息
	 */
	public ZoomVO create(
			String name,
			ZoomMode mode,
			String rename,
			Boolean myAudio,
			Boolean myVideo,
			ZoomMemberType type) throws Exception{
		
		UserVO self = userQuery.current();
		
		ZoomPO zoom = new ZoomPO();
		zoom.setName(name);
		zoom.setMode(mode);
		zoom.setStatus(ZoomStatus.START);
		//TODO 默认创建公开会议
		zoom.setSecretLevel(ZoomSecretLevel.PUBLIC);
		zoom.setCreatorUserId(self.getId());
		zoom.setCreatorUserNickname(self.getNickname());
		zoom.setCreatorUserRename(rename);
		zoom.setUpdateTime(new Date());
		zoomDao.save(zoom);
		//会议号码小于8位往前边补0
		String id = zoom.getId().toString();
		StringBufferWrapper code = new StringBufferWrapper();
		if(id.length() < 8){
			for(int i=0; i<(11-id.length()); i++){
				code.append(0);
			}
		}
		code.append(id);
		zoom.setCode(code.toString());
		zoomDao.save(zoom);
		
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
		return new ZoomVO().set(zoom)
						   .setMe(me)
						   .setChairman(me)
						   .setTotalMembers(1l)
						   .addMember(me);
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
	 * @return ZoomVO 会议信息
	 */
	public ZoomVO join(
			String code,
			String rename,
			Boolean myAudio,
			Boolean myVideo) throws Exception{
		
		ZoomPO zoom = zoomDao.findByCode(code);
		if(zoom == null){
			throw new ZoomNotFoundException(code); 
		}else if(zoom.getStatus().equals(ZoomStatus.STOP)){
			throw new ZoomStopedException();
		}
		
		UserVO self = null;
		boolean tourist = true;
		try{
			self = userQuery.current();
			tourist = false;
		}catch(Exception e){
			self = userService.addTourist(rename);
		}
		
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
			zoomMemberDao.save(member);
		}
		
		//添加历史
		if(tourist){
			String remark = new StringBufferWrapper().append("会议名称：").append(zoom.getName()).append("@@").append("创建者：").append(zoom.getCreatorUserRename()).toString();
			historyService.addZoomHistory(zoom.getCode(), self.getId().toString(), remark);
		}
		
		//通知会议成员
		List<ZoomMemberPO> members = zoomMemberDao.findByZoomId(zoom.getId());
		for(ZoomMemberPO m:members){
			if(m.getId().equals(member.getId())) continue;
			if(!m.getJoin()) continue;
			websocketMessageService.push(m.getUserId(), "zoomJoin", JSON.parseObject(JSON.toJSONString(new ZoomMemberVO().set(member))), tourist?self.getUuid():self.getId().toString(), rename);
		}
		
		ZoomMemberVO me = new ZoomMemberVO().set(member);
		ZoomVO zoomInfo = new ZoomVO().set(zoom).setMe(me);
		zoomInfo.setTotalMembers(Long.valueOf(members.size()));
		for(ZoomMemberPO m:members){
			ZoomMemberVO m1 = new ZoomMemberVO().set(m);
			if(m.getChairman()) zoomInfo.setChairman(m1);
			zoomInfo.addMember(m1);
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
			Boolean myVideo) throws Exception{
		
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
			zoomMemberDao.save(selfMember);
		}
		
		//通知会议成员
		List<ZoomMemberPO> members = zoomMemberDao.findByZoomId(zoom.getId());
		for(ZoomMemberPO m:members){
			if(m.getId().equals(selfMember.getId())) continue;
			m.setChairman(false);
			JSONObject content = new JSONObject();
			content.put("code", zoom.getCode());
			content.put("name", zoom.getName());
			content.put("chairman", rename);
			websocketMessageService.push(m.getUserId(), "zoomStart", content, self.getId().toString(), rename);
		}
		zoomMemberDao.save(members);
		
		ZoomMemberVO me = new ZoomMemberVO().set(selfMember);
		ZoomVO zoomInfo = new ZoomVO().set(zoom).setMe(me).setChairman(me);
		zoomInfo.setTotalMembers(Long.valueOf(members.size()));
		for(ZoomMemberPO m:members){
			zoomInfo.addMember(new ZoomMemberVO().set(m));
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
		for(ZoomMemberPO m:members){
			if(m.getTourist()){
				tourists.add(m);
			}else{
				logined.add(m);
			}
			if(m.getChairman()) continue;
			JSONObject content = new JSONObject();
			content.put("code", zoom.getCode());
			content.put("name", zoom.getName());
			websocketMessageService.push(m.getUserId(), "zoomStop", content, chairman.getUserId(), chairman.getRename());
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
	 */
	public void exit(Long zoomMemberId) throws Exception{
		
		ZoomMemberPO selfMember = zoomMemberDao.findOne(zoomMemberId);
		if(selfMember == null){
			throw new ZoomMemberNotFoundException(zoomMemberId);
		}
		ZoomPO zoom = zoomDao.findOne(selfMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(selfMember.getZoomId()); 
		}
		
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), zoomMemberId);
		
		//通知成员
		if(others!=null && others.size()>0){
			ZoomMemberVO exitMember = new ZoomMemberVO().set(selfMember);
			for(ZoomMemberPO other:others){
				websocketMessageService.push(other.getUserId(), "zoomExit", JSON.parseObject(JSON.toJSONString(exitMember)), selfMember.getUserId(), selfMember.getRename());
			}
		}
		
		if(selfMember.getTourist()){
			userService.removeTourist(Long.valueOf(selfMember.getUserId()));
			zoomMemberDao.delete(selfMember);
		}else{
			selfMember.setChairman(false);
			selfMember.setJoin(false);
			selfMember.setSpokesman(false);
			zoomMemberDao.save(selfMember);
		}
		
	}
	
	/**
	 * 移交主席权限<br/>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午9:27:05
	 * @param Long targetZoomMemberId 目标主席成员id
	 * @return ZoomMemberVO 新主席成员
	 */
	public ZoomMemberVO changeChairman(Long targetZoomMemberId) throws Exception{
		
		ZoomMemberPO targetMember = zoomMemberDao.findOne(targetZoomMemberId);
		if(targetMember == null){
			throw new ZoomMemberNotFoundException(targetZoomMemberId);
		}
		ZoomPO zoom = zoomDao.findOne(targetMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(targetMember.getZoomId()); 
		}
		targetMember.setChairman(true);
		zoomMemberDao.save(targetMember);
		ZoomMemberVO chairman = new ZoomMemberVO().set(targetMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), targetZoomMemberId);
		String fromUserId = null;
		String fromUserRename = null;
		if(others!=null && others.size()>0){
			for(ZoomMemberPO other:others){
				if(other.getChairman()){
					other.setChairman(false);
					zoomMemberDao.save(other);
					fromUserId = other.getUserId();
					fromUserRename = other.getRename();
					break;
				}
			}
		}
		if(others!=null && others.size()>0){
			for(ZoomMemberPO other:others){
				if(other.getUserId().equals(fromUserId)) continue;
				websocketMessageService.push(other.getUserId(), "zoomChangeChairman", JSON.parseObject(JSON.toJSONString(chairman)), fromUserId, fromUserRename);
			}
		}
		
		return chairman;
	}
	
	/**
	 * 添加发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午10:40:13
	 * @param Long targetZoomMemberId 目标发言人成员id
	 * @return ZoomMemberVO 发言人
	 */
	public ZoomMemberVO addSpokesman(Long targetZoomMemberId) throws Exception{
		
		ZoomMemberPO targetMember = zoomMemberDao.findOne(targetZoomMemberId);
		if(targetMember == null){
			throw new ZoomMemberNotFoundException(targetZoomMemberId);
		}
		ZoomPO zoom = zoomDao.findOne(targetMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(targetMember.getZoomId()); 
		}
		targetMember.setSpokesman(true);
		zoomMemberDao.save(targetMember);
		ZoomMemberVO spokesman = new ZoomMemberVO().set(targetMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), targetZoomMemberId);
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
		
		if(others!=null && others.size()>0){
			for(ZoomMemberPO other:others){
				if(other.getUserId().equals(fromUserId)) continue;
				websocketMessageService.push(other.getUserId(), "zoomAddSpokesman", JSON.parseObject(JSON.toJSONString(spokesman)), fromUserId, fromUserRename);
			}
		}
		
		return spokesman;
	}
	
	/**
	 * 删除发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午10:54:16
	 * @param Long targetZoomMemberId 目标发言人成员id
	 * @return ZoomMemberVO 发言人
	 */
	public ZoomMemberVO removeSpokesman(Long targetZoomMemberId) throws Exception{
		
		ZoomMemberPO targetMember = zoomMemberDao.findOne(targetZoomMemberId);
		if(targetMember == null){
			throw new ZoomMemberNotFoundException(targetZoomMemberId);
		}
		ZoomPO zoom = zoomDao.findOne(targetMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(targetMember.getZoomId()); 
		}
		targetMember.setSpokesman(false);
		zoomMemberDao.save(targetMember);
		ZoomMemberVO removedSpokesman = new ZoomMemberVO().set(targetMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), targetZoomMemberId);
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
		
		if(others!=null && others.size()>0){
			for(ZoomMemberPO other:others){
				if(other.getUserId().equals(fromUserId)) continue;
				websocketMessageService.push(other.getUserId(), "zoomRemoveSpokesman", JSON.parseObject(JSON.toJSONString(removedSpokesman)), fromUserId, fromUserRename);
			}
		}
		
		return removedSpokesman;
	}
	
	/**
	 * 邀请入会<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:23:01
	 * @param String code 会议号码
	 * @param Long userId 目标用户id
	 */
	public void invitation(String code, Long userId) throws Exception{
		
		ZoomPO zoom = zoomDao.findByCode(code);
		if(zoom == null){
			throw new ZoomNotFoundException(code); 
		}
		
		List<UserVO> users = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(userId).getList());
		if(users==null || users.size()<=0){
			throw new UserNotFoundException(userId);
		}
		UserVO targetUser = users.get(0);
		
		ZoomMemberPO chairman = zoomMemberDao.findByZoomIdAndChairman(zoom.getId(), true);
		
		JSONObject content = new JSONObject();
		content.put("code", zoom.getCode());
		content.put("name", zoom.getName());
		websocketMessageService.push(targetUser.getId().toString(), "zoomInvitation", content, chairman.getUserId(), chairman.getRename());
	}
	
	/**
	 * 踢出成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:35:12
	 * @param Long targetZoomMemberId 目标会议成员
	 * @return ZoomMemberVO 被踢出的会议成员
	 */
	public ZoomMemberVO kickOut(Long targetZoomMemberId) throws Exception{
		
		ZoomMemberPO targetMember = zoomMemberDao.findOne(targetZoomMemberId);
		if(targetMember == null){
			throw new ZoomMemberNotFoundException(targetZoomMemberId);
		}
		
		ZoomPO zoom = zoomDao.findOne(targetMember.getZoomId());
		if(zoom == null){
			throw new ZoomNotFoundException(targetMember.getZoomId()); 
		}
		ZoomMemberVO kickOutMember = new ZoomMemberVO().set(targetMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), targetZoomMemberId);
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
		
		if(others!=null && others.size()>0){
			for(ZoomMemberPO other:others){
				if(other.getUserId().equals(fromUserId)) continue;
				websocketMessageService.push(other.getUserId(), "zoomKickOut", JSON.parseObject(JSON.toJSONString(kickOutMember)), fromUserId, fromUserRename);
			}
		}
		
		if(targetMember.getTourist()){
			userService.removeTourist(Long.valueOf(targetMember.getUserId()));
			zoomMemberDao.delete(targetMember);
		}else{
			targetMember.setChairman(false);
			targetMember.setJoin(false);
			targetMember.setSpokesman(false);
			zoomMemberDao.save(targetMember);
		}
		
		return kickOutMember.set(targetMember);
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
		
		selfMember.setShareScreen(true);
		zoomMemberDao.save(selfMember);
		ZoomMemberVO openShareScreenMember = new ZoomMemberVO().set(selfMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), myZoomMemberId);
		if(others!=null && others.size()>0){
			for(ZoomMemberPO other:others){
				websocketMessageService.push(other.getUserId(), "zoomOpenShareScreen", JSON.parseObject(JSON.toJSONString(openShareScreenMember)), selfMember.getUserId(), selfMember.getRename());
			}
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
		
		selfMember.setShareScreen(false);
		zoomMemberDao.save(selfMember);
		ZoomMemberVO closeShareScreenMember = new ZoomMemberVO().set(selfMember);
		
		//通知成员
		List<ZoomMemberPO> others = zoomMemberDao.findByZoomIdAndIdNot(zoom.getId(), myZoomMemberId);
		if(others!=null && others.size()>0){
			for(ZoomMemberPO other:others){
				websocketMessageService.push(other.getUserId(), "zoomCloseShareScreen", JSON.parseObject(JSON.toJSONString(closeShareScreenMember)), selfMember.getUserId(), selfMember.getRename());
			}
		}
		
		return closeShareScreenMember;
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
