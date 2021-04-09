package com.sumavision.bvc.device.command.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.command.group.dao.CommandGroupMessageStyleDAO;
import com.sumavision.bvc.command.group.message.CommandGroupMessageStylePO;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandMessageStyleServiceImpl 
* @Description: 指挥消息样式
* @author zsy
* @date 2019年11月19日 上午10:56:48 
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandMessageStyleServiceImpl {
	
	@Autowired
	private CommandGroupMessageStyleDAO commandGroupMessageStyleDao;
	
	/**
	 * 添加一个模板<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:27:49
	 * @param userId
	 * @param name
	 * @param fontFamily
	 * @param fontSize
	 * @param textDecoration
	 * @param color
	 * @param rollingSpeed
	 * @param rollingMode
	 * @param rollingLocation
	 * @param rollingTime
	 * @param rollingTimeUnlimited
	 * @return
	 * @throws Exception
	 */
	public CommandGroupMessageStylePO addStyleTemplate(
			Long userId,
			String name,
			String fontFamily,
		    String fontSize,
			String textDecoration,
			String color,
			String rollingSpeed,
			String rollingMode,
			String rollingLocation,
			String rollingTime,
			boolean rollingTimeUnlimited) throws Exception{
		
		CommandGroupMessageStylePO style = new CommandGroupMessageStylePO();
		style.edit(
				userId,				
				name,
				fontFamily,
				fontSize,
				textDecoration,
				color,
				rollingSpeed,
				rollingMode,
				rollingLocation,
				rollingTime,
				rollingTimeUnlimited);
		
		commandGroupMessageStyleDao.save(style);
		return style;
	}
	
	/**
	 * 修改一个模板<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:27:29
	 * @param id
	 * @param name
	 * @param fontFamily
	 * @param fontSize
	 * @param textDecoration
	 * @param color
	 * @param rollingSpeed
	 * @param rollingMode
	 * @param rollingLocation
	 * @param rollingTime
	 * @param rollingTimeUnlimited
	 * @throws Exception
	 */
	public void editStyleTemplate(
			Long id,
//			Long userId,
			String name,
			String fontFamily,
		    String fontSize,
			String textDecoration,
			String color,
			String rollingSpeed,
			String rollingMode,
			String rollingLocation,
			String rollingTime,
			boolean rollingTimeUnlimited) throws Exception{
		
		CommandGroupMessageStylePO style = commandGroupMessageStyleDao.findOne(id);
		style.edit(
				style.getUserId(),//创建者不变
				name,
				fontFamily,
				fontSize,
				textDecoration,
				color,
				rollingSpeed,
				rollingMode,
				rollingLocation,
				rollingTime,
				rollingTimeUnlimited);
		
		commandGroupMessageStyleDao.save(style);
	}
	
	/**
	 * 删除一个模板<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:27:06
	 * @param id
	 */
	public void removeStyleTemplate(Long id){
		commandGroupMessageStyleDao.delete(id);
	}
	
}
