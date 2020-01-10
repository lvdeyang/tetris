package com.sumavision.tetris.p2p.room;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 点对点通话controller<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月10日 下午5:17:51
 */
@Controller
@RequestMapping(value = "/api/android/p2p")
public class RoomController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private RoomService roomService;

	/**
	 * 获取通讯录<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 上午10:11:21
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/peer/list")
	public Object getPeerList(HttpServletRequest request) throws Exception{
		
		//获取当前用户
		UserVO user = userQuery.current();
		
		List<RoomPeerVO> peers = new ArrayList<RoomPeerVO>();
		
		//获取所有用户
		List<UserVO> users = userQuery.listByCompanyIdWithExceptAndClassify(Long.valueOf(user.getGroupId()), "安卓采集终端", new ArrayListWrapper<Long>().add(user.getId()).getList(), UserClassify.COMPANY);
		for(UserVO userVO: users){
			RoomPeerVO peer = new RoomPeerVO();
			peer.setId(userVO.getId())
				.setName(userVO.getNickname())
				.setIsOnline(userVO.getStatus() == null || userVO.getStatus().equals("OFFLINE")? false: true);
			peers.add(peer);
		}
		
		return peers;
	}
	
	/**
	 * 发起点对点通话<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 上午10:44:11
	 * @param String roomId 房间号
	 * @param Long otherId 被呼叫人id
	 * @param String otherName 被呼叫人名
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start")
	public Object start(
			String roomId,
			Long otherId,
			String otherName,
			HttpServletRequest request) throws Exception{
		
		//获取当前用户
		UserVO user = userQuery.current();
		
		roomService.start(roomId, user.getId(), user.getNickname(), otherId, otherName);
		
		return null;
	}
	
	/**
	 * 点对点通话接听<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午3:08:52
	 * @param String roomId 房间号
	 * @param Long userId 自身用户id
	 * @param Long otherId 对方用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/answer")
	public Object answer(
			String roomId,
			Long otherId,
			HttpServletRequest request) throws Exception{
		
		//获取当前用户
		UserVO user = userQuery.current();
		
		roomService.answer(roomId, otherId, user.getId());
		
		return null;
	}
	
	/**
	 * 点对点通话拒绝<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午3:08:52
	 * @param String roomId 房间号
	 * @param Long userId 自身用户id
	 * @param Long otherId 对方用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/hangUp")
	public Object hangUp(
			String roomId,
			Long otherId,
			HttpServletRequest request) throws Exception{
		
		//获取当前用户
		UserVO user = userQuery.current();
		
		roomService.hangUp(roomId, otherId, user.getId());
		
		return null;
	}
	
	/**
	 * 获取websocket连接url<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月12日 上午11:17:58
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/websocket/info")
	public Object getWebsocketInfo(HttpServletRequest request) throws Exception{
		
		//获取当前用户
		UserVO user = userQuery.current();
		
		String ws = roomService.getWebSocketInfo();
		
		return ws;
	}
	
}
