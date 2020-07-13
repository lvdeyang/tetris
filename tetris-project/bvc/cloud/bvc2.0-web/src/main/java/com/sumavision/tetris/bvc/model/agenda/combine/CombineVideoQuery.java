package com.sumavision.tetris.bvc.model.agenda.combine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CombineVideoQuery {

	@Autowired
	@Qualifier("com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoDAO")
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineVideoPositionDAO combineVideoPositionDao;
	
	@Autowired
	private CombineVideoSrcDAO combineVideoSrcDao;
	
	/**
	 * 查询业务下的合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月2日 下午5:10:56
	 * @param Long businessId 业务id
	 * @param String businessType 业务类型
	 * @return List<CombineVideoVO> 合屏列表
	 */
	public List<CombineVideoVO> load(
			Long businessId,
			String businessType) throws Exception{
		List<CombineVideoPO> entities = combineVideoDao.findByBusinessIdAndBusinessType(businessId, CombineBusinessType.valueOf(businessType));
		return CombineVideoVO.getConverter(CombineVideoVO.class).convert(entities, CombineVideoVO.class);
	}
	
	/**
	 * 查询合屏分屏参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月7日 下午1:55:06
	 * @param Long combineVideoId 合屏id
	 * @return List<CombineVideoPositionVO> 分屏参数
	 */
	public List<CombineVideoPositionVO> loadPositions(
			Long combineVideoId) throws Exception{
		List<CombineVideoPositionPO> positionEntities =  combineVideoPositionDao.findByCombineVideoId(combineVideoId);
		List<CombineVideoSrcPO> srcEntities = null;
		if(positionEntities!=null && positionEntities.size()>0){
			List<Long> positionEntityIds = new ArrayList<Long>();
			for(CombineVideoPositionPO positionEntity:positionEntities){
				positionEntityIds.add(positionEntity.getId());
			}
			srcEntities = combineVideoSrcDao.findByCombineVideoPositionIdIn(positionEntityIds);
		}
		List<CombineVideoPositionVO> positions = new ArrayList<CombineVideoPositionVO>();
		if(positionEntities!=null && positionEntities.size()>0){
			for(CombineVideoPositionPO positionEntity:positionEntities){
				CombineVideoPositionVO position = new CombineVideoPositionVO().set(positionEntity);
				if(srcEntities!=null && srcEntities.size()>0){
					for(CombineVideoSrcPO srcEntity:srcEntities){
						if(srcEntity.getCombineVideoPositionId().equals(position.getId())){
							position.addSrc(new CombineVideoSrcVO().set(srcEntity));
						}
					}
				}
				position.sortSrc();
				positions.add(position);
			}
		}
		return positions;
	}
	
}
