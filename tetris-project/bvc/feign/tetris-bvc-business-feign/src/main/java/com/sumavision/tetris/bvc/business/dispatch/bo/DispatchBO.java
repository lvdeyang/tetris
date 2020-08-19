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
	
	//设备开始调度、更新调度
	private List<StartBundleDispatchBO> startBundleDispatch = new ArrayList<StartBundleDispatchBO>();
	
	//用户开始调度、更新调度
	private List<StartUserDispatchBO> startUserDispatch = new ArrayList<StartUserDispatchBO>();
	
	//停止设备调度
	private List<StopBundleDispatchBO> stopBundleDispatch = new ArrayList<StopBundleDispatchBO>();
	
	//按任务id停止调度。可以停止设备调度和用户调度
	private List<StopTaskDispatchBO> stopTaskDispatch = new ArrayList<StopTaskDispatchBO>();
	
	//以userId和meetingCode和停止调度。可以停止设备调度和用户调度。一次停止该用户在该会议下的全部调度
	private List<StopTaskDispatchByUserIdAndMeetingCodeBO> stopTaskDispatchByUserIdAndMeetingCode = new ArrayList<StopTaskDispatchByUserIdAndMeetingCodeBO>();
	
	//以userId和meetingCode和源 停止 用户调度。仅用于停止“用户调度”任务，否则返回错误。要求任务只有1个通道才能停止。一次只停止一个任务
	private List<StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO> stopTaskDispatchByUserIdAndMeetingCodeAndSource = new ArrayList<StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO>();
	
	//停止若干个用户的所有调度。可以停止设备调度和用户调度
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

	public List<StopTaskDispatchByUserIdAndMeetingCodeBO> getStopTaskDispatchByUserIdAndMeetingCode() {
		return stopTaskDispatchByUserIdAndMeetingCode;
	}

	public DispatchBO setStopTaskDispatchByUserIdAndMeetingCode(List<StopTaskDispatchByUserIdAndMeetingCodeBO> stopTaskDispatchByUserIdAndMeetingCode) {
		this.stopTaskDispatchByUserIdAndMeetingCode = stopTaskDispatchByUserIdAndMeetingCode;
		return this;
	}

	public List<StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO> getStopTaskDispatchByUserIdAndMeetingCodeAndSource() {
		return stopTaskDispatchByUserIdAndMeetingCodeAndSource;
	}

	public DispatchBO setStopTaskDispatchByUserIdAndMeetingCodeAndSource(List<StopTaskDispatchByUserIdAndMeetingCodeAndSourceBO> stopTaskDispatchByUserIdAndMeetingCodeAndSource) {
		this.stopTaskDispatchByUserIdAndMeetingCodeAndSource = stopTaskDispatchByUserIdAndMeetingCodeAndSource;
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
