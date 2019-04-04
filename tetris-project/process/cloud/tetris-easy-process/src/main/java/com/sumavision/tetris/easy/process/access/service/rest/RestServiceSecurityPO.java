package com.sumavision.tetris.easy.process.access.service.rest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 接口访问安全<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月4日 下午1:20:34
 */
@Entity
@Table(name = "TETRIS_PROCESS_SERVICE_REST_SECURITY")
public class RestServiceSecurityPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;
	
	/** rest服务id */
	private Long restServiceId;
	
	/** key */
	private String primaryKey;
	
	/** value */
	private String value;
	
	/** 安全配置类型 */
	private RestServiceSecurityType type;

	@Column(name = "REST_SERVICE_ID")
	public Long getRestServiceId() {
		return restServiceId;
	}

	public void setRestServiceId(Long restServiceId) {
		this.restServiceId = restServiceId;
	}

	@Column(name = "PRIMARY_KEY")
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public RestServiceSecurityType getType() {
		return type;
	}

	public void setType(RestServiceSecurityType type) {
		this.type = type;
	}

}
