package com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationPackageMediaTypePermissionService {
	@Autowired
	private OperationPackageMediaTypePermissionDAO mediaTypePermissionDAO;
	
	/**
	 * 根据套餐id设置关联(传入数据类型为关联VO)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:26:53
	 * @param Long packageId 套餐id
	 * @param List<OperationMediaTypePackagePermissionVO> mediaTypePackagePermissionVOs 关联关系
	 * @return List<OperationMediaTypePackagePermissionVO> 关联数组
	 */
	public List<OperationPackageMediaTypePermissionVO> addByVos(
			Long packageId,
			List<OperationPackageMediaTypePermissionVO> mediaTypePermissionVOs) throws Exception {
		List<OperationPackageMediaTypePermissionPO> permissionPOs = new ArrayList<OperationPackageMediaTypePermissionPO>();
		if (mediaTypePermissionVOs == null || mediaTypePermissionVOs.isEmpty()) return new ArrayList<OperationPackageMediaTypePermissionVO>();
		for (OperationPackageMediaTypePermissionVO permissionVO : mediaTypePermissionVOs) {
			OperationPackageMediaTypePermissionPO permissionPO = new OperationPackageMediaTypePermissionPO();
			permissionPO.setPackageId(packageId);
			permissionPO.setMediaType(permissionVO.getMediaType());
			permissionPO.setNum(permissionVO.getNum());
			permissionPOs.add(permissionPO);
		}
		mediaTypePermissionDAO.save(permissionPOs);
		return OperationPackageMediaTypePermissionVO.getConverter(OperationPackageMediaTypePermissionVO.class).convert(permissionPOs, OperationPackageMediaTypePermissionVO.class);
	}
	
	/**
	 * 根据套餐id复制<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:59:42
	 * @param Long sourcePackageId 源套餐id
	 * @param Long destPackageId 目的套餐id
	 * @return List<OperationPackageMediaTypePermissionVO>
	 */
	public List<OperationPackageMediaTypePermissionVO> copeByPakcageId(Long sourcePackageId, Long destPackageId) throws Exception {
		List<OperationPackageMediaTypePermissionPO> permissionPOs = mediaTypePermissionDAO.findByPackageId(sourcePackageId);
		return addByVos(destPackageId, OperationPackageMediaTypePermissionVO.getConverter(OperationPackageMediaTypePermissionVO.class).convert(permissionPOs, OperationPackageMediaTypePermissionVO.class));
	}
	
	/**
	 * 根据套餐id删除所有关联<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:29:20
	 * @param Long packageId 套餐id
	 */
	public void removeByPackageId(Long packageId) throws Exception {
		mediaTypePermissionDAO.removeByPackageId(packageId);
	}
}
