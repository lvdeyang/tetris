package com.sumavision.bvc.control.system.vo;

import com.sumavision.bvc.system.dto.RecordSchemeDTO;
import com.sumavision.bvc.system.dto.TplContentRecordSchemeDTO;
import com.sumavision.bvc.system.po.RecordSchemePO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * @ClassName: 录制
 * @author zy
 * @date 2018年7月25日 下午9:00:20 
 */
public class RecordSchemeVO extends AbstractBaseVO<RecordSchemeVO, RecordSchemeDTO>{
	
	/** 方案名称  */
	private String name;
	
	/** 录制角色id */
	private Long roleId;
	
	/** 录制角色名称*/
	private String roleName;

	public String getName() {
		return name;
	}

	public RecordSchemeVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public RecordSchemeVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public RecordSchemeVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	@Override
	public RecordSchemeVO set(RecordSchemeDTO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName());
		return this;
	}
	
	public RecordSchemeVO set(RecordSchemePO entity){
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRoleId(entity.getRole().getId())
			.setRoleName(entity.getRole().getName());
		return this;
	}
	
	public RecordSchemeVO set(TplContentRecordSchemeDTO entity){
		this.setId(entity.getRecordId())
			.setName(entity.getRecordName());
		return this;
	}

}
