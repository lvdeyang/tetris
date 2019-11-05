package com.sumavision.tetris.cs.channel.ability;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum BroadAbilityQueryType {
	NEW("new", "新建推流"),
	DELETE("delete", "删除推流"),
	STOP("stop", "停止推流"),
	COVER("cover", "覆盖推流"),
	SEEK("seek", "跳转"),
	CHANGE("change", "更换推流"),
	START("start", "开始推流");
	
	private String cmd;
	private String remark;

	public String getCmd() {
		return cmd;
	}
	
	public String getRemark() {
		return remark;
	}

	private BroadAbilityQueryType(String cmd, String remark){
		this.cmd = cmd;
		this.remark = remark;
	}
	
	public static BroadAbilityQueryType fromCmd(String cmd) throws Exception{
		BroadAbilityQueryType[] values = BroadAbilityQueryType.values();
		for(BroadAbilityQueryType value:values){
			if(value.getCmd().equals(cmd)){
				return value;
			}
		}
		throw new ErrorTypeException("cmd", cmd);
	}
}
