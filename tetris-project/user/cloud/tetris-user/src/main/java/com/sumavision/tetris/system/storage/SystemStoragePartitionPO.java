package com.sumavision.tetris.system.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 存储分区<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月6日 下午5:26:48
 */
@Entity
@Table(name = "TETRIS_SYSTEM_STORAGE_PARTITION")
public class SystemStoragePartitionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 分区名 */
	private String name;
	
	/** 存储id */
	private Long storageId;
	
	/** 分区总大小 */
	private Long totalSize;
	
	/** 分区使用大小 */
	private Long usedSize;
	
	/** 分区可用空间大小 */
	private Long freeSize;
	
	/** 备注 */
	private String remark;

	@Column(name = "STORAGE_ID")
	public Long getStorageId() {
		return storageId;
	}

	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TOTAL_SIZE")
	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	@Column(name = "USED_SIZE")
	public Long getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(Long usedSize) {
		this.usedSize = usedSize;
	}

	@Column(name = "FREE_SIZE")
	public Long getFreeSize() {
		return freeSize;
	}

	public void setFreeSize(Long freeSize) {
		this.freeSize = freeSize;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
