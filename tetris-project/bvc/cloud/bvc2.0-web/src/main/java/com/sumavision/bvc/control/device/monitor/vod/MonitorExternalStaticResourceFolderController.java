package com.sumavision.bvc.control.device.monitor.vod;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.monitor.vod.exception.ExternalFolderCannotConnectException;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.monitor.vod.MonitorExternalStaticResourceFolderDAO;
import com.sumavision.bvc.device.monitor.vod.MonitorExternalStaticResourceFolderPO;
import com.sumavision.bvc.device.monitor.vod.MonitorExternalStaticResourceFolderQuery;
import com.sumavision.bvc.device.monitor.vod.MonitorExternalStaticResourceFolderService;
import com.sumavision.bvc.device.monitor.vod.ProtocolType;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/external/static/resource/folder")
public class MonitorExternalStaticResourceFolderController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorExternalStaticResourceFolderDAO monitorExternalStaticResourceFolderDao;
	
	@Autowired
	private MonitorExternalStaticResourceFolderQuery monitorExternalStaticResourceFolderQuery;
	
	@Autowired
	private MonitorExternalStaticResourceFolderService monitorExternalStaticResourceFolderService;
	
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value = "/index")
	public ModelAndView index(String token){
		
		ModelAndView mv = new ModelAndView("web/bvc/monitor/external-folder/external-folder");
		mv.addObject("token", token);
		
		return mv;
	}
	
	/**
	 * 协议类型查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午10:24:32
	 * @return 协议类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		Set<String> protocolTypes = new HashSet<String>();
		ProtocolType[] values = ProtocolType.values();
		for(ProtocolType value:values){
			protocolTypes.add(value.getName());
		}
		return protocolTypes;
	}
	
	/**
	 * 分页查询所有外部文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 下午4:56:29
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<MonitorExternalStaticResourceFolderVO> 文件夹列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		List<MonitorExternalStaticResourceFolderPO> entities = monitorExternalStaticResourceFolderQuery.findAll(currentPage, pageSize);
		
		long total = monitorExternalStaticResourceFolderDao.count();
		
		List<MonitorExternalStaticResourceFolderVO> rows = new ArrayList<MonitorExternalStaticResourceFolderVO>();
		
		if(entities!=null && entities.size()>0){
			for(MonitorExternalStaticResourceFolderPO entity:entities){
				MonitorExternalStaticResourceFolderVO row = new MonitorExternalStaticResourceFolderVO().setFullPath(entity)
																									   .setCreateUsername(entity.getCreateUsername())
																									   .setName(entity.getName());
				rows.add(row);																					   
			}
		}
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 分页查询用户创建外部文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午10:15:09
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<MonitorExternalStaticResourceFolderVO> 文件夹列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		int total = monitorExternalStaticResourceFolderDao.countByCreateUserId(userId.toString());
		
		List<MonitorExternalStaticResourceFolderPO> entities = monitorExternalStaticResourceFolderQuery.findByCreateUserId(userId, currentPage, pageSize);
		
		List<MonitorExternalStaticResourceFolderVO> rows = MonitorExternalStaticResourceFolderVO.getConverter(MonitorExternalStaticResourceFolderVO.class).convert(entities, MonitorExternalStaticResourceFolderVO.class);
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 添加一个外部文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午10:34:01
	 * @param String name 别名
	 * @param String ip ip
	 * @param String port 端口
	 * @param String folderPath 目录结构
	 * @param String protocolType 协议类型
	 * @param String username 用户名
	 * @param String password 密码
	 * @return MonitorExternalStaticResourceFolderVO 外部文件夹
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String ip,
			String port,
			String folderPath,
			String protocolType,
			String username,
			String password,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		MonitorExternalStaticResourceFolderPO entity = monitorExternalStaticResourceFolderService.add(name, ip, port, folderPath, protocolType, username, password, user.getId(), user.getName());
		
		return new MonitorExternalStaticResourceFolderVO().set(entity);
	}
	
	/**
	 * 修改外部文件夹<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午10:44:58
	 * @param @PathVariable Long id 文件夹id
	 * @param String name 别名
	 * @param String ip ip
	 * @param String port 端口
	 * @param String folderPath 目录结构
	 * @param String protocolType 协议类型
	 * @param String username 用户名
	 * @param String password 密码
	 * @return MonitorExternalStaticResourceFolderVO 文件夹
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			String ip,
			String port,
			String folderPath,
			String protocolType,
			String username,
			String password,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorExternalStaticResourceFolderPO entity = monitorExternalStaticResourceFolderService.edit(id, name, ip, port, folderPath, protocolType, username, password, userId);
		
		return new MonitorExternalStaticResourceFolderVO().set(entity);
	}
	
	/**
	 * 删除外部文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月16日 上午10:46:16
	 * @param @PathVariable Long id 文件夹id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorExternalStaticResourceFolderService.remove(id, userId);
		
		return null;
	}
	
	/**
	 * 遍历文件夹下的点播资源<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月17日 上午9:24:57
	 * @param 外部文件夹全路径fullPath
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<StaticResourceVO> 资源列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/scanning")
	public Object scanning(
			String fullPath,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		List<StaticResourceVO> resources = new ArrayList<StaticResourceVO>();
		
		Set<String> existResources = resourceService.queryResourcePreviewUrl();
		
		if(fullPath.startsWith("http://") || fullPath.startsWith("https://")){
			
			Document doc = null;
			try{
				doc = Jsoup.connect(fullPath).get();
			}catch(Exception e){
				throw new ExternalFolderCannotConnectException(fullPath);
			}
			Elements es = doc.getElementsByTag("a");
			for(int i=0; i<es.size(); i++){
				String itemName = es.get(i).attr("href");
				if(itemName.equals("../")) continue;
				String previewUrl = null;
				itemName = URLDecoder.decode(itemName);
				if(itemName.indexOf("\\.") >= 0){
					previewUrl = new StringBufferWrapper().append(fullPath).append(itemName).toString();
				}else{
					previewUrl = new StringBufferWrapper().append(fullPath).append(itemName).append("video.m3u8").toString();
				}
				if(existResources.contains(previewUrl)) continue;
				resources.add(new StaticResourceVO().setName(itemName.endsWith("/")?itemName.substring(0, itemName.length()-1):itemName)
													.setFullPath(previewUrl));
			}
			
		}else if(fullPath.startsWith("ftp://")){
			
		}
		
		Collections.sort(resources, new StaticResourceVO.StaticResourceComparator());
		List<StaticResourceVO> rows = new ArrayList<StaticResourceVO>();
		if(resources.size() > (currentPage*pageSize)){
			for(int i=(currentPage-1)*pageSize; i<(currentPage*pageSize); i++){
				rows.add(resources.get(i));
			}
		}else{
			if(resources.size() > (currentPage-1)*pageSize){
				for(int i=(currentPage-1)*pageSize; i<resources.size(); i++){
					rows.add(resources.get(i));
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("total", resources.size())
												   .put("rows", rows)
												   .getMap();
	}
	
}
