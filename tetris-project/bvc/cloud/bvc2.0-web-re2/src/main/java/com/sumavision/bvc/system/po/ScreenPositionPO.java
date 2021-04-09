package com.sumavision.bvc.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 屏幕方案的分屏布局 
 * @author lvdeyang 
 * @date 2018年7月24日 下午3:53:42 
 */
@Entity
@Table(name="BVC_SYSTEM_SCREEN_POSITION")
public class ScreenPositionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 屏幕序号 */
	private int serialnum;
	
	/** 左偏移 */
	private String x;
	
	/** 上偏移 */
	private String y;
	
	/** 宽 */
	private String w;
	
	/** 高 */
	private String h;

	private ScreenLayoutPO layout;
	
	@Column(name = "SERIAL_NUM")
	public int getSerialnum() {
		return serialnum;
	}
	
	public void setSerialnum(int serialnum) {
		this.serialnum = serialnum;
	}
	
	@Column(name = "X")
	public String getX() {
		return x;
	}
	
	public void setX(String x) {
		this.x = x;
	}
	
	@Column(name = "Y")
	public String getY() {
		return y;
	}
	
	public void setY(String y) {
		this.y = y;
	}
	
	@Column(name = "W")
	public String getW() {
		return w;
	}
	
	public void setW(String w) {
		this.w = w;
	}
	
	@Column(name = "H")
	public String getH() {
		return h;
	}
	
	public void setH(String h) {
		this.h = h;
	}

	@ManyToOne
	@JoinColumn(name="SCREEN_LAYOUT_ID")
	public ScreenLayoutPO getLayout() {
		return layout;
	}

	public void setLayout(ScreenLayoutPO layout) {
		this.layout = layout;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + serialnum;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		ScreenPositionPO other = (ScreenPositionPO) obj;
		if (serialnum != other.serialnum)
			return false;
		return true;
	}
	
}
