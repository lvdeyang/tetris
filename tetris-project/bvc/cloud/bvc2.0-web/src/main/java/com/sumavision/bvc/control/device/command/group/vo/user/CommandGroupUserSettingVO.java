package com.sumavision.bvc.control.device.command.group.vo.user;

import com.sumavision.bvc.command.group.user.setting.CommandGroupUserSettingPO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandGroupUserSettingVO {
	
	private String responseMode;
	
	/**  */
	private String vodMode;
	
	/**  */
	private String sendAnswerMode;
	
	/**  */
	private String commandMeetingAutoStart;
	
	/**  */
	private String subtitle;
	
	/**  */
	private String visibleRange;
	
	/**  */
	private String recordMode;
	
	/**  */
	private String dedicatedMode;

	
	
	public CommandGroupUserSettingVO set(CommandGroupUserSettingPO setting){

		this.setResponseMode(setting.getResponseMode().getCode());
		this.setVodMode(setting.getVodMode().getCode());
		this.setSendAnswerMode(setting.getSendAnswerMode().getCode());
		this.setCommandMeetingAutoStart(setting.getCommandMeetingAutoStart().getCode());
		this.setSubtitle(setting.getSubtitle().getCode());
		this.setVisibleRange(setting.getVisibleRange().getCode());
		this.setDedicatedMode(setting.getDedicatedMode().getCode());
		
		return this;
	}
	
}
