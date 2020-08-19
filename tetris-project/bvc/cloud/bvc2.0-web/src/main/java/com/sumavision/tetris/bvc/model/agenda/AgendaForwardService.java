package com.sumavision.tetris.bvc.model.agenda;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Service
public class AgendaForwardService {

	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private AgendaForwardQuery agendaForwardQuery;
	
	/**
	 * 添加议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 上午10:11:51
	 * @param String type 议程转发类型
	 * @param String businessInfoType 议程转发业务类型
	 * @param String sourceType 源类型
	 * @param String sourceId 源id
	 * @param String audioSourceType 音频源类型
	 * @param String audioSourceId 音频源id
	 * @param String destinationType 目的类型
	 * @param String destinationId 目的id
	 * @param Long agendaId 议程id
	 * @return AgendaForwardVO 议程转发
	 */
	public AgendaForwardVO add(
			String type,
			String businessInfoType,
			String sourceType,
			String sourceId,
			String audioSourceType,
			String audioSourceId,
			String destinationType,
			String destinationId,
			Long agendaId) throws Exception{
		AgendaForwardPO entity = new AgendaForwardPO();
		entity.setType(AgendaForwardType.valueOf(type));
		if(businessInfoType!=null && !"".equals(businessInfoType)) entity.setBusinessInfoType(BusinessInfoType.fromName(businessInfoType));
		entity.setSourceType(sourceType==null?null:AgendaSourceType.valueOf(sourceType));
		entity.setSourceId(sourceId);
		entity.setAudioSourceType(audioSourceType==null?null:AgendaSourceType.valueOf(audioSourceType));
		entity.setAudioSourceId(audioSourceId);
		entity.setDestinationType(AgendaDestinationType.valueOf(destinationType));
		entity.setDestinationId(destinationId);
		entity.setAgendaId(agendaId);
		entity.setUpdateTime(new Date());
		agendaForwardDao.save(entity);
		return agendaForwardQuery.packForwards(new ArrayListWrapper<AgendaForwardPO>().add(entity).getList()).get(0);
	}
	
	/**
	 * 删除议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月9日 上午10:14:28
	 * @param Long id 转发id
	 */
	public void delete(Long id) throws Exception{
		AgendaForwardPO entity = agendaForwardDao.findOne(id);
		if(entity != null){
			agendaForwardDao.delete(entity);
		}
	}
	
}
