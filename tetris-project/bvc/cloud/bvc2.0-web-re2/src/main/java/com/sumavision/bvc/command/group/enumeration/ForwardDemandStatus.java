package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: AgendaForwardDemandPO 的执行状态
 * @Description: 完成、执行中、未执行
 * @author zsy
 * @date 2019年11月14日 下午1:22:00
 */
public enum ForwardDemandStatus {
	
	DONE("完成", "转发中"),
//	DOING("执行中"),
//	ACCEPT("已同意"),//需要吗？
	DENY("拒绝", "停止"),//指挥转发点播时
	WAITING_FOR_RESPONSE("执行完成", "执行完成"),//指挥转发点播时
//	STOP("停止", "停止"),//指挥转发点播时，主席或成员自己将点播停止
//	PAUSE("暂停"),
	NO_AVAILABLE_PLAYER("没有可用播放器", "停止"),//指挥转发点播时表示已同意但没有播放器可用
	UNDONE("未执行", "停止");//停会时都处于UNDONE；开会之后UNDONE可能是成员未连接、尚未执行、成员或主席主动将其停止、因其它业务而暂停，但成员连接后只有找到播放器才能是UNDONE

	private String name;
	
	private String code;
	
	private ForwardDemandStatus(String name, String code){
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	public static ForwardDemandStatus fromName(String name) throws Exception{
		ForwardDemandStatus[] values = ForwardDemandStatus.values();
		for(ForwardDemandStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
