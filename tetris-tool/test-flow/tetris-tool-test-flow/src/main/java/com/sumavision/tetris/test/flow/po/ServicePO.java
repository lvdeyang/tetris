package com.sumavision.tetris.test.flow.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 测试服务信息<br/> 
 * @author lvdeyang
 * @date 2018年8月29日 下午7:00:50 
 */
@Entity
@Table(name = "TETRIS_TEST_FLOW_SERVICE")
public class ServicePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 服务名称 */
	private String name;
	
	/** 服务ip */
	private String ip;
	
	/** 服务端口 */
	private String port;
	
	/** context-path */
	private String contextPath;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "PORT")
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Column(name = "CONTEXT_PATH")
	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
}
