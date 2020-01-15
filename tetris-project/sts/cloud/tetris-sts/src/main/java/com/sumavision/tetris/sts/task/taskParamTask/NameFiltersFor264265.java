package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;
import java.util.ArrayList;

import com.alibaba.fastjson.serializer.NameFilter;

public class NameFiltersFor264265 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 303944209356545976L;
	private EncodeCommon encodeCommon;
	private ArrayList<NameFilter> nameFilters = new ArrayList<NameFilter>();
	public EncodeCommon getEncodeCommon() {
		return encodeCommon;
	}
	public void setEncodeCommon(EncodeCommon encodeCommon) {
		this.encodeCommon = encodeCommon;
	}
	public ArrayList<NameFilter> getNameFilters() {
		return nameFilters;
	}
	public void setNameFilters(ArrayList<NameFilter> nameFilters) {
		this.nameFilters = nameFilters;
	}
	

}
