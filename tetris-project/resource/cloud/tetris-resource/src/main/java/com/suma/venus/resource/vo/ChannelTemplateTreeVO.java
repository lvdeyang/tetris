package com.suma.venus.resource.vo;

import java.util.List;

/**
 * 能力模板VO
 * @author lxw
 *
 */
public class ChannelTemplateTreeVO {
	
	private String deviceModel;
	
	private List<ParamTreeNodeVO> paramTree;

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public List<ParamTreeNodeVO> getParamTree() {
		return paramTree;
	}

	public void setParamTree(List<ParamTreeNodeVO> paramTree) {
		this.paramTree = paramTree;
	}
	
}
