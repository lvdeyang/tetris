package com.sumavision.bvc.control.system.vo;

import com.alibaba.druid.stat.TableStat.Name;
import com.sumavision.bvc.system.po.AuthorizationPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * @ClassName: AuthorizationVO 
 * @author wjw 
 * @date 2019年1月8日 下午3:19:19
 */
public class AuthorizationVO extends AbstractBaseVO<AuthorizationVO, AuthorizationPO>{

	private String name;
	
	private String remark;
	
	public String getName() {
		return name;
	}

	public AuthorizationVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public AuthorizationVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	@Override
	public AuthorizationVO set(AuthorizationPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime())
			.setName(entity.getName())
			.setRemark(entity.getRemark());
		
		return this;
	}

}
