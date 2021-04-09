package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.terminal.hall.exception.ConferenceHallNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.util.BaseUtils;
import com.sumavision.tetris.orm.exception.ErrorTypeException;

@Service
public class ConferenceHallService {

	@Autowired
	private ConferenceHallRolePermissionDAO conferenceHallRolePermissionDao;

	@Autowired
	private PrivilegeDAO privilegeDao;

	@Autowired
	private PageInfoDAO pageInfoDao;

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
	
	
	public List<ConferenceHallPO> addBatchForPO(
			Long terminalId,
			JSONArray bundles) throws Exception{
		
		List<String> bundleIds = new ArrayList<String>();
		for(int i=0; i<bundles.size(); i++){
			bundleIds.add(bundles.getJSONObject(i).getString("bundleId"));
		}
		List<BundlePO> bundleEntities = bundleDao.findByBundleIdIn(bundleIds);
		
		List<TerminalBundlePO> terminalBundles = terminalBundleDao.findByTerminalId(terminalId);
		TerminalBundlePO terminalBundle = terminalBundles.get(0);
		List<ConferenceHallPO> halls = new ArrayList<ConferenceHallPO>();
		List<TerminalBundleConferenceHallPermissionPO> permissions = new ArrayList<TerminalBundleConferenceHallPermissionPO>();
		Map<BundlePO, ConferenceHallPO> bundleHallMapper = new HashMap<BundlePO, ConferenceHallPO>();
		for(BundlePO bundleEntity:bundleEntities){
			ConferenceHallPO hall = new ConferenceHallPO();
			hall.setFromDevice(true);
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
		
		//建立PageInfoPO
		List<PageInfoPO> pageInfos = new ArrayList<PageInfoPO>();
		for(ConferenceHallPO hall : halls){
			PageInfoPO pageInfo = new PageInfoPO(hall.getId().toString(), terminalId, GroupMemberType.MEMBER_HALL);
			pageInfos.add(pageInfo);
		}
		pageInfoDao.save(pageInfos);
		
		//复制权限
		List<ConferenceHallRolePermissionPO> pers = new ArrayList<ConferenceHallRolePermissionPO>();
		for(BundlePO bundle : bundleHallMapper.keySet()){
			ConferenceHallPO hall = bundleHallMapper.get(bundle);
			List<PrivilegePO> privileges = privilegeDao.findEffectiveByBundleId(bundle.getBundleId() + "-%");
			for(PrivilegePO privilege : privileges){
				try{
					ConferenceHallRolePermissionPO per = new ConferenceHallRolePermissionPO();
					String roleIdStr = privilege.getName().split("-privilege-")[0].replace("roleId-", "");
					per.setRoleId(Long.parseLong(roleIdStr));
					per.setConferenceHallId(hall.getId());
					String code = privilege.getResourceIndentity().replace(bundle.getBundleId() + "-", "-");
					PrivilegeType type = PrivilegeType.fromCode(code);
					per.setPrivilegeType(type);
					pers.add(per);
				}catch(ErrorTypeException e){
					System.out.println(bundle.getBundleId() + e.getMessage());
				}catch(Exception e){
					System.out.println(bundle.getBundleId() + " 设备转会场，复制权限出错：");
					e.printStackTrace();
				}
			}
		}
		conferenceHallRolePermissionDao.save(pers);		
		
		return halls;
	}
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
		
		TerminalPO terminal = terminalDao.findOne(terminalId);
		
		List<ConferenceHallPO> hallPOs = addBatchForPO(terminalId, bundles);
		
		List<ConferenceHallVO> hallVOs = ConferenceHallVO.getConverter(ConferenceHallVO.class).convert(hallPOs, ConferenceHallVO.class);
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
		
		PageInfoPO pageInfo = new PageInfoPO(entity.getId().toString(), terminalId, GroupMemberType.MEMBER_HALL);
		pageInfoDao.save(pageInfo);
		
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
		
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalIdAndGroupMemberType(id.toString(), entity.getTerminalId(), GroupMemberType.MEMBER_HALL);
		if(pageInfo != null){
			pageInfoDao.delete(pageInfo);
		}
	}
	
