package com.sumavision.tetris.easy.process.access.point;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class AccessPointParamQuery {

	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	/**
	 * 分页查询接入点下的参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午10:31:35
	 * @param Long accessPointId 接入点id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<AccessPointParamPO> 参数列表
	 */
	public List<AccessPointParamPO> findByAccessPointId(Long accessPointId, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<AccessPointParamPO> params = accessPointParamDao.findByAccessPointId(accessPointId, page);
		return params.getContent();
	}
	
}
