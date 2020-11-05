package com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OperationPackageStreamPermissionQuery {
	@Autowired
	private OperationPackageStreamPermissionDAO streamPermissionDAO;
	
	/**
	 * 根据套餐id获取关联<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 下午2:55:31
	 * @param Long packageId 套餐id
	 * @return OperationPackageStreamPermissionVO
	 */
	public OperationPackageStreamPermissionVO queryByPackageId(Long packageId) throws Exception {
		OperationPackageStreamPermissionPO permissionPO = streamPermissionDAO.findByPackageId(packageId);
		return permissionPO == null ? null : new OperationPackageStreamPermissionVO().set(permissionPO);
	}
}
