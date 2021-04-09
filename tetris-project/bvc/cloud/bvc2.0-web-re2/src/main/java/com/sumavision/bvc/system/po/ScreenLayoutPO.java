package com.sumavision.bvc.system.po;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 屏幕布局方案 
 * @author lvdeyang
 * @date 2018年7月24日 下午3:50:01 
 */
@Entity
@Table(name="BVC_SYSTEM_SCREEN_LAYOUT")
public class ScreenLayoutPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 方案名称 */
	private String name;

	/** 网页端布局数据，格式：{basic:{column:4, row:4}, cellspan:[{x:0, y0, r:1, b:1}]} */
	private String websiteDraw;
	
	private Set<ScreenPositionPO> positions;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "WEBSITE_DRAW")
	public String getWebsiteDraw() {
		return websiteDraw;
	}
	
	public void setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
	}

	@OneToMany(mappedBy = "layout", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<ScreenPositionPO> getPositions() {
		return positions;
	}

	public void setPositions(Set<ScreenPositionPO> positions) {
		this.positions = positions;
	}
	
}
