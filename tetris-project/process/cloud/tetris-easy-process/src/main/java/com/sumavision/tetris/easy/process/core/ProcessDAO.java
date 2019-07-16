package com.sumavision.tetris.easy.process.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProcessPO.class, idClass = Long.class)
public interface ProcessDAO extends BaseDAO<ProcessPO>{

	/**
	 * 根据自定义流程id查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:38:11
	 * @param String processId 流程id
	 * @return ProcessPO 流程数据
	 */
	public ProcessPO findByProcessId(String processId);
	
	/**
	 * 查询公司下的流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午1:10:41
	 * @param String companyId 公司id
	 * @param Pageable page 分页信息
	 * @return List<ProcessPO> 流程列表
	 */
	@Query(value = "SELECT process.ID, process.UUID, process.UPDATE_TIME, process.PROCESS_ID, process.NAME, process.TYPE, process.REMARKS, process.PATH, process.BPMN FROM TETRIS_PROCESS process LEFT JOIN TETRIS_PROCESS_COMPANY_PERMISSION permission ON process.ID=permission.PROCESS_ID WHERE permission.COMPANY_ID=?1 \n#pageable\n",
		   countQuery = "SELECT count(process.id) FROM TETRIS_PROCESS process LEFT JOIN TETRIS_PROCESS_COMPANY_PERMISSION permission ON process.id=permission.PROCESS_ID WHERE permission.COMPANY_ID=?1",
		   nativeQuery = true)
	public Page<ProcessPO> findByCompanyId(String companyId, Pageable page) throws Exception;
	
	/**
	 * 统计公司下的流程数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午1:12:26
	 * @param String companyId 公司id
	 * @return int 流程数量
	 */
	@Query(value = "SELECT count(process.id) FROM TETRIS_PROCESS process LEFT JOIN TETRIS_PROCESS_COMPANY_PERMISSION permission ON process.id=permission.PROCESS_ID WHERE permission.COMPANY_ID=?1", nativeQuery = true)
	public int countByCompanyId(String companyId);
	
}
