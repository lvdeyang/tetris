package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;


@RepositoryDefinition(domainClass = ConferenceHallPO.class, idClass = Long.class)
public interface ConferenceHallDAO extends BaseDAO<ConferenceHallPO>{

	/**
	 * 根据终端查询会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月20日 下午5:03:14
	 * @param Long terminalId 终端id
	 * @return List<ConferenceHallPO> 会场列表
	 */
	public List<ConferenceHallPO> findByTerminalId(Long terminalId);
	
	/**
	 * 根据名称模糊查询会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 上午11:36:35
	 * @param String name 名称
	 * @param Pageable page 分页信息
	 * @return Page<ConferenceHallPO> 会场列表
	 */
	public Page<ConferenceHallPO> findByNameLike(String name, Pageable page);
	
	/**
	 * 根据会场名称以及终端名称模糊查询会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月20日 下午5:59:18
	 * @param String name 名称表达式
	 * @param Pageable page 分页信息
	 * @return Page<ConferenceHallPO> 会场列表
	 */
	@Query(value = "SELECT H.* FROM TETRIS_BVC_BUSINESS_TERMINAL_CONFERENCE_HALL H LEFT JOIN TETRIS_BVC_MODEL_TERMINAL T ON H.TERMINAL_ID=T.ID WHERE H.NAME LIKE ?1 OR T.NAME LIKE ?1 \n#pageable\n",
			countQuery = "SELECT COUNT(H.ID) FROM TETRIS_BVC_BUSINESS_TERMINAL_CONFERENCE_HALL H LEFT JOIN TETRIS_BVC_MODEL_TERMINAL T ON H.TERMINAL_ID=T.ID WHERE H.NAME LIKE ?1 OR T.NAME LIKE ?1",
			nativeQuery = true)
	public Page<ConferenceHallPO> findByNameLikeOrTerminalNameLike(String name, Pageable page);
	

	/**
	 * 查询文件夹下游权限的会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月19日 下午1:48:46
	 * @param Long folderId 文件夹id
	 * @return List<ConferenceHallPO> 会场列表
	 */
	@Query(value = "SELECT DISTINCT H.* FROM TETRIS_BVC_BUSINESS_TERMINAL_CONFERENCE_HALL H LEFT JOIN TETRIS_BVC_BUSINESS_CONFERENCE_HALL_ROLE_PERMISSION P ON H.ID=P.CONFERENCE_HALL_ID WHERE P.ROLE_ID IN ?1 AND H.FOLDER_ID=?2", nativeQuery = true)
	public List<ConferenceHallPO> findPermissionConferenceHallsByFolderId(Collection<Long> roleIds, Long folderId);
}
