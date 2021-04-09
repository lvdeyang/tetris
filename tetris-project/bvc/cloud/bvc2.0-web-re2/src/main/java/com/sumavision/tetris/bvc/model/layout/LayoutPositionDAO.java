package com.sumavision.tetris.bvc.model.layout;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = LayoutPositionPO.class, idClass = Long.class)
public interface LayoutPositionDAO extends BaseDAO<LayoutPositionPO>{

	/**
	 * 查询虚拟源中的布局，并按照序号排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 上午9:40:20
	 * @param Long layoutId 虚拟源id
	 * @return List<LayoutPositionPO> 布局列表
	 */
	public List<LayoutPositionPO> findByLayoutIdOrderBySerialNum(Long layoutId);
	
	/**
	 * 根据虚拟源id查询<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月19日 下午4:44:05
	 * @return
	 */
	public List<LayoutPositionPO> findByLayoutId(Long layoutId);
	
	/**
	 * 查询虚拟源中的布局（带例外），并按照序号排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月28日 上午9:02:11
	 * @param Long layoutId 虚拟源id
	 * @param Long id 例外布局id
	 * @return List<LayoutPositionPO> 布局列表
	 */
	public List<LayoutPositionPO> findByLayoutIdAndIdNotOrderBySerialNum(Long layoutId, Long id);
	
	/**
	 * 根据虚拟源id以及序号查询虚拟源布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 下午2:35:04
	 * @param Long layoutId 虚拟源id
	 * @param Integer serialNum 布局序号
	 * @return LayoutPositionPO 虚拟源布局
	 */
	public LayoutPositionPO findTopByLayoutIdAndSerialNum(Long layoutId, Integer serialNum);
	
}
