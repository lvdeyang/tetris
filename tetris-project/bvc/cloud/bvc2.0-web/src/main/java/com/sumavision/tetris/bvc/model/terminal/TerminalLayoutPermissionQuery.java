package com.sumavision.tetris.bvc.model.terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutVO;
import com.sumavision.tetris.bvc.model.terminal.screen.TerminalScreenDAO;
import com.sumavision.tetris.bvc.model.terminal.screen.TerminalScreenPO;

@Component
public class TerminalLayoutPermissionQuery {

	@Autowired
	private TerminalLayoutPermissionDAO terminalLayoutPermissionDao;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private TerminalScreenDAO terminalScreenDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	/**
	 * 查询终端可添加的布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午10:27:47
	 * @param Long terminalId 终端id
	 * @return List<LayoutVO> 布局列表
	 */
	public List<LayoutVO> queryLayouts(Long terminalId) throws Exception{
		List<LayoutVO> layouts = new ArrayList<LayoutVO>();
		List<TerminalScreenPO> screens = terminalScreenDao.findByTerminalId(terminalId);
		if(screens==null || screens.size()<=0) return layouts;
		List<LayoutPositionPO> positions = layoutPositionDao.findAll();
		if(positions==null || positions.size()<=0) return layouts;
		Map<Long, List<LayoutPositionPO>> positionMap = new HashMap<Long, List<LayoutPositionPO>>();
		for(LayoutPositionPO position:positions){
			if(!positionMap.containsKey(position.getLayoutId())){
				positionMap.put(position.getLayoutId(), new ArrayList<LayoutPositionPO>());
			}
			positionMap.get(position.getLayoutId()).add(position);
		}
		List<Long> layoutIds = new ArrayList<Long>();
		Set<Long> mapKeys = positionMap.keySet();
		for(Long mapKey:mapKeys){
			List<LayoutPositionPO> values = positionMap.get(mapKey);
			if(values.size() > screens.size()) continue;
			boolean useable = true;
			for(LayoutPositionPO value:values){
				boolean finded = false;
				for(TerminalScreenPO screen:screens){
					if(value.getScreenPrimaryKey().equals(screen.getScreenPrimaryKey())){
						finded = true;
						break;
					}
				}
				if(!finded){
					useable = false;
					break;
				}
			}
			if(useable) layoutIds.add(mapKey);
		}
		List<LayoutPO> layoutEntities = layoutDao.findAll(layoutIds);
		for(LayoutPO layoutEntity:layoutEntities){
			layouts.add(new LayoutVO().set(layoutEntity));
		}
		return layouts;
	}
	
	/**
	 * 查询终端布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午10:24:48
	 * @param Long terminalId 终端id
	 * @return List<TerminalLayoutPermissionVO> 布局列表
	 */
	public List<TerminalLayoutPermissionVO> load(Long terminalId) throws Exception{
		List<TerminalLayoutPermissionVO> layouts = new ArrayList<TerminalLayoutPermissionVO>();
		List<TerminalLayoutPermissionPO> permissions = terminalLayoutPermissionDao.findByTerminalId(terminalId);
		if(permissions!=null && permissions.size()>0){
			List<Long> layoutIds = new ArrayList<Long>();
			for(TerminalLayoutPermissionPO permission:permissions){
				layoutIds.add(permission.getLayoutId());
			}
			List<LayoutPO> layoutEntities = layoutDao.findAll(layoutIds);
			for(TerminalLayoutPermissionPO permission:permissions){
				TerminalLayoutPermissionVO layout = new TerminalLayoutPermissionVO().set(permission);
				layouts.add(layout);
				if(layoutEntities!=null && layoutEntities.size()>0){
					for(LayoutPO layoutEntity:layoutEntities){
						if(layoutEntity.getId().equals(permission.getLayoutId())){
							layout.setLayoutName(layoutEntity.getName());
							break;
						}
					}
				}
			}
		}
		return layouts;
	}
	
}
