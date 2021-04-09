package com.sumavision.bvc.command.group.forward;

import java.util.Date;

import com.sumavision.bvc.command.group.enumeration.ForwardDemandBusinessType;
import com.sumavision.bvc.command.group.enumeration.ForwardDemandStatus;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AgendaForwardDemandVO extends AbstractBaseVO<AgendaForwardDemandVO, AgendaForwardDemandPO>{
	
	/** 转发的业务类型 */
	private String forwardType;
	
	/** 开始转发时间*/
	private String beginForwardTime;
	
	/** 执行状态 (取code属性)*/
	private String executeStatus;
	
	/** 目的号码 */
	private String dstCode;
	
	/** 目标成员名称（用于显示） */
	private String dstUserName;
	
	/** 源号码 */
	private String srcCode;
	
	/** 源名称（设备是设备名称，成员是成员名称）*/
	private String srcName;
	
	/** 关联的议程转发的id*/
	private String agendaForwardId;

	public String getForwardType() {
		return forwardType;
	}

	public AgendaForwardDemandVO setForwardType(String forwardType) {
		this.forwardType = forwardType;
		return this;
	}

	public String getBeginForwardTime() {
		return beginForwardTime;
	}

	public AgendaForwardDemandVO setBeginForwardTime(String beginForwardTime) {
		this.beginForwardTime = beginForwardTime;
		return this;
	}

	public String getExecuteStatus() {
		return executeStatus;
	}

	public AgendaForwardDemandVO setExecuteStatus(String executeStatus) {
		this.executeStatus = executeStatus;
		return this;
	}

	public String getDstCode() {
		return dstCode;
	}

	public AgendaForwardDemandVO setDstCode(String dstCode) {
		this.dstCode = dstCode;
		return this;
	}

	public String getDstUserName() {
		return dstUserName;
	}

	public AgendaForwardDemandVO setDstUserName(String dstUserName) {
		this.dstUserName = dstUserName;
		return this;
	}

	public String getSrcCode() {
		return srcCode;
	}

	public AgendaForwardDemandVO setSrcCode(String srcCode) {
		this.srcCode = srcCode;
		return this;
	}

	public String getSrcName() {
		return srcName;
	}

	public AgendaForwardDemandVO setSrcName(String srcName) {
		this.srcName = srcName;
		return this;
	}

	public String getAgendaForwardId() {
		return agendaForwardId;
	}

	public AgendaForwardDemandVO setAgendaForwardId(String agendaForwardId) {
		this.agendaForwardId = agendaForwardId;
		return this;
	}

	@Override
	public AgendaForwardDemandVO set(AgendaForwardDemandPO entity) throws Exception {
		this.setId(entity.getId())
			.setForwardType(entity.getForwardType()==null?"-":entity.getForwardType().getName())
			.setBeginForwardTime(entity.getBeginForwardTime()==null?"-":DateUtil.format(entity.getBeginForwardTime(), DateUtil.dateTimePattern))
			.setExecuteStatus(entity.getExecuteStatus()==null?"-":entity.getExecuteStatus().getCode())
			.setDstCode(entity.getDstCode()==null?"-":entity.getDstCode())
			.setDstUserName(entity.getDstUserName()==null?"-":entity.getDstUserName())
			.setSrcCode(entity.getSrcCode()==null?"-":entity.getSrcCode())
			.setSrcName(entity.getSrcName()==null?"-":entity.getSrcName())
			.setAgendaForwardId(entity.getAgendaForwardId()==null?"-":entity.getAgendaForwardId().toString());
		return this;
	}

}
