package com.sumavision.tetris.mims.app.operation.accessRecord;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.mims.app.operation.mediaPackage.OperationPackagePermissionType;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = OperationRecordPO.class, idClass = Long.class)
public interface OperationRecordDAO extends BaseDAO<OperationRecordPO>{
	public List<OperationRecordPO> findByUserId(Long userId);
	
	public List<OperationRecordPO> findByUserIdAndMimsType(Long userId, String mimsType);
	
	public List<OperationRecordPO> findByUserIdAndMimsTypeAndMimsId(Long userId, String mimsType, String mimsId);
	
	public List<OperationRecordPO> findByUserIdAndMimsUuid(Long userId, String mimsUuid);
	
	public List<OperationRecordPO> findByPermissionId(Long permissionId);
	
	public List<OperationRecordPO> findByPermissionIdAndPermissionType(Long permissionId, OperationPackagePermissionType permissionType);
	
	public List<OperationRecordPO> findByPermissionIdAndMimsUuidAndPermissionType(Long permissionId, String mimsUuid, OperationPackagePermissionType permissionType);
	
	public List<OperationRecordPO> findByPermissionIdAndMimsTypeAndPermissionType(Long permissionId, String mediaType, OperationPackagePermissionType permissionType);
	
	public void deleteByUserId(Long userId);
}
