package com.sumavision.bvc.control.device.monitor.vod;

import java.util.Comparator;

public class StaticResourceVO {

	/** 文件名称 */
	private String name;
	
	/** 全路径 */
	private String fullPath;

	public String getName() {
		return name;
	}

	public StaticResourceVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getFullPath() {
		return fullPath;
	}

	public StaticResourceVO setFullPath(String fullPath) {
		this.fullPath = fullPath;
		return this;
	}
	
	public static class StaticResourceComparator implements Comparator<StaticResourceVO> {

		@Override
		public int compare(StaticResourceVO o1, StaticResourceVO o2) {
			return o1.getName().compareTo(o2.getName());
		}
		
	}
	
}
