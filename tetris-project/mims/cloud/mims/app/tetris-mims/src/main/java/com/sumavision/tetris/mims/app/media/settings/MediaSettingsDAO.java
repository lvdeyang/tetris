package com.sumavision.tetris.mims.app.media.settings;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaSettingsPO.class, idClass = Long.class)
public interface MediaSettingsDAO extends BaseDAO<MediaSettingsPO>{
	
	/**
	 * 查询公司媒资仓库设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午2:54:08
	 * @param Long companyId 公司id
	 * @return List<MediaSettingsPO> 设置列表
	 */
	public List<MediaSettingsPO> findByCompanyId(Long companyId);
	
	/**
	 * 查询公司特定类型仓库设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:33:54
	 * @param Long companyId 公司id
	 * @param MediaSettingsType type 设置类型
	 * @return MediaSettingsPO 仓库设置
	 */
	public MediaSettingsPO findByCompanyIdAndType(Long companyId, MediaSettingsType type);
	
}
