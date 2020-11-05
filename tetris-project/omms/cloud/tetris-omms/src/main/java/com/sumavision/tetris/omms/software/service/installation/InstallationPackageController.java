package com.sumavision.tetris.omms.software.service.installation;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;

@Controller
@RequestMapping(value = "/installation/package")
public class InstallationPackageController {

	@Autowired
	private InstallationPackageQuery installationPackageQuery;
	
	@Autowired
	private InstallationPackageService installationPackageService;
	
	/**
	 * 查询服务安装包<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月27日 下午1:33:41
	 * @param Long serviceTypeId 服务类型id
	 * @return List<InstallationPackageVO> 安装包列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long serviceTypeId,
			HttpServletRequest request) throws Exception{
		
		return installationPackageQuery.load(serviceTypeId);
	}
	
	/**
	 * 添加版本<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月27日 下午5:55:54
	 * @param Long serviceTypeId 服务id
	 * @param String creator 创建者
	 * @param String remark 备注
	 * @param FileItem installationPackage 安装包内容
	 * @return InstallationPackageVO 安装包
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(HttpServletRequest request) throws Exception{
		MultipartHttpServletRequestWrapper requestWrapper = new MultipartHttpServletRequestWrapper(request, -1);
		Long serviceTypeId = requestWrapper.getLong("serviceTypeId");
		String creator = requestWrapper.getString("creator");
		String remark = requestWrapper.getString("remark");
		FileItem installationPackage = requestWrapper.getFileItem("installationPackage");
		return installationPackageService.add(serviceTypeId, creator, remark, installationPackage);
	}
	
	/**
	 * 删除版本<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月28日 下午1:19:11
	 * @param Long id 版本id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		installationPackageService.remove(id);
		return null;
	}
	
}
