package com.sumavision.tetris.zoom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 会议消息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月2日 上午10:20:54
 */
@Entity
@Table(name = "TETRIS_ZOOM_MESSAGE")
public class ZoomMessagePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

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

	@Column(name = "FROM_MEMBER_ID")
	public Long getFromMemberId() {
		return fromMemberId;
	}

	public void setFromMemberId(Long fromMemberId) {
		this.fromMemberId = fromMemberId;
	}

	@Column(name = "FROM_MEMBER_RENAME")
	public String getFromMemberRename() {
		return fromMemberRename;
	}

	public void setFromMemberRename(String fromMemberRename) {
		this.fromMemberRename = fromMemberRename;
	}

	@Lob
	@Column(name = "MESSAGE", columnDefinition = "LONGTEXT")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "BROADCAST")
	public Boolean getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(Boolean broadcast) {
		this.broadcast = broadcast;
	}

	@Column(name = "TO_MEMBER_ID")
	public Long getToMemberId() {
		return toMemberId;
	}

	public void setToMemberId(Long toMemberId) {
		this.toMemberId = toMemberId;
	}

	@Column(name = "TO_MEMBER_RENAME")
	public String getToMemberRename() {
		return toMemberRename;
	}

	public void setToMemberRename(String toMemberRename) {
		this.toMemberRename = toMemberRename;
	}

	@Column(name = "ZOOM_ID")
	public Long getZoomId() {
		return zoomId;
	}

	public void setZoomId(Long zoomId) {
		this.zoomId = zoomId;
	}
	
}
