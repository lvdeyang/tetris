package com.sumavision.tetris.bvc.business.dispatch.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DispatchBO 
 * @Description: 调度协议
 * @author zsy
 * @date 2020年3月11日 上午11:14:03 
 */
public class DispatchBO {
	
	private List<StartBundleDispatchBO> startBundleDispatch = new ArrayList<StartBundleDispatchBO>();
	
	private List<StartUserDispatchBO> startUserDispatch = new ArrayList<StartUserDispatchBO>();
	
	private List<StopBundleDispatchBO> stopBundleDispatch = new ArrayList<StopBundleDispatchBO>();
	
	private List<StopTaskDispatchBO> stopTaskDispatch = new ArrayList<StopTaskDispatchBO>();
	
	private List<StopUserDispatchBO> stopUserDispatch = new ArrayList<StopUserDispatchBO>();

	public List<StartBundleDispatchBO> getStartBundleDispatch() {
		return startBundleDispatch;
	}

	public DispatchBO setStartBundleDispatch(List<StartBundleDispatchBO> startBundleDispatch) {
		this.startBundleDispatch = startBundleDispatch;
		return this;
	}

	public List<StartUserDispatchBO> getStartUserDispatch() {
		return startUserDispatch;
	}

	public DispatchBO setStartUserDispatch(List<StartUserDispatchBO> startUserDispatch) {
		this.startUserDispatch = startUserDispatch;
		return this;
	}

	public List<StopBundleDispatchBO> getStopBundleDispatch() {
		return stopBundleDispatch;
	}

	public DispatchBO setStopBundleDispatch(List<StopBundleDispatchBO> stopBundleDispatch) {
		this.stopBundleDispatch = stopBundleDispatch;
		return this;
	}

	public List<StopTaskDispatchBO> getStopTaskDispatch() {
		return stopTaskDispatch;
	}

	public DispatchBO setStopTaskDispatch(List<StopTaskDispatchBO> stopTaskDispatch) {
		this.stopTaskDispatch = stopTaskDispatch;
		return this;
	}

	public List<StopUserDispatchBO> getStopUserDispatch() {
		return stopUserDispatch;
	}

	public DispatchBO setStopUserDispatch(List<StopUserDispatchBO> stopUserDispatch) {
		this.stopUserDispatch = stopUserDispatch;
		return this;
	}

}
