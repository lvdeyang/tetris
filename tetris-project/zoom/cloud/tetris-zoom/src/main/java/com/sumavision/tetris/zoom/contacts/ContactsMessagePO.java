package com.sumavision.tetris.zoom.contacts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 联系人消息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月2日 上午10:21:35
 */
@Entity
@Table(name = "TETRIS_CONTACTS_MESSAGE")
public class ContactsMessagePO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	/** 消息发送成员id */
	private String fromUserId;
	
	/** 消息发送成员会议名称 */
	private String fromUserNickname;
	
	/** 消息内容 */
	private String message;
	
	/** 消息目标成员id */
	private String toUserId;
	
	/** 消息目标成员会议名称 */
	private String toUserNickname;
	
	/** 消息类型 */
	private ContactsMessageType type;

	@Column(name = "FROM_USER_ID")
	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	@Column(name = "FROM_USER_NICKNAME")
	public String getFromUserNickname() {
		return fromUserNickname;
	}

	public void setFromUserNickname(String fromUserNickname) {
		this.fromUserNickname = fromUserNickname;
	}

	@Lob
	@Column(name = "MESSAGE", columnDefinition = "LONGTEXT")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "TO_USER_ID")
	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	@Column(name = "TO_USER_NICKNAME")
	public String getToUserNickname() {
		return toUserNickname;
	}

	public void setToUserNickname(String toUserNickname) {
		this.toUserNickname = toUserNickname;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ContactsMessageType getType() {
		return type;
	}

	public void setType(ContactsMessageType type) {
		this.type = type;
	}
	
}
