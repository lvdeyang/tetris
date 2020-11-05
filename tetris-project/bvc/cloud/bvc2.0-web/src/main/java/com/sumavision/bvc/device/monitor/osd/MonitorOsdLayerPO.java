package com.sumavision.bvc.device.monitor.osd;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.bvc.device.monitor.subtitle.MonitorSubtitleFont;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 屏幕显示图层<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月6日 上午8:44:48
 */
@Entity
@Table(name = "BVC_MONITOR_OSD_LAYER")
public class MonitorOsdLayerPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	/** 位置坐标百分比 */
	private Integer x;
	
	/** 位置坐标百分比 */
	private Integer y;
	
	/** 字幕的id值，用于添加和删除字幕的标识，可以标识图层或者行号 0~7*/
	private Integer layerIndex;
	
	/** 取值为0和1,1为显示；0为不显示；当为0时，代表删除一个字幕 */
	private Integer isShow = 1;
	
	/** 图层内容类型 */
	private MonitorOsdLayerType type;
	
	/** 字体默认是黑体 */
	private MonitorSubtitleFont font;
	
	/** 字体高度，单位：像素，取值0-100 */
	private Integer height = 20;
	
	/** 16进制颜色 没有# */
	private String color = "ffffff";

	/** 图层内容对应的资源id */
	private Long contentId;
	
	/** 图层内容对应的资源别名 */
	private String contentName;
	
	/** 图层内容对应的资源的创建用户 */
	private String contentUsername;
	
	/** 屏幕显示 */
	private MonitorOsdPO osd;
	
	@Column(name = "X")
	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	@Column(name = "Y")
	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	@Column(name = "LAYER_INDEX")
	public Integer getLayerIndex() {
		return layerIndex;
	}

	public void setLayerIndex(Integer layerIndex) {
		this.layerIndex = layerIndex;
	}

	@Column(name = "IS_SHOW")
	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public MonitorOsdLayerType getType() {
		return type;
	}

	public void setType(MonitorOsdLayerType type) {
		this.type = type;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "FONT")
	public MonitorSubtitleFont getFont() {
		return font;
	}

	public void setFont(MonitorSubtitleFont font) {
		this.font = font;
	}

	@Column(name = "HEIGHT")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Column(name = "COLOR")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "CONTENT_ID")
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Column(name = "CONTENT_NAME")
	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	@Column(name = "CONTENT_USERNAME")
	public String getContentUsername() {
		return contentUsername;
	}

	public void setContentUsername(String contentUsername) {
		this.contentUsername = contentUsername;
	}

	@ManyToOne
	@JoinColumn(name = "MONITOR_OSD_ID")
	public MonitorOsdPO getOsd() {
		return osd;
	}

	public void setOsd(MonitorOsdPO osd) {
		this.osd = osd;
	}

	@Override
	public int hashCode() {
		String uuid = this.getUuid();
		final int prime = 31;
		int result = 0;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		String uuid = this.getUuid();
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		MonitorOsdLayerPO other = (MonitorOsdLayerPO) obj;
		if (uuid == null) {
			if (other.getUuid() != null)
				return false;
		} else if (!uuid.equals(other.getUuid()))
			return false;
		return true;
	}
	
	public static class MonitorOsdLayerComparator implements Comparator<MonitorOsdLayerPO>{

		@Override
		public int compare(MonitorOsdLayerPO o1, MonitorOsdLayerPO o2) {
			return o1.getLayerIndex().intValue() - o2.getLayerIndex().intValue();
		}
		
	}
	
}
