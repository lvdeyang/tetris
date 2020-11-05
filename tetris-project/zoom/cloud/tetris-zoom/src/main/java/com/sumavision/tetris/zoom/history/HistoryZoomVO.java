package com.sumavision.tetris.zoom.history;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.zoom.ZoomPO;

public class HistoryZoomVO {

	private Long id;
	
	private String code;
	
	private String name;
	
	private String createTime;
	
	private String creatorUserId;
	
	private String creatorUserNickname;
	
	private String creatorUserRename;

	public Long getId() {
		return id;
	}

	public HistoryZoomVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getCode() {
		return code;
	}

	public HistoryZoomVO setCode(String code) {
		this.code = code;
		return this;
	}

	public String getName() {
		return name;
	}

	public HistoryZoomVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public HistoryZoomVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getCreatorUserId() {
		return creatorUserId;
	}

	public HistoryZoomVO setCreatorUserId(String creatorUserId) {
		this.creatorUserId = creatorUserId;
		return this;
	}

	public String getCreatorUserNickname() {
		return creatorUserNickname;
	}

	public HistoryZoomVO setCreatorUserNickname(String creatorUserNickname) {
		this.creatorUserNickname = creatorUserNickname;
		return this;
	}

	public String getCreatorUserRename() {
		return creatorUserRename;
	}

	public HistoryZoomVO setCreatorUserRename(String creatorUserRename) {
		this.creatorUserRename = creatorUserRename;
		return this;
	}
	
	public HistoryZoomVO set(ZoomPO entity){
		this.setCode(entity.getCode())
			.setName(entity.getName())
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setCreatorUserId(entity.getCreatorUserId().toString())
			.setCreatorUserNickname(entity.getCreatorUserNickname())
			.setCreatorUserRename(entity.getCreatorUserRename());
		return this;
	}
	
	public HistoryZoomVO set(ZoomPO entity, HistoryPO history){
		this.setId(history.getId())
			.setCode(entity.getCode())
			.setName(entity.getName())
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setCreatorUserId(entity.getCreatorUserId().toString())
			.setCreatorUserNickname(entity.getCreatorUserNickname())
			.setCreatorUserRename(entity.getCreatorUserRename());
		return this;
	}
	
}
