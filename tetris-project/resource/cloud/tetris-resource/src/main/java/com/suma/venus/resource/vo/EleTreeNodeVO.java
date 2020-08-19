package com.suma.venus.resource.vo;

import java.util.List;

/**
 * element-ui 树插件节点数据结构体
 * @author Administrator
 *
 */
public class EleTreeNodeVO {

	/**节点名称*/
	private String label;
	
	/**子节点*/
	private List<EleTreeNodeVO> children;
	
	public EleTreeNodeVO() {}
	
	
	public EleTreeNodeVO(String label) {
		this.label = label;
	}

	public EleTreeNodeVO(String label, List<EleTreeNodeVO> children) {
		this.label = label;
		this.children = children;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<EleTreeNodeVO> getChildren() {
		return children;
	}

	public void setChildren(List<EleTreeNodeVO> children) {
		this.children = children;
	}
	
}
