package com.sumavision.tetris.test.flow.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 接口录制模板<br/> 
 * @author lvdeyang
 * @date 2018年8月30日 下午5:37:36 
 */
@Entity
@Table(name = "TETRIS_TEST_FLOW_SHEME")
public class SchemePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 服务唯一标识（微服务id） */
	private String serviceUuid;
	
	/** 接口名称 */
	private String name;
	
	/** 接口所在类名 */
	private String className;
	
	/** 接口方法名	 */
	private String methodName;
	
	/** 接口地址 */
	private String uri;
	
	/** 接口参数 */
	private String param;
	
	/** 接口期望值 */
	private byte[] expect;
	
	/** 调用顺序 */
	private long serialNum;

	@Column(name = "SERVICE_UUID")
	public String getServiceUuid() {
		return serviceUuid;
	}

	public void setServiceUuid(String serviceUuid) {
		this.serviceUuid = serviceUuid;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CLASS_NAME")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "METHOD_NAME")
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Column(name = "URI")
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Column(name = "PARAM")
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "EXPECT", columnDefinition="BLOB")
	public byte[] getExpect() {
		return expect;
	}

	public void setExpect(byte[] expect) {
		this.expect = expect;
	}

	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(name = "SERIALNUM")
	public long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(long serialNum) {
		this.serialNum = serialNum;
	}
	
}
