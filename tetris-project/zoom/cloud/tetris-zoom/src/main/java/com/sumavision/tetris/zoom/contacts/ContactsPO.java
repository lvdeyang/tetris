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
	private Long userId;
	
	private String contactsUserId;
	
	/** 用户昵称 */
	private String contactsUserNickname;
	
	/** 重命名 */
	private String rename;
	
	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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
	
}
