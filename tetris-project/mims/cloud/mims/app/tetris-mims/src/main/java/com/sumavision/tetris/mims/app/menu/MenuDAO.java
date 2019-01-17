package com.sumavision.tetris.mims.app.menu;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;


@RepositoryDefinition(domainClass = MenuPO.class, idClass = long.class)
public interface MenuDAO extends BaseDAO<MenuPO>{

	/**
	 * 获取用户有权限的菜单，并根据显示序号排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月20日 下午1:37:13
	 * @param String classify 用户分类
	 * @return List<MenuPO> 菜单
	 */
	@Query(value = "select m.id, m.uuid, m.update_time, m.title, m.link, m.icon, m.style, m.is_group, m.parent_id, m.menu_id_path, m.serial from MIMS_MENU m left join MIMS_MENU_PERMISSION mp on m.id=mp.menu_id where mp.user_classify=?1 order by m.serial asc", nativeQuery = true)
	public List<MenuPO> findByUserClassify(String classify);
	
	/**
	 * id查询,并且根据显示序号排序（一般用于遍历父菜单id后查询父菜单）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月20日 下午1:37:49
	 * @param Collection<Long> ids 菜单id
	 * @return List<MenuPO> 菜单
	 */
	public List<MenuPO> findByIdInOrderBySerialAsc(Collection<Long> ids);
	
}
