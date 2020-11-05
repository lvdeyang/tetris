package com.sumavision.tetris.zoom.api.android;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.zoom.ZoomMemberDAO;
import com.sumavision.tetris.zoom.ZoomMemberType;
import com.sumavision.tetris.zoom.ZoomMode;
import com.sumavision.tetris.zoom.ZoomQuery;
import com.sumavision.tetris.zoom.ZoomSecretLevel;
import com.sumavision.tetris.zoom.ZoomService;

@Controller
@RequestMapping(value = "/api/zoom/android")
public class ApiZoomAndroidController {

	@Autowired
	private ZoomService zoomService;
	
	@Autowired
	private ZoomQuery zoomQuery;
	
	@Autowired
	private ZoomMemberDAO zoomMemberDao;
	
	/**
	 * 查询用户信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午3:26:37
	 * @return user UserVO 用户基本信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/user/info")
	public Object queryUserInfo(HttpServletRequest request) throws Exception{
		return zoomQuery.queryUserInfo();
	}
	
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
		
		return zoomService.create(name, ZoomMode.valueOf(mode), rename, myAudio, myVideo, ZoomSecretLevel.PUBLIC, ZoomMemberType.TERMINl);
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
		
		return zoomService.join(code, rename, myAudio, myVideo, ZoomMemberType.TERMINl);
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
		
		return zoomService.start(code, rename, myAudio, myVideo, ZoomMemberType.TERMINl);
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
		
		Map<String, Object> result = zoomService.exit(zoomMemberId);
		if(result != null){
			List<Long> memberIds = zoomMemberDao.findIdByZoomIdAndIdNotIn(Long.valueOf(result.get("zoomId").toString()), new ArrayListWrapper<Long>().add(zoomMemberId).getList(), true);
			if(memberIds==null || memberIds.size()<=0){
				zoomService.stop(result.get("zoomCode").toString());
			}else{
				zoomService.changeChairman(memberIds.get(0));
				zoomService.exit(zoomMemberId);
			}
		}
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
	 * @param JSONString targetZoomMemberIds 目标发言人成员id列表
	 * @return List<ZoomMemberVO> 发言人成员信息列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/spokesman")
	public Object addSpokesman(
			String targetZoomMemberIds,
			HttpServletRequest request) throws Exception{
		
		return zoomService.addSpokesman(JSON.parseArray(targetZoomMemberIds, Long.class));
	}
	
	/**
	 * 删除发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午10:54:16
	 * @param JSONString targetZoomMemberIds 目标发言人成员id列表
	 * @return List<ZoomMemberVO> 发言人列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/spokesman")
	public Object removeSpokesman(
			String targetZoomMemberIds,
			HttpServletRequest request) throws Exception{
		
		return zoomService.removeSpokesman(JSON.parseArray(targetZoomMemberIds, Long.class));
	}
	
	/**
	 * 邀请入会<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:23:01
	 * @param String code 会议号码
	 * @param JSONString userIds 目标用户id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/invitation")
	public Object invitation(
			String code, 
			String userIds,
			HttpServletRequest request) throws Exception{
		
		zoomService.invitation(code, JSON.parseArray(userIds, Long.class));
		return null;
	}
	
	/**
	 * 踢出成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:35:12
	 * @param JSONString targetZoomMemberIds 目标会议成员id列表
	 * @return List<ZoomMemberVO> 被踢出的会议成员列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/kick/out")
	public Object kickOut(
			String targetZoomMemberIds,
			HttpServletRequest request) throws Exception{
		
		return zoomService.kickOut(JSON.parseArray(targetZoomMemberIds, Long.class));
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
	 * 成员开启视频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午2:23:12
	 * @param Long myZoomMemberId 会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/open/video")
	public Object openVideo(Long myZoomMemberId) throws Exception{
		
		return zoomService.openVideo(myZoomMemberId);
	}
	
	/**
	 * 成员关闭视频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午2:29:12
	 * @param Long myZoomMemberId 会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/close/video")
	public Object closeVideo(Long myZoomMemberId) throws Exception{
		
		return zoomService.closeVideo(myZoomMemberId);
	}
	
	/**
	 * 成员开启音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午2:30:53
	 * @param Long myZoomMemberId 会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/open/audio")
	public Object openAudio(Long myZoomMemberId) throws Exception{
		
		return zoomService.openAudio(myZoomMemberId);
	}
	
	/**
	 * 成员关闭音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午2:30:53
	 * @param Long myZoomMemberId 会议成员id
	 * @return ZoomMemberVO 会议成员
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/close/audio")
	public Object closeAudio(Long myZoomMemberId) throws Exception{
		
		return zoomService.closeAudio(myZoomMemberId);
	}
	
	/**
	 * 会议成员重命名<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月3日 上午10:29:07
	 * @param Long myZoomMemberId 会议成员id
	 * @param String rename 重命名
	 * @return ZoomMemberVO 重命名后的成员
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/rename")
	public Object rename(Long myZoomMemberId, String rename) throws Exception{
		
		return zoomService.rename(myZoomMemberId, rename);
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
