package com.sumavision.tetris.bvc.model.agenda;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;

@Service
public class AgendaService {

	@Autowired
	private AgendaDAO agendaDao;
	
	/**
	 * 添加议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:38:25
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Integer volume 音量
	 * @param String audioOperationType 音频操作类型
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO add(
			String name,
			String remark,
			Integer volume,
			String audioOperationType) throws Exception{
		AgendaPO agenda = new AgendaPO();
		agenda.setName(name);
		agenda.setRemark(remark);
		agenda.setVolume(volume);
		agenda.setAudioOperationType(AudioOperationType.valueOf(audioOperationType));
		agenda.setUpdateTime(new Date());
		agendaDao.save(agenda);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 修改议程信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:42:26
	 * @param Long id 议程id
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Integer volume 音量
	 * @param String audioOperationType 音频操作类型
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO edit(
			Long id,
			String name,
			String remark,
			Integer volume,
			String audioOperationType) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda == null){
			throw new AgendaNotFoundException(id);
		}
		agenda.setName(name);
		agenda.setRemark(remark);
		agenda.setVolume(volume);
		agenda.setAudioOperationType(AudioOperationType.valueOf(audioOperationType));
		agenda.setUpdateTime(new Date());
		agendaDao.save(agenda);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 删除议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:43:30
	 * @param Long id 议程id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda != null){
			agendaDao.delete(agenda);
		}
	}
	
}
