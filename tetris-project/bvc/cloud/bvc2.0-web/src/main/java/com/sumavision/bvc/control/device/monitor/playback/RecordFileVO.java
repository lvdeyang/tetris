package com.sumavision.bvc.control.device.monitor.playback;

import java.util.Comparator;
import java.util.Date;

import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class RecordFileVO extends AbstractBaseVO<RecordFileVO, MonitorRecordPO>{

	/** 通过录制得到的文件 */
	public static final String RECORD = "record";
	
	/** 导入的文件 */
	public static final String IMPORT = "import";
	
	/** 文件名 */
	private String fileName;
	
	/** 预览地址 */
	private String previewUrl;
	
	/** 开始时间 */
	private String startTime;
	
	/** 结束时间 */
	private String endTime;
	
	/** 当前用户是否可以删除 */
	private boolean removeable;
	
	/** 当前文件是否可播放--主要根据是否有接入层id来判断 */
	private boolean playable;
	
	public String getFileName() {
		return fileName;
	}

	public RecordFileVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public RecordFileVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public RecordFileVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public RecordFileVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public RecordFileVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	public boolean isPlayable() {
		return playable;
	}

	public RecordFileVO setPlayable(boolean playable) {
		this.playable = playable;
		return this;
	}

	@Override
	public RecordFileVO set(MonitorRecordPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setFileName(entity.getFileName())
			.setPreviewUrl(entity.getPreviewUrl())
			.setStartTime(DateUtil.format(entity.getStartTime(), DateUtil.dateTimePattern))
			.setEndTime(entity.getEndTime()==null?DateUtil.format(new Date(), DateUtil.dateTimePattern):DateUtil.format(entity.getEndTime(), DateUtil.dateTimePattern));
		return this;
	}
	
	public RecordFileVO set(MonitorRecordPO entity, Long userId, AccessNodeBO layer) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setFileName(entity.getFileName())
			.setPreviewUrl(new StringBufferWrapper().append("http://").append(layer==null?"0.0.0.0":layer.getIp()).append(":").append(layer==null?"0":layer.getPort()).append("/").append(entity.getPreviewUrl()).toString())
			.setStartTime(DateUtil.format(entity.getStartTime(), DateUtil.dateTimePattern))
			.setEndTime(entity.getEndTime()==null?"录制中...":DateUtil.format(entity.getEndTime(), DateUtil.dateTimePattern))
			.setRemoveable(entity.getUserId().equals(userId)?(MonitorRecordStatus.STOP.equals(entity.getStatus())?true:false):false)
			.setPlayable(layer==null?false:true);
		return this;
	}
	
	public RecordFileVO set(MonitorRecordPO entity, Long userId, String type, AccessNodeBO layer) throws Exception {
		this.setId(entity.getId())
			.setUuid(new StringBufferWrapper().append(type).append(entity.getUuid()).toString())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setFileName(entity.getFileName())
			.setPreviewUrl(new StringBufferWrapper().append("http://").append(layer==null?"0.0.0.0":layer.getIp()).append(":").append(layer==null?"0":layer.getPort()).append("/").append(entity.getPreviewUrl()).toString())
			.setStartTime(DateUtil.format(entity.getStartTime(), DateUtil.dateTimePattern))
			.setEndTime(entity.getEndTime()==null?DateUtil.format(new Date(), DateUtil.dateTimePattern):DateUtil.format(entity.getEndTime(), DateUtil.dateTimePattern))
			.setRemoveable(entity.getUserId().equals(userId)?(MonitorRecordStatus.STOP.equals(entity.getStatus())?true:false):false);
		return this;
	}
	
	public static class RecordFileComparator implements Comparator<RecordFileVO>{
		@Override
		public int compare(RecordFileVO o1, RecordFileVO o2) {
			return o1.getFileName().compareTo(o2.getFileName());
		}
	}

}
