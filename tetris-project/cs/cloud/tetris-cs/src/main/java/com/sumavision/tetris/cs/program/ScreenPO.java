package com.sumavision.tetris.cs.program;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CS_SCREEN")
public class ScreenPO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long programId;
	private Long serialNum;
	private Long screenIndex;
	private Long resourceId;
	private Long mimsId;
	private String name;
	private String previewUrl;

	@Column(name="PROGRAM_ID")
	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	@Column(name="SERIAL_NUM")
	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}
	
	@Column(name="SCREEN_INDEX")
	public void setScreenIndex(Long screenIndex) {
		this.screenIndex = screenIndex;
	}
	
	public Long getScreenIndex() {
		return screenIndex;
	}
	
	@Column(name="RESOURCE_ID")
	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	@Column(name="MIMS_ID")
	public Long getMimsId() {
		return mimsId;
	}

	public void setMimsId(Long mimsId) {
		this.mimsId = mimsId;
	}

	@Column(name="NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="PREVIEW_URL")
	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
	
	
}
