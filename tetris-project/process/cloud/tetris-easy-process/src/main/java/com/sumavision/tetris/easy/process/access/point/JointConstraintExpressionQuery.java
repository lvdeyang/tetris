package com.sumavision.tetris.easy.process.access.point;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class JointConstraintExpressionQuery {

	@Autowired
	private JointConstraintExpressionDAO jointConstraintExpressionDao;
	
	/**
	 * 分页查询接入点下的联合约束<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午5:00:40
	 * @param Long accessPointId 接入点id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<JointConstraintExpressionPO> 约束列表
	 */
	public List<JointConstraintExpressionPO> findByAccessPointId(Long accessPointId, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<JointConstraintExpressionPO> constraints = jointConstraintExpressionDao.findByAccessPointId(accessPointId, page);
		return constraints.getContent();
	}
	
	/**
	 * 查询一个参数的约束引用<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午3:00:26
	 * @param String primaryKey 参数主键
	 * @return List<JointConstraintExpressionPO> 约束列表
	 */
	public List<JointConstraintExpressionPO> findByParamPrimaryKey(String primaryKey) throws Exception{
		String primaryKeyReg = new StringBufferWrapper().append("%")
														.append(primaryKey)
														.append("%")
														.toString();
		return jointConstraintExpressionDao.findByParamPrimaryKey(primaryKeyReg);
	}
	
}
