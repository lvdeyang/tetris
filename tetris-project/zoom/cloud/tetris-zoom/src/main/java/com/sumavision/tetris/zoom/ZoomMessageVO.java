package com.sumavision.tetris.zoom;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ZoomMessageVO extends AbstractBaseVO<ZoomMessageVO, ZoomMessagePO>{

	/** 消息id */
	private Long id;
	
	/** 消息uuid */
	private String uuid;
	
	/** 消息发送时间 */
	private String updateTime;
	
	/** 消息发送成员id */
	private Long fromMemberId;
	
	/** 消息发送成员会议名称 */
	private String fromMemberRename;
	
	/** 消息内容 */
	private String message;
	
	/** 是否是广播 */
	private Boolean broadcast;
	
	/** 消息目标成员id */
	private Long toMemberId;
	
	/** 消息目标成员会议名称 */
	private String toMemberRename;
	
	/** 会议id */
	private Long zoomId;
	
	public Long getId() {
		return id;
	}

	public ZoomMessageVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public ZoomMessageVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public ZoomMessageVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Long getFromMemberId() {
		return fromMemberId;
	}

	public ZoomMessageVO setFromMemberId(Long fromMemberId) {
		this.fromMemberId = fromMemberId;
		return this;
	}

	public String getFromMemberRename() {
		return fromMemberRename;
	}

	public ZoomMessageVO setFromMemberRename(String fromMemberRename) {
		this.fromMemberRename = fromMemberRename;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ZoomMessageVO setMessage(String message) {
		this.message = message;
		return this;
	}

	public Boolean getBroadcast() {
		return broadcast;
	}

	public ZoomMessageVO setBroadcast(Boolean broadcast) {
		this.broadcast = broadcast;
		return this;
	}

	public Long getToMemberId() {
		return toMemberId;
	}

	public ZoomMessageVO setToMemberId(Long toMemberId) {
		this.toMemberId = toMemberId;
		return this;
	}

	public String getToMemberRename() {
		return toMemberRename;
	}

	public ZoomMessageVO setToMemberRename(String toMemberRename) {
		this.toMemberRename = toMemberRename;
		return this;
	}

	public Long getZoomId() {
		return zoomId;
	}

	public ZoomMessageVO setZoomId(Long zoomId) {
		this.zoomId = zoomId;
		return this;
	}

	@Override
	public ZoomMessageVO set(ZoomMessagePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setFromMemberId(entity.getFromMemberId())
			.setFromMemberRename(entity.getFromMemberRename())
			.setMessage(entity.getMessage())
			.setBroadcast(entity.getBroadcast())
			.setToMemberId(entity.getToMemberId())
			.setToMemberRename(entity.getToMemberRename());
		return this;
	}
	
}
