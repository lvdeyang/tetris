package com.sumavision.tetris.mims.app.operation.mediaPackage;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = OperationPackagePO.class, idClass = Long.class)
public interface OperationPackageDAO extends BaseDAO<OperationPackagePO>{
	
	/**
	 * 根据套餐状态获取列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月17日 下午3:43:04
	 * @param OperationPackageStatus status
	 */
	public List<OperationPackagePO> findByGroupIdAndStatusOrderByUpdateTimeDesc(String groupId, OperationPackageStatus status);
	
	/**
	 * 根据用户id和套餐关联状态获取绑定的套餐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午4:51:24
	 * @param Long userId 用户id
	 * @param List<OperationPackageUserUseStatus> status 套餐关联状态
	 * @return List<OperationPackagePO> 套餐列表
	 */
	@Query(value = "SELECT package.* FROM MIMS_OPERATION_PACKAGE package "
			+ "LEFT JOIN MIMS_OPERATION_PACKAGE_USER_PERMISSION permission "
			+ "ON package.id = permission.package_id "
			+ "WHERE permission.user_id = ?1 "
			+ "AND permission.status IN ?2", nativeQuery = true)
	public List<OperationPackagePO> findByUserIdAndPermissionAndUseStatusIn(Long userId, List<String> status);
}
