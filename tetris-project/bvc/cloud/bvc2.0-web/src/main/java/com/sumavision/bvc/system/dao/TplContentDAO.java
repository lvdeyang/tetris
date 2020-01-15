package com.sumavision.bvc.system.dao;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.system.dto.TplContentBusinessRoleDTO;
import com.sumavision.bvc.system.dto.TplContentRecordSchemeDTO;
import com.sumavision.bvc.system.dto.TplContentScreenLayoutDTO;
import com.sumavision.bvc.system.po.TplContentPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = TplContentPO.class, idClass = Long.class)
public interface TplContentDAO extends MetBaseDAO<TplContentPO>{
	
	/**
	 * @Title: 获取会议模板的业务角色内容 
	 * @param tplIds
	 * @return Page<TplContentBusinessRoleDTO> 
	 */
	@Query(value = "select new com.sumavision.bvc.system.dto.TplContentBusinessRoleDTO(tc.id, tc.tpl.id, br.id, br.name)" +
					"from TplContentPO tc, BusinessRolePO br " +
					"where tc.contentId=br.id " +
					"and tc.type='BUSINESSROLE' " +
					"and tc.tpl.id in ?1")
	public List<TplContentBusinessRoleDTO> findAllBusinessRolesByTplIdsOutputDTO(Collection<Long> tplIds);
	
	/**
	 * @Title: 获取会议模板的录制方案内容 
	 * @param @param tplIds
	 * @return Page<TplContentRecordSchemeDTO>
	 */
	@Query(value = "select new com.sumavision.bvc.system.dto.TplContentRecordSchemeDTO(tc.id, tc.tpl.id, rs.id, rs.name)" +
				   "from TplContentPO tc, RecordSchemePO rs " +
				   "where tc.contentId=rs.id " +
				   "and tc.type='RECORDSCHEME' " +
				   "and tc.tpl.id in ?1")
	public List<TplContentRecordSchemeDTO> findAllRecordSchemesByTplIdsOutputDTO(Collection<Long> tplIds);
	
	/**
	 * @Title: 获取会议模板的屏幕布局内容 
	 * @param tplIds
	 * @return Page<TplContentScreenLayoutDTO>
	 */
	@Query(value = "select new com.sumavision.bvc.system.dto.TplContentScreenLayoutDTO(tc.id, tc.tpl.id, sl.id, sl.name, sl.websiteDraw)" +
				   "from TplContentPO tc, ScreenLayoutPO sl " +
				   "where tc.contentId=sl.id " +
				   "and tc.type='SCREENLAYOUT' " +
				   "and tc.tpl.id in ?1")
	public List<TplContentScreenLayoutDTO> findAllScreenLayoutsByTplIdsOutputDTO(Collection<Long> tplIds);
	
}
