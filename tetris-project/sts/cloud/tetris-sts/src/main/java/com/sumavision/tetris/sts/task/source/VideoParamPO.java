package com.sumavision.tetris.sts.task.source;



import java.io.Serializable;
import java.util.ArrayList;import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.serializer.NameFilter;
import com.sumavision.tetris.sts.common.CommonPO;
import com.sumavision.tetris.sts.task.OsdCfgBO;
import com.sumavision.tetris.sts.task.taskParamTask.Encode264Common;
import com.sumavision.tetris.sts.task.taskParamTask.Encode264CommonCPUx264BO;
import com.sumavision.tetris.sts.task.taskParamTask.Encode264TotalBO;
import com.sumavision.tetris.sts.task.taskParamTask.Encode265Common;
import com.sumavision.tetris.sts.task.taskParamTask.Encode265CommonCPUx265;
import com.sumavision.tetris.sts.task.taskParamTask.Encode265TotalBO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeCommon;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeMpeg2BO;
import com.sumavision.tetris.sts.task.taskParamTask.EncodeMpeg2M2vBO;
import com.sumavision.tetris.sts.task.taskParamTask.NameFiltersFor264265;





@Entity
@Table
public class VideoParamPO extends CommonPO<VideoParamPO> implements Serializable,Comparable<VideoParamPO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5029238576800483202L;

	/**
	 * 轨道id，节目信息中此id就是videoElements的索引，表示当前video是编的哪个视频
	 */
	private Integer trackId;
	
	/**
	 * osd参数
	 */
	
	private Set<OsdCfgBO> osdParams;
	
	public VideoParamPO(){
		osdParams = new HashSet<OsdCfgBO>();
	}
	@Transient
	public Set<OsdCfgBO> getOsdParams() {
		return osdParams;
	}

	public void setOsdParams(Set<OsdCfgBO> osdParams) {
		this.osdParams = osdParams;
	}
	public void addOsd(OsdCfgBO osdCfgBO) {
		this.osdParams.add(osdCfgBO);
	}
	
	/**
	 * json格式存储osd信息,JsonArray
	 */
	private String osdJson;
	
	
	
	private Integer pid;
	
	private Integer width;
	
	private Integer height;
	
	//输出帧率
	private Float fps;
	
	//视频码率
	private Long bitrate;
	
	private Long maxBitrate;
	
	//码率控制方式：ABR、VBR、NearCBR
	private String rcMode;
	
	//输出分辨率
	private String resolution;
	
	//输出宽高比
	private String ratio;
	
	/**
	 * 帧场模式
	 */
	private String encodingType;
	
	/**
	 * 场优先模式
	 */
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
	private Integer bFrames;
	
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

	//这六个参数，能力层做了修改，为了数据库统一，暂时不动，以后通过脚本更新数据库字段时统一删除
	private String blackPoint;
	private String blackWidth;
	private String blackHeight;
	private String cutPoint;
	private String cutWidth;
	private String cutHeight;


	private String autoCut;
	private String cutTop;
	private String cutBottom;
	private String cutLeft;
	private String cutRight;

	private Integer autoBlackSide;
	private String blackTop;
	private String blackBottom;
	private String blackLeft;
	private String blackRight;
	
	/**
	 * 亮度
	 */
	private Integer brightness;
	
	//private Integer contrast;
	/**
	 * 对比度
	 */
	private String contrast;
	
	//private Integer saturation;
	/**
     * 饱和度
     */
	private String saturation;

	private String gamma;

	/**
     * 色度
     */
	private String chroma;

	private String deblock;
	
	private String sharpen;
	
	/**
	 * "off" "on"
	 */
	private String enhance;
	
	private String scaleMode;
	
	private String openGop;
	
	/**
	 * 老命令发现参数，文档中没有
	 */
	private Integer keyintMax;
	
	private Integer vbvSize;
	
	private Integer bitDepth;

	//截图参数
	private String ftpUrl;
	private Integer timeSpan;
	private Integer maxBuf;
	private String jpegPre;
	private String cycDel;
	
	/**
     * 色彩增强
     */
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
	/**
	 * 颜色基准
	 */
	private String colorPrimary;
	/**
	 * 传输特质
	 */
	private String colorTransfer;
	/**
	 * 颜色空间
	 */
	private String colorSpace;
	/**
	 * 色彩范围
	 */
	private String colorRange;
	
	//新协议版本，视频解码方式
	private String decodeMode;
	
	//编码库"x264、x265、uux264、uux265、msdk、cuda"
	private String codeType;
	private Integer gop_size;
	private Integer vbv_buffer_size;
	private Integer keyint_min;
	//private Integer keyint_max;
	private String pixel_format;
	private Boolean low_latency;

	@Column
	public String getColorPrimary() {
		return colorPrimary;
	}

	public void setColorPrimary(String colorPrimary) {
		this.colorPrimary = colorPrimary;
	}

	@Column
	public String getColorTransfer() {
		return colorTransfer;
	}

	public void setColorTransfer(String colorTransfer) {
		this.colorTransfer = colorTransfer;
	}

	@Column
	public String getColorSpace() {
		return colorSpace;
	}

	public void setColorSpace(String colorSpace) {
		this.colorSpace = colorSpace;
	}

	@Column
	public String getColorRange() {
		return colorRange;
	}

	public void setColorRange(String colorRange) {
		this.colorRange = colorRange;
	}



	@Column
	public Integer getPid() {
		return pid;
	}
	
	@Column(columnDefinition="TEXT")
	public String getOsdJson() {
		return osdJson;
	}

	public void setOsdJson(String osdJson) {
		this.osdJson = osdJson;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	@Column
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	@Column
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	@Column
	public Float getFps() {
		return fps;
	}

	public void setFps(Float fps) {
		this.fps = fps;
	}
	@Column
	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}
	@Column
	public String getRcMode() {
		return rcMode;
	}

	public void setRcMode(String rcMode) {
		this.rcMode = rcMode;
	}
	@Column
	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	@Column
	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	@Column
	public String getEncodingType() {
		return encodingType;
	}

	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}
	
	@Column
	public String getFieldOrder() {
		return fieldOrder;
	}

	public void setFieldOrder(String fieldOrder) {
		this.fieldOrder = fieldOrder;
	}
	
	@Column
	public String getDeinterlace() {
		return deinterlace;
	}

	public void setDeinterlace(String deinterlace) {
		this.deinterlace = deinterlace;
	}
	@Column
	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}
	@Column
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	@Column
	public Float getLevel() {
		return level;
	}

	public void setLevel(Float level) {
		this.level = level;
	}
	@Column
	public Integer getRefFrames() {
		return refFrames;
	}

	public void setRefFrames(Integer refFrames) {
		this.refFrames = refFrames;
	}
	@Column
	public Integer getbFrames() {
		return bFrames;
	}

	public void setbFrames(Integer bFrames) {
		this.bFrames = bFrames;
	}
	@Column
	public String getRefine() {
		return refine;
	}

	public void setRefine(String refine) {
		this.refine = refine;
	}
	@Column
	public Integer getiLookahead() {
		return iLookahead;
	}

	public void setiLookahead(Integer iLookahead) {
		this.iLookahead = iLookahead;
	}
	@Column
	public String getMbtreeSwitch() {
		return mbtreeSwitch;
	}

	public void setMbtreeSwitch(String mbtreeSwitch) {
		this.mbtreeSwitch = mbtreeSwitch;
	}
	@Column
	public String getBframeAdaptive() {
		return bframeAdaptive;
	}

	public void setBframeAdaptive(String bframeAdaptive) {
		this.bframeAdaptive = bframeAdaptive;
	}
	@Column
	public String getBframeReference() {
		return bframeReference;
	}

	public void setBframeReference(String bframeReference) {
		this.bframeReference = bframeReference;
	}
	@Column
	public Long getImeRange() {
		return imeRange;
	}

	public void setImeRange(Long imeRange) {
		this.imeRange = imeRange;
	}
	@Column
	public String getScenecut() {
		return scenecut;
	}

	public void setScenecut(String scenecut) {
		this.scenecut = scenecut;
	}
	@Column
	public String getEntropyType() {
		return entropyType;
	}

	public void setEntropyType(String entropyType) {
		this.entropyType = entropyType;
	}
	@Column
	public String getBlackPoint() {
		return blackPoint;
	}

	public void setBlackPoint(String blackPoint) {
		this.blackPoint = blackPoint;
	}
	@Column
	public String getBlackWidth() {
		return blackWidth;
	}

	public void setBlackWidth(String blackWidth) {
		this.blackWidth = blackWidth;
	}
	@Column
	public String getBlackHeight() {
		return blackHeight;
	}

	public void setBlackHeight(String blackHeight) {
		this.blackHeight = blackHeight;
	}
	@Column
	public Integer getAutoBlackSide() {
		return autoBlackSide;
	}

	public void setAutoBlackSide(Integer autoBlackSide) {
		this.autoBlackSide = autoBlackSide;
	}
	@Column
	public String getCutPoint() {
		return cutPoint;
	}

	public void setCutPoint(String cutPoint) {
		this.cutPoint = cutPoint;
	}
	@Column
	public String getCutWidth() {
		return cutWidth;
	}

	public void setCutWidth(String cutWidth) {
		this.cutWidth = cutWidth;
	}
	@Column
	public String getCutHeight() {
		return cutHeight;
	}

	public void setCutHeight(String cutHeight) {
		this.cutHeight = cutHeight;
	}
	@Column
	public Integer getBrightness() {
		return brightness;
	}

	public void setBrightness(Integer brightness) {
		this.brightness = brightness;
	}
	@Column
	public String getContrast() {
		return contrast;
	}

	public void setContrast(String contrast) {
		this.contrast = contrast;
	}
	@Column
	public String getSaturation() {
		return saturation;
	}

	public void setSaturation(String saturation) {
		this.saturation = saturation;
	}
	@Column
	public String getEnhance() {
		return enhance;
	}

	public void setEnhance(String enhance) {
		this.enhance = enhance;
	}
	@Column
	public String getScaleMode() {
		return scaleMode;
	}

	public void setScaleMode(String scaleMode) {
		this.scaleMode = scaleMode;
	}
	@Column
	public Integer getKeyintMax() {
		return keyintMax;
	}

	public void setKeyintMax(Integer keyintMax) {
		this.keyintMax = keyintMax;
	}
	@Column
	public Integer getTrackId() {
		return trackId;
	}

	public void setTrackId(Integer trackId) {
		this.trackId = trackId;
	}

	@Column
	public Integer getVbvSize() {
		return vbvSize;
	}

	public void setVbvSize(Integer vbvSize) {
		this.vbvSize = vbvSize;
	}

	@Column
	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	@Column
	public Integer getTimeSpan() {
		return timeSpan;
	}

	public void setTimeSpan(Integer timeSpan) {
		this.timeSpan = timeSpan;
	}

	@Column
	public Integer getMaxBuf() {
		return maxBuf;
	}

	public void setMaxBuf(Integer maxBuf) {
		this.maxBuf = maxBuf;
	}

	@Column
	public String getJpegPre() {
		return jpegPre;
	}

	public void setJpegPre(String jpegPre) {
		this.jpegPre = jpegPre;
	}
	
	@Column
	public String getColorEnhance() {
		return colorEnhance;
	}

	public void setColorEnhance(String colorEnhance) {
		this.colorEnhance = colorEnhance;
	}
	
	@Column
	public String getDenoise() {
		return denoise;
	}

	public void setDenoise(String denoise) {
		this.denoise = denoise;
	}

	@Column
	public Long getMaxBitrate() {
		return maxBitrate;
	}

	public void setMaxBitrate(Long maxBitrate) {
		this.maxBitrate = maxBitrate;
	}

	@Column
	public String getPicStruct() {
		return picStruct;
	}

	public void setPicStruct(String picStruct) {
		this.picStruct = picStruct;
	}

	@Column
	public String getOpenGop() {
		return openGop;
	}

	public void setOpenGop(String openGop) {
		this.openGop = openGop;
	}
	@Column
	public String getSharpen() {
		return sharpen;
	}

	public void setSharpen(String sharpen) {
		this.sharpen = sharpen;
	}
	
	@Column
	public Integer getBitDepth() {
		return bitDepth;
	}

	public void setBitDepth(Integer bitDepth) {
		this.bitDepth = bitDepth;
	}

	@Column
	public String getDeblock() {
		return deblock;
	}

	public void setDeblock(String deblock) {
		this.deblock = deblock;
	}

	@Column
	public String getGamma() {
		return gamma;
	}

	public void setGamma(String gamma) {
		this.gamma = gamma;
	}

	@Column
	public String getChroma() {
		return chroma;
	}

	public void setChroma(String chroma) {
		this.chroma = chroma;
	}
	@Column
	public String getAutoCut() {
		return autoCut;
	}

	public void setAutoCut(String autoCut) {
		this.autoCut = autoCut;
	}
	@Column
	public String getCutTop() {
		return cutTop;
	}

	public void setCutTop(String cutTop) {
		this.cutTop = cutTop;
	}
	@Column
	public String getCutBottom() {
		return cutBottom;
	}

	public void setCutBottom(String cutBottom) {
		this.cutBottom = cutBottom;
	}
	@Column
	public String getCutLeft() {
		return cutLeft;
	}

	public void setCutLeft(String cutLeft) {
		this.cutLeft = cutLeft;
	}
	@Column
	public String getCutRight() {
		return cutRight;
	}

	public void setCutRight(String cutRight) {
		this.cutRight = cutRight;
	}
	@Column
	public String getBlackTop() {
		return blackTop;
	}

	public void setBlackTop(String blackTop) {
		this.blackTop = blackTop;
	}
	@Column
	public String getBlackBottom() {
		return blackBottom;
	}

	public void setBlackBottom(String blackBottom) {
		this.blackBottom = blackBottom;
	}
	@Column
	public String getBlackLeft() {
		return blackLeft;
	}

	public void setBlackLeft(String blackLeft) {
		this.blackLeft = blackLeft;
	}
	@Column
	public String getBlackRight() {
		return blackRight;
	}

	public void setBlackRight(String blackRight) {
		this.blackRight = blackRight;
	}
	
	@Column
	public String getCycDel() {
		return cycDel;
	}

	public void setCycDel(String cycDel) {
		this.cycDel = cycDel;
	}

	@Column
	public Integer getIdrFrequency() {
		return idrFrequency;
	}

	public void setIdrFrequency(Integer idrFrequency) {
		this.idrFrequency = idrFrequency;
	}

	@Column
	public String getDownFrameRate() {
		return downFrameRate;
	}

	public void setDownFrameRate(String downFrameRate) {
		this.downFrameRate = downFrameRate;
	}

	@Column
	public String getChromaFormat() {
		return chromaFormat;
	}

	public void setChromaFormat(String chromaFormat) {
		this.chromaFormat = chromaFormat;
	}

	@Column
	public String getWp() {
		return wp;
	}

	public void setWp(String wp) {
		this.wp = wp;
	}

	@Column
	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}
	@Column
	public String getSao() {
		return sao;
	}

	public void setSao(String sao) {
		this.sao = sao;
	}
	@Column
	public String getAmp() {
		return amp;
	}

	public void setAmp(String amp) {
		this.amp = amp;
	}
	
	@Column
	public String getDecodeMode() {
		return decodeMode;
	}
	public void setDecodeMode(String decodeMode) {
		this.decodeMode = decodeMode;
	}
	
	@Column
	public Integer getKeyint_min() {
		return keyint_min;
	}
	public void setKeyint_min(Integer keyint_min) {
		this.keyint_min = keyint_min;
	}
