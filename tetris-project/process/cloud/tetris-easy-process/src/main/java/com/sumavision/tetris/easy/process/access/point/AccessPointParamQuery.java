package com.sumavision.tetris.easy.process.access.point;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class AccessPointParamQuery {

	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	/**
	 * 分页查询接入点下的参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午10:31:35
	 * @param Long accessPointId 接入点id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<AccessPointParamPO> 参数列表
	 */
	public List<AccessPointParamPO> findByAccessPointId(Long accessPointId, int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<AccessPointParamPO> params = accessPointParamDao.findByAccessPointId(accessPointId, page);
		return params.getContent();
	}
	
	/**
	 * 根据接入点和参数描述查询参数树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月20日 下午2:32:41
	 * @param Long accessPointId 接入点id
	 * @param ParamDirection direction 参数描述
	 * @return List<AccessPointParamVO> 参数树根列表
	 */
	public List<AccessPointParamVO> findTreeByAccessPointIdAndDirection(Long accessPointId, ParamDirection direction) throws Exception{
		List<AccessPointParamPO> entities = accessPointParamDao.findByAccessPointIdInAndDirection(new ArrayListWrapper<Long>().add(accessPointId).getList(), direction);
		List<AccessPointParamVO> rootParams = findRootParams(entities);
		packParamTree(rootParams, entities);
		return rootParams;
	}
	
	/**
	 * 查询根节点参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月20日 下午2:34:00
	 * @param List<AccessPointParamPO> entities 所有参数
	 * @return List<AccessPointParamVO> 参数根节点列表
	 */
	private List<AccessPointParamVO> findRootParams(List<AccessPointParamPO> entities) throws Exception{
		List<AccessPointParamVO> rootParams = new ArrayList<AccessPointParamVO>();
		if(entities!=null && entities.size()>0){
			for(AccessPointParamPO entity:entities){
				if(entity.getParentId() == null) rootParams.add(new AccessPointParamVO().set(entity));
			}
		}
		return rootParams;
	}
	
	/**
	 * 组装参数树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月20日 下午2:35:15
	 * @param List<AccessPointParamVO> rootParams 根节点参数
	 * @param List<AccessPointParamPO> entities 所有参数
	 */
	private void packParamTree(List<AccessPointParamVO> rootParams, List<AccessPointParamPO> entities) throws Exception{
		if(rootParams==null || rootParams.size()<=0) return;
		for(AccessPointParamVO rootParam:rootParams){
			for(AccessPointParamPO entity:entities){
				if(rootParam.getId().equals(entity.getParentId())){
					if(rootParam.getSub() == null) rootParam.setSub(new ArrayList<AccessPointParamVO>());
					rootParam.getSub().add(new AccessPointParamVO().set(entity));
				}
			}
			if(rootParam.getSub() != null){
				packParamTree(rootParam.getSub(), entities);
			}
		}
	}
	
	/**
	 * 查询一个参数下的所有子参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月20日 下午4:24:18
	 * @param Long id 参数id
	 * @return List<AccessPointParamPO> 所有子参数
	 */
	public List<AccessPointParamPO> findAllSubParams(Long id){
		return accessPointParamDao.findAllSubParams(new StringBufferWrapper().append("%/")
																	         .append(id)
																	         .toString(), 
													new StringBufferWrapper().append("%/")
																		     .append(id)
																		     .append("/%")
																		     .toString());
	}
	
}
