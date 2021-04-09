package com.sumavision.tetris.bvc.business.po.combine.audio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
import com.sumavision.tetris.bvc.business.po.combine.audio.CombineAudioPermissionPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 描述一个实际的音频，可能是单音频或混音。<br/>
 * 混音需要去掉目标成员的通道，所以业务中1个音频配置可能产生多个BusinessCombineAudioPO，且不去掉任何成员的“全部的”混音allAudio，会与那些去掉了某个成员的混音memberAudios互相关联<br/>
 * 业务的处理中要为每个成员创建一个成员混音，即使该混音本质是单音频，或者没有源
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月17日 上午10:37:27
 */
@Entity
@Table(name="BVC_BUSINESS_GROUP_COMBINE_AUDIO")
public class BusinessCombineAudioPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 是否要混音（实际可用的源多于1个） */
	private boolean mix = false;
	
	/** 是否有源（实际可用的源为0，不需要转发，更不用混音） */
	private boolean hasSource = true;
	
	/** 源 */	
	private List<BusinessCombineAudioSrcPO> srcs;
	
	/** 是否是全部声音，即：没有去掉任何成员的声音 */
	private boolean isAll = true;
	
	//---------isAll==true时以下字段有效 */
	/** 音频信息唯一标识，用于判重，例如：
	 * CustomAudioPO中属性的分隔符： "-"
	 * CustomAudioPO属性集合的分隔符："_"
	 * 最终效果为："groupid_(sourceId-sourceType)_(...)_(...)" */
	/** 如果是自动配置的全局混音，标识为auto_groupid_(sourceId-sourceType)_(...)_(...)*/
	private String audioUid;
	
	/** 关联给各成员的不同混音 */
	private List<BusinessCombineAudioPO> memberAudios;
	
	/** 关联议程或议程转发（注意解绑）。目前按照1:1来做，预留1:n的扩展性 */
	private Set<CombineAudioPermissionPO> combineAudioPermissions;
	
	//---------isAll==false时以下字段有效
	
	/** 目标成员id，需要去掉该成员的通道 */
	private Long dstMemberId;
	
	/** 关联“全部声音” */
	private BusinessCombineAudioPO allAudio;
	
	//---------isAll 区分结束

	@Column(name="AUDIO_UID", columnDefinition = "longtext")
	public String getAudioUid() {
		return audioUid;
	}

	public void setAudioUid(String audioUid) {
		this.audioUid = audioUid;
	}

	public boolean getIsAll() {
		return isAll;
	}

	public void setIsAll(boolean isAll) {
		this.isAll = isAll;
	}

	public boolean isMix() {
		return mix;
	}

	public void setMix(boolean mix) {
		this.mix = mix;
	}

	public boolean isHasSource() {
		return hasSource;
	}

	public void setHasSource(boolean hasSource) {
		this.hasSource = hasSource;
	}

	public Long getDstMemberId() {
		return dstMemberId;
	}

	public void setDstMemberId(Long dstMemberId) {
		this.dstMemberId = dstMemberId;
	}

	@OneToMany(mappedBy = "combineAudio", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessCombineAudioSrcPO> getSrcs() {
		return srcs;
	}

	public void setSrcs(List<BusinessCombineAudioSrcPO> srcs) {
		this.srcs = srcs;
	}

	@ManyToOne
	@JoinColumn(name = "ALL_AUDIO_ID")
	public BusinessCombineAudioPO getAllAudio() {
		return allAudio;
	}

	public void setAllAudio(BusinessCombineAudioPO allAudio) {
		this.allAudio = allAudio;
	}

	@OneToMany(mappedBy = "allAudio", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CombineAudioPermissionPO> getCombineAudioPermissions() {
		return combineAudioPermissions;
	}

	public void setCombineAudioPermissions(Set<CombineAudioPermissionPO> combineAudioPermissions) {
		this.combineAudioPermissions = combineAudioPermissions;
	}

	@OneToMany(mappedBy = "allAudio", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessCombineAudioPO> getMemberAudios() {
		return memberAudios;
	}

	public void setMemberAudios(List<BusinessCombineAudioPO> memberAudios) {
		this.memberAudios = memberAudios;
	}

//	@OneToMany(mappedBy = "combineAudio", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//	public Set<CombineAudioAgendaForwardPermissionPO> getCombineAudioAgendeForwardPermissions() {
//		return combineAudioAgendeForwardPermissions;
//	}
//
//	public void setCombineAudioAgendeForwardPermissions(Set<CombineAudioAgendaForwardPermissionPO> combineAudioAgendeForwardPermissions) {
//		this.combineAudioAgendeForwardPermissions = combineAudioAgendeForwardPermissions;
//	}
//	
//	@Enumerated(value = EnumType.STRING)
//	public AudioType getAudioType() {
//		return audioType;
//	}
//
//	public void setAudioType(AudioType audioType) {
//		this.audioType = audioType;
//	}

	public BusinessCombineAudioPO(){
		this.setUpdateTime(new Date());
	}

	
	//-----------------------数据
//	/** 是否要混音（实际可用的源多于1个） */
//	private boolean mix = false;
//	
//	/** 是否有源（实际可用的源为0，不需要转发，更不用混音） */
//	private boolean hasSource = true;
//	
//	/** 源 */	
//	private List<BusinessCombineAudioSrcPO> srcs;
//	
//	/** 是否是全部声音，即：没有去掉任何成员的声音 */
//	private boolean isAll = true;
//	
//	//---------isAll==true时以下字段有效 */
//	/** 音频信息唯一标识，用于判重，例如：
//	 * CustomAudioPO中属性的分隔符： "-"
//	 * CustomAudioPO属性集合的分隔符："_"
//	 * 最终效果为："groupid_(sourceId-sourceType)_(...)_(...)" */
//	/** 如果是自动配置的全局混音，标识为auto_groupid_(sourceId-sourceType)_(...)_(...)*/
//	private String audioUid;
//	
//	/** 关联给各成员的不同混音 */
//	private List<BusinessCombineAudioPO> memberAudios;
//	
//	/** 关联议程或议程转发（注意解绑）。目前按照1:1来做，预留1:n的扩展性 */
//	private Set<CombineAudioPermissionPO> combineAudioPermissions;
//	
//	//---------isAll==false时以下字段有效
//	
//	/** 目标成员id，需要去掉该成员的通道 */
//	private Long dstMemberId;
//	
//	/** 关联“全部声音” */
//	private BusinessCombineAudioPO allAudio;
	//--------------------数据
	/**
	 * 复制成员的混音<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月15日 下午4:06:58
	 * @param src
	 * @return
	 */
	public BusinessCombineAudioPO set(BusinessCombineAudioPO combineAudio){
		//根据议程转发
		this.setMix(combineAudio.isMix());
		if(this.getSrcs() == null){
			this.setSrcs(combineAudio.getSrcs());
		}else {
			this.getSrcs().clear();
			this.getSrcs().addAll(combineAudio.getSrcs() == null ? new ArrayList<BusinessCombineAudioSrcPO>() : combineAudio.getSrcs());
		}
		this.setHasSource(combineAudio.isHasSource());
		this.setDstMemberId(combineAudio.getDstMemberId());
		this.setAllAudio(combineAudio.getAllAudio());
		return this;
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
	/*@Transient
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
	}*/

	/**
	 * @ClassName: 时间排序器， 按照时间从大到小排列<br/> 
	 * @author wjw
	 * @date 2018年12月25日 下午15:36:10 
	 */
	public static final class DateComparatorFromPO implements Comparator<BusinessCombineAudioPO>{
		@Override
		public int compare(BusinessCombineAudioPO o1, BusinessCombineAudioPO o2) {
			
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
