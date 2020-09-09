package com.sumavision.tetris.omms.hardware.server;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 服务器硬件<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 上午10:39:34
 */
@Entity
@Table(name = "TETRIS_OMMS_SERVER")
public class ServerPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 别名 */
	private String name;
	
	/** ip */
	private String ip;
	
	/** 小工具端口 */
	private String gadgetPort;
	
	/** 小工具用户名 */
	private String gadgetUsername;
	
	/** 小工具密码 */
	private String gadgetPassword;
	
	/** ftp端口 */
	private String ftpPort;
	
	/** ftp用户名 */
	private String ftpUsername;
	
	/** ftp密码 */
	private String ftpPassword;
	
	/** 备注 */
	private String remark;
	
	/** 创建者 */
	private String creator;

	/** 创建时间 */
	private Date createTime;
	
	/** 服务器状态 */
	private ServerStatus status;

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

	@Column(name = "GADGET_PORT")
	public String getGadgetPort() {
		return gadgetPort;
	}

	public void setGadgetPort(String gadgetPort) {
		this.gadgetPort = gadgetPort;
	}

	@Column(name = "GADGET_USERNAME")
	public String getGadgetUsername() {
		return gadgetUsername;
	}

	public void setGadgetUsername(String gadgetUsername) {
		this.gadgetUsername = gadgetUsername;
	}

	@Column(name = "GADGET_PASSWORD")
	public String getGadgetPassword() {
		return gadgetPassword;
	}

	public void setGadgetPassword(String gadgetPassword) {
		this.gadgetPassword = gadgetPassword;
	}

	@Column(name = "FTP_PORT")
	public String getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}

	@Column(name = "FTP_USERNAME")
	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	@Column(name = "FTP_PASSWORD")
	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATOR")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public ServerStatus getStatus() {
		return status;
	}

	public void setStatus(ServerStatus status) {
		this.status = status;
	}
	
}
