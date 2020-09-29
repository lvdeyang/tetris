package com.sumavision.tetris.patrol.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AddressPO.class, idClass = Long.class)
public interface AddressDAO extends BaseDAO<AddressPO>{

	/**
	 * 根据名称动态查询地址<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午5:32:51
	 * @param String nameExpression 名称like表达式
	 * @param Pageable page 分页信息
	 * @return Page<AddressPO> 地址列表
	 */
	@Query(value = "SELECT * FROM TETRIS_PATROL_ADDRESS WHERE IF(?1 is null, true, NAME like ?1) \n#pageable\n",
			countQuery = "SELECT count(*) FROM TETRIS_PATROL_ADDRESS WHERE IF(?1 is null, true, NAME like ?1)",
			nativeQuery = true)
	public Page<AddressPO> findByConditions(String nameExpression, Pageable page);
	
}
