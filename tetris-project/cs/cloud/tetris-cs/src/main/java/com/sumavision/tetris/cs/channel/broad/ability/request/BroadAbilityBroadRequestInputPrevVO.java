package com.sumavision.tetris.cs.channel.broad.ability.request;

import java.util.List;

public class BroadAbilityBroadRequestInputPrevVO {
	/** 索引 */
	private Integer index;
	
	/** 源类型 */
	private String type;
	
	/** 文件源参数 */
	private BroadAbilityBroadRequestInputPrevFileVO file;
	
	/** 流源参数 */
	private BroadAbilityBroadRequestInputPrevStreamVO stream;
	
	/** 源媒体类型 */
	private String mediaType;
	
	/** 文件源参数数组 */
	private List<BroadAbilityBroadRequestInputPrevFileVO> file_array;
	
	/**对应索引*/
	private Integer pre;
	
	private Integer next;
	
	/** 源地址 */
	private String url;
	
	/** 源封装 */
	private String pcm;
	
	/** 时长 */
	private Long duration;
	
	/** 流开始绝对时间 */
	private String startTime;
	
	/** 流结束绝对时间 */
	private String endTime;
	
	/** 收流网口*/
	private String localIp;
	
	/** 流类型*/
	//private String type;

	
	public List<BroadAbilityBroadRequestInputPrevFileVO> getFile_array() {
		return file_array;
	}

	public BroadAbilityBroadRequestInputPrevVO setFile_array(List<BroadAbilityBroadRequestInputPrevFileVO> file_array) {
		this.file_array = file_array;
		return this;
	}
	
	
	public String getMediaType() {
		return mediaType;
	}

	public BroadAbilityBroadRequestInputPrevVO setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public String getType() {
		return type;
	}

	public BroadAbilityBroadRequestInputPrevVO setType(String type) {
		this.type = type;
		return this;
	}

	public BroadAbilityBroadRequestInputPrevFileVO getFile() {
		return file;
	}

	public BroadAbilityBroadRequestInputPrevVO setFile(BroadAbilityBroadRequestInputPrevFileVO file) {
		this.file = file;
		return this;
	}

	public BroadAbilityBroadRequestInputPrevStreamVO getStream() {
		return stream;
	}

	public BroadAbilityBroadRequestInputPrevVO setStream(BroadAbilityBroadRequestInputPrevStreamVO stream) {
		this.stream = stream;
		return this;
	}

	public Integer getIndex() {
		return index;
	}

	public BroadAbilityBroadRequestInputPrevVO setIndex(Integer index) {
		this.index = index;
		return this;
	}

	public Integer getPre() {
		return pre;
	}

	public BroadAbilityBroadRequestInputPrevVO setPre(Integer pre) {
		this.pre = pre;
		return this;
	}

	public Integer getNext() {
		return next;
	}

	public BroadAbilityBroadRequestInputPrevVO setNext(Integer next) {
		this.next = next;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public BroadAbilityBroadRequestInputPrevVO setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getPcm() {
		return pcm;
	}

	public BroadAbilityBroadRequestInputPrevVO setPcm(String pcm) {
		this.pcm = pcm;
		return this;
	}

	public Long getDuration() {
		return duration;
	}

	public BroadAbilityBroadRequestInputPrevVO setDuration(Long duration) {
		this.duration = duration;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public BroadAbilityBroadRequestInputPrevVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public BroadAbilityBroadRequestInputPrevVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getLocalIp() {
		return localIp;
	}

	public BroadAbilityBroadRequestInputPrevVO setLocalIp(String localIp) {
		this.localIp = localIp;
		return this;
	}
}
