package com.sumavision.tetris.bvc.model.agenda;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 自定义音频，包括：1.议程自定义音频，2.议程转发自定义音频<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月4日 上午8:57:57
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_CUSTOM_AUDIO")
public class CustomAudioPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	public static String autoAudioUidPrefix = "auto";
	
	/** 音频源id：角色音频编码通道、会场音频编码通道 */
	private Long sourceId;
	
	/** 音频源类型 */
	private SourceType sourceType;
	
	/** 关联id */
	private Long permissionId;
	
	/** 关联类型 */
	private CustomAudioPermissionType permissionType;

	@Column(name = "SOURCE_ID")
	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SOURCE_TYPE")
	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "PERMISSION_ID")
	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "PERMISSION_TYPE")
	public CustomAudioPermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(CustomAudioPermissionType permissionType) {
		this.permissionType = permissionType;
	}
	
}
