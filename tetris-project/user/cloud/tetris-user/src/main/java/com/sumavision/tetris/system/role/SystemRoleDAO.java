package com.sumavision.tetris.system.role;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SystemRolePO.class, idClass = Long.class)
public interface SystemRoleDAO extends BaseDAO<SystemRolePO>{

	/**
	 * 查询所有的系统角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 上午11:27:24
	 * @param Collection<Long> ids 例外角色id列表
	 * @return List<SystemRolePO> 系统角色列表
	 */
	public List<SystemRolePO> findByIdNotIn(Collection<Long> ids);
	
	/**
	 * 分页查询用户绑定的系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月21日 上午11:59:48
	 * @param Long userId 用户id
	 * @param Pageable page 分页信息
	 * @return Page<SystemRolePO> 系统角色列表
	 */
	@Query(value = "SELECT role.* FROM tetris_user_system_role role LEFT JOIN tetris_user_system_role_permission permission ON role.id=permission.role_id WHERE permission.user_id=?1 \n#pageable\n", nativeQuery = true)
	public Page<SystemRolePO> findByUserId(Long userId, Pageable page);
	
}
