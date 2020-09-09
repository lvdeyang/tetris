package com.sumavision.signal.bvc.entity.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.entity.enumeration.RepeaterType;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RepeaterPO.class, idClass = long.class)
public interface RepeaterDAO extends BaseDAO<RepeaterPO>{

	/**
	 * 根据名称查询流转发器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午10:41:29
	 * @param String name 名称
	 * @return RepeaterPO 流转发器
	 */
	public RepeaterPO findByName(String name);
	
	/**
	 * 根据ip查询流转发器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午10:46:49
	 * @param String ip ip地址
	 * @return RepeaterPO 流转发器
	 */
	public RepeaterPO findByIp(String ip);
	
	/**
	 * 根据流转发器ip查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月4日 上午9:30:55
	 * @param String address 流转发器地址
	 * @return RepeaterPO 流转发器
	 */
	public RepeaterPO findByAddress(String address);
	
	/**
	 * 根据转发器类型查找<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月4日 上午10:24:59
	 * @param RepeaterType type
	 * @return List<RepeaterPO> 流转发器
	 */
	public List<RepeaterPO> findByType(RepeaterType type);
	
}
