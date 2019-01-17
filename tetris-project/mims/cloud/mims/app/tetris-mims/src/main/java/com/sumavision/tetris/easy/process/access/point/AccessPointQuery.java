package com.sumavision.tetris.easy.process.access.point;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 接入点查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月18日 下午3:33:03
 */
@Component
public class AccessPointQuery {

	@Autowired
	private AccessPointDAO accessPointDao;
	
	/**
	 * 分页查询服务下的接入点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午3:31:19
	 * @param Long serviceId 服务id
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return List<AccessPointPO> 接入点列表
	 */
	public List<AccessPointPO> findByServiceId(Long serviceId, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<AccessPointPO> accessPoints = accessPointDao.findByServiceId(serviceId, page);
		return accessPoints.getContent();
	}
	
}
