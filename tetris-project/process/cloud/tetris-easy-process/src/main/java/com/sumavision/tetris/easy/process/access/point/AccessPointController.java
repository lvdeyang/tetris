package com.sumavision.tetris.easy.process.access.point;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.access.point.exception.AccessPointCannotDeleteException;
import com.sumavision.tetris.easy.process.access.point.exception.AccessPointNotExistException;
import com.sumavision.tetris.easy.process.access.service.ServiceType;
import com.sumavision.tetris.easy.process.access.service.exception.ServiceNotExistException;
import com.sumavision.tetris.easy.process.access.service.rest.RestServiceDAO;
import com.sumavision.tetris.easy.process.access.service.rest.RestServicePO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/access/point")
public class AccessPointController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private RestServiceDAO restServiceDao; 
	
	@Autowired
	private AccessPointQuery accessPointTool;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	@Autowired
	private AccessPointService accessPointService;
	
	@Autowired
	private AccessPointProcessPermissionDAO accessPointProcessPermissionDao;
	
	/**
	 * 查询流程下的接入点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月22日 上午10:44:07
	 * @param @PathVariable Long processId 流程id
	 * @return List<AccessPointVO> 接入点列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/by/process/{processId}")
	public Object queryByProcess(
			@PathVariable Long processId,
			HttpServletRequest request) throws Exception{
		
		List<AccessPointProcessPermissionPO> permissions = accessPointProcessPermissionDao.findByProcessId(processId);
		Set<Long> accessPointIds = new HashSet<Long>();
		if(permissions!=null && permissions.size()>0){
			for(AccessPointProcessPermissionPO permission:permissions){
				accessPointIds.add(permission.getAccessPointId());
			}
		}
		
		List<AccessPointPO> entities = accessPointDao.findAll(accessPointIds);
		List<AccessPointVO> accessPoints = AccessPointVO.getConverter(AccessPointVO.class).convert(entities, AccessPointVO.class);
		
		return accessPoints;
	}
	
	/**
	 * 分页查询接入点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午3:21:40
	 * @param Long serviceId 服务id
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Long serviceId,
			String serviceType,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		RestServicePO service = null;
		
		ServiceType e_serviceType = ServiceType.valueOf(serviceType);
		if(e_serviceType.equals(ServiceType.REST)){
			service = restServiceDao.findOne(serviceId);
		}
		
		if(service == null){
			throw new ServiceNotExistException(serviceId);
		}
		
		int total = accessPointDao.countByServiceId(serviceId);
		List<AccessPointPO> entities = accessPointTool.findByServiceId(serviceId, currentPage, pageSize);
		List<AccessPointVO> rows = new ArrayList<AccessPointVO>();
		if(entities!=null && entities.size()>0){
			for(AccessPointPO entity:entities){
				rows.add(new AccessPointVO().set(entity, e_serviceType, service.getName()));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", rows)
																		 .put("total", total)
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 获取所有的接入点类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午5:08:49
	 * @return Set<String> 接入点类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/all/types")
	public Object queryAllTypes(HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		AccessPointType[] values = AccessPointType.values();
		
		Set<String> types = new HashSet<String>();
		for(AccessPointType value:values){
			types.add(value.getName());
		}
		
		return types;
	}
	
	/**
	 * 添加接入点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午5:26:47
	 * @param Long serviceId 服务id
	 * @param String serviceType 服务类型
	 * @param String name 接入点名称
	 * @param String type 接入点类型
	 * @param String method 接入点方法
	 * @param String remarks 接入点备注
	 * @return AccessPointVO 接入点数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long serviceId,
			String serviceType,
			String scope,
			String name,
			String type,
			String method,
			String remarks,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		RestServicePO service = null;
		
		ServiceType e_serviceType = ServiceType.valueOf(serviceType);
		if(e_serviceType.equals(ServiceType.REST)){
			service = restServiceDao.findOne(serviceId);
		}
		
		if(service == null){
			throw new ServiceNotExistException(serviceId);
		}
		
		AccessPointPO accessPoint = new AccessPointPO();
		accessPoint.setName(name);
		accessPoint.setType(AccessPointType.fromName(type));
		accessPoint.setMethod(ServiceType.REST.equals(e_serviceType)?AccessPointPO.formatRestMethod(method):method);
		accessPoint.setRemarks(remarks);
		accessPoint.setScope(AccessPointScope.fromName(scope));
		accessPoint.setServiceId(service.getId());
		accessPoint.setServiceType(e_serviceType);
		accessPoint.setUpdateTime(new Date());
		accessPointDao.save(accessPoint);
		
		return new AccessPointVO().set(accessPoint, e_serviceType, service.getName());
	}
	
	/**
	 * 编辑接入点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午9:06:39
	 * @param @PathVariable Long id 接入点id
	 * @param String name 接入点名称
	 * @param String type 接入点类型
	 * @param String method 接入点方法
	 * @param String remarks 接入点备注
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			String type,
			String method,
			String remarks,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		AccessPointPO accessPoint = accessPointDao.findOne(id);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(id);
		}
		
		accessPoint.setName(name);
		accessPoint.setType(AccessPointType.fromName(type));
		accessPoint.setMethod(ServiceType.REST.equals(accessPoint.getServiceType())?AccessPointPO.formatRestMethod(method):method);
		accessPoint.setRemarks(remarks);
		accessPoint.setUpdateTime(new Date());
		
		accessPointDao.save(accessPoint);
		
		return null;
	}
	
	/**
	 * 删除接入点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午9:35:51
	 * @param @PathVariable Long id 接入点id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		AccessPointPO accessPoint = accessPointDao.findOne(id);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(id);
		}
		
		List<AccessPointProcessPermissionPO> permissions = accessPointProcessPermissionDao.findByAccessPointId(id);
		
		if(permissions!=null && permissions.size()>0){
			throw new AccessPointCannotDeleteException(id);
		}
		
		accessPointService.delete(accessPoint);
		
		return null;
	}
	
}
