package com.sumavision.tetris.capacity.bo.task;

import com.sumavision.tetris.application.template.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文本osd参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月9日 下午2:28:33
 */
public class TextOsdBO {
	
	private String plat = "cpu";
	
	private Integer nv_card_idx;

	private List<OsdBO> text_osds;

	public String getPlat() {
		return plat;
	}

	public TextOsdBO setPlat(String plat) {
		this.plat = plat;
		return this;
	}

	public Integer getNv_card_idx() {
		return nv_card_idx;
	}

	public TextOsdBO setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
		return this;
	}

	public List<OsdBO> getText_osds() {
		return text_osds;
	}

	public TextOsdBO setText_osds(List<OsdBO> text_osds) {
		this.text_osds = text_osds;
		return this;
	}

	public TextOsdBO() {
	}

	public TextOsdBO(ProcessVO processVO) {
		this.text_osds = new ArrayList<>();
		this.plat = processVO.getPlat().name().toLowerCase();
		this.nv_card_idx = processVO.getNv_card_idx();
		for (int i = 0; i < processVO.getPic_osds().size(); i++) {
			TextOsdVO textOsdVO = processVO.getText_osds().get(i);
			this.text_osds.add(new OsdBO(textOsdVO));
		}
	}
}
