package com.sumavision.tetris.omms.software.service.installation.history;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/installation/package/history")
public class InstallationPackageHistoryController {

	@Autowired
	private InstallationPackageHistoryQuery installationPackageHistoryQuery;
	
	@Autowired
	private InstallationPackageHistoryService installationPackageHistoryService;
	
	
	/**
	 * 根据服务类型查询历史安装包<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月28日 上午9:40:10
	 * @param serviceTypeId 服务类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/search/package")
	public Object searchPackage(
			Long serviceTypeId, 
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		return installationPackageHistoryQuery.searchPackagePage(serviceTypeId,currentPage,pageSize);
	}
	
	/**
	 * 删除历史安装包<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月28日 上午9:38:30
	 * @param id  安装包id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping( value = "/delete/package/{id}")
	public Object deletePackage(@PathVariable Long id, HttpServletRequest request) throws Exception{
		installationPackageHistoryService.deletePackage(id);
		return null ;
	}
	
}
