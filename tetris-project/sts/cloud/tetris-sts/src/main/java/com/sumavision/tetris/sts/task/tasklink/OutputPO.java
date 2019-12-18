package com.sumavision.tetris.sts.task.tasklink;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.sts.common.CommonConstants.ProtoType;
import com.sumavision.tetris.sts.common.CommonPO;
import com.sumavision.tetris.sts.task.outputBO.AsiBO;
import com.sumavision.tetris.sts.task.outputBO.CommonOutputBO;
import com.sumavision.tetris.sts.task.outputBO.DashBO;
import com.sumavision.tetris.sts.task.outputBO.HdsBO;
import com.sumavision.tetris.sts.task.outputBO.HlsBO;
import com.sumavision.tetris.sts.task.outputBO.HttpFlvBO;
import com.sumavision.tetris.sts.task.outputBO.HttpTsBO;
import com.sumavision.tetris.sts.task.outputBO.MssBO;
import com.sumavision.tetris.sts.task.outputBO.RtmpFlvBO;
import com.sumavision.tetris.sts.task.outputBO.RtspBO;
import com.sumavision.tetris.sts.task.outputBO.TsRtpBO;
import com.sumavision.tetris.sts.task.outputBO.TsSrtBO;
import com.sumavision.tetris.sts.task.outputBO.TsUdpBO;


