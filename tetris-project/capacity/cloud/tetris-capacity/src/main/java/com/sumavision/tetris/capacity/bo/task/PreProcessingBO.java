package com.sumavision.tetris.capacity.bo.task;

/**
 * 预处理参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月29日 下午7:04:56
 */
public class PreProcessingBO {

	/** 去交错 */
	private DeinterlaceBO deinterlace;
	
	/** 缩放 */
	private ScaleBO scale;
	
	/** 裁剪 */
	private CutBO cut;
	
	private OsdBO osd;
	
	/** 图像增强 */
	private EnhanceBO enhance;
	
	/** 重采样 */
	private ResampleBO resample;

	public DeinterlaceBO getDeinterlace() {
		return deinterlace;
	}

	public PreProcessingBO setDeinterlace(DeinterlaceBO deinterlace) {
		this.deinterlace = deinterlace;
		return this;
	}

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

	public OsdBO getOsd() {
		return osd;
	}

	public PreProcessingBO setOsd(OsdBO osd) {
		this.osd = osd;
		return this;
	}

	public EnhanceBO getEnhance() {
		return enhance;
	}

	public PreProcessingBO setEnhance(EnhanceBO enhance) {
		this.enhance = enhance;
		return this;
	}

	public ResampleBO getResample() {
		return resample;
	}

	public PreProcessingBO setResample(ResampleBO resample) {
		this.resample = resample;
		return this;
	}
	
}
