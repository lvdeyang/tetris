package com.sumavision.bvc.command.group.forward;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * <p>议程转发对应媒体转发的扩展信息存储（如果是文件，没有走重新执行议程）</p>
 * <b>作者:</b>lixin<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月20日 下午1:28:10
 */
@Entity
@Table(name = "AGENDA_FORWARD_DEMAND")
public class AgendaForwardDemandPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 转发的业务类型 */
	private ForwardDemandBusinessType forwardType;
	
	/** 开始转发时间*/
	private Date beginForwardTime;
	
	/** 执行状态 (取code属性)*/
	private ForwardDemandStatus executeStatus;
	
	/** 目的号码 */
	private String dstCode;
	
	/** 目标成员名称（用于显示） */
	private String dstUserName;
	
	/** 源号码 ,当是文件的时候是resourceId*/
	private String srcCode;
	
	/** 源名称（设备是设备名称，成员是成员名称）*/
	private String srcName;
	
	/** 关联的议程转发的id*/
	private Long agendaForwardId;
	
	/** 会议GroupId*/
	private Long businessId;
	
	/** 如果点播的是文件，保存预览地址*/
	private String previewUrl;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "EXECUTE_STATUS")
	public ForwardDemandStatus getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(ForwardDemandStatus executeStatus) {
		this.executeStatus = executeStatus;
	}
	
	@Column(name = "DST_USER_NAME")
	public String getDstUserName() {
		return dstUserName;
	}

	public void setDstUserName(String dstUserName) {
		this.dstUserName = dstUserName;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "FORWARD_TYPE")
	public ForwardDemandBusinessType getForwardType() {
		return forwardType;
	}

	public void setForwardType(ForwardDemandBusinessType forwardType) {
		this.forwardType = forwardType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BEGIN_FORWARD_TIME")
	public Date getBeginForwardTime() {
		return beginForwardTime;
	}

	public void setBeginForwardTime(Date beginForwardTime) {
		this.beginForwardTime = beginForwardTime;
	}

	@Column(name = "DST_CODE")
	public String getDstCode() {
		return dstCode;
	}

	public void setDstCode(String dstCode) {
		this.dstCode = dstCode;
	}

	@Column(name = "SRC_CODE")
	public String getSrcCode() {
		return srcCode;
	}

	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}

	@Column(name = "SRC_NAME")
	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	@Column(name = "AGENDA_FORWARD_ID")
	public Long getAgendaForwardId() {
		return agendaForwardId;
	}

	public void setAgendaForwardId(Long agendaForwardId) {
		this.agendaForwardId = agendaForwardId;
	}

	@Column(name = "BUSINESS_ID")
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	@Column(name = "PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
	
}
