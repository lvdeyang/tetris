package com.sumavision.bvc.control.system.vo;

import com.sumavision.bvc.device.group.enumeration.AutoBuildAgenda;
import com.sumavision.bvc.system.dto.TplContentBusinessRoleDTO;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * @ClassName: 自动建立的议程
 * @author zsy
 * @date 2019年3月29日 下午15:43:40 
 */
public class AutoBuildAgendaVO{
	
	private Long id;
	
	/** 议程名称 */
	private String name;

	public Long getId() {
		return id;
	}

	public AutoBuildAgendaVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public AutoBuildAgendaVO setName(String name) {
		this.name = name;
		return this;
	}

	public AutoBuildAgendaVO set(AutoBuildAgenda entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName());
		return this;
	}

}
