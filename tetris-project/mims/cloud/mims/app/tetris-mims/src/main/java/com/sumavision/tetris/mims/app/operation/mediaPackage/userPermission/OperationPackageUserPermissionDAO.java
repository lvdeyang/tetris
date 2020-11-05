package com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = OperationPackageUserPermissionPO.class, idClass = Long.class)
public interface OperationPackageUserPermissionDAO extends BaseDAO<OperationPackageUserPermissionPO>{
	public List<OperationPackageUserPermissionPO> findByUserId(Long userId);
	
	public List<OperationPackageUserPermissionPO> findByUserIdAndStatus(Long userId, OperationPackageUserUseStatus status);
	
	public List<OperationPackageUserPermissionPO> findByPackageId(Long packageId);
	
	public List<OperationPackageUserPermissionPO> findByUserIdAndPackageIdAndStatusIn(Long userId, Long packageId, List<String> status);
	
	public List<OperationPackageUserPermissionPO> findByStatisticId(Long statisticId);
	
	@Query(value = "SELECT * FROM MIMS_OPERATION_PACKAGE_USER_PERMISSION permission "
			+ "WHERE permission.user_id = ?1 "
			+ "AND permission.status = ?2", nativeQuery = true)
	public List<OperationPackageUserPermissionPO> findUsingByUserId(Long userId, OperationPackageUserUseStatus status);
	
	@Query(value = "SELECT * FROM MIMS_OPERATION_PACKAGE_USER_PERMISSION WHERE user_id = ?1 AND status in ?2", nativeQuery = true)
	public List<OperationPackageUserPermissionPO> findByUserIdAndStatusIn(Long userId, List<String> status);
}
