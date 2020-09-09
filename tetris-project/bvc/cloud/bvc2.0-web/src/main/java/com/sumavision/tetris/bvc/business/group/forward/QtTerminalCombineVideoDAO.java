package com.sumavision.tetris.bvc.business.group.forward;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = QtTerminalCombineVideoPO.class, idClass = Long.class)
public interface QtTerminalCombineVideoDAO extends BaseDAO<QtTerminalCombineVideoPO>{

	/**
	 * 查询终端合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月28日 上午10:02:55
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @return List<QtTerminalCombineVideoPO>合屏
	 */
	public List<QtTerminalCombineVideoPO> findByUserIdAndTerminalId(String userId, Long terminalId);
	
}
