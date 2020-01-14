package com.sumavision.tetris.sts.task;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.sts.task.source.VideoParamPO;

public class VideoParamBONew implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3689980922709151462L;
	
	private Integer pid;
	private Integer trackId;
	//视频解码方式
	private String decodeMode;
	

	
	//去隔行
	private String deinterlace;

	
	//帧场标识
	//private String picStruct;

	

	//黑边
	private Integer autoBlackSide;
	private String blackTop;
	private String blackBottom;
	private String blackLeft;
	private String blackRight;

	//去块
	private String deblock;

	//裁剪
	private String autoCut;
	private String cutTop;
	private String cutBottom;
	private String cutLeft;
	private String cutRight;

	//private String gamma;
	//private String chroma;
	//private String enhance;

	//采用何种模式进行缩放slow/fast
	private String mode;
	private Integer scale_width;
	private Integer scale_height;

	

	private JSONArray osds;

	//位深
	//private Integer bitDepth;

	//截图参数
//	private String ftpUrl;
//	private Integer timeSpan;
//	private Integer maxBuf;
//	private String jpegPre;
//	private String cycDel;

	//色彩增强
//	private String colorEnhance;
	

	//SCU300转码器新增参数
	//private Integer idrFrequency;
//	private String downFrameRate;
//	private String chromaFormat;
//	private String wp;
//	private String sao;
//	private String amp;
	
	

	
	/*****************************************************
	 * 图像增强参数
	 ****************************************************/
	//亮度
	private Integer brightness;
	//对比度
	//private String contrast;
	private Integer contrast;
	
	//饱和度
	//private String saturation;
	private Integer saturation;
	
	//去噪 ：off/gaussian/median/3d
	private String denoise;
	
	private String sharpen;
	
	//private String colorSpace;
	private String colorspace;
	//private String colorTransfer;
	private String colortransfer;
	//private String colorPrimary;
	private String colorprimaries;
	//private String colorRange;
	private String colorrange;


	/*****************************************************
	 * 编码参数
	 ****************************************************/
	//编码方式，当前取h264
	private String codec;
	//编码库"x264、x265、uux264、uux265、msdk、cuda"
	private String codeType;
	private Integer width;
	private Integer height;

	//输出帧率1-60
	//private Float fps;
	private String fps;

	//视频码率
	//private long bitrate;
	private Integer bitrate;

	//private Long maxBitrate;
	private Integer max_bitrate;
	private Integer gop_size;
	private Integer keyint_min;
	//vbv缓存
	private Integer vbv_buffer_size;
	
	private String pixel_format;
	private Boolean low_latency;

	//码率控制方式：ABR、VBR、NearCBR
	//private String rcMode;
	private String rc_mode;

	//输出分辨率
	//private String resolution;

	//输出宽高比
	private String ratio;

	//帧编码或场编码 :frame、interlace
	//private String encodingType;
	private String encoding_type;
	
	//I帧最大值
	//private Integer keyintMax;
	private Integer keyint_max;

	//是否为开gop	
	//private String openGop;
	private Boolean open_gop;
	//main、high
	private String tier;

	//编码档次
	private String profile;

	//编码level
	//private Float level;
	private Integer level;

	//参考帧数
	//private Integer refFrames;
	private Integer ref_frames;

	//连续B帧数(最大)
	//private Integer bFrame;
	private Integer max_bframe;

	//搜索强度 slow,middle,fast
	private String refine;

	//编码延时
	//private Integer iLookahead;
	private Integer lookahead;

	//宏块树搜索开关
	//private String mbtreeSwitch;
	private Boolean mbtree;

	//B帧自适应
	//private String bframeAdaptive;
	private Boolean bframe_adaptive;
	
	//B帧参考模式none、strict、normal
	//private String bframeReference;
	private String bframe_reference;

	//运动搜索范围
	//private Long imeRange;
	private Integer me_range;

	//场景切换开关
	//private String scenecut;
	private Boolean scenecut;
	
	//熵编码算法cabac,cavlc
	//private String entropyType;
	private String entropy_type;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getTrackId() {
		return trackId;
	}

	public void setTrackId(Integer trackId) {
		this.trackId = trackId;
	}

	public String getDecodeMode() {
		return decodeMode;
	}

	public void setDecodeMode(String decodeMode) {
		this.decodeMode = decodeMode;
	}

	public String getDeinterlace() {
		return deinterlace;
	}

	public void setDeinterlace(String deinterlace) {
		this.deinterlace = deinterlace;
	}

	public Integer getAutoBlackSide() {
		return autoBlackSide;
	}

	public void setAutoBlackSide(Integer autoBlackSide) {
		this.autoBlackSide = autoBlackSide;
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

	public String getDeblock() {
		return deblock;
	}

	public void setDeblock(String deblock) {
		this.deblock = deblock;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getScale_width() {
		return scale_width;
	}

	public void setScale_width(Integer scale_width) {
		this.scale_width = scale_width;
	}

	public Integer getScale_height() {
		return scale_height;
	}

	public void setScale_height(Integer scale_height) {
		this.scale_height = scale_height;
	}

	public JSONArray getOsds() {
		return osds;
	}

	public void setOsds(JSONArray osds) {
		this.osds = osds;
	}

	public Integer getBrightness() {
		return brightness;
	}

	public void setBrightness(Integer brightness) {
		this.brightness = brightness;
	}

	public Integer getContrast() {
		return contrast;
	}

	public void setContrast(Integer contrast) {
		this.contrast = contrast;
	}

	public Integer getSaturation() {
		return saturation;
	}

	public void setSaturation(Integer saturation) {
		this.saturation = saturation;
	}

	public String getDenoise() {
		return denoise;
	}

	public void setDenoise(String denoise) {
		this.denoise = denoise;
	}

	public String getSharpen() {
		return sharpen;
	}

	public void setSharpen(String sharpen) {
		this.sharpen = sharpen;
	}

	public String getColorspace() {
		return colorspace;
	}

	public void setColorspace(String colorspace) {
		this.colorspace = colorspace;
	}

	public String getColortransfer() {
		return colortransfer;
	}

	public void setColortransfer(String colortransfer) {
		this.colortransfer = colortransfer;
	}

	public String getColorprimaries() {
		return colorprimaries;
	}

	public void setColorprimaries(String colorprimaries) {
		this.colorprimaries = colorprimaries;
	}

	public String getColorrange() {
		return colorrange;
	}

	public void setColorrange(String colorrange) {
		this.colorrange = colorrange;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
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

	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	public Integer getMax_bitrate() {
		return max_bitrate;
	}

	public void setMax_bitrate(Integer max_bitrate) {
		this.max_bitrate = max_bitrate;
	}

	public Integer getGop_size() {
		return gop_size;
	}

	public void setGop_size(Integer gop_size) {
		this.gop_size = gop_size;
	}

	public Integer getKeyint_min() {
		return keyint_min;
	}

	public void setKeyint_min(Integer keyint_min) {
		this.keyint_min = keyint_min;
	}

	public Integer getVbv_buffer_size() {
		return vbv_buffer_size;
	}

	public void setVbv_buffer_size(Integer vbv_buffer_size) {
		this.vbv_buffer_size = vbv_buffer_size;
	}

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

	public String getRc_mode() {
		return rc_mode;
	}

	public void setRc_mode(String rc_mode) {
		this.rc_mode = rc_mode;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getEncoding_type() {
		return encoding_type;
	}

	public void setEncoding_type(String encoding_type) {
		this.encoding_type = encoding_type;
	}

	public Integer getKeyint_max() {
		return keyint_max;
	}

	public void setKeyint_max(Integer keyint_max) {
		this.keyint_max = keyint_max;
	}

	public Boolean getOpen_gop() {
		return open_gop;
	}

	public void setOpen_gop(Boolean open_gop) {
		this.open_gop = open_gop;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getRef_frames() {
		return ref_frames;
	}

	public void setRef_frames(Integer ref_frames) {
		this.ref_frames = ref_frames;
	}

	public Integer getMax_bframe() {
		return max_bframe;
	}

	public void setMax_bframe(Integer max_bframe) {
		this.max_bframe = max_bframe;
	}

	public String getRefine() {
		return refine;
	}

	public void setRefine(String refine) {
		this.refine = refine;
	}

	public Integer getLookahead() {
		return lookahead;
	}

	public void setLookahead(Integer lookahead) {
		this.lookahead = lookahead;
	}

	public Boolean getMbtree() {
		return mbtree;
	}

	public void setMbtree(Boolean mbtree) {
		this.mbtree = mbtree;
	}

	public Boolean getBframe_adaptive() {
		return bframe_adaptive;
	}

	public void setBframe_adaptive(Boolean bframe_adaptive) {
		this.bframe_adaptive = bframe_adaptive;
	}

	public String getBframe_reference() {
		return bframe_reference;
	}

	public void setBframe_reference(String bframe_reference) {
		this.bframe_reference = bframe_reference;
	}

	public Integer getMe_range() {
		return me_range;
	}

	public void setMe_range(Integer me_range) {
		this.me_range = me_range;
	}

	public Boolean getScenecut() {
		return scenecut;
	}

	public void setScenecut(Boolean scenecut) {
		this.scenecut = scenecut;
	}

	public String getEntropy_type() {
		return entropy_type;
	}

	public void setEntropy_type(String entropy_type) {
		this.entropy_type = entropy_type;
	}
	
	public VideoParamPO transToVideoParamPO(){
		VideoParamPO videoParamPO = new VideoParamPO();
		videoParamPO.setPid(pid);
		videoParamPO.setTrackId(trackId);
		videoParamPO.setWidth(width);
		videoParamPO.setHeight(height);
		videoParamPO.setFps(Float.valueOf(fps));
		videoParamPO.setBitrate(bitrate.longValue());
		videoParamPO.setRcMode(rc_mode);
		//videoParamPO.setResolution(resolution);
		videoParamPO.setRatio(ratio);
		videoParamPO.setEncodingType(encoding_type);
		//videoParamPO.setFieldOrder(fieldOrder);
		videoParamPO.setDeinterlace(deinterlace);
		//能力目前的mpeg2需要改为mpeg2-video
		videoParamPO.setCodec(codec.startsWith("mpeg2") ? "mpeg2-video" : codec);
		videoParamPO.setProfile(profile);
		videoParamPO.setLevel(level.floatValue());
		videoParamPO.setRefFrames(ref_frames);
		videoParamPO.setbFrames(max_bframe);
		videoParamPO.setRefine(refine);
		videoParamPO.setiLookahead(lookahead);
		videoParamPO.setMbtreeSwitch(mbtree==true?"On":"Off");
		videoParamPO.setBframeAdaptive(bframe_adaptive==true?"On":"Off");
		videoParamPO.setBframeReference(bframe_reference);
		videoParamPO.setImeRange(me_range.longValue());
		videoParamPO.setScenecut(scenecut==true?"On":"Off");
		videoParamPO.setEntropyType(entropy_type);
		videoParamPO.setAutoBlackSide(autoBlackSide);
		videoParamPO.setBrightness(brightness);
		videoParamPO.setContrast(contrast.toString());
		videoParamPO.setSaturation(saturation.toString());
		//videoParamPO.setEnhance(enhance);
		//videoParamPO.setScaleMode(scaleMode);
		videoParamPO.setKeyintMax(keyint_max);
		//videoParamPO.setVbvSize(vbvSize);
		videoParamPO.setOsdJson(osds==null ? null:osds.toJSONString());
		//videoParamPO.setFtpUrl(ftpUrl);
		//videoParamPO.setTimeSpan(timeSpan);
		//videoParamPO.setMaxBuf(maxBuf);
		//videoParamPO.setJpegPre(jpegPre);
		//videoParamPO.setCycDel(cycDel);
		//videoParamPO.setColorEnhance(colorEnhance);
		videoParamPO.setDenoise(denoise);
		videoParamPO.setMaxBitrate(max_bitrate.longValue());
		//videoParamPO.setPicStruct(picStruct);
		videoParamPO.setOpenGop(open_gop==true?"On":"Off");
		videoParamPO.setSharpen(sharpen);
		//videoParamPO.setBitDepth(bitDepth);
		videoParamPO.setDeblock(deblock);
		//videoParamPO.setChroma(chroma);
		//videoParamPO.setGamma(gamma);
		videoParamPO.setAutoCut(autoCut);
		videoParamPO.setCutTop(cutTop);
		videoParamPO.setCutBottom(cutBottom);
		videoParamPO.setCutLeft(cutLeft);
		videoParamPO.setCutRight(cutRight);

		videoParamPO.setBlackTop(blackTop);
		videoParamPO.setBlackBottom(blackBottom);
		videoParamPO.setBlackLeft(blackLeft);
		videoParamPO.setBlackRight(blackRight);

		//videoParamPO.setIdrFrequency(idrFrequency);
		//videoParamPO.setDownFrameRate(downFrameRate);
		//videoParamPO.setChromaFormat(chromaFormat);
		//videoParamPO.setWp(wp);
		videoParamPO.setTier(tier);
		//videoParamPO.setSao(sao);
		//videoParamPO.setAmp(amp);

		videoParamPO.setColorPrimary(colorprimaries);
		videoParamPO.setColorTransfer(colortransfer);
		videoParamPO.setColorSpace(colorspace);
		videoParamPO.setColorRange(colorrange);
		
		videoParamPO.setDecodeMode(decodeMode);
		videoParamPO.setCodeType(codeType);
		videoParamPO.setKeyintMax(keyint_max);
		videoParamPO.setKeyint_min(keyint_min);
		videoParamPO.setPixel_format(pixel_format);
		videoParamPO.setLow_latency(low_latency);

		return videoParamPO;
	}

}
