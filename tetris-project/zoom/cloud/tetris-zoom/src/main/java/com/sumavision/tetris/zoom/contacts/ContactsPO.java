package com.sumavision.tetris.zoom.contacts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CONTACTS")
public class ContactsPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 用户id */
	private String userId;
	
	/** 联系人用户id */
	private String contactsUserId;
	
	/** 用户昵称 */
	private String contactsUserNickname;
	
	/** 重命名 */
	private String rename;
	
	/** 分组id */
	private Long sourceGroupId;
	
	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "CONTACTS_USER_ID")
	public String getContactsUserId() {
		return contactsUserId;
	}

	public void setContactsUserId(String contactsUserId) {
		this.contactsUserId = contactsUserId;
	}

	@Column(name = "CONTACTS_USER_NICKNAME")
	public String getContactsUserNickname() {
		return contactsUserNickname;
	}

	public void setContactsUserNickname(String contactsUserNickname) {
		this.contactsUserNickname = contactsUserNickname;
	}

	@Column(name = "RE_NAME")
	public String getRename() {
		return rename;
	}

	public void setRename(String rename) {
		this.rename = rename;
	}

	@Column(name = "SOURCE_GROUP_ID")
	public Long getSourceGroupId() {
		return sourceGroupId;
	}

	public void setSourceGroupId(Long sourceGroupId) {
		this.sourceGroupId = sourceGroupId;
	}
	
}
