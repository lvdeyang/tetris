package com.sumavision.tetris.tool.test.flow.client.core.annotation.enumeration;

import com.sumavision.tetris.tool.test.flow.client.core.annotation.exception.ErrorTestFlowSwitchException;

/**
 * @ClassName: 测试开关 <br/>
 * @author lvdeyang
 * @date 2018年8月30日 下午2:37:17 
 */
public enum TestFlowSwitch {

	/** 关闭测试 */
	OFF("off"),
	
	/** 接口录制 */
	STORE("store"),
	
	/** 接口测试 */
	TEST("test");
	
	private String name;
	
	private TestFlowSwitch(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取测试开关<br/> 
	 * @param name 名称
	 * @throws Exception 
	 * @return TestFlowSwitch 开关
	 */
	public static TestFlowSwitch fromName(String name) throws Exception{
		TestFlowSwitch[] values = TestFlowSwitch.values();
		for(TestFlowSwitch value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTestFlowSwitchException(name);
	}
	
}
