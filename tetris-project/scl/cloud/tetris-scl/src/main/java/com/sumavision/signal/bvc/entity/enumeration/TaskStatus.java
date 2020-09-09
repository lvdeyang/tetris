package com.sumavision.signal.bvc.entity.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 任务状态<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年8月14日 上午10:00:38
 */
public enum TaskStatus {
	
	negative(-1l, "failed", "集群超时！"),
	zero(0l, "success", "成功"),
	one(1l, "failed", "切换失败，信息不正确"),
	two(2l, "failed", "添加失败，系统能力不够"),
	three(3l, "failed", "任务不存在"),
	four(4l, "failed", "任务参数不合法"),
	five(5l, "failed", "命令执行超时"),
	six(6l, "failed", "设备连接中断"),
	seven(7l, "failed", "设备处理命令缓慢"),
	eight(8l, "failed", "向设备发送命令失败"),
	nine(9l, "failed", "系统选择设备出错"),
	ten(10l, "failed", "设备故障，设备已经启动备份"),
	eleven(11l, "failed", "授权失败"),
	twelve(12l, "failed", "未知命令"),
	thirteen(13l, "failed", "系统初始化时无法添加任务"),
	fourteen(14l, "failed", "没有找到合适的网段（检查网段是否配置）"),
	fifteen(15l, "failed", "设备网口未授权"),
	sixteen(16l, "failed", "任务中设备接收ip字段与实际不一致"),
	seventeen(17l, "failed", "重复执行删除任务"),
	eighteen(18l, "failed", "流切发送网口码率达到最大值"),
	nineteen(19l, "failed", "流切接收网口码率达到最大值"),
	twenty(20l, "creating", "初始化中ing");
	
	private Long num;
	
	private String status;
	
	private String message;
	
	private TaskStatus(Long num, String status, String message){
		this.num = num;
		this.status = status;
		this.message = message;
	}

	public Long getNum() {
		return num;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	
	public static TaskStatus fromNum(Long num) throws Exception{
		TaskStatus[] taskStatuss = TaskStatus.values();
		for(TaskStatus taskStatus: taskStatuss){
			if(taskStatus.getNum().equals(num)){
				return taskStatus;
			}
		}
		throw new ErrorTypeException("num", num.toString());
	}
	
	
	
}
