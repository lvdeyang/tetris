package com.sumavision.tetris.mims.app.media.api.server;

public class ServerRemoveVO {
	/** 删除媒资的类型 */
	private String type;
	/** 删除媒资的id组，用"，"分割 */
	private String materialIds;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMaterialIds() {
		return materialIds;
	}
	
	public void setMaterialIds(String materialIds) {
		this.materialIds = materialIds;
	}
}
