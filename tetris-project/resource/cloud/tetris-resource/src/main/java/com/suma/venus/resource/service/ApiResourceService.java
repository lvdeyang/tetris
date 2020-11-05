package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.controller.ControllerBase;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ExtraInfoDao;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.vo.BundleTreeVO;
import com.suma.venus.resource.vo.BundleVO;
import com.suma.venus.resource.vo.G01BundleVO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApiResourceService extends ControllerBase{
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private WorkNodeDao workNodeDao;
	
	@Autowired
	private ExtraInfoDao extraInfoDao;
	

	/**
	 * 添加g01设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:07:29
	 * @param String bundleIp 设备ip
	 * @param String daId DA系统id
	 * @return G01BundleVO g01设备信息
	 */
	public G01BundleVO addG01Bundle(String bundleIp, String daId,String location,String group,String type) throws Exception{
		
		String identify = new StringBufferWrapper().append(daId)
				 								   .append("_")
				 								   .append(bundleIp)
				 								   .toString();
		
		BundlePO bundle = bundleDao.findByDeviceModelAndUsername("g01", identify);
		String string = location.replace("，", ",");
		String[] locat = string.split(",");
		String longitude = locat[0];
		String latitude = locat[1];
//		Long folderId = Long.parseLong(group);

		String[] folderStrings = group.split(",");
		List<FolderPO> folderPOs = folderDao.findAll();
		Long parentId = -1l;
		
		for(String name:folderStrings){
			for(FolderPO folderPO : folderPOs){
				if(folderPO.getName().equals(name) && parentId.equals(folderPO.getParentId())){
					parentId = folderPO.getId();
					break;
				}
			}
		}
				
		if(bundle != null){
			throw new BaseException(StatusCode.ERROR, "bundleIp为：" + bundleIp + "，daId为：" + daId + " 的设备已经存在！");
		}
		
		//创建G01设备
		BundlePO g01 = new BundlePO();
		g01.setBundleName(identify);
		g01.setUsername(identify);
		g01.setOnlinePassword(identify);
		g01.setBundleId(BundlePO.createBundleId());
//		g01.setDeviceModel("g01");
		g01.setBundleType("VenusTerminal");
		g01.setDeviceIp(bundleIp);
		g01.setOnlineStatus(ONLINE_STATUS.OFFLINE);
		g01.setLongitude(longitude);
		g01.setLatitude(latitude);
		g01.setDeviceModel(type);
		g01.setFolderId(parentId);
		
		
//		bundleService.configDefaultAbility(g01);
		
		bundleDao.save(g01);
		
		return new G01BundleVO().setBundleId(g01.getBundleId());
	}
	
	/**
	 * g01设备认证上线<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:09:32
	 * @param String bundleIp 设备ip
	 * @param String daId DA系统id
	 * @return G01BundleVO g01设备信息
	 */
	public G01BundleVO certifyG01Bundle(String bundleIp, String daId) throws Exception{
		
		String identify = new StringBufferWrapper().append(daId)
				 								   .append("_")
				 								   .append(bundleIp)
				 								   .toString();
		
		BundlePO bundle = bundleDao.findByDeviceModelAndUsername("g01", identify);
		if(bundle == null){
			throw new BaseException(StatusCode.ERROR, "bundleIp为：" + bundleIp + "，daId为：" + daId + " 的设备不存在！");
		}
		
		bundle.setOnlineStatus(ONLINE_STATUS.ONLINE);
		bundleDao.save(bundle);
		
		return new G01BundleVO().setBundleId(bundle.getBundleId());
	}
	
	/**
	 * 删除g01设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:17:35
	 * @param String bundleIp 设备ip
	 * @param String daId DA系统id
	 */
	public void deleteG01Bundle(String bundleId) throws Exception{
		
		BundlePO bundle = bundleDao.findByBundleId(bundleId);
		if(bundle == null){
			throw new BaseException(StatusCode.ERROR, "bundleId为：" + " 的设备不存在！");
		}
		
		bundleDao.delete(bundle);
	}
	
	/**
	 * 更新g01设备<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月24日 上午9:53:19
	 * @param String bundleId  设备id
	 * @param String location 设备地址，经纬度
	 * @param String group 设备分组
	 * @param String type 设备类型
	 * @return
	 */
	public G01BundleVO updateG01Bundle(String bundleId,String location,String group,String type)throws Exception{
		
		String string = location.replace("，", ",");
		String[] locat = string.split(",");
		String longitude = locat[0];
		String latitude = locat[1];
		
		String[] folderStrings = group.split(",");
		List<FolderPO> folderPOs = folderDao.findAll();
		Long parentId = -1l;
		
		for(String name:folderStrings){
			for(FolderPO folderPO : folderPOs){
				if(folderPO.getName().equals(name) && parentId.equals(folderPO.getParentId())){
					parentId = folderPO.getId();
					break;
				}
			}
		}
		
		BundlePO bundlePO = bundleDao.findByBundleId(bundleId);
		bundlePO.setLongitude(longitude);
		bundlePO.setLatitude(latitude);
		bundlePO.setFolderId(parentId);
		bundlePO.setDeviceModel(type);
		
		bundleDao.save(bundlePO);
		
		return new G01BundleVO().setBundleId(bundlePO.getBundleId());
	}
	
	/**
	 *查询emr设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月25日 上午8:39:47
	 * @return Map<String, Object> data emr设备
	 */
	public List<BundleTreeVO> queryBundle()throws Exception{
		
		List<String> deviceModel = new ArrayList<String>();
		deviceModel.add("environmental_monitor");
		deviceModel.add("network_device");
		deviceModel.add("ground_receiver");
		deviceModel.add("adapter");
		deviceModel.add("subnet");
		deviceModel.add("multiplexer");
		deviceModel.add("Transmitter");
		deviceModel.add("PC");
		deviceModel.add("satellite_modulator");
		deviceModel.add("satellite_receiver");
		deviceModel.add("antenna");
		deviceModel.add("Exciter");
		deviceModel.add("amplifier");
		deviceModel.add("decoder");
		deviceModel.add("encoder");
		deviceModel.add("stream_dispatch");
		deviceModel.add("IPQAM");
		deviceModel.add("channel_break");
		deviceModel.add("ts_monitor");
		deviceModel.add("transmitter");
		deviceModel.add("5G");
		deviceModel.add("default");//默认类型default
		
		List<FolderPO> folders = folderDao.findAll();
		if (folders.isEmpty()) {
			throw new Exception("数据库错误：不存在根节点");
		}
		List<BundlePO> bundlePOs = bundleDao.findByDeviceModelIn(deviceModel);
		
		List<BundleTreeVO> tree = new LinkedList<>();
		
		List<FolderPO> roots = findRoots(folders);
		for(FolderPO root:roots){
			BundleTreeVO _root = new BundleTreeVO().set(root)
											   .setChildren(new ArrayList<BundleTreeVO>());
			tree.add(_root);
			recursionFolder(_root, folders, bundlePOs);
		}
		return tree;
	} 
	
	public void recursionFolder(
			BundleTreeVO root, 
			List<FolderPO> folders, 
			List<BundlePO> bundles){
		
		//往里装文件夹
		for(FolderPO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getGroupId())){
				BundleTreeVO folderNode = new BundleTreeVO().set(folder)
														.setChildren(new ArrayList<BundleTreeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles);
			}
		}
		
		//往里装设备
		if(bundles!=null && bundles.size()>0){
			for(BundlePO bundle:bundles){
				if(bundle.getFolderId()!=null && root.getGroupId().equals(bundle.getFolderId().toString())){
					BundleTreeVO bundleNode = new BundleTreeVO().set(bundle)
															.setChildren(new ArrayList<BundleTreeVO>());
					root.getChildren().add(bundleNode);
				}
			}
		}
		
	}
	
	private List<FolderPO> findRoots(List<FolderPO> folders){
		List<FolderPO> roots = new ArrayList<FolderPO>();
		for(FolderPO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==BundleTreeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}

	/**
	 * 根据接入层节点查询设备<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月27日 下午5:47:27
	 * @param worknodeId 接入层节点id
	 * @return List<BundleVO> 设备列表
	 */
	public List<BundleVO> bundleList(String worknodeId)throws Exception{
		
		WorkNodePO workNodePO = workNodeDao.findByNodeUid(worknodeId);
		if (workNodePO == null) throw new BaseException(StatusCode.ERROR, "暂无可查询到的设备");
		List<BundlePO> bundlePOs = bundleDao.findByAccessNodeUid(workNodePO.getNodeUid());
		List<BundleVO> bundleVOs = new ArrayList<BundleVO>();
		if(bundlePOs.size() != 0){
			for (BundlePO bundlePO : bundlePOs) {
				Map<String, Object> param = new HashMap<String, Object>();
				List<ExtraInfoPO> extraInfoPOs = extraInfoDao.findByBundleId(bundlePO.getBundleId());
				if (extraInfoPOs.size() != 0) {
					for (ExtraInfoPO extraInfoPO : extraInfoPOs) {
						String name = extraInfoPO.getName();
						String value = extraInfoPO.getValue();
						param.put(name, value);
					}
					JSONObject jsonObject = new JSONObject(param);
					BundleVO bundleVO = BundleVO.fromPO(bundlePO);
					bundleVO.setParam(jsonObject);
					bundleVOs.add(bundleVO);
				}else {
					BundleVO bundleVO = BundleVO.fromPO(bundlePO);
					bundleVOs.add(bundleVO);
					continue;
				}
			}
		}else throw new BaseException(StatusCode.ERROR, "暂无可查询到的设备");
		return bundleVOs;
	}

}
