package com.sumavision.tetris.patrol.sign;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SignPO.class, idClass = Long.class)
public interface SignDAO extends BaseDAO<SignPO>{

	/**
	 * 查询某地址的签到<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午4:14:34
	 * @param Long addressId 地址id
	 * @return List<SignPO> 签到内容
	 */
	public List<SignPO> findByAddressId(Long addressId);
	
	/**
	 * 根据条件动态查询签到信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:06:24
	 * @param String nameExpression 签到用户姓名like表达式
	 * @param String addressNameExpression 地址名称like表达式
	 * @param Date beginTime 时间区域下限
	 * @param Date endTime 时间区域上限
	 * @return List<SignPO> 签到信息列表
	 */
	@Query(value = "SELECT SIGN.* FROM TETRIS_PATROL_SIGN SIGN "
			+ "LEFT JOIN TETRIS_PATROL_ADDRESS ADDRESS ON SIGN.ADDRESS_ID=ADDRESS.ID "
			+ "WHERE IF(?1 is null, true, SIGN.NAME like ?1) "
			+ "AND IF(?2 is null, true, ADDRESS.NAME like ?2) "
			+ "AND IF(?3 is null, true, SIGN.SIGN_TIME >= ?3) "
			+ "AND IF(?4 is null, true, SIGN.SIGN_TIME <= ?4) "
			+ "ORDER BY SIGN.SIGN_TIME DESC",
			nativeQuery = true)
	public List<SignPO> findByConditions(
			String nameExpression, 
			String addressNameExpression, 
			Date beginTime, 
			Date endTime);
	
	/**
	 * 根据条件动态分页查询签到信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:06:24
	 * @param String nameExpression 签到用户姓名like表达式
	 * @param String addressNameExpression 地址名称like表达式
	 * @param Date beginTime 时间区域下限
	 * @param Date endTime 时间区域上限
	 * @return List<SignPO> 签到信息列表
	 */
	@Query(value = "SELECT SIGN.* FROM TETRIS_PATROL_SIGN SIGN "
			+ "LEFT JOIN TETRIS_PATROL_ADDRESS ADDRESS ON SIGN.ADDRESS_ID=ADDRESS.ID "
			+ "WHERE IF(?1 is null, true, SIGN.NAME like ?1) "
			+ "AND IF(?2 is null, true, ADDRESS.NAME like ?2) "
			+ "AND IF(?3 is null, true, SIGN.SIGN_TIME >= ?3) "
			+ "AND IF(?4 is null, true, SIGN.SIGN_TIME <= ?4) "
			+ "ORDER BY SIGN.SIGN_TIME DESC"
			+ "\n#pageable\n",
			countQuery = "SELECT COUNT(SIGN.ID) FROM TETRIS_PATROL_SIGN SIGN "
					+ "LEFT JOIN TETRIS_PATROL_ADDRESS ADDRESS ON SIGN.ADDRESS_ID=ADDRESS.ID "
					+ "WHERE IF(?1 is null, true, SIGN.NAME like ?1) "
					+ "AND IF(?2 is null, true, ADDRESS.NAME like ?2) "
					+ "AND IF(?3 is null, true, SIGN.SIGN_TIME >= ?3) "
					+ "AND IF(?4 is null, true, SIGN.SIGN_TIME <= ?4) ",
			nativeQuery = true)
	public Page<SignPO> findByConditions(
			String nameExpression, 
			String addressNameExpression, 
			Date beginTime, 
			Date endTime,
			Pageable page);
	
}
