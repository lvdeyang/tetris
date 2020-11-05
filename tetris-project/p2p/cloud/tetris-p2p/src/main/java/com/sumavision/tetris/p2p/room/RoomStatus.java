package com.sumavision.tetris.p2p.room;

public enum RoomStatus {

	CONNECT("通话中"),
	CONNECTING("呼叫中");
	
	private String name;
	
	private RoomStatus(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
