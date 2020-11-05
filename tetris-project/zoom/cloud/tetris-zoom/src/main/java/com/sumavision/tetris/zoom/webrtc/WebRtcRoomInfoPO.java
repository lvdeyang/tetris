package com.sumavision.tetris.zoom.webrtc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * webrtc房间信息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月17日 下午2:32:02
 */
@Entity
@Table(name = "TETRIS_ZOOM_WEBRTC_ROOM_INFO")
public class WebRtcRoomInfoPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

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

	@Column(name = "ZOOM_ID")
	public Long getZoomId() {
		return zoomId;
	}

	public void setZoomId(Long zoomId) {
		this.zoomId = zoomId;
	}

	@Column(name = "WEB_RTC_ID")
	public Long getWebRtcId() {
		return webRtcId;
	}

	public void setWebRtcId(Long webRtcId) {
		this.webRtcId = webRtcId;
	}

	@Column(name = "IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "WEB_RTC_LAYER_ID")
	public String getWebRtcLayerId() {
		return webRtcLayerId;
	}

	public void setWebRtcLayerId(String webRtcLayerId) {
		this.webRtcLayerId = webRtcLayerId;
	}

	@Column(name = "WEB_RTC_LAYER_HTTP_PORT")
	public String getWebRtcLayerHttpPort() {
		return webRtcLayerHttpPort;
	}

	public void setWebRtcLayerHttpPort(String webRtcLayerHttpPort) {
		this.webRtcLayerHttpPort = webRtcLayerHttpPort;
	}

	@Column(name = "WEB_RTC_HTTP_PORT")
	public String getWebRtcHttpPort() {
		return webRtcHttpPort;
	}

	public void setWebRtcHttpPort(String webRtcHttpPort) {
		this.webRtcHttpPort = webRtcHttpPort;
	}

	@Column(name = "WEB_RTC_WEB_SOCKET_PORT")
	public String getWebRtcWebSocketPort() {
		return webRtcWebSocketPort;
	}

	public void setWebRtcWebSocketPort(String webRtcWebSocketPort) {
		this.webRtcWebSocketPort = webRtcWebSocketPort;
	}

	@Column(name = "ROOM_ID")
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
}
