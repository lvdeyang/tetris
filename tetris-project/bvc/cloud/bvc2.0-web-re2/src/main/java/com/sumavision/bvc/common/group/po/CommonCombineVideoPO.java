package com.sumavision.bvc.common.group.po;

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

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 合屏 
 * @author zy
 * @date 2018年7月31日 下午2:26:08 
 */
@Entity
@Table(name="BVC_COMMON_COMBINE_VIDEO")
public class CommonCombineVideoPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 前端布局布局json，存在这里主要用于布局对比 */
	private String websiteDraw;
	
	/** 关联分屏布局 */
	private Set<CommonCombineVideoPositionPO> positions;
	
	/** 关联设备组 */
	private CommonGroupPO group;
	
	@Column(name = "WEBSITEDRAW", length = 1024)
	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public void setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
	}

	@OneToMany(mappedBy = "combineVideo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonCombineVideoPositionPO> getPositions() {
		return positions;
	}

	public void setPositions(Set<CommonCombineVideoPositionPO> positions) {
		this.positions = positions;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public CommonGroupPO getGroup() {
		return group;
	}

	public void setGroup(CommonGroupPO group) {
		this.group = group;
	}
	
	/**
	 * @Title: 从配置视频中复制数据 
	 * @param video 配置视频
	 * @return DeviceGroupPO
	 */
	public CommonCombineVideoPO set(CommonConfigVideoPO video){
		this.setUuid(video.getUuid());
		this.setUpdateTime(new Date());
		this.setWebsiteDraw(video.getWebsiteDraw());
		this.setPositions(new HashSet<CommonCombineVideoPositionPO>());
		Set<CommonConfigVideoPositionPO> configPositions = video.getPositions();
		for(CommonConfigVideoPositionPO configPosition:configPositions){
			CommonCombineVideoPositionPO position = new CommonCombineVideoPositionPO().set(configPosition);
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
		Set<CommonCombineVideoPositionPO> positions = this.getPositions();
		if(positions != null){
			for(CommonCombineVideoPositionPO position:positions){
				List<CommonCombineVideoSrcPO> srcs = position.getSrcs();
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
	public static final class DateComparatorFromPO implements Comparator<CommonCombineVideoPO>{
		@Override
		public int compare(CommonCombineVideoPO o1, CommonCombineVideoPO o2) {
			
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
