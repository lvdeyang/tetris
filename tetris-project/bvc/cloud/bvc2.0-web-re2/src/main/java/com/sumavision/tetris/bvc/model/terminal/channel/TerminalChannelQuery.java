package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenChannelPermissionDAO;

@Component
public class TerminalChannelQuery {

	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;

	@Autowired
	private TerminalPhysicalScreenChannelPermissionDAO terminalPhysicalScreenChannelPermissionDAO;
	
	@Autowired
	private TerminalChannelBundleChannelPermissionDAO terminalChannelBundleChannelPermissionDAO;
	
	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDAO;
	
	/**
	 * 根据终端和类型查询终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 上午11:13:42
	 * @param Long terminalId 终端id
	 * @param String type 终端通道类型
	 * @return List<TerminalChannelVO> 通道列表
	 */
	public List<TerminalChannelVO> loadByType(Long terminalId, String type) throws Exception{
		List<TerminalChannelPO> entities = terminalChannelDao.findByTerminalIdAndTypeOrderByTypeAscNameAsc(terminalId, TerminalChannelType.valueOf(type));
		return TerminalChannelVO.getConverter(TerminalChannelVO.class).convert(entities, TerminalChannelVO.class);
	}
	
	/**
	 * 查询终端下的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:01:59
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelVO> 通道列表
	 */
	public List<TerminalChannelVO> load(Long terminalId) throws Exception{
		List<TerminalChannelPO> channelEntities = terminalChannelDao.findByTerminalIdOrderByTypeAscNameAsc(terminalId);
		return TerminalChannelVO.getConverter(TerminalChannelVO.class).convert(channelEntities, TerminalChannelVO.class);
	} 
	
	/**
	 * 查询终端下的视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:01:59
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelVO> 视频解码通道列表
	 */
	/*public List<TerminalChannelVO> loadVideoDecode(Long terminalId) throws Exception{
		List<TerminalChannelPO> channelEntities = terminalChannelDao.findByTerminalIdAndTypeOrderByTypeAscNameAsc(terminalId, TerminalChannelType.VIDEO_DECODE);
		Set<Long> terminalBundleIds = new HashSet<Long>();
		if(channelEntities!=null && channelEntities.size()>0){
			for(TerminalChannelPO channel:channelEntities){
				terminalBundleIds.add(channel.getTerminalBundleId());
			}
			List<TerminalBundlePO> bundleEntities =terminalBundleDao.findAll(terminalBundleIds);
			List<TerminalChannelVO> channels = TerminalChannelVO.getConverter(TerminalChannelVO.class).convert(channelEntities, TerminalChannelVO.class);
			if(bundleEntities!=null && bundleEntities.size()>0){
				for(TerminalChannelVO channel:channels){
					for(TerminalBundlePO bundle:bundleEntities){
						if(channel.getTerminalBundleId().equals(bundle.getId())){
							channel.setTerminalBundleName(bundle.getName());
							break;
						}
					}
				}
			}
			return channels;
		}
		return null;
	}*/
	
