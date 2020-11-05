package com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationPackageMediaPermissionService {
	@Autowired
	private OperationPackageMediaPermissionDAO mediaPackagePermissionDAO;
	
	/**
	 * 根据套餐id设置关联(传入数据类型为关联VO)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:26:53
	 * @param Long packageId 套餐id
	 * @param List<OperationMediaPackagePermissionVO> mediaPackagePermissionVOs 关联关系
	 * @return List<OperationMediaPackagePermissionVO> 关联数组
	 */
	public List<OperationPackageMediaPermissionVO> addByVos(
			Long packageId,
			List<OperationPackageMediaPermissionVO> mediaPermissionVOs) throws Exception {
		List<OperationPackageMediaPermissionPO> permissionPOs = new ArrayList<OperationPackageMediaPermissionPO>();
		if (mediaPermissionVOs == null || mediaPermissionVOs.isEmpty()) return new ArrayList<OperationPackageMediaPermissionVO>();
		for (OperationPackageMediaPermissionVO permissionVO : mediaPermissionVOs) {
			OperationPackageMediaPermissionPO permissionPO = new OperationPackageMediaPermissionPO();
			permissionPO.setPackageId(packageId);
			permissionPO.setMimsName(permissionVO.getMimsName());
			permissionPO.setMimsId(permissionVO.getMimsId());
			permissionPO.setMimsType(permissionVO.getMimsType());
			permissionPO.setMimsUuid(permissionVO.getMimsUuid());
			permissionPO.setNum(permissionVO.getNum());
			permissionPOs.add(permissionPO);
		}
		mediaPackagePermissionDAO.save(permissionPOs);
		return OperationPackageMediaPermissionVO.getConverter(OperationPackageMediaPermissionVO.class).convert(permissionPOs, OperationPackageMediaPermissionVO.class);
	}
	
	/**
	 * 根据套餐id复制<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:59:42
	 * @param Long sourcePackageId 源套餐id
	 * @param Long destPackageId 目的套餐id
	 * @return List<OperationPackageMediaPermissionVO>
	 */
	public List<OperationPackageMediaPermissionVO> copeByPakcageId(Long sourcePackageId, Long destPackageId) throws Exception {
		List<OperationPackageMediaPermissionPO> permissionPOs = mediaPackagePermissionDAO.findByPackageId(sourcePackageId);
		return addByVos(destPackageId, OperationPackageMediaPermissionVO.getConverter(OperationPackageMediaPermissionVO.class).convert(permissionPOs, OperationPackageMediaPermissionVO.class));
	}
	
	/**
	 * 根据套餐id删除所有关联<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:29:20
	 * @param Long packageId 套餐id
	 */
	public void removeByPackageId(Long packageId) throws Exception {
		mediaPackagePermissionDAO.removeByPackageId(packageId);
	}
}
