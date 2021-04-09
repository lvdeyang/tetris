package com.sumavision.bvc.control.device.command.group.vo.decoder;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderSchemePO;
import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderScreenPO;

public class DecoderSchemeVO {
	
	private String schemeId;

	/** 名称 */
	private String name;

	/** 方案下的分屏信息 */
	private List<DecoderScreenVO> screens;
	
	public String getSchemeId() {
		return schemeId;
	}

	public DecoderSchemeVO setSchemeId(String schemeId) {
		this.schemeId = schemeId;
		return this;
	}

	public String getName() {
		return name;
	}

	public DecoderSchemeVO setName(String name) {
		this.name = name;
		return this;
	}

	public List<DecoderScreenVO> getScreens() {
		return screens;
	}

	public DecoderSchemeVO setScreens(List<DecoderScreenVO> screens) {
		this.screens = screens;
		return this;
	}

	private DecoderSchemeVO setScreenByPO(List<CommandGroupDecoderScreenPO> screens) {
		this.setScreens(new ArrayList<DecoderScreenVO>()); 
		if(screens == null) return this;
		for(CommandGroupDecoderScreenPO screen : screens){
			this.getScreens().add(new DecoderScreenVO().set(screen));
		}
		return this;
	}
	
	public DecoderSchemeVO set(CommandGroupDecoderSchemePO schemePO){
		this.setName(schemePO.getName());
		this.setSchemeId(String.valueOf(schemePO.getId()));
		this.setScreenByPO(schemePO.getScreens());
		
		return this;
	}
	
}
