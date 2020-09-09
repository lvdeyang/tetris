package com.sumavision.tetris.mims.app.media.settings;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaSettingsService {

	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 保存仓库设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:39:51
	 * @param String type 设置类型
	 * @param String settings 设置内容
	 * @return MediaSettingsPO 仓库设置
	 */
	public MediaSettingsPO save(String type, String settings) throws Exception{
		UserVO user = userQuery.current();
		Long companyId = Long.valueOf(user.getGroupId());
		
		MediaSettingsType settingsType = MediaSettingsType.valueOf(type);
		MediaSettingsPO entity = mediaSettingsDao.findByCompanyIdAndType(companyId, settingsType);
		if(entity == null){
			entity = new MediaSettingsPO();
			entity.setUpdateTime(new Date());
			entity.setType(settingsType);
			entity.setCompanyId(companyId);
		}
		entity.setSettings(settings);
		mediaSettingsDao.save(entity);
		
		return entity;
	}
	
	/**
	 * 清除仓库设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:51:56
	 * @param String type 设置类型
	 */
	public void delete(String type) throws Exception{
		UserVO user = userQuery.current();
		Long companyId = Long.valueOf(user.getGroupId());
		MediaSettingsType settingsType = MediaSettingsType.valueOf(type);
		MediaSettingsPO entity = mediaSettingsDao.findByCompanyIdAndType(companyId, settingsType);
		if(entity != null){
			mediaSettingsDao.delete(entity);
		}
	}
	
}
