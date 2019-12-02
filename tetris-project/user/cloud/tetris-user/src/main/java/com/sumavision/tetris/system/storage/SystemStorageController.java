package com.sumavision.tetris.system.storage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/system/storage")
public class SystemStorageController {

	@Autowired
	private SystemStorageService systemStorageService;
	
	@Autowired
	private SystemStorageQuery systemStorageQuery;
	
	@Autowired
	private SystemStorageDAO systemStorageDao;
	
	/**
	 * 分页查询系统存储<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午10:26:07
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<SystemStorageVO> rows 存储列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		List<SystemStoragePO> entities = systemStorageQuery.findAll(currentPage, pageSize);
		
		long total = systemStorageDao.count();
		
		List<SystemStorageVO> rows = SystemStorageVO.getConverter(SystemStorageVO.class).convert(entities, SystemStorageVO.class);
		
		return new HashMapWrapper<String, Object>().put("rows", rows)
												   .put("total", total)
												   .getMap();
	}
	
	/**
	 * 添加存储<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午9:13:03
	 * @param String name 存储名称
	 * @param String rootPath 存储根路径
	 * @param String baseFtpPath ftp根路径
	 * @param String basePreviewPath 预览http根路径
	 * @param String gadgetBasePath 小工具访问http根路径 
	 * @param String serverGadgetType 服务小工具类型
	 * @param String remark 备注
	 * @return SystemStorageVO 存储
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String rootPath,
			String baseFtpPath,
			String basePreviewPath,
			String gadgetBasePath,
			String serverGadgetType,
			String remark,
			HttpServletRequest request) throws Exception{
		
		SystemStoragePO entity = systemStorageService.add(
				name, rootPath, baseFtpPath, 
				basePreviewPath, gadgetBasePath, serverGadgetType, 
				remark);
		
		return new SystemStorageVO().set(entity);
	}
	
	/**
	 * 删除存储<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午10:36:17
	 * @param @PathVariable Long id 存储id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		systemStorageService.remove(id);
		return null;
	}
	
	/**
	 * 修改系统存储参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午8:48:37
	 * @param @PathVariable Long id 存储id
	 * @param String name 存储名称
	 * @param String rootPath 存储根路径
	 * @param String baseFtpPath ftp根路径
	 * @param String basePreviewPath 预览http根路径
	 * @param String gadgetBasePath 小工具访问http根路径 
	 * @param String serverGadgetType 服务小工具类型
	 * @param String remark 备注
	 * @return SystemStoragePO 存储
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			String rootPath,
			String baseFtpPath,
			String basePreviewPath,
			String gadgetBasePath,
			String serverGadgetType,
			String remark,
			HttpServletRequest request) throws Exception{
		
		SystemStoragePO entity = systemStorageService.edit(id, name, rootPath, baseFtpPath, basePreviewPath, gadgetBasePath, serverGadgetType, remark);
		
		return new SystemStorageVO().set(entity);
	} 
	
}
