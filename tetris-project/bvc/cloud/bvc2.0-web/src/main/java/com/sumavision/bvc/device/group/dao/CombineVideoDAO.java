package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;

import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@Component("com.sumavision.bvc.device.group.dao.CombineVideoDAO")
@RepositoryDefinition(domainClass = CombineVideoPO.class, idClass = long.class)
public interface CombineVideoDAO extends MetBaseDAO<CombineVideoPO> {

	/**
	 * @Title: 查询设备组中的合屏的ids
	 * @param groupId 设备组id
	 * @return List<Long> 合屏ids
	 * @throws
	 */
	@Query(value = "select cvideo.id from com.sumavision.bvc.device.group.po.CombineVideoPO cvideo left join cvideo.positions position left join position.srcs src where cvideo.group.id=?1 group by cvideo.id having count(position.id)>1", nativeQuery = false)
	public Page<Long> findByGroupId(Long groupId, Pageable page);
	
	/**
	 * @Title: 根据uuid查合屏
	 * @param uuid 合屏uuid
	 * @return CombineVideoPO 
	 * @throws
	 */
	@Query("select cvideo from com.sumavision.bvc.device.group.po.CombineVideoPO cvideo where cvideo.uuid=?1")	
	public CombineVideoPO findByUuid(String uuid);
	
	/**
	 * 查找group的所有合屏<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月11日 上午10:13:07
	 * @param reconGroupId
	 * @return
	 */
	@Query("select cvideo from com.sumavision.bvc.device.group.po.CombineVideoPO cvideo where cvideo.reconGroupId=?1")
	public List<CombineVideoPO> findByReconGroupId(Long reconGroupId);
}
