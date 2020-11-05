package com.sumavision.tetris.record.source;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 输入源PO
 *
 */

@Entity
@Table(name = "source")
public class SourcePO extends AbstractBasePO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6328674975012810301L;

	/**
	 * 输入自定义名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 输入URL
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 录制路径
	 */
	// @Column(name = "ftpUrl")
	// private String ftpUrl;

	/**
	 * 收流localIP
	 */
	@Column(name = "localIp")
	private String localIp;

	/**
	 * 节目id
	 */
	// @Column(name = "programId")
	// private Integer programID;

	// 录制状态：0，未开始，1录制，2暂停
	// @Column(name = "recordStatus")
	// private Integer recordStatus = 0;

	/**
	 * 节目id
	 */
	// @Column(name = "programName")
	// private String programName;

	// @Column(name = "vPort")
	// private Integer vPort;

	// @Column(name = "aPort")
	// private Integer aPort;

	// @Column(name = "vType")
	// private String vType;

	// @Column(name = "aType")
	// private String aType;

	// @Column(name = "isAnalyze")
	// private Integer isAnalyze;

	// @Column(name = "analyzeId")
	// private Long analyzeId;

	/**
	 * 是否有视频 0标识没有视频，是纯音频源 1标识有视频
	 */
	// @Column(name = "isVideo")
	// private Integer isVideo;

	/**
	 * 录制设备
	 */
	@Column(name = "deviceId")
	private Long deviceId;

	// @Column(name = "recordingId")
	// private Long recordingId;

	// @Column(name = "epgFile")
	// private String epgFile;

	/**
	 * 统一任务ID，对外接口中使用，用以标识外部创建的输入源的唯一ID
	 */
	// @Column(name = "uniTaskId")
	// private String uniTaskId;

	/**
	 * 源删除的状态：0为正常，1标识源已停用，但是未删除，以解决录制源删除，但是还要回看之前录制内容的问题
	 */
	@Column(name = "delStatus")
	private Integer delStatus = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLocalIp() {
		return localIp;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

}
