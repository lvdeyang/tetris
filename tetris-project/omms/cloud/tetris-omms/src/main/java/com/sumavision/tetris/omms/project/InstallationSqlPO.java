package com.sumavision.tetris.omms.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * sql安装包<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 下午2:41:31
 */
@Entity
@Table(name = "TETRIS_OMMS_INSTALLATION_SQL")
public class InstallationSqlPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 服务类型id */
	private Long serviceTypeId;
	
	/** 项目id */
	private Long projectId;
	
	/** sql内容 */
	private String sqlContent;

	@Column(name = "SERVICE_TYPE_ID")
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	@Column(name = "PROJECT_ID")
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	@Lob
	@Column(name = "SQL_CONTENT", columnDefinition = "LONGTEXT")
	public String getSqlContent() {
		return sqlContent;
	}

	public void setSqlContent(String sqlContent) {
		this.sqlContent = sqlContent;
	}
	
}
