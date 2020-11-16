package com.sumavision.tetris.user.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.alarm.bo.OprlogParamBO;
import com.sumavision.tetris.alarm.bo.OprlogParamBO.EOprlogType;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeign;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;
import com.sumavision.tetris.user.UserService;
import com.sumavision.tetris.websocket.core.event.WebsocketSessionClosedEvent;

/**
 * websocket用户离线事件<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月23日 上午10:50:41
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WebsocketSessionClosedUserListener implements ApplicationListener<WebsocketSessionClosedEvent>{

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private AlarmFeign alarmFeign;
	
	@Override
	public void onApplicationEvent(WebsocketSessionClosedEvent event) {
		
		
		try{
			UserPO user = userDAO.findOne(event.getUserId());
			Date day=new Date();    
			SimpleDateFormat offLineDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			OprlogParamBO log = new OprlogParamBO();
			log.setSourceService("tetris-user");
			log.setUserName(user.getNickname());
			log.setOprName("用户下线");
			log.setSourceServiceIP("");
			log.setOprDetail(new StringBufferWrapper().append("用户“")
													  .append(user.getNickname())
													  .append("”下线（")
													  .append(offLineDateFormat.format(day))
													  .append("）")
													  .toString());
			log.setOprlogType(EOprlogType.USER_OFFLINE);
			alarmFeign.sendOprlog(log);
		}catch(Exception e){
			System.out.println("用户下线日志存储失败！");
		}
		
		try {
			userService.userOffline(event.getUserId(), TerminalType.QT_ZK);
			userService.userOffline(event.getUserId(), TerminalType.ZOOM_ANDROID);
			userService.userOffline(event.getUserId(), TerminalType.ZOOM_QT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}