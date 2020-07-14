package com.sumavision.tetris.bvc.model.terminal.screen;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TerminalScreenPrimaryKeyPO.class, idClass = Long.class)
public interface TerminalScreenPrimaryKeyDAO extends BaseDAO<TerminalScreenPrimaryKeyPO>{

	/**
	 * 查询屏幕主键<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:13:31
	 * @param String screenPrimaryKey 主键
	 * @return List<TerminalScreenPrimaryKeyPO> 主键
	 */
	public List<TerminalScreenPrimaryKeyPO> findByScreenPrimaryKey(String screenPrimaryKey);
	
	/**
	 * 查询屏幕主键<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:13:31
	 * @param Collection<String> screenPrimaryKeys 主键列表
	 * @return List<TerminalScreenPrimaryKeyPO> 主键列表
	 */
	public List<TerminalScreenPrimaryKeyPO> findByScreenPrimaryKeyIn(Collection<String> screenPrimaryKeys);
	
}
