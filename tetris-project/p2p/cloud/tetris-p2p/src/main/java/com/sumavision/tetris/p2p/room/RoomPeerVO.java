package com.sumavision.tetris.p2p.room;

/**
 * 用户数据<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月11日 上午9:22:20
 */
public class RoomPeerVO {

	/** 用户名 */
	private String name;
	
	/** 用户id */
	private Long id;
	
	/** 是否在线 */
	private Boolean isOnline;

	public String getName() {
		return name;
	}

	public RoomPeerVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getId() {
		return id;
	}

	public RoomPeerVO setId(Long id) {
		this.id = id;
		return this;
	}

	public Boolean getIsOnline() {
		return isOnline;
	}

	public RoomPeerVO setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
		return this;
	}
	
}
