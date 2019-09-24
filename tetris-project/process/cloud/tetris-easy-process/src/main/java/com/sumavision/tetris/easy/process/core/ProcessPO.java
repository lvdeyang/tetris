package com.sumavision.tetris.easy.process.core;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 流程对象<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月24日 下午5:09:08
 */
@Entity
@Table(name = "TETRIS_PROCESS")
public class ProcessPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 用户定义流程id */
	private String processId;
	
	/** 流程名称 */
	private String name;
	
	/** 流程说明 */
	private String remarks;
	
	/** bpmn内容 */
	private String bpmn;
	
	/** 
	 * 用户任务绑定变量 
	 * {
	 * 	show:[{
	 * 	 taskId:"任务id", 
	 * 	 id:"变量id",
	 * 	 key:"变量主键", 
	 * 	 name:"变量名称", 
	 *   type:"b[|t][|e][|i][|v][|a]}", 
	 *   typeName:"文本[|大文本][|枚举][|图片][|视频][|音频]",
	 * 	 redio:[{label:"标签",value:"值"}]
	 * 	}], 
	 *  set:[{
	 *   taskId:"任务id", 
	 *   id:"变量id",
	 *   key:"变量主键", 
	 *   name:"变量名称", 
	 *   type:"b[|t][|e][|i][|v][|a]}", 
	 *   typeName:"文本[|大文本][|枚举][|图片][|视频][|音频]",
	 *   redio:[{label:"标签",value:"值"}]
	 *  }]
	 * }
	 */
	private String userTaskBindVariables;
	
	/** 流程类型 */
	private ProcessType type;
	
	/** 临时配置文件位置 */
	private String path;
	
	/** 发布时间 */
	private Date publishTime;
	
	public ProcessPO(){
		this.setUuid(new StringBufferWrapper().append("_").append(this.getUuid()).toString());
	}
	
	@Column(name = "PROCESS_ID")
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BPMN", columnDefinition = "text")
	public String getBpmn() {
		return bpmn;
	}

	public void setBpmn(String bpmn) {
		this.bpmn = bpmn;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "USER_TASK_BIND_VARIABLES", columnDefinition = "text")
	public String getUserTaskBindVariables() {
		return userTaskBindVariables;
	}

	public void setUserTaskBindVariables(String userTaskBindVariables) {
		this.userTaskBindVariables = userTaskBindVariables;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public ProcessType getType() {
		return type;
	}

	public void setType(ProcessType type) {
		this.type = type;
	}

	@Column(name = "PATH")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PUBLISH_TIME")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	
}
