package com.sumavision.tetris.easy.process.access.point;

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

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.access.point.exception.AccessPointNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.sdk.constraint.ConstraintBO;
import com.sumavision.tetris.sdk.constraint.ConstraintVO;
import com.sumavision.tetris.sdk.constraint.InternalConstraintBean;
import com.sumavision.tetris.sdk.constraint.api.ConstraintQuery;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/access/point/param")
public class AccessPointParamController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	@Autowired
	private ConstraintQuery constraintQuery;
	
	@Autowired
	private AccessPointParamService accessPointParamService;
	
	@Autowired
	private AccessPointParamQuery accessPointParamQuery;
	
	/**
	 * 查询接入点下所有参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午3:15:39
	 * @param Long accessPointId 接入点id
	 * @return List<AccessPointParamVO> 参数列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/all")
	public Object listAll(
			Long accessPointId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		AccessPointPO accessPoint =  accessPointDao.findOne(accessPointId);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(accessPointId);
		}
		
		List<AccessPointParamPO> entities = accessPointParamDao.findByAccessPointIdIn(new ArrayListWrapper<Long>().add(accessPointId).getList());
		
		List<AccessPointParamVO> params = AccessPointParamVO.getConverter(AccessPointParamVO.class).convert(entities, AccessPointParamVO.class);
		
		return params;
	}
	
	/**
	 * 查询接入点参数树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午10:59:44
	 * @param Long accessPointId 接入点id
	 * @return List<AccessPointParamVO> paramValues 参数树
	 * @return List<AccessPointParamVO> returnValues 返回值树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/tree")
	public Object listTree(
			Long accessPointId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		AccessPointPO accessPoint =  accessPointDao.findOne(accessPointId);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(accessPointId);
		}
		
		List<AccessPointParamVO> paramValues = accessPointParamQuery.findTreeByAccessPointIdAndDirection(accessPointId, ParamDirection.FORWARD);
		
		List<AccessPointParamVO> returnValues = accessPointParamQuery.findTreeByAccessPointIdAndDirection(accessPointId, ParamDirection.REVERSE);
	
		return new HashMapWrapper<String, Object>().put("paramValues", paramValues)
												   .put("returnValues", returnValues)
												   .getMap();
	}
	
	/**
	 * 获取枚举类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午11:42:42
	 * @return Set<String> paramTypes 参数类型（是否是枚举）
	 * @return Set<String> paramDirections 参数描述（参数还是返回值）
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/all/types")
	public Object queryAllTypes(HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		ParamType[] e_paramTypes = ParamType.values();
		Set<String> paramTypes = new HashSet<String>();
		for(ParamType e_paramType:e_paramTypes){
			paramTypes.add(e_paramType.getName());
		}
		
		ParamDirection[] e_paramDirections = ParamDirection.values();
		Set<String> paramDirections = new HashSet<String>();
		for(ParamDirection e_paramDirection:e_paramDirections){
			paramDirections.add(e_paramDirection.getName());
		}
		
		List<ConstraintBO<InternalConstraintBean>> constraintEntities = constraintQuery.list();
		List<ConstraintVO> internalConstraints = ConstraintVO.getConverter(ConstraintVO.class).convert(constraintEntities, ConstraintVO.class);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("paramTypes", paramTypes)
																		 .put("paramDirections", paramDirections)
																		 .put("internalConstraints", internalConstraints)
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 添加接入点根参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午12:13:33
	 * @param Long accessPointId 接入点id
	 * @param String primaryKey 流程参数主建
	 * @param String referenceKey 接入点参数主键
	 * @param String name 参数名
	 * @param String defaultValue 参数默认值
	 * @param String remarks 参数说明
	 * @param Integer serial 参数顺序
	 * @param String type 参数类型：基本类型或枚举
	 * @param String(JSONArray) values 如果是枚举，枚举项数组
	 * @param String(JSONArray) constraintExpressions 约束列表
	 * @return AccessPointParamVO 接入点参数数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/root/param/value")
	public Object addRootParamValue(
			Long accessPointId,
			String primaryKey,
			String primaryKeyPath,
			String referenceKey,
			String referenceKeyPath,
			String name,
			String defaultValue,
			String remarks,
			Integer serial,
			String type,
			String values,
			String constraintExpressions,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		AccessPointParamPO param = accessPointParamService.add(
				accessPointId, primaryKey, primaryKeyPath, referenceKey, referenceKeyPath,
				name, defaultValue, remarks, serial, 
				type, values, ParamDirection.FORWARD.getName(), constraintExpressions, null);
		
		return new AccessPointParamVO().set(param);
	}
	
	/**
	 * 添加接入点根返回值<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午12:13:33
	 * @param Long accessPointId 接入点id
	 * @param String primaryKey 流程参数主建
	 * @param String referenceKey 接入点参数主键
	 * @param String name 参数名
	 * @param String defaultValue 参数默认值
	 * @param String remarks 参数说明
	 * @param Integer serial 参数顺序
	 * @param String type 参数类型：基本类型或枚举
	 * @param String(JSONArray) values 如果是枚举，枚举项数组
	 * @param String(JSONArray) constraintExpressions 约束列表
	 * @return AccessPointParamVO 接入点参数数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/root/return/value")
	public Object addRootReturnValue(
			Long accessPointId,
			String primaryKey,
			String primaryKeyPath,
			String referenceKey,
			String referenceKeyPath,
			String name,
			String defaultValue,
			String remarks,
			Integer serial,
			String type,
			String values,
			String constraintExpressions,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		AccessPointParamPO param = accessPointParamService.add(
				accessPointId, primaryKey, primaryKeyPath, referenceKey, referenceKeyPath, 
				name, defaultValue, remarks, serial, 
				type, values, ParamDirection.REVERSE.getName(), constraintExpressions, null);
		
		return new AccessPointParamVO().set(param);
	}
	
	/**
	 * 添加接入点子参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午12:13:33
	 * @param Long accessPointId 接入点id
	 * @param String primaryKey 流程参数主建
	 * @param String referenceKey 接入点参数主键
	 * @param String name 参数名
	 * @param String defaultValue 参数默认值
	 * @param String remarks 参数说明
	 * @param Integer serial 参数顺序
	 * @param String type 参数类型：基本类型或枚举
	 * @param String(JSONArray) values 如果是枚举，枚举项数组
	 * @param String(JSONArray) constraintExpressions 约束列表
	 * @param Long parentId 父参数id
	 * @return AccessPointParamVO 接入点参数数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/param/value")
	public Object addParamValue(
			Long accessPointId,
			String primaryKey,
			String primaryKeyPath,
			String referenceKey,
			String referenceKeyPath,
			String name,
			String defaultValue,
			String remarks,
			Integer serial,
			String type,
			String values,
			String constraintExpressions,
			Long parentId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		AccessPointParamPO param = accessPointParamService.add(
				accessPointId, primaryKey, primaryKeyPath, referenceKey, referenceKeyPath,
				name, defaultValue, remarks, serial, 
				type, values, ParamDirection.FORWARD.getName(), constraintExpressions, parentId);
		
		return new AccessPointParamVO().set(param);
	}
	
	/**
	 * 添加接入点子返回值<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午12:13:33
	 * @param Long accessPointId 接入点id
	 * @param String primaryKey 流程参数主建
	 * @param String referenceKey 接入点参数主键
	 * @param String name 参数名
	 * @param String defaultValue 参数默认值
	 * @param String remarks 参数说明
	 * @param Integer serial 参数顺序
	 * @param String type 参数类型：基本类型或枚举
	 * @param String(JSONArray) values 如果是枚举，枚举项数组
	 * @param String(JSONArray) constraintExpressions 约束列表
	 * @param Long parentId 父参数id
	 * @return AccessPointParamVO 接入点参数数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/return/value")
	public Object addReturnValue(
			Long accessPointId,
			String primaryKey,
			String primaryKeyPath,
			String referenceKey,
			String referenceKeyPath,
			String name,
			String defaultValue,
			String remarks,
			Integer serial,
			String type,
			String values,
			String constraintExpressions,
			Long parentId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		AccessPointParamPO param = accessPointParamService.add(
				accessPointId, primaryKey, primaryKeyPath, referenceKey, referenceKeyPath,
				name, defaultValue, remarks, serial, 
				type, values, ParamDirection.REVERSE.getName(), constraintExpressions, parentId);
		
		return new AccessPointParamVO().set(param);
	}
	
	
	
	/**
	 * 删除参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午3:33:32
	 * @param @PathVariable id 参数id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		accessPointParamService.delete(id);
		
		return null;
	}
	
}
