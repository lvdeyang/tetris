package com.sumavision.tetris.bvc.business.terminal.hall;

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

@Component
public class ConferenceHallRolePermissionQuery {

	@Autowired
	private ConferenceHallDAO conferenceHallDao;
	
	@Autowired
	private ConferenceHallRolePermissionDAO conferenceHallRolePermissionDao;
	
	/**
	 * 分页查询授权情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月5日 下午3:29:13
	 * @param String name 会场名称模糊查询
	 * @param Long roleId 角色id
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return rows List<ConferenceHallVO> 权限列表
	 * @return total long 总数据量
	 */
	public Map<String, Object> load(
			String name,
			Long roleId,
			int currentPage,
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ConferenceHallPO> pagedEntities = null;
		if(name!=null && !"".equals(name)){
			pagedEntities = conferenceHallDao.findByNameLike(new StringBufferWrapper().append("%").append(name).append("%").toString(), page);
		}else{
			pagedEntities = conferenceHallDao.findAll(page);
		}
		long total = pagedEntities.getTotalElements();
		List<ConferenceHallPO> entities = pagedEntities.getContent();
		List<ConferenceHallVO> rows = ConferenceHallVO.getConverter(ConferenceHallVO.class).convert(entities, ConferenceHallVO.class);
		if(entities!=null && entities.size()>0){
			Set<Long> conferenceHallIds = new HashSet<Long>();
			for(ConferenceHallPO entity:entities){
				conferenceHallIds.add(entity.getId());
			}
			List<ConferenceHallRolePermissionPO> permissions = conferenceHallRolePermissionDao.findByRoleIdAndConferenceHallIdIn(roleId, conferenceHallIds);
			if(permissions!=null && permissions.size()>0){
				for(ConferenceHallVO row:rows){
					for(ConferenceHallRolePermissionPO permission:permissions){
						if(row.getId().equals(permission.getConferenceHallId())){
							row.addPermission(permission.getPrivilegeType());
						}
					}
				}
			}
		}
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
}
