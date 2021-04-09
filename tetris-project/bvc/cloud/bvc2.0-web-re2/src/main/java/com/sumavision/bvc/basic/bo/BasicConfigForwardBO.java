package com.sumavision.bvc.basic.bo;

import java.util.List;

public class BasicConfigForwardBO {
	
	private String type;
	
	private BasicConfigForwardSrcBO src;
	
	private List<BasicConfigForwardDstBO> dsts;

	public String getType() {
		return type;
	}

	public BasicConfigForwardBO setType(String type) {
		this.type = type;
		return this;
	}

	public BasicConfigForwardSrcBO getSrc() {
		return src;
	}

	public BasicConfigForwardBO setSrc(BasicConfigForwardSrcBO src) {
		this.src = src;
		return this;
	}

	public List<BasicConfigForwardDstBO> getDsts() {
		return dsts;
	}

	public BasicConfigForwardBO setDsts(List<BasicConfigForwardDstBO> dsts) {
		this.dsts = dsts;
		return this;
	}
}
