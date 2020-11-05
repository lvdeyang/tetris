package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: CommandGroupForwardPO 的执行状态
 * @Description: 完成、执行中、未执行
 * @author zsy
 * @date 2019年9月20日 下午1:22:00
 */
public enum ExecuteStatus {
	
	DONE("完成"),
//	DOING("执行中"),
//	ACCEPT("已同意"),//需要吗？
//	DENY("拒绝"),//指挥转发点播时
//	WAITING_FOR_RESPONSE("等待回复"),//指挥转发点播时
	STOP("停止"),//主席或成员主动将其停止
//	PAUSE("暂停"),
	NO_AVAILABLE_PLAYER("没有可用播放器"),//指挥转发点播时表示已同意但没有播放器可用
	UNDONE("未执行");//停会时都处于UNDONE；开会之后UNDONE可能是成员未连接、尚未执行、因其它业务而暂停，但成员连接后只有找到播放器才能是UNDONE

	private String name;
	
	private ExecuteStatus(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static ExecuteStatus fromName(String name) throws Exception{
		ExecuteStatus[] values = ExecuteStatus.values();
		for(ExecuteStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