	/**
	 * 查询通道以及所关联的设备类型通道（不关联屏幕）<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月28日 下午8:41:18
	 * @param terminalId 终端id
	 * @return List<TerminalChannelVO> 视频解码通道列表
	 */
	/*public List<TerminalChannelVO> loadChannelPermission(Long terminalId)throws Exception{
		List<TerminalChannelVO> terminalChannelVOs =  new ArrayList<TerminalChannelVO>();
		List<TerminalChannelPO> terminalChannelPOs = terminalChannelDao.findByTerminalId(terminalId);
		Set<Long> screenchannelIds = new HashSet<Long>();
		//去除已关联屏幕大的通道
		List<TerminalPhysicalScreenChannelPermissionPO> terminalPhysicalScreenChannelPermissionPOs = terminalPhysicalScreenChannelPermissionDAO.findAll();
		if (!terminalChannelPOs.isEmpty() && terminalChannelPOs != null && !terminalPhysicalScreenChannelPermissionPOs.isEmpty() && terminalPhysicalScreenChannelPermissionPOs != null){
			for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
				for (TerminalPhysicalScreenChannelPermissionPO terminalPhysicalScreenChannelPermissionPO : terminalPhysicalScreenChannelPermissionPOs) {
					if(terminalChannelPO.getId() == terminalPhysicalScreenChannelPermissionPO.getTerminalChannelId()){
						terminalChannelPOs.remove(terminalChannelPO);
					}
				}
			}
		}
		//没有关联屏幕的通道
		Set<Long> terminalChannelIds = new HashSet<Long>();
		if(!terminalChannelPOs.isEmpty() && terminalChannelPOs != null){
			for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
				terminalChannelIds.add(terminalChannelPO.getId());
			}
			TerminalChannelVO terminalChannelVO  = new TerminalChannelVO();
			for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
				terminalChannelVO.set(terminalChannelPO);
			}
			List<TerminalChannelBundleChannelPermissionDTO>  bundleChannelPermissionDTOs = terminalChannelBundleChannelPermissionDAO.findByPermission(terminalChannelIds);
			if (!bundleChannelPermissionDTOs.isEmpty()&&bundleChannelPermissionDTOs != null) {
				List<TerminalChannelBundleChannelPermissionDTO> children = new ArrayList<TerminalChannelBundleChannelPermissionDTO>();
				for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
					for (TerminalChannelBundleChannelPermissionDTO terminalChannelBundleChannelPermissionDTO : bundleChannelPermissionDTOs) {
						if(terminalChannelPO.getId() == terminalChannelBundleChannelPermissionDTO.getTerminalChannelId()){
							children.add(terminalChannelBundleChannelPermissionDTO);
						}
					}
				}
				terminalChannelVO.setChildren(children);
			}
			terminalChannelVOs.add(terminalChannelVO);
		}
		
		//		List<TerminalChannelVO> terminalChannelVOs = new ArrayList<TerminalChannelVO>();
//		List<TerminalChannelPO> terminalChannelPOs = terminalChannelDao.findByTerminalId(terminalId);
//		Set<Long> terminalChannelId = new HashSet<Long>();
//		if(!terminalChannelPOs.isEmpty() && terminalChannelPOs!= null){
//			for(TerminalChannelPO terminalChannelPO:terminalChannelPOs){
//				terminalChannelId.add(terminalChannelPO.getId());
//			}
//			List<TerminalPhysicalScreenChannelPermissionPO> terminalPhysicalScreenChannelPermissionPOs = terminalPhysicalScreenChannelPermissionDAO.findByTerminalChannelIdIn(terminalChannelId);
//			if (!terminalPhysicalScreenChannelPermissionPOs.isEmpty() && terminalPhysicalScreenChannelPermissionPOs!=null) {
//				for (TerminalChannelPO terminalChannelPO:terminalChannelPOs) {
//					for (TerminalPhysicalScreenChannelPermissionPO terminalPhysicalScreenChannelPermissionPO : terminalPhysicalScreenChannelPermissionPOs) {
//						if (terminalChannelPO.getId() == terminalPhysicalScreenChannelPermissionPO.getTerminalChannelId()) {
//							terminalChannelPOs.remove(terminalChannelPO);
//						}
//					}
//				}
//			}
//		}
//		Set<Long> channelIdSet = new HashSet<Long>();
//		Set<Long> bundleChannelId = new HashSet<Long>();
// 		if(!terminalChannelPOs.isEmpty() && terminalChannelPOs!=null){
//			for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
//				channelIdSet.add(terminalChannelPO.getId());
//			}
//			List<TerminalChannelBundleChannelPermissionPO> terminalChannelBundleChannelPermissionPOs = terminalChannelBundleChannelPermissionDAO.findByTerminalChannelIdIn(channelIdSet);
//			Set<Long> permissionIds = new HashSet<Long>();
//			if (!terminalChannelBundleChannelPermissionPOs.isEmpty() && terminalChannelBundleChannelPermissionPOs != null) {
//				for (TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPO : terminalChannelBundleChannelPermissionPOs) {
//					permissionIds.add(terminalChannelBundleChannelPermissionPO.getTerminalBundleChannelId());
//				}
//			}
//			List<TerminalBundleChannelPO> terminalBundleChannelPOs = terminalBundleChannelDAO.findByIdIn(permissionIds);
//			if (!terminalChannelBundleChannelPermissionPOs.isEmpty() && terminalChannelBundleChannelPermissionPOs != null) {
//				for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
//					TerminalChannelVO terminalChannelVO = new TerminalChannelVO();
//					terminalChannelVO.setId(terminalChannelPO.getId())
//					.setName(terminalChannelPO.getName())
//					.setType(terminalChannelPO.getType().toString())
//					.setTerminalId(terminalChannelPO.getTerminalId());
//					List<TerminalChannelBundleChannelPermissionVO> terminalChannelBundleChannelPermissionVOs = new ArrayList<TerminalChannelBundleChannelPermissionVO>();
//					for (TerminalChannelBundleChannelPermissionPO terminalChannelBundleChannelPermissionPO : terminalChannelBundleChannelPermissionPOs) {
//						if (terminalChannelPO.getId() == terminalChannelBundleChannelPermissionPO.getTerminalChannelId()) {
//							TerminalChannelBundleChannelPermissionVO terminalChannelBundleChannelPermissionVO = new TerminalChannelBundleChannelPermissionVO();
//							for (TerminalBundleChannelPO terminalBundleChannelPO : terminalBundleChannelPOs) {
//								if (terminalChannelBundleChannelPermissionPO.getTerminalBundleChannelId() ==terminalBundleChannelPO.getId()) {
//									terminalChannelBundleChannelPermissionVO.setName(terminalBundleChannelPO.get)
//								}
//							}
//							terminalChannelBundleChannelPermissionVO.setId(terminalChannelBundleChannelPermissionPO.getId())
//							.setTerminalBundleId(terminalChannelBundleChannelPermissionPO.getTerminalBundleId())
//							.setTerminalChannelId(terminalChannelBundleChannelPermissionPO.getTerminalChannelId())
//							.setChannelParamsType(terminalChannelBundleChannelPermissionPO.getChannelParamsType().toString());
//							terminalChannelBundleChannelPermissionVOs.add(terminalChannelBundleChannelPermissionVO);
//						}
//					}
//					terminalChannelVO.setChildren(terminalChannelBundleChannelPermissionVOs);
//					terminalChannelVOs.add(terminalChannelVO);
//				}
//				}else for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
//					TerminalChannelVO terminalChannelVO = new TerminalChannelVO();
//					terminalChannelVO.setId(terminalChannelPO.getId())
//					.setName(terminalChannelPO.getName())
//					.setType(terminalChannelPO.getType().toString())
//					.setTerminalId(terminalChannelPO.getTerminalId());
//					terminalChannelVOs.add(terminalChannelVO);
//					}
//		}
		return terminalChannelVOs;
	}*/
}
