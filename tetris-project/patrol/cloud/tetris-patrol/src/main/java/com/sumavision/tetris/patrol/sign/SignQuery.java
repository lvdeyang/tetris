package com.sumavision.tetris.patrol.sign;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.patrol.address.AddressDAO;
import com.sumavision.tetris.patrol.address.AddressPO;

@Component
public class SignQuery {

	@Autowired
	private SignDAO signDao;
	
	@Autowired
	private AddressDAO addressDao;
	
	/**
	 * 根据条件分页查询签到信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:17:28
	 * @param String name 签到用户姓名
	 * @param String addressName 地址名称
	 * @param Date beginTime 查询时间区域下限
	 * @param Date endTime 查询时间区域上限
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<SignVO> rows 签到信息
	 */
	public Map<String, Object> load(
			String name,
			String addressName,
			Date beginTime,
			Date endTime,
			int currentPage,
			int pageSize) throws Exception{
		Page<SignPO> pagedEntites = findByConditions(name, addressName, beginTime, endTime, currentPage, pageSize);
		List<SignVO> rows = null;
		long total = 0l;
		if(pagedEntites != null){
			List<SignPO> entities = pagedEntites.getContent();
			Set<Long> addressIds = new HashSet<Long>();
			for(SignPO entity:entities){
				addressIds.add(entity.getAddressId());
			}
			rows = SignVO.getConverter(SignVO.class).convert(entities, SignVO.class);
			total = pagedEntites.getTotalElements();
			if(addressIds.size() > 0){
				List<AddressPO> addressEntities = addressDao.findAll(addressIds);
				for(SignVO row:rows){
					for(AddressPO addressEntity:addressEntities){
						if(row.getAddressId().equals(addressEntity.getId())){
							row.setAddressName(addressEntity.getName());
							break;
						}
					}
				}
			}
			
		}
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 根据条件查询签到信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:17:28
	 * @param String name 签到用户姓名
	 * @param String addressName 地址名称
	 * @param Date beginTime 查询时间区域下限
	 * @param Date endTime 查询时间区域上限
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return Page<SignPO> 签到信息
	 */
	public List<SignPO> findByConditions(
			String name, 
			String addressName, 
			Date beginTime, 
			Date endTime) throws Exception{
		String nameExpretion = null;
		String addressNameExpretion = null;
		if(name != null){
			nameExpretion = new StringBufferWrapper().append("%").append("name").append("%").toString();
		}
		if(addressName != null){
			addressNameExpretion = new StringBufferWrapper().append("%").append(addressName).append("%").toString();
		}
		return signDao.findByConditions(nameExpretion, addressNameExpretion, beginTime, endTime);
	}
	
	/**
	 * 根据条件分页查询签到信息<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月13日 下午6:17:28
	 * @param String name 签到用户姓名
	 * @param String addressName 地址名称
	 * @param Date beginTime 查询时间区域下限
	 * @param Date endTime 查询时间区域上限
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return Page<SignPO> 签到信息
	 */
	public Page<SignPO> findByConditions(
			String name, 
			String addressName, 
			Date beginTime, 
			Date endTime,
			int currentPage, 
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		String nameExpretion = null;
		String addressNameExpretion = null;
		if(name != null){
			nameExpretion = new StringBufferWrapper().append("%").append("name").append("%").toString();
		}
		if(addressName != null){
			addressNameExpretion = new StringBufferWrapper().append("%").append(addressName).append("%").toString();
		}
		return signDao.findByConditions(nameExpretion, addressNameExpretion, beginTime, endTime, page);
	}
	
}
