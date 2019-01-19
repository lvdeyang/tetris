package com.sumavision.tetris.user;

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
	@Query(value = "from com.sumavision.tetris.user.UserPO user ORDER BY user.updateTime DESC")
	public Page<UserPO> findAllOrderByUpdateTimeDesc(Pageable page);
	
}
