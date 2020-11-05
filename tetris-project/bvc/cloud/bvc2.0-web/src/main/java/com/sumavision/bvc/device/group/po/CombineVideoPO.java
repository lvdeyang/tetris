package com.sumavision.bvc.device.group.po;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPositionDAO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcDAO;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 合屏 
 * @author zy
 * @date 2018年7月31日 下午2:26:08 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_COMBINE_VIDEO")
public class CombineVideoPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 前端布局布局json，存在这里主要用于布局对比 */
	private String websiteDraw;
	
	/** 关联分屏布局 */
	private Set<CombineVideoPositionPO> positions;
	
	/** 关联设备组 */
	private DeviceGroupPO group;
	
	/** 关联重构后的group */
	private Long reconGroupId;
	
	@Column(name = "WEBSITEDRAW", length = 1024)
	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public void setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
	}

	@OneToMany(mappedBy = "combineVideo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CombineVideoPositionPO> getPositions() {
		return positions;
	}

	public void setPositions(Set<CombineVideoPositionPO> positions) {
		this.positions = positions;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public DeviceGroupPO getGroup() {
		return group;
	}

	public void setGroup(DeviceGroupPO group) {
		this.group = group;
	}
	
	@Column(name = "RECON_GROUP_ID")
	public Long getReconGroupId() {
		return reconGroupId;
	}

	public void setReconGroupId(Long reconGroupId) {
		this.reconGroupId = reconGroupId;
	}
	
	/**
	 * @Title: 从配置视频中复制数据 
	 * @param video 配置视频
	 * @return DeviceGroupPO
	 */
	public CombineVideoPO set(DeviceGroupConfigVideoPO video){
		this.setUuid(video.getUuid());
		this.setUpdateTime(new Date());
		this.setWebsiteDraw(video.getWebsiteDraw());
		this.setPositions(new HashSet<CombineVideoPositionPO>());
		Set<DeviceGroupConfigVideoPositionPO> configPositions = video.getPositions();
		for(DeviceGroupConfigVideoPositionPO configPosition:configPositions){
			CombineVideoPositionPO position = new CombineVideoPositionPO().set(configPosition);
			position.setCombineVideo(this);
			this.getPositions().add(position);
		}
		return this;
	}
	public CombineVideoPO set(
			com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPO video){
		this.setUuid(video.getUuid());
		this.setUpdateTime(new Date());
		this.setWebsiteDraw(video.getWebsiteDraw());
		this.setPositions(new HashSet<CombineVideoPositionPO>());
		
		CombineVideoPositionDAO combineVideoPositionDao = SpringContext.getBean(CombineVideoPositionDAO.class);
		List<com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPositionPO> positions = combineVideoPositionDao.findByCombineVideoId(video.getId());
		for(com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoPositionPO configPosition:positions){
			CombineVideoPositionPO position = new CombineVideoPositionPO().set(configPosition);
			position.setCombineVideo(this);
			this.getPositions().add(position);
		}
		return this;
	}
	
	/**
	 * @Title: 判断合屏是否是有效的<br/> 
	 * @return boolean 
	 */
	@Transient
	public boolean isEffective(){
		boolean hasSrc = false;
		Set<CombineVideoPositionPO> positions = this.getPositions();
		if(positions != null){
			for(CombineVideoPositionPO position:positions){
				List<CombineVideoSrcPO> srcs = position.getSrcs();
				if(srcs!=null && srcs.size()>0){
					hasSrc = true;
					break;
				}
			}
		}
		if(!hasSrc) return false;
		
		if(this.getPositions().size()==1 && this.getPositions().iterator().next().getSrcs().size()==1) return false;
		
		return true;
	}

	/**
	 * @ClassName: 时间排序器， 按照时间从大到小排列<br/> 
	 * @author wjw
	 * @date 2018年12月25日 下午15:36:10 
	 */
	public static final class DateComparatorFromPO implements Comparator<CombineVideoPO>{
		@Override
		public int compare(CombineVideoPO o1, CombineVideoPO o2) {
			
			Date date1 = o1.getUpdateTime();
			Date date2 = o2.getUpdateTime();
			
			if(date1.after(date2)){
				return 1;
			}
			if(date1 == date2){
				return 0;
			}
			return -1;
		}
	}
	
}
