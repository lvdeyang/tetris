package com.sumavision.tetris.mims.app.operation.mediaPackage.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission.OperationPackageStreamPermissionQuery;
import com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission.OperationPackageStreamPermissionVO;

@Component
public class OperationPackageDetailQuery {
	@Autowired
	private OperationPackageMediaPermissionQuery mediaPermissionQuery;
	
	@Autowired
	private OperationPackageMediaTypePermissionQuery mediaTypePermissionQuery;
	
	@Autowired
	private OperationPackageStreamPermissionQuery streamPermissionQuery;
	
	/**
	 * 根据套餐id获取详情<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午9:54:06
	 * @param Long packageId 套餐id
	 * @return OperationPackageDetailVO 套餐详情
	 */
	public OperationPackageDetailVO queryByPackageId(Long packageId) throws Exception {
		OperationPackageDetailVO detailVO = new OperationPackageDetailVO();
		if (packageId == null) return detailVO;
		List<OperationPackageMediaPermissionVO> mediaPermissionVOs = mediaPermissionQuery.queryByPackageId(packageId);
		List<OperationPackageMediaTypePermissionVO> mediaTypePermissionVOs = mediaTypePermissionQuery.queryByPackageId(packageId);
		OperationPackageStreamPermissionVO streamPermissionVO = streamPermissionQuery.queryByPackageId(packageId);
		detailVO.setPackageId(packageId)
		.setMediaPermissions(mediaPermissionVOs)
		.setMediaTypePermissions(mediaTypePermissionVOs)
		.setStreamPermission(streamPermissionVO);
		return detailVO;
	}
}
