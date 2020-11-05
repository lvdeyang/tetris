package com.sumavision.tetris.bvc.business.dispatch.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.tetris.bvc.business.dispatch.bo.ChannelBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StartBundleDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.StartUserDispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.enumeration.DispatchStatus;
import com.sumavision.tetris.bvc.business.dispatch.enumeration.DispatchType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @Title: 调度
 * @author zsy
 * @date 2020年3月11日 上午11:15:02 
 */
@Entity
@Table(name="TETRIS_DISPATCH")
public class TetrisDispatchPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	private DispatchType dispatchType;
	
	/** 调度状态，默认DONE */
	private DispatchStatus dispatchStatus = DispatchStatus.DONE;
	
	/** 通常为false；为true时，调度服务只进行记录而不实际调度 */
	private boolean recordOnly = false;
	
	private String taskId = "";
	
	private String meetingCode;

	private String userId;
	
	private String bundleId = "";
	
	private String layerId = "";

	private List<TetrisDispatchChannelPO> channels;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DISPATCH_TYPE")
	public DispatchType getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(DispatchType dispatchType) {
		this.dispatchType = dispatchType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "DISPATCH_STATUS")
	public DispatchStatus getDispatchStatus() {
		return dispatchStatus;
	}

	public void setDispatchStatus(DispatchStatus dispatchStatus) {
		this.dispatchStatus = dispatchStatus;
	}

	@Column(name = "RECORD_ONLY")
	public boolean isRecordOnly() {
		return recordOnly;
	}

	public void setRecordOnly(boolean recordOnly) {
		this.recordOnly = recordOnly;
	}

	@Column(name = "TASK_ID")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "MEETING_CODE")
	public String getMeetingCode() {
		return meetingCode;
	}

	public void setMeetingCode(String meetingCode) {
		this.meetingCode = meetingCode;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@OneToMany(mappedBy = "dispatch", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<TetrisDispatchChannelPO> getChannels() {
		return channels;
	}

	public void setChannels(List<TetrisDispatchChannelPO> channels) {
		this.channels = channels;
	}
	
	/**
	 * 从StartBundleDispatchBO生成TetrisDispatchPO，包括通道TetrisDispatchChannelPO<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 上午9:43:43
	 * @param bundleDispatch
	 * @return
	 */
	public TetrisDispatchPO setBundleAndChannel(StartBundleDispatchBO bundleDispatch){
		this.dispatchType = DispatchType.BUNDLE_BUNDLE;
		if(bundleDispatch.isRecordOnly()){
			this.dispatchStatus = DispatchStatus.RECORD_ONLY;
		}
		this.recordOnly = bundleDispatch.isRecordOnly();
		this.taskId = bundleDispatch.getTaskId();
		this.meetingCode = bundleDispatch.getMeetingCode();
		this.userId = bundleDispatch.getUserId();
		this.bundleId = bundleDispatch.getBundleId();
		this.layerId = bundleDispatch.getLayerId();
		this.setChannels(new ArrayList<TetrisDispatchChannelPO>());
		//channel	
		for(ChannelBO channelBO : bundleDispatch.getChannels()){
			TetrisDispatchChannelPO channelPO = new TetrisDispatchChannelPO().set(channelBO);
			channelPO.setDispatch(this);
			this.getChannels().add(channelPO);
		}	
		
		return this;
	}
	
	/**
	 * 从StartUserDispatchBO生成TetrisDispatchPO，包括通道TetrisDispatchChannelPO<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 上午9:44:19
	 * @param userDispatch
	 * @return
	 */
	public TetrisDispatchPO setBundleAndChannel(StartUserDispatchBO userDispatch){
		this.dispatchType = DispatchType.BUNDLE_USER;
		if(userDispatch.isRecordOnly()){
			this.dispatchStatus = DispatchStatus.RECORD_ONLY;
		}
		this.recordOnly = userDispatch.isRecordOnly();
		this.taskId = userDispatch.getTaskId();
		this.meetingCode = userDispatch.getMeetingCode();
		this.userId = userDispatch.getUserId();
		this.setChannels(new ArrayList<TetrisDispatchChannelPO>());
		//channel	
		for(ChannelBO channelBO : userDispatch.getChannels()){
			TetrisDispatchChannelPO channelPO = new TetrisDispatchChannelPO().set(channelBO);
			channelPO.setDispatch(this);
			this.getChannels().add(channelPO);
		}	
		
		return this;
	}
	
}
