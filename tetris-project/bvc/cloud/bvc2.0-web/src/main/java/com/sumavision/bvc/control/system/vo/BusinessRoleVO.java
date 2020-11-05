package com.sumavision.bvc.control.system.vo;

import com.sumavision.bvc.system.dto.TplContentBusinessRoleDTO;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * @ClassName: 权限角色
 * @author zy
 * @date 2018年7月25日 下午8:43:40 
 */
public class BusinessRoleVO extends AbstractBaseVO<BusinessRoleVO, BusinessRolePO>{
	
	/** 角色名称 */
	private String name;
	
	/** 特殊属性:比如唯一性, 可录制 */
	private String special;
	
	private String type;

	public String getName() {
		return name;
	}

	public BusinessRoleVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getSpecial() {
		return special;
	}

	public BusinessRoleVO setSpecial(String special) {
		this.special = special;
		return this;
	}

	public String getType() {
		return type;
	}

	public BusinessRoleVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public BusinessRoleVO set(BusinessRolePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setSpecial(entity.getSpecial()==null?"":entity.getSpecial().getName())
			.setType(entity.getType()==null?"":entity.getType().getName());
		return this;
	}
	
	public BusinessRoleVO set(TplContentBusinessRoleDTO entity) {
		this.setId(entity.getRoleId())
			.setName(entity.getRoleName());
		return this;
	}

}
