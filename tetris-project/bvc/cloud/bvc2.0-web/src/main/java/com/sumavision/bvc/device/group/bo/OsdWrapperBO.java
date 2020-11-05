package com.sumavision.bvc.device.group.bo;

import java.util.Collection;

public class OsdWrapperBO {

	/** 源的设备号码 */
	private String devid;
	
	/** 源的设备名称 */
	private String devname;
	
	/** 分辨率 */
	private String resolution;
	
	/** 图层列表 */
	private Collection<OsdBO> layers;

	public String getDevid() {
		return devid;
	}

	public OsdWrapperBO setDevid(String devid) {
		this.devid = devid;
		return this;
	}

	public String getDevname() {
		return devname;
	}

	public OsdWrapperBO setDevname(String devname) {
		this.devname = devname;
		return this;
	}

	public String getResolution() {
		return resolution;
	}

	public OsdWrapperBO setResolution(String resolution) {
		this.resolution = resolution;
		return this;
	}

	public Collection<OsdBO> getLayers() {
		return layers;
	}

	public OsdWrapperBO setLayers(Collection<OsdBO> layers) {
		this.layers = layers;
		return this;
	}
	
}
