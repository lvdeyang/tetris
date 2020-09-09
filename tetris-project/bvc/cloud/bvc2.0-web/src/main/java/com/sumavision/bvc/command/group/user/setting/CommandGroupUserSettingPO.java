package com.sumavision.bvc.command.group.user.setting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 用户设置<br/>
 * @Description: <br/>
 * @author zsy 
 * @date 2019年9月20日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_GROUP_USER_SETTING")
public class CommandGroupUserSettingPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/**  */
	private AutoManualMode responseMode = AutoManualMode.MANUAL;
	
	/**  */
	private CastMode vodMode = CastMode.UNICAST;
	
	/**  */
	private AutoManualMode sendAnswerMode = AutoManualMode.MANUAL;
	
	/**  */
	private AutoManualMode commandMeetingAutoStart = AutoManualMode.MANUAL;
	
	/**  */
	private OpenCloseMode subtitle = OpenCloseMode.CLOSE;
	
	/**  */
	private VisibleRange visibleRange = VisibleRange.ALL;
	
	/**  */
	private AutoManualMode recordMode = AutoManualMode.MANUAL;
	
	/**  */
	private SecretMode dedicatedMode = SecretMode.NO_AUDIO_VIDEO;

	/** 关联用户信息 */
	private CommandGroupUserInfoPO userInfo;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "RESPONSE_MODE")
	public AutoManualMode getResponseMode() {
		return responseMode;
	}

	public void setResponseMode(AutoManualMode responseMode) {
		this.responseMode = responseMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VOD_MODE")
	public CastMode getVodMode() {
		return vodMode;
	}

	public void setVodMode(CastMode vodMode) {
		this.vodMode = vodMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SEND_ANSWER_MODE")
	public AutoManualMode getSendAnswerMode() {
		return sendAnswerMode;
	}

	public void setSendAnswerMode(AutoManualMode sendAnswerMode) {
		this.sendAnswerMode = sendAnswerMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "COMMAND_MEETING_AUTO_START")
	public AutoManualMode getCommandMeetingAutoStart() {
		return commandMeetingAutoStart;
	}

	public void setCommandMeetingAutoStart(AutoManualMode commandMeetingAutoStart) {
		this.commandMeetingAutoStart = commandMeetingAutoStart;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SUBTITLE")
	public OpenCloseMode getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(OpenCloseMode subtitle) {
		this.subtitle = subtitle;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VISIBLE_RANGE")
	public VisibleRange getVisibleRange() {
		return visibleRange;
	}

	public void setVisibleRange(VisibleRange visibleRange) {
		this.visibleRange = visibleRange;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "RECORD_MODE")
	public AutoManualMode getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(AutoManualMode recordMode) {
		this.recordMode = recordMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DEDICATED_MODE")
	public SecretMode getDedicatedMode() {
		return dedicatedMode;
	}

	public void setDedicatedMode(SecretMode dedicatedMode) {
		this.dedicatedMode = dedicatedMode;
	}

	@OneToOne
	@JoinColumn(name = "USER_ID")
	public CommandGroupUserInfoPO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(CommandGroupUserInfoPO userInfo) {
		this.userInfo = userInfo;
	}
	
}
