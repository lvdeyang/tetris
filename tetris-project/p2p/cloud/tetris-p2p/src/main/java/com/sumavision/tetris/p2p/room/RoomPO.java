package com.sumavision.tetris.p2p.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 点对点房间<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月10日 下午1:38:09
 */
@Entity
@Table(name = "TETRIS_P2P_ROOM")
public class RoomPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 发起人用户id */
	private Long initiatorId;
	
	/** 发起人用户名 */
	private String initiatorName;
	
	/** 房间唯一标识 */
	private String roomUuid;
	
	/** 被呼叫人用户id */
	private Long calleeId;
	
	/** 被呼叫人用户名  */
	private String calleeName;
	
	/** 房间状态 */
	private RoomStatus status;
	
	/** 消息id */
	private Long messageId;

	@Column(name = "INITIATOR_ID")
	public Long getInitiatorId() {
		return initiatorId;
	}

	public void setInitiatorId(Long initiatorId) {
		this.initiatorId = initiatorId;
	}

	@Column(name = "INITIATOR_NAME")
	public String getInitiatorName() {
		return initiatorName;
	}

	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}

	@Column(name = "ROOM_UUID")
	public String getRoomUuid() {
		return roomUuid;
	}

	public void setRoomUuid(String roomUuid) {
		this.roomUuid = roomUuid;
	}

	@Column(name = "CALLEE_ID")
	public Long getCalleeId() {
		return calleeId;
	}

	public void setCalleeId(Long calleeId) {
		this.calleeId = calleeId;
	}

	@Column(name = "CALLEE_NAME")
	public String getCalleeName() {
		return calleeName;
	}

	public void setCalleeName(String calleeName) {
		this.calleeName = calleeName;
	}

	@Column(name = "STATUS")
	public RoomStatus getStatus() {
		return status;
	}

	public void setStatus(RoomStatus status) {
		this.status = status;
	}

	@Column(name = "MESSAGE_ID")
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	
}
