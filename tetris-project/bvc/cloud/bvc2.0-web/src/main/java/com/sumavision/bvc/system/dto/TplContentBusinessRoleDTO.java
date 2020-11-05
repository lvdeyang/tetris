package com.sumavision.bvc.system.dto;

/**
 * @ClassName: 会议模板内容——业务角色 
 * @author lvdeyang
 * @date 2018年7月30日 上午10:17:23 
 */
public class TplContentBusinessRoleDTO {
	
	/** 模板内容id */
	private Long contentId;
	
	/** 模板id */
	private Long tplId;
	
	/** 业务角色id */
	private Long roleId;
	
	/** 业务角色名称 */
	private String roleName;
	
	public TplContentBusinessRoleDTO(){}
	
	public TplContentBusinessRoleDTO(Long contentId, Long tplId, Long roleId, String roleName){
		this.contentId = contentId;
		this.tplId = tplId;
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public Long getContentId() {
		return contentId;
	}

	public TplContentBusinessRoleDTO setContentId(Long contentId) {
		this.contentId = contentId;
		return this;
	}

	public Long getTplId() {
		return tplId;
	}

	public TplContentBusinessRoleDTO setTplId(Long tplId) {
		this.tplId = tplId;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public TplContentBusinessRoleDTO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public TplContentBusinessRoleDTO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}
	
}
