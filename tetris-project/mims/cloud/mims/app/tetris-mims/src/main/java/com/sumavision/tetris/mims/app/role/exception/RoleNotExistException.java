package com.sumavision.tetris.mims.app.role.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RoleNotExistException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(RoleNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public RoleNotExistException(Long roleId) {
		super(StatusCode.FORBIDDEN, "数据不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("角色不存在，角色id：")
										   .append(roleId)
										   .toString());
	}
	
	public RoleNotExistException(String companyId){
		super(StatusCode.FORBIDDEN, "数据不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("公司内置管理员角色不存在，公司id：")
										   .append(companyId)
										   .toString());
	}

	
}
