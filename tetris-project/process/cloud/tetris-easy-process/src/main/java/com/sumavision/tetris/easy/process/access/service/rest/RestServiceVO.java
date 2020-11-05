package com.sumavision.tetris.easy.process.access.service.rest;

import java.util.Set;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.easy.process.access.point.AccessPointVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * rest服务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月17日 上午11:43:19
 */
public class RestServiceVO extends AbstractBaseVO<RestServiceVO, RestServicePO>{

	private String name;
	
	private String host;
	
	private String port;
	
	private String contextPath;
	
	private String remarks;
	
	private Set<AccessPointVO> accessPoints;
	
	public String getName() {
		return name;
	}

	public RestServiceVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getHost() {
		return host;
	}

	public RestServiceVO setHost(String host) {
		this.host = host;
		return this;
	}

	public String getPort() {
		return port;
	}

	public RestServiceVO setPort(String port) {
		this.port = port;
		return this;
	}

	public String getContextPath() {
		return contextPath;
	}

	public RestServiceVO setContextPath(String contextPath) {
		this.contextPath = contextPath;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public RestServiceVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}
	
	public Set<AccessPointVO> getAccessPoints() {
		return accessPoints;
	}

	public RestServiceVO setAccessPoints(Set<AccessPointVO> accessPoints) {
		this.accessPoints = accessPoints;
		return this;
	}

	@Override
	public RestServiceVO set(RestServicePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setHost(entity.getHost())
			.setPort(entity.getPort())
			.setContextPath(entity.getContextPath())
			.setRemarks(entity.getRemarks());
		return this;
	}

}
