package com.sumavision.bvc.control.system.vo;

import com.sumavision.bvc.system.dto.TplContentScreenLayoutDTO;
import com.sumavision.bvc.system.po.ScreenLayoutPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * @ClassName: 屏幕布局方案 
 * @author zh
 * @date 2018年7月25日 下午8:50:45
 */
public class ScreenLayoutVO extends AbstractBaseVO<ScreenLayoutVO, ScreenLayoutPO>{
	
	/** 方案名称 */
	private String name;

	/** 网页端布局数据，格式：{basic:{column:4, row:4}, cellspan:[{x:0, y0, r:1, b:1}]} */
	private String websiteDraw;

	public String getName() {
		return name;
	}

	public ScreenLayoutVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public ScreenLayoutVO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}

	@Override
	public ScreenLayoutVO set(ScreenLayoutPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUuid(entity.getUuid())
			.setWebsiteDraw(entity.getWebsiteDraw());
		return this;
	}
	
	public ScreenLayoutVO set(TplContentScreenLayoutDTO entity){
		this.setId(entity.getLayoutId())
			.setName(entity.getLayoutName())
			.setWebsiteDraw(entity.getWebsiteDraw());
		return this;
	}

}
