package com.sumavision.tetris.zoom.contacts;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ContactsVO extends AbstractBaseVO<ContactsVO, ContactsPO>{

	/** 用户id */
	private String userId;
	
	/** 用户昵称 */
	private String userNickname;
	
	/** 重命名 */
	private String rename;
	
	/** 联系人状态 */
	private String status;
	
	/** 在线终端类型 */
	private List<String> onlineTerminalTypes;
	
	public String getUserId() {
		return userId;
	}

	public ContactsVO setUserId(String userId) {
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

	public String getStatus() {
		return status;
	}

	public ContactsVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public List<String> getOnlineTerminalTypes() {
		return onlineTerminalTypes;
	}

	public ContactsVO setOnlineTerminalTypes(List<String> onlineTerminalTypes) {
		this.onlineTerminalTypes = onlineTerminalTypes;
		return this;
	}

	@Override
	public ContactsVO set(ContactsPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUserId(entity.getContactsUserId())
			.setUserNickname(entity.getContactsUserNickname())
			.setRename(rename);
		return this;
	}
	
}
