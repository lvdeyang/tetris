package com.sumavision.tetris.zoom.webrtc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebRtcRoomInfoQuery {

	@Autowired
	private WebRtcRoomInfoDAO webRtcRoomInfoDao;
	
	/**
	 * 查询会议的webrtc信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月18日 下午4:18:44
	 * @param Long zoomId 会议室id
	 * @return WebRtcVO webRtc信息
	 */
	public WebRtcVO findZoomWebRtc(Long zoomId){
		List<WebRtcRoomInfoPO> webRtcRoomInfos = webRtcRoomInfoDao.findByZoomId(zoomId);
		if(webRtcRoomInfos==null || webRtcRoomInfos.size()<=0) return null;
		return new WebRtcRoomInfoVO().set(webRtcRoomInfos.get(0)).transform();
	}
	
}
