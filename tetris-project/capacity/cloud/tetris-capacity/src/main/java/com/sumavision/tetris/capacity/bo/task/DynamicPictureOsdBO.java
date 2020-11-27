package com.sumavision.tetris.capacity.bo.task;

import com.sumavision.tetris.application.template.PictureOsdVO;
import com.sumavision.tetris.application.template.ProcessVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态图片参数<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月12日 下午3:56:51
 */
public class DynamicPictureOsdBO {

	private String plat = "cpu";
	
	private Integer nv_card_idx;

	private List<PictureOsdObjectBO> dynamic_pic_osds  ;

	public String getPlat() {
		return plat;
	}

	public DynamicPictureOsdBO setPlat(String plat) {
		this.plat = plat;
		return this;
	}

	public Integer getNv_card_idx() {
		return nv_card_idx;
	}

	public DynamicPictureOsdBO setNv_card_idx(Integer nv_card_idx) {
		this.nv_card_idx = nv_card_idx;
		return this;
	}

	public List<PictureOsdObjectBO> getDynamic_pic_osds() {
		return dynamic_pic_osds;
	}

	public DynamicPictureOsdBO setDynamic_pic_osds(List<PictureOsdObjectBO> dynamic_pic_osds) {
		this.dynamic_pic_osds = dynamic_pic_osds;
		return this;
	}

	public DynamicPictureOsdBO() {
	}

	public DynamicPictureOsdBO(ProcessVO processVO) {
		this.dynamic_pic_osds = new ArrayList<>();
		this.plat = processVO.getPlat().name().toLowerCase();
		this.nv_card_idx = processVO.getNv_card_idx();
		for (int i = 0; i < processVO.getPic_osds().size(); i++) {
			PictureOsdVO pictureOsdVO = processVO.getPic_osds().get(i);
			this.dynamic_pic_osds.add(new PictureOsdObjectBO(pictureOsdVO));
		}
	}
}
