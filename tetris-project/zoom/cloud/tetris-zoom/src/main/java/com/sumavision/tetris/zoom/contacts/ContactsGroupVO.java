package com.sumavision.tetris.zoom.contacts;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.zoom.SourceGroupPO;
import com.sumavision.tetris.zoom.SourceGroupType;

public class ContactsGroupVO {

	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	private String name;
	
	private String type;
	
	private String userId;
	
	private Integer countOnline;
	
	private Integer countOffline;
	
	private Integer countTotal;
	
	private List<ContactsVO> contacts;
	
	public Long getId() {
		return id;
	}

	public ContactsGroupVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public ContactsGroupVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public ContactsGroupVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public ContactsGroupVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public ContactsGroupVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public ContactsGroupVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public Integer getCountOnline() {
		return countOnline;
	}

	public ContactsGroupVO setCountOnline(Integer countOnline) {
		this.countOnline = countOnline;
		return this;
	}

	public Integer getCountOffline() {
		return countOffline;
	}

	public ContactsGroupVO setCountOffline(Integer countOffline) {
		this.countOffline = countOffline;
		return this;
	}

	public Integer getCountTotal() {
		return countTotal;
	}

	public ContactsGroupVO setCountTotal(Integer countTotal) {
		this.countTotal = countTotal;
		return this;
	}

	public List<ContactsVO> getContacts() {
		return contacts;
	}

	public ContactsGroupVO setContacts(List<ContactsVO> contacts) {
		this.contacts = contacts;
		return this;
	}

	public ContactsGroupVO set(SourceGroupPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType().toString())
			.setUserId(entity.getUserId())
			.setContacts(new ArrayList<ContactsVO>());
		return this;
	}
	
	/**
	 * 获取默认联系人分组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 上午9:46:25
	 * @return ContactsGroupVO 分组
	 */
	public static ContactsGroupVO defaultGroup() throws Exception{
		ContactsGroupVO group = new ContactsGroupVO();
		group.setName("默认分组");
		group.setType(SourceGroupType.CONTACTS.toString());
		group.setContacts(new ArrayList<ContactsVO>());
		return group;
	}
	
}
