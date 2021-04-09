package com.sumavision.bvc.device.monitor.subtitle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.monitor.osd.MonitorOsdLayerDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdLayerPO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdLayerType;
import com.sumavision.bvc.device.monitor.subtitle.exception.SubtitleIsInUseException;
import com.sumavision.bvc.device.monitor.subtitle.exception.SubtitleNotExistException;
import com.sumavision.bvc.device.monitor.subtitle.exception.UserHasNoPermissionForSubtitleException;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorSubtitleService {

	@Autowired
	private MonitorSubtitleDAO monitorSubtitleDao;
	
	@Autowired
	private MonitorOsdLayerDAO monitorOsdLayerDao;
	
	/**
	 * 添加一个字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午1:22:04
	 * @param String content 字幕内容
	 * @param String font 字体
	 * @param String height 字体大小
	 * @param String color 颜色
	 * @param String userId 用户id
	 * @param String username 用户名
	 * @return MonitorSubtitlePO 字幕
	 */
	public MonitorSubtitlePO add(
			String name,
			String content,
			String font,
			Integer height,
			String color,
			Long userId,
			String username) throws Exception{
		
		MonitorSubtitlePO subtitle = new MonitorSubtitlePO();
		subtitle.setName(name);
		subtitle.setContent(content);
		subtitle.setFont(("".equals(font)||font==null)?MonitorSubtitleFont.HEITI:MonitorSubtitleFont.fromName(font));
		subtitle.setHeight(("".equals(height)||height==null)?20:height);
		subtitle.setColor(("".equals(color)||color==null)?"ffffff":color.toLowerCase());
		subtitle.setUserId(userId.toString());
		subtitle.setUsername(username);
		monitorSubtitleDao.save(subtitle);
		
		return subtitle;
	}
	
	/**
	 * 删除字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午1:42:09
	 * @param Long id 字幕id
	 * @param Long userId 用户id
	 */
	public void remove(Long id, Long userId) throws Exception{
		
		MonitorSubtitlePO entity = monitorSubtitleDao.findOne(id);
		
		if(entity == null) return;
		if(!entity.getUserId().equals(userId.toString())) {
			throw new UserHasNoPermissionForSubtitleException(id);
		}
		
		List<MonitorOsdLayerPO> referenceLayers = monitorOsdLayerDao.findByTypeAndContentId(MonitorOsdLayerType.SUBTITLE, id);
		if(referenceLayers!=null && referenceLayers.size()>0){
			throw new SubtitleIsInUseException(referenceLayers.get(0).getId());
		}
		
		monitorSubtitleDao.delete(entity);
		
	}
	
	/**
	 * 修改字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午1:53:47
	 * @param Long id 字幕id
	 * @param String content 字幕内容
	 * @param String font 字体
	 * @param Integer height 字体大小
	 * @param String color 字体颜色
	 * @param Long userId 用户id
	 * @return MonitorSubtitlePO 字幕
	 */
	public MonitorSubtitlePO edit(
			Long id,
			String name,
			String content,
			String font,
			Integer height,
			String color,
			Long userId) throws Exception{
		
		MonitorSubtitlePO subtitle = monitorSubtitleDao.findOne(id);
		if(subtitle == null){
			throw new SubtitleNotExistException(id);
		}
		
		if(!subtitle.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionForSubtitleException(id);
		}
		
		subtitle.setName(name);
		subtitle.setContent(content);
		subtitle.setFont(font==null?MonitorSubtitleFont.HEITI:MonitorSubtitleFont.fromName(font));
		subtitle.setHeight(height==null?20:height);
		subtitle.setColor(color==null?"ffffff":color.toLowerCase());
		monitorSubtitleDao.save(subtitle);
		
		return subtitle;
	}
	
}
