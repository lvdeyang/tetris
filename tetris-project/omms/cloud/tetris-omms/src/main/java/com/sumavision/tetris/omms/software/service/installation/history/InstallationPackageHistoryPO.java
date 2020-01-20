package com.sumavision.tetris.omms.software.service.installation.history;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 安装包历史版本<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 下午2:03:33
 */
@Entity
@Table(name = "TETRIS_OMMS_INSTALLATION_PACKAGE_HISTORY")
public class InstallationPackageHistoryPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 服务类型id */
	private Long serviceTypeId;
	
	/** 文件路径 */
	private String filePath;
	
	/** 版本号 */
	private String version;
	
	/** 备注 */
	private String remark;
	
	/** 创建人 */
	private String creator;
	
	/** 创建时间 */
	private Date createTime;

	@Column(name = "SERVICE_TYPE_ID")
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	@Lob
	@Column(name = "FILE_PATH", columnDefinition = "LONGTEXT")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "VERSION")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATOR")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
