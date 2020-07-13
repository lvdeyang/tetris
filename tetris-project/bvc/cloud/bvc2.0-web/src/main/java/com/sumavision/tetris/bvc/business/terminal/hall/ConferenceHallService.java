package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.business.terminal.hall.exception.ConferenceHallNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;

@Service
public class ConferenceHallService {

	@Autowired
	private ConferenceHallDAO conferenceHallDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	/**
	 * 添加会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午4:45:20
	 * @param String name 会场名称
	 * @param Long terminalId 终端类型id
	 * @return ConferenceHallVO 会场
	 */
	@Transactional(rollbackFor = Exception.class)
	public ConferenceHallVO add(
			String name,
			Long terminalId) throws Exception{
		
		TerminalPO terminalEntity = terminalDao.findOne(terminalId);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalId);
		}
		ConferenceHallPO entity = new ConferenceHallPO();
		entity.setName(name);
		entity.setTerminalId(terminalId);
		entity.setUpdateTime(new Date());
		conferenceHallDao.save(entity);
		
		return new ConferenceHallVO().set(entity).setTerminalName(terminalEntity.getName());
	}
	
	/**
	 * 修改会场名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 上午11:14:43
	 * @param Long id 会场id
	 * @param String name 会场名称
	 * @return ConferenceHallVO 会场
	 */
	@Transactional(rollbackFor = Exception.class)
	public ConferenceHallVO editName(
			Long id,
			String name) throws Exception{
		ConferenceHallPO entity = conferenceHallDao.findOne(id);
		if(entity == null){
			throw new ConferenceHallNotFoundException(id);
		}
		entity.setName(name);
		conferenceHallDao.save(entity);
		TerminalPO terminalEntity = terminalDao.findOne(entity.getTerminalId());
		return new ConferenceHallVO().set(entity).setTerminalName(terminalEntity.getName());
	}
	
	/**
	 * 删除会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午4:47:04
	 * @param Long id 会场id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		ConferenceHallPO entity = conferenceHallDao.findOne(id);
		if(entity != null){
			conferenceHallDao.delete(entity);
		}
		List<TerminalBundleConferenceHallPermissionPO> permissions = terminalBundleConferenceHallPermissionDao.findByConferenceHallId(id);
		if(permissions!=null && permissions.size()>0){
			terminalBundleConferenceHallPermissionDao.deleteInBatch(permissions);
		}
	}
	
}
