package com.sumavision.tetris.mims.app.operation.mediaPackage.detail;

import java.util.List;

import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaPermission.OperationPackageMediaPermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.mediaTypePermission.OperationPackageMediaTypePermissionVO;
import com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission.OperationPackageStreamPermissionVO;

public class OperationPackageDetailVO {
	private Long packageId;
	
	private List<OperationPackageMediaPermissionVO> mediaPermissions;
	
	private List<OperationPackageMediaTypePermissionVO> mediaTypePermissions;
	
	private OperationPackageStreamPermissionVO streamPermission;
	
	public Long getPackageId() {
		return packageId;
	}

	public OperationPackageDetailVO setPackageId(Long packageId) {
		this.packageId = packageId;
		return this;
	}

	public List<OperationPackageMediaPermissionVO> getMediaPermissions() {
		return mediaPermissions;
	}

	public OperationPackageDetailVO setMediaPermissions(List<OperationPackageMediaPermissionVO> mediaPermissions) {
		this.mediaPermissions = mediaPermissions;
		return this;
	}

	public List<OperationPackageMediaTypePermissionVO> getMediaTypePermissions() {
		return mediaTypePermissions;
	}

	public OperationPackageDetailVO setMediaTypePermissions(List<OperationPackageMediaTypePermissionVO> mediaTypePermissions) {
		this.mediaTypePermissions = mediaTypePermissions;
		return this;
	}

	public OperationPackageStreamPermissionVO getStreamPermission() {
		return streamPermission;
	}

	public OperationPackageDetailVO setStreamPermission(OperationPackageStreamPermissionVO streamPermission) {
		this.streamPermission = streamPermission;
		return this;
	}
}
