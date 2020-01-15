package com.sumavision.bvc.command.group.setup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 指挥设置<br/>
 * @Description: <br/>
 * @author zsy 
 * @date 2019年9月25日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_SETUP")
public class CommandGroupSetupPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 设置名称 */
//	private String name;
	
	/** 呼叫应答方式 */
	private AutoOrManualType callResponseMode;

	/** 点播方式 */
	private TransmissionMode demandTransmissionMode;
	
	/** 视频转发应答方式 */
	private AutoOrManualType videoForwardResponseMode;
	
	/** 指挥会议启动模式 */
	private AutoOrManualType commandStartMode;
	
	/** 字幕设置：启用/禁用 */
	private OsdSetupType osdSetup;
	
	/** 可见范围 */
	private VisibleRangeType visibleRange;
	
	/** 录像模式 */
	private AutoOrManualType recordMode;
	
	/** 录像模式 */
	private HideVideoAudioType secretCommandMode;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "CALL_RESPONSE")
	public AutoOrManualType getCallResponseMode() {
		return callResponseMode;
	}

	public void setCallResponseMode(AutoOrManualType callResponseMode) {
		this.callResponseMode = callResponseMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DEMAND_TRANSMISSION_MODE")
	public TransmissionMode getDemandTransmissionMode() {
		return demandTransmissionMode;
	}

	public void setDemandTransmissionMode(TransmissionMode demandTransmissionMode) {
		this.demandTransmissionMode = demandTransmissionMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_FORWARD_RESPONSE_MODE")
	public AutoOrManualType getVideoForwardResponseMode() {
		return videoForwardResponseMode;
	}

	public void setVideoForwardResponseMode(AutoOrManualType videoForwardResponseMode) {
		this.videoForwardResponseMode = videoForwardResponseMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "COMMAND_START_MODE")
	public AutoOrManualType getCommandStartMode() {
		return commandStartMode;
	}

	public void setCommandStartMode(AutoOrManualType commandStartMode) {
		this.commandStartMode = commandStartMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "OSD_SETUP")
	public OsdSetupType getOsdSetup() {
		return osdSetup;
	}

	public void setOsdSetup(OsdSetupType osdSetup) {
		this.osdSetup = osdSetup;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VISIBLE_RANGE")
	public VisibleRangeType getVisibleRange() {
		return visibleRange;
	}

	public void setVisibleRange(VisibleRangeType visibleRange) {
		this.visibleRange = visibleRange;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "RECORD_MODE")
	public AutoOrManualType getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(AutoOrManualType recordMode) {
		this.recordMode = recordMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SECRET_COMMAND_MODE")
	public HideVideoAudioType getSecretCommandMode() {
		return secretCommandMode;
	}

	public void setSecretCommandMode(HideVideoAudioType secretCommandMode) {
		this.secretCommandMode = secretCommandMode;
	}
}
