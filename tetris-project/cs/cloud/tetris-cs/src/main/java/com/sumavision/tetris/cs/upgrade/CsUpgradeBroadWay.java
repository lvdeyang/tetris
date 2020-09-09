package com.sumavision.tetris.cs.upgrade;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum CsUpgradeBroadWay {
	BROAD_4G("4G"),
	BROAD_DTMB("DTMB");
	
	private String name;
	
	public String getName() {
		return name;
	}

	private CsUpgradeBroadWay(String name) {
		this.name = name;
	}
	
	public static CsUpgradeBroadWay fromName(String name) throws Exception{
		CsUpgradeBroadWay[] values = CsUpgradeBroadWay.values();
		for (CsUpgradeBroadWay csUpgradeBroadWay : values) {
			if (csUpgradeBroadWay.getName().equals(name)) {
				return csUpgradeBroadWay;
			}
		}
		throw new ErrorTypeException("broadWay", name);
	}
}
