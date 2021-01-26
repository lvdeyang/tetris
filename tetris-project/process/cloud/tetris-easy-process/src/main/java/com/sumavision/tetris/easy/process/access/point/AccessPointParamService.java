package com.sumavision.tetris.easy.process.access.point;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.easy.process.access.point.exception.AccessPointNotExistException;
import com.sumavision.tetris.easy.process.access.point.exception.AccessPointParamNotExistException;
import com.sumavision.tetris.easy.process.access.point.exception.ParamReferencedByJointConstraintsException;
import com.sumavision.tetris.easy.process.access.point.exception.PrimaryKeyAlreadyExistException;

@Service
@Transactional(rollbackFor = Exception.class)
public class AccessPointParamService {

	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	@Autowired
	private AccessPointParamQuery accessPointParamQuery;
	
	@Autowired
	private JointConstraintExpressionQuery jointConstraintExpressionQuery;
	
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
	 * @param Long parentId 父级参数id
	 * @return AccessPointParamPO 接入点参数数据
	 */
	public AccessPointParamPO add(
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
			String direction,
			String constraintExpressions,
			Long parentId) throws Exception{
		
		AccessPointParamPO existParam = accessPointParamDao.findByPrimaryKey(primaryKey);
		
		if(existParam != null){
			throw new PrimaryKeyAlreadyExistException(primaryKey);
		}
		
		/*existParam = accessPointParamDao.findByAccessPointIdAndSerial(accessPointId, serial);
		
		if(existParam != null){
			throw new SerialAlreadyExistException(accessPointId, serial);
		}*/
		
		AccessPointPO accessPoint = accessPointDao.findById(accessPointId);
		
		if(accessPoint == null){
			throw new AccessPointNotExistException(accessPointId);
		}
		
		AccessPointParamPO parentParam = null;
		
		if(parentId != null){
			parentParam = accessPointParamDao.findById(parentId);
			if(parentParam == null){
				throw new AccessPointParamNotExistException(parentId);
			}
		}
		
		List<String> parsedValue = JSON.parseArray(values, String.class);
		List<String> parsedConstraint = JSON.parseArray(constraintExpressions, String.class);
		
		AccessPointParamPO param = new AccessPointParamPO();
		param.setAccessPointId(accessPointId);
		param.setPrimaryKey(primaryKey);
		param.setPrimaryKeyPath(primaryKeyPath);
		param.setReferenceKey(referenceKey);
		param.setReferenceKeyPath(referenceKeyPath);
		param.setName(name);
		param.setDefaultValue(defaultValue);
		param.setRemarks(remarks);
		param.setSerial(serial);
		param.setType(ParamType.fromName(type));
		param.setEnums(StringUtils.join(parsedValue, ","));
		param.setDirection(ParamDirection.fromName(direction));
		param.setConstraintExpression(StringUtils.join(parsedConstraint, "&&"));
		
		if(parentParam != null){
			param.setParentId(parentParam.getId());
			StringBufferWrapper parentPath = new StringBufferWrapper();
			if(parentParam.getParentPath() == null){
				parentPath.append("/").append(parentParam.getId());
			}else{
				parentPath.append(parentParam.getParentPath()).append("/").append(parentParam.getId());
			}
			param.setParentPath(parentPath.toString());
		}
		
		accessPointParamDao.save(param);
		
		return param;
	}
	
	/**
	 * 删除接入点参数并删除其所有子参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月20日 下午4:43:32
	 * @param Long id 参数id
	 */
	public void delete(Long id) throws Exception{
		
		AccessPointParamPO param = accessPointParamDao.findById(id);
		
		if(param == null) return;
		
		List<AccessPointParamPO> subParams = accessPointParamQuery.findAllSubParams(param.getId());
		
		if(param != null){
			List<JointConstraintExpressionPO> constraints = jointConstraintExpressionQuery.findByParamPrimaryKey(param.getPrimaryKey());
			if(constraints!=null && constraints.size()>0){
				throw new ParamReferencedByJointConstraintsException(param.getPrimaryKey());
			}
			accessPointParamDao.delete(param);
			
			if(subParams!=null && subParams.size()>0){
				for(AccessPointParamPO subParam:subParams){
					constraints = jointConstraintExpressionQuery.findByParamPrimaryKey(subParam.getPrimaryKey());
					if(constraints!=null && constraints.size()>0){
						throw new ParamReferencedByJointConstraintsException(subParam.getPrimaryKey());
					}
				}
				accessPointParamDao.deleteInBatch(subParams);
			}
		}	
		
	}
	
}
