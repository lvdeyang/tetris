package com.sumavision.bvc.control.device.jv230.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.device.jv230.vo.CombineJv230ConfigVO;
import com.sumavision.bvc.control.device.jv230.vo.CombineJv230VO;
import com.sumavision.bvc.control.device.jv230.vo.Jv230VO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.jv230.dao.CombineJv230ConfigDAO;
import com.sumavision.bvc.device.jv230.dao.CombineJv230DAO;
import com.sumavision.bvc.device.jv230.dao.Jv230DAO;
import com.sumavision.bvc.device.jv230.po.CombineJv230ConfigPO;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO.ChannelComparatorFromChannelScheme;
import com.sumavision.bvc.device.jv230.po.Jv230PO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

@Controller
@RequestMapping(value = "/jv230")
public class Jv230Controller {

	@Autowired
	private CombineJv230DAO combineJv230Dao;
	
	@Autowired
	private CombineJv230ConfigDAO combineJv230ConfigDao;
	
	@Autowired
	private Jv230DAO jv230Dao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * @Title: 拼接屏分页查询<br/>
	 * @throws Exception
	 * @return bundles List<CombineJv230VO> 拼接屏列表
	 * @return total long 总数据量
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public Object queryAll(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);

		PageRequest page = new PageRequest(currentPage-1, pageSize);		
		
		Page<CombineJv230PO> pagedCombineJv230 = combineJv230Dao.findByUserId(userId, page);
		long total = pagedCombineJv230.getTotalElements();
		List<CombineJv230VO> _bundles = CombineJv230VO.getConverter(CombineJv230VO.class).convert(pagedCombineJv230.getContent(), CombineJv230VO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _bundles);
		data.put("total", total);
		
