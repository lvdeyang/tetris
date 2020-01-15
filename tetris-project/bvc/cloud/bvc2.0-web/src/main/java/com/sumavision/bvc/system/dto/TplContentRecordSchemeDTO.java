package com.sumavision.bvc.system.dto;

/**
 * @ClassName: 会议模板内容——录制方案 
 * @author lvdeyang
 * @date 2018年7月30日 上午10:21:39 
 */
public class TplContentRecordSchemeDTO {

	/** 会议模板内容id */
	private Long contentId;
	
	/** 会议模板id */
	private Long tplId;
	
	/** 录制方案id */
	private Long recordId;

	/** 录制方案名称 */
	private String recordName;
	
	public TplContentRecordSchemeDTO(){}
	
	public TplContentRecordSchemeDTO(Long contentId, Long tplId, Long recordId, String recordName){
		this.contentId = contentId;
		this.tplId = tplId;
		this.recordId = recordId;
		this.recordName = recordName;
	}

	public Long getContentId() {
		return contentId;
	}

	public TplContentRecordSchemeDTO setContentId(Long contentId) {
		this.contentId = contentId;
		return this;
	}

	public Long getTplId() {
		return tplId;
	}

	public TplContentRecordSchemeDTO setTplId(Long tplId) {
		this.tplId = tplId;
		return this;
	}

	public Long getRecordId() {
		return recordId;
	}

	public TplContentRecordSchemeDTO setRecordId(Long recordId) {
		this.recordId = recordId;
		return this;
	}

	public String getRecordName() {
		return recordName;
	}

	public TplContentRecordSchemeDTO setRecordName(String recordName) {
		this.recordName = recordName;
		return this;
	}
	
}
