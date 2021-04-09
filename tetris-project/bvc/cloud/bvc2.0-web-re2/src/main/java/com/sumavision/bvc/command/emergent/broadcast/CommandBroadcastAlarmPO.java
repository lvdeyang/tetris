package com.sumavision.bvc.command.emergent.broadcast;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 应急指挥告警记录<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月22日 下午4:47:26
 */
@Entity
@Table(name="BVC_COMMAND_BROADCAST_ALARM")
public class CommandBroadcastAlarmPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;
	
	/** 告警名称，可能无用 */
	private String name;
	
	private String unifiedId;
	
	/** 创建时间 */
	private Date createtime;
	
	/** 关联设备 */
	private List<CommandBroadcastAlarmBundlePO> bundles;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "UNIFIED_ID")
	public String getUnifiedId() {
		return unifiedId;
	}

	public void setUnifiedId(String unifiedId) {
		this.unifiedId = unifiedId;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@OneToMany(mappedBy = "alarm", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CommandBroadcastAlarmBundlePO> getBundles() {
		return bundles;
	}

	public void setBundles(List<CommandBroadcastAlarmBundlePO> bundles) {
		this.bundles = bundles;
	}
}
