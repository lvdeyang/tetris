package com.sumavision.tetris.user;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月18日 下午4:55:22
 */
@RepositoryDefinition(domainClass = UserPO.class, idClass = Long.class)
public interface UserDAO extends BaseDAO<UserPO>{
	
	/**
	 * 分页查询用户列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 上午10:14:26
	 * @return Page<UserPO> 用户列表
	 */
	@Query(value = "from com.sumavision.tetris.user.UserPO user order by user.updateTime desc")
	public Page<UserPO> findAllOrderByUpdateTimeDesc(Pageable page);
	
	/**
	 * 分页查询用户列表（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午6:29:45
	 * @param Collection<Long> except 例外用户id列表
	 * @param Pageable page 分页信息
	 * @return Page<UserPO> 用户列表
	 */
	@Query(value = "from com.sumavision.tetris.user.UserPO user where id not in ?1 order by user.updateTime desc")
	public Page<UserPO> findWithExceptOrderByUpdateTimeDesc(Collection<Long> except, Pageable page);
	
	/**
	 * 统计用户数量（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午6:34:17
	 * @param Collection<Long> except 例外用户id列表
	 * @return int 用户数量
	 */
	@Query(value = "select count(user.id) from com.sumavision.tetris.user.UserPO user where id not in ?1")
	public int countWithExcept(Collection<Long> except);
	
	/**
	 * 获取公司内的用户（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 下午3:05:24
	 * @param Long companyId 公司id
	 * @param Collection<Long> except 例外用户id列表
	 * @return 用户列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.id NOT IN ?2", nativeQuery = true)
	public List<UserPO> findByCompanyIdAndExcept(Long companyId, Collection<Long> except);
	
	/**
	 * 获取公司内用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 下午3:06:50
	 * @param Long companyId 公司id
	 * @return List<UserPO> 用户列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1", nativeQuery = true)
	public List<UserPO> findByCompanyId(Long companyId);
	
	/**
	 * 分页查询公司下的用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:10:01
	 * @param Long companyId 公司id
	 * @param Pageable page 分页信息
	 * @return Page<UserPO> 用户列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 \n#pageable\n", nativeQuery = true)
	public Page<UserPO> findByCompanyId(Long companyId, Pageable page);
	
	/**
	 * 查询公司下的用户数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:14:16
	 * @param Long companyId 公司id
	 * @return int 用户数量
	 */
	@Query(value = "SELECT count(user.id) from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1", nativeQuery = true)
	public int countByCompanyId(Long companyId);
	
	/**
	 * 分页查询公司下的用户（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:10:01
	 * @param Long companyId 公司id
	 * @param Collection<Long> except 例外用户id列表
	 * @param Pageable page 分页信息
	 * @return Page<UserPO> 用户列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.id NOT IN ?2 \n#pageable\n", nativeQuery = true)
	public Page<UserPO> findByCompanyIdWithExcept(Long companyId, Collection<Long> except, Pageable page);
	
	/**
	 * 查询公司下的用户数量（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:14:16
	 * @param Long companyId 公司id
	 * @param Collection<Long> except 例外用户id列表
	 * @return int 用户数量
	 */
	@Query(value = "SELECT count(user.id) from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.id NOT IN ?2", nativeQuery = true)
	public int countByCompanyIdWithExcept(Long companyId, Collection<Long> except);
	
}
