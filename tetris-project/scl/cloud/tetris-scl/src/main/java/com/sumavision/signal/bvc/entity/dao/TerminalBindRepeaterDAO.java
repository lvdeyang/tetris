package com.sumavision.signal.bvc.entity.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.entity.po.TerminalBindRepeaterPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalBindRepeaterPO.class, idClass = long.class)
public interface TerminalBindRepeaterDAO extends BaseDAO<TerminalBindRepeaterPO>{

	/**
	 * 根据网口id查询绑定<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午1:40:08
	 * @param Long id 网口id
	 * @return List<TerminalBindRepeaterPO>
	 */
	public List<TerminalBindRepeaterPO> findByAccessId(Long id);
	
	/**
	 * 根据repeaterId删除<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午9:42:11
	 * @param Long repeaterId 转发器id
	 */
	public void deleteByRepeaterId(Long repeaterId);
	
	/**
	 * 根据accessId删除<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午9:59:41
	 * @param Long accessId
	 */
	public void deleteByAccessId(Long accessId);
	
	/**
	 * 根据bundleId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月25日 下午5:02:56
	 * @param String bundleId
	 * @return TerminalBindRepeaterPO
	 */
	public TerminalBindRepeaterPO findByBundleId(String bundleId);
	
	/**
	 * 根据bundleIds查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月3日 上午8:55:59
	 * @param Collection<String> bundleIds 
	 * @return List<TerminalBindRepeaterPO>
	 */
	public List<TerminalBindRepeaterPO> findByBundleIdIn(Collection<String> bundleIds);
	
	/**
	 * 根据repeaterId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月11日 上午9:15:44
	 * @param Long repeaterId 流转发器id
	 * @return List<TerminalBindRepeaterPO>
	 */
	public List<TerminalBindRepeaterPO> findByRepeaterId(Long repeaterId);
	
	/**
	 * 根据接入id查询bind<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月6日 上午8:57:46
	 * @param String layerId 接入id
	 * @return List<TerminalBindRepeaterPO>
	 */
	public List<TerminalBindRepeaterPO> findByLayerId(String layerId);
	
	@Modifying
	public void deleteByIdIn(Collection<Long> ids);
}
