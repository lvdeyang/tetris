package com.sumavision.tetris.mims.app.media.settings;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaSettingsVO extends AbstractBaseVO<MediaSettingsVO, MediaSettingsPO>{

	private String settings;
	
	private Long companyId;
	
	private String type;
	
	public String getSettings() {
		return settings;
	}

	public MediaSettingsVO setSettings(String settings) {
		this.settings = settings;
		return this;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public MediaSettingsVO setCompanyId(Long companyId) {
		this.companyId = companyId;
		return this;
	}

	public String getType() {
		return type;
	}

	public MediaSettingsVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public MediaSettingsVO set(MediaSettingsPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSettings(entity.getSettings())
			.setCompanyId(entity.getCompanyId())
			.setType(entity.getType().toString());
		return this;
	}

}
