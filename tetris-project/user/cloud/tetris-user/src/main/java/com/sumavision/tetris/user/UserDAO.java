package com.sumavision.tetris.user;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

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
	 * 根据id查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午3:18:07
	 * @param Collection<Long> ids 用户id列表
	 * @return List<UserPO> 用户列表
	 */
	public List<UserPO> findByIdIn(Collection<Long> ids);
	
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
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.id NOT IN ?2 \n#pageable\n", 
		   countQuery = "SELECT count(user.id) from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.id NOT IN ?2 \n#pageable\n",
		   nativeQuery = true)
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
	
	/**
	 * 根据用户名查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 上午10:52:46
	 * @param String username 用户名
	 * @return UserPO 用户
	 */
	public UserPO findByUsername(String username);
	
	/**
	 * 根据用户名和自动生成类型查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午1:03:54
	 * @param String username 用户名
	 * @param boolean autoGeneration 是否自动生成
	 * @return UserPO 用户
	 */
	public UserPO findByUsernameAndAutoGeneration(String username, boolean autoGeneration);
	
	/**
	 * 根据token查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午11:11:18
	 * @param String token 用户登录的token
	 * @return UserPO 用户
	 */
	//public UserPO findByToken(String token);
	
	/**
	 * 根据手机号查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 下午1:20:06
	 * @param String mobile 手机号
	 * @return UserPO 用户
	 */
	public UserPO findByMobile(String mobile);
	
	/**
	 * 根据手机号查询用户（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 下午1:20:06
	 * @param String mobile 手机号
	 * @param Long except 例外用户id
	 * @return UserPO 用户
	 */
	@Query(value = "from com.sumavision.tetris.user.UserPO user where user.mobile=?1 and user.id<>?2")
	public UserPO findByMobileWithExcept(String mobile, Long except);
	
	/**
	 * 根据邮箱查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 下午1:21:17
	 * @param String mail 邮箱
	 * @return UserPO 用户
	 */
	public UserPO findByMail(String mail);
	
	/**
	 * 根据邮箱查询用户（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 下午1:21:17
	 * @param String mail 邮箱
	 * @param Long except 例外用户id
	 * @return UserPO 用户
	 */
	@Query(value = "from com.sumavision.tetris.user.UserPO user where user.mail=?1 and user.id<>?2")
	public UserPO findByMailWithExcept(String mail, Long except);
	
	/**
	 * 分页查询隶属角色下的用户<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 上午11:10:01
	 * @param Long companyId 公司id
	 * @param Pageable page 分页信息
	 * @return Page<UserPO> 用户列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_subordinate_role_permission permission ON user.id=permission.user_id WHERE permission.role_id=?1 \n#pageable\n", nativeQuery = true)
	public Page<UserPO> findByRoleId(Long roleId, Pageable page);
	
	/**
	 * 查询角色关联的用户数量（带例外）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 上午11:14:16
	 * @param Long roleId 角色id
	 * @param Collection<Long> except 例外用户id列表
	 * @return int 用户数量
	 */
	@Query(value = "SELECT count(user.id) from tetris_user user LEFT JOIN tetris_subordinate_role_permission permission ON user.id=permission.user_id WHERE permission.role_id=?1 AND user.id NOT IN ?2", nativeQuery = true)
	public int countByRoleIdWithExcept(Long roleId, Collection<Long> except);
	
	/**
	 * 分页查询角色关联的用户（带例外）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 上午11:10:01
	 * @param Long roleId 角色id
	 * @param Collection<Long> except 例外用户id列表
	 * @param Pageable page 分页信息
	 * @return Page<UserPO> 用户列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_subordinate_role_permission permission ON user.id=permission.user_id WHERE permission.role_id=?1 AND user.id NOT IN ?2 \n#pageable\n", nativeQuery = true)
	public Page<UserPO> findByRoleIdWithExcept(Long role, Collection<Long> except, Pageable page);
	
	/**
	 * 查询公司下类型用户<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:09:44
	 * @param Long companyId 公司id
	 * @param UserClassify classify 用户类型
	 * @return List<UserPO> 用户列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.classify=?2 \n#pageable\n", nativeQuery = true)
	public List<UserPO> findByCompanyIdAndClassfy(Long companyId, String classify);
	
	/**
	 * 查询公司下类型用户（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:09:44
	 * @param Long companyId 公司id
	 * @param Collection<Long> except
	 * @param UserClassify classify 用户类型
	 * @return List<UserPO> 用户列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.id NOT IN ?2 AND user.classify=?3", nativeQuery = true)
	public List<UserPO> findByCompanyIdWithExceptAndClassfy(Long companyId, Collection<Long> except, String classify);
	
	/**
	 * 根据用户名模糊查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 上午10:33:20
	 * @param String userName 用户名
	 * @return List<UserPO> 用户列表
	 */
	public List<UserPO> findByUsernameLike(String userName);
	
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.username LIKE ?2 \n#pageable\n", nativeQuery = true)
	public Page<UserPO> findByCompanyIdAndLikeName(Long companyId, String userName, Pageable page);
	
	/**
	 * 根据用户名模糊查询同一公司下的用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午10:03:20
	 * @param Long companyId 公司id
	 * @param String userName 用户名
	 * @return List<UserPO> 用户列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.nickname LIKE ?2", nativeQuery = true)
	public List<UserPO> findByCompanyIdAndLikeNickName(Long companyId, String userName);
	
	/**
	 * 根据用户昵称查询用户--同一公司下<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:13:01
	 * @param Long companyId 公司id
	 * @param String userName 用户昵称
	 * @return UserPO
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.nickname = ?2", nativeQuery = true)
	public UserPO findByCompanyIdAndNickName(Long companyId, String userName);
	
	/**
	 * 根据用户号码查询用户--同一公司下<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午7:00:50
	 * @param Long companyId 公司id
	 * @param String userno 用户号码
	 * @return UserPO 用户信息
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.userno = ?2", nativeQuery = true)
	public UserPO findByCompanyIdAndUserno(Long companyId, String userno);
	
	/**
	 * 根据用户号码查询用户--同一公司下<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午7:00:50
	 * @param Long companyId 公司id
	 * @param Collection<String> userno 用户号码列表
	 * @return List<UserPO> 用户信息列表
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.userno IN ?2", nativeQuery = true)
	public List<UserPO> findByCompanyIdAndUsernoIn(Long companyId, Collection<String> usernos);
	
	/**
	 * 根据角色id查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月3日 上午11:05:53
	 * @param Long roleId 角色id
	 * @return List<UserPO>
	 */
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_user_system_role_permission permission ON user.id=permission.user_id WHERE permission.role_id=?1", nativeQuery = true)
	public List<UserPO> findByRoleId(Long roleId);
	
	@Query(value = "SELECT user.* from tetris_user user LEFT JOIN tetris_company_user_permission permission ON user.id=permission.user_id WHERE permission.company_id=?1 AND user.username LIKE ?2 AND user.id NOT IN ?3 \n#pageable\n", nativeQuery = true)
	public Page<UserPO> findByCompanyIdAndLikeNameWithExcept(Long companyId, String userName, Collection<Long> except, Pageable page);

	public List<UserPO> findByNicknameIn(Collection<String> nicknames);
	
	/**
	 * 根据用户名查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月9日 下午6:52:50
	 * @param Collection<String> usernames 用户名列表
	 * @return List<UserPO> 用户列表
	 */
	public List<UserPO> findByUsernameIn(Collection<String> usernames);
	
	/**
	 * 根据用户号码查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 上午10:30:28
	 * @param String userno 用户号码
	 * @return UserPO
	 */
	public UserPO findByUserno(String userno);
	
	/**
	 * 根据用户号码批量查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 上午10:30:28
	 * @param Collection<String> usernos 用户号码
	 * @return List<UserPO>
	 */
	public List<UserPO> findByUsernoIn(Collection<String> usernos);
	
	/**
	 * 根据用户ids和用户类型删除用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午2:13:55
	 * @param Collection<Long> userIds 用户id列表
	 * @param UserClassify classify 用户类型
	 */
	@Transactional
	public void deleteByIdInAndClassify(Collection<Long> userIds, UserClassify classify);
	
	/**
	 * 删除用户类型所有用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午5:22:14
	 * @param UserClassify classify 用户类型
	 */
	@Transactional
	public void deleteByClassify(UserClassify classify);
	
}