@Entity
public class OutputPO extends CommonPO<OutputPO> implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7440982438467949985L;

	private Long nodeId;
	
	/**
	 * 当前输出节点类型
	 */
	private OutputType outputType;
	
	public enum OutputType{
		TRANS , //转码
		ENCAPSULATE, //网关
		NEWABILITY
	}
	
	/**
	 * 所属输入的节点id
	 */
	private Long inputId;
	
	/**
	 * 节目id，即为节目号，表示输出源中的哪个节目，
	 * 切记跟CommonOutputBO中的programNum不一样，后者是表示输出后的节目号是多少
	 */
	private Integer programId;
	
	/**
	 * 所属任务的节点id，网关输出无用
	 */
	private Long taskId;

	
	/**
	 * 所在deviceNode的id，是device内部的一个节点，注意不是devicePO的id，代码涉及面广，后续重构完将名称改成deviceNodeId
	 */
	private Long deviceId;
	
	private String pubName;
	
	private ProtoType type;

	/**
	 * 文档中网关有两个取值，stop和run，目前看貌似不是必填值，暂时不填
	 */
	private String status;
	
	/**
	 * 下面这两个参数分别是video-pid,audio-pid的输出选择，例
	 * vidselect : 1,3
	 * audselect : 1,4
	 * 表示视频输出选择十路中的1和3，音频输出选择十路中的1和4
	 */
	private String vidSelect;
	private String audSelect;

	private String subSelect;
	
	private String localIp;
	
	/**
	 * 网络分组id，对外的输出才有此字段
	 */
	private Long netGroupId;
	
	private String ip;
	
	private Integer port;
	
	private Long streamMediaId;
	
	private String m3u8Name;
	
	/**
	 * json格式存储output页签内的输出页签参数，根据streamType，对应bo.task.output中的bo
	 */
	private String jsonParam;
	
	/**
	 * jsonParam转换后的BO，不存数据库的字段
	 */
	private CommonOutputBO outputBO;

	
	private void transferJsonToBO(){
		if(type != null){
			switch (type) {
			case TSUDP:
			case TSUDPPASSBY:
				outputBO = JSONObject.parseObject(jsonParam, TsUdpBO.class);
				break;
			case ASI:
				outputBO = JSONObject.parseObject(jsonParam, AsiBO.class);
				break;
			case HDS:
				outputBO = JSONObject.parseObject(jsonParam, HdsBO.class);
				break;
			case DASH:
				outputBO = JSONObject.parseObject(jsonParam, DashBO.class);
				break;
			case HTTPTSPASSBY:
				outputBO = JSONObject.parseObject(jsonParam, HttpTsBO.class);
				break;
			case HLS:
				outputBO = JSONObject.parseObject(jsonParam, HlsBO.class);
				break;
			case HTTPFLV:
				outputBO = JSONObject.parseObject(jsonParam, HttpFlvBO.class);
				break;
			case HTTPTS:
				outputBO = JSONObject.parseObject(jsonParam, HttpTsBO.class);
				break;
			case MSS:
				outputBO = JSONObject.parseObject(jsonParam, MssBO.class);
				break;
			case RTMPFLV:
				outputBO = JSONObject.parseObject(jsonParam, RtmpFlvBO.class);
				break;
			case RTSPRTP:
				outputBO = JSONObject.parseObject(jsonParam, RtspBO.class);
				break;
			case TSSRT:
				outputBO = JSONObject.parseObject(jsonParam, TsSrtBO.class);
				break;
			case TSSRTPASSBY:
				outputBO = JSONObject.parseObject(jsonParam, TsSrtBO.class);
				break;
			case TSRTP:
				outputBO = JSONObject.parseObject(jsonParam, TsRtpBO.class);
				break;
			default:
				outputBO = JSONObject.parseObject(jsonParam, CommonOutputBO.class);
				break;
			}
		}
	}
	

	public void setOutputBO(CommonOutputBO outputBO) {
		this.outputBO = outputBO;
		jsonParam = JSONObject.toJSONString(outputBO);
		if(type.equals(ProtoType.HLS)){
			HlsBO bo = (HlsBO)outputBO;
			setM3u8Name(bo.getM3u8Name());
		}
	}
	
	@Transient
	public CommonOutputBO getOutputBO() {
		if(outputBO == null){
			transferJsonToBO();
		}
		return outputBO;
	}
	
	@Column
	public Integer getProgramId() {
		return programId;
	}
	public void setProgramId(Integer programId) {
		this.programId = programId;
	}
	@Column
	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	@Column
	public String getPubName() {
		return pubName;
	}

	public void setPubName(String pubName) {
		this.pubName = pubName;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public ProtoType getType() {
		return type;
	}
	public void setType(ProtoType type) {
		this.type = type;
	}
	@Column
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column
	public String getVidSelect() {
		return vidSelect;
	}
	public void setVidSelect(String vidSelect) {
		this.vidSelect = vidSelect;
	}
	@Column
	public String getAudSelect() {
		return audSelect;
	}
	public void setAudSelect(String audSelect) {
		this.audSelect = audSelect;
	}
	@Column
	public String getSubSelect() {
		return subSelect;
	}

	public void setSubSelect(String subSelect) {
		this.subSelect = subSelect;
	}

	@Column
	public OutputType getOutputType() {
		return outputType;
	}
	public void setOutputType(OutputType outputType) {
		this.outputType = outputType;
	}
	@Column
	public Long getInputId() {
		return inputId;
	}
	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}
	@Column
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	@Column(columnDefinition="TEXT")
	public String getJsonParam() {
		//存储前，bo不为null，存在bo被修改的可能，更新一次json
		if(outputBO != null){
			setJsonParam(JSONObject.toJSONString(outputBO));
		}
		return jsonParam;
	}
	public void setJsonParam(String jsonParam) {
		this.jsonParam = jsonParam;
		transferJsonToBO();
		if(type!=null && type.equals(ProtoType.HLS)){
			HlsBO bo = (HlsBO)outputBO;
			setM3u8Name(bo.getM3u8Name());
		}
	}
	@Column
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	
	@Column
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	@Column
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Column
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	@Column
	public Long getNetGroupId() {
		return netGroupId;
	}
	public void setNetGroupId(Long netGroupId) {
		this.netGroupId = netGroupId;
	}

	@Column
	public Long getStreamMediaId() {
		return streamMediaId;
	}

	public void setStreamMediaId(Long streamMediaId) {
		this.streamMediaId = streamMediaId;
	}

	@Column
	public String getM3u8Name() {
		return m3u8Name;
	}

	public void setM3u8Name(String m3u8Name) {
		this.m3u8Name = m3u8Name;
	}

	public Boolean isSameWithCfg(OutputPO outputPO){
		if(outputPO == null){
			return false;
		}
		if (this.inputId == null) {
			if (outputPO.inputId != null)
				return false;
		} else if (!this.inputId.equals(outputPO.inputId))
			return false;
		if (this.nodeId == null) {
			if (outputPO.nodeId != null)
				return false;
		} else if (!this.nodeId.equals(outputPO.nodeId))
			return false;
		if (this.programId == null) {
			if (outputPO.programId != null)
				return false;
		} else if (!this.programId.equals(outputPO.programId))
			return false;
		if (this.taskId == null) {
			if (outputPO.taskId != null)
				return false;
		} else if (!this.taskId.equals(outputPO.taskId))
			return false;
		if (this.type == null) {
			if (outputPO.type != null)
				return false;
		} else if (!this.type.equals(outputPO.type))
			return false;
		
		this.transferJsonToBO();
		if(type != null){
			switch (type) {
			case TSUDP:
			case TSRTP:
			case TSSRT:
				if (this.localIp == null) {
					if (outputPO.localIp != null)
						return false;
				} else if (!this.localIp.equals(outputPO.localIp))
					return false;
				if (this.ip == null) {
					if (outputPO.ip != null)
						return false;
				} else if (!this.ip.equals(outputPO.ip))
					return false;
				if (this.port == null) {
					if (outputPO.port != null)
						return false;
				} else if (!this.port.equals(outputPO.port))
					return false;
				break;
			case HTTPTS:
			case RTSPRTP:
			case RTMPFLV:
			case MSS:
				if (this.localIp == null) {
					if (outputPO.localIp != null)
						return false;
				} else if (!this.localIp.equals(outputPO.localIp))
					return false;
				if (this.ip == null) {
					if (outputPO.ip != null)
						return false;
				} else if (!this.ip.equals(outputPO.ip))
					return false;
				if (this.port == null) {
					if (outputPO.port != null)
						return false;
				} else if (!this.port.equals(outputPO.port))
					return false;
				if (this.pubName == null) {
					if (outputPO.pubName != null)
						return false;
				} else if (!this.pubName.equals(outputPO.pubName))
					return false;
				break;
			case HTTPFLV:
			case HDS:
				if (this.localIp == null) {
					if (outputPO.localIp != null)
						return false;
				} else if (!this.localIp.equals(outputPO.localIp))
					return false;
				if (this.ip == null) {
					if (outputPO.ip != null)
						return false;
				} else if (!this.ip.equals(outputPO.ip))
					return false;
//				if (this.port == null) {
//					if (outputPO.port != null)
//						return false;
//				} else if (!this.port.equals(outputPO.port))
//					return false;
				if (this.pubName == null) {
					if (outputPO.pubName != null)
						return false;
				} else if (!this.pubName.equals(outputPO.pubName))
					return false;
				break;
			case HLS:
				if (this.localIp == null) {
					if (outputPO.localIp != null)
						return false;
				} else if (!this.localIp.equals(outputPO.localIp))
					return false;
				if (this.pubName == null) {
					if (outputPO.pubName != null)
						return false;
				} else if (!this.pubName.equals(outputPO.pubName))
					return false;
				break;
			case DASH:
				if (this.pubName == null) {
					if (outputPO.pubName != null)
						return false;
				} else if (!this.pubName.equals(outputPO.pubName))
					return false;
				break;
			case TSUDPPASSBY:
				if (this.localIp == null) {
					if (outputPO.localIp != null)
						return false;
				} else if (!this.localIp.equals(outputPO.localIp))
					return false;
				if (this.ip == null) {
					if (outputPO.ip != null)
						return false;
				} else if (!this.ip.equals(outputPO.ip))
					return false;
				if (this.port == null) {
					if (outputPO.port != null)
						return false;
				} else if (!this.port.equals(outputPO.port))
					return false;
				TsUdpBO tsUdpBO = (TsUdpBO)this.outputBO;
				TsUdpBO tsUdpCfgBO = (TsUdpBO)outputPO.getOutputBO();
				if (tsUdpBO.getProvider() == null) {
					if (tsUdpCfgBO.getProvider() != null)
						return false;
				} else if (!tsUdpBO.getProvider().equals(tsUdpCfgBO.getProvider()))
					return false;
				if (tsUdpBO.getName() == null) {
					if (tsUdpCfgBO.getName() != null)
						return false;
				} else if (!tsUdpBO.getName().equals(tsUdpCfgBO.getName()))
					return false;
				if (tsUdpBO.getAudPids() == null) {
					if (tsUdpCfgBO.getAudPids() != null)
						return false;
				} else if (!tsUdpBO.getAudPids().equals(tsUdpCfgBO.getAudPids()))
					return false;
				if (tsUdpBO.getVidPids() == null) {
					if (tsUdpCfgBO.getVidPids() != null)
						return false;
				} else if (!tsUdpBO.getVidPids().equals(tsUdpCfgBO.getVidPids()))
					return false;
				break;
			case TSSRTPASSBY:
				if (this.localIp == null) {
					if (outputPO.localIp != null)
						return false;
				} else if (!this.localIp.equals(outputPO.localIp))
					return false;
				if (this.ip == null) {
					if (outputPO.ip != null)
						return false;
				} else if (!this.ip.equals(outputPO.ip))
					return false;
				if (this.port == null) {
					if (outputPO.port != null)
						return false;
				} else if (!this.port.equals(outputPO.port))
					return false;
				TsSrtBO tsSrtBO = (TsSrtBO)this.outputBO;
				TsSrtBO tsSrtCfgBO = (TsSrtBO)outputPO.getOutputBO();
				if (tsSrtBO.getProvider() == null) {
					if (tsSrtCfgBO.getProvider() != null)
						return false;
				} else if (!tsSrtBO.getProvider().equals(tsSrtCfgBO.getProvider()))
					return false;
				if (tsSrtBO.getName() == null) {
					if (tsSrtCfgBO.getName() != null)
						return false;
				} else if (!tsSrtBO.getName().equals(tsSrtCfgBO.getName()))
					return false;
				if (tsSrtBO.getModeSelect() == null) {
					if (tsSrtCfgBO.getModeSelect() != null)
						return false;
				} else if (!tsSrtBO.getModeSelect().equals(tsSrtCfgBO.getModeSelect()))
					return false;
				if (tsSrtBO.getAudPids() == null) {
					if (tsSrtCfgBO.getAudPids() != null)
						return false;
				} else if (!tsSrtBO.getAudPids().equals(tsSrtCfgBO.getAudPids()))
					return false;
				if (tsSrtBO.getVidPids() == null) {
					if (tsSrtCfgBO.getVidPids() != null)
						return false;
				} else if (!tsSrtBO.getVidPids().equals(tsSrtCfgBO.getVidPids()))
					return false;
				break;
			case HTTPTSPASSBY:
				if (this.localIp == null) {
					if (outputPO.localIp != null)
						return false;
				} else if (!this.localIp.equals(outputPO.localIp))
					return false;
				if (this.ip == null) {
					if (outputPO.ip != null)
						return false;
				} else if (!this.ip.equals(outputPO.ip))
					return false;
				if (this.port == null) {
					if (outputPO.port != null)
						return false;
				} else if (!this.port.equals(outputPO.port))
					return false;
				HttpTsBO httpTsBO = (HttpTsBO)this.outputBO;
				HttpTsBO httpTsCfgBO = (HttpTsBO)outputPO.getOutputBO();
				if (httpTsBO.getProvider() == null) {
					if (httpTsCfgBO.getProvider() != null)
						return false;
				} else if (!httpTsBO.getProvider().equals(httpTsCfgBO.getProvider()))
					return false;
				if (httpTsBO.getName() == null) {
					if (httpTsCfgBO.getName() != null)
						return false;
				} else if (!httpTsBO.getName().equals(httpTsCfgBO.getName()))
					return false;
				if (httpTsBO.getAudPids() == null) {
					if (httpTsCfgBO.getAudPids() != null)
						return false;
				} else if (!httpTsBO.getAudPids().equals(httpTsCfgBO.getAudPids()))
					return false;
				if (httpTsBO.getVidPids() == null) {
					if (httpTsCfgBO.getVidPids() != null)
						return false;
				} else if (!httpTsBO.getVidPids().equals(httpTsCfgBO.getVidPids()))
					return false;
				break;
			default:
                break;
			}
		}
		if(this.type != ProtoType.TSUDPPASSBY && this.type != ProtoType.HTTPTSPASSBY && this.type != ProtoType.TSSRTPASSBY){
			if(!this.outputBO.isSameWithCfg(outputPO.getOutputBO())){
				return false;
			}
		}
		return true;
	}
}
