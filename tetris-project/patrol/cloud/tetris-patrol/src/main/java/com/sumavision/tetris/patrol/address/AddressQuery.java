package com.sumavision.tetris.patrol.address;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.patrol.config.server.ServerProps;

@Component
public class AddressQuery {

	@Autowired
	private AddressDAO addressDao;
	
	@Autowired
	private ServerProps serverProps;
	
	/**
	 * 根据名城动态分页查询地址信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午5:51:58
	 * @param String name 地址名称
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 数据总量
	 * @return List<AddressVO> rows 地址列表
	 */
	public Map<String, Object> load(String name, int currentPage, int pageSize) throws Exception{
		Page<AddressPO> pagedEntities = findByConditions(name, currentPage, pageSize);
		List<AddressVO> rows = null;
		long total = 0l;
		if(pagedEntities != null){
			rows = AddressVO.getConverter(AddressVO.class).convert(pagedEntities.getContent(), AddressVO.class);
			total = pagedEntities.getTotalElements();
			for(AddressVO row:rows){
				row.setUrl(new StringBufferWrapper().append("http://")
						                            //.append(serverProps.getIp())
													.append("124.126.224.49")
						                            .append(":")
						                            .append(serverProps.getPort())
						                            .append("/sign/sign/page/")
						                            .append(row.getUuid())
						                            .toString());
			}
		}
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 根据名城动态分页查询地址<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午5:43:38
	 * @param String name 地址名
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return Page<AddressPO> 地址列表
	 */
	public Page<AddressPO> findByConditions(String name, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		String nameExpretion = null;
		if(name != null){
			nameExpretion = new StringBufferWrapper().append("%").append(name).append("%").toString();
		}
		return addressDao.findByConditions(nameExpretion, page);
	}
	
}
