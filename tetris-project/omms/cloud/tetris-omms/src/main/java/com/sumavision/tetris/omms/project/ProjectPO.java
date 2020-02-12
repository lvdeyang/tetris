package com.sumavision.tetris.omms.project;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 项目<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 下午2:05:19
 */
public class ProjectPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 项目名称 */
	private String name;
	
	/** 备注 */
	private String remark;
	
	/** 立项人 */
	private String creator;
	
	/** 立项时间 */
	private Date createTime;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
