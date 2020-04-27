package com.sumavision.tetris.zoom.contacts;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 联系人消息<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月2日 上午10:21:35
 */
public class ContactsMessageVO extends AbstractBaseVO<ContactsMessageVO, ContactsMessagePO>{

	/** 消息发送成员id */
	private String fromUserId;
	
	/** 消息发送者在对方联系人中的名称 */
	private String fromUsername;
	
	/** 消息发送者在对方联系人中的id */
	private Long fromContactsId;
	
	/** 消息发送者在对方联系人中的分组id */
	private Long fromSourceGroupId;
	
	/** 消息内容 */
	private String message;
	
	/** 消息类型 */
	private String type;
	
	public String getFromUserId() {
		return fromUserId;
	}

	public ContactsMessageVO setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
		return this;
	}

	public String getFromUsername() {
		return fromUsername;
	}

	public ContactsMessageVO setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
		return this;
	}

	public Long getFromContactsId() {
		return fromContactsId;
	}

	public ContactsMessageVO setFromContactsId(Long fromContactsId) {
		this.fromContactsId = fromContactsId;
		return this;
	}

	public Long getFromSourceGroupId() {
		return fromSourceGroupId;
	}

	public ContactsMessageVO setFromSourceGroupId(Long fromSourceGroupId) {
		this.fromSourceGroupId = fromSourceGroupId;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ContactsMessageVO setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getType() {
		return type;
	}

	public ContactsMessageVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public ContactsMessageVO set(ContactsMessagePO entity) throws Exception {
		/*this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setFromUserId(entity.getFromUserId())
			.setFromUserNickname(entity.getToContactsRename()==null?entity.getToUserNickname():entity.getToContactsRename())
			.setMessage(entity.getMessage());*/
		return this;
	}
	
}
