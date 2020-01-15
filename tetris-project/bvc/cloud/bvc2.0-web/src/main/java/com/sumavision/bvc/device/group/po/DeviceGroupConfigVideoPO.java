package com.sumavision.bvc.device.group.po;

import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.enumeration.VideoOperationType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组配置视频 
 * @author lvdeyang
 * @date 2018年8月4日 上午11:00:48 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_CONFIG_VIDEO")
public class DeviceGroupConfigVideoPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 屏幕名称 */
	private String name;
	
	/** 视频操作方式 */
	private VideoOperationType videoOperation;
	
	/** 前端生成布局的json字符串格式：{basic:{column:4, row:4}, cellspan:[{x,y,r,b}]} */
	private String websiteDraw;
	
	/** 标识视频是否录制 */
	private Boolean record;
	
	/** 视频中的屏幕布局 */
	private Set<DeviceGroupConfigVideoPositionPO> positions;
	
	/** 小屏源 */
	private DeviceGroupConfigVideoSmallSrcPO small;
	
	/** 视频中的转发目的 */
	private Set<DeviceGroupConfigVideoDstPO> dsts;
	
	/** 关联设备组配置 */
	private DeviceGroupConfigPO config;
	
	/** 屏幕布局 */
	private ScreenLayout layout;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_OPERATION")
	public VideoOperationType getVideoOperation() {
		return videoOperation;
	}

	public void setVideoOperation(VideoOperationType videoOperation) {
		this.videoOperation = videoOperation;
	}

	@Column(name = "WEBSITEDRAW", length = 1024)
	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public void setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
	}
	
	@Column(name = "RECORD")
	public Boolean isRecord() {
		return record;
	}

	public void setRecord(Boolean record) {
		this.record = record;
	}

	@OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<DeviceGroupConfigVideoPositionPO> getPositions() {
		return positions;
	}

	public void setPositions(Set<DeviceGroupConfigVideoPositionPO> positions) {
		this.positions = positions;
	}

	@OneToOne(mappedBy = "video", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public DeviceGroupConfigVideoSmallSrcPO getSmall() {
		return small;
	}

	public void setSmall(DeviceGroupConfigVideoSmallSrcPO small) {
		this.small = small;
	}

	@OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<DeviceGroupConfigVideoDstPO> getDsts() {
		return dsts;
	}

	public void setDsts(Set<DeviceGroupConfigVideoDstPO> dsts) {
		this.dsts = dsts;
	}

	@ManyToOne
	@JoinColumn(name = "CONFIG_ID")
	public DeviceGroupConfigPO getConfig() {
		return config;
	}

	public void setConfig(DeviceGroupConfigPO config) {
		this.config = config;
	} 
	
	@Column(name = "LAYOUT")
	@Enumerated(value = EnumType.STRING)
	public ScreenLayout getLayout() {
		return layout;
	}

	public void setLayout(ScreenLayout layout) {
		this.layout = layout;
	}

	/**
	 * @Title: 判断一个配置视频是否配置了源<br/> 
	 * @return boolean 
	 */
	public boolean hasSrc(){
		Set<DeviceGroupConfigVideoPositionPO> positions = this.getPositions();
		if(positions != null){
			for(DeviceGroupConfigVideoPositionPO position:positions){
				List<DeviceGroupConfigVideoSrcPO> srcs = position.getSrcs();
				if(srcs!=null && srcs.size()>0){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @Title: 判断一个配置视频的小屏是否配置了源<br/> 
	 * @return boolean 
	 */
	public boolean hasSmallSrc(){
		DeviceGroupConfigVideoSmallSrcPO src = this.getSmall();
		if(src != null){
			return true;
		}
		return false;
	}
	
}
