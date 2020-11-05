package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.bvc.model.agenda.combine.exception.CombineVideoNotFoundException;

@Service
public class CombineVideoService {

	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineVideoPositionDAO combineVideoPositionDao;
	
	@Autowired
	@Qualifier("com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoSrcDAO")
	private CombineVideoSrcDAO combineVideoSrcDao;
	
	/**
	 * 添加合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午5:26:48
	 * @param Long businessId 业务id
	 * @param String businessType 业务类型
	 * @param String name 合屏名称
	 * @return CombineVideoVO 合屏
	 */
	@Transactional(rollbackFor = Exception.class)
	public CombineVideoVO add(
			Long businessId,
		    String businessType,
		    String name) throws Exception{
		
		CombineVideoPO entity = new CombineVideoPO();
		entity.setName(name);
		entity.setBusinessId(businessId);
		entity.setBusinessType(CombineBusinessType.valueOf(businessType));
		entity.setUpdateTime(new Date());
		combineVideoDao.save(entity);
		
		return new CombineVideoVO().set(entity);
	}
	
	/**
	 * 修改合屏名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午5:31:41
	 * @param Long id 合屏id
	 * @param String name 名称
	 * @return CombineVideoVO 合屏
	 */
	@Transactional(rollbackFor = Exception.class)
	public CombineVideoVO editName(
			Long id,
			String name) throws Exception{
		CombineVideoPO entity = combineVideoDao.findOne(id);
		if(entity == null){
			throw new CombineVideoNotFoundException(id);
		}
		entity.setName(name);
		combineVideoDao.save(entity);
		return new CombineVideoVO().set(entity);
	}
	
	/**
	 * 编辑分屏布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 上午9:44:08
	 * @param Long id 合屏id
	 * @param JSONObject websiteDraw 前端布局参数
	 * @param JSONArray positions 分屏布局参数以及配置参数
	 * @return CombineVideoVO 合屏
	 */
	@Transactional(rollbackFor = Exception.class)
	public CombineVideoVO editPosition(
			Long id,
			JSONObject websiteDraw,
			JSONArray positions) throws Exception{
		CombineVideoPO entity = combineVideoDao.findOne(id);
		if(entity == null){
			throw new CombineVideoNotFoundException(id);
		}
		List<CombineVideoPositionPO> existPositionEntities = combineVideoPositionDao.findByCombineVideoId(entity.getId());
		if(existPositionEntities!=null && existPositionEntities.size()>0){
			List<Long> existPositionIds = new ArrayList<Long>();
			for(CombineVideoPositionPO existPositionEntity:existPositionEntities){
				existPositionIds.add(existPositionEntity.getId());
			}
			combineVideoPositionDao.deleteInBatch(existPositionEntities);
			List<CombineVideoSrcPO> existSrcEntities = combineVideoSrcDao.findByCombineVideoPositionIdIn(existPositionIds);
			if(existSrcEntities!=null && existSrcEntities.size()>0){
				combineVideoSrcDao.deleteInBatch(existSrcEntities);
			}
		}
		entity.setWebsiteDraw(websiteDraw.toJSONString());
		combineVideoDao.save(entity);
		List<CombineVideoPositionPO> positionEntities = new ArrayList<CombineVideoPositionPO>();
		for(int i=0; i<positions.size(); i++){
			JSONObject position = positions.getJSONObject(i);
			CombineVideoPositionPO positionEntity = new CombineVideoPositionPO();
			positionEntity.setSerialnum(position.getIntValue("serialNum"));
			positionEntity.setX(tenThousand(position.getString("x")));
			positionEntity.setY(tenThousand(position.getString("y")));
			positionEntity.setW(tenThousand(position.getString("w")));
			positionEntity.setH(tenThousand(position.getString("h")));
			JSONObject data = position.getJSONObject("data");
			if(data != null){
				positionEntity.setPictureType(PictureType.valueOf(data.getString("pictureType")));
				positionEntity.setPollingTime(data.getString("pollingTime"));
			}
			positionEntity.setUpdateTime(new Date());
			positionEntity.setCombineVideoId(entity.getId());
			positionEntities.add(positionEntity);
		}
		combineVideoPositionDao.save(positionEntities);
		List<CombineVideoSrcPO> srcEntities = new ArrayList<CombineVideoSrcPO>();
		for(int i=0; i<positions.size(); i++){
			JSONObject position = positions.getJSONObject(i);
			JSONObject data = position.getJSONObject("data");
			if(data == null) continue;
			if(data.containsKey("src") && data.get("src")!=null){
				JSONArray srcs = data.getJSONArray("src");
				for(int j=0; j<srcs.size(); j++){
					JSONObject src = srcs.getJSONObject(j);
					CombineVideoSrcPO srcEntity = new CombineVideoSrcPO();
					srcEntity.setSrcId(src.getString("id"));
					srcEntity.setCombineVideoSrcType(CombineVideoSrcType.valueOf(src.getString("type")));
					srcEntity.setSerial(j);
					srcEntity.setUpdateTime(new Date());
					for(CombineVideoPositionPO positionEntity:positionEntities){
						if(positionEntity.getSerialnum() == position.getIntValue("serialNum")){
							srcEntity.setCombineVideoPositionId(positionEntity.getId());
							break;
						}
					}
					srcEntities.add(srcEntity);
				}
			}
		}
		combineVideoSrcDao.save(srcEntities);
		return new CombineVideoVO().set(entity);
	}
	
	/**
	 * 分数转换万分比分子<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 上午10:36:38
	 * @param String fraction 分数
	 * @return 万分比分子
	 */
	private String tenThousand(String fraction) throws Exception{
		String[] values = fraction.split("/");
		//分子
		int molecule = Integer.parseInt(values[0]);
		//分母
		int denominator = Integer.parseInt(values[1]);
		return String.valueOf(molecule*10000/denominator);
	}
	
	/**
	 * 删除合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午5:50:22
	 * @param Long id 合屏id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		CombineVideoPO entity = combineVideoDao.findOne(id);
		if(entity != null){
			combineVideoDao.delete(entity);
			List<CombineVideoPositionPO> existPositionEntities = combineVideoPositionDao.findByCombineVideoId(entity.getId());
			if(existPositionEntities!=null && existPositionEntities.size()>0){
				List<Long> existPositionIds = new ArrayList<Long>();
				for(CombineVideoPositionPO existPositionEntity:existPositionEntities){
					existPositionIds.add(existPositionEntity.getId());
				}
				combineVideoPositionDao.deleteInBatch(existPositionEntities);
				List<CombineVideoSrcPO> existSrcEntities = combineVideoSrcDao.findByCombineVideoPositionIdIn(existPositionIds);
				if(existSrcEntities!=null && existSrcEntities.size()>0){
					combineVideoSrcDao.deleteInBatch(existSrcEntities);
				}
			}
		}
	}
	
}
