package com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OperationPackageMediaPermissionQuery {
	@Autowired
	private OperationPackageMediaPermissionDAO mediaPermissionDAO;
	
	/**
	 * 根据套餐id获取资源绑定关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午9:53:15
	 * @param Long packageId 套餐id
	 * @return List<OperationMediaPackagePermissionVO> 关联数组
	 */
	public List<OperationPackageMediaPermissionVO> queryByPackageId(Long packageId) throws Exception {
		List<OperationPackageMediaPermissionPO> permissionPOs = mediaPermissionDAO.findByPackageId(packageId);
		return OperationPackageMediaPermissionVO.getConverter(OperationPackageMediaPermissionVO.class).convert(permissionPOs, OperationPackageMediaPermissionVO.class);
	}
	
	/**
	 * 根据套餐id和资源uuid查询绑定信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 上午9:31:32
	 * @param Long packageId 套餐id
	 * @param String mimsUuid 资源uuid
	 * @return
	 */
	public OperationPackageMediaPermissionVO queryByPackageIdAndMimsUuid(Long packageId, String mimsUuid) throws Exception {
		OperationPackageMediaPermissionPO permissionPO = mediaPermissionDAO.findByPackageIdAndMimsUuid(packageId, mimsUuid);
		return permissionPO == null ? null : new OperationPackageMediaPermissionVO().set(permissionPO);
	}
}
