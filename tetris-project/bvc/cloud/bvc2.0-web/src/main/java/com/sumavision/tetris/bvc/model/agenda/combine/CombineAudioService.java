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
import com.sumavision.tetris.bvc.model.agenda.combine.exception.CombineAudioNotFoundException;

@Service
public class CombineAudioService {

	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	@Qualifier("com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioSrcDAO")
	private CombineAudioSrcDAO combineAudioSrcDao;
	
	/**
	 * 添加混音<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午3:46:21
	 * @param Long businessId 业务id
	 * @param String businessType 业务类型
	 * @param String name 混音名称
	 * @return CombineAudioVO 混音
	 */
	@Transactional(rollbackFor = Exception.class)
	public CombineAudioVO add(
			Long businessId,
			String businessType,
			String name) throws Exception{
		CombineAudioPO entity = new CombineAudioPO();
		entity.setName(name);
		entity.setBusinessId(businessId);
		entity.setBusinessType(CombineBusinessType.valueOf(businessType));
		entity.setUpdateTime(new Date());
		combineAudioDao.save(entity);
		return new CombineAudioVO().set(entity);
	}
	
	/**
	 * 修改混音名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午3:59:48
	 * @param Long id 混音id
	 * @param String name 名称
	 * @return CombineAudioVO 混音
	 */
	@Transactional(rollbackFor = Exception.class)
	public CombineAudioVO editName(
			Long id,
			String name) throws Exception{
		CombineAudioPO entity = combineAudioDao.findOne(id);
		if(entity == null){
			throw new CombineAudioNotFoundException(id);
		}
		entity.setName(name);
		combineAudioDao.save(entity);
		return new CombineAudioVO().set(entity);
	}
	
	/**
	 * 编辑混音源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月8日 上午11:48:47
	 * @param Long id 混音id
	 * @param JSONArray srcs 源列表
	 * @return CombineAudioVO 混音
	 */
	@Transactional(rollbackFor = Exception.class)
	public CombineAudioVO editSrcs(
			Long id,
			JSONArray srcs) throws Exception{
		CombineAudioPO entity = combineAudioDao.findOne(id);
		if(entity == null){
			throw new CombineAudioNotFoundException(id);
		}
		List<CombineAudioSrcPO> existSrcEntities = combineAudioSrcDao.findByCombineAudioId(entity.getId());
		if(existSrcEntities!=null && existSrcEntities.size()>0){
			combineAudioSrcDao.deleteInBatch(existSrcEntities);
		}
		List<CombineAudioSrcPO> srcEntities = new ArrayList<CombineAudioSrcPO>();
		for(int i=0; i<srcs.size(); i++){
			JSONObject src = srcs.getJSONObject(i);
			CombineAudioSrcPO srcEntity = new CombineAudioSrcPO();
			srcEntity.setSrcId(src.getString("id"));
			srcEntity.setCombineAudioSrcType(CombineAudioSrcType.valueOf(src.getString("type")));
			srcEntity.setCombineAudioId(entity.getId());
			srcEntity.setUpdateTime(new Date());
			srcEntities.add(srcEntity);
		}
		combineAudioSrcDao.save(srcEntities);
		return new CombineAudioVO().set(entity);
	}
	
	/**
	 * 删除混音<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午4:03:42
	 * @param Long id 混音id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		CombineAudioPO entity = combineAudioDao.findOne(id);
		if(entity != null){
			combineAudioDao.delete(entity);
			List<CombineAudioSrcPO> existSrcEntities = combineAudioSrcDao.findByCombineAudioId(entity.getId());
			if(existSrcEntities!=null && existSrcEntities.size()>0){
				combineAudioSrcDao.deleteInBatch(existSrcEntities);
			}
		}
	}
	
}
