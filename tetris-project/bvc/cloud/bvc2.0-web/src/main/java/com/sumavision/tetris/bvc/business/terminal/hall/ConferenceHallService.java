package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.tetris.bvc.business.terminal.hall.exception.ConferenceHallNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
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
	private FolderDao folderDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	/**
	 * 批量添加会场（直接绑定设备）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月4日 上午10:29:52
	 * @param Long terminalId 终端id
	 * @param JSONString bundles 设备列表
	 * @return List<ConferenceHallVO> 会场列表
	 */
	public List<ConferenceHallVO> addBatch(
			Long terminalId,
			JSONArray bundles) throws Exception{
		List<String> bundleIds = new ArrayList<String>();
		for(int i=0; i<bundles.size(); i++){
			bundleIds.add(bundles.getJSONObject(i).getString("bundleId"));
		}
		List<BundlePO> bundleEntities = bundleDao.findByBundleIdIn(bundleIds);
		TerminalPO terminal = terminalDao.findOne(terminalId);
		List<TerminalBundlePO> terminalBundles = terminalBundleDao.findByTerminalId(terminalId);
		TerminalBundlePO terminalBundle = terminalBundles.get(0);
		List<ConferenceHallPO> halls = new ArrayList<ConferenceHallPO>();
		List<TerminalBundleConferenceHallPermissionPO> permissions = new ArrayList<TerminalBundleConferenceHallPermissionPO>();
		Map<BundlePO, ConferenceHallPO> bundleHallMapper = new HashMap<BundlePO, ConferenceHallPO>();
		for(BundlePO bundleEntity:bundleEntities){
			ConferenceHallPO hall = new ConferenceHallPO();
			hall.setUpdateTime(new Date());
			hall.setName(bundleEntity.getBundleName());
			hall.setFolderId(bundleEntity.getFolderId());
			hall.setTerminalId(terminalId);
			halls.add(hall);
			bundleHallMapper.put(bundleEntity, hall);
		}
		conferenceHallDao.save(halls);
		for(BundlePO bundleEntity:bundleEntities){
			TerminalBundleConferenceHallPermissionPO permission = new TerminalBundleConferenceHallPermissionPO();
			permission.setUpdateTime(new Date());
			permission.setConferenceHallId(bundleHallMapper.get(bundleEntity).getId());
			permission.setTerminalBundleId(terminalBundle.getId());
			permission.setBundleId(bundleEntity.getBundleId());
			permission.setBundleName(bundleEntity.getBundleName());
			permission.setBundleType(bundleEntity.getDeviceModel());
			permissions.add(permission);
		}
		terminalBundleConferenceHallPermissionDao.save(permissions);
		List<ConferenceHallVO> hallVOs = ConferenceHallVO.getConverter(ConferenceHallVO.class).convert(halls, ConferenceHallVO.class);
		for(ConferenceHallVO hallVO:hallVOs){
			hallVO.setTerminalName(terminal.getName());
		}
		return hallVOs;
	}
	
	/**
	 * 添加会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午4:45:20
	 * @param String name 会场名称
	 * @param Long terminalId 终端类型id
	 * @param Long folderId 文件夹id
	 * @return ConferenceHallVO 会场
	 */
	@Transactional(rollbackFor = Exception.class)
	public ConferenceHallVO add(
			String name,
			Long terminalId,
			Long folderId) throws Exception{
		
		TerminalPO terminalEntity = terminalDao.findOne(terminalId);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalId);
		}
		ConferenceHallPO entity = new ConferenceHallPO();
		entity.setName(name);
		entity.setTerminalId(terminalId);
		entity.setFolderId(folderId);
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
	 * @param Long folderId 文件夹id
	 * @return ConferenceHallVO 会场
	 */
	@Transactional(rollbackFor = Exception.class)
	public ConferenceHallVO editName(
			Long id,
			String name,
			Long folderId) throws Exception{
		ConferenceHallPO entity = conferenceHallDao.findOne(id);
		if(entity == null){
			throw new ConferenceHallNotFoundException(id);
		}
		entity.setName(name);
		entity.setFolderId(folderId);
		conferenceHallDao.save(entity);
		TerminalPO terminalEntity = terminalDao.findOne(entity.getTerminalId());
		FolderPO folder = null;
		if(folderId != null) folder = folderDao.findOne(folderId);
		return new ConferenceHallVO().set(entity).setTerminalName(terminalEntity.getName()).setFolderName(folder==null?"":folder.getName());
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
