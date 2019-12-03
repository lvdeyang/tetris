package com.sumavision.tetris.capacity.bo.task;

import com.alibaba.fastjson.JSONObject;

/**
 * osd参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月30日 上午8:32:10
 */
public class OsdBO {

	private SubtitleBO subtitle;
	
	private LogoBO logo;
	
	private FuzzyBO fuzzy;
	
	private JSONObject screen_cap;

	public SubtitleBO getSubtitle() {
		return subtitle;
	}

	public OsdBO setSubtitle(SubtitleBO subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public LogoBO getLogo() {
		return logo;
	}

	public OsdBO setLogo(LogoBO logo) {
		this.logo = logo;
		return this;
	}

	public FuzzyBO getFuzzy() {
		return fuzzy;
	}

	public OsdBO setFuzzy(FuzzyBO fuzzy) {
		this.fuzzy = fuzzy;
		return this;
	}

	public JSONObject getScreen_cap() {
		return screen_cap;
	}

	public OsdBO setScreen_cap(JSONObject screen_cap) {
		this.screen_cap = screen_cap;
		return this;
	}
	
}
