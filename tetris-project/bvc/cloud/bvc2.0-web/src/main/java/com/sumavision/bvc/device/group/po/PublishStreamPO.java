package com.sumavision.bvc.device.group.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组中的录制 
 * @author lvdeyang
 * @date 2018年8月8日 下午4:52:53 
 */
@Entity
@Table(name="BVC_DEVICE_GROUP_RECORD_PUBLISH")
public class PublishStreamPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 流类型 */
	private String format = "rtmp";
	
	/** 发布地址 */
	private String url;
	
	/** 发布地址转换 */
	private String transferUrl;
	
	/** cdn推流地址 */
	private String udp;
	
	/** 媒资id */
	private Long mimsId;

	/** 关联录制 */
	private RecordPO record;

	@ManyToOne
	@JoinColumn(name = "RECORD_ID")
	public RecordPO getRecord() {
		return record;
	}

	public void setRecord(RecordPO record) {
		this.record = record;
	}

	@Column(name = "FORMAT")
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "UDP")
	public String getUdp() {
		return udp;
	}

	public void setUdp(String udp) {
		this.udp = udp;
	}

	@Column(name = "MIMS_ID")
	public Long getMimsId() {
		return mimsId;
	}

	public void setMimsId(Long mimsId) {
		this.mimsId = mimsId;
	}

	@Column(name = "TRANSFER_URL")
	public String getTransferUrl() {
		return transferUrl;
	}

	public void setTransferUrl(String transferUrl) {
		this.transferUrl = transferUrl;
	}
	
}
