package com.sumavision.tetris.agent.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.agent.vo.CodecParamVO;
import com.sumavision.tetris.agent.vo.LocalPublishVO;
import com.sumavision.tetris.agent.vo.PassByParamVO;
import com.sumavision.tetris.agent.vo.PassByVO;
import com.sumavision.tetris.agent.vo.RemotePullVO;
import com.sumavision.tetris.agent.vo.RemoteVO;
import com.sumavision.tetris.agent.vo.ResourceVO;
import com.sumavision.tetris.agent.vo.ResponseResourceVO;
import com.sumavision.tetris.agent.vo.StopVO;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.bvc.business.dispatch.TetrisDispatchService;
import com.sumavision.tetris.bvc.business.dispatch.bo.ChannelBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.DispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.PassByBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.SourceParamBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StartUserDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StopTaskDispatchByUserIdAndMeetingCodeBO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.resouce.feign.resource.ResourceService;
import com.sumavision.tetris.resouce.feign.resource.UserBO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.zoom.ZoomDAO;
import com.sumavision.tetris.zoom.ZoomMemberPO;
import com.sumavision.tetris.zoom.ZoomMemberType;
import com.sumavision.tetris.zoom.ZoomMemberVO;
import com.sumavision.tetris.zoom.ZoomPO;
import com.sumavision.tetris.zoom.ZoomService;
import com.sumavision.tetris.zoom.ZoomVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class TerminalAgentService {

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ZoomDAO zoomDao;
	
	@Autowired
	private ZoomService zoomService;
	
	@Autowired
	private TetrisDispatchService tetrisDispatchService;
	
	/**
	 * 获取用户能看的资源，包括用户、会议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月11日 下午2:58:23
	 * @return AgentResponse 协议信息
	 */
	public ResponseResourceVO queryResources() throws Exception{
			
		ResponseResourceVO response = new ResponseResourceVO();
		
		UserVO user = userQuery.current();
		//用户信息
		List<UserBO> userBOs = resourceService.queryUsersByUserId(user.getId(), "");
		//会议信息
		List<ZoomPO> zooms = zoomDao.findAll();

		response.setResponse_users(new ArrayList<ResourceVO>());
		
		for(UserBO userBO: userBOs){
			ResourceVO userResource = new ResourceVO();
			userResource.setName(userBO.getName())
						.setNumber(userBO.getUserNo())
						.setType("user");
			response.getResponse_users().add(userResource);
		}
		
		for(ZoomPO zoom: zooms){
			ResourceVO meetingResource = new ResourceVO();
			meetingResource.setName(zoom.getName())
						   .setNumber(zoom.getCode())
						   .setType("meeting");
			response.getResponse_users().add(meetingResource);
		}

		return response;
	}
	
	/**
	 * Jv220呼叫用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午3:26:26
	 * @param String calledNo 被呼叫用户号码
	 * @return PassByVO 协议信息
	 */
	public PassByVO callUser(String calledNo) throws Exception{
		
		UserVO callUser = userQuery.current();
		UserVO calledUser = userQuery.findByUserno(calledNo);
		
		//TODO:调用拉人建会接口
		ZoomVO zoom = null;
		
		PassByVO passBy = zoomVo2PassByVO(zoom);
		passBy.setOperate("call_user")
			  .setLocal_user_no(callUser.getUserno())
			  .setRemote_user_no(calledNo);
		
		return passBy;
	}
	
	/**
	 * Jv220加入会议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午3:50:34
	 * @param String code 会议号码
	 * @return PassByVO 协议信息
	 */
	public PassByVO callMeeting(String code) throws Exception{
		
		UserVO callUser = userQuery.current();
		
		//调用加入会议接口
		ZoomVO zoom = zoomService.join(code, callUser.getNickname(), true, true, ZoomMemberType.JV220);
		
		PassByVO passBy = zoomVo2PassByVO(zoom);
		passBy.setOperate("call_meeting")
			  .setLocal_user_no(callUser.getUserno());
		
		return passBy;
	}
	
	/**
	 * jv220退会<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 上午9:03:57
	 * @param Long memberId 会议成员id
	 */
	public void exit(Long memberId) throws Exception{
		zoomService.exit(memberId);
	}
	
	/**
	 * jv220挂断<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 上午9:15:38
	 * @param String userno 用户号码
	 */
	public void bye(String userno) throws Exception{
		
		//TODO:根据用户号码查询所有zoomMembers
		List<ZoomMemberPO> members = null;
		for(ZoomMemberPO member: members){
			zoomService.exit(member.getId());
		}
		
	}
	
	/**
	 * jv220恢复<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 上午9:30:34
	 * @param String userno 用户号码
	 * @return List<PassByVO> 协议信息
	 */
	public List<PassByVO> resume(String userno) throws Exception{
		
		UserVO callUser = userQuery.current();
		
		List<PassByVO> passBys = new ArrayList<PassByVO>();
		
		//TODO：根据用户号码查询所在zoom信息
		List<ZoomVO> zooms = null;
		for(ZoomVO zoom: zooms){
			PassByVO passBy = zoomVo2PassByVO(zoom);
			passBy.setOperate("call_user")
				  .setLocal_user_no(callUser.getUserno());
			passBys.add(passBy);
		}
		
		return passBys;
	}
	
	/**
	 * zoom会议信息生成passby协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午3:21:08
	 * @param ZoomVO zoom zoom会议信息
	 * @return PassByVO 协议信息
	 */
	public PassByVO zoomVo2PassByVO(ZoomVO zoom) throws Exception{
		
		PassByVO passBy = new PassByVO();
		
		//自己编码
		ZoomMemberVO me = zoom.getMe();
		
		if(me.getJoin()){
			
			LocalPublishVO local = setLocal(me);
			
			//远端解码
			List<RemoteVO> remotes = new ArrayList<RemoteVO>();
			//主席
			int priority = 1;
			ZoomMemberVO chairman = zoom.getChairman();
			if(chairman.getShareScreen()){
				RemoteVO chairmanScreenRemote = setScreenRemote(chairman, priority, "open");
				remotes.add(chairmanScreenRemote);
				priority++;
			}
			RemoteVO chairmanRemote = setRemote(chairman, priority);
			remotes.add(chairmanRemote);
			priority++;
			
			//发言人
			List<ZoomMemberVO> spokesmans = zoom.getSpokesmen();
			for(ZoomMemberVO spokesman: spokesmans){
				if(spokesman.getShareScreen()){
					RemoteVO spokesmanScreenRemote = setScreenRemote(chairman, priority, "open");
					remotes.add(spokesmanScreenRemote);
					priority++;
				}
				RemoteVO spokesmanRemote = setRemote(spokesman, priority);
				remotes.add(spokesmanRemote);
				priority++;
			}
			
			//成员列表（包含主席和发言人） -- 不流调
//			List<ZoomMemberVO> members = zoom.getMembers();
//			for(ZoomMemberVO member: members){
//				RemoteVO memberRemote = setRemote(member, priority);
//				remotes.add(memberRemote);
//				priority++;
//			}
			
			//共享屏幕--不解析
			
			PassByParamVO param = new PassByParamVO().setLocal_publish(local)
												     .setRemote(new ArrayListWrapper<RemoteVO>().addAll(remotes).getList());
			
			passBy.setRemote_meeting_no(zoom.getCode())
				  .setLocal_user_identify(me.getId().toString())
			      .setParam(param);
			
		}
		
		return passBy;
	}
	
	/**
	 * 消息发送<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 上午10:26:10
	 * @param String code 会议号码
	 * @param JSONObject jsonObject 消息体
	 * @param List<ZoomMemberVO> targets 接收消息方
	 * @param String businessId 业务id
	 */
	public void push(String code, JSONObject jsonObject, List<ZoomMemberVO> targets, String businessId) throws Exception{
		
		List<PassByBO> passBys = new ArrayList<PassByBO>();
		DispatchBO dispatch = new DispatchBO();
		
		String operate = "";
		
		//处理各种各样的businessId
		if(businessId.equals("zoomJoin")){
			
			operate = "business_call_220_join";
			ZoomMemberVO changeMember = JSONObject.parseObject(JSON.toJSONString(jsonObject), ZoomMemberVO.class);
			passBys = generateCallMessage(code, changeMember, targets, operate, true, true);
			dispatch = generateStartUserDispatch(code, changeMember, targets, true, true, true);
			
		}else if(businessId.equals("zoomStart") || businessId.equals("zoomInvitation")){
			
			for(ZoomMemberVO target: targets){
				//调用zoomJoin
				ZoomVO zoomVO = zoomService.join(code, target.getRename(), target.getMyAudio(), target.getMyVideo(), ZoomMemberType.JV220);
			
				//接入
				PassByVO passBy = zoomVo2PassByVO(zoomVO);
				passBy.setOperate("business_call_220_start")
					  .setLocal_user_no(target.getUserno());
				PassByBO passByBO = new PassByBO().setLayer_id(target.getLayerId())
												  .setBundle_id(target.getBundleId())
												  .setPass_by_content(JSON.toJSONString(passBy));
				passBys.add(passByBO);
				
				//流调
				dispatch.getStartUserDispatch().addAll(generateUserDispatch(zoomVO));
			}
			
		}else if(businessId.equals("zoomStop")){
			
			operate = "business_stop_220";
			passBys = stopAllUsers(code, targets);
			dispatch = stopAllUsersDispatch(code, targets);
			
		}else if(businessId.equals("zoomExit") || businessId.equals("zoomKickOut")){
			
			ZoomMemberVO changeMember = JSONObject.parseObject(JSON.toJSONString(jsonObject), ZoomMemberVO.class);
			operate = "business_stop_220_channel";
			passBys = stopUser(code, changeMember, targets);
			dispatch = stopUserDispatch(code, changeMember, targets);
			
		}else if(businessId.equals("zoomOpenShareScreen")){
			
			ZoomMemberVO changeMember = JSONObject.parseObject(JSON.toJSONString(jsonObject), ZoomMemberVO.class);
			operate = "business_call_220_join";
			passBys = generateCallMessage(code, changeMember, targets, operate, true, false);
			dispatch = generateStartUserDispatch(code, changeMember, targets, false, false, true);
			
		}else if(businessId.equals("zoomCloseShareScreen")){
			
			ZoomMemberVO changeMember = JSONObject.parseObject(JSON.toJSONString(jsonObject), ZoomMemberVO.class);
			operate = "business_call_220_stop";
			passBys = stopScreenRemote(code, changeMember, targets);
			dispatch = generateStopUserDispatch(code, changeMember, targets, false, false, true);
			
		}else if(businessId.equals("zoomChangeChairman")){
			
			ZoomMemberVO oldMember = JSONObject.parseObject(jsonObject.getString("old"), ZoomMemberVO.class);
			ZoomMemberVO newMember = JSONObject.parseObject(jsonObject.getString("new"), ZoomMemberVO.class);
			
			operate = "business_stop_220_channel";
			passBys.addAll(stopUser(code, oldMember, targets));
			dispatch.getStopTaskDispatchByUserIdAndMeetingCode().addAll(generateStopUserDispatch(code, oldMember, targets, true, true, true).getStopTaskDispatchByUserIdAndMeetingCode());
			
			operate = "business_call_220_join";
			passBys.addAll(generateCallMessage(code, newMember, targets, operate, true, false));
			dispatch.getStartUserDispatch().addAll(generateStartUserDispatch(code, newMember, targets, false, false, true).getStartUserDispatch());
			
		}else if(businessId.equals("zoomAddSpokesman")){
			
			operate = "business_call_220_join";
			ZoomMemberVO spokesmen = JSONObject.parseObject(JSON.toJSONString(jsonObject), ZoomMemberVO.class);
			passBys = generateCallMessage(code, spokesmen, targets, operate, true, true);
			dispatch = generateStartUserDispatch(code, spokesmen, targets, true, true, true);
			
		}else if(businessId.equals("zoomRemoveSpokesman")){
			
			ZoomMemberVO spokesmen = JSONObject.parseObject(JSON.toJSONString(jsonObject), ZoomMemberVO.class);
			operate = "business_stop_220_channel";
			passBys = stopUser(code, spokesmen, targets);
			dispatch = stopUserDispatch(code, spokesmen, targets);
		}
		
		//调用流调
		tetrisDispatchService.dispatch(dispatch);
		
		//调用消息服务
		tetrisDispatchService.dispatch(passBys);
	}
	
	/**
	 * 停止一个屏幕共享<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 上午10:08:30
	 * @param String code 会议号码
	 * @param ZoomMemberVO changeMember 停止屏幕共享的成员
	 * @param List<ZoomMemberVO> targets 消息目标
	 * @return List<PassByBO> 协议信息
	 */
	public List<PassByBO> stopScreenRemote(
			String code, 
			ZoomMemberVO changeMember, 
			List<ZoomMemberVO> targets) throws Exception{
		
		List<PassByBO> passBys = new ArrayList<PassByBO>();	
		
		for(ZoomMemberVO target: targets){
			
			if(changeMember.getShareScreen()){
				StopVO remoteScreenStop = stopScreenRemote(changeMember, target, code);
				PassByBO remoteScreenPassBy = new PassByBO().setLayer_id(target.getLayerId())
												      		.setBundle_id(target.getBundleId())
												      		.setPass_by_content(JSON.toJSONString(remoteScreenStop));
				passBys.add(remoteScreenPassBy);
			}
		}
		
		return passBys;
	}
	
	/**
	 * 停止一个会议，停止所有用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 下午3:24:51
	 * @param String code 会议号码
	 * @param List<ZoomMemberVO> members 会议成员
	 * @return List<PassByBO> 协议信息
	 */
	public List<PassByBO> stopAllUsers(
			String code, 
			List<ZoomMemberVO> members) throws Exception{
		
		List<PassByBO> passBys = new ArrayList<PassByBO>();	
		
		for(ZoomMemberVO member: members){
			
			StopVO localStop = stopLocal(member, code);
			
			PassByBO localPassBy = new PassByBO().setLayer_id(member.getLayerId())
											     .setBundle_id(member.getBundleId())
											     .setPass_by_content(JSON.toJSONString(localStop));
			
			passBys.add(localPassBy);
		}
		
		return passBys;
	}
	
	/**
	 * 停止会议中成员的调度<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月20日 下午2:41:58
	 * @param String code 会议号码
	 * @param List<ZoomMemberVO> members 成员
	 * @return DispatchBO 调度协议
	 */
	public DispatchBO stopAllUsersDispatch(
			String code,
			List<ZoomMemberVO> members){
		
		DispatchBO dispatch = new DispatchBO();
		
		for(ZoomMemberVO member: members){
			
			StopTaskDispatchByUserIdAndMeetingCodeBO stop = new StopTaskDispatchByUserIdAndMeetingCodeBO();
			stop.setMeetingCode(code)
				.setUserId(member.getUserId());
			
			dispatch.getStopTaskDispatchByUserIdAndMeetingCode().add(stop);
		}
		
		return dispatch;
	}
	
	/**
	 * 停止一个用户--1.停止该用户所有
	 *         2.停止其他用户中该用户的通道<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 下午3:14:21
	 * @param String code 会议号码
	 * @param ZoomMemberVO changeMember 停止的成员
	 * @param List<ZoomMemberVO> targets 通知目标成员
	 * @return List<PassByBO> 协议信息
	 */
	public List<PassByBO> stopUser(
			String code, 
			ZoomMemberVO changeMember, 
			List<ZoomMemberVO> targets) throws Exception{
		
		List<PassByBO> passBys = new ArrayList<PassByBO>();	
		
		StopVO localStop = stopLocal(changeMember, code);
		
		PassByBO localPassBy = new PassByBO().setLayer_id(changeMember.getLayerId())
										     .setBundle_id(changeMember.getBundleId())
										     .setPass_by_content(JSON.toJSONString(localStop));
		
		passBys.add(localPassBy);
		
		for(ZoomMemberVO target: targets){
			
			if(changeMember.getMyVideo() || changeMember.getMyAudio()){
				StopVO remoteStop = stopRemote(changeMember, target, code);
				PassByBO remotePassBy = new PassByBO().setLayer_id(changeMember.getLayerId())
												      .setBundle_id(target.getBundleId())
												      .setPass_by_content(JSON.toJSONString(remoteStop));
				passBys.add(remotePassBy);
			}
			if(changeMember.getShareScreen()){
				StopVO remoteScreenStop = stopScreenRemote(changeMember, target, code);
				PassByBO remoteScreenPassBy = new PassByBO().setLayer_id(changeMember.getLayerId())
												      		.setBundle_id(target.getBundleId())
												      		.setPass_by_content(JSON.toJSONString(remoteScreenStop));
				passBys.add(remoteScreenPassBy);
			}
		}
		
		return passBys;
	}
	
	/**
	 * 停止一个用户调度--1.停止该用户所有
	 *         2.停止其他用户中该用户的通道<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月20日 下午4:50:57
	 * @param String code 会议号码
	 * @param ZoomMemberVO changeMember 停止的成员
	 * @param List<ZoomMemberVO> targets 通知目标成员
	 * @return DispatchBO 调度信息
	 */
	public DispatchBO stopUserDispatch(
			String code, 
			ZoomMemberVO changeMember, 
			List<ZoomMemberVO> targets) throws Exception{
		
		DispatchBO dispatch = new DispatchBO();
		
		StopTaskDispatchByUserIdAndMeetingCodeBO stop = new StopTaskDispatchByUserIdAndMeetingCodeBO();
		stop.setMeetingCode(code)
			.setUserId(changeMember.getUserId());
		
		dispatch.getStopTaskDispatchByUserIdAndMeetingCode().add(stop);
		
		dispatch.getStopTaskDispatchByUserIdAndMeetingCode().addAll(generateStopUserDispatch(code, changeMember, targets, true, true, true).getStopTaskDispatchByUserIdAndMeetingCode());
		
		return dispatch;
	}
	
	/**
	 * 生成call消息--包含open和stop<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 下午1:57:25
	 * @param String code 会议号码
	 * @param ZoomMemberVO changeMember 变化的成员（加入、踢出、改变视音频屏幕共享）
	 * @param List<ZoomMemberVO> targets 接受消息方
	 * @param String operate 消息标识
	 * @param boolean shareScreen 是否下发屏幕共享
	 * @param boolean videoAudio 是否下发音视频
	 * @return List<PassByBO> 协议信息
	 */
	public List<PassByBO> generateCallMessage(
			String code, 
			ZoomMemberVO changeMember, 
			List<ZoomMemberVO> targets, 
			String operate,
			boolean shareScreen,
			boolean videoAudio) throws Exception{
		
		List<PassByBO> passBys = new ArrayList<PassByBO>();
		
		List<RemoteVO> remotes = new ArrayList<RemoteVO>();
		
		int priority = 1;
		if(shareScreen){
			if(changeMember.getShareScreen()){
				RemoteVO changeMemberScreenRemote = setScreenRemote(changeMember, priority, "open");
				remotes.add(changeMemberScreenRemote);
				priority++;
			}
		}

		//因为remote中全量，所以这里定义的是videoAudio
		if(videoAudio){
			RemoteVO changeMemberRemote = setRemote(changeMember, priority);
			remotes.add(changeMemberRemote);
			priority++;
		}
		for(ZoomMemberVO target: targets){
			LocalPublishVO local = setLocal(target);
			PassByVO passByVO = new PassByVO().setOperate(operate)
											  .setLocal_user_no(target.getUserno())
											  .setLocal_user_identify(target.getId().toString())
											  .setRemote_meeting_no(code)
											  .setParam(new PassByParamVO().setLocal_publish(local)
													  					   .setRemote(new ArrayListWrapper<RemoteVO>().addAll(remotes).getList()));
			
			PassByBO passByBO = new PassByBO().setLayer_id(target.getLayerId())
											  .setBundle_id(target.getBundleId())
											  .setPass_by_content(JSON.toJSONString(passByVO));
			
			passBys.add(passByBO);
		}
		
		return passBys;
	}
	
	/**
	 * zoom成员信息生成本地编码协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午2:41:59
	 * @param ZoomMemberVO member
	 * @return LocalPublishVO 协议信息
	 */
	public LocalPublishVO setLocal(ZoomMemberVO member) throws Exception{
		LocalPublishVO local = new LocalPublishVO().setStatus("open")
												   .setBundle_id(member.getBundleId())
												   .setVideo1_name(member.getVideoChannelId())
												   .setAudio1_name(member.getAudioChannelId())
												   .setCodec_param(new CodecParamVO());
		return local;
	}
	
	/**
	 * 成员信息生成远端解码协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午2:21:45
	 * @param ZoomMemberVO member zoom成员信息
	 * @param int priority 优先级
	 * @return RemoteVO 协议信息
	 */
	public RemoteVO setRemote(ZoomMemberVO member, int priority) throws Exception{
		
		RemoteVO remote = new RemoteVO().setLocal_channel_no(member.getId().toString())
										.setPriority(priority);

		RemotePullVO video = new RemotePullVO().setBundle_id(member.getBundleId())
											   .setLayer_id(member.getLayerId())
										   	   .setChannel_id(member.getVideoChannelId());	
		if(!member.getMyVideo()){
			video.setStatus("stop");
		}else{
			video.setStatus("open");
		}
		
		RemotePullVO audio = new RemotePullVO().setBundle_id(member.getBundleId())			
								               .setLayer_id(member.getLayerId())
								               .setChannel_id(member.getAudioChannelId());
		if(!member.getMyAudio()){
			audio.setStatus("close");
		}else{
			audio.setStatus("open");
		}
		
		remote.setRemote_audio_pull(audio)
		      .setRemote_video_pull(video);
		
		return remote;
	}
	
	/**
	 * zoom成员信息生成屏幕共享解码协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午2:51:29
	 * @param ZoomMemberVO member zoom成员信息
	 * @param int priority 优先级
	 * @return RemoteVO 协议信息
	 */
	public RemoteVO setScreenRemote(ZoomMemberVO member, int priority, String status) throws Exception{
		
		RemoteVO remote = new RemoteVO().setLocal_channel_no(new StringBufferWrapper().append(member.getId()).append("-screen").toString())
										.setPriority(priority)
										.setRemote_video_pull(new RemotePullVO().setStatus(status)
																				.setLayer_id(member.getLayerId())
																				.setBundle_id(member.getBundleId())
																				.setChannel_id(member.getScreenVideoChannelId()))
										.setRemote_audio_pull(new RemotePullVO().setStatus(status)
												                                .setLayer_id(member.getLayerId())
												                                .setBundle_id(member.getBundleId())
												                                .setChannel_id(member.getScreenAudioChannelId()));

		return remote;
	}
	
	/**
	 * 停止本地220业务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月13日 下午2:35:12
	 * @param ZoomMemberVO member 成员信息
	 * @param String code 会议号码
	 * @return StopVO 协议信息
	 */
	public StopVO stopLocal(ZoomMemberVO member, String code) throws Exception{
		
		StopVO local = new StopVO().setOperate("business_stop_220")
				   				   .setLocal_user_no(member.getUserno())
				   				   .setRemote_meeting_no(code)
				   				   .setLocal_user_identify(member.getId().toString());
		return local;
	}
	
	/**
	 * 停止看远端<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 上午10:19:00
	 * @param ZoomMemberVO member 远端成员信息
	 * @param ZoomMemberVO target 接收消息成员信息
	 * @param String code 会议号码
	 * @return StopVO 协议信息
	 */
	public StopVO stopRemote(ZoomMemberVO member, ZoomMemberVO target, String code) throws Exception{
		
		StopVO remote = new StopVO().setOperate("business_stop_220_channel")
				   				    .setLocal_user_no(target.getUserno())
				   				    .setRemote_meeting_no(code)
				   				    .setLocal_user_identify(target.getId().toString())
				   				    .setRemote_channel_no(member.getId().toString());
		return remote;
	}
	
	/**
	 * 停止看屏幕共享<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 上午10:20:57
	 * @param ZoomMemberVO member 屏幕共享成员信息
	 * @param ZoomMemberVO target 接收消息成员信息
	 * @param String code 会议号码
	 * @return StopVO 协议信息
	 */
	public StopVO stopScreenRemote(ZoomMemberVO member, ZoomMemberVO target, String code) throws Exception{
		
		StopVO screenRemote = new StopVO().setOperate("business_stop_220_channel")
				   				    	  .setLocal_user_no(target.getUserno())
				   				    	  .setRemote_meeting_no(code)
				   				    	  .setLocal_user_identify(target.getId().toString())
				   				    	  .setRemote_channel_no(new StringBufferWrapper().append(member.getId()).append("-screen").toString());
		return screenRemote;
	}
	
	/**
	 * 生成开始转发调度协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 上午9:59:34
	 * @param String code 会议号码
	 * @param ZoomMemberVO changeMember 入会的成员
	 * @param List<ZoomMemberVO> targets 目的成员列表
	 * @param boolean video 是否控制视频
	 * @param boolean audio 是否控制音频
	 * @param boolean screen 是否控制屏幕共享
	 * @return DispatchBO 调度协议
	 */
	public DispatchBO generateStartUserDispatch(
			String code,
			ZoomMemberVO changeMember, 
			List<ZoomMemberVO> targets,
			boolean video,
			boolean audio,
			boolean screen) throws Exception{
		
		DispatchBO dispatch = new DispatchBO();
		List<StartUserDispatchBO> userDispatches = new ArrayList<StartUserDispatchBO>();
		
		for(ZoomMemberVO member: targets){
			userDispatches.addAll(generateUserDispatch(code, changeMember, member, true, true, true));
		}
		
		dispatch.setStartUserDispatch(new ArrayListWrapper<StartUserDispatchBO>().addAll(userDispatches).getList());
		
		return dispatch;
	}
	
	/**
	 * 生成停止转发调度协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 上午11:29:00
	 * @param String code 会议号码
	 * @param ZoomMemberVO changeMember 停止的成员
	 * @param List<ZoomMemberVO> targets 所有目的
	 * @param boolean video 是否控制视频
	 * @param boolean video 是否控制音频
	 * @param boolean video 是否控制屏幕共享
	 * @return DispatchBO 调度协议
	 */
	public DispatchBO generateStopUserDispatch(
			String code,
			ZoomMemberVO changeMember, 
			List<ZoomMemberVO> targets,
			boolean video,
			boolean audio,
			boolean screen) throws Exception{
		
		DispatchBO dispatch = new DispatchBO();
		List<StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO> userDispatches = new ArrayList<StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO>();

		for(ZoomMemberVO target: targets){
			if(video){
				StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO stopVideo = new StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO().setUserId(target.getUserId())
										 .setSourceBundleId(changeMember.getBundleId())
										 .setSourceChannelId(changeMember.getVideoChannelId());
				
				userDispatches.add(stopVideo);
			}
			
			if(audio){
				StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO stopAudio = new StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO().setUserId(target.getUserId())
										 .setSourceBundleId(changeMember.getBundleId())
										 .setSourceChannelId(changeMember.getAudioChannelId());
				
				userDispatches.add(stopAudio);
			}
			
			if(screen){
				StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO stopScreenVideo = new StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO().setUserId(target.getUserId())
												.setSourceBundleId(changeMember.getBundleId())
												.setSourceChannelId(changeMember.getScreenVideoChannelId());
				
				userDispatches.add(stopScreenVideo);
				
				StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO stopScreenAudio = new StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO().setUserId(target.getUserId())
										 	   .setSourceBundleId(changeMember.getBundleId())
											   .setSourceChannelId(changeMember.getScreenAudioChannelId());
				
				userDispatches.add(stopScreenAudio);
			}
		}
		
		dispatch.getStopTaskDispatchByUserIdAndMeetingCodeAndSource().addAll(userDispatches);
		
		return dispatch;
	}
	
	/**
	 * 根据ZoomVO生成开始调度协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月20日 上午11:38:16
	 * @param ZoomVO zoom 成员会议信息
	 * @return List<StartUserDispatchBO> 开始调度协议
	 */
	public List<StartUserDispatchBO> generateUserDispatch(ZoomVO zoom) throws Exception{
		
		List<StartUserDispatchBO> userDispatches = new ArrayList<StartUserDispatchBO>();
		
		ZoomMemberVO me = zoom.getMe();
		
		ZoomMemberVO chairman = zoom.getChairman();
		
		userDispatches.addAll(generateUserDispatch(zoom.getCode(), chairman, me, true, true, true));
		
		List<ZoomMemberVO> spokes = zoom.getSpokesmen();
		for(ZoomMemberVO spoke: spokes){
			userDispatches.addAll(generateUserDispatch(zoom.getCode(), spoke, me, true, true, true));
		}
		
		return userDispatches;
		
	}
	
	/**
	 * 生成用户调度协议--根据源和目的<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月20日 上午11:27:20
	 * @param String code 会议号码
	 * @param ZoomMemberVO source 源
	 * @param ZoomMemberVO dst 目的
	 * @param boolean video 是否控制视频
	 * @param boolean audio 是否控制音频
	 * @param boolean screen 是否控制屏幕共享
	 * @return List<StartUserDispatchBO> 调度协议
	 */
	public List<StartUserDispatchBO> generateUserDispatch(
			String code,
			ZoomMemberVO source,
			ZoomMemberVO dst,
			boolean video,
			boolean audio,
			boolean screen) throws Exception{
		
		List<StartUserDispatchBO> userDispatches = new ArrayList<StartUserDispatchBO>();
		
		if(video){
			if(source.getMyVideo()){
				SourceParamBO sourceParam = new SourceParamBO().setLayerId(source.getLayerId())
															   .setBundleId(source.getBundleId())
															   .setChannelId(source.getVideoChannelId());
				ChannelBO channel = new ChannelBO().setSource_param(sourceParam);
				StartUserDispatchBO userDispatch = new StartUserDispatchBO().setUserId(dst.getUserId())
																			.setMeetingCode(code)
																			.setChannels(new ArrayListWrapper<ChannelBO>().add(channel).getList());
				userDispatches.add(userDispatch);
			}
		}
		
		if(audio){
			if(source.getMyAudio()){
				SourceParamBO sourceParam = new SourceParamBO().setLayerId(source.getLayerId())
															   .setBundleId(source.getBundleId())
															   .setChannelId(source.getAudioChannelId());
				ChannelBO channel = new ChannelBO().setSource_param(sourceParam);
				StartUserDispatchBO userDispatch = new StartUserDispatchBO().setUserId(dst.getUserId())
																			.setMeetingCode(code)
																			.setChannels(new ArrayListWrapper<ChannelBO>().add(channel).getList());
				userDispatches.add(userDispatch);
			}
		}

		if(screen){
			if(source.getShareScreen()){
				SourceParamBO videoSourceParam = new SourceParamBO().setLayerId(source.getLayerId())
																    .setBundleId(source.getBundleId())
																    .setChannelId(source.getScreenVideoChannelId());
				ChannelBO videoChannel = new ChannelBO().setSource_param(videoSourceParam);
				StartUserDispatchBO videoUserDispatch = new StartUserDispatchBO().setUserId(dst.getUserId())
																				 .setMeetingCode(code)
																				 .setChannels(new ArrayListWrapper<ChannelBO>().add(videoChannel).getList());
				userDispatches.add(videoUserDispatch);
				
				SourceParamBO audioSourceParam = new SourceParamBO().setLayerId(source.getLayerId())
																    .setBundleId(source.getBundleId())
																    .setChannelId(source.getScreenAudioChannelId());
				ChannelBO audioChannel = new ChannelBO().setSource_param(audioSourceParam);
				StartUserDispatchBO audioUserDispatch = new StartUserDispatchBO().setUserId(dst.getUserId())
																				 .setMeetingCode(code)
																				 .setChannels(new ArrayListWrapper<ChannelBO>().add(audioChannel).getList());
				userDispatches.add(audioUserDispatch);
			}
		}

		return userDispatches;
	}
	
}
