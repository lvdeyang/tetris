package com.sumavision.tetris.easy.process.access.point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.access.point.exception.AccessPointNotExistException;
import com.sumavision.tetris.easy.process.access.point.exception.ParamReferencedByJointConstraintsException;
import com.sumavision.tetris.easy.process.access.point.exception.PrimaryKeyAlreadyExistException;
import com.sumavision.tetris.easy.process.access.point.exception.SerialAlreadyExistException;
import com.sumavision.tetris.easy.process.access.service.exception.UserHasNoPermissionForServiceQueryException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.sdk.constraint.ConstraintBO;
import com.sumavision.tetris.sdk.constraint.ConstraintVO;
import com.sumavision.tetris.sdk.constraint.InternalConstraintBean;
import com.sumavision.tetris.sdk.constraint.api.ConstraintQuery;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/access/point/param")
public class AccessPointParamController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private AccessPointParamQuery accessPointParamTool;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	@Autowired
	private ConstraintQuery constraintQuery;
	
	@Autowired
	private JointConstraintExpressionQuery jointConstraintExpressionTool;
	
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
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForServiceQueryException(user.getUuid(), "服务接入点查询");
		}
		
		AccessPointPO accessPoint =  accessPointDao.findOne(accessPointId);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(accessPointId);
		}
		
		List<AccessPointParamPO> entities = accessPointParamDao.findByAccessPointIdIn(new ArrayListWrapper<Long>().add(accessPointId).getList());
	
		List<AccessPointParamVO> params = AccessPointParamVO.getConverter(AccessPointParamVO.class).convert(entities, AccessPointParamVO.class);
	
		return params;
	}
	
	/**
	 * 接入点参数分页查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午10:59:44
	 * @param Long accessPointId 接入点id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<AccessPointParamVO> rows 参数列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Long accessPointId,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForServiceQueryException(user.getUuid(), "服务接入点查询");
		}
		
		AccessPointPO accessPoint =  accessPointDao.findOne(accessPointId);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(accessPointId);
		}
		
		int total = accessPointParamDao.countByAccessPointId(accessPointId);
		
		List<AccessPointParamPO> entities = accessPointParamTool.findByAccessPointId(accessPointId, currentPage, pageSize);
		List<AccessPointParamVO> rows = new ArrayList<AccessPointParamVO>();
		if(entities!=null && entities.size()>0){
			for(AccessPointParamPO entity:entities){
				rows.add(new AccessPointParamVO().set(entity, accessPoint.getName()));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("total", total)
																		 .put("rows", rows)
																		 .getMap();
		
		return result;
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
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForServiceQueryException(user.getUuid(), "服务接入点查询");
		}
		
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
	 * 添加接入点参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午12:13:33
	 * @param Long accessPointId 接入点id
	 * @param String primaryKey 参数建
	 * @param String name 参数名
	 * @param String defaultValue 参数默认值
	 * @param String remarks 参数说明
	 * @param Integer serial 参数顺序
	 * @param String type 参数类型：基本类型或枚举
	 * @param String(JSONArray) values 如果是枚举，枚举项数组
	 * @param String direction 参数描述，参数或返回值
	 * @param String(JSONArray) constraintExpressions 约束列表
	 * @return AccessPointParamVO 接入点参数数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long accessPointId,
			String primaryKey,
			String name,
			String defaultValue,
			String remarks,
			Integer serial,
			String type,
			String values,
			String direction,
			String constraintExpressions,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForServiceQueryException(user.getUuid(), "服务接入点查询");
		}
		
		AccessPointParamPO existParam = accessPointParamDao.findByPrimaryKey(primaryKey);
		
		if(existParam != null){
			throw new PrimaryKeyAlreadyExistException(primaryKey);
		}
		
		existParam = accessPointParamDao.findByAccessPointIdAndSerial(accessPointId, serial);
		
		if(existParam != null){
			throw new SerialAlreadyExistException(accessPointId, serial);
		}
		
		AccessPointPO accessPoint = accessPointDao.findOne(accessPointId);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(accessPointId);
		}
		
		List<String> parsedValue = JSON.parseArray(values, String.class);
		List<String> parsedConstraint = JSON.parseArray(constraintExpressions, String.class);
		
		AccessPointParamPO param = new AccessPointParamPO();
		param.setAccessPointId(accessPointId);
		param.setPrimaryKey(primaryKey);
		param.setName(name);
		param.setDefaultValue(defaultValue);
		param.setRemarks(remarks);
		param.setSerial(serial);
		param.setType(ParamType.fromName(type));
		param.setEnums(StringUtils.join(parsedValue, ","));
		param.setDirection(ParamDirection.fromName(direction));
		param.setConstraintExpression(StringUtils.join(parsedConstraint, "&&"));
		accessPointParamDao.save(param);
		
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
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForServiceQueryException(user.getUuid(), "服务接入点查询");
		}
		
		AccessPointParamPO param = accessPointParamDao.findOne(id);
		
		if(param != null){
			List<JointConstraintExpressionPO> constraints = jointConstraintExpressionTool.findByParamPrimaryKey(param.getPrimaryKey());
			if(constraints!=null && constraints.size()>0){
				throw new ParamReferencedByJointConstraintsException(param.getPrimaryKey());
			}
			
			accessPointParamDao.delete(param);
		}
		
		return null;
	}
	
}
