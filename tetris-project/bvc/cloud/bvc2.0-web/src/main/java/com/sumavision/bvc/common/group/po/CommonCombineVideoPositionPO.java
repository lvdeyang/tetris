package com.sumavision.bvc.common.group.po;

import java.util.ArrayList;
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
 * @ClassName: 设备组中合屏屏幕布局 
 * @author zy
 * @date 2018年7月31日 下午2:28:02 
 */
@Entity
@Table(name="BVC_COMMON_COMBINE_VIDEO_POSITION")
public class CommonCombineVideoPositionPO extends AbstractBasePO{

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
	
	/** 画面类型：轮询【|静止】 */
	private PictureType pictureType;
	
	/** TYPE为轮询时 ，记录轮询时间 */
	private String pollingTime;
	
	/** TYPE为轮询时 ，记录轮询状态：轮询中【|暂停】 */
	private PollingStatus pollingStatus;

	/** 关联合屏数据 */
	private CommonCombineVideoPO combineVideo;
	
	/** 关联分屏中的源 */
	private List<CommonCombineVideoSrcPO> srcs;

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

	@ManyToOne
	@JoinColumn(name = "COMBINE_VIDEO_ID")
	public CommonCombineVideoPO getCombineVideo() {
		return combineVideo;
	}

	public void setCombineVideo(CommonCombineVideoPO combineVideo) {
		this.combineVideo = combineVideo;
	}
	
	@OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommonCombineVideoSrcPO> getSrcs() {
		return srcs;
	}

	public void setSrcs(List<CommonCombineVideoSrcPO> srcs) {
		this.srcs = srcs;
	}
	
	/**
	 * @Title: 从配置中复制数据<br/> 
	 * @param configPosition 配置数据
	 * @return CombineVideoPO 合屏
	 */
	public CommonCombineVideoPositionPO set(CommonConfigVideoPositionPO configPosition){
		this.setSerialnum(configPosition.getSerialnum());
		this.setX(configPosition.getX());
		this.setY(configPosition.getY());
		this.setW(configPosition.getW());
		this.setH(configPosition.getH());
		this.setPictureType(configPosition.getPictureType());
		this.setPollingTime(configPosition.getPollingTime());
		this.setPollingStatus(configPosition.getPollingStatus());
		this.setSrcs(new ArrayList<CommonCombineVideoSrcPO>());
		List<CommonConfigVideoSrcPO> configSrcs = configPosition.getSrcs();
		if(configSrcs!=null && configSrcs.size()>0){
			for(CommonConfigVideoSrcPO configSrc:configSrcs){
				CommonCombineVideoSrcPO src = new CommonCombineVideoSrcPO().set(configSrc);
				src.setPosition(this);
				this.getSrcs().add(src);
			}
		}
		return this;
	}
	
}
