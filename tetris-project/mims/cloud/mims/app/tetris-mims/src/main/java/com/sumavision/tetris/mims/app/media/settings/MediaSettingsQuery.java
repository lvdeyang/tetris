package com.sumavision.tetris.mims.app.media.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.easy.process.core.ProcessQuery;
import com.sumavision.tetris.easy.process.core.ProcessVO;
import com.sumavision.tetris.mims.app.media.exception.NoReviewProcessFoundException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class MediaSettingsQuery {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	/**
	 * 校验是否需要启动流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午11:43:07
	 * @param MediaSettingsType type 流程设置类型
	 * @return boolean 是否需要启动流程
	 */
	public boolean needProcess(MediaSettingsType type) throws Exception{
		UserVO user = userQuery.current();
		Long companyId = Long.valueOf(user.getGroupId());
		MediaSettingsPO switchMediaUploadReview = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.SWITCH_MEDIA_UPLOAD_REVIEW);
		boolean _switch = false;
		if(switchMediaUploadReview!=null && "true".equals(switchMediaUploadReview.getSettings())){
			_switch = true;
			MediaSettingsPO processUploadPicture = mediaSettingsDao.findByCompanyIdAndType(companyId, type);
			if(processUploadPicture == null){
				throw new NoReviewProcessFoundException(type.getName());
			}
			Long processId = Long.valueOf(processUploadPicture.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			if(process == null){
				throw new NoReviewProcessFoundException(processId, type.getName());
			}
		}
		return _switch;
	}
	
}
