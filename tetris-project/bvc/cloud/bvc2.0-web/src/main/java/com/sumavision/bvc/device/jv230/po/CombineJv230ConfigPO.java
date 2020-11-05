package com.sumavision.bvc.device.jv230.po;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 大屏配置
 * @author wjw
 * @date 2018年11月26日 上午10:05:32
 */
@Entity
@Table(name = "BVC_COMBINE_JV230_CONFIG")
public class CombineJv230ConfigPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	//大屏配置名称
	private String name;
	
	//上屏状态
	private String status;
	
	//备注
	private String remark;
	
	//创建时间
	private Date createTime;
	
	//配置内容
	private String content;
	
	/**
	 * @Fields combineJv230:配置对应大屏
	 */
	private CombineJv230PO combineJv230;
	
	/**
	 * @Fields taskConfig:配置下的任务
	 */
	private Set<ConfigTaskPO> configTasks = new HashSet<ConfigTaskPO>();

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "CREATETIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne
	@JoinColumn(name="COMBINE_JV230_ID")
	public CombineJv230PO getCombineJv230() {
		return combineJv230;
	}

	public void setCombineJv230(CombineJv230PO combineJv230) {
		this.combineJv230 = combineJv230;
	}

	@OneToMany(mappedBy = "combineJv230Config", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<ConfigTaskPO> getConfigTasks() {
		return configTasks;
	}

	public void setConfigTasks(Set<ConfigTaskPO> configTasks) {
		this.configTasks = configTasks;
	}
}
