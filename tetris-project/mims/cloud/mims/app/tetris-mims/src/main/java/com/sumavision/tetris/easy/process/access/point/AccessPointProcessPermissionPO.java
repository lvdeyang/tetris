package com.sumavision.tetris.easy.process.access.point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 接入点流程权限<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月28日 下午3:49:14
 */
@Entity
@Table(name = "TETRIS_ACCESS_POINT_PROCESS_PERMISSION")
public class AccessPointProcessPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 接入点id */
	private Long accessPointId;
	
	/** 流程id */
	private Long processId;

	@Column(name = "ACCESS_POINT_ID")
	public Long getAccessPointId() {
		return accessPointId;
	}

	public void setAccessPointId(Long accessPointId) {
		this.accessPointId = accessPointId;
	}

	@Column(name = "PROCESS_ID")
	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

}
