package com.sumavision.tetris.omms.hardware.server;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ServerVO extends AbstractBaseVO<ServerVO, ServerPO>{

	private Long id;
	
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
	
	/** 备注 */
	private String remark;
	
	/** 创建者 */
	private String creator;

	/** 创建时间 */
	private String createTime;
	
	/** 服务器状态 */
	private String status;
	
	public Long getId() {
		return id;
	}

	public ServerVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public ServerVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public ServerVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getGadgetPort() {
		return gadgetPort;
	}

	public ServerVO setGadgetPort(String gadgetPort) {
		this.gadgetPort = gadgetPort;
		return this;
	}

	public String getGadgetUsername() {
		return gadgetUsername;
	}

	public ServerVO setGadgetUsername(String gadgetUsername) {
		this.gadgetUsername = gadgetUsername;
		return this;
	}

	public String getGadgetPassword() {
		return gadgetPassword;
	}

	public ServerVO setGadgetPassword(String gadgetPassword) {
		this.gadgetPassword = gadgetPassword;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public ServerVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getCreator() {
		return creator;
	}

	public ServerVO setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public ServerVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public ServerVO setStatus(String status) {
		this.status = status;
		return this;
	}

	@Override
	public ServerVO set(ServerPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setIp(entity.getIp())
			.setGadgetPort(entity.getGadgetPort())
			.setGadgetUsername(entity.getGadgetUsername())
			.setGadgetPassword(entity.getGadgetPassword())
			.setRemark(entity.getRemark())
			.setCreator(entity.getCreator())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setStatus(entity.getStatus().getName());
		return this;
	}

}
