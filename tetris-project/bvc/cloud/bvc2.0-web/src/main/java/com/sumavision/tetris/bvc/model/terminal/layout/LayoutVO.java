package com.sumavision.tetris.bvc.model.terminal.layout;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class LayoutVO extends AbstractBaseVO<LayoutVO, LayoutPO>{

	/** 名称 ,内置屏幕：单屏、1左2右、1大2小、1小2大、四屏、六屏、九屏、十六屏*/
	private String name;
	
	/** web端布局 */
	private String websiteDraw;
	
	public String getName() {
		return name;
	}

	public LayoutVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public LayoutVO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}

	@Override
	public LayoutVO set(LayoutPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setWebsiteDraw(entity.getWebsiteDraw());
		return this;
	}

}
