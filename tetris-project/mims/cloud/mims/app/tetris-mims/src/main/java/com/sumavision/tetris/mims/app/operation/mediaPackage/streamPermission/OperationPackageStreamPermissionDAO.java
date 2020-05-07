package com.sumavision.tetris.mims.app.operation.mediaPackage.streamPermission;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = OperationPackageStreamPermissionPO.class, idClass = Long.class)
public interface OperationPackageStreamPermissionDAO extends BaseDAO<OperationPackageStreamPermissionPO>{
	
	/**
	 * 根据套餐id获取关联<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 下午2:55:31
	 * @param Long packageId 套餐id
	 * @return OperationPackageStreamPermissionPO
	 */
	public OperationPackageStreamPermissionPO findByPackageId(Long packageId);
	
	/**
	 * 根据套餐id删除关联<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午10:17:23
	 * @param Long packageId 套餐id
	 */
	@Modifying
	@Query(value = "delete from MIMS_OPERATION_PACKAGE_STREAM_PERMISSION where package_id=?1 ", nativeQuery = true)
	public void removeByPackageId(Long packageId);
}
