package com.sumavision.tetris.easy.process.access.service.rest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * rest服务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 上午11:57:04
 */
@Entity
@Table(name = "TETRIS_PROCESS_SERVICE_REST")
public class RestServicePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 服务名称 */
	private String name;

	/** 服务ip地址 */
	private String host;
	
	/** 端口 */
	private String port;
	
	/** context-path */
	private String contextPath;
	
	/** 备注 */
	private String remarks;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "HOST")
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	/**
	 * 格式化context-path，以/开头，不以/结尾<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午5:30:06
	 * @param contextPath
	 * @return 格式化的 contextPath
	 */
	public static String formatContextPath(String contextPath){
		if(contextPath==null || "".equals(contextPath)) return contextPath;
		if(!contextPath.startsWith("/")){
			contextPath = new StringBufferWrapper().append("/").append(contextPath).toString();
		}
		if(contextPath.endsWith("/")){
			contextPath = contextPath.substring(0, contextPath.length()-1);
		}
		return contextPath;
	}
	
}
