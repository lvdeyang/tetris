package com.sumavision.tetris.bvc.business.dispatch.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 调度的执行状态
 * @Description: 完成、执行中、未执行
 * @author zsy
 * @date 2019年9月20日 下午1:22:00
 */
public enum DispatchStatus {

	RECORD_ONLY("仅记录"),//仅记录，不调度，用于webrtc等内部拉流
	DONE("完成");
//	STOP("停止"),
//	PAUSE("暂停"),
//	UNDONE("未执行");

	private String name;
	
	private DispatchStatus(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static DispatchStatus fromName(String name) throws Exception{
		DispatchStatus[] values = DispatchStatus.values();
		for(DispatchStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