//	@Column
//	@XmlAttribute(name = "keyint_max")
//	public Integer getKeyint_max() {
//		return keyint_max;
//	}
//	public void setKeyint_max(Integer keyint_max) {
//		this.keyint_max = keyint_max;
//	}
	@Column
	public String getPixel_format() {
		return pixel_format;
	}
	public void setPixel_format(String pixel_format) {
		this.pixel_format = pixel_format;
	}
	@Column
	public Boolean getLow_latency() {
		return low_latency;
	}
	public void setLow_latency(Boolean low_latency) {
		this.low_latency = low_latency;
	}
	
	@Column
	public Integer getGop_size() {
		return gop_size;
	}
	public void setGop_size(Integer gop_size) {
		this.gop_size = gop_size;
	}
	
	@Column
	public Integer getVbv_buffer_size() {
		return vbv_buffer_size;
	}
	public void setVbv_buffer_size(Integer vbv_buffer_size) {
		this.vbv_buffer_size = vbv_buffer_size;
	}
	
	
	@Column
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	
	@Override
	public int compareTo(VideoParamPO o) {
		// TODO Auto-generated method stub
		if(this.getTrackId() > o.getTrackId()){
			return 1;
		}
		return -1;
	}
	
	
	//新协议中h264（h265类似）包括：CPU-x264、CPU-uux264、GPU-Intel、GPU-Nvidia
	public NameFiltersFor264265 getEncodeCommon(String codec){
		EncodeCommon encodeCommon = null;
		ArrayList<NameFilter> nameFilters = new ArrayList<NameFilter>();
		switch (codec) {
			case "h264":
				Encode264Common encode264Common = null;
				if (codeType.contains("x264")) {
					encode264Common = new Encode264CommonCPUx264BO(keyint_min, keyintMax, refine, pixel_format,
							bframeReference, mbtreeSwitch.contains("Off")?false:true, imeRange.intValue(), low_latency);
				}else if (codeType.contains("uux264")) {
					
				}else if (codeType.contains("msdk")) {
					
				}else if (codeType.contains("cuda")) {
					
				}
				
				NameFilter Encode264CommonNameFilter = new NameFilter() {
					@Override
					public String process(Object object, String name, Object value) {
						// TODO Auto-generated method stub
						if (value instanceof Encode264Common) {
							switch (codeType) {
							case "x264":
								return "x264";
							case "uux264":
								return "uux264";
							case "msdk":
								return "msdk_encode";
							case "cuda":
								return "hevc";
							}
						}
						return name;
					}
				};
				nameFilters.add(Encode264CommonNameFilter);
				
				encodeCommon = new Encode264TotalBO(width, height, ratio, fps.toString(), gop_size,
						rcMode.toLowerCase(), bitrate.intValue()*1000, maxBitrate==null?1500000:maxBitrate.intValue()*1000, bFrames, openGop.contains("Off")?false:true, 
						profile, level.intValue()*10, refFrames, iLookahead, scenecut.contains("Off")?false:true, 
						mbtreeSwitch.contains("Off")?false:true, entropyType.toLowerCase(), encodingType.toLowerCase(), vbv_buffer_size, encode264Common);
				break;
			case "h265":
				Encode265Common encode265Common = null;
				if (codeType.contains("x265")) {
					encode265Common = new Encode265CommonCPUx265("fast", refine, "yuv420", bframeReference);
				}else if (codeType.contains("uux264")) {
					
				}else if (codeType.contains("msdk")) {
					
				}else if (codeType.contains("cuda")) {
					
				}
				
				NameFilter Encode265CommonNameFilter = new NameFilter() {
					@Override
					public String process(Object object, String name, Object value) {
						// TODO Auto-generated method stub
						if (value instanceof Encode265Common) {
							switch (codeType) {
							case "x265":
								return "x265";
							case "ux265":
								return "ux265";
							case "msdk":
								return "msdk_encode";
							case "cuda":
								return "hevc";
							}
						}
						return name;
					}
				};
				nameFilters.add(Encode265CommonNameFilter);
				
				encodeCommon = new Encode265TotalBO(width, height, ratio, fps.toString(), bFrames, gop_size,
						rcMode.toLowerCase(), bitrate.intValue()*1000,  maxBitrate==null?1500000:maxBitrate.intValue()*1000, openGop.contains("Off")?false:true, profile,
						level.intValue()*10, mbtreeSwitch.contains("Off")?false:true, refFrames, iLookahead, bframeAdaptive.contains("Off")?false:true, 1500000, encode265Common);
				break;
			case "mpeg2":
			case "mpeg2-video":
				EncodeMpeg2M2vBO m2v = new EncodeMpeg2M2vBO(true, scenecut==null?null:(scenecut.contains("Off")?false:true), 100);
				encodeCommon = new EncodeMpeg2BO(width, height, ratio, fps.toString(), rcMode.toLowerCase(), bitrate.intValue()*1000, 
						 maxBitrate==null?1500000:maxBitrate.intValue()*1000, bFrames, 25, openGop.contains("Off")?false:true, m2v);
				break;
			case "avs":
				//暂留
				break;
		}
		
		NameFiltersFor264265 nameFiltersFor264265 = new NameFiltersFor264265();
		nameFiltersFor264265.setEncodeCommon(encodeCommon);
		nameFiltersFor264265.setNameFilters(nameFilters);
		return nameFiltersFor264265;
	}
	
	
	
	public Boolean isSameWithCfg(VideoParamPO videoParamPO, String transType){
		if(videoParamPO == null){
			return false;
		}
		
		//基本参数比较
		if (this.pid == null) {
			if (videoParamPO.pid != null)
				return false;
		} else if (!this.pid.equals(videoParamPO.pid))
			return false;
		if (this.codec == null) {
			if (videoParamPO.codec != null)
				return false;
		} else if (!this.codec.equals(videoParamPO.codec))
			return false;
		if (this.ratio == null) {
			if (videoParamPO.ratio != null)
				return false;
		} else if (!this.ratio.equals(videoParamPO.ratio))
			return false;
		if (this.rcMode == null) {
			if (videoParamPO.rcMode != null)
				return false;
		} else if (!this.rcMode.equals(videoParamPO.rcMode))
			return false;
		if (this.fieldOrder == null){
			if (videoParamPO.fieldOrder!=null)
				return false;
		}else if (!this.fieldOrder.equals(videoParamPO.fieldOrder))
			return false;

		//视频参数比较
		if (this.resolution == null) {
			if (videoParamPO.resolution != null)
				return false;
		} else if (!this.resolution.equals(videoParamPO.resolution))
			return false;
		if (this.bitrate == null) {
			if (videoParamPO.bitrate != null)
				return false;
		} else if (!(this.bitrate).equals(videoParamPO.bitrate/1000))
			return false;
		if (this.level == null) {
			if (videoParamPO.level != null)
				return false;
		} else if (!this.level.equals(videoParamPO.level))
			return false;
		if (this.profile == null) {
			if (videoParamPO.profile != null)
				return false;
		} else if (!this.profile.equals(videoParamPO.profile))
			return false;
		if (this.scenecut == null) {
			if (videoParamPO.scenecut != null)
				return false;
		} else if (!this.scenecut.equals(videoParamPO.scenecut))
			return false;
		if (this.openGop == null) {
			if (videoParamPO.openGop != null)
				return false;
		} else if (!this.openGop.equals(videoParamPO.openGop))
			return false;
		if (this.bitDepth == null) {
			if (videoParamPO.bitDepth != null)
				return false;
		} else if (!this.bitDepth.equals(videoParamPO.bitDepth))
			return false;
		
		//不同转码器下发参数不同
		if(transType.equals("SCU300")){
			if (this.idrFrequency == null) {
				if (videoParamPO.idrFrequency != null)
					return false;
			} else if (!this.idrFrequency.equals(videoParamPO.idrFrequency))
				return false;
			if (this.downFrameRate == null) {
				if (videoParamPO.downFrameRate != null)
					return false;
			} else if (!this.downFrameRate.equals(videoParamPO.downFrameRate))
				return false;
			if (this.chromaFormat == null) {
				if (videoParamPO.chromaFormat != null)
					return false;
			} else if (!this.chromaFormat.equals(videoParamPO.chromaFormat))
				return false;
			if (this.wp == null) {
				if (videoParamPO.wp != null)
					return false;
			} else if (!this.wp.equals(videoParamPO.wp))
				return false;
//			if (this.tier == null) {
//				if (videoParamPO.tier != null)
//					return false;
//			} else if (!this.tier.equals(videoParamPO.tier))
//				return false;
			if (this.sao == null) {
				if (videoParamPO.sao != null)
					return false;
			} else if (!this.sao.equals(videoParamPO.sao))
				return false;
			if (this.amp == null) {
				if (videoParamPO.amp != null)
					return false;
			} else if (!this.amp.equals(videoParamPO.amp))
				return false;
		}else{
			if (this.keyintMax == null) {
				if (videoParamPO.keyintMax != null)
					return false;
			} else if (!this.keyintMax.equals(videoParamPO.keyintMax))
				return false;
			if (this.bFrames == null) {
				if (videoParamPO.bFrames != null)
					return false;
			} else if (!this.bFrames.equals(videoParamPO.bFrames))
				return false;
			if (this.encodingType == null) {
				if (videoParamPO.encodingType != null)
					return false;
			} else if (!this.encodingType.equals(videoParamPO.encodingType))
				return false;
			if (this.maxBitrate == null) {
				if (videoParamPO.maxBitrate != null)
					return false;
			} else if (!(this.maxBitrate).equals(videoParamPO.maxBitrate))
				return false;
			if (this.fps == null) {
				if (videoParamPO.fps != null)
					return false;
			} else if (!this.fps.equals(videoParamPO.fps))
				return false;
			if (this.refine == null) {
				if (videoParamPO.refine != null)
					return false;
			} else if (!this.refine.equals(videoParamPO.refine))
				return false;
			if (this.entropyType == null) {
				if (videoParamPO.entropyType != null)
					return false;
			} else if (!this.entropyType.equals(videoParamPO.entropyType))
				return false;
			if (this.refFrames == null) {
				if (videoParamPO.refFrames != null)
					return false;
			} else if (!this.refFrames.equals(videoParamPO.refFrames))
				return false;
			if (this.imeRange == null) {
				if (videoParamPO.imeRange != null)
					return false;
			} else if (!this.imeRange.equals(videoParamPO.imeRange))
				return false;
			if (this.bframeAdaptive == null) {
				if (videoParamPO.bframeAdaptive != null)
					return false;
			} else if (!this.bframeAdaptive.equals(videoParamPO.bframeAdaptive))
				return false;
			if (this.bframeReference == null) {
				if (videoParamPO.bframeReference != null)
					return false;
			} else if (!this.bframeReference.equals(videoParamPO.bframeReference))
				return false;
			if (this.mbtreeSwitch == null) {
				if (videoParamPO.mbtreeSwitch != null)
					return false;
			} else if (!this.mbtreeSwitch.equals(videoParamPO.mbtreeSwitch))
				return false;
			if (this.iLookahead == null) {
				if (videoParamPO.iLookahead != null)
					return false;
			} else if (!this.iLookahead.equals(videoParamPO.iLookahead))
				return false;
			if (this.picStruct == null) {
				if (videoParamPO.picStruct != null)
					return false;
			} else if (!this.picStruct.equals(videoParamPO.picStruct))
				return false;
			if (this.deblock == null) {
				if (videoParamPO.deblock != null)
					return false;
			} else if (!this.deblock.equals(videoParamPO.deblock))
				return false;
		}
		
		//图像处理参数
		if (this.deinterlace == null) {
			if (videoParamPO.deinterlace != null)
				return false;
		} else if (!this.deinterlace.equals(videoParamPO.deinterlace))
			return false;
		if (this.scaleMode == null) {
			if (videoParamPO.scaleMode != null)
				return false;
		} else if (!this.scaleMode.equals(videoParamPO.scaleMode))
			return false;
		if (this.sharpen == null) {
			if (videoParamPO.sharpen != null)
				return false;
		} else if (!this.sharpen.equals(videoParamPO.sharpen))
			return false;
		if (this.colorEnhance == null) {
			if (videoParamPO.colorEnhance != null)
				return false;
		} else if (!this.colorEnhance.equals(videoParamPO.colorEnhance))
			return false;
		if (this.denoise == null) {
			if (videoParamPO.denoise != null)
				return false;
		} else if (!this.denoise.equals(videoParamPO.denoise))
			return false;
		if (this.brightness == null) {
			if (videoParamPO.brightness != null)
				return false;
		} else if (!this.brightness.equals(videoParamPO.brightness))
			return false;
		if (this.contrast == null) {
			if (videoParamPO.contrast != null)
				return false;
		} else if (!this.contrast.equals(videoParamPO.contrast))
			return false;
		if (this.chroma == null) {
			if (videoParamPO.chroma != null)
				return false;
		} else if (!this.chroma.equals(videoParamPO.chroma))
			return false;
		if (this.saturation == null) {
			if (videoParamPO.saturation != null)
				return false;
		} else if (!this.saturation.equals(videoParamPO.saturation))
			return false;
		if (this.gamma == null) {
			if (videoParamPO.gamma != null)
				return false;
		} else if (!this.gamma.equals(videoParamPO.gamma))
			return false;
		if (this.colorPrimary == null) {
			if (videoParamPO.colorPrimary != null)
				return false;
		} else if (!this.colorPrimary.equals(videoParamPO.colorPrimary))
			return false;
		if (this.colorTransfer == null) {
			if (videoParamPO.colorTransfer != null)
				return false;
		} else if (!this.colorTransfer.equals(videoParamPO.colorTransfer))
			return false;
		if (this.colorSpace == null) {
			if (videoParamPO.colorSpace != null)
				return false;
		} else if (!this.colorSpace.equals(videoParamPO.colorSpace))
			return false;
		if (this.colorRange == null) {
			if (videoParamPO.colorRange != null)
				return false;
		} else if (!this.colorRange.equals(videoParamPO.colorRange))
			return false;
		if (this.autoBlackSide == null) {
			if (videoParamPO.autoBlackSide != null)
				return false;
		} else if (!this.autoBlackSide.equals(videoParamPO.autoBlackSide))
			return false;
		if (this.blackTop == null) {
			if (videoParamPO.blackTop != null)
				return false;
		} else if (!this.blackTop.equals(videoParamPO.blackTop))
			return false;
		if (this.blackBottom == null) {
			if (videoParamPO.blackBottom != null)
				return false;
		} else if (!this.blackBottom.equals(videoParamPO.blackBottom))
			return false;
		if (this.blackLeft == null) {
			if (videoParamPO.blackLeft != null)
				return false;
		} else if (!this.blackLeft.equals(videoParamPO.blackLeft))
			return false;
		if (this.blackRight == null) {
			if (videoParamPO.blackRight != null)
				return false;
		} else if (!this.blackRight.equals(videoParamPO.blackRight))
			return false;
//		if (this.autoCut == null) {
//			if (videoParamPO.autoCut != null)
//				return false;
//		} else if (!this.autoCut.equals(videoParamPO.autoCut))
//			return false;
		if (this.cutTop == null) {
			if (videoParamPO.cutTop != null)
				return false;
		} else if (!this.cutTop.equals(videoParamPO.cutTop))
			return false;
		if (this.cutBottom == null) {
			if (videoParamPO.cutBottom != null)
				return false;
		} else if (!this.cutBottom.equals(videoParamPO.cutBottom))
			return false;
		if (this.cutLeft == null) {
			if (videoParamPO.cutLeft != null)
				return false;
		} else if (!this.cutLeft.equals(videoParamPO.cutLeft))
			return false;
		if (this.cutRight == null) {
			if (videoParamPO.cutRight != null)
				return false;
		} else if (!this.cutRight.equals(videoParamPO.cutRight))
			return false;
	
		//截图
		if (this.ftpUrl == null) {
			if (videoParamPO.ftpUrl != null)
				return false;
		} else if(!this.ftpUrl.equals("")){
			try {
				if(videoParamPO.ftpUrl == null)
					return false;
				else if(!new String(this.ftpUrl.getBytes("utf-8"),"gbk").equals(videoParamPO.ftpUrl))
					return false;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (this.timeSpan == null) {
				if (videoParamPO.timeSpan != null)
					return false;
			} else if (!this.timeSpan.equals(videoParamPO.timeSpan/1000))
				return false;
			if (this.maxBuf == null) {
				if (videoParamPO.maxBuf != null)
					return false;
			} else if (!this.maxBuf.equals(videoParamPO.maxBuf))
				return false;
			if (this.jpegPre == null) {
				if (videoParamPO.jpegPre != null)
					return false;
			} else if (!this.jpegPre.equals(videoParamPO.jpegPre))
				return false;
			if (this.cycDel == null) {
				if (videoParamPO.cycDel != null)
					return false;
			} else if (!this.cycDel.equals(videoParamPO.cycDel))
				return false;
		}
		
		//osd 预览参数
		if(this.osdJson == null){
			if(videoParamPO.osdParams != null)
				return false;
		} else{
			List<OsdCfgBO> osdList = OsdCfgBO.transFromJson(this.osdJson);
			if(osdList.size() > 0 && osdList.size() == videoParamPO.osdParams.size()){
				for (int i = 1; i < osdList.size() + 1; i++) {
					for (OsdCfgBO osdCfgBO : videoParamPO.osdParams) {
						if(i == osdCfgBO.getId()){
							if(!osdList.get(i - 1).isSameWithCfg(osdCfgBO)){
								return false;
							}else{
								continue;
		}}}}}}
		
		return true;
	}}
