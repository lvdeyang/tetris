package com.suma.venus.resource.vo;
/**
 * 能力模型参数树节点VO
 * @author lxw
 *
 */
public class ParamTreeNodeVO {
	
	//node id
	private String id;
	
	//parent node id
	private String pid;
	
	private String name;
	
	public ParamTreeNodeVO(){}
	
	public ParamTreeNodeVO(String id, String pid, String name) {
		this.id = id;
		this.pid = pid;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
