package com.sumavision.tetris.test.flow.enumeration;

import com.sumavision.tetris.test.flow.exception.ErrorReportStatusException;

/**
 * @ClassName: 测试结果<br/> 
 * @author lvdeyang
 * @date 2018年8月30日 下午5:42:04 
 */
public enum ReportStatus {

	SUCCESS("成功"),
	FAIL("失败"),
	UNEQUAL("结果不一致");
	
	private String name;
	
	private ReportStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ReportStatus fromName(String name) throws Exception{
		ReportStatus[] values = ReportStatus.values();
		for(ReportStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorReportStatusException(name);
	}
	
}
