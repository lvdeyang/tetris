package com.sumavision.bvc.control.device.monitor.live;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDevicePO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 设备点播任务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月24日 上午11:12:27
 */
public class MonitorLiveDeviceVO extends AbstractBaseVO<MonitorLiveDeviceVO, MonitorLiveDevicePO>{

	private String udpUrl;
	
	private String videoBundleId;

	private String videoBundleName;
	
	private String videoChannelName;
	
	private String audioBundleId;
	
	private String audioBundleName;
	
	private String audioChannelName;
	
	private String dstVideoBundleId;
	
	private String dstVideoBundleName;
	
	private String dstVideoChannelName;
	
	private String dstAudioBundleId;
	
	private String dstAudioBundleName;
	
	private String dstAudioChannelName;
	
	private Long osdId;
	
	private String osdName;
	
	private String osdUsername;
	
	private String type;
	
	/** 源扩展字段*/
	private String extraInfo;
	
	/** 目的扩展字段*/
	private String dstExtraInfo;
	
	/** RUN代表在执行，STOP表示停止*/
	private String status;
	
	public String getVideoBundleId() {
		return videoBundleId;
	}

	public MonitorLiveDeviceVO setVideoBundleId(String videoBundleId) {
		this.videoBundleId = videoBundleId;
		return this;
	}

	public String getAudioBundleId() {
		return audioBundleId;
	}

	public MonitorLiveDeviceVO setAudioBundleId(String audioBundleId) {
		this.audioBundleId = audioBundleId;
		return this;
	}

	public String getDstVideoBundleId() {
		return dstVideoBundleId;
	}

	public MonitorLiveDeviceVO setDstVideoBundleId(String dstVideoBundleId) {
		this.dstVideoBundleId = dstVideoBundleId;
		return this;
	}

	public String getDstAudioBundleId() {
		return dstAudioBundleId;
	}

	public MonitorLiveDeviceVO setDstAudioBundleId(String dstAudioBundleId) {
		this.dstAudioBundleId = dstAudioBundleId;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public MonitorLiveDeviceVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getDstExtraInfo() {
		return dstExtraInfo;
	}

	public MonitorLiveDeviceVO setDstExtraInfo(String dstExtraInfo) {
		this.dstExtraInfo = dstExtraInfo;
		return this;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public MonitorLiveDeviceVO setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
		return this;
	}

	public String getUdpUrl() {
		return udpUrl;
	}

	public MonitorLiveDeviceVO setUdpUrl(String udpUrl) {
		this.udpUrl = udpUrl;
		return this;
	}

	public String getVideoBundleName() {
		return videoBundleName;
	}

	public MonitorLiveDeviceVO setVideoBundleName(String videoBundleName) {
		this.videoBundleName = videoBundleName;
		return this;
	}

	public String getVideoChannelName() {
		return videoChannelName;
	}

	public MonitorLiveDeviceVO setVideoChannelName(String videoChannelName) {
		this.videoChannelName = videoChannelName;
		return this;
	}

	public String getAudioBundleName() {
		return audioBundleName;
	}

	public MonitorLiveDeviceVO setAudioBundleName(String audioBundleName) {
		this.audioBundleName = audioBundleName;
		return this;
	}

	public String getAudioChannelName() {
		return audioChannelName;
	}

	public MonitorLiveDeviceVO setAudioChannelName(String audioChannelName) {
		this.audioChannelName = audioChannelName;
		return this;
	}

	public String getDstVideoBundleName() {
		return dstVideoBundleName;
	}

	public MonitorLiveDeviceVO setDstVideoBundleName(String dstVideoBundleName) {
		this.dstVideoBundleName = dstVideoBundleName;
		return this;
	}

	public String getDstVideoChannelName() {
		return dstVideoChannelName;
	}

	public MonitorLiveDeviceVO setDstVideoChannelName(String dstVideoChannelName) {
		this.dstVideoChannelName = dstVideoChannelName;
		return this;
	}

	public String getDstAudioBundleName() {
		return dstAudioBundleName;
	}

	public MonitorLiveDeviceVO setDstAudioBundleName(String dstAudioBundleName) {
		this.dstAudioBundleName = dstAudioBundleName;
		return this;
	}

	public String getDstAudioChannelName() {
		return dstAudioChannelName;
	}

	public MonitorLiveDeviceVO setDstAudioChannelName(String dstAudioChannelName) {
		this.dstAudioChannelName = dstAudioChannelName;
		return this;
	}
	
	public Long getOsdId() {
		return osdId;
	}

	public MonitorLiveDeviceVO setOsdId(Long osdId) {
		this.osdId = osdId;
		return this;
	}

	public String getOsdName() {
		return osdName;
	}

	public MonitorLiveDeviceVO setOsdName(String osdName) {
		this.osdName = osdName;
		return this;
	}

	public String getOsdUsername() {
		return osdUsername;
	}

	public MonitorLiveDeviceVO setOsdUsername(String osdUsername) {
		this.osdUsername = osdUsername;
		return this;
	}

	public String getType() {
		return type;
	}

	public MonitorLiveDeviceVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public MonitorLiveDeviceVO set(MonitorLiveDevicePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setVideoBundleName(entity.getVideoBundleName())
			.setVideoChannelName(entity.getVideoChannelName())
			.setAudioBundleName(entity.getAudioBundleName()==null?"-":entity.getAudioBundleName())
			.setAudioChannelName(entity.getAudioChannelName()==null?"-":entity.getAudioChannelName())
			.setDstVideoBundleName(entity.getDstVideoBundleName())
			.setDstVideoChannelName(entity.getDstVideoChannelName())
			.setDstAudioBundleName(entity.getDstAudioBundleName()==null?"-":entity.getDstAudioBundleName())
			.setDstAudioChannelName(entity.getDstAudioChannelName()==null?"-":entity.getDstAudioChannelName())
			.setOsdId(entity.getOsdId())
			.setOsdName("-")
			.setOsdUsername("-")
			.setType(entity.getDstDeviceType()==null?"":entity.getDstDeviceType().getName())
			.setStatus(entity.getStatus()==null?"":(entity.getStatus().toString().equals("RUN")?"运行中":"已停止"))
			.setAudioBundleId(entity.getAudioBundleId()==null?"-":entity.getAudioBundleId())
			.setVideoBundleId(entity.getVideoBundleId()==null?"-":entity.getVideoBundleId())
			.setDstAudioBundleId(entity.getDstAudioBundleId()==null?"-":entity.getDstAudioBundleId())
			.setDstVideoBundleId(entity.getDstVideoBundleId()==null?"-":entity.getDstVideoBundleId());
		return this;
	}
	
	public MonitorLiveDeviceVO set(MonitorLiveDevicePO entity,List<ExtraInfoPO> extraInfos,List<ExtraInfoPO> dstExtraInfos)throws Exception{
		set(entity);
		//添加扩展字段
		JSONObject extraInfo = new JSONObject();
		if(extraInfos != null && extraInfos.size()>0){
			for(ExtraInfoPO extra : extraInfos){
				extraInfo.put(extra.getName(), extra.getValue());
			}
			this.setExtraInfo(extraInfo.toJSONString());
		}	
		
		JSONObject dstExtraInfo = new JSONObject();
		if(dstExtraInfos != null && dstExtraInfos.size()>0){
			for(ExtraInfoPO extra : dstExtraInfos){
				dstExtraInfo.put(extra.getName(), extra.getValue());
			}
			this.setDstExtraInfo(dstExtraInfo.toJSONString());
		}
		return this;
	}
	
}