	/**
	 * 将设备转换为会场<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月7日 下午1:06:50
	 * @param bundleList
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<ConferenceHallPO> bundleExchangeToHall(List<BundlePO> bundleList) throws Exception{
		
		//注释部分是将ConferenceHallPO转ConferenceHallVO
//		List<ConferenceHallVO> bundleToHallVOList = new ArrayList<ConferenceHallVO>();
		List<ConferenceHallPO> bundleToHallList = new ArrayList<ConferenceHallPO>();
		//<terminalId, List<BundelPO>>
		Map<Long, List<BundlePO>> needCreateHallMap = new HashMap<Long, List<BundlePO>>();
		
		List<TerminalType> terminalTypes =  bundleList.stream().map(BundlePO::getDeviceModel).collect(Collectors.toSet()).stream().map(model->{
			try {
				return TerminalType.fromDeviceModel(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).filter(BaseUtils::objectIsNotNull).collect(Collectors.toList());
		Map<String, TerminalPO> deviceModelAndTerminalMap = terminalDao.findByTypeIn(terminalTypes).stream().collect(Collectors.toMap(terminal->{return terminal.getType().getDevicModel();}, Function.identity()));
		
		for(BundlePO bundle : bundleList){
			
			//先查找有没有：根据bundleId、bundleType查到TerminalBundleConferenceHallPermissionPO，然后验证对应的ConferenceHallPO是否还关联了其它设备
			List<TerminalBundleConferenceHallPermissionPO> permissionList = terminalBundleConferenceHallPermissionDao.findByBundleTypeAndBundleId(bundle.getDeviceModel(), bundle.getBundleId());
			List<Long> conferenceHallIdList = permissionList.stream().map(TerminalBundleConferenceHallPermissionPO::getConferenceHallId).collect(Collectors.toList());
			Map<Long, ConferenceHallPO> hallIdAndConferenceHallMap = conferenceHallDao.findAll(conferenceHallIdList).stream().collect(Collectors.toMap(ConferenceHallPO::getId, Function.identity()));
			Map<Long, List<TerminalBundleConferenceHallPermissionPO>> hallIdAndHallPermissionMap = 
					terminalBundleConferenceHallPermissionDao.findAll(conferenceHallIdList).stream().collect(Collectors.groupingBy(TerminalBundleConferenceHallPermissionPO::getConferenceHallId));
			
			//查找会长只有一个设备，并且该会场还是设备包装为的会场。
			boolean exist = false;
			for(Entry<Long, List<TerminalBundleConferenceHallPermissionPO>> entry : hallIdAndHallPermissionMap.entrySet()){
				if(entry.getValue().size() == 1 && hallIdAndConferenceHallMap.get(entry.getKey()).getFromDevice() != null && hallIdAndConferenceHallMap.get(entry.getKey()).getFromDevice()){
					ConferenceHallPO existHall = hallIdAndConferenceHallMap.get(entry.getKey());
//					TerminalPO terminal = deviceModelAndTerminalMap.get(existHall.getTerminalId());
//					List<ConferenceHallVO> hallVoList = ConferenceHallVO.getConverter(ConferenceHallVO.class).convert(new ArrayListWrapper<ConferenceHallPO>().add(existHall).getList(), ConferenceHallVO.class).stream().map(hallVo->{
//						return hallVo.setTerminalName(terminal.getName());
//					}).collect(Collectors.toList());
//					bundleToHallVOList.addAll(hallVoList);
					bundleToHallList.add(existHall);
					exist = true;
				}
			}
			
			if(!exist){
				TerminalPO terminal = deviceModelAndTerminalMap.get(bundle.getDeviceModel());
				
				if(needCreateHallMap.get(terminal.getId()) == null){
					needCreateHallMap.put(terminal.getId(), new ArrayList<BundlePO>());
				}
				needCreateHallMap.get(terminal.getId()).add(bundle);
			}
		}
		
		for(Entry<Long, List<BundlePO>> entry : needCreateHallMap.entrySet()){
			JSONArray jsonArray = new JSONArray();
			jsonArray.addAll(entry.getValue());
			List<ConferenceHallPO> newHallList = addBatchForPO(entry.getKey(), jsonArray);
//			TerminalPO terminal = deviceModelAndTerminalMap.get(entry.getKey());
//			List<ConferenceHallVO> newhallVOs = ConferenceHallVO.getConverter(ConferenceHallVO.class).convert(newHallList, ConferenceHallVO.class).stream().map(hallVo->{
//				return hallVo.setTerminalName(terminal.getName());
//			}).collect(Collectors.toList());
//			bundleToHallVOList.addAll(newhallVOs);
			bundleToHallList.addAll(newHallList);
		}
		
		return bundleToHallList;
//		return bundleToHallVOList;
	}
	
	/**
	 * 从设备找到设备包装的会场<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月8日 上午10:22:04
	 * @param bundle
	 * @throws Exception
	 */
	public ConferenceHallPO findConferenceHallPackingByBundle(BundlePO bundle) throws Exception{
		
		List<TerminalBundleConferenceHallPermissionPO> permissionList = terminalBundleConferenceHallPermissionDao.findByBundleTypeAndBundleId(bundle.getDeviceModel(), bundle.getBundleId());
		List<Long> conferenceHallIdList = permissionList.stream().map(TerminalBundleConferenceHallPermissionPO::getConferenceHallId).collect(Collectors.toList());
		
		Map<Long, ConferenceHallPO> hallIdAndConferenceHallMap = conferenceHallDao.findAll(conferenceHallIdList).stream().collect(Collectors.toMap(ConferenceHallPO::getId, Function.identity()));
		Map<Long, List<TerminalBundleConferenceHallPermissionPO>> hallIdAndHallPermissionMap = 
				terminalBundleConferenceHallPermissionDao.findAll(conferenceHallIdList).stream().collect(Collectors.groupingBy(TerminalBundleConferenceHallPermissionPO::getConferenceHallId));
		
		for(Entry<Long, List<TerminalBundleConferenceHallPermissionPO>> entry : hallIdAndHallPermissionMap.entrySet()){
			if(entry.getValue().size() == 1){
				return hallIdAndConferenceHallMap.get(entry.getKey());
			}
		}
		return null;
	}
}
