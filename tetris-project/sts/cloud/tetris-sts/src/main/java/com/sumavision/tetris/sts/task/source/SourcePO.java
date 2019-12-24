package com.sumavision.tetris.sts.task.source;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.sumavision.tetris.sts.common.CommonConstants;
import com.sumavision.tetris.sts.common.CommonConstants.ProtoType;
import com.sumavision.tetris.sts.common.CommonPO;


/**
 * 节目源信息
 * @author gaofeng
 *
 */
@Entity
@Table(name="source")
public class SourcePO extends CommonPO<SourcePO> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1990430716212330950L;

	/**
	 * 节目源名称
	 */
	private String sourceName;
	/**
	 * 节目源url
	 */
	private String sourceUrl;
	/**
	 * 节目源ip
	 */
	private String sourceIp;
	/**
	 * 节目源端口
	 */
	private Integer sourcePort;
	/**
	 * 标识组播还是单播
	 * 0：单播
	 * 1：组播
	 */
	private Integer displayType;

	private Integer cardIndex;
	
	public enum SourceType{
		STREAM,SDI,PASSBY
	}
	private SourceType sourceType = SourceType.STREAM;
	
	private Long sourceGroupId;

	private Long netGroupId;
	
	private Long deviceGroupId;
	/**
	 * 用于存储SDI卡的设备id
	 */
	private Long deviceId;
	/**
	 * 是否为主源
	 * 0：备源
	 * 1：主源
	 */
	private Integer isMain;
	
	/**
	 * 输入源状态
	 * 异常，正常，刷新中，待刷新
	 */
	public enum SourceStatus{
		ABNORMAL,NORMAL,REFRESHING,NEED_REFRESH
	}
	
	/**
	 * 源状态
	 */
	private SourceStatus status;
	
	/**
	 * 是否启动
	 */
	private Boolean launchFlag = true;
	/**
	 * 协议类型
	 */
	private ProtoType protoType;
	/**
	 * Pid自适应，on，off
	 */
	private String adaptive;

	private String localIp;

	@JSONField(name="program_array")
	private List<ProgramPO> programPOs = new ArrayList<ProgramPO>();

	private Boolean isIgmpv3 = false;

	//过滤模式 include;exclude
	private CommonConstants.FilterMode filterMode;

	private String filterIpSegmentsJson;

	private List<FilterIpSegment> filterIpSegments;

	//垫播url
	private String matFileName;

	//模式选择 针对ts-srt
	private String modeSelect;
		
	//延时时间 针对ts-srt
	private Integer latency;

	//pts开关，网关参数，江苏用，yzx add, 默认值0表示关
	private String genPts = "0";

	@Column
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	@Column
	public String getSourceUrl() {
		if(sourceUrl == null || sourceUrl.equals("")){
			switch (protoType) {
			case TSUDP:
				sourceUrl = "udp://"+sourceIp + ":"+sourcePort;				
			default:
				
				break;
			}
		}
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	@Column
	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	@Column
	public Integer getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(Integer sourcePort) {
		this.sourcePort = sourcePort;
	}
	@Column
	public Integer getDisplayType() {
		return displayType;
	}

	public void setDisplayType(Integer displayType) {
		this.displayType = displayType;
	}
	@Column
	public Integer getIsMain() {
		return isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
	}
	
	@Column
	@Enumerated(EnumType.STRING)
	public ProtoType getProtoType() {
		return protoType;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public SourceStatus getStatus() {
		return status;
	}

	public void setStatus(SourceStatus status) {
		this.status = status;
	}

	public void setProtoType(ProtoType protoType) {
		this.protoType = protoType;
	}

	@Column
	public String getAdaptive() {
		return adaptive;
	}
	public void setAdaptive(String adaptive) {
		this.adaptive = adaptive;
	}

	@Column
	public Long getNetGroupId() {
		return netGroupId;
	}

	public void setNetGroupId(Long netGroupId) {
		this.netGroupId = netGroupId;
	}

	@Column
	public Boolean getIsIgmpv3() {
		return isIgmpv3;
	}

	public void setIsIgmpv3(Boolean isIgmpv3) {
		this.isIgmpv3 = isIgmpv3;
	}

	@Column

	@Enumerated(EnumType.STRING)
	public CommonConstants.FilterMode getFilterMode() {
		return filterMode;
	}


	public void setFilterMode(CommonConstants.FilterMode filterMode) {
		this.filterMode = filterMode;
	}

	@Column
	public String getFilterIpSegmentsJson() {
		return filterIpSegmentsJson;
	}

	public void setFilterIpSegmentsJson(String filterIpSegmentsJson) {
		this.filterIpSegmentsJson = filterIpSegmentsJson;
	}

	@OneToMany(fetch= FetchType.EAGER , cascade=CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name="sourceId")
	public List<ProgramPO> getProgramPOs() {
		return programPOs;
	}

	public void setProgramPOs(List<ProgramPO> programPOs) {
		this.programPOs = programPOs;
	}

	@Transient
	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	@Transient
	public List<FilterIpSegment> getFilterIpSegments() {
		this.filterIpSegments = JSON.parseArray(this.filterIpSegmentsJson , FilterIpSegment.class);
		return filterIpSegments;
	}

	public void setFilterIpSegments(List<FilterIpSegment> filterIpSegments) {
		this.filterIpSegments = filterIpSegments;
		this.filterIpSegmentsJson = JSON.toJSONString(filterIpSegments);
	}
	
	
	@Column
	public Long getSourceGroupId() {
		return sourceGroupId;
	}

	public void setSourceGroupId(Long sourceGroupId) {
		this.sourceGroupId = sourceGroupId;
	}

	@Column
	public Long getDeviceGroupId() {
		return deviceGroupId;
	}

	public void setDeviceGroupId(Long deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}

	@Column
	public String getMatFileName() {
		return matFileName;
	}

	public void setMatFileName(String matFileName) {
		this.matFileName = matFileName;
	}
	
	
	@Column
	@Enumerated(EnumType.STRING)
	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Column
	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Column
	public Integer getCardIndex() {
		return cardIndex;
	}

	public void setCardIndex(Integer cardIndex) {
		this.cardIndex = cardIndex;
	}
	@Column
	public Boolean getLaunchFlag() {
		return launchFlag;
	}

	public void setLaunchFlag(Boolean launchFlag) {
		this.launchFlag = launchFlag;
	}
	

	/**
	 * 节目信息从source放入input需要clone
	 * @return
	 */
	public List<ProgramPO> deepCloneProgram(){
		List<ProgramPO> programPOs = new ArrayList<ProgramPO>();
		for(ProgramPO programPO : getProgramPOs()){
			programPOs.add(programPO.deepClone());
		}
		return programPOs;
	}
	
	/**
	 * 比较本源的节目跟目标节目关键信息是否一致
	 * @param otherPrograms
	 * @return
	 */
	public boolean comparePrograms(List<ProgramPO> otherPrograms){
		if(this.getProgramPOs().size() != otherPrograms.size()){
			return false;
		}
		Collections.sort(this.getProgramPOs());
		Collections.sort(otherPrograms);
		for(int index = 0; index < otherPrograms.size(); index++){
			if(!this.getProgramPOs().get(index).equals(otherPrograms.get(index))){
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "SourcePO [sourceName=" + sourceName + ", sourceUrl="
				+ sourceUrl + ", sourceIp=" + sourceIp + ", sourcePort="
				+ sourcePort + ", displayType=" + displayType
				+ ", sourceGroupId=" + sourceGroupId + ", netGroupId="
				+ netGroupId + ", deviceGroupId=" + deviceGroupId + ", isMain="
				+ isMain + ", status=" + status + ", protoType=" + protoType
				+ ", adaptive=" + adaptive + ", localIp=" + localIp
				+ ", programPOs=" + programPOs + ", isIgmpv3=" + isIgmpv3
				+ ", filterMode=" + filterMode + ", filterIpSegmentsJson="
				+ filterIpSegmentsJson + ", filterIpSegments="
				+ filterIpSegments + "]";
	}
	
	@Column
	public String getModeSelect() {
		return modeSelect;
	}

	public void setModeSelect(String modeSelect) {
		this.modeSelect = modeSelect;
	}

	@Column
	public Integer getLatency() {
		return latency;
	}

	public void setLatency(Integer latency) {
		this.latency = latency;
	}

	@Column
	public String getGenPts() {
		return genPts;
	}

	public void setGenPts(String genPts) {
		this.genPts = genPts;
	}

}
