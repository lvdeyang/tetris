package com.sumavision.tetris.sts.task;

import java.io.Serializable;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.sts.common.CommonConstants.ProtoType;
import com.sumavision.tetris.sts.common.SpringBeanFactory;
import com.sumavision.tetris.sts.task.outputBO.DashBO;
import com.sumavision.tetris.sts.task.outputBO.HlsBO;
import com.sumavision.tetris.sts.task.outputBO.RtmpFlvBO;
import com.sumavision.tetris.sts.task.outputBO.TsSrtBO;
import com.sumavision.tetris.sts.task.tasklink.OutputPO;
import com.sumavision.tetris.sts.task.tasklink.StreamMediaDao;
import com.sumavision.tetris.sts.task.tasklink.StreamMediaPO;

public class OutputBO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4145059914635153270L;

	private Long id;
	
	private String pubName;
	
	/**
	 * 输出类型
	 */
	private ProtoType type;
	
	private Integer dstType;
	
	/**
	 * 下面这两个参数分别是video-pid,audio-pid的输出选择，例
	 * vidselect : 1,3
	 * audselect : 1,4
	 * 表示视频输出选择十路中的1和3，音频输出选择十路中的1和4
	 */
	private String vidSelect;
	private String audSelect;
	private String subSelect;
	
	private Long netGroupId;
	
	private String ip;
	
	private Integer port;
	
	/**
	 * 流媒体id，发布类型输出才有
	 */
	private Long streamMediaId;
	
	/**
	 * output页签中的子页签参数，根据type决定类型，分别对应bo.taks.output包中的bo
	 */
	private String jsonOutParam;

	public boolean paramCompare(Object o) {
		if (this == o) return true;
		if (!(o instanceof OutputBO)) return false;
		OutputBO outputBO = (OutputBO) o;
		return Objects.equals(pubName, outputBO.pubName) &&
				type == outputBO.type &&
				Objects.equals(dstType, outputBO.dstType) &&
				Objects.equals(vidSelect, outputBO.vidSelect) &&
				Objects.equals(audSelect, outputBO.audSelect) &&
				Objects.equals(subSelect, outputBO.subSelect) &&
				Objects.equals(netGroupId, outputBO.netGroupId) &&
				Objects.equals(ip, outputBO.ip) &&
				Objects.equals(port, outputBO.port) &&
				Objects.equals(streamMediaId, outputBO.streamMediaId) &&
				Objects.equals(jsonOutParam, outputBO.jsonOutParam);
	}

	public OutputBO(){

	}

	public OutputBO(OutputPO outputPO){
		setPubName(outputPO.getPubName());
		setType(outputPO.getType());
		setDstType(outputPO.getOutputBO().getDstType());
		setVidSelect(outputPO.getVidSelect());
		setAudSelect(outputPO.getAudSelect());
		setSubSelect(outputPO.getSubSelect());
		setNetGroupId(outputPO.getNetGroupId());
		setIp(outputPO.getIp());
		setPort(outputPO.getPort());
		setStreamMediaId(outputPO.getStreamMediaId());
		setJsonOutParam(outputPO.getJsonParam());
	}



	public String getOutputUrl(){
		return getOutputUrl("");
	}
	
	public String getOutputUrl(String pubIp){
		StreamMediaDao streamMediaDao = SpringBeanFactory.getBean(StreamMediaDao.class);
		switch(type){
		case TSUDP:
		case TSUDPPASSBY:
			return "udp://@" + ip + ":" + port;
		case HTTPTS:
			return "http://" + ip + ":" + port +"/" + pubName;
		case TSRTP:
			return "rtp://@" + ip + ":" + port;
		case TSSRT:
		case TSSRTPASSBY:
			TsSrtBO tsSrtBO = JSONObject.parseObject(jsonOutParam, TsSrtBO.class);
			if(tsSrtBO.getModeSelect().equals("listener")){
				return "srt://@" + pubIp + ":" + port;
			}else{
				return "srt://@" + ip + ":" + port;
			}
		case RTSPRTP:
			return "rtsp://" + ip + ":" + port + "/" + pubName +".sdp";
		case HTTPFLV:
			int httpFlvPort = 1936;
			
			if(streamMediaId != null){
				StreamMediaPO streamMediaPO = streamMediaDao.findOne(streamMediaId);
				if(streamMediaPO != null){
					httpFlvPort = streamMediaPO.getStreamMediaCfg().getHttpFlvPort();
				}
			}
			return "http://" + ip + ":" + httpFlvPort + "/" + pubName;
		case RTMPFLV:
			String rtmpPubPoint = "";
			if(streamMediaId != null && streamMediaId != 0){
				StreamMediaPO streamMediaPO = streamMediaDao.findOne(streamMediaId);
				if(streamMediaPO != null){
					rtmpPubPoint = streamMediaPO.getStreamMediaCfg().getRtmpName();
				}
			}else{
				RtmpFlvBO rtmpFlvBO = JSONObject.parseObject(jsonOutParam, RtmpFlvBO.class);
				rtmpPubPoint = rtmpFlvBO.getPubPoint();
			}
			return "rtmp://" + ip + ":" + port + "/" + rtmpPubPoint +"/" + pubName;
		case HLS:
			HlsBO hlsBO = JSONObject.parseObject(jsonOutParam, HlsBO.class);
			if(hlsBO.getDstType() == 1){
				return hlsBO.getUrl();
			}else{
				if(streamMediaId != null){
					StreamMediaPO streamMediaPO = streamMediaDao.findOne(streamMediaId);
					if(streamMediaPO != null){
						return "http://" + pubIp + ":" + streamMediaPO.getStreamMediaCfg().getHlsPort() + "/" + pubName + "/" + hlsBO.getM3u8Name() + ".m3u8";
					}
				}
				return "http://" + pubIp + ":8888" + "/" + pubName + "/" + hlsBO.getM3u8Name() + ".m3u8";
			}
		case DASH:
			DashBO dashBO = JSONObject.parseObject(jsonOutParam, DashBO.class);
			if(dashBO.getDstType() == 1){
				return dashBO.getUrl();
			}else{
				if(streamMediaId != null){
					StreamMediaPO streamMediaPO = streamMediaDao.findOne(streamMediaId);
					if(streamMediaPO != null){
						return "http://" + pubIp + ":" + streamMediaPO.getStreamMediaCfg().getDashPort() + "/" + pubName + "/" + dashBO.getM3u8Name() + ".mpd";
					}
				}
				return "http://" + pubIp + ":1901" + "/" + pubName + "/" + dashBO.getM3u8Name() + ".mpd";
			}
		case HDS:
			int hdsPubPort = 0;
			String hdsPubName = pubName;
			if(pubName.contains("\\?")){
				hdsPubName = pubName.split("\\?")[1];
			}
			if(streamMediaId != null && streamMediaId != 0){
				StreamMediaPO streamMediaPO = streamMediaDao.findOne(streamMediaId);
				if(streamMediaPO != null){
					hdsPubPort = streamMediaPO.getStreamMediaCfg().getHdsPort();
					return "http://"+ pubIp +":"+ hdsPubPort +"/hds/"+ hdsPubName +".f4m";
				}
			}
			return "http://"+ ip +":"+ port +"/hds/"+ hdsPubName +".f4m";
		case MSS:
			if(streamMediaId != null && streamMediaId != 0){
				StreamMediaPO streamMediaPO = streamMediaDao.findOne(streamMediaId);
				if(streamMediaPO != null){
					int mssPubPort = streamMediaPO.getStreamMediaCfg().getSsmPort();
					return "http://"+ pubIp +":"+ mssPubPort +"/"+ pubName +".isml/manifest";
				}
			}
			return "http://"+ ip +":"+ port +"/"+ pubName +".isml/manifest";
		default:
			return "";
		}
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getPubName() {
		return pubName;
	}

	public void setPubName(String pubName) {
		this.pubName = pubName;
	}

	public String getVidSelect() {
		return vidSelect;
	}

	public void setVidSelect(String vidSelect) {
		this.vidSelect = vidSelect;
	}

	public String getAudSelect() {
		return audSelect;
	}

	public void setAudSelect(String audSelect) {
		this.audSelect = audSelect;
	}

	
	public ProtoType getType() {
		return type;
	}

	public void setType(ProtoType type) {
		this.type = type;
	}

	public JSONObject generateJsonOutParam() {
		return JSONObject.parseObject(jsonOutParam);
	}


	public String getJsonOutParam() {
		return jsonOutParam;
	}
	public void setJsonOutParam(String jsonOutParam) {
		this.jsonOutParam = jsonOutParam;
	}
	
	public Integer getDstType() {
		return dstType;
	}

	public void setDstType(Integer dstType) {
		this.dstType = dstType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Long getNetGroupId() {
		return netGroupId;
	}

	public void setNetGroupId(Long netGroupId) {
		this.netGroupId = netGroupId;
	}

	public Long getStreamMediaId() {
		return streamMediaId;
	}

	public void setStreamMediaId(Long streamMediaId) {
		this.streamMediaId = streamMediaId;
	}

	public String getSubSelect() {
		return subSelect;
	}

	public void setSubSelect(String subSelect) {
		this.subSelect = subSelect;
	}
	
	
}
