package com.sumavision.tetris.bvc.business.po.combine.video;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="BVC_BUSINESS_GROUP_COMBINE_VIDEO")
public class BusinessCombineVideoPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 前端布局布局json，存在这里主要用于布局对比 */
	private String websiteDraw;
	
	/** 合屏信息唯一标识，用于判重，规则：待完善 */
	private String combineVideoUid;
	
	/** 关联分屏布局 */
	private List<BusinessCombineVideoPositionPO> positions;
	
	/** 关联合屏模板（注意解绑） */
	private Set<CombineTemplateGroupAgendeForwardPermissionPO> combineTemplates;
	
	@Column(name = "WEBSITEDRAW", length = 1024)
	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public void setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
	}

	@Column(name="COMBINE_VIDEO_UID", columnDefinition = "longtext")
	public String getCombineVideoUid() {
		return combineVideoUid;
	}

	public void setCombineVideoUid(String combineVideoUid) {
		this.combineVideoUid = combineVideoUid;
	}

	@OneToMany(mappedBy = "combineVideo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessCombineVideoPositionPO> getPositions() {
		return positions;
	}

	public void setPositions(List<BusinessCombineVideoPositionPO> positions) {
		this.positions = positions;
	}
	
	@OneToMany(mappedBy = "combineVideo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CombineTemplateGroupAgendeForwardPermissionPO> getCombineTemplates() {
		return combineTemplates;
	}

	public void setCombineTemplates(Set<CombineTemplateGroupAgendeForwardPermissionPO> combineTemplates) {
		this.combineTemplates = combineTemplates;
	}
	
	public BusinessCombineVideoPO(){
		this.setUpdateTime(new Date());
	}
	
	/**
	 * @Title: 从配置视频中复制数据 
	 * @param video 配置视频
	 * @return DeviceGroupPO
	 */
	/*public BusinessCombineVideoPO set(DeviceGroupConfigVideoPO video){
		this.setUuid(video.getUuid());
		this.setUpdateTime(new Date());
		this.setWebsiteDraw(video.getWebsiteDraw());
		this.setPositions(new HashSet<BusinessCombineVideoPositionPO>());
		Set<DeviceGroupConfigVideoPositionPO> configPositions = video.getPositions();
		for(DeviceGroupConfigVideoPositionPO configPosition:configPositions){
			BusinessCombineVideoPositionPO position = new BusinessCombineVideoPositionPO().set(configPosition);
			position.setCombineVideo(this);
			this.getPositions().add(position);
		}
		return this;
	}*/

	/**
	 * @Title: 判断合屏是否是有效的<br/> 
	 * @return boolean 
	 */
	@Transient
	public boolean isEffective(){
		boolean hasSrc = false;
		List<BusinessCombineVideoPositionPO> positions = this.getPositions();
		if(positions != null){
			for(BusinessCombineVideoPositionPO position:positions){
				List<BusinessCombineVideoSrcPO> srcs = position.getSrcs();
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
	public static final class DateComparatorFromPO implements Comparator<BusinessCombineVideoPO>{
		@Override
		public int compare(BusinessCombineVideoPO o1, BusinessCombineVideoPO o2) {
			
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
