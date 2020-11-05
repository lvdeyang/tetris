package com.sumavision.tetris.streamTranscoding.addTask.requestVO;

import javax.xml.bind.annotation.XmlAttribute;

public class VideoVO {
	private String codec;
	private Long bitrate;
	private String resolution;
	private String ratio;
	private String pid;
	private Integer adjustTime;
	private String profile;
	private Integer keyintMin;
	private Integer keyintMax;
	private Integer refFrames;
	private Integer bFrame;
	private Integer fps;
	private float level;
	private String deinterlace;
	private String rcMode;
	private Long iLookahead;
	private String mbtreeSwitch;
	private String openGop;
	private String bframeAdaptive;
	private String bframeReference;
	private Integer imeRange;
	private String scenecut;
	private String entropyType;
	private Integer refine;
	private String twoPass;
	private String encodingType;
	
	public VideoVO(){}
	
	public VideoVO(String codec, Long bitrate, String resolution, String ratio, String pid){
		this(
				codec,
				bitrate,
				resolution,
				ratio,
				pid,
				0,
				"main",
				5,
				25,
				4,
				4,
				25,
				4.0f,
				"On",
				"CBR",
				10l,
				"On",
				"Off",
				"On",
				"Normal",
				32,
				"Off",
				"CABAC",
				4,
				"Off",
				"Progressive");
	}
	
	public String getCodec() {
		return codec;
	}
	
	@XmlAttribute(name = "codec")
	public void setCodec(String codec) {
		this.codec = codec;
	}
	
	public Long getBitrate() {
		return bitrate;
	}
	
	@XmlAttribute(name = "bitrate")
	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}

	public String getRatio() {
		return ratio;
	}

	@XmlAttribute(name = "ratio")
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getPid() {
		return pid;
	}

	@XmlAttribute(name = "pid")
	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getResolution() {
		return resolution;
	}

	@XmlAttribute(name = "resolution")
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Integer getAdjustTime() {
		return adjustTime;
	}

	@XmlAttribute(name = "adjust-time")
	public void setAdjustTime(Integer adjustTime) {
		this.adjustTime = adjustTime;
	}

	public String getProfile() {
		return profile;
	}

	@XmlAttribute(name = "profile")
	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Integer getKeyintMin() {
		return keyintMin;
	}

	@XmlAttribute(name = "keyint-min")
	public void setKeyintMin(Integer keyintMin) {
		this.keyintMin = keyintMin;
	}

	public Integer getKeyintMax() {
		return keyintMax;
	}

	@XmlAttribute(name = "keyint-max")
	public void setKeyintMax(Integer keyintMax) {
		this.keyintMax = keyintMax;
	}

	public Integer getRefFrames() {
		return refFrames;
	}

	@XmlAttribute(name = "ref-frames")
	public void setRefFrames(Integer refFrames) {
		this.refFrames = refFrames;
	}

	public Integer getbFrame() {
		return bFrame;
	}

	@XmlAttribute(name = "b-frame")
	public void setbFrame(Integer bFrame) {
		this.bFrame = bFrame;
	}

	public Integer getFps() {
		return fps;
	}

	@XmlAttribute(name = "fps")
	public void setFps(Integer fps) {
		this.fps = fps;
	}

	public float getLevel() {
		return level;
	}

	@XmlAttribute(name = "level")
	public void setLevel(float level) {
		this.level = level;
	}

	public String getDeinterlace() {
		return deinterlace;
	}

	@XmlAttribute(name = "deinterlace")
	public void setDeinterlace(String deinterlace) {
		this.deinterlace = deinterlace;
	}

	public String getRcMode() {
		return rcMode;
	}

	@XmlAttribute(name = "rc-mode")
	public void setRcMode(String rcMode) {
		this.rcMode = rcMode;
	}

	public Long getiLookahead() {
		return iLookahead;
	}

	@XmlAttribute(name = "i-lookahead")
	public void setiLookahead(Long iLookahead) {
		this.iLookahead = iLookahead;
	}

	public String getMbtreeSwitch() {
		return mbtreeSwitch;
	}

	@XmlAttribute(name = "mbtree-switch")
	public void setMbtreeSwitch(String mbtreeSwitch) {
		this.mbtreeSwitch = mbtreeSwitch;
	}

	public String getOpenGop() {
		return openGop;
	}

	@XmlAttribute(name = "open-gop")
	public void setOpenGop(String openGop) {
		this.openGop = openGop;
	}

	public String getBframeAdaptive() {
		return bframeAdaptive;
	}

	@XmlAttribute(name = "bframe-adaptive")
	public void setBframeAdaptive(String bframeAdaptive) {
		this.bframeAdaptive = bframeAdaptive;
	}

	public String getBframeReference() {
		return bframeReference;
	}

	@XmlAttribute(name = "bframe-reference")
	public void setBframeReference(String bframeReference) {
		this.bframeReference = bframeReference;
	}

	public Integer getImeRange() {
		return imeRange;
	}

	@XmlAttribute(name = "ime-range")
	public void setImeRange(Integer imeRange) {
		this.imeRange = imeRange;
	}

	public String getScenecut() {
		return scenecut;
	}

	@XmlAttribute(name = "scenecut")
	public void setScenecut(String scenecut) {
		this.scenecut = scenecut;
	}

	public String getEntropyType() {
		return entropyType;
	}

	@XmlAttribute(name = "entropy-type")
	public void setEntropyType(String entropyType) {
		this.entropyType = entropyType;
	}

	public Integer getRefine() {
		return refine;
	}

	@XmlAttribute(name = "refine")
	public void setRefine(Integer refine) {
		this.refine = refine;
	}

	public String getTwoPass() {
		return twoPass;
	}

	@XmlAttribute(name = "two-pass")
	public void setTwoPass(String twoPass) {
		this.twoPass = twoPass;
	}

	public String getEncodingType() {
		return encodingType;
	}

	@XmlAttribute(name = "encoding-type")
	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}
	
	public VideoVO(
			String codec,
			Long bitrate,
			String resolution,
			String ratio,
			String pid,
			Integer adjustTime,
			String profile,
			Integer keyintMin,
			Integer keyintMax,
			Integer refFrames,
			Integer bFrame,
			Integer fps,
			float level,
			String deinterlace,
			String rcMode,
			Long iLookahead,
			String mbtreeSwitch,
			String openGop,
			String bframeAdaptive,
			String bframeReference,
			Integer imeRange,
			String scenecut,
			String entropyType,
			Integer refine,
			String twoPass,
			String encodingType){
		this.codec = codec;
		this.bitrate = bitrate;
		this.resolution = resolution;
		this.ratio = ratio;
		this.pid = pid;
		this.adjustTime = adjustTime;
		this.profile = profile;
		this.keyintMin = keyintMin;
		this.keyintMax = keyintMax;
		this.refFrames = refFrames;
		this.bFrame = bFrame;
		this.fps = fps;
		this.level = level;
		this.deinterlace = deinterlace;
		this.rcMode = rcMode;
		this.iLookahead = iLookahead;
		this.mbtreeSwitch = mbtreeSwitch;
		this.openGop = openGop;
		this.bframeAdaptive = bframeAdaptive;
		this.bframeReference = bframeReference;
		this.imeRange = imeRange;
		this.scenecut = scenecut;
		this.entropyType = entropyType;
		this.refine = refine;
		this.twoPass = twoPass;
		this.encodingType = encodingType;
	}
	
	public VideoVO copy() {
		return new VideoVO(this.getCodec(), this.getBitrate(), this.getResolution(), this.getRatio(), this.getPid());
	}
}
