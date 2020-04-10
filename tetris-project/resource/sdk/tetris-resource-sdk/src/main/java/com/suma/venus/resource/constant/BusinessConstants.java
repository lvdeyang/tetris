package com.suma.venus.resource.constant;


/**
 * 跟业务有关的常量
 * @author lxw
 *
 */
public class BusinessConstants {

	public enum BUSINESS_OPR_TYPE{
		DIANBO("点播"),//点播
		RECORD("录制"),//录制
		CALL("呼叫"),//呼叫
		ZK("指挥");//指挥
		
		private String name;
		
		private BUSINESS_OPR_TYPE(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
	}
	
}
