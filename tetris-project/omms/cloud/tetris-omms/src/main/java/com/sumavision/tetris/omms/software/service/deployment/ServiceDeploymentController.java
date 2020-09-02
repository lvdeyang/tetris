package com.sumavision.tetris.omms.software.service.deployment;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/service/deployment")
public class ServiceDeploymentController {

	@Autowired
	private ServiceDeploymentService serviceDeploymentService;
	
	@Autowired
	private ServiceDeploymentQuery serviceDeploymentQuery;
	
	/**
	 * 上传安装包到服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月1日 上午10:08:16
	 * @param Long serverId 目标服务器id
	 * @param Long installationPackageId 安装包id
	 * @return ServiceDeploymentVO 部署信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload")
	public Object upload(
			Long serverId,
			Long installationPackageId,
			HttpServletRequest request) throws Exception{
		
		return serviceDeploymentService.upload(serverId, installationPackageId);
	}
	
	/**
	 * 查询状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月1日 上午10:26:33
	 * @param Long serviceDeploymentId 部署id
	 * @return ServiceDeploymentVO 状态
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/upload/status")
	public Object queryUploadStatus(
			Long serviceDeploymentId,
			HttpServletRequest request) throws Exception{
		
		return serviceDeploymentQuery.queryUploadStatus(serviceDeploymentId);
	}
	
}
