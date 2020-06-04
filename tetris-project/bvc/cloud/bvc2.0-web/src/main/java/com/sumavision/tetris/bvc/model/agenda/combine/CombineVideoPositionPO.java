package com.sumavision.tetris.bvc.model.agenda.combine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程合屏布局<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:32:26
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_COMBINE_VIDEO_POSITION")
public class CombineVideoPositionPO extends AbstractBasePO{

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
	
	/** 隶属合屏id */
	private Long combineVideoId;

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

	@Column(name = "COMBINE_VIDEO_ID")
	public Long getCombineVideoId() {
		return combineVideoId;
	}

	public void setCombineVideoId(Long combineVideoId) {
		this.combineVideoId = combineVideoId;
	}
	
}
