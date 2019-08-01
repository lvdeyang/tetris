package com.sumavision.tetris.mims.app.media.settings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 媒资仓库设置<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年7月17日 下午1:47:08
 */
@Entity
@Table(name = "MIMS_MEDIA_SETTINGS")
public class MediaSettingsPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 设置值 */
	private String settings;
	
	/** 公司id */
	private Long companyId;
	
	/** 设置类型 */
	private MediaSettingsType type;

	@Column(name = "SETTINGS")
	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	@Column(name = "COMPANY_ID")
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public MediaSettingsType getType() {
		return type;
	}

	public void setType(MediaSettingsType type) {
		this.type = type;
	}
	
}
