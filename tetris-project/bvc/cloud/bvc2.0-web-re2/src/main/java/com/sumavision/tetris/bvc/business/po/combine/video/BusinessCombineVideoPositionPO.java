package com.sumavision.tetris.bvc.business.po.combine.video;

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
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_BUSINESS_GROUP_COMBINE_VIDEO_POSITION")
public class BusinessCombineVideoPositionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 屏幕序号（可能没用，暂时与layoutPositionSerialNum相同赋值） */
	private int serialnum;
	
	/** 画面布局的序号，与layoutPosition对应，可用于排序，CombineTemplatePositionPO.getLayoutPositionSerialNum */
	private Integer layoutPositionSerialNum;
	
	/** 左偏移，万分比的分子 */
	private String x;
	
	/** 上偏移 */
	private String y;
	
	/** 宽 */
	private String w;
	
	/** 高 */
	private String h;
	
	/** 布局涂层 */
	private String zIndex;
	
	/** 画面类型：轮询【|静止】 */
	private PictureType pictureType;
	
	/** TYPE为轮询时 ，记录轮询时间 */
	private String pollingTime;
	
	/** TYPE为轮询时 ，记录轮询状态：轮询中【|暂停】 */
	private PollingStatus pollingStatus = PollingStatus.RUN;

	/** 关联合屏数据 */
	private BusinessCombineVideoPO combineVideo;
	
	/** 关联分屏中的源 */
	private List<BusinessCombineVideoSrcPO> srcs;

	@Column(name = "SERIALNUM")
	public int getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(int serialnum) {
		this.serialnum = serialnum;
	}

	@Column(name = "LAYOUT_POSITION_SERIALNUM")
	public Integer getLayoutPositionSerialNum() {
		return layoutPositionSerialNum;
	}

	public void setLayoutPositionSerialNum(Integer layoutPositionSerialNum) {
		this.layoutPositionSerialNum = layoutPositionSerialNum;
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
	
	@Column(name = "Z_INDEX")
	public String getzIndex() {
		return zIndex;
	}
	
	public void setzIndex(String zIndex) {
		this.zIndex = zIndex;
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
	public BusinessCombineVideoPO getCombineVideo() {
		return combineVideo;
	}

	public void setCombineVideo(BusinessCombineVideoPO combineVideo) {
		this.combineVideo = combineVideo;
	}
	
	@OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessCombineVideoSrcPO> getSrcs() {
		return srcs;
	}

	public void setSrcs(List<BusinessCombineVideoSrcPO> srcs) {
		this.srcs = srcs;
	}
	
	/**
	 * @Title: 从配置中复制数据<br/> 
	 * @param tplPosition 配置数据
	 * @return CombineVideoPO 合屏
	 */
	/*public BusinessCombineVideoPositionPO set(DeviceGroupConfigVideoPositionPO configPosition){
		this.setSerialnum(configPosition.getSerialnum());
		this.setX(configPosition.getX());
		this.setY(configPosition.getY());
		this.setW(configPosition.getW());
		this.setH(configPosition.getH());
		this.setPictureType(configPosition.getPictureType());
		this.setPollingTime(configPosition.getPollingTime());
		this.setPollingStatus(configPosition.getPollingStatus());
		this.setSrcs(new ArrayList<BusinessCombineVideoSrcPO>());
		List<DeviceGroupConfigVideoSrcPO> configSrcs = configPosition.getSrcs();
		if(configSrcs!=null && configSrcs.size()>0){
			for(DeviceGroupConfigVideoSrcPO configSrc:configSrcs){
				BusinessCombineVideoSrcPO src = new BusinessCombineVideoSrcPO().set(configSrc);
				src.setPosition(this);
				this.getSrcs().add(src);
			}
		}
		return this;
	}*/
	public BusinessCombineVideoPositionPO set(
			CombineTemplatePositionPO tplPosition,
			List<AgendaForwardSourcePO> sources
//			List<BusinessGroupMemberTerminalChannelPO> srcChannels
			){
		/*this.setSerialnum(tplPosition.getLayoutPositionSerialNum());
		this.setX(tplPosition.getX());
		this.setY(tplPosition.getY());
		this.setW(tplPosition.getWidth());
		this.setH(tplPosition.getHeight());
		if(srcChannels ==null || srcChannels.size()<=1){
			this.setPictureType(PictureType.STATIC);
		}else{
			this.setPictureType(PictureType.POLLING);
		}
		this.setPollingTime(tplPosition.getPollingTime());
		this.setPollingStatus(tplPosition.getPollingStatus());
		this.setSrcs(new ArrayList<BusinessCombineVideoSrcPO>());
		List<DeviceGroupConfigVideoSrcPO> configSrcs = tplPosition.getSrcs();
		if(configSrcs!=null && configSrcs.size()>0){
			for(DeviceGroupConfigVideoSrcPO configSrc:configSrcs){
				BusinessCombineVideoSrcPO src = new BusinessCombineVideoSrcPO().set(configSrc);
				src.setPosition(this);
				this.getSrcs().add(src);
			}
		}*/
		return this;
	}
	/*public CombineVideoPositionPO set(
			com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPositionPO configPosition){
		this.setSerialnum(configPosition.getSerialnum());
		this.setX(configPosition.getX());
		this.setY(configPosition.getY());
		this.setW(configPosition.getW());
		this.setH(configPosition.getH());
		this.setPollingTime(configPosition.getPollingTime());
		try {
			this.setPictureType(PictureType.fromName(configPosition.getPictureType().getName()));
			if(configPosition.getPollingStatus() != null){
				this.setPollingStatus(PollingStatus.fromName(configPosition.getPollingStatus().getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setSrcs(new ArrayList<CombineVideoSrcPO>());
		
		CombineVideoSrcDAO combineVideoSrcDao = SpringContext.getBean(CombineVideoSrcDAO.class);
		List<com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcPO> configSrcs = combineVideoSrcDao.findByCombineVideoPositionIdIn(new ArrayListWrapper<Long>().add(configPosition.getId()).getList());
		if(configSrcs!=null && configSrcs.size()>0){
			for(com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcPO configSrc:configSrcs){
				CombineVideoSrcPO src = new CombineVideoSrcPO().set(configSrc);
				src.setPosition(this);
				this.getSrcs().add(src);
			}
		}
		return this;
	}*/
	
}
