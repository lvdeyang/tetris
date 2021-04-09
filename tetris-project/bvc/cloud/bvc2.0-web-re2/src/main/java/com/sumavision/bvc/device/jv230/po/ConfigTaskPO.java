package com.sumavision.bvc.device.jv230.po;

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
 * @ClassName: 大屏配置任务 
 * @Description: 一个任务代表一个源（视频源、轮询、图片、字幕）
 * @author wjw
 * @date 2018年11月26日 上午10:12:32
 */
@Entity
@Table(name = "BVC_COMBINE_JV230_CONFIG_TASK")
public class ConfigTaskPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	//任务类型
	private String type;
	
	//任务内容
	private byte[] content;	
	
	//轮询时间--默认设置30s
	private String time = "30";
	
	/**
	 * @Fields largescreenConfigList:任务对应大屏配置
	 */
	private CombineJv230ConfigPO combineJv230Config;
	
	/**
	 * @Fields configLocationPOs:配置任务位置列表
	 */
	private Set<ConfigLocationPO> configLocations = new HashSet<ConfigLocationPO>();

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name ="CONTENT", columnDefinition = "LONGBLOB", nullable = true)
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Column(name = "TIME")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@ManyToOne
	@JoinColumn(name="COMBINE_JV230_CONFIG_ID")
	public CombineJv230ConfigPO getCombineJv230Config() {
		return combineJv230Config;
	}

	public void setCombineJv230Config(CombineJv230ConfigPO combineJv230Config) {
		this.combineJv230Config = combineJv230Config;
	}

	@OneToMany(mappedBy = "configTask", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<ConfigLocationPO> getConfigLocations() {
		return configLocations;
	}

	public void setConfigLocations(Set<ConfigLocationPO> configLocations) {
		this.configLocations = configLocations;
	}
}
