package com.sumavision.bvc.device.group.bo;

public class RecordCycleBO {
	
	private int total_size_mb;
	
	private Long max_time;
	
	private Long max_size;

	public Long getMax_time() {
		return max_time;
	}

	public RecordCycleBO setMax_time(Long max_time) {
		this.max_time = max_time;
		return this;
	}

	public Long getMax_size() {
		return max_size;
	}

	public RecordCycleBO setMax_size(Long max_size) {
		this.max_size = max_size;
		return this;
	}

	public int getTotal_size_mb() {
		return total_size_mb;
	}

	public RecordCycleBO setTotal_size_mb(int total_size_mb) {
		this.total_size_mb = total_size_mb;
		return this;
	}
}
