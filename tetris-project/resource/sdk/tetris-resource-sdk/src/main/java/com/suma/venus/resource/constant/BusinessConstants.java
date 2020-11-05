package com.suma.venus.resource.constant;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 跟业务有关的常量
 * @author lxw
 *
 */
public class BusinessConstants {

	public enum BUSINESS_OPR_TYPE{
		DIANBO("点播","-w"),//点播
		RECORD("录制","-r"),//录制
		CLOUD("云台","-c"),//云台
		CALL("呼叫","-hj"),//呼叫
		ZK("指挥","-zk"),//指挥
		HY("会议","-hy"),//会议
		LR("本地录制","-lr"),//本地录制
		DOWNLOAD("下载","-d");//下载
		
		private String name;
		
		private String code;
		
		private BUSINESS_OPR_TYPE(String name,String code){
			this.name = name;
			this.code = code;
		}
		
		public String getName(){
			return this.name;
		}

		public String getCode() {
			return code;
		}
		
		public static BUSINESS_OPR_TYPE forCode(String code) throws ErrorTypeException{
			for(BUSINESS_OPR_TYPE type:BUSINESS_OPR_TYPE.values()){
				if(type.getCode().equals(code)){
					return type;
				}
			}
			throw new ErrorTypeException("code", code);
		}
		
		public static BUSINESS_OPR_TYPE forName(String name) throws ErrorTypeException{
			for(BUSINESS_OPR_TYPE type:BUSINESS_OPR_TYPE.values()){
				if(type.getName().equals(name)){
					return type;
				}
			}
			throw new ErrorTypeException("name", name);
		}
	}
	
}
