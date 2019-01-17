package com.sumavision.tetris.easy.process.access.service.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sumavision.tetris.easy.process.access.point.AccessPointDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamPO;
import com.sumavision.tetris.easy.process.access.point.JointConstraintExpressionDAO;
import com.sumavision.tetris.easy.process.access.point.JointConstraintExpressionPO;

@Service
@Transactional(rollbackFor = Exception.class)
public class RestService {

	@Autowired
	private RestServiceDAO restServiceDao;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	@Autowired
	private JointConstraintExpressionDAO jointConstraintExpressionDao;
	
	/**
	 * 删除rest服务<br/>
	 * <p>
	 * 	删除rest服务<br/>
	 *  删除rest服务接入点<br/>
	 *  删除rest服务接入点参数<br/>
	 *  删除rest服务接入点约束<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午1:31:18
	 * @param RestServicePO service rest服务
	 */
	public void delete(RestServicePO service) throws Exception{
		
		List<AccessPointPO> accessPoints = accessPointDao.findByServiceId(service.getId());
		
		Set<Long> accessPointIds = new HashSet<Long>();
		if(accessPoints!=null && accessPoints.size()>0){
			for(AccessPointPO accessPoint:accessPoints){
				accessPointIds.add(accessPoint.getId());
			}
			
			List<AccessPointParamPO> params = accessPointParamDao.findByAccessPointIdIn(accessPointIds);
			if(params!=null && params.size()>0) accessPointParamDao.deleteInBatch(params);
			
			List<JointConstraintExpressionPO> constraints = jointConstraintExpressionDao.findByAccessPointIdIn(accessPointIds);
			if(constraints!=null && constraints.size()>0) jointConstraintExpressionDao.deleteInBatch(constraints);
		
			accessPointDao.deleteInBatch(accessPoints);
		}
		
		restServiceDao.delete(service);
		
	}
	
}
