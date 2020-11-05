package com.sumavision.bvc.control.device.monitor.live;

/**
 * 用户双向通话任务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月22日 下午2:54:53
 */
public class MonitorUserCallVO {

	/** 任务id */
	private Long id;
	
	/** 通话用户 */
	private String targetUsername;

	public Long getId() {
		return id;
	}

	public MonitorUserCallVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getTargetUsername() {
		return targetUsername;
	}

	public MonitorUserCallVO setTargetUsername(String targetUsername) {
		this.targetUsername = targetUsername;
		return this;
	}
	
}
