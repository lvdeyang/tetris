package com.sumavision.tetris.zoom.contacts;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ContactsVO extends AbstractBaseVO<ContactsVO, ContactsPO>{

	/** 用户id */
	private Long userId;
	
	/** 用户昵称 */
	private String userNickname;
	
	/** 重命名 */
	private String rename;
	
	public Long getUserId() {
		return userId;
	}

	public ContactsVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public ContactsVO setUserNickname(String userNickname) {
		this.userNickname = userNickname;
		return this;
	}

	public String getRename() {
		return rename;
	}

	public ContactsVO setRename(String rename) {
		this.rename = rename;
		return this;
	}

	@Override
	public ContactsVO set(ContactsPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUserId(userId)
			.setUserNickname(userNickname)
			.setRename(rename);
		return this;
	}
	
}
