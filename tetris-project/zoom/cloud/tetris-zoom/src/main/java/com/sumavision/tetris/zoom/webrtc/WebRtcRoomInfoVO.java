package com.sumavision.tetris.zoom.webrtc;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class WebRtcRoomInfoVO extends AbstractBaseVO<WebRtcRoomInfoVO, WebRtcRoomInfoPO>{

	/** 隶属会议室 */
	private Long zoomId;
	
	/** webrtc模块id */
	private Long webRtcId;
	
	/** webrtc模块ip */
	private String ip;
	
	/** webrtc接入层id */
	private String webRtcLayerId;
	
	/** webrtc接入层http端口 */
	private String webRtcLayerHttpPort;
	
	/** 终端访问 webrtc http端口 */
	private String webRtcHttpPort;
	
	/** 终端访问webrtc websocket端口 */
	private String webRtcWebSocketPort;
	
	/** 房间号码 */
	private String roomId;

	public Long getZoomId() {
		return zoomId;
	}

	public WebRtcRoomInfoVO setZoomId(Long zoomId) {
		this.zoomId = zoomId;
		return this;
	}

	public Long getWebRtcId() {
		return webRtcId;
	}

	public WebRtcRoomInfoVO setWebRtcId(Long webRtcId) {
		this.webRtcId = webRtcId;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public WebRtcRoomInfoVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getWebRtcLayerId() {
		return webRtcLayerId;
	}

	public WebRtcRoomInfoVO setWebRtcLayerId(String webRtcLayerId) {
		this.webRtcLayerId = webRtcLayerId;
		return this;
	}

	public String getWebRtcLayerHttpPort() {
		return webRtcLayerHttpPort;
	}

	public WebRtcRoomInfoVO setWebRtcLayerHttpPort(String webRtcLayerHttpPort) {
		this.webRtcLayerHttpPort = webRtcLayerHttpPort;
		return this;
	}

	public String getWebRtcHttpPort() {
		return webRtcHttpPort;
	}

	public WebRtcRoomInfoVO setWebRtcHttpPort(String webRtcHttpPort) {
		this.webRtcHttpPort = webRtcHttpPort;
		return this;
	}

	public String getWebRtcWebSocketPort() {
		return webRtcWebSocketPort;
	}

	public WebRtcRoomInfoVO setWebRtcWebSocketPort(String webRtcWebSocketPort) {
		this.webRtcWebSocketPort = webRtcWebSocketPort;
		return this;
	}

	public String getRoomId() {
		return roomId;
	}

	public WebRtcRoomInfoVO setRoomId(String roomId) {
		this.roomId = roomId;
		return this;
	}

	@Override
	public WebRtcRoomInfoVO set(WebRtcRoomInfoPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setZoomId(entity.getZoomId())
			.setWebRtcId(entity.getWebRtcId())
			.setIp(entity.getIp())
			.setWebRtcLayerId(entity.getWebRtcLayerId())
			.setWebRtcLayerHttpPort(entity.getWebRtcLayerHttpPort())
			.setWebRtcHttpPort(entity.getWebRtcHttpPort())
			.setWebRtcWebSocketPort(entity.getWebRtcWebSocketPort())
			.setRoomId(entity.getRoomId());
		return this;
	}
	
	/**
	 * 数据转换<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月17日 下午2:48:51
	 * @return WebRtcVO 转换数据
	 */
	public WebRtcVO transform(){
		return new WebRtcVO().setIp(this.getIp())
							 .setHttpPort(this.getWebRtcHttpPort())
							 .setWebsocketPort(this.getWebRtcWebSocketPort());
	}
	
}
