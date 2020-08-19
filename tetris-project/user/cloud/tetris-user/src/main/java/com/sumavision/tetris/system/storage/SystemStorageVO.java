package com.sumavision.tetris.system.storage;

import java.net.URL;

import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class SystemStorageVO extends AbstractBaseVO<SystemStorageVO, SystemStoragePO>{

	private String name;
	
	private String rootPath;
	
	/** ftp根路径 */
	private String uploadProtocol;
	private String uploadIp;
	private String uploadPort;
	
	/** 预览http根路径 */
	private String previewProtocol;
	private String previewIp;
	private String previewPort;
	
	/** 小工具访问http根路径 */
	private String controlProtocol;
	private String controlIp;
	private String controlPort;
	
	private String serverGadgetType;
	private String serverGadgetName;
	
	private String totalSize;
	
	private String usedSize;
	
	private String freeSize;
	
	private String status;
	
	private String remark;
	
	public String getName() {
		return name;
	}

	public SystemStorageVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRootPath() {
		return rootPath;
	}

	public SystemStorageVO setRootPath(String rootPath) {
		this.rootPath = rootPath;
		return this;
	}

	public String getUploadProtocol() {
		return uploadProtocol;
	}

	public SystemStorageVO setUploadProtocol(String uploadProtocol) {
		this.uploadProtocol = uploadProtocol;
		return this;
	}

	public String getUploadIp() {
		return uploadIp;
	}

	public SystemStorageVO setUploadIp(String uploadIp) {
		this.uploadIp = uploadIp;
		return this;
	}

	public String getUploadPort() {
		return uploadPort;
	}

	public SystemStorageVO setUploadPort(String uploadPort) {
		this.uploadPort = uploadPort;
		return this;
	}

	public String getPreviewProtocol() {
		return previewProtocol;
	}

	public SystemStorageVO setPreviewProtocol(String previewProtocol) {
		this.previewProtocol = previewProtocol;
		return this;
	}

	public String getPreviewIp() {
		return previewIp;
	}

	public SystemStorageVO setPreviewIp(String previewIp) {
		this.previewIp = previewIp;
		return this;
	}

	public String getPreviewPort() {
		return previewPort;
	}

	public SystemStorageVO setPreviewPort(String previewPort) {
		this.previewPort = previewPort;
		return this;
	}

	public String getControlProtocol() {
		return controlProtocol;
	}

	public SystemStorageVO setControlProtocol(String controlProtocol) {
		this.controlProtocol = controlProtocol;
		return this;
	}

	public String getControlIp() {
		return controlIp;
	}

	public SystemStorageVO setControlIp(String controlIp) {
		this.controlIp = controlIp;
		return this;
	}

	public String getControlPort() {
		return controlPort;
	}

	public SystemStorageVO setControlPort(String controlPort) {
		this.controlPort = controlPort;
		return this;
	}

	public String getServerGadgetType() {
		return serverGadgetType;
	}

	public SystemStorageVO setServerGadgetType(String serverGadgetType) {
		this.serverGadgetType = serverGadgetType;
		return this;
	}

	public String getServerGadgetName() {
		return serverGadgetName;
	}

	public SystemStorageVO setServerGadgetName(String serverGadgetName) {
		this.serverGadgetName = serverGadgetName;
		return this;
	}

	public String getTotalSize() {
		return totalSize;
	}

	public SystemStorageVO setTotalSize(String totalSize) {
		this.totalSize = totalSize;
		return this;
	}

	public String getUsedSize() {
		return usedSize;
	}

	public SystemStorageVO setUsedSize(String usedSize) {
		this.usedSize = usedSize;
		return this;
	}

	public String getFreeSize() {
		return freeSize;
	}

	public SystemStorageVO setFreeSize(String freeSize) {
		this.freeSize = freeSize;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public SystemStorageVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public SystemStorageVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	@Override
	public SystemStorageVO set(SystemStoragePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRootPath(entity.getRootPath())
			.setServerGadgetType(entity.getServerGadgetType().toString())
			.setServerGadgetName(entity.getServerGadgetType().getName())
			.setTotalSize(entity.getTotalSize()==null?"0B":ByteUtil.getSize(entity.getTotalSize()))
			.setUsedSize(entity.getUsedSize()==null?"0B":ByteUtil.getSize(entity.getUsedSize()))
			.setFreeSize(entity.getFreeSize()==null?"0B":ByteUtil.getSize(entity.getFreeSize()))
			.setStatus(entity.getStatus().intValue()==0?"离线":"在线")
			.setRemark(entity.getRemark());
		URL url = new URL(entity.getBaseFtpPath());
		this.setUploadProtocol(new StringBufferWrapper().append(url.getProtocol()).append("://").toString())
			.setUploadIp(url.getHost())
			.setUploadPort(String.valueOf(url.getPort()));
		url = new URL(entity.getBasePreviewPath());
		this.setPreviewProtocol(new StringBufferWrapper().append(url.getProtocol()).append("://").toString())
			.setPreviewIp(url.getHost())
			.setPreviewPort(String.valueOf(url.getPort()));
		url = new URL(entity.getGadgetBasePath());
		this.setControlProtocol(new StringBufferWrapper().append(url.getProtocol()).append("://").toString())
			.setControlIp(url.getHost())
			.setControlPort(String.valueOf(url.getPort()));
		return this;
	}
	
}
