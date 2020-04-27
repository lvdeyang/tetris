package com.suma.venus.resource.vo;

import java.util.List;

public class NodeInfoVO {

	private String seq;
	
	private String cmd;
	
	private String layer_id;
	
	private NodeVO self;
	
	private List<NodeVO> supers;
	
	private List<NodeVO> subors;
	
	private List<NodeVO> relations;

	public String getSeq() {
		return seq;
	}

	public NodeInfoVO setSeq(String seq) {
		this.seq = seq;
		return this;
	}

	public String getCmd() {
		return cmd;
	}

	public NodeInfoVO setCmd(String cmd) {
		this.cmd = cmd;
		return this;
	}

	public String getLayer_id() {
		return layer_id;
	}

	public NodeInfoVO setLayer_id(String layer_id) {
		this.layer_id = layer_id;
		return this;
	}

	public NodeVO getSelf() {
		return self;
	}

	public NodeInfoVO setSelf(NodeVO self) {
		this.self = self;
		return this;
	}

	public List<NodeVO> getSupers() {
		return supers;
	}

	public NodeInfoVO setSupers(List<NodeVO> supers) {
		this.supers = supers;
		return this;
	}

	public List<NodeVO> getSubors() {
		return subors;
	}

	public NodeInfoVO setSubors(List<NodeVO> subors) {
		this.subors = subors;
		return this;
	}

	public List<NodeVO> getRelations() {
		return relations;
	}

	public NodeInfoVO setRelations(List<NodeVO> relations) {
		this.relations = relations;
		return this;
	}
	
}
