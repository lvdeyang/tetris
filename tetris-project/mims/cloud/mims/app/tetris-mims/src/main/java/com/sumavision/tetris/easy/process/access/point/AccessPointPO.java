package com.sumavision.tetris.easy.process.access.point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.easy.process.access.service.ServiceType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 流程接入点<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午1:07:14
 */
@Entity
@Table(name = "TETRIS_PROCESS_ACCESS_POINT")
public class AccessPointPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 接入点名称 */
	private String name;
	
	/** 接入点类型 */
	private AccessPointType type;

	/** 调用方法 */
	private String method;
	
	/** 接入点方法类型 */
	private AccessPointMethodType methodType;
	
	/** 隶属服务id */
	private Long serviceId;
	
	/** 隶属服务类型 */
	private ServiceType serviceType;
	
	/** 备注 */
	private String remarks;
	
	/** 作用域 */
	private AccessPointScope scope;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public AccessPointType getType() {
		return type;
	}

	public void setType(AccessPointType type) {
		this.type = type;
	}

	@Column(name = "METHOD")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "METHOD_TYPE")
	public AccessPointMethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(AccessPointMethodType methodType) {
		this.methodType = methodType;
	}

	@Column(name = "SERVICE_ID")
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SERVICE_TYPE")
	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SCOPE")
	public AccessPointScope getScope() {
		return scope;
	}

	public void setScope(AccessPointScope scope) {
		this.scope = scope;
	}
	
	/**
	 * 格式化rest服务方法路径，以/开头，不以/结尾<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午5:34:16
	 * @param String method 方法
	 * @return String method 格式化方法
	 */
	public static String formatRestMethod(String method){
		if(method==null || "".equals(method)) return method;
		if(!method.startsWith("/")){
			method = new StringBufferWrapper().append("/").append(method).toString();
		}
		if(method.endsWith("/")){
			method = method.substring(0, method.length()-1);
		}
		return method;
	}
	
}
