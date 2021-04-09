package com.sumavision.tetris.bvc.model.agenda;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AgendaPO.class, idClass = Long.class)
public interface AgendaDAO extends BaseDAO<AgendaPO>{
	
	public AgendaPO findByBusinessInfoType(BusinessInfoType businessInfoType);
	
	/**
	 * 查询内置议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 下午4:01:20
	 * @return List<AgendaPO> 内置议程列表
	 */
	public Page<AgendaPO> findByBusinessIdNull(Pageable page);
	
	/**
	 * 根据业务查询议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月25日 上午11:46:04
	 * @param Long businessId 业务id
	 * @param BusinessInfoType businessInfoType 业务类型
	 * @return List<AgendaPO> 议程列表
	 */
	public List<AgendaPO> findByBusinessIdAndBusinessInfoType(Long businessId, BusinessInfoType businessInfoType);
	
	/**
	 * 根据会议id查询自定义议程（由于后台原因，businessId暂认为是groupId）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月14日 下午3:04:13
	 * @param Long businessId 业务id
	 * @return List<AgendaPO> 自定义议程列表
	 */
	public List<AgendaPO> findByBusinessId(Long businessId);
	

	public List<AgendaPO> findByBusinessIdAndBusinessInfoTypeNull(Long businessId);
	
}
