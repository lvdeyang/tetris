package com.sumavision.bvc.basic.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.basic.po.BasicConfigPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BasicConfigPO.class, idClass = Long.class)
public interface BasicConfigDAO extends MetBaseDAO<BasicConfigPO>{

	/**
	 * 根据议程名查议程<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午1:26:14
	 * @param String name 议程名
	 * @return BasicConfigPO
	 */
	public BasicConfigPO findByName(String name);
	
	/**
	 * 根据用途标签查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午4:04:34
	 * @param String tag 用途标签
	 * @return BasicConfigPO
	 */
	public BasicConfigPO findByTag(String tag);
	
}
