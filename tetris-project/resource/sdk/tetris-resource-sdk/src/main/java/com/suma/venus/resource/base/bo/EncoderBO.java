package com.suma.venus.resource.base.bo;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public class EncoderBO {

	private String encoderId;
	
	private String encoderName;
	
	/** 联网用 */
	private String encoderNo;
	
	private ENCODER_TYPE encoderType;
	
	public enum ENCODER_TYPE{
		JV210("jv210"), 
		ENCODER("encoder");
		
		private String name;
		
		public String getName() {
			return name;
		}

		private ENCODER_TYPE(String name){
			this.name = name;
		}
		
		public static ENCODER_TYPE fromName(String name) throws Exception{
			ENCODER_TYPE[] values = ENCODER_TYPE.values();
			for(ENCODER_TYPE value: values){
				if(value.getName().equals(name)){
					return value;
				}
			}
			
			throw new ErrorTypeException("name", name);
		}
	}

	public String getEncoderId() {
		return encoderId;
	}

	public void setEncoderId(String encoderId) {
		this.encoderId = encoderId;
	}

	public String getEncoderName() {
		return encoderName;
	}

	public void setEncoderName(String encoderName) {
		this.encoderName = encoderName;
	}

	public ENCODER_TYPE getEncoderType() {
		return encoderType;
	}

	public void setEncoderType(ENCODER_TYPE encoderType) {
		this.encoderType = encoderType;
	}

	public String getEncoderNo() {
		return encoderNo;
	}

	public void setEncoderNo(String encoderNo) {
		this.encoderNo = encoderNo;
	}
	
}
