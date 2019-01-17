package com.sumavision.tetris.user;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 登录用户信息<br/>
 * <p>特殊注明：用uuid字段存储userId</p>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月19日 下午4:51:51
 */
public class UserVO extends AbstractBaseVO<UserVO, Object>{

	/** 用户名 */
	private String name;
	
	/** 头像 */
	private String icon;
	
	/** 用户状态 */
	private String status;
	
	/** 用户分类 */
	private String classify;
	
	/** 发送给当前用户的信息数目 */
	private Integer numbersOfMessage;
	
	/** 用户隶属组织id */
	private String groupId;
	
	/** 用户隶属组织名称 */
	private String groupName;
	
	/** 素材库文件夹id */
	private Long rootFolderId;
	
	/** 素材库文件夹名称 */
	private String rootFolderName;

	public String getName() {
		return name;
	}

	public UserVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public UserVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public UserVO setStatus(String status) {
		this.status = status;
		return this;
	}
	
	public String getClassify() {
		return classify;
	}

	public UserVO setClassify(String classify) {
		this.classify = classify;
		return this;
	}

	public Integer getNumbersOfMessage() {
		return numbersOfMessage;
	}

	public UserVO setNumbersOfMessage(Integer numbersOfMessage) {
		this.numbersOfMessage = numbersOfMessage;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public UserVO setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getGroupName() {
		return groupName;
	}

	public UserVO setGroupName(String groupName) {
		this.groupName = groupName;
		return this;
	}
	
	public Long getRootFolderId() {
		return rootFolderId;
	}

	public UserVO setRootFolderId(Long rootFolderId) {
		this.rootFolderId = rootFolderId;
		return this;
	}

	public String getRootFolderName() {
		return rootFolderName;
	}

	public UserVO setRootFolderName(String rootFolderName) {
		this.rootFolderName = rootFolderName;
		return this;
	}

	@Override
	public UserVO set(Object entity) throws Exception {
		return null;
	}
	
}