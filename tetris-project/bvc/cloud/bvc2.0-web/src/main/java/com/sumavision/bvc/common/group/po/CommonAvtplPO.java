package com.sumavision.bvc.common.group.po;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 会议关联参数模板 （区别系统级参数模板）
 * @author zy
 * @date 2018年7月31日 上午10:10:38 
 */
@Entity
@Table(name="BVC_COMMON_AVTPL")
public class CommonAvtplPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/**关联系统参数模板id*/
	private Long avTplId;
	
	/** 参数模板名称 */
	private String name;
	
	/** 视频编码格式 */
	private VideoFormat videoFormat;
	
	/** 备用视频编码格式 */
	private VideoFormat videoFormatSpare;
	
	/** 音频编码格式 */
	private AudioFormat audioFormat;

	/** 模板三挡参数 */
	private Set<CommonAvtplGearsPO> gears;
	
	/** 关联设备组 */
	private CommonGroupPO group;
	
	@Column(name = "avtpl_id")
	public Long getAvTplId() {
		return avTplId;
	}

	public void setAvTplId(Long avTplId) {
		this.avTplId = avTplId;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_FORMAT")
	public VideoFormat getVideoFormat() {
		return videoFormat;
	}

	public void setVideoFormat(VideoFormat videoFormat) {
		this.videoFormat = videoFormat;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "VIDEO_FORMAT_SPARE")
	public VideoFormat getVideoFormatSpare() {
		return videoFormatSpare;
	}

	public void setVideoFormatSpare(VideoFormat videoFormatSpare) {
		this.videoFormatSpare = videoFormatSpare;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "AUDIO_FORMAT")
	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(AudioFormat audioFormat) {
		this.audioFormat = audioFormat;
	}

	@OneToMany(mappedBy = "avtpl", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonAvtplGearsPO> getGears() {
		return gears;
	}

	public void setGears(Set<CommonAvtplGearsPO> gears) {
		this.gears = gears;
	}

	@OneToOne
	@JoinColumn(name = "GROUP_ID")
	public CommonGroupPO getGroup() {
		return group;
	}

	public void setGroup(CommonGroupPO group) {
		this.group = group;
	}
	
	/**
	 * @Title: 从系统资源中复制数据 
	 * @param entity 系统资源
	 * @return AvtplPO 设备组资源
	 */
	public CommonAvtplPO set(com.sumavision.bvc.system.po.AvtplPO entity){
		this.setAvTplId(entity.getId());
		this.setUuid(entity.getUuid());
		this.setUpdateTime(entity.getUpdateTime());
		this.setName(entity.getName());
		this.setVideoFormat(entity.getVideoFormat());
		this.setVideoFormatSpare(entity.getVideoFormatSpare());
		this.setAudioFormat(entity.getAudioFormat());
		return this;
	}
	
}
