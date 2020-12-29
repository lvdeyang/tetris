package com.sumavision.tetris.capacity.bo.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 预处理参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月29日 下午7:04:56
 */
public class PreProcessingBO {

	/** 缩放 */
	@JSONField(ordinal = 1)
	private ScaleBO scale;

	/** 帧率变换 */
	@JSONField(ordinal = 2)
	private FpsConvertBO fps_convert;

	/** 裁剪 */
	@JSONField(ordinal = 3)
	private CutBO cut;
	
	/** 文本osd */
	@JSONField(ordinal = 4)
	private TextOsdBO text_osd;
	
	/** 静态图片osd */
	@JSONField(ordinal = 5)
	private StaticPictureOsdBO static_pic_osd;
	
	/** 动态图片osd */
	@JSONField(ordinal = 6)
	private DynamicPictureOsdBO dynamic_pic_osd;
	
	/** 模糊 */
	@JSONField(ordinal = 7)
	private FuzzyBO fuzzy;
	
	/** HDR */
	@JSONField(name = "SDRHDRConvert",ordinal = 8)
	private HdrBO SDRHDRConvert;
	
	/** 图像增强 */
	@JSONField(ordinal = 9)
	private EnhanceBO enhance;
	
	/** 图像滤波 */
	@JSONField(ordinal = 10)
	private ImageFilterBO imageFilter;

	/** 音频增益 */
	@JSONField(ordinal = 11)
	private AudioGainBO aud_gain;

	/** 重采样 */
	@JSONField(ordinal = 12)
	private ResampleBO resample;
	

	/** 音柱 */
	@JSONField(ordinal = 13)
	private AudioColumnBO audiocolumn;

	public ScaleBO getScale() {
		return scale;
	}

	public PreProcessingBO setScale(ScaleBO scale) {
		this.scale = scale;
		return this;
	}

	public CutBO getCut() {
		return cut;
	}

	public PreProcessingBO setCut(CutBO cut) {
		this.cut = cut;
		return this;
	}

	public TextOsdBO getText_osd() {
		return text_osd;
	}

	public PreProcessingBO setText_osd(TextOsdBO text_osd) {
		this.text_osd = text_osd;
		return this;
	}

	public StaticPictureOsdBO getStatic_pic_osd() {
		return static_pic_osd;
	}

	public PreProcessingBO setStatic_pic_osd(StaticPictureOsdBO static_pic_osd) {
		this.static_pic_osd = static_pic_osd;
		return this;
	}

	public DynamicPictureOsdBO getDynamic_pic_osd() {
		return dynamic_pic_osd;
	}

	public PreProcessingBO setDynamic_pic_osd(DynamicPictureOsdBO dynamic_pic_osd) {
		this.dynamic_pic_osd = dynamic_pic_osd;
		return this;
	}

	public EnhanceBO getEnhance() {
		return enhance;
	}

	public PreProcessingBO setEnhance(EnhanceBO enhance) {
		this.enhance = enhance;
		return this;
	}

	public FuzzyBO getFuzzy() {
		return fuzzy;
	}

	public PreProcessingBO setFuzzy(FuzzyBO fuzzy) {
		this.fuzzy = fuzzy;
		return this;
	}

	public ResampleBO getResample() {
		return resample;
	}

	public PreProcessingBO setResample(ResampleBO resample) {
		this.resample = resample;
		return this;
	}

	public FpsConvertBO getFps_convert() {
		return fps_convert;
	}

	public PreProcessingBO setFps_convert(FpsConvertBO fps_convert) {
		this.fps_convert = fps_convert;
		return this;
	}

	public HdrBO getSDRHDRConvert() {
		return SDRHDRConvert;
	}

	public PreProcessingBO setSDRHDRConvert(HdrBO sDRHDRConvert) {
		SDRHDRConvert = sDRHDRConvert;
		return this;
	}

	public ImageFilterBO getImageFilter() {
		return imageFilter;
	}

	public PreProcessingBO setImageFilter(ImageFilterBO imageFilter) {
		this.imageFilter = imageFilter;
		return this;
	}

	public AudioGainBO getAud_gain() {
		return aud_gain;
	}

	public PreProcessingBO setAud_gain(AudioGainBO aud_gain) {
		this.aud_gain = aud_gain;
		return this;
	}

	public AudioColumnBO getAudiocolumn() {
		return audiocolumn;
	}

	public PreProcessingBO setAudiocolumn(AudioColumnBO audiocolumn) {
		this.audiocolumn = audiocolumn;
		return this;
	}
}
