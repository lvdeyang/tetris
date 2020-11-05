package com.sumavision.bvc.device.group.bo;

public class CycleBO {
	
	private Long total_size_mb;
	
	private Long time_duration;

	public Long getTotal_size_mb() {
		return total_size_mb;
	}

	public CycleBO setTotal_size_mb(Long total_size_mb) {
		this.total_size_mb = total_size_mb;
		return this;
	}

	public Long getTime_duration() {
		return time_duration;
	}

	public CycleBO setTime_duration(Long time_duration) {
		this.time_duration = time_duration;
		return this;
	}
	
}
