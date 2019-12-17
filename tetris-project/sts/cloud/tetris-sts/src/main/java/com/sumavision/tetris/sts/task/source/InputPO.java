package com.sumavision.tetris.sts.task.source;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.sts.common.CommonConstants;
import com.sumavision.tetris.sts.common.CommonConstants.NodeStatus;
import com.sumavision.tetris.sts.common.CommonConstants.ProtoType;
import com.sumavision.tetris.sts.common.CommonPO;

@Entity
@Table
public class InputPO extends CommonPO<InputPO> implements Serializable{

	
	private static final long serialVersionUID = -6001494191858822681L;

	private InputType inputType;
	
	public enum InputType{
		TRANS , //转码
		ENCAPSULATE, //网关
		NEWABILITY //新能力，不区分网关转码
	}
	
	/**
	 * 节点id
	 */
	private Long nodeId;
	
	/**
	 * 输入类型，stream、passby、card
	 */
	private String type;
	
	private String workMode;
	
	private String localIp;
	
	/**
	 * 所在deviceNode的id，是device内部的一个节点，注意不是devicePO的id，代码涉及面广，后续重构完将名称改成deviceNodeId
	 */
	private Long deviceId;
	/**
	 * 输入ip（有url的情况下，是否需要此ip和port，或者存储localIp）
	 */
	private String sourceIp;
	/**
	 * 输入端口
	 */
	private Integer port;
	/**
	 * 所属节目源的id，转码和接收外部源的网关的input有这个id，切忌内部网关（出网关）没有这个id！！
	 */
	private Long sourceId;
	
	/**
	 * 作为中间节点的入，需要存储上级output节点的nodeid
	 */
	private Long preOutputNodeId;
	
	/**
	 * 任务节点状态
	 * 0：正常
	 * 1：异常
	 * 2：删除中
	 */
	private NodeStatus nodeStatus = NodeStatus.NORMAL;

	/**
	 * 网关输入字段，输入类型udp-ts、rtp-ts等等
	 */
	private ProtoType urlType;
	
	private String url;
	
	/**
	 * 网关输入字段，当输入源类型为rtp-es时有效，其他情况无效。值为音频媒体IP和端口
	 */
	private String url2;

	/**
	 * 网关输入字段，输入名称
	 */
	private String name;
	
	/*
	 * IGMPV3
	 */
	private Boolean isIgmpv3;

	//过滤模式 include;exclude
	private CommonConstants.FilterMode filterMode;

	private String filterIpSegmentsJson;

	private List<FilterIpSegment> filterIpSegments;
	
	private String startIp;
	private String endIp;
	
	
	@Transient
	public String getStartIp() {
		if(this.filterIpSegments != null && this.filterIpSegments.size() > 0){
			return this.filterIpSegments.get(0).getStartIp();
		}else{
			return startIp;
		}
	}
	
	@Transient
	public String getEndIp() {
		if(this.filterIpSegments != null && this.filterIpSegments.size() > 0){
			return this.filterIpSegments.get(0).getEndIp();
		}else{
			return endIp;
		}
	}
	
	//垫播url
	private String matsowingUrl;

	private Integer cardNumber;
	
	//模式选择 针对ts-srt
	private String modeSelect;
	
	//延时时间 针对ts-srt
	private Integer latency;

	//yzx add 网关参数，江苏需要
	private String genPts;

