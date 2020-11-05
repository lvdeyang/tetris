package com.sumavision.tetris.bvc.business.terminal.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class TerminalBundleUserPermissionQuery {

	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	/**
	 * 查询用户为终端设备绑定的真实设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午11:04:21
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @return TerminalBundleUserPermissionVO 真实设备信息
	 */
	public TerminalBundleUserPermissionVO load(
			String userId,
			Long terminalId,
			Long terminalBundleId) throws Exception{
		TerminalBundleUserPermissionPO entity = terminalBundleUserPermissionDao.findByUserIdAndTerminalIdAndTerminalBundleId(userId, terminalId, terminalBundleId);
		if(entity != null){
			return new TerminalBundleUserPermissionVO().set(entity);
		}
		return null;
	}
	
	/**
	 * 根据类型查询设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午1:42:33
	 * @param String bundleName 设备名称
	 * @param String deviceModel 设备类型
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows  List<JSONObject> 设备列表 
	 */
	public Map<String, Object> loadBundles(
			String bundleName,
			String deviceModel,
			int currentPage,
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<BundlePO> pagedEntities = null;
		if(bundleName==null || "".equals(bundleName)){
			if(deviceModel==null || "".equals(deviceModel)){
				pagedEntities = bundleDao.findByBundleType("VenusTerminal", page);
			}else{
				pagedEntities = bundleDao.findByDeviceModelAndBundleType(deviceModel, "VenusTerminal", page);
			}
		}else{
			if(deviceModel==null || "".equals(deviceModel)){
				pagedEntities = bundleDao.findByBundleTypeAndBundleNameLike("VenusTerminal", new StringBufferWrapper().append("%").append(bundleName).append("%").toString(), page);
			}else{
				pagedEntities = bundleDao.findByDeviceModelAndBundleTypeAndBundleNameLike(deviceModel, "VenusTerminal", new StringBufferWrapper().append("%").append(bundleName).append("%").toString(), page);
			}
		}
		List<BundlePO> entities = pagedEntities.getContent();
		long total = pagedEntities.getTotalElements();
		List<JSONObject> rows = new ArrayList<JSONObject>();
		if(entities!=null && entities.size()>0){
			for(BundlePO entity:entities){
				JSONObject bundle = new JSONObject();
				bundle.put("bundleId", entity.getBundleId());
				bundle.put("bundleName", entity.getBundleName());
				bundle.put("bundleType", entity.getDeviceModel());
				rows.add(bundle);
			}
		}
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
}
