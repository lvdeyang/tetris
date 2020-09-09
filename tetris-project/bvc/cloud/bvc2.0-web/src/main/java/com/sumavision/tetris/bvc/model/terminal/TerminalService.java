package com.sumavision.tetris.bvc.model.terminal;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;

@Service
public class TerminalService {

	@Autowired
	private TerminalDAO terminalDao;
	
	/**
	 * 添加终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午3:14:38
	 * @param String name 终端名称
	 * @param String typeName 类型名称
	 * @return TerminalVO 终端
	 */
	public TerminalVO create(String name, String typeName) throws Exception{
		TerminalPO entity = new TerminalPO();
		entity.setName(name);
		if(typeName != null){
			entity.setType(TerminalType.fromName(typeName));
		}
		entity.setUpdateTime(new Date());
		terminalDao.save(entity);
		return new TerminalVO().set(entity);
	}
	
	/**
	 * 修改终端名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月8日 上午9:37:28
	 * @param Long id 终端id
	 * @param String name 名称
	 * @param String typeName 类型名称
	 * @return TerminalVO 终端
	 */
	public TerminalVO edit(Long id, String name, String typeName) throws Exception{
		TerminalPO entity = terminalDao.findOne(id);
		if(entity == null){
			throw new TerminalNotFoundException(id);
		}
		entity.setName(name);
		if(typeName != null){
			entity.setType(TerminalType.fromName(typeName));
		}
		entity.setUpdateTime(new Date());
		terminalDao.save(entity);
		return new TerminalVO().set(entity);
	}
	
	/**
	 * 删除终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月8日 上午9:43:50
	 * @param Long id 终端id
 	 * @return TerminalVO 终端
	 */
	public TerminalVO delete(Long id) throws Exception{
		TerminalPO entity = terminalDao.findOne(id);
		if(entity != null){
			terminalDao.delete(entity);
		}
		return new TerminalVO().set(entity);
	}
	
}