	@Column
	public Integer getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(Integer cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Column
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	@Column
	public InputType getInputType() {
		return inputType;
	}
	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}
	@Column
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	@Column
	public String getSourceIp() {
		return sourceIp;
	}
	
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	@Column
	public Integer getPort() {
		return port;
	}
	/**
	 * 所属节目源的id，转码和接收外部源的网关的input有这个id，切忌内部网关（出网关）没有这个id！！
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	@Column
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	@Column
	public NodeStatus getNodeStatus() {
		return nodeStatus;
	}
	public void setNodeStatus(NodeStatus nodeStatus) {
		this.nodeStatus = nodeStatus;
	}
	@Column
	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	@Column
	public ProtoType getUrlType() {
		return urlType;
	}
	public void setUrlType(ProtoType urlType) {
		this.urlType = urlType;
	}
	@Column
	public String getUrl2() {
		return url2;
	}
	public void setUrl2(String url2) {
		this.url2 = url2;
	}
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column
	public Long getPreOutputNodeId() {
		return preOutputNodeId;
	}
	public void setPreOutputNodeId(Long preOutputNodeId) {
		this.preOutputNodeId = preOutputNodeId;
	}
	

	@Column
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
//		this.sourceIp = url.substring(url.lastIndexOf("/") + 1).split(":")[0];
//		this.port = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1).split(":")[1]);
		this.url = url;
	}

	/* 覆写的原因是，这种序列化复制的方式，input下关联的program不能正常存储（不是因为id）。。
	 * @see com.suma.xianrd.sirius.pojo.CommonPO#deepClone()
	 */
	@Override
	public InputPO deepClone() throws ClassNotFoundException, IOException {
		InputPO inputPO = (InputPO)super.deepClone();
//		inputPO.setProgramPOs(new ArrayList<ProgramPO>(inputPO.getProgramPOs()));
		return inputPO;
	}
	
	@Column
	public String getMatsowingUrl() {
		return matsowingUrl;
	}
	public void setMatsowingUrl(String matsowingUrl) {
		this.matsowingUrl = matsowingUrl;
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
	@Column
	public String getWorkMode() {
		return workMode;
	}
	public void setWorkMode(String workMode) {
		this.workMode = workMode;
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
	
	public Boolean isIdentical(InputPO inpotPO){
		return true;
	}
	
	public Boolean isSameWithCfg(InputPO inputPo){
		if(inputPo == null){
			return false;
		}
		
		if (this.nodeId == null) {
			if (inputPo.nodeId != null)
				return false;
		} else if (!this.nodeId.equals(inputPo.nodeId))
			return false;
		if (this.type == null) {
			if (inputPo.type != null)
				return false;
		} else if (!this.type.equals(inputPo.type))
			return false;
		if (this.url == null) {
			if (inputPo.url != null)
				return false;
		} else if (!this.url.equals(inputPo.url))
			return false;
		if (this.localIp == null) {
			if (inputPo.localIp != null)
				return false;
		} else if (!this.localIp.equals(inputPo.localIp))
			return false;
		if (this.filterMode == null) {
			if (inputPo.filterMode != null)
				return false;
		} else if (!this.filterMode.equals(inputPo.filterMode))
			return false;
//		if(this.filterIpSegments != null && this.filterIpSegments.size() > 0){
//			if(this.getStartIp() == null){
//				if(inputPo.startIp != null)
//					return false;
//			}else if(!this.getStartIp().equals(inputPo.startIp))
//				return false;
//			if(this.getEndIp() == null){
//				if(inputPo.endIp != null)
//					return false;
//			}else if(!this.getEndIp().equals(inputPo.endIp))
//				return false;
//		}
		
		if(this.inputType.equals(InputType.TRANS)){
			if (this.modeSelect == null) {
				if (inputPo.modeSelect != null)
					return false;
			} else if (!this.modeSelect.equals(inputPo.modeSelect))
				return false;
			if (this.latency == null) {
				if (inputPo.latency != null)
					return false;
			} else if (!this.latency.equals(inputPo.latency))
				return false;
			if (this.cardNumber == null) {
				if (inputPo.cardNumber != null)
					return false;
			} else if (!this.cardNumber.equals(inputPo.cardNumber))
				return false;
		}else{
			if (this.genPts == null) {
				if (inputPo.genPts != null)
					return false;
			} else if (!this.genPts.equals(inputPo.genPts))
				return false;
			if(this.type.equals(CommonConstants.INPUT_TYPE_PASSBY)){
				if (this.modeSelect == null) {
					if (inputPo.modeSelect != null)
						return false;
				} else if (!this.modeSelect.equals(inputPo.modeSelect))
					return false;
				if (this.latency == null) {
					if (inputPo.latency != null)
						return false;
				} else if (!this.latency.equals(inputPo.latency))
					return false;	
			}
		}
		
		return true;
	}
	
}
