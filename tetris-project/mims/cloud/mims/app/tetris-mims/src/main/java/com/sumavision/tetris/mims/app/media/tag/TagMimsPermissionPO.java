package com.sumavision.tetris.mims.app.media.tag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_TAG_MIMS_PERMISSION")
public class TagMimsPermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 标签id */
	private Long tagId;
	
	/** 媒资id */
	private Long mimsId;
	
	/** 媒资类型 */
	private FolderType mimsType;

	@Column(name = "TAG_ID")
	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	@Column(name = "MIMS_ID")
	public Long getMimsId() {
		return mimsId;
	}

	public void setMimsId(Long mimsId) {
		this.mimsId = mimsId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "MIMS_TYPE")
	public FolderType getMimsType() {
		return mimsType;
	}

	public void setMimsType(FolderType mimsType) {
		this.mimsType = mimsType;
	}
}
