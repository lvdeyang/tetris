package com.sumavision.tetris.mims.app.operation.mediaPackage.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageService;
import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackageVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionService;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionService;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission.OperationPackageStreamPermissionService;
import com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission.OperationPackageStreamPermissionVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class OperationPackageDetailService {
	@Autowired
	private OperationPackageService operationPackageService;
	
	@Autowired
	private OperationPackageMediaPermissionService mediaPermissionService;
	
	@Autowired
	private OperationPackageMediaTypePermissionService mediaTypePermissionService;
	
	@Autowired
	private OperationPackageStreamPermissionService streamPermissionService;
	
	/**
	 * 编辑套餐绑定信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:05:07
	 * @param Long packageId 套餐id
	 * @return OperationPackageDetailVO 套餐信息
	 */
	public OperationPackageDetailVO setByPackage(
			Long packageId,
			List<OperationPackageMediaPermissionVO> mediaPermissionVOs,
			List<OperationPackageMediaTypePermissionVO> mediaTypePermissionVOs,
			OperationPackageStreamPermissionVO streamPermissionVO) throws Exception{
		OperationPackageDetailVO detailVO = new OperationPackageDetailVO();
		Long setPackageId;
		if (!operationPackageService.packageUsed(packageId)) {
			mediaPermissionService.removeByPackageId(packageId);
			mediaTypePermissionService.removeByPackageId(packageId);
			streamPermissionService.removeByPackageId(packageId);
			setPackageId = packageId;
		} else {
			OperationPackageVO newPackage = operationPackageService.copy(packageId);
			operationPackageService.setInvalid(packageId);
			setPackageId = newPackage.getId();
		}
		List<OperationPackageMediaPermissionVO> newMediaPackagePermissionVOs =
				mediaPermissionService.addByVos(setPackageId, mediaPermissionVOs);
		List<OperationPackageMediaTypePermissionVO> newMediaTypePackagePermissionVOs = 
				mediaTypePermissionService.addByVos(setPackageId, mediaTypePermissionVOs);
		OperationPackageStreamPermissionVO newStreamPermissionVO = 
				streamPermissionService.addByVos(setPackageId, streamPermissionVO);
		detailVO.setPackageId(setPackageId)
		.setMediaPermissions(newMediaPackagePermissionVOs)
		.setMediaTypePermissions(newMediaTypePackagePermissionVOs)
		.setStreamPermission(newStreamPermissionVO);
		
		return detailVO;
	}
	
	/**
	 * 根据套餐id复制<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:59:42
	 * @param Long sourcePackageId 源套餐id
	 * @param Long destPackageId 目的套餐id
	 * @return OperationPackageDetailVO
	 */
	public OperationPackageDetailVO copeByPakcageId(Long sourcePackageId, Long destPackageId) throws Exception {
		List<OperationPackageMediaPermissionVO> mediaPermissionVOs = mediaPermissionService.copeByPakcageId(sourcePackageId, destPackageId);
		List<OperationPackageMediaTypePermissionVO> mediaTypePermissionVOs = mediaTypePermissionService.copeByPakcageId(sourcePackageId, destPackageId);
		OperationPackageStreamPermissionVO streamPermissionVO = streamPermissionService.copeByPakcageId(sourcePackageId, destPackageId);
		return new OperationPackageDetailVO()
				.setMediaPermissions(mediaPermissionVOs)
				.setMediaTypePermissions(mediaTypePermissionVOs)
				.setStreamPermission(streamPermissionVO);
	}
}
