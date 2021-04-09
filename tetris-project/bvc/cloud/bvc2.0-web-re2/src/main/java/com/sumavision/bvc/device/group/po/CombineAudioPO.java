package com.sumavision.bvc.device.group.po;

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
 * @ClassName: 描述会议中的混音
 * @author zy
 * @date 2018年7月31日 下午2:20:14 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_COMBINE_AUDIO")
public class CombineAudioPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 音量 */
	private int volume;
	
	/** 关联设备组 */
	private DeviceGroupPO group;
	
	/** 关联混音源通道 */
	private Set<CombineAudioSrcPO> srcs;

	@Column(name = "VOLUME")
	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	public DeviceGroupPO getGroup() {
		return group;
	}

	public void setGroup(DeviceGroupPO group) {
		this.group = group;
	}

	@OneToMany(mappedBy = "combineAudio", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CombineAudioSrcPO> getSrcs() {
		return srcs;
	}

	public void setSrcs(Set<CombineAudioSrcPO> srcs) {
		this.srcs = srcs;
	}
	
}
