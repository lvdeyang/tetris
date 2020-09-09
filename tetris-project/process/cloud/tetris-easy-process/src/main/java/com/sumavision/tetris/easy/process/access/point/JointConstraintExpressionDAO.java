package com.sumavision.tetris.easy.process.access.point;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = JointConstraintExpressionPO.class, idClass = Long.class)
public interface JointConstraintExpressionDAO extends BaseDAO<JointConstraintExpressionPO>{

	/**
	 * 根据接入点（批量）获取接入点约束<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午1:23:07
	 * @param Collection<Long> accessPointIds 接入点id列表
	 * @return List<JointConstraintExpressionPO> 约束列表
	 */
	public List<JointConstraintExpressionPO> findByAccessPointIdIn(Collection<Long> accessPointIds);
	
	/**
	 * 统计接入点下联合约束的数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午4:34:14
	 * @param Long accessPointId 接入点id
	 * @return int 联合约束数量
	 */
	public int countByAccessPointId(Long accessPointId);
	
	/**
	 * 分页查询接入点下的联合约束<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午4:38:44
	 * @param Long accessPointId 接入点id
	 * @param Pageable page 分页信息
	 * @return Page<JointConstraintExpressionPO> 约束列表
	 */
	public Page<JointConstraintExpressionPO> findByAccessPointId(Long accessPointId, Pageable page);
	
	/**
	 * 查询一个参数的约束引用<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午2:56:24
	 * @param String primaryKeyReg 参数主键匹配字符串
	 * @return List<JointConstraintExpressionPO> 约束列表
	 */
	@Query(value = "SELECT joint_constraint.id, joint_constraint.uuid, joint_constraint.update_time, joint_constraint.name, joint_constraint.expression, joint_constraint.access_point_id FROM tetris_process_joint_constraint joint_constraint WHERE joint_constraint.expression LIKE ?1", nativeQuery = true)
	public List<JointConstraintExpressionPO> findByParamPrimaryKey(String primaryKeyReg);
}
