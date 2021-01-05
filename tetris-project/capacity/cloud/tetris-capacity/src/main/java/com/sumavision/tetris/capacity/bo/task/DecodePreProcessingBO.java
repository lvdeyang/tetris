package com.sumavision.tetris.capacity.bo.task;

import com.alibaba.fastjson.annotation.JSONField;

public class DecodePreProcessingBO {

	private DeinterlaceBO deinterlace;

	private FpsConvertBO fps_convert;

	private CutBO cut;

	private FuzzyBO fuzzy;

	private TextOsdBO text_osd;

	private StaticPictureOsdBO static_pic_osd;

	private DynamicPictureOsdBO dynamic_pic_osd;

	private EnhanceBO enhance;

	private ImageFilterBO imageFilterBO;

	@JSONField(name = "SDRHDRConvert")
	private HdrBO SDRHDRConvert;

	public DeinterlaceBO getDeinterlace() {
		return deinterlace;
	}

	public void setDeinterlace(DeinterlaceBO deinterlace) {
		this.deinterlace = deinterlace;
	}

	public FpsConvertBO getFps_convert() {
		return fps_convert;
	}

	public void setFps_convert(FpsConvertBO fps_convert) {
		this.fps_convert = fps_convert;
	}

	public CutBO getCut() {
		return cut;
	}

	public void setCut(CutBO cut) {
		this.cut = cut;
	}

	public FuzzyBO getFuzzy() {
		return fuzzy;
	}

	public void setFuzzy(FuzzyBO fuzzy) {
		this.fuzzy = fuzzy;
	}

	public TextOsdBO getText_osd() {
		return text_osd;
	}

	public void setText_osd(TextOsdBO text_osd) {
		this.text_osd = text_osd;
	}

	public StaticPictureOsdBO getStatic_pic_osd() {
		return static_pic_osd;
	}

	public void setStatic_pic_osd(StaticPictureOsdBO static_pic_osd) {
		this.static_pic_osd = static_pic_osd;
	}

	public DynamicPictureOsdBO getDynamic_pic_osd() {
		return dynamic_pic_osd;
	}

	public void setDynamic_pic_osd(DynamicPictureOsdBO dynamic_pic_osd) {
		this.dynamic_pic_osd = dynamic_pic_osd;
	}

	public EnhanceBO getEnhance() {
		return enhance;
	}

	public void setEnhance(EnhanceBO enhance) {
		this.enhance = enhance;
	}

	public ImageFilterBO getImageFilterBO() {
		return imageFilterBO;
	}

	public void setImageFilterBO(ImageFilterBO imageFilterBO) {
		this.imageFilterBO = imageFilterBO;
	}

	public HdrBO getSDRHDRConvert() {
		return SDRHDRConvert;
	}

	public void setSDRHDRConvert(HdrBO SDRHDRConvert) {
		this.SDRHDRConvert = SDRHDRConvert;
	}
}
