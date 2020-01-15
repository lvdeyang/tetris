package com.sumavision.bvc.device.command.user.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserSettingDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.setting.AutoManualMode;
import com.sumavision.bvc.command.group.user.setting.CastMode;
import com.sumavision.bvc.command.group.user.setting.CommandGroupUserSettingPO;
import com.sumavision.bvc.command.group.user.setting.OpenCloseMode;
import com.sumavision.bvc.command.group.user.setting.SecretMode;
import com.sumavision.bvc.command.group.user.setting.VisibleRange;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandSplitServiceImpl 
* @Description: 分屏相关业务
* @author zsy
* @date 2019年11月15日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandSettingServiceImpl {
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandGroupUserSettingDAO commandGroupUserSettingDao;
	
	public CommandGroupUserSettingPO querySetting(Long userId){
		
		CommandGroupUserSettingPO setting = commandGroupUserInfoDao.findByUserId(userId).getSetting();
		return setting;
	}
	
	/**
	 * 设置呼叫应答方式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:05:00
	 * @param userId
	 * @param mode
	 */
	public void setResponseMode(Long userId, AutoManualMode mode){
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserSettingPO setting = userInfo.getSetting();
		setting.setResponseMode(mode);
		commandGroupUserSettingDao.save(setting);
	}
	
	/**
	 * 设置单播/组播方式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:05:00
	 * @param userId
	 * @param mode
	 */
	public void setVodMode(Long userId, CastMode mode){
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserSettingPO setting = userInfo.getSetting();
		setting.setVodMode(mode);
		commandGroupUserSettingDao.save(setting);
	}
	
	/**
	 * 设置视频转发应答方式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:05:00
	 * @param userId
	 * @param mode
	 */
	public void setSendAnswerMode(Long userId, AutoManualMode mode){
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserSettingPO setting = userInfo.getSetting();
		setting.setSendAnswerMode(mode);
		commandGroupUserSettingDao.save(setting);
	}
	
	/**
	 * 设置启动模式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:05:00
	 * @param userId
	 * @param mode
	 */
	public void setCommandMeetingAutoStart(Long userId, AutoManualMode mode){
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserSettingPO setting = userInfo.getSetting();
		setting.setCommandMeetingAutoStart(mode);
		commandGroupUserSettingDao.save(setting);
	}
	
	/**
	 * 设置字幕设置<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:05:00
	 * @param userId
	 * @param mode
	 */
	public void setSubtitle(Long userId, OpenCloseMode mode){
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserSettingPO setting = userInfo.getSetting();
		setting.setSubtitle(mode);
		commandGroupUserSettingDao.save(setting);
	}
	
	/**
	 * 设置可见范围<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:05:00
	 * @param userId
	 * @param mode
	 */
	public void setVisibleRange(Long userId, VisibleRange mode){
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserSettingPO setting = userInfo.getSetting();
		setting.setVisibleRange(mode);
		commandGroupUserSettingDao.save(setting);
	}
	
	/**
	 * 设置录像模式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:05:00
	 * @param userId
	 * @param mode
	 */
	public void setRecordMode(Long userId, AutoManualMode mode){
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserSettingPO setting = userInfo.getSetting();
		setting.setRecordMode(mode);
		commandGroupUserSettingDao.save(setting);
	}
	
	/**
	 * 设置专向方式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 上午9:05:00
	 * @param userId
	 * @param mode
	 */
	public void setDedicatedMode(Long userId, SecretMode mode){
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		CommandGroupUserSettingPO setting = userInfo.getSetting();
		setting.setDedicatedMode(mode);
		commandGroupUserSettingDao.save(setting);
	}
}
