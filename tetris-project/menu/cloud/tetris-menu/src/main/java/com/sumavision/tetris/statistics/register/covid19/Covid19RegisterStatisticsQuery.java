package com.sumavision.tetris.statistics.register.covid19;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class Covid19RegisterStatisticsQuery {

	@Autowired
	private Covid19RegisterStatisticsDAO covid19RegisterStatisticsDao;
	
	/**
	 * 分页查询登记信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月13日 下午4:20:35
	 * @param currentPage 当前页
	 * @param pageSize 每页数据量
	 * @return List<Covid19RegisterStatisticsVO> 登记列表
	 */
	public List<Covid19RegisterStatisticsVO> findAll(int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<Covid19RegisterStatisticsPO> pagedEntities = covid19RegisterStatisticsDao.findAll(page);
		List<Covid19RegisterStatisticsPO> entities = pagedEntities.getContent();
		if(entities!=null && entities.size()>0){
			return Covid19RegisterStatisticsVO.getConverter(Covid19RegisterStatisticsVO.class).convert(entities, Covid19RegisterStatisticsVO.class);
		}
		return null;
	}
	
}
