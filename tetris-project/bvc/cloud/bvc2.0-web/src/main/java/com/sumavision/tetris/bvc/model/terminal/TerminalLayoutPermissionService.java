package com.sumavision.tetris.bvc.model.terminal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPO;

@Service
public class TerminalLayoutPermissionService {

	@Autowired
	private TerminalLayoutPermissionDAO terminalLayoutPermissionDao;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	/**
	 * 添加布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午10:59:02
	 * @param Long terminalId 终端id
	 * @param JSONArray layoutIds 布局id列表
	 * @return List<TerminalLayoutPermissionVO> 布局列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<TerminalLayoutPermissionVO> add(
			Long terminalId,
			String layoutIds) throws Exception{
		List<Long> ids = JSON.parseArray(layoutIds, Long.class);
		List<TerminalLayoutPermissionVO> layouts = new ArrayList<TerminalLayoutPermissionVO>();
		List<TerminalLayoutPermissionPO> permissions = new ArrayList<TerminalLayoutPermissionPO>();
		for(Long id:ids){
			TerminalLayoutPermissionPO permission = new TerminalLayoutPermissionPO();
			permission.setTerminalId(terminalId);
			permission.setLayoutId(id);
			permission.setUpdateTime(new Date());
			permissions.add(permission);
		}
		terminalLayoutPermissionDao.save(permissions);
		List<LayoutPO> layoutEntities = layoutDao.findAll(ids);
		for(TerminalLayoutPermissionPO permission:permissions){
			TerminalLayoutPermissionVO layout = new TerminalLayoutPermissionVO().set(permission);
			layouts.add(layout);
			for(LayoutPO layoutEntity:layoutEntities){
				if(layoutEntity.getId().equals(permission.getLayoutId())){
					layout.setLayoutName(layoutEntity.getName());
					break;
				}
			}
		}
		return layouts;
	}
	
	/**
	 * 移除终端布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月28日 上午11:11:24
	 * @param Long id 布局id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		TerminalLayoutPermissionPO permission = terminalLayoutPermissionDao.findOne(id);
		if(permission != null){
			terminalLayoutPermissionDao.delete(permission);
		}
	}
	
}
