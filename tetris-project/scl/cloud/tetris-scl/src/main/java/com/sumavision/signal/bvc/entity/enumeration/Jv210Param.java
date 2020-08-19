package com.sumavision.signal.bvc.entity.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum Jv210Param {

	G711A("pcma", "0", "0"),
	G711U("pcmu", "0", "1"),
	G729("g729", "1", ""),
	AAC("aac", "2", ""),
	
	//协议画面：远端1--"0",远端2--"1",本地1--"2",本地2--"3"
	VIDEOENCODE1("VenusVideoIn_1", "2", ""),
	VIDEOENCODE2("VenusVideoIn_2", "3", ""),
	VIDEODECODE1("VenusVideoOut_1", "0", ""),
	VIDEODECODE2("VenusVideoOut_2", "1", "");

	private String name;
	
	private String value;
	
	private String relation;
	
	private Jv210Param(String name, String value, String relation){
		this.name = name;
		this.value = value;
		this.relation = relation;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getRelation() {
		return relation;
	}
	
	public static Jv210Param fromName(String name) throws Exception{
		Jv210Param[] values = Jv210Param.values();
		for(Jv210Param value: values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	
}
