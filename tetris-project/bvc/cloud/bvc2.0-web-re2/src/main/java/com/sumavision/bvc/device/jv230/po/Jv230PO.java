package com.sumavision.bvc.device.jv230.po;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: jv230设备信息 <br/>
 * @author ldy
 * @date 2018年8月24日 下午1:16:00 
 */
@Entity
@Table(name = "BVC_USER_RESOURCE_JV230")
public class Jv230PO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 物理屏序号 */
	private int serialnum;
	
	/** 横坐标 */
	private String x;
	
	/** 纵坐标 */
	private String y;
	
	/** 宽 */
	private String w;
	
	/** 高 */
	private String h;
	
	/** 来自于资源管理的设备id*/
	private String bundleId;
	
	/** 来自于资源管理的设备名称*/
	private String bundleName;
	
	/** 来自于资源管理的接入层id*/
	private String layerId;
	
	/** jv230通道 */
	private Set<Jv230ChannelPO> channels;
	
	/** 关联组合设备 */
	private CombineJv230PO combineJv230;

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

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "BUNDLE_NAME")
	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@OneToMany(mappedBy = "jv230", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<Jv230ChannelPO> getChannels() {
		return channels;
	}

	public void setChannels(Set<Jv230ChannelPO> channels) {
		this.channels = channels;
	}

	@ManyToOne
	@JoinColumn(name = "COMBINE_JV230_ID")
	public CombineJv230PO getCombineJv230() {
		return combineJv230;
	}

	public void setCombineJv230(CombineJv230PO combineJv230) {
		this.combineJv230 = combineJv230;
	}
	
}
