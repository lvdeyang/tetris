package com.sumavision.tetris.media.editor.task;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_MEDIA_EDITOR_TASK")
public class MediaEditorTaskPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	/** 创建任务的用户 */
	private String userId;
	
	/** 拟定一个标题 */
	private String title;
	
	/** 媒体编辑任务状态 */
	private MediaEditorTaskStatus status;
	
	/** 备注 */
	private String remarks;
	
	/** 流程实例id */
	private String processInstanceId;
	
	/** 节点id */
	private Long accessPointId;
	
	/** 任务创建时间 */
	private Date createTime;
	
	/** 任务进度 */
	private int completeRate;

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public MediaEditorTaskStatus getStatus() {
		return status;
	}

	public void setStatus(MediaEditorTaskStatus status) {
		this.status = status;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "PROCESS_INSTANCE_ID")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	@Column(name = "ACCESS_POINT_ID")
	public Long getAccessPointId() {
		return accessPointId;
	}

	public void setAccessPointId(Long accessPointId) {
		this.accessPointId = accessPointId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "COMPLETE_RATE")
	public Integer getCompleteRate() {
		return completeRate;
	}

	public void setCompleteRate(int completeRate) {
		this.completeRate = completeRate;
	}
}
