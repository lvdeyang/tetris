package com.sumavision.tetris.sts.task;

import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.sts.task.source.VideoParamPO;

/**
 * 音频参数vo
 * @author gaofeng
 *
 */
public class VideoParamBO implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -3525535515522396686L;

	private int pid;

	/**
	 * 轨道id
	 */
	private Integer trackId;

	private Integer width;

	private Integer height;

	//输出帧率
	private Float fps;

	//视频码率
	private long bitrate;

	private Long maxBitrate;

	//码率控制方式：ABR、VBR、NearCBR
	private String rcMode;

	//输出分辨率
	private String resolution;

	//输出宽高比
	private String ratio;

	private String encodingType;

	private String fieldOrder;
	//去隔行
	private String deinterlace;

	//编码方式，当前取h264
	private String codec;

	private String profile;

	//编码level
	private Float level;

	//参考帧数
	private Integer refFrames;

	//连续B帧数
	private Integer bFrame;

	//搜索强度
	private String refine;

	//前驱分析
	private Integer iLookahead;

	private String mbtreeSwitch;

	private String bframeAdaptive;

	private String bframeReference;

	private Long imeRange;

	private String scenecut;

	private String picStruct;

	private String entropyType;

	private Integer autoBlackSide;
	private String blackTop;
	private String blackBottom;
	private String blackLeft;
	private String blackRight;

	private String deblock;

	private String autoCut;
	private String cutTop;
	private String cutBottom;
	private String cutLeft;
	private String cutRight;

	private Integer brightness;

	private String contrast;

	private String saturation;

	private String gamma;

	private String chroma;

	private String enhance;

	private String scaleMode;

	private Integer keyintMax;

	private Integer vbvSize;



	private String openGop;

	private JSONArray osds;

	private String sharpen;

	private Integer bitDepth;

	//截图参数
	private String ftpUrl;
	private Integer timeSpan;
	private Integer maxBuf;
	private String jpegPre;
	private String cycDel;

	private String colorEnhance;
	private String denoise;

	//SCU300转码器新增参数
	private Integer idrFrequency;
	private String downFrameRate;
	private String chromaFormat;
	private String wp;
	private String tier;
	private String sao;
	private String amp;


	//20190521为算法组（马晓）新增视频图像处理参数
	//参考去隔行参数写的
	private String colorPrimary;
	private String colorTransfer;
	private String colorSpace;
	private String colorRange;
	
	//新协议版本，视频解码方式
	private String decodeMode;
	
	//编码库"x264、x265、uux264、uux265、msdk、cuda"
	private String codeType;
	private Integer gop_size;
	private Integer keyint_min;
	private Integer vbv_buffer_size;
	
	//private Integer keyint_max;
	private String pixel_format;
	private Boolean low_latency;


	public VideoParamPO transToVideoParamPO(){
		VideoParamPO videoParamPO = new VideoParamPO();
		videoParamPO.setPid(pid);
		videoParamPO.setTrackId(trackId);
		videoParamPO.setWidth(width);
		videoParamPO.setHeight(height);
		videoParamPO.setFps(fps);
		videoParamPO.setBitrate(bitrate);
		videoParamPO.setRcMode(rcMode);
		videoParamPO.setResolution(resolution);
		videoParamPO.setRatio(ratio);
		videoParamPO.setEncodingType(encodingType);
		videoParamPO.setFieldOrder(fieldOrder);
		videoParamPO.setDeinterlace(deinterlace);
		//能力目前的mpeg2需要改为mpeg2-video
		videoParamPO.setCodec(codec.startsWith("mpeg2") ? "mpeg2-video" : codec);
		videoParamPO.setProfile(profile);
		videoParamPO.setLevel(level);
		videoParamPO.setRefFrames(refFrames);
		videoParamPO.setbFrames(bFrame);
		videoParamPO.setRefine(refine);
		videoParamPO.setiLookahead(iLookahead);
		videoParamPO.setMbtreeSwitch(mbtreeSwitch);
		videoParamPO.setBframeAdaptive(bframeAdaptive);
		videoParamPO.setBframeReference(bframeReference);
		videoParamPO.setImeRange(imeRange);
		videoParamPO.setScenecut(scenecut);
		videoParamPO.setEntropyType(entropyType);
		videoParamPO.setAutoBlackSide(autoBlackSide);
		videoParamPO.setBrightness(brightness);
		videoParamPO.setContrast(contrast);
		videoParamPO.setSaturation(saturation);
		videoParamPO.setEnhance(enhance);
		videoParamPO.setScaleMode(scaleMode);
		videoParamPO.setKeyintMax(keyintMax);
		videoParamPO.setVbvSize(vbvSize);
		videoParamPO.setOsdJson(osds==null ? null:osds.toJSONString());
		videoParamPO.setFtpUrl(ftpUrl);
		videoParamPO.setTimeSpan(timeSpan);
		videoParamPO.setMaxBuf(maxBuf);
		videoParamPO.setJpegPre(jpegPre);
		videoParamPO.setCycDel(cycDel);
		videoParamPO.setColorEnhance(colorEnhance);
		videoParamPO.setDenoise(denoise);
		videoParamPO.setMaxBitrate(maxBitrate);
		videoParamPO.setPicStruct(picStruct);
		videoParamPO.setOpenGop(openGop);
		videoParamPO.setSharpen(sharpen);
		videoParamPO.setBitDepth(bitDepth);
		videoParamPO.setDeblock(deblock);
		videoParamPO.setChroma(chroma);
		videoParamPO.setGamma(gamma);
		videoParamPO.setAutoCut(autoCut);
		videoParamPO.setCutTop(cutTop);
		videoParamPO.setCutBottom(cutBottom);
		videoParamPO.setCutLeft(cutLeft);
		videoParamPO.setCutRight(cutRight);

		videoParamPO.setBlackTop(blackTop);
		videoParamPO.setBlackBottom(blackBottom);
		videoParamPO.setBlackLeft(blackLeft);
		videoParamPO.setBlackRight(blackRight);

		videoParamPO.setIdrFrequency(idrFrequency);
		videoParamPO.setDownFrameRate(downFrameRate);
		videoParamPO.setChromaFormat(chromaFormat);
		videoParamPO.setWp(wp);
		videoParamPO.setTier(tier);
		videoParamPO.setSao(sao);
		videoParamPO.setAmp(amp);

		videoParamPO.setColorPrimary(colorPrimary);
		videoParamPO.setColorTransfer(colorTransfer);
		videoParamPO.setColorSpace(colorSpace);
		videoParamPO.setColorRange(colorRange);
		
		videoParamPO.setDecodeMode(decodeMode);
		videoParamPO.setCodeType(codeType);
		videoParamPO.setKeyintMax(keyintMax);
		videoParamPO.setKeyint_min(keyint_min);
		videoParamPO.setPixel_format(pixel_format);
		videoParamPO.setLow_latency(low_latency);

		return videoParamPO;
	}

	/**
	 * 判断关键参数是否相等，不等则需要通过update-task命令修改
	 * @param other
	 * @return
	 */
	public boolean keyParamCompare(VideoParamBO other) {
		if (autoCut == null) {
			if (other.autoCut != null)
				return false;
		} else if (!autoCut.equals(other.autoCut))
			return false;
		if (bFrame == null) {
			if (other.bFrame != null)
				return false;
		} else if (!bFrame.equals(other.bFrame))
			return false;
		if (bframeAdaptive == null) {
			if (other.bframeAdaptive != null)
				return false;
		} else if (!bframeAdaptive.equals(other.bframeAdaptive))
			return false;
		if (bframeReference == null) {
			if (other.bframeReference != null)
				return false;
		} else if (!bframeReference.equals(other.bframeReference))
			return false;
		if (bitDepth == null) {
			if (other.bitDepth != null)
				return false;
		} else if (!bitDepth.equals(other.bitDepth))
			return false;
		if (bitrate != other.bitrate)
			return false;
		if (codec == null) {
			if (other.codec != null)
				return false;
		} else if (!codec.equals(other.codec))
			return false;
		if (deblock == null) {
			if (other.deblock != null)
				return false;
		} else if (!deblock.equals(other.deblock))
			return false;
		if (deinterlace == null) {
			if (other.deinterlace != null)
				return false;
		} else if (!deinterlace.equals(other.deinterlace))
			return false;
		if (encodingType == null) {
			if (other.encodingType != null)
				return false;
		} else if (!encodingType.equals(other.encodingType))
			return false;
		if (fieldOrder == null) {
			if (other.fieldOrder != null)
				return false;
		} else if (!fieldOrder.equals(other.fieldOrder))
			return false;
		if (enhance == null) {
			if (other.enhance != null)
				return false;
		} else if (!enhance.equals(other.enhance))
			return false;
		if (entropyType == null) {
			if (other.entropyType != null)
				return false;
		} else if (!entropyType.equals(other.entropyType))
			return false;
		if (fps == null) {
			if (other.fps != null)
				return false;
		} else if (!fps.equals(other.fps))
			return false;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (iLookahead == null) {
			if (other.iLookahead != null)
				return false;
		} else if (!iLookahead.equals(other.iLookahead))
			return false;
		if (imeRange == null) {
			if (other.imeRange != null)
				return false;
		} else if (!imeRange.equals(other.imeRange))
			return false;
		if (keyintMax == null) {
			if (other.keyintMax != null)
				return false;
		} else if (!keyintMax.equals(other.keyintMax))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (maxBitrate == null) {
			if (other.maxBitrate != null)
				return false;
		} else if (!maxBitrate.equals(other.maxBitrate))
			return false;
		if (mbtreeSwitch == null) {
			if (other.mbtreeSwitch != null)
				return false;
		} else if (!mbtreeSwitch.equals(other.mbtreeSwitch))
			return false;
		if (openGop == null) {
			if (other.openGop != null)
				return false;
		} else if (!openGop.equals(other.openGop))
			return false;
		if (picStruct == null) {
			if (other.picStruct != null)
				return false;
		} else if (!picStruct.equals(other.picStruct))
			return false;
		if (pid != other.pid)
			return false;
		if (profile == null) {
			if (other.profile != null)
				return false;
		} else if (!profile.equals(other.profile))
			return false;
		if (ratio == null) {
			if (other.ratio != null)
				return false;
		} else if (!ratio.equals(other.ratio))
			return false;
		if (rcMode == null) {
			if (other.rcMode != null)
				return false;
		} else if (!rcMode.equals(other.rcMode))
			return false;
		if (refFrames == null) {
			if (other.refFrames != null)
				return false;
		} else if (!refFrames.equals(other.refFrames))
			return false;
		if (refine == null) {
			if (other.refine != null)
				return false;
		} else if (!refine.equals(other.refine))
			return false;
		if (resolution == null) {
			if (other.resolution != null)
				return false;
		} else if (!resolution.equals(other.resolution))
			return false;
		if (scenecut == null) {
			if (other.scenecut != null)
				return false;
		} else if (!scenecut.equals(other.scenecut))
			return false;
		if (trackId == null) {
			if (other.trackId != null)
				return false;
		} else if (!trackId.equals(other.trackId))
			return false;
		if (vbvSize == null) {
			if (other.vbvSize != null)
				return false;
		} else if (!vbvSize.equals(other.vbvSize))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;

		if (colorPrimary == null) {
			if (other.colorPrimary != null)
				return false;
		} else if (!colorPrimary.equals(other.colorPrimary))
			return false;
		if (colorTransfer == null) {
			if (other.colorTransfer != null)
				return false;
		} else if (!colorTransfer.equals(other.colorTransfer))
			return false;
		if (colorSpace == null) {
			if (other.colorSpace != null)
				return false;
		} else if (!colorSpace.equals(other.colorSpace))
			return false;
		if (colorRange == null) {
			if (other.colorRange != null)
				return false;
		} else if (!colorRange.equals(other.colorRange))
			return false;
		//不知道为什么只比较关键参数，现在加上其他编码参数比较，否则修改任务时涉及该部分参数
		//会修改失效 yzx add 20190929
		if (this.autoBlackSide == null) {
			if (other.autoBlackSide != null)
				return false;
		} else if (!this.autoBlackSide.equals(other.autoBlackSide))
			return false;
		if (this.blackTop == null) {
			if (other.blackTop != null)
				return false;
		} else if (!this.blackTop.equals(other.blackTop))
			return false;
		if (this.blackBottom == null) {
			if (other.blackBottom != null)
				return false;
		} else if (!this.blackBottom.equals(other.blackBottom))
			return false;
		if (this.blackLeft == null) {
			if (other.blackLeft != null)
				return false;
		} else if (!this.blackLeft.equals(other.blackLeft))
			return false;
		if (this.blackRight == null) {
			if (other.blackRight != null)
				return false;
		} else if (!this.blackRight.equals(other.blackRight))
			return false;
		//亮度,色度，饱和度，对比度
		if (this.brightness == null) {
			if (other.brightness != null)
				return false;
		} else if (!this.brightness.equals(other.brightness))
			return false;
		if (this.chroma == null) {
			if (other.chroma != null)
				return false;
		} else if (!this.chroma.equals(other.chroma))
			return false;
		if (this.saturation == null) {
			if (other.saturation != null)
				return false;
		} else if (!this.saturation.equals(other.saturation))
			return false;
		if (this.contrast == null) {
			if (other.contrast != null)
				return false;
		} else if (!this.contrast.equals(other.contrast))
			return false;
		//手动裁剪
		if (this.cutTop == null) {
			if (other.cutTop != null)
				return false;
		} else if (!this.cutTop.equals(other.cutTop))
			return false;
		if (this.cutBottom == null) {
			if (other.cutBottom != null)
				return false;
		} else if (!this.cutBottom.equals(other.cutBottom))
			return false;
		if (this.cutLeft == null) {
			if (other.cutLeft != null)
				return false;
		} else if (!this.cutLeft.equals(other.cutLeft))
			return false;
		if (this.cutRight == null) {
			if (other.cutRight != null)
				return false;
		} else if (!this.cutRight.equals(other.cutRight))
			return false;
		//新协议版本添加参数
		if (this.decodeMode == null) {
			if (other.decodeMode != null)
				return false;
		} else if (!this.decodeMode.equals(other.decodeMode))
			return false;
		if (this.keyint_min == null) {
			if (other.keyint_min != null)
				return false;
		} else if (!this.keyint_min.equals(other.keyint_min))
			return false;
		if (this.pixel_format == null) {
			if (other.pixel_format != null)
				return false;
		} else if (!this.pixel_format.equals(other.pixel_format))
			return false;
		if (this.low_latency == null) {
			if (other.low_latency != null)
				return false;
		} else if (!this.low_latency.equals(other.low_latency))
			return false;
		if (this.gop_size == null) {
			if (other.gop_size != null)
				return false;
		} else if (!this.gop_size.equals(other.gop_size))
			return false;
		if (this.vbv_buffer_size == null) {
			if (other.vbv_buffer_size != null)
				return false;
		} else if (!this.vbv_buffer_size.equals(other.vbv_buffer_size))
			return false;
		if (this.codeType == null) {
			if (other.codeType != null)
				return false;
		} else if (!this.codeType.equals(other.codeType))
			return false;
		return true;
	}

	public String getDeblock() {
		return deblock;
	}

	public void setDeblock(String deblock) {
		this.deblock = deblock;
	}

	public Integer getTrackId() {
		return trackId;
	}


	public void setTrackId(Integer trackId) {
		this.trackId = trackId;
	}


	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Float getFps() {
		return fps;
	}

	public void setFps(Float fps) {
		this.fps = fps;
	}

	public long getBitrate() {
		return bitrate;
	}

	public void setBitrate(long bitrate) {
		this.bitrate = bitrate;
	}

	public String getRcMode() {
		return rcMode;
	}

	public void setRcMode(String rcMode) {
		this.rcMode = rcMode;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getEncodingType() {
		return encodingType;
	}

	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}

	public String getFieldOrder() {
		return fieldOrder;
	}

	public void setFieldOrder(String fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	public String getDeinterlace() {
		return deinterlace;
	}

	public void setDeinterlace(String deinterlace) {
		this.deinterlace = deinterlace;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Float getLevel() {
		return level;
	}

	public void setLevel(Float level) {
		this.level = level;
	}

	public Integer getRefFrames() {
		return refFrames;
	}

	public void setRefFrames(Integer refFrames) {
		this.refFrames = refFrames;
	}



	public Integer getbFrame() {
		return bFrame;
	}


	public void setbFrame(Integer bFrame) {
		this.bFrame = bFrame;
	}


	public String getRefine() {
		return refine;
	}

	public void setRefine(String refine) {
		this.refine = refine;
	}

	public Integer getiLookahead() {
		return iLookahead;
	}

	public void setiLookahead(Integer iLookahead) {
		this.iLookahead = iLookahead;
	}

	public String getMbtreeSwitch() {
		return mbtreeSwitch;
	}

	public void setMbtreeSwitch(String mbtreeSwitch) {
		this.mbtreeSwitch = mbtreeSwitch;
	}

	public String getBframeAdaptive() {
		return bframeAdaptive;
	}

	public void setBframeAdaptive(String bframeAdaptive) {
		this.bframeAdaptive = bframeAdaptive;
	}

	public String getBframeReference() {
		return bframeReference;
	}

	public void setBframeReference(String bframeReference) {
		this.bframeReference = bframeReference;
	}

	public Long getImeRange() {
		return imeRange;
	}

	public void setImeRange(Long imeRange) {
		this.imeRange = imeRange;
	}

	public String getScenecut() {
		return scenecut;
	}

	public void setScenecut(String scenecut) {
		this.scenecut = scenecut;
	}

	public String getEntropyType() {
		return entropyType;
	}

	public void setEntropyType(String entropyType) {
		this.entropyType = entropyType;
	}


	public Integer getAutoBlackSide() {
		return autoBlackSide;
	}

	public void setAutoBlackSide(Integer autoBlackSide) {
		this.autoBlackSide = autoBlackSide;
	}

	public Integer getBrightness() {
		return brightness;
	}

	public void setBrightness(Integer brightness) {
		this.brightness = brightness;
	}

	public String getContrast() {
		return contrast;
	}

	public void setContrast(String contrast) {
		this.contrast = contrast;
	}

	public String getSaturation() {
		return saturation;
	}

	public void setSaturation(String saturation) {
		this.saturation = saturation;
	}

	public String getEnhance() {
		return enhance;
	}

	public void setEnhance(String enhance) {
		this.enhance = enhance;
	}

	public String getScaleMode() {
		return scaleMode;
	}

	public void setScaleMode(String scaleMode) {
		this.scaleMode = scaleMode;
	}

	public Integer getKeyintMax() {
		return keyintMax;
	}

	public void setKeyintMax(Integer keyintMax) {
		this.keyintMax = keyintMax;
	}


	public Integer getVbvSize() {
		return vbvSize;
	}


	public void setVbvSize(Integer vbvSize) {
		this.vbvSize = vbvSize;
	}


	public JSONArray getOsds() {
		return osds;
	}


	public void setOsds(JSONArray osds) {
		this.osds = osds;
	}


	public String getFtpUrl() {
		return ftpUrl;
	}


	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}


	public Integer getTimeSpan() {
		return timeSpan;
	}


	public void setTimeSpan(Integer timeSpan) {
		this.timeSpan = timeSpan;
	}


	public Integer getMaxBuf() {
		return maxBuf;
	}


	public void setMaxBuf(Integer maxBuf) {
		this.maxBuf = maxBuf;
	}


	public String getJpegPre() {
		return jpegPre;
	}


	public void setJpegPre(String jpegPre) {
		this.jpegPre = jpegPre;
	}

	public String getColorEnhance() {
		return colorEnhance;
	}


	public void setColorEnhance(String colorEnhance) {
		this.colorEnhance = colorEnhance;
	}


	public String getDenoise() {
		return denoise;
	}


	public void setDenoise(String denoise) {
		this.denoise = denoise;
	}


	public Long getMaxBitrate() {
		return maxBitrate;
	}


	public void setMaxBitrate(Long maxBitrate) {
		this.maxBitrate = maxBitrate;
	}

	public String getPicStruct() {
		return picStruct;
	}

	public void setPicStruct(String picStruct) {
		this.picStruct = picStruct;
	}

	public String getOpenGop() {
		return openGop;
	}

	public void setOpenGop(String openGop) {
		this.openGop = openGop;
	}


	public String getSharpen() {
		return sharpen;
	}


	public void setSharpen(String sharpen) {
		this.sharpen = sharpen;
	}


	public Integer getBitDepth() {
		return bitDepth;
	}


	public void setBitDepth(Integer bitDepth) {
		this.bitDepth = bitDepth;
	}

	public String getGamma() {
		return gamma;
	}

	public void setGamma(String gamma) {
		this.gamma = gamma;
	}

	public String getChroma() {
		return chroma;
	}

	public void setChroma(String chroma) {
		this.chroma = chroma;
	}

	public String getAutoCut() {
		return autoCut;
	}

	public void setAutoCut(String autoCut) {
		this.autoCut = autoCut;
	}

	public String getCutTop() {
		return cutTop;
	}

	public void setCutTop(String cutTop) {
		this.cutTop = cutTop;
	}

	public String getCutBottom() {
		return cutBottom;
	}

	public void setCutBottom(String cutBottom) {
		this.cutBottom = cutBottom;
	}

	public String getCutLeft() {
		return cutLeft;
	}

	public void setCutLeft(String cutLeft) {
		this.cutLeft = cutLeft;
	}

	public String getCutRight() {
		return cutRight;
	}

	public void setCutRight(String cutRight) {
		this.cutRight = cutRight;
	}

	public String getBlackTop() {
		return blackTop;
	}

	public void setBlackTop(String blackTop) {
		this.blackTop = blackTop;
	}

	public String getBlackBottom() {
		return blackBottom;
	}

	public void setBlackBottom(String blackBottom) {
		this.blackBottom = blackBottom;
	}

	public String getBlackLeft() {
		return blackLeft;
	}

	public void setBlackLeft(String blackLeft) {
		this.blackLeft = blackLeft;
	}

	public String getBlackRight() {
		return blackRight;
	}

	public void setBlackRight(String blackRight) {
		this.blackRight = blackRight;
	}


	public String getCycDel() {
		return cycDel;
	}


	public void setCycDel(String cycDel) {
		this.cycDel = cycDel;
	}

	public Integer getIdrFrequency() {
		return idrFrequency;
	}

	public void setIdrFrequency(Integer idrFrequency) {
		this.idrFrequency = idrFrequency;
	}

	public String getDownFrameRate() {
		return downFrameRate;
	}

	public void setDownFrameRate(String downFrameRate) {
		this.downFrameRate = downFrameRate;
	}

	public String getChromaFormat() {
		return chromaFormat;
	}

	public void setChromaFormat(String chromaFormat) {
		this.chromaFormat = chromaFormat;
	}

	public String getWp() {
		return wp;
	}

	public void setWp(String wp) {
		this.wp = wp;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getSao() {
		return sao;
	}

	public void setSao(String sao) {
		this.sao = sao;
	}

	public String getAmp() {
		return amp;
	}

	public void setAmp(String amp) {
		this.amp = amp;
	}

	public String getColorPrimary() {
		return colorPrimary;
	}

	public void setColorPrimary(String colorPrimary) {
		this.colorPrimary = colorPrimary;
	}

	public String getColorTransfer() {
		return colorTransfer;
	}

	public void setColorTransfer(String colorTransfer) {
		this.colorTransfer = colorTransfer;
	}

	public String getColorSpace() {
		return colorSpace;
	}

	public void setColorSpace(String colorSpace) {
		this.colorSpace = colorSpace;
	}

	public String getColorRange() {
		return colorRange;
	}

	public void setColorRange(String colorRange) {
		this.colorRange = colorRange;
	}

	public String getDecodeMode() {
		return decodeMode;
	}

	public void setDecodeMode(String decodeMode) {
		this.decodeMode = decodeMode;
	}

	public Integer getKeyint_min() {
		return keyint_min;
	}

	public void setKeyint_min(Integer keyint_min) {
		this.keyint_min = keyint_min;
	}

//	public Integer getKeyint_max() {
//		return keyint_max;
//	}
//
//	public void setKeyint_max(Integer keyint_max) {
//		this.keyint_max = keyint_max;
//	}

	public String getPixel_format() {
		return pixel_format;
	}

	public void setPixel_format(String pixel_format) {
		this.pixel_format = pixel_format;
	}

	public Boolean getLow_latency() {
		return low_latency;
	}

	public void setLow_latency(Boolean low_latency) {
		this.low_latency = low_latency;
	}

	public Integer getGop_size() {
		return gop_size;
	}

	public void setGop_size(Integer gop_size) {
		this.gop_size = gop_size;
	}

	public Integer getVbv_buffer_size() {
		return vbv_buffer_size;
	}

	public void setVbv_buffer_size(Integer vbv_buffer_size) {
		this.vbv_buffer_size = vbv_buffer_size;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

}
