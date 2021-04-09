package com.sumavision.tetris.bvc.model.terminal;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TerminalType {

	JV210("jv210", "jv210", "ENCODER_DECODER", true),
	JV220("jv220", null, null, false),
	ANDROID_TVOS("机顶盒", "tvos", "ENCODER_DECODER", true),
	QT_ZK("qt指控终端", null, null, false),
	PC_PLATFORM("web页面", null, null, false);
	
	private String name;
	
	private String devicModel;
	
	private String terminalBundleType;
	
	private Boolean singleBundle;
	
	private TerminalType(String name, String devicModel, String terminalBundleType, Boolean singleBundle){
		this.name = name;
		this.devicModel = devicModel;
		this.terminalBundleType = terminalBundleType;
		this.singleBundle = singleBundle;
	}

	public String getName(){
		return this.name;
	}
	
	public String getDevicModel() {
		return devicModel;
	}
	
	public String getTerminalBundleType(){
		return terminalBundleType;
	}
	
	public Boolean getSingleBundle(){
		return singleBundle;
	}
	
	public static TerminalType fromName(String name) throws Exception{
		TerminalType[] values = TerminalType.values();
		for(TerminalType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static TerminalType fromDeviceModel(String devicModel) throws Exception{
		TerminalType[] values = TerminalType.values();
		for(TerminalType value:values){
			if(value.getDevicModel()!=null && value.getDevicModel().equals(devicModel)){
				return value;
			}
		}
		throw new ErrorTypeException("devicModel", devicModel);
	}
	
	public static TerminalType fromTokenType(com.sumavision.tetris.auth.token.TerminalType tokenType) throws Exception{
		TerminalType[] values = TerminalType.values();
		for(TerminalType value:values){
			if(value.toString().equals(tokenType.toString())){
				return value;
			}
		}
		return null;
	}
	
	public static List<TerminalType> findBySignleBundle(Boolean singleBundle){
		List<TerminalType> types = new ArrayList<TerminalType>();
		TerminalType[] values = TerminalType.values();
		for(TerminalType value:values){
			if(value.getSingleBundle().equals(singleBundle)){
				types.add(value);
			}
		}
		return types;
	}
	
}
