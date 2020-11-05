package com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationPackageStreamPermissionService {
	@Autowired
	private OperationPackageStreamPermissionDAO streamPermissionDAO;
	
	/**
	 * 根据套餐id设置关联(传入数据类型为关联VO)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:26:53
	 * @param Long packageId 套餐id
	 * @param OperationPackageStreamPermissionVO mediaPackagePermissionVO 关联关系
	 * @return OperationMediaPackagePermissionVO 关联关系
	 */
	public OperationPackageStreamPermissionVO addByVos(
			Long packageId,
			OperationPackageStreamPermissionVO streamPermissionVO) throws Exception {
		if (streamPermissionVO == null) return new OperationPackageStreamPermissionVO();
		OperationPackageStreamPermissionPO permissionPO = new OperationPackageStreamPermissionPO();
		permissionPO.setPackageId(packageId);
		permissionPO.setNum(streamPermissionVO.getNum());
		streamPermissionDAO.save(permissionPO);
		return new OperationPackageStreamPermissionVO().set(permissionPO);
	}
	
	/**
	 * 根据套餐id复制<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:59:42
	 * @param Long sourcePackageId 源套餐id
	 * @param Long destPackageId 目的套餐id
	 * @return List<OperationPackageStreamPermissionVO>
	 */
	public OperationPackageStreamPermissionVO copeByPakcageId(Long sourcePackageId, Long destPackageId) throws Exception {
		OperationPackageStreamPermissionPO permissionPOs = streamPermissionDAO.findByPackageId(sourcePackageId);
		return addByVos(destPackageId, new OperationPackageStreamPermissionVO().set(permissionPOs));
	}
	
	/**
	 * 根据套餐id删除所有关联<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:29:20
	 * @param Long packageId 套餐id
	 */
	public void removeByPackageId(Long packageId) throws Exception {
		streamPermissionDAO.removeByPackageId(packageId);
	}
}
