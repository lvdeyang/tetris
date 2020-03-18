package com.sumavision.tetris.zoom.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.zoom.ZoomMemberType;
import com.sumavision.tetris.zoom.ZoomMode;
import com.sumavision.tetris.zoom.ZoomService;

@Controller
@RequestMapping(value = "/api/zoom/android")
public class ApiZoomAndroidController {

	@Autowired
	private ZoomService zoomService;
	
	/**
	 * 创建会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 上午11:34:43
	 * @param String name 会议名称
	 * @param String mode DISCUSSION_MODE：讨论模式|CONVERSION_MODE：大会模式
	 * @param String rename 入会名称
	 * @param Boolean myAudio 是否开启音频
	 * @param Boolean myVideo 是否开启视频
	 * @return ZoomVO 会议信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/create")
	public Object create(
			String name,
			String mode,
			String rename,
			Boolean myAudio,
			Boolean myVideo,
			HttpServletRequest request) throws Exception{
		
		return zoomService.create(name, ZoomMode.valueOf(mode), rename, myAudio, myVideo, ZoomMemberType.TERMINl);
	}
	
	/**
	 * 加入会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午3:09:24
	 * @param String code 会议号码
	 * @param String rename 入会名称
	 * @param Boolean myAudio 是否开启音频
	 * @param Boolean myVideo 是否开启视频
	 * @return ZoomVO 会议信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/join")
	public Object join(
			String code,
			String rename,
			Boolean myAudio,
			Boolean myVideo,
			HttpServletRequest request) throws Exception{
		
		return zoomService.join(code, rename, myAudio, myVideo);
	}
	
	/**
	 * 开始会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 上午11:52:45
	 * @param String code 会议号码
	 * @param String rename 入会名称
	 * @param Boolean myAudio 是否开启音频
	 * @param Boolean myVideo 是否开启视频
	 * @return ZoomVO 会议信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start")
	public Object start(
			String code,
			String rename,
			Boolean myAudio,
			Boolean myVideo,
			HttpServletRequest request) throws Exception{
		
		return zoomService.start(code, rename, myAudio, myVideo);
	}
	
	/**
	 * 停止会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午2:24:11
	 * @param String code 会议号码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(String code, HttpServletRequest request) throws Exception{
	
		zoomService.stop(code);
		return null;
	}
	
	/**
	 * 退出会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午3:52:52
	 * @param Long zoomMemberId 会议成员id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/exit")
	public Object exit(
			Long zoomMemberId,
			HttpServletRequest request) throws Exception{
		
		zoomService.exit(zoomMemberId);
		return null;
	}
	
	/**
	 * 移交主席权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午9:32:51
	 * @param Long targetZoomMemberId 目标主席成员id
	 * @return ZoomMemberVO 新主席成员信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/chairman")
	public Object changeChairman(
			Long targetZoomMemberId,
			HttpServletRequest request) throws Exception{
		
		return zoomService.changeChairman(targetZoomMemberId);
	}
	
	/**
	 * 添加发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午10:44:58
	 * @param Long targetZoomMemberId 目标发言人成员id
	 * @return ZoomMemberVO 发言人成员信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/spokesman")
	public Object addSpokesman(
			Long targetZoomMemberId,
			HttpServletRequest request) throws Exception{
		
		return zoomService.addSpokesman(targetZoomMemberId);
	}
	
	/**
	 * 删除发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午10:54:16
	 * @param Long targetZoomMemberId 目标发言人成员id
	 * @return ZoomMemberVO 发言人
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/spokesman")
	public Object removeSpokesman(
			Long targetZoomMemberId,
			HttpServletRequest request) throws Exception{
		
		return zoomService.removeSpokesman(targetZoomMemberId);
	}
	
	/**
	 * 邀请入会<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:23:01
	 * @param String code 会议号码
	 * @param Long userId 目标用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/invitation")
	public Object invitation(
			String code, 
			Long userId,
			HttpServletRequest request) throws Exception{
		
		zoomService.invitation(code, userId);
		return null;
	}
	
	/**
	 * 踢出成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:35:12
	 * @param Long targetZoomMemberId 目标会议成员
	 * @return ZoomMemberVO 被踢出的会议成员
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/kick/out")
	public Object kickOut(
			Long targetZoomMemberId,
			HttpServletRequest request) throws Exception{
		
		return zoomService.kickOut(targetZoomMemberId);
	}
	
	/**
	 * 打开共享屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午3:24:07
	 * @param Long myZoomMemberId 当前会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/open/share/screen")
	public Object openShareScreen(Long myZoomMemberId) throws Exception{
		
		return zoomService.openShareScreen(myZoomMemberId);
	}
	
	/**
	 * 关闭共享屏幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午3:24:07
	 * @param Long myZoomMemberId 当前会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/close/share/screen")
	public Object closeShareScreen(Long myZoomMemberId) throws Exception{
		
		return zoomService.closeShareScreen(myZoomMemberId);
	}
	
	/**
	 * 删除会议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午4:50:53
	 * @param String code 会议号码
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(String code) throws Exception{
		
		zoomService.remove(code);
		return null;
	}
	
}
