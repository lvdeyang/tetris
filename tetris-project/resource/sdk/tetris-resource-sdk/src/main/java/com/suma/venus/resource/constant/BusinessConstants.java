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
		CLOUD("云台"),//云台
		CALL("呼叫"),//呼叫
		ZK("指挥"),//指挥
		HY("会议"),//会议
		LR("本地录制"),//本地录制
		DOWNLOAD("下载");//下载
		
		private String name;
		
		private BUSINESS_OPR_TYPE(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
	}
	
}
