package com.sumavision.bvc.device.monitor.osd;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.device.monitor.osd.exception.MonitorOsdLayerMoreThanMaximumException;
import com.sumavision.bvc.device.monitor.osd.exception.MonitorOsdLayerNotExistException;
import com.sumavision.bvc.device.monitor.osd.exception.MonitorOsdNotExistException;
import com.sumavision.bvc.device.monitor.osd.exception.UserHasNoPermissionForOsdException;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorOsdLayerService {

	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private MonitorOsdLayerDAO monitorOsdLayerDao;
	
	/**
	 * 添加osd图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午10:55:58
	 * @param Long osdId osd id
	 * @param int x 万分比坐标
	 * @param int y 万分比坐标
	 * @param int layerIndex 图层顺序
	 * @param MonitorOsdLayerType type 图层内容类型
	 * @param Long contentId 图层内容资源id
	 * @param String contentName 图层内容资源名称
	 * @param String contentUsername 图层内容创建用户
	 * @param long userId 用户id 
	 * @return MonitorOsdLayerPO 图层
	 */
	public MonitorOsdLayerPO add(
			Long osdId,
			int x,
			int y,
			int layerIndex,
			MonitorOsdLayerType type,
			Long contentId,
			String contentName,
			String contentUsername,
			Long userId) throws Exception{
		
		MonitorOsdPO osd = monitorOsdDao.findOne(osdId);
		if(osd == null){
			throw new MonitorOsdNotExistException(osdId);
		}
		
		if(!osd.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionForOsdException(osdId);
		}
		
		Set<MonitorOsdLayerPO> existLayers = osd.getLayers();
		if(existLayers!=null && existLayers.size()>7){
			throw new MonitorOsdLayerMoreThanMaximumException(8);
		}
		
		MonitorOsdLayerPO layer = new MonitorOsdLayerPO();
		layer.setX(x);
		layer.setY(y);
		layer.setLayerIndex(layerIndex);
		layer.setType(type);
		layer.setContentId(contentId);
		layer.setContentName(contentName);
		layer.setContentUsername(contentUsername);
		layer.setOsd(osd);
		osd.getLayers().add(layer);
		monitorOsdLayerDao.save(layer);

		return layer;
	}
	
	/**
	 * 修改一个图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午11:01:37
	 * @param Long id 图层id
	 * @param int x 万分比坐标
	 * @param int y 万分比坐标
	 * @param int layerIndex 图层顺序
	 * @param MonitorOsdLayerType type 图层内容类型
	 * @param Long contentId 图层内容资源id
	 * @param String contentName 图层内容资源名称
	 * @param long userId 用户id 
	 * @return MonitorOsdLayerPO 图层
	 */
	public MonitorOsdLayerPO edit(
			Long id,
			int x,
			int y,
			MonitorOsdLayerType type,
			Long contentId,
			String contentName,
			String subtitleUsername,
			Long userId) throws Exception{
		
		MonitorOsdLayerPO layer = monitorOsdLayerDao.findOne(id);
		if(layer == null){
			throw new MonitorOsdLayerNotExistException(id);
		}
		
		MonitorOsdPO osd = layer.getOsd();
		if(osd == null){
			throw new MonitorOsdNotExistException(id);
		}
		
		if(!osd.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionForOsdException(id);
		}
		
		layer.setX(x);
		layer.setY(y);
		layer.setType(type);
		layer.setContentId(contentId);
		layer.setContentName(contentName);
		layer.setContentUsername(subtitleUsername);
		monitorOsdLayerDao.save(layer);
		
		return layer;
	}
	
	/**
	 * 删除图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午11:31:44
	 * @param Long id 图层id
	 * @param Long userId 用户id
	 * @return List<MonitorOsdLayerPO> 剩余图层重新排序
	 */
	public List<MonitorOsdLayerPO> remove(Long id, Long userId) throws Exception{
		
		MonitorOsdLayerPO layer = monitorOsdLayerDao.findOne(id);
		if(layer == null){
			throw new MonitorOsdLayerNotExistException(id);
		}
		
		MonitorOsdPO osd = layer.getOsd();
		if(osd == null){
			throw new MonitorOsdNotExistException(id);
		}
		
		if(!osd.getUserId().equals(userId.toString())){
			throw new UserHasNoPermissionForOsdException(id);
		}
		
		osd.getLayers().remove(layer);
		layer.setOsd(null);
		monitorOsdDao.save(osd);
		monitorOsdLayerDao.delete(layer);
		
		List<MonitorOsdLayerPO> layerEntities = new ArrayListWrapper<MonitorOsdLayerPO>().addAll(osd.getLayers()).getList();
		Collections.sort(layerEntities, new MonitorOsdLayerPO.MonitorOsdLayerComparator());
		
		for(int i=0; i<layerEntities.size(); i++){
			layerEntities.get(i).setLayerIndex(i);
		}
		monitorOsdLayerDao.save(layerEntities);
		
		return layerEntities;
	}
	
	/**
	 * 图层排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午11:36:57
	 * @param JSONArray layers 图层顺序
	 */
	public void sort(JSONArray layers) throws Exception{
		
		Set<Long> layerIds = new HashSet<Long>();
		
		for(int i=0; i<layers.size(); i++){
			layerIds.add(layers.getJSONObject(i).getLong("id"));
		}
		
		List<MonitorOsdLayerPO> entities = monitorOsdLayerDao.findAll(layerIds);
		for(MonitorOsdLayerPO entity:entities){
			for(int i=0; i<layers.size(); i++){
				if(layers.getJSONObject(i).getLong("id").equals(entity.getId())){
					entity.setLayerIndex(layers.getJSONObject(i).getInteger("layerIndex"));
					break;
				}
			}
		}
		monitorOsdLayerDao.save(entities);
	}
	
}
