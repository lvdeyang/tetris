package com.sumavision.tetris.capacity.bo.task;

import java.util.List;

/**
 * 区域模糊参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月30日 上午8:47:54
 */
public class FuzzyBO {

	private List<FuzzyObjectBO> fuzzys;
	
	private String plat;
	
	private Integer nv_card_idx;

	public List<FuzzyObjectBO> getFuzzys() {
		return fuzzys;
	}

	public FuzzyBO setFuzzys(List<FuzzyObjectBO> fuzzys) {
		this.fuzzys = fuzzys;
		return this;
	}

	public String getPlat() {
		return plat;
	}

	public FuzzyBO setPlat(String plat) {
		this.plat = plat;
		return this;
	}

	public Integer getNv_card_idx() {
		return nv_card_idx;
	}

	public FuzzyBO setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
		return this;
	}
	
}
