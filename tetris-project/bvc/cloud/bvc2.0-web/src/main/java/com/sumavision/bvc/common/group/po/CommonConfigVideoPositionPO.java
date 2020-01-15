package com.sumavision.bvc.common.group.po;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组配置视频分屏布局 
 * @author lvdeyang
 * @date 2018年8月4日 上午11:02:02 
 */
@Entity
@Table(name="BVC_COMMON_CONFIG_VIDEO_POSITION")
public class CommonConfigVideoPositionPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 屏幕序号 */
	private int serialnum;
	
	/** 横坐标 */
	private String x;
	
	/** 纵坐标 */
	private String y;
	
	/** 宽 */
	private String w;
	
	/** 高 */
	private String h;
	
	/** 画面类型：轮询【|静止】 */
	private PictureType pictureType;
	
	/** TYPE为轮询时 ，记录轮询时间 */
	private String pollingTime;
	
	/** TYPE为轮询时 ，记录轮询状态：轮询中【|暂停】 */
	private PollingStatus pollingStatus;
	
	/** 分屏布局中的源 */
	private List<CommonConfigVideoSrcPO> srcs;
	
	/** 关联配置视频 */
	private CommonConfigVideoPO video;

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

	@Enumerated(value = EnumType.STRING)
	@Column(name = "PICTURE_TYPE")
	public PictureType getPictureType() {
		return pictureType;
	}

	public void setPictureType(PictureType pictureType) {
		this.pictureType = pictureType;
	}

	@Column(name = "POLLING_TIME")
	public String getPollingTime() {
		return pollingTime;
	}

	public void setPollingTime(String pollingTime) {
		this.pollingTime = pollingTime;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "POLLING_STATUS")
	public PollingStatus getPollingStatus() {
		return pollingStatus;
	}

	public void setPollingStatus(PollingStatus pollingStatus) {
		this.pollingStatus = pollingStatus;
	}
	
	@OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommonConfigVideoSrcPO> getSrcs() {
		return srcs;
	}

	public void setSrcs(List<CommonConfigVideoSrcPO> srcs) {
		this.srcs = srcs;
	}

	@ManyToOne
	@JoinColumn(name = "VIDEO_ID")
	public CommonConfigVideoPO getVideo() {
		return video;
	}

	public void setVideo(CommonConfigVideoPO video) {
		this.video = video;
	}
	
}
