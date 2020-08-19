package com.sumavision.bvc.basic.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.basic.po.BasicRolePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BasicRolePO.class, idClass = Long.class)
public interface BasicRoleDAO extends MetBaseDAO<BasicRolePO>{

	/**
	 * 根据角色名查询角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 上午9:44:58
	 * @param String name 角色名
	 * @return BasicRolePO 角色信息
	 */
	public BasicRolePO findByName(String name);
	
	/**
	 * 根据角色名批量查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 上午10:05:56
	 * @param Collection<String> names
	 * @return List<BasicRolePO>
	 */
	public List<BasicRolePO> findByNameIn(Collection<String> names);
	
}