		return data;
		
	}
	
	/**
	 * @Title: 获取用户可用的jv230设备<br/> 
	 * @throws Exception
	 * @return List<TreeNodeVO> jv230设备树 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/useable/jav230")
	public Object queryUseableJv230(HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		
		//查询有权限的jv230
		List<BundlePO> bundleEntities = resourceQueryUtil.queryUseableJv230s(userId);
		Set<Long> folderIds = new HashSet<Long>();
		if(bundleEntities!=null && bundleEntities.size()>0){
			for(BundlePO bundleEntity:bundleEntities){
				BundleBO bundle = new BundleBO().set(bundleEntity);
				bundles.add(bundle);
				folderIds.add(bundle.getFolderId());
			}
		}
		
		//查询所有文件夹
		List<FolderPO> folderEntities = resourceQueryUtil.queryFoldersTree(folderIds);
		if(folderEntities!=null && folderEntities.size()>0){
			for(FolderPO folderEntity:folderEntities){
				FolderBO folderBO = new FolderBO().set(folderEntity);
				folders.add(folderBO);	
			}
		}
		
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, bundles, null);
		}
		
		return _roots;
	}
	
	/**
	 * @Title: 获取拼接屏已经配置的jv230设备<br/> 
	 * @param id 拼接屏id
	 * @throws Exception 
	 * @return Object 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/setted/jv230/{id}")
	public Object querySettedJv230(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		List<Jv230PO> jv230s = jv230Dao.queryByCombineJv230Id(id);
		
		List<Jv230VO> _jv230s = Jv230VO.getConverter(Jv230VO.class).convert(jv230s, Jv230VO.class);
		
		return _jv230s;
	}
	
	/**
	 * @Title: 保存拼接屏<br/> 
	 * @param name 拼接屏名称
	 * @param column 布局列数  默认4
	 * @param row 布局行数  默认4
	 * @throws Exception 
	 * @return CombineJv230VO 拼接屏数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String name,
			String column,
			String row,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		column = (column==null||"".equals(column))?"4":column;
		row = (row==null||"".equals(row))?"4":row;
		
		JSONObject websiteDraw = new JSONObject(true);
		websiteDraw.put("basic", new HashMapWrapper<String, Integer>().put("column", Integer.parseInt(column)).put("row", Integer.parseInt(row)).getMap());
		websiteDraw.put("cellspan", new ArrayList<Object>());
		
		CombineJv230PO bundle = new CombineJv230PO();
		bundle.setUserId(user.getId());
		bundle.setUserName(user.getName());
		bundle.setName(name);
		bundle.setUpdateTime(new Date());
		bundle.setWebsiteDraw(websiteDraw.toJSONString());
		combineJv230Dao.save(bundle);
		
		return new CombineJv230VO().set(bundle);
	}
	
	/**
	 * @Title: 修改拼接屏 <br/>
	 * @param id 拼接屏id
	 * @param name 拼接屏名称
	 * @throws Exception 
	 * @return CombineJv230VO 拼接屏数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			String column,
			String row,
			HttpServletRequest request) throws Exception{
		
		column = (column==null||"".equals(column))?"4":column;
		row = (row==null||"".equals(row))?"4":row;
		
		JSONObject websiteDraw = new JSONObject();
		websiteDraw.put("basic", new HashMapWrapper<String, String>().put("column", column).put("row", row).getMap());
		websiteDraw.put("cellspan", new ArrayList<Object>());
		
		CombineJv230PO bundle = combineJv230Dao.findOne(id);
		bundle.setName(name);
		bundle.setUpdateTime(new Date());
		bundle.setWebsiteDraw(websiteDraw.toJSONString());
		combineJv230Dao.save(bundle);
		
		return new CombineJv230VO().set(bundle);
	}
	
	/**
	 * @Title: 配置一个拼接屏<br/> 
	 * @param id
	 * @param config
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/do/config/{id}")
	public Object doConfig(
			@PathVariable Long id,
			String config,
			HttpServletRequest request) throws Exception{
		
		CombineJv230PO combineJv230 =  combineJv230Dao.findOne(id);
		Set<Jv230PO> bundles = combineJv230.getBundles();
		
		//解关联
		if(bundles!=null && bundles.size()>0){
			for(Jv230PO bundle:bundles){
				bundle.setCombineJv230(null);
			}
			combineJv230.getBundles().removeAll(bundles);
			jv230Dao.deleteInBatch(bundles);
		}
		
		if(bundles == null) combineJv230.setBundles(new HashSet<Jv230PO>());
		
		List<String> bundleIds = new ArrayList<String>();
		
		//创建jv230设备
		JSONArray configs = JSON.parseArray(config);
		for(int i=0; i<configs.size(); i++){
			JSONObject scopeConfig = configs.getJSONObject(i);
			JSONObject scopeBundleInfo = scopeConfig.getJSONObject("data");
			
			Jv230PO jv230 = new Jv230PO();
			jv230.setSerialnum(scopeConfig.getIntValue("serialNum"));
			jv230.setX(scopeConfig.getString("x"));
			jv230.setY(scopeConfig.getString("y"));
			jv230.setW(scopeConfig.getString("w"));
			jv230.setH(scopeConfig.getString("h"));
			jv230.setLayerId(scopeBundleInfo.getString("layerId"));
			jv230.setBundleId(scopeBundleInfo.getString("bundleId"));
			jv230.setBundleName(scopeBundleInfo.getString("bundleName"));
			
			//加关联
			jv230.setCombineJv230(combineJv230);
			combineJv230.getBundles().add(jv230);
			
			//保存id
			bundleIds.add(jv230.getBundleId());
		}
		
		//创建jv230通道
		List<ChannelSchemeDTO> channels = resourceChannelDao.findByBundleIds(bundleIds);
		if(channels!=null && channels.size()>0){
			for(Jv230PO scopeJv230:combineJv230.getBundles()){
				List<ChannelSchemeDTO> videoOut = new ArrayList<ChannelSchemeDTO>();
				List<ChannelSchemeDTO> videoIn = new ArrayList<ChannelSchemeDTO>();
				List<ChannelSchemeDTO> audioOut = new ArrayList<ChannelSchemeDTO>();
				List<ChannelSchemeDTO> audioIn = new ArrayList<ChannelSchemeDTO>();
				for(ChannelSchemeDTO channel:channels){
					if(channel.getBundleId().equals(scopeJv230.getBundleId())){
						if("VenusVideoOut".equals(channel.getBaseType())){
							videoOut.add(channel);
						}else if("VenusVideoIn".equals(channel.getBaseType())){
							videoIn.add(channel);
						}else if("VenusAudioOut".equals(channel.getBaseType())){
							audioOut.add(channel);
						}else if("VenusAudioIn".equals(channel.getBaseType())){
							audioIn.add(channel);
						}
					}
				}
				//排序
				ChannelComparatorFromChannelScheme comparator = new Jv230ChannelPO.ChannelComparatorFromChannelScheme();
				Collections.sort(videoOut, comparator);
				Collections.sort(videoIn, comparator);
				Collections.sort(audioOut, comparator);
				Collections.sort(audioIn, comparator);
				
				for(int i=0; i<videoOut.size(); i++){
					ChannelSchemeDTO channelScheme = videoOut.get(i);
					Jv230ChannelPO jv230Channel = new Jv230ChannelPO();
					jv230Channel.setLayerId(scopeJv230.getLayerId());
					jv230Channel.setBundleId(scopeJv230.getBundleId());
					jv230Channel.setBundleName(scopeJv230.getBundleName());
					jv230Channel.setChannelId(channelScheme.getChannelId());
					jv230Channel.setChannelName(channelScheme.getChannelName());
					jv230Channel.setChannelType(channelScheme.getBaseType());
					jv230Channel.setSerialNum(Long.valueOf(channelScheme.getChannelId().split("_")[1]));
					jv230Channel.setOccupied(false);
					jv230Channel.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(jv230Channel.getChannelType()).append("-").append(i+1).toString()));
					if(scopeJv230.getChannels() == null) scopeJv230.setChannels(new HashSet<Jv230ChannelPO>());
					//加关联
					jv230Channel.setJv230(scopeJv230);
					scopeJv230.getChannels().add(jv230Channel);
				}
				
				for(int i=0; i<videoIn.size(); i++){
					ChannelSchemeDTO channelScheme = videoIn.get(i);
					Jv230ChannelPO jv230Channel = new Jv230ChannelPO();
					jv230Channel.setLayerId(scopeJv230.getLayerId());
					jv230Channel.setBundleId(scopeJv230.getBundleId());
					jv230Channel.setBundleName(scopeJv230.getBundleName());
					jv230Channel.setChannelId(channelScheme.getChannelId());
					jv230Channel.setChannelName(channelScheme.getChannelName());
					jv230Channel.setChannelType(channelScheme.getBaseType());
					jv230Channel.setSerialNum(Long.valueOf(channelScheme.getChannelId().split("_")[1]));
					jv230Channel.setOccupied(false);
					jv230Channel.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(jv230Channel.getChannelType()).append("-").append(i+1).toString()));
					if(scopeJv230.getChannels() == null) scopeJv230.setChannels(new HashSet<Jv230ChannelPO>());
					//加关联
					jv230Channel.setJv230(scopeJv230);
					scopeJv230.getChannels().add(jv230Channel);
				}
				
				for(int i=0; i<audioOut.size(); i++){
					ChannelSchemeDTO channelScheme = audioOut.get(i);
					Jv230ChannelPO jv230Channel = new Jv230ChannelPO();
					jv230Channel.setLayerId(scopeJv230.getLayerId());
					jv230Channel.setBundleId(scopeJv230.getBundleId());
					jv230Channel.setBundleName(scopeJv230.getBundleName());
					jv230Channel.setChannelId(channelScheme.getChannelId());
					jv230Channel.setChannelName(channelScheme.getChannelName());
					jv230Channel.setChannelType(channelScheme.getBaseType());
					jv230Channel.setSerialNum(Long.valueOf(channelScheme.getChannelId().split("_")[1]));
					jv230Channel.setOccupied(false);
					jv230Channel.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(jv230Channel.getChannelType()).append("-").append(i+1).toString()));
					if(scopeJv230.getChannels() == null) scopeJv230.setChannels(new HashSet<Jv230ChannelPO>());
					//加关联
					jv230Channel.setJv230(scopeJv230);
					scopeJv230.getChannels().add(jv230Channel);
				}
				
				for(int i=0; i<audioIn.size(); i++){
					ChannelSchemeDTO channelScheme = audioIn.get(i);
					Jv230ChannelPO jv230Channel = new Jv230ChannelPO();
					jv230Channel.setLayerId(scopeJv230.getLayerId());
					jv230Channel.setBundleId(scopeJv230.getBundleId());
					jv230Channel.setBundleName(scopeJv230.getBundleName());
					jv230Channel.setChannelId(channelScheme.getChannelId());
					jv230Channel.setChannelName(channelScheme.getChannelName());
					jv230Channel.setChannelType(channelScheme.getBaseType());
					jv230Channel.setSerialNum(Long.valueOf(channelScheme.getChannelId().split("_")[1]));
					jv230Channel.setOccupied(false);
					jv230Channel.setType(ChannelType.fromChannelType(new StringBufferWrapper().append(jv230Channel.getChannelType()).append("-").append(i+1).toString()));
					if(scopeJv230.getChannels() == null) scopeJv230.setChannels(new HashSet<Jv230ChannelPO>());
					//加关联
					jv230Channel.setJv230(scopeJv230);
					scopeJv230.getChannels().add(jv230Channel);
				}
			}
		}
		
		//持久化数据
		combineJv230Dao.save(combineJv230);
		
		return null;
	}
	
	/**
	 * @Title: 删除一个拼接屏<br/> 
	 * @param id 拼接屏id
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		combineJv230Dao.delete(id);
		return null;
	}
	
	/**
	 * @Title: 批量删除拼接屏<br/> 
	 * @param ids 拼接屏id数组
	 * @throws Exception 
	 * @return
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all")
	public Object removeAll(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);
		
		combineJv230Dao.deleteByIdIn(ids);
		return null;
	}
	
	/**
	 * @Title: 获取大屏配置列表<br/> 
	 * @param combineJv230Id 大屏id
	 * @throws Exception
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/combineJv230/config/{combineJv230Id}")
	public Object queryCombineJv230Config(
			@PathVariable Long combineJv230Id,
			HttpServletRequest request) throws Exception{
		
		CombineJv230PO combineJv230 = combineJv230Dao.findOne(combineJv230Id);
		Set<CombineJv230ConfigPO> comfigs = combineJv230.getConfigs();
		
		List<CombineJv230ConfigVO> _configs = CombineJv230ConfigVO.getConverter(CombineJv230ConfigVO.class).convert(comfigs, CombineJv230ConfigVO.class);
		
		JSONObject data = new JSONObject();
		data.put("rows", _configs);
		data.put("total", _configs.size());
		
		return data;
	}
	
	/**
	 * @Title: 保存大屏配置<br/> 
	 * @param name 拼接屏名称
	 * @param status 上屏状态
	 * @param remark 备注信息
	 * @throws Exception 
	 * @return CombineJv230ConfigVO 配置数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/combineJv230/config/{combineJv230Id}")	
	public Object saveConfig(
			@PathVariable Long combineJv230Id,
			String name,
			String status,
			String remark,
			HttpServletRequest request) throws Exception{
		
		CombineJv230PO combineJv230 = combineJv230Dao.findOne(combineJv230Id);
		CombineJv230ConfigPO config = new CombineJv230ConfigPO();
		config.setName(name);
		config.setRemark(remark);
		config.setStatus(status);
		config.setCombineJv230(combineJv230);
		if(combineJv230.getConfigs() == null) combineJv230.setConfigs(new HashSet<CombineJv230ConfigPO>());
		combineJv230.getConfigs().add(config);
		combineJv230ConfigDao.save(config);
		
		return new CombineJv230ConfigVO().set(config);
	}
	
	/**
	 * 更新大屏配置
	 * @Title: updateConfig 
	 * @param configId 大屏配置id
	 * @param name 大屏配置名称
	 * @param remark 大屏配置备注信息
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/combineJv230/config/{configId}")
	public Object updateConfig(
			@PathVariable Long configId,
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		CombineJv230ConfigPO config = combineJv230ConfigDao.findOne(configId);
		config.setName(name);
		config.setRemark(remark);
		combineJv230ConfigDao.save(config);
		
		return new CombineJv230ConfigVO().set(config);
		
	}
	
	/**
	 * 删除大屏配置
	 * @Title: removeConfig 
	 * @param configId 大屏配置id
	 * @return Object    返回类型 
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/combineJv230/config/{configId}")
	public Object removeConfig(
			@PathVariable Long configId,
			HttpServletRequest request) throws Exception{
		
		CombineJv230ConfigPO config = combineJv230ConfigDao.findOne(configId);
		CombineJv230PO combineJv230 = config.getCombineJv230();
		combineJv230.getConfigs().remove(config);
		config.setCombineJv230(null);		
		
		combineJv230ConfigDao.delete(config);
		return null;
	}
	
	/**
	 * 批量删除大屏配置
	 * @Title: removeAllConfig 
	 * @return Object    返回类型 
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all/combineJv230/config")
	public Object removeAllConfig(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);
		List<CombineJv230ConfigPO> configs = combineJv230ConfigDao.findAll(ids);
		for(CombineJv230ConfigPO config: configs){
			config.getCombineJv230().getConfigs().remove(config);
			config.setCombineJv230(null);
		}		
		
		combineJv230ConfigDao.deleteInBatch(configs);
		return null;
	}
	
	/**
	 * @Title: 查找根目录 <br/>
	 * @param folders 文件夹集合
	 * @return List<FolderBO> 根文件夹
	 */
	private List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder.getParentId() == TreeNodeVO.FOLDERID_ROOT){
				roots.add(folder);
			}
		}
		return roots;
	}
	
	/**
	 * @Title: 递归文件夹<br/> 
	 * @param root 当前文件夹
	 * @param folders 全部文件夹
	 * @param bundles 全部设备
	 * @param channels 全部通道
	 */
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles, 
			List<ChannelBO> channels){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles, channels);
			}
		}
		
		//往里装设备
		for(BundleBO bundle:bundles){
			if(bundle.getFolderId().toString().equals(root.getId())){
				TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(bundleNode);
				if(channels != null){
					for(ChannelBO channel:channels){
						if(channel.getBundleId().equals(bundleNode.getId())){
							TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
							bundleNode.getChildren().add(channelNode);
						}
					}
				}
			}
		}
	}
	
}
