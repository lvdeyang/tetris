package com.sumavision.bvc.device.group.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组关联的屏幕布局下的分屏布局（区别于系统级数据）
 * @author zy
 * @date 2018年7月31日 上午10:44:08 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_SCREEN_POSITION")
public class DeviceGroupScreenPositionPO extends AbstractBasePO{
	
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
	
	private DeviceGroupScreenLayoutPO layout;

	
	@Column(name = "SERIALNUM")
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
	@JoinColumn(name="LAYOUT_ID")
	public DeviceGroupScreenLayoutPO getLayout() {
		return layout;
	}

	public void setLayout(DeviceGroupScreenLayoutPO layout) {
		this.layout = layout;
	}
	
	/**
	 * @Title: 从系统资源复制数据 
	 * @param entity 系统资源
	 * @return ScreenPositionPO 设备组资源 
	 */
	public DeviceGroupScreenPositionPO set(com.sumavision.bvc.system.po.ScreenPositionPO entity){
		this.setSerialnum(entity.getSerialnum());
		this.setX(entity.getX());
		this.setY(entity.getY());
		this.setW(entity.getW());
		this.setH(entity.getH());
		return this;
	}
	
}
