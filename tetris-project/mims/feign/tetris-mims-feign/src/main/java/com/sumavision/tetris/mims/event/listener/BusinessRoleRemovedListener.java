package com.sumavision.tetris.mims.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.business.role.event.BusinessRoleRemovedEvent;
import com.sumavision.tetris.mims.event.BusinessRoleRemovedFeign;

/**
 * 业务角色删除事件代理<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月25日 下午4:31:31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BusinessRoleRemovedListener implements ApplicationListener<BusinessRoleRemovedEvent>{

	@Autowired
	private BusinessRoleRemovedFeign businessRoleRemovedFeign;
	
	/**
	 * 业务角色删除事件代理<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 下午4:31:31
	 */
	@Override
	public void onApplicationEvent(BusinessRoleRemovedEvent event) {
		try{
			businessRoleRemovedFeign.businessRoleRemoved(event.getRoleId(), event.getRoleName(), event.getCompanyId());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
