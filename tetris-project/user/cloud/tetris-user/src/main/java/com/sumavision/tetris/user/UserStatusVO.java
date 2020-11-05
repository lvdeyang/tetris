package com.sumavision.tetris.user;

/**
 * 专用查询用户状态<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年4月14日 上午11:12:47
 */
public class UserStatusVO {

	/** 用户id */
	private Long id;
	
	/** 用户状态 */
	private UserStatus status;

	public UserStatusVO(Long id, String status){
		this.id = id;
		this.status = UserStatus.valueOf(status);
	}
	
	public Long getId() {
		return id;
	}

	public UserStatusVO setId(Long id) {
		this.id = id;
		return this;
	}

	public UserStatus getStatus() {
		return status;
	}

	public UserStatusVO setStatus(UserStatus status) {
		this.status = status;
		return this;
	}
	
}
