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
	
	/**
	 * 删除部署数据<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 上午10:53:28
	 * @param serviceDeploymentId 部署id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/deployment/data")
	public Object deleteDeploymentData(
			Long deploymentId,
			HttpServletRequest request) throws Exception{
		
		serviceDeploymentService.deleteDeploymentData(deploymentId);
		return null;
	}
	
	/**
	 * 根据服务器查询部署服务<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 上午10:12:06
	 * @param serverId 服务器id
	 * @return Map<String, Object> 部署服务参数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long serverId,
			int currentPage,
			int pageSize,
			HttpServletRequest request)throws Exception{
		return serviceDeploymentQuery.load(serverId, currentPage, pageSize);
	}
	
	/**
	 * 执行安装操作<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月3日 下午8:53:07
	 * @param Long deploymentId 部署id
	 * @param JSONString config config.ini json形式
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/install")
	public Object install(
			Long deploymentId,
			String config,
			HttpServletRequest request) throws Exception{
		
		serviceDeploymentService.install(deploymentId, config);
		return null;
	}
	
	/**
	 * 重启进程<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月14日 下午2:03:25
	 * @param deploymentId 部署id
	 * @param processId 进程名称
	 * @param request
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/restart")
	public Object restart(
			Long deploymentId, 
			String processId,
			HttpServletRequest request) throws Exception{
		
		serviceDeploymentService.restart(deploymentId, processId);
		return null;
	}
	
	/**
	 * 执行卸载<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 上午10:35:27
	 * @param Long deploymentId 部署id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/uninstall")
	public Object uninstall(
			Long deploymentId,
			String type,
			String notes,
			HttpServletRequest request) throws Exception{
		
		serviceDeploymentService.uninstall(deploymentId, type, notes);
		return null;
	}
	
	/**
	 * 安装包备份<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月15日 下午5:23:51
	 * @param deploymentId 部署id
	 * @param request
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/backup")
	public Object backup(
			Long deploymentId,
			String deploymentName,
			String notes,
			Boolean databaseBackup,
			HttpServletRequest request) throws Exception{
		
		return serviceDeploymentService.backup(deploymentId, deploymentName, notes, databaseBackup);
	}
	
	/**
	 * 根据部署id查询所有备份信息<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月16日 下午4:18:51
	 * @param deploymentId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/findBackup")
	public Object findBackup(
			Long deploymentId,
			HttpServletRequest request) throws Exception{
		
		return serviceDeploymentQuery.findBackup(deploymentId);
	}
	
	/**
	 * 删除备份信息<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月19日 下午4:13:30
	 * @param backupId 备份id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/deleteBackup")
	public Object deleteBackup(
			Long backupId,
			HttpServletRequest request) throws Exception{
		
		serviceDeploymentService.deleteBackup(backupId);
		return null;
	}
	
	/**
	 * 备份恢复<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月20日 上午11:47:00
	 * @param backupId 备份id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/restore")
	public Object restore(
			Long backupId,
			Boolean database,
			HttpServletRequest request) throws Exception{
		
		serviceDeploymentService.restore(backupId ,database);
		return null;
	}
	
	/**
	 * 升级<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月22日 下午3:00:03
	 * @param Long deploymentId 部署id
	 * @param JSONString config config.ini json形式
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/update")
	public Object update(
			Long deploymentId, 
			Long updatePackageId,
			String config,
			Boolean isBackup, 
			String notes,
			HttpServletRequest request) throws Exception{
		
		serviceDeploymentService.update(deploymentId, updatePackageId, config, isBackup, notes);
		return null;
	}
	
	/**
	 * 
	 * 数据库备份<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 下午3:16:19
	 * @param id 进程id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/database/backup")
	public Object databaseBackup(
			Long id,
			HttpServletRequest request) throws Exception{
		
		serviceDeploymentService.databaseBackup(id);
		return null;
	}
	
	/**
	 * 查询创建的Sql服务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月30日 下午4:48:24
	 * @return List<String> ipAndPort ip和端口的组合
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/database/IpAndPort")
	public Object databaseIpAndPort()throws Exception{
		
		return serviceDeploymentQuery.databaseIpAndPort();
	}
}
