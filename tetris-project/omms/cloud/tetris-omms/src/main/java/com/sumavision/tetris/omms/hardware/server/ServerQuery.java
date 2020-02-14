package com.sumavision.tetris.omms.hardware.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ServerQuery {

	@Autowired
	private ServerDAO serverDao;
	
	/**
	 * 分页查询服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:48:03
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<ServerVO> 服务器列表
	 */
	public List<ServerVO> load(
			int currentPage, 
			int pageSize) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ServerPO> pagedEntities = serverDao.findAll(page);
		List<ServerPO> entities = pagedEntities.getContent();
		if(entities!=null && entities.size()>0){
			return ServerVO.getConverter(ServerVO.class).convert(entities, ServerVO.class);
		}
		return null;
	}
	
}
