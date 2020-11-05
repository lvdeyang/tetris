package com.sumavision.tetris.easy.process.access.point;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

/**
 * 接入点操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月19日 上午9:23:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccessPointService {

	@Autowired
	private AccessPointDAO accessPointDao;
	
	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	@Autowired
	private JointConstraintExpressionDAO jointConstraintExpressionDao;
	
	/**
	 * 删除接入点<br/>
	 * <p>
	 * 	1.删除接入点参数
	 *  2.删除接入点约束
	 *  3.删除接入点
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午9:24:18
	 * @param AccessPointPO accessPoint 接入点
	 */
	public void delete(AccessPointPO accessPoint) throws Exception{
		
		List<Long> condition = new ArrayListWrapper<Long>().add(accessPoint.getId()).getList();
		
		List<AccessPointParamPO> params = accessPointParamDao.findByAccessPointIdIn(condition);
		if(params!=null && params.size()>0){
			accessPointParamDao.deleteInBatch(params);
		}
		
		List<JointConstraintExpressionPO> constraints = jointConstraintExpressionDao.findByAccessPointIdIn(condition);
		if(constraints!=null && constraints.size()>0){
			jointConstraintExpressionDao.deleteInBatch(constraints);
		}
		
		accessPointDao.delete(accessPoint);
		
	}
	
}
