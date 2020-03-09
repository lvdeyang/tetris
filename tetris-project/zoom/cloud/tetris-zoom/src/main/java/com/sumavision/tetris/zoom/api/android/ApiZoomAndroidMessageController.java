package com.sumavision.tetris.zoom.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.zoom.ZoomMessageService;

@Controller
@RequestMapping(value = "/api/zoom/android/message")
public class ApiZoomAndroidMessageController {

	@Autowired
	private ZoomMessageService zoomMessageService;
	
	/**
	 * 发会议私信<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午1:57:31
	 * @param Long myZoomMemberId 消息发送者成员id
	 * @param Long targetZoomMemberId 消息发送目标成员id
	 * @param String message 消息内容
	 * @return ZoomMessageVO 消息数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/private/letter")
	public Object privateLetter(
			Long myZoomMemberId,
			Long targetZoomMemberId,
			String message,
			HttpServletRequest request) throws Exception{
		
		return zoomMessageService.privateLetter(myZoomMemberId, targetZoomMemberId, message);
	}
	
	/**
	 * 群发会议私信<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午2:03:05
	 * @param Long myZoomMemberId 消息发送者成员id
	 * @param JSONString targetZoomMemberIds 消息发送目标成员id列表
	 * @param message 消息内容
	 * @return List<ZoomMessageVO> 消息数据列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/private/letters")
	public Object privateLetters(
			Long myZoomMemberId,
			String targetZoomMemberIds,
			String message,
			HttpServletRequest request) throws Exception{
		
		return zoomMessageService.privateLetters(myZoomMemberId, JSON.parseArray(targetZoomMemberIds, Long.class), message);
	}
	
	/**
	 * 会议广播消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午2:35:14
	 * @param Long myZoomMemberId 消息发送者成员id
	 * @param String message 消息内容
	 * @return ZoomMessageVO 消息数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast")
	public Object broadcast(
			Long myZoomMemberId,
			String message,
			HttpServletRequest request) throws Exception{
		
		return zoomMessageService.broadcast(myZoomMemberId, message);
	}
	
}
