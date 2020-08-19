package com.sumavision.tetris.test.flow.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;
import com.sumavision.tetris.test.flow.enumeration.ReportStatus;

/**
 * @ClassName: 接口测试报告<br/> 
 * @author lvdeyang
 * @date 2018年8月31日 上午11:06:03 
 */
@Entity
@Table(name = "TETRIS_TEST_FLOW_REPORT")
public class ReportPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 服务uuid */
	private String serviceUuid;
	
	/** 接口方案id */
	private Long schemeId;
	
	/** 接口测试结果 */
	private ReportStatus status;
	
	/** 当测试结果与期望值不一致时存储测试结果 */
	private String actualValue;
	
	/** 接口调用耗时：单位毫秒 */
	private String timeSpend;

	@Column(name = "SERVICE_UUID")
	public String getServiceUuid() {
		return serviceUuid;
	}

	public void setServiceUuid(String serviceUuid) {
		this.serviceUuid = serviceUuid;
	}

	@Column(name = "SCHEME_ID")
	public Long getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(Long schemeId) {
		this.schemeId = schemeId;
	}

	@Column(name = "STATUS")
	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}

	@Column(name = "ACTUAL_VALUE")
	public String getActualValue() {
		return actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	@Column(name = "TIME_SPEND")
	public String getTimeSpend() {
		return timeSpend;
	}

	public void setTimeSpend(String timeSpend) {
		this.timeSpend = timeSpend;
	}
	
}
