package com.sumavision.bvc.device.monitor.osd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.bo.OsdBO;
import com.sumavision.bvc.device.group.bo.OsdWrapperBO;
import com.sumavision.bvc.device.monitor.osd.exception.MonitorOsdNotExistException;
import com.sumavision.bvc.device.monitor.osd.exception.UserHasNoPermissionForOsdException;
import com.sumavision.bvc.device.monitor.subtitle.MonitorSubtitleDAO;
import com.sumavision.bvc.device.monitor.subtitle.MonitorSubtitlePO;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorOsdService {
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private MonitorSubtitleDAO monitorSubtitleDao;
	
	/**
	 * 添加一个osd显示<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午8:50:45
	 * @param String name osd名称
	 * @param Long userId 用户id
	 * @param String username 用户名
	 * @return MonitorOsdPO osd
	 */
	public MonitorOsdPO add(
			String name, 
			Long userId, 
			String username) throws Exception{
		
		MonitorOsdPO osd = new MonitorOsdPO();
		osd.setName(name);
		osd.setUserId(userId.toString());
		osd.setUsername(username);
		osd.setUpdateTime(new Date());
		monitorOsdDao.save(osd);
		
		return osd;
	}
	
	/**
	 * 删除osd<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午8:57:26
	 * @param Long id osd id
	 * @param Long userId 用户id
	 */
	public void remove(
			Long id,
			Long userId) throws Exception{
		
		MonitorOsdPO osd = monitorOsdDao.findOne(id);
		if(osd == null){
			throw new MonitorOsdNotExistException(id);
		}
		
		if(!osd.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionForOsdException(id);
		}
		
		monitorOsdDao.delete(osd);
	}
	
	/**
	 * 修改osd<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午8:59:30
	 * @param Long id osd id
	 * @param String name osd名称
	 * @param Long userId 用户id
	 * @return MonitorOsdPO osd
	 */
	public MonitorOsdPO edit(
			Long id,
			String name,
			Long userId) throws Exception{
		
		MonitorOsdPO osd = monitorOsdDao.findOne(id);
		if(osd == null){
			throw new MonitorOsdNotExistException(id);
		}
		
		if(!osd.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionForOsdException(id);
		}
		
		osd.setName(name);
		osd.setUpdateTime(new Date());
		monitorOsdDao.save(osd);
		
		return osd;
	}
	
	/**
	 * 转换osd协议数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 上午10:52:09
	 * @param MonitorOsdPO osdEntity osd
	 * @param String devid 源的设备号码（如果是录制回放任务则取录制文件的设备的信息）
	 * @param String devname 源的设备名称（如果是录制回放任务则取录制文件的设备的信息）
	 * @return OsdWrapperBO 协议
	 */
	public OsdWrapperBO protocol(MonitorOsdPO osdEntity, String devid, String devname){
		
		OsdWrapperBO osds = new OsdWrapperBO();
		
		if(osdEntity == null) return osds;
		
		osds.setDevid(devid)
			.setDevname(devname);
		
		List<OsdBO> protocol = new ArrayList<OsdBO>();
		
		Set<Long> subtitleIds = new HashSet<Long>();
		
		Set<Integer> indexes = new HashSetWrapper<Integer>().add(0).add(1).add(2).add(3).add(4).add(5).add(6).add(7).getSet();
		Set<Integer> notUsed = new HashSet<Integer>();
		
		Set<MonitorOsdLayerPO> layerEntites = osdEntity.getLayers();
		if(layerEntites!=null && layerEntites.size()>=0) {
			for(MonitorOsdLayerPO layerEntity:layerEntites){
				if(layerEntity.getType().equals(MonitorOsdLayerType.SUBTITLE)){
					if(layerEntity.getContentId() != null) subtitleIds.add(layerEntity.getContentId());
				}
			}
			
			for(Integer index:indexes){
				boolean finded = false;
				for(MonitorOsdLayerPO layerEntity:layerEntites){
					if(layerEntity.getLayerIndex().equals(index)){
						finded = true;
						break;
					}
				}
				if(!finded){
					notUsed.add(index);
				}
			}
			
			List<MonitorSubtitlePO> subtitleEntities = monitorSubtitleDao.findAll(subtitleIds);
			for(MonitorOsdLayerPO layerEntity:layerEntites){
				if(layerEntity.getType().equals(MonitorOsdLayerType.SUBTITLE) && 
						(subtitleEntities!=null && subtitleEntities.size()>0)){
					for(MonitorSubtitlePO subtitleEntity:subtitleEntities){
						if(subtitleEntity.getId().equals(layerEntity.getContentId())){
							protocol.add(new OsdBO().setContent(subtitleEntity.getContent())
													.setColor(String.valueOf(Integer.parseInt(subtitleEntity.getColor(), 16)))
													.setFont(subtitleEntity.getFont().getProtocol())
													.setHeight(subtitleEntity.getHeight())
													.setX(layerEntity.getX())
													.setY(layerEntity.getY())
													.setIndex(layerEntity.getLayerIndex())
													.setShow(layerEntity.getIsShow()));
						}
					}
				}else if(layerEntity.getType().equals(MonitorOsdLayerType.DATE) || 
						layerEntity.getType().equals(MonitorOsdLayerType.DATETIME) || 
						layerEntity.getType().equals(MonitorOsdLayerType.DEVNAME)){
					protocol.add(new OsdBO().setContent(layerEntity.getType().getProtocol())
											.setX(layerEntity.getX())
											.setY(layerEntity.getY())
											.setIndex(layerEntity.getLayerIndex())
											.setShow(layerEntity.getIsShow()));
				}
			}
		}
		
		if(notUsed.size() > 0){
			for(Integer index:notUsed){
				protocol.add(new OsdBO().setIndex(index)
									    .setShow(0)
									    .setContent(""));
			}
		}
		
		osds.setLayers(protocol);
		
		return osds;
	}
	
	/**
	 * 清除osd指令<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月28日 下午7:00:56
	 * @param String devid 源的设备号码（如果是录制回放任务则取录制文件的设备的信息）
	 * @param String devname 源的设备名称（如果是录制回放任务则取录制文件的设备的信息）
	 * @return OsdWrapperBO 协议
	 */
	public OsdWrapperBO clearProtocol(String devid, String devname){
		
		OsdWrapperBO osds = new OsdWrapperBO().setDevid(devid)
											  .setDevname(devname);
		
		osds.setLayers(new ArrayList<OsdBO>());
		
		for(int i=0; i<8; i++){
			osds.getLayers().add(new OsdBO().setIndex(i)
										    .setShow(0)
										    .setContent(""));
		}
		
		return osds;
	}
	
}
