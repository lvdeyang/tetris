package com.sumavision.bvc.command.emergent.broadcast;

import java.util.Date;
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

import com.sumavision.bvc.command.group.enumeration.EditStatus;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 应急指挥喊话<br/>
 * @Description: <br/>
 * @author zsy 
 * @date 2020年3月10日 下午1:06:00
 */
@Entity
@Table(name="BVC_COMMAND_BROADCAST_SPEAK")
public class CommandBroadcastSpeakPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 喊话名称，可能无用 */
	private String name;
	
	/** 喊话服务返回的id，用于bvc监听应急喊话是否停止 */
	private Long appMessageId;
	
	/** 喊话推送的ts流目标地址（ip:port）*/
	private String targetUdp;
	
	/** 创建时间 */
	private Date createtime;
	
	/** 关联扬声器 */
	private List<CommandBroadcastSpeakerPO> speakers;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "APP_MESSAGE_ID")
	public Long getAppMessageId() {
		return appMessageId;
	}

	public void setAppMessageId(Long appMessageId) {
		this.appMessageId = appMessageId;
	}

	@Column(name = "TARGET_UDP")
	public String getTargetUdp() {
		return targetUdp;
	}

	public void setTargetUdp(String targetUdp) {
		this.targetUdp = targetUdp;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@OneToMany(mappedBy = "speak", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandBroadcastSpeakerPO> getSpeakers() {
		return speakers;
	}

	public void setSpeakers(List<CommandBroadcastSpeakerPO> speakers) {
		this.speakers = speakers;
	}
}
