package com.sumavision.tetris.guide.BO;

public class GuideX264BO {

	private int keyint_min;
	
	private int me_range;
	
	/**原始数据格式 */
	private String pixel_format;
	
	//能设置为String么？
	/**B帧参考模式 */
	private String bframe_reference;
	
	/**B帧自适应*/
	private boolean bframe_adaptive;
	
	/**质量模式 */
	private int refine;
	
	private String tune_mode;

	public int getKeyint_min() {
		return keyint_min;
	}

	public void setKeyint_min(int keyint_min) {
		this.keyint_min = keyint_min;
	}

	public int getMe_range() {
		return me_range;
	}

	public void setMe_range(int me_range) {
		this.me_range = me_range;
	}

	public String getPixel_format() {
		return pixel_format;
	}

	public void setPixel_format(String pixel_format) {
		this.pixel_format = pixel_format;
	}

	public String getBframe_reference() {
		return bframe_reference;
	}

	public void setBframe_reference(String bframe_reference) {
		this.bframe_reference = bframe_reference;
	}

	public boolean isBframe_adaptive() {
		return bframe_adaptive;
	}

	public void setBframe_adaptive(boolean bframe_adaptive) {
		this.bframe_adaptive = bframe_adaptive;
	}

	public int getRefine() {
		return refine;
	}

	public void setRefine(int refine) {
		this.refine = refine;
	}

	public String getTune_mode() {
		return tune_mode;
	}

	public void setTune_mode(String tune_mode) {
		this.tune_mode = tune_mode;
	}
}
