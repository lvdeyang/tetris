package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class CustomAudioVO extends AbstractBaseVO<CustomAudioVO, CustomAudioPO>{

	private Long sourceId;
	
	private String sourceName;
	
	private String sourceType;
	
	private String sourceTypeName;
	
	private Long permissionId;
	
	private String permissionType;
	
	private String permissionTypeName;
	
	public Long getSourceId() {
		return sourceId;
	}

	public CustomAudioVO setSourceId(Long sourceId) {
		this.sourceId = sourceId;
		return this;
	}

	public String getSourceName() {
		return sourceName;
	}

	public CustomAudioVO setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public String getSourceType() {
		return sourceType;
	}

	public CustomAudioVO setSourceType(String sourceType) {
		this.sourceType = sourceType;
		return this;
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public CustomAudioVO setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
		return this;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public CustomAudioVO setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
		return this;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public CustomAudioVO setPermissionType(String permissionType) {
		this.permissionType = permissionType;
		return this;
	}

	public String getPermissionTypeName() {
		return permissionTypeName;
	}

	public CustomAudioVO setPermissionTypeName(String permissionTypeName) {
		this.permissionTypeName = permissionTypeName;
		return this;
	}

	@Override
	public CustomAudioVO set(CustomAudioPO entity) throws Exception {
		this.setId(entity.getId())
			.setSourceId(entity.getSourceId())
			.setSourceType(entity.getSourceType().toString())
			.setSourceTypeName(entity.getSourceType().getName())
			.setPermissionId(entity.getPermissionId())
			.setPermissionType(entity.getPermissionType().toString())
			.setPermissionTypeName(entity.getPermissionType().getName());
		return this;
	}

}
